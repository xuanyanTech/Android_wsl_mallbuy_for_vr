#include<stdio.h>
#include<jni.h>
#include"com_xuanyantech_seecool_ndk_DataProvider.h"
#include<android/log.h>

#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


static JavaVM *m_JavaVM;//java虚拟机
static jclass m_jcls_JNI;

extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *java_vm, void *reserved) {
    m_JavaVM = java_vm;

    JNIEnv *jni_env = 0;

    //获取JavaVM的JNIEnv
    if (m_JavaVM->GetEnv((void **) (&jni_env), JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    jclass jcls_JNI = jni_env->FindClass("com/huotu/mall/wenslimall/partnermall/ndk/DataProvider");
    m_jcls_JNI = (jclass) jni_env->NewGlobalRef((jobject) jcls_JNI);//全局变量，方便调用
    return JNI_VERSION_1_4;
}

//#define JNIFUNCTION_NATIVE(sig) Java_com_xuanyantech_seecool_ndk_DataProvider_##sig


void photoSucceed(const char *photoPath) {
    JNIEnv *env;
    m_JavaVM->AttachCurrentThread(&env, NULL);//获取当前线程的JNIEnv
    jmethodID methodID = env->GetMethodID(m_jcls_JNI, "photoSucceedToAndroid",
                                          "(Ljava/lang/String;)V");
    jobject obj_manager = env->AllocObject(m_jcls_JNI);//获取实例对象
    env->CallVoidMethod(obj_manager, methodID, env->NewStringUTF(photoPath));
}

void getGiftPath(const char *path) {
    JNIEnv *env;
    m_JavaVM->AttachCurrentThread(&env, NULL);//获取当前线程的JNIEnv
    jmethodID methodID = env->GetMethodID(m_jcls_JNI, "getGiftPathToAndroid",
                                          "(Ljava/lang/String;)V");
    jobject obj_manager = env->AllocObject(m_jcls_JNI);//获取实例对象
    env->CallVoidMethod(obj_manager, methodID, env->NewStringUTF(path));
}

void videoRerurnEvent() {
    JNIEnv *env;
    m_JavaVM->AttachCurrentThread(&env, NULL);//获取当前线程的JNIEnv
    jmethodID methodID = env->GetMethodID(m_jcls_JNI, "unityBack",
                                          "()V");
    jobject obj_manager = env->AllocObject(m_jcls_JNI);//获取实例对象
    env->CallVoidMethod(obj_manager, methodID, env);
}



