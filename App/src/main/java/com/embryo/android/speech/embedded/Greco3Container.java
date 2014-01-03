package com.embryo.android.speech.embedded;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.base.Supplier;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class Greco3Container {
    public static final int[] SUPPORTED_FORMAT_VERSIONS = {2, 3, 4, 5, 6};
    private final Supplier<Integer> mDeviceClassSupplier;
    private final Greco3DataManager mGreco3DataManager;
    private final Greco3EngineManager mGreco3EngineManager;

    private Greco3Container(Greco3DataManager paramGreco3DataManager, Greco3EngineManager paramGreco3EngineManager, Supplier<Integer> paramSupplier) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mGreco3EngineManager = paramGreco3EngineManager;
        this.mDeviceClassSupplier = paramSupplier;
    }

    public static Greco3Container create(Context paramContext, ExecutorService paramExecutorService, Executor paramExecutor) {
        DeviceClassSupplier localDeviceClassSupplier = new DeviceClassSupplier(paramContext);
        Greco3DataManager localGreco3DataManager = new Greco3DataManager(paramContext, null, SUPPORTED_FORMAT_VERSIONS, paramExecutorService, paramExecutor);
        Greco3EngineManager localGreco3EngineManager = new Greco3EngineManager(localGreco3DataManager, null, new com.embryo.android.voicesearch.greco3.BundledEndpointerModelCopier(paramContext.getResources()));
        localGreco3DataManager.setPathDeleter(localGreco3EngineManager);
        return new Greco3Container(localGreco3DataManager, localGreco3EngineManager, localDeviceClassSupplier);
    }

    public Greco3EngineManager getGreco3EngineManager() {
        return this.mGreco3EngineManager;
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Container

 * JD-Core Version:    0.7.0.1

 */