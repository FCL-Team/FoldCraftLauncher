// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi

#include <android/log.h>
#include <asm/unistd.h>
#include <dlfcn.h>
#include <elf.h>
#include <errno.h>
#include <fcntl.h>
#include <jni.h>
#include <linux/limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/user.h>
#include <unistd.h>

#include <androidnsbypass/nsbypass.h>
#include <androidnsbypass/nsbypass_t.h>

#include "fasthook/nsbypass_dlfcn.h"

#include "elf_soname_patcher.h"
#include "utils.h"

// Creating this needed to base off of pojav and liblinkernsbypass. These are my conclusions
// after studying those implementations

/*
 * The Pojav Implementation
 *
 * On startup, turnip, libvulkan, and a library hooking android_dlopen_ext/android_load_sphal_library
 * are loaded into "driver_namespace" which is
 * local_android_create_namespace("pojav-driver",
 *                                 /system/lib64/:<nativeLibraryDir>,
 *                                 /system/lib64/:<nativeLibraryDir>,
 *                                 ANDROID_NAMESPACE_TYPE_SHARED_ISOLATED,
 *                                 "/system/:/data/:/vendor/:/apex/", // Bugged cause some phones grab from /system_ext
 *                                 NULL);
 * Turnip is loaded and then the handle is saved, which is returned by the hook when libvulkan
 * tries requesting a android_dlopen_ext/android_load_sphal_library of vulkan.<soc_codename>.so
 * aka stock vulkan driver
 *
 * The handle for libvulkan is then saved as env var VULKAN_PTR which is patched into LWJGL3
 *
 * Behaviour is buggy when called multiple times to create a same named namespace (which it was)
 */

/*
 * The libadrenotools implementation
 *
 * Upon calling adrenotools_open_libvulkan, a namespace "hookNs" is made with a library hooking
 * android_dlopen_ext/android_load_sphal_library and then libvulkan.so is loaded.
 *
 * Upon libvulkan.so calling android_dlopen_ext/android_load_sphal_library, the hook intercepts and
 * creates a namespace "driverNs" and provided extinfo by those aforementioned dlopen methods is
 * used to get the parent namespace for namespace created for the driver.
 *
 * More hooks is loaded inside "driverNs" and then subsequently turnip.
 *
 * The hooks are more file redirects for the driver to work properly + configuration.
 *
 * This is closer to how android itself loads it. Actually, this is how xiaomi loads its hooks.
 * See libmivk.so, wherever you may find a dump of it online.
 *
 * Only difference is they split the hook implementation and actual hook functions seperately to go
 * around some bug or something, never encountered one though, probably cause I don't use the
 * plt/got to resolve android_dlopen_ext but they do.
 */

// Structs to store pointers in
// This does not include __loader_android_init_anonymous_namespace
// because its useless and about to be deleted.
// https://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:bionic/linker/linker.cpp;l=2448-2449
typedef struct {
    private_create_namespace_t create_namespace;
    private_link_namespaces_t link_namespaces;
    private_link_namespaces_all_libs_t link_namespaces_all_libs;
    private_get_exported_namespace_t get_exported_namespace;
} private_namespace_funcs;

typedef struct {
    private_dlopen_function_t dlopen;
    private_dlopen_ext_function_t dlopen_ext;
    private_dlclose_function_t dlclose;
    private_dlsym_function_t dlsym;
} private_dl_funcs;

static private_dl_funcs s_privateDlFuncs = {0};
static private_namespace_funcs s_linkerFuncs = {0};
static struct android_namespace_t* escapeNs;

// Aligns pointer to page size so mprotect properly gets the whole page
inline static void *align_ptr_to_pagesize(void *ptr) {
    return (void *)(((uintptr_t)ptr) & ~(getpagesize() - 1));
}
#if (defined __aarch64__)
#include <sys/mman.h>

