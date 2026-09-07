// SPDX-License-Identifier: MIT
// Copyright (c) 2026 alexytomi
#pragma once

#ifndef NSBYPASS_NSBYPASS_DLFCN_H
#define NSBYPASS_NSBYPASS_DLFCN_H

#ifdef __cplusplus
extern "C" {
#endif

/*
 * I'm not too sure on how this all actually manages to work.
 * tldr, it's scanning /proc/self/maps for which addresses to read then manually parsing whats there
 * into something that is close to how dlFuncs function. Handles here are not compatible with
 * normal dlFuncs, please do not swap them around.
 *
 * I'm pretty sure what these do are self explanatory.
 *
 * These fall back to just using the real dlFuncs when below android 7.
 */
int nsbypass_dlclose(void *handle);
// Flags are ignored and unused, except in the case of fallback.
void *nsbypass_dlopen(const char *libPath, int flags);
void *nsbypass_dlsym(void *handle, const char *name);


#ifdef __cplusplus
}
#endif

#endif //NSBYPASS_NSBYPASS_DLFCN_H
