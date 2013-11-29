package com.google.android.speech.logger;

import com.google.speech.logs.VoicesearchClientLogProto.VoiceSearchClientLog;
import java.io.IOException;
import java.util.ArrayList;

public abstract interface LogSender
{
  public abstract void send(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList)
    throws IOException;
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.logger.LogSender
 * JD-Core Version:    0.7.0.1
 */