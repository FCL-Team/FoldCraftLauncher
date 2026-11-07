#ifndef FCL_QUEST_VR_H
#define FCL_QUEST_VR_H

#include <stdbool.h>
#include "ctxbridges/gl_bridge.h"

bool quest_vr_enabled(void);
bool quest_vr_init(void);
gl_render_window_t *quest_vr_init_context(gl_render_window_t *share);
gl_render_window_t *quest_vr_get_current(void);
void quest_vr_make_current(gl_render_window_t *bundle);
void quest_vr_swap_buffers(void);
void quest_vr_setup_window(void);
void quest_vr_swap_interval(int interval);
void quest_vr_terminate(void);

#endif
