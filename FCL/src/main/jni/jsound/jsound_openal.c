/*
 * libjsound 的 OpenAL 平台后端：按 DirectAudio.h 契约实现 DAUDIO_* 全部接口。
 * 参考 DirectAudio.h（https://github.com/openjdk/jdk17u/blob/master/src/java.desktop/share/native/libjsound/DirectAudio.h）。
 *
 * libopenal 由 FCLauncher 在 JVM 启动前以 RTLD_GLOBAL 预加载，这里按 soname
 * 复用同一实例；进入每个后端接口时把共享 ALC context 置为调用线程 current，
 * 避免同线程上游戏自身的 AL context 被误用。
 *
 * 本后端只接收 OpenAL 原生格式（16-bit signed LE / 8-bit unsigned LE，1~2 声道），
 * 请求格式与其不一致时的符号/大小端/增益转换由 DirectAudioDevice.c 通用层完成；
 * 播放位置沿用 ALSA 后端的相对估算公式（javaBytePos 减去设备内未播字节），
 * 使 setFramePosition 驱动的 Clip 循环语义与桌面端一致。
 */
#include <dlfcn.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <android/log.h>

#include "SoundDefs.h"
#include "DirectAudio.h"

#define JSOUND_TAG "FCL-jsound"
#define JSOUND_LOGE(...) __android_log_print(ANDROID_LOG_ERROR, JSOUND_TAG, __VA_ARGS__)
#define JSOUND_LOGW(...) __android_log_print(ANDROID_LOG_WARN, JSOUND_TAG, __VA_ARGS__)
#define JSOUND_LOGI(...) __android_log_print(ANDROID_LOG_INFO, JSOUND_TAG, __VA_ARGS__)

/* 播放环形缓冲：6 块 × 50ms，与原 Java 版 MioJSound 的 StreamVoice 参数一致 */
#define PLAYBACK_BUFFERS 6
#define CHUNK_MS 50

/* ---- OpenAL 常量（仅列出用到的） ---- */
#define JSOUND_AL_NONE 0
#define JSOUND_AL_SOURCE_STATE 0x1010
#define JSOUND_AL_BUFFERS_PROCESSED 0x1016
#define JSOUND_AL_PLAYING 0x1012
#define JSOUND_AL_BUFFER 0x1009
#define JSOUND_AL_BYTE_OFFSET 0x1026
#define JSOUND_AL_FORMAT_MONO8 0x1100
#define JSOUND_AL_FORMAT_MONO16 0x1101
#define JSOUND_AL_FORMAT_STEREO8 0x1102
#define JSOUND_AL_FORMAT_STEREO16 0x1103
#define JSOUND_ALC_TRUE 1
#define JSOUND_ALC_CAPTURE_SAMPLES 0x3110

typedef struct ALCdevice ALCdevice;
typedef struct ALCcontext ALCcontext;
typedef int ALenum;
typedef int ALint;
typedef unsigned int ALuint;
typedef int ALsizei;
typedef float ALfloat;
typedef int ALCenum;
typedef int ALCint;
typedef unsigned int ALCuint;
typedef char ALCchar;
typedef char ALCboolean;
typedef int ALCsizei;

typedef ALCdevice* (*alcOpenDeviceFn)(const ALCchar*);
typedef ALCboolean (*alcCloseDeviceFn)(ALCdevice*);
typedef ALCcontext* (*alcCreateContextFn)(ALCdevice*, const ALCint*);
typedef ALCboolean (*alcMakeContextCurrentFn)(ALCcontext*);
typedef ALCdevice* (*alcCaptureOpenDeviceFn)(const ALCchar*, ALCuint, ALCenum, ALCsizei);
typedef ALCboolean (*alcCaptureCloseDeviceFn)(ALCdevice*);
typedef void (*alcCaptureStartFn)(ALCdevice*);
typedef void (*alcCaptureStopFn)(ALCdevice*);
typedef void (*alcCaptureSamplesFn)(ALCdevice*, void*, ALCsizei);
typedef void (*alcGetIntegervFn)(ALCdevice*, ALCenum, ALCsizei, ALCint*);
typedef void (*alGenSourcesFn)(ALsizei, ALuint*);
typedef void (*alDeleteSourcesFn)(ALsizei, const ALuint*);
typedef void (*alGenBuffersFn)(ALsizei, ALuint*);
typedef void (*alDeleteBuffersFn)(ALsizei, const ALuint*);
typedef void (*alBufferDataFn)(ALuint, ALenum, const void*, ALsizei, ALsizei);
typedef void (*alSourceQueueBuffersFn)(ALuint, ALsizei, const ALuint*);
typedef void (*alSourceUnqueueBuffersFn)(ALuint, ALsizei, ALuint*);
typedef void (*alSourcePlayFn)(ALuint);
typedef void (*alSourceStopFn)(ALuint);
typedef void (*alSourceiFn)(ALuint, ALenum, ALint);
typedef void (*alGetSourceiFn)(ALuint, ALenum, ALint*);

