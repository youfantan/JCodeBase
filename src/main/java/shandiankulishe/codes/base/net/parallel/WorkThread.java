package shandiankulishe.codes.base.net.parallel;

import shandiankulishe.codes.base.err.StackTrace;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;

public class WorkThread extends Thread{
    public WorkThread(HttpURLConnection connection, RandomAccessFile out) {
        this.connection = connection;
        this.out = out;
    }

    private long downloaded;
    public long getDownloaded() {
        return downloaded;
    }
    private boolean flag=false;

    public boolean getFlag() {
        return flag;
    }

    private HttpURLConnection connection;
    private RandomAccessFile out;
    @Override
    public void run() {
        try {
            BufferedInputStream in=new BufferedInputStream(connection.getInputStream());
            int len;
            downloaded=0;
            byte[] buffer=new byte[256];
            while ((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
                downloaded+=256;
            }
            out.close();
            in.close();
            flag=true;
        } catch (IOException e) {
            e.printStackTrace();
            StackTrace trace=new StackTrace("Java IOException","WorkThread IOException(JVM THREAD ID)", Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]),Thread.currentThread().getId());
        }

    }
}
