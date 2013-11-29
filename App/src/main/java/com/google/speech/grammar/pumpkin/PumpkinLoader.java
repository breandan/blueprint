package com.google.speech.grammar.pumpkin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public abstract class PumpkinLoader
{
  protected static ActionFrameManager actionFrameManager;
  protected static final Logger logger = Logger.getLogger(PumpkinLoader.class.getName());
  protected final String actionPath;
  protected final String grmPath;
  @Nullable
  protected File pumpkinConfig;
  @Nullable
  protected byte[] pumpkinConfigBytes;
  protected Tagger tagger;
  protected UserValidators userValidators;
  
  public PumpkinLoader(String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    this.actionPath = paramString1;
    this.grmPath = paramString2;
    this.pumpkinConfig = null;
    this.pumpkinConfigBytes = paramArrayOfByte;
  }
  
  public Tagger getTagger()
  {
    return this.tagger;
  }
  
  public UserValidators getUserValidators()
  {
    return this.userValidators;
  }
  
  /* Error */
  public void init()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic 61	com/google/speech/grammar/pumpkin/PumpkinLoader:actionFrameManager	Lcom/google/speech/grammar/pumpkin/ActionFrameManager;
    //   5: ifnonnull +13 -> 18
    //   8: new 63	com/google/speech/grammar/pumpkin/ActionFrameManager
    //   11: dup
    //   12: invokespecial 64	com/google/speech/grammar/pumpkin/ActionFrameManager:<init>	()V
    //   15: putstatic 61	com/google/speech/grammar/pumpkin/PumpkinLoader:actionFrameManager	Lcom/google/speech/grammar/pumpkin/ActionFrameManager;
    //   18: aload_0
    //   19: getfield 46	com/google/speech/grammar/pumpkin/PumpkinLoader:pumpkinConfig	Ljava/io/File;
    //   22: ifnull +49 -> 71
    //   25: aload_0
    //   26: invokevirtual 68	com/google/speech/grammar/pumpkin/PumpkinLoader:loadPumpkinConfig	()Lcom/google/speech/grammar/pumpkin/PumpkinConfigProto$PumpkinConfig;
    //   29: astore_2
    //   30: aload_0
    //   31: getfield 52	com/google/speech/grammar/pumpkin/PumpkinLoader:tagger	Lcom/google/speech/grammar/pumpkin/Tagger;
    //   34: ifnonnull +15 -> 49
    //   37: aload_0
    //   38: new 70	com/google/speech/grammar/pumpkin/Tagger
    //   41: dup
    //   42: aload_2
    //   43: invokespecial 73	com/google/speech/grammar/pumpkin/Tagger:<init>	(Lcom/google/speech/grammar/pumpkin/PumpkinConfigProto$PumpkinConfig;)V
    //   46: putfield 52	com/google/speech/grammar/pumpkin/PumpkinLoader:tagger	Lcom/google/speech/grammar/pumpkin/Tagger;
    //   49: aload_0
    //   50: getfield 56	com/google/speech/grammar/pumpkin/PumpkinLoader:userValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
    //   53: ifnonnull +15 -> 68
    //   56: aload_0
    //   57: new 75	com/google/speech/grammar/pumpkin/UserValidators
    //   60: dup
    //   61: aload_2
    //   62: invokespecial 76	com/google/speech/grammar/pumpkin/UserValidators:<init>	(Lcom/google/speech/grammar/pumpkin/PumpkinConfigProto$PumpkinConfig;)V
    //   65: putfield 56	com/google/speech/grammar/pumpkin/PumpkinLoader:userValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: aload_0
    //   72: getfield 52	com/google/speech/grammar/pumpkin/PumpkinLoader:tagger	Lcom/google/speech/grammar/pumpkin/Tagger;
    //   75: ifnonnull +18 -> 93
    //   78: aload_0
    //   79: new 70	com/google/speech/grammar/pumpkin/Tagger
    //   82: dup
    //   83: aload_0
    //   84: getfield 48	com/google/speech/grammar/pumpkin/PumpkinLoader:pumpkinConfigBytes	[B
    //   87: invokespecial 79	com/google/speech/grammar/pumpkin/Tagger:<init>	([B)V
    //   90: putfield 52	com/google/speech/grammar/pumpkin/PumpkinLoader:tagger	Lcom/google/speech/grammar/pumpkin/Tagger;
    //   93: aload_0
    //   94: getfield 56	com/google/speech/grammar/pumpkin/PumpkinLoader:userValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
    //   97: ifnonnull -29 -> 68
    //   100: aload_0
    //   101: new 75	com/google/speech/grammar/pumpkin/UserValidators
    //   104: dup
    //   105: aload_0
    //   106: getfield 48	com/google/speech/grammar/pumpkin/PumpkinLoader:pumpkinConfigBytes	[B
    //   109: invokespecial 80	com/google/speech/grammar/pumpkin/UserValidators:<init>	([B)V
    //   112: putfield 56	com/google/speech/grammar/pumpkin/PumpkinLoader:userValidators	Lcom/google/speech/grammar/pumpkin/UserValidators;
    //   115: goto -47 -> 68
    //   118: astore_1
    //   119: aload_0
    //   120: monitorexit
    //   121: aload_1
    //   122: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	123	0	this	PumpkinLoader
    //   118	4	1	localObject	Object
    //   29	33	2	localPumpkinConfig	PumpkinConfigProto.PumpkinConfig
    // Exception table:
    //   from	to	target	type
    //   2	18	118	finally
    //   18	49	118	finally
    //   49	68	118	finally
    //   71	93	118	finally
    //   93	115	118	finally
  }
  
  protected abstract PumpkinConfigProto.PumpkinConfig loadPumpkinConfig()
    throws IOException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.grammar.pumpkin.PumpkinLoader
 * JD-Core Version:    0.7.0.1
 */