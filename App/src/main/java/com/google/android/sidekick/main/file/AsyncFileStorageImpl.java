package com.google.android.sidekick.main.file;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public class AsyncFileStorageImpl
  implements AsyncFileStorage
{
  private static final String TAG = Tag.getTag(AsyncFileStorageImpl.class);
  private final FileBytesReader mFileReader;
  private final FileBytesWriter mFileWriter;
  private Handler mHandler;
  
  public AsyncFileStorageImpl(FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter)
  {
    this.mFileReader = paramFileBytesReader;
    this.mFileWriter = paramFileBytesWriter;
  }
  
  private Handler getHandler()
  {
    try
    {
      if (this.mHandler == null)
      {
        HandlerThread localHandlerThread = new HandlerThread(TAG, 10);
        localHandlerThread.start();
        this.mHandler = new AsyncFileStorageHandler(localHandlerThread.getLooper());
      }
      Handler localHandler = this.mHandler;
      return localHandler;
    }
    finally {}
  }
  
  private void readFromFileInternal(String paramString, Function<byte[], Void> paramFunction, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramFunction);
    getHandler().obtainMessage(2, new ReadData(paramString, paramFunction, paramBoolean)).sendToTarget();
  }
  
  private void updateFileInternal(String paramString, Function<byte[], byte[]> paramFunction, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramFunction);
    getHandler().obtainMessage(4, new UpdateData(paramString, paramFunction, paramBoolean)).sendToTarget();
  }
  
  private void writeToFileInternal(String paramString, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramArrayOfByte);
    getHandler().obtainMessage(1, new WriteData(paramString, paramArrayOfByte, paramBoolean)).sendToTarget();
  }
  
  public void deleteFile(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    getHandler().obtainMessage(3, paramString).sendToTarget();
  }
  
  public void readFromEncryptedFile(String paramString, Function<byte[], Void> paramFunction)
  {
    readFromFileInternal(paramString, paramFunction, true);
  }
  
  public void readFromFile(String paramString, Function<byte[], Void> paramFunction)
  {
    readFromFileInternal(paramString, paramFunction, false);
  }
  
  public void updateEncryptedFile(String paramString, Function<byte[], byte[]> paramFunction)
  {
    updateFileInternal(paramString, paramFunction, true);
  }
  
  public void writeToEncryptedFile(String paramString, byte[] paramArrayOfByte)
  {
    writeToFileInternal(paramString, paramArrayOfByte, true);
  }
  
  public void writeToFile(String paramString, byte[] paramArrayOfByte)
  {
    writeToFileInternal(paramString, paramArrayOfByte, false);
  }
  
  private class AsyncFileStorageHandler
    extends Handler
  {
    AsyncFileStorageHandler(Looper paramLooper)
    {
      super();
    }
    
    private void deleteFile(String paramString)
    {
      AsyncFileStorageImpl.this.mFileWriter.deleteFile(paramString);
    }
    
    private void readFile(AsyncFileStorageImpl.ReadData paramReadData)
    {
      try
      {
        byte[] arrayOfByte = readFileBytes(AsyncFileStorageImpl.ReadData.access$500(paramReadData), AsyncFileStorageImpl.ReadData.access$600(paramReadData));
        return;
      }
      finally
      {
        AsyncFileStorageImpl.ReadData.access$700(paramReadData).apply(null);
      }
    }
    
    private byte[] readFileBytes(String paramString, boolean paramBoolean)
    {
      if (paramBoolean) {
        return AsyncFileStorageImpl.this.mFileReader.readEncryptedFileBytes(paramString, 524288);
      }
      return AsyncFileStorageImpl.this.mFileReader.readFileBytes(paramString, 524288);
    }
    
    private void updateFile(AsyncFileStorageImpl.UpdateData paramUpdateData)
    {
      byte[] arrayOfByte1 = readFileBytes(AsyncFileStorageImpl.UpdateData.access$800(paramUpdateData), AsyncFileStorageImpl.UpdateData.access$900(paramUpdateData));
      byte[] arrayOfByte2 = (byte[])AsyncFileStorageImpl.UpdateData.access$1000(paramUpdateData).apply(arrayOfByte1);
      if (arrayOfByte2 != null)
      {
        if (arrayOfByte2.length == 0) {
          deleteFile(AsyncFileStorageImpl.UpdateData.access$800(paramUpdateData));
        }
      }
      else {
        return;
      }
      writeFile(new AsyncFileStorageImpl.WriteData(AsyncFileStorageImpl.UpdateData.access$800(paramUpdateData), arrayOfByte2, AsyncFileStorageImpl.UpdateData.access$900(paramUpdateData)));
    }
    
    private void writeFile(AsyncFileStorageImpl.WriteData paramWriteData)
    {
      if (AsyncFileStorageImpl.WriteData.access$100(paramWriteData))
      {
        AsyncFileStorageImpl.this.mFileWriter.writeEncryptedFileBytes(AsyncFileStorageImpl.WriteData.access$200(paramWriteData), AsyncFileStorageImpl.WriteData.access$300(paramWriteData), 524288);
        return;
      }
      if (AsyncFileStorageImpl.WriteData.access$300(paramWriteData).length <= 524288)
      {
        AsyncFileStorageImpl.this.mFileWriter.writeFileBytes(AsyncFileStorageImpl.WriteData.access$200(paramWriteData), AsyncFileStorageImpl.WriteData.access$300(paramWriteData));
        return;
      }
      Log.e(AsyncFileStorageImpl.TAG, "Data is too large (" + AsyncFileStorageImpl.WriteData.access$300(paramWriteData).length + " bytes) to write to disk: " + AsyncFileStorageImpl.WriteData.access$200(paramWriteData));
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default: 
        Log.w(AsyncFileStorageImpl.TAG, "Unknown message sent to AsyncFileStorageHandler");
        return;
      case 1: 
        writeFile((AsyncFileStorageImpl.WriteData)Preconditions.checkNotNull(paramMessage.obj));
        return;
      case 2: 
        readFile((AsyncFileStorageImpl.ReadData)Preconditions.checkNotNull(paramMessage.obj));
        return;
      case 3: 
        deleteFile((String)Preconditions.checkNotNull(paramMessage.obj));
        return;
      }
      updateFile((AsyncFileStorageImpl.UpdateData)Preconditions.checkNotNull(paramMessage.obj));
    }
  }
  
  private static class ReadData
  {
    private final Function<byte[], Void> mCallback;
    private final boolean mEncrypted;
    private final String mFilename;
    
    ReadData(String paramString, Function<byte[], Void> paramFunction, boolean paramBoolean)
    {
      this.mFilename = paramString;
      this.mCallback = paramFunction;
      this.mEncrypted = paramBoolean;
    }
  }
  
  private static class UpdateData
  {
    private final Function<byte[], byte[]> mCallback;
    private final boolean mEncrypted;
    private final String mFilename;
    
    UpdateData(String paramString, Function<byte[], byte[]> paramFunction, boolean paramBoolean)
    {
      this.mFilename = paramString;
      this.mCallback = paramFunction;
      this.mEncrypted = paramBoolean;
    }
  }
  
  private static class WriteData
  {
    private final byte[] mData;
    private final boolean mEncrypted;
    private final String mFilename;
    
    WriteData(String paramString, byte[] paramArrayOfByte, boolean paramBoolean)
    {
      this.mFilename = paramString;
      this.mData = paramArrayOfByte;
      this.mEncrypted = paramBoolean;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.file.AsyncFileStorageImpl
 * JD-Core Version:    0.7.0.1
 */