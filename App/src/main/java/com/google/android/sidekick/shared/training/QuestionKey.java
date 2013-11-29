package com.google.android.sidekick.shared.training;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Entity;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.Sports.SportTeamPlayer;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.StockQuotes.StockData;
import com.google.protobuf.micro.ByteStringMicro;
import java.util.Iterator;
import java.util.List;

public class QuestionKey
  implements Parcelable
{
  public static final Parcelable.Creator<QuestionKey> CREATOR = new Parcelable.Creator()
  {
    public QuestionKey createFromParcel(Parcel paramAnonymousParcel)
    {
      return new QuestionKey((Sidekick.Question)ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, Sidekick.Question.class));
    }
    
    public QuestionKey[] newArray(int paramAnonymousInt)
    {
      return new QuestionKey[paramAnonymousInt];
    }
  };
  private final Sidekick.Question mQuestion;
  
  public QuestionKey(Sidekick.Question paramQuestion)
  {
    Preconditions.checkNotNull(paramQuestion);
    this.mQuestion = paramQuestion;
  }
  
  private static boolean doParametersMatch(List<Sidekick.Question.Entity> paramList1, List<Sidekick.Question.Entity> paramList2)
  {
    if (paramList1.size() != paramList2.size()) {
      return false;
    }
    for (int i = 0;; i++)
    {
      if (i >= paramList1.size()) {
        break label136;
      }
      Sidekick.Question.Entity localEntity1 = (Sidekick.Question.Entity)paramList1.get(i);
      Sidekick.Question.Entity localEntity2 = (Sidekick.Question.Entity)paramList2.get(i);
      if ((localEntity1.hasSportTeamPlayer() != localEntity2.hasSportTeamPlayer()) || ((localEntity1.hasSportTeamPlayer()) && (!Objects.equal(localEntity1.getSportTeamPlayer().getFreebaseId(), localEntity2.getSportTeamPlayer().getFreebaseId()))) || (localEntity1.hasStockData() != localEntity2.hasStockData()) || ((localEntity1.hasStockData()) && (localEntity1.getStockData().getGin() != localEntity2.getStockData().getGin()))) {
        break;
      }
    }
    label136:
    return true;
  }
  
  private static int parametersHashCode(List<Sidekick.Question.Entity> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Question.Entity localEntity = (Sidekick.Question.Entity)localIterator.next();
      if ((localEntity.hasSportTeamPlayer()) && (localEntity.getSportTeamPlayer().hasFreebaseId())) {
        i ^= localEntity.getSportTeamPlayer().getFreebaseId().hashCode();
      } else if (localEntity.hasStockData()) {
        i = (int)(i ^ localEntity.getStockData().getGin() << 7);
      }
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    Sidekick.Question localQuestion;
    do
    {
      return true;
      if (!(paramObject instanceof QuestionKey)) {
        return false;
      }
      localQuestion = ((QuestionKey)paramObject).mQuestion;
    } while ((Objects.equal(this.mQuestion.getFingerprint(), localQuestion.getFingerprint())) && (doParametersMatch(this.mQuestion.getParameterList(), localQuestion.getParameterList())));
    return false;
  }
  
  public int hashCode()
  {
    return this.mQuestion.getFingerprint().hashCode() ^ parametersHashCode(this.mQuestion.getParameterList()) << 5;
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("templateId", this.mQuestion.getTemplateId()).add("fingerprint", this.mQuestion.getFingerprint()).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    ProtoParcelable.writeProtoToParcel(this.mQuestion, paramParcel);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.QuestionKey
 * JD-Core Version:    0.7.0.1
 */