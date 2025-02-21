//
// Created by Tungsten on 2022/10/11.
//

#include <fcl_internal.h>
#include <android/log.h>

FCLinjectorfun injectorCallback;

void fclSetInjectorCallback(FCLinjectorfun callback) {
    injectorCallback = callback;
}

void fclSetHitResultType(int type) {
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setHitResultType, "(I)V", type);
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_refreshHitResultType(JNIEnv *env, jobject thiz) {
    if (injectorCallback)
        injectorCallback();
}

void EventQueue_init(EventQueue* queue) {
    queue->count = 0;
    queue->head = NULL;
    queue->tail = NULL;
}

FCLEvent* EventQueue_add(EventQueue* queue) {
    FCLEvent* ret = NULL;
    QueueElement* e = malloc(sizeof(QueueElement));
    if (e != NULL) {
        e->next = NULL;
        if (queue->count > 0) {
            queue->tail->next = e;
            queue->tail = e;
        }
        else { // count == 0
            queue->head = e;
            queue->tail = e;
        }
        queue->count++;
        ret = &queue->tail->event;
    }
    return ret;
}

int EventQueue_take(EventQueue* queue, FCLEvent* event) {
    int ret = 0;
    if (queue->count > 0) {
        QueueElement* e = queue->head;
        if (queue->count == 1) {
            queue->head = NULL;
            queue->tail = NULL;
        }
        else {
            queue->head = e->next;
        }
        queue->count--;
        ret = 1;
        if (event != NULL) {
            memcpy(event, &e->event, sizeof(FCLEvent));
        }
        free(e);
    }
    return ret;
}

void EventQueue_clear(EventQueue* queue) {
    while (queue->count > 0) {
        EventQueue_take(queue, NULL);
    }
}

void fclSetCursorMode(int mode) {
    if (!fcl->has_event_pipe) {
        return;
    }
    PrepareFCLBridgeJNI();
    CallFCLBridgeJNIFunc( , Void, setCursorMode, "(I)V", mode);
}

int fclGetEventFd() {
    if (!fcl->has_event_pipe) {
        return -1;
    }
    return fcl->event_pipe_fd[0];
}

int fclWaitForEvent(int timeout) {
    if (!fcl->has_event_pipe) {
        return 0;
    }
    struct epoll_event ev;
    int ret = epoll_wait(fcl->epoll_fd, &ev, 1, timeout);
    if (ret > 0 && (ev.events & EPOLLIN)) {
        return 1;
    }
    return 0;
}

int fclPollEvent(FCLEvent* event) {
    if (!fcl->has_event_pipe) {
        return 0;
    }
    if (pthread_mutex_lock(&fcl->event_queue_mutex)) {
        FCL_INTERNAL_LOG("Failed to acquire mutex");
        return 0;
    }
    char c;
    int ret = 0;
    if (read(fcl->event_pipe_fd[0], &c, 1) > 0) {
        ret = EventQueue_take(&fcl->event_queue, event);
    }
    if (pthread_mutex_unlock(&fcl->event_queue_mutex)) {
        FCL_INTERNAL_LOG("Failed to release mutex");
        return 0;
    }
    return ret;
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_pushEvent(JNIEnv* env, jclass clazz, jlong time, jint type, jint p1, jint p2) {
    if (!fcl->has_event_pipe) {
        return;
    }
    if (pthread_mutex_lock(&fcl->event_queue_mutex)) {
        FCL_INTERNAL_LOG("Failed to acquire mutex");
        return;
    }
    FCLEvent* event = EventQueue_add(&fcl->event_queue);
    if (event == NULL) {
        FCL_INTERNAL_LOG("Failed to add event to event queue");
        return;
    }
    event->time = time;
    event->type = type;
    event->state = 0;
    switch (type) {
        case KeyChar:
            event->keychar = p2;
            break;
        case MotionNotify:
            event->x = p1;
            event->y = p2;
            break;
        case ButtonPress:
        case ButtonRelease:
            event->button = p1;
            break;
        case KeyPress:
        case KeyRelease:
            event->keycode = p1;
            event->keychar = p2;
            break;
        case ConfigureNotify:
            event->width = p1;
            event->height = p2;
            break;
        case FCLMessage:
            event->message = p1;
            break;
    }
    write(fcl->event_pipe_fd[1], "E", 1);
    if (pthread_mutex_unlock(&fcl->event_queue_mutex)) {
        FCL_INTERNAL_LOG("Failed to release mutex");
    }
}

JNIEXPORT void JNICALL Java_com_tungsten_fclauncher_bridge_FCLBridge_setEventPipe(JNIEnv* env, jclass clazz) {
    if (pipe(fcl->event_pipe_fd) == -1) {
        FCL_INTERNAL_LOG("Failed to create event pipe : %s", strerror(errno));
        return;
    }
    fcl->epoll_fd = epoll_create(3);
    if (fcl->epoll_fd == -1) {
        FCL_INTERNAL_LOG("Failed to get epoll fd : %s", strerror(errno));
        return;
    }
    struct epoll_event ev;
    ev.events = EPOLLIN;
    ev.data.fd = fcl->event_pipe_fd[0];
    if (epoll_ctl(fcl->epoll_fd, EPOLL_CTL_ADD, fcl->event_pipe_fd[0], &ev) == -1) {
        FCL_INTERNAL_LOG("Failed to add epoll event : %s", strerror(errno));
        return;
    }
    EventQueue_init(&fcl->event_queue);
    pthread_mutex_init(&fcl->event_queue_mutex, NULL);
    fcl->has_event_pipe = 1;
    FCL_INTERNAL_LOG("Succeeded to set event pipe");
}