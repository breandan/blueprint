package com.google.android.voicesearch.greco3;

import android.content.res.Resources;
import android.util.Log;
import com.google.android.speech.embedded.EndpointerModelCopier;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.common.base.Supplier;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BundledEndpointerModelCopier
  implements EndpointerModelCopier
{
  private static final String[] BUNDLED_EP_FILE_NAMES = { "ep_acoustic_model", "endpointer_dictation.config", "endpointer_voicesearch.config", "metadata" };
  private static final int[] BUNDLED_EP_RESOURCES = { 2131165190, 2131165188, 2131165189, 2131165192 };
  private final Resources mResources;
  
  public BundledEndpointerModelCopier(Resources paramResources)
  {
    this.mResources = paramResources;
  }
  
  private static boolean hasEndpointerResources(Greco3DataManager paramGreco3DataManager, String paramString)
  {
    return (paramGreco3DataManager.hasResources(paramString, Greco3Mode.ENDPOINTER_VOICESEARCH)) && (paramGreco3DataManager.hasResources(paramString, Greco3Mode.ENDPOINTER_DICTATION));
  }
  
  public boolean copyEndpointerModels(Supplier<File> paramSupplier, Greco3DataManager paramGreco3DataManager)
  {
    if (hasEndpointerResources(paramGreco3DataManager, "en-US")) {
      return false;
    }
    return doCopyModels(paramSupplier);
  }
  
  boolean doCopyModels(Supplier<File> paramSupplier)
  {
    File localFile = new File((File)paramSupplier.get(), "en-US");
    if ((!localFile.exists()) && (!localFile.mkdir()))
    {
      Log.e("VS.EPModelCopier", "Unable to create model dir: " + localFile.getAbsolutePath());
      return false;
    }
    int i = 0;
    try
    {
      while (i < BUNDLED_EP_RESOURCES.length)
      {
        ByteStreams.copy(this.mResources.openRawResource(BUNDLED_EP_RESOURCES[i]), new FileOutputStream(new File(localFile, BUNDLED_EP_FILE_NAMES[i])));
        i++;
      }
      return true;
    }
    catch (IOException localIOException)
    {
      Log.e("VS.EPModelCopier", "Error copying EP models: " + localIOException);
    }
    return false;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.BundledEndpointerModelCopier
 * JD-Core Version:    0.7.0.1
 */