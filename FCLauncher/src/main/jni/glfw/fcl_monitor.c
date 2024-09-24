//
// Created by Tungsten on 2022/10/11.
//
//========================================================================
// This file is derived from x11_monitor.c
//========================================================================

#include <internal.h>

#include <stdlib.h>
#include <string.h>
#include <fcl_bridge.h>


static void modeChangeHandle(int width, int height, void* data)
{
    GLFWvidmode mode;
    _GLFWmonitor* monitor = data;

    mode.width = width;
    mode.height = height;
    mode.redBits = 8;
    mode.greenBits = 8;
    mode.blueBits = 8;
    mode.refreshRate = 0;

    monitor->modeCount++;
    monitor->modes =
        realloc(monitor->modes, monitor->modeCount * sizeof(GLFWvidmode));
    monitor->modes[monitor->modeCount - 1] = mode;

    monitor->fcl.currentMode = monitor->modeCount - 1;
}


//////////////////////////////////////////////////////////////////////////
//////                       GLFW internal API                      //////
//////////////////////////////////////////////////////////////////////////

void _glfwPollMonitorsFCL(void)
{
    struct ANativeWindow* window = fclGetNativeWindow();
    const float dpi = 141.f;
    _GLFWmonitor* monitor = _glfwAllocMonitor("FCL Monitor 0",
                                              (int) (ANativeWindow_getWidth(window) * 25.4f / dpi),
                                              (int) (ANativeWindow_getHeight(window) * 25.4f / dpi));
    _glfwInputMonitor(monitor, GLFW_CONNECTED, _GLFW_INSERT_FIRST);
}


//////////////////////////////////////////////////////////////////////////
//////                       GLFW platform API                      //////
//////////////////////////////////////////////////////////////////////////

void _glfwPlatformFreeMonitor(_GLFWmonitor* monitor)
{
}

void _glfwPlatformGetMonitorPos(_GLFWmonitor* monitor, int* xpos, int* ypos)
{
    if (xpos)
        *xpos = 0;
    if (ypos)
        *ypos = 0;
}

void _glfwPlatformGetMonitorContentScale(_GLFWmonitor* monitor,
                                         float* xscale, float* yscale)
{
    if (xscale)
        *xscale = _glfw.fcl.contentScaleX;
    if (yscale)
        *yscale = _glfw.fcl.contentScaleY;
}

void _glfwPlatformGetMonitorWorkarea(_GLFWmonitor* monitor, int* xpos, int* ypos, int* width, int* height)
{
    if (xpos)
        *xpos = 0;
    if (ypos)
        *ypos = 0;
    if (width)
        *width = monitor->modes[monitor->fcl.currentMode].width;
    if (height)
        *height = monitor->modes[monitor->fcl.currentMode].height;
}

GLFWvidmode* _glfwPlatformGetVideoModes(_GLFWmonitor* monitor, int* count)
{
    if (monitor->modes == NULL || monitor->modeCount == 0) {
        struct ANativeWindow* window = fclGetNativeWindow();
        modeChangeHandle(ANativeWindow_getWidth(window),
                         ANativeWindow_getHeight(window),
                         monitor);
    }
    *count = monitor->modeCount;
    return monitor->modes;
}

void _glfwPlatformGetVideoMode(_GLFWmonitor* monitor, GLFWvidmode* mode)
{
    if (monitor->modes == NULL || monitor->modeCount == 0) {
        struct ANativeWindow* window = fclGetNativeWindow();
        modeChangeHandle(ANativeWindow_getWidth(window),
                         ANativeWindow_getHeight(window),
                         monitor);
    }
    *mode = monitor->modes[monitor->fcl.currentMode];
}

GLFWbool _glfwPlatformGetGammaRamp(_GLFWmonitor* monitor, GLFWgammaramp* ramp)
{
    _glfwInputError(GLFW_PLATFORM_ERROR,
                    "FCL: Gamma ramp access not supported");
    return GLFW_FALSE;
}

void _glfwPlatformSetGammaRamp(_GLFWmonitor* monitor, const GLFWgammaramp* ramp)
{
    _glfwInputError(GLFW_PLATFORM_ERROR,
                    "FCL: Gamma ramp access not supported");
}

