//
// Created by maks on 11.05.2026.
//

#include <stdint.h>
#include <sys/mman.h>
#include <unistd.h>
#include <dlfcn.h>
#include <errno.h>
#include "android_namespace_func.h"

#define TAG __FILE_NAME__
#include <log.h>

/* upper 6 bits of an ARM64 instruction are the instruction name */
#define OP_MS 0b11111100000000000000000000000000
/* Branch Label instruction opcode and immediate mask */
#define BL_OP 0b10010100000000000000000000000000
#define BL_IM 0b00000011111111111111111111111111

#define PAGE_ROUND_DOWN(X) (void*)(((uintptr_t)X) & ~(pagesize-1))

// Find the first "branch to label" function in the function provided in func_start
static void* find_branch_label(int pagesize, void* func_start) {
    // round down the pointer to get the start of the function's page
    void* func_page_start = PAGE_ROUND_DOWN(func_start);
    // remap to r-x to bypass "execute only" protections on MIUI
    mprotect(func_page_start, pagesize, PROT_READ | PROT_EXEC);
    uint32_t* bl_addr = func_start;
    // search for the "branch to label" opcode
    while((*bl_addr & OP_MS) != BL_OP) {
        bl_addr++; // walk through memory until we find it or die
    }
    // offset the address to find where the "branch to label" instrunction
    // points to.
    return ((char*)bl_addr) + (*bl_addr & BL_IM) * 4;
}

void* arm64l_locate_libdl_android() {
    int pagesize = getpagesize();
    loader_dlopen_t loader_dlopen = find_branch_label(pagesize, &dlopen);
    if(loader_dlopen == NULL) return NULL;
    LOGI("loader_dlopen: %p", loader_dlopen);
    void* loader_dlopen_page_start = PAGE_ROUND_DOWN(loader_dlopen);
    // reprotecting the functions removes protection from indirect jumps
    if(mprotect(loader_dlopen_page_start, pagesize, PROT_READ | PROT_EXEC) != 0) {
        LOGE("failed to strip indirect jump prot: %i", errno);
        return NULL;
    }
    void *dl_android_handle = loader_dlopen("libdl_android.so", RTLD_NOW, &dlopen);
    if(!dl_android_handle) LOGE("failed: %s", dlerror());
    return dl_android_handle;
}