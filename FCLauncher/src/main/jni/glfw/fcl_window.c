//
// Created by Tungsten on 2022/10/11.
//
//========================================================================
// This file is derived from x11_window.c
//========================================================================

#include <internal.h>

#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <errno.h>
#include <assert.h>
#include <fcl_event.h>
#include <fcl_bridge.h>

// Translates an FCL event modifier state mask
//
static int translateState(int state)
{
    int mods = 0;

    if (state & ShiftMask)
        mods |= GLFW_MOD_SHIFT;
    if (state & ControlMask)
        mods |= GLFW_MOD_CONTROL;
    if (state & Mod1Mask)
        mods |= GLFW_MOD_ALT;
    if (state & Mod4Mask)
        mods |= GLFW_MOD_SUPER;
    if (state & LockMask)
        mods |= GLFW_MOD_CAPS_LOCK;
    if (state & Mod2Mask)
        mods |= GLFW_MOD_NUM_LOCK;

    return mods;
}

// Translates an FCL key code to a GLFW key token
//
static int translateKey(int scancode)
{
    // Use the pre-filled LUT (see createKeyTables() in fcl_init.c)
    if (scancode < 0 || scancode > 255)
        return GLFW_KEY_UNKNOWN;

    return _glfw.fcl.keycodes[scancode];
}

// Apply disabled cursor mode to a focused window
//
static void disableCursor(_GLFWwindow* window)
{
    _glfw.fcl.disabledCursorWindow = window;
    _glfwPlatformGetCursorPos(window,
                              &_glfw.fcl.restoreCursorPosX,
                              &_glfw.fcl.restoreCursorPosY);
    // updateCursorImage(window);
    _glfwCenterCursorInContentArea(window);
    fclSetCursorMode(CursorDisabled);
}

// Exit disabled cursor mode for the specified window
//
static void enableCursor(_GLFWwindow* window)
{
    _glfw.fcl.disabledCursorWindow = NULL;
    fclSetCursorMode(CursorEnabled);
    _glfwPlatformSetCursorPos(window,
                              _glfw.fcl.restoreCursorPosX,
                              _glfw.fcl.restoreCursorPosY);
    // updateCursorImage(window);
}

// Get the ANativeWindow and peer infomation
//
static GLFWbool createNativeWindow(_GLFWwindow* window,
                                   const _GLFWwndconfig* wndconfig)
{
    // window width and height requirements ignored
    window->fcl.handle = fclGetNativeWindow();
    ANativeWindow_acquire(window->fcl.handle);

    if (!window->fcl.handle)
    {
        _glfwInputError(GLFW_PLATFORM_ERROR,
                       "FCL: Failed to get window");
        return GLFW_FALSE;
    }

    _glfw.fcl.eventCurrent = window;

    if (!wndconfig->decorated)
        _glfwPlatformSetWindowDecorated(window, GLFW_FALSE);

    window->fcl.maximized = GLFW_TRUE;
    _glfwPlatformSetWindowTitle(window, wndconfig->title);
    _glfwPlatformGetWindowPos(window, &window->fcl.xpos, &window->fcl.ypos);
    _glfwPlatformGetWindowSize(window, &window->fcl.width, &window->fcl.height);

    return GLFW_TRUE;
}

// Make the specified window and its video mode active on its monitor
//
static void acquireMonitor(_GLFWwindow* window)
{
    _glfwInputMonitorWindow(window->monitor, window);
}

// Remove the window
//
static void releaseMonitor(_GLFWwindow* window)
{
    if (window->monitor->window != window)
        return;

    _glfwInputMonitorWindow(window->monitor, NULL);
}

