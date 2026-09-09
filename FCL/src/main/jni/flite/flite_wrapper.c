// fliteWrapper shim：text2speech 1.13.9 及更早（MC 1.20.1-）经 JNA 调用的接口，
// 直接转发 libflite 的桥接实现
#include <jni.h>

extern int fcl_flite_init(void);
extern float fcl_flite_say(const char *text);

JNIEXPORT jint JNICALL init(void) {
    return fcl_flite_init();
}

JNIEXPORT jfloat JNICALL say(const char *text) {
    return fcl_flite_say(text);
}
