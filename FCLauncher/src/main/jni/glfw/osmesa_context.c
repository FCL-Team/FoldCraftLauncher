//
// Created by Tungsten on 2022/10/11.
//

#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <internal.h>
#include <android/native_window.h>
#include <android/log.h>
#include <jni.h>

int (*vtest_main) (int argc, char** argv);
void (*vtest_swap_buffers) (void);

ANativeWindow_Buffer buf;
int32_t stride;

#ifndef FCL_NSBYPASS_H
#define FCL_NSBYPASS_H

void* load_turnip_vulkan();

#endif

void* makeContextCurrentEGL(void* win) {
    _GLFWwindow* window = win;
    if (!eglMakeCurrent(_glfw.egl.display,
                        window->context.egl.handle == 0 ? (EGLSurface *) 0 : window->context.egl.surface,
                        window->context.egl.handle == 0 ? (EGLSurface *) 0 : window->context.egl.surface,
                        window->context.egl.handle))
    {
        _glfwInputError(GLFW_PLATFORM_ERROR,
                        "EGL: Failed to make context current: %04x",
                        eglGetError());
    }

    if (strcmp(getenv("LIBGL_STRING"), "VirGLRenderer") == 0) {
        vtest_main(3, (const char*[]) {"vtest", "--no-loop-or-fork", "--use-gles", NULL, NULL});
    }
}

static void makeContextCurrentOSMesa(_GLFWwindow* window)
{
    if (window)
    {
        int width, height;
        _glfwPlatformGetFramebufferSize(window, &width, &height);

        // Check to see if we need to allocate a new buffer
        if ((window->context.osmesa.buffer == NULL) ||
            (width != window->context.osmesa.width) ||
            (height != window->context.osmesa.height))
        {
            free(window->context.osmesa.buffer);

            // Allocate the new buffer (width * height * 8-bit RGBA)
            window->context.osmesa.buffer = calloc(4, (size_t) width * height);
            window->context.osmesa.width  = width;
            window->context.osmesa.height = height;
        }

        if (!OSMesaMakeCurrent(window->context.osmesa.handle,
                               window->context.osmesa.buffer,
                               GL_UNSIGNED_BYTE,
                               width, height))
        {
            _glfwInputError(GLFW_PLATFORM_ERROR,
                            "OSMesa: Failed to make context current");
            return;
        }

        if (strcmp(getenv("LIBGL_STRING"), "VirGLRenderer") != 0) {
            ANativeWindow_lock(window->fcl.handle, &buf, NULL);
            OSMesaPixelStore(OSMESA_ROW_LENGTH, buf.stride);
            stride = buf.stride;
            OSMesaPixelStore(OSMESA_Y_UP, 0);
        }

        window->context.Clear = (PFNGLCLEAR) window->context.getProcAddress("glClear");
        window->context.ClearColor = (PFNGLCLEARCOLOR) window->context.getProcAddress("glClearColor");
        window->context.ReadPixels = (PFNGLREADPIXELS) window->context.getProcAddress("glReadPixels");
        window->context.Finish = (PFNGLFINISH) window->context.getProcAddress("glFinish");
        if (!window->context.Clear || !window->context.ClearColor || !window->context.ReadPixels || !window->context.Finish) {
            _glfwInputError(GLFW_PLATFORM_ERROR, "Entry point retrieval is broken");
            return;
        }

        window->context.Clear(GL_COLOR_BUFFER_BIT);
        window->context.ClearColor(0.4f, 0.4f, 0.4f, 1.0f);

        int pixelsArr[4];
        window->context.ReadPixels(0, 0, 1, 1, GL_RGB, GL_INT, &pixelsArr);

        window->context.swapBuffers(window);
    }

    _glfwPlatformSetTls(&_glfw.contextSlot, window);
}

static GLFWglproc getProcAddressOSMesa(const char* procname)
{
    return (GLFWglproc) OSMesaGetProcAddress(procname);
}

static void destroyContextOSMesa(_GLFWwindow* window)
{
    if (window->context.osmesa.handle)
    {
        OSMesaDestroyContext(window->context.osmesa.handle);
        window->context.osmesa.handle = NULL;
    }

    if (window->context.osmesa.buffer)
    {
        free(window->context.osmesa.buffer);
        window->context.osmesa.width = 0;
        window->context.osmesa.height = 0;
    }
}

