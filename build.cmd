mkdir native\build
cd native\build
cmake -G"Visual Studio 16 2019" ..
msbuild native.vcxproj /p:configuration=release
msbuild native.vcxproj /p:configuration=debug
cd ../..
mkdir dest
mkdir dest\Release
mkdir dest\Debug
copy build\libs\* dest
copy native\build\Release\* dest\Release
copy native\build\Debug\* dest\Debug
7z a -ttar build.tar .\dest\*
7z a -tgzip build.tar.gz build.tar