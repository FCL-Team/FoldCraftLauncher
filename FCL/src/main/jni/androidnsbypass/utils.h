// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi

#include <elf.h>

// Arch specific helpers

#if defined(__aarch64__) || defined(__x86_64__)
#define BITNESS 64
#define SEARCH_PATH "/system/lib64"
// Do not use the full path, let it dynamically find the path
// The /system/bin/linker64 file is NOT what we want.
#define LINKER "linker64"

#define ELF_EHDR Elf64_Ehdr
#define ELF_SHDR Elf64_Shdr
#define ELF_HALF Elf64_Half
#define ELF_XWORD Elf64_Xword
#define ELF_DYN Elf64_Dyn
#define ELF_SYM Elf64_Sym
#elif defined(__arm__) || defined(__i386__)
#define BITNESS 32
#define SEARCH_PATH "/system/lib"
#define LINKER "linker"

#define ELF_EHDR Elf32_Ehdr
#define ELF_SHDR Elf32_Shdr
#define ELF_HALF Elf32_Half
#define ELF_XWORD Elf32_Xword
#define ELF_DYN Elf32_Dyn
#define ELF_SYM Elf32_Sym
#else
// Why are you in RISCV?? /j
#error "Unsupported or unknown CPU architecture"
#endif

// Logging
#include <android/log.h>

#ifndef TAG
#define TAG "androidnsbypass"
#endif

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__)

// General functions that am annoyed NDK doesn't provide

#ifdef __cplusplus
extern "C" {
#endif

int is_android_6_or_lower(void);

#ifdef __cplusplus
}
#endif
