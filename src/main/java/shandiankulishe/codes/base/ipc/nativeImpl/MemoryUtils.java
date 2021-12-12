package shandiankulishe.codes.base.ipc.nativeImpl;

public class MemoryUtils {
    public native void Alloc(String mapName);
    public native String Read(String mapName);
    public native void Write(String mapName,String content);
    public native void Close(String mapName);
}
