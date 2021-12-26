# JCodeBase

## Copyright 2021 shandiankulishe

## What is it
a simple codebase.includes many utils wrote in cpp,java etc.
## How to use
install visual studio native toolchain(vcvarsall.bat etc) and run this commands in windows:  
``` 
mkdir build&&cd build
cmake ..
msbuild native.vcxproj /p:configuration=release
msbuild native.vcxproj /p:configuration=debug
cd ..\..
copy native\build\Release\* .
gradlew build
```  
After build,copy the native\build\Release\native.dll and build\libs\JCodeBase-xx.jar to your project dir and use.  
Project document is now producing.
## Available Modoules:
+ Memory Based IPC(windows only) **NOT FINISHED**
+ Parallel Download **NOT FINISHED**
## CI/CD
The project use github actions as its ci