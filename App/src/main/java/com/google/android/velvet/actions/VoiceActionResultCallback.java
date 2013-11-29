package com.google.android.velvet.actions;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.search.core.SearchController;
import com.google.android.search.core.state.ActionState;
import com.google.android.search.core.state.VelvetEventBus;
import com.google.android.shared.util.IntentStarter.ResultCallback;
import com.google.android.sidekick.main.actions.ActionLauncherActivity;
import com.google.android.speech.contacts.ContactIdLookupSupplier;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.velvet.ui.VelvetActivity;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.fragments.action.CommunicationAction;
import com.google.android.voicesearch.fragments.action.VoiceAction;
import com.google.common.base.Supplier;
import java.util.List;
import javax.annotation.Nullable;

public class VoiceActionResultCallback
  implements Parcelable, IntentStarter.ResultCallback
{
  public static final Parcelable.Creator<VoiceActionResultCallback> CREATOR = new Parcelable.Creator()
  {
    public VoiceActionResultCallback createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VoiceActionResultCallback(paramAnonymousParcel.readInt(), null);
    }
    
    public VoiceActionResultCallback[] newArray(int paramAnonymousInt)
    {
      return new VoiceActionResultCallback[paramAnonymousInt];
    }
  };
  private final int mCallbackId;
  @Nullable
  private ContactLookup mContactLookup;
  
  private VoiceActionResultCallback(int paramInt)
  {
    this.mCallbackId = paramInt;
  }
  
  public static IntentStarter.ResultCallback createPickContactCallback()
  {
    return new VoiceActionResultCallback(1);
  }
  
  private ContactLookup getContactLookup(Context paramContext)
  {
    if (this.mContactLookup == null) {
      this.mContactLookup = ContactLookup.newInstance(paramContext);
    }
    return this.mContactLookup;
  }
  
  @Nullable
  private VoiceAction getCurrentVoiceAction(Context paramContext)
  {
    if ((paramContext instanceof VelvetActivity)) {
      return ((VelvetActivity)paramContext).getSearchController().getEventBus().getActionState().getTopMostVoiceAction();
    }
    if ((paramContext instanceof ActionLauncherActivity)) {
      return ((ActionLauncherActivity)paramContext).getCurrentVoiceAction();
    }
    VelvetStrictMode.logW("ActivityResultHandler", "Not a supported Activity: " + paramContext);
    return null;
  }
  
  private void handleContact(final CommunicationAction paramCommunicationAction, Context paramContext, Uri paramUri)
  {
    final long l = ContentUris.parseId(paramUri);
    new AsyncTask()
    {
      protected List<Person> doInBackground(Void... paramAnonymousVarArgs)
      {
        List localList = (List)new ContactIdLookupSupplier(this.val$contactLookup, paramCommunicationAction.getSelectMode(), l).get();
        if (localList.isEmpty()) {
          localList = (List)new ContactIdLookupSupplier(this.val$contactLookup, ContactSelectMode.SHOW_CONTACT_INFO, l).get();
        }
        return localList;
      }
      
      protected void onPostExecute(List<Person> paramAnonymousList)
      {
        if (paramCommunicationAction.getRecipient() != null)
        {
          paramCommunicationAction.getRecipient().refineCandidates(paramAnonymousList);
          return;
        }
        PersonDisambiguation localPersonDisambiguation = new PersonDisambiguation(paramCommunicationAction.getSelectMode().getContactLookupMode());
        paramCommunicationAction.setRecipient(localPersonDisambiguation);
        localPersonDisambiguation.setTitle("local contact");
        localPersonDisambiguation.setCandidates(paramAnonymousList, false);
      }
    }.execute(new Void[0]);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void onResult(int paramInt, Intent paramIntent, Context paramContext)
  {
    VoiceAction localVoiceAction;
    if (paramInt == -1)
    {
      localVoiceAction = getCurrentVoiceAction(paramContext);
      if (localVoiceAction == null) {}
    }
    switch (this.mCallbackId)
    {
    default: 
      Log.w("ActivityResultHandler", "Can't handle result: " + this.mCallbackId);
      return;
    }
    if ((paramIntent != null) && (paramIntent.getData() != null) && ((localVoiceAction instanceof CommunicationAction)))
    {
      handleContact((CommunicationAction)localVoiceAction, paramContext, paramIntent.getData());
      return;
    }
    Log.w("ActivityResultHandler", "Can't handle pick contact result: data = " + paramIntent + ", action=" + localVoiceAction);
  }
  
  public void setContactLookupForTest(ContactLookup paramContactLookup)
  {
    this.mContactLookup = paramContactLookup;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mCallbackId);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.actions.VoiceActionResultCallback
 * JD-Core Version:    0.7.0.1
 */