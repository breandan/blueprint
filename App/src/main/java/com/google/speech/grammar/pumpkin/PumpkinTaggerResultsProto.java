package com.google.speech.grammar.pumpkin;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class PumpkinTaggerResultsProto {
    public static final class ActionArgument
            extends MessageMicro {
        private int cachedSize = -1;
        private boolean hasName;
        private boolean hasScore;
        private boolean hasType;
        private boolean hasUnnormalizedValue;
        private boolean hasUserType;
        private boolean hasValue;
        private String name_ = "";
        private float score_ = 0.0F;
        private int type_ = -1;
        private String unnormalizedValue_ = "";
        private String userType_ = "";
        private String value_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getName() {
            return this.name_;
        }

        public ActionArgument setName(String paramString) {
            this.hasName = true;
            this.name_ = paramString;
            return this;
        }

        public float getScore() {
            return this.score_;
        }

        public ActionArgument setScore(float paramFloat) {
            this.hasScore = true;
            this.score_ = paramFloat;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasName();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
            }
            if (hasType()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getType());
            }
            if (hasUserType()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getUserType());
            }
            if (hasScore()) {
                i += CodedOutputStreamMicro.computeFloatSize(4, getScore());
            }
            if (hasValue()) {
                i += CodedOutputStreamMicro.computeStringSize(5, getValue());
            }
            if (hasUnnormalizedValue()) {
                i += CodedOutputStreamMicro.computeStringSize(6, getUnnormalizedValue());
            }
            this.cachedSize = i;
            return i;
        }

        public int getType() {
            return this.type_;
        }

        public ActionArgument setType(int paramInt) {
            this.hasType = true;
            this.type_ = paramInt;
            return this;
        }

        public String getUnnormalizedValue() {
            return this.unnormalizedValue_;
        }

        public ActionArgument setUnnormalizedValue(String paramString) {
            this.hasUnnormalizedValue = true;
            this.unnormalizedValue_ = paramString;
            return this;
        }

        public String getUserType() {
            return this.userType_;
        }

        public ActionArgument setUserType(String paramString) {
            this.hasUserType = true;
            this.userType_ = paramString;
            return this;
        }

        public String getValue() {
            return this.value_;
        }

        public ActionArgument setValue(String paramString) {
            this.hasValue = true;
            this.value_ = paramString;
            return this;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public boolean hasScore() {
            return this.hasScore;
        }

        public boolean hasType() {
            return this.hasType;
        }

        public boolean hasUnnormalizedValue() {
            return this.hasUnnormalizedValue;
        }

        public boolean hasUserType() {
            return this.hasUserType;
        }

        public boolean hasValue() {
            return this.hasValue;
        }

        public ActionArgument mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setName(paramCodedInputStreamMicro.readString());
                        break;
                    case 16:
                        setType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setUserType(paramCodedInputStreamMicro.readString());
                        break;
                    case 37:
                        setScore(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 42:
                        setValue(paramCodedInputStreamMicro.readString());
                        break;
                }
                setUnnormalizedValue(paramCodedInputStreamMicro.readString());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasName()) {
                paramCodedOutputStreamMicro.writeString(1, getName());
            }
            if (hasType()) {
                paramCodedOutputStreamMicro.writeInt32(2, getType());
            }
            if (hasUserType()) {
                paramCodedOutputStreamMicro.writeString(3, getUserType());
            }
            if (hasScore()) {
                paramCodedOutputStreamMicro.writeFloat(4, getScore());
            }
            if (hasValue()) {
                paramCodedOutputStreamMicro.writeString(5, getValue());
            }
            if (hasUnnormalizedValue()) {
                paramCodedOutputStreamMicro.writeString(6, getUnnormalizedValue());
            }
        }
    }

    public static final class HypothesisResult
            extends MessageMicro {
        private List<PumpkinTaggerResultsProto.ActionArgument> actionArgument_ = Collections.emptyList();
        private String actionExportName_ = "";
        private String actionName_ = "";
        private int cachedSize = -1;
        private boolean hasActionExportName;
        private boolean hasActionName;
        private boolean hasScore;
        private boolean hasTaggedHypothesis;
        private float score_ = 0.0F;
        private String taggedHypothesis_ = "";

        public HypothesisResult addActionArgument(PumpkinTaggerResultsProto.ActionArgument paramActionArgument) {
            if (paramActionArgument == null) {
                throw new NullPointerException();
            }
            if (this.actionArgument_.isEmpty()) {
                this.actionArgument_ = new ArrayList();
            }
            this.actionArgument_.add(paramActionArgument);
            return this;
        }

        public List<PumpkinTaggerResultsProto.ActionArgument> getActionArgumentList() {
            return this.actionArgument_;
        }

        public String getActionExportName() {
            return this.actionExportName_;
        }

        public HypothesisResult setActionExportName(String paramString) {
            this.hasActionExportName = true;
            this.actionExportName_ = paramString;
            return this;
        }

        public String getActionName() {
            return this.actionName_;
        }

        public HypothesisResult setActionName(String paramString) {
            this.hasActionName = true;
            this.actionName_ = paramString;
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public float getScore() {
            return this.score_;
        }

        public HypothesisResult setScore(float paramFloat) {
            this.hasScore = true;
            this.score_ = paramFloat;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasActionExportName();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getActionExportName());
            }
            if (hasActionName()) {
                i += CodedOutputStreamMicro.computeStringSize(2, getActionName());
            }
            Iterator localIterator = getActionArgumentList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(3, (PumpkinTaggerResultsProto.ActionArgument) localIterator.next());
            }
            if (hasScore()) {
                i += CodedOutputStreamMicro.computeFloatSize(4, getScore());
            }
            if (hasTaggedHypothesis()) {
                i += CodedOutputStreamMicro.computeStringSize(5, getTaggedHypothesis());
            }
            this.cachedSize = i;
            return i;
        }

        public String getTaggedHypothesis() {
            return this.taggedHypothesis_;
        }

        public HypothesisResult setTaggedHypothesis(String paramString) {
            this.hasTaggedHypothesis = true;
            this.taggedHypothesis_ = paramString;
            return this;
        }

        public boolean hasActionExportName() {
            return this.hasActionExportName;
        }

        public boolean hasActionName() {
            return this.hasActionName;
        }

        public boolean hasScore() {
            return this.hasScore;
        }

        public boolean hasTaggedHypothesis() {
            return this.hasTaggedHypothesis;
        }

        public HypothesisResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setActionExportName(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        setActionName(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        PumpkinTaggerResultsProto.ActionArgument localActionArgument = new PumpkinTaggerResultsProto.ActionArgument();
                        paramCodedInputStreamMicro.readMessage(localActionArgument);
                        addActionArgument(localActionArgument);
                        break;
                    case 37:
                        setScore(paramCodedInputStreamMicro.readFloat());
                        break;
                }
                setTaggedHypothesis(paramCodedInputStreamMicro.readString());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasActionExportName()) {
                paramCodedOutputStreamMicro.writeString(1, getActionExportName());
            }
            if (hasActionName()) {
                paramCodedOutputStreamMicro.writeString(2, getActionName());
            }
            Iterator localIterator = getActionArgumentList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(3, (PumpkinTaggerResultsProto.ActionArgument) localIterator.next());
            }
            if (hasScore()) {
                paramCodedOutputStreamMicro.writeFloat(4, getScore());
            }
            if (hasTaggedHypothesis()) {
                paramCodedOutputStreamMicro.writeString(5, getTaggedHypothesis());
            }
        }
    }

    public static final class PumpkinTaggerResults
            extends MessageMicro {
        private int cachedSize = -1;
        private List<PumpkinTaggerResultsProto.HypothesisResult> hypothesis_ = Collections.emptyList();

        public static PumpkinTaggerResults parseFrom(byte[] paramArrayOfByte)
                throws InvalidProtocolBufferMicroException {
            return (PumpkinTaggerResults) new PumpkinTaggerResults().mergeFrom(paramArrayOfByte);
        }

        public PumpkinTaggerResults addHypothesis(PumpkinTaggerResultsProto.HypothesisResult paramHypothesisResult) {
            if (paramHypothesisResult == null) {
                throw new NullPointerException();
            }
            if (this.hypothesis_.isEmpty()) {
                this.hypothesis_ = new ArrayList();
            }
            this.hypothesis_.add(paramHypothesisResult);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public PumpkinTaggerResultsProto.HypothesisResult getHypothesis(int paramInt) {
            return (PumpkinTaggerResultsProto.HypothesisResult) this.hypothesis_.get(paramInt);
        }

        public int getHypothesisCount() {
            return this.hypothesis_.size();
        }

        public List<PumpkinTaggerResultsProto.HypothesisResult> getHypothesisList() {
            return this.hypothesis_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getHypothesisList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(1, (PumpkinTaggerResultsProto.HypothesisResult) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public PumpkinTaggerResults mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int tag = paramCodedInputStreamMicro.readTag();
                switch (tag) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, tag)) {
                            continue;
                        }
                    case 0:
                        return this;
                    case 10:
                        PumpkinTaggerResultsProto.HypothesisResult value = new PumpkinTaggerResultsProto.HypothesisResult();
                        paramCodedInputStreamMicro.readMessage(value);
                        addHypothesis(value);
                }
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getHypothesisList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(1, (PumpkinTaggerResultsProto.HypothesisResult) localIterator.next());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.grammar.pumpkin.PumpkinTaggerResultsProto

 * JD-Core Version:    0.7.0.1

 */