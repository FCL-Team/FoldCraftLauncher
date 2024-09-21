//
// Created by Tungsten on 2022/10/11.
//

#include <internal.h>

#define OSMESA_RGBA 0x1908
#define OSMESA_FORMAT 0x22
#define OSMESA_DEPTH_BITS 0x30
#define OSMESA_STENCIL_BITS 0x31
#define OSMESA_ACCUM_BITS 0x32
#define OSMESA_PROFILE 0x33
#define OSMESA_CORE_PROFILE 0x34
#define OSMESA_COMPAT_PROFILE 0x35
#define OSMESA_CONTEXT_MAJOR_VERSION 0x36
#define OSMESA_CONTEXT_MINOR_VERSION 0x37

#define OSMESA_ROW_LENGTH	0x10
#define OSMESA_Y_UP		0x11

typedef void* OSMesaContext;
typedef void (*OSMESAproc)(void);

typedef OSMesaContext (GLAPIENTRY * PFN_OSMesaCreateContext)(GLenum,OSMesaContext);
typedef OSMesaContext (GLAPIENTRY * PFN_OSMesaGetCurrentContext)(void);
typedef void (GLAPIENTRY * PFN_OSMesaDestroyContext)(OSMesaContext);
typedef void (GLAPIENTRY * PFN_OSMesaPixelStore)(GLint,GLint);
typedef int (GLAPIENTRY * PFN_OSMesaMakeCurrent)(OSMesaContext,void*,int,int,int);
typedef int (GLAPIENTRY * PFN_OSMesaGetColorBuffer)(OSMesaContext,int*,int*,int*,void**);
typedef int (GLAPIENTRY * PFN_OSMesaGetDepthBuffer)(OSMesaContext,int*,int*,int*,void**);
typedef GLFWglproc (GLAPIENTRY * PFN_OSMesaGetProcAddress)(const char*);
#define OSMesaCreateContext _glfw.osmesa.CreateContext
#define OSMesaGetCurrentContext _glfw.osmesa.GetCurrentContext
#define OSMesaDestroyContext _glfw.osmesa.DestroyContext
#define OSMesaPixelStore _glfw.osmesa.PixelStore
#define OSMesaMakeCurrent _glfw.osmesa.MakeCurrent
#define OSMesaGetColorBuffer _glfw.osmesa.GetColorBuffer
#define OSMesaGetDepthBuffer _glfw.osmesa.GetDepthBuffer
#define OSMesaGetProcAddress _glfw.osmesa.GetProcAddress

#define _GLFW_OSMESA_CONTEXT_STATE              _GLFWcontextOSMesa osmesa
#define _GLFW_OSMESA_LIBRARY_CONTEXT_STATE      _GLFWlibraryOSMesa osmesa


// OSMesa-specific per-context data
//
typedef struct _GLFWcontextOSMesa
{
    OSMesaContext       handle;
    int                 width;
    int                 height;
    void*               buffer;

} _GLFWcontextOSMesa;

// OSMesa-specific global data
//
typedef struct _GLFWlibraryOSMesa
{
    void*           handle;

    PFN_OSMesaCreateContext         CreateContext;
    PFN_OSMesaGetCurrentContext     GetCurrentContext;
    PFN_OSMesaDestroyContext        DestroyContext;
    PFN_OSMesaPixelStore            PixelStore;
    PFN_OSMesaMakeCurrent           MakeCurrent;
    PFN_OSMesaGetColorBuffer        GetColorBuffer;
    PFN_OSMesaGetDepthBuffer        GetDepthBuffer;
    PFN_OSMesaGetProcAddress        GetProcAddress;

} _GLFWlibraryOSMesa;


GLFWbool _glfwInitOSMesa(void);
void _glfwTerminateOSMesa(void);
GLFWbool _glfwCreateContextOSMesa(_GLFWwindow* window,
                                  const _GLFWctxconfig* ctxconfig,
                                  const _GLFWfbconfig* fbconfig);

