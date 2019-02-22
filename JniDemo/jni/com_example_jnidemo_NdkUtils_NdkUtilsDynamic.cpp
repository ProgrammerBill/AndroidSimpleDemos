#include <jni.h>
#include "JNIHelp.h"
#include <log/log.h>
#define LOG_TAG "jnidemo"
#include <stdlib.h>
#include "TimeUtils/TimeUtils.h"

JNIEXPORT jstring com_example_jnidemo_NdkUtils_NdkUtilsDynamic_getTime(
        JNIEnv *env, jobject){
    char *timebuf = (char *)malloc(sizeof(char) * 16);
    getTime(timebuf);
    ALOGD("JNI static getTime is %s\n", timebuf);
    jstring str = env->NewStringUTF(timebuf);
    free(timebuf);
    return str;
}


static const JNINativeMethod g_methods[] = {
    { "getTime",
      "()Ljava/lang/String;",
      (void *)com_example_jnidemo_NdkUtils_NdkUtilsDynamic_getTime
    },
};

static int register_com_example_jnidemo_NdkUtils_NdkUtilsDynamic(JNIEnv* env)
{
    jclass clazz;
    clazz = env->FindClass("com/example/jnidemo/NdkUtils/NdkUtilsDynamic");
    if (clazz == NULL)
        return JNI_FALSE;
    if (env->RegisterNatives(clazz, g_methods, NELEM(g_methods)) < 0)
        return JNI_FALSE;
    return JNI_TRUE;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
     JNIEnv* env = NULL;

     jint Ret = -1;
     if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK)
         goto bail;
     if (!register_com_example_jnidemo_NdkUtils_NdkUtilsDynamic(env))
         goto bail;

     Ret = JNI_VERSION_1_4;
 bail:
     return Ret;
}
