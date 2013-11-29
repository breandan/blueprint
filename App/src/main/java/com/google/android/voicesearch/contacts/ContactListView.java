package com.google.android.voicesearch.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import com.google.android.speech.contacts.Contact;
import java.util.Iterator;
import java.util.List;

public class ContactListView
  extends LinearLayout
{
  private final View.OnClickListener mClickButtonListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (ContactListView.this.mListener != null) {
        ContactListView.this.mListener.onActionButtonClicked(((ContactSelectItem)paramAnonymousView).getContact());
      }
    }
  };
  private final View.OnClickListener mClickContactListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if (ContactListView.this.mListener != null) {
        ContactListView.this.mListener.onContactSelected(((ContactSelectItem)paramAnonymousView).getContact());
      }
    }
  };
  private final LayoutInflater mLayoutInflater;
  private Listener mListener;
  private final View.OnTouchListener mTouchContactListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if ((ContactListView.this.mListener != null) && (paramAnonymousMotionEvent.getActionMasked() == 0)) {
        ContactListView.this.mListener.onContactTouched(((ContactSelectItem)paramAnonymousView).getContact());
      }
      return false;
    }
  };
  
  public ContactListView(Context paramContext)
  {
    this(paramContext, null, 0);
  }
  
  public ContactListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ContactListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mLayoutInflater = ((LayoutInflater)paramContext.getSystemService("layout_inflater"));
    setOrientation(1);
  }
  
  public void setContactSelectedListener(Listener paramListener)
  {
    this.mListener = paramListener;
  }
  
  public void setContacts(List<Contact> paramList, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    removeAllViews();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Contact localContact = (Contact)localIterator.next();
      ContactSelectItem localContactSelectItem = (ContactSelectItem)this.mLayoutInflater.inflate(paramInt1, this, false);
      localContactSelectItem.setContact(localContact, paramInt2, paramInt3);
      addView(localContactSelectItem);
      localContactSelectItem.setOnClickListener(this.mClickContactListener);
      localContactSelectItem.setOnTouchListener(this.mTouchContactListener);
      if (paramBoolean) {
        localContactSelectItem.setActionButtonOnClickListener(this.mClickButtonListener);
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onActionButtonClicked(Contact paramContact);
    
    public abstract void onContactSelected(Contact paramContact);
    
    public abstract void onContactTouched(Contact paramContact);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.contacts.ContactListView
 * JD-Core Version:    0.7.0.1
 */