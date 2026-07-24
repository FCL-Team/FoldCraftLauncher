#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <pthread.h>
#include <SLES/OpenSLES.h>
#include <SLES/AndroidOpenSLES.h>

#define FALSE 0
#define TRUE 1

typedef unsigned char u_char;
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
typedef struct _snd_mixer_selem_id { char name[64]; unsigned int index; } snd_mixer_selem_id_t;
typedef struct _snd_hctl snd_hctl_t;
typedef struct _snd_seq snd_seq_t;
typedef struct _snd_rawmidi snd_rawmidi_t;

#define SND_PCM_STREAM_PLAYBACK 0
#define SND_PCM_STREAM_CAPTURE 1
#define SND_PCM_ACCESS_RW_INTERLEAVED 3
#define SND_PCM_FORMAT_U8 1
#define SND_PCM_FORMAT_S16_LE 2
#define SND_PCM_FORMAT_S32_LE 10
#define SND_PCM_STATE_OPEN 0
#define SND_PCM_STATE_SETUP 1
#define SND_PCM_STATE_PREPARED 2
#define SND_PCM_STATE_RUNNING 3
#define SND_PCM_STATE_XRUN 4
#define SND_PCM_STATE_DRAINING 5
#define SND_PCM_STATE_PAUSED 6
#define SND_PCM_STATE_DISCONNECTED 8

typedef struct {
    unsigned int device; unsigned int subdevice; int stream; int card;
    unsigned int subdevices_count; unsigned int subdevices_avail;
    char id[64]; char name[256]; char subname[256];
} snd_pcm_info_t;

typedef struct { char id[64]; char name[256]; char driver[64]; } snd_ctl_card_info_t;

typedef struct {
    unsigned int rate; unsigned int channels; unsigned int buffer_frames;
    unsigned int period_frames; unsigned int periods;
    snd_pcm_format_t format; snd_pcm_access_t access;
    int bits; int sbits; int signed_fmt; int big_endian; int linear;
} snd_pcm_hw_params_t;

typedef struct { unsigned int flags; } snd_pcm_sw_params_t;

struct _snd_pcm {
    int stream; int state; char name[256];
    snd_pcm_hw_params_t hw;
    SLObjectItf engineObj; SLEngineItf engine;
    SLObjectItf outputMixObj; SLObjectItf playerObj;
    SLPlayItf player; SLAndroidSimpleBufferQueueItf bufferQueue;
    short *buffer; int buffer_size; int period_frames;
    int frame_size; int write_pos; int play_pos; int buffered_frames;
    pthread_mutex_t mutex; int engine_initialized;
};

static int g_engine_initialized = 0;
static SLObjectItf g_engineObj = NULL;
static SLEngineItf g_engine = NULL;
static SLObjectItf g_outputMixObj = NULL;
static pthread_mutex_t g_engine_mutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct {
    snd_pcm_format_t alsa_fmt; int bits; int sbits;
    int signed_fmt; int big_endian; int linear; SLuint32 sl_format;
} format_entry_t;

static format_entry_t format_table[] = {
    { SND_PCM_FORMAT_U8,      8,  8, 0, 0, 1, SL_DATAFORMAT_PCM_SUB_BITS_8 },
    { SND_PCM_FORMAT_S16_LE, 16, 16, 1, 0, 1, SL_DATAFORMAT_PCM_SUB_BITS_16 },
    { SND_PCM_FORMAT_S32_LE, 32, 32, 1, 0, 1, SL_DATAFORMAT_PCM_SUB_BITS_32 },
    { -1, 0, 0, 0, 0, 0, 0 },
};

static format_entry_t* find_format(snd_pcm_format_t fmt) {
    for (int i = 0; format_table[i].alsa_fmt >= 0; i++)
        if (format_table[i].alsa_fmt == fmt) return &format_table[i];
    return NULL;
}

static const char* alsa_error_str(int err) {
    switch (err) {
        case 0: return "Success";
        case -1: return "Operation not permitted";
        case -2: return "No such file or directory";
        case -5: return "Input/output error";
        case -12: return "Cannot allocate memory";
        case -16: return "Device or resource busy";
        case -19: return "No such device";
        case -22: return "Invalid argument";
        case -77: return "Function not implemented";
        case -84: return "Invalid value";
        default: return "Unknown ALSA error";
    }
}

