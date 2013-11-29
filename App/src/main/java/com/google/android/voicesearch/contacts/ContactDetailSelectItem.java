package com.google.android.voicesearch.contacts;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.common.base.Preconditions;

public class ContactDetailSelectItem
  extends LinearLayout
{
  private Contact mContact;
  private Person mPerson;
  
  public ContactDetailSelectItem(Context paramContext)
  {
    super(paramContext);
  }
  
  public ContactDetailSelectItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ContactDetailSelectItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void hideView(int paramInt)
  {
    View localView = findViewById(paramInt);
    if (localView != null) {
      localView.setVisibility(8);
    }
  }
  
  private void setTextViewText(int paramInt, CharSequence paramCharSequence)
  {
    TextView localTextView = (TextView)findViewById(paramInt);
    if (localTextView != null)
    {
      localTextView.setVisibility(0);
      localTextView.setText(paramCharSequence);
    }
  }
  
  public Contact getContact()
  {
    return this.mContact;
  }
  
  public Person getPerson()
  {
    return this.mPerson;
  }
  
  public void setPersonAndContact(Person paramPerson, Contact paramContact)
  {
    this.mPerson = ((Person)Preconditions.checkNotNull(paramPerson));
    this.mContact = ((Contact)Preconditions.checkNotNull(paramContact));
    Preconditions.checkArgument(paramContact.hasValue());
    setTextViewText(2131296468, paramContact.getValue());
    String str = paramContact.getLabel(getResources());
    if (TextUtils.isEmpty(str))
    {
      hideView(2131296467);
      return;
    }
    setTextViewText(2131296467, str);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.contacts.ContactDetailSelectItem
 * JD-Core Version:    0.7.0.1
 */