// The logic of doing this stems from
// https://cs.android.com/android/platform/superproject/+/329d792f6d5e33e8a6fc5a02809c795ce17774ab:bionic/libdl/libdl.cpp;drc=a493fe415304efd19f089cbfc7d78c9db7d7263c;l=86-114
// Where the dl* functions all just call __loader variants

// This is just coincidentally convenient for ARM64 opcodes.
// The other arches don't find this approach very simple.

/* upper 6 bits of an ARM64 instruction are the instruction name */
#define OP_MS 0b11111100000000000000000000000000
/* Branch Label instruction opcode and immediate mask */
#define BL_OP 0b10010100000000000000000000000000
#define BL_IM 0b00000011111111111111111111111111

static void* find_branch_label(void* func_start) {
    long page_size = sysconf(_SC_PAGESIZE);
    // Some devices (MIUI) ship with --X mapping for executables so work around that
    if (mprotect(align_ptr_to_pagesize(func_start), page_size, PROT_READ | PROT_EXEC)){
        LOGW("Failed to set readable bit on provided func! This might fail..  %p", func_start);
    }
    uint32_t* bl_addr = func_start;
    // Search for the "branch to label" opcode
    while((*bl_addr & OP_MS) != BL_OP) {
        bl_addr++; // walk through memory until we find it or die
    }
    // Offset the address to find where the "branch to label" instrunction
    // points to.
    void* t = ((char*)bl_addr) + (*bl_addr & BL_IM) * 4;
    // Reprotecting the functions removes (BTI) protection from indirect jumps.
    // While technically out of scope of "find_branch_label", this is just
    // cleaner overall.
    if (mprotect(align_ptr_to_pagesize(t), page_size, PROT_WRITE | PROT_READ | PROT_EXEC) != 0) {
        LOGW("Failed to remove BTI protection from private API page. This might fail..  %p", t);
    }
    return t;
}
#endif

static void *resolve_symbol(void *handle, const char *name, private_dl_funcs *funcs, bool prefer_private_api)
{
    if (!handle) return NULL;
    dlerror(); // Clear any stale error.
    void *symbol = NULL;
    if (prefer_private_api && funcs->dlsym != NULL) {
        symbol = funcs->dlsym(handle, name, &dlsym);
    }
    if (!symbol) {
        symbol = dlsym(handle, name);
    }
    const char *error = dlerror();
    if (error) {
        LOGW("Failed to resolve %s: %s", name, error);
        return NULL;
    }
    return symbol;
}

static bool resolve_dl_funcs(private_dl_funcs *privateDlFuncs, void *handle, bool prefer_private_api)
{
    if (!handle) return false;
    if (!privateDlFuncs->dlopen) privateDlFuncs->dlopen = resolve_symbol(handle, "__loader_dlopen", privateDlFuncs, prefer_private_api);
    if (!privateDlFuncs->dlopen_ext) privateDlFuncs->dlopen_ext = resolve_symbol(handle, "__loader_android_dlopen_ext", privateDlFuncs, prefer_private_api);
    if (!privateDlFuncs->dlclose) privateDlFuncs->dlclose = resolve_symbol(handle, "__loader_dlclose", privateDlFuncs, prefer_private_api);
    if (!privateDlFuncs->dlsym) privateDlFuncs->dlsym = resolve_symbol(handle, "__loader_dlsym", privateDlFuncs, prefer_private_api);
    return privateDlFuncs->dlopen && privateDlFuncs->dlopen_ext && privateDlFuncs->dlclose && privateDlFuncs->dlsym;
}

