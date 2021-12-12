#include "shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils.h"
JNIEXPORT void JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Alloc
(JNIEnv* env, jobject obj, jstring name) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = CreateFileMapping(
        INVALID_HANDLE_VALUE,
        NULL,
        PAGE_READWRITE,
        0,
        DEFAULT_MAPPING_SIZE,
        ipc_cache_name);
}
JNIEXPORT void JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Close
(JNIEnv* env, jobject obj, jstring name) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile==NULL)
    {
        printf("%S\n","no mapping found");
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        if (pBuffer==NULL)
        {
            printf("%S\n", "no mapping buffer found");
        }
        else {
            UnmapViewOfFile(pBuffer);
            CloseHandle(hMapFile);
        }
    }
}
JNIEXPORT jstring JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Read
(JNIEnv* env, jobject obj, jstring name) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    const char* cache_content = NULL;
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        printf("%S\n", "no mapping found");
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        if (pBuffer == NULL)
        {
            printf("%S\n", "no mapping buffer found");
        }
        else {
            cache_content = (char*)pBuffer;
        }
    }
    return env->NewStringUTF(cache_content);
}
JNIEXPORT void JNICALL Java_shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils_Write
(JNIEnv* env, jobject obj, jstring name, jstring content) {
    const char* ipc_cache_name = env->GetStringUTFChars(name, NULL);
    const char* cache_content = NULL;
    HANDLE hMapFile = OpenFileMapping(FILE_MAP_ALL_ACCESS, 0, ipc_cache_name);
    if (hMapFile == NULL)
    {
        printf("%S\n", "no mapping found");
    }
    else {
        LPVOID pBuffer = MapViewOfFile(hMapFile, FILE_MAP_ALL_ACCESS, 0, 0, 0);
        strcpy((char*)pBuffer, env->GetStringUTFChars(content, NULL));
    }
}