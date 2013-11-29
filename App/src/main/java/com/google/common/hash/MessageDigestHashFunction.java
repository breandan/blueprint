package com.google.common.hash;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

final class MessageDigestHashFunction
  extends AbstractStreamingHashFunction
{
  private final String algorithmName;
  private final int bits;
  
  MessageDigestHashFunction(String paramString)
  {
    this.algorithmName = paramString;
    this.bits = (8 * getMessageDigest(paramString).getDigestLength());
  }
  
  private static MessageDigest getMessageDigest(String paramString)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString);
      return localMessageDigest;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new AssertionError(localNoSuchAlgorithmException);
    }
  }
  
  public Hasher newHasher()
  {
    return new MessageDigestHasher(getMessageDigest(this.algorithmName), null);
  }
  
  private static class MessageDigestHasher
    implements Hasher
  {
    private final MessageDigest digest;
    private boolean done;
    private final ByteBuffer scratch;
    
    private MessageDigestHasher(MessageDigest paramMessageDigest)
    {
      this.digest = paramMessageDigest;
      this.scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
    }
    
    private void checkNotDone()
    {
      if (!this.done) {}
      for (boolean bool = true;; bool = false)
      {
        Preconditions.checkState(bool, "Cannot use Hasher after calling #hash() on it");
        return;
      }
    }
    
    public HashCode hash()
    {
      this.done = true;
      return HashCodes.fromBytes(this.digest.digest());
    }
    
    public Hasher putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      checkNotDone();
      Preconditions.checkPositionIndexes(paramInt1, paramInt1 + paramInt2, paramArrayOfByte.length);
      this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.hash.MessageDigestHashFunction
 * JD-Core Version:    0.7.0.1
 */