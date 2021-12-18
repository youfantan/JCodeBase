#include "shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils.h"
#include "def.h"

jbyteArray pCharToByteArray(JNIEnv* env, const char* buf);
void NoMappingFound(JNIEnv* env, jobject obj, const char* ipc_cache_name);
void AlreadyExists(JNIEnv* env, jobject obj, const char* ipc_cache_name);
jobject CreateStackTrace(JNIEnv* env, char* TraceName, char* TraceMessage, long ProcessID, long ThreadID);

JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Alloc
(JNIEnv* env, jobject obj, jstring name, jint size) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        hMapFile = CreateFileMapping(
            INVALID_HANDLE_VALUE,
            NULL,
            PAGE_READWRITE,
            0,
            size,
            ipc_cache_name);
        return true;
    }
    else {
        AlreadyExists(env,obj, ipc_cache_name);
        return false;
    }
}

JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Close
(JNIEnv* env, jobject obj, jstring name) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile==NULL)
    {
        NoMappingFound(env,obj, ipc_cache_name);
        return false;
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        UnmapViewOfFile(pBuffer);
        CloseHandle(hMapFile);
        return true;
    }
}
JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Read
(JNIEnv* env, jobject obj, jstring name, jobject buffer)
{
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        NoMappingFound(env, obj, ipc_cache_name);
        return false;
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        strcpy((char*)env->GetDirectBufferAddress(buffer), (char*)pBuffer);
        return true;
    }
}
JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Write
(JNIEnv* env, jobject obj, jstring name, jobject buffer) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    const char* cache_content = (char*)env->GetDirectBufferAddress(buffer);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        NoMappingFound(env, obj, ipc_cache_name);
        return false;
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        strcpy((char*)pBuffer, cache_content);
        return true;
    }
}

jbyteArray pCharToByteArray(JNIEnv* env, const char* buf) {
    size_t len = strlen(buf);
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, (jbyte*)buf);
    return array;
}
jobject CreateStackTrace(JNIEnv* env, char* TraceName, char* TraceMessage, long ProcessID, long ThreadID) {
    jclass clz = env->FindClass("shandiankulishe/codes/base/err/StackTrace");
    jmethodID mid = env->GetMethodID(clz, "<init>", "(Ljava/lang/String;Ljava/lang/String;JJ)V");
    jobject stacktrace = env->NewObject(clz, mid, env->NewStringUTF(TraceName), env->NewStringUTF(TraceMessage), ProcessID, ThreadID);
    return stacktrace;
}
void NoMappingFound(JNIEnv* env, jobject obj,const char* ipc_cache_name) {
    long tid = GetCurrentThreadId();
    long pid = GetCurrentProcessId();
    char exception_message[256] = { 0 };
    strcat(exception_message, "No Mapping ");
    strcat(exception_message, ipc_cache_name);
    strcat(exception_message, " was found in memory");
    jclass clz = env->GetObjectClass(obj);
    jmethodID mid = env->GetMethodID(clz, "AppendError", "(Lshandiankulishe/codes/base/err/StackTrace;)V");
    env->CallVoidMethod(obj, mid, CreateStackTrace(env, "Memory NativeImpl StackTrace", exception_message, pid, tid));
}
void AlreadyExists(JNIEnv* env, jobject obj,const char* ipc_cache_name){
    long tid = GetCurrentThreadId();
    long pid = GetCurrentProcessId();
    char exception_message[256] = { 0 };
    strcat(exception_message, "Mapping ");
    strcat(exception_message, ipc_cache_name);
    strcat(exception_message, " already exists");
    jclass clz = env->GetObjectClass(obj);
    jmethodID mid = env->GetMethodID(clz, "AppendError", "(Lshandiankulishe/codes/base/err/StackTrace;)V");
    env->CallVoidMethod(obj, mid,CreateStackTrace(env,"Memory NativeImpl StackTrace",exception_message,pid,tid));
}