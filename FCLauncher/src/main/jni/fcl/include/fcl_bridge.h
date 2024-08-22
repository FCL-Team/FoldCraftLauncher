//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
#define FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H

#include <android/native_window.h>
#include "fcl_event.h"

typedef void (* FCLinjectorfun)();

void fclSetInjectorCallback(FCLinjectorfun callback);
void fclSetHitResultType(int type);
void fclSetPrimaryClipString(const char* string);
const char* fclGetPrimaryClipString(void);

#endif //FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