static int init_engine(void) {
    pthread_mutex_lock(&g_engine_mutex);
    if (g_engine_initialized) { pthread_mutex_unlock(&g_engine_mutex); return 0; }
    SLresult r;
    r = slCreateEngine(&g_engineObj, 0, NULL, 0, NULL, NULL);
    if (r != SL_RESULT_SUCCESS) { pthread_mutex_unlock(&g_engine_mutex); return -5; }
    r = (*g_engineObj)->Realize(g_engineObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*g_engineObj)->Destroy(g_engineObj); g_engineObj = NULL; pthread_mutex_unlock(&g_engine_mutex); return -5; }
    r = (*g_engineObj)->GetInterface(g_engineObj, SL_IID_ENGINE, &g_engine);
    if (r != SL_RESULT_SUCCESS) { (*g_engineObj)->Destroy(g_engineObj); g_engineObj = NULL; pthread_mutex_unlock(&g_engine_mutex); return -5; }
    const SLInterfaceID ids[] = { SL_IID_VOLUME };
    const SLboolean req[] = { SL_BOOLEAN_FALSE };
    r = (*g_engine)->CreateOutputMix(g_engine, &g_outputMixObj, 1, ids, req);
    if (r != SL_RESULT_SUCCESS) { (*g_engineObj)->Destroy(g_engineObj); g_engineObj = NULL; g_engine = NULL; pthread_mutex_unlock(&g_engine_mutex); return -5; }
    r = (*g_outputMixObj)->Realize(g_outputMixObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*g_outputMixObj)->Destroy(g_outputMixObj); g_outputMixObj = NULL; (*g_engineObj)->Destroy(g_engineObj); g_engineObj = NULL; g_engine = NULL; pthread_mutex_unlock(&g_engine_mutex); return -5; }
    g_engine_initialized = 1;
    fprintf(stderr, "ALSA stub: OpenSL ES engine initialized\n");
    pthread_mutex_unlock(&g_engine_mutex);
    return 0;
}

static void buffer_queue_callback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    (void)bq; (void)context;
}

static int pcm_setup_player(snd_pcm_t *pcm) {
    if (pcm->playerObj) { (*pcm->playerObj)->Destroy(pcm->playerObj); pcm->playerObj = NULL; pcm->player = NULL; pcm->bufferQueue = NULL; }
    format_entry_t *fe = find_format(pcm->hw.format);
    if (!fe) return -22;
    pcm->frame_size = (pcm->hw.channels * fe->bits) / 8;
    if (pcm->frame_size == 0) pcm->frame_size = 2;
    pcm->buffer_size = pcm->hw.buffer_frames;
    pcm->period_frames = pcm->hw.period_frames;
    SLDataFormat_PCM fmt;
    fmt.formatType = SL_DATAFORMAT_PCM;
    fmt.numChannels = pcm->hw.channels;
    fmt.samplesPerSec = pcm->hw.rate * 1000;
    fmt.bitsPerSample = fe->bits;
    fmt.containerSize = fe->bits;
    fmt.channelMask = (pcm->hw.channels == 1) ? SL_SPEAKER_FRONT_CENTER : SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT;
    fmt.endianness = fe->big_endian ? SL_BYTEORDER_BIGENDIAN : SL_BYTEORDER_LITTLEENDIAN;
    SLDataLocator_AndroidSimpleBufferQueue loc_bufq = { SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 1 };
    SLDataSource audioSrc = { &loc_bufq, &fmt };
    SLDataLocator_OutputMix loc_outmix = { SL_DATALOCATOR_OUTPUTMIX, g_outputMixObj };
    SLDataSink audioSink = { &loc_outmix, NULL };
    SLInterfaceID ids[] = { SL_IID_ANDROIDSIMPLEBUFFERQUEUE };
    SLboolean req[] = { SL_BOOLEAN_TRUE };
    SLresult r = (*g_engine)->CreateAudioPlayer(g_engine, &pcm->playerObj, &audioSrc, &audioSink, 1, ids, req);
    if (r != SL_RESULT_SUCCESS) return -5;
    r = (*pcm->playerObj)->Realize(pcm->playerObj, SL_BOOLEAN_FALSE);
    if (r != SL_RESULT_SUCCESS) { (*pcm->playerObj)->Destroy(pcm->playerObj); pcm->playerObj = NULL; return -5; }
    r = (*pcm->playerObj)->GetInterface(pcm->playerObj, SL_IID_PLAY, &pcm->player);
    if (r != SL_RESULT_SUCCESS) return -5;
    r = (*pcm->playerObj)->GetInterface(pcm->playerObj, SL_IID_ANDROIDSIMPLEBUFFERQUEUE, &pcm->bufferQueue);
    if (r != SL_RESULT_SUCCESS) return -5;
    r = (*pcm->bufferQueue)->RegisterCallback(pcm->bufferQueue, buffer_queue_callback, pcm);
    if (r != SL_RESULT_SUCCESS) return -5;
    return 0;
}

int snd_strerror(int err) { return -1; }
const char* snd_strerror_pointer(int err) { return alsa_error_str(err); }
typedef void (*snd_lib_error_handler_t)(const char*,int,const char*,int,const char*,...);
snd_lib_error_handler_t snd_lib_error_set_handler(snd_lib_error_handler_t h) { return NULL; }

