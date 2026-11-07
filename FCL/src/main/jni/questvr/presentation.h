#ifndef FCL_QUEST_VR_PRESENTATION_H
#define FCL_QUEST_VR_PRESENTATION_H

#include <stdbool.h>
#include <stdint.h>
#include <jni.h>

struct ANativeWindow;

typedef struct {
    struct ANativeWindow *window;
    uint64_t generation;
    bool immersive;
} quest_vr_presentation;

bool quest_vr_get_presentation(quest_vr_presentation *presentation);
void quest_vr_set_flat_surface(JNIEnv *env, jobject surface);
void quest_vr_notify_game_frame(void);
void quest_vr_notify_flat_frame(void);
void quest_vr_notify_openxr_stopped(void);
void quest_vr_openxr_instance_created(void);
void quest_vr_openxr_instance_destroyed(void);
void quest_vr_openxr_create_failed(void);

#endif
