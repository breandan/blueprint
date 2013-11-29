package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.voicesearch.util.ExampleContactHelper.Contact;

public class HelpCard
  extends AbstractCardView<HelpController>
  implements View.OnClickListener, HelpController.Ui
{
  private SimpleHelpCard mSimpleHelpCard;
  
  public HelpCard(Context paramContext)
  {
    super(paramContext);
  }
  
  public void onClick(View paramView)
  {
    ((HelpController)getController()).refreshExample();
  }
  
  public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mSimpleHelpCard = new SimpleHelpCard(getContext());
    this.mSimpleHelpCard.setOnRefreshExample(this);
    return this.mSimpleHelpCard;
  }
  
  public void setExampleQuery(String paramString)
  {
    this.mSimpleHelpCard.setExampleQuery(paramString);
  }
  
  public void setHeadline(String paramString)
  {
    this.mSimpleHelpCard.setHeadline(paramString);
  }
  
  public void setIntroduction(String paramString1, String paramString2)
  {
    this.mSimpleHelpCard.setIntroduction(paramString1, paramString2);
  }
  
  public void setPreviewContact(ExampleContactHelper.Contact paramContact)
  {
    this.mSimpleHelpCard.setPreviewContact(paramContact);
  }
  
  public void setPreviewDate(String paramString1, String paramString2, String paramString3)
  {
    this.mSimpleHelpCard.setPreviewDate(paramString1, paramString2, paramString3);
  }
  
  public void setPreviewTime(String paramString1, String paramString2)
  {
    this.mSimpleHelpCard.setPreviewTime(paramString1, paramString2);
  }
  
  public void setPreviewUrl(String paramString)
  {
    this.mSimpleHelpCard.setPreviewUrl(paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.HelpCard
 * JD-Core Version:    0.7.0.1
 */