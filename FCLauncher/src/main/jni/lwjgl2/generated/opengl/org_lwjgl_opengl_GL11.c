/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glAccumPROC) (GLenum op, GLfloat value);
typedef void (APIENTRY *glAlphaFuncPROC) (GLenum func, GLfloat ref);
typedef void (APIENTRY *glClearColorPROC) (GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha);
typedef void (APIENTRY *glClearAccumPROC) (GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha);
typedef void (APIENTRY *glClearPROC) (GLbitfield mask);
typedef void (APIENTRY *glCallListsPROC) (GLsizei n, GLenum type, const GLvoid * lists);
typedef void (APIENTRY *glCallListPROC) (GLuint list);
typedef void (APIENTRY *glBlendFuncPROC) (GLenum sfactor, GLenum dfactor);
typedef void (APIENTRY *glBitmapPROC) (GLsizei width, GLsizei height, GLfloat xorig, GLfloat yorig, GLfloat xmove, GLfloat ymove, const GLubyte * bitmap);
typedef void (APIENTRY *glBindTexturePROC) (GLenum target, GLuint texture);
typedef void (APIENTRY *glPrioritizeTexturesPROC) (GLsizei n, const GLuint * textures, const GLfloat * priorities);
typedef GLboolean (APIENTRY *glAreTexturesResidentPROC) (GLsizei n, const GLuint * textures, GLboolean * residences);
typedef void (APIENTRY *glBeginPROC) (GLenum mode);
typedef void (APIENTRY *glEndPROC) ();
typedef void (APIENTRY *glArrayElementPROC) (GLint i);
typedef void (APIENTRY *glClearDepthPROC) (GLdouble depth);
typedef void (APIENTRY *glDeleteListsPROC) (GLuint list, GLsizei range);
typedef void (APIENTRY *glDeleteTexturesPROC) (GLsizei n, const GLuint * textures);
typedef void (APIENTRY *glCullFacePROC) (GLenum mode);
typedef void (APIENTRY *glCopyTexSubImage2DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glCopyTexSubImage1DPROC) (GLenum target, GLint level, GLint xoffset, GLint x, GLint y, GLsizei width);
typedef void (APIENTRY *glCopyTexImage2DPROC) (GLenum target, GLint level, GLint internalFormat, GLint x, GLint y, GLsizei width, GLsizei height, GLint border);
typedef void (APIENTRY *glCopyTexImage1DPROC) (GLenum target, GLint level, GLint internalFormat, GLint x, GLint y, GLsizei width, GLint border);
typedef void (APIENTRY *glCopyPixelsPROC) (GLint x, GLint y, GLint width, GLint height, GLint type);
typedef void (APIENTRY *glColorPointerPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glColorMaterialPROC) (GLenum face, GLenum mode);
typedef void (APIENTRY *glColorMaskPROC) (GLboolean red, GLboolean green, GLboolean blue, GLboolean alpha);
typedef void (APIENTRY *glColor3bPROC) (GLbyte red, GLbyte green, GLbyte blue);
typedef void (APIENTRY *glColor3fPROC) (GLfloat red, GLfloat green, GLfloat blue);
typedef void (APIENTRY *glColor3dPROC) (GLdouble red, GLdouble green, GLdouble blue);
typedef void (APIENTRY *glColor3ubPROC) (GLubyte red, GLubyte green, GLubyte blue);
typedef void (APIENTRY *glColor4bPROC) (GLbyte red, GLbyte green, GLbyte blue, GLbyte alpha);
typedef void (APIENTRY *glColor4fPROC) (GLfloat red, GLfloat green, GLfloat blue, GLfloat alpha);
typedef void (APIENTRY *glColor4dPROC) (GLdouble red, GLdouble green, GLdouble blue, GLdouble alpha);
typedef void (APIENTRY *glColor4ubPROC) (GLubyte red, GLubyte green, GLubyte blue, GLubyte alpha);
typedef void (APIENTRY *glClipPlanePROC) (GLenum plane, const GLdouble * equation);
typedef void (APIENTRY *glClearStencilPROC) (GLint s);
typedef void (APIENTRY *glEvalPoint1PROC) (GLint i);
typedef void (APIENTRY *glEvalPoint2PROC) (GLint i, GLint j);
typedef void (APIENTRY *glEvalMesh1PROC) (GLenum mode, GLint i1, GLint i2);
typedef void (APIENTRY *glEvalMesh2PROC) (GLenum mode, GLint i1, GLint i2, GLint j1, GLint j2);
typedef void (APIENTRY *glEvalCoord1fPROC) (GLfloat u);
typedef void (APIENTRY *glEvalCoord1dPROC) (GLdouble u);
typedef void (APIENTRY *glEvalCoord2fPROC) (GLfloat u, GLfloat v);
typedef void (APIENTRY *glEvalCoord2dPROC) (GLdouble u, GLdouble v);
typedef void (APIENTRY *glEnableClientStatePROC) (GLenum cap);
typedef void (APIENTRY *glDisableClientStatePROC) (GLenum cap);
typedef void (APIENTRY *glEnablePROC) (GLenum cap);
typedef void (APIENTRY *glDisablePROC) (GLenum cap);
typedef void (APIENTRY *glEdgeFlagPointerPROC) (GLint stride, const GLvoid * pointer);
typedef void (APIENTRY *glEdgeFlagPROC) (GLboolean flag);
typedef void (APIENTRY *glDrawPixelsPROC) (GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glDrawElementsPROC) (GLenum mode, GLsizei count, GLenum type, const GLvoid * indices);
typedef void (APIENTRY *glDrawBufferPROC) (GLenum mode);
typedef void (APIENTRY *glDrawArraysPROC) (GLenum mode, GLint first, GLsizei count);
typedef void (APIENTRY *glDepthRangePROC) (GLdouble zNear, GLdouble zFar);
typedef void (APIENTRY *glDepthMaskPROC) (GLboolean flag);
typedef void (APIENTRY *glDepthFuncPROC) (GLenum func);
typedef void (APIENTRY *glFeedbackBufferPROC) (GLsizei size, GLenum type, GLfloat * buffer);
typedef void (APIENTRY *glGetPixelMapfvPROC) (GLenum map, GLfloat * values);
typedef void (APIENTRY *glGetPixelMapuivPROC) (GLenum map, GLuint * values);
typedef void (APIENTRY *glGetPixelMapusvPROC) (GLenum map, GLushort * values);
typedef void (APIENTRY *glGetMaterialfvPROC) (GLenum face, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetMaterialivPROC) (GLenum face, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetMapfvPROC) (GLenum target, GLenum query, GLfloat * v);
typedef void (APIENTRY *glGetMapdvPROC) (GLenum target, GLenum query, GLdouble * v);
typedef void (APIENTRY *glGetMapivPROC) (GLenum target, GLenum query, GLint * v);
typedef void (APIENTRY *glGetLightfvPROC) (GLenum light, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetLightivPROC) (GLenum light, GLenum pname, GLint * params);
typedef GLint (APIENTRY *glGetErrorPROC) ();
typedef void (APIENTRY *glGetClipPlanePROC) (GLenum plane, GLdouble * equation);
typedef void (APIENTRY *glGetBooleanvPROC) (GLenum pname, GLboolean * params);
typedef void (APIENTRY *glGetDoublevPROC) (GLenum pname, GLdouble * params);
typedef void (APIENTRY *glGetFloatvPROC) (GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetIntegervPROC) (GLenum pname, GLint * params);
typedef void (APIENTRY *glGenTexturesPROC) (GLsizei n, GLuint * textures);
typedef GLuint (APIENTRY *glGenListsPROC) (GLsizei range);
typedef void (APIENTRY *glFrustumPROC) (GLdouble left, GLdouble right, GLdouble bottom, GLdouble top, GLdouble zNear, GLdouble zFar);
typedef void (APIENTRY *glFrontFacePROC) (GLenum mode);
typedef void (APIENTRY *glFogfPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glFogiPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glFogfvPROC) (GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glFogivPROC) (GLenum pname, const GLint * params);
typedef void (APIENTRY *glFlushPROC) ();
typedef void (APIENTRY *glFinishPROC) ();
typedef void (APIENTRY *glGetPointervPROC) (GLenum pname, GLvoid ** result);
typedef GLboolean (APIENTRY *glIsEnabledPROC) (GLenum cap);
typedef void (APIENTRY *glInterleavedArraysPROC) (GLenum format, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glInitNamesPROC) ();
typedef void (APIENTRY *glHintPROC) (GLenum target, GLenum mode);
typedef void (APIENTRY *glGetTexParameterfvPROC) (GLenum target, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTexParameterivPROC) (GLenum target, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexLevelParameterfvPROC) (GLenum target, GLint level, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTexLevelParameterivPROC) (GLenum target, GLint level, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexImagePROC) (GLenum target, GLint level, GLenum format, GLenum type, GLvoid * pixels);
typedef void (APIENTRY *glGetTexGenivPROC) (GLenum coord, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexGenfvPROC) (GLenum coord, GLenum pname, GLfloat * params);
typedef void (APIENTRY *glGetTexGendvPROC) (GLenum coord, GLenum pname, GLdouble * params);
typedef void (APIENTRY *glGetTexEnvivPROC) (GLenum coord, GLenum pname, GLint * params);
typedef void (APIENTRY *glGetTexEnvfvPROC) (GLenum coord, GLenum pname, GLfloat * params);
typedef const GLubyte * (APIENTRY *glGetStringPROC) (GLint name);
typedef void (APIENTRY *glGetPolygonStipplePROC) (GLubyte * mask);
typedef GLboolean (APIENTRY *glIsListPROC) (GLuint list);
typedef void (APIENTRY *glMaterialfPROC) (GLenum face, GLenum pname, GLfloat param);
typedef void (APIENTRY *glMaterialiPROC) (GLenum face, GLenum pname, GLint param);
typedef void (APIENTRY *glMaterialfvPROC) (GLenum face, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glMaterialivPROC) (GLenum face, GLenum pname, const GLint * params);
typedef void (APIENTRY *glMapGrid1fPROC) (GLint un, GLfloat u1, GLfloat u2);
typedef void (APIENTRY *glMapGrid1dPROC) (GLint un, GLdouble u1, GLdouble u2);
typedef void (APIENTRY *glMapGrid2fPROC) (GLint un, GLfloat u1, GLfloat u2, GLint vn, GLfloat v1, GLfloat v2);
typedef void (APIENTRY *glMapGrid2dPROC) (GLint un, GLdouble u1, GLdouble u2, GLint vn, GLdouble v1, GLdouble v2);
typedef void (APIENTRY *glMap2fPROC) (GLenum target, GLfloat u1, GLfloat u2, GLint ustride, GLint uorder, GLfloat v1, GLfloat v2, GLint vstride, GLint vorder, const GLfloat * points);
typedef void (APIENTRY *glMap2dPROC) (GLenum target, GLdouble u1, GLdouble u2, GLint ustride, GLint uorder, GLdouble v1, GLdouble v2, GLint vstride, GLint vorder, const GLdouble * points);
typedef void (APIENTRY *glMap1fPROC) (GLenum target, GLfloat u1, GLfloat u2, GLint stride, GLint order, const GLfloat * points);
typedef void (APIENTRY *glMap1dPROC) (GLenum target, GLdouble u1, GLdouble u2, GLint stride, GLint order, const GLdouble * points);
typedef void (APIENTRY *glLogicOpPROC) (GLenum opcode);
typedef void (APIENTRY *glLoadNamePROC) (GLuint name);
typedef void (APIENTRY *glLoadMatrixfPROC) (const GLfloat * m);
typedef void (APIENTRY *glLoadMatrixdPROC) (const GLdouble * m);
typedef void (APIENTRY *glLoadIdentityPROC) ();
typedef void (APIENTRY *glListBasePROC) (GLuint base);
typedef void (APIENTRY *glLineWidthPROC) (GLfloat width);
typedef void (APIENTRY *glLineStipplePROC) (GLint factor, GLushort pattern);
typedef void (APIENTRY *glLightModelfPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glLightModeliPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glLightModelfvPROC) (GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glLightModelivPROC) (GLenum pname, const GLint * params);
typedef void (APIENTRY *glLightfPROC) (GLenum light, GLenum pname, GLfloat param);
typedef void (APIENTRY *glLightiPROC) (GLenum light, GLenum pname, GLint param);
typedef void (APIENTRY *glLightfvPROC) (GLenum light, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glLightivPROC) (GLenum light, GLenum pname, const GLint * params);
typedef GLboolean (APIENTRY *glIsTexturePROC) (GLuint texture);
typedef void (APIENTRY *glMatrixModePROC) (GLenum mode);
typedef void (APIENTRY *glPolygonStipplePROC) (const GLubyte * mask);
typedef void (APIENTRY *glPolygonOffsetPROC) (GLfloat factor, GLfloat units);
typedef void (APIENTRY *glPolygonModePROC) (GLenum face, GLenum mode);
typedef void (APIENTRY *glPointSizePROC) (GLfloat size);
typedef void (APIENTRY *glPixelZoomPROC) (GLfloat xfactor, GLfloat yfactor);
typedef void (APIENTRY *glPixelTransferfPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPixelTransferiPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glPixelStorefPROC) (GLenum pname, GLfloat param);
typedef void (APIENTRY *glPixelStoreiPROC) (GLenum pname, GLint param);
typedef void (APIENTRY *glPixelMapfvPROC) (GLenum map, GLsizei mapsize, const GLfloat * values);
typedef void (APIENTRY *glPixelMapuivPROC) (GLenum map, GLsizei mapsize, const GLuint * values);
typedef void (APIENTRY *glPixelMapusvPROC) (GLenum map, GLsizei mapsize, const GLushort * values);
typedef void (APIENTRY *glPassThroughPROC) (GLfloat token);
typedef void (APIENTRY *glOrthoPROC) (GLdouble left, GLdouble right, GLdouble bottom, GLdouble top, GLdouble zNear, GLdouble zFar);
typedef void (APIENTRY *glNormalPointerPROC) (GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glNormal3bPROC) (GLbyte nx, GLbyte ny, GLbyte nz);
typedef void (APIENTRY *glNormal3fPROC) (GLfloat nx, GLfloat ny, GLfloat nz);
typedef void (APIENTRY *glNormal3dPROC) (GLdouble nx, GLdouble ny, GLdouble nz);
typedef void (APIENTRY *glNormal3iPROC) (GLint nx, GLint ny, GLint nz);
typedef void (APIENTRY *glNewListPROC) (GLuint list, GLenum mode);
typedef void (APIENTRY *glEndListPROC) ();
typedef void (APIENTRY *glMultMatrixfPROC) (const GLfloat * m);
typedef void (APIENTRY *glMultMatrixdPROC) (const GLdouble * m);
typedef void (APIENTRY *glShadeModelPROC) (GLenum mode);
typedef void (APIENTRY *glSelectBufferPROC) (GLsizei size, GLuint * buffer);
typedef void (APIENTRY *glScissorPROC) (GLint x, GLint y, GLsizei width, GLsizei height);
typedef void (APIENTRY *glScalefPROC) (GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glScaledPROC) (GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glRotatefPROC) (GLfloat angle, GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glRotatedPROC) (GLdouble angle, GLdouble x, GLdouble y, GLdouble z);
typedef GLint (APIENTRY *glRenderModePROC) (GLenum mode);
typedef void (APIENTRY *glRectfPROC) (GLfloat x1, GLfloat y1, GLfloat x2, GLfloat y2);
typedef void (APIENTRY *glRectdPROC) (GLdouble x1, GLdouble y1, GLdouble x2, GLdouble y2);
typedef void (APIENTRY *glRectiPROC) (GLint x1, GLint y1, GLint x2, GLint y2);
typedef void (APIENTRY *glReadPixelsPROC) (GLint x, GLint y, GLsizei width, GLsizei height, GLenum format, GLenum type, GLvoid * pixels);
typedef void (APIENTRY *glReadBufferPROC) (GLenum mode);
typedef void (APIENTRY *glRasterPos2fPROC) (GLfloat x, GLfloat y);
typedef void (APIENTRY *glRasterPos2dPROC) (GLdouble x, GLdouble y);
typedef void (APIENTRY *glRasterPos2iPROC) (GLint x, GLint y);
typedef void (APIENTRY *glRasterPos3fPROC) (GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glRasterPos3dPROC) (GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glRasterPos3iPROC) (GLint x, GLint y, GLint z);
typedef void (APIENTRY *glRasterPos4fPROC) (GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glRasterPos4dPROC) (GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glRasterPos4iPROC) (GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glPushNamePROC) (GLuint name);
typedef void (APIENTRY *glPopNamePROC) ();
typedef void (APIENTRY *glPushMatrixPROC) ();
typedef void (APIENTRY *glPopMatrixPROC) ();
typedef void (APIENTRY *glPushClientAttribPROC) (GLbitfield mask);
typedef void (APIENTRY *glPopClientAttribPROC) ();
typedef void (APIENTRY *glPushAttribPROC) (GLbitfield mask);
typedef void (APIENTRY *glPopAttribPROC) ();
typedef void (APIENTRY *glStencilFuncPROC) (GLenum func, GLint ref, GLuint mask);
typedef void (APIENTRY *glVertexPointerPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glVertex2fPROC) (GLfloat x, GLfloat y);
typedef void (APIENTRY *glVertex2dPROC) (GLdouble x, GLdouble y);
typedef void (APIENTRY *glVertex2iPROC) (GLint x, GLint y);
typedef void (APIENTRY *glVertex3fPROC) (GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glVertex3dPROC) (GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glVertex3iPROC) (GLint x, GLint y, GLint z);
typedef void (APIENTRY *glVertex4fPROC) (GLfloat x, GLfloat y, GLfloat z, GLfloat w);
typedef void (APIENTRY *glVertex4dPROC) (GLdouble x, GLdouble y, GLdouble z, GLdouble w);
typedef void (APIENTRY *glVertex4iPROC) (GLint x, GLint y, GLint z, GLint w);
typedef void (APIENTRY *glTranslatefPROC) (GLfloat x, GLfloat y, GLfloat z);
typedef void (APIENTRY *glTranslatedPROC) (GLdouble x, GLdouble y, GLdouble z);
typedef void (APIENTRY *glTexImage1DPROC) (GLenum target, GLint level, GLint internalformat, GLsizei width, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTexImage2DPROC) (GLenum target, GLint level, GLint internalformat, GLint width, GLint height, GLint border, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTexSubImage1DPROC) (GLenum target, GLint level, GLint xoffset, GLsizei width, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTexSubImage2DPROC) (GLenum target, GLint level, GLint xoffset, GLint yoffset, GLsizei width, GLsizei height, GLenum format, GLenum type, const GLvoid * pixels);
typedef void (APIENTRY *glTexParameterfPROC) (GLenum target, GLenum pname, GLfloat param);
typedef void (APIENTRY *glTexParameteriPROC) (GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glTexParameterfvPROC) (GLenum target, GLenum pname, const GLfloat * param);
typedef void (APIENTRY *glTexParameterivPROC) (GLenum target, GLenum pname, const GLint * param);
typedef void (APIENTRY *glTexGenfPROC) (GLenum coord, GLenum pname, GLfloat param);
typedef void (APIENTRY *glTexGendPROC) (GLenum coord, GLenum pname, GLdouble param);
typedef void (APIENTRY *glTexGenfvPROC) (GLenum coord, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glTexGendvPROC) (GLenum coord, GLenum pname, const GLdouble * params);
typedef void (APIENTRY *glTexGeniPROC) (GLenum coord, GLenum pname, GLint param);
typedef void (APIENTRY *glTexGenivPROC) (GLenum coord, GLenum pname, const GLint * params);
typedef void (APIENTRY *glTexEnvfPROC) (GLenum target, GLenum pname, GLfloat param);
typedef void (APIENTRY *glTexEnviPROC) (GLenum target, GLenum pname, GLint param);
typedef void (APIENTRY *glTexEnvfvPROC) (GLenum target, GLenum pname, const GLfloat * params);
typedef void (APIENTRY *glTexEnvivPROC) (GLenum target, GLenum pname, const GLint * params);
typedef void (APIENTRY *glTexCoordPointerPROC) (GLint size, GLenum type, GLsizei stride, const GLvoid * pointer);
typedef void (APIENTRY *glTexCoord1fPROC) (GLfloat s);
typedef void (APIENTRY *glTexCoord1dPROC) (GLdouble s);
typedef void (APIENTRY *glTexCoord2fPROC) (GLfloat s, GLfloat t);
typedef void (APIENTRY *glTexCoord2dPROC) (GLdouble s, GLdouble t);
typedef void (APIENTRY *glTexCoord3fPROC) (GLfloat s, GLfloat t, GLfloat r);
typedef void (APIENTRY *glTexCoord3dPROC) (GLdouble s, GLdouble t, GLdouble r);
typedef void (APIENTRY *glTexCoord4fPROC) (GLfloat s, GLfloat t, GLfloat r, GLfloat q);
typedef void (APIENTRY *glTexCoord4dPROC) (GLdouble s, GLdouble t, GLdouble r, GLdouble q);
typedef void (APIENTRY *glStencilOpPROC) (GLenum fail, GLenum zfail, GLenum zpass);
typedef void (APIENTRY *glStencilMaskPROC) (GLuint mask);
typedef void (APIENTRY *glViewportPROC) (GLint x, GLint y, GLsizei width, GLsizei height);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglAccum(JNIEnv *env, jclass clazz, jint op, jfloat value, jlong function_pointer) {
	glAccumPROC glAccum = (glAccumPROC)((intptr_t)function_pointer);
	glAccum(op, value);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglAlphaFunc(JNIEnv *env, jclass clazz, jint func, jfloat ref, jlong function_pointer) {
	glAlphaFuncPROC glAlphaFunc = (glAlphaFuncPROC)((intptr_t)function_pointer);
	glAlphaFunc(func, ref);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClearColor(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jfloat alpha, jlong function_pointer) {
	glClearColorPROC glClearColor = (glClearColorPROC)((intptr_t)function_pointer);
	glClearColor(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClearAccum(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jfloat alpha, jlong function_pointer) {
	glClearAccumPROC glClearAccum = (glClearAccumPROC)((intptr_t)function_pointer);
	glClearAccum(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClear(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glClearPROC glClear = (glClearPROC)((intptr_t)function_pointer);
	glClear(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCallLists(JNIEnv *env, jclass clazz, jint n, jint type, jlong lists, jlong function_pointer) {
	const GLvoid *lists_address = (const GLvoid *)(intptr_t)lists;
	glCallListsPROC glCallLists = (glCallListsPROC)((intptr_t)function_pointer);
	glCallLists(n, type, lists_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCallList(JNIEnv *env, jclass clazz, jint list, jlong function_pointer) {
	glCallListPROC glCallList = (glCallListPROC)((intptr_t)function_pointer);
	glCallList(list);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglBlendFunc(JNIEnv *env, jclass clazz, jint sfactor, jint dfactor, jlong function_pointer) {
	glBlendFuncPROC glBlendFunc = (glBlendFuncPROC)((intptr_t)function_pointer);
	glBlendFunc(sfactor, dfactor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglBitmap(JNIEnv *env, jclass clazz, jint width, jint height, jfloat xorig, jfloat yorig, jfloat xmove, jfloat ymove, jlong bitmap, jlong function_pointer) {
	const GLubyte *bitmap_address = (const GLubyte *)(intptr_t)bitmap;
	glBitmapPROC glBitmap = (glBitmapPROC)((intptr_t)function_pointer);
	glBitmap(width, height, xorig, yorig, xmove, ymove, bitmap_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglBitmapBO(JNIEnv *env, jclass clazz, jint width, jint height, jfloat xorig, jfloat yorig, jfloat xmove, jfloat ymove, jlong bitmap_buffer_offset, jlong function_pointer) {
	const GLubyte *bitmap_address = (const GLubyte *)(intptr_t)offsetToPointer(bitmap_buffer_offset);
	glBitmapPROC glBitmap = (glBitmapPROC)((intptr_t)function_pointer);
	glBitmap(width, height, xorig, yorig, xmove, ymove, bitmap_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglBindTexture(JNIEnv *env, jclass clazz, jint target, jint texture, jlong function_pointer) {
	glBindTexturePROC glBindTexture = (glBindTexturePROC)((intptr_t)function_pointer);
	glBindTexture(target, texture);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPrioritizeTextures(JNIEnv *env, jclass clazz, jint n, jlong textures, jlong priorities, jlong function_pointer) {
	const GLuint *textures_address = (const GLuint *)(intptr_t)textures;
	const GLfloat *priorities_address = (const GLfloat *)(intptr_t)priorities;
	glPrioritizeTexturesPROC glPrioritizeTextures = (glPrioritizeTexturesPROC)((intptr_t)function_pointer);
	glPrioritizeTextures(n, textures_address, priorities_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL11_nglAreTexturesResident(JNIEnv *env, jclass clazz, jint n, jlong textures, jlong residences, jlong function_pointer) {
	const GLuint *textures_address = (const GLuint *)(intptr_t)textures;
	GLboolean *residences_address = (GLboolean *)(intptr_t)residences;
	glAreTexturesResidentPROC glAreTexturesResident = (glAreTexturesResidentPROC)((intptr_t)function_pointer);
	GLboolean __result = glAreTexturesResident(n, textures_address, residences_address);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglBegin(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glBeginPROC glBegin = (glBeginPROC)((intptr_t)function_pointer);
	glBegin(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEnd(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndPROC glEnd = (glEndPROC)((intptr_t)function_pointer);
	glEnd();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglArrayElement(JNIEnv *env, jclass clazz, jint i, jlong function_pointer) {
	glArrayElementPROC glArrayElement = (glArrayElementPROC)((intptr_t)function_pointer);
	glArrayElement(i);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClearDepth(JNIEnv *env, jclass clazz, jdouble depth, jlong function_pointer) {
	glClearDepthPROC glClearDepth = (glClearDepthPROC)((intptr_t)function_pointer);
	glClearDepth(depth);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDeleteLists(JNIEnv *env, jclass clazz, jint list, jint range, jlong function_pointer) {
	glDeleteListsPROC glDeleteLists = (glDeleteListsPROC)((intptr_t)function_pointer);
	glDeleteLists(list, range);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDeleteTextures(JNIEnv *env, jclass clazz, jint n, jlong textures, jlong function_pointer) {
	const GLuint *textures_address = (const GLuint *)(intptr_t)textures;
	glDeleteTexturesPROC glDeleteTextures = (glDeleteTexturesPROC)((intptr_t)function_pointer);
	glDeleteTextures(n, textures_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCullFace(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glCullFacePROC glCullFace = (glCullFacePROC)((intptr_t)function_pointer);
	glCullFace(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCopyTexSubImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glCopyTexSubImage2DPROC glCopyTexSubImage2D = (glCopyTexSubImage2DPROC)((intptr_t)function_pointer);
	glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCopyTexSubImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint x, jint y, jint width, jlong function_pointer) {
	glCopyTexSubImage1DPROC glCopyTexSubImage1D = (glCopyTexSubImage1DPROC)((intptr_t)function_pointer);
	glCopyTexSubImage1D(target, level, xoffset, x, y, width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCopyTexImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint x, jint y, jint width, jint height, jint border, jlong function_pointer) {
	glCopyTexImage2DPROC glCopyTexImage2D = (glCopyTexImage2DPROC)((intptr_t)function_pointer);
	glCopyTexImage2D(target, level, internalFormat, x, y, width, height, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCopyTexImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalFormat, jint x, jint y, jint width, jint border, jlong function_pointer) {
	glCopyTexImage1DPROC glCopyTexImage1D = (glCopyTexImage1DPROC)((intptr_t)function_pointer);
	glCopyTexImage1D(target, level, internalFormat, x, y, width, border);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglCopyPixels(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint type, jlong function_pointer) {
	glCopyPixelsPROC glCopyPixels = (glCopyPixelsPROC)((intptr_t)function_pointer);
	glCopyPixels(x, y, width, height, type);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColorPointer(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glColorPointerPROC glColorPointer = (glColorPointerPROC)((intptr_t)function_pointer);
	glColorPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColorPointerBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glColorPointerPROC glColorPointer = (glColorPointerPROC)((intptr_t)function_pointer);
	glColorPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColorMaterial(JNIEnv *env, jclass clazz, jint face, jint mode, jlong function_pointer) {
	glColorMaterialPROC glColorMaterial = (glColorMaterialPROC)((intptr_t)function_pointer);
	glColorMaterial(face, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColorMask(JNIEnv *env, jclass clazz, jboolean red, jboolean green, jboolean blue, jboolean alpha, jlong function_pointer) {
	glColorMaskPROC glColorMask = (glColorMaskPROC)((intptr_t)function_pointer);
	glColorMask(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor3b(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glColor3bPROC glColor3b = (glColor3bPROC)((intptr_t)function_pointer);
	glColor3b(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor3f(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jlong function_pointer) {
	glColor3fPROC glColor3f = (glColor3fPROC)((intptr_t)function_pointer);
	glColor3f(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor3d(JNIEnv *env, jclass clazz, jdouble red, jdouble green, jdouble blue, jlong function_pointer) {
	glColor3dPROC glColor3d = (glColor3dPROC)((intptr_t)function_pointer);
	glColor3d(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor3ub(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jlong function_pointer) {
	glColor3ubPROC glColor3ub = (glColor3ubPROC)((intptr_t)function_pointer);
	glColor3ub(red, green, blue);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor4b(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jbyte alpha, jlong function_pointer) {
	glColor4bPROC glColor4b = (glColor4bPROC)((intptr_t)function_pointer);
	glColor4b(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor4f(JNIEnv *env, jclass clazz, jfloat red, jfloat green, jfloat blue, jfloat alpha, jlong function_pointer) {
	glColor4fPROC glColor4f = (glColor4fPROC)((intptr_t)function_pointer);
	glColor4f(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor4d(JNIEnv *env, jclass clazz, jdouble red, jdouble green, jdouble blue, jdouble alpha, jlong function_pointer) {
	glColor4dPROC glColor4d = (glColor4dPROC)((intptr_t)function_pointer);
	glColor4d(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglColor4ub(JNIEnv *env, jclass clazz, jbyte red, jbyte green, jbyte blue, jbyte alpha, jlong function_pointer) {
	glColor4ubPROC glColor4ub = (glColor4ubPROC)((intptr_t)function_pointer);
	glColor4ub(red, green, blue, alpha);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClipPlane(JNIEnv *env, jclass clazz, jint plane, jlong equation, jlong function_pointer) {
	const GLdouble *equation_address = (const GLdouble *)(intptr_t)equation;
	glClipPlanePROC glClipPlane = (glClipPlanePROC)((intptr_t)function_pointer);
	glClipPlane(plane, equation_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglClearStencil(JNIEnv *env, jclass clazz, jint s, jlong function_pointer) {
	glClearStencilPROC glClearStencil = (glClearStencilPROC)((intptr_t)function_pointer);
	glClearStencil(s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalPoint1(JNIEnv *env, jclass clazz, jint i, jlong function_pointer) {
	glEvalPoint1PROC glEvalPoint1 = (glEvalPoint1PROC)((intptr_t)function_pointer);
	glEvalPoint1(i);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalPoint2(JNIEnv *env, jclass clazz, jint i, jint j, jlong function_pointer) {
	glEvalPoint2PROC glEvalPoint2 = (glEvalPoint2PROC)((intptr_t)function_pointer);
	glEvalPoint2(i, j);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalMesh1(JNIEnv *env, jclass clazz, jint mode, jint i1, jint i2, jlong function_pointer) {
	glEvalMesh1PROC glEvalMesh1 = (glEvalMesh1PROC)((intptr_t)function_pointer);
	glEvalMesh1(mode, i1, i2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalMesh2(JNIEnv *env, jclass clazz, jint mode, jint i1, jint i2, jint j1, jint j2, jlong function_pointer) {
	glEvalMesh2PROC glEvalMesh2 = (glEvalMesh2PROC)((intptr_t)function_pointer);
	glEvalMesh2(mode, i1, i2, j1, j2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalCoord1f(JNIEnv *env, jclass clazz, jfloat u, jlong function_pointer) {
	glEvalCoord1fPROC glEvalCoord1f = (glEvalCoord1fPROC)((intptr_t)function_pointer);
	glEvalCoord1f(u);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalCoord1d(JNIEnv *env, jclass clazz, jdouble u, jlong function_pointer) {
	glEvalCoord1dPROC glEvalCoord1d = (glEvalCoord1dPROC)((intptr_t)function_pointer);
	glEvalCoord1d(u);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalCoord2f(JNIEnv *env, jclass clazz, jfloat u, jfloat v, jlong function_pointer) {
	glEvalCoord2fPROC glEvalCoord2f = (glEvalCoord2fPROC)((intptr_t)function_pointer);
	glEvalCoord2f(u, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEvalCoord2d(JNIEnv *env, jclass clazz, jdouble u, jdouble v, jlong function_pointer) {
	glEvalCoord2dPROC glEvalCoord2d = (glEvalCoord2dPROC)((intptr_t)function_pointer);
	glEvalCoord2d(u, v);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEnableClientState(JNIEnv *env, jclass clazz, jint cap, jlong function_pointer) {
	glEnableClientStatePROC glEnableClientState = (glEnableClientStatePROC)((intptr_t)function_pointer);
	glEnableClientState(cap);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDisableClientState(JNIEnv *env, jclass clazz, jint cap, jlong function_pointer) {
	glDisableClientStatePROC glDisableClientState = (glDisableClientStatePROC)((intptr_t)function_pointer);
	glDisableClientState(cap);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEnable(JNIEnv *env, jclass clazz, jint cap, jlong function_pointer) {
	glEnablePROC glEnable = (glEnablePROC)((intptr_t)function_pointer);
	glEnable(cap);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDisable(JNIEnv *env, jclass clazz, jint cap, jlong function_pointer) {
	glDisablePROC glDisable = (glDisablePROC)((intptr_t)function_pointer);
	glDisable(cap);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEdgeFlagPointer(JNIEnv *env, jclass clazz, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glEdgeFlagPointerPROC glEdgeFlagPointer = (glEdgeFlagPointerPROC)((intptr_t)function_pointer);
	glEdgeFlagPointer(stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEdgeFlagPointerBO(JNIEnv *env, jclass clazz, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glEdgeFlagPointerPROC glEdgeFlagPointer = (glEdgeFlagPointerPROC)((intptr_t)function_pointer);
	glEdgeFlagPointer(stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEdgeFlag(JNIEnv *env, jclass clazz, jboolean flag, jlong function_pointer) {
	glEdgeFlagPROC glEdgeFlag = (glEdgeFlagPROC)((intptr_t)function_pointer);
	glEdgeFlag(flag);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawPixels(JNIEnv *env, jclass clazz, jint width, jint height, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glDrawPixelsPROC glDrawPixels = (glDrawPixelsPROC)((intptr_t)function_pointer);
	glDrawPixels(width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawPixelsBO(JNIEnv *env, jclass clazz, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glDrawPixelsPROC glDrawPixels = (glDrawPixelsPROC)((intptr_t)function_pointer);
	glDrawPixels(width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawElements(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)indices;
	glDrawElementsPROC glDrawElements = (glDrawElementsPROC)((intptr_t)function_pointer);
	glDrawElements(mode, count, type, indices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawElementsBO(JNIEnv *env, jclass clazz, jint mode, jint count, jint type, jlong indices_buffer_offset, jlong function_pointer) {
	const GLvoid *indices_address = (const GLvoid *)(intptr_t)offsetToPointer(indices_buffer_offset);
	glDrawElementsPROC glDrawElements = (glDrawElementsPROC)((intptr_t)function_pointer);
	glDrawElements(mode, count, type, indices_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawBuffer(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glDrawBufferPROC glDrawBuffer = (glDrawBufferPROC)((intptr_t)function_pointer);
	glDrawBuffer(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDrawArrays(JNIEnv *env, jclass clazz, jint mode, jint first, jint count, jlong function_pointer) {
	glDrawArraysPROC glDrawArrays = (glDrawArraysPROC)((intptr_t)function_pointer);
	glDrawArrays(mode, first, count);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDepthRange(JNIEnv *env, jclass clazz, jdouble zNear, jdouble zFar, jlong function_pointer) {
	glDepthRangePROC glDepthRange = (glDepthRangePROC)((intptr_t)function_pointer);
	glDepthRange(zNear, zFar);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDepthMask(JNIEnv *env, jclass clazz, jboolean flag, jlong function_pointer) {
	glDepthMaskPROC glDepthMask = (glDepthMaskPROC)((intptr_t)function_pointer);
	glDepthMask(flag);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglDepthFunc(JNIEnv *env, jclass clazz, jint func, jlong function_pointer) {
	glDepthFuncPROC glDepthFunc = (glDepthFuncPROC)((intptr_t)function_pointer);
	glDepthFunc(func);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFeedbackBuffer(JNIEnv *env, jclass clazz, jint size, jint type, jlong buffer, jlong function_pointer) {
	GLfloat *buffer_address = (GLfloat *)(intptr_t)buffer;
	glFeedbackBufferPROC glFeedbackBuffer = (glFeedbackBufferPROC)((intptr_t)function_pointer);
	glFeedbackBuffer(size, type, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapfv(JNIEnv *env, jclass clazz, jint map, jlong values, jlong function_pointer) {
	GLfloat *values_address = (GLfloat *)(intptr_t)values;
	glGetPixelMapfvPROC glGetPixelMapfv = (glGetPixelMapfvPROC)((intptr_t)function_pointer);
	glGetPixelMapfv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapfvBO(JNIEnv *env, jclass clazz, jint map, jlong values_buffer_offset, jlong function_pointer) {
	GLfloat *values_address = (GLfloat *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetPixelMapfvPROC glGetPixelMapfv = (glGetPixelMapfvPROC)((intptr_t)function_pointer);
	glGetPixelMapfv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapuiv(JNIEnv *env, jclass clazz, jint map, jlong values, jlong function_pointer) {
	GLuint *values_address = (GLuint *)(intptr_t)values;
	glGetPixelMapuivPROC glGetPixelMapuiv = (glGetPixelMapuivPROC)((intptr_t)function_pointer);
	glGetPixelMapuiv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapuivBO(JNIEnv *env, jclass clazz, jint map, jlong values_buffer_offset, jlong function_pointer) {
	GLuint *values_address = (GLuint *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetPixelMapuivPROC glGetPixelMapuiv = (glGetPixelMapuivPROC)((intptr_t)function_pointer);
	glGetPixelMapuiv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapusv(JNIEnv *env, jclass clazz, jint map, jlong values, jlong function_pointer) {
	GLushort *values_address = (GLushort *)(intptr_t)values;
	glGetPixelMapusvPROC glGetPixelMapusv = (glGetPixelMapusvPROC)((intptr_t)function_pointer);
	glGetPixelMapusv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPixelMapusvBO(JNIEnv *env, jclass clazz, jint map, jlong values_buffer_offset, jlong function_pointer) {
	GLushort *values_address = (GLushort *)(intptr_t)offsetToPointer(values_buffer_offset);
	glGetPixelMapusvPROC glGetPixelMapusv = (glGetPixelMapusvPROC)((intptr_t)function_pointer);
	glGetPixelMapusv(map, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetMaterialfv(JNIEnv *env, jclass clazz, jint face, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetMaterialfvPROC glGetMaterialfv = (glGetMaterialfvPROC)((intptr_t)function_pointer);
	glGetMaterialfv(face, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetMaterialiv(JNIEnv *env, jclass clazz, jint face, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetMaterialivPROC glGetMaterialiv = (glGetMaterialivPROC)((intptr_t)function_pointer);
	glGetMaterialiv(face, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetMapfv(JNIEnv *env, jclass clazz, jint target, jint query, jlong v, jlong function_pointer) {
	GLfloat *v_address = (GLfloat *)(intptr_t)v;
	glGetMapfvPROC glGetMapfv = (glGetMapfvPROC)((intptr_t)function_pointer);
	glGetMapfv(target, query, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetMapdv(JNIEnv *env, jclass clazz, jint target, jint query, jlong v, jlong function_pointer) {
	GLdouble *v_address = (GLdouble *)(intptr_t)v;
	glGetMapdvPROC glGetMapdv = (glGetMapdvPROC)((intptr_t)function_pointer);
	glGetMapdv(target, query, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetMapiv(JNIEnv *env, jclass clazz, jint target, jint query, jlong v, jlong function_pointer) {
	GLint *v_address = (GLint *)(intptr_t)v;
	glGetMapivPROC glGetMapiv = (glGetMapivPROC)((intptr_t)function_pointer);
	glGetMapiv(target, query, v_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetLightfv(JNIEnv *env, jclass clazz, jint light, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetLightfvPROC glGetLightfv = (glGetLightfvPROC)((intptr_t)function_pointer);
	glGetLightfv(light, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetLightiv(JNIEnv *env, jclass clazz, jint light, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetLightivPROC glGetLightiv = (glGetLightivPROC)((intptr_t)function_pointer);
	glGetLightiv(light, pname, params_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL11_nglGetError(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glGetErrorPROC glGetError = (glGetErrorPROC)((intptr_t)function_pointer);
	GLint __result = glGetError();
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetClipPlane(JNIEnv *env, jclass clazz, jint plane, jlong equation, jlong function_pointer) {
	GLdouble *equation_address = (GLdouble *)(intptr_t)equation;
	glGetClipPlanePROC glGetClipPlane = (glGetClipPlanePROC)((intptr_t)function_pointer);
	glGetClipPlane(plane, equation_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetBooleanv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	GLboolean *params_address = (GLboolean *)(intptr_t)params;
	glGetBooleanvPROC glGetBooleanv = (glGetBooleanvPROC)((intptr_t)function_pointer);
	glGetBooleanv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetDoublev(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetDoublevPROC glGetDoublev = (glGetDoublevPROC)((intptr_t)function_pointer);
	glGetDoublev(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetFloatv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetFloatvPROC glGetFloatv = (glGetFloatvPROC)((intptr_t)function_pointer);
	glGetFloatv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetIntegerv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetIntegervPROC glGetIntegerv = (glGetIntegervPROC)((intptr_t)function_pointer);
	glGetIntegerv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGenTextures(JNIEnv *env, jclass clazz, jint n, jlong textures, jlong function_pointer) {
	GLuint *textures_address = (GLuint *)(intptr_t)textures;
	glGenTexturesPROC glGenTextures = (glGenTexturesPROC)((intptr_t)function_pointer);
	glGenTextures(n, textures_address);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL11_nglGenLists(JNIEnv *env, jclass clazz, jint range, jlong function_pointer) {
	glGenListsPROC glGenLists = (glGenListsPROC)((intptr_t)function_pointer);
	GLuint __result = glGenLists(range);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFrustum(JNIEnv *env, jclass clazz, jdouble left, jdouble right, jdouble bottom, jdouble top, jdouble zNear, jdouble zFar, jlong function_pointer) {
	glFrustumPROC glFrustum = (glFrustumPROC)((intptr_t)function_pointer);
	glFrustum(left, right, bottom, top, zNear, zFar);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFrontFace(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glFrontFacePROC glFrontFace = (glFrontFacePROC)((intptr_t)function_pointer);
	glFrontFace(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFogf(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glFogfPROC glFogf = (glFogfPROC)((intptr_t)function_pointer);
	glFogf(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFogi(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glFogiPROC glFogi = (glFogiPROC)((intptr_t)function_pointer);
	glFogi(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFogfv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glFogfvPROC glFogfv = (glFogfvPROC)((intptr_t)function_pointer);
	glFogfv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFogiv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glFogivPROC glFogiv = (glFogivPROC)((intptr_t)function_pointer);
	glFogiv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFlush(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glFlushPROC glFlush = (glFlushPROC)((intptr_t)function_pointer);
	glFlush();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglFinish(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glFinishPROC glFinish = (glFinishPROC)((intptr_t)function_pointer);
	glFinish();
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL11_nglGetPointerv(JNIEnv *env, jclass clazz, jint pname, jlong result_size, jlong function_pointer) {
	glGetPointervPROC glGetPointerv = (glGetPointervPROC)((intptr_t)function_pointer);
	GLvoid * __result;
	glGetPointerv(pname, &__result);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL11_nglIsEnabled(JNIEnv *env, jclass clazz, jint cap, jlong function_pointer) {
	glIsEnabledPROC glIsEnabled = (glIsEnabledPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsEnabled(cap);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglInterleavedArrays(JNIEnv *env, jclass clazz, jint format, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glInterleavedArraysPROC glInterleavedArrays = (glInterleavedArraysPROC)((intptr_t)function_pointer);
	glInterleavedArrays(format, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglInterleavedArraysBO(JNIEnv *env, jclass clazz, jint format, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glInterleavedArraysPROC glInterleavedArrays = (glInterleavedArraysPROC)((intptr_t)function_pointer);
	glInterleavedArrays(format, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglInitNames(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glInitNamesPROC glInitNames = (glInitNamesPROC)((intptr_t)function_pointer);
	glInitNames();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglHint(JNIEnv *env, jclass clazz, jint target, jint mode, jlong function_pointer) {
	glHintPROC glHint = (glHintPROC)((intptr_t)function_pointer);
	glHint(target, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTexParameterfvPROC glGetTexParameterfv = (glGetTexParameterfvPROC)((intptr_t)function_pointer);
	glGetTexParameterfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexParameterivPROC glGetTexParameteriv = (glGetTexParameterivPROC)((intptr_t)function_pointer);
	glGetTexParameteriv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexLevelParameterfv(JNIEnv *env, jclass clazz, jint target, jint level, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTexLevelParameterfvPROC glGetTexLevelParameterfv = (glGetTexLevelParameterfvPROC)((intptr_t)function_pointer);
	glGetTexLevelParameterfv(target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexLevelParameteriv(JNIEnv *env, jclass clazz, jint target, jint level, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexLevelParameterivPROC glGetTexLevelParameteriv = (glGetTexLevelParameterivPROC)((intptr_t)function_pointer);
	glGetTexLevelParameteriv(target, level, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexImage(JNIEnv *env, jclass clazz, jint target, jint level, jint format, jint type, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glGetTexImagePROC glGetTexImage = (glGetTexImagePROC)((intptr_t)function_pointer);
	glGetTexImage(target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexImageBO(JNIEnv *env, jclass clazz, jint target, jint level, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glGetTexImagePROC glGetTexImage = (glGetTexImagePROC)((intptr_t)function_pointer);
	glGetTexImage(target, level, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexGeniv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexGenivPROC glGetTexGeniv = (glGetTexGenivPROC)((intptr_t)function_pointer);
	glGetTexGeniv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexGenfv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTexGenfvPROC glGetTexGenfv = (glGetTexGenfvPROC)((intptr_t)function_pointer);
	glGetTexGenfv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexGendv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	GLdouble *params_address = (GLdouble *)(intptr_t)params;
	glGetTexGendvPROC glGetTexGendv = (glGetTexGendvPROC)((intptr_t)function_pointer);
	glGetTexGendv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexEnviv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	GLint *params_address = (GLint *)(intptr_t)params;
	glGetTexEnvivPROC glGetTexEnviv = (glGetTexEnvivPROC)((intptr_t)function_pointer);
	glGetTexEnviv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetTexEnvfv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	GLfloat *params_address = (GLfloat *)(intptr_t)params;
	glGetTexEnvfvPROC glGetTexEnvfv = (glGetTexEnvfvPROC)((intptr_t)function_pointer);
	glGetTexEnvfv(coord, pname, params_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_GL11_nglGetString(JNIEnv *env, jclass clazz, jint name, jlong function_pointer) {
	glGetStringPROC glGetString = (glGetStringPROC)((intptr_t)function_pointer);
	const GLubyte * __result = glGetString(name);
	return NewStringNativeUnsigned(env, __result);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPolygonStipple(JNIEnv *env, jclass clazz, jlong mask, jlong function_pointer) {
	GLubyte *mask_address = (GLubyte *)(intptr_t)mask;
	glGetPolygonStipplePROC glGetPolygonStipple = (glGetPolygonStipplePROC)((intptr_t)function_pointer);
	glGetPolygonStipple(mask_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglGetPolygonStippleBO(JNIEnv *env, jclass clazz, jlong mask_buffer_offset, jlong function_pointer) {
	GLubyte *mask_address = (GLubyte *)(intptr_t)offsetToPointer(mask_buffer_offset);
	glGetPolygonStipplePROC glGetPolygonStipple = (glGetPolygonStipplePROC)((intptr_t)function_pointer);
	glGetPolygonStipple(mask_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL11_nglIsList(JNIEnv *env, jclass clazz, jint list, jlong function_pointer) {
	glIsListPROC glIsList = (glIsListPROC)((intptr_t)function_pointer);
	GLboolean __result = glIsList(list);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMaterialf(JNIEnv *env, jclass clazz, jint face, jint pname, jfloat param, jlong function_pointer) {
	glMaterialfPROC glMaterialf = (glMaterialfPROC)((intptr_t)function_pointer);
	glMaterialf(face, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMateriali(JNIEnv *env, jclass clazz, jint face, jint pname, jint param, jlong function_pointer) {
	glMaterialiPROC glMateriali = (glMaterialiPROC)((intptr_t)function_pointer);
	glMateriali(face, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMaterialfv(JNIEnv *env, jclass clazz, jint face, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glMaterialfvPROC glMaterialfv = (glMaterialfvPROC)((intptr_t)function_pointer);
	glMaterialfv(face, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMaterialiv(JNIEnv *env, jclass clazz, jint face, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glMaterialivPROC glMaterialiv = (glMaterialivPROC)((intptr_t)function_pointer);
	glMaterialiv(face, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMapGrid1f(JNIEnv *env, jclass clazz, jint un, jfloat u1, jfloat u2, jlong function_pointer) {
	glMapGrid1fPROC glMapGrid1f = (glMapGrid1fPROC)((intptr_t)function_pointer);
	glMapGrid1f(un, u1, u2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMapGrid1d(JNIEnv *env, jclass clazz, jint un, jdouble u1, jdouble u2, jlong function_pointer) {
	glMapGrid1dPROC glMapGrid1d = (glMapGrid1dPROC)((intptr_t)function_pointer);
	glMapGrid1d(un, u1, u2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMapGrid2f(JNIEnv *env, jclass clazz, jint un, jfloat u1, jfloat u2, jint vn, jfloat v1, jfloat v2, jlong function_pointer) {
	glMapGrid2fPROC glMapGrid2f = (glMapGrid2fPROC)((intptr_t)function_pointer);
	glMapGrid2f(un, u1, u2, vn, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMapGrid2d(JNIEnv *env, jclass clazz, jint un, jdouble u1, jdouble u2, jint vn, jdouble v1, jdouble v2, jlong function_pointer) {
	glMapGrid2dPROC glMapGrid2d = (glMapGrid2dPROC)((intptr_t)function_pointer);
	glMapGrid2d(un, u1, u2, vn, v1, v2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMap2f(JNIEnv *env, jclass clazz, jint target, jfloat u1, jfloat u2, jint ustride, jint uorder, jfloat v1, jfloat v2, jint vstride, jint vorder, jlong points, jlong function_pointer) {
	const GLfloat *points_address = (const GLfloat *)(intptr_t)points;
	glMap2fPROC glMap2f = (glMap2fPROC)((intptr_t)function_pointer);
	glMap2f(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMap2d(JNIEnv *env, jclass clazz, jint target, jdouble u1, jdouble u2, jint ustride, jint uorder, jdouble v1, jdouble v2, jint vstride, jint vorder, jlong points, jlong function_pointer) {
	const GLdouble *points_address = (const GLdouble *)(intptr_t)points;
	glMap2dPROC glMap2d = (glMap2dPROC)((intptr_t)function_pointer);
	glMap2d(target, u1, u2, ustride, uorder, v1, v2, vstride, vorder, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMap1f(JNIEnv *env, jclass clazz, jint target, jfloat u1, jfloat u2, jint stride, jint order, jlong points, jlong function_pointer) {
	const GLfloat *points_address = (const GLfloat *)(intptr_t)points;
	glMap1fPROC glMap1f = (glMap1fPROC)((intptr_t)function_pointer);
	glMap1f(target, u1, u2, stride, order, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMap1d(JNIEnv *env, jclass clazz, jint target, jdouble u1, jdouble u2, jint stride, jint order, jlong points, jlong function_pointer) {
	const GLdouble *points_address = (const GLdouble *)(intptr_t)points;
	glMap1dPROC glMap1d = (glMap1dPROC)((intptr_t)function_pointer);
	glMap1d(target, u1, u2, stride, order, points_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLogicOp(JNIEnv *env, jclass clazz, jint opcode, jlong function_pointer) {
	glLogicOpPROC glLogicOp = (glLogicOpPROC)((intptr_t)function_pointer);
	glLogicOp(opcode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLoadName(JNIEnv *env, jclass clazz, jint name, jlong function_pointer) {
	glLoadNamePROC glLoadName = (glLoadNamePROC)((intptr_t)function_pointer);
	glLoadName(name);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLoadMatrixf(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLfloat *m_address = (const GLfloat *)(intptr_t)m;
	glLoadMatrixfPROC glLoadMatrixf = (glLoadMatrixfPROC)((intptr_t)function_pointer);
	glLoadMatrixf(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLoadMatrixd(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLdouble *m_address = (const GLdouble *)(intptr_t)m;
	glLoadMatrixdPROC glLoadMatrixd = (glLoadMatrixdPROC)((intptr_t)function_pointer);
	glLoadMatrixd(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLoadIdentity(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glLoadIdentityPROC glLoadIdentity = (glLoadIdentityPROC)((intptr_t)function_pointer);
	glLoadIdentity();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglListBase(JNIEnv *env, jclass clazz, jint base, jlong function_pointer) {
	glListBasePROC glListBase = (glListBasePROC)((intptr_t)function_pointer);
	glListBase(base);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLineWidth(JNIEnv *env, jclass clazz, jfloat width, jlong function_pointer) {
	glLineWidthPROC glLineWidth = (glLineWidthPROC)((intptr_t)function_pointer);
	glLineWidth(width);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLineStipple(JNIEnv *env, jclass clazz, jint factor, jshort pattern, jlong function_pointer) {
	glLineStipplePROC glLineStipple = (glLineStipplePROC)((intptr_t)function_pointer);
	glLineStipple(factor, pattern);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightModelf(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glLightModelfPROC glLightModelf = (glLightModelfPROC)((intptr_t)function_pointer);
	glLightModelf(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightModeli(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glLightModeliPROC glLightModeli = (glLightModeliPROC)((intptr_t)function_pointer);
	glLightModeli(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightModelfv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glLightModelfvPROC glLightModelfv = (glLightModelfvPROC)((intptr_t)function_pointer);
	glLightModelfv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightModeliv(JNIEnv *env, jclass clazz, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glLightModelivPROC glLightModeliv = (glLightModelivPROC)((intptr_t)function_pointer);
	glLightModeliv(pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightf(JNIEnv *env, jclass clazz, jint light, jint pname, jfloat param, jlong function_pointer) {
	glLightfPROC glLightf = (glLightfPROC)((intptr_t)function_pointer);
	glLightf(light, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLighti(JNIEnv *env, jclass clazz, jint light, jint pname, jint param, jlong function_pointer) {
	glLightiPROC glLighti = (glLightiPROC)((intptr_t)function_pointer);
	glLighti(light, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightfv(JNIEnv *env, jclass clazz, jint light, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glLightfvPROC glLightfv = (glLightfvPROC)((intptr_t)function_pointer);
	glLightfv(light, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglLightiv(JNIEnv *env, jclass clazz, jint light, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glLightivPROC glLightiv = (glLightivPROC)((intptr_t)function_pointer);
	glLightiv(light, pname, params_address);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_GL11_nglIsTexture(JNIEnv *env, jclass clazz, jint texture, jlong function_pointer) {
	glIsTexturePROC glIsTexture = (glIsTexturePROC)((intptr_t)function_pointer);
	GLboolean __result = glIsTexture(texture);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMatrixMode(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glMatrixModePROC glMatrixMode = (glMatrixModePROC)((intptr_t)function_pointer);
	glMatrixMode(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPolygonStipple(JNIEnv *env, jclass clazz, jlong mask, jlong function_pointer) {
	const GLubyte *mask_address = (const GLubyte *)(intptr_t)mask;
	glPolygonStipplePROC glPolygonStipple = (glPolygonStipplePROC)((intptr_t)function_pointer);
	glPolygonStipple(mask_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPolygonStippleBO(JNIEnv *env, jclass clazz, jlong mask_buffer_offset, jlong function_pointer) {
	const GLubyte *mask_address = (const GLubyte *)(intptr_t)offsetToPointer(mask_buffer_offset);
	glPolygonStipplePROC glPolygonStipple = (glPolygonStipplePROC)((intptr_t)function_pointer);
	glPolygonStipple(mask_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPolygonOffset(JNIEnv *env, jclass clazz, jfloat factor, jfloat units, jlong function_pointer) {
	glPolygonOffsetPROC glPolygonOffset = (glPolygonOffsetPROC)((intptr_t)function_pointer);
	glPolygonOffset(factor, units);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPolygonMode(JNIEnv *env, jclass clazz, jint face, jint mode, jlong function_pointer) {
	glPolygonModePROC glPolygonMode = (glPolygonModePROC)((intptr_t)function_pointer);
	glPolygonMode(face, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPointSize(JNIEnv *env, jclass clazz, jfloat size, jlong function_pointer) {
	glPointSizePROC glPointSize = (glPointSizePROC)((intptr_t)function_pointer);
	glPointSize(size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelZoom(JNIEnv *env, jclass clazz, jfloat xfactor, jfloat yfactor, jlong function_pointer) {
	glPixelZoomPROC glPixelZoom = (glPixelZoomPROC)((intptr_t)function_pointer);
	glPixelZoom(xfactor, yfactor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelTransferf(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPixelTransferfPROC glPixelTransferf = (glPixelTransferfPROC)((intptr_t)function_pointer);
	glPixelTransferf(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelTransferi(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glPixelTransferiPROC glPixelTransferi = (glPixelTransferiPROC)((intptr_t)function_pointer);
	glPixelTransferi(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelStoref(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPixelStorefPROC glPixelStoref = (glPixelStorefPROC)((intptr_t)function_pointer);
	glPixelStoref(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelStorei(JNIEnv *env, jclass clazz, jint pname, jint param, jlong function_pointer) {
	glPixelStoreiPROC glPixelStorei = (glPixelStoreiPROC)((intptr_t)function_pointer);
	glPixelStorei(pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapfv(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)values;
	glPixelMapfvPROC glPixelMapfv = (glPixelMapfvPROC)((intptr_t)function_pointer);
	glPixelMapfv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapfvBO(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values_buffer_offset, jlong function_pointer) {
	const GLfloat *values_address = (const GLfloat *)(intptr_t)offsetToPointer(values_buffer_offset);
	glPixelMapfvPROC glPixelMapfv = (glPixelMapfvPROC)((intptr_t)function_pointer);
	glPixelMapfv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapuiv(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values, jlong function_pointer) {
	const GLuint *values_address = (const GLuint *)(intptr_t)values;
	glPixelMapuivPROC glPixelMapuiv = (glPixelMapuivPROC)((intptr_t)function_pointer);
	glPixelMapuiv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapuivBO(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values_buffer_offset, jlong function_pointer) {
	const GLuint *values_address = (const GLuint *)(intptr_t)offsetToPointer(values_buffer_offset);
	glPixelMapuivPROC glPixelMapuiv = (glPixelMapuivPROC)((intptr_t)function_pointer);
	glPixelMapuiv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapusv(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values, jlong function_pointer) {
	const GLushort *values_address = (const GLushort *)(intptr_t)values;
	glPixelMapusvPROC glPixelMapusv = (glPixelMapusvPROC)((intptr_t)function_pointer);
	glPixelMapusv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPixelMapusvBO(JNIEnv *env, jclass clazz, jint map, jint mapsize, jlong values_buffer_offset, jlong function_pointer) {
	const GLushort *values_address = (const GLushort *)(intptr_t)offsetToPointer(values_buffer_offset);
	glPixelMapusvPROC glPixelMapusv = (glPixelMapusvPROC)((intptr_t)function_pointer);
	glPixelMapusv(map, mapsize, values_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPassThrough(JNIEnv *env, jclass clazz, jfloat token, jlong function_pointer) {
	glPassThroughPROC glPassThrough = (glPassThroughPROC)((intptr_t)function_pointer);
	glPassThrough(token);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglOrtho(JNIEnv *env, jclass clazz, jdouble left, jdouble right, jdouble bottom, jdouble top, jdouble zNear, jdouble zFar, jlong function_pointer) {
	glOrthoPROC glOrtho = (glOrthoPROC)((intptr_t)function_pointer);
	glOrtho(left, right, bottom, top, zNear, zFar);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormalPointer(JNIEnv *env, jclass clazz, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glNormalPointerPROC glNormalPointer = (glNormalPointerPROC)((intptr_t)function_pointer);
	glNormalPointer(type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormalPointerBO(JNIEnv *env, jclass clazz, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glNormalPointerPROC glNormalPointer = (glNormalPointerPROC)((intptr_t)function_pointer);
	glNormalPointer(type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormal3b(JNIEnv *env, jclass clazz, jbyte nx, jbyte ny, jbyte nz, jlong function_pointer) {
	glNormal3bPROC glNormal3b = (glNormal3bPROC)((intptr_t)function_pointer);
	glNormal3b(nx, ny, nz);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormal3f(JNIEnv *env, jclass clazz, jfloat nx, jfloat ny, jfloat nz, jlong function_pointer) {
	glNormal3fPROC glNormal3f = (glNormal3fPROC)((intptr_t)function_pointer);
	glNormal3f(nx, ny, nz);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormal3d(JNIEnv *env, jclass clazz, jdouble nx, jdouble ny, jdouble nz, jlong function_pointer) {
	glNormal3dPROC glNormal3d = (glNormal3dPROC)((intptr_t)function_pointer);
	glNormal3d(nx, ny, nz);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNormal3i(JNIEnv *env, jclass clazz, jint nx, jint ny, jint nz, jlong function_pointer) {
	glNormal3iPROC glNormal3i = (glNormal3iPROC)((intptr_t)function_pointer);
	glNormal3i(nx, ny, nz);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglNewList(JNIEnv *env, jclass clazz, jint list, jint mode, jlong function_pointer) {
	glNewListPROC glNewList = (glNewListPROC)((intptr_t)function_pointer);
	glNewList(list, mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglEndList(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glEndListPROC glEndList = (glEndListPROC)((intptr_t)function_pointer);
	glEndList();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMultMatrixf(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLfloat *m_address = (const GLfloat *)(intptr_t)m;
	glMultMatrixfPROC glMultMatrixf = (glMultMatrixfPROC)((intptr_t)function_pointer);
	glMultMatrixf(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglMultMatrixd(JNIEnv *env, jclass clazz, jlong m, jlong function_pointer) {
	const GLdouble *m_address = (const GLdouble *)(intptr_t)m;
	glMultMatrixdPROC glMultMatrixd = (glMultMatrixdPROC)((intptr_t)function_pointer);
	glMultMatrixd(m_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglShadeModel(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glShadeModelPROC glShadeModel = (glShadeModelPROC)((intptr_t)function_pointer);
	glShadeModel(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglSelectBuffer(JNIEnv *env, jclass clazz, jint size, jlong buffer, jlong function_pointer) {
	GLuint *buffer_address = (GLuint *)(intptr_t)buffer;
	glSelectBufferPROC glSelectBuffer = (glSelectBufferPROC)((intptr_t)function_pointer);
	glSelectBuffer(size, buffer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglScissor(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glScissorPROC glScissor = (glScissorPROC)((intptr_t)function_pointer);
	glScissor(x, y, width, height);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglScalef(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glScalefPROC glScalef = (glScalefPROC)((intptr_t)function_pointer);
	glScalef(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglScaled(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glScaledPROC glScaled = (glScaledPROC)((intptr_t)function_pointer);
	glScaled(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRotatef(JNIEnv *env, jclass clazz, jfloat angle, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glRotatefPROC glRotatef = (glRotatefPROC)((intptr_t)function_pointer);
	glRotatef(angle, x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRotated(JNIEnv *env, jclass clazz, jdouble angle, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glRotatedPROC glRotated = (glRotatedPROC)((intptr_t)function_pointer);
	glRotated(angle, x, y, z);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_GL11_nglRenderMode(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glRenderModePROC glRenderMode = (glRenderModePROC)((intptr_t)function_pointer);
	GLint __result = glRenderMode(mode);
	return __result;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRectf(JNIEnv *env, jclass clazz, jfloat x1, jfloat y1, jfloat x2, jfloat y2, jlong function_pointer) {
	glRectfPROC glRectf = (glRectfPROC)((intptr_t)function_pointer);
	glRectf(x1, y1, x2, y2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRectd(JNIEnv *env, jclass clazz, jdouble x1, jdouble y1, jdouble x2, jdouble y2, jlong function_pointer) {
	glRectdPROC glRectd = (glRectdPROC)((intptr_t)function_pointer);
	glRectd(x1, y1, x2, y2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRecti(JNIEnv *env, jclass clazz, jint x1, jint y1, jint x2, jint y2, jlong function_pointer) {
	glRectiPROC glRecti = (glRectiPROC)((intptr_t)function_pointer);
	glRecti(x1, y1, x2, y2);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglReadPixels(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jlong pixels, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)pixels;
	glReadPixelsPROC glReadPixels = (glReadPixelsPROC)((intptr_t)function_pointer);
	glReadPixels(x, y, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglReadPixelsBO(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	GLvoid *pixels_address = (GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glReadPixelsPROC glReadPixels = (glReadPixelsPROC)((intptr_t)function_pointer);
	glReadPixels(x, y, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglReadBuffer(JNIEnv *env, jclass clazz, jint mode, jlong function_pointer) {
	glReadBufferPROC glReadBuffer = (glReadBufferPROC)((intptr_t)function_pointer);
	glReadBuffer(mode);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos2f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jlong function_pointer) {
	glRasterPos2fPROC glRasterPos2f = (glRasterPos2fPROC)((intptr_t)function_pointer);
	glRasterPos2f(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos2d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jlong function_pointer) {
	glRasterPos2dPROC glRasterPos2d = (glRasterPos2dPROC)((intptr_t)function_pointer);
	glRasterPos2d(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos2i(JNIEnv *env, jclass clazz, jint x, jint y, jlong function_pointer) {
	glRasterPos2iPROC glRasterPos2i = (glRasterPos2iPROC)((intptr_t)function_pointer);
	glRasterPos2i(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos3f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glRasterPos3fPROC glRasterPos3f = (glRasterPos3fPROC)((intptr_t)function_pointer);
	glRasterPos3f(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos3d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glRasterPos3dPROC glRasterPos3d = (glRasterPos3dPROC)((intptr_t)function_pointer);
	glRasterPos3d(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos3i(JNIEnv *env, jclass clazz, jint x, jint y, jint z, jlong function_pointer) {
	glRasterPos3iPROC glRasterPos3i = (glRasterPos3iPROC)((intptr_t)function_pointer);
	glRasterPos3i(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos4f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glRasterPos4fPROC glRasterPos4f = (glRasterPos4fPROC)((intptr_t)function_pointer);
	glRasterPos4f(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos4d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glRasterPos4dPROC glRasterPos4d = (glRasterPos4dPROC)((intptr_t)function_pointer);
	glRasterPos4d(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglRasterPos4i(JNIEnv *env, jclass clazz, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glRasterPos4iPROC glRasterPos4i = (glRasterPos4iPROC)((intptr_t)function_pointer);
	glRasterPos4i(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPushName(JNIEnv *env, jclass clazz, jint name, jlong function_pointer) {
	glPushNamePROC glPushName = (glPushNamePROC)((intptr_t)function_pointer);
	glPushName(name);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPopName(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPopNamePROC glPopName = (glPopNamePROC)((intptr_t)function_pointer);
	glPopName();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPushMatrix(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPushMatrixPROC glPushMatrix = (glPushMatrixPROC)((intptr_t)function_pointer);
	glPushMatrix();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPopMatrix(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPopMatrixPROC glPopMatrix = (glPopMatrixPROC)((intptr_t)function_pointer);
	glPopMatrix();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPushClientAttrib(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glPushClientAttribPROC glPushClientAttrib = (glPushClientAttribPROC)((intptr_t)function_pointer);
	glPushClientAttrib(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPopClientAttrib(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPopClientAttribPROC glPopClientAttrib = (glPopClientAttribPROC)((intptr_t)function_pointer);
	glPopClientAttrib();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPushAttrib(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glPushAttribPROC glPushAttrib = (glPushAttribPROC)((intptr_t)function_pointer);
	glPushAttrib(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglPopAttrib(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glPopAttribPROC glPopAttrib = (glPopAttribPROC)((intptr_t)function_pointer);
	glPopAttrib();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglStencilFunc(JNIEnv *env, jclass clazz, jint func, jint ref, jint mask, jlong function_pointer) {
	glStencilFuncPROC glStencilFunc = (glStencilFuncPROC)((intptr_t)function_pointer);
	glStencilFunc(func, ref, mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertexPointer(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glVertexPointerPROC glVertexPointer = (glVertexPointerPROC)((intptr_t)function_pointer);
	glVertexPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertexPointerBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glVertexPointerPROC glVertexPointer = (glVertexPointerPROC)((intptr_t)function_pointer);
	glVertexPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex2f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jlong function_pointer) {
	glVertex2fPROC glVertex2f = (glVertex2fPROC)((intptr_t)function_pointer);
	glVertex2f(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex2d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jlong function_pointer) {
	glVertex2dPROC glVertex2d = (glVertex2dPROC)((intptr_t)function_pointer);
	glVertex2d(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex2i(JNIEnv *env, jclass clazz, jint x, jint y, jlong function_pointer) {
	glVertex2iPROC glVertex2i = (glVertex2iPROC)((intptr_t)function_pointer);
	glVertex2i(x, y);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex3f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glVertex3fPROC glVertex3f = (glVertex3fPROC)((intptr_t)function_pointer);
	glVertex3f(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex3d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glVertex3dPROC glVertex3d = (glVertex3dPROC)((intptr_t)function_pointer);
	glVertex3d(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex3i(JNIEnv *env, jclass clazz, jint x, jint y, jint z, jlong function_pointer) {
	glVertex3iPROC glVertex3i = (glVertex3iPROC)((intptr_t)function_pointer);
	glVertex3i(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex4f(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jfloat w, jlong function_pointer) {
	glVertex4fPROC glVertex4f = (glVertex4fPROC)((intptr_t)function_pointer);
	glVertex4f(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex4d(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jdouble w, jlong function_pointer) {
	glVertex4dPROC glVertex4d = (glVertex4dPROC)((intptr_t)function_pointer);
	glVertex4d(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglVertex4i(JNIEnv *env, jclass clazz, jint x, jint y, jint z, jint w, jlong function_pointer) {
	glVertex4iPROC glVertex4i = (glVertex4iPROC)((intptr_t)function_pointer);
	glVertex4i(x, y, z, w);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTranslatef(JNIEnv *env, jclass clazz, jfloat x, jfloat y, jfloat z, jlong function_pointer) {
	glTranslatefPROC glTranslatef = (glTranslatefPROC)((intptr_t)function_pointer);
	glTranslatef(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTranslated(JNIEnv *env, jclass clazz, jdouble x, jdouble y, jdouble z, jlong function_pointer) {
	glTranslatedPROC glTranslated = (glTranslatedPROC)((intptr_t)function_pointer);
	glTranslated(x, y, z);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexImage1DPROC glTexImage1D = (glTexImage1DPROC)((intptr_t)function_pointer);
	glTexImage1D(target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexImage1DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexImage1DPROC glTexImage1D = (glTexImage1DPROC)((intptr_t)function_pointer);
	glTexImage1D(target, level, internalformat, width, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexImage2DPROC glTexImage2D = (glTexImage2DPROC)((intptr_t)function_pointer);
	glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexImage2DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint internalformat, jint width, jint height, jint border, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexImage2DPROC glTexImage2D = (glTexImage2DPROC)((intptr_t)function_pointer);
	glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexSubImage1D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexSubImage1DPROC glTexSubImage1D = (glTexSubImage1DPROC)((intptr_t)function_pointer);
	glTexSubImage1D(target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexSubImage1DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint width, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexSubImage1DPROC glTexSubImage1D = (glTexSubImage1DPROC)((intptr_t)function_pointer);
	glTexSubImage1D(target, level, xoffset, width, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexSubImage2D(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)pixels;
	glTexSubImage2DPROC glTexSubImage2D = (glTexSubImage2DPROC)((intptr_t)function_pointer);
	glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexSubImage2DBO(JNIEnv *env, jclass clazz, jint target, jint level, jint xoffset, jint yoffset, jint width, jint height, jint format, jint type, jlong pixels_buffer_offset, jlong function_pointer) {
	const GLvoid *pixels_address = (const GLvoid *)(intptr_t)offsetToPointer(pixels_buffer_offset);
	glTexSubImage2DPROC glTexSubImage2D = (glTexSubImage2DPROC)((intptr_t)function_pointer);
	glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexParameterf(JNIEnv *env, jclass clazz, jint target, jint pname, jfloat param, jlong function_pointer) {
	glTexParameterfPROC glTexParameterf = (glTexParameterfPROC)((intptr_t)function_pointer);
	glTexParameterf(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexParameteri(JNIEnv *env, jclass clazz, jint target, jint pname, jint param, jlong function_pointer) {
	glTexParameteriPROC glTexParameteri = (glTexParameteriPROC)((intptr_t)function_pointer);
	glTexParameteri(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexParameterfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong param, jlong function_pointer) {
	const GLfloat *param_address = (const GLfloat *)(intptr_t)param;
	glTexParameterfvPROC glTexParameterfv = (glTexParameterfvPROC)((intptr_t)function_pointer);
	glTexParameterfv(target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexParameteriv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong param, jlong function_pointer) {
	const GLint *param_address = (const GLint *)(intptr_t)param;
	glTexParameterivPROC glTexParameteriv = (glTexParameterivPROC)((intptr_t)function_pointer);
	glTexParameteriv(target, pname, param_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGenf(JNIEnv *env, jclass clazz, jint coord, jint pname, jfloat param, jlong function_pointer) {
	glTexGenfPROC glTexGenf = (glTexGenfPROC)((intptr_t)function_pointer);
	glTexGenf(coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGend(JNIEnv *env, jclass clazz, jint coord, jint pname, jdouble param, jlong function_pointer) {
	glTexGendPROC glTexGend = (glTexGendPROC)((intptr_t)function_pointer);
	glTexGend(coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGenfv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glTexGenfvPROC glTexGenfv = (glTexGenfvPROC)((intptr_t)function_pointer);
	glTexGenfv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGendv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	const GLdouble *params_address = (const GLdouble *)(intptr_t)params;
	glTexGendvPROC glTexGendv = (glTexGendvPROC)((intptr_t)function_pointer);
	glTexGendv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGeni(JNIEnv *env, jclass clazz, jint coord, jint pname, jint param, jlong function_pointer) {
	glTexGeniPROC glTexGeni = (glTexGeniPROC)((intptr_t)function_pointer);
	glTexGeni(coord, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexGeniv(JNIEnv *env, jclass clazz, jint coord, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glTexGenivPROC glTexGeniv = (glTexGenivPROC)((intptr_t)function_pointer);
	glTexGeniv(coord, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexEnvf(JNIEnv *env, jclass clazz, jint target, jint pname, jfloat param, jlong function_pointer) {
	glTexEnvfPROC glTexEnvf = (glTexEnvfPROC)((intptr_t)function_pointer);
	glTexEnvf(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexEnvi(JNIEnv *env, jclass clazz, jint target, jint pname, jint param, jlong function_pointer) {
	glTexEnviPROC glTexEnvi = (glTexEnviPROC)((intptr_t)function_pointer);
	glTexEnvi(target, pname, param);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexEnvfv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLfloat *params_address = (const GLfloat *)(intptr_t)params;
	glTexEnvfvPROC glTexEnvfv = (glTexEnvfvPROC)((intptr_t)function_pointer);
	glTexEnvfv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexEnviv(JNIEnv *env, jclass clazz, jint target, jint pname, jlong params, jlong function_pointer) {
	const GLint *params_address = (const GLint *)(intptr_t)params;
	glTexEnvivPROC glTexEnviv = (glTexEnvivPROC)((intptr_t)function_pointer);
	glTexEnviv(target, pname, params_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoordPointer(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)pointer;
	glTexCoordPointerPROC glTexCoordPointer = (glTexCoordPointerPROC)((intptr_t)function_pointer);
	glTexCoordPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoordPointerBO(JNIEnv *env, jclass clazz, jint size, jint type, jint stride, jlong pointer_buffer_offset, jlong function_pointer) {
	const GLvoid *pointer_address = (const GLvoid *)(intptr_t)offsetToPointer(pointer_buffer_offset);
	glTexCoordPointerPROC glTexCoordPointer = (glTexCoordPointerPROC)((intptr_t)function_pointer);
	glTexCoordPointer(size, type, stride, pointer_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord1f(JNIEnv *env, jclass clazz, jfloat s, jlong function_pointer) {
	glTexCoord1fPROC glTexCoord1f = (glTexCoord1fPROC)((intptr_t)function_pointer);
	glTexCoord1f(s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord1d(JNIEnv *env, jclass clazz, jdouble s, jlong function_pointer) {
	glTexCoord1dPROC glTexCoord1d = (glTexCoord1dPROC)((intptr_t)function_pointer);
	glTexCoord1d(s);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord2f(JNIEnv *env, jclass clazz, jfloat s, jfloat t, jlong function_pointer) {
	glTexCoord2fPROC glTexCoord2f = (glTexCoord2fPROC)((intptr_t)function_pointer);
	glTexCoord2f(s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord2d(JNIEnv *env, jclass clazz, jdouble s, jdouble t, jlong function_pointer) {
	glTexCoord2dPROC glTexCoord2d = (glTexCoord2dPROC)((intptr_t)function_pointer);
	glTexCoord2d(s, t);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord3f(JNIEnv *env, jclass clazz, jfloat s, jfloat t, jfloat r, jlong function_pointer) {
	glTexCoord3fPROC glTexCoord3f = (glTexCoord3fPROC)((intptr_t)function_pointer);
	glTexCoord3f(s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord3d(JNIEnv *env, jclass clazz, jdouble s, jdouble t, jdouble r, jlong function_pointer) {
	glTexCoord3dPROC glTexCoord3d = (glTexCoord3dPROC)((intptr_t)function_pointer);
	glTexCoord3d(s, t, r);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord4f(JNIEnv *env, jclass clazz, jfloat s, jfloat t, jfloat r, jfloat q, jlong function_pointer) {
	glTexCoord4fPROC glTexCoord4f = (glTexCoord4fPROC)((intptr_t)function_pointer);
	glTexCoord4f(s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglTexCoord4d(JNIEnv *env, jclass clazz, jdouble s, jdouble t, jdouble r, jdouble q, jlong function_pointer) {
	glTexCoord4dPROC glTexCoord4d = (glTexCoord4dPROC)((intptr_t)function_pointer);
	glTexCoord4d(s, t, r, q);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglStencilOp(JNIEnv *env, jclass clazz, jint fail, jint zfail, jint zpass, jlong function_pointer) {
	glStencilOpPROC glStencilOp = (glStencilOpPROC)((intptr_t)function_pointer);
	glStencilOp(fail, zfail, zpass);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglStencilMask(JNIEnv *env, jclass clazz, jint mask, jlong function_pointer) {
	glStencilMaskPROC glStencilMask = (glStencilMaskPROC)((intptr_t)function_pointer);
	glStencilMask(mask);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_GL11_nglViewport(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height, jlong function_pointer) {
	glViewportPROC glViewport = (glViewportPROC)((intptr_t)function_pointer);
	glViewport(x, y, width, height);
}

