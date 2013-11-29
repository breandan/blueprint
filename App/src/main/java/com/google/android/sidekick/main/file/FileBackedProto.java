package com.google.android.sidekick.main.file;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Supplier;
import com.google.protobuf.micro.MessageMicro;
import javax.annotation.Nullable;

public class FileBackedProto<T extends MessageMicro>
{
  private static final String TAG = Tag.getTag(FileBackedProto.class);
  private T mDataProto;
  private final boolean mEncrypted;
  private final FileBytesReader mFileBytesReader;
  private final FileBytesWriter mFileBytesWriter;
  private final String mFilename;
  private boolean mInitialized;
  private final Object mLock = new Object();
  private final Supplier<T> mProtoFactory;
  
  public FileBackedProto(Supplier<T> paramSupplier, String paramString, FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter, boolean paramBoolean)
  {
    this.mProtoFactory = paramSupplier;
    this.mFilename = paramString;
    this.mFileBytesReader = paramFileBytesReader;
    this.mFileBytesWriter = paramFileBytesWriter;
    this.mEncrypted = paramBoolean;
  }
  
  private void flush()
  {
    if (this.mEncrypted)
    {
      this.mFileBytesWriter.writeEncryptedFileBytes(this.mFilename, this.mDataProto.toByteArray(), 524288);
      return;
    }
    this.mFileBytesWriter.writeFileBytes(this.mFilename, this.mDataProto.toByteArray());
  }
  
  private void initializeIfNeeded()
  {
    if (!this.mInitialized) {}
    try
    {
      readFromDisk();
      return;
    }
    finally
    {
      this.mInitialized = true;
    }
  }
  
