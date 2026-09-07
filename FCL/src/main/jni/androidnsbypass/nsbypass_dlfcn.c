// Copyright (c) 2016 avs333
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
//		of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
//		to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//		copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
//		The above copyright notice and this permission notice shall be included in all
//		copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// 		AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

// SPDX-License-Identifier: MIT

// Derived from:
// FastHook / enhanced_dlfcn.c
// https://github.com/turing-technician/FastHook/blob/8fa01b7baec06034ee9c7f51f0345f1142b0f602/fasthook/src/main/cpp/enhanced_dlfcn.c
// Originally from https://github.com/turing-technician/Enhanced_dlfunctions/blob/a642a18bb80c78ea2d57b613382612c6ff1595ed/app/src/main/cpp/enhanced_dlfcn.c

// Modifications:
// Copyright (c) 2026 alexytomi
/* Changes:
 * - Modified to integrate into the library and renaming funcs to nsbypass as "enhanced_dlfcn"
 * isn't exactly what this does. It was used as a way to bypass the linker namespace restrictions
 * in android 7.
 * See https://source.android.com/docs/core/architecture/vndk/linker-namespace
 * Thus nsbypass would be a better name for these.
 * - Added relative file support to nsbypass_dlopen and removed looking for r-xp mapping only.
 * That addition must've been some specific case for fasthook, I can't figure out why they did that.
 * - Redirect these functions to the real implementations on Android 6 and below. This is useless
 * on below android 7 because linker namespace restrictions didn't exist and private APIs were all
 * easily accessible.
 * - Removed ctx->bias and instead assume p_offset == p_vaddr and set load_addr to start of address
 * mapping, this will break if p_offset != p_vaddr, in which case please find p_vaddr and use it
 * to subtract from load_addr instead of the p_offset obtained from /proc/self/maps. While
 * using p_offset should be better, there was likely a reason they used sh->sh_addr - sh->sh_offset
 * instead. If your usecase is broken with the new logic, simply uncomment it and remove the
 * subtraction of p_offset from load_addr
 */


// This is not actually dlfunc. It doesn't load anything.
// It bypasses normal linker restrictions by searching the memory for already loaded symbols.
// It cannot access symbols that are not already loaded.
// This way you can get your hands on function handles you otherwise can't because namespace

// Not needed on Android 6 and below (namespace restrictions don't exist there).
// See https://source.android.com/docs/core/permissions/namespaces_libraries

#include <android/log.h>
#include <dlfcn.h>
#include <elf.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>
#include <inttypes.h>

#include "fasthook/nsbypass_dlfcn.h"
#include "utils.h"

struct ctx {
	void *load_addr;
	void *dynstr;
	void *dynsym;
    size_t dynsym_num;
	void *strtab;
	void *symtab;
    size_t symtab_num;
//	off_t bias;
};

int nsbypass_dlclose(void *handle) {
	if (is_android_6_or_lower()) return dlclose(handle);
	if (handle) {
		struct ctx *ctx = (struct ctx *) handle;
		if (ctx->dynsym) free(ctx->dynsym);    /* we're saving dynsym and dynstr */
		if (ctx->dynstr) free(ctx->dynstr);    /* from library file just in case */
		if (ctx->symtab) free(ctx->symtab);
		if (ctx->strtab) free(ctx->strtab);
		free(ctx);
	}
	return 0;
}

