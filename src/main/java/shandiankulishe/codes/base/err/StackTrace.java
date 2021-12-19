package shandiankulishe.codes.base.err;

public class StackTrace {
    public String getTraceName() {
        return TraceName;
    }

    public void setTraceName(String traceName) {
        TraceName = traceName;
    }

    public String getTraceMessage() {
        return TraceMessage;
    }

    public void setTraceMessage(String traceMessage) {
        TraceMessage = traceMessage;
    }

    public long getProcessID() {
        return ProcessID;
    }

    public void setProcessID(long processID) {
        ProcessID = processID;
    }

    public long getThreadID() {
        return ThreadID;
    }

    public void setThreadID(long threadID) {
        ThreadID = threadID;
    }

    public StackTrace(String traceName, String traceMessage, long processID, long threadID) {
        TraceName = traceName;
        TraceMessage = traceMessage;
        ProcessID = processID;
        ThreadID = threadID;
    }

    private String TraceName;
    private String TraceMessage;
    private long ProcessID;
    private long ThreadID;
    @Override
    public String toString(){
        String str= """
                ---- S T A C K  T R A C E ----
                STACK TRACE NAME:%s
                STACK TRACE MESSAGE:%s
                PROCESS ID:%d
                THREAD ID:%d
                ---- E  N  D ----
                """;
        return String.format(str,getTraceName(),getTraceMessage(),getProcessID(),getThreadID());
    }
}
