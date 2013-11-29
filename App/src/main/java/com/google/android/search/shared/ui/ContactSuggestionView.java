package com.google.android.search.shared.ui;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.search.shared.api.Suggestion;

public class ContactSuggestionView
  extends DefaultSuggestionView
{
  private static final String SCHEMA_CONTACTS = ContactsContract.AUTHORITY_URI.getScheme();
  private ContactBadge mQuickContact;
  
  public ContactSuggestionView(Context paramContext)
  {
    super(paramContext);
  }
  
  public ContactSuggestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ContactSuggestionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    if (!super.bindAsSuggestion(paramSuggestion, paramString, paramSuggestionFormatter)) {
      return false;
    }
    String str = paramSuggestion.getSuggestionIntentDataString();
    Uri localUri;
    if (str != null)
    {
      localUri = Uri.parse(str);
      if ("tel".equals(localUri.getScheme())) {
        this.mQuickContact.assignContactFromPhone(localUri.getSchemeSpecificPart(), true);
      }
    }
    for (;;)
    {
      this.mQuickContact.setExtraOnClickListener(new ContactBadgeClickListener(null));
      return true;
      if ("mailto".equals(localUri.getScheme()))
      {
        this.mQuickContact.assignContactFromEmail(localUri.getSchemeSpecificPart(), true);
      }
      else if ((SCHEMA_CONTACTS.equals(localUri.getScheme())) && ("com.android.contacts".equals(localUri.getAuthority())))
      {
        this.mQuickContact.assignContactUri(localUri);
      }
      else
      {
        Log.w("ContactSuggestionView", "Unsupported URI: " + str);
        this.mQuickContact.assignContactUri(null);
        continue;
        Log.w("ContactSuggestionView", paramSuggestion.toString() + " missing valid URI.");
        this.mQuickContact.assignContactUri(null);
      }
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mQuickContact = ((ContactBadge)findViewById(2131296479));
  }
  
  private class ContactBadgeClickListener
    implements View.OnClickListener
  {
    private ContactBadgeClickListener() {}
    
    public void onClick(View paramView)
    {
      ContactSuggestionView.this.onSuggestionQuickContactClicked();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.ContactSuggestionView
 * JD-Core Version:    0.7.0.1
 */