static pthread_mutex_t g_engineLock = PTHREAD_MUTEX_INITIALIZER;
static int g_engineReady = 0;
static ALCdevice* g_device = NULL;
static ALCcontext* g_context = NULL;

static alcOpenDeviceFn p_alcOpenDevice;
static alcCloseDeviceFn p_alcCloseDevice;
static alcCreateContextFn p_alcCreateContext;
static alcMakeContextCurrentFn p_alcMakeContextCurrent;
static alcCaptureOpenDeviceFn p_alcCaptureOpenDevice;
static alcCaptureCloseDeviceFn p_alcCaptureCloseDevice;
static alcCaptureStartFn p_alcCaptureStart;
static alcCaptureStopFn p_alcCaptureStop;
static alcCaptureSamplesFn p_alcCaptureSamples;
static alcGetIntegervFn p_alcGetIntegerv;
static alGenSourcesFn p_alGenSources;
static alDeleteSourcesFn p_alDeleteSources;
static alGenBuffersFn p_alGenBuffers;
static alDeleteBuffersFn p_alDeleteBuffers;
static alBufferDataFn p_alBufferData;
static alSourceQueueBuffersFn p_alSourceQueueBuffers;
static alSourceUnqueueBuffersFn p_alSourceUnqueueBuffers;
static alSourcePlayFn p_alSourcePlay;
static alSourceStopFn p_alSourceStop;
static alSourceiFn p_alSourcei;
static alGetSourceiFn p_alGetSourcei;

static void* jsound_dlsym(void* lib, const char* name, int* ok) {
    void* sym = dlsym(lib, name);
    if (sym == NULL) {
        JSOUND_LOGE("dlsym %s 失败: %s", name, dlerror());
        *ok = 0;
    }
    return sym;
}

