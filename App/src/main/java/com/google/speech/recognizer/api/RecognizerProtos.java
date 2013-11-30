package com.google.speech.recognizer.api;

import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.common.Alternates;
import com.google.speech.common.Alternates.RecognitionClientAlternates;
import com.google.speech.decoder.common.Alignment;
import com.google.speech.decoder.common.Alignment.AlignmentProto;
import com.google.speech.decoder.confidence.ConfFeature;
import com.google.speech.decoder.confidence.ConfFeature.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import speech.InterpretationProto;
import speech.InterpretationProto.Interpretation;

public final class RecognizerProtos
{
  public static final class AudioLevelEvent
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasLevel;
    private boolean hasTimeUsec;
    private float level_ = 0.0F;
    private long timeUsec_ = 0L;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getLevel()
    {
      return this.level_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLevel();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getLevel());
      }
      if (hasTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getTimeUsec());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getTimeUsec()
    {
      return this.timeUsec_;
    }
    
    public boolean hasLevel()
    {
      return this.hasLevel;
    }
    
    public boolean hasTimeUsec()
    {
      return this.hasTimeUsec;
    }
    
    public AudioLevelEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 13: 
          setLevel(paramCodedInputStreamMicro.readFloat());
          break;
        }
        setTimeUsec(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public AudioLevelEvent setLevel(float paramFloat)
    {
      this.hasLevel = true;
      this.level_ = paramFloat;
      return this;
    }
    
    public AudioLevelEvent setTimeUsec(long paramLong)
    {
      this.hasTimeUsec = true;
      this.timeUsec_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLevel()) {
        paramCodedOutputStreamMicro.writeFloat(1, getLevel());
      }
      if (hasTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(2, getTimeUsec());
      }
    }
  }
  
  public static final class EndpointerEvent
    extends MessageMicro
  {
    private int cachedSize = -1;
    private int eventType_ = 0;
    private boolean hasEventType;
    private boolean hasTimeUsec;
    private long timeUsec_ = 0L;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getEventType()
    {
      return this.eventType_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasEventType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getEventType());
      }
      if (hasTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getTimeUsec());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getTimeUsec()
    {
      return this.timeUsec_;
    }
    
    public boolean hasEventType()
    {
      return this.hasEventType;
    }
    
    public boolean hasTimeUsec()
    {
      return this.hasTimeUsec;
    }
    
    public EndpointerEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setEventType(paramCodedInputStreamMicro.readInt32());
          break;
        }
        setTimeUsec(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public EndpointerEvent setEventType(int paramInt)
    {
      this.hasEventType = true;
      this.eventType_ = paramInt;
      return this;
    }
    
    public EndpointerEvent setTimeUsec(long paramLong)
    {
      this.hasTimeUsec = true;
      this.timeUsec_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasEventType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getEventType());
      }
      if (hasTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(2, getTimeUsec());
      }
    }
  }
  
  public static final class Hypothesis
    extends MessageMicro
  {
    private boolean accept_ = true;
    private Alternates.RecognitionClientAlternates alternates_ = null;
    private int cachedSize = -1;
    private float confidence_ = 0.0F;
    private boolean hasAccept;
    private boolean hasAlternates;
    private boolean hasConfidence;
    private boolean hasPhoneAlign;
    private boolean hasPrenormText;
    private boolean hasScrubbedText;
    private boolean hasSemanticResult;
    private boolean hasStateAlign;
    private boolean hasText;
    private boolean hasWordAlign;
    private Alignment.AlignmentProto phoneAlign_ = null;
    private String prenormText_ = "";
    private String scrubbedText_ = "";
    private RecognizerProtos.SemanticResult semanticResult_ = null;
    private Alignment.AlignmentProto stateAlign_ = null;
    private String text_ = "";
    private Alignment.AlignmentProto wordAlign_ = null;
    private List<ConfFeature.WordConfFeature> wordConfFeature_ = Collections.emptyList();
    
    public Hypothesis addWordConfFeature(ConfFeature.WordConfFeature paramWordConfFeature)
    {
      if (paramWordConfFeature == null) {
        throw new NullPointerException();
      }
      if (this.wordConfFeature_.isEmpty()) {
        this.wordConfFeature_ = new ArrayList();
      }
      this.wordConfFeature_.add(paramWordConfFeature);
      return this;
    }
    
    public boolean getAccept()
    {
      return this.accept_;
    }
    
    public Alternates.RecognitionClientAlternates getAlternates()
    {
      return this.alternates_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getConfidence()
    {
      return this.confidence_;
    }
    
    public Alignment.AlignmentProto getPhoneAlign()
    {
      return this.phoneAlign_;
    }
    
    public String getPrenormText()
    {
      return this.prenormText_;
    }
    
    public String getScrubbedText()
    {
      return this.scrubbedText_;
    }
    
    public RecognizerProtos.SemanticResult getSemanticResult()
    {
      return this.semanticResult_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getConfidence());
      }
      if (hasPhoneAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getPhoneAlign());
      }
      if (hasWordAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getWordAlign());
      }
      Iterator localIterator = getWordConfFeatureList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, (ConfFeature.WordConfFeature)localIterator.next());
      }
      if (hasAlternates()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, getAlternates());
      }
      if (hasSemanticResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getSemanticResult());
      }
      if (hasStateAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getStateAlign());
      }
      if (hasAccept()) {
        i += CodedOutputStreamMicro.computeBoolSize(11, getAccept());
      }
      if (hasPrenormText()) {
        i += CodedOutputStreamMicro.computeStringSize(12, getPrenormText());
      }
      if (hasScrubbedText()) {
        i += CodedOutputStreamMicro.computeStringSize(13, getScrubbedText());
      }
      this.cachedSize = i;
      return i;
    }
    
    public Alignment.AlignmentProto getStateAlign()
    {
      return this.stateAlign_;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public Alignment.AlignmentProto getWordAlign()
    {
      return this.wordAlign_;
    }
    
    public List<ConfFeature.WordConfFeature> getWordConfFeatureList()
    {
      return this.wordConfFeature_;
    }
    
    public boolean hasAccept()
    {
      return this.hasAccept;
    }
    
    public boolean hasAlternates()
    {
      return this.hasAlternates;
    }
    
    public boolean hasConfidence()
    {
      return this.hasConfidence;
    }
    
    public boolean hasPhoneAlign()
    {
      return this.hasPhoneAlign;
    }
    
    public boolean hasPrenormText()
    {
      return this.hasPrenormText;
    }
    
    public boolean hasScrubbedText()
    {
      return this.hasScrubbedText;
    }
    
    public boolean hasSemanticResult()
    {
      return this.hasSemanticResult;
    }
    
    public boolean hasStateAlign()
    {
      return this.hasStateAlign;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public boolean hasWordAlign()
    {
      return this.hasWordAlign;
    }
    
    public Hypothesis mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          setText(paramCodedInputStreamMicro.readString());
          break;
        case 21: 
          setConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 26: 
          Alignment.AlignmentProto localAlignmentProto3 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto3);
          setPhoneAlign(localAlignmentProto3);
          break;
        case 34: 
          Alignment.AlignmentProto localAlignmentProto2 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto2);
          setWordAlign(localAlignmentProto2);
          break;
        case 42: 
          ConfFeature.WordConfFeature localWordConfFeature = new ConfFeature.WordConfFeature();
          paramCodedInputStreamMicro.readMessage(localWordConfFeature);
          addWordConfFeature(localWordConfFeature);
          break;
        case 50: 
          Alternates.RecognitionClientAlternates localRecognitionClientAlternates = new Alternates.RecognitionClientAlternates();
          paramCodedInputStreamMicro.readMessage(localRecognitionClientAlternates);
          setAlternates(localRecognitionClientAlternates);
          break;
        case 58: 
          RecognizerProtos.SemanticResult localSemanticResult = new RecognizerProtos.SemanticResult();
          paramCodedInputStreamMicro.readMessage(localSemanticResult);
          setSemanticResult(localSemanticResult);
          break;
        case 82: 
          Alignment.AlignmentProto localAlignmentProto1 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto1);
          setStateAlign(localAlignmentProto1);
          break;
        case 88: 
          setAccept(paramCodedInputStreamMicro.readBool());
          break;
        case 98: 
          setPrenormText(paramCodedInputStreamMicro.readString());
          break;
        }
        setScrubbedText(paramCodedInputStreamMicro.readString());
      }
    }
    
    public Hypothesis setAccept(boolean paramBoolean)
    {
      this.hasAccept = true;
      this.accept_ = paramBoolean;
      return this;
    }
    
    public Hypothesis setAlternates(Alternates.RecognitionClientAlternates paramRecognitionClientAlternates)
    {
      if (paramRecognitionClientAlternates == null) {
        throw new NullPointerException();
      }
      this.hasAlternates = true;
      this.alternates_ = paramRecognitionClientAlternates;
      return this;
    }
    
    public Hypothesis setConfidence(float paramFloat)
    {
      this.hasConfidence = true;
      this.confidence_ = paramFloat;
      return this;
    }
    
    public Hypothesis setPhoneAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasPhoneAlign = true;
      this.phoneAlign_ = paramAlignmentProto;
      return this;
    }
    
    public Hypothesis setPrenormText(String paramString)
    {
      this.hasPrenormText = true;
      this.prenormText_ = paramString;
      return this;
    }
    
    public Hypothesis setScrubbedText(String paramString)
    {
      this.hasScrubbedText = true;
      this.scrubbedText_ = paramString;
      return this;
    }
    
    public Hypothesis setSemanticResult(RecognizerProtos.SemanticResult paramSemanticResult)
    {
      if (paramSemanticResult == null) {
        throw new NullPointerException();
      }
      this.hasSemanticResult = true;
      this.semanticResult_ = paramSemanticResult;
      return this;
    }
    
    public Hypothesis setStateAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasStateAlign = true;
      this.stateAlign_ = paramAlignmentProto;
      return this;
    }
    
    public Hypothesis setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public Hypothesis setWordAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasWordAlign = true;
      this.wordAlign_ = paramAlignmentProto;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(2, getConfidence());
      }
      if (hasPhoneAlign()) {
        paramCodedOutputStreamMicro.writeMessage(3, getPhoneAlign());
      }
      if (hasWordAlign()) {
        paramCodedOutputStreamMicro.writeMessage(4, getWordAlign());
      }
      Iterator localIterator = getWordConfFeatureList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(5, (ConfFeature.WordConfFeature)localIterator.next());
      }
      if (hasAlternates()) {
        paramCodedOutputStreamMicro.writeMessage(6, getAlternates());
      }
      if (hasSemanticResult()) {
        paramCodedOutputStreamMicro.writeMessage(7, getSemanticResult());
      }
      if (hasStateAlign()) {
        paramCodedOutputStreamMicro.writeMessage(10, getStateAlign());
      }
      if (hasAccept()) {
        paramCodedOutputStreamMicro.writeBool(11, getAccept());
      }
      if (hasPrenormText()) {
        paramCodedOutputStreamMicro.writeString(12, getPrenormText());
      }
      if (hasScrubbedText()) {
        paramCodedOutputStreamMicro.writeString(13, getScrubbedText());
      }
    }
  }
  
  public static final class PartialPart
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasSemanticResult;
    private boolean hasStability;
    private boolean hasText;
    private RecognizerProtos.SemanticResult semanticResult_ = null;
    private double stability_ = 0.0D;
    private String text_ = "";
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public RecognizerProtos.SemanticResult getSemanticResult()
    {
      return this.semanticResult_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasText();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeStringSize(1, getText());
      }
      if (hasStability()) {
        i += CodedOutputStreamMicro.computeDoubleSize(2, getStability());
      }
      if (hasSemanticResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getSemanticResult());
      }
      this.cachedSize = i;
      return i;
    }
    
    public double getStability()
    {
      return this.stability_;
    }
    
    public String getText()
    {
      return this.text_;
    }
    
    public boolean hasSemanticResult()
    {
      return this.hasSemanticResult;
    }
    
    public boolean hasStability()
    {
      return this.hasStability;
    }
    
    public boolean hasText()
    {
      return this.hasText;
    }
    
    public PartialPart mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          setText(paramCodedInputStreamMicro.readString());
          break;
        case 17: 
          setStability(paramCodedInputStreamMicro.readDouble());
          break;
        }
        RecognizerProtos.SemanticResult localSemanticResult = new RecognizerProtos.SemanticResult();
        paramCodedInputStreamMicro.readMessage(localSemanticResult);
        setSemanticResult(localSemanticResult);
      }
    }
    
    public PartialPart setSemanticResult(RecognizerProtos.SemanticResult paramSemanticResult)
    {
      if (paramSemanticResult == null) {
        throw new NullPointerException();
      }
      this.hasSemanticResult = true;
      this.semanticResult_ = paramSemanticResult;
      return this;
    }
    
    public PartialPart setStability(double paramDouble)
    {
      this.hasStability = true;
      this.stability_ = paramDouble;
      return this;
    }
    
    public PartialPart setText(String paramString)
    {
      this.hasText = true;
      this.text_ = paramString;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasText()) {
        paramCodedOutputStreamMicro.writeString(1, getText());
      }
      if (hasStability()) {
        paramCodedOutputStreamMicro.writeDouble(2, getStability());
      }
      if (hasSemanticResult()) {
        paramCodedOutputStreamMicro.writeMessage(3, getSemanticResult());
      }
    }
  }
  
  public static final class PartialResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private long endTimeUsec_ = 0L;
    private boolean hasEndTimeUsec;
    private boolean hasHotwordConfFeature;
    private boolean hasHotwordConfidence;
    private boolean hasHotwordEndTimeUsec;
    private boolean hasHotwordFired;
    private boolean hasHotwordStartTimeUsec;
    private boolean hasLatticeFst;
    private boolean hasPhoneAlign;
    private boolean hasStartTimeUsec;
    private boolean hasStateAlign;
    private boolean hasVerificationResult;
    private boolean hasWordAlign;
    private HotwordFeature.HotwordConfidenceFeature hotwordConfFeature_ = null;
    private float hotwordConfidence_ = 0.0F;
    private long hotwordEndTimeUsec_ = 0L;
    private boolean hotwordFired_ = false;
    private long hotwordStartTimeUsec_ = 0L;
    private List<RecognizerProtos.Hypothesis> hypothesis_ = Collections.emptyList();
    private ByteStringMicro latticeFst_ = ByteStringMicro.EMPTY;
    private List<RecognizerProtos.PartialPart> part_ = Collections.emptyList();
    private Alignment.AlignmentProto phoneAlign_ = null;
    private long startTimeUsec_ = 0L;
    private Alignment.AlignmentProto stateAlign_ = null;
    private RecognizerProtos.VerificationResult verificationResult_ = null;
    private Alignment.AlignmentProto wordAlign_ = null;
    private List<ConfFeature.WordConfFeature> wordConfFeature_ = Collections.emptyList();
    
    public PartialResult addHypothesis(RecognizerProtos.Hypothesis paramHypothesis)
    {
      if (paramHypothesis == null) {
        throw new NullPointerException();
      }
      if (this.hypothesis_.isEmpty()) {
        this.hypothesis_ = new ArrayList();
      }
      this.hypothesis_.add(paramHypothesis);
      return this;
    }
    
    public PartialResult addPart(RecognizerProtos.PartialPart paramPartialPart)
    {
      if (paramPartialPart == null) {
        throw new NullPointerException();
      }
      if (this.part_.isEmpty()) {
        this.part_ = new ArrayList();
      }
      this.part_.add(paramPartialPart);
      return this;
    }
    
    public PartialResult addWordConfFeature(ConfFeature.WordConfFeature paramWordConfFeature)
    {
      if (paramWordConfFeature == null) {
        throw new NullPointerException();
      }
      if (this.wordConfFeature_.isEmpty()) {
        this.wordConfFeature_ = new ArrayList();
      }
      this.wordConfFeature_.add(paramWordConfFeature);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public long getEndTimeUsec()
    {
      return this.endTimeUsec_;
    }
    
    public HotwordFeature.HotwordConfidenceFeature getHotwordConfFeature()
    {
      return this.hotwordConfFeature_;
    }
    
    public float getHotwordConfidence()
    {
      return this.hotwordConfidence_;
    }
    
    public long getHotwordEndTimeUsec()
    {
      return this.hotwordEndTimeUsec_;
    }
    
    public boolean getHotwordFired()
    {
      return this.hotwordFired_;
    }
    
    public long getHotwordStartTimeUsec()
    {
      return this.hotwordStartTimeUsec_;
    }
    
    public List<RecognizerProtos.Hypothesis> getHypothesisList()
    {
      return this.hypothesis_;
    }
    
    public ByteStringMicro getLatticeFst()
    {
      return this.latticeFst_;
    }
    
    public RecognizerProtos.PartialPart getPart(int paramInt)
    {
      return (RecognizerProtos.PartialPart)this.part_.get(paramInt);
    }
    
    public int getPartCount()
    {
      return this.part_.size();
    }
    
    public List<RecognizerProtos.PartialPart> getPartList()
    {
      return this.part_;
    }
    
    public Alignment.AlignmentProto getPhoneAlign()
    {
      return this.phoneAlign_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator1 = getPartList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (RecognizerProtos.PartialPart)localIterator1.next());
      }
      if (hasStartTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getStartTimeUsec());
      }
      if (hasEndTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(3, getEndTimeUsec());
      }
      if (hasWordAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getWordAlign());
      }
      if (hasPhoneAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getPhoneAlign());
      }
      Iterator localIterator2 = getWordConfFeatureList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(6, (ConfFeature.WordConfFeature)localIterator2.next());
      }
      if (hasHotwordConfFeature()) {
        i += CodedOutputStreamMicro.computeMessageSize(7, getHotwordConfFeature());
      }
      if (hasHotwordConfidence()) {
        i += CodedOutputStreamMicro.computeFloatSize(8, getHotwordConfidence());
      }
      if (hasHotwordFired()) {
        i += CodedOutputStreamMicro.computeBoolSize(9, getHotwordFired());
      }
      if (hasStateAlign()) {
        i += CodedOutputStreamMicro.computeMessageSize(10, getStateAlign());
      }
      if (hasHotwordStartTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(11, getHotwordStartTimeUsec());
      }
      if (hasHotwordEndTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(12, getHotwordEndTimeUsec());
      }
      if (hasLatticeFst()) {
        i += CodedOutputStreamMicro.computeBytesSize(13, getLatticeFst());
      }
      Iterator localIterator3 = getHypothesisList().iterator();
      while (localIterator3.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(14, (RecognizerProtos.Hypothesis)localIterator3.next());
      }
      if (hasVerificationResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(15, getVerificationResult());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getStartTimeUsec()
    {
      return this.startTimeUsec_;
    }
    
    public Alignment.AlignmentProto getStateAlign()
    {
      return this.stateAlign_;
    }
    
    public RecognizerProtos.VerificationResult getVerificationResult()
    {
      return this.verificationResult_;
    }
    
    public Alignment.AlignmentProto getWordAlign()
    {
      return this.wordAlign_;
    }
    
    public List<ConfFeature.WordConfFeature> getWordConfFeatureList()
    {
      return this.wordConfFeature_;
    }
    
    public boolean hasEndTimeUsec()
    {
      return this.hasEndTimeUsec;
    }
    
    public boolean hasHotwordConfFeature()
    {
      return this.hasHotwordConfFeature;
    }
    
    public boolean hasHotwordConfidence()
    {
      return this.hasHotwordConfidence;
    }
    
    public boolean hasHotwordEndTimeUsec()
    {
      return this.hasHotwordEndTimeUsec;
    }
    
    public boolean hasHotwordFired()
    {
      return this.hasHotwordFired;
    }
    
    public boolean hasHotwordStartTimeUsec()
    {
      return this.hasHotwordStartTimeUsec;
    }
    
    public boolean hasLatticeFst()
    {
      return this.hasLatticeFst;
    }
    
    public boolean hasPhoneAlign()
    {
      return this.hasPhoneAlign;
    }
    
    public boolean hasStartTimeUsec()
    {
      return this.hasStartTimeUsec;
    }
    
    public boolean hasStateAlign()
    {
      return this.hasStateAlign;
    }
    
    public boolean hasVerificationResult()
    {
      return this.hasVerificationResult;
    }
    
    public boolean hasWordAlign()
    {
      return this.hasWordAlign;
    }
    
    public PartialResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 10: 
          RecognizerProtos.PartialPart localPartialPart = new RecognizerProtos.PartialPart();
          paramCodedInputStreamMicro.readMessage(localPartialPart);
          addPart(localPartialPart);
          break;
        case 16: 
          setStartTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 24: 
          setEndTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 34: 
          Alignment.AlignmentProto localAlignmentProto3 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto3);
          setWordAlign(localAlignmentProto3);
          break;
        case 42: 
          Alignment.AlignmentProto localAlignmentProto2 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto2);
          setPhoneAlign(localAlignmentProto2);
          break;
        case 50: 
          ConfFeature.WordConfFeature localWordConfFeature = new ConfFeature.WordConfFeature();
          paramCodedInputStreamMicro.readMessage(localWordConfFeature);
          addWordConfFeature(localWordConfFeature);
          break;
        case 58: 
          HotwordFeature.HotwordConfidenceFeature localHotwordConfidenceFeature = new HotwordFeature.HotwordConfidenceFeature();
          paramCodedInputStreamMicro.readMessage(localHotwordConfidenceFeature);
          setHotwordConfFeature(localHotwordConfidenceFeature);
          break;
        case 69: 
          setHotwordConfidence(paramCodedInputStreamMicro.readFloat());
          break;
        case 72: 
          setHotwordFired(paramCodedInputStreamMicro.readBool());
          break;
        case 82: 
          Alignment.AlignmentProto localAlignmentProto1 = new Alignment.AlignmentProto();
          paramCodedInputStreamMicro.readMessage(localAlignmentProto1);
          setStateAlign(localAlignmentProto1);
          break;
        case 88: 
          setHotwordStartTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 96: 
          setHotwordEndTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 106: 
          setLatticeFst(paramCodedInputStreamMicro.readBytes());
          break;
        case 114: 
          RecognizerProtos.Hypothesis localHypothesis = new RecognizerProtos.Hypothesis();
          paramCodedInputStreamMicro.readMessage(localHypothesis);
          addHypothesis(localHypothesis);
          break;
        }
        RecognizerProtos.VerificationResult localVerificationResult = new RecognizerProtos.VerificationResult();
        paramCodedInputStreamMicro.readMessage(localVerificationResult);
        setVerificationResult(localVerificationResult);
      }
    }
    
    public PartialResult setEndTimeUsec(long paramLong)
    {
      this.hasEndTimeUsec = true;
      this.endTimeUsec_ = paramLong;
      return this;
    }
    
    public PartialResult setHotwordConfFeature(HotwordFeature.HotwordConfidenceFeature paramHotwordConfidenceFeature)
    {
      if (paramHotwordConfidenceFeature == null) {
        throw new NullPointerException();
      }
      this.hasHotwordConfFeature = true;
      this.hotwordConfFeature_ = paramHotwordConfidenceFeature;
      return this;
    }
    
    public PartialResult setHotwordConfidence(float paramFloat)
    {
      this.hasHotwordConfidence = true;
      this.hotwordConfidence_ = paramFloat;
      return this;
    }
    
    public PartialResult setHotwordEndTimeUsec(long paramLong)
    {
      this.hasHotwordEndTimeUsec = true;
      this.hotwordEndTimeUsec_ = paramLong;
      return this;
    }
    
    public PartialResult setHotwordFired(boolean paramBoolean)
    {
      this.hasHotwordFired = true;
      this.hotwordFired_ = paramBoolean;
      return this;
    }
    
    public PartialResult setHotwordStartTimeUsec(long paramLong)
    {
      this.hasHotwordStartTimeUsec = true;
      this.hotwordStartTimeUsec_ = paramLong;
      return this;
    }
    
    public PartialResult setLatticeFst(ByteStringMicro paramByteStringMicro)
    {
      this.hasLatticeFst = true;
      this.latticeFst_ = paramByteStringMicro;
      return this;
    }
    
    public PartialResult setPhoneAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasPhoneAlign = true;
      this.phoneAlign_ = paramAlignmentProto;
      return this;
    }
    
    public PartialResult setStartTimeUsec(long paramLong)
    {
      this.hasStartTimeUsec = true;
      this.startTimeUsec_ = paramLong;
      return this;
    }
    
    public PartialResult setStateAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasStateAlign = true;
      this.stateAlign_ = paramAlignmentProto;
      return this;
    }
    
    public PartialResult setVerificationResult(RecognizerProtos.VerificationResult paramVerificationResult)
    {
      if (paramVerificationResult == null) {
        throw new NullPointerException();
      }
      this.hasVerificationResult = true;
      this.verificationResult_ = paramVerificationResult;
      return this;
    }
    
    public PartialResult setWordAlign(Alignment.AlignmentProto paramAlignmentProto)
    {
      if (paramAlignmentProto == null) {
        throw new NullPointerException();
      }
      this.hasWordAlign = true;
      this.wordAlign_ = paramAlignmentProto;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator1 = getPartList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (RecognizerProtos.PartialPart)localIterator1.next());
      }
      if (hasStartTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(2, getStartTimeUsec());
      }
      if (hasEndTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(3, getEndTimeUsec());
      }
      if (hasWordAlign()) {
        paramCodedOutputStreamMicro.writeMessage(4, getWordAlign());
      }
      if (hasPhoneAlign()) {
        paramCodedOutputStreamMicro.writeMessage(5, getPhoneAlign());
      }
      Iterator localIterator2 = getWordConfFeatureList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(6, (ConfFeature.WordConfFeature)localIterator2.next());
      }
      if (hasHotwordConfFeature()) {
        paramCodedOutputStreamMicro.writeMessage(7, getHotwordConfFeature());
      }
      if (hasHotwordConfidence()) {
        paramCodedOutputStreamMicro.writeFloat(8, getHotwordConfidence());
      }
      if (hasHotwordFired()) {
        paramCodedOutputStreamMicro.writeBool(9, getHotwordFired());
      }
      if (hasStateAlign()) {
        paramCodedOutputStreamMicro.writeMessage(10, getStateAlign());
      }
      if (hasHotwordStartTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(11, getHotwordStartTimeUsec());
      }
      if (hasHotwordEndTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(12, getHotwordEndTimeUsec());
      }
      if (hasLatticeFst()) {
        paramCodedOutputStreamMicro.writeBytes(13, getLatticeFst());
      }
      Iterator localIterator3 = getHypothesisList().iterator();
      while (localIterator3.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(14, (RecognizerProtos.Hypothesis)localIterator3.next());
      }
      if (hasVerificationResult()) {
        paramCodedOutputStreamMicro.writeMessage(15, getVerificationResult());
      }
    }
  }
  
  public static final class RecognitionEvent
    extends MessageMicro
  {
    private int cachedSize = -1;
    private RecognizerProtos.RecognitionResult combinedResult_ = null;
    private int eventType_ = 0;
    private long generationTimeMs_ = 0L;
    private boolean hasCombinedResult;
    private boolean hasEventType;
    private boolean hasGenerationTimeMs;
    private boolean hasPartialResult;
    private boolean hasResult;
    private boolean hasStatus;
    private RecognizerProtos.PartialResult partialResult_ = null;
    private RecognizerProtos.RecognitionResult result_ = null;
    private int status_ = 0;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public RecognizerProtos.RecognitionResult getCombinedResult()
    {
      return this.combinedResult_;
    }
    
    public int getEventType()
    {
      return this.eventType_;
    }
    
    public long getGenerationTimeMs()
    {
      return this.generationTimeMs_;
    }
    
    public RecognizerProtos.PartialResult getPartialResult()
    {
      return this.partialResult_;
    }
    
    public RecognizerProtos.RecognitionResult getResult()
    {
      return this.result_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasEventType();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getEventType());
      }
      if (hasStatus()) {
        i += CodedOutputStreamMicro.computeInt32Size(2, getStatus());
      }
      if (hasResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, getResult());
      }
      if (hasPartialResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, getPartialResult());
      }
      if (hasCombinedResult()) {
        i += CodedOutputStreamMicro.computeMessageSize(5, getCombinedResult());
      }
      if (hasGenerationTimeMs()) {
        i += CodedOutputStreamMicro.computeInt64Size(6, getGenerationTimeMs());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getStatus()
    {
      return this.status_;
    }
    
    public boolean hasCombinedResult()
    {
      return this.hasCombinedResult;
    }
    
    public boolean hasEventType()
    {
      return this.hasEventType;
    }
    
    public boolean hasGenerationTimeMs()
    {
      return this.hasGenerationTimeMs;
    }
    
    public boolean hasPartialResult()
    {
      return this.hasPartialResult;
    }
    
    public boolean hasResult()
    {
      return this.hasResult;
    }
    
    public boolean hasStatus()
    {
      return this.hasStatus;
    }
    
    public RecognitionEvent mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setEventType(paramCodedInputStreamMicro.readInt32());
          break;
        case 16: 
          setStatus(paramCodedInputStreamMicro.readInt32());
          break;
        case 26: 
          RecognizerProtos.RecognitionResult localRecognitionResult2 = new RecognizerProtos.RecognitionResult();
          paramCodedInputStreamMicro.readMessage(localRecognitionResult2);
          setResult(localRecognitionResult2);
          break;
        case 34: 
          RecognizerProtos.PartialResult localPartialResult = new RecognizerProtos.PartialResult();
          paramCodedInputStreamMicro.readMessage(localPartialResult);
          setPartialResult(localPartialResult);
          break;
        case 42: 
          RecognizerProtos.RecognitionResult localRecognitionResult1 = new RecognizerProtos.RecognitionResult();
          paramCodedInputStreamMicro.readMessage(localRecognitionResult1);
          setCombinedResult(localRecognitionResult1);
          break;
        }
        setGenerationTimeMs(paramCodedInputStreamMicro.readInt64());
      }
    }
    
    public RecognitionEvent setCombinedResult(RecognizerProtos.RecognitionResult paramRecognitionResult)
    {
      if (paramRecognitionResult == null) {
        throw new NullPointerException();
      }
      this.hasCombinedResult = true;
      this.combinedResult_ = paramRecognitionResult;
      return this;
    }
    
    public RecognitionEvent setEventType(int paramInt)
    {
      this.hasEventType = true;
      this.eventType_ = paramInt;
      return this;
    }
    
    public RecognitionEvent setGenerationTimeMs(long paramLong)
    {
      this.hasGenerationTimeMs = true;
      this.generationTimeMs_ = paramLong;
      return this;
    }
    
    public RecognitionEvent setPartialResult(RecognizerProtos.PartialResult paramPartialResult)
    {
      if (paramPartialResult == null) {
        throw new NullPointerException();
      }
      this.hasPartialResult = true;
      this.partialResult_ = paramPartialResult;
      return this;
    }
    
    public RecognitionEvent setResult(RecognizerProtos.RecognitionResult paramRecognitionResult)
    {
      if (paramRecognitionResult == null) {
        throw new NullPointerException();
      }
      this.hasResult = true;
      this.result_ = paramRecognitionResult;
      return this;
    }
    
    public RecognitionEvent setStatus(int paramInt)
    {
      this.hasStatus = true;
      this.status_ = paramInt;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasEventType()) {
        paramCodedOutputStreamMicro.writeInt32(1, getEventType());
      }
      if (hasStatus()) {
        paramCodedOutputStreamMicro.writeInt32(2, getStatus());
      }
      if (hasResult()) {
        paramCodedOutputStreamMicro.writeMessage(3, getResult());
      }
      if (hasPartialResult()) {
        paramCodedOutputStreamMicro.writeMessage(4, getPartialResult());
      }
      if (hasCombinedResult()) {
        paramCodedOutputStreamMicro.writeMessage(5, getCombinedResult());
      }
      if (hasGenerationTimeMs()) {
        paramCodedOutputStreamMicro.writeInt64(6, getGenerationTimeMs());
      }
    }
  }
  
  public static final class RecognitionResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private long endTimeUsec_ = 0L;
    private boolean hasEndTimeUsec;
    private boolean hasLatticeFst;
    private boolean hasStartTimeUsec;
    private List<RecognizerProtos.Hypothesis> hypothesis_ = Collections.emptyList();
    private ByteStringMicro latticeFst_ = ByteStringMicro.EMPTY;
    private long startTimeUsec_ = 0L;
    
    public RecognitionResult addHypothesis(RecognizerProtos.Hypothesis paramHypothesis)
    {
      if (paramHypothesis == null) {
        throw new NullPointerException();
      }
      if (this.hypothesis_.isEmpty()) {
        this.hypothesis_ = new ArrayList();
      }
      this.hypothesis_.add(paramHypothesis);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public long getEndTimeUsec()
    {
      return this.endTimeUsec_;
    }
    
    public RecognizerProtos.Hypothesis getHypothesis(int paramInt)
    {
      return (RecognizerProtos.Hypothesis)this.hypothesis_.get(paramInt);
    }
    
    public int getHypothesisCount()
    {
      return this.hypothesis_.size();
    }
    
    public List<RecognizerProtos.Hypothesis> getHypothesisList()
    {
      return this.hypothesis_;
    }
    
    public ByteStringMicro getLatticeFst()
    {
      return this.latticeFst_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStartTimeUsec();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeInt64Size(1, getStartTimeUsec());
      }
      if (hasEndTimeUsec()) {
        i += CodedOutputStreamMicro.computeInt64Size(2, getEndTimeUsec());
      }
      Iterator localIterator = getHypothesisList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (RecognizerProtos.Hypothesis)localIterator.next());
      }
      if (hasLatticeFst()) {
        i += CodedOutputStreamMicro.computeBytesSize(4, getLatticeFst());
      }
      this.cachedSize = i;
      return i;
    }
    
    public long getStartTimeUsec()
    {
      return this.startTimeUsec_;
    }
    
    public boolean hasEndTimeUsec()
    {
      return this.hasEndTimeUsec;
    }
    
    public boolean hasLatticeFst()
    {
      return this.hasLatticeFst;
    }
    
    public boolean hasStartTimeUsec()
    {
      return this.hasStartTimeUsec;
    }
    
    public RecognitionResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setStartTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 16: 
          setEndTimeUsec(paramCodedInputStreamMicro.readInt64());
          break;
        case 26: 
          RecognizerProtos.Hypothesis localHypothesis = new RecognizerProtos.Hypothesis();
          paramCodedInputStreamMicro.readMessage(localHypothesis);
          addHypothesis(localHypothesis);
          break;
        }
        setLatticeFst(paramCodedInputStreamMicro.readBytes());
      }
    }
    
    public RecognitionResult setEndTimeUsec(long paramLong)
    {
      this.hasEndTimeUsec = true;
      this.endTimeUsec_ = paramLong;
      return this;
    }
    
    public RecognitionResult setLatticeFst(ByteStringMicro paramByteStringMicro)
    {
      this.hasLatticeFst = true;
      this.latticeFst_ = paramByteStringMicro;
      return this;
    }
    
    public RecognitionResult setStartTimeUsec(long paramLong)
    {
      this.hasStartTimeUsec = true;
      this.startTimeUsec_ = paramLong;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStartTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(1, getStartTimeUsec());
      }
      if (hasEndTimeUsec()) {
        paramCodedOutputStreamMicro.writeInt64(2, getEndTimeUsec());
      }
      Iterator localIterator = getHypothesisList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (RecognizerProtos.Hypothesis)localIterator.next());
      }
      if (hasLatticeFst()) {
        paramCodedOutputStreamMicro.writeBytes(4, getLatticeFst());
      }
    }
  }
  
  public static final class SemanticResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<InterpretationProto.Interpretation> interpretation_ = Collections.emptyList();
    
    public SemanticResult addInterpretation(InterpretationProto.Interpretation paramInterpretation)
    {
      if (paramInterpretation == null) {
        throw new NullPointerException();
      }
      if (this.interpretation_.isEmpty()) {
        this.interpretation_ = new ArrayList();
      }
      this.interpretation_.add(paramInterpretation);
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public InterpretationProto.Interpretation getInterpretation(int paramInt)
    {
      return (InterpretationProto.Interpretation)this.interpretation_.get(paramInt);
    }
    
    public int getInterpretationCount()
    {
      return this.interpretation_.size();
    }
    
    public List<InterpretationProto.Interpretation> getInterpretationList()
    {
      return this.interpretation_;
    }
    
    public int getSerializedSize()
    {
      int i = 0;
      Iterator localIterator = getInterpretationList().iterator();
      while (localIterator.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(1, (InterpretationProto.Interpretation)localIterator.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public SemanticResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
            InterpretationProto.Interpretation localInterpretation = new InterpretationProto.Interpretation();
            paramCodedInputStreamMicro.readMessage(localInterpretation);
            addInterpretation(localInterpretation);
        case 0:
          return this;
        }
      }
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      Iterator localIterator = getInterpretationList().iterator();
      while (localIterator.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(1, (InterpretationProto.Interpretation)localIterator.next());
      }
    }
  }
  
  public static final class VerificationResult
    extends MessageMicro
  {
    private int cachedSize = -1;
    private boolean hasSpeakerId;
    private boolean hasSpeakerVerified;
    private boolean hasVerificationScore;
    private String speakerId_ = "";
    private boolean speakerVerified_ = false;
    private float verificationScore_ = 0.0F;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasSpeakerVerified();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeBoolSize(1, getSpeakerVerified());
      }
      if (hasSpeakerId()) {
        i += CodedOutputStreamMicro.computeStringSize(2, getSpeakerId());
      }
      if (hasVerificationScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getVerificationScore());
      }
      this.cachedSize = i;
      return i;
    }
    
    public String getSpeakerId()
    {
      return this.speakerId_;
    }
    
    public boolean getSpeakerVerified()
    {
      return this.speakerVerified_;
    }
    
    public float getVerificationScore()
    {
      return this.verificationScore_;
    }
    
    public boolean hasSpeakerId()
    {
      return this.hasSpeakerId;
    }
    
    public boolean hasSpeakerVerified()
    {
      return this.hasSpeakerVerified;
    }
    
    public boolean hasVerificationScore()
    {
      return this.hasVerificationScore;
    }
    
    public VerificationResult mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      for (;;)
      {
        int i = paramCodedInputStreamMicro.readTag();
        switch (i)
        {
        default: 
          if (parseUnknownField(paramCodedInputStreamMicro, i)) {
            continue;
          }
        case 0: 
          return this;
        case 8: 
          setSpeakerVerified(paramCodedInputStreamMicro.readBool());
          break;
        case 18: 
          setSpeakerId(paramCodedInputStreamMicro.readString());
          break;
        }
        setVerificationScore(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public VerificationResult setSpeakerId(String paramString)
    {
      this.hasSpeakerId = true;
      this.speakerId_ = paramString;
      return this;
    }
    
    public VerificationResult setSpeakerVerified(boolean paramBoolean)
    {
      this.hasSpeakerVerified = true;
      this.speakerVerified_ = paramBoolean;
      return this;
    }
    
    public VerificationResult setVerificationScore(float paramFloat)
    {
      this.hasVerificationScore = true;
      this.verificationScore_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasSpeakerVerified()) {
        paramCodedOutputStreamMicro.writeBool(1, getSpeakerVerified());
      }
      if (hasSpeakerId()) {
        paramCodedOutputStreamMicro.writeString(2, getSpeakerId());
      }
      if (hasVerificationScore()) {
        paramCodedOutputStreamMicro.writeFloat(3, getVerificationScore());
      }
    }
  }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.recognizer.api.RecognizerProtos

 * JD-Core Version:    0.7.0.1

 */