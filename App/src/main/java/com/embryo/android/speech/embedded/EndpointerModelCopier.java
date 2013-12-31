package com.embryo.android.speech.embedded;

import com.google.common.base.Supplier;

import java.io.File;

public abstract interface EndpointerModelCopier {
    public abstract boolean copyEndpointerModels(Supplier<File> paramSupplier, Greco3DataManager paramGreco3DataManager);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     EndpointerModelCopier

 * JD-Core Version:    0.7.0.1

 */