package com.google.android.e100;

import android.text.TextUtils;

public class MessageContents
{
  private boolean mNeedsAnnounce = true;
  private String mNextMessage;
  private String mPreviousMessage;
  
  public boolean needsAnnouncement()
  {
    return this.mNeedsAnnounce;
  }
  
  public void onAnnouncementCreated()
  {
    this.mNeedsAnnounce = false;
  }
  
  public boolean onMessageReceived(String paramString, boolean paramBoolean)
  {
    boolean bool1 = true;
    if (TextUtils.isEmpty(this.mNextMessage))
    {
      this.mNextMessage = paramString;
      if (!paramBoolean) {}
      for (;;)
      {
        this.mNeedsAnnounce = bool1;
        return paramBoolean;
        bool1 = false;
      }
    }
    this.mNextMessage = (this.mNextMessage + " " + paramString);
    boolean bool2;
    boolean bool3;
    if ((this.mNeedsAnnounce) && (paramBoolean))
    {
      bool2 = bool1;
      bool3 = this.mNeedsAnnounce;
      if (paramBoolean) {
        break label104;
      }
    }
    for (;;)
    {
      this.mNeedsAnnounce = (bool1 & bool3);
      return bool2;
      bool2 = false;
      break;
      label104:
      bool1 = false;
    }
  }
  
  public String takeNextMessage()
  {
    if (TextUtils.isEmpty(this.mNextMessage)) {
      throw new IllegalArgumentException("Next notification not available. Perhaps it was already retrieved?");
    }
    String str = this.mNextMessage;
    this.mPreviousMessage = this.mNextMessage;
    this.mNextMessage = null;
    return str;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.e100.MessageContents
 * JD-Core Version:    0.7.0.1
 */