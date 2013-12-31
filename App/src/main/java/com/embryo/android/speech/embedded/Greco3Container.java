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
    private final Greco3Preferences mGreco3Preferences;

    private Greco3Container(Greco3DataManager paramGreco3DataManager, Greco3EngineManager paramGreco3EngineManager, Greco3Preferences paramGreco3Preferences, Supplier<Integer> paramSupplier) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mGreco3EngineManager = paramGreco3EngineManager;
        this.mGreco3Preferences = paramGreco3Preferences;
        this.mDeviceClassSupplier = paramSupplier;
    }

    public static Greco3Container create(Context paramContext, SharedPreferences paramSharedPreferences, ExecutorService paramExecutorService, Executor paramExecutor) {
        DeviceClassSupplier localDeviceClassSupplier = new DeviceClassSupplier(paramContext);
        Greco3Preferences localGreco3Preferences = new Greco3Preferences(paramSharedPreferences);
        Greco3DataManager localGreco3DataManager = new Greco3DataManager(paramContext, localGreco3Preferences, SUPPORTED_FORMAT_VERSIONS, paramExecutorService, paramExecutor);
        Greco3EngineManager localGreco3EngineManager = new Greco3EngineManager(localGreco3DataManager, localGreco3Preferences, new com.embryo.android.voicesearch.greco3.BundledEndpointerModelCopier(paramContext.getResources()));
        localGreco3DataManager.setPathDeleter(localGreco3EngineManager);
        return new Greco3Container(localGreco3DataManager, localGreco3EngineManager, localGreco3Preferences, localDeviceClassSupplier);
    }

    public Supplier<Integer> getDeviceClassSupplier() {
        return this.mDeviceClassSupplier;
    }

    public Greco3DataManager getGreco3DataManager() {
        return this.mGreco3DataManager;
    }

    public Greco3EngineManager getGreco3EngineManager() {
        return this.mGreco3EngineManager;
    }

    public Greco3Preferences getGreco3Preferences() {
        return this.mGreco3Preferences;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Container

 * JD-Core Version:    0.7.0.1

 */