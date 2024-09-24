//
// Created by Tungsten on 2022/10/11.
//

#define _GLFW_PLATFORM_LIBRARY_TIMER_STATE _GLFWtimerPOSIX posix

#include <stdint.h>
#include <internal.h>


// POSIX-specific global timer data
//
typedef struct _GLFWtimerPOSIX
{
    GLFWbool    monotonic;
    uint64_t    frequency;

} _GLFWtimerPOSIX;


void _glfwInitTimerPOSIX(void);

