package com.google.android.sidekick.main.location;

import android.location.Location;
import android.util.Log;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.sidekick.main.inject.SignedCipherHelper;
import com.google.android.sidekick.shared.util.Tag;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public class LocationStorage
{
  private static final String TAG = Tag.getTag(LocationStorage.class);
  private final SignedCipherHelper mSignedCipherHelper;
  
  public LocationStorage(SignedCipherHelper paramSignedCipherHelper)
  {
    this.mSignedCipherHelper = paramSignedCipherHelper;
  }
  
  Location bytesToLocation(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length != 20) {
      return null;
    }
    ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
    Location localLocation = new Location(null);
    localLocation.setLatitude(localByteBuffer.getDouble());
    localLocation.setLongitude(localByteBuffer.getDouble());
    localLocation.setAccuracy(localByteBuffer.getFloat());
    return localLocation;
  }
  
  Location decryptLocation(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = this.mSignedCipherHelper.decryptBytes(paramArrayOfByte);
    if (arrayOfByte == null) {
      return null;
    }
    return bytesToLocation(arrayOfByte);
  }
  
  byte[] encryptLocation(Location paramLocation)
  {
    byte[] arrayOfByte = locationToBytes(paramLocation);
    if (arrayOfByte != null) {
      return this.mSignedCipherHelper.encryptBytes(arrayOfByte);
    }
    return null;
  }
  
  byte[] locationToBytes(Location paramLocation)
  {
    byte[] arrayOfByte = new byte[20];
    ByteBuffer localByteBuffer = ByteBuffer.wrap(arrayOfByte);
    localByteBuffer.putDouble(paramLocation.getLatitude());
    localByteBuffer.putDouble(paramLocation.getLongitude());
    localByteBuffer.putFloat(paramLocation.getAccuracy());
    return arrayOfByte;
  }
  
  @Nullable
  Location readCurrentLocation(SharedPreferencesExt paramSharedPreferencesExt, String paramString)
  {
    byte[] arrayOfByte = paramSharedPreferencesExt.getBytes(paramString, null);
    Location localLocation = null;
    if (arrayOfByte != null)
    {
      localLocation = decryptLocation(arrayOfByte);
      if (localLocation == null)
      {
        Log.w(TAG, "Clearing bad lastloc from prefs");
        SharedPreferencesExt.Editor localEditor = paramSharedPreferencesExt.edit();
        localEditor.remove("lastloc");
        localEditor.apply();
      }
    }
    return localLocation;
  }
  
  void saveCurrentLocation(@Nullable Location paramLocation, SharedPreferencesExt paramSharedPreferencesExt, String paramString)
  {
    SharedPreferencesExt.Editor localEditor = paramSharedPreferencesExt.edit();
    if (paramLocation == null) {
      localEditor.remove(paramString);
    }
    for (;;)
    {
      localEditor.apply();
      return;
      byte[] arrayOfByte = encryptLocation(paramLocation);
      if (arrayOfByte == null)
      {
        Log.e(TAG, "error writing sidekick location (crypto fail)");
        return;
      }
      localEditor.putBytes(paramString, arrayOfByte);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.location.LocationStorage
 * JD-Core Version:    0.7.0.1
 */