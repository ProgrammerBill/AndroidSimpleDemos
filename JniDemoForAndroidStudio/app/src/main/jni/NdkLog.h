//
// Created by bill on 2/22/19.
//

#include <android/log.h>
#ifndef JNIDEMO_NDKLOG_H
#define JNIDEMO_NDKLOG_H
#ifndef LOG
#define ALOGD(...) \
    ((void)__android_log_buf_print(LOG_ID_SYSTEM, ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__))
#endif

#ifndef NELEM
#define NELEM(x) ((int)(sizeof(x)/sizeof((x)[0])))
#endif

#endif //JNIDEMO_NDKLOG_H
