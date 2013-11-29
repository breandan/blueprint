package com.google.android.speech.grammar;

import android.content.res.Resources;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.Nullable;

public class TextGrammarLoader
{
  private final String mPackageName;
  private final Resources mResources;
  
  public TextGrammarLoader(Resources paramResources, String paramString)
  {
    this.mResources = paramResources;
    this.mPackageName = paramString;
  }
  
  @Nullable
  public StringBuilder get(String paramString, int paramInt)
    throws IOException
  {
    int i = this.mResources.getIdentifier(paramString, "raw", this.mPackageName);
    if (i != 0)
    {
      StringBuilder localStringBuilder = new StringBuilder(paramInt);
      localObject1 = null;
      try
      {
        localInputStreamReader = new InputStreamReader(this.mResources.openRawResource(i));
        Closeables.closeQuietly((Closeable)localObject1);
      }
      finally
      {
        try
        {
          CharStreams.copy(localInputStreamReader, localStringBuilder);
          Closeables.closeQuietly(localInputStreamReader);
          return localStringBuilder;
        }
        finally
        {
          for (;;)
          {
            InputStreamReader localInputStreamReader;
            localObject1 = localInputStreamReader;
          }
        }
        localObject2 = finally;
      }
      throw localObject2;
    }
    else
    {
      return null;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.grammar.TextGrammarLoader
 * JD-Core Version:    0.7.0.1
 */