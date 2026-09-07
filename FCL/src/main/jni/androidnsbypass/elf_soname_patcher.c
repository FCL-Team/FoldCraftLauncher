// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi

#include "elf_soname_patcher.h"
#include "utils.h"

// Used the following as reference
// https://github.com/bylaws/liblinkernsbypass/blob/master/elf_soname_patcher.cpp
// https://github.com/PojavLauncherTeam/PojavLauncher/blob/v3_openjdk/app_pojavlauncher/src/main/jni/driver_helper/nsbypass.c

bool patch_elf_soname(int realfd, int patchfd, uint16_t patchid) {
    struct stat realstat;
    if(fstat(realfd, &realstat)) return false;
    if(ftruncate64(patchfd, realstat.st_size) == -1) return false;
    char* target = mmap(NULL, realstat.st_size, PROT_READ | PROT_WRITE, MAP_SHARED, patchfd, 0);
    if(target == MAP_FAILED) return false;
    if(read(realfd, target, realstat.st_size) != realstat.st_size) {
        munmap(target, realstat.st_size);
        return false;
    }

    ELF_EHDR *ehdr = (ELF_EHDR*)target;
    ELF_SHDR *shdr = (ELF_SHDR*)(target + ehdr->e_shoff);

    // Iterate over section headers to find the .dynamic section
    for(ELF_HALF i = 0; i < ehdr->e_shnum; i++) {
        ELF_SHDR *hdr = &shdr[i];
        if(hdr->sh_type == SHT_DYNAMIC) {
            char* strtab = target + shdr[hdr->sh_link].sh_offset;
            ELF_DYN *dynEntries = (ELF_DYN*)(target + hdr->sh_offset);

            // Iterate over .dynamic entries to find DT_SONAME
            for(ELF_XWORD k = 0; k < (hdr->sh_size / hdr->sh_entsize);k++) {
                ELF_DYN* dynEntry = &dynEntries[k];
                if(dynEntry->d_tag == DT_SONAME) {
                    char* soname = strtab + dynEntry->d_un.d_val;
                    char sprb[4];
                    // Partially replace the old soname with the soname patch
                    snprintf(sprb, 4, "%03x", patchid);
                    memcpy(soname, sprb, 3);
                    munmap(target, realstat.st_size);
                    return true;
                }
            }
        }
    }
    return false;
}

bool patch_elf_soname_path(const char *elfPath, int patchfd, uint16_t patchid)
{
    if (elfPath == NULL) {
        errno = EINVAL;
        return false;
    }

    int realFd = open(elfPath, O_RDONLY | O_CLOEXEC);
    if (realFd == -1) {
        return false;
    }

    return patch_elf_soname(realFd, patchfd, patchid);
    close(realFd);
}
