//
// Created by maks on 24.09.2022.
//

#include <stdlib.h>
#include <android/log.h>
#include <assert.h>
#include <string.h>
#include "environ.h"
#define TAG __FILE_NAME__
#include <log.h>

struct pojav_environ_s *pojav_environ;
__attribute__((constructor)) void env_init() {
    char* strptr_env = getenv("POJAV_ENVIRON");
    if(strptr_env == NULL) {
        LOGI("No environ found, creating...");
        pojav_environ = malloc(sizeof(struct pojav_environ_s));
        assert(pojav_environ);
        memset(pojav_environ, 0 , sizeof(struct pojav_environ_s));
        if(asprintf(&strptr_env, "%p", pojav_environ) == -1) abort();
        setenv("POJAV_ENVIRON", strptr_env, 1);
        free(strptr_env);
    }else{
        LOGI("Found existing environ: %s", strptr_env);
        pojav_environ = (void*) strtoul(strptr_env, NULL, 0x10);
    }
    LOGI("%p", pojav_environ);
}