cmake_minimum_required (VERSION 3.8)

project ("native")
include_directories(".")
add_library(native SHARED shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils.cpp jni.h jni_md.h shandiankulishe_codes_base_ipc_nativeImpl_MemoryUtils.h)