// Linker namespace funcs always need private API
static bool resolve_linker_funcs(private_namespace_funcs *funcs, void *handle, private_dl_funcs *privateDlFuncs)
{
    if (!handle) return false;
    if (!funcs->create_namespace) funcs->create_namespace = resolve_symbol(handle, "__loader_android_create_namespace", privateDlFuncs, true);
    if (!funcs->link_namespaces) funcs->link_namespaces = resolve_symbol(handle, "__loader_android_link_namespaces", privateDlFuncs, true);
    if (!funcs->link_namespaces_all_libs) funcs->link_namespaces_all_libs = resolve_symbol(handle, "__loader_android_link_namespaces_all_libs", privateDlFuncs, true);
    if (!funcs->get_exported_namespace) funcs->get_exported_namespace = resolve_symbol(handle, "__loader_android_get_exported_namespace", privateDlFuncs, true);
    return funcs->create_namespace && funcs->link_namespaces && funcs->link_namespaces_all_libs && funcs->get_exported_namespace;
}

/*
 * There are four files of interest to us.
    /system/lib64/ld-android.so
    /system/lib64/libc.so
    /system/lib64/libdl.so
    /system/lib64/libdl_android.so

 * They have the private APIs we want, but uh, these aren't the real files? I think they're in
 * apex or something because readelfing them does NOT match what we can dlsym from them, private
 * or not.
 */

/**
 * @brief Fetches private API dlFunctions using various methods till we find all that we want.
 *
 *  - (aarch64 only) Using &dlopen, scan the assembly instructions until it finds the private API
 *  call and uses the pointers from there. This is likely to be libdl.so being scanned.\n
 *  - Public API dlopen & dlsym on libdl.so then libc.so\n
 *  - If private API dlopen is found try ld-android.so and libdl_android.so\n
 *  - If private API dlsym is found, try RTLD_DEFAULT\n
 *  - Scan /proc/self/maps for an r-xp instance of linker64 then scan that instance for pointers\n
 * @return Private API versions of dlFunc*
 */
private_dl_funcs get_private_dl_functions()
{
    // TODO: Verify if this works on Android 8 or lower, they have a weird thing
    // that doesn't exactly just have __loader_* laying around so am not sure about it.
    private_dl_funcs dlFuncs = {0};
    char *error;
    void* linkerHandle;
    // First attempt the normal libadrenotools method (ARM64 shenanigans)
#if (defined __aarch64__)
    LOGI("Obtaining private API dlFuncs via BTI instruction from libdl.so");
    dlFuncs.dlopen = find_branch_label(&dlopen);
    dlFuncs.dlopen_ext = find_branch_label(&android_dlopen_ext);
    dlFuncs.dlclose = find_branch_label(&dlclose);
    dlFuncs.dlsym = find_branch_label(&dlsym);
    if (dlFuncs.dlopen != NULL &&
            dlFuncs.dlopen_ext != NULL &&
            dlFuncs.dlclose != NULL &&
            dlFuncs.dlsym != NULL) {
        return dlFuncs;
    }
    LOGW("Obtaining dlFuncs via branch label instruction search failed, this is not supposed to happen on aarch64. Falling back.");
    LOGI("Obtaining missing private API dlFuncs from libdl.so..");
#else
    LOGI("Obtaining private API dlFuncs using libdl.so..");
#endif

    // I don't know why this is allowed but ok
    linkerHandle = dlopen("libdl.so", RTLD_LAZY);
    if (resolve_dl_funcs(&dlFuncs, linkerHandle, false)) return dlFuncs;

    // what the fuck
    LOGI("Obtaining private API dlFuncs using libc.so..");
    linkerHandle = dlopen("libc.so", RTLD_LAZY);
    if (resolve_dl_funcs(&dlFuncs, linkerHandle, false)) return dlFuncs;

    // If somehow only dlopen was found try ld-android as it offers dlopen??? What the fuck
    // This isn't even matching /system/lib64 anymore but whatever
    // Probably something to do with loading /apex libs instead
    // See https://cs.android.com/android/platform/superproject/+/android-latest-release:bionic/linker/linker.cpp;drc=ca1e7187c08ccba8da4b613d2b348fd7efa4e2b7;l=2176
    if (dlFuncs.dlopen != NULL) {
        LOGW("This is weird, somehow you have dlopen but are missing other dlFuncs. Obtaining missing private API funcs from ld-android.so..");
        linkerHandle = dlFuncs.dlopen("ld-android.so", RTLD_LAZY, &dlsym);
        // Yeah idk why normal dlsym works here, its insane bro
        if (resolve_dl_funcs(&dlFuncs, linkerHandle, false)) return dlFuncs;
        LOGW("Obtaining missing private API funcs from ld-android.so failed, trying libdl_android.so..");
        linkerHandle = dlFuncs.dlopen("libdl_android.so", RTLD_LAZY, &dlsym);
        // Oh lets also try the other one for good fucking measure.
        if (resolve_dl_funcs(&dlFuncs, linkerHandle, false)) return dlFuncs;
    }

    // Probably overkill, will probably never happen.
    if (dlFuncs.dlsym != NULL){
        LOGW("This is very weird, somehow you have dlsym but are missing other dlFuncs. Obtaining missing private API funcs from RTLD_DEFAULT..");
        linkerHandle = RTLD_DEFAULT;
        if (resolve_dl_funcs(&dlFuncs, linkerHandle, true)) return dlFuncs;
    }

    LOGW("Obtaining dlFuncs via dlopen/dlsym failed, Obtaining missing private API funcs by memory scanning, this is unreliable.");
    linkerHandle = nsbypass_dlopen(LINKER, 0); // NEVER dlsym this handle, SIGSEGV will murder you.
    if (!linkerHandle) return dlFuncs;
    if (!dlFuncs.dlopen) dlFuncs.dlopen = nsbypass_dlsym(linkerHandle, "__loader_dlopen");
    if (!dlFuncs.dlopen_ext) dlFuncs.dlopen_ext = nsbypass_dlsym(linkerHandle, "__loader_android_dlopen_ext");
    if (!dlFuncs.dlclose) dlFuncs.dlclose = nsbypass_dlsym(linkerHandle, "__loader_dlclose");
    if (!dlFuncs.dlsym) dlFuncs.dlsym = nsbypass_dlsym(linkerHandle, "__loader_dlsym");

    return dlFuncs;
}

