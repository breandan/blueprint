package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.voicesearch.util.ExampleContactHelper.Contact;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.majel.proto.ActionV2Protos.HelpAction.Feature.Example;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HelpAction
  implements VoiceAction
{
  public static final Parcelable.Creator<HelpAction> CREATOR = new Parcelable.Creator()
  {
    public HelpAction createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      if (paramAnonymousParcel.readByte() == 1) {
        return new HelpAction(str, paramAnonymousParcel.readString());
      }
      int i = paramAnonymousParcel.readInt();
      ArrayList localArrayList1 = Lists.newArrayListWithCapacity(i);
      int j = 0;
      for (;;)
      {
        if (j < i)
        {
          ActionV2Protos.HelpAction.Feature.Example localExample = new ActionV2Protos.HelpAction.Feature.Example();
          try
          {
            localExample.mergeFrom(paramAnonymousParcel.createByteArray());
            localArrayList1.add(localExample);
            j++;
          }
          catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
          {
            for (;;)
            {
              localInvalidProtocolBufferMicroException.printStackTrace();
            }
          }
        }
      }
      int k = paramAnonymousParcel.readInt();
      HashMap localHashMap = Maps.newHashMapWithExpectedSize(k);
      for (int m = 0; m < k; m++)
      {
        ArrayList localArrayList2 = Lists.newArrayList();
        int n = paramAnonymousParcel.readInt();
        paramAnonymousParcel.readTypedList(localArrayList2, ExampleContactHelper.Contact.CREATOR);
        localHashMap.put(Integer.valueOf(n), localArrayList2);
      }
      return new HelpAction(str, localArrayList1, localHashMap);
    }
    
    public HelpAction[] newArray(int paramAnonymousInt)
    {
      return new HelpAction[paramAnonymousInt];
    }
  };
  private final Map<Integer, List<ExampleContactHelper.Contact>> mExampleContacts;
  private final List<ActionV2Protos.HelpAction.Feature.Example> mExamples;
  private final String mHeadline;
  private final String mIntroductionMessage;
  
  public HelpAction(String paramString1, String paramString2)
  {
    this.mExamples = null;
    this.mExampleContacts = null;
    this.mHeadline = ((String)Preconditions.checkNotNull(paramString1));
    this.mIntroductionMessage = ((String)Preconditions.checkNotNull(paramString2));
  }
  
  public HelpAction(String paramString, List<ActionV2Protos.HelpAction.Feature.Example> paramList, Map<Integer, List<ExampleContactHelper.Contact>> paramMap)
  {
    this.mExamples = ((List)Preconditions.checkNotNull(paramList));
    this.mExampleContacts = ((Map)Preconditions.checkNotNull(paramMap));
    this.mHeadline = ((String)Preconditions.checkNotNull(paramString));
    this.mIntroductionMessage = null;
    boolean bool1;
    if (!this.mExamples.isEmpty())
    {
      bool1 = true;
      Preconditions.checkState(bool1);
      Iterator localIterator = this.mExampleContacts.values().iterator();
      label78:
      if (!localIterator.hasNext()) {
        return;
      }
      if (((List)localIterator.next()).isEmpty()) {
        break label123;
      }
    }
    label123:
    for (boolean bool2 = true;; bool2 = false)
    {
      Preconditions.checkState(bool2);
      break label78;
      bool1 = false;
      break;
    }
  }
  
  public static int getContactSubstitutionType(ActionV2Protos.HelpAction.Feature.Example paramExample)
  {
    Iterator localIterator = paramExample.getSubstitutionList().iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      if (i == 3) {
        return 1;
      }
      if (i == 4) {
        return 2;
      }
    }
    return 0;
  }
  
  public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor)
  {
    return paramVoiceActionVisitor.visit(this);
  }
  
  public boolean canExecute()
  {
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Map<Integer, List<ExampleContactHelper.Contact>> getExampleContacts()
  {
    if (!isIntroduction()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return this.mExampleContacts;
    }
  }
  
  public List<ActionV2Protos.HelpAction.Feature.Example> getExampleList()
  {
    if (!isIntroduction()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return this.mExamples;
    }
  }
  
  public String getHeadline()
  {
    return this.mHeadline;
  }
  
  public String getIntroductionMessage()
  {
    Preconditions.checkState(isIntroduction());
    return this.mIntroductionMessage;
  }
  
  public boolean isIntroduction()
  {
    return this.mExamples == null;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.mHeadline);
    if (this.mExamples != null)
    {
      paramParcel.writeByte((byte)0);
      paramParcel.writeInt(this.mExamples.size());
      Iterator localIterator1 = this.mExamples.iterator();
      while (localIterator1.hasNext()) {
        paramParcel.writeByteArray(((ActionV2Protos.HelpAction.Feature.Example)localIterator1.next()).toByteArray());
      }
      paramParcel.writeInt(this.mExampleContacts.size());
      Iterator localIterator2 = this.mExampleContacts.entrySet().iterator();
      while (localIterator2.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator2.next();
        paramParcel.writeInt(((Integer)localEntry.getKey()).intValue());
        paramParcel.writeTypedList((List)localEntry.getValue());
      }
    }
    paramParcel.writeByte((byte)1);
    paramParcel.writeString(this.mIntroductionMessage);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.action.HelpAction
 * JD-Core Version:    0.7.0.1
 */