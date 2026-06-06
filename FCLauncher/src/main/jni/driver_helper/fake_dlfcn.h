//
// Created by maks on 11.05.2026.
//

#ifndef POJAVLAUNCHER_FAKE_DLFCN_H
#define POJAVLAUNCHER_FAKE_DLFCN_H

int fake_dlclose(void *handle);
void *fake_dlopen(const char *libpath, int flags);
void *fake_dlsym(void *handle, const char *name);

#endif //POJAVLAUNCHER_FAKE_DLFCN_H
