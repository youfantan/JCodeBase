import org.junit.jupiter.api.Test;
import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.io.File;

public class TestIPC {
    @Test
    public void TestMemoryUtils(){
        System.load(new File("native.dll").getAbsolutePath());
        MemoryUtils utils=new MemoryUtils();
        utils.Alloc("Test");
        utils.Write("Test","Test Str");
        System.out.println(Thread.currentThread().getName()+":"+utils.Read("Test"));
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+":"+utils.Read("Test"));
        }).start();
        utils.Close("Test");
    }
}
