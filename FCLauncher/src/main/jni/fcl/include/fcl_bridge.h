//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
#define FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H

#include <android/native_window.h>

typedef void (* FCLinjectorfun)();

void fclSetInjectorCallback(FCLinjectorfun callback);
void fclSetHitResultType(int type);

#endif //FOLD_CRAFT_LAUNCHER_FCL_BRIDGE_H