static _GLFWwindow* mWindow;
// Process the specified FCL event
//
static void processEvent(FCLEvent *event)
{
    if (mWindow == NULL) {
        mWindow = _glfw.fcl.eventCurrent;
    }
    _GLFWwindow* window = mWindow;

    switch (event->type)
    {
        case KeyPress:
        {
            const int keycode = event->keycode;
            const int keychar = event->keychar;
            const int key = translateKey(keycode);
            const int mods = translateState(event->state);
            const int plain = !(mods & (GLFW_MOD_CONTROL | GLFW_MOD_ALT));

            _glfwInputKey(window, key, keycode, GLFW_PRESS, mods);
            if (keychar) {
                _glfwInputChar(window, keychar, mods, plain);
            }
            return;
        }

        case KeyRelease:
        {
            const int keycode = event->keycode;
            const int key = translateKey(keycode);
            const int mods = translateState(event->state);

            _glfwInputKey(window, key, keycode, GLFW_RELEASE, mods);
            return;
        }

        case ButtonPress:
        {
            const int mods = translateState(event->state);

            if (event->button == Button1)
                _glfwInputMouseClick(window, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS, mods);
            else if (event->button == Button2)
                _glfwInputMouseClick(window, GLFW_MOUSE_BUTTON_MIDDLE, GLFW_PRESS, mods);
            else if (event->button == Button3)
                _glfwInputMouseClick(window, GLFW_MOUSE_BUTTON_RIGHT, GLFW_PRESS, mods);

            // Like X11, FCL provides scroll events as mouse button presses
            else if (event->button == Button4)
                _glfwInputScroll(window, 0.0, 1.0);
            else if (event->button == Button5)
                _glfwInputScroll(window, 0.0, -1.0);
            else if (event->button == Button6)
                _glfwInputScroll(window, 1.0, 0.0);
            else if (event->button == Button7)
                _glfwInputScroll(window, -1.0, 0.0);

            else
            {
                // Additional buttons after 7 are treated as regular buttons
                // We subtract 4 to fill the gap left by scroll input above
                _glfwInputMouseClick(window,
                                     event->button - Button1 - 4,
                                     GLFW_PRESS,
                                     mods);
            }

            return;
        }

        case ButtonRelease:
        {
            const int mods = translateState(event->state);

            if (event->button == Button1)
            {
                _glfwInputMouseClick(window,
                                     GLFW_MOUSE_BUTTON_LEFT,
                                     GLFW_RELEASE,
                                     mods);
            }
            else if (event->button == Button2)
            {
                _glfwInputMouseClick(window,
                                     GLFW_MOUSE_BUTTON_MIDDLE,
                                     GLFW_RELEASE,
                                     mods);
            }
            else if (event->button == Button3)
            {
                _glfwInputMouseClick(window,
                                     GLFW_MOUSE_BUTTON_RIGHT,
                                     GLFW_RELEASE,
                                     mods);
            }
            else if (event->button > Button7)
            {
                // Additional buttons after 7 are treated as regular buttons
                // We subtract 4 to fill the gap left by scroll input above
                _glfwInputMouseClick(window,
                                     event->button - Button1 - 4,
                                     GLFW_RELEASE,
                                     mods);
            }

            return;
        }

        case MotionNotify:
        {
            const int x = event->x;
            const int y = event->y;

            if (x != window->fcl.warpCursorPosX ||
                y != window->fcl.warpCursorPosY)
            {
                // The cursor was moved by something other than GLFW

                if (window->cursorMode == GLFW_CURSOR_DISABLED)
                {
                    if (_glfw.fcl.disabledCursorWindow != window)
                        return;
                    if (window->rawMouseMotion)
                        return;

                    const int dx = x - window->fcl.lastCursorPosX;
                    const int dy = y - window->fcl.lastCursorPosY;

                    _glfwInputCursorPos(window,
                                        window->virtualCursorPosX + dx,
                                        window->virtualCursorPosY + dy);
                }
                else
                    _glfwInputCursorPos(window, x, y);
            }

            window->fcl.lastCursorPosX = x;
            window->fcl.lastCursorPosY = y;
            return;
        }

        case KeyChar:
        {
            const int keychar = event->keychar;
            const int mods = translateState(event->state);
            const int plain = !(mods & (GLFW_MOD_CONTROL | GLFW_MOD_ALT));

            if (keychar) {
                _glfwInputChar(window, keychar, mods, plain);
            }
            return;
        }

        case ConfigureNotify:
        {
            const int width = event->width;
            const int height = event->height;
            if (width != window->fcl.width ||
                height != window->fcl.height)
            {
                _glfwInputFramebufferSize(window,
                                          width,
                                          height);

                _glfwInputWindowSize(window,
                                     width,
                                     height);

                window->fcl.width = width;
                window->fcl.height = height;
            }

            return;
        }

        case FCLMessage:
        {
            if (event->message == CloseRequest)
            {
                // The FCL was asked to close the window, for
                // example by the user pressing 'back' key
                _glfwInputWindowCloseRequest(window);
            }
        }
    }
}