  /* Error */
  private void readFromDisk()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 41	com/google/android/sidekick/main/file/FileBackedProto:mProtoFactory	Lcom/google/common/base/Supplier;
    //   4: invokeinterface 83 1 0
    //   9: checkcast 54	com/google/protobuf/micro/MessageMicro
    //   12: astore_3
    //   13: aload_0
    //   14: getfield 49	com/google/android/sidekick/main/file/FileBackedProto:mEncrypted	Z
    //   17: ifeq +72 -> 89
    //   20: aload_0
    //   21: getfield 45	com/google/android/sidekick/main/file/FileBackedProto:mFileBytesReader	Lcom/google/android/sidekick/main/file/FileBytesReader;
    //   24: aload_0
    //   25: getfield 43	com/google/android/sidekick/main/file/FileBackedProto:mFilename	Ljava/lang/String;
    //   28: ldc 59
    //   30: invokevirtual 89	com/google/android/sidekick/main/file/FileBytesReader:readEncryptedFileBytes	(Ljava/lang/String;I)[B
    //   33: astore 9
    //   35: aload 9
    //   37: astore 4
    //   39: aload 4
    //   41: ifnull +10 -> 51
    //   44: aload_3
    //   45: aload 4
    //   47: invokevirtual 93	com/google/protobuf/micro/MessageMicro:mergeFrom	([B)Lcom/google/protobuf/micro/MessageMicro;
    //   50: pop
    //   51: aload_0
    //   52: aload_3
    //   53: putfield 52	com/google/android/sidekick/main/file/FileBackedProto:mDataProto	Lcom/google/protobuf/micro/MessageMicro;
    //   56: aload_0
    //   57: getfield 52	com/google/android/sidekick/main/file/FileBackedProto:mDataProto	Lcom/google/protobuf/micro/MessageMicro;
    //   60: ifnonnull +28 -> 88
    //   63: getstatic 33	com/google/android/sidekick/main/file/FileBackedProto:TAG	Ljava/lang/String;
    //   66: ldc 95
    //   68: invokestatic 101	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   71: pop
    //   72: aload_0
    //   73: aload_0
    //   74: getfield 41	com/google/android/sidekick/main/file/FileBackedProto:mProtoFactory	Lcom/google/common/base/Supplier;
    //   77: invokeinterface 83 1 0
    //   82: checkcast 54	com/google/protobuf/micro/MessageMicro
    //   85: putfield 52	com/google/android/sidekick/main/file/FileBackedProto:mDataProto	Lcom/google/protobuf/micro/MessageMicro;
    //   88: return
    //   89: aload_0
    //   90: getfield 45	com/google/android/sidekick/main/file/FileBackedProto:mFileBytesReader	Lcom/google/android/sidekick/main/file/FileBytesReader;
    //   93: aload_0
    //   94: getfield 43	com/google/android/sidekick/main/file/FileBackedProto:mFilename	Ljava/lang/String;
    //   97: ldc 59
    //   99: invokevirtual 104	com/google/android/sidekick/main/file/FileBytesReader:readFileBytes	(Ljava/lang/String;I)[B
    //   102: astore 4
    //   104: goto -65 -> 39
    //   107: astore 6
    //   109: getstatic 33	com/google/android/sidekick/main/file/FileBackedProto:TAG	Ljava/lang/String;
    //   112: new 106	java/lang/StringBuilder
    //   115: dup
    //   116: invokespecial 107	java/lang/StringBuilder:<init>	()V
    //   119: ldc 109
    //   121: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: aload_0
    //   125: getfield 43	com/google/android/sidekick/main/file/FileBackedProto:mFilename	Ljava/lang/String;
    //   128: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: aload 6
    //   136: invokestatic 120	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   139: pop
    //   140: aload_0
    //   141: getfield 47	com/google/android/sidekick/main/file/FileBackedProto:mFileBytesWriter	Lcom/google/android/sidekick/main/file/FileBytesWriter;
    //   144: aload_0
    //   145: getfield 43	com/google/android/sidekick/main/file/FileBackedProto:mFilename	Ljava/lang/String;
    //   148: invokevirtual 124	com/google/android/sidekick/main/file/FileBytesWriter:deleteFile	(Ljava/lang/String;)V
    //   151: goto -100 -> 51
    //   154: astore_1
    //   155: aload_0
    //   156: getfield 52	com/google/android/sidekick/main/file/FileBackedProto:mDataProto	Lcom/google/protobuf/micro/MessageMicro;
    //   159: ifnonnull +28 -> 187
    //   162: getstatic 33	com/google/android/sidekick/main/file/FileBackedProto:TAG	Ljava/lang/String;
    //   165: ldc 95
    //   167: invokestatic 101	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   170: pop
    //   171: aload_0
    //   172: aload_0
    //   173: getfield 41	com/google/android/sidekick/main/file/FileBackedProto:mProtoFactory	Lcom/google/common/base/Supplier;
    //   176: invokeinterface 83 1 0
    //   181: checkcast 54	com/google/protobuf/micro/MessageMicro
    //   184: putfield 52	com/google/android/sidekick/main/file/FileBackedProto:mDataProto	Lcom/google/protobuf/micro/MessageMicro;
    //   187: aload_1
    //   188: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	189	0	this	FileBackedProto
    //   154	34	1	localObject	Object
    //   12	41	3	localMessageMicro	MessageMicro
    //   37	66	4	arrayOfByte1	byte[]
    //   107	28	6	localInvalidProtocolBufferMicroException	com.google.protobuf.micro.InvalidProtocolBufferMicroException
    //   33	3	9	arrayOfByte2	byte[]
    // Exception table:
    //   from	to	target	type
    //   44	51	107	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   0	35	154	finally
    //   44	51	154	finally
    //   51	56	154	finally
    //   89	104	154	finally
    //   109	151	154	finally
  }
  
  public void deleteFile()
  {
    
    synchronized (this.mLock)
    {
      this.mFileBytesWriter.deleteFile(this.mFilename);
      this.mDataProto = null;
      this.mInitialized = false;
      return;
    }
  }
  
  public void doReadModifyWrite(ReadModifyWrite<T> paramReadModifyWrite)
  {
    
    synchronized (this.mLock)
    {
      initializeIfNeeded();
      MessageMicro localMessageMicro = (MessageMicro)paramReadModifyWrite.readModifyMaybeWrite(ProtoUtils.copyOf(this.mDataProto, (MessageMicro)this.mProtoFactory.get()));
      if (localMessageMicro != null)
      {
        this.mDataProto = localMessageMicro;
        flush();
      }
      return;
    }
  }
  
  public T getData()
  {
    
    synchronized (this.mLock)
    {
      initializeIfNeeded();
      MessageMicro localMessageMicro = this.mDataProto;
      return localMessageMicro;
    }
  }
  
  public static abstract interface ReadModifyWrite<T>
  {
    @Nullable
    public abstract T readModifyMaybeWrite(T paramT);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.file.FileBackedProto
 * JD-Core Version:    0.7.0.1
 */