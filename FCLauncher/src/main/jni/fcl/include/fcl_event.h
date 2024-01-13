//
// Created by Tungsten on 2022/10/11.
//

#ifndef FOLD_CRAFT_LAUNCHER_FCL_EVENT_H
#define FOLD_CRAFT_LAUNCHER_FCL_EVENT_H

#define KeyPress              2
#define KeyRelease            3
#define ButtonPress           4
#define ButtonRelease         5
#define MotionNotify          6
#define KeyChar               7
#define ConfigureNotify       22
#define FCLMessage            37

#define Button1               1
#define Button2               2
#define Button3               3
#define Button4               4
#define Button5               5
#define Button6               6
#define Button7               7

#define CursorEnabled         1
#define CursorDisabled        0

#define ShiftMask               (1<<0)
#define LockMask                (1<<1)
#define ControlMask             (1<<2)
#define Mod1Mask                (1<<3)
#define Mod2Mask                (1<<4)
#define Mod3Mask                (1<<5)
#define Mod4Mask                (1<<6)
#define Mod5Mask                (1<<7)

#define CloseRequest            0

typedef struct {
    long long time;
    int type;
    int state;
    int button;
    int message;
    int x;
    int y;
    int keycode;
    int keychar;
    int width;
    int height;
} FCLEvent;

#endif //FOLD_CRAFT_LAUNCHER_FCL_EVENT_H
