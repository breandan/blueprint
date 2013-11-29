package com.google.android.sidekick.main;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.sidekick.main.inject.SignedCipherHelper;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Preconditions;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SignedCipherHelperImpl
  implements SignedCipherHelper
{
  private static final String TAG = Tag.getTag(SignedCipherHelperImpl.class);
  private boolean mIsInitialized;
  private KeyPair mKeyPair;
  private final Object mLock = new Object();
  private final GsaPreferenceController mPrefController;
  private SecureRandom mSecureRandom;
  
  public SignedCipherHelperImpl(GsaPreferenceController paramGsaPreferenceController)
  {
    this.mPrefController = paramGsaPreferenceController;
  }
  
  private static Mac generateHmac(byte[] paramArrayOfByte, int paramInt1, int paramInt2, SecretKeySpec paramSecretKeySpec)
    throws GeneralSecurityException
  {
    Mac localMac = Mac.getInstance("HmacSHA1");
    localMac.init(paramSecretKeySpec);
    localMac.update(Base64.encode(paramArrayOfByte, paramInt1, paramInt2, 3));
    return localMac;
  }
  
  private KeyPair generateKeyPairLocked(SharedPreferences paramSharedPreferences)
  {
    try
    {
      KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
      localKeyGenerator.init(256);
      SecretKey localSecretKey1 = localKeyGenerator.generateKey();
      SecretKey localSecretKey2 = localKeyGenerator.generateKey();
      String str1 = Base64.encodeToString(localSecretKey1.getEncoded(), 3);
      String str2 = Base64.encodeToString(localSecretKey2.getEncoded(), 3);
      SharedPreferences.Editor localEditor = paramSharedPreferences.edit();
      localEditor.putString("winston", str1);
      localEditor.putString("wolf", str2);
      localEditor.apply();
      return new KeyPair(localSecretKey1.getEncoded(), localSecretKey2.getEncoded());
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.e(TAG, "Cannot create KeyGenerator for AES");
    }
    return null;
  }
  
  private void maybeInitKeysLocked()
  {
    if (this.mIsInitialized) {
      return;
    }
    SharedPreferencesExt localSharedPreferencesExt = this.mPrefController.getMainPreferences();
    localKeyPair = readKeyPairFromPrefsLocked(localSharedPreferencesExt);
    if (localKeyPair == null) {
      localKeyPair = generateKeyPairLocked(localSharedPreferencesExt);
    }
    try
    {
      SecureRandom localSecureRandom2 = SecureRandom.getInstance("SHA1PRNG");
      localSecureRandom1 = localSecureRandom2;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      for (;;)
      {
        Log.e(TAG, "Cannot create SecureRandom for SHA1PRNG");
        localKeyPair = null;
        SecureRandom localSecureRandom1 = null;
      }
    }
    this.mKeyPair = localKeyPair;
    this.mSecureRandom = localSecureRandom1;
    this.mIsInitialized = true;
  }
  
  private KeyPair readKeyPairFromPrefsLocked(SharedPreferences paramSharedPreferences)
  {
    String str1 = paramSharedPreferences.getString("winston", null);
    String str2 = paramSharedPreferences.getString("wolf", null);
    if ((str1 == null) || (str2 == null)) {
      return null;
    }
    try
    {
      KeyPair localKeyPair = new KeyPair(Base64.decode(str1, 3), Base64.decode(str2, 3));
      return localKeyPair;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      paramSharedPreferences.edit().remove("winston").remove("wolf").apply();
      Log.w(TAG, "Failed to read keys successfully; clearing old ones");
    }
    return null;
  }
  
  public byte[] decryptBytes(byte[] paramArrayOfByte)
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    KeyPair localKeyPair;
    int i;
    synchronized (this.mLock)
    {
      maybeInitKeysLocked();
      if (this.mKeyPair == null)
      {
        Log.w(TAG, "No key pair");
        return null;
      }
      localKeyPair = this.mKeyPair;
      try
      {
        i = -20 + (-16 + paramArrayOfByte.length);
        if (i < 0)
        {
          Log.e(TAG, "Failed to decrypt: bad data");
          return null;
        }
      }
      catch (GeneralSecurityException localGeneralSecurityException)
      {
        Log.e(TAG, "Failed to decrypt", localGeneralSecurityException);
        return null;
      }
    }
    byte[] arrayOfByte1 = generateHmac(paramArrayOfByte, 36, i, localKeyPair.mHmacKey).doFinal();
    if (!Arrays.equals(Arrays.copyOfRange(paramArrayOfByte, 16, 36), arrayOfByte1))
    {
      Log.e(TAG, "Signature mismatch");
      return null;
    }
    IvParameterSpec localIvParameterSpec = new IvParameterSpec(paramArrayOfByte, 0, 16);
    Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    localCipher.init(2, localKeyPair.mSecretKey, localIvParameterSpec);
    byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte, 36, i);
    return arrayOfByte2;
  }
  
  public byte[] encryptBytes(byte[] paramArrayOfByte)
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    synchronized (this.mLock)
    {
      maybeInitKeysLocked();
      if (this.mKeyPair == null)
      {
        Log.w(TAG, "No key pair");
        return null;
      }
      KeyPair localKeyPair = this.mKeyPair;
      SecureRandom localSecureRandom = this.mSecureRandom;
      try
      {
        i = 16 * (1 + paramArrayOfByte.length / 16);
        arrayOfByte1 = new byte[i + 36];
        byte[] arrayOfByte2 = new byte[16];
        localSecureRandom.nextBytes(arrayOfByte2);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 16);
        IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte1, 0, 16);
        Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        localCipher.init(1, localKeyPair.mSecretKey, localIvParameterSpec);
        if (i != localCipher.doFinal(paramArrayOfByte, 0, paramArrayOfByte.length, arrayOfByte1, 36)) {
          break label214;
        }
        bool = true;
      }
      catch (GeneralSecurityException localGeneralSecurityException)
      {
        byte[] arrayOfByte1;
        Mac localMac;
        for (;;)
        {
          int i;
          Log.e(TAG, "Failed to encrypt", localGeneralSecurityException);
          return null;
          localObject2 = finally;
          throw localObject2;
          boolean bool = false;
        }
        localMac.doFinal(arrayOfByte1, 16);
        return arrayOfByte1;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Log.e(TAG, "Failed to encrypt", localIllegalStateException);
      }
      Preconditions.checkState(bool);
      localMac = generateHmac(arrayOfByte1, 36, i, localKeyPair.mHmacKey);
      if (localMac.getMacLength() != 20) {
        throw new IllegalStateException("hmac size unexpected");
      }
    }
    label214:
    return null;
  }
  
  private static class KeyPair
  {
    private final SecretKeySpec mHmacKey;
    private final SecretKeySpec mSecretKey;
    
    KeyPair(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      this.mSecretKey = new SecretKeySpec(paramArrayOfByte1, "AES");
      this.mHmacKey = new SecretKeySpec(paramArrayOfByte2, "HmacSHA1");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.SignedCipherHelperImpl
 * JD-Core Version:    0.7.0.1
 */