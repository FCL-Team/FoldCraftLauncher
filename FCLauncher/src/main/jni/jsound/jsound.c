#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <jni.h>
#include <pthread.h>
#include <string.h>
#include <stdlib.h>
#include <stdio.h>

#define BUFFER_COUNT 4
#define BUFFER_SIZE 4096
#define RING_SIZE (256 * 1024)

typedef struct {
    SLObjectItf engineObj;
    SLEngineItf engine;
    SLObjectItf outputMixObj;
    SLObjectItf playerObj;
    SLPlayItf play;
    SLBufferQueueItf queue;

    char ring[RING_SIZE];
    volatile int writePos;
    volatile int readPos;

    char buffers[BUFFER_COUNT][BUFFER_SIZE];
    volatile int enqueueHead;
    volatile int enqueueTail;
    volatile int buffersInQueue;

    pthread_mutex_t mutex;
    pthread_cond_t dataCond;
    pthread_cond_t spaceCond;
    pthread_cond_t drainCond;

    volatile int running;
    volatile int draining;
    volatile int drained;
} AudioCtx;

static SLObjectItf gEngineObj = NULL;
static SLEngineItf gEngine = NULL;
static int gRefCount = 0;

static int minInt(int a, int b) { return a < b ? a : b; }

static int ringAvailable(AudioCtx *ctx) {
    return (ctx->readPos - ctx->writePos - 1 + RING_SIZE) % RING_SIZE;
}

static int ringUsed(AudioCtx *ctx) {
    return (ctx->writePos - ctx->readPos + RING_SIZE) % RING_SIZE;
}

static void ringWrite(AudioCtx *ctx, const char *src, int len) {
    int part = minInt(len, RING_SIZE - ctx->writePos);
    memcpy(ctx->ring + ctx->writePos, src, part);
    if (part < len) {
        memcpy(ctx->ring, src + part, len - part);
    }
    ctx->writePos = (ctx->writePos + len) % RING_SIZE;
}

static void ringRead(AudioCtx *ctx, char *dst, int len) {
    int part = minInt(len, RING_SIZE - ctx->readPos);
    memcpy(dst, ctx->ring + ctx->readPos, part);
    if (part < len) {
        memcpy(dst + part, ctx->ring, len - part);
    }
    ctx->readPos = (ctx->readPos + len) % RING_SIZE;
}

static void bufferQueueCallback(SLBufferQueueItf caller, void *context, SLuint32 size) {
    AudioCtx *ctx = (AudioCtx*)context;
    int enqueueIdx = -1;

    pthread_mutex_lock(&ctx->mutex);

    if (ctx->buffersInQueue > 0) {
        ctx->buffersInQueue--;
        ctx->enqueueHead = (ctx->enqueueHead + 1) % BUFFER_COUNT;
    }

    if (ringUsed(ctx) >= BUFFER_SIZE) {
        enqueueIdx = ctx->enqueueTail;
        ringRead(ctx, ctx->buffers[enqueueIdx], BUFFER_SIZE);
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
        pthread_cond_signal(&ctx->spaceCond);
    } else if (ctx->draining) {
        if (ringUsed(ctx) == 0 && ctx->buffersInQueue == 0) {
            ctx->drained = 1;
            pthread_cond_signal(&ctx->drainCond);
        }
        enqueueIdx = ctx->enqueueTail;
        memset(ctx->buffers[enqueueIdx], 0, BUFFER_SIZE);
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    } else {
        enqueueIdx = ctx->enqueueTail;
        memset(ctx->buffers[enqueueIdx], 0, BUFFER_SIZE);
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    }
    pthread_mutex_unlock(&ctx->mutex);

    if (enqueueIdx >= 0) {
        (*caller)->Enqueue(caller, ctx->buffers[enqueueIdx], BUFFER_SIZE);
    }
}

