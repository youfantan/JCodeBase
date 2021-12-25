import org.junit.jupiter.api.Test;
import shandiankulishe.codes.base.CallBack;
import shandiankulishe.codes.base.net.parallel.RangedDownload;
import shandiankulishe.codes.base.net.parallel.RangedDownloadInfo;

public class TestNetwork {
    @Test
    public void TestRangedDownload() throws InterruptedException {
        String url="https://launcher.mojang.com/v1/objects/d49eb6caed53d23927648c97451503442f9e26fd/client.jar";
        String savePath="client.jar";
        int threadSize=64;
        RangedDownload download=new RangedDownload(url,savePath,threadSize);
        download.execute(new CallBack() {
            @Override
            public void call() {
                RangedDownloadInfo info=download.getInfo();
                System.out.println("Expected Downloaded:"+info.getExpectedDownloadLength());
                System.out.println("Total Downloaded:"+info.getTotalDownloadedLength());
                System.out.println("Speed:"+info.getSpeed());
                StringBuffer buffer=new StringBuffer();
                buffer.append("{\n");
                for (long len :
                        info.getThreadsLength()) {
                    buffer.append("\t");
                    buffer.append(len);
                    buffer.append(" bytes\n");
                }
                buffer.append("}");
                System.out.println("Threads Downloaded:"+buffer.toString());
            }
        });
    }
}
