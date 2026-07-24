#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>

#define FALSE 0
#define TRUE 1

typedef unsigned char u_char;
typedef unsigned int UINT32;
typedef int snd_pcm_stream_t;
typedef int snd_pcm_access_t;
typedef int snd_pcm_format_t;
typedef int snd_pcm_state_t;
typedef int snd_mixer_selem_channel_id_t;
typedef long snd_pcm_sframes_t;
typedef unsigned int snd_pcm_uframes_t;

typedef struct _snd_pcm snd_pcm_t;
typedef struct _snd_ctl snd_ctl_t;
typedef struct _snd_mixer snd_mixer_t;
typedef struct _snd_mixer_elem snd_mixer_elem_t;
typedef struct _snd_mixer_selem_id snd_mixer_selem_id_t;
typedef struct _snd_hctl snd_hctl_t;
typedef struct _snd_seq snd_seq_t;
typedef struct _snd_rawmidi snd_rawmidi_t;

typedef struct {
    unsigned int device;
    unsigned int subdevice;
    int stream;
    int card;
    unsigned int subdevices_count;
    unsigned int subdevices_avail;
    char id[64];
    char name[256];
    char subname[256];
} snd_pcm_info_t;

typedef struct {
    char id[64];
    char name[256];
    char driver[64];
} snd_ctl_card_info_t;

typedef struct {
    unsigned int flags;
} snd_pcm_hw_params_t;

typedef struct {
    unsigned int flags;
} snd_pcm_sw_params_t;

static const char* alsa_error_str(int err) {
    switch (err) {
        case 0: return "Success";
        case -1: return "Operation not permitted";
        case -2: return "No such file or directory";
        case -5: return "Input/output error";
        case -16: return "Device or resource busy";
        case -19: return "No such device";
        case -22: return "Invalid argument";
        case -27: return "File too large";
        case -77: return "Function not implemented";
        case -84: return "Invalid value";
        case -99: return "Address family not supported";
        case -114: return "Operation already in progress";
        case -115: return "Operation in progress";
        case -118: return "No data available";
        default: return "Unknown ALSA error";
    }
}

int snd_strerror(int err) {
    printf("ALSA stub: snd_strerror(%d) called\n", err);
    return -1;
}

const char* snd_strerror_pointer(int err) {
    return alsa_error_str(err);
}

typedef void (*snd_lib_error_handler_t)(const char *file, int line, const char *function, int err, const char *fmt, ...);

static void default_error_handler(const char *file, int line, const char *function, int err, const char *fmt, ...) {
    printf("ALSA lib %s:%d:(%s) error %d: %s\n", file, line, function, err, snd_strerror_pointer(err));
}

snd_lib_error_handler_t snd_lib_error_set_handler(snd_lib_error_handler_t handler) {
    printf("ALSA stub: snd_lib_error_set_handler(%p)\n", (void*)handler);
    return NULL;
}

int snd_pcm_open(snd_pcm_t **pcm, const char *name, snd_pcm_stream_t stream, int mode) {
    printf("ALSA stub: snd_pcm_open(%s, stream=%d, mode=%d)\n", name, stream, mode);
    *pcm = NULL;
    return -19;
}

int snd_pcm_close(snd_pcm_t *pcm) {
    printf("ALSA stub: snd_pcm_close(%p)\n", (void*)pcm);
    return 0;
}

int snd_pcm_info(snd_pcm_t *pcm, snd_pcm_info_t *info) {
    printf("ALSA stub: snd_pcm_info(%p)\n", (void*)pcm);
    return -19;
}

int snd_pcm_info_malloc(snd_pcm_info_t **ptr) {
    printf("ALSA stub: snd_pcm_info_malloc\n");
    *ptr = (snd_pcm_info_t*)calloc(1, sizeof(snd_pcm_info_t));
    return *ptr ? 0 : -12;
}

void snd_pcm_info_free(snd_pcm_info_t *obj) {
    printf("ALSA stub: snd_pcm_info_free\n");
    free(obj);
}