int snd_pcm_open(snd_pcm_t **pcm, const char *name, snd_pcm_stream_t stream, int mode) {
    if (!pcm) return -22;
    if (!g_engine_initialized) { int ret = init_engine(); if (ret) { *pcm = NULL; return ret; } }
    snd_pcm_t *p = (snd_pcm_t*)calloc(1, sizeof(snd_pcm_t));
    if (!p) return -12;
    pthread_mutex_init(&p->mutex, NULL);
    p->stream = stream; p->state = SND_PCM_STATE_OPEN;
    strncpy(p->name, name, sizeof(p->name) - 1);
    p->hw.rate = 44100; p->hw.channels = 2;
    p->hw.format = SND_PCM_FORMAT_S16_LE;
    p->hw.access = SND_PCM_ACCESS_RW_INTERLEAVED;
    p->hw.buffer_frames = 4096; p->hw.period_frames = 1024;
    p->hw.periods = 4; p->hw.bits = 16; p->hw.sbits = 16;
    p->hw.signed_fmt = 1; p->hw.linear = 1;
    p->engine_initialized = 1;
    *pcm = p;
    fprintf(stderr, "ALSA stub: snd_pcm_open(%s, stream=%d) = %p\n", name, stream, (void*)p);
    return 0;
}

int snd_pcm_close(snd_pcm_t *pcm) {
    if (!pcm) return 0;
    if (pcm->playerObj) {
        if (pcm->player) (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_STOPPED);
        (*pcm->playerObj)->Destroy(pcm->playerObj);
    }
    if (pcm->buffer) free(pcm->buffer);
    pthread_mutex_destroy(&pcm->mutex);
    free(pcm);
    return 0;
}

int snd_pcm_info(snd_pcm_t *pcm, snd_pcm_info_t *info) {
    if (!pcm || !info) return -22;
    info->card = 0; info->device = 0; info->subdevice = 0;
    info->stream = pcm->stream; info->subdevices_count = 1; info->subdevices_avail = 1;
    strcpy(info->id, "default");
    snprintf(info->name, sizeof(info->name), "Android Audio (%s)", pcm->name);
    strcpy(info->subname, "");
    return 0;
}

int snd_pcm_info_malloc(snd_pcm_info_t **ptr) { *ptr = (snd_pcm_info_t*)calloc(1, sizeof(snd_pcm_info_t)); return *ptr ? 0 : -12; }
void snd_pcm_info_free(snd_pcm_info_t *obj) { free(obj); }
void snd_pcm_info_set_device(snd_pcm_info_t *obj, unsigned int val) { obj->device = val; }
void snd_pcm_info_set_subdevice(snd_pcm_info_t *obj, unsigned int val) { obj->subdevice = val; }
void snd_pcm_info_set_stream(snd_pcm_info_t *obj, int val) { obj->stream = val; }
int snd_pcm_info_get_card(const snd_pcm_info_t *obj) { return obj->card; }
unsigned int snd_pcm_info_get_subdevices_count(const snd_pcm_info_t *obj) { return obj->subdevices_count; }
unsigned int snd_pcm_info_get_subdevices_avail(const snd_pcm_info_t *obj) { return obj->subdevices_avail; }
const char* snd_pcm_info_get_id(const snd_pcm_info_t *obj) { return obj->id; }
const char* snd_pcm_info_get_name(const snd_pcm_info_t *obj) { return obj->name; }
const char* snd_pcm_info_get_subdevice_name(const snd_pcm_info_t *obj) { return obj->subname; }
int snd_pcm_info_get_stream(const snd_pcm_info_t *obj) { return obj->stream; }

int snd_pcm_hw_params_malloc(snd_pcm_hw_params_t **ptr) { *ptr = (snd_pcm_hw_params_t*)calloc(1, sizeof(snd_pcm_hw_params_t)); return *ptr ? 0 : -12; }
void snd_pcm_hw_params_free(snd_pcm_hw_params_t *obj) { free(obj); }

int snd_pcm_hw_params_any(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    if (!params) return -22; memcpy(params, &pcm->hw, sizeof(snd_pcm_hw_params_t)); return 0;
}

int snd_pcm_hw_params_set_access(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_access_t access) {
    if (!params) return -22; params->access = access; return 0;
}

int snd_pcm_hw_params_set_format(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_format_t val) {
    if (!params) return -22;
    format_entry_t *fe = find_format(val);
    if (!fe) return -22;
    params->format = val; params->bits = fe->bits; params->sbits = fe->sbits;
    params->signed_fmt = fe->signed_fmt; params->big_endian = fe->big_endian; params->linear = fe->linear;
    return 0;
}

int snd_pcm_hw_params_set_channels(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int val) {
    if (!params) return -22; if (val < 1 || val > 2) return -22; params->channels = val; return 0;
}