static void swapBuffersOSMesa(_GLFWwindow* window)
{
    if (strcmp(getenv("LIBGL_STRING"), "VirGLRenderer") == 0) {
        window->context.Finish();
        vtest_swap_buffers();
    } else {
        OSMesaContext context = OSMesaGetCurrentContext();
        if (context == NULL) {
            printf("OSMesa: attempted to swap buffers without context!");
            return;
        }
        OSMesaMakeCurrent(context, buf.bits, GL_UNSIGNED_BYTE, buf.width, buf.height);
        if (stride != buf.stride) OSMesaPixelStore(OSMESA_ROW_LENGTH, buf.stride);
        stride = buf.stride;
        window->context.Finish();
        ANativeWindow_unlockAndPost(window->fcl.handle);
        ANativeWindow_lock(window->fcl.handle, &buf, NULL);
    }
}

static void swapIntervalOSMesa(int interval)
{
    if (strcmp(getenv("LIBGL_STRING"), "VirGLRenderer") == 0) {
        eglSwapInterval(_glfw.egl.display, interval);
    }
}

static int extensionSupportedOSMesa(const char* extension)
{
    // OSMesa does not have extensions
    return GLFW_FALSE;
}


//////////////////////////////////////////////////////////////////////////
//////                       GLFW internal API                      //////
//////////////////////////////////////////////////////////////////////////

static void set_vulkan_ptr(void* ptr) {
    char envval[64];
    sprintf(envval, "%"PRIxPTR, (uintptr_t)ptr);
    setenv("VULKAN_PTR", envval, 1);
}

void load_vulkan() {
    if(getenv("VULKAN_DRIVER_SYSTEM") == NULL && android_get_device_api_level() >= 28) {
#ifdef ADRENO_POSSIBLE
        void* result = load_turnip_vulkan();
        if(result != NULL) {
            printf("AdrenoSupp: Loaded Turnip, loader address: %p\n", result);
            set_vulkan_ptr(result);
            return;
        }
#endif
    }
    printf("OSMDroid: loading vulkan regularly...\n");
    void* vulkan_ptr = dlopen("libvulkan.so", RTLD_LAZY | RTLD_LOCAL);
    printf("OSMDroid: loaded vulkan, ptr=%p\n", vulkan_ptr);
    set_vulkan_ptr(vulkan_ptr);
}

GLFWbool _glfwInitOSMesa(void)
{
    if (_glfw.osmesa.handle)
        return GLFW_TRUE;

    char *lib_name = getenv("LIB_MESA_NAME");
    if (!lib_name) {
        lib_name = getenv("LIBGL_NAME");
    }
    _glfw.osmesa.handle = _glfw_dlopen(lib_name);

    const char *renderer = getenv("LIBGL_STRING");
    
    if (strcmp(renderer, "Zink") == 0)
        load_vulkan();

    if (!_glfw.osmesa.handle)
    {
        _glfwInputError(GLFW_API_UNAVAILABLE, "OSMesa: Library not found");
        return GLFW_FALSE;
    }

    _glfw.osmesa.CreateContext = (PFN_OSMesaCreateContext)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaCreateContext");
    _glfw.osmesa.GetCurrentContext = (PFN_OSMesaGetCurrentContext)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaGetCurrentContext");
    _glfw.osmesa.DestroyContext = (PFN_OSMesaDestroyContext)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaDestroyContext");
    _glfw.osmesa.PixelStore = (PFN_OSMesaPixelStore)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaPixelStore");
    _glfw.osmesa.MakeCurrent = (PFN_OSMesaMakeCurrent)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaMakeCurrent");
    _glfw.osmesa.GetColorBuffer = (PFN_OSMesaGetColorBuffer)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaGetColorBuffer");
    _glfw.osmesa.GetDepthBuffer = (PFN_OSMesaGetDepthBuffer)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaGetDepthBuffer");
    _glfw.osmesa.GetProcAddress = (PFN_OSMesaGetProcAddress)
        _glfw_dlsym(_glfw.osmesa.handle, "OSMesaGetProcAddress");

    if (strcmp(renderer, "VirGLRenderer") == 0) {
        char* fileName = calloc(1, 1024);
        sprintf(fileName, "%s/libvirgl_test_server.so", getenv("FCL_NATIVEDIR"));
        void *handle = _glfw_dlopen(fileName);
        if (!handle) {
            printf("VirGL: %s\n", dlerror());
            return GLFW_FALSE;
        }
        vtest_main = _glfw_dlsym(handle, "vtest_main");
        vtest_swap_buffers = _glfw_dlsym(handle, "vtest_swap_buffers");
        free(fileName);
        if (!vtest_main || !vtest_swap_buffers) {
            _glfwInputError(GLFW_PLATFORM_ERROR, "OSMesa: Failed to load required entry points");
            _glfwTerminateOSMesa();
            return GLFW_FALSE;
        }
    }

    if (!_glfw.osmesa.CreateContext ||
        !_glfw.osmesa.GetCurrentContext ||
        !_glfw.osmesa.DestroyContext ||
        !_glfw.osmesa.PixelStore ||
        !_glfw.osmesa.MakeCurrent ||
        !_glfw.osmesa.GetColorBuffer ||
        !_glfw.osmesa.GetDepthBuffer ||
        !_glfw.osmesa.GetProcAddress)
    {
        _glfwInputError(GLFW_PLATFORM_ERROR, "OSMesa: Failed to load required entry points");
        _glfwTerminateOSMesa();
        return GLFW_FALSE;
    }

    return GLFW_TRUE;
}