static void handleEvents(int timeout)
{
    if (fclWaitForEvent(timeout) == 0) {
        return;
    }
    FCLEvent event;
    while (fclPollEvent(&event))
    {
        processEvent(&event);
        if (fclWaitForEvent(0) == 0) {
            break;
        }
    }
}


//////////////////////////////////////////////////////////////////////////
//////                       GLFW platform API                      //////
//////////////////////////////////////////////////////////////////////////

int _glfwPlatformCreateWindow(_GLFWwindow* window,
                              const _GLFWwndconfig* wndconfig,
                              const _GLFWctxconfig* ctxconfig,
                              const _GLFWfbconfig* fbconfig)
{
    if (!createNativeWindow(window, wndconfig))
        return GLFW_FALSE;

    if (ctxconfig->client != GLFW_NO_API)
    {
        if (ctxconfig->source == GLFW_EGL_CONTEXT_API ||
            ctxconfig->source == GLFW_NATIVE_CONTEXT_API)
        {
            if (!_glfwInitEGL())
                return GLFW_FALSE;
            if (!_glfwCreateContextEGL(window, ctxconfig, fbconfig))
                return GLFW_FALSE;
        }
        else if (ctxconfig->source == GLFW_OSMESA_CONTEXT_API)
        {
            const char *renderer = getenv("LIBGL_STRING");
            if (!strcmp(renderer, "Zink") ||
               !strcmp(renderer, "Freedreno") ||
               !strcmp(renderer, "VirGLRenderer") ||
               !strcmp(renderer, "custom_gallium"))
            {
                if (!_glfwInitEGL())
                    return GLFW_FALSE;
            }
            if (!_glfwInitOSMesa())
                return GLFW_FALSE;
            if (!_glfwCreateContextOSMesa(window, ctxconfig, fbconfig))
                return GLFW_FALSE;
        }
    }

    if (window->monitor)
    {
        _glfwPlatformShowWindow(window);
        acquireMonitor(window);
    }

    return GLFW_TRUE;
}

void _glfwPlatformDestroyWindow(_GLFWwindow* window)
{
    if (_glfw.fcl.disabledCursorWindow == window)
        _glfw.fcl.disabledCursorWindow = NULL;

    if (window->monitor)
        releaseMonitor(window);

    if (window->context.destroy)
        window->context.destroy(window);

    if (window->fcl.handle)
    {
        ANativeWindow_release(window->fcl.handle);
        window->fcl.handle = NULL;
    }
}

void _glfwPlatformSetWindowTitle(_GLFWwindow* window, const char* title)
{
}

void _glfwPlatformSetWindowIcon(_GLFWwindow* window,
                                int count, const GLFWimage* images)
{
}

void _glfwPlatformGetWindowPos(_GLFWwindow* window, int* xpos, int* ypos)
{
    if (xpos)
        *xpos = 0;
    if (ypos)
        *ypos = 0;
}

void _glfwPlatformSetWindowPos(_GLFWwindow* window, int xpos, int ypos)
{
}

void _glfwPlatformGetWindowSize(_GLFWwindow* window, int* width, int* height)
{
    if (width)
        *width = ANativeWindow_getWidth(window->fcl.handle);
    if (height)
        *height = ANativeWindow_getHeight(window->fcl.handle);
}

void _glfwPlatformSetWindowSize(_GLFWwindow* window, int width, int height)
{
    if (window->monitor)
    {
        if (window->monitor->window == window)
            acquireMonitor(window);
    }
}

void _glfwPlatformSetWindowSizeLimits(_GLFWwindow* window,
                                      int minwidth, int minheight,
                                      int maxwidth, int maxheight)
{
}

void _glfwPlatformSetWindowAspectRatio(_GLFWwindow* window, int numer, int denom)
{
}

void _glfwPlatformGetFramebufferSize(_GLFWwindow* window, int* width, int* height)
{
    _glfwPlatformGetWindowSize(window, width, height);
}

void _glfwPlatformGetWindowFrameSize(_GLFWwindow* window,
                                     int* left, int* top,
                                     int* right, int* bottom)
{
    if (left)
        *left = 0;
    if (top)
        *top = 0;
    if (right)
        *right = 0;
    if (bottom)
        *bottom = 0;
}

