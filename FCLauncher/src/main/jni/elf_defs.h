//
// Created by maks on 11.05.2026.
//

#ifndef POJAVLAUNCHER_ELF_DEFS_H
#define POJAVLAUNCHER_ELF_DEFS_H

#include <elf.h>

#if defined(__x86_64__) || defined(__aarch64__)
#define ELF_EHDR Elf64_Ehdr
#define ELF_SHDR Elf64_Shdr
#define ELF_HALF Elf64_Half
#define ELF_XWORD Elf64_Xword
#define ELF_DYN Elf64_Dyn
#define ELF_SYM Elf64_Sym
#elif defined(__arm__) || defined(__i386__)
#define ELF_EHDR Elf32_Ehdr
#define ELF_SHDR Elf32_Shdr
#define ELF_HALF Elf32_Half
#define ELF_XWORD Elf32_Xword
#define ELF_DYN Elf32_Dyn
#define ELF_SYM Elf32_Sym
#else
#error please add architecture macro with correct bitness
#endif

#endif //POJAVLAUNCHER_ELF_DEFS_H