void _glfwTerminateOSMesa(void)
{
    if (_glfw.osmesa.handle)
    {
        _glfw_dlclose(_glfw.osmesa.handle);
        _glfw.osmesa.handle = NULL;
    }
}

#define setAttrib(a, v) \
{ \
    assert(((size_t) index + 1) < sizeof(attribs) / sizeof(attribs[0])); \
    attribs[index++] = a; \
    attribs[index++] = v; \
}

GLFWbool _glfwCreateContextOSMesa(_GLFWwindow* window,
                                  const _GLFWctxconfig* ctxconfig,
                                  const _GLFWfbconfig* fbconfig)
{
    const char *renderer = getenv("LIBGL_STRING");
    if (strcmp(renderer, "VirGLRenderer") == 0) {
        EGLConfig config = malloc(sizeof(EGLConfig));
        EGLint egl_attributes[] = {
                EGL_BLUE_SIZE, 8,
                EGL_GREEN_SIZE, 8,
                EGL_RED_SIZE, 8,
                EGL_ALPHA_SIZE, 8,
                EGL_DEPTH_SIZE, 24,
                EGL_SURFACE_TYPE, EGL_WINDOW_BIT|0x0001,
                EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
                EGL_NONE
        };

        EGLint num_configs = 0;
        EGLint vid;

        if (eglChooseConfig(_glfw.egl.display, egl_attributes, &config, 1, &num_configs) != GLFW_TRUE) {
            _glfwInputError(GLFW_API_UNAVAILABLE, "eglChooseConfig() failed: %04x",
                            eglGetError());
            return GLFW_FALSE;
        }

        if (num_configs == 0) {
            _glfwInputError(GLFW_API_UNAVAILABLE, "%s",
                            "eglChooseConfig() found no matching config");
            return GLFW_FALSE;
        }

        if (eglGetConfigAttrib(_glfw.egl.display, config, EGL_NATIVE_VISUAL_ID, &vid) != GLFW_TRUE) {
            _glfwInputError(GLFW_API_UNAVAILABLE, "eglGetConfigAttrib() failed: %04x",
                            eglGetError());
            return GLFW_FALSE;
        }

        ANativeWindow_setBuffersGeometry(window->fcl.handle, 0, 0, vid);

        eglBindAPI(EGL_OPENGL_ES_API);

        window->context.egl.surface = eglCreateWindowSurface(_glfw.egl.display,
                                       config,
                                       _GLFW_EGL_NATIVE_WINDOW,
                                       NULL);
        if (window->context.egl.surface == EGL_NO_SURFACE)
        {
            _glfwInputError(GLFW_PLATFORM_ERROR,
                            "EGL: Failed to create window surface: %04x",
                            eglGetError());
            return GLFW_FALSE;
        }
        window->context.egl.config = config;

        {
            EGLint val;
            assert(eglGetConfigAttrib(_glfw.egl.display, config, EGL_SURFACE_TYPE, &val));
            assert(val & EGL_WINDOW_BIT);
        }

        const EGLint virgl_context_attrib[] = {
                EGL_CONTEXT_CLIENT_VERSION, 3,
                EGL_NONE
        };
        window->context.egl.handle = eglCreateContext(_glfw.egl.display, config, NULL, virgl_context_attrib);

        if (window->context.egl.handle == EGL_NO_CONTEXT)
        {
            _glfwInputError(GLFW_VERSION_UNAVAILABLE,
                            "EGL: Failed to create context: %04x",
                            eglGetError());
            return GLFW_FALSE;
        }

        pthread_t t;
        pthread_create(&t, NULL, makeContextCurrentEGL, (void*) window);
        usleep(100 * 1000);
    }

    OSMesaContext share = NULL;

    if (ctxconfig->client == GLFW_OPENGL_ES_API)
    {
        _glfwInputError(GLFW_API_UNAVAILABLE,
                        "OSMesa: OpenGL ES is not available on OSMesa");
        return GLFW_FALSE;
    }

    if (ctxconfig->share)
        share = ctxconfig->share->context.osmesa.handle;

    window->context.osmesa.handle = OSMesaCreateContext(OSMESA_RGBA, share);

    if (window->context.osmesa.handle == NULL)
    {
        _glfwInputError(GLFW_VERSION_UNAVAILABLE,
                        "OSMesa: Failed to create context");
        return GLFW_FALSE;
    }

    window->context.makeCurrent = makeContextCurrentOSMesa;
    window->context.swapBuffers = swapBuffersOSMesa;
    window->context.swapInterval = swapIntervalOSMesa;
    window->context.extensionSupported = extensionSupportedOSMesa;
    window->context.getProcAddress = getProcAddressOSMesa;
    window->context.destroy = destroyContextOSMesa;

    return GLFW_TRUE;
}

