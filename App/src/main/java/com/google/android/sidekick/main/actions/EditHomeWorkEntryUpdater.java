package com.google.android.sidekick.main.actions;

import com.google.android.sidekick.main.entry.EntryUpdater.EntryUpdaterFunc;
import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.Location;
import javax.annotation.Nullable;

public class EditHomeWorkEntryUpdater
  implements EntryUpdater.EntryUpdaterFunc
{
  private final Sidekick.Location mEditedLocation;
  private final ProtoKey<Sidekick.Entry> mTargetEntryKey;
  
  public EditHomeWorkEntryUpdater(Sidekick.Entry paramEntry, @Nullable Sidekick.Location paramLocation)
  {
    this.mTargetEntryKey = new ProtoKey(paramEntry);
    this.mEditedLocation = paramLocation;
  }
  
  public Sidekick.Entry apply(ProtoKey<Sidekick.Entry> paramProtoKey)
  {
    if (this.mTargetEntryKey.equals(paramProtoKey))
    {
      Sidekick.Entry localEntry1 = (Sidekick.Entry)paramProtoKey.getProto();
      Sidekick.Entry localEntry2 = (Sidekick.Entry)com.google.android.shared.util.ProtoUtils.copyOf(localEntry1);
      Sidekick.FrequentPlace localFrequentPlace;
      if ((this.mEditedLocation != null) && (localEntry1.hasFrequentPlaceEntry()) && (localEntry1.getFrequentPlaceEntry().hasFrequentPlace()))
      {
        localFrequentPlace = localEntry1.getFrequentPlaceEntry().getFrequentPlace();
        if (!localFrequentPlace.hasLocation()) {
          break label128;
        }
        if (this.mEditedLocation.hasName()) {
          localFrequentPlace.getLocation().setName(this.mEditedLocation.getName());
        }
        if (this.mEditedLocation.hasAddress()) {
          localFrequentPlace.getLocation().setAddress(this.mEditedLocation.getAddress());
        }
      }
      for (;;)
      {
        com.google.android.sidekick.shared.util.ProtoUtils.removeAction(localEntry1, 16);
        return localEntry2;
        label128:
        localFrequentPlace.setLocation(this.mEditedLocation);
      }
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.EditHomeWorkEntryUpdater
 * JD-Core Version:    0.7.0.1
 */