static int initOpenSL(AudioCtx *ctx) {
    SLresult result;

    pthread_mutex_lock(&ctx->mutex);
    if (gEngineObj == NULL) {
        result = slCreateEngine(&gEngineObj, 0, NULL, 0, NULL, NULL);
        if (result != SL_RESULT_SUCCESS) {
            pthread_mutex_unlock(&ctx->mutex);
            return 0;
        }
        result = (*gEngineObj)->Realize(gEngineObj, SL_BOOLEAN_FALSE);
        if (result != SL_RESULT_SUCCESS) {
            (*gEngineObj)->Destroy(gEngineObj);
            gEngineObj = NULL;
            pthread_mutex_unlock(&ctx->mutex);
            return 0;
        }
        result = (*gEngineObj)->GetInterface(gEngineObj, SL_IID_ENGINE, &gEngine);
        if (result != SL_RESULT_SUCCESS) {
            (*gEngineObj)->Destroy(gEngineObj);
            gEngineObj = NULL;
            pthread_mutex_unlock(&ctx->mutex);
            return 0;
        }
    }
    gRefCount++;
    ctx->engineObj = gEngineObj;
    ctx->engine = gEngine;
    pthread_mutex_unlock(&ctx->mutex);

    result = (*ctx->engine)->CreateOutputMix(ctx->engine, &ctx->outputMixObj, 0, NULL, NULL);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->outputMixObj)->Realize(ctx->outputMixObj, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) return 0;

    SLDataLocator_AndroidSimpleBufferQueue locBufQueue = {
        SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, BUFFER_COUNT
    };
    SLDataFormat_PCM formatPcm = {
        SL_DATAFORMAT_PCM,
        2,
        SL_SAMPLINGRATE_44_1,
        SL_PCMSAMPLEFORMAT_FIXED_16,
        SL_PCMSAMPLEFORMAT_FIXED_16,
        SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
        SL_BYTEORDER_LITTLEENDIAN
    };
    SLDataSource audioSrc = { &locBufQueue, &formatPcm };

    SLDataLocator_OutputMix locOutMix = {
        SL_DATALOCATOR_OUTPUTMIX, ctx->outputMixObj
    };
    SLDataSink audioSnk = { &locOutMix, NULL };

    SLInterfaceID ids[] = { SL_IID_BUFFERQUEUE };
    SLboolean req[] = { SL_BOOLEAN_TRUE };

    result = (*ctx->engine)->CreateAudioPlayer(ctx->engine, &ctx->playerObj,
        &audioSrc, &audioSnk, 1, ids, req);
    if (result != SL_RESULT_SUCCESS) return 0;
    result = (*ctx->playerObj)->Realize(ctx->playerObj, SL_BOOLEAN_FALSE);
    if (result != SL_RESULT_SUCCESS) return 0;

    result = (*ctx->playerObj)->GetInterface(ctx->playerObj, SL_IID_PLAY, &ctx->play);
    if (result != SL_RESULT_SUCCESS) return 0;

    result = (*ctx->playerObj)->GetInterface(ctx->playerObj, SL_IID_BUFFERQUEUE, &ctx->queue);
    if (result != SL_RESULT_SUCCESS) return 0;

    result = (*ctx->queue)->RegisterCallback(ctx->queue, bufferQueueCallback, ctx);
    if (result != SL_RESULT_SUCCESS) return 0;

    int i;
    for (i = 0; i < BUFFER_COUNT; i++) {
        memset(ctx->buffers[ctx->enqueueTail], 0, BUFFER_SIZE);
        result = (*ctx->queue)->Enqueue(ctx->queue, ctx->buffers[ctx->enqueueTail], BUFFER_SIZE);
        if (result != SL_RESULT_SUCCESS) break;
        ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
        ctx->buffersInQueue++;
    }

    result = (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PLAYING);
    if (result != SL_RESULT_SUCCESS) return 0;

    return 1;
}

static void shutdownOpenSL(AudioCtx *ctx) {
    if (ctx->play)
        (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_STOPPED);
    if (ctx->queue)
        (*ctx->queue)->Clear(ctx->queue);
    if (ctx->playerObj) {
        (*ctx->playerObj)->Destroy(ctx->playerObj);
        ctx->playerObj = NULL;
    }
    if (ctx->outputMixObj) {
        (*ctx->outputMixObj)->Destroy(ctx->outputMixObj);
        ctx->outputMixObj = NULL;
    }

    pthread_mutex_lock(&ctx->mutex);
    gRefCount--;
    if (gRefCount == 0 && gEngineObj != NULL) {
        (*gEngineObj)->Destroy(gEngineObj);
        gEngineObj = NULL;
        gEngine = NULL;
    }
    pthread_mutex_unlock(&ctx->mutex);
}

static AudioCtx* createCtx(void) {
    AudioCtx *ctx = (AudioCtx*)calloc(1, sizeof(AudioCtx));
    if (!ctx) return NULL;
    ctx->ringBufferSize = RING_SIZE;
    ctx->writePos = 0;
    ctx->readPos = 0;
    ctx->buffersInQueue = 0;
    ctx->enqueueHead = 0;
    ctx->enqueueTail = 0;
    ctx->running = 1;
    ctx->draining = 0;
    ctx->drained = 0;
    ctx->engineObj = NULL;
    ctx->engine = NULL;
    ctx->outputMixObj = NULL;
    ctx->playerObj = NULL;
    ctx->play = NULL;
    ctx->queue = NULL;
    pthread_mutex_init(&ctx->mutex, NULL);
    pthread_cond_init(&ctx->dataCond, NULL);
    pthread_cond_init(&ctx->spaceCond, NULL);
    pthread_cond_init(&ctx->drainCond, NULL);
    return ctx;
}

static void destroyCtx(AudioCtx *ctx) {
    if (!ctx) return;
    shutdownOpenSL(ctx);
    pthread_mutex_destroy(&ctx->mutex);
    pthread_cond_destroy(&ctx->dataCond);
    pthread_cond_destroy(&ctx->spaceCond);
    pthread_cond_destroy(&ctx->drainCond);
    free(ctx);
}

static jint JNICALL nGetNumDevices(JNIEnv *env, jclass clazz, jint type) {
    return (type == 0) ? 1 : 0;
}

