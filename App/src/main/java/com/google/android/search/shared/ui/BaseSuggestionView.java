package com.google.android.search.shared.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Preconditions;

public abstract class BaseSuggestionView
  extends RelativeLayout
  implements SuggestionView
{
  private Suggestion mBoundSuggestion;
  protected View mDivider;
  protected ImageView mIcon;
  private UriLoader<Drawable> mIconLoader;
  protected int mIconMode;
  private SuggestionView.ClickListener mListener;
  protected TextView mText1;
  protected TextView mText2;
  
  public BaseSuggestionView(Context paramContext)
  {
    super(paramContext);
  }
  
  public BaseSuggestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public BaseSuggestionView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void updateText1(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mText1.setSingleLine(true);
      this.mText1.setMaxLines(1);
      this.mText1.setEllipsize(TextUtils.TruncateAt.MIDDLE);
      this.mText1.setGravity(80);
      return;
    }
    this.mText1.setSingleLine(false);
    this.mText1.setMaxLines(2);
    this.mText1.setEllipsize(TextUtils.TruncateAt.START);
    this.mText1.setGravity(16);
  }
  
  public boolean bindAsSuggestion(Suggestion paramSuggestion, String paramString, SuggestionFormatter paramSuggestionFormatter)
  {
    if (this.mBoundSuggestion != paramSuggestion)
    {
      setOnClickListener(new ClickListener(null));
      this.mBoundSuggestion = paramSuggestion;
      return true;
    }
    return false;
  }
  
  public Suggestion getBoundSuggestion()
  {
    return this.mBoundSuggestion;
  }
  
  protected UriLoader<Drawable> getIconLoader()
  {
    return this.mIconLoader;
  }
  
  public CharSequence getText1()
  {
    return this.mText1.getText();
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mText1 = ((TextView)findViewById(2131296480));
    this.mText2 = ((TextView)findViewById(2131296481));
    this.mIcon = ((ImageView)findViewById(2131296479));
    this.mDivider = findViewById(2131296482);
  }
  
  protected void onRemoveFromHistoryClicked()
  {
    this.mListener.onSuggestionRemoveFromHistoryClicked(this.mBoundSuggestion);
  }
  
  protected void onSuggestionClicked()
  {
    this.mListener.onSuggestionClicked(this.mBoundSuggestion);
  }
  
  protected void onSuggestionQueryRefineClicked()
  {
    this.mListener.onSuggestionQueryRefineClicked(this.mBoundSuggestion);
  }
  
  protected void onSuggestionQuickContactClicked()
  {
    this.mListener.onSuggestionQuickContactClicked(this.mBoundSuggestion);
  }
  
  public void setClickListener(SuggestionView.ClickListener paramClickListener)
  {
    this.mListener = paramClickListener;
  }
  
  public void setDividerVisibility(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 4) || (paramInt == 8)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      this.mDivider.setVisibility(paramInt);
      return;
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    ImageView localImageView;
    if (this.mIcon != null)
    {
      localImageView = this.mIcon;
      if (!paramBoolean) {
        break label29;
      }
    }
    label29:
    for (float f = 1.0F;; f = 0.5F)
    {
      localImageView.setAlpha(f);
      return;
    }
  }
  
  public void setIconLoader(UriLoader<Drawable> paramUriLoader)
  {
    this.mIconLoader = paramUriLoader;
  }
  
  public void setIconMode(int paramInt)
  {
    this.mIconMode = paramInt;
  }
  
  protected void setText1(CharSequence paramCharSequence)
  {
    this.mText1.setText(paramCharSequence);
  }
  
  protected void setText2(CharSequence paramCharSequence)
  {
    this.mText2.setText(paramCharSequence);
    if (TextUtils.isEmpty(paramCharSequence))
    {
      this.mText2.setVisibility(8);
      updateText1(false);
      return;
    }
    this.mText2.setVisibility(0);
    updateText1(true);
  }
  
  public String toString()
  {
    return super.toString() + " suggestion=" + this.mBoundSuggestion;
  }
  
  private class ClickListener
    implements View.OnClickListener
  {
    private ClickListener() {}
    
    public void onClick(View paramView)
    {
      BaseSuggestionView.this.onSuggestionClicked();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.BaseSuggestionView
 * JD-Core Version:    0.7.0.1
 */