int snd_pcm_hw_params_set_rate_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    if (!params || !val) return -22;
    unsigned int rates[] = { 8000, 11025, 16000, 22050, 32000, 44100, 48000, 96000 };
    unsigned int best = rates[0]; int best_diff = abs((int)*val - (int)rates[0]);
    for (int i = 1; i < 8; i++) { int d = abs((int)*val - (int)rates[i]); if (d < best_diff) { best_diff = d; best = rates[i]; } }
    params->rate = best; *val = best; if (dir) *dir = 0;
    return 0;
}

int snd_pcm_hw_params_set_rate_resample(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int val) { return 0; }

int snd_pcm_hw_params_set_periods_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    if (!params || !val) return -22;
    if (*val < 2) *val = 2; if (*val > 8) *val = 8;
    params->periods = *val; if (dir) *dir = 0;
    return 0;
}

int snd_pcm_hw_params_set_buffer_size_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, snd_pcm_uframes_t *val) {
    if (!params || !val) return -22; params->buffer_frames = *val; return 0;
}

int snd_pcm_hw_params_set_buffer_time_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    if (!params || !val) return -22;
    params->buffer_frames = (*val * params->rate) / 1000000; if (dir) *dir = 0;
    return 0;
}

int snd_pcm_hw_params_set_period_time_near(snd_pcm_t *pcm, snd_pcm_hw_params_t *params, unsigned int *val, int *dir) {
    if (!params || !val) return -22;
    params->period_frames = (*val * params->rate) / 1000000;
    if (params->period_frames < 64) params->period_frames = 64;
    if (params->period_frames > params->buffer_frames) params->period_frames = params->buffer_frames / 4;
    if (dir) *dir = 0;
    return 0;
}

int snd_pcm_hw_params(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    if (!pcm || !params) return -22;
    memcpy(&pcm->hw, params, sizeof(snd_pcm_hw_params_t));
    pcm->state = SND_PCM_STATE_SETUP;
    if (pcm->stream == SND_PCM_STREAM_PLAYBACK) { int ret = pcm_setup_player(pcm); if (ret) return ret; }
    int fs = (pcm->hw.channels * pcm->hw.bits) / 8; if (fs == 0) fs = 2;
    pcm->frame_size = fs; pcm->buffer_size = pcm->hw.buffer_frames;
    if (pcm->buffer) free(pcm->buffer);
    pcm->buffer = (short*)calloc(pcm->buffer_size, fs);
    if (!pcm->buffer) return -12;
    fprintf(stderr, "ALSA stub: hw_params: %dHz %dch fmt=%d buf=%d period=%d\n",
            pcm->hw.rate, pcm->hw.channels, pcm->hw.format, pcm->hw.buffer_frames, pcm->hw.period_frames);
    return 0;
}

int snd_pcm_hw_params_current(snd_pcm_t *pcm, snd_pcm_hw_params_t *params) {
    if (!pcm || !params) return -22; memcpy(params, &pcm->hw, sizeof(snd_pcm_hw_params_t)); return 0;
}

int snd_pcm_hw_params_get_access(const snd_pcm_hw_params_t *params, snd_pcm_access_t *a) { if (!params || !a) return -22; *a = params->access; return 0; }
int snd_pcm_hw_params_get_format(const snd_pcm_hw_params_t *params, snd_pcm_format_t *f) { if (!params || !f) return -22; *f = params->format; return 0; }
int snd_pcm_hw_params_get_channels(const snd_pcm_hw_params_t *params, unsigned int *v) { if (!params || !v) return -22; *v = params->channels; return 0; }
int snd_pcm_hw_params_get_rate(const snd_pcm_hw_params_t *params, unsigned int *v, int *d) { if (!params || !v) return -22; *v = params->rate; if (d) *d = 0; return 0; }
int snd_pcm_hw_params_get_period_size(const snd_pcm_hw_params_t *params, snd_pcm_uframes_t *v, int *d) { if (!params || !v) return -22; *v = params->period_frames; if (d) *d = 0; return 0; }
int snd_pcm_hw_params_get_buffer_size(const snd_pcm_hw_params_t *params, snd_pcm_uframes_t *v) { if (!params || !v) return -22; *v = params->buffer_frames; return 0; }
int snd_pcm_hw_params_get_sbits(const snd_pcm_hw_params_t *params) { return params ? params->sbits : 0; }
int snd_pcm_hw_params_get_buffer_time(const snd_pcm_hw_params_t *params, unsigned int *v, int *d) { if (!params || !v) return -22; *v = (params->buffer_frames * 1000000) / params->rate; if (d) *d = 0; return 0; }
int snd_pcm_hw_params_get_period_time(const snd_pcm_hw_params_t *params, unsigned int *v, int *d) { if (!params || !v) return -22; *v = (params->period_frames * 1000000) / params->rate; if (d) *d = 0; return 0; }
int snd_pcm_hw_params_can_resume(const snd_pcm_hw_params_t *params) { (void)params; return 0; }

