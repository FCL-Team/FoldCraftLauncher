//
// Created by mio on 2024/8/20.
//

#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <dlfcn.h>
#include <assert.h>
#include <malloc.h>
#include <stdlib.h>
#include "virgl.h"
#include "environ/environ.h"
#include "ctxbridges/osm_bridge.h"
#include "egl_bridge.h"
#include "ctxbridges/egl_loader.h"

int (*vtest_main_p)(int argc, char **argv);

void (*vtest_swap_buffers_p)(void);

void *virglGetCurrentContext() {
    return (void *) OSMesaGetCurrentContext_p();
}

void virglSwapBuffers() {
    glFinish_p();
    vtest_swap_buffers_p();
}

void virglMakeCurrent(void *window) {
    printf("OSMDroid: making current\n");
    OSMesaMakeCurrent_p((OSMesaContext) window,
                        setbuffer,
                        GL_UNSIGNED_BYTE,
                        pojav_environ->savedWidth,
                        pojav_environ->savedHeight);

    printf("OSMDroid: vendor: %s\n", glGetString_p(GL_VENDOR));
    printf("OSMDroid: renderer: %s\n", glGetString_p(GL_RENDERER));
    glClear_p(GL_COLOR_BUFFER_BIT);
    glClearColor_p(0.4f, 0.4f, 0.4f, 1.0f);

    // Trigger a texture creation, which then set VIRGL_TEXTURE_ID
    int pixelsArr[4];
    glReadPixels_p(0, 0, 1, 1, GL_RGB, GL_INT, &pixelsArr);

    virglSwapBuffers();
}

void *virglCreateContext(void *contextSrc) {
    printf("OSMDroid: generating context\n");
    void *ctx = OSMesaCreateContext_p(OSMESA_RGBA, contextSrc);
    printf("OSMDroid: context=%p\n", ctx);
    return ctx;
}

void loadSymbolsVirGL() {
    dlsym_OSMesa();
    dlsym_EGL();

    char *fileName = calloc(1, 1024);

    sprintf(fileName, "%s/libvirgl_test_server.so", getenv("POJAV_NATIVEDIR"));
    void *handle = dlopen(fileName, RTLD_LAZY);
    printf("VirGL: libvirgl_test_server = %p\n", handle);
    if (!handle) {
        printf("VirGL: %s\n", dlerror());
    }
    vtest_main_p = dlsym(handle, "vtest_main");
    vtest_swap_buffers_p = dlsym(handle, "vtest_swap_buffers");

    free(fileName);
}

void *egl_make_current(void *window) {
    EGLBoolean success = eglMakeCurrent_p(
            potatoBridge.eglDisplay,
            window == 0 ? (EGLSurface *) 0 : potatoBridge.eglSurface,
            window == 0 ? (EGLSurface *) 0 : potatoBridge.eglSurface,
            /* window==0 ? EGL_NO_CONTEXT : */ (EGLContext *) window
    );

    if (success == EGL_FALSE) {
        printf("EGLBridge: Error: eglMakeCurrent() failed: %p\n", eglGetError_p());
    } else {
        printf("EGLBridge: eglMakeCurrent() succeed!\n");
    }

    printf("VirGL: vtest_main = %p\n", vtest_main_p);
    printf("VirGL: Calling VTest server's main function\n");
    vtest_main_p(3, (const char *[]) {"vtest", "--no-loop-or-fork", "--use-gles", NULL, NULL});
}

void virglSwapInterval(int interval) {
    eglSwapInterval_p(potatoBridge.eglDisplay, interval);
}

int virglInit() {
    if (potatoBridge.eglDisplay == NULL || potatoBridge.eglDisplay == EGL_NO_DISPLAY) {
        potatoBridge.eglDisplay = eglGetDisplay_p(EGL_DEFAULT_DISPLAY);
        if (potatoBridge.eglDisplay == EGL_NO_DISPLAY) {
            printf("EGLBridge: Error eglGetDefaultDisplay() failed: %p\n", eglGetError_p());
            return 0;
        }
    }

    printf("EGLBridge: Initializing\n");
    // printf("EGLBridge: ANativeWindow pointer = %p\n", pojav_environ->pojavWindow);
    //(*env)->ThrowNew(env,(*env)->FindClass(env,"java/lang/Exception"),"Trace exception");
    if (!eglInitialize_p(potatoBridge.eglDisplay, NULL, NULL)) {
        printf("EGLBridge: Error eglInitialize() failed: %s\n", eglGetError_p());
        return 0;
    }

    static const EGLint attribs[] = {
            EGL_RED_SIZE, 8,
            EGL_GREEN_SIZE, 8,
            EGL_BLUE_SIZE, 8,
            EGL_ALPHA_SIZE, 8,
            // Minecraft required on initial 24
            EGL_DEPTH_SIZE, 24,
            EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
            EGL_NONE
    };

    EGLint num_configs;
    EGLint vid;

    if (!eglChooseConfig_p(potatoBridge.eglDisplay, attribs, &config, 1, &num_configs)) {
        printf("EGLBridge: Error couldn't get an EGL visual config: %s\n", eglGetError_p());
        return 0;
    }

    assert(config);
    assert(num_configs > 0);

    if (!eglGetConfigAttrib_p(potatoBridge.eglDisplay, config, EGL_NATIVE_VISUAL_ID, &vid)) {
        printf("EGLBridge: Error eglGetConfigAttrib() failed: %s\n", eglGetError_p());
        return 0;
    }

    ANativeWindow_setBuffersGeometry(pojav_environ->pojavWindow, 0, 0, vid);

    eglBindAPI_p(EGL_OPENGL_ES_API);

    potatoBridge.eglSurface = eglCreateWindowSurface_p(potatoBridge.eglDisplay, config,
                                                       pojav_environ->pojavWindow, NULL);

    if (!potatoBridge.eglSurface) {
        printf("EGLBridge: Error eglCreateWindowSurface failed: %p\n", eglGetError_p());
        //(*env)->ThrowNew(env,(*env)->FindClass(env,"java/lang/Exception"),"Trace exception");
        return 0;
    }

    // sanity checks
    {
        EGLint val;
        assert(eglGetConfigAttrib_p(potatoBridge.eglDisplay, config, EGL_SURFACE_TYPE, &val));
        assert(val & EGL_WINDOW_BIT);
    }

    printf("EGLBridge: Initialized!\n");
    printf("EGLBridge: ThreadID=%d\n", gettid());
    printf("EGLBridge: EGLDisplay=%p, EGLSurface=%p\n",
/* window==0 ? EGL_NO_CONTEXT : */
           potatoBridge.eglDisplay,
           potatoBridge.eglSurface
    );

    // Init EGL context and vtest server
    const EGLint ctx_attribs[] = {
            EGL_CONTEXT_CLIENT_VERSION, 3,
            EGL_NONE
    };
    EGLContext *ctx = eglCreateContext_p(potatoBridge.eglDisplay, config, NULL, ctx_attribs);
    printf("VirGL: created EGL context %p\n", ctx);

    pthread_t t;
    pthread_create(&t, NULL, egl_make_current, (void *) ctx);
    usleep(100 * 1000); // need enough time for the server to init

    if (OSMesaCreateContext_p == NULL) {
        printf("OSMDroid: %s\n", dlerror());
    }
    return 0;
}