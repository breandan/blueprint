package com.embryo.speech.logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class VoicesearchClientLogProto {
    public static final class AlternateCorrectionData
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasLength;
        private boolean hasNewText;
        private boolean hasOldText;
        private boolean hasRecognizerSegmentIndex;
        private boolean hasStart;
        private boolean hasUnit;
        private int length_ = 0;
        private String newText_ = "";
        private String oldText_ = "";
        private int recognizerSegmentIndex_ = 0;
        private int start_ = 0;
        private int unit_ = 1;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getLength() {
            return this.length_;
        }

        public AlternateCorrectionData setLength(int paramInt) {
            this.hasLength = true;
            this.length_ = paramInt;
            return this;
        }

        public String getNewText() {
            return this.newText_;
        }

        public AlternateCorrectionData setNewText(String paramString) {
            this.hasNewText = true;
            this.newText_ = paramString;
            return this;
        }

        public String getOldText() {
            return this.oldText_;
        }

        public AlternateCorrectionData setOldText(String paramString) {
            this.hasOldText = true;
            this.oldText_ = paramString;
            return this;
        }

        public int getRecognizerSegmentIndex() {
            return this.recognizerSegmentIndex_;
        }

        public AlternateCorrectionData setRecognizerSegmentIndex(int paramInt) {
            this.hasRecognizerSegmentIndex = true;
            this.recognizerSegmentIndex_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasRecognizerSegmentIndex();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getRecognizerSegmentIndex());
            }
            if (hasUnit()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getUnit());
            }
            if (hasStart()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getStart());
            }
            if (hasLength()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(4, getLength());
            }
            if (hasOldText()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(5, getOldText());
            }
            if (hasNewText()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(6, getNewText());
            }
            this.cachedSize = i;
            return i;
        }

        public int getStart() {
            return this.start_;
        }

        public AlternateCorrectionData setStart(int paramInt) {
            this.hasStart = true;
            this.start_ = paramInt;
            return this;
        }

        public int getUnit() {
            return this.unit_;
        }

        public AlternateCorrectionData setUnit(int paramInt) {
            this.hasUnit = true;
            this.unit_ = paramInt;
            return this;
        }

        public boolean hasLength() {
            return this.hasLength;
        }

        public boolean hasNewText() {
            return this.hasNewText;
        }

        public boolean hasOldText() {
            return this.hasOldText;
        }

        public boolean hasRecognizerSegmentIndex() {
            return this.hasRecognizerSegmentIndex;
        }

        public boolean hasStart() {
            return this.hasStart;
        }

        public boolean hasUnit() {
            return this.hasUnit;
        }

        public AlternateCorrectionData mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setRecognizerSegmentIndex(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setUnit(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setStart(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 32:
                        setLength(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 42:
                        setOldText(paramCodedInputStreamMicro.readString());
                        break;
                    case 50:
                        setNewText(paramCodedInputStreamMicro.readString());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasRecognizerSegmentIndex()) {
                paramCodedOutputStreamMicro.writeInt32(1, getRecognizerSegmentIndex());
            }
            if (hasUnit()) {
                paramCodedOutputStreamMicro.writeInt32(2, getUnit());
            }
            if (hasStart()) {
                paramCodedOutputStreamMicro.writeInt32(3, getStart());
            }
            if (hasLength()) {
                paramCodedOutputStreamMicro.writeInt32(4, getLength());
            }
            if (hasOldText()) {
                paramCodedOutputStreamMicro.writeString(5, getOldText());
            }
            if (hasNewText()) {
                paramCodedOutputStreamMicro.writeString(6, getNewText());
            }
        }
    }

    public static final class AuthTokenStatus
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasStatusCode;
        private int statusCode_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getSerializedSize() {
            boolean bool = hasStatusCode();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getStatusCode());
            }
            this.cachedSize = i;
            return i;
        }

        public int getStatusCode() {
            return this.statusCode_;
        }

        public AuthTokenStatus setStatusCode(int paramInt) {
            this.hasStatusCode = true;
            this.statusCode_ = paramInt;
            return this;
        }

        public boolean hasStatusCode() {
            return this.hasStatusCode;
        }

        public AuthTokenStatus mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setStatusCode(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasStatusCode()) {
                paramCodedOutputStreamMicro.writeInt32(1, getStatusCode());
            }
        }
    }

    public static final class BluetoothDevice
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private String deviceNameHash_ = "";
        private String deviceName_ = "";
        private boolean hasDeviceName;
        private boolean hasDeviceNameHash;
        private boolean hasOrgIdentifier;
        private String orgIdentifier_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getDeviceName() {
            return this.deviceName_;
        }

        public BluetoothDevice setDeviceName(String paramString) {
            this.hasDeviceName = true;
            this.deviceName_ = paramString;
            return this;
        }

        public String getDeviceNameHash() {
            return this.deviceNameHash_;
        }

        public BluetoothDevice setDeviceNameHash(String paramString) {
            this.hasDeviceNameHash = true;
            this.deviceNameHash_ = paramString;
            return this;
        }

        public String getOrgIdentifier() {
            return this.orgIdentifier_;
        }

        public BluetoothDevice setOrgIdentifier(String paramString) {
            this.hasOrgIdentifier = true;
            this.orgIdentifier_ = paramString;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasDeviceName();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(1, getDeviceName());
            }
            if (hasDeviceNameHash()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(2, getDeviceNameHash());
            }
            if (hasOrgIdentifier()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(3, getOrgIdentifier());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasDeviceName() {
            return this.hasDeviceName;
        }

        public boolean hasDeviceNameHash() {
            return this.hasDeviceNameHash;
        }

        public boolean hasOrgIdentifier() {
            return this.hasOrgIdentifier;
        }

        public BluetoothDevice mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setDeviceName(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        setDeviceNameHash(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        setOrgIdentifier(paramCodedInputStreamMicro.readString());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasDeviceName()) {
                paramCodedOutputStreamMicro.writeString(1, getDeviceName());
            }
            if (hasDeviceNameHash()) {
                paramCodedOutputStreamMicro.writeString(2, getDeviceNameHash());
            }
            if (hasOrgIdentifier()) {
                paramCodedOutputStreamMicro.writeString(3, getOrgIdentifier());
            }
        }
    }

    public static final class BugReport
            extends com.embryo.protobuf.micro.MessageMicro {
        private int bugNumber_ = 0;
        private int cachedSize = -1;
        private boolean hasBugNumber;

        public int getBugNumber() {
            return this.bugNumber_;
        }

        public BugReport setBugNumber(int paramInt) {
            this.hasBugNumber = true;
            this.bugNumber_ = paramInt;
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getSerializedSize() {
            boolean bool = hasBugNumber();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getBugNumber());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasBugNumber() {
            return this.hasBugNumber;
        }

        public BugReport mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setBugNumber(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasBugNumber()) {
                paramCodedOutputStreamMicro.writeInt32(1, getBugNumber());
            }
        }
    }

    public static final class ClientEvent
            extends com.embryo.protobuf.micro.MessageMicro {
        private VoicesearchClientLogProto.AlternateCorrectionData alternateCorrection_ = null;
        private int audioInputDevice_ = 1;
        private VoicesearchClientLogProto.AuthTokenStatus authTokenStatus_ = null;
        private VoicesearchClientLogProto.BluetoothDevice bluetoothDevice_ = null;
        private VoicesearchClientLogProto.BugReport bugReport_ = null;
        private int cachedSize = -1;
        private int cardType_ = 0;
        private long clientTimeMs_ = 0L;
        private VoicesearchClientLogProto.ContactDisplayInfo contactDisplayInfo_ = null;
        private VoicesearchClientLogProto.ContactInfo contactInfo_ = null;
        private VoicesearchClientLogProto.ContactLookupInfo contactLookupInfo_ = null;
        private VoicesearchClientLogProto.ContactSelectInfo contactSelectInfo_ = null;
        private VoicesearchClientLogProto.EmbeddedParserDetails embeddedParserDetails_ = null;
        private RecognizerOuterClass.RecognizerLog embeddedRecognizerLog_ = null;
        private int errorType_ = 0;
        private int eventSource_ = 1;
        private int eventType_ = 0;
        private VoicesearchClientLogProto.GmsCoreData gmsCoreData_ = null;
        private VoicesearchClientLogProto.GwsCorrectionData gwsCorrection_ = null;
        private boolean hasAlternateCorrection;
        private boolean hasAudioInputDevice;
        private boolean hasAuthTokenStatus;
        private boolean hasBluetoothDevice;
        private boolean hasBugReport;
        private boolean hasCardType;
        private boolean hasClientTimeMs;
        private boolean hasContactDisplayInfo;
        private boolean hasContactInfo;
        private boolean hasContactLookupInfo;
        private boolean hasContactSelectInfo;
        private boolean hasEmbeddedParserDetails;
        private boolean hasEmbeddedRecognizerLog;
        private boolean hasErrorType;
        private boolean hasEventSource;
        private boolean hasEventType;
        private boolean hasGmsCoreData;
        private boolean hasGwsCorrection;
        private boolean hasIcingCorpusDiagnostic;
        private boolean hasIntentType;
        private boolean hasLatency;
        private boolean hasNetworkType;
        private boolean hasOnDeviceSource;
        private boolean hasRequestId;
        private boolean hasRequestType;
        private boolean hasScoOutputDisabled;
        private boolean hasScreenTransition;
        private boolean hasTypingCorrection;
        private VoicesearchClientLogProto.IcingCorpusDiagnostics icingCorpusDiagnostic_ = null;
        private int intentType_ = 0;
        private VoicesearchClientLogProto.LatencyData latency_ = null;
        private int networkType_ = 0;
        private VoicesearchClientLogProto.OnDeviceSource onDeviceSource_ = null;
        private String requestId_ = "";
        private int requestType_ = 1;
        private boolean scoOutputDisabled_ = false;
        private VoicesearchClientLogProto.ScreenTransitionData screenTransition_ = null;
        private VoicesearchClientLogProto.TypingCorrection typingCorrection_ = null;

        public VoicesearchClientLogProto.AlternateCorrectionData getAlternateCorrection() {
            return this.alternateCorrection_;
        }

        public ClientEvent setAlternateCorrection(VoicesearchClientLogProto.AlternateCorrectionData paramAlternateCorrectionData) {
            if (paramAlternateCorrectionData == null) {
                throw new NullPointerException();
            }
            this.hasAlternateCorrection = true;
            this.alternateCorrection_ = paramAlternateCorrectionData;
            return this;
        }

        public int getAudioInputDevice() {
            return this.audioInputDevice_;
        }

        public ClientEvent setAudioInputDevice(int paramInt) {
            this.hasAudioInputDevice = true;
            this.audioInputDevice_ = paramInt;
            return this;
        }

        public VoicesearchClientLogProto.AuthTokenStatus getAuthTokenStatus() {
            return this.authTokenStatus_;
        }

        public ClientEvent setAuthTokenStatus(VoicesearchClientLogProto.AuthTokenStatus paramAuthTokenStatus) {
            if (paramAuthTokenStatus == null) {
                throw new NullPointerException();
            }
            this.hasAuthTokenStatus = true;
            this.authTokenStatus_ = paramAuthTokenStatus;
            return this;
        }

        public VoicesearchClientLogProto.BluetoothDevice getBluetoothDevice() {
            return this.bluetoothDevice_;
        }

        public ClientEvent setBluetoothDevice(VoicesearchClientLogProto.BluetoothDevice paramBluetoothDevice) {
            if (paramBluetoothDevice == null) {
                throw new NullPointerException();
            }
            this.hasBluetoothDevice = true;
            this.bluetoothDevice_ = paramBluetoothDevice;
            return this;
        }

        public VoicesearchClientLogProto.BugReport getBugReport() {
            return this.bugReport_;
        }

        public ClientEvent setBugReport(VoicesearchClientLogProto.BugReport paramBugReport) {
            if (paramBugReport == null) {
                throw new NullPointerException();
            }
            this.hasBugReport = true;
            this.bugReport_ = paramBugReport;
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getCardType() {
            return this.cardType_;
        }

        public ClientEvent setCardType(int paramInt) {
            this.hasCardType = true;
            this.cardType_ = paramInt;
            return this;
        }

        public long getClientTimeMs() {
            return this.clientTimeMs_;
        }

        public ClientEvent setClientTimeMs(long paramLong) {
            this.hasClientTimeMs = true;
            this.clientTimeMs_ = paramLong;
            return this;
        }

        public VoicesearchClientLogProto.ContactDisplayInfo getContactDisplayInfo() {
            return this.contactDisplayInfo_;
        }

        public ClientEvent setContactDisplayInfo(VoicesearchClientLogProto.ContactDisplayInfo paramContactDisplayInfo) {
            if (paramContactDisplayInfo == null) {
                throw new NullPointerException();
            }
            this.hasContactDisplayInfo = true;
            this.contactDisplayInfo_ = paramContactDisplayInfo;
            return this;
        }

        public VoicesearchClientLogProto.ContactInfo getContactInfo() {
            return this.contactInfo_;
        }

        public ClientEvent setContactInfo(VoicesearchClientLogProto.ContactInfo paramContactInfo) {
            if (paramContactInfo == null) {
                throw new NullPointerException();
            }
            this.hasContactInfo = true;
            this.contactInfo_ = paramContactInfo;
            return this;
        }

        public VoicesearchClientLogProto.ContactLookupInfo getContactLookupInfo() {
            return this.contactLookupInfo_;
        }

        public ClientEvent setContactLookupInfo(VoicesearchClientLogProto.ContactLookupInfo paramContactLookupInfo) {
            if (paramContactLookupInfo == null) {
                throw new NullPointerException();
            }
            this.hasContactLookupInfo = true;
            this.contactLookupInfo_ = paramContactLookupInfo;
            return this;
        }

        public VoicesearchClientLogProto.ContactSelectInfo getContactSelectInfo() {
            return this.contactSelectInfo_;
        }

        public ClientEvent setContactSelectInfo(VoicesearchClientLogProto.ContactSelectInfo paramContactSelectInfo) {
            if (paramContactSelectInfo == null) {
                throw new NullPointerException();
            }
            this.hasContactSelectInfo = true;
            this.contactSelectInfo_ = paramContactSelectInfo;
            return this;
        }

        public VoicesearchClientLogProto.EmbeddedParserDetails getEmbeddedParserDetails() {
            return this.embeddedParserDetails_;
        }

        public ClientEvent setEmbeddedParserDetails(VoicesearchClientLogProto.EmbeddedParserDetails paramEmbeddedParserDetails) {
            if (paramEmbeddedParserDetails == null) {
                throw new NullPointerException();
            }
            this.hasEmbeddedParserDetails = true;
            this.embeddedParserDetails_ = paramEmbeddedParserDetails;
            return this;
        }

        public RecognizerOuterClass.RecognizerLog getEmbeddedRecognizerLog() {
            return this.embeddedRecognizerLog_;
        }

        public ClientEvent setEmbeddedRecognizerLog(RecognizerOuterClass.RecognizerLog paramRecognizerLog) {
            if (paramRecognizerLog == null) {
                throw new NullPointerException();
            }
            this.hasEmbeddedRecognizerLog = true;
            this.embeddedRecognizerLog_ = paramRecognizerLog;
            return this;
        }

        public int getErrorType() {
            return this.errorType_;
        }

        public ClientEvent setErrorType(int paramInt) {
            this.hasErrorType = true;
            this.errorType_ = paramInt;
            return this;
        }

        public int getEventSource() {
            return this.eventSource_;
        }

        public ClientEvent setEventSource(int paramInt) {
            this.hasEventSource = true;
            this.eventSource_ = paramInt;
            return this;
        }

        public int getEventType() {
            return this.eventType_;
        }

        public ClientEvent setEventType(int paramInt) {
            this.hasEventType = true;
            this.eventType_ = paramInt;
            return this;
        }

        public VoicesearchClientLogProto.GmsCoreData getGmsCoreData() {
            return this.gmsCoreData_;
        }

        public ClientEvent setGmsCoreData(VoicesearchClientLogProto.GmsCoreData paramGmsCoreData) {
            if (paramGmsCoreData == null) {
                throw new NullPointerException();
            }
            this.hasGmsCoreData = true;
            this.gmsCoreData_ = paramGmsCoreData;
            return this;
        }

        public VoicesearchClientLogProto.GwsCorrectionData getGwsCorrection() {
            return this.gwsCorrection_;
        }

        public ClientEvent setGwsCorrection(VoicesearchClientLogProto.GwsCorrectionData paramGwsCorrectionData) {
            if (paramGwsCorrectionData == null) {
                throw new NullPointerException();
            }
            this.hasGwsCorrection = true;
            this.gwsCorrection_ = paramGwsCorrectionData;
            return this;
        }

        public VoicesearchClientLogProto.IcingCorpusDiagnostics getIcingCorpusDiagnostic() {
            return this.icingCorpusDiagnostic_;
        }

        public ClientEvent setIcingCorpusDiagnostic(VoicesearchClientLogProto.IcingCorpusDiagnostics paramIcingCorpusDiagnostics) {
            if (paramIcingCorpusDiagnostics == null) {
                throw new NullPointerException();
            }
            this.hasIcingCorpusDiagnostic = true;
            this.icingCorpusDiagnostic_ = paramIcingCorpusDiagnostics;
            return this;
        }

        public int getIntentType() {
            return this.intentType_;
        }

        public ClientEvent setIntentType(int paramInt) {
            this.hasIntentType = true;
            this.intentType_ = paramInt;
            return this;
        }

        public VoicesearchClientLogProto.LatencyData getLatency() {
            return this.latency_;
        }

        public ClientEvent setLatency(VoicesearchClientLogProto.LatencyData paramLatencyData) {
            if (paramLatencyData == null) {
                throw new NullPointerException();
            }
            this.hasLatency = true;
            this.latency_ = paramLatencyData;
            return this;
        }

        public int getNetworkType() {
            return this.networkType_;
        }

        public ClientEvent setNetworkType(int paramInt) {
            this.hasNetworkType = true;
            this.networkType_ = paramInt;
            return this;
        }

        public VoicesearchClientLogProto.OnDeviceSource getOnDeviceSource() {
            return this.onDeviceSource_;
        }

        public ClientEvent setOnDeviceSource(VoicesearchClientLogProto.OnDeviceSource paramOnDeviceSource) {
            if (paramOnDeviceSource == null) {
                throw new NullPointerException();
            }
            this.hasOnDeviceSource = true;
            this.onDeviceSource_ = paramOnDeviceSource;
            return this;
        }

        public String getRequestId() {
            return this.requestId_;
        }

        public ClientEvent setRequestId(String paramString) {
            this.hasRequestId = true;
            this.requestId_ = paramString;
            return this;
        }

        public int getRequestType() {
            return this.requestType_;
        }

        public ClientEvent setRequestType(int paramInt) {
            this.hasRequestType = true;
            this.requestType_ = paramInt;
            return this;
        }

        public boolean getScoOutputDisabled() {
            return this.scoOutputDisabled_;
        }

        public ClientEvent setScoOutputDisabled(boolean paramBoolean) {
            this.hasScoOutputDisabled = true;
            this.scoOutputDisabled_ = paramBoolean;
            return this;
        }

        public VoicesearchClientLogProto.ScreenTransitionData getScreenTransition() {
            return this.screenTransition_;
        }

        public ClientEvent setScreenTransition(VoicesearchClientLogProto.ScreenTransitionData paramScreenTransitionData) {
            if (paramScreenTransitionData == null) {
                throw new NullPointerException();
            }
            this.hasScreenTransition = true;
            this.screenTransition_ = paramScreenTransitionData;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasClientTimeMs();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeUInt64Size(1, getClientTimeMs());
            }
            if (hasEventType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getEventType());
            }
            if (hasRequestId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(3, getRequestId());
            }
            if (hasScreenTransition()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(4, getScreenTransition());
            }
            if (hasCardType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(5, getCardType());
            }
            if (hasLatency()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(6, getLatency());
            }
            if (hasAlternateCorrection()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(7, getAlternateCorrection());
            }
            if (hasBugReport()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(8, getBugReport());
            }
            if (hasEmbeddedRecognizerLog()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(9, getEmbeddedRecognizerLog());
            }
            if (hasAudioInputDevice()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(10, getAudioInputDevice());
            }
            if (hasEventSource()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(11, getEventSource());
            }
            if (hasContactInfo()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(12, getContactInfo());
            }
            if (hasRequestType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(13, getRequestType());
            }
            if (hasTypingCorrection()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(14, getTypingCorrection());
            }
            if (hasOnDeviceSource()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(15, getOnDeviceSource());
            }
            if (hasIntentType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(16, getIntentType());
            }
            if (hasNetworkType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(17, getNetworkType());
            }
            if (hasScoOutputDisabled()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeBoolSize(18, getScoOutputDisabled());
            }
            if (hasErrorType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(19, getErrorType());
            }
            if (hasGmsCoreData()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(20, getGmsCoreData());
            }
            if (hasAuthTokenStatus()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(21, getAuthTokenStatus());
            }
            if (hasIcingCorpusDiagnostic()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(22, getIcingCorpusDiagnostic());
            }
            if (hasEmbeddedParserDetails()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(23, getEmbeddedParserDetails());
            }
            if (hasContactLookupInfo()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(24, getContactLookupInfo());
            }
            if (hasContactDisplayInfo()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(25, getContactDisplayInfo());
            }
            if (hasContactSelectInfo()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(26, getContactSelectInfo());
            }
            if (hasBluetoothDevice()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(27, getBluetoothDevice());
            }
            if (hasGwsCorrection()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(28, getGwsCorrection());
            }
            this.cachedSize = i;
            return i;
        }

        public VoicesearchClientLogProto.TypingCorrection getTypingCorrection() {
            return this.typingCorrection_;
        }

        public ClientEvent setTypingCorrection(VoicesearchClientLogProto.TypingCorrection paramTypingCorrection) {
            if (paramTypingCorrection == null) {
                throw new NullPointerException();
            }
            this.hasTypingCorrection = true;
            this.typingCorrection_ = paramTypingCorrection;
            return this;
        }

        public boolean hasAlternateCorrection() {
            return this.hasAlternateCorrection;
        }

        public boolean hasAudioInputDevice() {
            return this.hasAudioInputDevice;
        }

        public boolean hasAuthTokenStatus() {
            return this.hasAuthTokenStatus;
        }

        public boolean hasBluetoothDevice() {
            return this.hasBluetoothDevice;
        }

        public boolean hasBugReport() {
            return this.hasBugReport;
        }

        public boolean hasCardType() {
            return this.hasCardType;
        }

        public boolean hasClientTimeMs() {
            return this.hasClientTimeMs;
        }

        public boolean hasContactDisplayInfo() {
            return this.hasContactDisplayInfo;
        }

        public boolean hasContactInfo() {
            return this.hasContactInfo;
        }

        public boolean hasContactLookupInfo() {
            return this.hasContactLookupInfo;
        }

        public boolean hasContactSelectInfo() {
            return this.hasContactSelectInfo;
        }

        public boolean hasEmbeddedParserDetails() {
            return this.hasEmbeddedParserDetails;
        }

        public boolean hasEmbeddedRecognizerLog() {
            return this.hasEmbeddedRecognizerLog;
        }

        public boolean hasErrorType() {
            return this.hasErrorType;
        }

        public boolean hasEventSource() {
            return this.hasEventSource;
        }

        public boolean hasEventType() {
            return this.hasEventType;
        }

        public boolean hasGmsCoreData() {
            return this.hasGmsCoreData;
        }

        public boolean hasGwsCorrection() {
            return this.hasGwsCorrection;
        }

        public boolean hasIcingCorpusDiagnostic() {
            return this.hasIcingCorpusDiagnostic;
        }

        public boolean hasIntentType() {
            return this.hasIntentType;
        }

        public boolean hasLatency() {
            return this.hasLatency;
        }

        public boolean hasNetworkType() {
            return this.hasNetworkType;
        }

        public boolean hasOnDeviceSource() {
            return this.hasOnDeviceSource;
        }

        public boolean hasRequestId() {
            return this.hasRequestId;
        }

        public boolean hasRequestType() {
            return this.hasRequestType;
        }

        public boolean hasScoOutputDisabled() {
            return this.hasScoOutputDisabled;
        }

        public boolean hasScreenTransition() {
            return this.hasScreenTransition;
        }

        public boolean hasTypingCorrection() {
            return this.hasTypingCorrection;
        }

        public ClientEvent mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setClientTimeMs(paramCodedInputStreamMicro.readUInt64());
                        break;
                    case 16:
                        setEventType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setRequestId(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        VoicesearchClientLogProto.ScreenTransitionData localScreenTransitionData = new VoicesearchClientLogProto.ScreenTransitionData();
                        paramCodedInputStreamMicro.readMessage(localScreenTransitionData);
                        setScreenTransition(localScreenTransitionData);
                        break;
                    case 40:
                        setCardType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 50:
                        VoicesearchClientLogProto.LatencyData localLatencyData = new VoicesearchClientLogProto.LatencyData();
                        paramCodedInputStreamMicro.readMessage(localLatencyData);
                        setLatency(localLatencyData);
                        break;
                    case 58:
                        VoicesearchClientLogProto.AlternateCorrectionData localAlternateCorrectionData = new VoicesearchClientLogProto.AlternateCorrectionData();
                        paramCodedInputStreamMicro.readMessage(localAlternateCorrectionData);
                        setAlternateCorrection(localAlternateCorrectionData);
                        break;
                    case 66:
                        VoicesearchClientLogProto.BugReport localBugReport = new VoicesearchClientLogProto.BugReport();
                        paramCodedInputStreamMicro.readMessage(localBugReport);
                        setBugReport(localBugReport);
                        break;
                    case 74:
                        RecognizerOuterClass.RecognizerLog localRecognizerLog = new RecognizerOuterClass.RecognizerLog();
                        paramCodedInputStreamMicro.readMessage(localRecognizerLog);
                        setEmbeddedRecognizerLog(localRecognizerLog);
                        break;
                    case 80:
                        setAudioInputDevice(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 88:
                        setEventSource(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 98:
                        VoicesearchClientLogProto.ContactInfo localContactInfo = new VoicesearchClientLogProto.ContactInfo();
                        paramCodedInputStreamMicro.readMessage(localContactInfo);
                        setContactInfo(localContactInfo);
                        break;
                    case 104:
                        setRequestType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 114:
                        VoicesearchClientLogProto.TypingCorrection localTypingCorrection = new VoicesearchClientLogProto.TypingCorrection();
                        paramCodedInputStreamMicro.readMessage(localTypingCorrection);
                        setTypingCorrection(localTypingCorrection);
                        break;
                    case 122:
                        VoicesearchClientLogProto.OnDeviceSource localOnDeviceSource = new VoicesearchClientLogProto.OnDeviceSource();
                        paramCodedInputStreamMicro.readMessage(localOnDeviceSource);
                        setOnDeviceSource(localOnDeviceSource);
                        break;
                    case 128:
                        setIntentType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 136:
                        setNetworkType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 144:
                        setScoOutputDisabled(paramCodedInputStreamMicro.readBool());
                        break;
                    case 152:
                        setErrorType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 162:
                        VoicesearchClientLogProto.GmsCoreData localGmsCoreData = new VoicesearchClientLogProto.GmsCoreData();
                        paramCodedInputStreamMicro.readMessage(localGmsCoreData);
                        setGmsCoreData(localGmsCoreData);
                        break;
                    case 170:
                        VoicesearchClientLogProto.AuthTokenStatus localAuthTokenStatus = new VoicesearchClientLogProto.AuthTokenStatus();
                        paramCodedInputStreamMicro.readMessage(localAuthTokenStatus);
                        setAuthTokenStatus(localAuthTokenStatus);
                        break;
                    case 178:
                        VoicesearchClientLogProto.IcingCorpusDiagnostics localIcingCorpusDiagnostics = new VoicesearchClientLogProto.IcingCorpusDiagnostics();
                        paramCodedInputStreamMicro.readMessage(localIcingCorpusDiagnostics);
                        setIcingCorpusDiagnostic(localIcingCorpusDiagnostics);
                        break;
                    case 186:
                        VoicesearchClientLogProto.EmbeddedParserDetails localEmbeddedParserDetails = new VoicesearchClientLogProto.EmbeddedParserDetails();
                        paramCodedInputStreamMicro.readMessage(localEmbeddedParserDetails);
                        setEmbeddedParserDetails(localEmbeddedParserDetails);
                        break;
                    case 194:
                        VoicesearchClientLogProto.ContactLookupInfo localContactLookupInfo = new VoicesearchClientLogProto.ContactLookupInfo();
                        paramCodedInputStreamMicro.readMessage(localContactLookupInfo);
                        setContactLookupInfo(localContactLookupInfo);
                        break;
                    case 202:
                        VoicesearchClientLogProto.ContactDisplayInfo localContactDisplayInfo = new VoicesearchClientLogProto.ContactDisplayInfo();
                        paramCodedInputStreamMicro.readMessage(localContactDisplayInfo);
                        setContactDisplayInfo(localContactDisplayInfo);
                        break;
                    case 210:
                        VoicesearchClientLogProto.ContactSelectInfo localContactSelectInfo = new VoicesearchClientLogProto.ContactSelectInfo();
                        paramCodedInputStreamMicro.readMessage(localContactSelectInfo);
                        setContactSelectInfo(localContactSelectInfo);
                        break;
                    case 218:
                        VoicesearchClientLogProto.BluetoothDevice localBluetoothDevice = new VoicesearchClientLogProto.BluetoothDevice();
                        paramCodedInputStreamMicro.readMessage(localBluetoothDevice);
                        setBluetoothDevice(localBluetoothDevice);
                        break;
                    case 226:
                        VoicesearchClientLogProto.GwsCorrectionData localGwsCorrectionData = new VoicesearchClientLogProto.GwsCorrectionData();
                        paramCodedInputStreamMicro.readMessage(localGwsCorrectionData);
                        setGwsCorrection(localGwsCorrectionData);
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasClientTimeMs()) {
                paramCodedOutputStreamMicro.writeUInt64(1, getClientTimeMs());
            }
            if (hasEventType()) {
                paramCodedOutputStreamMicro.writeInt32(2, getEventType());
            }
            if (hasRequestId()) {
                paramCodedOutputStreamMicro.writeString(3, getRequestId());
            }
            if (hasScreenTransition()) {
                paramCodedOutputStreamMicro.writeMessage(4, getScreenTransition());
            }
            if (hasCardType()) {
                paramCodedOutputStreamMicro.writeInt32(5, getCardType());
            }
            if (hasLatency()) {
                paramCodedOutputStreamMicro.writeMessage(6, getLatency());
            }
            if (hasAlternateCorrection()) {
                paramCodedOutputStreamMicro.writeMessage(7, getAlternateCorrection());
            }
            if (hasBugReport()) {
                paramCodedOutputStreamMicro.writeMessage(8, getBugReport());
            }
            if (hasEmbeddedRecognizerLog()) {
                paramCodedOutputStreamMicro.writeMessage(9, getEmbeddedRecognizerLog());
            }
            if (hasAudioInputDevice()) {
                paramCodedOutputStreamMicro.writeInt32(10, getAudioInputDevice());
            }
            if (hasEventSource()) {
                paramCodedOutputStreamMicro.writeInt32(11, getEventSource());
            }
            if (hasContactInfo()) {
                paramCodedOutputStreamMicro.writeMessage(12, getContactInfo());
            }
            if (hasRequestType()) {
                paramCodedOutputStreamMicro.writeInt32(13, getRequestType());
            }
            if (hasTypingCorrection()) {
                paramCodedOutputStreamMicro.writeMessage(14, getTypingCorrection());
            }
            if (hasOnDeviceSource()) {
                paramCodedOutputStreamMicro.writeMessage(15, getOnDeviceSource());
            }
            if (hasIntentType()) {
                paramCodedOutputStreamMicro.writeInt32(16, getIntentType());
            }
            if (hasNetworkType()) {
                paramCodedOutputStreamMicro.writeInt32(17, getNetworkType());
            }
            if (hasScoOutputDisabled()) {
                paramCodedOutputStreamMicro.writeBool(18, getScoOutputDisabled());
            }
            if (hasErrorType()) {
                paramCodedOutputStreamMicro.writeInt32(19, getErrorType());
            }
            if (hasGmsCoreData()) {
                paramCodedOutputStreamMicro.writeMessage(20, getGmsCoreData());
            }
            if (hasAuthTokenStatus()) {
                paramCodedOutputStreamMicro.writeMessage(21, getAuthTokenStatus());
            }
            if (hasIcingCorpusDiagnostic()) {
                paramCodedOutputStreamMicro.writeMessage(22, getIcingCorpusDiagnostic());
            }
            if (hasEmbeddedParserDetails()) {
                paramCodedOutputStreamMicro.writeMessage(23, getEmbeddedParserDetails());
            }
            if (hasContactLookupInfo()) {
                paramCodedOutputStreamMicro.writeMessage(24, getContactLookupInfo());
            }
            if (hasContactDisplayInfo()) {
                paramCodedOutputStreamMicro.writeMessage(25, getContactDisplayInfo());
            }
            if (hasContactSelectInfo()) {
                paramCodedOutputStreamMicro.writeMessage(26, getContactSelectInfo());
            }
            if (hasBluetoothDevice()) {
                paramCodedOutputStreamMicro.writeMessage(27, getBluetoothDevice());
            }
            if (hasGwsCorrection()) {
                paramCodedOutputStreamMicro.writeMessage(28, getGwsCorrection());
            }
        }
    }

    public static final class ContactDisplayInfo
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int contactShownCount_ = 0;
        private boolean hasContactShownCount;
        private boolean hasPeopleShownCount;
        private boolean hasSyncedContactShownCount;
        private int peopleShownCount_ = 0;
        private int syncedContactShownCount_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getContactShownCount() {
            return this.contactShownCount_;
        }

        public ContactDisplayInfo setContactShownCount(int paramInt) {
            this.hasContactShownCount = true;
            this.contactShownCount_ = paramInt;
            return this;
        }

        public int getPeopleShownCount() {
            return this.peopleShownCount_;
        }

        public ContactDisplayInfo setPeopleShownCount(int paramInt) {
            this.hasPeopleShownCount = true;
            this.peopleShownCount_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasContactShownCount();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getContactShownCount());
            }
            if (hasPeopleShownCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getPeopleShownCount());
            }
            if (hasSyncedContactShownCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getSyncedContactShownCount());
            }
            this.cachedSize = i;
            return i;
        }

        public int getSyncedContactShownCount() {
            return this.syncedContactShownCount_;
        }

        public ContactDisplayInfo setSyncedContactShownCount(int paramInt) {
            this.hasSyncedContactShownCount = true;
            this.syncedContactShownCount_ = paramInt;
            return this;
        }

        public boolean hasContactShownCount() {
            return this.hasContactShownCount;
        }

        public boolean hasPeopleShownCount() {
            return this.hasPeopleShownCount;
        }

        public boolean hasSyncedContactShownCount() {
            return this.hasSyncedContactShownCount;
        }

        public ContactDisplayInfo mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setContactShownCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setPeopleShownCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setSyncedContactShownCount(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasContactShownCount()) {
                paramCodedOutputStreamMicro.writeInt32(1, getContactShownCount());
            }
            if (hasPeopleShownCount()) {
                paramCodedOutputStreamMicro.writeInt32(2, getPeopleShownCount());
            }
            if (hasSyncedContactShownCount()) {
                paramCodedOutputStreamMicro.writeInt32(3, getSyncedContactShownCount());
            }
        }
    }

    public static final class ContactInfo
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private double grammarWeight_ = 0.0D;
        private boolean hasGrammarWeight;
        private boolean hasSource;
        private boolean hasSyncedContact;
        private int source_ = 0;
        private boolean syncedContact_ = false;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public double getGrammarWeight() {
            return this.grammarWeight_;
        }

        public ContactInfo setGrammarWeight(double paramDouble) {
            this.hasGrammarWeight = true;
            this.grammarWeight_ = paramDouble;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasGrammarWeight();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeDoubleSize(1, getGrammarWeight());
            }
            if (hasSyncedContact()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeBoolSize(2, getSyncedContact());
            }
            if (hasSource()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getSource());
            }
            this.cachedSize = i;
            return i;
        }

        public int getSource() {
            return this.source_;
        }

        public ContactInfo setSource(int paramInt) {
            this.hasSource = true;
            this.source_ = paramInt;
            return this;
        }

        public boolean getSyncedContact() {
            return this.syncedContact_;
        }

        public ContactInfo setSyncedContact(boolean paramBoolean) {
            this.hasSyncedContact = true;
            this.syncedContact_ = paramBoolean;
            return this;
        }

        public boolean hasGrammarWeight() {
            return this.hasGrammarWeight;
        }

        public boolean hasSource() {
            return this.hasSource;
        }

        public boolean hasSyncedContact() {
            return this.hasSyncedContact;
        }

        public ContactInfo mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                    case 9:
                        setGrammarWeight(paramCodedInputStreamMicro.readDouble());
                        break;
                    case 16:
                        setSyncedContact(paramCodedInputStreamMicro.readBool());
                        break;
                    case 24:
                        setSource(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasGrammarWeight()) {
                paramCodedOutputStreamMicro.writeDouble(1, getGrammarWeight());
            }
            if (hasSyncedContact()) {
                paramCodedOutputStreamMicro.writeBool(2, getSyncedContact());
            }
            if (hasSource()) {
                paramCodedOutputStreamMicro.writeInt32(3, getSource());
            }
        }
    }

    public static final class ContactLookupInfo
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int contactDedupCount_ = 0;
        private int exactQueryCount_ = 0;
        private int fuzzyQueryCount_ = 0;
        private boolean hasContactDedupCount;
        private boolean hasExactQueryCount;
        private boolean hasFuzzyQueryCount;
        private boolean hasNameMatchCount;
        private boolean hasNameTypeMatchCount;
        private boolean hasPeopleFoundCount;
        private boolean hasPrimaryFilterCount;
        private int nameMatchCount_ = 0;
        private int nameTypeMatchCount_ = 0;
        private int peopleFoundCount_ = 0;
        private int primaryFilterCount_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getContactDedupCount() {
            return this.contactDedupCount_;
        }

        public ContactLookupInfo setContactDedupCount(int paramInt) {
            this.hasContactDedupCount = true;
            this.contactDedupCount_ = paramInt;
            return this;
        }

        public int getExactQueryCount() {
            return this.exactQueryCount_;
        }

        public ContactLookupInfo setExactQueryCount(int paramInt) {
            this.hasExactQueryCount = true;
            this.exactQueryCount_ = paramInt;
            return this;
        }

        public int getFuzzyQueryCount() {
            return this.fuzzyQueryCount_;
        }

        public ContactLookupInfo setFuzzyQueryCount(int paramInt) {
            this.hasFuzzyQueryCount = true;
            this.fuzzyQueryCount_ = paramInt;
            return this;
        }

        public int getNameMatchCount() {
            return this.nameMatchCount_;
        }

        public ContactLookupInfo setNameMatchCount(int paramInt) {
            this.hasNameMatchCount = true;
            this.nameMatchCount_ = paramInt;
            return this;
        }

        public int getNameTypeMatchCount() {
            return this.nameTypeMatchCount_;
        }

        public ContactLookupInfo setNameTypeMatchCount(int paramInt) {
            this.hasNameTypeMatchCount = true;
            this.nameTypeMatchCount_ = paramInt;
            return this;
        }

        public int getPeopleFoundCount() {
            return this.peopleFoundCount_;
        }

        public ContactLookupInfo setPeopleFoundCount(int paramInt) {
            this.hasPeopleFoundCount = true;
            this.peopleFoundCount_ = paramInt;
            return this;
        }

        public int getPrimaryFilterCount() {
            return this.primaryFilterCount_;
        }

        public ContactLookupInfo setPrimaryFilterCount(int paramInt) {
            this.hasPrimaryFilterCount = true;
            this.primaryFilterCount_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasExactQueryCount();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getExactQueryCount());
            }
            if (hasFuzzyQueryCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getFuzzyQueryCount());
            }
            if (hasNameTypeMatchCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getNameTypeMatchCount());
            }
            if (hasNameMatchCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(4, getNameMatchCount());
            }
            if (hasContactDedupCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(5, getContactDedupCount());
            }
            if (hasPrimaryFilterCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(6, getPrimaryFilterCount());
            }
            if (hasPeopleFoundCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(7, getPeopleFoundCount());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasContactDedupCount() {
            return this.hasContactDedupCount;
        }

        public boolean hasExactQueryCount() {
            return this.hasExactQueryCount;
        }

        public boolean hasFuzzyQueryCount() {
            return this.hasFuzzyQueryCount;
        }

        public boolean hasNameMatchCount() {
            return this.hasNameMatchCount;
        }

        public boolean hasNameTypeMatchCount() {
            return this.hasNameTypeMatchCount;
        }

        public boolean hasPeopleFoundCount() {
            return this.hasPeopleFoundCount;
        }

        public boolean hasPrimaryFilterCount() {
            return this.hasPrimaryFilterCount;
        }

        public ContactLookupInfo mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setExactQueryCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setFuzzyQueryCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setNameTypeMatchCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 32:
                        setNameMatchCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 40:
                        setContactDedupCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 48:
                        setPrimaryFilterCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 56:
                        setPeopleFoundCount(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasExactQueryCount()) {
                paramCodedOutputStreamMicro.writeInt32(1, getExactQueryCount());
            }
            if (hasFuzzyQueryCount()) {
                paramCodedOutputStreamMicro.writeInt32(2, getFuzzyQueryCount());
            }
            if (hasNameTypeMatchCount()) {
                paramCodedOutputStreamMicro.writeInt32(3, getNameTypeMatchCount());
            }
            if (hasNameMatchCount()) {
                paramCodedOutputStreamMicro.writeInt32(4, getNameMatchCount());
            }
            if (hasContactDedupCount()) {
                paramCodedOutputStreamMicro.writeInt32(5, getContactDedupCount());
            }
            if (hasPrimaryFilterCount()) {
                paramCodedOutputStreamMicro.writeInt32(6, getPrimaryFilterCount());
            }
            if (hasPeopleFoundCount()) {
                paramCodedOutputStreamMicro.writeInt32(7, getPeopleFoundCount());
            }
        }
    }

    public static final class ContactSelectInfo
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int contactSelectPosition_ = 0;
        private boolean hasContactSelectPosition;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getContactSelectPosition() {
            return this.contactSelectPosition_;
        }

        public ContactSelectInfo setContactSelectPosition(int paramInt) {
            this.hasContactSelectPosition = true;
            this.contactSelectPosition_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasContactSelectPosition();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getContactSelectPosition());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasContactSelectPosition() {
            return this.hasContactSelectPosition;
        }

        public ContactSelectInfo mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setContactSelectPosition(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasContactSelectPosition()) {
                paramCodedOutputStreamMicro.writeInt32(1, getContactSelectPosition());
            }
        }
    }

    public static final class EmbeddedParserDetails
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasHypothesisIndex;
        private int hypothesisIndex_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getHypothesisIndex() {
            return this.hypothesisIndex_;
        }

        public EmbeddedParserDetails setHypothesisIndex(int paramInt) {
            this.hasHypothesisIndex = true;
            this.hypothesisIndex_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasHypothesisIndex();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getHypothesisIndex());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasHypothesisIndex() {
            return this.hasHypothesisIndex;
        }

        public EmbeddedParserDetails mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setHypothesisIndex(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasHypothesisIndex()) {
                paramCodedOutputStreamMicro.writeInt32(1, getHypothesisIndex());
            }
        }
    }

    public static final class GmsCoreData
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int googlePlayServicesAvailability_ = 0;
        private int googlePlayServicesVersionCode_ = 0;
        private boolean hasGooglePlayServicesAvailability;
        private boolean hasGooglePlayServicesVersionCode;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getGooglePlayServicesAvailability() {
            return this.googlePlayServicesAvailability_;
        }

        public GmsCoreData setGooglePlayServicesAvailability(int paramInt) {
            this.hasGooglePlayServicesAvailability = true;
            this.googlePlayServicesAvailability_ = paramInt;
            return this;
        }

        public int getGooglePlayServicesVersionCode() {
            return this.googlePlayServicesVersionCode_;
        }

        public GmsCoreData setGooglePlayServicesVersionCode(int paramInt) {
            this.hasGooglePlayServicesVersionCode = true;
            this.googlePlayServicesVersionCode_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasGooglePlayServicesAvailability();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getGooglePlayServicesAvailability());
            }
            if (hasGooglePlayServicesVersionCode()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getGooglePlayServicesVersionCode());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasGooglePlayServicesAvailability() {
            return this.hasGooglePlayServicesAvailability;
        }

        public boolean hasGooglePlayServicesVersionCode() {
            return this.hasGooglePlayServicesVersionCode;
        }

        public GmsCoreData mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setGooglePlayServicesAvailability(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setGooglePlayServicesVersionCode(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasGooglePlayServicesAvailability()) {
                paramCodedOutputStreamMicro.writeInt32(1, getGooglePlayServicesAvailability());
            }
            if (hasGooglePlayServicesVersionCode()) {
                paramCodedOutputStreamMicro.writeInt32(2, getGooglePlayServicesVersionCode());
            }
        }
    }

    public static final class GwsCorrectionData
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasLength;
        private boolean hasNewText;
        private boolean hasOldText;
        private boolean hasStart;
        private int length_ = 0;
        private String newText_ = "";
        private String oldText_ = "";
        private int start_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getLength() {
            return this.length_;
        }

        public GwsCorrectionData setLength(int paramInt) {
            this.hasLength = true;
            this.length_ = paramInt;
            return this;
        }

        public String getNewText() {
            return this.newText_;
        }

        public GwsCorrectionData setNewText(String paramString) {
            this.hasNewText = true;
            this.newText_ = paramString;
            return this;
        }

        public String getOldText() {
            return this.oldText_;
        }

        public GwsCorrectionData setOldText(String paramString) {
            this.hasOldText = true;
            this.oldText_ = paramString;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasStart();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getStart());
            }
            if (hasLength()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getLength());
            }
            if (hasOldText()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(3, getOldText());
            }
            if (hasNewText()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(4, getNewText());
            }
            this.cachedSize = i;
            return i;
        }

        public int getStart() {
            return this.start_;
        }

        public GwsCorrectionData setStart(int paramInt) {
            this.hasStart = true;
            this.start_ = paramInt;
            return this;
        }

        public boolean hasLength() {
            return this.hasLength;
        }

        public boolean hasNewText() {
            return this.hasNewText;
        }

        public boolean hasOldText() {
            return this.hasOldText;
        }

        public boolean hasStart() {
            return this.hasStart;
        }

        public GwsCorrectionData mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setStart(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setLength(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setOldText(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        setNewText(paramCodedInputStreamMicro.readString());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasStart()) {
                paramCodedOutputStreamMicro.writeInt32(1, getStart());
            }
            if (hasLength()) {
                paramCodedOutputStreamMicro.writeInt32(2, getLength());
            }
            if (hasOldText()) {
                paramCodedOutputStreamMicro.writeString(3, getOldText());
            }
            if (hasNewText()) {
                paramCodedOutputStreamMicro.writeString(4, getNewText());
            }
        }
    }

    public static final class IcingCorpusDiagnostics
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean deviceStorageLow_ = false;
        private List<IcingCorpusDiagnostic> diagnostics_ = Collections.emptyList();
        private boolean hasDeviceStorageLow;

        public IcingCorpusDiagnostics addDiagnostics(IcingCorpusDiagnostic paramIcingCorpusDiagnostic) {
            if (paramIcingCorpusDiagnostic == null) {
                throw new NullPointerException();
            }
            if (this.diagnostics_.isEmpty()) {
                this.diagnostics_ = new ArrayList();
            }
            this.diagnostics_.add(paramIcingCorpusDiagnostic);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public boolean getDeviceStorageLow() {
            return this.deviceStorageLow_;
        }

        public IcingCorpusDiagnostics setDeviceStorageLow(boolean paramBoolean) {
            this.hasDeviceStorageLow = true;
            this.deviceStorageLow_ = paramBoolean;
            return this;
        }

        public List<IcingCorpusDiagnostic> getDiagnosticsList() {
            return this.diagnostics_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getDiagnosticsList().iterator();
            while (localIterator.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(1, (IcingCorpusDiagnostic) localIterator.next());
            }
            if (hasDeviceStorageLow()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeBoolSize(2, getDeviceStorageLow());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasDeviceStorageLow() {
            return this.hasDeviceStorageLow;
        }

        public IcingCorpusDiagnostics mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        IcingCorpusDiagnostic localIcingCorpusDiagnostic = new IcingCorpusDiagnostic();
                        paramCodedInputStreamMicro.readMessage(localIcingCorpusDiagnostic);
                        addDiagnostics(localIcingCorpusDiagnostic);
                        break;
                    case 16:
                        setDeviceStorageLow(paramCodedInputStreamMicro.readBool());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getDiagnosticsList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(1, (IcingCorpusDiagnostic) localIterator.next());
            }
            if (hasDeviceStorageLow()) {
                paramCodedOutputStreamMicro.writeBool(2, getDeviceStorageLow());
            }
        }

        public static final class IcingCorpusDiagnostic
                extends com.embryo.protobuf.micro.MessageMicro {
            private int cachedSize = -1;
            private int corpus_ = 0;
            private int diagnostic_ = 0;
            private boolean hasCorpus;
            private boolean hasDiagnostic;

            public int getCachedSize() {
                if (this.cachedSize < 0) {
                    getSerializedSize();
                }
                return this.cachedSize;
            }

            public int getCorpus() {
                return this.corpus_;
            }

            public IcingCorpusDiagnostic setCorpus(int paramInt) {
                this.hasCorpus = true;
                this.corpus_ = paramInt;
                return this;
            }

            public int getDiagnostic() {
                return this.diagnostic_;
            }

            public IcingCorpusDiagnostic setDiagnostic(int paramInt) {
                this.hasDiagnostic = true;
                this.diagnostic_ = paramInt;
                return this;
            }

            public int getSerializedSize() {
                boolean bool = hasCorpus();
                int i = 0;
                if (bool) {
                    i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getCorpus());
                }
                if (hasDiagnostic()) {
                    i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getDiagnostic());
                }
                this.cachedSize = i;
                return i;
            }

            public boolean hasCorpus() {
                return this.hasCorpus;
            }

            public boolean hasDiagnostic() {
                return this.hasDiagnostic;
            }

            public IcingCorpusDiagnostic mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                            setCorpus(paramCodedInputStreamMicro.readInt32());
                            break;
                        case 16:
                            setDiagnostic(paramCodedInputStreamMicro.readInt32());
                            break;
                    }
                }
            }

            public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasCorpus()) {
                    paramCodedOutputStreamMicro.writeInt32(1, getCorpus());
                }
                if (hasDiagnostic()) {
                    paramCodedOutputStreamMicro.writeInt32(2, getDiagnostic());
                }
            }
        }
    }

    public static final class LatencyBreakdownEvent
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int event_ = 0;
        private boolean hasEvent;
        private boolean hasOffsetMsec;
        private boolean hasSublatency;
        private int offsetMsec_ = 0;
        private int sublatency_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getEvent() {
            return this.event_;
        }

        public LatencyBreakdownEvent setEvent(int paramInt) {
            this.hasEvent = true;
            this.event_ = paramInt;
            return this;
        }

        public int getOffsetMsec() {
            return this.offsetMsec_;
        }

        public LatencyBreakdownEvent setOffsetMsec(int paramInt) {
            this.hasOffsetMsec = true;
            this.offsetMsec_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasEvent();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getEvent());
            }
            if (hasOffsetMsec()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getOffsetMsec());
            }
            if (hasSublatency()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getSublatency());
            }
            this.cachedSize = i;
            return i;
        }

        public int getSublatency() {
            return this.sublatency_;
        }

        public LatencyBreakdownEvent setSublatency(int paramInt) {
            this.hasSublatency = true;
            this.sublatency_ = paramInt;
            return this;
        }

        public boolean hasEvent() {
            return this.hasEvent;
        }

        public boolean hasOffsetMsec() {
            return this.hasOffsetMsec;
        }

        public boolean hasSublatency() {
            return this.hasSublatency;
        }

        public LatencyBreakdownEvent mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setEvent(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setOffsetMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setSublatency(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasEvent()) {
                paramCodedOutputStreamMicro.writeInt32(1, getEvent());
            }
            if (hasOffsetMsec()) {
                paramCodedOutputStreamMicro.writeInt32(2, getOffsetMsec());
            }
            if (hasSublatency()) {
                paramCodedOutputStreamMicro.writeInt32(3, getSublatency());
            }
        }
    }

    public static final class LatencyData
            extends com.embryo.protobuf.micro.MessageMicro {
        private List<VoicesearchClientLogProto.LatencyBreakdownEvent> breakdown_ = Collections.emptyList();
        private int cachedSize = -1;
        private int durationMsec_ = 0;
        private int factor_ = 0;
        private boolean hasDurationMsec;
        private boolean hasFactor;
        private boolean hasTimeout;
        private boolean timeout_ = false;

        public LatencyData addBreakdown(VoicesearchClientLogProto.LatencyBreakdownEvent paramLatencyBreakdownEvent) {
            if (paramLatencyBreakdownEvent == null) {
                throw new NullPointerException();
            }
            if (this.breakdown_.isEmpty()) {
                this.breakdown_ = new ArrayList();
            }
            this.breakdown_.add(paramLatencyBreakdownEvent);
            return this;
        }

        public VoicesearchClientLogProto.LatencyBreakdownEvent getBreakdown(int paramInt) {
            return this.breakdown_.get(paramInt);
        }

        public int getBreakdownCount() {
            return this.breakdown_.size();
        }

        public List<VoicesearchClientLogProto.LatencyBreakdownEvent> getBreakdownList() {
            return this.breakdown_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getDurationMsec() {
            return this.durationMsec_;
        }

        public LatencyData setDurationMsec(int paramInt) {
            this.hasDurationMsec = true;
            this.durationMsec_ = paramInt;
            return this;
        }

        public int getFactor() {
            return this.factor_;
        }

        public LatencyData setFactor(int paramInt) {
            this.hasFactor = true;
            this.factor_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasDurationMsec();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getDurationMsec());
            }
            if (hasTimeout()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeBoolSize(2, getTimeout());
            }
            if (hasFactor()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getFactor());
            }
            Iterator localIterator = getBreakdownList().iterator();
            while (localIterator.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(4, (VoicesearchClientLogProto.LatencyBreakdownEvent) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean getTimeout() {
            return this.timeout_;
        }

        public LatencyData setTimeout(boolean paramBoolean) {
            this.hasTimeout = true;
            this.timeout_ = paramBoolean;
            return this;
        }

        public boolean hasDurationMsec() {
            return this.hasDurationMsec;
        }

        public boolean hasFactor() {
            return this.hasFactor;
        }

        public boolean hasTimeout() {
            return this.hasTimeout;
        }

        public LatencyData mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setDurationMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setTimeout(paramCodedInputStreamMicro.readBool());
                        break;
                    case 24:
                        setFactor(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 34:
                        VoicesearchClientLogProto.LatencyBreakdownEvent localLatencyBreakdownEvent = new VoicesearchClientLogProto.LatencyBreakdownEvent();
                        paramCodedInputStreamMicro.readMessage(localLatencyBreakdownEvent);
                        addBreakdown(localLatencyBreakdownEvent);
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasDurationMsec()) {
                paramCodedOutputStreamMicro.writeInt32(1, getDurationMsec());
            }
            if (hasTimeout()) {
                paramCodedOutputStreamMicro.writeBool(2, getTimeout());
            }
            if (hasFactor()) {
                paramCodedOutputStreamMicro.writeInt32(3, getFactor());
            }
            Iterator localIterator = getBreakdownList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(4, (VoicesearchClientLogProto.LatencyBreakdownEvent) localIterator.next());
            }
        }
    }

    public static final class OnDeviceSource
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int canonicalSource_ = 1;
        private boolean hasCanonicalSource;
        private boolean hasPackageName;
        private boolean hasSourceType;
        private String packageName_ = "";
        private int sourceType_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getCanonicalSource() {
            return this.canonicalSource_;
        }

        public OnDeviceSource setCanonicalSource(int paramInt) {
            this.hasCanonicalSource = true;
            this.canonicalSource_ = paramInt;
            return this;
        }

        public String getPackageName() {
            return this.packageName_;
        }

        public OnDeviceSource setPackageName(String paramString) {
            this.hasPackageName = true;
            this.packageName_ = paramString;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasPackageName();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(1, getPackageName());
            }
            if (hasCanonicalSource()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getCanonicalSource());
            }
            if (hasSourceType()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getSourceType());
            }
            this.cachedSize = i;
            return i;
        }

        public int getSourceType() {
            return this.sourceType_;
        }

        public OnDeviceSource setSourceType(int paramInt) {
            this.hasSourceType = true;
            this.sourceType_ = paramInt;
            return this;
        }

        public boolean hasCanonicalSource() {
            return this.hasCanonicalSource;
        }

        public boolean hasPackageName() {
            return this.hasPackageName;
        }

        public boolean hasSourceType() {
            return this.hasSourceType;
        }

        public OnDeviceSource mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setPackageName(paramCodedInputStreamMicro.readString());
                        break;
                    case 16:
                        setCanonicalSource(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setSourceType(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasPackageName()) {
                paramCodedOutputStreamMicro.writeString(1, getPackageName());
            }
            if (hasCanonicalSource()) {
                paramCodedOutputStreamMicro.writeInt32(2, getCanonicalSource());
            }
            if (hasSourceType()) {
                paramCodedOutputStreamMicro.writeInt32(3, getSourceType());
            }
        }
    }

    public static final class ScreenTransitionData
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private int from_ = 0;
        private boolean hasFrom;
        private boolean hasTo;
        private int to_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getFrom() {
            return this.from_;
        }

        public ScreenTransitionData setFrom(int paramInt) {
            this.hasFrom = true;
            this.from_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasFrom();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getFrom());
            }
            if (hasTo()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getTo());
            }
            this.cachedSize = i;
            return i;
        }

        public int getTo() {
            return this.to_;
        }

        public ScreenTransitionData setTo(int paramInt) {
            this.hasTo = true;
            this.to_ = paramInt;
            return this;
        }

        public boolean hasFrom() {
            return this.hasFrom;
        }

        public boolean hasTo() {
            return this.hasTo;
        }

        public ScreenTransitionData mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setFrom(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setTo(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasFrom()) {
                paramCodedOutputStreamMicro.writeInt32(1, getFrom());
            }
            if (hasTo()) {
                paramCodedOutputStreamMicro.writeInt32(2, getTo());
            }
        }
    }

    public static final class TypingCorrection
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasRecognizerSegmentIndex;
        private int recognizerSegmentIndex_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getRecognizerSegmentIndex() {
            return this.recognizerSegmentIndex_;
        }

        public TypingCorrection setRecognizerSegmentIndex(int paramInt) {
            this.hasRecognizerSegmentIndex = true;
            this.recognizerSegmentIndex_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasRecognizerSegmentIndex();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getRecognizerSegmentIndex());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasRecognizerSegmentIndex() {
            return this.hasRecognizerSegmentIndex;
        }

        public TypingCorrection mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setRecognizerSegmentIndex(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasRecognizerSegmentIndex()) {
                paramCodedOutputStreamMicro.writeInt32(1, getRecognizerSegmentIndex());
            }
        }
    }

    public static final class VoiceSearchClientLog
            extends com.embryo.protobuf.micro.MessageMicro {
        private String applicationId_ = "";
        private String applicationVersionName_ = "";
        private String applicationVersion_ = "";
        private List<VoicesearchClientLogProto.ClientEvent> bundledClientEvents_ = Collections.emptyList();
        private int cachedSize = -1;
        private String deviceModel_ = "";
        private List<String> experimentId_ = Collections.emptyList();
        private List<Integer> gsaConfigExperimentId_ = Collections.emptyList();
        private boolean hasApplicationId;
        private boolean hasApplicationVersion;
        private boolean hasApplicationVersionName;
        private boolean hasDeviceModel;
        private boolean hasImeLangCount;
        private boolean hasInstallId;
        private boolean hasLocale;
        private boolean hasPackageId;
        private boolean hasPairedBluetooth;
        private boolean hasPlatformId;
        private boolean hasPlatformVersion;
        private boolean hasRequestTimeMsec;
        private boolean hasTriggerApplicationId;
        private boolean hasVoicesearchLangCount;
        private int imeLangCount_ = 0;
        private String installId_ = "";
        private String locale_ = "";
        private String packageId_ = "";
        private boolean pairedBluetooth_ = false;
        private String platformId_ = "";
        private String platformVersion_ = "";
        private long requestTimeMsec_ = 0L;
        private String triggerApplicationId_ = "";
        private int voicesearchLangCount_ = 0;

        public static VoiceSearchClientLog parseFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            return new VoiceSearchClientLog().mergeFrom(paramCodedInputStreamMicro);
        }

        public VoiceSearchClientLog addBundledClientEvents(VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramClientEvent == null) {
                throw new NullPointerException();
            }
            if (this.bundledClientEvents_.isEmpty()) {
                this.bundledClientEvents_ = new ArrayList();
            }
            this.bundledClientEvents_.add(paramClientEvent);
            return this;
        }

        public VoiceSearchClientLog addExperimentId(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.experimentId_.isEmpty()) {
                this.experimentId_ = new ArrayList();
            }
            this.experimentId_.add(paramString);
            return this;
        }

        public VoiceSearchClientLog addGsaConfigExperimentId(int paramInt) {
            if (this.gsaConfigExperimentId_.isEmpty()) {
                this.gsaConfigExperimentId_ = new ArrayList();
            }
            this.gsaConfigExperimentId_.add(Integer.valueOf(paramInt));
            return this;
        }

        public String getApplicationId() {
            return this.applicationId_;
        }

        public VoiceSearchClientLog setApplicationId(String paramString) {
            this.hasApplicationId = true;
            this.applicationId_ = paramString;
            return this;
        }

        public String getApplicationVersion() {
            return this.applicationVersion_;
        }

        public VoiceSearchClientLog setApplicationVersion(String paramString) {
            this.hasApplicationVersion = true;
            this.applicationVersion_ = paramString;
            return this;
        }

        public String getApplicationVersionName() {
            return this.applicationVersionName_;
        }

        public VoiceSearchClientLog setApplicationVersionName(String paramString) {
            this.hasApplicationVersionName = true;
            this.applicationVersionName_ = paramString;
            return this;
        }

        public VoicesearchClientLogProto.ClientEvent getBundledClientEvents(int paramInt) {
            return this.bundledClientEvents_.get(paramInt);
        }

        public int getBundledClientEventsCount() {
            return this.bundledClientEvents_.size();
        }

        public List<VoicesearchClientLogProto.ClientEvent> getBundledClientEventsList() {
            return this.bundledClientEvents_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getDeviceModel() {
            return this.deviceModel_;
        }

        public VoiceSearchClientLog setDeviceModel(String paramString) {
            this.hasDeviceModel = true;
            this.deviceModel_ = paramString;
            return this;
        }

        public List<String> getExperimentIdList() {
            return this.experimentId_;
        }

        public List<Integer> getGsaConfigExperimentIdList() {
            return this.gsaConfigExperimentId_;
        }

        public int getImeLangCount() {
            return this.imeLangCount_;
        }

        public VoiceSearchClientLog setImeLangCount(int paramInt) {
            this.hasImeLangCount = true;
            this.imeLangCount_ = paramInt;
            return this;
        }

        public String getInstallId() {
            return this.installId_;
        }

        public VoiceSearchClientLog setInstallId(String paramString) {
            this.hasInstallId = true;
            this.installId_ = paramString;
            return this;
        }

        public String getLocale() {
            return this.locale_;
        }

        public VoiceSearchClientLog setLocale(String paramString) {
            this.hasLocale = true;
            this.locale_ = paramString;
            return this;
        }

        public String getPackageId() {
            return this.packageId_;
        }

        public VoiceSearchClientLog setPackageId(String paramString) {
            this.hasPackageId = true;
            this.packageId_ = paramString;
            return this;
        }

        public boolean getPairedBluetooth() {
            return this.pairedBluetooth_;
        }

        public VoiceSearchClientLog setPairedBluetooth(boolean paramBoolean) {
            this.hasPairedBluetooth = true;
            this.pairedBluetooth_ = paramBoolean;
            return this;
        }

        public String getPlatformId() {
            return this.platformId_;
        }

        public VoiceSearchClientLog setPlatformId(String paramString) {
            this.hasPlatformId = true;
            this.platformId_ = paramString;
            return this;
        }

        public String getPlatformVersion() {
            return this.platformVersion_;
        }

        public VoiceSearchClientLog setPlatformVersion(String paramString) {
            this.hasPlatformVersion = true;
            this.platformVersion_ = paramString;
            return this;
        }

        public long getRequestTimeMsec() {
            return this.requestTimeMsec_;
        }

        public VoiceSearchClientLog setRequestTimeMsec(long paramLong) {
            this.hasRequestTimeMsec = true;
            this.requestTimeMsec_ = paramLong;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasRequestTimeMsec();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeUInt64Size(1, getRequestTimeMsec());
            }
            if (hasInstallId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(2, getInstallId());
            }
            if (hasPlatformId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(3, getPlatformId());
            }
            if (hasPlatformVersion()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(4, getPlatformVersion());
            }
            if (hasDeviceModel()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(5, getDeviceModel());
            }
            if (hasApplicationId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(6, getApplicationId());
            }
            if (hasTriggerApplicationId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(7, getTriggerApplicationId());
            }
            if (hasApplicationVersion()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(8, getApplicationVersion());
            }
            if (hasLocale()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(9, getLocale());
            }
            Iterator localIterator1 = getBundledClientEventsList().iterator();
            while (localIterator1.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(10, (VoicesearchClientLogProto.ClientEvent) localIterator1.next());
            }
            if (hasPackageId()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(11, getPackageId());
            }
            if (hasImeLangCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(12, getImeLangCount());
            }
            if (hasVoicesearchLangCount()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(13, getVoicesearchLangCount());
            }
            if (hasPairedBluetooth()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeBoolSize(14, getPairedBluetooth());
            }
            int j = 0;
            Iterator localIterator2 = getExperimentIdList().iterator();
            while (localIterator2.hasNext()) {
                j += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator2.next());
            }
            int k = i + j + 1 * getExperimentIdList().size();
            if (hasApplicationVersionName()) {
                k += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(16, getApplicationVersionName());
            }
            int m = 0;
            Iterator localIterator3 = getGsaConfigExperimentIdList().iterator();
            while (localIterator3.hasNext()) {
                m += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator3.next()).intValue());
            }
            int n = k + m + 2 * getGsaConfigExperimentIdList().size();
            this.cachedSize = n;
            return n;
        }

        public String getTriggerApplicationId() {
            return this.triggerApplicationId_;
        }

        public VoiceSearchClientLog setTriggerApplicationId(String paramString) {
            this.hasTriggerApplicationId = true;
            this.triggerApplicationId_ = paramString;
            return this;
        }

        public int getVoicesearchLangCount() {
            return this.voicesearchLangCount_;
        }

        public VoiceSearchClientLog setVoicesearchLangCount(int paramInt) {
            this.hasVoicesearchLangCount = true;
            this.voicesearchLangCount_ = paramInt;
            return this;
        }

        public boolean hasApplicationId() {
            return this.hasApplicationId;
        }

        public boolean hasApplicationVersion() {
            return this.hasApplicationVersion;
        }

        public boolean hasApplicationVersionName() {
            return this.hasApplicationVersionName;
        }

        public boolean hasDeviceModel() {
            return this.hasDeviceModel;
        }

        public boolean hasImeLangCount() {
            return this.hasImeLangCount;
        }

        public boolean hasInstallId() {
            return this.hasInstallId;
        }

        public boolean hasLocale() {
            return this.hasLocale;
        }

        public boolean hasPackageId() {
            return this.hasPackageId;
        }

        public boolean hasPairedBluetooth() {
            return this.hasPairedBluetooth;
        }

        public boolean hasPlatformId() {
            return this.hasPlatformId;
        }

        public boolean hasPlatformVersion() {
            return this.hasPlatformVersion;
        }

        public boolean hasRequestTimeMsec() {
            return this.hasRequestTimeMsec;
        }

        public boolean hasTriggerApplicationId() {
            return this.hasTriggerApplicationId;
        }

        public boolean hasVoicesearchLangCount() {
            return this.hasVoicesearchLangCount;
        }

        public VoiceSearchClientLog mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setRequestTimeMsec(paramCodedInputStreamMicro.readUInt64());
                        break;
                    case 18:
                        setInstallId(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        setPlatformId(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        setPlatformVersion(paramCodedInputStreamMicro.readString());
                        break;
                    case 42:
                        setDeviceModel(paramCodedInputStreamMicro.readString());
                        break;
                    case 50:
                        setApplicationId(paramCodedInputStreamMicro.readString());
                        break;
                    case 58:
                        setTriggerApplicationId(paramCodedInputStreamMicro.readString());
                        break;
                    case 66:
                        setApplicationVersion(paramCodedInputStreamMicro.readString());
                        break;
                    case 74:
                        setLocale(paramCodedInputStreamMicro.readString());
                        break;
                    case 82:
                        VoicesearchClientLogProto.ClientEvent localClientEvent = new VoicesearchClientLogProto.ClientEvent();
                        paramCodedInputStreamMicro.readMessage(localClientEvent);
                        addBundledClientEvents(localClientEvent);
                        break;
                    case 90:
                        setPackageId(paramCodedInputStreamMicro.readString());
                        break;
                    case 96:
                        setImeLangCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 104:
                        setVoicesearchLangCount(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 112:
                        setPairedBluetooth(paramCodedInputStreamMicro.readBool());
                        break;
                    case 122:
                        addExperimentId(paramCodedInputStreamMicro.readString());
                        break;
                    case 130:
                        setApplicationVersionName(paramCodedInputStreamMicro.readString());
                        break;
                    case 136:
                        addGsaConfigExperimentId(paramCodedInputStreamMicro.readInt32());
                        break;
                }
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasRequestTimeMsec()) {
                paramCodedOutputStreamMicro.writeUInt64(1, getRequestTimeMsec());
            }
            if (hasInstallId()) {
                paramCodedOutputStreamMicro.writeString(2, getInstallId());
            }
            if (hasPlatformId()) {
                paramCodedOutputStreamMicro.writeString(3, getPlatformId());
            }
            if (hasPlatformVersion()) {
                paramCodedOutputStreamMicro.writeString(4, getPlatformVersion());
            }
            if (hasDeviceModel()) {
                paramCodedOutputStreamMicro.writeString(5, getDeviceModel());
            }
            if (hasApplicationId()) {
                paramCodedOutputStreamMicro.writeString(6, getApplicationId());
            }
            if (hasTriggerApplicationId()) {
                paramCodedOutputStreamMicro.writeString(7, getTriggerApplicationId());
            }
            if (hasApplicationVersion()) {
                paramCodedOutputStreamMicro.writeString(8, getApplicationVersion());
            }
            if (hasLocale()) {
                paramCodedOutputStreamMicro.writeString(9, getLocale());
            }
            Iterator localIterator1 = getBundledClientEventsList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(10, (VoicesearchClientLogProto.ClientEvent) localIterator1.next());
            }
            if (hasPackageId()) {
                paramCodedOutputStreamMicro.writeString(11, getPackageId());
            }
            if (hasImeLangCount()) {
                paramCodedOutputStreamMicro.writeInt32(12, getImeLangCount());
            }
            if (hasVoicesearchLangCount()) {
                paramCodedOutputStreamMicro.writeInt32(13, getVoicesearchLangCount());
            }
            if (hasPairedBluetooth()) {
                paramCodedOutputStreamMicro.writeBool(14, getPairedBluetooth());
            }
            Iterator localIterator2 = getExperimentIdList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeString(15, (String) localIterator2.next());
            }
            if (hasApplicationVersionName()) {
                paramCodedOutputStreamMicro.writeString(16, getApplicationVersionName());
            }
            Iterator localIterator3 = getGsaConfigExperimentIdList().iterator();
            while (localIterator3.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(17, ((Integer) localIterator3.next()).intValue());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     VoicesearchClientLogProto

 * JD-Core Version:    0.7.0.1

 */