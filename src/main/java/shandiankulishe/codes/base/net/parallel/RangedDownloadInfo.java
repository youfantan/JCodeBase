package shandiankulishe.codes.base.net.parallel;

public class RangedDownloadInfo {
    public RangedDownloadInfo(){}
    public RangedDownloadInfo(long totalDownloadedLength, long expectedDownloadLength, long[] threadsLength, double speed) {
        this.totalDownloadedLength = totalDownloadedLength;
        this.expectedDownloadLength = expectedDownloadLength;
        this.threadsLength = threadsLength;
        this.speed = speed;
    }

    public long getTotalDownloadedLength() {
        return totalDownloadedLength;
    }

    public void setTotalDownloadedLength(long totalDownloadedLength) {
        this.totalDownloadedLength = totalDownloadedLength;
    }

    public long getExpectedDownloadLength() {
        return expectedDownloadLength;
    }

    public void setExpectedDownloadLength(long expectedDownloadLength) {
        this.expectedDownloadLength = expectedDownloadLength;
    }

    public long[] getThreadsLength() {
        return threadsLength;
    }

    public void setThreadsLength(long[] threadsLength) {
        this.threadsLength = threadsLength;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    private long totalDownloadedLength;
    private long expectedDownloadLength;
    private long[] threadsLength;
    private double speed;
}
