package com.embryo.android.speech.logger;

import com.embryo.android.voicesearch.logger.EventLogger;
import com.embryo.speech.logs.VoicesearchClientLogProto;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class SuggestionLogger {
    private final LinkedHashMap<Integer, SuggestionLogInfo> mSuggestionInfos = new LinkedHashMap();

    private SuggestionLogInfo getInfo(int paramInt) {
        return (SuggestionLogInfo) this.mSuggestionInfos.get(Integer.valueOf(paramInt));
    }

    public void addSuggestion(int paramInt1, String paramString, int paramInt2, int paramInt3, int paramInt4) {
        try {
            if (this.mSuggestionInfos.size() > 100) {
                Iterator localIterator = this.mSuggestionInfos.keySet().iterator();
                localIterator.next();
                localIterator.remove();
            }
            this.mSuggestionInfos.put(Integer.valueOf(paramInt1), new SuggestionLogInfo(paramString, paramInt2, paramInt3, paramInt4));
            return;
        } finally {
        }
    }

    public void log(int paramInt, String paramString1, String paramString2) {
        try {
            SuggestionLogInfo localSuggestionLogInfo = getInfo(paramInt);
            SuggestionData localSuggestionData = null;
            if (localSuggestionLogInfo != null) {
                localSuggestionData = localSuggestionLogInfo.asData(paramString1, paramString2);
            }
            EventLogger.recordClientEvent(15, localSuggestionData);
            return;
        } finally {
        }
    }

    public static class SuggestionData {
        private final VoicesearchClientLogProto.AlternateCorrectionData mProto;
        private final String mRequestId;

        public SuggestionData(VoicesearchClientLogProto.AlternateCorrectionData paramAlternateCorrectionData, String paramString) {
            this.mProto = paramAlternateCorrectionData;
            this.mRequestId = paramString;
        }

        public VoicesearchClientLogProto.AlternateCorrectionData getProto() {
            return this.mProto;
        }

        public String getRequestId() {
            return this.mRequestId;
        }
    }

    public class SuggestionLogInfo {
        private final int mLength;
        private final String mRequestId;
        private final int mSegmentId;
        private final int mStart;

        public SuggestionLogInfo(String paramString, int paramInt1, int paramInt2, int paramInt3) {
            this.mRequestId = paramString;
            this.mSegmentId = paramInt1;
            this.mStart = paramInt2;
            this.mLength = paramInt3;
        }

        public SuggestionLogger.SuggestionData asData(String paramString1, String paramString2) {
            return new SuggestionLogger.SuggestionData(new VoicesearchClientLogProto.AlternateCorrectionData().setRecognizerSegmentIndex(this.mSegmentId).setStart(this.mStart).setLength(this.mLength).setOldText(paramString1).setNewText(paramString2), this.mRequestId);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SuggestionLogger

 * JD-Core Version:    0.7.0.1

 */