void *nsbypass_dlopen(const char *libPath, int flags) {
	if (is_android_6_or_lower()) return dlopen(libPath, flags);
	FILE *maps;
	// Increase the buffer because we added searching for absolute file.
	// 256 might not be enough buffer to get the whole path in.
	char mapsSearchBuff[2048];
	struct ctx *ctx = 0;
	uintptr_t load_addr, size;
	int k, fd = -1, found = 0;
	void *shoff;
    ELF_EHDR *elf = (ELF_EHDR *) MAP_FAILED;

#define fatal(fmt, args...) do { LOGE(fmt,##args); goto err_exit; } while(0)

	maps = fopen("/proc/self/maps", "r");
	if (!maps) fatal("failed to open maps");

	while (!found && fgets(mapsSearchBuff, sizeof(mapsSearchBuff), maps))
		if (strstr(mapsSearchBuff, libPath)) found = 1;

	fclose(maps);

	// If user didn't prove an absolute path we have to find the actual full path ourselves
	// This is NOT the file path where the file lives in, so scan memory again.
	if (libPath[0] != '/') {
		// Looks through the current buffer which contains the libPath and the path
		// mapsSearchBuff probably looks like
		// 7a8d366000-7a8d453000 r-xp 00039000 07:60 16                             /apex/com.android.runtime/bin/linker64
		char *libPathStart = strstr(mapsSearchBuff, libPath); // at ..bin/[l]inker64
		if (libPathStart != NULL) {
			char *pathStart = libPathStart;
			// Move backward until we find the spaces area.
			while (pathStart > mapsSearchBuff &&
					pathStart[-1] != ' ' &&
					pathStart[-1] != '\t') {
				--pathStart;
			}
			// Hopefully this is the start of the path
			if (*pathStart == '/') {
				libPath = pathStart;
                // Remove \n or else it tries to open() a path with a newline which it sadly
                // isn't too happy about doing.
                char *pathEnd = strchr(libPath, '\n');
                if (pathEnd != NULL) {
                    *pathEnd = '\0';
                }
			} else {
				fatal(
						"An error happened while resolving the full path of %s; "
						"searching stopped at %s",
						libPath,
						pathStart
				);
			}
		}
	}

	if (!found) fatal("%s not found in my userspace", libPath);

	unsigned long p_offset;

	// We have no use for the permission bits because it can be mapping multiple sections of memory
	/*
	    7cd416b000-7cd41a3000 r--p 00000000 07:58 16                             /apex/com.android.runtime/bin/linker64
		7cd41a3000-7cd428b000 r-xp 00038000 07:58 16                             /apex/com.android.runtime/bin/linker64
		7cd428b000-7cd4293000 r--p 00120000 07:58 16                             /apex/com.android.runtime/bin/linker64
		7cd4293000-7cd4295000 rw-p 00127000 07:58 16                             /apex/com.android.runtime/bin/linker64
	 */

	// p_offset is the file offset, this is not necessarily the same as p_vaddr but we assume it is
	// FIXME: This is wrong. Find p_vaddr properly.
	if (sscanf(mapsSearchBuff,
			"%" SCNxPTR "-%*" SCNxPTR " %*4s %lx",
			&load_addr,
			&p_offset) != 2) {
		fatal("failed to parse maps entry for %s", libPath);
	}

	load_addr -= p_offset;

	LOGI("%s loaded in Android at 0x%" PRIxPTR, libPath, load_addr);
	/* Now, mmap the same library once again */

	fd = open(libPath, O_RDONLY);
	if (fd < 0) fatal("failed to open %s", libPath);

	size = lseek(fd, 0, SEEK_END);
	if (size <= 0) fatal("lseek() failed for %s", libPath);

	elf = (ELF_EHDR *) mmap(0, size, PROT_READ, MAP_SHARED, fd, 0);
	close(fd);
	fd = -1;

	if (elf == MAP_FAILED) fatal("mmap() failed for %s", libPath);

	ctx = (struct ctx *) calloc(1, sizeof(struct ctx));
	if (!ctx) fatal("no memory for %s", libPath);

	ctx->load_addr = (void *) load_addr;
	shoff = ((void *) elf) + elf->e_shoff;

	ELF_SHDR *shstrtab = (ELF_SHDR *)(shoff + elf->e_shstrndx * elf->e_shentsize);
	char * shstr = malloc(shstrtab->sh_size);
	memcpy(shstr, ((void *) elf) + shstrtab->sh_offset, shstrtab->sh_size);

	for (k = 0; k < elf->e_shnum; k++, shoff += elf->e_shentsize) {

        ELF_SHDR *sh = (ELF_SHDR *) shoff;
		LOGD("%s: k=%d shdr=%p type=%d", __func__, k, sh, sh->sh_type);

		switch (sh->sh_type) {

			case SHT_DYNSYM:
				if (ctx->dynsym) fatal("%s: duplicate DYNSYM sections", libPath); /* .dynsym */
				ctx->dynsym = malloc(sh->sh_size);
				if (!ctx->dynsym) fatal("%s: no memory for .dynsym", libPath);
				memcpy(ctx->dynsym, ((void *) elf) + sh->sh_offset, sh->sh_size);
				ctx->dynsym_num = (sh->sh_size / sizeof(ELF_SYM));
				break;

			case SHT_SYMTAB:
				if (ctx->symtab) fatal("%s: duplicate SYMTAB sections", libPath); /* .symtab */
				ctx->symtab = malloc(sh->sh_size);
				if (!ctx->symtab) fatal("%s: no memory for .symtab", libPath);
				memcpy(ctx->symtab, ((void *) elf) + sh->sh_offset, sh->sh_size);
				ctx->symtab_num = (sh->sh_size / sizeof(ELF_SYM));
				break;

			case SHT_STRTAB:
				if(!strcmp(shstr+sh->sh_name,".dynstr")) {
					if (ctx->dynstr) break;    /* .dynstr is guaranteed to be the first STRTAB */
					ctx->dynstr = malloc(sh->sh_size);
					if (!ctx->dynstr) fatal("%s: no memory for .dynstr", libPath);
					memcpy(ctx->dynstr, ((void *) elf) + sh->sh_offset, sh->sh_size);
				}else if(!strcmp(shstr+sh->sh_name,".strtab")) {
					if (ctx->strtab) break;
					ctx->strtab = malloc(sh->sh_size);
					if (!ctx->strtab) fatal("%s: no memory for .strtab", libPath);
					memcpy(ctx->strtab, ((void *) elf) + sh->sh_offset, sh->sh_size);
				}
				break;
		/*
		 * This is the old completely wrong way fasthook tried to find the offset.
		 * This offset was obtained here then applied in dlsym
		 * It's been replaced with p_offset from /proc/self/maps. This is still wrong.
		 */
//			case SHT_PROGBITS:
//				if (!ctx->dynstr || !ctx->dynsym || ctx->bias) break;
//				/* won't even bother checking against the section name */
//				ctx->bias = (off_t) sh->sh_addr - (off_t) sh->sh_offset;
////				k = elf->e_shnum;  /* exit for */
//				break;
		}
	}

	munmap(elf, size);
	elf = 0;

	if (!ctx->dynstr || !ctx->dynsym) fatal("dynamic sections not found in %s", libPath);

#undef fatal

	LOGD("%s: ok, dynsym = %p, dynstr = %p symtab = %p strtab = %p", libPath, ctx->dynsym, ctx->dynstr, ctx->symtab, ctx->strtab);

	return ctx;

	err_exit:
	if (fd >= 0) close(fd);
	if (elf != MAP_FAILED) munmap(elf, size);
	nsbypass_dlclose(ctx);
	return 0;
}

