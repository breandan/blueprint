package com.google.android.voicesearch.fragments.reminders;

import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.android.recurrencepicker.EventRecurrence;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.SearchUrlHelper.Builder;
import com.google.android.search.core.google.gaia.LoginHelper;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.UriRequest;
import com.google.android.shared.util.ProtoUtils;
import com.google.android.sidekick.main.DataBackendVersionStore;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.caribou.tasks.RecurrenceProtos.Recurrence;
import com.google.caribou.tasks.RecurrenceProtos.RecurrenceId;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.majel.proto.ActionV2Protos.AbsoluteTimeTrigger;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.AddReminderAction;
import com.google.majel.proto.ActionV2Protos.LocalResultCandidateList;
import com.google.majel.proto.ActionV2Protos.LocationTrigger;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class ReminderSaver
{
  static final String QUERY_PARAM_PINFO = "pinfo";
  private final ExecutorService mBackgroundExecutor;
  private final DataBackendVersionStore mDataBackendVersionStore;
  private AsyncTask<Void, Void, String> mFetchConfirmationUrlTask;
  private final GsaConfigFlags mGsaConfigFlags;
  private final HttpHelper mHttpHelper;
  private final LoginHelper mLoginHelper;
  private final SearchUrlHelper mSearchUrlHelper;
  
  public ReminderSaver(HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper, ExecutorService paramExecutorService, DataBackendVersionStore paramDataBackendVersionStore, LoginHelper paramLoginHelper, GsaConfigFlags paramGsaConfigFlags)
  {
    this.mBackgroundExecutor = paramExecutorService;
    this.mHttpHelper = paramHttpHelper;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mDataBackendVersionStore = paramDataBackendVersionStore;
    this.mLoginHelper = paramLoginHelper;
    this.mGsaConfigFlags = paramGsaConfigFlags;
  }
  
  static ActionV2Protos.AddReminderAction createAddReminderAction(SetReminderAction paramSetReminderAction, GsaConfigFlags paramGsaConfigFlags)
  {
    ActionV2Protos.AddReminderAction localAddReminderAction = new ActionV2Protos.AddReminderAction();
    if (paramSetReminderAction.getOriginalTaskId() != null) {
      localAddReminderAction.setTaskId(paramSetReminderAction.getOriginalTaskId());
    }
    if (paramSetReminderAction.getRecurrenceId() != null) {
      localAddReminderAction.setRecurrenceId(new RecurrenceProtos.RecurrenceId().setId(paramSetReminderAction.getRecurrenceId()));
    }
    localAddReminderAction.setLabel(paramSetReminderAction.getLabel());
    localAddReminderAction.setConfirmationUrlPath(paramSetReminderAction.getConfirmationUrlPath());
    if (paramSetReminderAction.getTriggerType() == 1) {
      if (paramGsaConfigFlags.getAddReminderRecurrenceEnabledVersion())
      {
        localEventRecurrence = paramSetReminderAction.getRecurrence();
        if (localEventRecurrence != null) {
          break label164;
        }
        localAbsoluteTimeTrigger = new ActionV2Protos.AbsoluteTimeTrigger();
        localAbsoluteTimeTrigger.setTimeMs(paramSetReminderAction.getDateTimeMs());
        if (paramSetReminderAction.getSymbolicTime() != null) {
          localAbsoluteTimeTrigger.setSymbolicTime(paramSetReminderAction.getSymbolicTime().actionV2Symbol);
        }
        localAddReminderAction.setAbsoluteTimeTrigger(localAbsoluteTimeTrigger);
      }
    }
    label164:
    while (paramSetReminderAction.getTriggerType() != 2) {
      for (;;)
      {
        ActionV2Protos.AbsoluteTimeTrigger localAbsoluteTimeTrigger;
        if (paramSetReminderAction.getEmbeddedAction() != null) {
          localAddReminderAction.setEmbeddedAction(paramSetReminderAction.getEmbeddedAction());
        }
        return localAddReminderAction;
        EventRecurrence localEventRecurrence = null;
        continue;
        RecurrenceProtos.Recurrence localRecurrence = RecurrenceHelper.convertEventRecurrenceToCaribouRecurrence(localEventRecurrence, paramSetReminderAction);
        if (localRecurrence != null) {
          localAddReminderAction.setRecurrence(localRecurrence);
        }
      }
    }
    ActionV2Protos.LocationTrigger localLocationTrigger = new ActionV2Protos.LocationTrigger();
    if (Feature.REMINDERS_LEAVING_TRIGGER.isEnabled()) {
      localLocationTrigger.setType(paramSetReminderAction.getLocationTriggerType());
    }
    for (;;)
    {
      ActionV2Protos.LocalResultCandidateList localLocalResultCandidateList = new ActionV2Protos.LocalResultCandidateList();
      localLocalResultCandidateList.addCandidateLocalResult(paramSetReminderAction.getLocation());
      localLocationTrigger.addLocalResultCandidateList(localLocalResultCandidateList);
      localAddReminderAction.setLocationTrigger(localLocationTrigger);
      break;
      localLocationTrigger.setType(0);
    }
  }
  
  private String doHttpRequest(String paramString, Map<String, String> paramMap)
  {
    try
    {
      String str = this.mHttpHelper.get(new HttpHelper.GetRequest(paramString, paramMap), 11);
      return str;
    }
    catch (HttpHelper.HttpException localHttpException)
    {
      Log.e("ReminderSaver", "HTTP request failed: " + localHttpException.getStatusCode());
      return null;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.e("ReminderSaver", "HTTP request failed", localIOException);
      }
    }
  }
  
  private String doHttpRequestWithRetries(String paramString, Map<String, String> paramMap)
  {
    String str1 = this.mGsaConfigFlags.getReminderTokenType();
    int i;
    int j;
    label26:
    String str2;
    if (!TextUtils.isEmpty(str1))
    {
      i = 1;
      if (i == 0) {
        break label140;
      }
      j = 3;
      str2 = null;
    }
    String str4;
    for (int k = 0;; k++)
    {
      if (k >= j) {
        break label152;
      }
      if (k == 1) {
        BugLogger.record(9369185);
      }
      if (i != 0)
      {
        if (str2 != null) {
          this.mLoginHelper.invalidateToken(str2);
        }
        str2 = this.mLoginHelper.blockingGetToken(str1, 1000L);
        if (str2 != null) {
          paramMap.put("Authorization", "Bearer " + str2);
        }
      }
      str4 = doHttpRequest(paramString, paramMap);
      if (str4 != null)
      {
        return str4;
        i = 0;
        break;
        label140:
        j = 1;
        break label26;
      }
    }
    label152:
    Uri localUri;
    if (i == 0)
    {
      BugLogger.record(8738549);
      localUri = Uri.parse(paramString);
      if (localUri.getQueryParameter("client") != null) {
        break label248;
      }
    }
    label248:
    for (String str3 = localUri.buildUpon().appendQueryParameter("client", "mobile-legacy").build().toString();; str3 = paramString)
    {
      Map localMap = getFallbackHeaders((String)paramMap.get("User-Agent"));
      if (localMap != null)
      {
        str4 = doHttpRequest(str3, localMap);
        if (str4 != null) {
          break;
        }
        BugLogger.record(8789172);
      }
      return null;
    }
  }
  
  private void doSaveReminder(final ActionV2Protos.AddReminderAction paramAddReminderAction, final SimpleCallback<Boolean> paramSimpleCallback)
  {
    new AsyncTask()
    {
      protected ReminderSaver.SaveReminderResult doInBackground(Void... paramAnonymousVarArgs)
      {
        ActionV2Protos.ActionV2 localActionV2 = new ActionV2Protos.ActionV2().setAddReminderActionExtension(paramAddReminderAction);
        UriRequest localUriRequest = ReminderSaver.this.mSearchUrlHelper.getSearchBaseUri(true, false).build();
        HashMap localHashMap = Maps.newHashMap();
        localHashMap.put("pinfo", ProtoUtils.messageToUrlSafeBase64(localActionV2));
        Uri localUri = Uri.parse(paramAddReminderAction.getConfirmationUrlPath());
        String str = SearchUrlHelper.makeAbsoluteUri(localUriRequest.getUri(), localUri, Sets.newHashSet(new String[] { "pinfo" }), localHashMap).toString();
        return ReminderSaver.this.doSaveReminderRequest(str, localUriRequest.getHeadersCopy());
      }
      
      protected void onPostExecute(ReminderSaver.SaveReminderResult paramAnonymousSaveReminderResult)
      {
        if ((paramAnonymousSaveReminderResult.mSuccess) && (!TextUtils.isEmpty(paramAnonymousSaveReminderResult.mVersionInfo))) {
          ReminderSaver.this.mDataBackendVersionStore.requireKansasVersionInfo(paramAnonymousSaveReminderResult.mVersionInfo);
        }
        paramSimpleCallback.onResult(Boolean.valueOf(paramAnonymousSaveReminderResult.mSuccess));
      }
    }.executeOnExecutor(this.mBackgroundExecutor, new Void[0]);
  }
  
  @Nullable
  private Map<String, String> getFallbackHeaders(String paramString)
  {
    String str = this.mLoginHelper.blockingGetToken("mobilepersonalfeeds", 1000L);
    if (str == null)
    {
      Log.e("ReminderSaver", "#saveReminder: Failed to get auth token");
      return null;
    }
    HashMap localHashMap = Maps.newHashMap();
    if (!TextUtils.isEmpty(paramString)) {
      localHashMap.put("User-Agent", paramString);
    }
    localHashMap.put("Authorization", "GoogleLogin auth=" + str);
    return localHashMap;
  }
  
  @Nullable
  SaveReminderResult doSaveReminderRequest(String paramString, Map<String, String> paramMap)
  {
    String str1 = doHttpRequestWithRetries(paramString, paramMap);
    Object localObject = null;
    if (str1 != null) {}
    try
    {
      String str2 = new JSONObject(str1).optString("vInfo");
      localObject = str2;
    }
    catch (JSONException localJSONException)
    {
      for (;;)
      {
        Log.e("ReminderSaver", "Failed to parse reminder response!");
        localObject = null;
      }
    }
    if (TextUtils.isEmpty(localObject))
    {
      Log.e("ReminderSaver", "Missing version info.  Request failed");
      return new SaveReminderResult(false, null);
    }
    return new SaveReminderResult(true, localObject);
  }
  
  public void fetchConfirmationUrlPath()
  {
    if (this.mFetchConfirmationUrlTask == null) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mFetchConfirmationUrlTask = new AsyncTask()
      {
        protected String doInBackground(Void... paramAnonymousVarArgs)
        {
          return ReminderSaver.this.fetchConfirmationUrlPathSync();
        }
      }.executeOnExecutor(this.mBackgroundExecutor, new Void[0]);
      return;
    }
  }
  
  @Nullable
  String fetchConfirmationUrlPathSync()
  {
    UriRequest localUriRequest = this.mSearchUrlHelper.getSearchBaseUri(true, false).build();
    ImmutableMap localImmutableMap = ImmutableMap.of("ctzn", TimeZone.getDefault().getID());
    String str = doHttpRequestWithRetries(SearchUrlHelper.makeAbsoluteUri(localUriRequest.getUri(), Uri.parse(this.mGsaConfigFlags.getGwsFetchReminderConfirmationUrlPath()), Sets.newHashSet(new String[] { "pinfo" }), localImmutableMap).toString(), localUriRequest.getHeadersCopy());
    if (TextUtils.isEmpty(str))
    {
      Log.e("ReminderSaver", "Failed to retrieve fetch confirmation URL");
      return null;
    }
    try
    {
      Iterator localIterator = PeanutProtos.Peanut.parseFrom(Base64.decode(str, 8)).getActionV2List().iterator();
      while (localIterator.hasNext())
      {
        ActionV2Protos.ActionV2 localActionV2 = (ActionV2Protos.ActionV2)localIterator.next();
        if ((localActionV2.hasAddReminderActionExtension()) && (localActionV2.getAddReminderActionExtension().hasConfirmationUrlPath())) {
          return localActionV2.getAddReminderActionExtension().getConfirmationUrlPath();
        }
      }
      Log.e("ReminderSaver", "Fetch confirmation URL response did not contain confirmation URL");
      return null;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Log.e("ReminderSaver", "Failed to decode fetch confirmation URL response");
      return null;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.e("ReminderSaver", "Failed to parse fetch confirmation URL peanut");
    }
    return null;
  }
  
  public void saveReminder(SetReminderAction paramSetReminderAction, final SimpleCallback<Boolean> paramSimpleCallback)
  {
    final ActionV2Protos.AddReminderAction localAddReminderAction = createAddReminderAction(paramSetReminderAction, this.mGsaConfigFlags);
    if (TextUtils.isEmpty(localAddReminderAction.getConfirmationUrlPath()))
    {
      new AsyncTask()
      {
        protected String doInBackground(Void... paramAnonymousVarArgs)
        {
          if (ReminderSaver.this.mFetchConfirmationUrlTask == null) {
            ReminderSaver.this.fetchConfirmationUrlPath();
          }
          try
          {
            String str = (String)ReminderSaver.this.mFetchConfirmationUrlTask.get();
            return str;
          }
          catch (InterruptedException localInterruptedException)
          {
            Log.e("ReminderSaver", "#saveReminder: confirmation URL retrieval failed", localInterruptedException);
            return null;
          }
          catch (ExecutionException localExecutionException)
          {
            for (;;)
            {
              Log.e("ReminderSaver", "#saveReminder: confirmation URL retrieval failed", localExecutionException);
            }
          }
        }
        
        protected void onPostExecute(String paramAnonymousString)
        {
          if (TextUtils.isEmpty(paramAnonymousString))
          {
            Log.e("ReminderSaver", "#saveReminder: failed to retrieve confirmation URL");
            paramSimpleCallback.onResult(Boolean.valueOf(false));
            return;
          }
          localAddReminderAction.setConfirmationUrlPath(paramAnonymousString);
          ReminderSaver.this.doSaveReminder(localAddReminderAction, paramSimpleCallback);
        }
      }.executeOnExecutor(this.mBackgroundExecutor, new Void[0]);
      return;
    }
    doSaveReminder(localAddReminderAction, paramSimpleCallback);
  }
  
  static class SaveReminderResult
  {
    public boolean mSuccess;
    @Nullable
    public String mVersionInfo;
    
    public SaveReminderResult(boolean paramBoolean, @Nullable String paramString)
    {
      this.mSuccess = paramBoolean;
      this.mVersionInfo = paramString;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.ReminderSaver
 * JD-Core Version:    0.7.0.1
 */