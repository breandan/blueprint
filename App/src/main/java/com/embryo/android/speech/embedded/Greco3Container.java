package com.embryo.android.speech.embedded;

import android.content.Context;

import com.embryo.android.search.core.preferences.SharedPreferencesProto;
import com.google.common.base.Supplier;
import com.google.speech.embedded.Greco3EngineManager;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class Greco3Container {
    public static final int[] SUPPORTED_FORMAT_VERSIONS = {2, 3, 4, 5, 6};
    private final Supplier<Integer> mDeviceClassSupplier;
    private final Greco3DataManager mGreco3DataManager;
    private final Greco3EngineManager mGreco3EngineManager;
    private final Greco3Preferences mGreco3Preferences;

    private Greco3Container(Greco3DataManager paramGreco3DataManager, Greco3EngineManager paramGreco3EngineManager, Greco3Preferences preferences, Supplier<Integer> paramSupplier) {
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mGreco3EngineManager = paramGreco3EngineManager;
        mGreco3Preferences = preferences;
        this.mDeviceClassSupplier = paramSupplier;
    }

    public static Greco3Container create(Context paramContext, ExecutorService paramExecutorService, Executor paramExecutor) {
        DeviceClassSupplier localDeviceClassSupplier = new DeviceClassSupplier(paramContext);
        Greco3Preferences greco3Preferences = new Greco3Preferences(new SharedPreferencesProto(new File(paramContext.getDir("shared_prefs", 0), "StartupSettings.bin")));
        Greco3DataManager localGreco3DataManager = new Greco3DataManager(paramContext, greco3Preferences, SUPPORTED_FORMAT_VERSIONS, paramExecutorService, paramExecutor);
        Greco3EngineManager localGreco3EngineManager = new Greco3EngineManager(localGreco3DataManager, null, new com.embryo.android.voicesearch.greco3.BundledEndpointerModelCopier(paramContext.getResources()));
        localGreco3DataManager.setPathDeleter(localGreco3EngineManager);
        return new Greco3Container(localGreco3DataManager, localGreco3EngineManager, greco3Preferences, localDeviceClassSupplier);
    }

    public Greco3EngineManager getGreco3EngineManager() {
        return this.mGreco3EngineManager;
    }

    public Greco3DataManager getGreco3DataManager() {
        return mGreco3DataManager;
    }

    public Greco3Preferences getGreco3Preferences() {
        return mGreco3Preferences;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Greco3Container

 * JD-Core Version:    0.7.0.1

 */