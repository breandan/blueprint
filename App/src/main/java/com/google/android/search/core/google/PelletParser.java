package com.google.android.search.core.google;

import android.util.JsonToken;
import android.util.Log;
import com.google.android.search.core.util.JsonUtf8Reader;
import com.google.android.search.core.util.JsonUtf8Reader.StringRef;
import com.google.android.shared.util.Util;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PelletParser
{
  private static final byte[] ANSWER_IN_SRP_KEY = { 101, 99, 116, 97, 105, 115 };
  private static final byte[] CARD_METADATA_KEY = { 101, 99, 116, 99, 109 };
  private final InputStream mIn;
  private JsonUtf8Reader mJsonReader;
  
  public PelletParser(InputStream paramInputStream)
  {
    Preconditions.checkNotNull(paramInputStream);
    this.mIn = paramInputStream;
  }
  
  private void init()
    throws IOException
  {
    if (this.mJsonReader == null)
    {
      this.mJsonReader = new JsonUtf8Reader(this.mIn);
      this.mJsonReader.setLenient(true);
    }
  }
  
  private Pellet parsePellet()
    throws IOException
  {
    init();
    if (!skipToNextJsonObject()) {}
    String str1;
    String str2;
    byte[] arrayOfByte1;
    byte[] arrayOfByte2;
    int i;
    int j;
    do
    {
      return null;
      this.mJsonReader.beginObject();
      str1 = null;
      str2 = null;
      arrayOfByte1 = null;
      arrayOfByte2 = null;
      i = 0;
      j = -1;
      while (this.mJsonReader.hasNext())
      {
        JsonUtf8Reader.StringRef localStringRef1 = this.mJsonReader.nextNameRef();
        if (localStringRef1.contentEquals((byte)101))
        {
          str1 = this.mJsonReader.nextString();
        }
        else if (localStringRef1.contentEquals((byte)99))
        {
          j = this.mJsonReader.nextInt();
        }
        else if (localStringRef1.contentEquals((byte)117))
        {
          str2 = this.mJsonReader.nextString();
        }
        else if (localStringRef1.contentEquals((byte)100))
        {
          arrayOfByte1 = this.mJsonReader.nextStringBytes();
        }
        else if (localStringRef1.contentEquals((byte)97))
        {
          try
          {
            this.mJsonReader.beginObject();
            while (this.mJsonReader.hasNext())
            {
              JsonUtf8Reader.StringRef localStringRef2 = this.mJsonReader.nextNameRef();
              if (localStringRef2.contentEquals(CARD_METADATA_KEY)) {
                arrayOfByte2 = this.mJsonReader.nextStringBytes();
              } else if (localStringRef2.contentEquals(ANSWER_IN_SRP_KEY)) {
                i = this.mJsonReader.nextInt();
              } else {
                this.mJsonReader.skipValue();
              }
            }
          }
          catch (IOException localIOException)
          {
            Log.e("Search.PelletParser", "Could not parse pellet 'a' field", localIOException);
          }
          this.mJsonReader.endObject();
        }
        else
        {
          this.mJsonReader.skipValue();
        }
      }
      this.mJsonReader.endObject();
    } while ((str1 == null) || (j == -1) || (str2 == null) || (arrayOfByte1 == null));
    return new Pellet(str1, j, str2, arrayOfByte1, arrayOfByte2, i);
  }
  
  private boolean skipToNextJsonObject()
    throws IOException
  {
    for (;;)
    {
      JsonToken localJsonToken = this.mJsonReader.peek();
      if ((localJsonToken == null) || (localJsonToken == JsonToken.END_DOCUMENT)) {
        return false;
      }
      if (localJsonToken == JsonToken.BEGIN_OBJECT) {
        return true;
      }
      Log.w("Search.PelletParser", "Unexpected JSON token. Expected pellet, but got " + localJsonToken);
      this.mJsonReader.skipValue();
    }
  }
  
  public Pellet read()
    throws IOException
  {
    try
    {
      Pellet localPellet = parsePellet();
      return localPellet;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      BugLogger.record(8543654);
      throw new IOException("Bad escape sequence found by JSON parser", localNumberFormatException);
    }
  }
  
  public static class Pellet
  {
    public final boolean mAnswerInSrp;
    @Nullable
    public final byte[] mCardMetadata;
    @Nonnull
    public final byte[] mData;
    @Nonnull
    public final String mId;
    public final boolean mMorePelletsToFollow;
    @Nonnull
    public final String mUrl;
    
    public Pellet(String paramString1, int paramInt1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
    {
      this.mId = ((String)Preconditions.checkNotNull(paramString1));
      this.mUrl = ((String)Preconditions.checkNotNull(paramString2));
      this.mData = ((byte[])Preconditions.checkNotNull(paramArrayOfByte1));
      int j;
      if (paramInt1 == i)
      {
        j = i;
        this.mMorePelletsToFollow = j;
        this.mCardMetadata = paramArrayOfByte2;
        if (paramInt2 <= 0) {
          break label81;
        }
      }
      for (;;)
      {
        this.mAnswerInSrp = i;
        return;
        j = 0;
        break;
        label81:
        i = 0;
      }
    }
    
    public String getJsonStringForTests()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("{");
      localStringBuilder1.append("\"e\":\"").append(this.mId).append("\",");
      StringBuilder localStringBuilder2 = localStringBuilder1.append("\"c\":\"");
      if (this.mMorePelletsToFollow) {}
      for (String str = "1";; str = "0")
      {
        localStringBuilder2.append(str).append("\",");
        localStringBuilder1.append("\"u\":\"").append(this.mUrl).append("\",");
        localStringBuilder1.append("\"d\":\"").append(new String(this.mData, Util.UTF_8)).append("\",");
        localStringBuilder1.append("\"a\":{\"_comment\":\"metadata omitted\"}");
        localStringBuilder1.append("}");
        return localStringBuilder1.toString();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PelletParser
 * JD-Core Version:    0.7.0.1
 */