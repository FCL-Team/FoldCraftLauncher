//
// Created by Tungsten on 2022/10/11.
//

//////////////////////////////////////////////////////////////////////////
//////                       GLFW platform API                      //////
//////////////////////////////////////////////////////////////////////////

#include <internal.h>

int _glfwPlatformPollJoystick(_GLFWjoystick* js, int mode)
{
    return GLFW_FALSE;
}

void _glfwPlatformUpdateGamepadGUID(char* guid)
{
}

