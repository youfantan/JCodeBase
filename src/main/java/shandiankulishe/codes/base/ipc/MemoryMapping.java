package shandiankulishe.codes.base.ipc;

import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MemoryMapping {
    private String mappingName;
    private long size;
    private ByteBuffer buffer;
    public MemoryMapping(String mappingName,long size){
        this.mappingName=mappingName;
        this.size=size;
        //alloc memory
        MemoryUtils.getINSTANCE().Alloc(mappingName, size);
        buffer=ByteBuffer.allocate((int)size);
    }
    public String getMappingName(){
        return mappingName;
    }

    public long getSize() {
        return size;
    }
    public byte[] getContent() throws MemoryException {
        buffer.clear();
        //get available bytes
        if (!MemoryUtils.getINSTANCE().Read(mappingName,buffer)){
            throw new MemoryException();
        }
        buffer.reset();
        buffer.flip();
        ByteArrayOutputStream o=new ByteArrayOutputStream();
        while (buffer.hasRemaining()){
            o.write(buffer.get());
        }
        return o.toByteArray();
    }
    public String getString() throws MemoryException {
        return new String(getContent(), StandardCharsets.UTF_8);
    }
    public void write(byte[] content) throws MemoryException {
        if (!MemoryUtils.getINSTANCE().Write(mappingName,content)){
            throw new MemoryException();
        }
    }
    public void write(String content) throws MemoryException {
        write(content.getBytes(StandardCharsets.UTF_8));
    }
    public void append(byte[] content) throws MemoryException {
        ByteArrayOutputStream o=new ByteArrayOutputStream();
        try {
            o.write(getContent());
            o.write(content);
        } catch (IOException e){
            e.printStackTrace();
        }
        write(o.toByteArray());
    }
    public void append(String content) throws MemoryException {
        append(content.getBytes(StandardCharsets.UTF_8));
    }
    public void release() throws MemoryException {
        if (!MemoryUtils.getINSTANCE().Close(mappingName)){
            throw new MemoryException();
        }
    }
}
