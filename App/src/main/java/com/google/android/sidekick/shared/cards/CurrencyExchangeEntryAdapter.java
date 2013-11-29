package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.CurrencyExchangeEntry;
import com.google.geo.sidekick.Sidekick.Entry;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyExchangeEntryAdapter
  extends BaseEntryAdapter
{
  static final NumberFormat NUMBER_FORMAT = ;
  private final Sidekick.CurrencyExchangeEntry mCurrencyEntry;
  
  static
  {
    NUMBER_FORMAT.setMinimumFractionDigits(2);
  }
  
  public CurrencyExchangeEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper)
  {
    super(paramEntry, paramActivityHelper);
    this.mCurrencyEntry = paramEntry.getCurrencyExchangeEntry();
  }
  
  private String getTargetAmount(float paramFloat)
  {
    return NUMBER_FORMAT.format(paramFloat * this.mCurrencyEntry.getLocalToHomeRate());
  }
  
  public String getSourceAmount(float paramFloat)
  {
    return NUMBER_FORMAT.format(paramFloat);
  }
  
  public View getView(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968648, paramViewGroup, false);
    ((TextView)localView.findViewById(2131296501)).setText(this.mCurrencyEntry.getLocalCurrency());
    float f = this.mCurrencyEntry.getDefaultLocalExchangeAmount();
    EditText localEditText1 = (EditText)localView.findViewById(2131296500);
    localEditText1.setHint(getSourceAmount(f));
    ((TextView)localView.findViewById(2131296503)).setText(this.mCurrencyEntry.getHomeCurrency());
    EditText localEditText2 = (EditText)localView.findViewById(2131296502);
    localEditText2.setHint(getTargetAmount(f));
    updateWhenChanged(localEditText1, localEditText2, this.mCurrencyEntry.getLocalToHomeRate());
    updateWhenChanged(localEditText2, localEditText1, 1.0F / this.mCurrencyEntry.getLocalToHomeRate());
    ((Button)localView.findViewById(2131296504)).setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 53)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        CurrencyExchangeEntryAdapter.this.showDisclaimer(paramContext);
      }
    });
    return localView;
  }
  
  void showDisclaimer(Context paramContext)
  {
    Locale localLocale = Locale.US;
    String str = paramContext.getString(2131362040);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Locale.getDefault().getLanguage();
    openUrl(paramContext, String.format(localLocale, str, arrayOfObject));
  }
  
  void updateWhenChanged(final EditText paramEditText1, final EditText paramEditText2, final float paramFloat)
  {
    paramEditText1.addTextChangedListener(new TextWatcher()
    {
      public void afterTextChanged(Editable paramAnonymousEditable)
      {
        try
        {
          String str = paramAnonymousEditable.toString();
          if ((!str.isEmpty()) || (paramEditText1.hasFocus()))
          {
            if (str.isEmpty())
            {
              paramEditText1.setHint(this.val$defaultSourceValue);
              paramEditText2.setHint(this.val$defaultTargetValue);
            }
            for (;;)
            {
              paramEditText2.setText("");
              return;
              float f = paramFloat * Float.parseFloat(str);
              paramEditText2.setHint(CurrencyExchangeEntryAdapter.NUMBER_FORMAT.format(f));
            }
          }
          return;
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      
      public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.CurrencyExchangeEntryAdapter
 * JD-Core Version:    0.7.0.1
 */