void *nsbypass_dlsym(void *handle, const char *name) {
	if (is_android_6_or_lower()) return dlsym(handle, name);
	int k;
	struct ctx *ctx = (struct ctx *) handle;
    ELF_SYM *dynsym = (ELF_SYM *) ctx->dynsym;
    ELF_SYM *symtab = (ELF_SYM *) ctx->symtab;
	char *dynstr = (char *) ctx->dynstr;
	char *strtab = (char *) ctx->strtab;

	for (k = 0; k < ctx->dynsym_num; k++, dynsym++) {
		if (strcmp(dynstr + dynsym->st_name, name) == 0) {
			/*  NB: sym->st_value is an offset into the section for relocatables,
            but a VMA for shared libs or exe files, so we have to subtract the bias */

			// Removed bias subtraction here, we now fetch offset from /proc/self/maps
			// and apply it to load_addr at dlopen.
			void *ret = ctx->load_addr + dynsym->st_value;
			return ret;
		}
	}

	if(symtab) {
		for (k = 0; k < ctx->symtab_num; k++, symtab++) {
			//log_info("%s found %u %s at %d", name, sym_tab->st_name,strings + sym_tab->st_name,k);
			if (strcmp(strtab + symtab->st_name, name) == 0) {
				/*  NB: sym->st_value is an offset into the section for relocatables,
                but a VMA for shared libs or exe files, so we have to subtract the bias */

				// Removed bias subtraction here, we now fetch offset from /proc/self/maps
				// and apply it to load_addr at dlopen.
				void *ret = ctx->load_addr + symtab->st_value;
				return ret;
			}
		}
	}
	return 0;
}