/**
 * Tests the provided dl functions to see if they work.
 * This way, any SIGSEGV or other stuff hard crashes early.
 * @param dlFuncs
 * @returns False if even 1 test fails, otherwise true.
 */
bool test_dlfuncs(
        private_dl_funcs dlFuncs)
{
#ifndef ENABLE_TESTS
    return true;
#else
    bool passed = true;
    LOGI("===TESTING OBTAINED PRIVATE API DLFUNCTIONS===");
    LOGI("If we crash here, now you know why.");

    LOGI("TESTING DLOPEN ON LIBDL.SO");
    void* libdlHandle = dlFuncs.dlopen("libdl.so", RTLD_NOLOAD, &dlopen);
    if (!libdlHandle) {
        LOGE("dlopen failed to obtain libdl.so! FAIL");
        passed = false;
    }

    if (libdlHandle) {
        LOGI("TESTING DLSYM ON LIBDL.SO TO FIND dlopen");
        void *dlopenAddress = dlFuncs.dlsym(libdlHandle, "dlopen", &dlopen);

        if (!dlopenAddress) {
            LOGE("dlsym failed to find dlopen from libdl.so! FAIL");
            passed = false;
        } else {
            LOGI("dlsym successfully found dlopen at %p from libdl.so", dlopenAddress);
        }

        LOGI("TESTING DLCLOSE ON LIBDL");
        int closeResult = dlFuncs.dlclose(libdlHandle);

        if (closeResult != 0) {
            LOGE("dlclose on libc.so failed with result %d! FAIL", closeResult);
            passed = false;
        } else {
            LOGI("dlclose succeeded");
        }
    }

    LOGI("TESTING DLOPEN_EXT ON LD-ANDROID.SO AKA PRIVATE API");
    void *ldAndroidHandle = dlFuncs.dlopen_ext(
                    "ld-android.so",
                    RTLD_LAZY,
                    NULL,
                    &dlopen);

    if (!ldAndroidHandle) {
        LOGE("android_dlopen_ext failed to open ld-android.so aka private API library! FAIL");
        passed = false;
        LOGI("TESTING DLOPEN ON LD-ANDROID.SO");
        ldAndroidHandle = dlFuncs.dlopen(
                "ld-android.so",
                RTLD_LAZY,
                &dlopen);
        if (ldAndroidHandle) {
            LOGW("dlopen opened ld-android.so aka private API library..android_dlopen_ext is likely invalid.");
        } else LOGE("dlopen failed to open ld-android.so. FAIL");
    } else {
        LOGI("android_dlopen_ext successfully: %p", ldAndroidHandle);
    }

    if (ldAndroidHandle) {
        LOGI("TESTING DLSYM ON LD-ANDROID.SO TO FIND __loader_android_dlopen_ext");
        void *dlopen_ext = dlFuncs.dlsym(ldAndroidHandle, "__loader_android_dlopen_ext", &dlsym);

        if (!dlopen_ext) {
            LOGE("dlsym failed to find __loader_android_dlopen_ext from ld-android.so! FAIL");
            passed = false;
        } else {
            LOGI("dlsym successfully found __loader_android_dlopen_ext at %p from ld-android.so", dlopen_ext);
        }

        LOGI("TESTING DLCLOSE ON LD-ANDROID.SO");
        int closeResult = dlFuncs.dlclose(ldAndroidHandle);

        if (closeResult != 0) {
            LOGE("dlclose on ld-android.so from dlopen_ext failed with result %d! FAIL", closeResult);
            passed = false;
        } else {
            LOGI("dlclose succeeded");
        }
    }

    LOGI("TESTING DLOPEN_EXT ON LIBC.SO");
    void* libcHandle = dlFuncs.dlopen_ext(
            "libc.so",
            RTLD_NOLOAD,
            NULL,
            &dlopen);

    if (!libcHandle) {
        LOGW("android_dlopen_ext failed to find libc.so using RTLD_NOLOAD...");
        libcHandle = dlFuncs.dlopen_ext("libc.so", RTLD_LAZY, NULL, &dlopen);
        if (!libcHandle) {
            LOGE("android_dlopen_ext failed to obtain libc.so! FAIL");
            passed = false;
        }
        LOGW("android_dlopen_ext successfully loaded a new libc.so at %p.. wait what? Are you even on android?", libcHandle);
    } else {
        LOGI("android_dlopen_ext successfully found libc.so at %p", libcHandle);
    }

    if (libcHandle) {
        LOGI("TESTING DLSYM");
        void *mallocAddress = dlFuncs.dlsym(libcHandle, "malloc", &dlsym);

        if (!mallocAddress) {
            LOGE("dlsym failed to find malloc from libc.so! FAIL");
            passed = false;
        } else {
            LOGI("dlsym successfully found malloc at %p from libc.so", mallocAddress);
        }

        LOGI("TESTING DLCLOSE");
        int closeResult = dlFuncs.dlclose(libcHandle);

        if (closeResult != 0) {
            LOGE("dlclose on libc.so from dlopen_ext failed with result %d! FAIL", closeResult);
            passed = false;
        } else {
            LOGI("dlclose succeeded");
        }
    }

    LOGI("=== FINISHED TESTING DL FUNCTIONS ===");
    return passed;
#endif
}

