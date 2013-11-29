package com.google.android.speech.contacts;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.search.core.Feature;
import com.google.android.velvet.actions.Disambiguation;
import com.google.common.base.Preconditions;
import com.google.majel.proto.ContactProtos.ContactReference;
import java.util.Iterator;
import java.util.List;

public class PersonDisambiguation
  extends Disambiguation<Person>
{
  public static final Parcelable.Creator<PersonDisambiguation> CREATOR = new Parcelable.Creator()
  {
    public PersonDisambiguation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PersonDisambiguation(paramAnonymousParcel, getClass().getClassLoader());
    }
    
    public PersonDisambiguation[] newArray(int paramAnonymousInt)
    {
      return new PersonDisambiguation[paramAnonymousInt];
    }
  };
  private final ContactLookup.Mode mLookupMode;
  
  protected PersonDisambiguation(Parcel paramParcel, ClassLoader paramClassLoader)
  {
    super(paramParcel, paramClassLoader);
    this.mLookupMode = ContactLookup.Mode.valueOf(paramParcel.readString());
  }
  
  public PersonDisambiguation(ContactLookup.Mode paramMode)
  {
    this.mLookupMode = paramMode;
  }
  
  public PersonDisambiguation(PersonDisambiguation paramPersonDisambiguation)
  {
    super(paramPersonDisambiguation);
    this.mLookupMode = paramPersonDisambiguation.mLookupMode;
  }
  
  public boolean autoSelectItem(Person paramPerson)
  {
    if (this.mLookupMode == ContactLookup.Mode.PERSON) {
      return true;
    }
    return paramPerson.autoSelectItem(this.mLookupMode);
  }
  
  public int getNumSelectableItems(List<Person> paramList)
  {
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext()) {
      i += ((Person)localIterator.next()).getNumSelectableItems(this.mLookupMode);
    }
    return i;
  }
  
  public ContactProtos.ContactReference toContactReference()
  {
    ContactProtos.ContactReference localContactReference;
    if ((!Feature.DISCOURSE_CONTEXT_CONTACTS.isEnabled()) && (!hasNoResults())) {
      localContactReference = new ContactProtos.ContactReference().setPlaceholderContact(true);
    }
    do
    {
      for (;;)
      {
        return localContactReference;
        localContactReference = new ContactProtos.ContactReference();
        localContactReference.setName((String)Preconditions.checkNotNull(getTitle()));
        if (!isOngoing()) {
          break;
        }
        Iterator localIterator = getCandidates().iterator();
        while (localIterator.hasNext()) {
          localContactReference.addContactInformation(((Person)localIterator.next()).toContactInformation());
        }
      }
    } while (!isCompleted());
    localContactReference.addContactInformation(((Person)get()).toContactInformation());
    return localContactReference;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(this.mLookupMode.name());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.PersonDisambiguation
 * JD-Core Version:    0.7.0.1
 */