void snd_pcm_info_set_device(snd_pcm_info_t *obj, unsigned int val) {
    obj->device = val;
}

void snd_pcm_info_set_subdevice(snd_pcm_info_t *obj, unsigned int val) {
    obj->subdevice = val;
}

void snd_pcm_info_set_stream(snd_pcm_info_t *obj, int val) {
    obj->stream = val;
}

int snd_pcm_info_get_card(const snd_pcm_info_t *obj) {
    return obj->card;
}

unsigned int snd_pcm_info_get_subdevices_count(const snd_pcm_info_t *obj) {
    return 0;
}

unsigned int snd_pcm_info_get_subdevices_avail(const snd_pcm_info_t *obj) {
    return 0;
}

const char* snd_pcm_info_get_id(const snd_pcm_info_t *obj) {
    return "default";
}

const char* snd_pcm_info_get_name(const snd_pcm_info_t *obj) {
    return "ALSA stub default device";
}

const char* snd_pcm_info_get_subdevice_name(const snd_pcm_info_t *obj) {
    return "";
}

int snd_pcm_info_get_stream(const snd_pcm_info_t *obj) {
    return obj->stream;
}

int snd_ctl_open(snd_ctl_t **handle, const char *name, int mode) {
    printf("ALSA stub: snd_ctl_open(%s)\n", name);
    *handle = NULL;
    return -19;
}

int snd_ctl_close(snd_ctl_t *handle) {
    printf("ALSA stub: snd_ctl_close(%p)\n", (void*)handle);
    return 0;
}

int snd_ctl_card_info_malloc(snd_ctl_card_info_t **ptr) {
    printf("ALSA stub: snd_ctl_card_info_malloc\n");
    *ptr = (snd_ctl_card_info_t*)calloc(1, sizeof(snd_ctl_card_info_t));
    return *ptr ? 0 : -12;
}

void snd_ctl_card_info_free(snd_ctl_card_info_t *obj) {
    printf("ALSA stub: snd_ctl_card_info_free\n");
    free(obj);
}

int snd_ctl_card_info(snd_ctl_t *handle, snd_ctl_card_info_t *info) {
    printf("ALSA stub: snd_ctl_card_info(%p)\n", (void*)handle);
    return -19;
}

const char* snd_ctl_card_info_get_id(const snd_ctl_card_info_t *obj) {
    return "stub";
}

const char* snd_ctl_card_info_get_name(const snd_ctl_card_info_t *obj) {
    return "ALSA Stub Card";
}

int snd_ctl_pcm_info(snd_ctl_t *handle, snd_pcm_info_t *info) {
    printf("ALSA stub: snd_ctl_pcm_info(%p)\n", (void*)handle);
    return -2;
}

int snd_ctl_pcm_next_device(snd_ctl_t *handle, int *device) {
    printf("ALSA stub: snd_ctl_pcm_next_device\n");
    *device = -1;
    return 0;
}

int snd_ctl_rawmidi_next_device(snd_ctl_t *handle, int *device) {
    *device = -1;
    return 0;
}

int snd_card_next(int *card) {
    printf("ALSA stub: snd_card_next\n");
    *card = -1;
    return 0;
}

int snd_pcm_hw_params_malloc(snd_pcm_hw_params_t **ptr) {
    printf("ALSA stub: snd_pcm_hw_params_malloc\n");
    *ptr = (snd_pcm_hw_params_t*)calloc(1, sizeof(snd_pcm_hw_params_t));
    return *ptr ? 0 : -12;
}

void snd_pcm_hw_params_free(snd_pcm_hw_params_t *obj) {
    printf("ALSA stub: snd_pcm_hw_params_free\n");
    free(obj);
}

int snd_pcm_hw_params_any(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    printf("ALSA stub: snd_pcm_hw_params_any\n");
    return -19;
}

int snd_pcm_hw_params_set_access(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_access_t access) {
    return -19;
}

int snd_pcm_hw_params_set_format(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_format_t val) {
    return -19;
}

int snd_pcm_hw_params_set_channels(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int val) {
    return -19;
}

