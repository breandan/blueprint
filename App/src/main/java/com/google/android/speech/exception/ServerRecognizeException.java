package com.google.android.speech.exception;

public class ServerRecognizeException
  extends RecognizeException
{
  private static final long serialVersionUID = 5707964384032481802L;
  private final int mErrorCode;
  
  public ServerRecognizeException(int paramInt)
  {
    this.mErrorCode = paramInt;
  }
  
  public boolean isAuthException()
  {
    return (this.mErrorCode == -74001) || (this.mErrorCode == -74002);
  }
  
  public String toString()
  {
    return super.toString() + " ErrorCode:" + this.mErrorCode;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.exception.ServerRecognizeException
 * JD-Core Version:    0.7.0.1
 */