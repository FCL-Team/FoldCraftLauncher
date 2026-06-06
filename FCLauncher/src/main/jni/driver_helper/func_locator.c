//
// Created by maks on 11.05.2026.
//

#include "android_namespace_func.h"
#include "fake_dlfcn.h"
#include <dlfcn.h>

#define TAG __FILE_NAME__
#include <log.h>

typedef void* (*dlsym_impl)(void* handle, const char* proc);

static bool load_symbols(dlsym_impl idlsym, void* handle, android_ldfuncs_t* funcs) {
    funcs->create_namespace = idlsym(handle, "android_create_namespace");
    funcs->link_namespaces = idlsym(handle, "android_link_namespaces");
    bool res = funcs->create_namespace && funcs->link_namespaces;
    LOGI("load_symbols res: %i", res);
    return res;
}

static void* fakel_locate_libdl_android() {
    // u mad? :trollface:
    void* linker_handle = fake_dlopen("linker64", 0);
    if(!linker_handle) {
        LOGE("fakel failed to find linker in VA space");
        return NULL;
    }
    loader_dlopen_t loader_dlopen = fake_dlsym(linker_handle, "__loader_dlopen");
    if(!loader_dlopen) {
        LOGE("fakel failed to find loader_dlopen entrypoint");
        goto fail;
    }
    void* dl_android = loader_dlopen("libdl_android.so", RTLD_NOW, &dlopen);
    if(!dl_android) {
        LOGE("fakel failed to load libdl_android: %s", dlerror());
        goto fail;
    }
    fake_dlclose(linker_handle);
    return dl_android;
    fail:
    fake_dlclose(linker_handle);
    return NULL;
}

#ifdef USE_ARM64_LOCATOR
extern void* arm64l_locate_libdl_android();
#endif

bool locate_namespace_funcs(android_ldfuncs_t* funcs) {
    void* handle = NULL;
#ifdef USE_ARM64_LOCATOR
    // Path 1: attempt to load libdl_android using the adrenotools loader hack. Pretty hacky
    // but gives us a proper reference
    handle = arm64l_locate_libdl_android();
    if(handle) {
        funcs->dl_handle = handle;
        funcs->close = dlclose;
        load_symbols(dlsym, handle, funcs);
        return true;
    }
#endif
    // Path 2: attempt to load libdl_android using fake_dlfcn to locate the internal linker
    // entrypoint. Even hackier but still gives a reliable reference
    handle = fakel_locate_libdl_android();
    if(handle) {
        funcs->dl_handle = handle;
        funcs->close = dlclose;
        load_symbols(dlsym, handle, funcs);
        return true;
    }

    handle = fake_dlopen("libdl_android.so", 0);
    if(handle) {
        funcs->dl_handle = handle;
        funcs->close = fake_dlclose;
        load_symbols(fake_dlsym, handle, funcs);
        return true;
    }
    return false;
}