/**
 * Uses private API dlopen and dlsym to bypass namespace restrictions on loading ld-android.so.
 * @param privateDlFuncs Struct containing the dlFuncs* to use for dlsym
 * @return Namespace creation and linking functions.
 */
private_namespace_funcs get_private_namespace_functions(
        private_dl_funcs privateDlFuncs)
{
    private_namespace_funcs linkerFuncs = {0};
    void* linkerHandle;
    // ld-android.so and linker64 provide the same SONAME in readelf.
    // ld-android.so is not present in /proc/self/maps so it cannot be found by the memory scanning
    // Can't use linker64 for dlsym, it'll sigsegv
    linkerHandle = privateDlFuncs.dlopen("ld-android.so", RTLD_LAZY, &dlsym);
    if (linkerHandle) { // Check if it found a handle
        LOGI("Obtaining linker namespace funcs from ld-android.so...");
        // Note: liblinkernsbypass uses ld-android.so for link* and libdl_android.so for create and
        // export, pojav copies this.
        // Gonna continue with the current setup unless something breaks.
        if (resolve_linker_funcs(&linkerFuncs, linkerHandle, &privateDlFuncs)) return linkerFuncs;
    }

    /*
     * Note: We can fall back to libc.so, libdl.so, and libdl_android.so using normal dlopen and
     * escaped dlsym (using &dlsym for caller_addr) but that seems overkill, the dlFuncs already
     * did the overkill part, this should just work.
     *
     * ld-android.so also shares linker SONAME, making it not very likely to fail.
     */

    LOGW("Unable to load all namespace functions! dlFunction loading probably failed? Obtaining missing functions using memory scanning.");
    linkerHandle = nsbypass_dlopen(LINKER, 0);
    if (!linkerFuncs.create_namespace) linkerFuncs.create_namespace = nsbypass_dlsym(linkerHandle, "__loader_android_create_namespace");
    if (!linkerFuncs.link_namespaces) linkerFuncs.link_namespaces = nsbypass_dlsym(linkerHandle, "__loader_android_link_namespaces");
    if (!linkerFuncs.link_namespaces_all_libs) linkerFuncs.link_namespaces_all_libs = nsbypass_dlsym(linkerHandle, "__loader_android_link_namespaces_all_libs");
    if (!linkerFuncs.get_exported_namespace) linkerFuncs.get_exported_namespace = nsbypass_dlsym(linkerHandle, "__loader_android_get_exported_namespace");

    return linkerFuncs;
}

