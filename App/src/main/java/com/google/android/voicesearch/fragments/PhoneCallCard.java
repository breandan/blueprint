package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup.Mode;
import com.google.android.speech.contacts.Person;
import com.google.android.voicesearch.contacts.ContactDisambiguationView;
import com.google.android.voicesearch.contacts.ContactSelectMode;
import com.google.android.voicesearch.ui.ActionEditorView;
import java.util.List;

public class PhoneCallCard
  extends CommunicationActionCardImpl<PhoneCallController>
  implements PhoneCallController.Ui
{
  private TextView mContactNotFoundView;
  private ImageView mEmptyCallPicture;
  private ActionEditorView mMainContent;
  
  public PhoneCallCard(Context paramContext)
  {
    super(paramContext);
  }
  
  private void showCallButton(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setConfirmIcon(2130837636);
      setConfirmText(2131363607);
      setConfirmTag(0);
    }
    this.mMainContent.showCountDownView(paramBoolean);
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mMainContent = createActionEditor(paramLayoutInflater, paramViewGroup, 2130968614);
    setContactDisambiguationView((ContactDisambiguationView)this.mMainContent.findViewById(2131296416));
    this.mContactNotFoundView = ((TextView)this.mMainContent.findViewById(2131296417));
    this.mEmptyCallPicture = ((ImageView)this.mMainContent.findViewById(2131296418));
    this.mEmptyCallPicture.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ((PhoneCallController)PhoneCallCard.this.getController()).pickContact();
      }
    });
    this.mMainContent.setContentClickable(false);
    return this.mMainContent;
  }
  
  public void setPeople(List<Person> paramList)
  {
    int i = 1;
    this.mEmptyCallPicture.setVisibility(8);
    setPeople(paramList, ContactSelectMode.CALL_CONTACT);
    this.mContactNotFoundView.setVisibility(8);
    if (paramList.size() == i) {
      if (((Person)paramList.get(0)).getNumSelectableItems(ContactLookup.Mode.PHONE_NUMBER) != i) {
        break label64;
      }
    }
    for (;;)
    {
      showCallButton(i);
      return;
      label64:
      int j = 0;
    }
  }
  
  public void setToContact(Contact paramContact)
  {
    throw new UnsupportedOperationException();
  }
  
  public void showContactDetailsNotFound(List<Person> paramList)
  {
    this.mEmptyCallPicture.setVisibility(8);
    setPeople(paramList, ContactSelectMode.CALL_CONTACT);
    this.mContactNotFoundView.setVisibility(8);
    setConfirmIcon(2130837673);
    setConfirmText(2131363615);
    setConfirmTag(0);
    this.mMainContent.showCountDownView(true);
  }
  
  public void showContactNotFound()
  {
    showContactDisambiguationView(false);
    this.mContactNotFoundView.setVisibility(0);
    this.mEmptyCallPicture.setVisibility(0);
    showFindPeople(true);
  }
  
  public void showEmptyRecipientCard()
  {
    this.mEmptyCallPicture.setVisibility(0);
    showFindPeople(true);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.PhoneCallCard
 * JD-Core Version:    0.7.0.1
 */