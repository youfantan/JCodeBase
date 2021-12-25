package shandiankulishe.codes.base.ipc;

import shandiankulishe.codes.base.err.StackTrace;
import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

public class MemoryException extends Exception{
    public MemoryException() {
        super();
    }

    public MemoryException(String message) {
        super(message);
    }
}
