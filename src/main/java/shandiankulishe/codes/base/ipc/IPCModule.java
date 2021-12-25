package shandiankulishe.codes.base.ipc;

public class IPCModule {
    private static boolean enableDaemon;
    public static void setEnableDaemon(boolean enableDaemon) {
        IPCModule.enableDaemon = enableDaemon;
        if (!IPCDaemon.getINSTANCE().started &&enableDaemon){
            IPCDaemon.getINSTANCE().startDaemon();
        }
    }
}
