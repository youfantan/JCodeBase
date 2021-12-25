package shandiankulishe.codes.base.ipc;

import shandiankulishe.codes.base.ipc.nativeImpl.MemoryUtils;

import java.util.ArrayList;
import java.util.List;

public class IPCDaemon extends Thread{
    private static final IPCDaemon INSTANCE=new IPCDaemon();
    public static IPCDaemon getINSTANCE(){
        return INSTANCE;
    }
    public boolean started=false;
    private List<String> mappings=new ArrayList<>();
    void push(String mappingName){
        mappings.add(mappingName);
    }
    void erase(String mappingName){
        mappings.remove(mappings.indexOf(mappingName));
    }
    void startDaemon(){
        started=true;
        Runtime.getRuntime().addShutdownHook(this);
    }
    @Override
    public void run() {
        for (int i = 0; i < mappings.size(); i++) {
            MemoryUtils.getINSTANCE().Close(mappings.get(i));
            System.out.println("Cleaned Mapping:"+mappings.get(i));
        }
    }
}
