package com.google.android.sidekick.main.training;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.apps.sidekick.calendar.Calendar.CalendarInfo;
import com.google.android.apps.sidekick.training.Training.QuestionWithEntry;
import com.google.android.apps.sidekick.training.Training.TrainingModeData;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.sidekick.main.actions.SendTrainingQuestionActionTask;
import com.google.android.sidekick.main.calendar.CalendarDataProvider;
import com.google.android.sidekick.main.file.FileBackedProto;
import com.google.android.sidekick.main.file.FileBackedProto.ReadModifyWrite;
import com.google.android.sidekick.main.file.FileBytesReader;
import com.google.android.sidekick.main.file.FileBytesWriter;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.TrainingQuestionManager;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestionNode;
import com.google.android.sidekick.shared.training.QuestionKey;
import com.google.android.sidekick.shared.training.TrainingRequestHelper;
import com.google.android.sidekick.shared.util.CalendarDataUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.velvet.VelvetBackgroundTasks;
import com.google.android.velvet.presenter.FirstUseCardHandler;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsQuery;
import com.google.geo.sidekick.Sidekick.ActionsResponse;
import com.google.geo.sidekick.Sidekick.AnsweredQuestions;
import com.google.geo.sidekick.Sidekick.AnsweredQuestionsMetadata;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Entity;
import com.google.geo.sidekick.Sidekick.QuestionNode;
import com.google.geo.sidekick.Sidekick.QuestionTemplate;
import com.google.geo.sidekick.Sidekick.QuestionTemplates;
import com.google.geo.sidekick.Sidekick.RequestPayload;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import com.google.geo.sidekick.Sidekick.StringDictionary;
import com.google.geo.sidekick.Sidekick.StringDictionary.DictionaryEntry;
import com.google.geo.sidekick.Sidekick.TrainingModeDataQuery;
import com.google.geo.sidekick.Sidekick.TrainingModeDataResponse;
import com.google.geo.sidekick.Sidekick.TrainingModeMetadata;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class TrainingQuestionManagerImpl
  implements TrainingQuestionManager
{
  private static final Supplier<Training.TrainingModeData> PROTO_FACTORY = new Supplier()
  {
    public Training.TrainingModeData get()
    {
      return new Training.TrainingModeData();
    }
  };
  private static final String TAG = Tag.getTag(TrainingQuestionManagerImpl.class);
  private final VelvetBackgroundTasks mBackgroundTasks;
  private final Executor mBgExecutor;
  private final CalendarDataProvider mCalendarProvider;
  private Map<QuestionKey, Training.QuestionWithEntry> mClientAnsweredQuestions;
  private final Clock mClock;
  private final FirstUseCardHandler mFirstUseCardHandler;
  private final AtomicBoolean mInitializedCalled = new AtomicBoolean();
  private final CountDownLatch mInitializedLatch = new CountDownLatch(1);
  private final Object mLock = new Object();
  private final NetworkClient mNetworkClient;
  private final NowOptInSettings mNowOptInSettings;
  private final Map<QuestionKey, Training.QuestionWithEntry> mPendingAnsweredQuestions;
  private Map<Long, Sidekick.QuestionTemplate> mQuestionTemplates;
  private Map<QuestionKey, Sidekick.Question> mServerAnsweredQuestions;
  private Map<String, String> mStringDictionary;
  private final FileBackedProto<Training.TrainingModeData> mTrainingModeData;
  
  public TrainingQuestionManagerImpl(FileBytesReader paramFileBytesReader, FileBytesWriter paramFileBytesWriter, NetworkClient paramNetworkClient, Clock paramClock, Executor paramExecutor, VelvetBackgroundTasks paramVelvetBackgroundTasks, FirstUseCardHandler paramFirstUseCardHandler, CalendarDataProvider paramCalendarDataProvider, NowOptInSettings paramNowOptInSettings)
  {
    this.mTrainingModeData = new FileBackedProto(PROTO_FACTORY, "training_question", paramFileBytesReader, paramFileBytesWriter, true);
    this.mNetworkClient = paramNetworkClient;
    this.mClock = paramClock;
    this.mPendingAnsweredQuestions = Maps.newHashMap();
    this.mBgExecutor = paramExecutor;
    this.mBackgroundTasks = paramVelvetBackgroundTasks;
    this.mFirstUseCardHandler = paramFirstUseCardHandler;
    this.mCalendarProvider = paramCalendarDataProvider;
    this.mNowOptInSettings = paramNowOptInSettings;
  }
  
  @Nullable
  private TrainingQuestionNode augmentCalendarData(TrainingQuestionNode paramTrainingQuestionNode, Collection<Calendar.CalendarInfo> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    Iterator localIterator1 = paramTrainingQuestionNode.getQuestion().getQuestion().getParameterList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Question.Entity localEntity = (Sidekick.Question.Entity)localIterator1.next();
      if (localEntity.hasCalendarAccountHash())
      {
        String str = localEntity.getCalendarAccountHash();
        Iterator localIterator2 = paramCollection.iterator();
        Calendar.CalendarInfo localCalendarInfo;
        do
        {
          if (!localIterator2.hasNext()) {
            break;
          }
          localCalendarInfo = (Calendar.CalendarInfo)localIterator2.next();
        } while (!str.equals(CalendarDataUtil.getHashString(localCalendarInfo.getId())));
        localEntity.setValue(localCalendarInfo.getDisplayName());
      }
    }
    return paramTrainingQuestionNode;
    return null;
  }
  
  @Nullable
  private TrainingQuestion convertQuestion(Sidekick.Question paramQuestion)
  {
    if (!paramQuestion.hasFingerprint())
    {
      Log.e(TAG, "Question missing fingerprint");
      return null;
    }
    Sidekick.QuestionTemplate localQuestionTemplate = (Sidekick.QuestionTemplate)this.mQuestionTemplates.get(Long.valueOf(paramQuestion.getTemplateId()));
    if (localQuestionTemplate == null)
    {
      Log.e(TAG, "Missing question template: " + paramQuestion.getTemplateId());
      return null;
    }
    QuestionKey localQuestionKey = new QuestionKey(paramQuestion);
    Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)this.mPendingAnsweredQuestions.get(localQuestionKey);
    if (localQuestionWithEntry == null) {
      localQuestionWithEntry = (Training.QuestionWithEntry)this.mClientAnsweredQuestions.get(localQuestionKey);
    }
    localQuestion1 = null;
    if (localQuestionWithEntry != null) {
      localQuestion1 = localQuestionWithEntry.getQuestion();
    }
    if (localQuestion1 == null) {
      localQuestion1 = (Sidekick.Question)this.mServerAnsweredQuestions.get(localQuestionKey);
    }
    localObject = paramQuestion;
    if (localQuestion1 != null) {}
    try
    {
      Sidekick.Question localQuestion2 = Sidekick.Question.parseFrom(paramQuestion.toByteArray());
      localQuestion2.setAnswer(localQuestion1.getAnswer());
      if (localQuestion1.hasAnswerTimestampMillis()) {
        localQuestion2.setAnswerTimestampMillis(localQuestion1.getAnswerTimestampMillis());
      }
      localObject = localQuestion2;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      for (;;)
      {
        Log.e(TAG, "Failed to clone question", localInvalidProtocolBufferMicroException);
        localObject = localQuestion1;
      }
    }
    return new TrainingQuestion(this.mStringDictionary, localQuestionTemplate, (Sidekick.Question)localObject);
  }
  
  private void flushPendingAnswers()
  {
    ExtraPreconditions.checkNotMainThread();
    this.mTrainingModeData.doReadModifyWrite(new FileBackedProto.ReadModifyWrite()
    {
      public Training.TrainingModeData readModifyMaybeWrite(Training.TrainingModeData paramAnonymousTrainingModeData)
      {
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator1 = paramAnonymousTrainingModeData.getClientAnsweredQuestionList().iterator();
        while (localIterator1.hasNext())
        {
          Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)localIterator1.next();
          localHashMap.put(new QuestionKey(localQuestionWithEntry.getQuestion()), localQuestionWithEntry);
        }
        synchronized (TrainingQuestionManagerImpl.this.mLock)
        {
          localHashMap.putAll(TrainingQuestionManagerImpl.this.mPendingAnsweredQuestions);
          if (TrainingQuestionManagerImpl.this.mClientAnsweredQuestions != null) {
            TrainingQuestionManagerImpl.this.mClientAnsweredQuestions.putAll(TrainingQuestionManagerImpl.this.mPendingAnsweredQuestions);
          }
          TrainingQuestionManagerImpl.this.mPendingAnsweredQuestions.clear();
          paramAnonymousTrainingModeData.clearClientAnsweredQuestion();
          Iterator localIterator2 = localHashMap.values().iterator();
          if (localIterator2.hasNext()) {
            paramAnonymousTrainingModeData.addClientAnsweredQuestion((Training.QuestionWithEntry)localIterator2.next());
          }
        }
        return paramAnonymousTrainingModeData;
      }
    });
  }
  
  @Nullable
  private static Long getMostRecentAnsweredQuestionTimestamp(List<Sidekick.Question> paramList)
  {
    Long localLong = null;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Sidekick.Question localQuestion = (Sidekick.Question)localIterator.next();
      if (localQuestion.hasAnswerTimestampMillis())
      {
        long l = localQuestion.getAnswerTimestampMillis();
        if ((localLong == null) || (l > localLong.longValue())) {
          localLong = Long.valueOf(l);
        }
      }
    }
    return localLong;
  }
  
  private void initialize()
  {
    
    if (!this.mInitializedCalled.getAndSet(true))
    {
      refreshFromTrainingModeData((Training.TrainingModeData)this.mTrainingModeData.getData());
      this.mInitializedLatch.countDown();
    }
    try
    {
      this.mInitializedLatch.await();
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w(TAG, "Initialization latch wait interrupted");
      Thread.currentThread().interrupt();
    }
  }
  
  private boolean isInitialized()
  {
    return this.mInitializedLatch.getCount() == 0L;
  }
  
  @Nullable
  private Collection<Calendar.CalendarInfo> maybeLoadCalendarInfo(Collection<Sidekick.QuestionNode> paramCollection)
  {
    Iterator localIterator2;
    do
    {
      Iterator localIterator1 = paramCollection.iterator();
      while (!localIterator2.hasNext())
      {
        if (!localIterator1.hasNext()) {
          break;
        }
        localIterator2 = ((Sidekick.QuestionNode)localIterator1.next()).getQuestion().getParameterList().iterator();
      }
    } while (!((Sidekick.Question.Entity)localIterator2.next()).hasCalendarAccountHash());
    return this.mCalendarProvider.getCalendarsList();
    return null;
  }
  
  private static Collection<Sidekick.Question> mergeAnsweredQuestions(List<Sidekick.Question> paramList1, List<Sidekick.Question> paramList2)
  {
    HashMap localHashMap = Maps.newHashMapWithExpectedSize(paramList1.size() + paramList2.size());
    Iterator localIterator1 = paramList1.iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Question localQuestion2 = (Sidekick.Question)localIterator1.next();
      localHashMap.put(new QuestionKey(localQuestion2), localQuestion2);
    }
    Iterator localIterator2 = paramList2.iterator();
    while (localIterator2.hasNext())
    {
      Sidekick.Question localQuestion1 = (Sidekick.Question)localIterator2.next();
      localHashMap.put(new QuestionKey(localQuestion1), localQuestion1);
    }
    return localHashMap.values();
  }
  
  private void refreshFromTrainingModeData(Training.TrainingModeData paramTrainingModeData)
  {
    synchronized (this.mLock)
    {
      this.mStringDictionary = Maps.newHashMap();
      if (paramTrainingModeData.hasStringDictionary())
      {
        Iterator localIterator4 = paramTrainingModeData.getStringDictionary().getEntryList().iterator();
        if (localIterator4.hasNext())
        {
          Sidekick.StringDictionary.DictionaryEntry localDictionaryEntry = (Sidekick.StringDictionary.DictionaryEntry)localIterator4.next();
          this.mStringDictionary.put(localDictionaryEntry.getKey(), localDictionaryEntry.getValue());
        }
      }
    }
    this.mQuestionTemplates = Maps.newHashMap();
    if (paramTrainingModeData.hasQuestionTemplates())
    {
      Iterator localIterator3 = paramTrainingModeData.getQuestionTemplates().getTemplateList().iterator();
      while (localIterator3.hasNext())
      {
        Sidekick.QuestionTemplate localQuestionTemplate = (Sidekick.QuestionTemplate)localIterator3.next();
        this.mQuestionTemplates.put(Long.valueOf(localQuestionTemplate.getId()), localQuestionTemplate);
      }
    }
    this.mServerAnsweredQuestions = Maps.newHashMapWithExpectedSize(paramTrainingModeData.getServerAnsweredQuestionCount());
    Iterator localIterator1 = paramTrainingModeData.getServerAnsweredQuestionList().iterator();
    while (localIterator1.hasNext())
    {
      Sidekick.Question localQuestion = (Sidekick.Question)localIterator1.next();
      this.mServerAnsweredQuestions.put(new QuestionKey(localQuestion), localQuestion);
    }
    this.mClientAnsweredQuestions = Maps.newHashMapWithExpectedSize(paramTrainingModeData.getClientAnsweredQuestionCount());
    Iterator localIterator2 = paramTrainingModeData.getClientAnsweredQuestionList().iterator();
    while (localIterator2.hasNext())
    {
      Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)localIterator2.next();
      this.mClientAnsweredQuestions.put(new QuestionKey(localQuestionWithEntry.getQuestion()), localQuestionWithEntry);
    }
  }
  
  private void removePrivateCalendarData(Iterable<Training.QuestionWithEntry> paramIterable)
  {
    Iterator localIterator1 = paramIterable.iterator();
    while (localIterator1.hasNext())
    {
      Iterator localIterator2 = ((Training.QuestionWithEntry)localIterator1.next()).getQuestion().getParameterList().iterator();
      while (localIterator2.hasNext())
      {
        Sidekick.Question.Entity localEntity = (Sidekick.Question.Entity)localIterator2.next();
        if (localEntity.hasCalendarAccountHash()) {
          localEntity.clearValue();
        }
      }
    }
  }
  
  private static boolean serverResponseHasUpdates(Sidekick.TrainingModeDataResponse paramTrainingModeDataResponse)
  {
    return (paramTrainingModeDataResponse.hasStringDictionary()) || (paramTrainingModeDataResponse.hasQuestionTemplates()) || (paramTrainingModeDataResponse.hasAnsweredQuestions());
  }
  
  private void updateFromServerResponse(Training.TrainingModeData paramTrainingModeData, Sidekick.TrainingModeDataResponse paramTrainingModeDataResponse, Iterable<Training.QuestionWithEntry> paramIterable)
  {
    if (paramTrainingModeDataResponse.hasStringDictionary()) {
      paramTrainingModeData.setStringDictionary(paramTrainingModeDataResponse.getStringDictionary());
    }
    if (paramTrainingModeDataResponse.hasQuestionTemplates()) {
      paramTrainingModeData.setQuestionTemplates(paramTrainingModeDataResponse.getQuestionTemplates());
    }
    if (paramTrainingModeDataResponse.hasAnsweredQuestions())
    {
      if (paramTrainingModeDataResponse.getAnsweredQuestions().getUpdateMethod() == 1) {}
      for (Object localObject3 = mergeAnsweredQuestions(paramTrainingModeData.getServerAnsweredQuestionList(), paramTrainingModeDataResponse.getAnsweredQuestions().getAnsweredQuestionList());; localObject3 = paramTrainingModeDataResponse.getAnsweredQuestions().getAnsweredQuestionList())
      {
        paramTrainingModeData.clearServerAnsweredQuestion();
        Iterator localIterator5 = ((Collection)localObject3).iterator();
        while (localIterator5.hasNext()) {
          paramTrainingModeData.addServerAnsweredQuestion((Sidekick.Question)localIterator5.next());
        }
      }
    }
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator1 = paramTrainingModeData.getClientAnsweredQuestionList().iterator();
    while (localIterator1.hasNext())
    {
      Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)localIterator1.next();
      localHashMap.put(new QuestionKey(localQuestionWithEntry.getQuestion()), localQuestionWithEntry);
    }
    synchronized (this.mLock)
    {
      Iterator localIterator2 = paramIterable.iterator();
      if (localIterator2.hasNext())
      {
        QuestionKey localQuestionKey = new QuestionKey(((Training.QuestionWithEntry)localIterator2.next()).getQuestion());
        this.mPendingAnsweredQuestions.remove(localQuestionKey);
      }
    }
    localHashMap.putAll(this.mPendingAnsweredQuestions);
    Iterator localIterator3 = paramIterable.iterator();
    while (localIterator3.hasNext()) {
      localHashMap.remove(new QuestionKey(((Training.QuestionWithEntry)localIterator3.next()).getQuestion()));
    }
    paramTrainingModeData.clearClientAnsweredQuestion();
    Iterator localIterator4 = localHashMap.values().iterator();
    while (localIterator4.hasNext()) {
      paramTrainingModeData.addClientAnsweredQuestion((Training.QuestionWithEntry)localIterator4.next());
    }
  }
  
  public void clearData()
  {
    this.mBackgroundTasks.forceRun("clear_training_data", 0L);
  }
  
  public void clearDataSync()
  {
    ExtraPreconditions.checkNotMainThread();
    this.mTrainingModeData.deleteFile();
    refreshFromTrainingModeData((Training.TrainingModeData)this.mTrainingModeData.getData());
    synchronized (this.mLock)
    {
      this.mPendingAnsweredQuestions.clear();
      return;
    }
  }
  
  @Nullable
  public TrainingQuestionNode convertQuestionNode(Sidekick.QuestionNode paramQuestionNode)
  {
    TrainingQuestion localTrainingQuestion = convertQuestion(paramQuestionNode.getQuestion());
    TrainingQuestionNode localTrainingQuestionNode1;
    if (localTrainingQuestion == null) {
      localTrainingQuestionNode1 = null;
    }
    for (;;)
    {
      return localTrainingQuestionNode1;
      localTrainingQuestionNode1 = new TrainingQuestionNode(localTrainingQuestion, paramQuestionNode.getParentQuestionAnswerMatchList());
      Iterator localIterator = paramQuestionNode.getChildList().iterator();
      while (localIterator.hasNext())
      {
        TrainingQuestionNode localTrainingQuestionNode2 = convertQuestionNode((Sidekick.QuestionNode)localIterator.next());
        if (localTrainingQuestionNode2 != null) {
          localTrainingQuestionNode1.addChild(localTrainingQuestionNode2);
        }
      }
    }
  }
  
  public Iterable<Training.QuestionWithEntry> getPendingAnsweredQuestionsWithEntries()
  {
    ExtraPreconditions.checkNotMainThread();
    HashMap localHashMap = Maps.newHashMap();
    Iterator localIterator = ((Training.TrainingModeData)this.mTrainingModeData.getData()).getClientAnsweredQuestionList().iterator();
    while (localIterator.hasNext())
    {
      Training.QuestionWithEntry localQuestionWithEntry = (Training.QuestionWithEntry)localIterator.next();
      localHashMap.put(new QuestionKey(localQuestionWithEntry.getQuestion()), localQuestionWithEntry);
    }
    synchronized (this.mLock)
    {
      localHashMap.putAll(this.mPendingAnsweredQuestions);
      return localHashMap.values();
    }
  }
  
  public Sidekick.TrainingModeMetadata getTrainingModeMetadata()
  {
    ExtraPreconditions.checkNotMainThread();
    Training.TrainingModeData localTrainingModeData = (Training.TrainingModeData)this.mTrainingModeData.getData();
    Sidekick.TrainingModeMetadata localTrainingModeMetadata = new Sidekick.TrainingModeMetadata();
    if ((localTrainingModeData.hasStringDictionary()) && (localTrainingModeData.getStringDictionary().hasMetadata())) {
      localTrainingModeMetadata.setStringDictionaryMetadata(localTrainingModeData.getStringDictionary().getMetadata());
    }
    if ((localTrainingModeData.hasQuestionTemplates()) && (localTrainingModeData.getQuestionTemplates().hasMetadata())) {
      localTrainingModeMetadata.setQuestionTemplatesMetadata(localTrainingModeData.getQuestionTemplates().getMetadata());
    }
    Long localLong = getMostRecentAnsweredQuestionTimestamp(localTrainingModeData.getServerAnsweredQuestionList());
    if (localLong != null) {
      localTrainingModeMetadata.setAnsweredQuestionsMetadata(new Sidekick.AnsweredQuestionsMetadata().setLastAnsweredQuestionTimestampMillis(localLong.longValue()));
    }
    return localTrainingModeMetadata;
  }
  
  public boolean isDirty()
  {
    for (;;)
    {
      synchronized (this.mLock)
      {
        if (this.mPendingAnsweredQuestions.isEmpty())
        {
          if ((this.mClientAnsweredQuestions == null) || (this.mClientAnsweredQuestions.isEmpty())) {
            break label55;
          }
          break label50;
          return bool;
        }
      }
      label50:
      boolean bool = true;
      continue;
      label55:
      bool = false;
    }
  }
  
  public List<TrainingQuestionNode> resolveQuestions(Collection<Sidekick.QuestionNode> paramCollection)
  {
    
    if (!isInitialized()) {
      initialize();
    }
    Collection localCollection = maybeLoadCalendarInfo(paramCollection);
    LinkedList localLinkedList;
    synchronized (this.mLock)
    {
      localLinkedList = Lists.newLinkedList();
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        TrainingQuestionNode localTrainingQuestionNode = convertQuestionNode((Sidekick.QuestionNode)localIterator.next());
        if ((localTrainingQuestionNode != null) && (localCollection != null)) {
          localTrainingQuestionNode = augmentCalendarData(localTrainingQuestionNode, localCollection);
        }
        if (localTrainingQuestionNode != null) {
          localLinkedList.add(localTrainingQuestionNode);
        }
      }
    }
    return localLinkedList;
  }
  
  public ListenableFuture<Collection<TrainingQuestionNode>> resolveQuestionsAsync(final Collection<Sidekick.QuestionNode> paramCollection)
  {
    ListenableFutureTask localListenableFutureTask = ListenableFutureTask.create(new Callable()
    {
      public Collection<TrainingQuestionNode> call()
        throws Exception
      {
        return TrainingQuestionManagerImpl.this.resolveQuestions(paramCollection);
      }
    });
    this.mBgExecutor.execute(localListenableFutureTask);
    return localListenableFutureTask;
  }
  
  public AsyncTask<Void, Void, Sidekick.ResponsePayload> sendAction(Sidekick.Entry paramEntry, Sidekick.Question paramQuestion, Sidekick.Action paramAction)
  {
    return new SendTrainingQuestionActionTask(this.mNetworkClient, paramEntry, paramAction, this.mClock, paramQuestion).execute(new Void[0]);
  }
  
  public void sendAnswers()
  {
    if ((isInitialized()) && (!isDirty())) {
      return;
    }
    this.mBackgroundTasks.forceRun("send_training_answers", 0L);
  }
  
  public void sendAnswersSync()
  {
    ExtraPreconditions.checkNotMainThread();
    Iterable localIterable = getPendingAnsweredQuestionsWithEntries();
    if (!localIterable.iterator().hasNext()) {
      return;
    }
    removePrivateCalendarData(localIterable);
    Sidekick.ActionsQuery localActionsQuery = TrainingRequestHelper.buildAnsweredQuestionsQuery(localIterable);
    Sidekick.RequestPayload localRequestPayload = new Sidekick.RequestPayload().setActionsQuery(localActionsQuery).setTrainingModeDataQuery(new Sidekick.TrainingModeDataQuery().setMetadata(getTrainingModeMetadata()));
    Sidekick.ResponsePayload localResponsePayload = this.mNetworkClient.sendRequestWithoutLocation(localRequestPayload);
    if (localResponsePayload == null) {
      Log.e(TAG, "Network error sending answered questions");
    }
    for (;;)
    {
      Log.i(TAG, "Flushing pending changes to file");
      flushPendingAnswers();
      return;
      if ((localResponsePayload.hasActionsResponse()) && (localResponsePayload.getActionsResponse().hasError()))
      {
        String str = TAG;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Integer.valueOf(localResponsePayload.getActionsResponse().getError());
        Log.e(str, String.format("Server error processing answered questions: %d", arrayOfObject));
      }
      else
      {
        if (localResponsePayload.hasTrainingModeDataResponse()) {
          break;
        }
        Log.e(TAG, String.format("Response missing TrainingModeDataResponse", new Object[0]));
      }
    }
    updateFromServerResponse(localResponsePayload.getTrainingModeDataResponse(), localIterable);
  }
  
  /* Error */
  public void setAnswer(Sidekick.Question paramQuestion, com.google.geo.sidekick.Sidekick.Question.Answer paramAnswer, @Nullable Sidekick.Entry paramEntry)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 262	com/google/geo/sidekick/Sidekick$Question:toByteArray	()[B
    //   4: invokestatic 266	com/google/geo/sidekick/Sidekick$Question:parseFrom	([B)Lcom/google/geo/sidekick/Sidekick$Question;
    //   7: astore 6
    //   9: aload 6
    //   11: aload_0
    //   12: getfield 90	com/google/android/sidekick/main/training/TrainingQuestionManagerImpl:mClock	Lcom/google/android/shared/util/Clock;
    //   15: invokeinterface 718 1 0
    //   20: invokevirtual 284	com/google/geo/sidekick/Sidekick$Question:setAnswerTimestampMillis	(J)Lcom/google/geo/sidekick/Sidekick$Question;
    //   23: pop
    //   24: aload 6
    //   26: aload_2
    //   27: invokevirtual 274	com/google/geo/sidekick/Sidekick$Question:setAnswer	(Lcom/google/geo/sidekick/Sidekick$Question$Answer;)Lcom/google/geo/sidekick/Sidekick$Question;
    //   30: pop
    //   31: new 250	com/google/android/sidekick/shared/training/QuestionKey
    //   34: dup
    //   35: aload 6
    //   37: invokespecial 253	com/google/android/sidekick/shared/training/QuestionKey:<init>	(Lcom/google/geo/sidekick/Sidekick$Question;)V
    //   40: astore 9
    //   42: new 255	com/google/android/apps/sidekick/training/Training$QuestionWithEntry
    //   45: dup
    //   46: invokespecial 719	com/google/android/apps/sidekick/training/Training$QuestionWithEntry:<init>	()V
    //   49: aload 6
    //   51: invokevirtual 723	com/google/android/apps/sidekick/training/Training$QuestionWithEntry:setQuestion	(Lcom/google/geo/sidekick/Sidekick$Question;)Lcom/google/android/apps/sidekick/training/Training$QuestionWithEntry;
    //   54: astore 10
    //   56: aload_3
    //   57: ifnull +10 -> 67
    //   60: aload 10
    //   62: aload_3
    //   63: invokevirtual 727	com/google/android/apps/sidekick/training/Training$QuestionWithEntry:setEntry	(Lcom/google/geo/sidekick/Sidekick$Entry;)Lcom/google/android/apps/sidekick/training/Training$QuestionWithEntry;
    //   66: pop
    //   67: aload_0
    //   68: getfield 77	com/google/android/sidekick/main/training/TrainingQuestionManagerImpl:mLock	Ljava/lang/Object;
    //   71: astore 11
    //   73: aload 11
    //   75: monitorenter
    //   76: aload_0
    //   77: getfield 98	com/google/android/sidekick/main/training/TrainingQuestionManagerImpl:mPendingAnsweredQuestions	Ljava/util/Map;
    //   80: aload 9
    //   82: aload 10
    //   84: invokeinterface 380 3 0
    //   89: pop
    //   90: aload 11
    //   92: monitorexit
    //   93: aload_0
    //   94: getfield 104	com/google/android/sidekick/main/training/TrainingQuestionManagerImpl:mFirstUseCardHandler	Lcom/google/android/velvet/presenter/FirstUseCardHandler;
    //   97: invokevirtual 732	com/google/android/velvet/presenter/FirstUseCardHandler:recordTrainingQuestionAnswered	()V
    //   100: return
    //   101: astore 4
    //   103: getstatic 54	com/google/android/sidekick/main/training/TrainingQuestionManagerImpl:TAG	Ljava/lang/String;
    //   106: ldc_w 734
    //   109: invokestatic 214	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   112: pop
    //   113: return
    //   114: astore 12
    //   116: aload 11
    //   118: monitorexit
    //   119: aload 12
    //   121: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	122	0	this	TrainingQuestionManagerImpl
    //   0	122	1	paramQuestion	Sidekick.Question
    //   0	122	2	paramAnswer	com.google.geo.sidekick.Sidekick.Question.Answer
    //   0	122	3	paramEntry	Sidekick.Entry
    //   101	1	4	localInvalidProtocolBufferMicroException	InvalidProtocolBufferMicroException
    //   7	43	6	localQuestion	Sidekick.Question
    //   40	41	9	localQuestionKey	QuestionKey
    //   54	29	10	localQuestionWithEntry	Training.QuestionWithEntry
    //   114	6	12	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   0	9	101	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   76	93	114	finally
    //   116	119	114	finally
  }
  
  public void updateFromServerResponse(final Sidekick.TrainingModeDataResponse paramTrainingModeDataResponse, final Iterable<Training.QuestionWithEntry> paramIterable)
  {
    
    if (paramTrainingModeDataResponse.hasUpdatedSidekickConfiguration()) {
      this.mNowOptInSettings.updateSidekickConfigurationForCurrentAccount(paramTrainingModeDataResponse.getUpdatedSidekickConfiguration());
    }
    if ((!serverResponseHasUpdates(paramTrainingModeDataResponse)) && (!paramIterable.iterator().hasNext()))
    {
      Log.i(TAG, "updateFromServerResponse: no new training mode data and no pending answered questions to clear");
      return;
    }
    this.mTrainingModeData.doReadModifyWrite(new FileBackedProto.ReadModifyWrite()
    {
      public Training.TrainingModeData readModifyMaybeWrite(Training.TrainingModeData paramAnonymousTrainingModeData)
      {
        TrainingQuestionManagerImpl.this.updateFromServerResponse(paramAnonymousTrainingModeData, paramTrainingModeDataResponse, paramIterable);
        return paramAnonymousTrainingModeData;
      }
    });
    refreshFromTrainingModeData((Training.TrainingModeData)this.mTrainingModeData.getData());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.training.TrainingQuestionManagerImpl
 * JD-Core Version:    0.7.0.1
 */