int snd_pcm_sw_params_malloc(snd_pcm_sw_params_t **ptr) { *ptr = (snd_pcm_sw_params_t*)calloc(1, sizeof(snd_pcm_sw_params_t)); return *ptr ? 0 : -12; }
void snd_pcm_sw_params_free(snd_pcm_sw_params_t *obj) { free(obj); }
int snd_pcm_sw_params_current(snd_pcm_t *pcm, snd_pcm_sw_params_t *params) { return 0; }
int snd_pcm_sw_params_set_avail_min(snd_pcm_t *p, snd_pcm_sw_params_t *pp, snd_pcm_uframes_t v) { return 0; }
int snd_pcm_sw_params_set_start_threshold(snd_pcm_t *p, snd_pcm_sw_params_t *pp, snd_pcm_uframes_t v) { return 0; }
int snd_pcm_sw_params_set_stop_threshold(snd_pcm_t *p, snd_pcm_sw_params_t *pp, snd_pcm_uframes_t v) { return 0; }
int snd_pcm_sw_params_set_silence_threshold(snd_pcm_t *p, snd_pcm_sw_params_t *pp, snd_pcm_uframes_t v) { return 0; }
int snd_pcm_sw_params_set_silence_size(snd_pcm_t *p, snd_pcm_sw_params_t *pp, snd_pcm_uframes_t v) { return 0; }
int snd_pcm_sw_params_get_silence_threshold(const snd_pcm_sw_params_t *p, snd_pcm_uframes_t *v) { return 0; }
int snd_pcm_sw_params_get_silence_size(const snd_pcm_sw_params_t *p, snd_pcm_uframes_t *v) { return 0; }
int snd_pcm_sw_params_get_boundary(const snd_pcm_sw_params_t *p, snd_pcm_uframes_t *v) { return 0; }
int snd_pcm_sw_params_set_period_event(snd_pcm_t *p, snd_pcm_sw_params_t *pp, int v) { return 0; }
int snd_pcm_sw_params(snd_pcm_t *pcm, snd_pcm_sw_params_t *params) { return 0; }

int snd_pcm_prepare(snd_pcm_t *pcm) {
    if (!pcm) return -22;
    pthread_mutex_lock(&pcm->mutex);
    pcm->write_pos = 0; pcm->play_pos = 0; pcm->buffered_frames = 0;
    if (pcm->player && pcm->stream == SND_PCM_STREAM_PLAYBACK) {
        (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_STOPPED);
        (*pcm->bufferQueue)->Clear(pcm->bufferQueue);
    }
    pcm->state = SND_PCM_STATE_PREPARED;
    pthread_mutex_unlock(&pcm->mutex);
    return 0;
}

snd_pcm_sframes_t snd_pcm_writei(snd_pcm_t *pcm, const void *buffer, snd_pcm_uframes_t size) {
    if (!pcm || !buffer) return -22;
    pthread_mutex_lock(&pcm->mutex);
    if (pcm->state != SND_PCM_STATE_PREPARED && pcm->state != SND_PCM_STATE_RUNNING) { pthread_mutex_unlock(&pcm->mutex); return -22; }
    if (pcm->stream != SND_PCM_STREAM_PLAYBACK) { pthread_mutex_unlock(&pcm->mutex); return -22; }
    if (pcm->state == SND_PCM_STATE_PREPARED && pcm->player) {
        SLresult r = (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_PLAYING);
        if (r == SL_RESULT_SUCCESS) pcm->state = SND_PCM_STATE_RUNNING;
    }
    if (pcm->bufferQueue) {
        SLresult r = (*pcm->bufferQueue)->Enqueue(pcm->bufferQueue, buffer, size * pcm->frame_size);
        if (r != SL_RESULT_SUCCESS) { pthread_mutex_unlock(&pcm->mutex); return -5; }
        pcm->buffered_frames += size;
    }
    pthread_mutex_unlock(&pcm->mutex);
    return size;
}

snd_pcm_sframes_t snd_pcm_readi(snd_pcm_t *pcm, void *buffer, snd_pcm_uframes_t size) { (void)buffer; (void)size; return -19; }

int snd_pcm_drain(snd_pcm_t *pcm) {
    if (!pcm) return -22;
    if (pcm->player) (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_STOPPED);
    pcm->state = SND_PCM_STATE_DRAINING;
    return 0;
}

int snd_pcm_drop(snd_pcm_t *pcm) {
    if (!pcm) return -22;
    if (pcm->player) { (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_STOPPED); (*pcm->bufferQueue)->Clear(pcm->bufferQueue); }
    pcm->buffered_frames = 0; pcm->state = SND_PCM_STATE_SETUP;
    return 0;
}