/* g_engineLock 持有下初始化 OpenAL 库与默认设备 */
static int jsound_initEngineLocked(void) {
    if (g_engineReady) {
        return 1;
    }
    void* lib = dlopen("libopenal.so", RTLD_NOW | RTLD_GLOBAL);
    if (lib == NULL) {
        lib = dlopen("libopenal.so", RTLD_LAZY);
    }
    if (lib == NULL) {
        JSOUND_LOGE("dlopen libopenal.so 失败: %s", dlerror());
        return 0;
    }
    int ok = 1;
    p_alcOpenDevice = (alcOpenDeviceFn) jsound_dlsym(lib, "alcOpenDevice", &ok);
    p_alcCloseDevice = (alcCloseDeviceFn) jsound_dlsym(lib, "alcCloseDevice", &ok);
    p_alcCreateContext = (alcCreateContextFn) jsound_dlsym(lib, "alcCreateContext", &ok);
    p_alcMakeContextCurrent = (alcMakeContextCurrentFn) jsound_dlsym(lib, "alcMakeContextCurrent", &ok);
    p_alcCaptureOpenDevice = (alcCaptureOpenDeviceFn) jsound_dlsym(lib, "alcCaptureOpenDevice", &ok);
    p_alcCaptureCloseDevice = (alcCaptureCloseDeviceFn) jsound_dlsym(lib, "alcCaptureCloseDevice", &ok);
    p_alcCaptureStart = (alcCaptureStartFn) jsound_dlsym(lib, "alcCaptureStart", &ok);
    p_alcCaptureStop = (alcCaptureStopFn) jsound_dlsym(lib, "alcCaptureStop", &ok);
    p_alcCaptureSamples = (alcCaptureSamplesFn) jsound_dlsym(lib, "alcCaptureSamples", &ok);
    p_alcGetIntegerv = (alcGetIntegervFn) jsound_dlsym(lib, "alcGetIntegerv", &ok);
    p_alGenSources = (alGenSourcesFn) jsound_dlsym(lib, "alGenSources", &ok);
    p_alDeleteSources = (alDeleteSourcesFn) jsound_dlsym(lib, "alDeleteSources", &ok);
    p_alGenBuffers = (alGenBuffersFn) jsound_dlsym(lib, "alGenBuffers", &ok);
    p_alDeleteBuffers = (alDeleteBuffersFn) jsound_dlsym(lib, "alDeleteBuffers", &ok);
    p_alBufferData = (alBufferDataFn) jsound_dlsym(lib, "alBufferData", &ok);
    p_alSourceQueueBuffers = (alSourceQueueBuffersFn) jsound_dlsym(lib, "alSourceQueueBuffers", &ok);
    p_alSourceUnqueueBuffers = (alSourceUnqueueBuffersFn) jsound_dlsym(lib, "alSourceUnqueueBuffers", &ok);
    p_alSourcePlay = (alSourcePlayFn) jsound_dlsym(lib, "alSourcePlay", &ok);
    p_alSourceStop = (alSourceStopFn) jsound_dlsym(lib, "alSourceStop", &ok);
    p_alSourcei = (alSourceiFn) jsound_dlsym(lib, "alSourcei", &ok);
    p_alGetSourcei = (alGetSourceiFn) jsound_dlsym(lib, "alGetSourcei", &ok);
    if (!ok) {
        return 0;
    }

    g_device = p_alcOpenDevice(NULL);
    if (g_device == NULL) {
        JSOUND_LOGE("alcOpenDevice 失败");
        return 0;
    }
    g_context = p_alcCreateContext(g_device, NULL);
    if (g_context == NULL || p_alcMakeContextCurrent(g_context) != JSOUND_ALC_TRUE) {
        JSOUND_LOGE("创建/激活 ALC context 失败");
        g_context = NULL;
        p_alcCloseDevice(g_device);
        g_device = NULL;
        return 0;
    }
    g_engineReady = 1;
    JSOUND_LOGI("OpenAL 设备就绪");
    return 1;
}

/* 进入后端接口时调用：确保引擎就绪并把 context 置为当前线程 */
static int jsound_ctxEnter(void) {
    pthread_mutex_lock(&g_engineLock);
    int ok = jsound_initEngineLocked();
    pthread_mutex_unlock(&g_engineLock);
    if (ok) {
        p_alcMakeContextCurrent(g_context);
    }
    return ok;
}

static void jsound_msleep(long ms) {
    struct timespec ts;
    ts.tv_sec = ms / 1000;
    ts.tv_nsec = (ms % 1000) * 1000000L;
    nanosleep(&ts, NULL);
}

typedef struct {
    int isSource;
    int frameSize;
    int sampleRate;
    /* 播放 */
    ALenum alFormat;
    ALuint source;
    ALuint buffers[PLAYBACK_BUFFERS];
    int bufferLens[PLAYBACK_BUFFERS];
    int ringHead;       /* 最旧已入队 buffer 的下标 */
    int queuedCount;    /* 已入队未播完的 buffer 数 */
    int chunkBytes;     /* 单块 50ms 的字节数（整帧对齐） */
    int capacityBytes;  /* PLAYBACK_BUFFERS * chunkBytes */
    int started;
    /* 录制 */
    ALCdevice* capture;
    int captureCapacityBytes;
} LineInfo;

/* 回收播完的 buffer */
static void jsound_reclaim(LineInfo* info) {
    ALint processed = 0;
    p_alGetSourcei(info->source, JSOUND_AL_BUFFERS_PROCESSED, &processed);
    while (processed-- > 0) {
        ALuint buf = JSOUND_AL_NONE;
        p_alSourceUnqueueBuffers(info->source, 1, &buf);
        info->bufferLens[info->ringHead] = 0;
        info->ringHead = (info->ringHead + 1) % PLAYBACK_BUFFERS;
        info->queuedCount--;
    }
}

