/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

#include <stddef.h>
#include <inttypes.h>

#define DISABLE_WARNINGS() \
    _Pragma("GCC diagnostic push")
    _Pragma("GCC diagnostic ignored \"-Wunused-parameter\"")
    _Pragma("GCC diagnostic ignored \"-Wunused-variable\"")
    _Pragma("GCC diagnostic ignored \"-Wsign-compare\"")

#define ENABLE_WARNINGS() \
    _Pragma("GCC diagnostic pop")

// JNIEXPORT_CRITICAL & CRITICAL are used as a workaround for JDK-8167409 on applicable functions.
#define JNIEXPORT_CRITICAL static
#define CRITICAL(function) _JavaCritical_##function
