package com.google.android.apps.sidekick.training;

import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.QuestionTemplates;
import com.google.geo.sidekick.Sidekick.StringDictionary;
import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Training
{
  public static final class QuestionWithEntry
    extends MessageMicro
  {
    private int cachedSize = -1;
    private Sidekick.Entry entry_ = null;
    private boolean hasEntry;
    private boolean hasQuestion;
    private Sidekick.Question question_ = null;
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public Sidekick.Entry getEntry()
    {
      return this.entry_;
    }
    
    public Sidekick.Question getQuestion()
    {
      return this.question_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasQuestion();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getQuestion());
      }
      if (hasEntry()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getEntry());
      }
      this.cachedSize = i;
      return i;
    }
    
    public boolean hasEntry()
    {
      return this.hasEntry;
    }
    
    public boolean hasQuestion()
    {
      return this.hasQuestion;
    }
    
    public QuestionWithEntry mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          Sidekick.Question localQuestion = new Sidekick.Question();
          paramCodedInputStreamMicro.readMessage(localQuestion);
          setQuestion(localQuestion);
          break;
        }
        Sidekick.Entry localEntry = new Sidekick.Entry();
        paramCodedInputStreamMicro.readMessage(localEntry);
        setEntry(localEntry);
      }
    }
    
    public QuestionWithEntry setEntry(Sidekick.Entry paramEntry)
    {
      if (paramEntry == null) {
        throw new NullPointerException();
      }
      this.hasEntry = true;
      this.entry_ = paramEntry;
      return this;
    }
    
    public QuestionWithEntry setQuestion(Sidekick.Question paramQuestion)
    {
      if (paramQuestion == null) {
        throw new NullPointerException();
      }
      this.hasQuestion = true;
      this.question_ = paramQuestion;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasQuestion()) {
        paramCodedOutputStreamMicro.writeMessage(1, getQuestion());
      }
      if (hasEntry()) {
        paramCodedOutputStreamMicro.writeMessage(2, getEntry());
      }
    }
  }
  
  public static final class TrainingModeData
    extends MessageMicro
  {
    private int cachedSize = -1;
    private List<Training.QuestionWithEntry> clientAnsweredQuestion_ = Collections.emptyList();
    private boolean hasQuestionTemplates;
    private boolean hasStringDictionary;
    private Sidekick.QuestionTemplates questionTemplates_ = null;
    private List<Sidekick.Question> serverAnsweredQuestion_ = Collections.emptyList();
    private Sidekick.StringDictionary stringDictionary_ = null;
    
    public static TrainingModeData parseFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
      throws IOException
    {
      return new TrainingModeData().mergeFrom(paramCodedInputStreamMicro);
    }
    
    public static TrainingModeData parseFrom(byte[] paramArrayOfByte)
      throws InvalidProtocolBufferMicroException
    {
      return (TrainingModeData)new TrainingModeData().mergeFrom(paramArrayOfByte);
    }
    
    public TrainingModeData addClientAnsweredQuestion(Training.QuestionWithEntry paramQuestionWithEntry)
    {
      if (paramQuestionWithEntry == null) {
        throw new NullPointerException();
      }
      if (this.clientAnsweredQuestion_.isEmpty()) {
        this.clientAnsweredQuestion_ = new ArrayList();
      }
      this.clientAnsweredQuestion_.add(paramQuestionWithEntry);
      return this;
    }
    
    public TrainingModeData addServerAnsweredQuestion(Sidekick.Question paramQuestion)
    {
      if (paramQuestion == null) {
        throw new NullPointerException();
      }
      if (this.serverAnsweredQuestion_.isEmpty()) {
        this.serverAnsweredQuestion_ = new ArrayList();
      }
      this.serverAnsweredQuestion_.add(paramQuestion);
      return this;
    }
    
    public TrainingModeData clearClientAnsweredQuestion()
    {
      this.clientAnsweredQuestion_ = Collections.emptyList();
      return this;
    }
    
    public TrainingModeData clearServerAnsweredQuestion()
    {
      this.serverAnsweredQuestion_ = Collections.emptyList();
      return this;
    }
    
    public int getCachedSize()
    {
      if (this.cachedSize < 0) {
        getSerializedSize();
      }
      return this.cachedSize;
    }
    
    public int getClientAnsweredQuestionCount()
    {
      return this.clientAnsweredQuestion_.size();
    }
    
    public List<Training.QuestionWithEntry> getClientAnsweredQuestionList()
    {
      return this.clientAnsweredQuestion_;
    }
    
    public Sidekick.QuestionTemplates getQuestionTemplates()
    {
      return this.questionTemplates_;
    }
    
    public int getSerializedSize()
    {
      boolean bool = hasStringDictionary();
      int i = 0;
      if (bool) {
        i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getStringDictionary());
      }
      if (hasQuestionTemplates()) {
        i += CodedOutputStreamMicro.computeMessageSize(2, getQuestionTemplates());
      }
      Iterator localIterator1 = getServerAnsweredQuestionList().iterator();
      while (localIterator1.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(3, (Sidekick.Question)localIterator1.next());
      }
      Iterator localIterator2 = getClientAnsweredQuestionList().iterator();
      while (localIterator2.hasNext()) {
        i += CodedOutputStreamMicro.computeMessageSize(4, (Training.QuestionWithEntry)localIterator2.next());
      }
      this.cachedSize = i;
      return i;
    }
    
    public int getServerAnsweredQuestionCount()
    {
      return this.serverAnsweredQuestion_.size();
    }
    
    public List<Sidekick.Question> getServerAnsweredQuestionList()
    {
      return this.serverAnsweredQuestion_;
    }
    
    public Sidekick.StringDictionary getStringDictionary()
    {
      return this.stringDictionary_;
    }
    
    public boolean hasQuestionTemplates()
    {
      return this.hasQuestionTemplates;
    }
    
    public boolean hasStringDictionary()
    {
      return this.hasStringDictionary;
    }
    
    public TrainingModeData mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
          Sidekick.StringDictionary localStringDictionary = new Sidekick.StringDictionary();
          paramCodedInputStreamMicro.readMessage(localStringDictionary);
          setStringDictionary(localStringDictionary);
          break;
        case 18: 
          Sidekick.QuestionTemplates localQuestionTemplates = new Sidekick.QuestionTemplates();
          paramCodedInputStreamMicro.readMessage(localQuestionTemplates);
          setQuestionTemplates(localQuestionTemplates);
          break;
        case 26: 
          Sidekick.Question localQuestion = new Sidekick.Question();
          paramCodedInputStreamMicro.readMessage(localQuestion);
          addServerAnsweredQuestion(localQuestion);
          break;
        }
        Training.QuestionWithEntry localQuestionWithEntry = new Training.QuestionWithEntry();
        paramCodedInputStreamMicro.readMessage(localQuestionWithEntry);
        addClientAnsweredQuestion(localQuestionWithEntry);
      }
    }
    
    public TrainingModeData setQuestionTemplates(Sidekick.QuestionTemplates paramQuestionTemplates)
    {
      if (paramQuestionTemplates == null) {
        throw new NullPointerException();
      }
      this.hasQuestionTemplates = true;
      this.questionTemplates_ = paramQuestionTemplates;
      return this;
    }
    
    public TrainingModeData setStringDictionary(Sidekick.StringDictionary paramStringDictionary)
    {
      if (paramStringDictionary == null) {
        throw new NullPointerException();
      }
      this.hasStringDictionary = true;
      this.stringDictionary_ = paramStringDictionary;
      return this;
    }
    
    public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
      throws IOException
    {
      if (hasStringDictionary()) {
        paramCodedOutputStreamMicro.writeMessage(1, getStringDictionary());
      }
      if (hasQuestionTemplates()) {
        paramCodedOutputStreamMicro.writeMessage(2, getQuestionTemplates());
      }
      Iterator localIterator1 = getServerAnsweredQuestionList().iterator();
      while (localIterator1.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(3, (Sidekick.Question)localIterator1.next());
      }
      Iterator localIterator2 = getClientAnsweredQuestionList().iterator();
      while (localIterator2.hasNext()) {
        paramCodedOutputStreamMicro.writeMessage(4, (Training.QuestionWithEntry)localIterator2.next());
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.apps.sidekick.training.Training
 * JD-Core Version:    0.7.0.1
 */