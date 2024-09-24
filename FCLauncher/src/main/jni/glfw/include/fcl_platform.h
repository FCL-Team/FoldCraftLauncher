//
// Created by Tungsten on 2022/10/11.
//
//========================================================================
// This file is derived from x11_platform.h
//========================================================================

#include <unistd.h>
#include <signal.h>
#include <stdint.h>
#include <dlfcn.h>

#include <internal.h>

typedef VkFlags VkAndroidSurfaceCreateFlagsKHR;

typedef struct VkAndroidSurfaceCreateInfoKHR
{
    VkStructureType                   sType;
    const void*                       pNext;
    VkAndroidSurfaceCreateFlagsKHR    flags;
    struct ANativeWindow*             window;
} VkAndroidSurfaceCreateInfoKHR;

typedef VkResult (APIENTRY *PFN_vkCreateAndroidSurfaceKHR)(VkInstance, const VkAndroidSurfaceCreateInfoKHR*, const VkAllocationCallbacks*, VkSurfaceKHR*);

#include "posix_thread.h"
#include "posix_time.h"
#include "egl_context.h"
#include "osmesa_context.h"

#define _glfw_dlopen(name) dlopen(name, RTLD_LAZY | RTLD_GLOBAL)
#define _glfw_dlclose(handle) dlclose(handle)
#define _glfw_dlsym(handle, name) dlsym(handle, name)

#define _GLFW_EGL_NATIVE_WINDOW  ((EGLNativeWindowType) window->fcl.handle)
#define _GLFW_EGL_NATIVE_DISPLAY EGL_DEFAULT_DISPLAY

#define _GLFW_PLATFORM_WINDOW_STATE         _GLFWwindowFCL  fcl
#define _GLFW_PLATFORM_LIBRARY_WINDOW_STATE _GLFWlibraryFCL fcl
#define _GLFW_PLATFORM_MONITOR_STATE        _GLFWmonitorFCL fcl
#define _GLFW_PLATFORM_CURSOR_STATE         _GLFWcursorFCL  fcl

#define _GLFW_PLATFORM_CONTEXT_STATE         struct { int dummyContext; }
#define _GLFW_PLATFORM_LIBRARY_CONTEXT_STATE struct { int dummyLibraryContext; }

// FCL-specific per-window data
//
typedef struct _GLFWwindowFCL
{
    struct ANativeWindow*  handle;

    GLFWbool        overrideRedirect;
    GLFWbool        iconified;
    GLFWbool        maximized;

    // Whether the visual supports framebuffer transparency
    GLFWbool        transparent;

    // Cached position and size used to filter out duplicate events
    int             width, height;
    int             xpos, ypos;

    // The last received cursor position, regardless of source
    int             lastCursorPosX, lastCursorPosY;
    // The last position the cursor was warped to by GLFW
    int             warpCursorPosX, warpCursorPosY;

} _GLFWwindowFCL;

// FCL-specific global data
//
typedef struct _GLFWlibraryFCL
{
    // System content scale
    float           contentScaleX, contentScaleY;
    // Key name string
    char            keynames[GLFW_KEY_LAST + 1][5];
    // FCL keycode to GLFW key LUT
    short int       keycodes[256];
    // GLFW key to FCL keycode LUT
    short int       scancodes[GLFW_KEY_LAST + 1];
    // Where to place the cursor when re-enabled
    double          restoreCursorPosX, restoreCursorPosY;
    // The window whose disabled cursor mode is active
    _GLFWwindow*    disabledCursorWindow;
    // The window receiving and processing events
    _GLFWwindow*    eventCurrent;

} _GLFWlibraryFCL;

// FCL-specific per-monitor data
//
typedef struct _GLFWmonitorFCL
{
    // Current monitor mode index
    int             currentMode;

} _GLFWmonitorFCL;

// FCL-specific per-cursor data
//
typedef struct _GLFWcursorFCL
{
    // Useless struct filler
    void* handle;

} _GLFWcursorFCL;


void _glfwPollMonitorsFCL(void);

