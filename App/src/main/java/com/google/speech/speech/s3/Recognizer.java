package com.google.speech.speech.s3;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.common.Alternates;
import com.google.speech.common.proto.RecognitionContextProto;
import com.google.speech.recognizer.api.RecognizerProtos;
import com.google.speech.util.Contacts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Recognizer {
    public static final class RecognizerEvent
            extends MessageMicro {
        private int cachedSize = -1;
        private RecognizerProtos.EndpointerEvent endpointerEvent_ = null;
        private boolean hasEndpointerEvent;
        private boolean hasLanguage;
        private boolean hasRecognitionEvent;
        private String language_ = "";
        private RecognizerProtos.RecognitionEvent recognitionEvent_ = null;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public RecognizerProtos.EndpointerEvent getEndpointerEvent() {
            return this.endpointerEvent_;
        }

        public RecognizerEvent setEndpointerEvent(RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
            if (paramEndpointerEvent == null) {
                throw new NullPointerException();
            }
            this.hasEndpointerEvent = true;
            this.endpointerEvent_ = paramEndpointerEvent;
            return this;
        }

        public String getLanguage() {
            return this.language_;
        }

        public RecognizerEvent setLanguage(String paramString) {
            this.hasLanguage = true;
            this.language_ = paramString;
            return this;
        }

        public RecognizerProtos.RecognitionEvent getRecognitionEvent() {
            return this.recognitionEvent_;
        }

        public RecognizerEvent setRecognitionEvent(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
            if (paramRecognitionEvent == null) {
                throw new NullPointerException();
            }
            this.hasRecognitionEvent = true;
            this.recognitionEvent_ = paramRecognitionEvent;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasRecognitionEvent();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getRecognitionEvent());
            }
            if (hasEndpointerEvent()) {
                i += CodedOutputStreamMicro.computeMessageSize(2, getEndpointerEvent());
            }
            if (hasLanguage()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getLanguage());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasEndpointerEvent() {
            return this.hasEndpointerEvent;
        }

        public boolean hasLanguage() {
            return this.hasLanguage;
        }

        public boolean hasRecognitionEvent() {
            return this.hasRecognitionEvent;
        }

        public RecognizerEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        RecognizerProtos.RecognitionEvent localRecognitionEvent = new RecognizerProtos.RecognitionEvent();
                        paramCodedInputStreamMicro.readMessage(localRecognitionEvent);
                        setRecognitionEvent(localRecognitionEvent);
                        break;
                    case 18:
                        RecognizerProtos.EndpointerEvent localEndpointerEvent = new RecognizerProtos.EndpointerEvent();
                        paramCodedInputStreamMicro.readMessage(localEndpointerEvent);
                        setEndpointerEvent(localEndpointerEvent);
                        break;
                }
                setLanguage(paramCodedInputStreamMicro.readString());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasRecognitionEvent()) {
                paramCodedOutputStreamMicro.writeMessage(1, getRecognitionEvent());
            }
            if (hasEndpointerEvent()) {
                paramCodedOutputStreamMicro.writeMessage(2, getEndpointerEvent());
            }
            if (hasLanguage()) {
                paramCodedOutputStreamMicro.writeString(3, getLanguage());
            }
        }
    }

    public static final class RecognizerVocabularyContext
            extends MessageMicro {
        private int cachedSize = -1;
        private List<Contacts.TopContact> topContact_ = Collections.emptyList();

        public RecognizerVocabularyContext addTopContact(Contacts.TopContact paramTopContact) {
            if (paramTopContact == null) {
                throw new NullPointerException();
            }
            if (this.topContact_.isEmpty()) {
                this.topContact_ = new ArrayList();
            }
            this.topContact_.add(paramTopContact);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getTopContactList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(1, (Contacts.TopContact) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public List<Contacts.TopContact> getTopContactList() {
            return this.topContact_;
        }

        public RecognizerVocabularyContext mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int i = paramCodedInputStreamMicro.readTag();
                switch (i) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, i)) {
                            continue;
                        }
                        Contacts.TopContact localTopContact = new Contacts.TopContact();
                        paramCodedInputStreamMicro.readMessage(localTopContact);
                        addTopContact(localTopContact);
                    case 0:
                        return this;
                }
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getTopContactList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(1, (Contacts.TopContact) localIterator.next());
            }
        }
    }

    public static final class S3RecognizerInfo
            extends MessageMicro {
        private Alternates.AlternateParams alternateParams_ = null;
        private int cachedSize = -1;
        private String condition_ = "";
        private float confidenceThreshold_ = 0.0F;
        private boolean dictationMode_ = false;
        private boolean enableAlternates_ = false;
        private boolean enableCombinedNbest_ = false;
        private boolean enableEndpointerEvents_ = false;
        private boolean enableLattice_ = false;
        private boolean enablePartialNbest_ = false;
        private boolean enablePartialResults_ = false;
        private boolean enablePersonalization_ = false;
        private boolean enableSemanticResults_ = false;
        private GrammarSelectorParams grammarParams_ = null;
        private boolean greco2CompatMode_ = false;
        private boolean hasAlternateParams;
        private boolean hasCondition;
        private boolean hasConfidenceThreshold;
        private boolean hasDictationMode;
        private boolean hasEnableAlternates;
        private boolean hasEnableCombinedNbest;
        private boolean hasEnableEndpointerEvents;
        private boolean hasEnableLattice;
        private boolean hasEnablePartialNbest;
        private boolean hasEnablePartialResults;
        private boolean hasEnablePersonalization;
        private boolean hasEnableSemanticResults;
        private boolean hasGrammarParams;
        private boolean hasGreco2CompatMode;
        private boolean hasLogFeaturesOnly;
        private boolean hasLoggingDataRequested;
        private boolean hasMaxNbest;
        private boolean hasProfanityFilter;
        private boolean hasRecognitionContext;
        private boolean hasResetIntervalMs;
        private boolean logFeaturesOnly_ = false;
        private boolean loggingDataRequested_ = false;
        private int maxNbest_ = 1;
        private int profanityFilter_ = 2;
        private RecognitionContextProto.RecognitionContext recognitionContext_ = null;
        private int resetIntervalMs_ = 0;

        public Alternates.AlternateParams getAlternateParams() {
            return this.alternateParams_;
        }

        public S3RecognizerInfo setAlternateParams(Alternates.AlternateParams paramAlternateParams) {
            if (paramAlternateParams == null) {
                throw new NullPointerException();
            }
            this.hasAlternateParams = true;
            this.alternateParams_ = paramAlternateParams;
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getCondition() {
            return this.condition_;
        }

        public S3RecognizerInfo setCondition(String paramString) {
            this.hasCondition = true;
            this.condition_ = paramString;
            return this;
        }

        public float getConfidenceThreshold() {
            return this.confidenceThreshold_;
        }

        public S3RecognizerInfo setConfidenceThreshold(float paramFloat) {
            this.hasConfidenceThreshold = true;
            this.confidenceThreshold_ = paramFloat;
            return this;
        }

        public boolean getDictationMode() {
            return this.dictationMode_;
        }

        public S3RecognizerInfo setDictationMode(boolean paramBoolean) {
            this.hasDictationMode = true;
            this.dictationMode_ = paramBoolean;
            return this;
        }

        public boolean getEnableAlternates() {
            return this.enableAlternates_;
        }

        public S3RecognizerInfo setEnableAlternates(boolean paramBoolean) {
            this.hasEnableAlternates = true;
            this.enableAlternates_ = paramBoolean;
            return this;
        }

        public boolean getEnableCombinedNbest() {
            return this.enableCombinedNbest_;
        }

        public S3RecognizerInfo setEnableCombinedNbest(boolean paramBoolean) {
            this.hasEnableCombinedNbest = true;
            this.enableCombinedNbest_ = paramBoolean;
            return this;
        }

        public boolean getEnableEndpointerEvents() {
            return this.enableEndpointerEvents_;
        }

        public S3RecognizerInfo setEnableEndpointerEvents(boolean paramBoolean) {
            this.hasEnableEndpointerEvents = true;
            this.enableEndpointerEvents_ = paramBoolean;
            return this;
        }

        public boolean getEnableLattice() {
            return this.enableLattice_;
        }

        public S3RecognizerInfo setEnableLattice(boolean paramBoolean) {
            this.hasEnableLattice = true;
            this.enableLattice_ = paramBoolean;
            return this;
        }

        public boolean getEnablePartialNbest() {
            return this.enablePartialNbest_;
        }

        public S3RecognizerInfo setEnablePartialNbest(boolean paramBoolean) {
            this.hasEnablePartialNbest = true;
            this.enablePartialNbest_ = paramBoolean;
            return this;
        }

        public boolean getEnablePartialResults() {
            return this.enablePartialResults_;
        }

        public S3RecognizerInfo setEnablePartialResults(boolean paramBoolean) {
            this.hasEnablePartialResults = true;
            this.enablePartialResults_ = paramBoolean;
            return this;
        }

        public boolean getEnablePersonalization() {
            return this.enablePersonalization_;
        }

        public S3RecognizerInfo setEnablePersonalization(boolean paramBoolean) {
            this.hasEnablePersonalization = true;
            this.enablePersonalization_ = paramBoolean;
            return this;
        }

        public boolean getEnableSemanticResults() {
            return this.enableSemanticResults_;
        }

        public S3RecognizerInfo setEnableSemanticResults(boolean paramBoolean) {
            this.hasEnableSemanticResults = true;
            this.enableSemanticResults_ = paramBoolean;
            return this;
        }

        public GrammarSelectorParams getGrammarParams() {
            return this.grammarParams_;
        }

        public S3RecognizerInfo setGrammarParams(GrammarSelectorParams paramGrammarSelectorParams) {
            if (paramGrammarSelectorParams == null) {
                throw new NullPointerException();
            }
            this.hasGrammarParams = true;
            this.grammarParams_ = paramGrammarSelectorParams;
            return this;
        }

        public boolean getGreco2CompatMode() {
            return this.greco2CompatMode_;
        }

        public S3RecognizerInfo setGreco2CompatMode(boolean paramBoolean) {
            this.hasGreco2CompatMode = true;
            this.greco2CompatMode_ = paramBoolean;
            return this;
        }

        public boolean getLogFeaturesOnly() {
            return this.logFeaturesOnly_;
        }

        public S3RecognizerInfo setLogFeaturesOnly(boolean paramBoolean) {
            this.hasLogFeaturesOnly = true;
            this.logFeaturesOnly_ = paramBoolean;
            return this;
        }

        public boolean getLoggingDataRequested() {
            return this.loggingDataRequested_;
        }

        public S3RecognizerInfo setLoggingDataRequested(boolean paramBoolean) {
            this.hasLoggingDataRequested = true;
            this.loggingDataRequested_ = paramBoolean;
            return this;
        }

        public int getMaxNbest() {
            return this.maxNbest_;
        }

        public S3RecognizerInfo setMaxNbest(int paramInt) {
            this.hasMaxNbest = true;
            this.maxNbest_ = paramInt;
            return this;
        }

        public int getProfanityFilter() {
            return this.profanityFilter_;
        }

        public S3RecognizerInfo setProfanityFilter(int paramInt) {
            this.hasProfanityFilter = true;
            this.profanityFilter_ = paramInt;
            return this;
        }

        public RecognitionContextProto.RecognitionContext getRecognitionContext() {
            return this.recognitionContext_;
        }

        public S3RecognizerInfo setRecognitionContext(RecognitionContextProto.RecognitionContext paramRecognitionContext) {
            if (paramRecognitionContext == null) {
                throw new NullPointerException();
            }
            this.hasRecognitionContext = true;
            this.recognitionContext_ = paramRecognitionContext;
            return this;
        }

        public int getResetIntervalMs() {
            return this.resetIntervalMs_;
        }

        public S3RecognizerInfo setResetIntervalMs(int paramInt) {
            this.hasResetIntervalMs = true;
            this.resetIntervalMs_ = paramInt;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasRecognitionContext();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getRecognitionContext());
            }
            if (hasMaxNbest()) {
                i += CodedOutputStreamMicro.computeInt32Size(3, getMaxNbest());
            }
            if (hasAlternateParams()) {
                i += CodedOutputStreamMicro.computeMessageSize(4, getAlternateParams());
            }
            if (hasEnablePartialResults()) {
                i += CodedOutputStreamMicro.computeBoolSize(5, getEnablePartialResults());
            }
            if (hasEnableLattice()) {
                i += CodedOutputStreamMicro.computeBoolSize(6, getEnableLattice());
            }
            if (hasProfanityFilter()) {
                i += CodedOutputStreamMicro.computeInt32Size(7, getProfanityFilter());
            }
            if (hasConfidenceThreshold()) {
                i += CodedOutputStreamMicro.computeFloatSize(8, getConfidenceThreshold());
            }
            if (hasGrammarParams()) {
                i += CodedOutputStreamMicro.computeMessageSize(9, getGrammarParams());
            }
            if (hasCondition()) {
                i += CodedOutputStreamMicro.computeStringSize(10, getCondition());
            }
            if (hasDictationMode()) {
                i += CodedOutputStreamMicro.computeBoolSize(11, getDictationMode());
            }
            if (hasEnableSemanticResults()) {
                i += CodedOutputStreamMicro.computeBoolSize(12, getEnableSemanticResults());
            }
            if (hasEnableAlternates()) {
                i += CodedOutputStreamMicro.computeBoolSize(13, getEnableAlternates());
            }
            if (hasEnableCombinedNbest()) {
                i += CodedOutputStreamMicro.computeBoolSize(14, getEnableCombinedNbest());
            }
            if (hasGreco2CompatMode()) {
                i += CodedOutputStreamMicro.computeBoolSize(15, getGreco2CompatMode());
            }
            if (hasEnablePersonalization()) {
                i += CodedOutputStreamMicro.computeBoolSize(16, getEnablePersonalization());
            }
            if (hasLoggingDataRequested()) {
                i += CodedOutputStreamMicro.computeBoolSize(17, getLoggingDataRequested());
            }
            if (hasLogFeaturesOnly()) {
                i += CodedOutputStreamMicro.computeBoolSize(18, getLogFeaturesOnly());
            }
            if (hasResetIntervalMs()) {
                i += CodedOutputStreamMicro.computeInt32Size(19, getResetIntervalMs());
            }
            if (hasEnableEndpointerEvents()) {
                i += CodedOutputStreamMicro.computeBoolSize(20, getEnableEndpointerEvents());
            }
            if (hasEnablePartialNbest()) {
                i += CodedOutputStreamMicro.computeBoolSize(21, getEnablePartialNbest());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasAlternateParams() {
            return this.hasAlternateParams;
        }

        public boolean hasCondition() {
            return this.hasCondition;
        }

        public boolean hasConfidenceThreshold() {
            return this.hasConfidenceThreshold;
        }

        public boolean hasDictationMode() {
            return this.hasDictationMode;
        }

        public boolean hasEnableAlternates() {
            return this.hasEnableAlternates;
        }

        public boolean hasEnableCombinedNbest() {
            return this.hasEnableCombinedNbest;
        }

        public boolean hasEnableEndpointerEvents() {
            return this.hasEnableEndpointerEvents;
        }

        public boolean hasEnableLattice() {
            return this.hasEnableLattice;
        }

        public boolean hasEnablePartialNbest() {
            return this.hasEnablePartialNbest;
        }

        public boolean hasEnablePartialResults() {
            return this.hasEnablePartialResults;
        }

        public boolean hasEnablePersonalization() {
            return this.hasEnablePersonalization;
        }

        public boolean hasEnableSemanticResults() {
            return this.hasEnableSemanticResults;
        }

        public boolean hasGrammarParams() {
            return this.hasGrammarParams;
        }

        public boolean hasGreco2CompatMode() {
            return this.hasGreco2CompatMode;
        }

        public boolean hasLogFeaturesOnly() {
            return this.hasLogFeaturesOnly;
        }

        public boolean hasLoggingDataRequested() {
            return this.hasLoggingDataRequested;
        }

        public boolean hasMaxNbest() {
            return this.hasMaxNbest;
        }

        public boolean hasProfanityFilter() {
            return this.hasProfanityFilter;
        }

        public boolean hasRecognitionContext() {
            return this.hasRecognitionContext;
        }

        public boolean hasResetIntervalMs() {
            return this.hasResetIntervalMs;
        }

        public S3RecognizerInfo mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        RecognitionContextProto.RecognitionContext localRecognitionContext = new RecognitionContextProto.RecognitionContext();
                        paramCodedInputStreamMicro.readMessage(localRecognitionContext);
                        setRecognitionContext(localRecognitionContext);
                        break;
                    case 24:
                        setMaxNbest(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 34:
                        Alternates.AlternateParams localAlternateParams = new Alternates.AlternateParams();
                        paramCodedInputStreamMicro.readMessage(localAlternateParams);
                        setAlternateParams(localAlternateParams);
                        break;
                    case 40:
                        setEnablePartialResults(paramCodedInputStreamMicro.readBool());
                        break;
                    case 48:
                        setEnableLattice(paramCodedInputStreamMicro.readBool());
                        break;
                    case 56:
                        setProfanityFilter(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 69:
                        setConfidenceThreshold(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 74:
                        GrammarSelectorParams localGrammarSelectorParams = new GrammarSelectorParams();
                        paramCodedInputStreamMicro.readMessage(localGrammarSelectorParams);
                        setGrammarParams(localGrammarSelectorParams);
                        break;
                    case 82:
                        setCondition(paramCodedInputStreamMicro.readString());
                        break;
                    case 88:
                        setDictationMode(paramCodedInputStreamMicro.readBool());
                        break;
                    case 96:
                        setEnableSemanticResults(paramCodedInputStreamMicro.readBool());
                        break;
                    case 104:
                        setEnableAlternates(paramCodedInputStreamMicro.readBool());
                        break;
                    case 112:
                        setEnableCombinedNbest(paramCodedInputStreamMicro.readBool());
                        break;
                    case 120:
                        setGreco2CompatMode(paramCodedInputStreamMicro.readBool());
                        break;
                    case 128:
                        setEnablePersonalization(paramCodedInputStreamMicro.readBool());
                        break;
                    case 136:
                        setLoggingDataRequested(paramCodedInputStreamMicro.readBool());
                        break;
                    case 144:
                        setLogFeaturesOnly(paramCodedInputStreamMicro.readBool());
                        break;
                    case 152:
                        setResetIntervalMs(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 160:
                        setEnableEndpointerEvents(paramCodedInputStreamMicro.readBool());
                        break;
                }
                setEnablePartialNbest(paramCodedInputStreamMicro.readBool());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasRecognitionContext()) {
                paramCodedOutputStreamMicro.writeMessage(1, getRecognitionContext());
            }
            if (hasMaxNbest()) {
                paramCodedOutputStreamMicro.writeInt32(3, getMaxNbest());
            }
            if (hasAlternateParams()) {
                paramCodedOutputStreamMicro.writeMessage(4, getAlternateParams());
            }
            if (hasEnablePartialResults()) {
                paramCodedOutputStreamMicro.writeBool(5, getEnablePartialResults());
            }
            if (hasEnableLattice()) {
                paramCodedOutputStreamMicro.writeBool(6, getEnableLattice());
            }
            if (hasProfanityFilter()) {
                paramCodedOutputStreamMicro.writeInt32(7, getProfanityFilter());
            }
            if (hasConfidenceThreshold()) {
                paramCodedOutputStreamMicro.writeFloat(8, getConfidenceThreshold());
            }
            if (hasGrammarParams()) {
                paramCodedOutputStreamMicro.writeMessage(9, getGrammarParams());
            }
            if (hasCondition()) {
                paramCodedOutputStreamMicro.writeString(10, getCondition());
            }
            if (hasDictationMode()) {
                paramCodedOutputStreamMicro.writeBool(11, getDictationMode());
            }
            if (hasEnableSemanticResults()) {
                paramCodedOutputStreamMicro.writeBool(12, getEnableSemanticResults());
            }
            if (hasEnableAlternates()) {
                paramCodedOutputStreamMicro.writeBool(13, getEnableAlternates());
            }
            if (hasEnableCombinedNbest()) {
                paramCodedOutputStreamMicro.writeBool(14, getEnableCombinedNbest());
            }
            if (hasGreco2CompatMode()) {
                paramCodedOutputStreamMicro.writeBool(15, getGreco2CompatMode());
            }
            if (hasEnablePersonalization()) {
                paramCodedOutputStreamMicro.writeBool(16, getEnablePersonalization());
            }
            if (hasLoggingDataRequested()) {
                paramCodedOutputStreamMicro.writeBool(17, getLoggingDataRequested());
            }
            if (hasLogFeaturesOnly()) {
                paramCodedOutputStreamMicro.writeBool(18, getLogFeaturesOnly());
            }
            if (hasResetIntervalMs()) {
                paramCodedOutputStreamMicro.writeInt32(19, getResetIntervalMs());
            }
            if (hasEnableEndpointerEvents()) {
                paramCodedOutputStreamMicro.writeBool(20, getEnableEndpointerEvents());
            }
            if (hasEnablePartialNbest()) {
                paramCodedOutputStreamMicro.writeBool(21, getEnablePartialNbest());
            }
        }

        public static final class GrammarSelectorParams
                extends MessageMicro {
            private int cachedSize = -1;
            private boolean hasSelectedApplication;
            private boolean hasSelectedSlot;
            private String selectedApplication_ = "";
            private String selectedSlot_ = "";

            public int getCachedSize() {
                if (this.cachedSize < 0) {
                    getSerializedSize();
                }
                return this.cachedSize;
            }

            public String getSelectedApplication() {
                return this.selectedApplication_;
            }

            public GrammarSelectorParams setSelectedApplication(String paramString) {
                this.hasSelectedApplication = true;
                this.selectedApplication_ = paramString;
                return this;
            }

            public String getSelectedSlot() {
                return this.selectedSlot_;
            }

            public GrammarSelectorParams setSelectedSlot(String paramString) {
                this.hasSelectedSlot = true;
                this.selectedSlot_ = paramString;
                return this;
            }

            public int getSerializedSize() {
                boolean bool = hasSelectedApplication();
                int i = 0;
                if (bool) {
                    i = 0 + CodedOutputStreamMicro.computeStringSize(1, getSelectedApplication());
                }
                if (hasSelectedSlot()) {
                    i += CodedOutputStreamMicro.computeStringSize(2, getSelectedSlot());
                }
                this.cachedSize = i;
                return i;
            }

            public boolean hasSelectedApplication() {
                return this.hasSelectedApplication;
            }

            public boolean hasSelectedSlot() {
                return this.hasSelectedSlot;
            }

            public GrammarSelectorParams mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                            setSelectedApplication(paramCodedInputStreamMicro.readString());
                            break;
                    }
                    setSelectedSlot(paramCodedInputStreamMicro.readString());
                }
            }

            public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasSelectedApplication()) {
                    paramCodedOutputStreamMicro.writeString(1, getSelectedApplication());
                }
                if (hasSelectedSlot()) {
                    paramCodedOutputStreamMicro.writeString(2, getSelectedSlot());
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.speech.s3.Recognizer

 * JD-Core Version:    0.7.0.1

 */