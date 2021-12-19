package shandiankulishe.codes.base.ipc;

import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

public class MemoryException extends Exception{
    public MemoryException() {
        super();
        MemoryUtils.getINSTANCE().PrintStackTraces();
    }

    public MemoryException(String message) {
        super(message);
        MemoryUtils.getINSTANCE().PrintStackTraces();
    }
}