void _glfwPlatformGetWindowContentScale(_GLFWwindow* window,
                                        float* xscale, float* yscale)
{
    if (xscale)
        *xscale = _glfw.fcl.contentScaleX;
    if (yscale)
        *yscale = _glfw.fcl.contentScaleY;
}

void _glfwPlatformIconifyWindow(_GLFWwindow* window)
{
}

void _glfwPlatformRestoreWindow(_GLFWwindow* window)
{
}

void _glfwPlatformMaximizeWindow(_GLFWwindow* window)
{
}

void _glfwPlatformShowWindow(_GLFWwindow* window)
{
}

void _glfwPlatformHideWindow(_GLFWwindow* window)
{
}

void _glfwPlatformRequestWindowAttention(_GLFWwindow* window)
{
}

void _glfwPlatformFocusWindow(_GLFWwindow* window)
{
}

void _glfwPlatformSetWindowMonitor(_GLFWwindow* window,
                                   _GLFWmonitor* monitor,
                                   int xpos, int ypos,
                                   int width, int height,
                                   int refreshRate)
{
    // Are these codes meaningful?
    if (window->monitor == monitor)
    {
        if (monitor)
        {
            if (monitor->window == window)
                acquireMonitor(window);
        }

        return;
    }

    if (window->monitor)
    {
        _glfwPlatformSetWindowDecorated(window, window->decorated);
        _glfwPlatformSetWindowFloating(window, window->floating);
        releaseMonitor(window);
    }

    _glfwInputWindowMonitor(window, monitor);

    if (window->monitor)
    {
        acquireMonitor(window);
    }
}

int _glfwPlatformWindowFocused(_GLFWwindow* window)
{
    return GLFW_TRUE;
}

int _glfwPlatformWindowIconified(_GLFWwindow* window)
{
    return GLFW_FALSE;
}

int _glfwPlatformWindowVisible(_GLFWwindow* window)
{
    return GLFW_TRUE;
}

int _glfwPlatformWindowMaximized(_GLFWwindow* window)
{
    return GLFW_TRUE;
}

int _glfwPlatformWindowHovered(_GLFWwindow* window)
{
    return GLFW_TRUE;
}

int _glfwPlatformFramebufferTransparent(_GLFWwindow* window)
{
    return GLFW_FALSE;
}

void _glfwPlatformSetWindowResizable(_GLFWwindow* window, GLFWbool enabled)
{
}

void _glfwPlatformSetWindowDecorated(_GLFWwindow* window, GLFWbool enabled)
{
}

void _glfwPlatformSetWindowFloating(_GLFWwindow* window, GLFWbool enabled)
{
}

float _glfwPlatformGetWindowOpacity(_GLFWwindow* window)
{
    return 1.f;
}

void _glfwPlatformSetWindowOpacity(_GLFWwindow* window, float opacity)
{
}

void _glfwPlatformSetRawMouseMotion(_GLFWwindow *window, GLFWbool enabled)
{
}

GLFWbool _glfwPlatformRawMouseMotionSupported(void)
{
    return GLFW_FALSE;
}

void _glfwPlatformPollEvents(void)
{
    handleEvents(0);
}

void _glfwPlatformWaitEvents(void)
{
    handleEvents(-1);
}

void _glfwPlatformWaitEventsTimeout(double timeout)
{
    handleEvents((int) (timeout * 1e3));
}

void _glfwPlatformPostEmptyEvent(void)
{
}

void _glfwPlatformGetCursorPos(_GLFWwindow* window, double* xpos, double* ypos)
{
    int x, y;

    // fclGetCursorPos(&x, &y);
    x = 0;
    y = 0;

    if (xpos)
        *xpos = x;
    if (ypos)
        *ypos = y;
}

void _glfwPlatformSetCursorPos(_GLFWwindow* window, double x, double y)
{
    // Store the new position so it can be recognized later
    window->fcl.warpCursorPosX = (int) x;
    window->fcl.warpCursorPosY = (int) y;
    // fclSetCursorPos(x, y);
}

void _glfwPlatformSetInjectorCallback(FCLinjectorfun callback) {
    fclSetInjectorCallback(callback);
}

