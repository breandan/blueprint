package com.google.android.search.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.google.android.search.shared.api.Suggestion;
import javax.annotation.Nullable;

public class DefaultSuggestionView
  extends BaseSuggestionView
{
  private AsyncIcon mAsyncIcon1;
  @Nullable
  private View mFrameIcon1;
  private int mFrameIconPadding;
  
  public DefaultSuggestionView(Context paramContext)
  {
    super(paramContext);
  }
  
  public DefaultSuggestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public DefaultSuggestionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private CharSequence formatUrl(CharSequence paramCharSequence)
  {
    SpannableString localSpannableString = new SpannableString(paramCharSequence);
    localSpannableString.setSpan(new TextAppearanceSpan(getContext(), 2131624077), 0, paramCharSequence.length(), 33);
    return localSpannableString;
  }
  
  public boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    if (!super.bindAsSuggestion(paramSuggestion, paramString, paramSuggestionFormatter)) {
      return false;
    }
    CharSequence localCharSequence1 = paramSuggestion.getSuggestionText1();
    String str = paramSuggestion.getSuggestionText2Url();
    CharSequence localCharSequence2;
    label85:
    View localView;
    if (str != null)
    {
      localCharSequence2 = formatUrl(str);
      setBackgroundResource(2130837522);
      setPadding(0, 0, 0, 0);
      if (this.mFrameIcon1 != null)
      {
        this.mFrameIcon1.setBackgroundResource(2131230791);
        if (!paramSuggestion.hasFullSizeIcon()) {
          break label176;
        }
        this.mFrameIcon1.setPadding(0, 0, 0, 0);
      }
      setText1(paramSuggestionFormatter.formatSuggestion(paramString, localCharSequence1, 2131624083, 2131624080));
      setText2(localCharSequence2);
      localView = (View)this.mIcon.getParent();
      localView.setContentDescription(localCharSequence1);
      if (this.mIconMode != 2) {
        break label202;
      }
      this.mAsyncIcon1.clearDrawable();
    }
    for (;;)
    {
      int i = this.mIconMode;
      int j = 0;
      if (i == 2) {
        j = 4;
      }
      localView.setVisibility(j);
      return true;
      localCharSequence2 = paramSuggestion.getSuggestionText2();
      break;
      label176:
      this.mFrameIcon1.setPadding(this.mFrameIconPadding, this.mFrameIconPadding, this.mFrameIconPadding, this.mFrameIconPadding);
      break label85;
      label202:
      this.mAsyncIcon1.set(paramSuggestion.getSuggestionIcon1(), paramSuggestion.getSourceIcon(), getIconLoader());
    }
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mFrameIcon1 = findViewById(2131297062);
    this.mAsyncIcon1 = new AsyncIcon(this.mIcon);
    this.mFrameIconPadding = getContext().getResources().getDimensionPixelSize(2131689570);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    if (paramBoolean) {
      if (this.mFrameIcon1 != null) {
        this.mFrameIcon1.setAlpha(1.0F);
      }
    }
    while (this.mFrameIcon1 == null) {
      return;
    }
    this.mFrameIcon1.setAlpha(0.5F);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.DefaultSuggestionView
 * JD-Core Version:    0.7.0.1
 */