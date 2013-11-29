package com.google.android.sidekick.main.actions;

import com.google.android.sidekick.main.entry.EntryUpdater.EntryUpdaterFunc;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;

public class RenamePlaceEntryUpdater
  implements EntryUpdater.EntryUpdaterFunc
{
  private final Sidekick.PlaceData mRenamedPlace;
  private final ProtoKey<Sidekick.Entry> mTargetEntryKey;
  
  public RenamePlaceEntryUpdater(Sidekick.Entry paramEntry, Sidekick.PlaceData paramPlaceData)
  {
    this.mTargetEntryKey = new ProtoKey(paramEntry);
    this.mRenamedPlace = paramPlaceData;
  }
  
  public Sidekick.Entry apply(ProtoKey<Sidekick.Entry> paramProtoKey)
  {
    if (this.mTargetEntryKey.equals(paramProtoKey))
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)paramProtoKey.getProto();
      Sidekick.Entry localEntry2 = (Sidekick.Entry)com.google.android.shared.util.ProtoUtils.copyOf(localEntry1);
      if ((localEntry1.hasFrequentPlaceEntry()) && (localEntry1.getFrequentPlaceEntry().hasFrequentPlace())) {
        localEntry1.getFrequentPlaceEntry().getFrequentPlace().setPlaceData(this.mRenamedPlace);
      }
      com.google.android.sidekick.shared.util.ProtoUtils.removeAction(localEntry1, 16);
      return localEntry2;
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.RenamePlaceEntryUpdater
 * JD-Core Version:    0.7.0.1
 */