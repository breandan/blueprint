package com.google.android.voicesearch.contacts;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactDisambiguationView
  extends LinearLayout
{
  private Callback mCallback;
  private final View.OnClickListener mContactDetailClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (ContactDisambiguationView.this.mCallback != null)
      {
        ContactDetailSelectItem localContactDetailSelectItem = (ContactDetailSelectItem)paramAnonymousView;
        ContactDisambiguationView.this.mCallback.onContactDetailSelected(localContactDetailSelectItem.getPerson(), localContactDetailSelectItem.getContact());
      }
    }
  };
  private final LayoutInflater mLayoutInflater;
  private final View.OnClickListener mPersonClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (ContactDisambiguationView.this.mCallback != null) {
        ContactDisambiguationView.this.mCallback.onPersonSelected(((PersonSelectItem)paramAnonymousView).getPerson());
      }
    }
  };
  
  public ContactDisambiguationView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ContactDisambiguationView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ContactDisambiguationView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mLayoutInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    getLayoutTransition().disableTransitionType(1);
  }
  
  private void createContactDetailsViews(Person paramPerson, List<Contact> paramList, int paramInt)
  {
    int i = 1;
    if (paramList.size() == i) {}
    for (;;)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Contact localContact = (Contact)localIterator.next();
        ContactDetailSelectItem localContactDetailSelectItem = (ContactDetailSelectItem)this.mLayoutInflater.inflate(paramInt, this, false);
        localContactDetailSelectItem.setPersonAndContact(paramPerson, localContact);
        addView(localContactDetailSelectItem);
        if (i == 0) {
          localContactDetailSelectItem.setOnClickListener(this.mContactDetailClickListener);
        }
      }
      i = 0;
    }
  }
  
  private void createPersonViews(List<Person> paramList, ContactSelectMode paramContactSelectMode, boolean paramBoolean)
  {
    int i = 1;
    if ((paramList.size() == i) && (maybeRetainSinglePersonView((Person)paramList.get(0), paramBoolean))) {
      return;
    }
    removeAllViews();
    label48:
    label56:
    Person localPerson;
    PersonSelectItem localPersonSelectItem;
    if (paramList.size() == i)
    {
      Iterator localIterator = paramList.iterator();
      if (localIterator.hasNext())
      {
        localPerson = (Person)localIterator.next();
        localPersonSelectItem = (PersonSelectItem)this.mLayoutInflater.inflate(2130968772, this, false);
        if (i != 0) {
          break label153;
        }
        localPersonSelectItem.setPerson(localPerson, localPerson.denormalizeContacts(paramContactSelectMode.getContactLookupMode()), null);
      }
    }
    for (;;)
    {
      addView(localPersonSelectItem);
      if (!paramBoolean) {
        break label56;
      }
      localPersonSelectItem.setOnClickListener(this.mPersonClickListener);
      localPersonSelectItem.setActionButtonOnClickListener(this.mCallback);
      break label56;
      break;
      i = 0;
      break label48;
      label153:
      localPersonSelectItem.setPerson(localPerson, null, null);
    }
  }
  
  private boolean maybeRetainSinglePersonView(Person paramPerson, boolean paramBoolean)
  {
    int i = getChildCount();
    int j = 0;
    ArrayList localArrayList = Lists.newArrayListWithCapacity(getChildCount());
    int k = 0;
    if (k < i)
    {
      View localView = getChildAt(k);
      if ((j == 0) && ((localView instanceof PersonSelectItem)) && (((PersonSelectItem)localView).getPerson().equals(paramPerson))) {
        if (paramBoolean)
        {
          localView.setOnClickListener(this.mPersonClickListener);
          label75:
          j = 1;
        }
      }
      for (;;)
      {
        k++;
        break;
        localView.setClickable(false);
        ((PersonSelectItem)localView).hideContactDetail();
        break label75;
        localArrayList.add(localView);
      }
    }
    if (j != 0)
    {
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext()) {
        removeView((View)localIterator.next());
      }
      return true;
    }
    return false;
  }
  
  public void setCallback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }
  
  public void setPeople(List<Person> paramList, ContactSelectMode paramContactSelectMode)
  {
    int i = 1;
    int n;
    if (!paramList.isEmpty())
    {
      int k = i;
      Preconditions.checkArgument(k);
      if (paramList.size() != i) {
        break label87;
      }
      n = i;
      label32:
      if (n != 0) {
        break label93;
      }
    }
    for (;;)
    {
      createPersonViews(paramList, paramContactSelectMode, i);
      if (n != 0)
      {
        Person localPerson = (Person)paramList.get(0);
        createContactDetailsViews(localPerson, localPerson.denormalizeContacts(paramContactSelectMode.getContactLookupMode()), paramContactSelectMode.getDetailLayoutResourceId());
      }
      return;
      int m = 0;
      break;
      label87:
      int i1 = 0;
      break label32;
      label93:
      int j = 0;
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onContactDetailSelected(Person paramPerson, Contact paramContact);
    
    public abstract void onPersonSelected(Person paramPerson);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.contacts.ContactDisambiguationView
 * JD-Core Version:    0.7.0.1
 */