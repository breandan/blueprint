package com.embryo.speech.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Alternates {
    public static final class Alternate
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private float confidence_ = 0.0F;
        private boolean hasConfidence;
        private boolean hasText;
        private String text_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public float getConfidence() {
            return this.confidence_;
        }

        public int getSerializedSize() {
            boolean bool = hasText();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSize(1, getText());
            }
            if (hasConfidence()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeFloatSize(2, getConfidence());
            }
            this.cachedSize = i;
            return i;
        }

        public String getText() {
            return this.text_;
        }

        public boolean hasConfidence() {
            return this.hasConfidence;
        }

        public boolean hasText() {
            return this.hasText;
        }

        public Alternate mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setText(paramCodedInputStreamMicro.readString());
                        break;
                }
                setConfidence(paramCodedInputStreamMicro.readFloat());
            }
        }

        public Alternate setConfidence(float paramFloat) {
            this.hasConfidence = true;
            this.confidence_ = paramFloat;
            return this;
        }

        public Alternate setText(String paramString) {
            this.hasText = true;
            this.text_ = paramString;
            return this;
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasText()) {
                paramCodedOutputStreamMicro.writeString(1, getText());
            }
            if (hasConfidence()) {
                paramCodedOutputStreamMicro.writeFloat(2, getConfidence());
            }
        }
    }

    public static final class AlternateParams
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasMaxSpanLength;
        private boolean hasMaxTotalSpanLength;
        private boolean hasUnit;
        private int maxSpanLength_ = 0;
        private int maxTotalSpanLength_ = 0;
        private int unit_ = 0;

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getMaxSpanLength() {
            return this.maxSpanLength_;
        }

        public int getMaxTotalSpanLength() {
            return this.maxTotalSpanLength_;
        }

        public int getSerializedSize() {
            boolean bool = hasMaxSpanLength();
            int i = 0;
            if (bool) {
                i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getMaxSpanLength());
            }
            if (hasMaxTotalSpanLength()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getMaxTotalSpanLength());
            }
            if (hasUnit()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getUnit());
            }
            this.cachedSize = i;
            return i;
        }

        public int getUnit() {
            return this.unit_;
        }

        public boolean hasMaxSpanLength() {
            return this.hasMaxSpanLength;
        }

        public boolean hasMaxTotalSpanLength() {
            return this.hasMaxTotalSpanLength;
        }

        public boolean hasUnit() {
            return this.hasUnit;
        }

        public AlternateParams mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                setUnit(paramCodedInputStreamMicro.readInt32());
            }
        }

        public AlternateParams setMaxSpanLength(int paramInt) {
            this.hasMaxSpanLength = true;
            this.maxSpanLength_ = paramInt;
            return this;
        }

        public AlternateParams setMaxTotalSpanLength(int paramInt) {
            this.hasMaxTotalSpanLength = true;
            this.maxTotalSpanLength_ = paramInt;
            return this;
        }

        public AlternateParams setUnit(int paramInt) {
            this.hasUnit = true;
            this.unit_ = paramInt;
            return this;
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasMaxSpanLength()) {
                paramCodedOutputStreamMicro.writeInt32(1, getMaxSpanLength());
            }
            if (hasMaxTotalSpanLength()) {
                paramCodedOutputStreamMicro.writeInt32(2, getMaxTotalSpanLength());
            }
            if (hasUnit()) {
                paramCodedOutputStreamMicro.writeInt32(3, getUnit());
            }
        }
    }

    public static final class AlternateSpan
            extends com.embryo.protobuf.micro.MessageMicro {
        private List<Alternates.Alternate> alternates_ = Collections.emptyList();
        private int cachedSize = -1;
        private float confidence_ = 0.0F;
        private boolean hasConfidence;
        private boolean hasLength;
        private boolean hasStart;
        private int length_ = 0;
        private int start_ = 0;

        public AlternateSpan addAlternates(Alternates.Alternate paramAlternate) {
            if (paramAlternate == null) {
                throw new NullPointerException();
            }
            if (this.alternates_.isEmpty()) {
                this.alternates_ = new ArrayList();
            }
            this.alternates_.add(paramAlternate);
            return this;
        }

        public Alternates.Alternate getAlternates(int paramInt) {
            return (Alternates.Alternate) this.alternates_.get(paramInt);
        }

        public int getAlternatesCount() {
            return this.alternates_.size();
        }

        public List<Alternates.Alternate> getAlternatesList() {
            return this.alternates_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public float getConfidence() {
            return this.confidence_;
        }

        public int getLength() {
            return this.length_;
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
            Iterator localIterator = getAlternatesList().iterator();
            while (localIterator.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(3, (Alternates.Alternate) localIterator.next());
            }
            if (hasConfidence()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeFloatSize(4, getConfidence());
            }
            this.cachedSize = i;
            return i;
        }

        public int getStart() {
            return this.start_;
        }

        public boolean hasConfidence() {
            return this.hasConfidence;
        }

        public boolean hasLength() {
            return this.hasLength;
        }

        public boolean hasStart() {
            return this.hasStart;
        }

        public AlternateSpan mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        Alternates.Alternate localAlternate = new Alternates.Alternate();
                        paramCodedInputStreamMicro.readMessage(localAlternate);
                        addAlternates(localAlternate);
                        break;
                }
                setConfidence(paramCodedInputStreamMicro.readFloat());
            }
        }

        public AlternateSpan setConfidence(float paramFloat) {
            this.hasConfidence = true;
            this.confidence_ = paramFloat;
            return this;
        }

        public AlternateSpan setLength(int paramInt) {
            this.hasLength = true;
            this.length_ = paramInt;
            return this;
        }

        public AlternateSpan setStart(int paramInt) {
            this.hasStart = true;
            this.start_ = paramInt;
            return this;
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasStart()) {
                paramCodedOutputStreamMicro.writeInt32(1, getStart());
            }
            if (hasLength()) {
                paramCodedOutputStreamMicro.writeInt32(2, getLength());
            }
            Iterator localIterator = getAlternatesList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(3, (Alternates.Alternate) localIterator.next());
            }
            if (hasConfidence()) {
                paramCodedOutputStreamMicro.writeFloat(4, getConfidence());
            }
        }
    }

    public static final class RecognitionClientAlternates
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private boolean hasMaxSpanLength;
        private boolean hasUnit;
        private int maxSpanLength_ = 0;
        private List<Alternates.AlternateSpan> span_ = Collections.emptyList();
        private int unit_ = 0;

        public RecognitionClientAlternates addSpan(Alternates.AlternateSpan paramAlternateSpan) {
            if (paramAlternateSpan == null) {
                throw new NullPointerException();
            }
            if (this.span_.isEmpty()) {
                this.span_ = new ArrayList();
            }
            this.span_.add(paramAlternateSpan);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getMaxSpanLength() {
            return this.maxSpanLength_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getSpanList().iterator();
            while (localIterator.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(1, (Alternates.AlternateSpan) localIterator.next());
            }
            if (hasMaxSpanLength()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getMaxSpanLength());
            }
            if (hasUnit()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(3, getUnit());
            }
            this.cachedSize = i;
            return i;
        }

        public List<Alternates.AlternateSpan> getSpanList() {
            return this.span_;
        }

        public int getUnit() {
            return this.unit_;
        }

        public boolean hasMaxSpanLength() {
            return this.hasMaxSpanLength;
        }

        public boolean hasUnit() {
            return this.hasUnit;
        }

        public RecognitionClientAlternates mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        Alternates.AlternateSpan localAlternateSpan = new Alternates.AlternateSpan();
                        paramCodedInputStreamMicro.readMessage(localAlternateSpan);
                        addSpan(localAlternateSpan);
                        break;
                    case 16:
                        setMaxSpanLength(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setUnit(paramCodedInputStreamMicro.readInt32());
            }
        }

        public RecognitionClientAlternates setMaxSpanLength(int paramInt) {
            this.hasMaxSpanLength = true;
            this.maxSpanLength_ = paramInt;
            return this;
        }

        public RecognitionClientAlternates setUnit(int paramInt) {
            this.hasUnit = true;
            this.unit_ = paramInt;
            return this;
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getSpanList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(1, (Alternates.AlternateSpan) localIterator.next());
            }
            if (hasMaxSpanLength()) {
                paramCodedOutputStreamMicro.writeInt32(2, getMaxSpanLength());
            }
            if (hasUnit()) {
                paramCodedOutputStreamMicro.writeInt32(3, getUnit());
            }
        }
    }
}
