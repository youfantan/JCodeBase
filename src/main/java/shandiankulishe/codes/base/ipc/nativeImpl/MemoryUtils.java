package shandiankulishe.codes.base.ipc.nativeImpl;

import java.nio.ByteBuffer;

public class MemoryUtils {
    private static final MemoryUtils INSTANCE=new MemoryUtils();
    public static MemoryUtils getINSTANCE() {
        return INSTANCE;
    }
    public native boolean Alloc(String mapName, long size);
    public native boolean Read(String mapName, ByteBuffer buffer);
    public native boolean Write(String mapName,byte[] content);
    public native boolean Close(String mapName);
}
