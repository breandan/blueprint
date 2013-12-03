package com.google.speech.speech.s3;

import com.google.majel.proto.ClientInfoProtos.BrowserParams;
import com.google.majel.proto.ClientInfoProtos.PreviewParams;
import com.google.majel.proto.ClientInfoProtos.ScreenParams;
import com.google.majel.proto.ContextProtos.Context;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Majel {
    public static final class MajelClientInfo
            extends MessageMicro {
        private ClientInfoProtos.BrowserParams browserParams_ = null;
        private int cachedSize = -1;
        private List<Integer> capabilities_ = Collections.emptyList();
        private String clientName_ = "";
        private int client_ = 0;
        private ContextProtos.Context context_ = null;
        private String gservicesCountryCode_ = "";
        private boolean hasBrowserParams;
        private boolean hasClient;
        private boolean hasClientName;
        private boolean hasContext;
        private boolean hasGservicesCountryCode;
        private boolean hasPreviewParams;
        private boolean hasSafesearchLevel;
        private boolean hasScreenParams;
        private boolean hasSystemTimeMs;
        private boolean hasTimeZone;
        private boolean hasUseCompressedResponse;
        private ClientInfProtos.PreviewParams previewParams_ = null;
        private int safesearchLevel_ = 1;
        private ClientInfoProtos.ScreenParams screenParams_ = null;
        private long systemTimeMs_ = 0L;
        private String timeZone_ = "";
        private boolean useCompressedResponse_ = false;

        public MajelClientInfo addCapabilities(int paramInt) {
            if (this.capabilities_.isEmpty()) {
                this.capabilities_ = new ArrayList();
            }
            this.capabilities_.add(Integer.valueOf(paramInt));
            return this;
        }

        public ClientInfoProtos.BrowserParams getBrowserParams() {
            return this.browserParams_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<Integer> getCapabilitiesList() {
            return this.capabilities_;
        }

        public int getClient() {
            return this.client_;
        }

        public String getClientName() {
            return this.clientName_;
        }

        public ContextProtos.Context getContext() {
            return this.context_;
        }

        public String getGservicesCountryCode() {
            return this.gservicesCountryCode_;
        }

        public ClientInfoProtos.PreviewParams getPreviewParams() {
            return this.previewParams_;
        }

        public int getSafesearchLevel() {
            return this.safesearchLevel_;
        }

        public ClientInfoProtos.ScreenParams getScreenParams() {
            return this.screenParams_;
        }

        public int getSerializedSize() {
            boolean bool = hasUseCompressedResponse();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getUseCompressedResponse());
            }
            int j = 0;
            Iterator localIterator = getCapabilitiesList().iterator();
            while (localIterator.hasNext()) {
                j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator.next()).intValue());
            }
            int k = i + j + 1 * getCapabilitiesList().size();
            if (hasContext()) {
                k += CodedOutputStreamMicro.computeMessageSize(3, getContext());
            }
            if (hasSafesearchLevel()) {
                k += CodedOutputStreamMicro.computeInt32Size(4, getSafesearchLevel());
            }
            if (hasPreviewParams()) {
                k += CodedOutputStreamMicro.computeMessageSize(5, getPreviewParams());
            }
            if (hasBrowserParams()) {
                k += CodedOutputStreamMicro.computeMessageSize(6, getBrowserParams());
            }
            if (hasSystemTimeMs()) {
                k += CodedOutputStreamMicro.computeInt64Size(7, getSystemTimeMs());
            }
            if (hasTimeZone()) {
                k += CodedOutputStreamMicro.computeStringSize(8, getTimeZone());
            }
            if (hasScreenParams()) {
                k += CodedOutputStreamMicro.computeMessageSize(9, getScreenParams());
            }
            if (hasClient()) {
                k += CodedOutputStreamMicro.computeInt32Size(10, getClient());
            }
            if (hasClientName()) {
                k += CodedOutputStreamMicro.computeStringSize(11, getClientName());
            }
            if (hasGservicesCountryCode()) {
                k += CodedOutputStreamMicro.computeStringSize(12, getGservicesCountryCode());
            }
            this.cachedSize = k;
            return k;
        }

        public long getSystemTimeMs() {
            return this.systemTimeMs_;
        }

        public String getTimeZone() {
            return this.timeZone_;
        }

        public boolean getUseCompressedResponse() {
            return this.useCompressedResponse_;
        }

        public boolean hasBrowserParams() {
            return this.hasBrowserParams;
        }

        public boolean hasClient() {
            return this.hasClient;
        }

        public boolean hasClientName() {
            return this.hasClientName;
        }

        public boolean hasContext() {
            return this.hasContext;
        }

        public boolean hasGservicesCountryCode() {
            return this.hasGservicesCountryCode;
        }

        public boolean hasPreviewParams() {
            return this.hasPreviewParams;
        }

        public boolean hasSafesearchLevel() {
            return this.hasSafesearchLevel;
        }

        public boolean hasScreenParams() {
            return this.hasScreenParams;
        }

        public boolean hasSystemTimeMs() {
            return this.hasSystemTimeMs;
        }

        public boolean hasTimeZone() {
            return this.hasTimeZone;
        }

        public boolean hasUseCompressedResponse() {
            return this.hasUseCompressedResponse;
        }

        public MajelClientInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int i = paramCodedInputStreamMicro.readTag();
                switch (i) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, i)) {
                            continue;
                        }
                    case 0:
                        return this;
                    case 8:
                        setUseCompressedResponse(paramCodedInputStreamMicro.readBool());
                        break;
                    case 16:
                        addCapabilities(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        ContextProtos.Context localContext = new ContextProtos.Context();
                        paramCodedInputStreamMicro.readMessage(localContext);
                        setContext(localContext);
                        break;
                    case 32:
                        setSafesearchLevel(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 42:
                        ClientInfoProtos.PreviewParams localPreviewParams = new ClientInfoProtos.PreviewParams();
                        paramCodedInputStreamMicro.readMessage(localPreviewParams);
                        setPreviewParams(localPreviewParams);
                        break;
                    case 50:
                        ClientInfoProtos.BrowserParams localBrowserParams = new ClientInfoProtos.BrowserParams();
                        paramCodedInputStreamMicro.readMessage(localBrowserParams);
                        setBrowserParams(localBrowserParams);
                        break;
                    case 56:
                        setSystemTimeMs(paramCodedInputStreamMicro.readInt64());
                        break;
                    case 66:
                        setTimeZone(paramCodedInputStreamMicro.readString());
                        break;
                    case 74:
                        ClientInfoProtos.ScreenParams localScreenParams = new ClientInfoProtos.ScreenParams();
                        paramCodedInputStreamMicro.readMessage(localScreenParams);
                        setScreenParams(localScreenParams);
                        break;
                    case 80:
                        setClient(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 90:
                        setClientName(paramCodedInputStreamMicro.readString());
                        break;
                }
                setGservicesCountryCode(paramCodedInputStreamMicro.readString());
            }
        }

        public MajelClientInfo setBrowserParams(ClientInfoProtos.BrowserParams paramBrowserParams) {
            if (paramBrowserParams == null) {
                throw new NullPointerException();
            }
            this.hasBrowserParams = true;
            this.browserParams_ = paramBrowserParams;
            return this;
        }

        public MajelClientInfo setClient(int paramInt) {
            this.hasClient = true;
            this.client_ = paramInt;
            return this;
        }

        public MajelClientInfo setClientName(String paramString) {
            this.hasClientName = true;
            this.clientName_ = paramString;
            return this;
        }

        public MajelClientInfo setContext(ContextProtos.Context paramContext) {
            if (paramContext == null) {
                throw new NullPointerException();
            }
            this.hasContext = true;
            this.context_ = paramContext;
            return this;
        }

        public MajelClientInfo setGservicesCountryCode(String paramString) {
            this.hasGservicesCountryCode = true;
            this.gservicesCountryCode_ = paramString;
            return this;
        }

        public MajelClientInfo setPreviewParams(ClientInfoProtos.PreviewParams paramPreviewParams) {
            if (paramPreviewParams == null) {
                throw new NullPointerException();
            }
            this.hasPreviewParams = true;
            this.previewParams_ = paramPreviewParams;
            return this;
        }

        public MajelClientInfo setSafesearchLevel(int paramInt) {
            this.hasSafesearchLevel = true;
            this.safesearchLevel_ = paramInt;
            return this;
        }

        public MajelClientInfo setScreenParams(ClientInfoProtos.ScreenParams paramScreenParams) {
            if (paramScreenParams == null) {
                throw new NullPointerException();
            }
            this.hasScreenParams = true;
            this.screenParams_ = paramScreenParams;
            return this;
        }

        public MajelClientInfo setSystemTimeMs(long paramLong) {
            this.hasSystemTimeMs = true;
            this.systemTimeMs_ = paramLong;
            return this;
        }

        public MajelClientInfo setTimeZone(String paramString) {
            this.hasTimeZone = true;
            this.timeZone_ = paramString;
            return this;
        }

        public MajelClientInfo setUseCompressedResponse(boolean paramBoolean) {
            this.hasUseCompressedResponse = true;
            this.useCompressedResponse_ = paramBoolean;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasUseCompressedResponse()) {
                paramCodedOutputStreamMicro.writeBool(1, getUseCompressedResponse());
            }
            Iterator localIterator = getCapabilitiesList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(2, ((Integer) localIterator.next()).intValue());
            }
            if (hasContext()) {
                paramCodedOutputStreamMicro.writeMessage(3, getContext());
            }
            if (hasSafesearchLevel()) {
                paramCodedOutputStreamMicro.writeInt32(4, getSafesearchLevel());
            }
            if (hasPreviewParams()) {
                paramCodedOutputStreamMicro.writeMessage(5, getPreviewParams());
            }
            if (hasBrowserParams()) {
                paramCodedOutputStreamMicro.writeMessage(6, getBrowserParams());
            }
            if (hasSystemTimeMs()) {
                paramCodedOutputStreamMicro.writeInt64(7, getSystemTimeMs());
            }
            if (hasTimeZone()) {
                paramCodedOutputStreamMicro.writeString(8, getTimeZone());
            }
            if (hasScreenParams()) {
                paramCodedOutputStreamMicro.writeMessage(9, getScreenParams());
            }
            if (hasClient()) {
                paramCodedOutputStreamMicro.writeInt32(10, getClient());
            }
            if (hasClientName()) {
                paramCodedOutputStreamMicro.writeString(11, getClientName());
            }
            if (hasGservicesCountryCode()) {
                paramCodedOutputStreamMicro.writeString(12, getGservicesCountryCode());
            }
        }
    }

    public static final class MajelServiceEvent
            extends MessageMicro {
        private int cachedSize = -1;
        private ByteStringMicro compressedMajelResponse_ = ByteStringMicro.EMPTY;
        private boolean hasCompressedMajelResponse;
        private boolean hasMajel;
        private MajelProtos.MajelResponse majel_ = null;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public ByteStringMicro getCompressedMajelResponse() {
            return this.compressedMajelResponse_;
        }

        public MajelProtos.MajelResponse getMajel() {
            return this.majel_;
        }

        public int getSerializedSize() {
            boolean bool = hasMajel();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getMajel());
            }
            if (hasCompressedMajelResponse()) {
                i += CodedOutputStreamMicro.computeBytesSize(2, getCompressedMajelResponse());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasCompressedMajelResponse() {
            return this.hasCompressedMajelResponse;
        }

        public boolean hasMajel() {
            return this.hasMajel;
        }

        public MajelServiceEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int i = paramCodedInputStreamMicro.readTag();
                switch (i) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, i)) {
                            continue;
                        }
                    case 0:
                        return this;
                    case 10:
                        MajelProtos.MajelResponse localMajelResponse = new MajelProtos.MajelResponse();
                        paramCodedInputStreamMicro.readMessage(localMajelResponse);
                        setMajel(localMajelResponse);
                        break;
                }
                setCompressedMajelResponse(paramCodedInputStreamMicro.readBytes());
            }
        }

        public MajelServiceEvent setCompressedMajelResponse(ByteStringMicro paramByteStringMicro) {
            this.hasCompressedMajelResponse = true;
            this.compressedMajelResponse_ = paramByteStringMicro;
            return this;
        }

        public MajelServiceEvent setMajel(MajelProtos.MajelResponse paramMajelResponse) {
            if (paramMajelResponse == null) {
                throw new NullPointerException();
            }
            this.hasMajel = true;
            this.majel_ = paramMajelResponse;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasMajel()) {
                paramCodedOutputStreamMicro.writeMessage(1, getMajel());
            }
            if (hasCompressedMajelResponse()) {
                paramCodedOutputStreamMicro.writeBytes(2, getCompressedMajelResponse());
            }
        }
    }

    public static final class MajelServiceRequest
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasOriginalQuery;
        private boolean hasQuery;
        private String originalQuery_ = "";
        private String query_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getOriginalQuery() {
            return this.originalQuery_;
        }

        public String getQuery() {
            return this.query_;
        }

        public int getSerializedSize() {
            boolean bool = hasQuery();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getQuery());
            }
            if (hasOriginalQuery()) {
                i += CodedOutputStreamMicro.computeStringSize(2, getOriginalQuery());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasOriginalQuery() {
            return this.hasOriginalQuery;
        }

        public boolean hasQuery() {
            return this.hasQuery;
        }

        public MajelServiceRequest mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int i = paramCodedInputStreamMicro.readTag();
                switch (i) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, i)) {
                            continue;
                        }
                    case 0:
                        return this;
                    case 10:
                        setQuery(paramCodedInputStreamMicro.readString());
                        break;
                }
                setOriginalQuery(paramCodedInputStreamMicro.readString());
            }
        }

        public MajelServiceRequest setOriginalQuery(String paramString) {
            this.hasOriginalQuery = true;
            this.originalQuery_ = paramString;
            return this;
        }

        public MajelServiceRequest setQuery(String paramString) {
            this.hasQuery = true;
            this.query_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasQuery()) {
                paramCodedOutputStreamMicro.writeString(1, getQuery());
            }
            if (hasOriginalQuery()) {
                paramCodedOutputStreamMicro.writeString(2, getOriginalQuery());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.speech.s3.Majel

 * JD-Core Version:    0.7.0.1

 */