package com.android.ex.photo.util;

import android.util.Log;
import java.io.InputStream;

public class Exif
{
  public static int getOrientation(InputStream paramInputStream, long paramLong)
  {
    if (paramInputStream == null) {
      return 0;
    }
    InputStreamBuffer localInputStreamBuffer = new InputStreamBuffer(paramInputStream, 16, false);
    boolean bool1 = has(localInputStreamBuffer, paramLong, 1);
    int i = 0;
    if (bool1)
    {
      if ((localInputStreamBuffer.get(0) == -1) && (localInputStreamBuffer.get(1) == -40)) {}
      for (int i7 = 1;; i7 = 0)
      {
        i = 0;
        if (i7 != 0) {
          break;
        }
        return 0;
      }
    }
    int j;
    int i4;
    int i5;
    do
    {
      for (;;)
      {
        boolean bool2 = has(localInputStreamBuffer, paramLong, i + 3);
        j = 0;
        if (!bool2) {
          break label184;
        }
        i4 = i + 1;
        if ((0xFF & localInputStreamBuffer.get(i)) != 255) {
          break label578;
        }
        i5 = 0xFF & localInputStreamBuffer.get(i4);
        if (i5 != 255) {
          break;
        }
        i = i4;
      }
      i = i4 + 1;
    } while ((i5 == 216) || (i5 == 1));
    if ((i5 == 217) || (i5 == 218)) {
      localInputStreamBuffer.advanceTo(i - 4);
    }
    for (;;)
    {
      label184:
      if (j > 8)
      {
        int k = pack(localInputStreamBuffer, i, 4, false);
        if ((k != 1229531648) && (k != 1296891946))
        {
          Log.e("CameraExif", "Invalid byte order");
          return 0;
          int i6 = pack(localInputStreamBuffer, i, 2, false);
          if ((i6 < 2) || (!has(localInputStreamBuffer, paramLong, -1 + (i + i6))))
          {
            Log.e("CameraExif", "Invalid length");
            return 0;
          }
          if ((i5 == 225) && (i6 >= 8) && (pack(localInputStreamBuffer, i + 2, 4, false) == 1165519206) && (pack(localInputStreamBuffer, i + 6, 2, false) == 0))
          {
            i += 8;
            j = i6 - 8;
            localInputStreamBuffer.advanceTo(i - 4);
            continue;
          }
          i += i6;
          localInputStreamBuffer.advanceTo(i - 4);
          break;
        }
        if (k == 1229531648) {}
        int m;
        for (boolean bool3 = true;; bool3 = false)
        {
          m = 2 + pack(localInputStreamBuffer, i + 4, 4, bool3);
          if ((m >= 10) && (m <= j)) {
            break;
          }
          Log.e("CameraExif", "Invalid offset");
          return 0;
        }
        int n = i + m;
        int i1 = j - m;
        localInputStreamBuffer.advanceTo(n - 4);
        int i3;
        for (int i2 = pack(localInputStreamBuffer, n - 2, 2, bool3);; i2 = i3)
        {
          i3 = i2 - 1;
          if ((i2 <= 0) || (i1 < 12)) {
            break;
          }
          if (pack(localInputStreamBuffer, n, 2, bool3) == 274)
          {
            switch (pack(localInputStreamBuffer, n + 8, 2, bool3))
            {
            case 2: 
            case 4: 
            case 5: 
            case 7: 
            default: 
              Log.i("CameraExif", "Unsupported orientation");
              return 0;
            case 1: 
              return 0;
            case 3: 
              return 180;
            case 6: 
              return 90;
            }
            return 270;
          }
          n += 12;
          i1 -= 12;
          localInputStreamBuffer.advanceTo(n - 4);
        }
      }
      return 0;
      label578:
      i = i4;
      j = 0;
    }
  }
  
  private static boolean has(InputStreamBuffer paramInputStreamBuffer, long paramLong, int paramInt)
  {
    if (paramLong >= 0L) {
      return paramInt < paramLong;
    }
    return paramInputStreamBuffer.has(paramInt);
  }
  
  private static int pack(InputStreamBuffer paramInputStreamBuffer, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = 1;
    if (paramBoolean)
    {
      paramInt1 += paramInt2 - 1;
      i = -1;
    }
    int j = 0;
    int m;
    for (int k = paramInt2;; k = m)
    {
      m = k - 1;
      if (k <= 0) {
        break;
      }
      j = j << 8 | 0xFF & paramInputStreamBuffer.get(paramInt1);
      paramInt1 += i;
    }
    return j;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.ex.photo.util.Exif
 * JD-Core Version:    0.7.0.1
 */