/* 丢弃全部已入队 buffer（Stop/Flush 路径；alSourceStop 后全部视为 processed） */
static void jsound_dropQueued(LineInfo* info) {
    p_alSourceStop(info->source);
    jsound_reclaim(info);
}

/* 启动条件满足且 source 未在播放时起播（覆盖首播与 underrun 恢复） */
static void jsound_ensurePlaying(LineInfo* info) {
    if (!info->started || info->queuedCount <= 0) {
        return;
    }
    ALint state = 0;
    p_alGetSourcei(info->source, JSOUND_AL_SOURCE_STATE, &state);
    if (state != JSOUND_AL_PLAYING) {
        p_alSourcePlay(info->source);
    }
}

static ALenum jsound_alFormat(int sampleSizeInBits, int channels) {
    if (sampleSizeInBits == 8) {
        return channels == 2 ? JSOUND_AL_FORMAT_STEREO8 : JSOUND_AL_FORMAT_MONO8;
    }
    if (sampleSizeInBits == 16) {
        return channels == 2 ? JSOUND_AL_FORMAT_STEREO16 : JSOUND_AL_FORMAT_MONO16;
    }
    return JSOUND_AL_NONE;
}

INT32 DAUDIO_GetDirectAudioDeviceCount() {
    return 1;
}

INT32 DAUDIO_GetDirectAudioDeviceDescription(INT32 mixerIndex, DirectAudioDeviceDescription* description) {
    (void) mixerIndex;
    description->deviceID = 0;
    description->maxSimulLines = 0;
    strncpy(description->name, "OpenAL Device", DAUDIO_STRING_LENGTH);
    strncpy(description->vendor, "Fold Craft Launcher", DAUDIO_STRING_LENGTH);
    strncpy(description->description, "OpenAL Soft output via libjsound", DAUDIO_STRING_LENGTH);
    strncpy(description->version, "1.0", DAUDIO_STRING_LENGTH);
    return TRUE;
}

void DAUDIO_GetFormats(INT32 mixerIndex, INT32 deviceID, int isSource, void* creator) {
    /* OpenAL 原生接受的格式集合：16-bit signed / 8-bit unsigned，单双声道，
     * 采样率由 OpenAL Soft 混音器重采样，无需穷举设备能力 */
    static const int rates[] = {8000, 11025, 16000, 22050, 32000, 44100, 48000, 88200, 96000, 176400, 192000};
    static const int rateCount = (int) (sizeof(rates) / sizeof(rates[0]));
    (void) mixerIndex;
    (void) deviceID;
    (void) isSource;
    for (int rateIndex = 0; rateIndex < rateCount; rateIndex++) {
        for (int channels = 1; channels <= 2; channels++) {
            DAUDIO_AddAudioFormat(creator, 16, 0, channels, (float) rates[rateIndex],
                                  DAUDIO_PCM, TRUE /* signed */, FALSE /* little endian */);
            DAUDIO_AddAudioFormat(creator, 8, 0, channels, (float) rates[rateIndex],
                                  DAUDIO_PCM, FALSE /* unsigned */, FALSE);
        }
    }
}

