#include "shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils.h"
#include "def.h"

jbyteArray pCharToByteArray(JNIEnv* env, const char* buf);
const char* jByteArrayTopChar(JNIEnv* env, jbyteArray array);
void NoMappingFound(JNIEnv* env, jobject obj, const char* ipc_cache_name);
void AlreadyExists(JNIEnv* env, jobject obj, const char* ipc_cache_name);
jobject CreateStackTrace(JNIEnv* env, char* TraceName, char* TraceMessage, long ProcessID, long ThreadID);
void JNI_DEBUG(const char* msg);
long bSize;

JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Alloc
(JNIEnv* env, jobject obj, jstring name, jlong size) {
    bSize = size;
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (GetLastError()!=ERROR_ALREADY_EXISTS&&hMapFile==NULL)
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
        std::string msg = "MAPPING ";
        msg.append(ipc_cache_name);
        msg.append(" ALREADY EXISTS");
        JNI_DEBUG(msg.c_str());
        AlreadyExists(env, obj, ipc_cache_name);
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
        std::string msg = "CLEANED MAPPING";
        msg.append(ipc_cache_name);
        JNI_DEBUG(msg.c_str());
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
        jclass clz = env->FindClass("java/nio/ByteBuffer");
        jmethodID pmid = env->GetMethodID(clz, "put", "([B)Ljava/nio/ByteBuffer;");
        env->CallObjectMethod(buffer, pmid, pCharToByteArray(env, (char*)pBuffer));
        jmethodID mid = env->GetMethodID(clz, "mark", "()Ljava/nio/ByteBuffer;");
        env->CallObjectMethod(buffer, mid);
        return true;
    }
}
JNIEXPORT jboolean JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Write
(JNIEnv* env, jobject obj, jstring name, jbyteArray array) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        NoMappingFound(env, obj, ipc_cache_name);
        return false;
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        strcpy((char*)pBuffer, jByteArrayTopChar(env,array));
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
const char* jByteArrayTopChar(JNIEnv* env, jbyteArray array)
{
    int len = env->GetArrayLength(array);
    jbyte* arr;
    arr = env->GetByteArrayElements(array, 0);
    char* res = new char[len+1];
    memset(res, 0, len + 1);
    memcpy(res, arr, len);
    res[len] = 0;
    env->ReleaseByteArrayElements(array, arr, 0);
    return res;
}
void JNI_DEBUG(const char* msg) {
#ifdef _DEBUG
    printf("[JNI DEBUG]:%s\n", msg);
#endif
}