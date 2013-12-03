package com.google.speech.decoder.common;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Alignment {
    public static final class AlignmentProto
            extends MessageMicro {
        private float amCost_ = 0.0F;
        private int cachedSize = -1;
        private float frameDuration_ = 0.0F;
        private boolean hasAmCost;
        private boolean hasFrameDuration;
        private boolean hasItableChecksum;
        private boolean hasItableFilename;
        private boolean hasLmCost;
        private boolean hasOtableChecksum;
        private boolean hasOtableFilename;
        private boolean hasTotalCost;
        private boolean hasWordLabelPosition;
        private String itableChecksum_ = "";
        private String itableFilename_ = "";
        private float lmCost_ = 0.0F;
        private String otableChecksum_ = "";
        private String otableFilename_ = "";
        private List<Segment> segment_ = Collections.emptyList();
        private float totalCost_ = 0.0F;
        private int wordLabelPosition_ = 1;

        public AlignmentProto addSegment(Segment paramSegment) {
            if (paramSegment == null) {
                throw new NullPointerException();
            }
            if (this.segment_.isEmpty()) {
                this.segment_ = new ArrayList();
            }
            this.segment_.add(paramSegment);
            return this;
        }

        public float getAmCost() {
            return this.amCost_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public float getFrameDuration() {
            return this.frameDuration_;
        }

        public String getItableChecksum() {
            return this.itableChecksum_;
        }

        public String getItableFilename() {
            return this.itableFilename_;
        }

        public float getLmCost() {
            return this.lmCost_;
        }

        public String getOtableChecksum() {
            return this.otableChecksum_;
        }

        public String getOtableFilename() {
            return this.otableFilename_;
        }

        public List<Segment> getSegmentList() {
            return this.segment_;
        }

        public int getSerializedSize() {
            boolean bool = hasFrameDuration();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getFrameDuration());
            }
            if (hasTotalCost()) {
                i += CodedOutputStreamMicro.computeFloatSize(2, getTotalCost());
            }
            if (hasAmCost()) {
                i += CodedOutputStreamMicro.computeFloatSize(3, getAmCost());
            }
            if (hasLmCost()) {
                i += CodedOutputStreamMicro.computeFloatSize(4, getLmCost());
            }
            Iterator localIterator = getSegmentList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeGroupSize(5, (Segment) localIterator.next());
            }
            if (hasItableChecksum()) {
                i += CodedOutputStreamMicro.computeStringSize(17, getItableChecksum());
            }
            if (hasItableFilename()) {
                i += CodedOutputStreamMicro.computeStringSize(18, getItableFilename());
            }
            if (hasOtableChecksum()) {
                i += CodedOutputStreamMicro.computeStringSize(19, getOtableChecksum());
            }
            if (hasOtableFilename()) {
                i += CodedOutputStreamMicro.computeStringSize(20, getOtableFilename());
            }
            if (hasWordLabelPosition()) {
                i += CodedOutputStreamMicro.computeInt32Size(21, getWordLabelPosition());
            }
            this.cachedSize = i;
            return i;
        }

        public float getTotalCost() {
            return this.totalCost_;
        }

        public int getWordLabelPosition() {
            return this.wordLabelPosition_;
        }

        public boolean hasAmCost() {
            return this.hasAmCost;
        }

        public boolean hasFrameDuration() {
            return this.hasFrameDuration;
        }

        public boolean hasItableChecksum() {
            return this.hasItableChecksum;
        }

        public boolean hasItableFilename() {
            return this.hasItableFilename;
        }

        public boolean hasLmCost() {
            return this.hasLmCost;
        }

        public boolean hasOtableChecksum() {
            return this.hasOtableChecksum;
        }

        public boolean hasOtableFilename() {
            return this.hasOtableFilename;
        }

        public boolean hasTotalCost() {
            return this.hasTotalCost;
        }

        public boolean hasWordLabelPosition() {
            return this.hasWordLabelPosition;
        }

        public AlignmentProto mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                    case 13:
                        setFrameDuration(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 21:
                        setTotalCost(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 29:
                        setAmCost(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 37:
                        setLmCost(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 43:
                        Segment localSegment = new Segment();
                        paramCodedInputStreamMicro.readGroup(localSegment, 5);
                        addSegment(localSegment);
                        break;
                    case 138:
                        setItableChecksum(paramCodedInputStreamMicro.readString());
                        break;
                    case 146:
                        setItableFilename(paramCodedInputStreamMicro.readString());
                        break;
                    case 154:
                        setOtableChecksum(paramCodedInputStreamMicro.readString());
                        break;
                    case 162:
                        setOtableFilename(paramCodedInputStreamMicro.readString());
                        break;
                }
                setWordLabelPosition(paramCodedInputStreamMicro.readInt32());
            }
        }

        public AlignmentProto setAmCost(float paramFloat) {
            this.hasAmCost = true;
            this.amCost_ = paramFloat;
            return this;
        }

        public AlignmentProto setFrameDuration(float paramFloat) {
            this.hasFrameDuration = true;
            this.frameDuration_ = paramFloat;
            return this;
        }

        public AlignmentProto setItableChecksum(String paramString) {
            this.hasItableChecksum = true;
            this.itableChecksum_ = paramString;
            return this;
        }

        public AlignmentProto setItableFilename(String paramString) {
            this.hasItableFilename = true;
            this.itableFilename_ = paramString;
            return this;
        }

        public AlignmentProto setLmCost(float paramFloat) {
            this.hasLmCost = true;
            this.lmCost_ = paramFloat;
            return this;
        }

        public AlignmentProto setOtableChecksum(String paramString) {
            this.hasOtableChecksum = true;
            this.otableChecksum_ = paramString;
            return this;
        }

        public AlignmentProto setOtableFilename(String paramString) {
            this.hasOtableFilename = true;
            this.otableFilename_ = paramString;
            return this;
        }

        public AlignmentProto setTotalCost(float paramFloat) {
            this.hasTotalCost = true;
            this.totalCost_ = paramFloat;
            return this;
        }

        public AlignmentProto setWordLabelPosition(int paramInt) {
            this.hasWordLabelPosition = true;
            this.wordLabelPosition_ = paramInt;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasFrameDuration()) {
                paramCodedOutputStreamMicro.writeFloat(1, getFrameDuration());
            }
            if (hasTotalCost()) {
                paramCodedOutputStreamMicro.writeFloat(2, getTotalCost());
            }
            if (hasAmCost()) {
                paramCodedOutputStreamMicro.writeFloat(3, getAmCost());
            }
            if (hasLmCost()) {
                paramCodedOutputStreamMicro.writeFloat(4, getLmCost());
            }
            Iterator localIterator = getSegmentList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeGroup(5, (Segment) localIterator.next());
            }
            if (hasItableChecksum()) {
                paramCodedOutputStreamMicro.writeString(17, getItableChecksum());
            }
            if (hasItableFilename()) {
                paramCodedOutputStreamMicro.writeString(18, getItableFilename());
            }
            if (hasOtableChecksum()) {
                paramCodedOutputStreamMicro.writeString(19, getOtableChecksum());
            }
            if (hasOtableFilename()) {
                paramCodedOutputStreamMicro.writeString(20, getOtableFilename());
            }
            if (hasWordLabelPosition()) {
                paramCodedOutputStreamMicro.writeInt32(21, getWordLabelPosition());
            }
        }

        public static final class Segment
                extends MessageMicro {
            private float amCost_ = 0.0F;
            private int cachedSize = -1;
            private float confidence_ = 0.0F;
            private float cost_ = 0.0F;
            private long end_ = 0L;
            private boolean hasAmCost;
            private boolean hasConfidence;
            private boolean hasCost;
            private boolean hasEnd;
            private boolean hasIlabel;
            private boolean hasIlabelStr;
            private boolean hasLmCost;
            private boolean hasOlabel;
            private boolean hasOlabelStr;
            private boolean hasStart;
            private String ilabelStr_ = "";
            private long ilabel_ = 0L;
            private float lmCost_ = 0.0F;
            private String olabelStr_ = "";
            private long olabel_ = 0L;
            private long start_ = 0L;
            private List<Long> states_ = Collections.emptyList();

            public Segment addStates(long paramLong) {
                if (this.states_.isEmpty()) {
                    this.states_ = new ArrayList();
                }
                this.states_.add(Long.valueOf(paramLong));
                return this;
            }

            public float getAmCost() {
                return this.amCost_;
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

            public float getCost() {
                return this.cost_;
            }

            public long getEnd() {
                return this.end_;
            }

            public long getIlabel() {
                return this.ilabel_;
            }

            public String getIlabelStr() {
                return this.ilabelStr_;
            }

            public float getLmCost() {
                return this.lmCost_;
            }

            public long getOlabel() {
                return this.olabel_;
            }

            public String getOlabelStr() {
                return this.olabelStr_;
            }

            public int getSerializedSize() {
                boolean bool = hasStart();
                int i = 0;
                if (bool) {
                    i = 0 + CodedOutputStreamMicro.computeInt64Size(6, getStart());
                }
                if (hasEnd()) {
                    i += CodedOutputStreamMicro.computeInt64Size(7, getEnd());
                }
                if (hasCost()) {
                    i += CodedOutputStreamMicro.computeFloatSize(8, getCost());
                }
                if (hasAmCost()) {
                    i += CodedOutputStreamMicro.computeFloatSize(9, getAmCost());
                }
                if (hasLmCost()) {
                    i += CodedOutputStreamMicro.computeFloatSize(10, getLmCost());
                }
                if (hasIlabel()) {
                    i += CodedOutputStreamMicro.computeInt64Size(11, getIlabel());
                }
                if (hasOlabel()) {
                    i += CodedOutputStreamMicro.computeInt64Size(12, getOlabel());
                }
                if (hasIlabelStr()) {
                    i += CodedOutputStreamMicro.computeStringSize(13, getIlabelStr());
                }
                if (hasOlabelStr()) {
                    i += CodedOutputStreamMicro.computeStringSize(14, getOlabelStr());
                }
                int j = 0;
                Iterator localIterator = getStatesList().iterator();
                while (localIterator.hasNext()) {
                    j += CodedOutputStreamMicro.computeInt64SizeNoTag(((Long) localIterator.next()).longValue());
                }
                int k = i + j + 1 * getStatesList().size();
                if (hasConfidence()) {
                    k += CodedOutputStreamMicro.computeFloatSize(16, getConfidence());
                }
                this.cachedSize = k;
                return k;
            }

            public long getStart() {
                return this.start_;
            }

            public List<Long> getStatesList() {
                return this.states_;
            }

            public boolean hasAmCost() {
                return this.hasAmCost;
            }

            public boolean hasConfidence() {
                return this.hasConfidence;
            }

            public boolean hasCost() {
                return this.hasCost;
            }

            public boolean hasEnd() {
                return this.hasEnd;
            }

            public boolean hasIlabel() {
                return this.hasIlabel;
            }

            public boolean hasIlabelStr() {
                return this.hasIlabelStr;
            }

            public boolean hasLmCost() {
                return this.hasLmCost;
            }

            public boolean hasOlabel() {
                return this.hasOlabel;
            }

            public boolean hasOlabelStr() {
                return this.hasOlabelStr;
            }

            public boolean hasStart() {
                return this.hasStart;
            }

            public Segment mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        case 48:
                            setStart(paramCodedInputStreamMicro.readInt64());
                            break;
                        case 56:
                            setEnd(paramCodedInputStreamMicro.readInt64());
                            break;
                        case 69:
                            setCost(paramCodedInputStreamMicro.readFloat());
                            break;
                        case 77:
                            setAmCost(paramCodedInputStreamMicro.readFloat());
                            break;
                        case 85:
                            setLmCost(paramCodedInputStreamMicro.readFloat());
                            break;
                        case 88:
                            setIlabel(paramCodedInputStreamMicro.readInt64());
                            break;
                        case 96:
                            setOlabel(paramCodedInputStreamMicro.readInt64());
                            break;
                        case 106:
                            setIlabelStr(paramCodedInputStreamMicro.readString());
                            break;
                        case 114:
                            setOlabelStr(paramCodedInputStreamMicro.readString());
                            break;
                        case 120:
                            addStates(paramCodedInputStreamMicro.readInt64());
                            break;
                    }
                    setConfidence(paramCodedInputStreamMicro.readFloat());
                }
            }

            public Segment setAmCost(float paramFloat) {
                this.hasAmCost = true;
                this.amCost_ = paramFloat;
                return this;
            }

            public Segment setConfidence(float paramFloat) {
                this.hasConfidence = true;
                this.confidence_ = paramFloat;
                return this;
            }

            public Segment setCost(float paramFloat) {
                this.hasCost = true;
                this.cost_ = paramFloat;
                return this;
            }

            public Segment setEnd(long paramLong) {
                this.hasEnd = true;
                this.end_ = paramLong;
                return this;
            }

            public Segment setIlabel(long paramLong) {
                this.hasIlabel = true;
                this.ilabel_ = paramLong;
                return this;
            }

            public Segment setIlabelStr(String paramString) {
                this.hasIlabelStr = true;
                this.ilabelStr_ = paramString;
                return this;
            }

            public Segment setLmCost(float paramFloat) {
                this.hasLmCost = true;
                this.lmCost_ = paramFloat;
                return this;
            }

            public Segment setOlabel(long paramLong) {
                this.hasOlabel = true;
                this.olabel_ = paramLong;
                return this;
            }

            public Segment setOlabelStr(String paramString) {
                this.hasOlabelStr = true;
                this.olabelStr_ = paramString;
                return this;
            }

            public Segment setStart(long paramLong) {
                this.hasStart = true;
                this.start_ = paramLong;
                return this;
            }

            public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasStart()) {
                    paramCodedOutputStreamMicro.writeInt64(6, getStart());
                }
                if (hasEnd()) {
                    paramCodedOutputStreamMicro.writeInt64(7, getEnd());
                }
                if (hasCost()) {
                    paramCodedOutputStreamMicro.writeFloat(8, getCost());
                }
                if (hasAmCost()) {
                    paramCodedOutputStreamMicro.writeFloat(9, getAmCost());
                }
                if (hasLmCost()) {
                    paramCodedOutputStreamMicro.writeFloat(10, getLmCost());
                }
                if (hasIlabel()) {
                    paramCodedOutputStreamMicro.writeInt64(11, getIlabel());
                }
                if (hasOlabel()) {
                    paramCodedOutputStreamMicro.writeInt64(12, getOlabel());
                }
                if (hasIlabelStr()) {
                    paramCodedOutputStreamMicro.writeString(13, getIlabelStr());
                }
                if (hasOlabelStr()) {
                    paramCodedOutputStreamMicro.writeString(14, getOlabelStr());
                }
                Iterator localIterator = getStatesList().iterator();
                while (localIterator.hasNext()) {
                    paramCodedOutputStreamMicro.writeInt64(15, ((Long) localIterator.next()).longValue());
                }
                if (hasConfidence()) {
                    paramCodedOutputStreamMicro.writeFloat(16, getConfidence());
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.decoder.common.Alignment

 * JD-Core Version:    0.7.0.1

 */