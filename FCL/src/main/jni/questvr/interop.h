#ifndef FCL_QUEST_VR_INTEROP_H
#define FCL_QUEST_VR_INTEROP_H
#include <stdbool.h>
#include <stdint.h>
#include <stddef.h>

void quest_vr_register_images(uint64_t swapchain, const uint32_t *images, size_t count);
void quest_vr_forget_images(uint64_t swapchain);
bool quest_vr_is_runtime_texture(uint32_t texture);
bool quest_vr_is_mobileglues(void);

#endif
