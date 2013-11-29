package com.google.android.sidekick.shared.remoteapi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import com.google.geo.sidekick.Sidekick.Question.Entity;
import com.google.geo.sidekick.Sidekick.QuestionTemplate;
import com.google.geo.sidekick.Sidekick.QuestionTemplate.ClientActionWithIcon;
import com.google.geo.sidekick.Sidekick.QuestionTemplate.YesNoQuestionClientAction;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class TrainingQuestion
  implements Parcelable, Function<Sidekick.QuestionTemplate.ClientActionWithIcon, Option>
{
  public static final Parcelable.Creator<TrainingQuestion> CREATOR = new Parcelable.Creator()
  {
    public TrainingQuestion createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      HashMap localHashMap = Maps.newHashMapWithExpectedSize(i);
      for (int j = 0; j < i; j++) {
        localHashMap.put(paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
      }
      return new TrainingQuestion(localHashMap, (Sidekick.QuestionTemplate)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.QuestionTemplate.class), (Sidekick.Question)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Question.class));
    }
    
    public TrainingQuestion[] newArray(int paramAnonymousInt)
    {
      return new TrainingQuestion[paramAnonymousInt];
    }
  };
  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(%+)(\\d+)");
  private Sidekick.Question mQuestion;
  private final Sidekick.QuestionTemplate mQuestionTemplate;
  private final Map<String, String> mStringDictionary;
  
  public TrainingQuestion(Map<String, String> paramMap, Sidekick.QuestionTemplate paramQuestionTemplate, Sidekick.Question paramQuestion)
  {
    Preconditions.checkNotNull(paramMap);
    Preconditions.checkNotNull(paramQuestionTemplate);
    Preconditions.checkNotNull(paramQuestion);
    this.mStringDictionary = paramMap;
    this.mQuestionTemplate = paramQuestionTemplate;
    this.mQuestion = paramQuestion;
  }
  
  private Option convertToOption(Sidekick.QuestionTemplate.ClientActionWithIcon paramClientActionWithIcon)
  {
    boolean bool = paramClientActionWithIcon.hasDisplayStringKey();
    String str = null;
    if (bool)
    {
      str = (String)this.mStringDictionary.get(paramClientActionWithIcon.getDisplayStringKey());
      if (str == null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramClientActionWithIcon.getDisplayStringKey();
        Log.e("Sidekick_TrainingQuestion", String.format("Dictionary missing string for key %s", arrayOfObject));
      }
    }
    int i = paramClientActionWithIcon.getIcon();
    if (paramClientActionWithIcon.hasClientAction()) {}
    for (Integer localInteger = Integer.valueOf(paramClientActionWithIcon.getClientAction());; localInteger = null) {
      return new Option(str, i, localInteger);
    }
  }
  
  private Map<String, String> extractRequiredStrings(Map<String, String> paramMap)
  {
    HashMap localHashMap = Maps.newHashMap();
    if (this.mQuestionTemplate.hasQuestionStringKey())
    {
      String str4 = this.mQuestionTemplate.getQuestionStringKey();
      localHashMap.put(str4, paramMap.get(str4));
    }
    if (this.mQuestionTemplate.hasJustificationStringKey())
    {
      String str3 = this.mQuestionTemplate.getJustificationStringKey();
      localHashMap.put(str3, paramMap.get(str3));
    }
    Iterator localIterator1 = this.mQuestionTemplate.getMultipleChoiceAnswerList().iterator();
    while (localIterator1.hasNext())
    {
      String str2 = ((Sidekick.QuestionTemplate.ClientActionWithIcon)localIterator1.next()).getDisplayStringKey();
      localHashMap.put(str2, paramMap.get(str2));
    }
    Iterator localIterator2 = this.mQuestionTemplate.getClientOnlyActionList().iterator();
    while (localIterator2.hasNext())
    {
      String str1 = ((Sidekick.QuestionTemplate.ClientActionWithIcon)localIterator2.next()).getDisplayStringKey();
      localHashMap.put(str1, paramMap.get(str1));
    }
    return localHashMap;
  }
  
  @Nullable
  private String getFullyFormedString(String paramString, List<Sidekick.Question.Entity> paramList)
  {
    String str = (String)this.mStringDictionary.get(paramString);
    if (str == null)
    {
      Log.e("Sidekick_TrainingQuestion", String.format("Dictionary missing string for key %s", new Object[] { paramString }));
      return null;
    }
    return injectParameters(str, paramList);
  }
  
  public static boolean hasUserInputParameter(Sidekick.Question paramQuestion)
  {
    Iterator localIterator = paramQuestion.getParameterList().iterator();
    while (localIterator.hasNext()) {
      if (((Sidekick.Question.Entity)localIterator.next()).getSource() == 2) {
        return true;
      }
    }
    return false;
  }
  
  static String injectParameters(String paramString, List<Sidekick.Question.Entity> paramList)
  {
    Matcher localMatcher = PLACEHOLDER_PATTERN.matcher(paramString);
    int i = 0;
    StringBuilder localStringBuilder = new StringBuilder();
    if (localMatcher.find())
    {
      localStringBuilder.append(paramString.substring(i, localMatcher.start()));
      String str = localMatcher.group(1);
      int j;
      if (str.length() % 2 == 1)
      {
        j = -1 + Integer.parseInt(localMatcher.group(2));
        if ((j >= 0) && (j < paramList.size()))
        {
          localStringBuilder.append(unescape(str.substring(0, -1 + str.length())));
          localStringBuilder.append(((Sidekick.Question.Entity)paramList.get(j)).getValue());
        }
      }
      for (;;)
      {
        i = localMatcher.end();
        break;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(j);
        arrayOfObject[1] = paramString;
        Log.e("Sidekick_TrainingQuestion", String.format("Missing value for index %d of string: %s", arrayOfObject));
        localStringBuilder.append(unescape(localMatcher.group()));
        continue;
        localStringBuilder.append(unescape(localMatcher.group()));
      }
    }
    localStringBuilder.append(unescape(paramString.substring(i)));
    return localStringBuilder.toString();
  }
  
  private static String unescape(String paramString)
  {
    return paramString.replaceAll("%%", "%");
  }
  
  public Option apply(Sidekick.QuestionTemplate.ClientActionWithIcon paramClientActionWithIcon)
  {
    return convertToOption(paramClientActionWithIcon);
  }
  
  public TrainingQuestion createQuestionWithParam(Sidekick.Question.Entity paramEntity)
  {
    Sidekick.Question localQuestion = new Sidekick.Question();
    for (;;)
    {
      int i;
      try
      {
        localQuestion.mergeFrom(this.mQuestion.toByteArray());
        i = 0;
        Iterator localIterator = localQuestion.getParameterList().iterator();
        if (localIterator.hasNext())
        {
          if (((Sidekick.Question.Entity)localIterator.next()).getSource() == 2) {
            localQuestion.setParameter(i, paramEntity);
          }
        }
        else {
          return new TrainingQuestion(this.mStringDictionary, this.mQuestionTemplate, localQuestion);
        }
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
      {
        Log.e("Sidekick_TrainingQuestion", "Failed to parse the original question");
        throw new RuntimeException("Failed to parse the original question", localInvalidProtocolBufferMicroException);
      }
      i++;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Iterable<ActionQuestionOption> getActionQuestionOptions()
  {
    ArrayList localArrayList = Lists.newArrayList();
    Iterator localIterator1 = this.mQuestionTemplate.getActionCompanionList().iterator();
    Iterator localIterator2 = this.mQuestion.getActionList().iterator();
    if ((localIterator1.hasNext()) && (localIterator2.hasNext()))
    {
      Sidekick.QuestionTemplate.ClientActionWithIcon localClientActionWithIcon = (Sidekick.QuestionTemplate.ClientActionWithIcon)localIterator1.next();
      Sidekick.Action localAction = (Sidekick.Action)localIterator2.next();
      int i = localClientActionWithIcon.getIcon();
      if (localClientActionWithIcon.hasClientAction()) {}
      for (Integer localInteger = Integer.valueOf(localClientActionWithIcon.getClientAction());; localInteger = null)
      {
        localArrayList.add(new ActionQuestionOption(localAction, i, localInteger));
        break;
      }
    }
    return localArrayList;
  }
  
  @Nullable
  public Sidekick.Question.Answer getAnswer()
  {
    return this.mQuestion.getAnswer();
  }
  
  public Collection<Integer> getAttributes()
  {
    return this.mQuestionTemplate.getAttributeList();
  }
  
  @Nullable
  public Integer getClientAction(Sidekick.Question.Answer paramAnswer)
  {
    switch (this.mQuestionTemplate.getType())
    {
    }
    Sidekick.QuestionTemplate.ClientActionWithIcon localClientActionWithIcon;
    do
    {
      Sidekick.QuestionTemplate.YesNoQuestionClientAction localYesNoQuestionClientAction;
      do
      {
        do
        {
          do
          {
            return null;
            Preconditions.checkArgument(paramAnswer.hasYesNoAnswer());
          } while (!this.mQuestionTemplate.hasYesNoQuestionClientAction());
          localYesNoQuestionClientAction = this.mQuestionTemplate.getYesNoQuestionClientAction();
          if (!paramAnswer.getYesNoAnswer()) {
            break;
          }
        } while (!localYesNoQuestionClientAction.hasYesAction());
        return Integer.valueOf(localYesNoQuestionClientAction.getYesAction());
      } while (!localYesNoQuestionClientAction.hasNoAction());
      return Integer.valueOf(localYesNoQuestionClientAction.getNoAction());
      Preconditions.checkArgument(paramAnswer.hasMultipleChoiceAnswer());
      localClientActionWithIcon = this.mQuestionTemplate.getMultipleChoiceAnswer(paramAnswer.getMultipleChoiceAnswer());
    } while (!localClientActionWithIcon.hasClientAction());
    return Integer.valueOf(localClientActionWithIcon.getClientAction());
  }
  
  @Nullable
  public Iterable<Option> getClientActionOptions()
  {
    if (this.mQuestionTemplate.getClientOnlyActionCount() == 0) {
      return null;
    }
    return Iterables.transform(this.mQuestionTemplate.getClientOnlyActionList(), this);
  }
  
  @Nullable
  public Integer getFulfillAction()
  {
    if (this.mQuestionTemplate.hasFulfillAction()) {
      return Integer.valueOf(this.mQuestionTemplate.getFulfillAction());
    }
    return null;
  }
  
  @Nullable
  public String getJustificationString()
  {
    if (!this.mQuestionTemplate.hasJustificationStringKey()) {
      return null;
    }
    return getFullyFormedString(this.mQuestionTemplate.getJustificationStringKey(), this.mQuestion.getJustificationParameterList());
  }
  
  public Option getMultipleChoiceOption(int paramInt)
  {
    return convertToOption(this.mQuestionTemplate.getMultipleChoiceAnswer(paramInt));
  }
  
  @Nullable
  public Iterable<Option> getMultipleChoiceOptions()
  {
    if (this.mQuestionTemplate.getMultipleChoiceAnswerCount() == 0) {
      return null;
    }
    return Iterables.transform(this.mQuestionTemplate.getMultipleChoiceAnswerList(), this);
  }
  
  @Nullable
  public Sidekick.Question.Entity getPrimaryEntity()
  {
    if (this.mQuestion.getParameterCount() > 0) {
      return this.mQuestion.getParameter(0);
    }
    return null;
  }
  
  public Sidekick.Question getQuestion()
  {
    return this.mQuestion;
  }
  
  @Nullable
  public String getQuestionString()
  {
    if (!this.mQuestionTemplate.hasQuestionStringKey())
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(this.mQuestionTemplate.getId());
      Log.e("Sidekick_TrainingQuestion", String.format("Question template %d missing question key", arrayOfObject));
      return null;
    }
    return getFullyFormedString(this.mQuestionTemplate.getQuestionStringKey(), this.mQuestion.getParameterList());
  }
  
  public int getType()
  {
    int i = this.mQuestionTemplate.getType();
    if ((i == 1) && (hasUserInputParameter(this.mQuestion))) {}
    switch (this.mQuestionTemplate.getFulfillAction())
    {
    default: 
      return i;
    case 2: 
      return -1;
    }
    return -2;
  }
  
  public boolean isAnswerable()
  {
    switch (getType())
    {
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    default: 
      return true;
    }
    return false;
  }
  
  public void updateAnswer(Sidekick.Question.Answer paramAnswer)
  {
    try
    {
      this.mQuestion = Sidekick.Question.parseFrom(this.mQuestion.toByteArray());
      this.mQuestion.setAnswer(paramAnswer);
      return;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e("Sidekick_TrainingQuestion", "Failed to clone question", localInvalidProtocolBufferMicroException);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Map localMap = extractRequiredStrings(this.mStringDictionary);
    paramParcel.writeInt(localMap.size());
    Iterator localIterator = localMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)localEntry.getKey());
      paramParcel.writeString((String)localEntry.getValue());
    }
    ProtoParcelable.writeProtoToParcel(this.mQuestionTemplate, paramParcel);
    ProtoParcelable.writeProtoToParcel(this.mQuestion, paramParcel);
  }
  
  public static class ActionQuestionOption
  {
    private final Sidekick.Action mAction;
    @Nullable
    private final Integer mClientAction;
    private final int mIconType;
    
    public ActionQuestionOption(Sidekick.Action paramAction, int paramInt, @Nullable Integer paramInteger)
    {
      this.mAction = paramAction;
      this.mIconType = paramInt;
      this.mClientAction = paramInteger;
    }
    
    public Sidekick.Action getAction()
    {
      return this.mAction;
    }
    
    @Nullable
    public Integer getClientAction()
    {
      return this.mClientAction;
    }
    
    public int getIconType()
    {
      return this.mIconType;
    }
  }
  
  public static class Option
  {
    @Nullable
    private final Integer mClientAction;
    @Nullable
    private final String mDisplayString;
    private final int mIconType;
    
    Option(@Nullable String paramString, int paramInt, @Nullable Integer paramInteger)
    {
      this.mDisplayString = paramString;
      this.mIconType = paramInt;
      this.mClientAction = paramInteger;
    }
    
    @Nullable
    public Integer getClientAction()
    {
      return this.mClientAction;
    }
    
    @Nullable
    public String getDisplayString()
    {
      return this.mDisplayString;
    }
    
    public int getIconType()
    {
      return this.mIconType;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.remoteapi.TrainingQuestion
 * JD-Core Version:    0.7.0.1
 */