//
// Created by tom on 9/1/26.
//

#include <dlfcn.h>
#include "utils.h"

typedef int (*get_device_api_level_fn)(void);

int is_android_6_or_lower(void)
{
    void *symbol;
    int api_level = 0;
    void *libcHandle = dlopen("libc.so", RTLD_LAZY);
    if (libcHandle == NULL) {
        LOGE("Can't check device API level because libc dlopen failed: %s. "
             "Assuming modern Android.", dlerror());
        return 0; // false
    }
    symbol = dlsym(libcHandle, "android_get_device_api_level");
    if (symbol == NULL) {
        // android_get_device_api_level() was added in API 24. If it's not here then we are lower.
        dlclose(libcHandle);
        return 1; // true
    }
    api_level = ((get_device_api_level_fn)symbol)();
    dlclose(libcHandle);
    return api_level < 24 ? 1 : 0;
}