/**
 * Tests the provided namespace functions to see if they work
 * This way, any SIGSEGV or other stuff hard crashes early.
 * Leaks memory.
 * @returns False if even 1 test fails, otherwise true.
 */
bool test_namespace_funcs(private_namespace_funcs nsFuncs)
{
#ifndef ENABLE_TESTS
    return true;
#else
    bool passed = true;
    LOGI("===TESTING OBTAINED PRIVATE API NAMESPACE===");
    LOGI("If we crash here, now you know why.");

    LOGI("Fetching \"default\" exported namespace");
    if (nsFuncs.get_exported_namespace("default")){
        LOGI("android_get_exported_namespace successfully found default namespace handle");
    } else {
        LOGE("android_get_exported_namespace failed to find default namespace handle");
        passed = false;
    }

    LOGI("Attempting to create escape namespace");
    escapeNs = nsFuncs.create_namespace(
            "g_default_namespace_copy",
            NULL,
            SYSTEM_LIBS_PATH,
            ANDROID_NAMESPACE_TYPE_SHARED,
            SYSTEM_LIBS_PATH,
            NULL,
            &dlopen); // This address is libdl.so, which should resolve to g_default_namespace
    if (escapeNs) {
        LOGI("android_create_namespace successfully made escapeNs");
    } else {
        LOGE("android_create_namespace failed to create namespace escapeNs, testing cannot continue. FAIL");
        return false;
    }
    // This is a memory leak, but its only once and for the process lifetime. So might as well make
    // it useful if it's gonna be leaking.
    // AFAIK there is no way to get rid of a namespace sadly. If you know how, make an issue.
    struct android_namespace_t *testNs = nsFuncs.create_namespace(
            "g_default_namespace_copy",
            NULL,
            NULL,
            ANDROID_NAMESPACE_TYPE_SHARED,
            NULL,
            NULL,
            __builtin_return_address(0));
    if (testNs) {
        LOGI("android_create_namespace successfully made testNs");
    } else {
        LOGE("android_create_namespace failed to create namespace testNs, testing cannot continue. FAIL");
        return false;
    }

    if (nsFuncs.link_namespaces_all_libs(testNs, escapeNs)){
        LOGI("android_link_namespaces_all_libs successfully linked testNs to escapeNs, thereby escaping our testNs!");
        if (nsFuncs.link_namespaces(testNs, NULL, "ld-android.so")){
            LOGI("android_link_namespaces successfully loaded ld-android.so into testNs, thereby loading a private API lib!");
        } else {
            LOGE("android_link_namespaces failed to load ld-android.so into testNs, escape was a lie. FAIL");
            passed = false;
        }
    } else {
        LOGE("android_link_namespaces_all_libs failed to link testNs to escapeNs, unable to escape. FAIL");
        if (nsFuncs.link_namespaces(testNs, NULL, "libc.so")){
            LOGI("android_link_namespaces successfully loaded libc.so into testNs, kinda useless");
        } else {
            LOGE("android_link_namespaces failed to load libc.so into testNs. FAIL");
            passed = false;
        }
    }
    return passed;
#endif
}

