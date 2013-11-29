package com.google.protobuf.nano;

import java.io.IOException;

public final class WireFormatNano
{
  public static final boolean[] EMPTY_BOOLEAN_ARRAY;
  public static final byte[] EMPTY_BYTES = new byte[0];
  public static final byte[][] EMPTY_BYTES_ARRAY;
  public static final double[] EMPTY_DOUBLE_ARRAY;
  public static final float[] EMPTY_FLOAT_ARRAY;
  public static final int[] EMPTY_INT_ARRAY;
  public static final long[] EMPTY_LONG_ARRAY;
  public static final String[] EMPTY_STRING_ARRAY;
  static final int MESSAGE_SET_ITEM_END_TAG;
  static final int MESSAGE_SET_ITEM_TAG = makeTag(1, 3);
  static final int MESSAGE_SET_MESSAGE_TAG;
  static final int MESSAGE_SET_TYPE_ID_TAG;
  
  static
  {
    MESSAGE_SET_ITEM_END_TAG = makeTag(1, 4);
    MESSAGE_SET_TYPE_ID_TAG = makeTag(2, 0);
    MESSAGE_SET_MESSAGE_TAG = makeTag(3, 2);
    EMPTY_INT_ARRAY = new int[0];
    EMPTY_LONG_ARRAY = new long[0];
    EMPTY_FLOAT_ARRAY = new float[0];
    EMPTY_DOUBLE_ARRAY = new double[0];
    EMPTY_BOOLEAN_ARRAY = new boolean[0];
    EMPTY_STRING_ARRAY = new String[0];
    EMPTY_BYTES_ARRAY = new byte[0][];
  }
  
  public static final int getRepeatedFieldArrayLength(CodedInputByteBufferNano paramCodedInputByteBufferNano, int paramInt)
    throws IOException
  {
    int i = 1;
    int j = paramCodedInputByteBufferNano.getPosition();
    paramCodedInputByteBufferNano.skipField(paramInt);
    for (;;)
    {
      if ((paramCodedInputByteBufferNano.getBytesUntilLimit() <= 0) || (paramCodedInputByteBufferNano.readTag() != paramInt))
      {
        paramCodedInputByteBufferNano.rewindToPosition(j);
        return i;
      }
      paramCodedInputByteBufferNano.skipField(paramInt);
      i++;
    }
  }
  
  public static int getTagFieldNumber(int paramInt)
  {
    return paramInt >>> 3;
  }
  
  static int getTagWireType(int paramInt)
  {
    return paramInt & 0x7;
  }
  
  static int makeTag(int paramInt1, int paramInt2)
  {
    return paramInt2 | paramInt1 << 3;
  }
  
  public static boolean parseUnknownField(CodedInputByteBufferNano paramCodedInputByteBufferNano, int paramInt)
    throws IOException
  {
    return paramCodedInputByteBufferNano.skipField(paramInt);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.protobuf.nano.WireFormatNano
 * JD-Core Version:    0.7.0.1
 */