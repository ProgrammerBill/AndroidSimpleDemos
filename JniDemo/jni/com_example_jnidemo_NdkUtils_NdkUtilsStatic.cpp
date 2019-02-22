#include <jni.h>
#include "JNIHelp.h"
#include "com_example_jnidemo_NdkUtils_NdkUtilsStatic.h"
#include <log/log.h>
#include <stdlib.h>
#define LOG_TAG "jnidemo"
#include "TimeUtils/TimeUtils.h"

/*
 * Class:     com_example_jnidemo_NdkUtils_NdkUtilsStatic
 * Method:    getTime
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_example_jnidemo_NdkUtils_NdkUtilsStatic_getTime
(JNIEnv *mEnv, jobject){
    char *timebuf = (char *)malloc(sizeof(char) * 16);
    getTime(timebuf);
    ALOGD("JNI getTime is %s\n", timebuf);
    jstring str = mEnv->NewStringUTF(timebuf);
    return str;
}

