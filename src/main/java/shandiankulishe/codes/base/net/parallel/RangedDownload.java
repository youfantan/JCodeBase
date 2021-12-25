package shandiankulishe.codes.base.net.parallel;

import shandiankulishe.codes.base.CallBack;
import shandiankulishe.codes.base.net.NetworkException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RangedDownload {
    private RangedDownloadInfo info=new RangedDownloadInfo();
    private String url;
    private String savePath;
    private int threadSize;
    private WorkThread[] workThreads;
    private long previousLength=0;
    private long getContentLength(){
        try {
            HttpURLConnection connection= (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.0 Safari/537.36");
            connection.setReadTimeout(10000);
            connection.setInstanceFollowRedirects(true);
            if (connection.getResponseCode()!=200){
                throw new NetworkException("Error Response Code");
            }
            return connection.getContentLength();
        } catch (IOException | NetworkException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public RangedDownload(String url, String savePath, int threadSize) {
        this.url = url;
        this.savePath = savePath;
        this.threadSize = threadSize;
        workThreads=new WorkThread[threadSize];
        long contentLength=getContentLength();
        info.setExpectedDownloadLength(contentLength);
        //分配线程
        long range=contentLength/threadSize;
        try {
            RandomAccessFile access=new RandomAccessFile(new File(savePath),"rw");
            for (int i = 0; i < threadSize; i++) {
                long start=i*range;
                long end=(i+1)*range-1;
                if (i==threadSize-1){
                    end=contentLength;
                }
                HttpURLConnection connection= (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.0 Safari/537.36");
                connection.setRequestProperty("Range","bytes="+start+"-"+end);
                connection.setReadTimeout(10000);
                connection.setInstanceFollowRedirects(true);
                RandomAccessFile f=new RandomAccessFile(new File(savePath),"rw");
                f.seek(start);
                workThreads[i]=new WorkThread(connection,f);
                workThreads[i].setName(String.valueOf(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void execute(CallBack callBack) throws InterruptedException {
        updateDownloadInfo();
        for (WorkThread td :
                workThreads) {
            td.start();
        }
        boolean flag=true;
        while (flag){
            flag=false;
            for (int i = 0; i < threadSize; i++) {
                flag=!workThreads[i].getFlag();
            }
            Thread.sleep(1000);
            updateDownloadInfo();
            callBack.call();
        }
    }
    public void executeAsync(CallBack callBack){
        new Thread(()->{
            try {
                execute(callBack);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void updateDownloadInfo(){
        long total=0;
        List<Long> lengthList=new ArrayList<>();
        for (WorkThread td :
             workThreads) {
            if (td==null){
                lengthList.add(0L);
            } else {
                lengthList.add(Integer.parseInt(td.getName()),td.getDownloaded());
                total+=td.getDownloaded();
            }
        }
        info.setThreadsLength(unwrapLongArray(lengthList.stream().toArray(Long[]::new)));
        long downloadedInPreviousSec=total-previousLength;
        BigDecimal speed=new BigDecimal(downloadedInPreviousSec);
        speed=speed.divide(new BigDecimal(1024));
        info.setSpeed(speed.doubleValue());
        info.setTotalDownloadedLength(total);
    }

    public RangedDownloadInfo getInfo() {
        return info;
    }
    private long[] unwrapLongArray(Long[] a){
        long[] a1=new long[a.length];
        for (int i = 0; i < a.length; i++) {
            a1[i]=a[i];
        }
        return a1;
    }
}
