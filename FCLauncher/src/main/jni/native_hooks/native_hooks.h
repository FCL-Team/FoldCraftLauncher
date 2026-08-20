//
// FCL native hooks：SDL3 集成相关 hook
//

#ifndef FCL_NATIVE_HOOKS_H
#define FCL_NATIVE_HOOKS_H

#include <bytehook.h>

typedef bytehook_stub_t (*bytehook_hook_all_t)(const char *callee_path_name, const char *sym_name, void *new_func,
                                               bytehook_hooked_t hooked, void *hooked_arg);

void create_sdl_hooks(bytehook_hook_all_t bytehook_hook_all_p);

#endif //FCL_NATIVE_HOOKS_H
