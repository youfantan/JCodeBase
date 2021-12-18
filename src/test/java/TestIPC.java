import org.junit.jupiter.api.Test;
import shandiankulishe.codes.base.err.StackTrace;
import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class TestIPC {
    @Test
    public void TestMemoryUtils(){
        System.load(new File("native.dll").getAbsolutePath());
        if (MemoryUtils.getINSTANCE().Alloc("test",1024)){
            ByteBuffer b=ByteBuffer.allocateDirect(1024);
            b.put("test ipc".getBytes(StandardCharsets.UTF_8));
            if (MemoryUtils.getINSTANCE().Write("test",b)){
                ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
                if (MemoryUtils.getINSTANCE().Read("test",buffer)){
                    CharBuffer cb= StandardCharsets.UTF_8.decode(buffer);
                    System.out.println(cb.toString());
                    if (MemoryUtils.getINSTANCE().Close("test"));
                }
            }
        }
        ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
        MemoryUtils.getINSTANCE().Write("testn",buffer);
        for (StackTrace trace:MemoryUtils.getINSTANCE().getLastErrors()
             ) {
            System.out.println(trace.toString());
        }
    }
}