int snd_pcm_recover(snd_pcm_t *pcm, int err, int silent) { (void)err; (void)silent; return snd_pcm_prepare(pcm); }
snd_pcm_state_t snd_pcm_state(snd_pcm_t *pcm) { return pcm ? pcm->state : SND_PCM_STATE_DISCONNECTED; }
snd_pcm_sframes_t snd_pcm_avail_update(snd_pcm_t *pcm) { return pcm ? (snd_pcm_sframes_t)(pcm->hw.buffer_frames - pcm->buffered_frames) : -22; }
snd_pcm_sframes_t snd_pcm_rewind(snd_pcm_t *pcm, snd_pcm_uframes_t frames) { (void)frames; return 0; }
int snd_pcm_resume(snd_pcm_t *pcm) { if (!pcm) return -22; if (pcm->player) (*pcm->player)->SetPlayState(pcm->player, SL_PLAYSTATE_PLAYING); pcm->state = SND_PCM_STATE_RUNNING; return 0; }
int snd_pcm_start(snd_pcm_t *pcm) { return snd_pcm_prepare(pcm); }
int snd_pcm_nonblock(snd_pcm_t *pcm, int nonblock) { (void)nonblock; return 0; }
snd_pcm_sframes_t snd_pcm_avail(snd_pcm_t *pcm) { return snd_pcm_avail_update(pcm); }
int snd_pcm_delay(snd_pcm_t *pcm, snd_pcm_sframes_t *delay) { if (!pcm || !delay) return -22; *delay = 0; return 0; }
int snd_pcm_wait(snd_pcm_t *pcm, int timeout) { (void)timeout; return 1; }
snd_pcm_sframes_t snd_pcm_mmap_begin(snd_pcm_t *pcm, const void **areas, snd_pcm_uframes_t *offset, snd_pcm_uframes_t *frames) { (void)areas; (void)offset; (void)frames; return -77; }
snd_pcm_sframes_t snd_pcm_mmap_commit(snd_pcm_t *pcm, snd_pcm_uframes_t offset, snd_pcm_uframes_t frames) { (void)offset; (void)frames; return -77; }

int snd_pcm_format_physical_width(snd_pcm_format_t format) { format_entry_t *fe = find_format(format); return fe ? fe->bits : 16; }
int snd_pcm_format_width(snd_pcm_format_t format) { format_entry_t *fe = find_format(format); return fe ? fe->sbits : 16; }
int snd_pcm_format_signed(snd_pcm_format_t format) { format_entry_t *fe = find_format(format); return fe ? fe->signed_fmt : 1; }
int snd_pcm_format_big_endian(snd_pcm_format_t format) { format_entry_t *fe = find_format(format); return fe ? fe->big_endian : 0; }
int snd_pcm_format_linear(snd_pcm_format_t format) { return 1; }
size_t snd_pcm_format_size(snd_pcm_format_t format, size_t samples) { format_entry_t *fe = find_format(format); return samples * ((fe ? fe->bits : 16) / 8); }
snd_pcm_format_t snd_pcm_build_linear_format(int w, int pw, int u, int be) { (void)w; (void)pw; (void)u; (void)be; return SND_PCM_FORMAT_S16_LE; }
int snd_pcm_link(snd_pcm_t *a, snd_pcm_t *b) { (void)a; (void)b; return 0; }
int snd_pcm_unlink(snd_pcm_t *p) { (void)p; return 0; }

int snd_mixer_open(snd_mixer_t **m, int mode) { (void)mode; *m = NULL; return -19; }
int snd_mixer_close(snd_mixer_t *m) { (void)m; return 0; }
int snd_mixer_attach(snd_mixer_t *m, const char *n) { (void)m; (void)n; return -19; }
int snd_mixer_detach(snd_mixer_t *m, const char *n) { (void)m; (void)n; return 0; }
int snd_mixer_selem_register(snd_mixer_t *m, void *o, void *c) { (void)m; (void)o; (void)c; return -19; }
int snd_mixer_load(snd_mixer_t *m) { (void)m; return -19; }
void snd_mixer_set_callback(snd_mixer_t *o, void *v) { (void)o; (void)v; }
void snd_mixer_elem_set_callback(snd_mixer_elem_t *o, void *v) { (void)o; (void)v; }
snd_mixer_elem_t* snd_mixer_first_elem(snd_mixer_t *m) { (void)m; return NULL; }
snd_mixer_elem_t* snd_mixer_last_elem(snd_mixer_t *m) { (void)m; return NULL; }
snd_mixer_elem_t* snd_mixer_elem_next(snd_mixer_elem_t *e) { (void)e; return NULL; }
snd_mixer_elem_t* snd_mixer_elem_prev(snd_mixer_elem_t *e) { (void)e; return NULL; }
int snd_mixer_handle_events(snd_mixer_t *m) { (void)m; return 0; }

