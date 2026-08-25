//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H
#define FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H

_Noreturn void nominal_exit(int code);

#define FCL_INTERNAL_LOG(x...) do { \
    printf("[FCL Internal] %s:%d\n", __FILE__, __LINE__); \
    printf(x); \
    printf("\n"); \
    } while (0)

#define FCL_LOG(x...) do { \
    printf(x); \
    printf("\n"); \
    } while (0)

#endif //FOLD_CRAFT_LAUNCHER_FCL_INTERNAL_H
