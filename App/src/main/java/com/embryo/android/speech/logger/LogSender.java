package com.embryo.android.speech.logger;

import com.embryo.speech.logs.VoicesearchClientLogProto;

import java.io.IOException;
import java.util.ArrayList;

public abstract interface LogSender {
    public abstract void send(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList)
            throws IOException;
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     LogSender

 * JD-Core Version:    0.7.0.1

 */