/**
 * Resolves all the global externs at load time, so they should always be available.
 * Fails hard if any of them are not.
 */
__attribute__((constructor)) static void resolve_global_symbols()
{
    if (is_android_6_or_lower()){
        LOGW("This library is not supposed to be used on sdk23 and lower. All APIs will remain "
             "non-functional. nsbypass_dlfcn will redirect to the real public API.");
        return;
    }
    // NOTE: Tests are fast enough for release actually, but I'll disable them anyway.
    s_privateDlFuncs = get_private_dl_functions();
    test_dlfuncs(s_privateDlFuncs);
    s_linkerFuncs = get_private_namespace_functions(s_privateDlFuncs);
    test_namespace_funcs(s_linkerFuncs);

    if (!s_linkerFuncs.create_namespace ||
            !s_linkerFuncs.link_namespaces ||
            !s_linkerFuncs.link_namespaces_all_libs ||
            !s_linkerFuncs.get_exported_namespace) {
        LOGE("Failed to resolve Android linker namespace functions! Cannot run nsbypass.");
        exit(121);
    }
    // Create if not yet made, which is the case if tests are disabled
    if (!escapeNs){
        escapeNs = s_linkerFuncs.create_namespace(
                "g_default_namespace_copy",
                NULL,
                SYSTEM_LIBS_PATH,
                ANDROID_NAMESPACE_TYPE_SHARED,
                SYSTEM_LIBS_PATH,
                NULL,
                &dlopen);
        if (!escapeNs) {
            LOGD("Failed to create escapeNs!");
            exit(122); // idk it felt like a 120
        }
    }
}

struct android_namespace_t* get_escape_namespace() {
    return escapeNs;
}

// dlopen in a specific namespace
void* linker_ns_dlopen(
        const char* name,
        int flag,
        struct android_namespace_t* ns)
{
    if (is_android_6_or_lower()) return NULL;
    if (!ns) return NULL;
    android_dlextinfo dlextinfo = {0};
    dlextinfo.flags = ANDROID_DLEXT_USE_NAMESPACE;
    dlextinfo.library_namespace = ns;
    return android_dlopen_ext(name, flag, &dlextinfo);
}

// To dlopen the same lib safely, we modify their SONAME.
// If you are sure that you have two separate namespaces then you don't need to do this, a normal
// linker_ns_dlopen will work, linker will keep two identical SONAMEs seperate so long as they
// aren't linked/accessible to each other before at load time, just don't trip on search paths.

