import org.junit.jupiter.api.Test;
import shandiankulishe.codes.base.err.StackTrace;
import shandiankulishe.codes.base.ipc.MemoryException;
import shandiankulishe.codes.base.ipc.MemoryMapping;
import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class TestIPC {
    private void print(ByteBuffer buffer) {
        System.out.printf("position: %d, limit: %d, capacity: %d\n",
                buffer.position(), buffer.limit(), buffer.capacity());
    }
    @Test
    public void TestMemoryUtils(){
        System.load(new File("native.dll").getAbsolutePath());
        MemoryUtils.getINSTANCE().Alloc("test",1024);
        MemoryUtils.getINSTANCE().Alloc("test",1024);//for test stack trace
        MemoryUtils.getINSTANCE().Write("test",ByteBuffer.allocateDirect(1024).put("test memory utils".getBytes(StandardCharsets.UTF_8)));
        ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
        MemoryUtils.getINSTANCE().Read("test",buffer);
        //CharBuffer c= StandardCharsets.UTF_8.decode(buffer);
        //System.out.println(c.length());
        print(buffer);
        buffer.reset();
        print(buffer);
        buffer.flip();
        print(buffer);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        while (buffer.hasRemaining()){
            baos.write(buffer.get());
        }
        System.out.println(baos.toString(StandardCharsets.UTF_8));
        MemoryUtils.getINSTANCE().Write("test",ByteBuffer.allocateDirect(1024).put("test memory utils twice".getBytes(StandardCharsets.UTF_8)));
        buffer=ByteBuffer.allocateDirect(1024);
        MemoryUtils.getINSTANCE().Read("test",buffer);
        print(buffer);
        buffer.reset();
        print(buffer);
        buffer.flip();
        print(buffer);
        baos=new ByteArrayOutputStream();
        while (buffer.hasRemaining()){
            baos.write(buffer.get());
        }
        System.out.println(baos.toString(StandardCharsets.UTF_8));
        for (StackTrace trace:MemoryUtils.getINSTANCE().getLastErrors()
             ) {
            System.out.println(trace.toString());
        }
        MemoryUtils.getINSTANCE().Close("test");
    }
    @Test
    public void TestMemoryMappings() throws MemoryException {
        System.load(new File("native.dll").getAbsolutePath());
        MemoryMapping mapping=new MemoryMapping("test",1024);
        mapping.write("test");
        mapping.append(" ipc");
        System.out.println(Arrays.toString(mapping.getContent()));
        mapping.release();
    }
}
