package com.google.android.sidekick.main.actions;

import android.content.Context;
import android.widget.Toast;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.speech.callback.SimpleCallback;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ActionsResponse;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.ExecutedUserAction;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.ResponsePayload;
import javax.annotation.Nullable;

public class SendEditActionTask
  extends InvalidatingRecordActionTask
{
  @Nullable
  private final SimpleCallback<Boolean> mCallback;
  private final Context mContext;
  private final Sidekick.Location mEditedLocation;
  
  public SendEditActionTask(Context paramContext, Sidekick.Entry paramEntry, Sidekick.Action paramAction, Sidekick.Location paramLocation, NetworkClient paramNetworkClient, @Nullable EntryProvider paramEntryProvider, @Nullable SimpleCallback<Boolean> paramSimpleCallback, Clock paramClock)
  {
    super(paramNetworkClient, paramEntryProvider, paramEntry, paramAction, paramClock);
    this.mCallback = paramSimpleCallback;
    this.mContext = paramContext;
    this.mEditedLocation = paramLocation;
  }
  
  protected Sidekick.ExecutedUserAction buildExecutedAction(Sidekick.Action paramAction, long paramLong)
  {
    if (this.mEditedLocation != null) {
      return super.buildExecutedAction(paramAction, paramLong).setEditedPlaceLocation(this.mEditedLocation);
    }
    return super.buildExecutedAction(paramAction, paramLong);
  }
  
  protected void onPostExecute(Sidekick.ResponsePayload paramResponsePayload)
  {
    int i = 2131362173;
    if (paramResponsePayload != null) {
      if ((paramResponsePayload.hasActionsResponse()) && (paramResponsePayload.getActionsResponse().hasError()))
      {
        super.onPostExecute(null);
        Context localContext = this.mContext;
        if (paramResponsePayload.getActionsResponse().getError() == 13) {
          i = 2131362537;
        }
        Toast.makeText(localContext, i, 1).show();
        if (this.mCallback != null) {
          this.mCallback.onResult(Boolean.valueOf(false));
        }
      }
    }
    do
    {
      do
      {
        return;
        super.onPostExecute(paramResponsePayload);
        Toast.makeText(this.mContext, 2131362538, 1).show();
      } while (this.mCallback == null);
      this.mCallback.onResult(Boolean.valueOf(true));
      return;
      super.onPostExecute(paramResponsePayload);
      Toast.makeText(this.mContext, i, 1).show();
    } while (this.mCallback == null);
    this.mCallback.onResult(Boolean.valueOf(false));
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.SendEditActionTask
 * JD-Core Version:    0.7.0.1
 */