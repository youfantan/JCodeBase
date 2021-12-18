package shandiankulishe.codes.base.ipc.nativeImpl;

import shandiankulishe.codes.base.err.StackTrace;
import shandiankulishe.codes.base.ipc.MemoryException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MemoryUtils {
    private List<StackTrace> traces=new ArrayList<>();
    public void AppendError(StackTrace trace){
        traces.add(trace);
        System.out.println("test");
    }
    public StackTrace[] getLastErrors(){
        StackTrace[] traceArray=traces.toArray(new StackTrace[traces.size()]);
        traces.clear();
        return traceArray;
    }
    private static final MemoryUtils INSTANCE=new MemoryUtils();
    public static MemoryUtils getINSTANCE() {
        return INSTANCE;
    }
    public native boolean Alloc(String mapName, int size);
    public native boolean Read(String mapName, ByteBuffer buffer);
    public native boolean Write(String mapName,ByteBuffer buffer);
    public native boolean Close(String mapName);
}
