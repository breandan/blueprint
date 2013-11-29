package com.google.android.speech.grammar;

import android.util.Log;
import com.google.android.speech.embedded.Greco3DataManager;
import com.google.android.speech.embedded.Greco3DataManager.LocaleResources;
import com.google.android.speech.embedded.Greco3Grammar;
import com.google.android.speech.embedded.Greco3GrammarCompiler;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Preconditions;
import com.google.wireless.voicesearch.proto.GstaticConfiguration.LanguagePack;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class HandsFreeGrammarCompiler
{
  private final GrammarContactRetriever mContactRetriever;
  private final Greco3DataManager mGreco3DataManager;
  private final Greco3Grammar mGreco3Grammar;
  private final TextGrammarLoader mTextGrammarLoader;
  
  public HandsFreeGrammarCompiler(Greco3DataManager paramGreco3DataManager, GrammarContactRetriever paramGrammarContactRetriever, TextGrammarLoader paramTextGrammarLoader, Greco3Grammar paramGreco3Grammar)
  {
    this.mGreco3DataManager = paramGreco3DataManager;
    this.mContactRetriever = paramGrammarContactRetriever;
    this.mTextGrammarLoader = paramTextGrammarLoader;
    this.mGreco3Grammar = paramGreco3Grammar;
  }
  
  private String buildAbnfGrammar(Greco3DataManager.LocaleResources paramLocaleResources)
  {
    String str = paramLocaleResources.getLanguageMetadata().getBcp47Locale();
    if (!"en-US".equals(str)) {}
    StringBuilder localStringBuilder;
    for (;;)
    {
      return null;
      try
      {
        localStringBuilder = this.mTextGrammarLoader.get(this.mGreco3Grammar.getApkFullName(str), 524288);
        if (localStringBuilder != null) {
          if (this.mGreco3Grammar.isAddContacts())
          {
            List localList = this.mContactRetriever.getContacts();
            EventLogger.recordSpeechEvent(14, Integer.valueOf(localList.size()));
            ContactsGrammarBuilder localContactsGrammarBuilder = new ContactsGrammarBuilder();
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext()) {
              localContactsGrammarBuilder.addContact((GrammarContact)localIterator.next());
            }
            localContactsGrammarBuilder.append(localStringBuilder);
          }
        }
      }
      catch (IOException localIOException)
      {
        Log.e("VS.G3ContactsCompiler", "I/O Exception reading ABNF grammar: ", localIOException);
        return null;
      }
    }
    return localStringBuilder.toString();
  }
  
  private boolean compileGrammar(Greco3DataManager.LocaleResources paramLocaleResources, String paramString, File paramFile1, File paramFile2)
  {
    Greco3GrammarCompiler localGreco3GrammarCompiler = new Greco3GrammarCompiler(paramLocaleResources.getConfigFile(Greco3Mode.COMPILER), paramLocaleResources.getResourcePaths());
    if (!localGreco3GrammarCompiler.init()) {
      return false;
    }
    boolean bool = localGreco3GrammarCompiler.compile(paramString, paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath());
    localGreco3GrammarCompiler.delete();
    return bool;
  }
  
  public String buildGrammar(String paramString)
  {
    Preconditions.checkState(this.mGreco3DataManager.isInitialized());
    Greco3DataManager.LocaleResources localLocaleResources = this.mGreco3DataManager.getResources(paramString);
    if (localLocaleResources == null)
    {
      Log.e("VS.G3ContactsCompiler", "Grammar compilation failed, no resources for locale :" + paramString);
      return null;
    }
    return buildAbnfGrammar(localLocaleResources);
  }
  
  /* Error */
  public boolean compileGrammar(String paramString1, String paramString2, File paramFile1, File paramFile2)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 5
    //   3: aload_0
    //   4: monitorenter
    //   5: aload_0
    //   6: getfield 19	com/google/android/speech/grammar/HandsFreeGrammarCompiler:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
    //   9: invokevirtual 172	com/google/android/speech/embedded/Greco3DataManager:isInitialized	()Z
    //   12: invokestatic 178	com/google/common/base/Preconditions:checkState	(Z)V
    //   15: new 196	com/google/android/shared/util/StopWatch
    //   18: dup
    //   19: invokespecial 197	com/google/android/shared/util/StopWatch:<init>	()V
    //   22: astore 7
    //   24: aload 7
    //   26: invokevirtual 201	com/google/android/shared/util/StopWatch:start	()Lcom/google/android/shared/util/StopWatch;
    //   29: pop
    //   30: aload_0
    //   31: getfield 19	com/google/android/speech/grammar/HandsFreeGrammarCompiler:mGreco3DataManager	Lcom/google/android/speech/embedded/Greco3DataManager;
    //   34: aload_2
    //   35: invokevirtual 182	com/google/android/speech/embedded/Greco3DataManager:getResources	(Ljava/lang/String;)Lcom/google/android/speech/embedded/Greco3DataManager$LocaleResources;
    //   38: astore 9
    //   40: aload 9
    //   42: ifnonnull +33 -> 75
    //   45: ldc 114
    //   47: new 128	java/lang/StringBuilder
    //   50: dup
    //   51: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   54: ldc 185
    //   56: invokevirtual 188	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: aload_2
    //   60: invokevirtual 188	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   66: invokestatic 191	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   69: pop
    //   70: aload_0
    //   71: monitorexit
    //   72: iload 5
    //   74: ireturn
    //   75: iconst_0
    //   76: istore 5
    //   78: aload_1
    //   79: ifnull -9 -> 70
    //   82: aload 7
    //   84: invokevirtual 201	com/google/android/shared/util/StopWatch:start	()Lcom/google/android/shared/util/StopWatch;
    //   87: pop
    //   88: aload_0
    //   89: aload 9
    //   91: aload_1
    //   92: aload_3
    //   93: aload 4
    //   95: invokespecial 203	com/google/android/speech/grammar/HandsFreeGrammarCompiler:compileGrammar	(Lcom/google/android/speech/embedded/Greco3DataManager$LocaleResources;Ljava/lang/String;Ljava/io/File;Ljava/io/File;)Z
    //   98: istore 5
    //   100: iload 5
    //   102: ifeq -32 -> 70
    //   105: ldc 114
    //   107: new 128	java/lang/StringBuilder
    //   110: dup
    //   111: invokespecial 183	java/lang/StringBuilder:<init>	()V
    //   114: ldc 205
    //   116: invokevirtual 188	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: aload 7
    //   121: invokevirtual 208	com/google/android/shared/util/StopWatch:getElapsedTime	()I
    //   124: invokevirtual 211	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   127: ldc 213
    //   129: invokevirtual 188	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   135: invokestatic 216	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   138: pop
    //   139: goto -69 -> 70
    //   142: astore 6
    //   144: aload_0
    //   145: monitorexit
    //   146: aload 6
    //   148: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	149	0	this	HandsFreeGrammarCompiler
    //   0	149	1	paramString1	String
    //   0	149	2	paramString2	String
    //   0	149	3	paramFile1	File
    //   0	149	4	paramFile2	File
    //   1	100	5	bool	boolean
    //   142	5	6	localObject	Object
    //   22	98	7	localStopWatch	com.google.android.shared.util.StopWatch
    //   38	52	9	localLocaleResources	Greco3DataManager.LocaleResources
    // Exception table:
    //   from	to	target	type
    //   5	40	142	finally
    //   45	70	142	finally
    //   82	100	142	finally
    //   105	139	142	finally
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.grammar.HandsFreeGrammarCompiler
 * JD-Core Version:    0.7.0.1
 */