void* DAUDIO_Open(INT32 mixerIndex, INT32 deviceID, int isSource,
                  int encoding, float sampleRate, int sampleSizeInBits,
                  int frameSize, int channels,
                  int isSigned, int isBigEndian, int bufferSizeInBytes) {
    (void) mixerIndex;
    (void) deviceID;
    (void) isSigned;
    (void) isBigEndian;
    if (encoding != DAUDIO_PCM || frameSize <= 0 || (channels != 1 && channels != 2)) {
        return NULL;
    }
    if (!jsound_ctxEnter()) {
        return NULL;
    }
    LineInfo* info = (LineInfo*) calloc(1, sizeof(LineInfo));
    if (info == NULL) {
        return NULL;
    }
    info->isSource = isSource;
    info->frameSize = frameSize;
    info->sampleRate = (int) (sampleRate + 0.5f);
    info->alFormat = jsound_alFormat(sampleSizeInBits, channels);

    if (isSource) {
        if (info->alFormat == JSOUND_AL_NONE) {
            free(info);
            return NULL;
        }
        p_alGenSources(1, &info->source);
        p_alGenBuffers(PLAYBACK_BUFFERS, info->buffers);
        info->chunkBytes = frameSize * (int) (info->sampleRate * CHUNK_MS / 1000.0f);
        if (info->chunkBytes < frameSize) {
            info->chunkBytes = frameSize;
        }
        info->capacityBytes = info->chunkBytes * PLAYBACK_BUFFERS;
    } else {
        if (info->alFormat == JSOUND_AL_NONE) {
            free(info);
            return NULL;
        }
        int frames = bufferSizeInBytes / frameSize;
        int minFrames = info->sampleRate / 10;
        if (frames < minFrames) {
            frames = minFrames;
        }
        if (frames > info->sampleRate) {
            frames = info->sampleRate;
        }
        info->capture = p_alcCaptureOpenDevice(NULL, (ALCuint) info->sampleRate, info->alFormat, (ALCsizei) frames);
        if (info->capture == NULL) {
            JSOUND_LOGW("alcCaptureOpenDevice 失败（%d Hz, %d bit, %d ch）", info->sampleRate, sampleSizeInBits, channels);
            free(info);
            return NULL;
        }
        info->captureCapacityBytes = frames * frameSize;
    }
    return info;
}

int DAUDIO_Start(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (!jsound_ctxEnter()) {
        return FALSE;
    }
    if (isSource) {
        info->started = 1;
        jsound_ensurePlaying(info);
    } else {
        p_alcCaptureStart(info->capture);
    }
    return TRUE;
}

int DAUDIO_Stop(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (!jsound_ctxEnter()) {
        return FALSE;
    }
    if (isSource) {
        info->started = 0;
        jsound_dropQueued(info);
    } else {
        p_alcCaptureStop(info->capture);
    }
    return TRUE;
}

void DAUDIO_Close(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (info == NULL) {
        return;
    }
    if (isSource) {
        if (jsound_ctxEnter()) {
            p_alSourcei(info->source, JSOUND_AL_BUFFER, JSOUND_AL_NONE);
            p_alDeleteBuffers(PLAYBACK_BUFFERS, info->buffers);
            p_alDeleteSources(1, &info->source);
        }
    } else if (info->capture != NULL) {
        p_alcCaptureStop(info->capture);
        p_alcCaptureCloseDevice(info->capture);
    }
    free(info);
}

int DAUDIO_Write(void* id, char* data, int byteSize) {
    LineInfo* info = (LineInfo*) id;
    if (byteSize <= 0 || info->frameSize <= 0) {
        return -1;
    }
    if (!jsound_ctxEnter()) {
        return -1;
    }
    int written = 0;
    while (written < byteSize) {
        jsound_reclaim(info);
        if (info->queuedCount >= PLAYBACK_BUFFERS) {
            if (!info->started) {
                /* 未启动且环形已满：返回已写字节，避免与 Java 侧 stop() 互等 */
                break;
            }
            /* 播放中：等待设备消化出空闲块（等同 ALSA 阻塞 writei） */
            jsound_msleep(4);
            continue;
        }
        int n = byteSize - written;
        if (n > info->chunkBytes) {
            n = info->chunkBytes;
        }
        int idx = (info->ringHead + info->queuedCount) % PLAYBACK_BUFFERS;
        p_alBufferData(info->buffers[idx], info->alFormat, data + written, (ALsizei) n, (ALsizei) info->sampleRate);
        p_alSourceQueueBuffers(info->source, 1, &info->buffers[idx]);
        info->bufferLens[idx] = n;
        info->queuedCount++;
        written += n;
        jsound_ensurePlaying(info);
    }
    return written;
}