int snd_pcm_hw_params_set_rate_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_set_rate_resample(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int val) {
    return -19;
}

int snd_pcm_hw_params_set_periods_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_set_buffer_size_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_uframes_t *val) {
    return -19;
}

int snd_pcm_hw_params_set_buffer_time_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_set_period_time_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    return -19;
}

int snd_pcm_hw_params_current(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    return -19;
}

int snd_pcm_hw_params_get_access(const snd_pcm_hw_params_t *params, snd_pcm_access_t *access) {
    return -19;
}

int snd_pcm_hw_params_get_format(const snd_pcm_hw_params_t *params, snd_pcm_format_t *format) {
    return -19;
}

int snd_pcm_hw_params_get_channels(const snd_pcm_hw_params_t *params, unsigned int *val) {
    return -19;
}

int snd_pcm_hw_params_get_rate(const snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_get_period_size(const snd_pcm_hw_params_t *params, snd_pcm_uframes_t *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_get_buffer_size(const snd_pcm_hw_params_t *params, snd_pcm_uframes_t *val) {
    return -19;
}

int snd_pcm_hw_params_get_sbits(const snd_pcm_hw_params_t *params) {
    return -19;
}

int snd_pcm_hw_params_get_buffer_time(const snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_get_period_time(const snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    return -19;
}

int snd_pcm_hw_params_can_resume(const snd_pcm_hw_params_t *params) {
    return 0;
}

int snd_pcm_sw_params_malloc(snd_pcm_sw_params_t **ptr) {
    printf("ALSA stub: snd_pcm_sw_params_malloc\n");
    *ptr = (snd_pcm_sw_params_t*)calloc(1, sizeof(snd_pcm_sw_params_t));
    return *ptr ? 0 : -12;
}

void snd_pcm_sw_params_free(snd_pcm_sw_params_t *obj) {
    printf("ALSA stub: snd_pcm_sw_params_free\n");
    free(obj);
}

int snd_pcm_sw_params_current(snd_pcm_t *pcm, snd_pcm_sw_params_t *params) {
    return -19;
}

int snd_pcm_sw_params_set_avail_min(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, snd_pcm_uframes_t val) {
    return -19;
}

int snd_pcm_sw_params_set_start_threshold(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, snd_pcm_uframes_t val) {
    return -19;
}

int snd_pcm_sw_params_set_stop_threshold(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, snd_pcm_uframes_t val) {
    return -19;
}

int snd_pcm_sw_params_set_silence_threshold(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, snd_pcm_uframes_t val) {
    return -19;
}

int snd_pcm_sw_params_set_silence_size(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, snd_pcm_uframes_t val) {
    return -19;
}

int snd_pcm_sw_params_get_silence_threshold(const snd_pcm_sw_params_t *params, snd_pcm_uframes_t *val) {
    return -19;
}

int snd_pcm_sw_params_get_silence_size(const snd_pcm_sw_params_t *params, snd_pcm_uframes_t *val) {
    return -19;
}

int snd_pcm_sw_params_get_boundary(const snd_pcm_sw_params_t *params, snd_pcm_uframes_t *val) {
    return -19;
}

int snd_pcm_sw_params_set_period_event(snd_pcm_t *pcm, snd_pcm_sw_params_t *params, int val) {
    return -19;
}

int snd_pcm_sw_params(snd_pcm_t *pcm, snd_pcm_sw_params_t *params) {
    return -19;
}

int snd_pcm_prepare(snd_pcm_t *pcm) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_writei(snd_pcm_t *pcm, const void *buffer, snd_pcm_uframes_t size) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_readi(snd_pcm_t *pcm, void *buffer, snd_pcm_uframes_t size) {
    return -19;
}

int snd_pcm_drain(snd_pcm_t *pcm) {
    return -19;
}

int snd_pcm_drop(snd_pcm_t *pcm) {
    return -19;
}

int snd_pcm_recover(snd_pcm_t *pcm, int err, int silent) {
    return -19;
}

snd_pcm_state_t snd_pcm_state(snd_pcm_t *pcm) {
    return 0;
}

snd_pcm_sframes_t snd_pcm_avail_update(snd_pcm_t *pcm) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_rewind(snd_pcm_t *pcm, snd_pcm_uframes_t frames) {
    return -19;
}

int snd_pcm_resume(snd_pcm_t *pcm) {
    return -19;
}

int snd_pcm_start(snd_pcm_t *pcm) {
    return -19;
}

int snd_pcm_nonblock(snd_pcm_t *pcm, int nonblock) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_avail(snd_pcm_t *pcm) {
    return -19;
}

int snd_pcm_delay(snd_pcm_t *pcm, snd_pcm_sframes_t *delay) {
    return -19;
}

int snd_pcm_wait(snd_pcm_t *pcm, int timeout) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_mmap_begin(snd_pcm_t *pcm, const void **areas, snd_pcm_uframes_t *offset, snd_pcm_uframes_t *frames) {
    return -19;
}

snd_pcm_sframes_t snd_pcm_mmap_commit(snd_pcm_t *pcm, snd_pcm_uframes_t offset, snd_pcm_uframes_t frames) {
    return -19;
}

int snd_pcm_format_physical_width(snd_pcm_format_t format) {
    return 16;
}

int snd_pcm_format_width(snd_pcm_format_t format) {
    return 16;
}

int snd_pcm_format_signed(snd_pcm_format_t format) {
    return 1;
}

int snd_pcm_format_big_endian(snd_pcm_format_t format) {
    return 0;
}

int snd_pcm_format_linear(snd_pcm_format_t format) {
    return 1;
}

size_t snd_pcm_format_size(snd_pcm_format_t format, size_t samples) {
    return samples * 2;
}

snd_pcm_format_t snd_pcm_build_linear_format(int width, int pwidth, int unsignd, int big_endian) {
    return 0;
}

int snd_pcm_link(snd_pcm_t *pcm1, snd_pcm_t *pcm2) {
    return -19;
}

int snd_pcm_unlink(snd_pcm_t *pcm) {
    return -19;
}

int snd_mixer_open(snd_mixer_t **mixer, int mode) {
    printf("ALSA stub: snd_mixer_open\n");
    *mixer = NULL;
    return -19;
}

int snd_mixer_close(snd_mixer_t *mixer) {
    return 0;
}

int snd_mixer_attach(snd_mixer_t *mixer, const char *name) {
    return -19;
}

int snd_mixer_detach(snd_mixer_t *mixer, const char *name) {
    return 0;
}

int snd_mixer_selem_register(snd_mixer_t *mixer, void *options, void *classp) {
    return -19;
}

int snd_mixer_load(snd_mixer_t *mixer) {
    return -19;
}

void snd_mixer_set_callback(snd_mixer_t *obj, void *val) {}

void snd_mixer_elem_set_callback(snd_mixer_elem_t *obj, void *val) {}

snd_mixer_elem_t* snd_mixer_first_elem(snd_mixer_t *mixer) {
    return NULL;
}

snd_mixer_elem_t* snd_mixer_last_elem(snd_mixer_t *mixer) {
    return NULL;
}

snd_mixer_elem_t* snd_mixer_elem_next(snd_mixer_elem_t *elem) {
    return NULL;
}

snd_mixer_elem_t* snd_mixer_elem_prev(snd_mixer_elem_t *elem) {
    return NULL;
}

int snd_mixer_handle_events(snd_mixer_t *mixer) {
    return -19;
}

int snd_mixer_selem_id_malloc(snd_mixer_selem_id_t **ptr) {
    printf("ALSA stub: snd_mixer_selem_id_malloc\n");
    *ptr = (snd_mixer_selem_id_t*)calloc(1, sizeof(snd_mixer_selem_id_t));
    return *ptr ? 0 : -12;
}

void snd_mixer_selem_id_free(snd_mixer_selem_id_t *obj) {
    free(obj);
}

void snd_mixer_selem_id_set_name(snd_mixer_selem_id_t *obj, const char *val) {}

const char* snd_mixer_selem_id_get_name(snd_mixer_selem_id_t *obj) {
    return "";
}

void snd_mixer_selem_id_set_index(snd_mixer_selem_id_t *obj, unsigned int val) {}

unsigned int snd_mixer_selem_id_get_index(snd_mixer_selem_id_t *obj) {
    return 0;
}

const char* snd_mixer_selem_get_name(snd_mixer_elem_t *elem) {
    return "";
}

unsigned int snd_mixer_selem_get_index(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_is_active(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_has_playback_switch(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_has_capture_switch(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_get_playback_switch(snd_mixer_elem_t *elem, int channel, int *value) {
    return -19;
}

int snd_mixer_selem_get_capture_switch(snd_mixer_elem_t *elem, int channel, int *value) {
    return -19;
}

int snd_mixer_selem_get_playback_volume(snd_mixer_elem_t *elem, snd_mixer_selem_channel_id_t channel, long *value) {
    return -19;
}

int snd_mixer_selem_get_capture_volume(snd_mixer_elem_t *elem, snd_mixer_selem_channel_id_t channel, long *value) {
    return -19;
}

int snd_mixer_selem_set_playback_volume_all(snd_mixer_elem_t *elem, long value) {
    return -19;
}

int snd_mixer_selem_set_capture_volume_all(snd_mixer_elem_t *elem, long value) {
    return -19;
}

int snd_mixer_selem_get_playback_volume_range(snd_mixer_elem_t *elem, long *min, long *max) {
    return -19;
}

int snd_mixer_selem_get_capture_volume_range(snd_mixer_elem_t *elem, long *min, long *max) {
    return -19;
}

int snd_mixer_selem_set_playback_volume_range(snd_mixer_elem_t *elem, long min, long max) {
    return -19;
}

int snd_mixer_selem_set_capture_volume_range(snd_mixer_elem_t *elem, long min, long max) {
    return -19;
}

int snd_mixer_selem_get_playback_dB(snd_mixer_elem_t *elem, snd_mixer_selem_channel_id_t channel, long *value) {
    return -19;
}

int snd_mixer_selem_get_capture_dB(snd_mixer_elem_t *elem, snd_mixer_selem_channel_id_t channel, long *value) {
    return -19;
}

int snd_mixer_selem_set_playback_dB_all(snd_mixer_elem_t *elem, long value, int dir) {
    return -19;
}

int snd_mixer_selem_set_capture_dB_all(snd_mixer_elem_t *elem, long value, int dir) {
    return -19;
}

int snd_mixer_selem_get_playback_dB_range(snd_mixer_elem_t *elem, long *min, long *max) {
    return -19;
}

int snd_mixer_selem_get_capture_dB_range(snd_mixer_elem_t *elem, long *min, long *max) {
    return -19;
}

int snd_mixer_selem_has_playback_volume(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_has_capture_volume(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_has_playback_volume_joined(snd_mixer_elem_t *elem) {
    return 1;
}

int snd_mixer_selem_has_capture_volume_joined(snd_mixer_elem_t *elem) {
    return 1;
}

int snd_mixer_selem_has_playback_switch_joined(snd_mixer_elem_t *elem) {
    return 1;
}

int snd_mixer_selem_has_capture_switch_joined(snd_mixer_elem_t *elem) {
    return 1;
}

int snd_mixer_selem_set_playback_switch_all(snd_mixer_elem_t *elem, int value) {
    return -19;
}

int snd_mixer_selem_set_capture_switch_all(snd_mixer_elem_t *elem, int value) {
    return -19;
}

int snd_mixer_selem_get_playback_switch_switch(snd_mixer_elem_t *elem, int channel, int *value) {
    return -19;
}

int snd_mixer_selem_has_capture_switch_switch(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_get_capture_switch_switch(snd_mixer_elem_t *elem, int channel, int *value) {
    return -19;
}

int snd_mixer_selem_get_type(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_get_count(snd_mixer_elem_t *elem) {
    return 0;
}

int snd_mixer_selem_channel_id(snd_mixer_selem_channel_id_t channel) {
    return (int)channel;
}

int snd_seq_open(snd_seq_t **seq, const char *name, int streams, int mode) {
    printf("ALSA stub: snd_seq_open(%s)\n", name);
    *seq = NULL;
    return -19;
}

int snd_seq_close(snd_seq_t *seq) {
    return 0;
}

int snd_seq_set_client_name(snd_seq_t *seq, const char *name) {
    return -19;
}

int snd_seq_create_simple_port(snd_seq_t *seq, const char *name, unsigned int caps, unsigned int type) {
    return -19;
}

int snd_seq_connect_from(snd_seq_t *seq, int my_port, int src_client, int src_port) {
    return -19;
}

int snd_seq_connect_to(snd_seq_t *seq, int my_port, int dst_client, int dst_port) {
    return -19;
}

int snd_seq_alloc_named_queue(snd_seq_t *seq, const char *name) {
    return -19;
}

int snd_seq_alloc_queue(snd_seq_t *seq) {
    return -19;
}

int snd_seq_free_queue(snd_seq_t *seq, int q) {
    return 0;
}

int snd_seq_set_tempo(snd_seq_t *seq, int q, int tempo) {
    return -19;
}

int snd_seq_drain_output(snd_seq_t *seq) {
    return -19;
}

int snd_seq_sync_output_queue(snd_seq_t *seq) {
    return -19;
}

int snd_seq_event_output(snd_seq_t *seq, void *ev) {
    return -19;
}

int snd_seq_event_input(snd_seq_t *seq, void **ev) {
    return -19;
}

int snd_seq_free_event(void *ev) {
    return 0;
}

int snd_seq_event_input_pending(snd_seq_t *seq, int fetch) {
    return 0;
}

int snd_rawmidi_open(snd_rawmidi_t **in, snd_rawmidi_t **out, const char *name, int mode) {
    printf("ALSA stub: snd_rawmidi_open(%s)\n", name);
    *in = NULL;
    *out = NULL;
    return -19;
}

int snd_rawmidi_close(snd_rawmidi_t *sub) {
    return 0;
}

int snd_rawmidi_read(snd_rawmidi_t *in, void *buf, size_t size) {
    return -19;
}

int snd_rawmidi_write(snd_rawmidi_t *out, const void *buf, size_t size) {
    return -19;
}

int snd_rawmidi_drain(snd_rawmidi_t *out) {
    return 0;
}

int snd_rawmidi_nonblock(snd_rawmidi_t *rm, int nonblock) {
    return -19;
}

int snd_rawmidi_params(snd_rawmidi_t *rm, void *params) {
    return -19;
}

unsigned int snd_rawmidi_count(snd_rawmidi_t *rm) {
    return 0;
}

int snd_rawmidi_params_malloc(void **ptr) {
    *ptr = calloc(1, 64);
    return *ptr ? 0 : -12;
}

void snd_rawmidi_params_free(void *obj) {
    free(obj);
}

int snd_rawmidi_params_set_buffer_size(snd_rawmidi_t *rm, void *params, unsigned long val) {
    return -19;
}

int snd_rawmidi_params_set_avail_min(snd_rawmidi_t *rm, void *params, unsigned long val) {
    return -19;
}

int snd_rawmidi_status_malloc(void **ptr) {
    *ptr = calloc(1, 64);
    return *ptr ? 0 : -12;
}

void snd_rawmidi_status_free(void *obj) {
    free(obj);
}

int snd_rawmidi_status(snd_rawmidi_t *rm, void *status) {
    return -19;
}

unsigned int snd_rawmidi_status_get_avail(void *status) {
    return 0;
}

int snd_rawmidi_nonblock_old(snd_rawmidi_t *rm, int nonblock) {
    return -19;
}

int snd_output_stdio_attach(void **output, FILE *fp, int close) {
    return 0;
}

int snd_output_close(void *output) {
    return 0;
}

int snd_config_update(void) {
    return 0;
}

int snd_config_update_free(void) {
    return 0;
}