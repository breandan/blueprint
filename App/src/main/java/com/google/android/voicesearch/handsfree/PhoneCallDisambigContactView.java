package com.google.android.voicesearch.handsfree;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.contacts.Contact;
import com.google.android.voicesearch.handsfree.ui.QuotedTextView;
import com.google.android.voicesearch.logger.EventLogger;
import java.util.List;

class PhoneCallDisambigContactView
  extends FrameLayout
  implements PhoneCallDisambigContactController.Ui
{
  private PhoneCallDisambigContactController mController;
  private RecognizerViewHelper mRecognizerViewHelper;
  
  public PhoneCallDisambigContactView(Context paramContext)
  {
    super(paramContext);
    initView();
  }
  
  private View createContactItem(LayoutInflater paramLayoutInflater, Contact paramContact, boolean paramBoolean, ViewGroup paramViewGroup)
  {
    if (paramBoolean) {}
    View localView;
    for (int i = 2130968710;; i = 2130968711)
    {
      localView = paramLayoutInflater.inflate(i, paramViewGroup, false);
      if (!paramBoolean) {
        break;
      }
      ((TextView)localView.findViewById(2131296475)).setText(paramContact.getLabel(getContext().getResources()).toUpperCase());
      return localView;
    }
    ((TextView)localView.findViewById(2131296464)).setText(paramContact.getName());
    ((TextView)localView.findViewById(2131296475)).setText(paramContact.getLabel(getContext().getResources()).toLowerCase());
    return localView;
  }
  
  private void initView()
  {
    ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130968709, this);
    findViewById(2131296674).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        PhoneCallDisambigContactView.this.mController.cancelByTouch();
      }
    });
    this.mRecognizerViewHelper = new RecognizerViewHelper(this);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mController.createView();
  }
  
  public void setContacts(List<Contact> paramList, boolean paramBoolean)
  {
    
    if (!paramBoolean)
    {
      View localView2 = findViewById(2131296672);
      if (localView2 == null) {
        localView2 = findViewById(2131296382);
      }
      localView2.setVisibility(8);
    }
    LayoutInflater localLayoutInflater = (LayoutInflater)getContext().getSystemService("layout_inflater");
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131296673);
    for (int i = -1 + paramList.size(); i >= 0; i--)
    {
      final Contact localContact = (Contact)paramList.get(i);
      View localView1 = createContactItem(localLayoutInflater, localContact, paramBoolean, localViewGroup);
      ((QuotedTextView)localView1.findViewById(2131296676)).setQuotedText(Integer.toString(i + 1));
      localView1.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EventLogger.recordClientEventWithSource(44, 16777216, Integer.valueOf(10));
          PhoneCallDisambigContactView.this.mController.pickContactByTouch(localContact);
        }
      });
      localViewGroup.addView(localView1, 0);
    }
  }
  
  public void setController(PhoneCallDisambigContactController paramPhoneCallDisambigContactController)
  {
    this.mController = paramPhoneCallDisambigContactController;
  }
  
  public void setLanguage(String paramString)
  {
    this.mRecognizerViewHelper.setLanguage(paramString);
  }
  
  public void setTitle(String paramString)
  {
    ((TextView)findViewById(2131296382)).setText(paramString);
  }
  
  public void showListening()
  {
    this.mRecognizerViewHelper.showListening();
  }
  
  public void showNotListening()
  {
    this.mRecognizerViewHelper.showNotListening();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.PhoneCallDisambigContactView
 * JD-Core Version:    0.7.0.1
 */