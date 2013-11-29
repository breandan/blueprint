package com.google.android.sidekick.shared.cards;

import com.google.android.shared.util.Clock;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.DirectionsLauncher;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.FrequentPlace;
import com.google.geo.sidekick.Sidekick.FrequentPlaceEntry;
import com.google.geo.sidekick.Sidekick.PlaceData;

class ContactEntryAdapter
  extends AbstractPlaceEntryAdapter
{
  ContactEntryAdapter(Sidekick.Entry paramEntry, Sidekick.FrequentPlaceEntry paramFrequentPlaceEntry, DirectionsLauncher paramDirectionsLauncher, ActivityHelper paramActivityHelper, Clock paramClock)
  {
    super(paramEntry, paramFrequentPlaceEntry, paramDirectionsLauncher, paramActivityHelper, paramClock);
    Sidekick.FrequentPlace localFrequentPlace = getFrequentPlace();
    if ((localFrequentPlace == null) || (!localFrequentPlace.hasPlaceData()) || (!localFrequentPlace.getPlaceData().hasContactData())) {
      throw new IllegalArgumentException("entry was expected to have hasContactData()" + paramEntry);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.ContactEntryAdapter
 * JD-Core Version:    0.7.0.1
 */