int DAUDIO_Read(void* id, char* data, int byteSize) {
    LineInfo* info = (LineInfo*) id;
    if (byteSize <= 0 || info->frameSize <= 0) {
        return -1;
    }
    int read = 0;
    while (read < byteSize) {
        ALCint available = 0;
        p_alcGetIntegerv(info->capture, JSOUND_ALC_CAPTURE_SAMPLES, 1, &available);
        if (available <= 0) {
            if (!info->started) {
                break;
            }
            /* 等待采集数据（等同 ALSA 阻塞 readi，采集运行中数据持续到达） */
            jsound_msleep(4);
            continue;
        }
        int frames = (byteSize - read) / info->frameSize;
        if (frames > available) {
            frames = (int) available;
        }
        p_alcCaptureSamples(info->capture, data + read, (ALCsizei) frames);
        read += frames * info->frameSize;
    }
    return read;
}

int DAUDIO_GetBufferSize(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    return isSource ? info->capacityBytes : info->captureCapacityBytes;
}

int DAUDIO_StillDraining(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (isSource) {
        if (!jsound_ctxEnter()) {
            return FALSE;
        }
        jsound_reclaim(info);
        return (info->started && info->queuedCount > 0) ? TRUE : FALSE;
    }
    return info->started ? TRUE : FALSE;
}

int DAUDIO_Flush(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (isSource) {
        if (!jsound_ctxEnter()) {
            return FALSE;
        }
        jsound_dropQueued(info);
    } else {
        /* 丢弃已采集未读的样本 */
        ALCint available = 0;
        p_alcGetIntegerv(info->capture, JSOUND_ALC_CAPTURE_SAMPLES, 1, &available);
        while (available > 0) {
            char scratch[4096];
            int frames = (int) (sizeof(scratch) / info->frameSize);
            if (frames > available) {
                frames = (int) available;
            }
            p_alcCaptureSamples(info->capture, scratch, (ALCsizei) frames);
            available -= frames;
        }
    }
    return TRUE;
}

int DAUDIO_GetAvailable(void* id, int isSource) {
    LineInfo* info = (LineInfo*) id;
    if (isSource) {
        if (!jsound_ctxEnter()) {
            return 0;
        }
        jsound_reclaim(info);
        int queuedBytes = 0;
        for (int i = 0; i < info->queuedCount; i++) {
            int idx = (info->ringHead + i) % PLAYBACK_BUFFERS;
            queuedBytes += info->bufferLens[idx];
        }
        return info->capacityBytes - queuedBytes;
    }
    ALCint available = 0;
    p_alcGetIntegerv(info->capture, JSOUND_ALC_CAPTURE_SAMPLES, 1, &available);
    return (int) available * info->frameSize;
}

INT64 DAUDIO_GetBytePosition(void* id, int isSource, INT64 javaBytePos) {
    LineInfo* info = (LineInfo*) id;
    if (!isSource) {
        /* 录制：位置 = Java 已读位置 + 已采集未读字节 */
        return javaBytePos + DAUDIO_GetAvailable(id, isSource);
    }
    if (info->queuedCount <= 0) {
        /* 已排空（含 flush/underrun），设备内无未播数据 */
        return javaBytePos;
    }
    if (!jsound_ctxEnter()) {
        return javaBytePos;
    }
    INT64 queuedBytes = 0;
    for (int i = 0; i < info->queuedCount; i++) {
        int idx = (info->ringHead + i) % PLAYBACK_BUFFERS;
        queuedBytes += info->bufferLens[idx];
    }
    /* 加上正在播放那块内的精确偏移 */
    ALint state = 0;
    p_alGetSourcei(info->source, JSOUND_AL_SOURCE_STATE, &state);
    if (state == JSOUND_AL_PLAYING) {
        ALint offset = 0;
        p_alGetSourcei(info->source, JSOUND_AL_BYTE_OFFSET, &offset);
        if (offset > 0 && offset < queuedBytes) {
            return javaBytePos - queuedBytes + offset;
        }
    }
    return javaBytePos - queuedBytes;
}

void DAUDIO_SetBytePosition(void* id, int isSource, INT64 javaBytePos) {
    /* 可忽略：GetBytePosition 以调用方传入的 javaBytePos 为基准（同 ALSA 后端） */
    (void) id;
    (void) isSource;
    (void) javaBytePos;
}

int DAUDIO_RequiresServicing(void* id, int isSource) {
    (void) id;
    (void) isSource;
    return FALSE;
}

void DAUDIO_Service(void* id, int isSource) {
    (void) id;
    (void) isSource;
}
