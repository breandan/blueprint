package com.google.android.voicesearch.fragments.reminders;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.actions.SendEditActionTask;
import com.google.android.sidekick.main.entry.EntryProvider;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;

public class MyPlacesSaver
{
  private final Clock mClock;
  private final Context mContext;
  private final EntryProvider mEntryProvider;
  private final NetworkClient mNetworkClient;
  
  public MyPlacesSaver(Context paramContext, NetworkClient paramNetworkClient, EntryProvider paramEntryProvider, Clock paramClock)
  {
    this.mContext = paramContext;
    this.mNetworkClient = paramNetworkClient;
    this.mEntryProvider = paramEntryProvider;
    this.mClock = paramClock;
  }
  
  private static Sidekick.Action getRenameOrEditAction(Sidekick.Entry paramEntry)
  {
    return ProtoUtils.findAction(paramEntry, 17, new int[] { 18 });
  }
  
  public void save(Sidekick.Entry paramEntry, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    Sidekick.Location localLocation = new Sidekick.Location();
    String str1 = paramEcoutezLocalResult.getAddress();
    if (!TextUtils.isEmpty(str1)) {
      localLocation.setAddress(str1);
    }
    String str2 = PlaceUtils.getLocationName(paramEntry);
    if (!TextUtils.isEmpty(str2)) {
      localLocation.setName(str2);
    }
    new SendEditActionTask(this.mContext, paramEntry, getRenameOrEditAction(paramEntry), localLocation, this.mNetworkClient, this.mEntryProvider, null, this.mClock).execute(new Void[0]);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.MyPlacesSaver
 * JD-Core Version:    0.7.0.1
 */