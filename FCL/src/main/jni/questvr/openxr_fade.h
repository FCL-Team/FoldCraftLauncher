#ifndef FCL_QUEST_VR_OPENXR_FADE_H
#define FCL_QUEST_VR_OPENXR_FADE_H

#include "openxr/openxr.h"
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#define QUEST_VR_FADE_DURATION_NS UINT64_C(300000000)
typedef struct {
    bool started, complete;
    XrTime first_display_time;
} quest_vr_fade_state;

typedef struct {
    XrFrameEndInfo info;
    const XrCompositionLayerBaseHeader **layers;
    XrCompositionLayerProjection *projections;
    XrCompositionLayerColorScaleBiasKHR *colors;
} quest_vr_fade_frame;

static bool quest_vr_fade_eligible(const XrCompositionLayerBaseHeader *base) {
    if (!base || base->type != XR_TYPE_COMPOSITION_LAYER_PROJECTION) return false;
    const XrCompositionLayerProjection *layer = (const void *) base;
    if (!layer->viewCount || !layer->views) return false;
    for (const XrBaseInStructure *next = layer->next; next; next = next->next)
        if (next->type == XR_TYPE_COMPOSITION_LAYER_COLOR_SCALE_BIAS_KHR) return false;
    return true;
}

static void quest_vr_fade_release(quest_vr_fade_frame *frame) {
    free(frame->layers);
    free(frame->projections);
    free(frame->colors);
    memset(frame, 0, sizeof(*frame));
}

static bool quest_vr_fade_prepare(quest_vr_fade_state *state, const XrFrameEndInfo *info,
                                  quest_vr_fade_frame *frame) {
    memset(frame, 0, sizeof(*frame));
    if (state->complete || !info || !info->layerCount || !info->layers) return false;
    bool eligible = false;
    for (uint32_t i = 0; i < info->layerCount; ++i)
        if (quest_vr_fade_eligible(info->layers[i])) { eligible = true; break; }
    if (!eligible) return false;
    uint64_t elapsed = state->started && info->displayTime > state->first_display_time
            ? (uint64_t) info->displayTime - (uint64_t) state->first_display_time : 0;
    if (elapsed >= QUEST_VR_FADE_DURATION_NS) { state->complete = true; return false; }
    frame->layers = calloc(info->layerCount, sizeof(*frame->layers));
    frame->projections = calloc(info->layerCount, sizeof(*frame->projections));
    frame->colors = calloc(info->layerCount, sizeof(*frame->colors));
    if (!frame->layers || !frame->projections || !frame->colors) {
        quest_vr_fade_release(frame);
        return false;
    }
    if (!state->started) { state->started = true; state->first_display_time = info->displayTime; }
    float t = (float) elapsed / (float) QUEST_VR_FADE_DURATION_NS;
    float brightness = t * t * (3.0f - 2.0f * t);
    frame->info = *info;
    frame->info.layers = frame->layers;
    for (uint32_t i = 0; i < info->layerCount; ++i) {
        frame->layers[i] = info->layers[i];
        if (!quest_vr_fade_eligible(info->layers[i])) continue;
        frame->projections[i] = *(const XrCompositionLayerProjection *) info->layers[i];
        frame->colors[i] = (XrCompositionLayerColorScaleBiasKHR) {
            .type = XR_TYPE_COMPOSITION_LAYER_COLOR_SCALE_BIAS_KHR,
            .next = frame->projections[i].next,
            .colorScale = {brightness, brightness, brightness, 1.0f},
            .colorBias = {0.0f, 0.0f, 0.0f, 0.0f}
        };
        frame->projections[i].next = &frame->colors[i];
        frame->layers[i] = (const void *) &frame->projections[i];
    }
    return true;
}

#endif
