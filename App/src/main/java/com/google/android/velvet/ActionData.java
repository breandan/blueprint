package com.google.android.velvet;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.search.core.discoursecontext.DiscourseContext;
import com.google.android.speech.embedded.TaggerResult;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.voicesearch.EffectOnWebResults;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.util.PumpkinActionFactory;
import com.google.audio.ears.proto.EarsService.EarsResultsResponse;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.ActionV2Metadata;
import com.google.majel.proto.ActionV2Protos.DeferredAction;
import com.google.majel.proto.ActionV2Protos.InteractionInfo;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.majel.proto.PeanutProtos.Text;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import com.google.speech.logs.VoicesearchClientLogProto.EmbeddedParserDetails;
import com.google.wireless.voicesearch.proto.CardMetdataProtos.CardMetadata;
import com.google.wireless.voicesearch.proto.CardMetdataProtos.LoggingUrls;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActionData
  implements Parcelable
{
  public static final ActionData ANSWER_IN_SRP = new ActionData(sLatestId.getAndIncrement(), true, null, null, null, null, null, null, null, EffectOnWebResults.NO_EFFECT, null);
  public static final Parcelable.Creator<ActionData> CREATOR = new Parcelable.Creator()
  {
    public ActionData createFromParcel(Parcel paramAnonymousParcel)
    {
      long l = paramAnonymousParcel.readLong();
      PeanutProtos.Peanut localPeanut = (PeanutProtos.Peanut)ActionData.readNullableProtoFromParcel(paramAnonymousParcel, PeanutProtos.Peanut.class);
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      TaggerResult localTaggerResult = (TaggerResult)paramAnonymousParcel.readParcelable(TaggerResult.class.getClassLoader());
      EarsService.EarsResultsResponse localEarsResultsResponse = (EarsService.EarsResultsResponse)ActionData.readNullableProtoFromParcel(paramAnonymousParcel, EarsService.EarsResultsResponse.class);
      RecognizeException localRecognizeException = (RecognizeException)paramAnonymousParcel.readSerializable();
      CardMetdataProtos.CardMetadata localCardMetadata = (CardMetdataProtos.CardMetadata)ActionData.readNullableProtoFromParcel(paramAnonymousParcel, CardMetdataProtos.CardMetadata.class);
      EffectOnWebResults localEffectOnWebResults = EffectOnWebResults.values()[paramAnonymousParcel.readInt()];
      String str3 = paramAnonymousParcel.readString();
      if ((localPeanut == null) && (str1 == null) && (str2 == null) && (localTaggerResult == null) && (localEarsResultsResponse == null) && (localRecognizeException == null)) {
        return ActionData.NONE;
      }
      return new ActionData(l, false, localPeanut, str1, str2, localTaggerResult, localEarsResultsResponse, localRecognizeException, localCardMetadata, localEffectOnWebResults, str3, null);
    }
    
    public ActionData[] newArray(int paramAnonymousInt)
    {
      return new ActionData[paramAnonymousInt];
    }
  };
  public static final ActionData NONE;
  private static AtomicLong sLatestId = new AtomicLong();
  @Nullable
  private final String mBaseUrl;
  @Nullable
  private final EarsService.EarsResultsResponse mEarsResponse;
  @Nonnull
  private final EffectOnWebResults mEffectOnWebResults;
  @Nullable
  private final String mEventId;
  @Nullable
  private final String mHtml;
  private final long mId;
  @Nullable
  private final CardMetdataProtos.CardMetadata mMetadata;
  @Nullable
  private final PeanutProtos.Peanut mPeanut;
  @Nullable
  private final TaggerResult mPumpkinTaggerResult;
  @Nullable
  private final RecognizeException mRecognizeException;
  
  static
  {
    NONE = new ActionData(sLatestId.getAndIncrement(), true, null, null, null, null, null, null, null, EffectOnWebResults.NO_EFFECT, null);
  }
  
  private ActionData(long paramLong, boolean paramBoolean, PeanutProtos.Peanut paramPeanut, String paramString1, String paramString2, TaggerResult paramTaggerResult, EarsService.EarsResultsResponse paramEarsResultsResponse, RecognizeException paramRecognizeException, CardMetdataProtos.CardMetadata paramCardMetadata, EffectOnWebResults paramEffectOnWebResults, String paramString3)
  {
    int j = 0;
    if (paramBoolean) {
      j = 0 + 1;
    }
    if (paramPeanut != null) {
      j++;
    }
    if ((paramString1 != null) && (paramString2 != null)) {
      j++;
    }
    if (paramTaggerResult != null) {
      j++;
    }
    if (paramEarsResultsResponse != null) {
      j++;
    }
    if (paramRecognizeException != null) {
      j++;
    }
    if (j == i) {}
    for (;;)
    {
      Preconditions.checkArgument(i);
      this.mId = paramLong;
      this.mPeanut = paramPeanut;
      this.mBaseUrl = paramString1;
      this.mHtml = paramString2;
      this.mPumpkinTaggerResult = paramTaggerResult;
      this.mEarsResponse = paramEarsResultsResponse;
      this.mRecognizeException = paramRecognizeException;
      this.mMetadata = paramCardMetadata;
      this.mEffectOnWebResults = paramEffectOnWebResults;
      this.mEventId = paramString3;
      return;
      i = 0;
    }
  }
  
  public static ActionData fromEarsResponse(EarsService.EarsResultsResponse paramEarsResultsResponse)
  {
    return new ActionData(sLatestId.getAndIncrement(), false, null, null, null, null, paramEarsResultsResponse, null, null, EffectOnWebResults.PREVENT, null);
  }
  
  public static ActionData fromHtml(@Nonnull String paramString1, @Nonnull String paramString2, @Nullable CardMetdataProtos.CardMetadata paramCardMetadata)
  {
    return new ActionData(sLatestId.getAndIncrement(), false, null, paramString1, paramString2, null, null, null, paramCardMetadata, EffectOnWebResults.NO_EFFECT, null);
  }
  
  public static ActionData fromPeanut(@Nonnull PeanutProtos.Peanut paramPeanut, @Nullable CardMetdataProtos.CardMetadata paramCardMetadata, @Nullable String paramString)
  {
    return new ActionData(sLatestId.getAndIncrement(), false, paramPeanut, null, null, null, null, null, paramCardMetadata, EffectOnWebResults.getPeanutEffect(paramPeanut), paramString);
  }
  
  public static ActionData fromPumpkinTaggerResult(TaggerResult paramTaggerResult)
  {
    return new ActionData(sLatestId.getAndIncrement(), false, null, null, null, paramTaggerResult, null, null, null, EffectOnWebResults.getPumpkinEffect(), null);
  }
  
  @Nullable
  private static <T extends MessageMicro> T readNullableProtoFromParcel(@Nonnull Parcel paramParcel, @Nonnull Class<T> paramClass)
  {
    try
    {
      if (paramParcel.readByte() == 1)
      {
        byte[] arrayOfByte = paramParcel.createByteArray();
        MessageMicro localMessageMicro = (MessageMicro)paramClass.newInstance();
        localMessageMicro.mergeFrom(arrayOfByte);
        return localMessageMicro;
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      VelvetStrictMode.logW("Velvet.ActionData", "Error reading proto", localIllegalAccessException);
      return null;
    }
    catch (InstantiationException localInstantiationException)
    {
      for (;;)
      {
        VelvetStrictMode.logW("Velvet.ActionData", "Error reading proto", localInstantiationException);
      }
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      for (;;)
      {
        VelvetStrictMode.logW("Velvet.ActionData", "Error reading proto", localInvalidProtocolBufferMicroException);
      }
    }
  }
  
  private static void writeNullableProtoToParcel(@Nonnull Parcel paramParcel, @Nullable MessageMicro paramMessageMicro)
  {
    if (paramMessageMicro == null)
    {
      paramParcel.writeByte((byte)0);
      return;
    }
    paramParcel.writeByte((byte)1);
    paramParcel.writeByteArray(paramMessageMicro.toByteArray());
  }
  
  public boolean canBeCompletedByFollowOn()
  {
    boolean bool1 = hasActionV2(0);
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = getActionV2(0).hasInteractionInfo();
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = getActionV2(0).getInteractionInfo().getIncomplete();
        bool2 = false;
        if (bool4) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    return ((paramObject instanceof ActionData)) && (((ActionData)paramObject).mId == this.mId);
  }
  
  public int getActionParserLog()
  {
    if (getPumpkinTaggerResult() != null) {
      return 93;
    }
    return 94;
  }
  
  public int getActionTypeLog(DiscourseContext paramDiscourseContext)
  {
    if (hasActionV2(0))
    {
      ActionV2Protos.ActionV2 localActionV2 = getActionV2(0);
      if (localActionV2.hasMetadata())
      {
        ActionV2Protos.ActionV2Metadata localActionV2Metadata = localActionV2.getMetadata();
        if (localActionV2Metadata.hasActionType()) {
          return localActionV2Metadata.getActionType();
        }
      }
      BugLogger.record(10632197);
      return 0;
    }
    if (this.mPumpkinTaggerResult != null) {
      return PumpkinActionFactory.getActionTypeLog(paramDiscourseContext, this.mPumpkinTaggerResult);
    }
    if (this.mEarsResponse != null) {
      return 4;
    }
    VelvetStrictMode.logW("Velvet.ActionData", "Cannot log action type");
    return 0;
  }
  
  @Nullable
  public ActionV2Protos.ActionV2 getActionV2(int paramInt)
  {
    if (hasPeanut()) {
      return this.mPeanut.getActionV2(paramInt);
    }
    return null;
  }
  
  @Nullable
  public String getBaseUrl()
  {
    return this.mBaseUrl;
  }
  
  @Nullable
  public CardMetdataProtos.CardMetadata getCardMetadata()
  {
    return this.mMetadata;
  }
  
  public long getCountdownDurationMs()
  {
    if (hasActionV2(0))
    {
      ActionV2Protos.ActionV2 localActionV2 = getActionV2(0);
      if (localActionV2.hasSuggestedDelayMs()) {
        return localActionV2.getSuggestedDelayMs();
      }
    }
    return 0L;
  }
  
  @Nullable
  public EarsService.EarsResultsResponse getEarsResponse()
  {
    return this.mEarsResponse;
  }
  
  @Nonnull
  public EffectOnWebResults getEffectOnWebResults()
  {
    return this.mEffectOnWebResults;
  }
  
  @Nullable
  public VoicesearchClientLogProto.EmbeddedParserDetails getEmbeddedParserDetails()
  {
    if (this.mPumpkinTaggerResult != null) {
      return this.mPumpkinTaggerResult.getEmbeddedParserDetails();
    }
    return null;
  }
  
  @Nullable
  public String getEventId()
  {
    return this.mEventId;
  }
  
  @Nullable
  public String getFollowOnPromptDisplayText()
  {
    if ((this.mPeanut != null) && (this.mPeanut.hasTextResponse())) {
      return this.mPeanut.getTextResponse().getDisplay();
    }
    return null;
  }
  
  @Nullable
  public String getHtml()
  {
    return this.mHtml;
  }
  
  public int getNumberOfAttempts()
  {
    boolean bool1 = hasActionV2(0);
    int i = 0;
    if (bool1)
    {
      ActionV2Protos.ActionV2 localActionV2 = getActionV2(0);
      boolean bool2 = localActionV2.hasDeferredActionExtension();
      i = 0;
      if (bool2)
      {
        ActionV2Protos.DeferredAction localDeferredAction = localActionV2.getDeferredActionExtension();
        boolean bool3 = localDeferredAction.hasNumberOfAttempts();
        i = 0;
        if (bool3) {
          i = localDeferredAction.getNumberOfAttempts();
        }
      }
    }
    return i;
  }
  
  @Nullable
  public PeanutProtos.Peanut getPeanut()
  {
    return this.mPeanut;
  }
  
  @Nullable
  public TaggerResult getPumpkinTaggerResult()
  {
    return this.mPumpkinTaggerResult;
  }
  
  @Nullable
  public RecognizeException getRecognizeException()
  {
    return this.mRecognizeException;
  }
  
  public boolean hasActionV2(int paramInt)
  {
    return (this.mPeanut != null) && (this.mPeanut.getActionV2Count() > paramInt);
  }
  
  public boolean hasHtml()
  {
    return this.mHtml != null;
  }
  
  public boolean hasPeanut()
  {
    return this.mPeanut != null;
  }
  
  public int hashCode()
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Long.valueOf(this.mId);
    return Objects.hashCode(arrayOfObject);
  }
  
  public boolean isEmpty()
  {
    return (this.mPeanut == null) && (this.mHtml == null) && (this.mPumpkinTaggerResult == null) && (this.mEarsResponse == null) && (this.mRecognizeException == null);
  }
  
  public boolean isFollowOnForPreviousAction()
  {
    boolean bool1;
    if ((!hasActionV2(0)) || (!getActionV2(0).hasInteractionInfo()) || (!getActionV2(0).getInteractionInfo().getIsFollowOn()))
    {
      TaggerResult localTaggerResult = this.mPumpkinTaggerResult;
      bool1 = false;
      if (localTaggerResult != null)
      {
        boolean bool2 = PumpkinActionFactory.isFollowOn(this.mPumpkinTaggerResult);
        bool1 = false;
        if (!bool2) {}
      }
    }
    else
    {
      bool1 = true;
    }
    return bool1;
  }
  
  public boolean isGwsLoggable()
  {
    return getCardMetadata() != null;
  }
  
  public boolean isNetworkAction()
  {
    return (this.mPeanut != null) || (this.mHtml != null) || (this.mEarsResponse != null);
  }
  
  public boolean shouldAutoExecute()
  {
    boolean bool1 = hasActionV2(0);
    boolean bool2 = false;
    if (bool1)
    {
      ActionV2Protos.ActionV2 localActionV2 = getActionV2(0);
      boolean bool3 = localActionV2.getExecute();
      bool2 = false;
      if (bool3)
      {
        boolean bool4 = localActionV2.hasSuggestedDelayMs();
        bool2 = false;
        if (bool4) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public boolean shouldLogShownToGws()
  {
    return (this.mMetadata != null) && (this.mMetadata.hasLoggingUrls()) && (!TextUtils.isEmpty(this.mMetadata.getLoggingUrls().getShowCardUrl()));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("ActionData{");
    localStringBuilder.append("id=").append(this.mId);
    if (this == NONE) {
      localStringBuilder.append(" NONE");
    }
    for (;;)
    {
      localStringBuilder.append(" FX=").append(this.mEffectOnWebResults);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
      if (this == ANSWER_IN_SRP)
      {
        localStringBuilder.append(" ANSWER_IN_SRP");
      }
      else
      {
        if (this.mPeanut != null)
        {
          localStringBuilder.append(" PEANUT(");
          localStringBuilder.append("ActionV2Count: ").append(this.mPeanut.getActionV2Count());
          localStringBuilder.append(")");
        }
        if (this.mHtml != null)
        {
          localStringBuilder.append(" HTML(");
          localStringBuilder.append("mBaseUrl").append(this.mBaseUrl).append("\", ");
          localStringBuilder.append("HTML length: ").append(this.mHtml.length()).append(")");
        }
        if (this.mPumpkinTaggerResult != null) {
          localStringBuilder.append(" PUMPKIN(").append(this.mPumpkinTaggerResult.getActionName()).append(")");
        }
        if (this.mEarsResponse != null) {
          localStringBuilder.append(" EARS");
        }
        if (this.mMetadata != null) {
          localStringBuilder.append(" METADATA");
        }
      }
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(this.mId);
    writeNullableProtoToParcel(paramParcel, this.mPeanut);
    paramParcel.writeString(this.mBaseUrl);
    paramParcel.writeString(this.mHtml);
    paramParcel.writeParcelable(this.mPumpkinTaggerResult, 0);
    writeNullableProtoToParcel(paramParcel, this.mEarsResponse);
    paramParcel.writeSerializable(this.mRecognizeException);
    writeNullableProtoToParcel(paramParcel, this.mMetadata);
    paramParcel.writeInt(this.mEffectOnWebResults.ordinal());
    paramParcel.writeString(this.mEventId);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ActionData
 * JD-Core Version:    0.7.0.1
 */