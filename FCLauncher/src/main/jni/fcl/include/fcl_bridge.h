//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
#define FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H

#include <android/native_window.h>
#include "fcl_event.h"

ANativeWindow* fclGetNativeWindow(void);
int fclWaitForEvent(int timeout);
int fclPollEvent(FCLEvent* event);
int fclGetEventFd(void);
void fclLog(const char *buffer);
void fclSetInjectorMode(int mode);
int fclGetInjectorMode();
void fclSetHitResultType(int type);
void fclSetCursorMode(int mode);
void fclSetPrimaryClipString(const char* string);
const char* fclGetPrimaryClipString(void);

#endif //FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
