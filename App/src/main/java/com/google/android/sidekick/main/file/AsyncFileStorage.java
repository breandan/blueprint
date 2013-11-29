package com.google.android.sidekick.main.file;

import com.google.common.base.Function;

public abstract interface AsyncFileStorage
{
  public abstract void deleteFile(String paramString);
  
  public abstract void readFromEncryptedFile(String paramString, Function<byte[], Void> paramFunction);
  
  public abstract void readFromFile(String paramString, Function<byte[], Void> paramFunction);
  
  public abstract void updateEncryptedFile(String paramString, Function<byte[], byte[]> paramFunction);
  
  public abstract void writeToEncryptedFile(String paramString, byte[] paramArrayOfByte);
  
  public abstract void writeToFile(String paramString, byte[] paramArrayOfByte);
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.file.AsyncFileStorage
 * JD-Core Version:    0.7.0.1
 */