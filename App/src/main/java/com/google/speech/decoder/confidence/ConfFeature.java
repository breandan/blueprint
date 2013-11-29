package com.google.speech.decoder.confidence;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;

public final class ConfFeature
{
  public static final class WordConfFeature
    extends MessageMicro
  {
    private float amScore_ = 0.0F;
    private float ascoreBest_ = 0.0F;
    private float ascoreMean_ = 0.0F;
    private float ascoreMeandiff_ = 0.0F;
    private float ascoreStddev_ = 0.0F;
    private float ascoreWorst_ = 0.0F;
    private int cachedSize = -1;
    private float durScore_ = 0.0F;
    private float framePosterior_ = 0.0F;
    private boolean hasAmScore;
    private boolean hasAscoreBest;
    private boolean hasAscoreMean;
    private boolean hasAscoreMeandiff;
    private boolean hasAscoreStddev;
    private boolean hasAscoreWorst;
    private boolean hasDurScore;
    private boolean hasFramePosterior;
    private boolean hasLatPosterior;
    private boolean hasLmScore;
    private boolean hasNumPhones;
    private boolean hasPivotPosterior;
    private boolean hasWordDuration;
    private float latPosterior_ = 0.0F;
    private float lmScore_ = 0.0F;
    private float numPhones_ = 0.0F;
    private float pivotPosterior_ = 0.0F;
    private float wordDuration_ = 0.0F;
    
    public float getAmScore()
    {
      return this.amScore_;
    }
    
    public float getAscoreBest()
    {
      return this.ascoreBest_;
    }
    
    public float getAscoreMean()
    {
      return this.ascoreMean_;
    }
    
    public float getAscoreMeandiff()
    {
      return this.ascoreMeandiff_;
    }
    
    public float getAscoreStddev()
    {
      return this.ascoreStddev_;
    }
    
    public float getAscoreWorst()
    {
      return this.ascoreWorst_;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public float getDurScore()
    {
      return this.durScore_;
    }
    
    public float getFramePosterior()
    {
      return this.framePosterior_;
    }
    
    public float getLatPosterior()
    {
      return this.latPosterior_;
    }
    
    public float getLmScore()
    {
      return this.lmScore_;
    }
    
    public float getNumPhones()
    {
      return this.numPhones_;
    }
    
    public float getPivotPosterior()
    {
      return this.pivotPosterior_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasLatPosterior();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeFloatSize(1, getLatPosterior());
      }
      if (hasFramePosterior()) {
        i += CodedOutputStreamMicro.computeFloatSize(2, getFramePosterior());
      }
      if (hasNumPhones()) {
        i += CodedOutputStreamMicro.computeFloatSize(3, getNumPhones());
      }
      if (hasWordDuration()) {
        i += CodedOutputStreamMicro.computeFloatSize(4, getWordDuration());
      }
      if (hasAscoreMean()) {
        i += CodedOutputStreamMicro.computeFloatSize(5, getAscoreMean());
      }
      if (hasAscoreStddev()) {
        i += CodedOutputStreamMicro.computeFloatSize(6, getAscoreStddev());
      }
      if (hasAscoreWorst()) {
        i += CodedOutputStreamMicro.computeFloatSize(7, getAscoreWorst());
      }
      if (hasAscoreMeandiff()) {
        i += CodedOutputStreamMicro.computeFloatSize(8, getAscoreMeandiff());
      }
      if (hasAscoreBest()) {
        i += CodedOutputStreamMicro.computeFloatSize(9, getAscoreBest());
      }
      if (hasLmScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(10, getLmScore());
      }
      if (hasDurScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(11, getDurScore());
      }
      if (hasAmScore()) {
        i += CodedOutputStreamMicro.computeFloatSize(12, getAmScore());
      }
      if (hasPivotPosterior()) {
        i += CodedOutputStreamMicro.computeFloatSize(13, getPivotPosterior());
      }
      this.cachedSize = i;
      return i;
    }
    
    public float getWordDuration()
    {
      return this.wordDuration_;
    }
    
    public boolean hasAmScore()
    {
      return this.hasAmScore;
    }
    
    public boolean hasAscoreBest()
    {
      return this.hasAscoreBest;
    }
    
    public boolean hasAscoreMean()
    {
      return this.hasAscoreMean;
    }
    
    public boolean hasAscoreMeandiff()
    {
      return this.hasAscoreMeandiff;
    }
    
    public boolean hasAscoreStddev()
    {
      return this.hasAscoreStddev;
    }
    
    public boolean hasAscoreWorst()
    {
      return this.hasAscoreWorst;
    }
    
    public boolean hasDurScore()
    {
      return this.hasDurScore;
    }
    
    public boolean hasFramePosterior()
    {
      return this.hasFramePosterior;
    }
    
    public boolean hasLatPosterior()
    {
      return this.hasLatPosterior;
    }
    
    public boolean hasLmScore()
    {
      return this.hasLmScore;
    }
    
    public boolean hasNumPhones()
    {
      return this.hasNumPhones;
    }
    
    public boolean hasPivotPosterior()
    {
      return this.hasPivotPosterior;
    }
    
    public boolean hasWordDuration()
    {
      return this.hasWordDuration;
    }
    
    public WordConfFeature mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          setLatPosterior(paramCodedInputStreamMicro.readFloat());
          break;
        case 21: 
          setFramePosterior(paramCodedInputStreamMicro.readFloat());
          break;
        case 29: 
          setNumPhones(paramCodedInputStreamMicro.readFloat());
          break;
        case 37: 
          setWordDuration(paramCodedInputStreamMicro.readFloat());
          break;
        case 45: 
          setAscoreMean(paramCodedInputStreamMicro.readFloat());
          break;
        case 53: 
          setAscoreStddev(paramCodedInputStreamMicro.readFloat());
          break;
        case 61: 
          setAscoreWorst(paramCodedInputStreamMicro.readFloat());
          break;
        case 69: 
          setAscoreMeandiff(paramCodedInputStreamMicro.readFloat());
          break;
        case 77: 
          setAscoreBest(paramCodedInputStreamMicro.readFloat());
          break;
        case 85: 
          setLmScore(paramCodedInputStreamMicro.readFloat());
          break;
        case 93: 
          setDurScore(paramCodedInputStreamMicro.readFloat());
          break;
        case 101: 
          setAmScore(paramCodedInputStreamMicro.readFloat());
          break;
        }
        setPivotPosterior(paramCodedInputStreamMicro.readFloat());
      }
    }
    
    public WordConfFeature setAmScore(float paramFloat)
    {
      this.hasAmScore = true;
      this.amScore_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setAscoreBest(float paramFloat)
    {
      this.hasAscoreBest = true;
      this.ascoreBest_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setAscoreMean(float paramFloat)
    {
      this.hasAscoreMean = true;
      this.ascoreMean_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setAscoreMeandiff(float paramFloat)
    {
      this.hasAscoreMeandiff = true;
      this.ascoreMeandiff_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setAscoreStddev(float paramFloat)
    {
      this.hasAscoreStddev = true;
      this.ascoreStddev_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setAscoreWorst(float paramFloat)
    {
      this.hasAscoreWorst = true;
      this.ascoreWorst_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setDurScore(float paramFloat)
    {
      this.hasDurScore = true;
      this.durScore_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setFramePosterior(float paramFloat)
    {
      this.hasFramePosterior = true;
      this.framePosterior_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setLatPosterior(float paramFloat)
    {
      this.hasLatPosterior = true;
      this.latPosterior_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setLmScore(float paramFloat)
    {
      this.hasLmScore = true;
      this.lmScore_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setNumPhones(float paramFloat)
    {
      this.hasNumPhones = true;
      this.numPhones_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setPivotPosterior(float paramFloat)
    {
      this.hasPivotPosterior = true;
      this.pivotPosterior_ = paramFloat;
      return this;
    }
    
    public WordConfFeature setWordDuration(float paramFloat)
    {
      this.hasWordDuration = true;
      this.wordDuration_ = paramFloat;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasLatPosterior()) {
        paramCodedOutputStreamMicro.writeFloat(1, getLatPosterior());
      }
      if (hasFramePosterior()) {
        paramCodedOutputStreamMicro.writeFloat(2, getFramePosterior());
      }
      if (hasNumPhones()) {
        paramCodedOutputStreamMicro.writeFloat(3, getNumPhones());
      }
      if (hasWordDuration()) {
        paramCodedOutputStreamMicro.writeFloat(4, getWordDuration());
      }
      if (hasAscoreMean()) {
        paramCodedOutputStreamMicro.writeFloat(5, getAscoreMean());
      }
      if (hasAscoreStddev()) {
        paramCodedOutputStreamMicro.writeFloat(6, getAscoreStddev());
      }
      if (hasAscoreWorst()) {
        paramCodedOutputStreamMicro.writeFloat(7, getAscoreWorst());
      }
      if (hasAscoreMeandiff()) {
        paramCodedOutputStreamMicro.writeFloat(8, getAscoreMeandiff());
      }
      if (hasAscoreBest()) {
        paramCodedOutputStreamMicro.writeFloat(9, getAscoreBest());
      }
      if (hasLmScore()) {
        paramCodedOutputStreamMicro.writeFloat(10, getLmScore());
      }
      if (hasDurScore()) {
        paramCodedOutputStreamMicro.writeFloat(11, getDurScore());
      }
      if (hasAmScore()) {
        paramCodedOutputStreamMicro.writeFloat(12, getAmScore());
      }
      if (hasPivotPosterior()) {
        paramCodedOutputStreamMicro.writeFloat(13, getPivotPosterior());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.decoder.confidence.ConfFeature
 * JD-Core Version:    0.7.0.1
 */