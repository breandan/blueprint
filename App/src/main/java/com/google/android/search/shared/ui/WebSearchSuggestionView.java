package com.google.android.search.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import com.google.android.search.shared.api.Suggestion;

public class WebSearchSuggestionView
  extends BaseSuggestionView
{
  private String mBoundSuggestionForQuery;
  private View mIconAndText1;
  protected ImageView mQueryBuilder;
  
  public WebSearchSuggestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    boolean bool = super.bindAsSuggestion(paramSuggestion, paramString, paramSuggestionFormatter);
    CharSequence localCharSequence1 = paramSuggestion.getSuggestionText1();
    if ((bool) || (!paramString.equals(this.mBoundSuggestionForQuery))) {
      if (!paramSuggestion.isUndoRewrite()) {
        break label90;
      }
    }
    label90:
    for (CharSequence localCharSequence2 = formatUndoSuggestion(paramSuggestion, paramSuggestionFormatter);; localCharSequence2 = formatSuggestion(paramSuggestion, paramString, paramSuggestionFormatter))
    {
      setText1(localCharSequence2);
      this.mBoundSuggestionForQuery = paramString;
      if (bool) {
        this.mQueryBuilder.setContentDescription(getResources().getString(2131363188, new Object[] { localCharSequence1 }));
      }
      return bool;
    }
  }
  
  protected CharSequence formatSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    CharSequence localCharSequence = paramSuggestion.getSuggestionText1();
    if (!(localCharSequence instanceof Spanned)) {
      localCharSequence = paramSuggestionFormatter.formatSuggestion(paramString, localCharSequence, 2131624081, 2131624082);
    }
    return localCharSequence;
  }
  
  protected CharSequence formatUndoSuggestion(Suggestion paramSuggestion, SuggestionFormatter paramSuggestionFormatter)
  {
    Resources localResources = getResources();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramSuggestion.getSuggestionQuery();
    String str = localResources.getString(2131363308, arrayOfObject);
    return paramSuggestionFormatter.formatSuggestion(paramSuggestion.getSuggestionQuery(), str, 2131624080, 0);
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mQueryBuilder = ((ImageView)findViewById(2131297250));
    this.mQueryBuilder.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        WebSearchSuggestionView.this.onSuggestionQueryRefineClicked();
      }
    });
    this.mIconAndText1 = findViewById(2131297249);
    this.mIconAndText1.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        WebSearchSuggestionView.this.onRemoveFromHistoryClicked();
        return true;
      }
    });
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    this.mIconAndText1.setEnabled(paramBoolean);
    this.mQueryBuilder.setEnabled(paramBoolean);
  }
  
  public void setLongClickable(boolean paramBoolean)
  {
    this.mIconAndText1.setLongClickable(paramBoolean);
  }
  
  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mIconAndText1.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnFocusChangeListener(View.OnFocusChangeListener paramOnFocusChangeListener)
  {
    this.mIconAndText1.setOnFocusChangeListener(paramOnFocusChangeListener);
    this.mQueryBuilder.setOnFocusChangeListener(paramOnFocusChangeListener);
  }
  
  public void setOnKeyListener(View.OnKeyListener paramOnKeyListener)
  {
    this.mIconAndText1.setOnKeyListener(paramOnKeyListener);
    this.mQueryBuilder.setOnKeyListener(paramOnKeyListener);
  }
  
  public void setOnLongClickListener(View.OnLongClickListener paramOnLongClickListener)
  {
    this.mIconAndText1.setOnLongClickListener(paramOnLongClickListener);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.WebSearchSuggestionView
 * JD-Core Version:    0.7.0.1
 */