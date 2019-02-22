#ifndef _TIMEUTILS_H_
#define _TIMEUTILS_H_
#ifdef __cplusplus
extern "C" {
#endif
#include <log/log.h>
#include <time.h>

void getTime(char * buf){
    time_t t;
    struct tm * tmp;
    time(&t);
    tmp = localtime(&t);
    if(strftime(buf,16,"%T",tmp) == 0){
        ALOGD("failed to gettime!\n");
    }
    ALOGD("getTime is %s\n", buf);
}

#ifdef __cplusplus
}
#endif
#endif
