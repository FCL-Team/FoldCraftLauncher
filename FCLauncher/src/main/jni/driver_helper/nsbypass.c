//
// Created by maks on 05.06.2023.
//
#include "nsbypass.h"
#include "android_namespace_func.h"
#include <dlfcn.h>
#include <android/dlext.h>
#include <android/log.h>
#include <sys/mman.h>
#include <sys/user.h>
#include <string.h>
#include <stdio.h>
#include <linux/limits.h>
#include <errno.h>
#include <unistd.h>
#include <asm/unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <elf.h>
#include <elf_defs.h>
#include <inttypes.h>

#define TAG __FILE_NAME__
#include <log.h>

/* Library search path */
#define SEARCH_PATH "/system/lib64"

static struct android_namespace_t* driver_namespace = NULL;

bool linker_ns_load(const char* lib_search_path) {
    if(driver_namespace != NULL) return true; // Do not initialize namespaces multiple times, this caused very funny bugs we spent hours debugging
    android_ldfuncs_t ldfuncs;
    if(!locate_namespace_funcs(&ldfuncs)) {
        return false;
    }

    // assemble the full path search path
    char full_path[strlen(SEARCH_PATH) + strlen(lib_search_path) + 2 + 1];
    sprintf(full_path, "%s:%s", SEARCH_PATH, lib_search_path);
    driver_namespace = ldfuncs.create_namespace("pojav-driver",
                                                      full_path,
                                                      full_path,
                                                      3 /* TYPE_SHAFED | TYPE_ISOLATED */,
                                                      "/system/:/data/:/vendor/:/apex/", NULL);
    ldfuncs.link_namespaces(driver_namespace, NULL, "ld-android.so");
    ldfuncs.link_namespaces(driver_namespace, NULL, "libnativeloader.so");
    ldfuncs.link_namespaces(driver_namespace, NULL, "libnativeloader_lazy.so");
    ldfuncs.close(ldfuncs.dl_handle);
    return true;
}

void* linker_ns_dlopen(const char* name, int flag) {
    android_dlextinfo dlextinfo;
    dlextinfo.flags = ANDROID_DLEXT_USE_NAMESPACE;
    dlextinfo.library_namespace = driver_namespace;
    return android_dlopen_ext(name, flag, &dlextinfo);
}

bool patch_elf_soname(int patchfd, int realfd, size_t size, const char* patchname) {
    char* target = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, patchfd, 0);
    if(!target) return false;
    if(read(realfd, target, size) != size) goto fail;


    ELF_EHDR *ehdr = (ELF_EHDR*)target;
    ELF_SHDR *shdr = (ELF_SHDR*)(target + ehdr->e_shoff);
    for(ELF_HALF i = 0; i < ehdr->e_shnum; i++) {
        ELF_SHDR *hdr = &shdr[i];
        if(hdr->sh_type == SHT_DYNAMIC) {
            char* strtab = target + shdr[hdr->sh_link].sh_offset;
            // If there's a warning below, it's bogus, ignore it
            ELF_DYN *dynEntries = (ELF_DYN*)(target + hdr->sh_offset);
            for(ELF_XWORD k = 0; k < (hdr->sh_size / hdr->sh_entsize);k++) {
                ELF_DYN* dynEntry = &dynEntries[k];
                if(dynEntry->d_tag == DT_SONAME) {
                    char* soname = strtab + dynEntry->d_un.d_val;
                    size_t soname_len = strlen(soname);
                    size_t patchname_len = strlen(patchname);
                    if(patchname_len != soname_len) goto fail;

                    strcpy(soname, patchname);
                    munmap(target, size);
                    return true;
                }
            }
        }
    }

    fail:
    munmap(target, size);
    return false;
}

#define PAGE_ALIGN(addr)        (((addr)+pagesize-1)&(~(pagesize-1)))

void* linker_ns_dlopen_unique(const char* tmpdir, const char* name, const char* patch_name, int flags) {
    int pagesize = getpagesize();
    char pathbuf[PATH_MAX];
    static uint16_t patchid;
    int patch_fd, real_fd;
    size_t fsize, totalsize;

    snprintf(pathbuf, PATH_MAX, "%s/%s", SEARCH_PATH, name);
    real_fd = open(pathbuf, O_RDONLY);
    if(real_fd == -1) return NULL;

    {
        struct stat64 real_stat;
        if (fstat64(real_fd, &real_stat)) goto fail_real;
        fsize = real_stat.st_size;
        totalsize = PAGE_ALIGN(fsize);
    }

    patch_fd = (int) syscall(__NR_memfd_create, patch_name, MFD_CLOEXEC);
    if(patch_fd == -1) {
        // TODO: use ASharedMemory as fallback
        // NOTE: use page-aligned size (totalsize) for ashmem
        snprintf(pathbuf, PATH_MAX, "%s/%"PRIu16"", tmpdir, patchid++);
        patch_fd = open(pathbuf, O_CREAT | O_RDWR, S_IRUSR | S_IWUSR);
    }
    if(patch_fd == -1) goto fail_real;

    if(ftruncate64(patch_fd, totalsize) == -1) goto fail_both;

    bool patch_result = patch_elf_soname(patch_fd, real_fd, fsize, patch_name);
    close(real_fd);
    if(!patch_result) {
        close(patch_fd);
        return NULL;
    }

    android_dlextinfo extinfo;
    extinfo.flags = ANDROID_DLEXT_USE_NAMESPACE | ANDROID_DLEXT_USE_LIBRARY_FD;
    extinfo.library_fd = patch_fd;
    extinfo.library_namespace = driver_namespace;
    return android_dlopen_ext(patch_name, flags, &extinfo);

    fail_both:
    close(patch_fd);
    fail_real:
    close(real_fd);
    return NULL;
}
