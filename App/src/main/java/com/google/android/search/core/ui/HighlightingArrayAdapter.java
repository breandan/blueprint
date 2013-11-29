package com.google.android.search.core.ui;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class HighlightingArrayAdapter<T>
  extends ArrayAdapter<T>
{
  private final CharacterStyle mMatchStyle;
  private final CharacterStyle mPostMatchStyle;
  private final CharacterStyle mPreMatchStyle;
  private String mQuery;
  
  public HighlightingArrayAdapter(Context paramContext, int paramInt1, int paramInt2, List<T> paramList)
  {
    super(paramContext, paramInt1, 16908310, paramList);
    this.mPreMatchStyle = new TextAppearanceSpan(paramContext, 2131624094);
    this.mPostMatchStyle = new TextAppearanceSpan(paramContext, 2131624094);
    this.mMatchStyle = new TextAppearanceSpan(paramContext, 2131624095);
  }
  
  public HighlightingArrayAdapter(Context paramContext, List<T> paramList)
  {
    this(paramContext, 2130968834, 16908310, paramList);
  }
  
  private CharSequence hilightQuery(CharSequence paramCharSequence, String paramString)
  {
    int i = paramCharSequence.toString().toLowerCase(Locale.getDefault()).indexOf(paramString);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
    if (i > -1)
    {
      int j = i + paramString.length();
      localSpannableStringBuilder.setSpan(this.mPreMatchStyle, 0, i, 0);
      localSpannableStringBuilder.setSpan(this.mMatchStyle, i, j, 0);
      localSpannableStringBuilder.setSpan(this.mPostMatchStyle, j, paramCharSequence.length(), 0);
      return localSpannableStringBuilder;
    }
    localSpannableStringBuilder.setSpan(this.mPreMatchStyle, 0, paramCharSequence.length(), 0);
    return localSpannableStringBuilder;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = super.getView(paramInt, paramView, paramViewGroup);
    Object localObject = getItem(paramInt);
    if (this.mQuery != null) {
      ((TextView)localView.findViewById(16908310)).setText(hilightQuery(localObject.toString(), this.mQuery));
    }
    return localView;
  }
  
  void setQuery(String paramString)
  {
    this.mQuery = paramString.toLowerCase(Locale.getDefault());
  }
  
  public void update(List<T> paramList, String paramString)
  {
    clear();
    setQuery(paramString);
    addAll(paramList);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ui.HighlightingArrayAdapter
 * JD-Core Version:    0.7.0.1
 */