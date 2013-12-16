package com.google.wireless.voicesearch.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class GstaticConfiguration {
    public static final class ActionFeatureFlags
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean enableCalendarEventAttendees_ = false;
        private boolean enableCapabilityHomeControl_ = false;
        private boolean hasEnableCalendarEventAttendees;
        private boolean hasEnableCapabilityHomeControl;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public boolean getEnableCalendarEventAttendees() {
            return this.enableCalendarEventAttendees_;
        }

        public boolean getEnableCapabilityHomeControl() {
            return this.enableCapabilityHomeControl_;
        }

        public int getSerializedSize() {
            boolean bool = hasEnableCalendarEventAttendees();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getEnableCalendarEventAttendees());
            }
            if (hasEnableCapabilityHomeControl()) {
                i += CodedOutputStreamMicro.computeBoolSize(2, getEnableCapabilityHomeControl());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasEnableCalendarEventAttendees() {
            return this.hasEnableCalendarEventAttendees;
        }

        public boolean hasEnableCapabilityHomeControl() {
            return this.hasEnableCapabilityHomeControl;
        }

        public ActionFeatureFlags mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setEnableCalendarEventAttendees(paramCodedInputStreamMicro.readBool());
                        break;
                }
                setEnableCapabilityHomeControl(paramCodedInputStreamMicro.readBool());
            }
        }

        public ActionFeatureFlags setEnableCalendarEventAttendees(boolean paramBoolean) {
            this.hasEnableCalendarEventAttendees = true;
            this.enableCalendarEventAttendees_ = paramBoolean;
            return this;
        }

        public ActionFeatureFlags setEnableCapabilityHomeControl(boolean paramBoolean) {
            this.hasEnableCapabilityHomeControl = true;
            this.enableCapabilityHomeControl_ = paramBoolean;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasEnableCalendarEventAttendees()) {
                paramCodedOutputStreamMicro.writeBool(1, getEnableCalendarEventAttendees());
            }
            if (hasEnableCapabilityHomeControl()) {
                paramCodedOutputStreamMicro.writeBool(2, getEnableCapabilityHomeControl());
            }
        }
    }

    public static final class Authentication
            extends MessageMicro {
        private int authTokenInvalidateBeforeUsePeriodMsec_ = 0;
        private int authTokenProactivelyInvalidatePeriodMsec_ = 0;
        private int cachedSize = -1;
        private boolean hasAuthTokenInvalidateBeforeUsePeriodMsec;
        private boolean hasAuthTokenProactivelyInvalidatePeriodMsec;

        public int getAuthTokenInvalidateBeforeUsePeriodMsec() {
            return this.authTokenInvalidateBeforeUsePeriodMsec_;
        }

        public int getAuthTokenProactivelyInvalidatePeriodMsec() {
            return this.authTokenProactivelyInvalidatePeriodMsec_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getSerializedSize() {
            boolean bool = hasAuthTokenInvalidateBeforeUsePeriodMsec();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getAuthTokenInvalidateBeforeUsePeriodMsec());
            }
            if (hasAuthTokenProactivelyInvalidatePeriodMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getAuthTokenProactivelyInvalidatePeriodMsec());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasAuthTokenInvalidateBeforeUsePeriodMsec() {
            return this.hasAuthTokenInvalidateBeforeUsePeriodMsec;
        }

        public boolean hasAuthTokenProactivelyInvalidatePeriodMsec() {
            return this.hasAuthTokenProactivelyInvalidatePeriodMsec;
        }

        public Authentication mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setAuthTokenInvalidateBeforeUsePeriodMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setAuthTokenProactivelyInvalidatePeriodMsec(paramCodedInputStreamMicro.readInt32());
            }
        }

        public Authentication setAuthTokenInvalidateBeforeUsePeriodMsec(int paramInt) {
            this.hasAuthTokenInvalidateBeforeUsePeriodMsec = true;
            this.authTokenInvalidateBeforeUsePeriodMsec_ = paramInt;
            return this;
        }

        public Authentication setAuthTokenProactivelyInvalidatePeriodMsec(int paramInt) {
            this.hasAuthTokenProactivelyInvalidatePeriodMsec = true;
            this.authTokenProactivelyInvalidatePeriodMsec_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasAuthTokenInvalidateBeforeUsePeriodMsec()) {
                paramCodedOutputStreamMicro.writeInt32(1, getAuthTokenInvalidateBeforeUsePeriodMsec());
            }
            if (hasAuthTokenProactivelyInvalidatePeriodMsec()) {
                paramCodedOutputStreamMicro.writeInt32(2, getAuthTokenProactivelyInvalidatePeriodMsec());
            }
        }
    }

    public static final class Bluetooth
            extends MessageMicro {
        private int cachedSize = -1;
        private int connectionTimeoutMs_ = 0;
        private boolean hasConnectionTimeoutMs;
        private boolean hasScoConnectionTimeoutMs;
        private int scoConnectionTimeoutMs_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getConnectionTimeoutMs() {
            return this.connectionTimeoutMs_;
        }

        public int getScoConnectionTimeoutMs() {
            return this.scoConnectionTimeoutMs_;
        }

        public int getSerializedSize() {
            boolean bool = hasConnectionTimeoutMs();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getConnectionTimeoutMs());
            }
            if (hasScoConnectionTimeoutMs()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getScoConnectionTimeoutMs());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasConnectionTimeoutMs() {
            return this.hasConnectionTimeoutMs;
        }

        public boolean hasScoConnectionTimeoutMs() {
            return this.hasScoConnectionTimeoutMs;
        }

        public Bluetooth mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setConnectionTimeoutMs(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setScoConnectionTimeoutMs(paramCodedInputStreamMicro.readInt32());
            }
        }

        public Bluetooth setConnectionTimeoutMs(int paramInt) {
            this.hasConnectionTimeoutMs = true;
            this.connectionTimeoutMs_ = paramInt;
            return this;
        }

        public Bluetooth setScoConnectionTimeoutMs(int paramInt) {
            this.hasScoConnectionTimeoutMs = true;
            this.scoConnectionTimeoutMs_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasConnectionTimeoutMs()) {
                paramCodedOutputStreamMicro.writeInt32(1, getConnectionTimeoutMs());
            }
            if (hasScoConnectionTimeoutMs()) {
                paramCodedOutputStreamMicro.writeInt32(2, getScoConnectionTimeoutMs());
            }
        }
    }

    public static final class Configuration
            extends MessageMicro {
        private GstaticConfiguration.ActionFeatureFlags actionFeatureFlags_ = null;
        private GstaticConfiguration.Authentication auth_ = null;
        private GstaticConfiguration.Bluetooth bluetooth_ = null;
        private int cachedSize = -1;
        private GstaticConfiguration.Debug debug_ = null;
        private GstaticConfiguration.Dictation dictation_ = null;
        private List<GstaticConfiguration.LanguagePack> embeddedRecognitionResources_ = Collections.emptyList();
        private GstaticConfiguration.EmbeddedRecognizer embeddedRecognizer_ = null;
        private GstaticConfiguration.EndpointerParams endpointerParams_ = null;
        private boolean hasActionFeatureFlags;
        private boolean hasAuth;
        private boolean hasBluetooth;
        private boolean hasDebug;
        private boolean hasDictation;
        private boolean hasEmbeddedRecognizer;
        private boolean hasEndpointerParams;
        private boolean hasHelp;
        private boolean hasId;
        private boolean hasIntentApi;
        private boolean hasNetworkRecognizer;
        private boolean hasPairHttpServerInfo;
        private boolean hasPersonalization;
        private boolean hasPlatform;
        private boolean hasServiceApi;
        private boolean hasSingleHttpServerInfo;
        private boolean hasSoundSearch;
        private boolean hasTcpServerInfo;
        private boolean hasVoiceSearch;
        private GstaticConfiguration.Help help_ = null;
        private String id_ = "";
        private GstaticConfiguration.IntentApi intentApi_ = null;
        private List<GstaticConfiguration.Language> languages_ = Collections.emptyList();
        private List<GstaticConfiguration.LocalizedResources> localizedResources_ = Collections.emptyList();
        private GstaticConfiguration.NetworkRecognizer networkRecognizer_ = null;
        private GstaticConfiguration.PairHttpServerInfo pairHttpServerInfo_ = null;
        private GstaticConfiguration.Personalization personalization_ = null;
        private GstaticConfiguration.Platform platform_ = null;
        private GstaticConfiguration.ServiceApi serviceApi_ = null;
        private GstaticConfiguration.HttpServerInfo singleHttpServerInfo_ = null;
        private GstaticConfiguration.SoundSearch soundSearch_ = null;
        private GstaticConfiguration.ServerInfo tcpServerInfo_ = null;
        private GstaticConfiguration.VoiceSearch voiceSearch_ = null;

        public static Configuration parseFrom(byte[] paramArrayOfByte)
                throws InvalidProtocolBufferMicroException {
            return (Configuration) new Configuration().mergeFrom(paramArrayOfByte);
        }

        public Configuration addEmbeddedRecognitionResources(GstaticConfiguration.LanguagePack paramLanguagePack) {
            if (paramLanguagePack == null) {
                throw new NullPointerException();
            }
            if (this.embeddedRecognitionResources_.isEmpty()) {
                this.embeddedRecognitionResources_ = new ArrayList();
            }
            this.embeddedRecognitionResources_.add(paramLanguagePack);
            return this;
        }

        public Configuration addLanguages(GstaticConfiguration.Language paramLanguage) {
            if (paramLanguage == null) {
                throw new NullPointerException();
            }
            if (this.languages_.isEmpty()) {
                this.languages_ = new ArrayList();
            }
            this.languages_.add(paramLanguage);
            return this;
        }

        public Configuration addLocalizedResources(GstaticConfiguration.LocalizedResources paramLocalizedResources) {
            if (paramLocalizedResources == null) {
                throw new NullPointerException();
            }
            if (this.localizedResources_.isEmpty()) {
                this.localizedResources_ = new ArrayList();
            }
            this.localizedResources_.add(paramLocalizedResources);
            return this;
        }

        public GstaticConfiguration.ActionFeatureFlags getActionFeatureFlags() {
            return this.actionFeatureFlags_;
        }

        public GstaticConfiguration.Authentication getAuth() {
            return this.auth_;
        }

        public GstaticConfiguration.Bluetooth getBluetooth() {
            return this.bluetooth_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public GstaticConfiguration.Debug getDebug() {
            return this.debug_;
        }

        public GstaticConfiguration.Dictation getDictation() {
            return this.dictation_;
        }

        public List<GstaticConfiguration.LanguagePack> getEmbeddedRecognitionResourcesList() {
            return this.embeddedRecognitionResources_;
        }

        public GstaticConfiguration.EmbeddedRecognizer getEmbeddedRecognizer() {
            return this.embeddedRecognizer_;
        }

        public GstaticConfiguration.EndpointerParams getEndpointerParams() {
            return this.endpointerParams_;
        }

        public GstaticConfiguration.Help getHelp() {
            return this.help_;
        }

        public String getId() {
            return this.id_;
        }

        public GstaticConfiguration.IntentApi getIntentApi() {
            return this.intentApi_;
        }

        public GstaticConfiguration.Language getLanguages(int paramInt) {
            return (GstaticConfiguration.Language) this.languages_.get(paramInt);
        }

        public int getLanguagesCount() {
            return this.languages_.size();
        }

        public List<GstaticConfiguration.Language> getLanguagesList() {
            return this.languages_;
        }

        public List<GstaticConfiguration.LocalizedResources> getLocalizedResourcesList() {
            return this.localizedResources_;
        }

        public GstaticConfiguration.NetworkRecognizer getNetworkRecognizer() {
            return this.networkRecognizer_;
        }

        public GstaticConfiguration.PairHttpServerInfo getPairHttpServerInfo() {
            return this.pairHttpServerInfo_;
        }

        public GstaticConfiguration.Personalization getPersonalization() {
            return this.personalization_;
        }

        public GstaticConfiguration.Platform getPlatform() {
            return this.platform_;
        }

        public int getSerializedSize() {
            boolean bool = hasId();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getId());
            }
            Iterator localIterator1 = getLanguagesList().iterator();
            while (localIterator1.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, (GstaticConfiguration.Language) localIterator1.next());
            }
            Iterator localIterator2 = getLocalizedResourcesList().iterator();
            while (localIterator2.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(4, (GstaticConfiguration.LocalizedResources) localIterator2.next());
            }
            if (hasPersonalization()) {
                i += CodedOutputStreamMicro.computeMessageSize(5, getPersonalization());
            }
            Iterator localIterator3 = getEmbeddedRecognitionResourcesList().iterator();
            while (localIterator3.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(6, (GstaticConfiguration.LanguagePack) localIterator3.next());
            }
            if (hasIntentApi()) {
                i += CodedOutputStreamMicro.computeMessageSize(7, getIntentApi());
            }
            if (hasDictation()) {
                i += CodedOutputStreamMicro.computeMessageSize(8, getDictation());
            }
            if (hasTcpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(9, getTcpServerInfo());
            }
            if (hasHelp()) {
                i += CodedOutputStreamMicro.computeMessageSize(10, getHelp());
            }
            if (hasEndpointerParams()) {
                i += CodedOutputStreamMicro.computeMessageSize(11, getEndpointerParams());
            }
            if (hasNetworkRecognizer()) {
                i += CodedOutputStreamMicro.computeMessageSize(12, getNetworkRecognizer());
            }
            if (hasVoiceSearch()) {
                i += CodedOutputStreamMicro.computeMessageSize(13, getVoiceSearch());
            }
            if (hasServiceApi()) {
                i += CodedOutputStreamMicro.computeMessageSize(14, getServiceApi());
            }
            if (hasAuth()) {
                i += CodedOutputStreamMicro.computeMessageSize(15, getAuth());
            }
            if (hasPairHttpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(16, getPairHttpServerInfo());
            }
            if (hasDebug()) {
                i += CodedOutputStreamMicro.computeMessageSize(17, getDebug());
            }
            if (hasBluetooth()) {
                i += CodedOutputStreamMicro.computeMessageSize(18, getBluetooth());
            }
            if (hasPlatform()) {
                i += CodedOutputStreamMicro.computeMessageSize(19, getPlatform());
            }
            if (hasSingleHttpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(20, getSingleHttpServerInfo());
            }
            if (hasEmbeddedRecognizer()) {
                i += CodedOutputStreamMicro.computeMessageSize(21, getEmbeddedRecognizer());
            }
            if (hasActionFeatureFlags()) {
                i += CodedOutputStreamMicro.computeMessageSize(22, getActionFeatureFlags());
            }
            if (hasSoundSearch()) {
                i += CodedOutputStreamMicro.computeMessageSize(23, getSoundSearch());
            }
            this.cachedSize = i;
            return i;
        }

        public GstaticConfiguration.ServiceApi getServiceApi() {
            return this.serviceApi_;
        }

        public GstaticConfiguration.HttpServerInfo getSingleHttpServerInfo() {
            return this.singleHttpServerInfo_;
        }

        public GstaticConfiguration.SoundSearch getSoundSearch() {
            return this.soundSearch_;
        }

        public GstaticConfiguration.ServerInfo getTcpServerInfo() {
            return this.tcpServerInfo_;
        }

        public GstaticConfiguration.VoiceSearch getVoiceSearch() {
            return this.voiceSearch_;
        }

        public boolean hasActionFeatureFlags() {
            return this.hasActionFeatureFlags;
        }

        public boolean hasAuth() {
            return this.hasAuth;
        }

        public boolean hasBluetooth() {
            return this.hasBluetooth;
        }

        public boolean hasDebug() {
            return this.hasDebug;
        }

        public boolean hasDictation() {
            return this.hasDictation;
        }

        public boolean hasEmbeddedRecognizer() {
            return this.hasEmbeddedRecognizer;
        }

        public boolean hasEndpointerParams() {
            return this.hasEndpointerParams;
        }

        public boolean hasHelp() {
            return this.hasHelp;
        }

        public boolean hasId() {
            return this.hasId;
        }

        public boolean hasIntentApi() {
            return this.hasIntentApi;
        }

        public boolean hasNetworkRecognizer() {
            return this.hasNetworkRecognizer;
        }

        public boolean hasPairHttpServerInfo() {
            return this.hasPairHttpServerInfo;
        }

        public boolean hasPersonalization() {
            return this.hasPersonalization;
        }

        public boolean hasPlatform() {
            return this.hasPlatform;
        }

        public boolean hasServiceApi() {
            return this.hasServiceApi;
        }

        public boolean hasSingleHttpServerInfo() {
            return this.hasSingleHttpServerInfo;
        }

        public boolean hasSoundSearch() {
            return this.hasSoundSearch;
        }

        public boolean hasTcpServerInfo() {
            return this.hasTcpServerInfo;
        }

        public boolean hasVoiceSearch() {
            return this.hasVoiceSearch;
        }

        public Configuration mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setId(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        GstaticConfiguration.Language localLanguage = new GstaticConfiguration.Language();
                        paramCodedInputStreamMicro.readMessage(localLanguage);
                        addLanguages(localLanguage);
                        break;
                    case 34:
                        GstaticConfiguration.LocalizedResources localLocalizedResources = new GstaticConfiguration.LocalizedResources();
                        paramCodedInputStreamMicro.readMessage(localLocalizedResources);
                        addLocalizedResources(localLocalizedResources);
                        break;
                    case 42:
                        GstaticConfiguration.Personalization localPersonalization = new GstaticConfiguration.Personalization();
                        paramCodedInputStreamMicro.readMessage(localPersonalization);
                        setPersonalization(localPersonalization);
                        break;
                    case 50:
                        GstaticConfiguration.LanguagePack localLanguagePack = new GstaticConfiguration.LanguagePack();
                        paramCodedInputStreamMicro.readMessage(localLanguagePack);
                        addEmbeddedRecognitionResources(localLanguagePack);
                        break;
                    case 58:
                        GstaticConfiguration.IntentApi localIntentApi = new GstaticConfiguration.IntentApi();
                        paramCodedInputStreamMicro.readMessage(localIntentApi);
                        setIntentApi(localIntentApi);
                        break;
                    case 66:
                        GstaticConfiguration.Dictation localDictation = new GstaticConfiguration.Dictation();
                        paramCodedInputStreamMicro.readMessage(localDictation);
                        setDictation(localDictation);
                        break;
                    case 74:
                        GstaticConfiguration.ServerInfo localServerInfo = new GstaticConfiguration.ServerInfo();
                        paramCodedInputStreamMicro.readMessage(localServerInfo);
                        setTcpServerInfo(localServerInfo);
                        break;
                    case 82:
                        GstaticConfiguration.Help localHelp = new GstaticConfiguration.Help();
                        paramCodedInputStreamMicro.readMessage(localHelp);
                        setHelp(localHelp);
                        break;
                    case 90:
                        GstaticConfiguration.EndpointerParams localEndpointerParams = new GstaticConfiguration.EndpointerParams();
                        paramCodedInputStreamMicro.readMessage(localEndpointerParams);
                        setEndpointerParams(localEndpointerParams);
                        break;
                    case 98:
                        GstaticConfiguration.NetworkRecognizer localNetworkRecognizer = new GstaticConfiguration.NetworkRecognizer();
                        paramCodedInputStreamMicro.readMessage(localNetworkRecognizer);
                        setNetworkRecognizer(localNetworkRecognizer);
                        break;
                    case 106:
                        GstaticConfiguration.VoiceSearch localVoiceSearch = new GstaticConfiguration.VoiceSearch();
                        paramCodedInputStreamMicro.readMessage(localVoiceSearch);
                        setVoiceSearch(localVoiceSearch);
                        break;
                    case 114:
                        GstaticConfiguration.ServiceApi localServiceApi = new GstaticConfiguration.ServiceApi();
                        paramCodedInputStreamMicro.readMessage(localServiceApi);
                        setServiceApi(localServiceApi);
                        break;
                    case 122:
                        GstaticConfiguration.Authentication localAuthentication = new GstaticConfiguration.Authentication();
                        paramCodedInputStreamMicro.readMessage(localAuthentication);
                        setAuth(localAuthentication);
                        break;
                    case 130:
                        GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo = new GstaticConfiguration.PairHttpServerInfo();
                        paramCodedInputStreamMicro.readMessage(localPairHttpServerInfo);
                        setPairHttpServerInfo(localPairHttpServerInfo);
                        break;
                    case 138:
                        GstaticConfiguration.Debug localDebug = new GstaticConfiguration.Debug();
                        paramCodedInputStreamMicro.readMessage(localDebug);
                        setDebug(localDebug);
                        break;
                    case 146:
                        GstaticConfiguration.Bluetooth localBluetooth = new GstaticConfiguration.Bluetooth();
                        paramCodedInputStreamMicro.readMessage(localBluetooth);
                        setBluetooth(localBluetooth);
                        break;
                    case 154:
                        GstaticConfiguration.Platform localPlatform = new GstaticConfiguration.Platform();
                        paramCodedInputStreamMicro.readMessage(localPlatform);
                        setPlatform(localPlatform);
                        break;
                    case 162:
                        GstaticConfiguration.HttpServerInfo localHttpServerInfo = new GstaticConfiguration.HttpServerInfo();
                        paramCodedInputStreamMicro.readMessage(localHttpServerInfo);
                        setSingleHttpServerInfo(localHttpServerInfo);
                        break;
                    case 170:
                        GstaticConfiguration.EmbeddedRecognizer localEmbeddedRecognizer = new GstaticConfiguration.EmbeddedRecognizer();
                        paramCodedInputStreamMicro.readMessage(localEmbeddedRecognizer);
                        setEmbeddedRecognizer(localEmbeddedRecognizer);
                        break;
                    case 178:
                        GstaticConfiguration.ActionFeatureFlags localActionFeatureFlags = new GstaticConfiguration.ActionFeatureFlags();
                        paramCodedInputStreamMicro.readMessage(localActionFeatureFlags);
                        setActionFeatureFlags(localActionFeatureFlags);
                        break;
                }
                GstaticConfiguration.SoundSearch localSoundSearch = new GstaticConfiguration.SoundSearch();
                paramCodedInputStreamMicro.readMessage(localSoundSearch);
                setSoundSearch(localSoundSearch);
            }
        }

        public Configuration setActionFeatureFlags(GstaticConfiguration.ActionFeatureFlags paramActionFeatureFlags) {
            if (paramActionFeatureFlags == null) {
                throw new NullPointerException();
            }
            this.hasActionFeatureFlags = true;
            this.actionFeatureFlags_ = paramActionFeatureFlags;
            return this;
        }

        public Configuration setAuth(GstaticConfiguration.Authentication paramAuthentication) {
            if (paramAuthentication == null) {
                throw new NullPointerException();
            }
            this.hasAuth = true;
            this.auth_ = paramAuthentication;
            return this;
        }

        public Configuration setBluetooth(GstaticConfiguration.Bluetooth paramBluetooth) {
            if (paramBluetooth == null) {
                throw new NullPointerException();
            }
            this.hasBluetooth = true;
            this.bluetooth_ = paramBluetooth;
            return this;
        }

        public Configuration setDebug(GstaticConfiguration.Debug paramDebug) {
            if (paramDebug == null) {
                throw new NullPointerException();
            }
            this.hasDebug = true;
            this.debug_ = paramDebug;
            return this;
        }

        public Configuration setDictation(GstaticConfiguration.Dictation paramDictation) {
            if (paramDictation == null) {
                throw new NullPointerException();
            }
            this.hasDictation = true;
            this.dictation_ = paramDictation;
            return this;
        }

        public Configuration setEmbeddedRecognizer(GstaticConfiguration.EmbeddedRecognizer paramEmbeddedRecognizer) {
            if (paramEmbeddedRecognizer == null) {
                throw new NullPointerException();
            }
            this.hasEmbeddedRecognizer = true;
            this.embeddedRecognizer_ = paramEmbeddedRecognizer;
            return this;
        }

        public Configuration setEndpointerParams(GstaticConfiguration.EndpointerParams paramEndpointerParams) {
            if (paramEndpointerParams == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerParams = true;
            this.endpointerParams_ = paramEndpointerParams;
            return this;
        }

        public Configuration setHelp(GstaticConfiguration.Help paramHelp) {
            if (paramHelp == null) {
                throw new NullPointerException();
            }
            this.hasHelp = true;
            this.help_ = paramHelp;
            return this;
        }

        public Configuration setId(String paramString) {
            this.hasId = true;
            this.id_ = paramString;
            return this;
        }

        public Configuration setIntentApi(GstaticConfiguration.IntentApi paramIntentApi) {
            if (paramIntentApi == null) {
                throw new NullPointerException();
            }
            this.hasIntentApi = true;
            this.intentApi_ = paramIntentApi;
            return this;
        }

        public Configuration setNetworkRecognizer(GstaticConfiguration.NetworkRecognizer paramNetworkRecognizer) {
            if (paramNetworkRecognizer == null) {
                throw new NullPointerException();
            }
            this.hasNetworkRecognizer = true;
            this.networkRecognizer_ = paramNetworkRecognizer;
            return this;
        }

        public Configuration setPairHttpServerInfo(GstaticConfiguration.PairHttpServerInfo paramPairHttpServerInfo) {
            if (paramPairHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasPairHttpServerInfo = true;
            this.pairHttpServerInfo_ = paramPairHttpServerInfo;
            return this;
        }

        public Configuration setPersonalization(GstaticConfiguration.Personalization paramPersonalization) {
            if (paramPersonalization == null) {
                throw new NullPointerException();
            }
            this.hasPersonalization = true;
            this.personalization_ = paramPersonalization;
            return this;
        }

        public Configuration setPlatform(GstaticConfiguration.Platform paramPlatform) {
            if (paramPlatform == null) {
                throw new NullPointerException();
            }
            this.hasPlatform = true;
            this.platform_ = paramPlatform;
            return this;
        }

        public Configuration setServiceApi(GstaticConfiguration.ServiceApi paramServiceApi) {
            if (paramServiceApi == null) {
                throw new NullPointerException();
            }
            this.hasServiceApi = true;
            this.serviceApi_ = paramServiceApi;
            return this;
        }

        public Configuration setSingleHttpServerInfo(GstaticConfiguration.HttpServerInfo paramHttpServerInfo) {
            if (paramHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasSingleHttpServerInfo = true;
            this.singleHttpServerInfo_ = paramHttpServerInfo;
            return this;
        }

        public Configuration setSoundSearch(GstaticConfiguration.SoundSearch paramSoundSearch) {
            if (paramSoundSearch == null) {
                throw new NullPointerException();
            }
            this.hasSoundSearch = true;
            this.soundSearch_ = paramSoundSearch;
            return this;
        }

        public Configuration setTcpServerInfo(GstaticConfiguration.ServerInfo paramServerInfo) {
            if (paramServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasTcpServerInfo = true;
            this.tcpServerInfo_ = paramServerInfo;
            return this;
        }

        public Configuration setVoiceSearch(GstaticConfiguration.VoiceSearch paramVoiceSearch) {
            if (paramVoiceSearch == null) {
                throw new NullPointerException();
            }
            this.hasVoiceSearch = true;
            this.voiceSearch_ = paramVoiceSearch;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasId()) {
                paramCodedOutputStreamMicro.writeString(1, getId());
            }
            Iterator localIterator1 = getLanguagesList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(2, (GstaticConfiguration.Language) localIterator1.next());
            }
            Iterator localIterator2 = getLocalizedResourcesList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(4, (GstaticConfiguration.LocalizedResources) localIterator2.next());
            }
            if (hasPersonalization()) {
                paramCodedOutputStreamMicro.writeMessage(5, getPersonalization());
            }
            Iterator localIterator3 = getEmbeddedRecognitionResourcesList().iterator();
            while (localIterator3.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(6, (GstaticConfiguration.LanguagePack) localIterator3.next());
            }
            if (hasIntentApi()) {
                paramCodedOutputStreamMicro.writeMessage(7, getIntentApi());
            }
            if (hasDictation()) {
                paramCodedOutputStreamMicro.writeMessage(8, getDictation());
            }
            if (hasTcpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(9, getTcpServerInfo());
            }
            if (hasHelp()) {
                paramCodedOutputStreamMicro.writeMessage(10, getHelp());
            }
            if (hasEndpointerParams()) {
                paramCodedOutputStreamMicro.writeMessage(11, getEndpointerParams());
            }
            if (hasNetworkRecognizer()) {
                paramCodedOutputStreamMicro.writeMessage(12, getNetworkRecognizer());
            }
            if (hasVoiceSearch()) {
                paramCodedOutputStreamMicro.writeMessage(13, getVoiceSearch());
            }
            if (hasServiceApi()) {
                paramCodedOutputStreamMicro.writeMessage(14, getServiceApi());
            }
            if (hasAuth()) {
                paramCodedOutputStreamMicro.writeMessage(15, getAuth());
            }
            if (hasPairHttpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(16, getPairHttpServerInfo());
            }
            if (hasDebug()) {
                paramCodedOutputStreamMicro.writeMessage(17, getDebug());
            }
            if (hasBluetooth()) {
                paramCodedOutputStreamMicro.writeMessage(18, getBluetooth());
            }
            if (hasPlatform()) {
                paramCodedOutputStreamMicro.writeMessage(19, getPlatform());
            }
            if (hasSingleHttpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(20, getSingleHttpServerInfo());
            }
            if (hasEmbeddedRecognizer()) {
                paramCodedOutputStreamMicro.writeMessage(21, getEmbeddedRecognizer());
            }
            if (hasActionFeatureFlags()) {
                paramCodedOutputStreamMicro.writeMessage(22, getActionFeatureFlags());
            }
            if (hasSoundSearch()) {
                paramCodedOutputStreamMicro.writeMessage(23, getSoundSearch());
            }
        }
    }

    public static final class Debug
            extends MessageMicro {
        private int cachedSize = -1;
        private List<GstaticConfiguration.DebugServer> debugServer_ = Collections.emptyList();

        public Debug addDebugServer(GstaticConfiguration.DebugServer paramDebugServer) {
            if (paramDebugServer == null) {
                throw new NullPointerException();
            }
            if (this.debugServer_.isEmpty()) {
                this.debugServer_ = new ArrayList();
            }
            this.debugServer_.add(paramDebugServer);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<GstaticConfiguration.DebugServer> getDebugServerList() {
            return this.debugServer_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getDebugServerList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(8, (GstaticConfiguration.DebugServer) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public Debug mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                GstaticConfiguration.DebugServer localDebugServer = new GstaticConfiguration.DebugServer();
                paramCodedInputStreamMicro.readMessage(localDebugServer);
                addDebugServer(localDebugServer);
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getDebugServerList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(8, (GstaticConfiguration.DebugServer) localIterator.next());
            }
        }
    }

    public static final class DebugServer
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasLabel;
        private boolean hasPairHttpServerInfo;
        private boolean hasSingleHttpServerInfo;
        private boolean hasTcpServerInfo;
        private String label_ = "";
        private GstaticConfiguration.PairHttpServerInfo pairHttpServerInfo_ = null;
        private GstaticConfiguration.HttpServerInfo singleHttpServerInfo_ = null;
        private GstaticConfiguration.ServerInfo tcpServerInfo_ = null;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getLabel() {
            return this.label_;
        }

        public GstaticConfiguration.PairHttpServerInfo getPairHttpServerInfo() {
            return this.pairHttpServerInfo_;
        }

        public int getSerializedSize() {
            boolean bool = hasLabel();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLabel());
            }
            if (hasTcpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, getTcpServerInfo());
            }
            if (hasSingleHttpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(3, getSingleHttpServerInfo());
            }
            if (hasPairHttpServerInfo()) {
                i += CodedOutputStreamMicro.computeMessageSize(4, getPairHttpServerInfo());
            }
            this.cachedSize = i;
            return i;
        }

        public GstaticConfiguration.HttpServerInfo getSingleHttpServerInfo() {
            return this.singleHttpServerInfo_;
        }

        public GstaticConfiguration.ServerInfo getTcpServerInfo() {
            return this.tcpServerInfo_;
        }

        public boolean hasLabel() {
            return this.hasLabel;
        }

        public boolean hasPairHttpServerInfo() {
            return this.hasPairHttpServerInfo;
        }

        public boolean hasSingleHttpServerInfo() {
            return this.hasSingleHttpServerInfo;
        }

        public boolean hasTcpServerInfo() {
            return this.hasTcpServerInfo;
        }

        public DebugServer mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setLabel(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        GstaticConfiguration.ServerInfo localServerInfo = new GstaticConfiguration.ServerInfo();
                        paramCodedInputStreamMicro.readMessage(localServerInfo);
                        setTcpServerInfo(localServerInfo);
                        break;
                    case 26:
                        GstaticConfiguration.HttpServerInfo localHttpServerInfo = new GstaticConfiguration.HttpServerInfo();
                        paramCodedInputStreamMicro.readMessage(localHttpServerInfo);
                        setSingleHttpServerInfo(localHttpServerInfo);
                        break;
                }
                GstaticConfiguration.PairHttpServerInfo localPairHttpServerInfo = new GstaticConfiguration.PairHttpServerInfo();
                paramCodedInputStreamMicro.readMessage(localPairHttpServerInfo);
                setPairHttpServerInfo(localPairHttpServerInfo);
            }
        }

        public DebugServer setLabel(String paramString) {
            this.hasLabel = true;
            this.label_ = paramString;
            return this;
        }

        public DebugServer setPairHttpServerInfo(GstaticConfiguration.PairHttpServerInfo paramPairHttpServerInfo) {
            if (paramPairHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasPairHttpServerInfo = true;
            this.pairHttpServerInfo_ = paramPairHttpServerInfo;
            return this;
        }

        public DebugServer setSingleHttpServerInfo(GstaticConfiguration.HttpServerInfo paramHttpServerInfo) {
            if (paramHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasSingleHttpServerInfo = true;
            this.singleHttpServerInfo_ = paramHttpServerInfo;
            return this;
        }

        public DebugServer setTcpServerInfo(GstaticConfiguration.ServerInfo paramServerInfo) {
            if (paramServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasTcpServerInfo = true;
            this.tcpServerInfo_ = paramServerInfo;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasLabel()) {
                paramCodedOutputStreamMicro.writeString(1, getLabel());
            }
            if (hasTcpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(2, getTcpServerInfo());
            }
            if (hasSingleHttpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(3, getSingleHttpServerInfo());
            }
            if (hasPairHttpServerInfo()) {
                paramCodedOutputStreamMicro.writeMessage(4, getPairHttpServerInfo());
            }
        }
    }

    public static final class Dialect
            extends MessageMicro {
        private String bcp47Locale_ = "";
        private int cachedSize = -1;
        private String displayName_ = "";
        private boolean hasBcp47Locale;
        private boolean hasDisplayName;
        private boolean hasImeSupported;
        private boolean hasMainJavaLocale;
        private List<Integer> helpActions_ = Collections.emptyList();
        private boolean imeSupported_ = false;
        private List<String> javaLocales_ = Collections.emptyList();
        private String mainJavaLocale_ = "";

        public Dialect addHelpActions(int paramInt) {
            if (this.helpActions_.isEmpty()) {
                this.helpActions_ = new ArrayList();
            }
            this.helpActions_.add(Integer.valueOf(paramInt));
            return this;
        }

        public Dialect addJavaLocales(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.javaLocales_.isEmpty()) {
                this.javaLocales_ = new ArrayList();
            }
            this.javaLocales_.add(paramString);
            return this;
        }

        public String getBcp47Locale() {
            return this.bcp47Locale_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getDisplayName() {
            return this.displayName_;
        }

        public List<Integer> getHelpActionsList() {
            return this.helpActions_;
        }

        public boolean getImeSupported() {
            return this.imeSupported_;
        }

        public List<String> getJavaLocalesList() {
            return this.javaLocales_;
        }

        public String getMainJavaLocale() {
            return this.mainJavaLocale_;
        }

        public int getSerializedSize() {
            boolean bool = hasDisplayName();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDisplayName());
            }
            if (hasBcp47Locale()) {
                i += CodedOutputStreamMicro.computeStringSize(2, getBcp47Locale());
            }
            int j = 0;
            Iterator localIterator1 = getJavaLocalesList().iterator();
            while (localIterator1.hasNext()) {
                j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator1.next());
            }
            int k = i + j + 1 * getJavaLocalesList().size();
            int m = 0;
            Iterator localIterator2 = getHelpActionsList().iterator();
            while (localIterator2.hasNext()) {
                m += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator2.next()).intValue());
            }
            int n = k + m + 1 * getHelpActionsList().size();
            if (hasMainJavaLocale()) {
                n += CodedOutputStreamMicro.computeStringSize(6, getMainJavaLocale());
            }
            if (hasImeSupported()) {
                n += CodedOutputStreamMicro.computeBoolSize(7, getImeSupported());
            }
            this.cachedSize = n;
            return n;
        }

        public boolean hasBcp47Locale() {
            return this.hasBcp47Locale;
        }

        public boolean hasDisplayName() {
            return this.hasDisplayName;
        }

        public boolean hasImeSupported() {
            return this.hasImeSupported;
        }

        public boolean hasMainJavaLocale() {
            return this.hasMainJavaLocale;
        }

        public Dialect mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setDisplayName(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        setBcp47Locale(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        addJavaLocales(paramCodedInputStreamMicro.readString());
                        break;
                    case 40:
                        addHelpActions(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 50:
                        setMainJavaLocale(paramCodedInputStreamMicro.readString());
                        break;
                }
                setImeSupported(paramCodedInputStreamMicro.readBool());
            }
        }

        public Dialect setBcp47Locale(String paramString) {
            this.hasBcp47Locale = true;
            this.bcp47Locale_ = paramString;
            return this;
        }

        public Dialect setDisplayName(String paramString) {
            this.hasDisplayName = true;
            this.displayName_ = paramString;
            return this;
        }

        public Dialect setImeSupported(boolean paramBoolean) {
            this.hasImeSupported = true;
            this.imeSupported_ = paramBoolean;
            return this;
        }

        public Dialect setMainJavaLocale(String paramString) {
            this.hasMainJavaLocale = true;
            this.mainJavaLocale_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasDisplayName()) {
                paramCodedOutputStreamMicro.writeString(1, getDisplayName());
            }
            if (hasBcp47Locale()) {
                paramCodedOutputStreamMicro.writeString(2, getBcp47Locale());
            }
            Iterator localIterator1 = getJavaLocalesList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeString(4, (String) localIterator1.next());
            }
            Iterator localIterator2 = getHelpActionsList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(5, ((Integer) localIterator2.next()).intValue());
            }
            if (hasMainJavaLocale()) {
                paramCodedOutputStreamMicro.writeString(6, getMainJavaLocale());
            }
            if (hasImeSupported()) {
                paramCodedOutputStreamMicro.writeBool(7, getImeSupported());
            }
        }
    }

    public static final class Dictation
            extends MessageMicro {
        private int cachedSize = -1;
        private int delayBetweenCommittingNewTextMsec_ = 200;
        private int embeddedRecognizerFallbackTimeout_ = 0;
        private GstaticConfiguration.EndpointerParams endpointerParams_ = null;
        private boolean hasDelayBetweenCommittingNewTextMsec;
        private boolean hasEmbeddedRecognizerFallbackTimeout;
        private boolean hasEndpointerParams;
        private boolean hasLogEditDistanceMaxContiguousChars;
        private boolean hasLogEditDistanceMaxNewCharsPerc;
        private boolean hasMaxSpanLength;
        private boolean hasMaxTotalSpanLength;
        private boolean hasPartialResultMinConfidence;
        private int logEditDistanceMaxContiguousChars_ = 15;
        private int logEditDistanceMaxNewCharsPerc_ = 50;
        private int maxSpanLength_ = 10;
        private int maxTotalSpanLength_ = 250;
        private float partialResultMinConfidence_ = 0.9F;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getDelayBetweenCommittingNewTextMsec() {
            return this.delayBetweenCommittingNewTextMsec_;
        }

        public int getEmbeddedRecognizerFallbackTimeout() {
            return this.embeddedRecognizerFallbackTimeout_;
        }

        public GstaticConfiguration.EndpointerParams getEndpointerParams() {
            return this.endpointerParams_;
        }

        public int getLogEditDistanceMaxContiguousChars() {
            return this.logEditDistanceMaxContiguousChars_;
        }

        public int getLogEditDistanceMaxNewCharsPerc() {
            return this.logEditDistanceMaxNewCharsPerc_;
        }

        public int getMaxSpanLength() {
            return this.maxSpanLength_;
        }

        public int getMaxTotalSpanLength() {
            return this.maxTotalSpanLength_;
        }

        public float getPartialResultMinConfidence() {
            return this.partialResultMinConfidence_;
        }

        public int getSerializedSize() {
            boolean bool = hasMaxSpanLength();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getMaxSpanLength());
            }
            if (hasMaxTotalSpanLength()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getMaxTotalSpanLength());
            }
            if (hasDelayBetweenCommittingNewTextMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(3, getDelayBetweenCommittingNewTextMsec());
            }
            if (hasPartialResultMinConfidence()) {
                i += CodedOutputStreamMicro.computeFloatSize(4, getPartialResultMinConfidence());
            }
            if (hasLogEditDistanceMaxContiguousChars()) {
                i += CodedOutputStreamMicro.computeInt32Size(5, getLogEditDistanceMaxContiguousChars());
            }
            if (hasLogEditDistanceMaxNewCharsPerc()) {
                i += CodedOutputStreamMicro.computeInt32Size(6, getLogEditDistanceMaxNewCharsPerc());
            }
            if (hasEndpointerParams()) {
                i += CodedOutputStreamMicro.computeMessageSize(7, getEndpointerParams());
            }
            if (hasEmbeddedRecognizerFallbackTimeout()) {
                i += CodedOutputStreamMicro.computeInt32Size(8, getEmbeddedRecognizerFallbackTimeout());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasDelayBetweenCommittingNewTextMsec() {
            return this.hasDelayBetweenCommittingNewTextMsec;
        }

        public boolean hasEmbeddedRecognizerFallbackTimeout() {
            return this.hasEmbeddedRecognizerFallbackTimeout;
        }

        public boolean hasEndpointerParams() {
            return this.hasEndpointerParams;
        }

        public boolean hasLogEditDistanceMaxContiguousChars() {
            return this.hasLogEditDistanceMaxContiguousChars;
        }

        public boolean hasLogEditDistanceMaxNewCharsPerc() {
            return this.hasLogEditDistanceMaxNewCharsPerc;
        }

        public boolean hasMaxSpanLength() {
            return this.hasMaxSpanLength;
        }

        public boolean hasMaxTotalSpanLength() {
            return this.hasMaxTotalSpanLength;
        }

        public boolean hasPartialResultMinConfidence() {
            return this.hasPartialResultMinConfidence;
        }

        public Dictation mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setMaxSpanLength(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setMaxTotalSpanLength(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setDelayBetweenCommittingNewTextMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 37:
                        setPartialResultMinConfidence(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 40:
                        setLogEditDistanceMaxContiguousChars(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 48:
                        setLogEditDistanceMaxNewCharsPerc(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 58:
                        GstaticConfiguration.EndpointerParams localEndpointerParams = new GstaticConfiguration.EndpointerParams();
                        paramCodedInputStreamMicro.readMessage(localEndpointerParams);
                        setEndpointerParams(localEndpointerParams);
                        break;
                }
                setEmbeddedRecognizerFallbackTimeout(paramCodedInputStreamMicro.readInt32());
            }
        }

        public Dictation setDelayBetweenCommittingNewTextMsec(int paramInt) {
            this.hasDelayBetweenCommittingNewTextMsec = true;
            this.delayBetweenCommittingNewTextMsec_ = paramInt;
            return this;
        }

        public Dictation setEmbeddedRecognizerFallbackTimeout(int paramInt) {
            this.hasEmbeddedRecognizerFallbackTimeout = true;
            this.embeddedRecognizerFallbackTimeout_ = paramInt;
            return this;
        }

        public Dictation setEndpointerParams(GstaticConfiguration.EndpointerParams paramEndpointerParams) {
            if (paramEndpointerParams == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerParams = true;
            this.endpointerParams_ = paramEndpointerParams;
            return this;
        }

        public Dictation setLogEditDistanceMaxContiguousChars(int paramInt) {
            this.hasLogEditDistanceMaxContiguousChars = true;
            this.logEditDistanceMaxContiguousChars_ = paramInt;
            return this;
        }

        public Dictation setLogEditDistanceMaxNewCharsPerc(int paramInt) {
            this.hasLogEditDistanceMaxNewCharsPerc = true;
            this.logEditDistanceMaxNewCharsPerc_ = paramInt;
            return this;
        }

        public Dictation setMaxSpanLength(int paramInt) {
            this.hasMaxSpanLength = true;
            this.maxSpanLength_ = paramInt;
            return this;
        }

        public Dictation setMaxTotalSpanLength(int paramInt) {
            this.hasMaxTotalSpanLength = true;
            this.maxTotalSpanLength_ = paramInt;
            return this;
        }

        public Dictation setPartialResultMinConfidence(float paramFloat) {
            this.hasPartialResultMinConfidence = true;
            this.partialResultMinConfidence_ = paramFloat;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasMaxSpanLength()) {
                paramCodedOutputStreamMicro.writeInt32(1, getMaxSpanLength());
            }
            if (hasMaxTotalSpanLength()) {
                paramCodedOutputStreamMicro.writeInt32(2, getMaxTotalSpanLength());
            }
            if (hasDelayBetweenCommittingNewTextMsec()) {
                paramCodedOutputStreamMicro.writeInt32(3, getDelayBetweenCommittingNewTextMsec());
            }
            if (hasPartialResultMinConfidence()) {
                paramCodedOutputStreamMicro.writeFloat(4, getPartialResultMinConfidence());
            }
            if (hasLogEditDistanceMaxContiguousChars()) {
                paramCodedOutputStreamMicro.writeInt32(5, getLogEditDistanceMaxContiguousChars());
            }
            if (hasLogEditDistanceMaxNewCharsPerc()) {
                paramCodedOutputStreamMicro.writeInt32(6, getLogEditDistanceMaxNewCharsPerc());
            }
            if (hasEndpointerParams()) {
                paramCodedOutputStreamMicro.writeMessage(7, getEndpointerParams());
            }
            if (hasEmbeddedRecognizerFallbackTimeout()) {
                paramCodedOutputStreamMicro.writeInt32(8, getEmbeddedRecognizerFallbackTimeout());
            }
        }
    }

    public static final class EmbeddedRecognizer
            extends MessageMicro {
        private int cachedSize = -1;
        private int grammarCompilationFrequencyMs_ = 0;
        private boolean hasGrammarCompilationFrequencyMs;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getGrammarCompilationFrequencyMs() {
            return this.grammarCompilationFrequencyMs_;
        }

        public int getSerializedSize() {
            boolean bool = hasGrammarCompilationFrequencyMs();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getGrammarCompilationFrequencyMs());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasGrammarCompilationFrequencyMs() {
            return this.hasGrammarCompilationFrequencyMs;
        }

        public EmbeddedRecognizer mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                setGrammarCompilationFrequencyMs(paramCodedInputStreamMicro.readInt32());
            }
        }

        public EmbeddedRecognizer setGrammarCompilationFrequencyMs(int paramInt) {
            this.hasGrammarCompilationFrequencyMs = true;
            this.grammarCompilationFrequencyMs_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasGrammarCompilationFrequencyMs()) {
                paramCodedOutputStreamMicro.writeInt32(1, getGrammarCompilationFrequencyMs());
            }
        }
    }

    public static final class EndpointerParams
            extends MessageMicro {
        private int cachedSize = -1;
        private int completeSilenceMsec_ = 0;
        private int extraSilenceAfterEndOfSpeechMsec_ = 0;
        private boolean hasCompleteSilenceMsec;
        private boolean hasExtraSilenceAfterEndOfSpeechMsec;
        private boolean hasNoSpeechDetectedTimeoutMsec;
        private boolean hasPossiblyCompleteSilenceMsec;
        private boolean hasSpeechMinimumLengthMsec;
        private int noSpeechDetectedTimeoutMsec_ = 0;
        private int possiblyCompleteSilenceMsec_ = 0;
        private int speechMinimumLengthMsec_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getCompleteSilenceMsec() {
            return this.completeSilenceMsec_;
        }

        public int getExtraSilenceAfterEndOfSpeechMsec() {
            return this.extraSilenceAfterEndOfSpeechMsec_;
        }

        public int getNoSpeechDetectedTimeoutMsec() {
            return this.noSpeechDetectedTimeoutMsec_;
        }

        public int getPossiblyCompleteSilenceMsec() {
            return this.possiblyCompleteSilenceMsec_;
        }

        public int getSerializedSize() {
            boolean bool = hasCompleteSilenceMsec();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getCompleteSilenceMsec());
            }
            if (hasPossiblyCompleteSilenceMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getPossiblyCompleteSilenceMsec());
            }
            if (hasSpeechMinimumLengthMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(3, getSpeechMinimumLengthMsec());
            }
            if (hasNoSpeechDetectedTimeoutMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(4, getNoSpeechDetectedTimeoutMsec());
            }
            if (hasExtraSilenceAfterEndOfSpeechMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(5, getExtraSilenceAfterEndOfSpeechMsec());
            }
            this.cachedSize = i;
            return i;
        }

        public int getSpeechMinimumLengthMsec() {
            return this.speechMinimumLengthMsec_;
        }

        public boolean hasCompleteSilenceMsec() {
            return this.hasCompleteSilenceMsec;
        }

        public boolean hasExtraSilenceAfterEndOfSpeechMsec() {
            return this.hasExtraSilenceAfterEndOfSpeechMsec;
        }

        public boolean hasNoSpeechDetectedTimeoutMsec() {
            return this.hasNoSpeechDetectedTimeoutMsec;
        }

        public boolean hasPossiblyCompleteSilenceMsec() {
            return this.hasPossiblyCompleteSilenceMsec;
        }

        public boolean hasSpeechMinimumLengthMsec() {
            return this.hasSpeechMinimumLengthMsec;
        }

        public EndpointerParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setCompleteSilenceMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setPossiblyCompleteSilenceMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 24:
                        setSpeechMinimumLengthMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 32:
                        setNoSpeechDetectedTimeoutMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setExtraSilenceAfterEndOfSpeechMsec(paramCodedInputStreamMicro.readInt32());
            }
        }

        public EndpointerParams setCompleteSilenceMsec(int paramInt) {
            this.hasCompleteSilenceMsec = true;
            this.completeSilenceMsec_ = paramInt;
            return this;
        }

        public EndpointerParams setExtraSilenceAfterEndOfSpeechMsec(int paramInt) {
            this.hasExtraSilenceAfterEndOfSpeechMsec = true;
            this.extraSilenceAfterEndOfSpeechMsec_ = paramInt;
            return this;
        }

        public EndpointerParams setNoSpeechDetectedTimeoutMsec(int paramInt) {
            this.hasNoSpeechDetectedTimeoutMsec = true;
            this.noSpeechDetectedTimeoutMsec_ = paramInt;
            return this;
        }

        public EndpointerParams setPossiblyCompleteSilenceMsec(int paramInt) {
            this.hasPossiblyCompleteSilenceMsec = true;
            this.possiblyCompleteSilenceMsec_ = paramInt;
            return this;
        }

        public EndpointerParams setSpeechMinimumLengthMsec(int paramInt) {
            this.hasSpeechMinimumLengthMsec = true;
            this.speechMinimumLengthMsec_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasCompleteSilenceMsec()) {
                paramCodedOutputStreamMicro.writeInt32(1, getCompleteSilenceMsec());
            }
            if (hasPossiblyCompleteSilenceMsec()) {
                paramCodedOutputStreamMicro.writeInt32(2, getPossiblyCompleteSilenceMsec());
            }
            if (hasSpeechMinimumLengthMsec()) {
                paramCodedOutputStreamMicro.writeInt32(3, getSpeechMinimumLengthMsec());
            }
            if (hasNoSpeechDetectedTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(4, getNoSpeechDetectedTimeoutMsec());
            }
            if (hasExtraSilenceAfterEndOfSpeechMsec()) {
                paramCodedOutputStreamMicro.writeInt32(5, getExtraSilenceAfterEndOfSpeechMsec());
            }
        }
    }

    public static final class Help
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasPrivacyUrl;
        private String privacyUrl_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getPrivacyUrl() {
            return this.privacyUrl_;
        }

        public int getSerializedSize() {
            boolean bool = hasPrivacyUrl();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getPrivacyUrl());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasPrivacyUrl() {
            return this.hasPrivacyUrl;
        }

        public Help mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                setPrivacyUrl(paramCodedInputStreamMicro.readString());
            }
        }

        public Help setPrivacyUrl(String paramString) {
            this.hasPrivacyUrl = true;
            this.privacyUrl_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasPrivacyUrl()) {
                paramCodedOutputStreamMicro.writeString(1, getPrivacyUrl());
            }
        }
    }

    public static final class HttpServerInfo
            extends MessageMicro {
        private int cachedSize = -1;
        private int chunkSize_ = 0;
        private int connectionTimeoutMsec_ = 0;
        private boolean disableCompression_ = false;
        private boolean hasChunkSize;
        private boolean hasConnectionTimeoutMsec;
        private boolean hasDisableCompression;
        private boolean hasHeader;
        private boolean hasReadTimeoutMsec;
        private boolean hasReplaceHostWithTld;
        private boolean hasUrl;
        private String header_ = "";
        private List<String> httpHeaderKey_ = Collections.emptyList();
        private List<String> httpHeaderValue_ = Collections.emptyList();
        private int readTimeoutMsec_ = 0;
        private boolean replaceHostWithTld_ = false;
        private String url_ = "";

        public HttpServerInfo addHttpHeaderKey(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.httpHeaderKey_.isEmpty()) {
                this.httpHeaderKey_ = new ArrayList();
            }
            this.httpHeaderKey_.add(paramString);
            return this;
        }

        public HttpServerInfo addHttpHeaderValue(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.httpHeaderValue_.isEmpty()) {
                this.httpHeaderValue_ = new ArrayList();
            }
            this.httpHeaderValue_.add(paramString);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getChunkSize() {
            return this.chunkSize_;
        }

        public int getConnectionTimeoutMsec() {
            return this.connectionTimeoutMsec_;
        }

        public boolean getDisableCompression() {
            return this.disableCompression_;
        }

        public String getHeader() {
            return this.header_;
        }

        public List<String> getHttpHeaderKeyList() {
            return this.httpHeaderKey_;
        }

        public List<String> getHttpHeaderValueList() {
            return this.httpHeaderValue_;
        }

        public int getReadTimeoutMsec() {
            return this.readTimeoutMsec_;
        }

        public boolean getReplaceHostWithTld() {
            return this.replaceHostWithTld_;
        }

        public int getSerializedSize() {
            boolean bool = hasUrl();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getUrl());
            }
            int j = 0;
            Iterator localIterator1 = getHttpHeaderKeyList().iterator();
            while (localIterator1.hasNext()) {
                j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator1.next());
            }
            int k = i + j + 1 * getHttpHeaderKeyList().size();
            int m = 0;
            Iterator localIterator2 = getHttpHeaderValueList().iterator();
            while (localIterator2.hasNext()) {
                m += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator2.next());
            }
            int n = k + m + 1 * getHttpHeaderValueList().size();
            if (hasHeader()) {
                n += CodedOutputStreamMicro.computeStringSize(4, getHeader());
            }
            if (hasConnectionTimeoutMsec()) {
                n += CodedOutputStreamMicro.computeInt32Size(5, getConnectionTimeoutMsec());
            }
            if (hasReadTimeoutMsec()) {
                n += CodedOutputStreamMicro.computeInt32Size(6, getReadTimeoutMsec());
            }
            if (hasChunkSize()) {
                n += CodedOutputStreamMicro.computeInt32Size(7, getChunkSize());
            }
            if (hasDisableCompression()) {
                n += CodedOutputStreamMicro.computeBoolSize(8, getDisableCompression());
            }
            if (hasReplaceHostWithTld()) {
                n += CodedOutputStreamMicro.computeBoolSize(9, getReplaceHostWithTld());
            }
            this.cachedSize = n;
            return n;
        }

        public String getUrl() {
            return this.url_;
        }

        public boolean hasChunkSize() {
            return this.hasChunkSize;
        }

        public boolean hasConnectionTimeoutMsec() {
            return this.hasConnectionTimeoutMsec;
        }

        public boolean hasDisableCompression() {
            return this.hasDisableCompression;
        }

        public boolean hasHeader() {
            return this.hasHeader;
        }

        public boolean hasReadTimeoutMsec() {
            return this.hasReadTimeoutMsec;
        }

        public boolean hasReplaceHostWithTld() {
            return this.hasReplaceHostWithTld;
        }

        public boolean hasUrl() {
            return this.hasUrl;
        }

        public HttpServerInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        addHttpHeaderKey(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        addHttpHeaderValue(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        setHeader(paramCodedInputStreamMicro.readString());
                        break;
                    case 40:
                        setConnectionTimeoutMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 48:
                        setReadTimeoutMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 56:
                        setChunkSize(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 64:
                        setDisableCompression(paramCodedInputStreamMicro.readBool());
                        break;
                }
                setReplaceHostWithTld(paramCodedInputStreamMicro.readBool());
            }
        }

        public HttpServerInfo setChunkSize(int paramInt) {
            this.hasChunkSize = true;
            this.chunkSize_ = paramInt;
            return this;
        }

        public HttpServerInfo setConnectionTimeoutMsec(int paramInt) {
            this.hasConnectionTimeoutMsec = true;
            this.connectionTimeoutMsec_ = paramInt;
            return this;
        }

        public HttpServerInfo setDisableCompression(boolean paramBoolean) {
            this.hasDisableCompression = true;
            this.disableCompression_ = paramBoolean;
            return this;
        }

        public HttpServerInfo setHeader(String paramString) {
            this.hasHeader = true;
            this.header_ = paramString;
            return this;
        }

        public HttpServerInfo setReadTimeoutMsec(int paramInt) {
            this.hasReadTimeoutMsec = true;
            this.readTimeoutMsec_ = paramInt;
            return this;
        }

        public HttpServerInfo setReplaceHostWithTld(boolean paramBoolean) {
            this.hasReplaceHostWithTld = true;
            this.replaceHostWithTld_ = paramBoolean;
            return this;
        }

        public HttpServerInfo setUrl(String paramString) {
            this.hasUrl = true;
            this.url_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasUrl()) {
                paramCodedOutputStreamMicro.writeString(1, getUrl());
            }
            Iterator localIterator1 = getHttpHeaderKeyList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeString(2, (String) localIterator1.next());
            }
            Iterator localIterator2 = getHttpHeaderValueList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeString(3, (String) localIterator2.next());
            }
            if (hasHeader()) {
                paramCodedOutputStreamMicro.writeString(4, getHeader());
            }
            if (hasConnectionTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(5, getConnectionTimeoutMsec());
            }
            if (hasReadTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(6, getReadTimeoutMsec());
            }
            if (hasChunkSize()) {
                paramCodedOutputStreamMicro.writeInt32(7, getChunkSize());
            }
            if (hasDisableCompression()) {
                paramCodedOutputStreamMicro.writeBool(8, getDisableCompression());
            }
            if (hasReplaceHostWithTld()) {
                paramCodedOutputStreamMicro.writeBool(9, getReplaceHostWithTld());
            }
        }
    }

    public static final class IntentApi
            extends MessageMicro {
        private int cachedSize = -1;
        private GstaticConfiguration.EndpointerParams endpointerParams_ = null;
        private boolean hasEndpointerParams;
        private boolean hasMaxNbest;
        private int maxNbest_ = 5;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public GstaticConfiguration.EndpointerParams getEndpointerParams() {
            return this.endpointerParams_;
        }

        public int getMaxNbest() {
            return this.maxNbest_;
        }

        public int getSerializedSize() {
            boolean bool = hasMaxNbest();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getMaxNbest());
            }
            if (hasEndpointerParams()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, getEndpointerParams());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasEndpointerParams() {
            return this.hasEndpointerParams;
        }

        public boolean hasMaxNbest() {
            return this.hasMaxNbest;
        }

        public IntentApi mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setMaxNbest(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                GstaticConfiguration.EndpointerParams localEndpointerParams = new GstaticConfiguration.EndpointerParams();
                paramCodedInputStreamMicro.readMessage(localEndpointerParams);
                setEndpointerParams(localEndpointerParams);
            }
        }

        public IntentApi setEndpointerParams(GstaticConfiguration.EndpointerParams paramEndpointerParams) {
            if (paramEndpointerParams == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerParams = true;
            this.endpointerParams_ = paramEndpointerParams;
            return this;
        }

        public IntentApi setMaxNbest(int paramInt) {
            this.hasMaxNbest = true;
            this.maxNbest_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasMaxNbest()) {
                paramCodedOutputStreamMicro.writeInt32(1, getMaxNbest());
            }
            if (hasEndpointerParams()) {
                paramCodedOutputStreamMicro.writeMessage(2, getEndpointerParams());
            }
        }
    }

    public static final class Language
            extends MessageMicro {
        private int cachedSize = -1;
        private List<GstaticConfiguration.Dialect> dialect_ = Collections.emptyList();
        private String displayName_ = "";
        private boolean hasDisplayName;

        public Language addDialect(GstaticConfiguration.Dialect paramDialect) {
            if (paramDialect == null) {
                throw new NullPointerException();
            }
            if (this.dialect_.isEmpty()) {
                this.dialect_ = new ArrayList();
            }
            this.dialect_.add(paramDialect);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public GstaticConfiguration.Dialect getDialect(int paramInt) {
            return (GstaticConfiguration.Dialect) this.dialect_.get(paramInt);
        }

        public int getDialectCount() {
            return this.dialect_.size();
        }

        public List<GstaticConfiguration.Dialect> getDialectList() {
            return this.dialect_;
        }

        public String getDisplayName() {
            return this.displayName_;
        }

        public int getSerializedSize() {
            boolean bool = hasDisplayName();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getDisplayName());
            }
            Iterator localIterator = getDialectList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, (GstaticConfiguration.Dialect) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasDisplayName() {
            return this.hasDisplayName;
        }

        public Language mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setDisplayName(paramCodedInputStreamMicro.readString());
                        break;
                }
                GstaticConfiguration.Dialect localDialect = new GstaticConfiguration.Dialect();
                paramCodedInputStreamMicro.readMessage(localDialect);
                addDialect(localDialect);
            }
        }

        public Language setDisplayName(String paramString) {
            this.hasDisplayName = true;
            this.displayName_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasDisplayName()) {
                paramCodedOutputStreamMicro.writeString(1, getDisplayName());
            }
            Iterator localIterator = getDialectList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(2, (GstaticConfiguration.Dialect) localIterator.next());
            }
        }
    }

    public static final class LanguagePack
            extends MessageMicro {
        private String bcp47Locale_ = "";
        private int cachedSize = -1;
        private List<String> compatibleBcp47Locales_ = Collections.emptyList();
        private String downloadUrl_ = "";
        private boolean hasBcp47Locale;
        private boolean hasDownloadUrl;
        private boolean hasHotwordQuality;
        private boolean hasLanguagePackId;
        private boolean hasManifestUrl;
        private boolean hasMinimumDeviceClass;
        private boolean hasSizeKb;
        private boolean hasVersion;
        private int hotwordQuality_ = 0;
        private List<Integer> languagePackFormatVersion_ = Collections.emptyList();
        private String languagePackId_ = "";
        private String manifestUrl_ = "";
        private int minimumDeviceClass_ = 0;
        private int sizeKb_ = 0;
        private int version_ = 0;

        public LanguagePack addCompatibleBcp47Locales(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.compatibleBcp47Locales_.isEmpty()) {
                this.compatibleBcp47Locales_ = new ArrayList();
            }
            this.compatibleBcp47Locales_.add(paramString);
            return this;
        }

        public LanguagePack addLanguagePackFormatVersion(int paramInt) {
            if (this.languagePackFormatVersion_.isEmpty()) {
                this.languagePackFormatVersion_ = new ArrayList();
            }
            this.languagePackFormatVersion_.add(Integer.valueOf(paramInt));
            return this;
        }

        public String getBcp47Locale() {
            return this.bcp47Locale_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<String> getCompatibleBcp47LocalesList() {
            return this.compatibleBcp47Locales_;
        }

        public String getDownloadUrl() {
            return this.downloadUrl_;
        }

        public int getHotwordQuality() {
            return this.hotwordQuality_;
        }

        public int getLanguagePackFormatVersion(int paramInt) {
            return ((Integer) this.languagePackFormatVersion_.get(paramInt)).intValue();
        }

        public int getLanguagePackFormatVersionCount() {
            return this.languagePackFormatVersion_.size();
        }

        public List<Integer> getLanguagePackFormatVersionList() {
            return this.languagePackFormatVersion_;
        }

        public String getLanguagePackId() {
            return this.languagePackId_;
        }

        public String getManifestUrl() {
            return this.manifestUrl_;
        }

        public int getMinimumDeviceClass() {
            return this.minimumDeviceClass_;
        }

        public int getSerializedSize() {
            boolean bool = hasBcp47Locale();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getBcp47Locale());
            }
            int j = 0;
            Iterator localIterator1 = getCompatibleBcp47LocalesList().iterator();
            while (localIterator1.hasNext()) {
                j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator1.next());
            }
            int k = i + j + 1 * getCompatibleBcp47LocalesList().size();
            if (hasDownloadUrl()) {
                k += CodedOutputStreamMicro.computeStringSize(3, getDownloadUrl());
            }
            if (hasVersion()) {
                k += CodedOutputStreamMicro.computeInt32Size(4, getVersion());
            }
            if (hasSizeKb()) {
                k += CodedOutputStreamMicro.computeInt32Size(5, getSizeKb());
            }
            if (hasLanguagePackId()) {
                k += CodedOutputStreamMicro.computeStringSize(6, getLanguagePackId());
            }
            int m = 0;
            Iterator localIterator2 = getLanguagePackFormatVersionList().iterator();
            while (localIterator2.hasNext()) {
                m += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator2.next()).intValue());
            }
            int n = k + m + 1 * getLanguagePackFormatVersionList().size();
            if (hasMinimumDeviceClass()) {
                n += CodedOutputStreamMicro.computeInt32Size(8, getMinimumDeviceClass());
            }
            if (hasManifestUrl()) {
                n += CodedOutputStreamMicro.computeStringSize(9, getManifestUrl());
            }
            if (hasHotwordQuality()) {
                n += CodedOutputStreamMicro.computeInt32Size(10, getHotwordQuality());
            }
            this.cachedSize = n;
            return n;
        }

        public int getSizeKb() {
            return this.sizeKb_;
        }

        public int getVersion() {
            return this.version_;
        }

        public boolean hasBcp47Locale() {
            return this.hasBcp47Locale;
        }

        public boolean hasDownloadUrl() {
            return this.hasDownloadUrl;
        }

        public boolean hasHotwordQuality() {
            return this.hasHotwordQuality;
        }

        public boolean hasLanguagePackId() {
            return this.hasLanguagePackId;
        }

        public boolean hasManifestUrl() {
            return this.hasManifestUrl;
        }

        public boolean hasMinimumDeviceClass() {
            return this.hasMinimumDeviceClass;
        }

        public boolean hasSizeKb() {
            return this.hasSizeKb;
        }

        public boolean hasVersion() {
            return this.hasVersion;
        }

        public LanguagePack mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setBcp47Locale(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        addCompatibleBcp47Locales(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        setDownloadUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 32:
                        setVersion(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 40:
                        setSizeKb(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 50:
                        setLanguagePackId(paramCodedInputStreamMicro.readString());
                        break;
                    case 56:
                        addLanguagePackFormatVersion(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 64:
                        setMinimumDeviceClass(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 74:
                        setManifestUrl(paramCodedInputStreamMicro.readString());
                        break;
                }
                setHotwordQuality(paramCodedInputStreamMicro.readInt32());
            }
        }

        public LanguagePack setBcp47Locale(String paramString) {
            this.hasBcp47Locale = true;
            this.bcp47Locale_ = paramString;
            return this;
        }

        public LanguagePack setDownloadUrl(String paramString) {
            this.hasDownloadUrl = true;
            this.downloadUrl_ = paramString;
            return this;
        }

        public LanguagePack setHotwordQuality(int paramInt) {
            this.hasHotwordQuality = true;
            this.hotwordQuality_ = paramInt;
            return this;
        }

        public LanguagePack setLanguagePackId(String paramString) {
            this.hasLanguagePackId = true;
            this.languagePackId_ = paramString;
            return this;
        }

        public LanguagePack setManifestUrl(String paramString) {
            this.hasManifestUrl = true;
            this.manifestUrl_ = paramString;
            return this;
        }

        public LanguagePack setMinimumDeviceClass(int paramInt) {
            this.hasMinimumDeviceClass = true;
            this.minimumDeviceClass_ = paramInt;
            return this;
        }

        public LanguagePack setSizeKb(int paramInt) {
            this.hasSizeKb = true;
            this.sizeKb_ = paramInt;
            return this;
        }

        public LanguagePack setVersion(int paramInt) {
            this.hasVersion = true;
            this.version_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasBcp47Locale()) {
                paramCodedOutputStreamMicro.writeString(1, getBcp47Locale());
            }
            Iterator localIterator1 = getCompatibleBcp47LocalesList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeString(2, (String) localIterator1.next());
            }
            if (hasDownloadUrl()) {
                paramCodedOutputStreamMicro.writeString(3, getDownloadUrl());
            }
            if (hasVersion()) {
                paramCodedOutputStreamMicro.writeInt32(4, getVersion());
            }
            if (hasSizeKb()) {
                paramCodedOutputStreamMicro.writeInt32(5, getSizeKb());
            }
            if (hasLanguagePackId()) {
                paramCodedOutputStreamMicro.writeString(6, getLanguagePackId());
            }
            Iterator localIterator2 = getLanguagePackFormatVersionList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(7, ((Integer) localIterator2.next()).intValue());
            }
            if (hasMinimumDeviceClass()) {
                paramCodedOutputStreamMicro.writeInt32(8, getMinimumDeviceClass());
            }
            if (hasManifestUrl()) {
                paramCodedOutputStreamMicro.writeString(9, getManifestUrl());
            }
            if (hasHotwordQuality()) {
                paramCodedOutputStreamMicro.writeInt32(10, getHotwordQuality());
            }
        }
    }

    public static final class LocalizedResources
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasJavaLocale;
        private String javaLocale_ = "";
        private List<GstaticConfiguration.Resource> resources_ = Collections.emptyList();

        public LocalizedResources addResources(GstaticConfiguration.Resource paramResource) {
            if (paramResource == null) {
                throw new NullPointerException();
            }
            if (this.resources_.isEmpty()) {
                this.resources_ = new ArrayList();
            }
            this.resources_.add(paramResource);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getJavaLocale() {
            return this.javaLocale_;
        }

        public List<GstaticConfiguration.Resource> getResourcesList() {
            return this.resources_;
        }

        public int getSerializedSize() {
            boolean bool = hasJavaLocale();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getJavaLocale());
            }
            Iterator localIterator = getResourcesList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, (GstaticConfiguration.Resource) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasJavaLocale() {
            return this.hasJavaLocale;
        }

        public LocalizedResources mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setJavaLocale(paramCodedInputStreamMicro.readString());
                        break;
                }
                GstaticConfiguration.Resource localResource = new GstaticConfiguration.Resource();
                paramCodedInputStreamMicro.readMessage(localResource);
                addResources(localResource);
            }
        }

        public LocalizedResources setJavaLocale(String paramString) {
            this.hasJavaLocale = true;
            this.javaLocale_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasJavaLocale()) {
                paramCodedOutputStreamMicro.writeString(1, getJavaLocale());
            }
            Iterator localIterator = getResourcesList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(2, (GstaticConfiguration.Resource) localIterator.next());
            }
        }
    }

    public static final class NetworkRecognizer
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasMaxRetries;
        private boolean hasMaxRetryTimeoutMsec;
        private int maxRetries_ = 0;
        private int maxRetryTimeoutMsec_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getMaxRetries() {
            return this.maxRetries_;
        }

        public int getMaxRetryTimeoutMsec() {
            return this.maxRetryTimeoutMsec_;
        }

        public int getSerializedSize() {
            boolean bool = hasMaxRetries();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getMaxRetries());
            }
            if (hasMaxRetryTimeoutMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getMaxRetryTimeoutMsec());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasMaxRetries() {
            return this.hasMaxRetries;
        }

        public boolean hasMaxRetryTimeoutMsec() {
            return this.hasMaxRetryTimeoutMsec;
        }

        public NetworkRecognizer mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setMaxRetries(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setMaxRetryTimeoutMsec(paramCodedInputStreamMicro.readInt32());
            }
        }

        public NetworkRecognizer setMaxRetries(int paramInt) {
            this.hasMaxRetries = true;
            this.maxRetries_ = paramInt;
            return this;
        }

        public NetworkRecognizer setMaxRetryTimeoutMsec(int paramInt) {
            this.hasMaxRetryTimeoutMsec = true;
            this.maxRetryTimeoutMsec_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasMaxRetries()) {
                paramCodedOutputStreamMicro.writeInt32(1, getMaxRetries());
            }
            if (hasMaxRetryTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(2, getMaxRetryTimeoutMsec());
            }
        }
    }

    public static final class PairHttpServerInfo
            extends MessageMicro {
        private int cachedSize = -1;
        private GstaticConfiguration.HttpServerInfo down_ = null;
        private boolean hasDown;
        private boolean hasUp;
        private GstaticConfiguration.HttpServerInfo up_ = null;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public GstaticConfiguration.HttpServerInfo getDown() {
            return this.down_;
        }

        public int getSerializedSize() {
            boolean bool = hasDown();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getDown());
            }
            if (hasUp()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, getUp());
            }
            this.cachedSize = i;
            return i;
        }

        public GstaticConfiguration.HttpServerInfo getUp() {
            return this.up_;
        }

        public boolean hasDown() {
            return this.hasDown;
        }

        public boolean hasUp() {
            return this.hasUp;
        }

        public PairHttpServerInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        GstaticConfiguration.HttpServerInfo localHttpServerInfo2 = new GstaticConfiguration.HttpServerInfo();
                        paramCodedInputStreamMicro.readMessage(localHttpServerInfo2);
                        setDown(localHttpServerInfo2);
                        break;
                }
                GstaticConfiguration.HttpServerInfo localHttpServerInfo1 = new GstaticConfiguration.HttpServerInfo();
                paramCodedInputStreamMicro.readMessage(localHttpServerInfo1);
                setUp(localHttpServerInfo1);
            }
        }

        public PairHttpServerInfo setDown(GstaticConfiguration.HttpServerInfo paramHttpServerInfo) {
            if (paramHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasDown = true;
            this.down_ = paramHttpServerInfo;
            return this;
        }

        public PairHttpServerInfo setUp(GstaticConfiguration.HttpServerInfo paramHttpServerInfo) {
            if (paramHttpServerInfo == null) {
                throw new NullPointerException();
            }
            this.hasUp = true;
            this.up_ = paramHttpServerInfo;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasDown()) {
                paramCodedOutputStreamMicro.writeMessage(1, getDown());
            }
            if (hasUp()) {
                paramCodedOutputStreamMicro.writeMessage(2, getUp());
            }
        }
    }

    public static final class Personalization
            extends MessageMicro {
        private int cachedSize = -1;
        private String dashboardUrl_ = "";
        private boolean hasDashboardUrl;
        private boolean hasMoreInfoUrl;
        private List<Integer> mccCountryCodes_ = Collections.emptyList();
        private String moreInfoUrl_ = "";

        public Personalization addMccCountryCodes(int paramInt) {
            if (this.mccCountryCodes_.isEmpty()) {
                this.mccCountryCodes_ = new ArrayList();
            }
            this.mccCountryCodes_.add(Integer.valueOf(paramInt));
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getDashboardUrl() {
            return this.dashboardUrl_;
        }

        public List<Integer> getMccCountryCodesList() {
            return this.mccCountryCodes_;
        }

        public String getMoreInfoUrl() {
            return this.moreInfoUrl_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getMccCountryCodesList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator.next()).intValue());
            }
            int j = 0 + i + 1 * getMccCountryCodesList().size();
            if (hasDashboardUrl()) {
                j += CodedOutputStreamMicro.computeStringSize(2, getDashboardUrl());
            }
            if (hasMoreInfoUrl()) {
                j += CodedOutputStreamMicro.computeStringSize(3, getMoreInfoUrl());
            }
            this.cachedSize = j;
            return j;
        }

        public boolean hasDashboardUrl() {
            return this.hasDashboardUrl;
        }

        public boolean hasMoreInfoUrl() {
            return this.hasMoreInfoUrl;
        }

        public Personalization mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        addMccCountryCodes(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 18:
                        setDashboardUrl(paramCodedInputStreamMicro.readString());
                        break;
                }
                setMoreInfoUrl(paramCodedInputStreamMicro.readString());
            }
        }

        public Personalization setDashboardUrl(String paramString) {
            this.hasDashboardUrl = true;
            this.dashboardUrl_ = paramString;
            return this;
        }

        public Personalization setMoreInfoUrl(String paramString) {
            this.hasMoreInfoUrl = true;
            this.moreInfoUrl_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getMccCountryCodesList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(1, ((Integer) localIterator.next()).intValue());
            }
            if (hasDashboardUrl()) {
                paramCodedOutputStreamMicro.writeString(2, getDashboardUrl());
            }
            if (hasMoreInfoUrl()) {
                paramCodedOutputStreamMicro.writeString(3, getMoreInfoUrl());
            }
        }
    }

    public static final class Platform
            extends MessageMicro {
        private int cachedSize = -1;
        private List<String> enabledNoiseSuppressors_ = Collections.emptyList();

        public Platform addEnabledNoiseSuppressors(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.enabledNoiseSuppressors_.isEmpty()) {
                this.enabledNoiseSuppressors_ = new ArrayList();
            }
            this.enabledNoiseSuppressors_.add(paramString);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<String> getEnabledNoiseSuppressorsList() {
            return this.enabledNoiseSuppressors_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getEnabledNoiseSuppressorsList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator.next());
            }
            int j = 0 + i + 1 * getEnabledNoiseSuppressorsList().size();
            this.cachedSize = j;
            return j;
        }

        public Platform mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                addEnabledNoiseSuppressors(paramCodedInputStreamMicro.readString());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getEnabledNoiseSuppressorsList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeString(1, (String) localIterator.next());
            }
        }
    }

    public static final class Resource
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasKey;
        private boolean hasValue;
        private String key_ = "";
        private String value_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getKey() {
            return this.key_;
        }

        public int getSerializedSize() {
            boolean bool = hasKey();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getKey());
            }
            if (hasValue()) {
                i += CodedOutputStreamMicro.computeStringSize(2, getValue());
            }
            this.cachedSize = i;
            return i;
        }

        public String getValue() {
            return this.value_;
        }

        public boolean hasKey() {
            return this.hasKey;
        }

        public boolean hasValue() {
            return this.hasValue;
        }

        public Resource mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setKey(paramCodedInputStreamMicro.readString());
                        break;
                }
                setValue(paramCodedInputStreamMicro.readString());
            }
        }

        public Resource setKey(String paramString) {
            this.hasKey = true;
            this.key_ = paramString;
            return this;
        }

        public Resource setValue(String paramString) {
            this.hasValue = true;
            this.value_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasKey()) {
                paramCodedOutputStreamMicro.writeString(1, getKey());
            }
            if (hasValue()) {
                paramCodedOutputStreamMicro.writeString(2, getValue());
            }
        }
    }

    public static final class ServerInfo
            extends MessageMicro {
        private List<Integer> blacklistMccMnc_ = Collections.emptyList();
        private int cachedSize = -1;
        private int connectionTimeoutMsec_ = 0;
        private boolean hasConnectionTimeoutMsec;
        private boolean hasHeader;
        private boolean hasHost;
        private boolean hasInBufferSize;
        private boolean hasOpenConnectionTimeoutMsec;
        private boolean hasOutBufferSize;
        private boolean hasPort;
        private boolean hasUseSsl;
        private String header_ = "";
        private String host_ = "";
        private int inBufferSize_ = 0;
        private int openConnectionTimeoutMsec_ = 0;
        private int outBufferSize_ = 0;
        private int port_ = 0;
        private boolean useSsl_ = false;

        public ServerInfo addBlacklistMccMnc(int paramInt) {
            if (this.blacklistMccMnc_.isEmpty()) {
                this.blacklistMccMnc_ = new ArrayList();
            }
            this.blacklistMccMnc_.add(Integer.valueOf(paramInt));
            return this;
        }

        public List<Integer> getBlacklistMccMncList() {
            return this.blacklistMccMnc_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getConnectionTimeoutMsec() {
            return this.connectionTimeoutMsec_;
        }

        public String getHeader() {
            return this.header_;
        }

        public String getHost() {
            return this.host_;
        }

        public int getInBufferSize() {
            return this.inBufferSize_;
        }

        public int getOpenConnectionTimeoutMsec() {
            return this.openConnectionTimeoutMsec_;
        }

        public int getOutBufferSize() {
            return this.outBufferSize_;
        }

        public int getPort() {
            return this.port_;
        }

        public int getSerializedSize() {
            boolean bool = hasHost();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getHost());
            }
            if (hasPort()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getPort());
            }
            if (hasHeader()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getHeader());
            }
            if (hasOutBufferSize()) {
                i += CodedOutputStreamMicro.computeInt32Size(4, getOutBufferSize());
            }
            if (hasInBufferSize()) {
                i += CodedOutputStreamMicro.computeInt32Size(5, getInBufferSize());
            }
            if (hasOpenConnectionTimeoutMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(6, getOpenConnectionTimeoutMsec());
            }
            if (hasConnectionTimeoutMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(7, getConnectionTimeoutMsec());
            }
            if (hasUseSsl()) {
                i += CodedOutputStreamMicro.computeBoolSize(8, getUseSsl());
            }
            int j = 0;
            Iterator localIterator = getBlacklistMccMncList().iterator();
            while (localIterator.hasNext()) {
                j += CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator.next()).intValue());
            }
            int k = i + j + 1 * getBlacklistMccMncList().size();
            this.cachedSize = k;
            return k;
        }

        public boolean getUseSsl() {
            return this.useSsl_;
        }

        public boolean hasConnectionTimeoutMsec() {
            return this.hasConnectionTimeoutMsec;
        }

        public boolean hasHeader() {
            return this.hasHeader;
        }

        public boolean hasHost() {
            return this.hasHost;
        }

        public boolean hasInBufferSize() {
            return this.hasInBufferSize;
        }

        public boolean hasOpenConnectionTimeoutMsec() {
            return this.hasOpenConnectionTimeoutMsec;
        }

        public boolean hasOutBufferSize() {
            return this.hasOutBufferSize;
        }

        public boolean hasPort() {
            return this.hasPort;
        }

        public boolean hasUseSsl() {
            return this.hasUseSsl;
        }

        public ServerInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setHost(paramCodedInputStreamMicro.readString());
                        break;
                    case 16:
                        setPort(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setHeader(paramCodedInputStreamMicro.readString());
                        break;
                    case 32:
                        setOutBufferSize(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 40:
                        setInBufferSize(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 48:
                        setOpenConnectionTimeoutMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 56:
                        setConnectionTimeoutMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 64:
                        setUseSsl(paramCodedInputStreamMicro.readBool());
                        break;
                }
                addBlacklistMccMnc(paramCodedInputStreamMicro.readInt32());
            }
        }

        public ServerInfo setConnectionTimeoutMsec(int paramInt) {
            this.hasConnectionTimeoutMsec = true;
            this.connectionTimeoutMsec_ = paramInt;
            return this;
        }

        public ServerInfo setHeader(String paramString) {
            this.hasHeader = true;
            this.header_ = paramString;
            return this;
        }

        public ServerInfo setHost(String paramString) {
            this.hasHost = true;
            this.host_ = paramString;
            return this;
        }

        public ServerInfo setInBufferSize(int paramInt) {
            this.hasInBufferSize = true;
            this.inBufferSize_ = paramInt;
            return this;
        }

        public ServerInfo setOpenConnectionTimeoutMsec(int paramInt) {
            this.hasOpenConnectionTimeoutMsec = true;
            this.openConnectionTimeoutMsec_ = paramInt;
            return this;
        }

        public ServerInfo setOutBufferSize(int paramInt) {
            this.hasOutBufferSize = true;
            this.outBufferSize_ = paramInt;
            return this;
        }

        public ServerInfo setPort(int paramInt) {
            this.hasPort = true;
            this.port_ = paramInt;
            return this;
        }

        public ServerInfo setUseSsl(boolean paramBoolean) {
            this.hasUseSsl = true;
            this.useSsl_ = paramBoolean;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasHost()) {
                paramCodedOutputStreamMicro.writeString(1, getHost());
            }
            if (hasPort()) {
                paramCodedOutputStreamMicro.writeInt32(2, getPort());
            }
            if (hasHeader()) {
                paramCodedOutputStreamMicro.writeString(3, getHeader());
            }
            if (hasOutBufferSize()) {
                paramCodedOutputStreamMicro.writeInt32(4, getOutBufferSize());
            }
            if (hasInBufferSize()) {
                paramCodedOutputStreamMicro.writeInt32(5, getInBufferSize());
            }
            if (hasOpenConnectionTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(6, getOpenConnectionTimeoutMsec());
            }
            if (hasConnectionTimeoutMsec()) {
                paramCodedOutputStreamMicro.writeInt32(7, getConnectionTimeoutMsec());
            }
            if (hasUseSsl()) {
                paramCodedOutputStreamMicro.writeBool(8, getUseSsl());
            }
            Iterator localIterator = getBlacklistMccMncList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(9, ((Integer) localIterator.next()).intValue());
            }
        }
    }

    public static final class ServiceApi
            extends MessageMicro {
        private int cachedSize = -1;
        private GstaticConfiguration.EndpointerParams endpointerParams_ = null;
        private boolean hasEndpointerParams;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public GstaticConfiguration.EndpointerParams getEndpointerParams() {
            return this.endpointerParams_;
        }

        public int getSerializedSize() {
            boolean bool = hasEndpointerParams();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getEndpointerParams());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasEndpointerParams() {
            return this.hasEndpointerParams;
        }

        public ServiceApi mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                GstaticConfiguration.EndpointerParams localEndpointerParams = new GstaticConfiguration.EndpointerParams();
                paramCodedInputStreamMicro.readMessage(localEndpointerParams);
                setEndpointerParams(localEndpointerParams);
            }
        }

        public ServiceApi setEndpointerParams(GstaticConfiguration.EndpointerParams paramEndpointerParams) {
            if (paramEndpointerParams == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerParams = true;
            this.endpointerParams_ = paramEndpointerParams;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasEndpointerParams()) {
                paramCodedOutputStreamMicro.writeMessage(1, getEndpointerParams());
            }
        }
    }

    public static final class SoundSearch
            extends MessageMicro {
        private List<String> blacklistedDevices_ = Collections.emptyList();
        private int cachedSize = -1;
        private boolean enableMusicDetector_ = false;
        private boolean enableMusicHotworder_ = false;
        private boolean enablePreemptiveDetection_ = false;
        private boolean hasEnableMusicDetector;
        private boolean hasEnableMusicHotworder;
        private boolean hasEnablePreemptiveDetection;
        private boolean hasMusicDetectorThreshold;
        private boolean hasStopMusicDetectionOnStartOfSpeech;
        private float musicDetectorThreshold_ = 0.0F;
        private boolean stopMusicDetectionOnStartOfSpeech_ = false;

        public SoundSearch addBlacklistedDevices(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.blacklistedDevices_.isEmpty()) {
                this.blacklistedDevices_ = new ArrayList();
            }
            this.blacklistedDevices_.add(paramString);
            return this;
        }

        public List<String> getBlacklistedDevicesList() {
            return this.blacklistedDevices_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public boolean getEnableMusicDetector() {
            return this.enableMusicDetector_;
        }

        public boolean getEnableMusicHotworder() {
            return this.enableMusicHotworder_;
        }

        public boolean getEnablePreemptiveDetection() {
            return this.enablePreemptiveDetection_;
        }

        public float getMusicDetectorThreshold() {
            return this.musicDetectorThreshold_;
        }

        public int getSerializedSize() {
            boolean bool = hasEnableMusicDetector();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getEnableMusicDetector());
            }
            if (hasMusicDetectorThreshold()) {
                i += CodedOutputStreamMicro.computeFloatSize(2, getMusicDetectorThreshold());
            }
            if (hasStopMusicDetectionOnStartOfSpeech()) {
                i += CodedOutputStreamMicro.computeBoolSize(3, getStopMusicDetectionOnStartOfSpeech());
            }
            int j = 0;
            Iterator localIterator = getBlacklistedDevicesList().iterator();
            while (localIterator.hasNext()) {
                j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator.next());
            }
            int k = i + j + 1 * getBlacklistedDevicesList().size();
            if (hasEnableMusicHotworder()) {
                k += CodedOutputStreamMicro.computeBoolSize(5, getEnableMusicHotworder());
            }
            if (hasEnablePreemptiveDetection()) {
                k += CodedOutputStreamMicro.computeBoolSize(6, getEnablePreemptiveDetection());
            }
            this.cachedSize = k;
            return k;
        }

        public boolean getStopMusicDetectionOnStartOfSpeech() {
            return this.stopMusicDetectionOnStartOfSpeech_;
        }

        public boolean hasEnableMusicDetector() {
            return this.hasEnableMusicDetector;
        }

        public boolean hasEnableMusicHotworder() {
            return this.hasEnableMusicHotworder;
        }

        public boolean hasEnablePreemptiveDetection() {
            return this.hasEnablePreemptiveDetection;
        }

        public boolean hasMusicDetectorThreshold() {
            return this.hasMusicDetectorThreshold;
        }

        public boolean hasStopMusicDetectionOnStartOfSpeech() {
            return this.hasStopMusicDetectionOnStartOfSpeech;
        }

        public SoundSearch mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setEnableMusicDetector(paramCodedInputStreamMicro.readBool());
                        break;
                    case 21:
                        setMusicDetectorThreshold(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 24:
                        setStopMusicDetectionOnStartOfSpeech(paramCodedInputStreamMicro.readBool());
                        break;
                    case 34:
                        addBlacklistedDevices(paramCodedInputStreamMicro.readString());
                        break;
                    case 40:
                        setEnableMusicHotworder(paramCodedInputStreamMicro.readBool());
                        break;
                }
                setEnablePreemptiveDetection(paramCodedInputStreamMicro.readBool());
            }
        }

        public SoundSearch setEnableMusicDetector(boolean paramBoolean) {
            this.hasEnableMusicDetector = true;
            this.enableMusicDetector_ = paramBoolean;
            return this;
        }

        public SoundSearch setEnableMusicHotworder(boolean paramBoolean) {
            this.hasEnableMusicHotworder = true;
            this.enableMusicHotworder_ = paramBoolean;
            return this;
        }

        public SoundSearch setEnablePreemptiveDetection(boolean paramBoolean) {
            this.hasEnablePreemptiveDetection = true;
            this.enablePreemptiveDetection_ = paramBoolean;
            return this;
        }

        public SoundSearch setMusicDetectorThreshold(float paramFloat) {
            this.hasMusicDetectorThreshold = true;
            this.musicDetectorThreshold_ = paramFloat;
            return this;
        }

        public SoundSearch setStopMusicDetectionOnStartOfSpeech(boolean paramBoolean) {
            this.hasStopMusicDetectionOnStartOfSpeech = true;
            this.stopMusicDetectionOnStartOfSpeech_ = paramBoolean;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasEnableMusicDetector()) {
                paramCodedOutputStreamMicro.writeBool(1, getEnableMusicDetector());
            }
            if (hasMusicDetectorThreshold()) {
                paramCodedOutputStreamMicro.writeFloat(2, getMusicDetectorThreshold());
            }
            if (hasStopMusicDetectionOnStartOfSpeech()) {
                paramCodedOutputStreamMicro.writeBool(3, getStopMusicDetectionOnStartOfSpeech());
            }
            Iterator localIterator = getBlacklistedDevicesList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeString(4, (String) localIterator.next());
            }
            if (hasEnableMusicHotworder()) {
                paramCodedOutputStreamMicro.writeBool(5, getEnableMusicHotworder());
            }
            if (hasEnablePreemptiveDetection()) {
                paramCodedOutputStreamMicro.writeBool(6, getEnablePreemptiveDetection());
            }
        }
    }

    public static final class VoiceSearch
            extends MessageMicro {
        private int actionCountDownMsec_ = 0;
        private int cachedSize = -1;
        private int embeddedRecognizerFallbackTimeout_ = 0;
        private GstaticConfiguration.EndpointerParams endpointerParams_ = null;
        private boolean hasActionCountDownMsec;
        private boolean hasEmbeddedRecognizerFallbackTimeout;
        private boolean hasEndpointerParams;

        public int getActionCountDownMsec() {
            return this.actionCountDownMsec_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getEmbeddedRecognizerFallbackTimeout() {
            return this.embeddedRecognizerFallbackTimeout_;
        }

        public GstaticConfiguration.EndpointerParams getEndpointerParams() {
            return this.endpointerParams_;
        }

        public int getSerializedSize() {
            boolean bool = hasEndpointerParams();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getEndpointerParams());
            }
            if (hasActionCountDownMsec()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getActionCountDownMsec());
            }
            if (hasEmbeddedRecognizerFallbackTimeout()) {
                i += CodedOutputStreamMicro.computeInt32Size(3, getEmbeddedRecognizerFallbackTimeout());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasActionCountDownMsec() {
            return this.hasActionCountDownMsec;
        }

        public boolean hasEmbeddedRecognizerFallbackTimeout() {
            return this.hasEmbeddedRecognizerFallbackTimeout;
        }

        public boolean hasEndpointerParams() {
            return this.hasEndpointerParams;
        }

        public VoiceSearch mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        GstaticConfiguration.EndpointerParams localEndpointerParams = new GstaticConfiguration.EndpointerParams();
                        paramCodedInputStreamMicro.readMessage(localEndpointerParams);
                        setEndpointerParams(localEndpointerParams);
                        break;
                    case 16:
                        setActionCountDownMsec(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setEmbeddedRecognizerFallbackTimeout(paramCodedInputStreamMicro.readInt32());
            }
        }

        public VoiceSearch setActionCountDownMsec(int paramInt) {
            this.hasActionCountDownMsec = true;
            this.actionCountDownMsec_ = paramInt;
            return this;
        }

        public VoiceSearch setEmbeddedRecognizerFallbackTimeout(int paramInt) {
            this.hasEmbeddedRecognizerFallbackTimeout = true;
            this.embeddedRecognizerFallbackTimeout_ = paramInt;
            return this;
        }

        public VoiceSearch setEndpointerParams(GstaticConfiguration.EndpointerParams paramEndpointerParams) {
            if (paramEndpointerParams == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerParams = true;
            this.endpointerParams_ = paramEndpointerParams;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasEndpointerParams()) {
                paramCodedOutputStreamMicro.writeMessage(1, getEndpointerParams());
            }
            if (hasActionCountDownMsec()) {
                paramCodedOutputStreamMicro.writeInt32(2, getActionCountDownMsec());
            }
            if (hasEmbeddedRecognizerFallbackTimeout()) {
                paramCodedOutputStreamMicro.writeInt32(3, getEmbeddedRecognizerFallbackTimeout());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.wireless.voicesearch.proto.GstaticConfiguration

 * JD-Core Version:    0.7.0.1

 */