int snd_mixer_selem_id_malloc(snd_mixer_selem_id_t **ptr) {
    *ptr = (snd_mixer_selem_id_t*)calloc(1, sizeof(snd_mixer_selem_id_t)); return *ptr ? 0 : -12;
}
void snd_mixer_selem_id_free(snd_mixer_selem_id_t *obj) { free(obj); }
void snd_mixer_selem_id_set_name(snd_mixer_selem_id_t *obj, const char *val) { if (obj && val) strncpy(obj->name, val, sizeof(obj->name) - 1); }
const char* snd_mixer_selem_id_get_name(snd_mixer_selem_id_t *obj) { return obj ? obj->name : ""; }
void snd_mixer_selem_id_set_index(snd_mixer_selem_id_t *obj, unsigned int val) { if (obj) obj->index = val; }
unsigned int snd_mixer_selem_id_get_index(snd_mixer_selem_id_t *obj) { return obj ? obj->index : 0; }
const char* snd_mixer_selem_get_name(snd_mixer_elem_t *e) { (void)e; return ""; }
unsigned int snd_mixer_selem_get_index(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_is_active(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_has_playback_switch(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_has_capture_switch(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_get_playback_switch(snd_mixer_elem_t *e, int c, int *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_get_capture_switch(snd_mixer_elem_t *e, int c, int *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_get_playback_volume(snd_mixer_elem_t *e, snd_mixer_selem_channel_id_t c, long *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_get_capture_volume(snd_mixer_elem_t *e, snd_mixer_selem_channel_id_t c, long *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_set_playback_volume_all(snd_mixer_elem_t *e, long v) { (void)e; (void)v; return -19; }
int snd_mixer_selem_set_capture_volume_all(snd_mixer_elem_t *e, long v) { (void)e; (void)v; return -19; }
int snd_mixer_selem_get_playback_volume_range(snd_mixer_elem_t *e, long *mn, long *mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_get_capture_volume_range(snd_mixer_elem_t *e, long *mn, long *mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_set_playback_volume_range(snd_mixer_elem_t *e, long mn, long mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_set_capture_volume_range(snd_mixer_elem_t *e, long mn, long mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_get_playback_dB(snd_mixer_elem_t *e, snd_mixer_selem_channel_id_t c, long *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_get_capture_dB(snd_mixer_elem_t *e, snd_mixer_selem_channel_id_t c, long *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_set_playback_dB_all(snd_mixer_elem_t *e, long v, int d) { (void)e; (void)v; (void)d; return -19; }
int snd_mixer_selem_set_capture_dB_all(snd_mixer_elem_t *e, long v, int d) { (void)e; (void)v; (void)d; return -19; }
int snd_mixer_selem_get_playback_dB_range(snd_mixer_elem_t *e, long *mn, long *mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_get_capture_dB_range(snd_mixer_elem_t *e, long *mn, long *mx) { (void)e; (void)mn; (void)mx; return -19; }
int snd_mixer_selem_has_playback_volume(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_has_capture_volume(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_has_playback_volume_joined(snd_mixer_elem_t *e) { (void)e; return 1; }
int snd_mixer_selem_has_capture_volume_joined(snd_mixer_elem_t *e) { (void)e; return 1; }
int snd_mixer_selem_has_playback_switch_joined(snd_mixer_elem_t *e) { (void)e; return 1; }
int snd_mixer_selem_has_capture_switch_joined(snd_mixer_elem_t *e) { (void)e; return 1; }
int snd_mixer_selem_set_playback_switch_all(snd_mixer_elem_t *e, int v) { (void)e; (void)v; return -19; }
int snd_mixer_selem_set_capture_switch_all(snd_mixer_elem_t *e, int v) { (void)e; (void)v; return -19; }
int snd_mixer_selem_get_playback_switch_switch(snd_mixer_elem_t *e, int c, int *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_has_capture_switch_switch(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_get_capture_switch_switch(snd_mixer_elem_t *e, int c, int *v) { (void)e; (void)c; (void)v; return -19; }
int snd_mixer_selem_get_type(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_get_count(snd_mixer_elem_t *e) { (void)e; return 0; }
int snd_mixer_selem_channel_id(snd_mixer_selem_channel_id_t c) { return (int)c; }

int snd_ctl_open(snd_ctl_t **h, const char *n, int m) { (void)n; (void)m; *h = NULL; return -19; }
int snd_ctl_close(snd_ctl_t *h) { (void)h; return 0; }
int snd_ctl_card_info_malloc(snd_ctl_card_info_t **ptr) {
    *ptr = (snd_ctl_card_info_t*)calloc(1, sizeof(snd_ctl_card_info_t));
    if (*ptr) { strcpy((*ptr)->id, "Android"); strcpy((*ptr)->name, "Android Audio"); strcpy((*ptr)->driver, "alsa_stub"); }
    return *ptr ? 0 : -12;
}
void snd_ctl_card_info_free(snd_ctl_card_info_t *obj) { free(obj); }
int snd_ctl_card_info(snd_ctl_t *h, snd_ctl_card_info_t *i) { (void)h; (void)i; return -19; }
const char* snd_ctl_card_info_get_id(const snd_ctl_card_info_t *o) { return o ? o->id : ""; }
const char* snd_ctl_card_info_get_name(const snd_ctl_card_info_t *o) { return o ? o->name : ""; }
const char* snd_ctl_card_info_get_driver(const snd_ctl_card_info_t *o) { return o ? o->driver : ""; }
int snd_ctl_pcm_info(snd_ctl_t *h, snd_pcm_info_t *i) { (void)h; (void)i; return -2; }
int snd_ctl_pcm_next_device(snd_ctl_t *h, int *d) { (void)h; *d = -1; return 0; }
int snd_ctl_rawmidi_next_device(snd_ctl_t *h, int *d) { (void)h; *d = -1; return 0; }
int snd_card_next(int *card) { *card = -1; return 0; }

int snd_seq_open(snd_seq_t **s, const char *n, int st, int m) { (void)n; (void)st; (void)m; *s = NULL; return -19; }
int snd_seq_close(snd_seq_t *s) { (void)s; return 0; }
int snd_seq_set_client_name(snd_seq_t *s, const char *n) { (void)s; (void)n; return -19; }
int snd_seq_create_simple_port(snd_seq_t *s, const char *n, unsigned int c, unsigned int t) { (void)s; (void)n; (void)c; (void)t; return -19; }
int snd_seq_connect_from(snd_seq_t *s, int mp, int sc, int sp) { (void)s; (void)mp; (void)sc; (void)sp; return -19; }
int snd_seq_connect_to(snd_seq_t *s, int mp, int dc, int dp) { (void)s; (void)mp; (void)dc; (void)dp; return -19; }
int snd_seq_alloc_named_queue(snd_seq_t *s, const char *n) { (void)s; (void)n; return -19; }
int snd_seq_alloc_queue(snd_seq_t *s) { (void)s; return -19; }
int snd_seq_free_queue(snd_seq_t *s, int q) { (void)s; (void)q; return 0; }
int snd_seq_set_tempo(snd_seq_t *s, int q, int t) { (void)s; (void)q; (void)t; return -19; }
int snd_seq_drain_output(snd_seq_t *s) { (void)s; return -19; }
int snd_seq_sync_output_queue(snd_seq_t *s) { (void)s; return -19; }
int snd_seq_event_output(snd_seq_t *s, void *e) { (void)s; (void)e; return -19; }
int snd_seq_event_input(snd_seq_t *s, void **e) { (void)s; (void)e; return -19; }
int snd_seq_free_event(void *e) { (void)e; return 0; }
int snd_seq_event_input_pending(snd_seq_t *s, int f) { (void)s; (void)f; return 0; }

int snd_rawmidi_open(snd_rawmidi_t **in, snd_rawmidi_t **out, const char *n, int m) {
    (void)n; (void)m; if (in) *in = NULL; if (out) *out = NULL; return -19;
}
int snd_rawmidi_close(snd_rawmidi_t *s) { (void)s; return 0; }
int snd_rawmidi_read(snd_rawmidi_t *in, void *b, size_t sz) { (void)in; (void)b; (void)sz; return -19; }
int snd_rawmidi_write(snd_rawmidi_t *out, const void *b, size_t sz) { (void)out; (void)b; (void)sz; return -19; }
int snd_rawmidi_drain(snd_rawmidi_t *out) { (void)out; return 0; }
int snd_rawmidi_nonblock(snd_rawmidi_t *rm, int nb) { (void)rm; (void)nb; return -19; }
int snd_rawmidi_params(snd_rawmidi_t *rm, void *p) { (void)rm; (void)p; return -19; }
unsigned int snd_rawmidi_count(snd_rawmidi_t *rm) { (void)rm; return 0; }
int snd_rawmidi_params_malloc(void **ptr) { *ptr = calloc(1, 64); return *ptr ? 0 : -12; }
void snd_rawmidi_params_free(void *obj) { free(obj); }
int snd_rawmidi_params_set_buffer_size(snd_rawmidi_t *rm, void *p, unsigned long v) { (void)rm; (void)p; (void)v; return -19; }
int snd_rawmidi_params_set_avail_min(snd_rawmidi_t *rm, void *p, unsigned long v) { (void)rm; (void)p; (void)v; return -19; }
int snd_rawmidi_status_malloc(void **ptr) { *ptr = calloc(1, 64); return *ptr ? 0 : -12; }
void snd_rawmidi_status_free(void *obj) { free(obj); }
int snd_rawmidi_status(snd_rawmidi_t *rm, void *st) { (void)rm; (void)st; return -19; }
unsigned int snd_rawmidi_status_get_avail(void *st) { (void)st; return 0; }
int snd_rawmidi_nonblock_old(snd_rawmidi_t *rm, int nb) { (void)rm; (void)nb; return -19; }
int snd_output_stdio_attach(void **o, FILE *fp, int c) { (void)o; (void)fp; (void)c; return 0; }
int snd_output_close(void *o) { (void)o; return 0; }
int snd_config_update(void) { return 0; }
int snd_config_update_free(void) { return 0; }