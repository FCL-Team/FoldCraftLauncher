//
// Created by maks on 23.01.2025.
//

#include "native_hooks.h"
#include <unistd.h>
#include <errno.h>
#include <stdio.h>


// Hooks for chmod and fchmod that always return success.
// This allows older Android versions to work with Java NIO zipfs inside of the Pojav folder.
typedef int (*chmod_func)(const char*, mode_t);
typedef int (*fchmod_func)(int, mode_t);

#define TEMPLATE_HOOK(X, Y, Z, W) static int X(Y, mode_t mode) { \
    int result = BYTEHOOK_CALL_PREV(X, Z, W, mode); \
    if(result != 0) errno = 0; \
    BYTEHOOK_POP_STACK(); \
    return 0; \
} \

TEMPLATE_HOOK(custom_chmod, const char* filename, chmod_func, filename)
TEMPLATE_HOOK(custom_fchmod, int fd, fchmod_func, fd)

#undef TEMPLATE_HOOK

void create_chmod_hooks(bytehook_hook_all_t bytehook_hook_all_p) {
    bytehook_stub_t stub_chmod = bytehook_hook_all_p(NULL, "chmod", &custom_chmod, NULL, NULL);
    bytehook_stub_t stub_fchmod = bytehook_hook_all_p(NULL, "fchmod", &custom_fchmod, NULL, NULL);
    printf("Successfully initialized chmod hooks, stubs: %p %p", stub_chmod, stub_fchmod);
}