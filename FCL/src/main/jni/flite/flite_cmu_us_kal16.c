// flite_cmu_us_kal16 shim：text2speech 1.17.9+ 仅要求该符号返回非空指针作为 voice，
// 语音合成实际全部走 libflite 的桥接实现
#include <jni.h>

static int dummy_voice;

JNIEXPORT void *JNICALL register_cmu_us_kal16(const char *dir) {
    (void) dir;
    return &dummy_voice;
}
