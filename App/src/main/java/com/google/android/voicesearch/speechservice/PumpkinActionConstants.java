package com.google.android.voicesearch.speechservice;

import java.util.Map;

public abstract interface PumpkinActionConstants {
    public static final Map<String, Integer> ACTION_FLAGS = new ImmutableMap.Builder().put("CallContact", Integer.valueOf(1)).put("CallNumber", Integer.valueOf(2)).put("OpenApp", Integer.valueOf(4)).put("Selection", Integer.valueOf(8)).put("SendTextToContact", Integer.valueOf(16)).put("Undo", Integer.valueOf(32)).put("Redo", Integer.valueOf(64)).put("Cancel", Integer.valueOf(128)).put("SelectRecipient", Integer.valueOf(256)).build();
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.speechservice.PumpkinActionConstants

 * JD-Core Version:    0.7.0.1

 */