#undef setAttrib


//////////////////////////////////////////////////////////////////////////
//////                        GLFW native API                       //////
//////////////////////////////////////////////////////////////////////////

GLFWAPI int glfwGetOSMesaColorBuffer(GLFWwindow* handle, int* width,
                                     int* height, int* format, void** buffer)
{
    void* mesaBuffer;
    GLint mesaWidth, mesaHeight, mesaFormat;
    _GLFWwindow* window = (_GLFWwindow*) handle;
    assert(window != NULL);

    _GLFW_REQUIRE_INIT_OR_RETURN(GLFW_FALSE);

    if (!OSMesaGetColorBuffer(window->context.osmesa.handle,
                              &mesaWidth, &mesaHeight,
                              &mesaFormat, &mesaBuffer))
    {
        _glfwInputError(GLFW_PLATFORM_ERROR,
                        "OSMesa: Failed to retrieve color buffer");
        return GLFW_FALSE;
    }

    if (width)
        *width = mesaWidth;
    if (height)
        *height = mesaHeight;
    if (format)
        *format = mesaFormat;
    if (buffer)
        *buffer = mesaBuffer;

    return GLFW_TRUE;
}

GLFWAPI int glfwGetOSMesaDepthBuffer(GLFWwindow* handle,
                                     int* width, int* height,
                                     int* bytesPerValue,
                                     void** buffer)
{
    void* mesaBuffer;
    GLint mesaWidth, mesaHeight, mesaBytes;
    _GLFWwindow* window = (_GLFWwindow*) handle;
    assert(window != NULL);

    _GLFW_REQUIRE_INIT_OR_RETURN(GLFW_FALSE);

    if (!OSMesaGetDepthBuffer(window->context.osmesa.handle,
                              &mesaWidth, &mesaHeight,
                              &mesaBytes, &mesaBuffer))
    {
        _glfwInputError(GLFW_PLATFORM_ERROR,
                        "OSMesa: Failed to retrieve depth buffer");
        return GLFW_FALSE;
    }

    if (width)
        *width = mesaWidth;
    if (height)
        *height = mesaHeight;
    if (bytesPerValue)
        *bytesPerValue = mesaBytes;
    if (buffer)
        *buffer = mesaBuffer;

    return GLFW_TRUE;
}

GLFWAPI OSMesaContext glfwGetOSMesaContext(GLFWwindow* handle)
{
    _GLFWwindow* window = (_GLFWwindow*) handle;
    _GLFW_REQUIRE_INIT_OR_RETURN(NULL);

    if (window->context.client == GLFW_NO_API)
    {
        _glfwInputError(GLFW_NO_WINDOW_CONTEXT, NULL);
        return NULL;
    }

    return window->context.osmesa.handle;
}

JNIEXPORT jlong JNICALL
Java_org_lwjgl_vulkan_VK_getVulkanDriverHandle(JNIEnv *env, jclass thiz) {
    if (getenv("VULKAN_PTR") == NULL) load_vulkan();
    return strtoul(getenv("VULKAN_PTR"), NULL, 0x10);
}
