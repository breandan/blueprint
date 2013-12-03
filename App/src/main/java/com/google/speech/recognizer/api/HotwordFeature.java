package com.google.speech.recognizer.api;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class HotwordFeature {
    public static final class HotwordConfidenceFeature
            extends MessageMicro {
        private float amScore_ = 0.0F;
        private float ascoreBest_ = 0.0F;
        private float ascoreMean_ = 0.0F;
        private float ascoreMeandiff_ = 0.0F;
        private float ascoreStddev_ = 0.0F;
        private float ascoreWorst_ = 0.0F;
        private int cachedSize = -1;
        private float durScore_ = 0.0F;
        private List<Float> frameDistance_ = Collections.emptyList();
        private boolean hasAmScore;
        private boolean hasAscoreBest;
        private boolean hasAscoreMean;
        private boolean hasAscoreMeandiff;
        private boolean hasAscoreStddev;
        private boolean hasAscoreWorst;
        private boolean hasDurScore;
        private boolean hasLmScore;
        private boolean hasNormalizedWordDuration;
        private boolean hasNumPhones;
        private boolean hasPhoneDurationScore;
        private boolean hasSpeakerRate;
        private boolean hasStability;
        private boolean hasStartFrame;
        private boolean hasWordDurationFrames;
        private float lmScore_ = 0.0F;
        private float normalizedWordDuration_ = 0.0F;
        private float numPhones_ = 0.0F;
        private List<Boolean> phAlignDelete_ = Collections.emptyList();
        private List<Boolean> phAlignInsert_ = Collections.emptyList();
        private List<Boolean> phAlign_ = Collections.emptyList();
        private List<Boolean> phExpectationAlign_ = Collections.emptyList();
        private List<Boolean> phExpectationDeleteAlign_ = Collections.emptyList();
        private List<Boolean> phExpectationDeleteNn_ = Collections.emptyList();
        private List<Boolean> phExpectationInsertAlign_ = Collections.emptyList();
        private List<Boolean> phExpectationInsertNn_ = Collections.emptyList();
        private List<Boolean> phExpectationNn_ = Collections.emptyList();
        private List<Boolean> phNnDelete_ = Collections.emptyList();
        private List<Boolean> phNnInsert_ = Collections.emptyList();
        private List<Boolean> phNn_ = Collections.emptyList();
        private float phoneDurationScore_ = 0.0F;
        private float speakerRate_ = 0.0F;
        private float stability_ = 0.0F;
        private float startFrame_ = 0.0F;
        private float wordDurationFrames_ = 0.0F;

        public HotwordConfidenceFeature addFrameDistance(float paramFloat) {
            if (this.frameDistance_.isEmpty()) {
                this.frameDistance_ = new ArrayList();
            }
            this.frameDistance_.add(Float.valueOf(paramFloat));
            return this;
        }

        public HotwordConfidenceFeature addPhAlign(boolean paramBoolean) {
            if (this.phAlign_.isEmpty()) {
                this.phAlign_ = new ArrayList();
            }
            this.phAlign_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhAlignDelete(boolean paramBoolean) {
            if (this.phAlignDelete_.isEmpty()) {
                this.phAlignDelete_ = new ArrayList();
            }
            this.phAlignDelete_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhAlignInsert(boolean paramBoolean) {
            if (this.phAlignInsert_.isEmpty()) {
                this.phAlignInsert_ = new ArrayList();
            }
            this.phAlignInsert_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationAlign(boolean paramBoolean) {
            if (this.phExpectationAlign_.isEmpty()) {
                this.phExpectationAlign_ = new ArrayList();
            }
            this.phExpectationAlign_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationDeleteAlign(boolean paramBoolean) {
            if (this.phExpectationDeleteAlign_.isEmpty()) {
                this.phExpectationDeleteAlign_ = new ArrayList();
            }
            this.phExpectationDeleteAlign_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationDeleteNn(boolean paramBoolean) {
            if (this.phExpectationDeleteNn_.isEmpty()) {
                this.phExpectationDeleteNn_ = new ArrayList();
            }
            this.phExpectationDeleteNn_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationInsertAlign(boolean paramBoolean) {
            if (this.phExpectationInsertAlign_.isEmpty()) {
                this.phExpectationInsertAlign_ = new ArrayList();
            }
            this.phExpectationInsertAlign_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationInsertNn(boolean paramBoolean) {
            if (this.phExpectationInsertNn_.isEmpty()) {
                this.phExpectationInsertNn_ = new ArrayList();
            }
            this.phExpectationInsertNn_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhExpectationNn(boolean paramBoolean) {
            if (this.phExpectationNn_.isEmpty()) {
                this.phExpectationNn_ = new ArrayList();
            }
            this.phExpectationNn_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhNn(boolean paramBoolean) {
            if (this.phNn_.isEmpty()) {
                this.phNn_ = new ArrayList();
            }
            this.phNn_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhNnDelete(boolean paramBoolean) {
            if (this.phNnDelete_.isEmpty()) {
                this.phNnDelete_ = new ArrayList();
            }
            this.phNnDelete_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public HotwordConfidenceFeature addPhNnInsert(boolean paramBoolean) {
            if (this.phNnInsert_.isEmpty()) {
                this.phNnInsert_ = new ArrayList();
            }
            this.phNnInsert_.add(Boolean.valueOf(paramBoolean));
            return this;
        }

        public float getAmScore() {
            return this.amScore_;
        }

        public float getAscoreBest() {
            return this.ascoreBest_;
        }

        public float getAscoreMean() {
            return this.ascoreMean_;
        }

        public float getAscoreMeandiff() {
            return this.ascoreMeandiff_;
        }

        public float getAscoreStddev() {
            return this.ascoreStddev_;
        }

        public float getAscoreWorst() {
            return this.ascoreWorst_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public float getDurScore() {
            return this.durScore_;
        }

        public List<Float> getFrameDistanceList() {
            return this.frameDistance_;
        }

        public float getLmScore() {
            return this.lmScore_;
        }

        public float getNormalizedWordDuration() {
            return this.normalizedWordDuration_;
        }

        public float getNumPhones() {
            return this.numPhones_;
        }

        public List<Boolean> getPhAlignDeleteList() {
            return this.phAlignDelete_;
        }

        public List<Boolean> getPhAlignInsertList() {
            return this.phAlignInsert_;
        }

        public List<Boolean> getPhAlignList() {
            return this.phAlign_;
        }

        public List<Boolean> getPhExpectationAlignList() {
            return this.phExpectationAlign_;
        }

        public List<Boolean> getPhExpectationDeleteAlignList() {
            return this.phExpectationDeleteAlign_;
        }

        public List<Boolean> getPhExpectationDeleteNnList() {
            return this.phExpectationDeleteNn_;
        }

        public List<Boolean> getPhExpectationInsertAlignList() {
            return this.phExpectationInsertAlign_;
        }

        public List<Boolean> getPhExpectationInsertNnList() {
            return this.phExpectationInsertNn_;
        }

        public List<Boolean> getPhExpectationNnList() {
            return this.phExpectationNn_;
        }

        public List<Boolean> getPhNnDeleteList() {
            return this.phNnDelete_;
        }

        public List<Boolean> getPhNnInsertList() {
            return this.phNnInsert_;
        }

        public List<Boolean> getPhNnList() {
            return this.phNn_;
        }

        public float getPhoneDurationScore() {
            return this.phoneDurationScore_;
        }

        public int getSerializedSize() {
            boolean bool = hasPhoneDurationScore();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getPhoneDurationScore());
            }
            if (hasSpeakerRate()) {
                i += CodedOutputStreamMicro.computeFloatSize(2, getSpeakerRate());
            }
            int j = i + 4 * getFrameDistanceList().size() + 1 * getFrameDistanceList().size();
            if (hasWordDurationFrames()) {
                j += CodedOutputStreamMicro.computeFloatSize(4, getWordDurationFrames());
            }
            if (hasNumPhones()) {
                j += CodedOutputStreamMicro.computeFloatSize(8, getNumPhones());
            }
            if (hasNormalizedWordDuration()) {
                j += CodedOutputStreamMicro.computeFloatSize(9, getNormalizedWordDuration());
            }
            if (hasAscoreMean()) {
                j += CodedOutputStreamMicro.computeFloatSize(10, getAscoreMean());
            }
            if (hasAscoreStddev()) {
                j += CodedOutputStreamMicro.computeFloatSize(11, getAscoreStddev());
            }
            if (hasAscoreWorst()) {
                j += CodedOutputStreamMicro.computeFloatSize(12, getAscoreWorst());
            }
            if (hasAscoreMeandiff()) {
                j += CodedOutputStreamMicro.computeFloatSize(13, getAscoreMeandiff());
            }
            if (hasAscoreBest()) {
                j += CodedOutputStreamMicro.computeFloatSize(14, getAscoreBest());
            }
            if (hasLmScore()) {
                j += CodedOutputStreamMicro.computeFloatSize(15, getLmScore());
            }
            if (hasDurScore()) {
                j += CodedOutputStreamMicro.computeFloatSize(16, getDurScore());
            }
            if (hasAmScore()) {
                j += CodedOutputStreamMicro.computeFloatSize(17, getAmScore());
            }
            if (hasStartFrame()) {
                j += CodedOutputStreamMicro.computeFloatSize(18, getStartFrame());
            }
            int k = j + 1 * getPhExpectationAlignList().size() + 2 * getPhExpectationAlignList().size() + 1 * getPhExpectationNnList().size() + 2 * getPhExpectationNnList().size() + 1 * getPhExpectationDeleteAlignList().size() + 2 * getPhExpectationDeleteAlignList().size() + 1 * getPhExpectationInsertAlignList().size() + 2 * getPhExpectationInsertAlignList().size();
            if (hasStability()) {
                k += CodedOutputStreamMicro.computeFloatSize(23, getStability());
            }
            int m = k + 1 * getPhExpectationDeleteNnList().size() + 2 * getPhExpectationDeleteNnList().size() + 1 * getPhExpectationInsertNnList().size() + 2 * getPhExpectationInsertNnList().size() + 1 * getPhAlignList().size() + 2 * getPhAlignList().size() + 1 * getPhAlignDeleteList().size() + 2 * getPhAlignDeleteList().size() + 1 * getPhAlignInsertList().size() + 2 * getPhAlignInsertList().size() + 1 * getPhNnList().size() + 2 * getPhNnList().size() + 1 * getPhNnDeleteList().size() + 2 * getPhNnDeleteList().size() + 1 * getPhNnInsertList().size() + 2 * getPhNnInsertList().size();
            this.cachedSize = m;
            return m;
        }

        public float getSpeakerRate() {
            return this.speakerRate_;
        }

        public float getStability() {
            return this.stability_;
        }

        public float getStartFrame() {
            return this.startFrame_;
        }

        public float getWordDurationFrames() {
            return this.wordDurationFrames_;
        }

        public boolean hasAmScore() {
            return this.hasAmScore;
        }

        public boolean hasAscoreBest() {
            return this.hasAscoreBest;
        }

        public boolean hasAscoreMean() {
            return this.hasAscoreMean;
        }

        public boolean hasAscoreMeandiff() {
            return this.hasAscoreMeandiff;
        }

        public boolean hasAscoreStddev() {
            return this.hasAscoreStddev;
        }

        public boolean hasAscoreWorst() {
            return this.hasAscoreWorst;
        }

        public boolean hasDurScore() {
            return this.hasDurScore;
        }

        public boolean hasLmScore() {
            return this.hasLmScore;
        }

        public boolean hasNormalizedWordDuration() {
            return this.hasNormalizedWordDuration;
        }

        public boolean hasNumPhones() {
            return this.hasNumPhones;
        }

        public boolean hasPhoneDurationScore() {
            return this.hasPhoneDurationScore;
        }

        public boolean hasSpeakerRate() {
            return this.hasSpeakerRate;
        }

        public boolean hasStability() {
            return this.hasStability;
        }

        public boolean hasStartFrame() {
            return this.hasStartFrame;
        }

        public boolean hasWordDurationFrames() {
            return this.hasWordDurationFrames;
        }

        public HotwordConfidenceFeature mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setPhoneDurationScore(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 21:
                        setSpeakerRate(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 29:
                        addFrameDistance(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 37:
                        setWordDurationFrames(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 69:
                        setNumPhones(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 77:
                        setNormalizedWordDuration(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 85:
                        setAscoreMean(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 93:
                        setAscoreStddev(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 101:
                        setAscoreWorst(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 109:
                        setAscoreMeandiff(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 117:
                        setAscoreBest(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 125:
                        setLmScore(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 133:
                        setDurScore(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 141:
                        setAmScore(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 149:
                        setStartFrame(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 152:
                        addPhExpectationAlign(paramCodedInputStreamMicro.readBool());
                        break;
                    case 160:
                        addPhExpectationNn(paramCodedInputStreamMicro.readBool());
                        break;
                    case 168:
                        addPhExpectationDeleteAlign(paramCodedInputStreamMicro.readBool());
                        break;
                    case 176:
                        addPhExpectationInsertAlign(paramCodedInputStreamMicro.readBool());
                        break;
                    case 189:
                        setStability(paramCodedInputStreamMicro.readFloat());
                        break;
                    case 192:
                        addPhExpectationDeleteNn(paramCodedInputStreamMicro.readBool());
                        break;
                    case 200:
                        addPhExpectationInsertNn(paramCodedInputStreamMicro.readBool());
                        break;
                    case 208:
                        addPhAlign(paramCodedInputStreamMicro.readBool());
                        break;
                    case 216:
                        addPhAlignDelete(paramCodedInputStreamMicro.readBool());
                        break;
                    case 224:
                        addPhAlignInsert(paramCodedInputStreamMicro.readBool());
                        break;
                    case 232:
                        addPhNn(paramCodedInputStreamMicro.readBool());
                        break;
                    case 240:
                        addPhNnDelete(paramCodedInputStreamMicro.readBool());
                        break;
                }
                addPhNnInsert(paramCodedInputStreamMicro.readBool());
            }
        }

        public HotwordConfidenceFeature setAmScore(float paramFloat) {
            this.hasAmScore = true;
            this.amScore_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setAscoreBest(float paramFloat) {
            this.hasAscoreBest = true;
            this.ascoreBest_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setAscoreMean(float paramFloat) {
            this.hasAscoreMean = true;
            this.ascoreMean_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setAscoreMeandiff(float paramFloat) {
            this.hasAscoreMeandiff = true;
            this.ascoreMeandiff_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setAscoreStddev(float paramFloat) {
            this.hasAscoreStddev = true;
            this.ascoreStddev_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setAscoreWorst(float paramFloat) {
            this.hasAscoreWorst = true;
            this.ascoreWorst_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setDurScore(float paramFloat) {
            this.hasDurScore = true;
            this.durScore_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setLmScore(float paramFloat) {
            this.hasLmScore = true;
            this.lmScore_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setNormalizedWordDuration(float paramFloat) {
            this.hasNormalizedWordDuration = true;
            this.normalizedWordDuration_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setNumPhones(float paramFloat) {
            this.hasNumPhones = true;
            this.numPhones_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setPhoneDurationScore(float paramFloat) {
            this.hasPhoneDurationScore = true;
            this.phoneDurationScore_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setSpeakerRate(float paramFloat) {
            this.hasSpeakerRate = true;
            this.speakerRate_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setStability(float paramFloat) {
            this.hasStability = true;
            this.stability_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setStartFrame(float paramFloat) {
            this.hasStartFrame = true;
            this.startFrame_ = paramFloat;
            return this;
        }

        public HotwordConfidenceFeature setWordDurationFrames(float paramFloat) {
            this.hasWordDurationFrames = true;
            this.wordDurationFrames_ = paramFloat;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasPhoneDurationScore()) {
                paramCodedOutputStreamMicro.writeFloat(1, getPhoneDurationScore());
            }
            if (hasSpeakerRate()) {
                paramCodedOutputStreamMicro.writeFloat(2, getSpeakerRate());
            }
            Iterator localIterator1 = getFrameDistanceList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeFloat(3, ((Float) localIterator1.next()).floatValue());
            }
            if (hasWordDurationFrames()) {
                paramCodedOutputStreamMicro.writeFloat(4, getWordDurationFrames());
            }
            if (hasNumPhones()) {
                paramCodedOutputStreamMicro.writeFloat(8, getNumPhones());
            }
            if (hasNormalizedWordDuration()) {
                paramCodedOutputStreamMicro.writeFloat(9, getNormalizedWordDuration());
            }
            if (hasAscoreMean()) {
                paramCodedOutputStreamMicro.writeFloat(10, getAscoreMean());
            }
            if (hasAscoreStddev()) {
                paramCodedOutputStreamMicro.writeFloat(11, getAscoreStddev());
            }
            if (hasAscoreWorst()) {
                paramCodedOutputStreamMicro.writeFloat(12, getAscoreWorst());
            }
            if (hasAscoreMeandiff()) {
                paramCodedOutputStreamMicro.writeFloat(13, getAscoreMeandiff());
            }
            if (hasAscoreBest()) {
                paramCodedOutputStreamMicro.writeFloat(14, getAscoreBest());
            }
            if (hasLmScore()) {
                paramCodedOutputStreamMicro.writeFloat(15, getLmScore());
            }
            if (hasDurScore()) {
                paramCodedOutputStreamMicro.writeFloat(16, getDurScore());
            }
            if (hasAmScore()) {
                paramCodedOutputStreamMicro.writeFloat(17, getAmScore());
            }
            if (hasStartFrame()) {
                paramCodedOutputStreamMicro.writeFloat(18, getStartFrame());
            }
            Iterator localIterator2 = getPhExpectationAlignList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(19, ((Boolean) localIterator2.next()).booleanValue());
            }
            Iterator localIterator3 = getPhExpectationNnList().iterator();
            while (localIterator3.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(20, ((Boolean) localIterator3.next()).booleanValue());
            }
            Iterator localIterator4 = getPhExpectationDeleteAlignList().iterator();
            while (localIterator4.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(21, ((Boolean) localIterator4.next()).booleanValue());
            }
            Iterator localIterator5 = getPhExpectationInsertAlignList().iterator();
            while (localIterator5.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(22, ((Boolean) localIterator5.next()).booleanValue());
            }
            if (hasStability()) {
                paramCodedOutputStreamMicro.writeFloat(23, getStability());
            }
            Iterator localIterator6 = getPhExpectationDeleteNnList().iterator();
            while (localIterator6.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(24, ((Boolean) localIterator6.next()).booleanValue());
            }
            Iterator localIterator7 = getPhExpectationInsertNnList().iterator();
            while (localIterator7.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(25, ((Boolean) localIterator7.next()).booleanValue());
            }
            Iterator localIterator8 = getPhAlignList().iterator();
            while (localIterator8.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(26, ((Boolean) localIterator8.next()).booleanValue());
            }
            Iterator localIterator9 = getPhAlignDeleteList().iterator();
            while (localIterator9.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(27, ((Boolean) localIterator9.next()).booleanValue());
            }
            Iterator localIterator10 = getPhAlignInsertList().iterator();
            while (localIterator10.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(28, ((Boolean) localIterator10.next()).booleanValue());
            }
            Iterator localIterator11 = getPhNnList().iterator();
            while (localIterator11.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(29, ((Boolean) localIterator11.next()).booleanValue());
            }
            Iterator localIterator12 = getPhNnDeleteList().iterator();
            while (localIterator12.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(30, ((Boolean) localIterator12.next()).booleanValue());
            }
            Iterator localIterator13 = getPhNnInsertList().iterator();
            while (localIterator13.hasNext()) {
                paramCodedOutputStreamMicro.writeBool(31, ((Boolean) localIterator13.next()).booleanValue());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.recognizer.api.HotwordFeature

 * JD-Core Version:    0.7.0.1

 */