static jstring JNICALL nGetDeviceDescription(JNIEnv *env, jclass clazz, jint mixerIndex, jint deviceID) {
    return (*env)->NewStringUTF(env, "Android OpenSL ES Audio");
}

static jlong JNICALL nOpen(JNIEnv *env, jclass clazz, jint mixerIndex, jint deviceID) {
    AudioCtx *ctx = createCtx();
    if (!ctx) return 0;
    if (!initOpenSL(ctx)) {
        destroyCtx(ctx);
        return 0;
    }
    return (jlong)(intptr_t)ctx;
}

static jint JNICALL nStart(JNIEnv *env, jobject thiz, jlong id) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx || !ctx->play) return -1;
    ctx->running = 1;
    SLresult result = (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PLAYING);
    return (result == SL_RESULT_SUCCESS) ? 0 : -1;
}

static jint JNICALL nStop(JNIEnv *env, jobject thiz, jlong id) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx || !ctx->play) return -1;
    ctx->running = 0;
    SLresult result = (*ctx->play)->SetPlayState(ctx->play, SL_PLAYSTATE_PAUSED);
    return (result == SL_RESULT_SUCCESS) ? 0 : -1;
}

static jint JNICALL nClose(JNIEnv *env, jobject thiz, jlong id) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx) return 0;
    ctx->running = 0;
    destroyCtx(ctx);
    return 0;
}

static jint JNICALL nWrite(JNIEnv *env, jobject thiz, jlong id, jbyteArray b, jint off, jint len) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx || !b) return -1;

    jbyte *data = (*env)->GetByteArrayElements(env, b, NULL);
    if (!data) return -1;

    int written = 0;
    while (written < len) {
        pthread_mutex_lock(&ctx->mutex);
        int avail = ringAvailable(ctx);
        if (avail == 0) {
            pthread_mutex_unlock(&ctx->mutex);
            break;
        }
        int chunk = minInt(len - written, avail);
        ringWrite(ctx, (const char*)(data + off + written), chunk);
        written += chunk;

        while (ctx->buffersInQueue < BUFFER_COUNT && ringUsed(ctx) >= BUFFER_SIZE) {
            int idx = ctx->enqueueTail;
            ringRead(ctx, ctx->buffers[idx], BUFFER_SIZE);
            (*ctx->queue)->Enqueue(ctx->queue, ctx->buffers[idx], BUFFER_SIZE);
            ctx->enqueueTail = (ctx->enqueueTail + 1) % BUFFER_COUNT;
            ctx->buffersInQueue++;
        }
        pthread_mutex_unlock(&ctx->mutex);
    }

    (*env)->ReleaseByteArrayElements(env, b, data, JNI_ABORT);
    return written;
}

static jint JNICALL nRead(JNIEnv *env, jobject thiz, jlong id, jbyteArray b, jint off, jint len) {
    return -1;
}

static jint JNICALL nAvailable(JNIEnv *env, jobject thiz, jlong id, jboolean isWrite) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx) return 0;
    if (!isWrite) return 0;
    pthread_mutex_lock(&ctx->mutex);
    int avail = ringAvailable(ctx);
    pthread_mutex_unlock(&ctx->mutex);
    return avail;
}

static jint JNICALL nDrain(JNIEnv *env, jobject thiz, jlong id) {
    AudioCtx *ctx = (AudioCtx*)(intptr_t)id;
    if (!ctx) return 0;
    pthread_mutex_lock(&ctx->mutex);
    ctx->draining = 1;
    while (!ctx->drained && (ringUsed(ctx) > 0 || ctx->buffersInQueue > 0)) {
        pthread_cond_wait(&ctx->drainCond, &ctx->mutex);
    }
    ctx->draining = 0;
    ctx->drained = 0;
    pthread_mutex_unlock(&ctx->mutex);
    return 0;
}

static JNINativeMethod methods[] = {
    {"nGetNumDevices",        "(I)I",                     (void*)nGetNumDevices},
    {"nGetDeviceDescription", "(II)Ljava/lang/String;",   (void*)nGetDeviceDescription},
    {"nOpen",                 "(II)J",                    (void*)nOpen},
    {"nStart",                "(J)I",                     (void*)nStart},
    {"nStop",                 "(J)I",                     (void*)nStop},
    {"nClose",                "(J)I",                     (void*)nClose},
    {"nWrite",                "(J[BII)I",                 (void*)nWrite},
    {"nRead",                 "(J[BII)I",                 (void*)nRead},
    {"nAvailable",            "(JZ)I",                    (void*)nAvailable},
    {"nDrain",                "(J)I",                     (void*)nDrain},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_6) != JNI_OK)
        return JNI_ERR;

    jclass clazz = (*env)->FindClass(env, "com/sun/media/sound/PortMixer");
    if (!clazz) {
        clazz = (*env)->FindClass(env, "com/sun/media/sound/PortMixerProvider");
        if (!clazz)
            return JNI_ERR;
    }

    if ((*env)->RegisterNatives(env, clazz, methods, sizeof(methods) / sizeof(methods[0])) != JNI_OK)
        return JNI_ERR;

    return JNI_VERSION_1_6;
}