// This technically has a chance of collision but that only happens after 4096 and even then would
// be rare. So this is probably fiiiine. If you wanna fix it, fix patch_elf_soname being limited
// to overwriting instead of prepending.
static uint16_t patch_id;
void* linker_ns_dlopen_unique(
        const char* libPath,
        const char* patchedLibDir,
        int flags,
        struct android_namespace_t* ns)
{
    if (is_android_6_or_lower()) return NULL;
    if (!ns || !libPath || !patchedLibDir) return NULL;
    char pathbuf[PATH_MAX];
    int patch_fd;

    // Deviate from liblinkernsbypass because the pojav method of having lib name be here
    // is likely to help in debugging
    char* libName = strrchr(libPath, '/'); libName++;
    snprintf(pathbuf, PATH_MAX ,"%s/%d%s_patched.so", patchedLibDir, patch_id, libName);
    patch_fd = open(pathbuf, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
    if(patch_fd == -1) return NULL;

    if(!patch_elf_soname_path(libPath, patch_fd, patch_id)) {
        return NULL;
    }

    patch_id++;
    android_dlextinfo extinfo = {0};
    extinfo.flags = ANDROID_DLEXT_USE_NAMESPACE | ANDROID_DLEXT_USE_LIBRARY_FD;
    extinfo.library_fd = patch_fd;
    extinfo.library_namespace = ns;
    snprintf(pathbuf, PATH_MAX, "/proc/self/fd/%d", patch_fd);
    return android_dlopen_ext(pathbuf, flags, &extinfo);
}

// Here in case we want to add any other behaviour. Pointers alone are janky.

struct android_namespace_t* private_create_namespace(
        const char* name,
        const char* ld_library_path,
        const char* default_library_path,
        uint64_t type,
        const char* permitted_when_isolated_path,
        struct android_namespace_t* parent_namespace,
        const void* caller_addr)
{
    if (is_android_6_or_lower()) return NULL;
    return s_linkerFuncs.create_namespace(
            name,
            ld_library_path,
            default_library_path,
            type,
            permitted_when_isolated_path,
            parent_namespace,
            caller_addr);
}

bool private_link_namespaces(
        struct android_namespace_t* from,
        struct android_namespace_t* to,
        const char* shared_libs_sonames)
{
    if (is_android_6_or_lower()) return false;
    return s_linkerFuncs.link_namespaces(
            from,
            to,
            shared_libs_sonames);
}

bool private_link_namespaces_all_libs(
        struct android_namespace_t* from,
        struct android_namespace_t* to)
{
    if (is_android_6_or_lower()) return false;
    return s_linkerFuncs.link_namespaces_all_libs(
            from,
            to);
}

struct android_namespace_t* private_get_exported_namespace(
        const char* name)
{
    if (is_android_6_or_lower()) return NULL;
    return s_linkerFuncs.get_exported_namespace(
            name);
}

int private_dlclose(void* handle)
{
    if (is_android_6_or_lower()) return -1;
    return s_privateDlFuncs.dlclose(handle);
}

void* private_dlopen(
        const char* filename,
        int flags,
        const void* caller_addr)
{
    if (is_android_6_or_lower()) return NULL;
    return s_privateDlFuncs.dlopen(
            filename,
            flags,
            caller_addr);
}

void* private_dlopen_ext(
        const char* filename,
        int flags,
        const android_dlextinfo* extinfo,
        const void* caller_addr)
{
    if (is_android_6_or_lower()) return NULL;
    return s_privateDlFuncs.dlopen_ext(
            filename,
            flags,
            extinfo,
            caller_addr);
}

void* private_dlsym(
        void* handle,
        const char* symbol,
        const void* caller_addr)
{
    if (is_android_6_or_lower()) return NULL;
    return s_privateDlFuncs.dlsym(
            handle,
            symbol,
            caller_addr);
}



