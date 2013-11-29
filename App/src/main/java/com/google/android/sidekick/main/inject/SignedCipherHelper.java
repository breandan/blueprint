package com.google.android.sidekick.main.inject;

public abstract interface SignedCipherHelper
{
  public abstract byte[] decryptBytes(byte[] paramArrayOfByte);
  
  public abstract byte[] encryptBytes(byte[] paramArrayOfByte);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.SignedCipherHelper
 * JD-Core Version:    0.7.0.1
 */