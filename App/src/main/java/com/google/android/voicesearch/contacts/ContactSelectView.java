package com.google.android.voicesearch.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.contacts.Contact;

public class ContactSelectView
  extends RelativeLayout
{
  private ContactListView mContactListView;
  private SimpleCallback<Contact> mContactSelectCallback;
  private TextView mRecognizedView;
  
  public ContactSelectView(Context paramContext)
  {
    super(paramContext);
  }
  
  public ContactSelectView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ContactSelectView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onFinishInflate()
  {
    this.mRecognizedView = ((TextView)findViewById(2131296470));
    this.mContactListView = ((ContactListView)findViewById(2131296472));
    this.mContactListView.setContactSelectedListener(new ContactListViewListenerAdapter()
    {
      public void onContactSelected(Contact paramAnonymousContact)
      {
        if (ContactSelectView.this.mContactSelectCallback != null) {
          ContactSelectView.this.mContactSelectCallback.onResult(paramAnonymousContact);
        }
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.contacts.ContactSelectView
 * JD-Core Version:    0.7.0.1
 */