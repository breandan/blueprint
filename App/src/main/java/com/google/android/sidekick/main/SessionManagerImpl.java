package com.google.android.sidekick.main;

import android.util.Log;
import com.google.android.search.core.GsaPreferenceController;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.preferences.SharedPreferencesExt.Editor;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.inject.SessionManager;
import com.google.android.sidekick.main.inject.SessionManager.SessionKey;
import com.google.android.sidekick.main.inject.SignedCipherHelper;
import com.google.android.sidekick.shared.util.Tag;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionManagerImpl
  implements SessionManager
{
  private static final int MAX_EXPIRATION_JITTER_SECONDS = (int)TimeUnit.HOURS.toSeconds(1L);
  private static final long SESSION_DURATION_SECONDS;
  private static final String TAG = Tag.getTag(SessionManagerImpl.class);
  private SessionManager.SessionKey mCachedSessionKey = null;
  private final SignedCipherHelper mCipher;
  private final Clock mClock;
  private final Object mGetAndSetLock = new Object();
  private SecureRandom mJitterGenerator;
  private final GsaPreferenceController mPrefController;
  
  static
  {
    SESSION_DURATION_SECONDS = TimeUnit.DAYS.toSeconds(7L);
  }
  
  public SessionManagerImpl(GsaPreferenceController paramGsaPreferenceController, Clock paramClock, SignedCipherHelper paramSignedCipherHelper)
  {
    this.mPrefController = paramGsaPreferenceController;
    this.mClock = paramClock;
    this.mCipher = paramSignedCipherHelper;
  }
  
  /* Error */
  private SessionManager.SessionKey decryptSessionKey(byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 67	com/google/android/sidekick/main/SessionManagerImpl:mCipher	Lcom/google/android/sidekick/main/inject/SignedCipherHelper;
    //   4: aload_1
    //   5: invokeinterface 77 2 0
    //   10: astore_2
    //   11: aload_2
    //   12: ifnonnull +5 -> 17
    //   15: aconst_null
    //   16: areturn
    //   17: new 79	java/io/DataInputStream
    //   20: dup
    //   21: new 81	java/io/ByteArrayInputStream
    //   24: dup
    //   25: aload_2
    //   26: invokespecial 84	java/io/ByteArrayInputStream:<init>	([B)V
    //   29: invokespecial 87	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   32: astore_3
    //   33: aload_3
    //   34: invokevirtual 91	java/io/DataInputStream:readLong	()J
    //   37: lstore 6
    //   39: aload_3
    //   40: invokevirtual 91	java/io/DataInputStream:readLong	()J
    //   43: lstore 8
    //   45: aload_3
    //   46: invokevirtual 91	java/io/DataInputStream:readLong	()J
    //   49: lstore 10
    //   51: aload_3
    //   52: invokevirtual 91	java/io/DataInputStream:readLong	()J
    //   55: lstore 12
    //   57: new 93	com/google/android/sidekick/main/inject/SessionManager$SessionKey
    //   60: dup
    //   61: new 95	java/util/UUID
    //   64: dup
    //   65: lload 6
    //   67: lload 8
    //   69: invokespecial 98	java/util/UUID:<init>	(JJ)V
    //   72: lload 10
    //   74: lload 12
    //   76: invokespecial 101	com/google/android/sidekick/main/inject/SessionManager$SessionKey:<init>	(Ljava/util/UUID;JJ)V
    //   79: astore 14
    //   81: aload_3
    //   82: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   85: aload 14
    //   87: areturn
    //   88: astore 5
    //   90: aload_3
    //   91: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   94: aconst_null
    //   95: areturn
    //   96: astore 4
    //   98: aload_3
    //   99: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   102: aload 4
    //   104: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	SessionManagerImpl
    //   0	105	1	paramArrayOfByte	byte[]
    //   10	16	2	arrayOfByte	byte[]
    //   32	67	3	localDataInputStream	java.io.DataInputStream
    //   96	7	4	localObject	Object
    //   88	1	5	localIOException	java.io.IOException
    //   37	29	6	l1	long
    //   43	25	8	l2	long
    //   49	24	10	l3	long
    //   55	20	12	l4	long
    //   79	7	14	localSessionKey	SessionManager.SessionKey
    // Exception table:
    //   from	to	target	type
    //   33	81	88	java/io/IOException
    //   33	81	96	finally
  }
  
  /* Error */
  private byte[] encryptSessionKey(SessionManager.SessionKey paramSessionKey)
  {
    // Byte code:
    //   0: new 111	java/io/ByteArrayOutputStream
    //   3: dup
    //   4: invokespecial 112	java/io/ByteArrayOutputStream:<init>	()V
    //   7: astore_2
    //   8: new 114	java/io/DataOutputStream
    //   11: dup
    //   12: aload_2
    //   13: invokespecial 117	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   16: astore_3
    //   17: aload_3
    //   18: aload_1
    //   19: getfield 121	com/google/android/sidekick/main/inject/SessionManager$SessionKey:key	Ljava/util/UUID;
    //   22: invokevirtual 124	java/util/UUID:getMostSignificantBits	()J
    //   25: invokevirtual 128	java/io/DataOutputStream:writeLong	(J)V
    //   28: aload_3
    //   29: aload_1
    //   30: getfield 121	com/google/android/sidekick/main/inject/SessionManager$SessionKey:key	Ljava/util/UUID;
    //   33: invokevirtual 131	java/util/UUID:getLeastSignificantBits	()J
    //   36: invokevirtual 128	java/io/DataOutputStream:writeLong	(J)V
    //   39: aload_3
    //   40: aload_1
    //   41: getfield 134	com/google/android/sidekick/main/inject/SessionManager$SessionKey:expirationSeconds	J
    //   44: invokevirtual 128	java/io/DataOutputStream:writeLong	(J)V
    //   47: aload_3
    //   48: aload_1
    //   49: getfield 137	com/google/android/sidekick/main/inject/SessionManager$SessionKey:jitteredExpirationSeconds	J
    //   52: invokevirtual 128	java/io/DataOutputStream:writeLong	(J)V
    //   55: aload_3
    //   56: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   59: aload_0
    //   60: getfield 67	com/google/android/sidekick/main/SessionManagerImpl:mCipher	Lcom/google/android/sidekick/main/inject/SignedCipherHelper;
    //   63: aload_2
    //   64: invokevirtual 141	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   67: invokeinterface 144 2 0
    //   72: areturn
    //   73: astore 5
    //   75: aload_3
    //   76: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   79: aconst_null
    //   80: areturn
    //   81: astore 4
    //   83: aload_3
    //   84: invokestatic 107	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   87: aload 4
    //   89: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	90	0	this	SessionManagerImpl
    //   0	90	1	paramSessionKey	SessionManager.SessionKey
    //   7	57	2	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   16	68	3	localDataOutputStream	java.io.DataOutputStream
    //   81	7	4	localObject	Object
    //   73	1	5	localIOException	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   17	55	73	java/io/IOException
    //   17	55	81	finally
  }
  
  private SessionManager.SessionKey readSessionkeyFromPreferences(SharedPreferencesExt paramSharedPreferencesExt)
  {
    byte[] arrayOfByte = paramSharedPreferencesExt.getBytes("session_key", null);
    SessionManager.SessionKey localSessionKey = null;
    if (arrayOfByte != null)
    {
      localSessionKey = decryptSessionKey(arrayOfByte);
      if (localSessionKey == null)
      {
        Log.w(TAG, "Clearing bad session_key from prefs");
        paramSharedPreferencesExt.edit().remove("session_key").apply();
      }
    }
    return localSessionKey;
  }
  
  private void writeSessionKeyToPreferences(SessionManager.SessionKey paramSessionKey, SharedPreferencesExt paramSharedPreferencesExt)
  {
    byte[] arrayOfByte = encryptSessionKey(paramSessionKey);
    if (arrayOfByte == null)
    {
      Log.e(TAG, "error writing session key (crypto fail)");
      return;
    }
    paramSharedPreferencesExt.edit().putBytes("session_key", arrayOfByte).apply();
  }
  
  public SessionManager.SessionKey getSessionKey()
  {
    ExtraPreconditions.checkNotMainThread();
    long l1 = TimeUnit.MILLISECONDS.toSeconds(this.mClock.currentTimeMillis());
    synchronized (this.mGetAndSetLock)
    {
      if (this.mCachedSessionKey == null) {
        this.mCachedSessionKey = readSessionkeyFromPreferences(this.mPrefController.getMainPreferences());
      }
      if ((this.mCachedSessionKey != null) && (this.mCachedSessionKey.jitteredExpirationSeconds > l1))
      {
        SessionManager.SessionKey localSessionKey2 = this.mCachedSessionKey;
        return localSessionKey2;
      }
      if (this.mJitterGenerator == null) {
        this.mJitterGenerator = new SecureRandom();
      }
      UUID localUUID = UUID.randomUUID();
      long l2 = l1 + SESSION_DURATION_SECONDS;
      this.mCachedSessionKey = new SessionManager.SessionKey(localUUID, l2, l2 + this.mJitterGenerator.nextInt(MAX_EXPIRATION_JITTER_SECONDS));
      writeSessionKeyToPreferences(this.mCachedSessionKey, this.mPrefController.getMainPreferences());
      SessionManager.SessionKey localSessionKey1 = this.mCachedSessionKey;
      return localSessionKey1;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.SessionManagerImpl
 * JD-Core Version:    0.7.0.1
 */