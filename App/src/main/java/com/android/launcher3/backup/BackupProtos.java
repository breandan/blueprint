package com.android.launcher3.backup;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Arrays;

public final class BackupProtos
{
  public static final class CheckedMessage
    extends MessageNano
  {
    public static final CheckedMessage[] EMPTY_ARRAY = new CheckedMessage[0];
    private int cachedSize = -1;
    public long checksum = 0L;
    public byte[] payload = WireFormatNano.EMPTY_BYTES;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeBytesSize(1, this.payload) + CodedOutputByteBufferNano.computeInt64Size(2, this.checksum);
      this.cachedSize = i;
      return i;
    }
    
    public CheckedMessage mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          this.payload = paramCodedInputByteBufferNano.readBytes();
          break;
        }
        this.checksum = paramCodedInputByteBufferNano.readInt64();
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeBytes(1, this.payload);
      paramCodedOutputByteBufferNano.writeInt64(2, this.checksum);
    }
  }
  
  public static final class Favorite
    extends MessageNano
  {
    public static final Favorite[] EMPTY_ARRAY = new Favorite[0];
    public int appWidgetId = 0;
    public String appWidgetProvider = "";
    private int cachedSize = -1;
    public int cellX = 0;
    public int cellY = 0;
    public int container = 0;
    public int displayMode = 0;
    public byte[] icon = WireFormatNano.EMPTY_BYTES;
    public String iconPackage = "";
    public String iconResource = "";
    public int iconType = 0;
    public long id = 0L;
    public String intent = "";
    public int itemType = 0;
    public int screen = 0;
    public int spanX = 0;
    public int spanY = 0;
    public String title = "";
    public String uri = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeInt64Size(1, this.id) + CodedOutputByteBufferNano.computeInt32Size(2, this.itemType);
      if (!this.title.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(3, this.title);
      }
      if (this.container != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(4, this.container);
      }
      if (this.screen != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(5, this.screen);
      }
      if (this.cellX != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(6, this.cellX);
      }
      if (this.cellY != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(7, this.cellY);
      }
      if (this.spanX != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(8, this.spanX);
      }
      if (this.spanY != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(9, this.spanY);
      }
      if (this.displayMode != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(10, this.displayMode);
      }
      if (this.appWidgetId != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(11, this.appWidgetId);
      }
      if (!this.appWidgetProvider.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(12, this.appWidgetProvider);
      }
      if (!this.intent.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(13, this.intent);
      }
      if (!this.uri.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(14, this.uri);
      }
      if (this.iconType != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(15, this.iconType);
      }
      if (!this.iconPackage.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(16, this.iconPackage);
      }
      if (!this.iconResource.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(17, this.iconResource);
      }
      if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
        i += CodedOutputByteBufferNano.computeBytesSize(18, this.icon);
      }
      this.cachedSize = i;
      return i;
    }
    
    public Favorite mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          this.id = paramCodedInputByteBufferNano.readInt64();
          break;
        case 16: 
          this.itemType = paramCodedInputByteBufferNano.readInt32();
          break;
        case 26: 
          this.title = paramCodedInputByteBufferNano.readString();
          break;
        case 32: 
          this.container = paramCodedInputByteBufferNano.readInt32();
          break;
        case 40: 
          this.screen = paramCodedInputByteBufferNano.readInt32();
          break;
        case 48: 
          this.cellX = paramCodedInputByteBufferNano.readInt32();
          break;
        case 56: 
          this.cellY = paramCodedInputByteBufferNano.readInt32();
          break;
        case 64: 
          this.spanX = paramCodedInputByteBufferNano.readInt32();
          break;
        case 72: 
          this.spanY = paramCodedInputByteBufferNano.readInt32();
          break;
        case 80: 
          this.displayMode = paramCodedInputByteBufferNano.readInt32();
          break;
        case 88: 
          this.appWidgetId = paramCodedInputByteBufferNano.readInt32();
          break;
        case 98: 
          this.appWidgetProvider = paramCodedInputByteBufferNano.readString();
          break;
        case 106: 
          this.intent = paramCodedInputByteBufferNano.readString();
          break;
        case 114: 
          this.uri = paramCodedInputByteBufferNano.readString();
          break;
        case 120: 
          this.iconType = paramCodedInputByteBufferNano.readInt32();
          break;
        case 130: 
          this.iconPackage = paramCodedInputByteBufferNano.readString();
          break;
        case 138: 
          this.iconResource = paramCodedInputByteBufferNano.readString();
          break;
        }
        this.icon = paramCodedInputByteBufferNano.readBytes();
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeInt64(1, this.id);
      paramCodedOutputByteBufferNano.writeInt32(2, this.itemType);
      if (!this.title.equals("")) {
        paramCodedOutputByteBufferNano.writeString(3, this.title);
      }
      if (this.container != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, this.container);
      }
      if (this.screen != 0) {
        paramCodedOutputByteBufferNano.writeInt32(5, this.screen);
      }
      if (this.cellX != 0) {
        paramCodedOutputByteBufferNano.writeInt32(6, this.cellX);
      }
      if (this.cellY != 0) {
        paramCodedOutputByteBufferNano.writeInt32(7, this.cellY);
      }
      if (this.spanX != 0) {
        paramCodedOutputByteBufferNano.writeInt32(8, this.spanX);
      }
      if (this.spanY != 0) {
        paramCodedOutputByteBufferNano.writeInt32(9, this.spanY);
      }
      if (this.displayMode != 0) {
        paramCodedOutputByteBufferNano.writeInt32(10, this.displayMode);
      }
      if (this.appWidgetId != 0) {
        paramCodedOutputByteBufferNano.writeInt32(11, this.appWidgetId);
      }
      if (!this.appWidgetProvider.equals("")) {
        paramCodedOutputByteBufferNano.writeString(12, this.appWidgetProvider);
      }
      if (!this.intent.equals("")) {
        paramCodedOutputByteBufferNano.writeString(13, this.intent);
      }
      if (!this.uri.equals("")) {
        paramCodedOutputByteBufferNano.writeString(14, this.uri);
      }
      if (this.iconType != 0) {
        paramCodedOutputByteBufferNano.writeInt32(15, this.iconType);
      }
      if (!this.iconPackage.equals("")) {
        paramCodedOutputByteBufferNano.writeString(16, this.iconPackage);
      }
      if (!this.iconResource.equals("")) {
        paramCodedOutputByteBufferNano.writeString(17, this.iconResource);
      }
      if (!Arrays.equals(this.icon, WireFormatNano.EMPTY_BYTES)) {
        paramCodedOutputByteBufferNano.writeBytes(18, this.icon);
      }
    }
  }
  
  public static final class Journal
    extends MessageNano
  {
    public static final Journal[] EMPTY_ARRAY = new Journal[0];
    public int appVersion = 0;
    public long bytes = 0L;
    private int cachedSize = -1;
    public BackupProtos.Key[] key = BackupProtos.Key.EMPTY_ARRAY;
    public int rows = 0;
    public long t = 0L;
    
    public final Journal clear()
    {
      this.appVersion = 0;
      this.t = 0L;
      this.bytes = 0L;
      this.rows = 0;
      this.key = BackupProtos.Key.EMPTY_ARRAY;
      this.cachedSize = -1;
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeInt32Size(1, this.appVersion) + CodedOutputByteBufferNano.computeInt64Size(2, this.t);
      if (this.bytes != 0L) {
        i += CodedOutputByteBufferNano.computeInt64Size(3, this.bytes);
      }
      if (this.rows != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(4, this.rows);
      }
      BackupProtos.Key[] arrayOfKey = this.key;
      int j = arrayOfKey.length;
      for (int k = 0; k < j; k++) {
        i += CodedOutputByteBufferNano.computeMessageSize(5, arrayOfKey[k]);
      }
      this.cachedSize = i;
      return i;
    }
    
    public Journal mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          this.appVersion = paramCodedInputByteBufferNano.readInt32();
          break;
        case 16: 
          this.t = paramCodedInputByteBufferNano.readInt64();
          break;
        case 24: 
          this.bytes = paramCodedInputByteBufferNano.readInt64();
          break;
        case 32: 
          this.rows = paramCodedInputByteBufferNano.readInt32();
          break;
        }
        int j = WireFormatNano.getRepeatedFieldArrayLength(paramCodedInputByteBufferNano, 42);
        int k = this.key.length;
        BackupProtos.Key[] arrayOfKey = new BackupProtos.Key[k + j];
        System.arraycopy(this.key, 0, arrayOfKey, 0, k);
        this.key = arrayOfKey;
        while (k < -1 + this.key.length)
        {
          this.key[k] = new BackupProtos.Key();
          paramCodedInputByteBufferNano.readMessage(this.key[k]);
          paramCodedInputByteBufferNano.readTag();
          k++;
        }
        this.key[k] = new BackupProtos.Key();
        paramCodedInputByteBufferNano.readMessage(this.key[k]);
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeInt32(1, this.appVersion);
      paramCodedOutputByteBufferNano.writeInt64(2, this.t);
      if (this.bytes != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, this.bytes);
      }
      if (this.rows != 0) {
        paramCodedOutputByteBufferNano.writeInt32(4, this.rows);
      }
      BackupProtos.Key[] arrayOfKey = this.key;
      int i = arrayOfKey.length;
      for (int j = 0; j < i; j++) {
        paramCodedOutputByteBufferNano.writeMessage(5, arrayOfKey[j]);
      }
    }
  }
  
  public static final class Key
    extends MessageNano
  {
    public static final Key[] EMPTY_ARRAY = new Key[0];
    private int cachedSize = -1;
    public long checksum = 0L;
    public long id = 0L;
    public String name = "";
    public int type = 1;
    
    public static Key parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferNanoException
    {
      return (Key)MessageNano.mergeFrom(new Key(), paramArrayOfByte);
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeInt32Size(1, this.type);
      if (!this.name.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(2, this.name);
      }
      if (this.id != 0L) {
        i += CodedOutputByteBufferNano.computeInt64Size(3, this.id);
      }
      if (this.checksum != 0L) {
        i += CodedOutputByteBufferNano.computeInt64Size(4, this.checksum);
      }
      this.cachedSize = i;
      return i;
    }
    
    public Key mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          this.type = paramCodedInputByteBufferNano.readInt32();
          break;
        case 18: 
          this.name = paramCodedInputByteBufferNano.readString();
          break;
        case 24: 
          this.id = paramCodedInputByteBufferNano.readInt64();
          break;
        }
        this.checksum = paramCodedInputByteBufferNano.readInt64();
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeInt32(1, this.type);
      if (!this.name.equals("")) {
        paramCodedOutputByteBufferNano.writeString(2, this.name);
      }
      if (this.id != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(3, this.id);
      }
      if (this.checksum != 0L) {
        paramCodedOutputByteBufferNano.writeInt64(4, this.checksum);
      }
    }
  }
  
  public static final class Resource
    extends MessageNano
  {
    public static final Resource[] EMPTY_ARRAY = new Resource[0];
    private int cachedSize = -1;
    public byte[] data = WireFormatNano.EMPTY_BYTES;
    public int dpi = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeInt32Size(1, this.dpi) + CodedOutputByteBufferNano.computeBytesSize(2, this.data);
      this.cachedSize = i;
      return i;
    }
    
    public Resource mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          this.dpi = paramCodedInputByteBufferNano.readInt32();
          break;
        }
        this.data = paramCodedInputByteBufferNano.readBytes();
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeInt32(1, this.dpi);
      paramCodedOutputByteBufferNano.writeBytes(2, this.data);
    }
  }
  
  public static final class Screen
    extends MessageNano
  {
    public static final Screen[] EMPTY_ARRAY = new Screen[0];
    private int cachedSize = -1;
    public long id = 0L;
    public int rank = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeInt64Size(1, this.id);
      if (this.rank != 0) {
        i += CodedOutputByteBufferNano.computeInt32Size(2, this.rank);
      }
      this.cachedSize = i;
      return i;
    }
    
    public Screen mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          this.id = paramCodedInputByteBufferNano.readInt64();
          break;
        }
        this.rank = paramCodedInputByteBufferNano.readInt32();
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeInt64(1, this.id);
      if (this.rank != 0) {
        paramCodedOutputByteBufferNano.writeInt32(2, this.rank);
      }
    }
  }
  
  public static final class Widget
    extends MessageNano
  {
    public static final Widget[] EMPTY_ARRAY = new Widget[0];
    private int cachedSize = -1;
    public boolean configure = false;
    public BackupProtos.Resource icon = null;
    public String label = "";
    public BackupProtos.Resource preview = null;
    public String provider = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      int i = 0 + CodedOutputByteBufferNano.computeStringSize(1, this.provider);
      if (!this.label.equals("")) {
        i += CodedOutputByteBufferNano.computeStringSize(2, this.label);
      }
      if (this.configure) {
        i += CodedOutputByteBufferNano.computeBoolSize(3, this.configure);
      }
      if (this.icon != null) {
        i += CodedOutputByteBufferNano.computeMessageSize(4, this.icon);
      }
      if (this.preview != null) {
        i += CodedOutputByteBufferNano.computeMessageSize(5, this.preview);
      }
      this.cachedSize = i;
      return i;
    }
    
    public Widget mergeFrom(CodedInputByteBufferNano paramCodedInputByteBufferNano)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputByteBufferNano.readTag();
        switch (i)
        {
        default: 
          if (WireFormatNano.parseUnknownField(paramCodedInputByteBufferNano, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          this.provider = paramCodedInputByteBufferNano.readString();
          break;
        case 18: 
          this.label = paramCodedInputByteBufferNano.readString();
          break;
        case 24: 
          this.configure = paramCodedInputByteBufferNano.readBool();
          break;
        case 34: 
          if (this.icon == null) {
            this.icon = new BackupProtos.Resource();
          }
          paramCodedInputByteBufferNano.readMessage(this.icon);
          break;
        }
        if (this.preview == null) {
          this.preview = new BackupProtos.Resource();
        }
        paramCodedInputByteBufferNano.readMessage(this.preview);
      }
    }
    
    public void writeTo(CodedOutputByteBufferNano paramCodedOutputByteBufferNano)
      throws IOException
    {
      paramCodedOutputByteBufferNano.writeString(1, this.provider);
      if (!this.label.equals("")) {
        paramCodedOutputByteBufferNano.writeString(2, this.label);
      }
      if (this.configure) {
        paramCodedOutputByteBufferNano.writeBool(3, this.configure);
      }
      if (this.icon != null) {
        paramCodedOutputByteBufferNano.writeMessage(4, this.icon);
      }
      if (this.preview != null) {
        paramCodedOutputByteBufferNano.writeMessage(5, this.preview);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.backup.BackupProtos
 * JD-Core Version:    0.7.0.1
 */