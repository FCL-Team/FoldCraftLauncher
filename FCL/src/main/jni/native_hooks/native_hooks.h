//
// Created by maks on 23.01.2025.
//

#ifndef POJAVLAUNCHER_NATIVE_HOOKS_H
#define POJAVLAUNCHER_NATIVE_HOOKS_H

#include <bytehook.h>

typedef bytehook_stub_t (*bytehook_hook_all_t)(const char *callee_path_name, const char *sym_name, void *new_func,
                                               bytehook_hooked_t hooked, void *hooked_arg);

void create_chmod_hooks(bytehook_hook_all_t bytehook_hook_all_p);

#endif //POJAVLAUNCHER_NATIVE_HOOKS_H