void _glfwPlatformSetHitResultType(int type) {
    fclSetHitResultType(type);
}

void _glfwPlatformSetCursorMode(_GLFWwindow* window, int mode)
{
    if (mode == GLFW_CURSOR_DISABLED)
    {
        if (_glfwPlatformWindowFocused(window))
            disableCursor(window);
    }
    else if (_glfw.fcl.disabledCursorWindow == window)
        enableCursor(window);
    // else
        // updateCursorImage(window);
}

const char* _glfwPlatformGetScancodeName(int scancode)
{
    if (scancode < 0 || scancode > 0xff ||
        _glfw.fcl.keycodes[scancode] == GLFW_KEY_UNKNOWN)
    {
//        _glfwInputError(GLFW_INVALID_VALUE, "Invalid scancode:%d",scancode);
        return NULL;
    }

    const int key = _glfw.fcl.keycodes[scancode];
    return _glfw.fcl.keynames[key];
}

int _glfwPlatformGetKeyScancode(int key)
{
    return _glfw.fcl.scancodes[key];
}

int _glfwPlatformCreateCursor(_GLFWcursor* cursor,
                              const GLFWimage* image,
                              int xhot, int yhot)
{
    return GLFW_TRUE;
}

int _glfwPlatformCreateStandardCursor(_GLFWcursor* cursor, int shape)
{
    return GLFW_TRUE;
}

void _glfwPlatformDestroyCursor(_GLFWcursor* cursor)
{
}

void _glfwPlatformSetCursor(_GLFWwindow* window, _GLFWcursor* cursor)
{
    if (window->cursorMode == GLFW_CURSOR_NORMAL)
    {
        // updateCursorImage(window);
    }
}

void _glfwPlatformSetClipboardString(const char* string)
{
    fclSetPrimaryClipString(string);
}

const char* _glfwPlatformGetClipboardString(void)
{
    return fclGetPrimaryClipString();
}

void _glfwPlatformGetRequiredInstanceExtensions(char** extensions)
{
    if (!_glfw.vk.KHR_surface || !_glfw.vk.KHR_android_surface)
        return;

    extensions[0] = "VK_KHR_surface";
    extensions[1] = "VK_KHR_android_surface";
}

int _glfwPlatformGetPhysicalDevicePresentationSupport(VkInstance instance,
                                                      VkPhysicalDevice device,
                                                      uint32_t queuefamily)
{
    return GLFW_FALSE;
}

VkResult _glfwPlatformCreateWindowSurface(VkInstance instance,
                                          _GLFWwindow* window,
                                          const VkAllocationCallbacks* allocator,
                                          VkSurfaceKHR* surface)
{
    VkResult err;
    VkAndroidSurfaceCreateInfoKHR sci;
    PFN_vkCreateAndroidSurfaceKHR vkCreateAndroidSurfaceKHR;

    vkCreateAndroidSurfaceKHR = (PFN_vkCreateAndroidSurfaceKHR)
        vkGetInstanceProcAddr(instance, "vkCreateAndroidSurfaceKHR");
    if (!vkCreateAndroidSurfaceKHR)
    {
        _glfwInputError(GLFW_API_UNAVAILABLE,
                        "FCL: Vulkan instance missing VK_KHR_android_surface extension");
        return VK_ERROR_EXTENSION_NOT_PRESENT;
    }

    memset(&sci, 0, sizeof(sci));
    sci.sType = VK_STRUCTURE_TYPE_ANDROID_SURFACE_CREATE_INFO_KHR;
    sci.window = window->fcl.handle;

    err = vkCreateAndroidSurfaceKHR(instance, &sci, allocator, surface);
    if (err)
    {
        _glfwInputError(GLFW_PLATFORM_ERROR,
                        "FCL: Failed to create Vulkan Android surface: %s",
                        _glfwGetVulkanResultString(err));
    }

    return err;
}


//////////////////////////////////////////////////////////////////////////
//////                        GLFW native API                       //////
//////////////////////////////////////////////////////////////////////////

GLFWAPI struct ANativeWindow* glfwGetFCLWindow(GLFWwindow* handle)
{
    _GLFWwindow* window = (_GLFWwindow*) handle;
    _GLFW_REQUIRE_INIT_OR_RETURN(NULL);
    return window->fcl.handle;
}

