import org.junit.jupiter.api.Test;
import shandiankulishe.codes.base.err.StackTrace;
import shandiankulishe.codes.base.ipc.MemoryException;
import shandiankulishe.codes.base.ipc.MemoryMapping;
import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class TestIPC {
    private void debugByteBuffer(ByteBuffer buffer) {
        System.out.printf("position: %d, limit: %d, capacity: %d\n",
                buffer.position(), buffer.limit(), buffer.capacity());
    }
    @Test
    public void TestMemoryUtils(){
        System.load(new File("native.dll").getAbsolutePath());
        MemoryUtils.getINSTANCE().Alloc("test",1024);
        MemoryUtils.getINSTANCE().Write("test","test memory utils".getBytes(StandardCharsets.UTF_8));
        ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
        MemoryUtils.getINSTANCE().Read("test",buffer);
        debugByteBuffer(buffer);
        buffer.reset();
        debugByteBuffer(buffer);
        buffer.flip();
        debugByteBuffer(buffer);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        while (buffer.hasRemaining()){
            baos.write(buffer.get());
        }
        System.out.println(baos.toString(StandardCharsets.UTF_8));
        MemoryUtils.getINSTANCE().Write("test","test memory utils twice".getBytes(StandardCharsets.UTF_8));
        buffer=ByteBuffer.allocateDirect(1024);
        MemoryUtils.getINSTANCE().Read("test",buffer);
        debugByteBuffer(buffer);
        buffer.reset();
        debugByteBuffer(buffer);
        buffer.flip();
        debugByteBuffer(buffer);
        baos=new ByteArrayOutputStream();
        while (buffer.hasRemaining()){
            baos.write(buffer.get());
        }
        System.out.println(baos.toString(StandardCharsets.UTF_8));
        MemoryUtils.getINSTANCE().Close("test");
        for (StackTrace trace:MemoryUtils.getINSTANCE().getLastErrors()
        ) {
            System.out.println(trace.toString());
        }
    }
    @Test
    public void TestMemoryMappings() throws MemoryException {
        System.load(new File("native.dll").getAbsolutePath());
        MemoryMapping mapping=new MemoryMapping("test",1024);
        mapping.write("test");
        System.out.println(new String(mapping.getContent()));
        mapping.append(" ipc");
        System.out.println(new String(mapping.getContent()));
        mapping.write("clear");
        System.out.println(new String(mapping.getContent()));
        mapping.release();
        for (StackTrace trace:MemoryUtils.getINSTANCE().getLastErrors()
        ) {
            System.out.println(trace.toString());
        }
    }
}
