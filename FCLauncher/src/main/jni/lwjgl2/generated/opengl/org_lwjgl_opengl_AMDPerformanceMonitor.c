/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glGetPerfMonitorGroupsAMDPROC) (GLint * numGroups, GLsizei groupsSize, GLuint * groups);
typedef void (APIENTRY *glGetPerfMonitorCountersAMDPROC) (GLuint group, GLint * numCounters, GLint * maxActiveCounters, GLsizei countersSize, GLuint * counters);
typedef void (APIENTRY *glGetPerfMonitorGroupStringAMDPROC) (GLuint group, GLsizei bufSize, GLsizei * length, GLchar * groupString);
typedef void (APIENTRY *glGetPerfMonitorCounterStringAMDPROC) (GLuint group, GLuint counter, GLsizei bufSize, GLsizei * length, GLchar * counterString);
typedef void (APIENTRY *glGetPerfMonitorCounterInfoAMDPROC) (GLuint group, GLuint counter, GLenum pname, GLvoid * data);
typedef void (APIENTRY *glGenPerfMonitorsAMDPROC) (GLsizei n, GLuint * monitors);
typedef void (APIENTRY *glDeletePerfMonitorsAMDPROC) (GLsizei n, GLuint * monitors);
typedef void (APIENTRY *glSelectPerfMonitorCountersAMDPROC) (GLuint monitor, GLboolean enable, GLuint group, GLint numCounters, GLuint * counterList);
typedef void (APIENTRY *glBeginPerfMonitorAMDPROC) (GLuint monitor);
typedef void (APIENTRY *glEndPerfMonitorAMDPROC) (GLuint monitor);
typedef void (APIENTRY *glGetPerfMonitorCounterDataAMDPROC) (GLuint monitor, GLenum pname, GLsizei dataSize, GLuint * data, GLint * bytesWritten);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorGroupsAMD(JNIEnv *env, jclass clazz, jlong numGroups, jint groupsSize, jlong groups, jlong function_pointer) {
	GLint *numGroups_address = (GLint *)(intptr_t)numGroups;
	GLuint *groups_address = (GLuint *)(intptr_t)groups;
	glGetPerfMonitorGroupsAMDPROC glGetPerfMonitorGroupsAMD = (glGetPerfMonitorGroupsAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorGroupsAMD(numGroups_address, groupsSize, groups_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorCountersAMD(JNIEnv *env, jclass clazz, jint group, jlong numCounters, jlong maxActiveCounters, jint countersSize, jlong counters, jlong function_pointer) {
	GLint *numCounters_address = (GLint *)(intptr_t)numCounters;
	GLint *maxActiveCounters_address = (GLint *)(intptr_t)maxActiveCounters;
	GLuint *counters_address = (GLuint *)(intptr_t)counters;
	glGetPerfMonitorCountersAMDPROC glGetPerfMonitorCountersAMD = (glGetPerfMonitorCountersAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorCountersAMD(group, numCounters_address, maxActiveCounters_address, countersSize, counters_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorGroupStringAMD(JNIEnv *env, jclass clazz, jint group, jint bufSize, jlong length, jlong groupString, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *groupString_address = (GLchar *)(intptr_t)groupString;
	glGetPerfMonitorGroupStringAMDPROC glGetPerfMonitorGroupStringAMD = (glGetPerfMonitorGroupStringAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorGroupStringAMD(group, bufSize, length_address, groupString_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorCounterStringAMD(JNIEnv *env, jclass clazz, jint group, jint counter, jint bufSize, jlong length, jlong counterString, jlong function_pointer) {
	GLsizei *length_address = (GLsizei *)(intptr_t)length;
	GLchar *counterString_address = (GLchar *)(intptr_t)counterString;
	glGetPerfMonitorCounterStringAMDPROC glGetPerfMonitorCounterStringAMD = (glGetPerfMonitorCounterStringAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorCounterStringAMD(group, counter, bufSize, length_address, counterString_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorCounterInfoAMD(JNIEnv *env, jclass clazz, jint group, jint counter, jint pname, jlong data, jlong function_pointer) {
	GLvoid *data_address = (GLvoid *)(intptr_t)data;
	glGetPerfMonitorCounterInfoAMDPROC glGetPerfMonitorCounterInfoAMD = (glGetPerfMonitorCounterInfoAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorCounterInfoAMD(group, counter, pname, data_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGenPerfMonitorsAMD(JNIEnv *env, jclass clazz, jint n, jlong monitors, jlong function_pointer) {
	GLuint *monitors_address = (GLuint *)(intptr_t)monitors;
	glGenPerfMonitorsAMDPROC glGenPerfMonitorsAMD = (glGenPerfMonitorsAMDPROC)((intptr_t)function_pointer);
	glGenPerfMonitorsAMD(n, monitors_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglDeletePerfMonitorsAMD(JNIEnv *env, jclass clazz, jint n, jlong monitors, jlong function_pointer) {
	GLuint *monitors_address = (GLuint *)(intptr_t)monitors;
	glDeletePerfMonitorsAMDPROC glDeletePerfMonitorsAMD = (glDeletePerfMonitorsAMDPROC)((intptr_t)function_pointer);
	glDeletePerfMonitorsAMD(n, monitors_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglSelectPerfMonitorCountersAMD(JNIEnv *env, jclass clazz, jint monitor, jboolean enable, jint group, jint numCounters, jlong counterList, jlong function_pointer) {
	GLuint *counterList_address = (GLuint *)(intptr_t)counterList;
	glSelectPerfMonitorCountersAMDPROC glSelectPerfMonitorCountersAMD = (glSelectPerfMonitorCountersAMDPROC)((intptr_t)function_pointer);
	glSelectPerfMonitorCountersAMD(monitor, enable, group, numCounters, counterList_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglBeginPerfMonitorAMD(JNIEnv *env, jclass clazz, jint monitor, jlong function_pointer) {
	glBeginPerfMonitorAMDPROC glBeginPerfMonitorAMD = (glBeginPerfMonitorAMDPROC)((intptr_t)function_pointer);
	glBeginPerfMonitorAMD(monitor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglEndPerfMonitorAMD(JNIEnv *env, jclass clazz, jint monitor, jlong function_pointer) {
	glEndPerfMonitorAMDPROC glEndPerfMonitorAMD = (glEndPerfMonitorAMDPROC)((intptr_t)function_pointer);
	glEndPerfMonitorAMD(monitor);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_AMDPerformanceMonitor_nglGetPerfMonitorCounterDataAMD(JNIEnv *env, jclass clazz, jint monitor, jint pname, jint dataSize, jlong data, jlong bytesWritten, jlong function_pointer) {
	GLuint *data_address = (GLuint *)(intptr_t)data;
	GLint *bytesWritten_address = (GLint *)(intptr_t)bytesWritten;
	glGetPerfMonitorCounterDataAMDPROC glGetPerfMonitorCounterDataAMD = (glGetPerfMonitorCounterDataAMDPROC)((intptr_t)function_pointer);
	glGetPerfMonitorCounterDataAMD(monitor, pname, dataSize, data_address, bytesWritten_address);
}

