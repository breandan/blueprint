package com.google.android.search.shared.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.googlequicksearchbox.R.styleable;
import com.google.android.search.shared.api.Suggestion;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Preconditions;
import java.util.List;

public class SuggestionListView
  extends FrameLayout
{
  private int mColumnCount = 1;
  private TextView mCountView;
  private SuggestionViewFactory mFactory;
  private View mFooterView;
  private SuggestionFormatter mFormatter;
  private View mHeaderView;
  private UriLoader<Drawable> mIconLoader;
  private int mNumNonSuggestionViews;
  private boolean mShowAllDividers;
  private SuggestionClickListener mSuggestionClickListener;
  private int mSuggestionViewTypeCount;
  private TextView mTitleView;
  private ViewRecycler mViewRecycler;
  
  public SuggestionListView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SuggestionListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SuggestionListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SuggestionListView, paramInt, 0);
    this.mColumnCount = localTypedArray.getInteger(0, 1);
    this.mShowAllDividers = localTypedArray.getBoolean(1, false);
    localTypedArray.recycle();
  }
  
  private int getViewType(View paramView)
  {
    return this.mFactory.getSuggestionViewType(((SuggestionView)paramView).getBoundSuggestion());
  }
  
  private boolean hasDuplicateIcons(List<Suggestion> paramList)
  {
    int i = paramList.size();
    if (i <= 1) {}
    label146:
    label152:
    label158:
    label164:
    label168:
    for (;;)
    {
      return false;
      Suggestion localSuggestion1 = (Suggestion)paramList.get(0);
      String str = null;
      if (localSuggestion1 != null)
      {
        if (!localSuggestion1.isContactSuggestion()) {
          str = localSuggestion1.getSuggestionIcon1();
        }
      }
      else
      {
        int j = 1;
        if (j >= i) {
          break;
        }
        Suggestion localSuggestion2 = (Suggestion)paramList.get(j);
        int k;
        label88:
        int m;
        label96:
        int n;
        if (localSuggestion2 != null)
        {
          if ((str == null) || (localSuggestion2.getSuggestionIcon1() == null)) {
            break label146;
          }
          k = 1;
          if (str != null) {
            break label152;
          }
          m = 1;
          if (localSuggestion2.getSuggestionIcon1() != null) {
            break label158;
          }
          n = 1;
          label107:
          if (m == n) {
            break label164;
          }
        }
        for (int i1 = 1;; i1 = 0)
        {
          if ((i1 != 0) || ((k != 0) && (!str.equals(localSuggestion2.getSuggestionIcon1())))) {
            break label168;
          }
          j++;
          break;
          k = 0;
          break label88;
          m = 0;
          break label96;
          n = 0;
          break label107;
        }
      }
    }
    return true;
  }
  
  private boolean isEndOfRow(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2 - 1) {}
    while ((1 + (paramInt1 - this.mNumNonSuggestionViews)) % this.mColumnCount == 0) {
      return true;
    }
    return false;
  }
  
  private void layoutChildAt(View paramView, int paramInt1, int paramInt2)
  {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramView.getMeasuredWidth(), paramInt2 + paramView.getMeasuredHeight());
  }
  
  private View prepareSuggestionView(Suggestion paramSuggestion, int paramInt, View paramView, String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramView == null) {
      paramView = this.mFactory.createSuggestionView(getContext(), paramSuggestion, this);
    }
    SuggestionView localSuggestionView = (SuggestionView)paramView;
    localSuggestionView.setIconLoader(this.mIconLoader);
    int i;
    if (paramBoolean1)
    {
      i = 0;
      localSuggestionView.setIconMode(i);
      localSuggestionView.bindAsSuggestion(paramSuggestion, paramString, this.mFormatter);
      if (paramBoolean2) {
        localSuggestionView.setClickListener(new SuggestionView.ClickListener()
        {
          public void onSuggestionClicked(Suggestion paramAnonymousSuggestion)
          {
            SuggestionListView.this.mSuggestionClickListener.onSuggestionClicked(paramAnonymousSuggestion);
          }
          
          public void onSuggestionQueryRefineClicked(Suggestion paramAnonymousSuggestion)
          {
            SuggestionListView.this.mSuggestionClickListener.onSuggestionQueryRefineClicked(paramAnonymousSuggestion);
          }
          
          public void onSuggestionQuickContactClicked(Suggestion paramAnonymousSuggestion)
          {
            SuggestionListView.this.mSuggestionClickListener.onSuggestionQuickContactClicked(paramAnonymousSuggestion);
          }
          
          public void onSuggestionRemoveFromHistoryClicked(Suggestion paramAnonymousSuggestion)
          {
            SuggestionListView.this.mSuggestionClickListener.onSuggestionRemoveFromHistoryClicked(paramAnonymousSuggestion);
          }
        });
      }
      localSuggestionView.setEnabled(paramBoolean2);
      localSuggestionView.setDividerVisibility(0);
      paramView.setNextFocusDownId(-1);
      if (!paramBoolean3) {
        break label134;
      }
    }
    label134:
    for (int j = paramView.getId();; j = -1)
    {
      paramView.setNextFocusRightId(j);
      return paramView;
      i = 2;
      break;
    }
  }
  
  private void removeSuggestionsAfterIndex(int paramInt)
  {
    for (int i = -1 + (getChildCount() - this.mNumNonSuggestionViews); i >= paramInt; i--)
    {
      int j = i + this.mNumNonSuggestionViews;
      View localView = getChildAt(j);
      removeViewAt(j);
      this.mViewRecycler.releaseView(localView, getViewType(localView), this.mSuggestionViewTypeCount);
    }
  }
  
  private void updateLastRow()
  {
    int i = getChildCount() - this.mNumNonSuggestionViews;
    if (i > 0)
    {
      int j = (i - 1) / this.mColumnCount * this.mColumnCount + this.mNumNonSuggestionViews;
      int k = -1;
      if ((this.mFooterView != null) && (this.mFooterView.getVisibility() == 0)) {
        k = this.mFooterView.getId();
      }
      int m = j;
      if (m < getChildCount())
      {
        View localView = getChildAt(m);
        localView.setNextFocusDownId(k);
        SuggestionView localSuggestionView = (SuggestionView)localView;
        if (this.mShowAllDividers) {}
        for (int n = 0;; n = 8)
        {
          localSuggestionView.setDividerVisibility(n);
          m++;
          break;
        }
      }
    }
  }
  
  public void hideSuggestions()
  {
    int i = getChildCount();
    for (int j = this.mNumNonSuggestionViews; j < i; j++) {
      getChildAt(j).setEnabled(false);
    }
  }
  
  public void init(SuggestionViewFactory paramSuggestionViewFactory, SuggestionFormatter paramSuggestionFormatter, UriLoader<Drawable> paramUriLoader, ViewRecycler paramViewRecycler)
  {
    this.mFactory = paramSuggestionViewFactory;
    this.mFormatter = paramSuggestionFormatter;
    this.mIconLoader = paramUriLoader;
    this.mViewRecycler = paramViewRecycler;
    this.mSuggestionViewTypeCount = this.mFactory.getNumSuggestionViewTypes();
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mHeaderView = findViewById(2131297063);
    this.mFooterView = findViewById(2131296965);
    this.mTitleView = ((TextView)findViewById(2131297064));
    this.mCountView = ((TextView)findViewById(2131297065));
    this.mNumNonSuggestionViews = getChildCount();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    if ((this.mHeaderView != null) && (this.mHeaderView.getVisibility() != 8))
    {
      layoutChildAt(this.mHeaderView, i, j);
      j += this.mHeaderView.getMeasuredHeight();
    }
    int k = getChildCount();
    int m = 0;
    int n = this.mNumNonSuggestionViews;
    if (n < k)
    {
      View localView = getChildAt(n);
      layoutChildAt(localView, i, j);
      m = Math.max(localView.getMeasuredHeight(), m);
      if (isEndOfRow(n, k))
      {
        j += m;
        m = 0;
        i = getPaddingLeft();
      }
      for (;;)
      {
        n++;
        break;
        i += localView.getMeasuredWidth();
      }
    }
    if ((this.mFooterView != null) && (this.mFooterView.getVisibility() != 8)) {
      layoutChildAt(this.mFooterView, i, j);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    if (i == 1073741824) {}
    int n;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkArgument(bool);
      n = getPaddingTop();
      if ((this.mHeaderView != null) && (this.mHeaderView.getVisibility() != 8))
      {
        measureChild(this.mHeaderView, paramInt1, paramInt2);
        n += this.mHeaderView.getMeasuredHeight();
      }
      int i1 = getChildCount();
      int i2 = getPaddingLeft() + getPaddingRight();
      int i3 = View.MeasureSpec.makeMeasureSpec(i2 + (k - i2) / this.mColumnCount, 1073741824);
      int i4 = 0;
      for (int i5 = this.mNumNonSuggestionViews; i5 < i1; i5++)
      {
        View localView = getChildAt(i5);
        measureChild(localView, i3, paramInt2);
        i4 = Math.max(localView.getMeasuredHeight(), i4);
        if (isEndOfRow(i5, i1))
        {
          n += i4;
          i4 = 0;
        }
      }
    }
    if ((this.mFooterView != null) && (this.mFooterView.getVisibility() != 8))
    {
      measureChild(this.mFooterView, paramInt1, paramInt2);
      n += this.mFooterView.getMeasuredHeight();
    }
    int i6 = n + getPaddingBottom();
    if (j == 1073741824) {
      i6 = m;
    }
    for (;;)
    {
      setMeasuredDimension(k, i6);
      return;
      if (j == -2147483648) {
        i6 = Math.min(i6, m);
      }
    }
  }
  
  public void retain(int paramInt)
  {
    if (paramInt == 0)
    {
      setVisibility(8);
      return;
    }
    removeSuggestionsAfterIndex(paramInt);
    updateLastRow();
  }
  
  public void setCountText(int paramInt)
  {
    if (this.mCountView != null) {
      this.mCountView.setText(String.valueOf(paramInt));
    }
  }
  
  public void setFooterClickListener(View.OnClickListener paramOnClickListener)
  {
    if (this.mFooterView != null) {
      this.mFooterView.setOnClickListener(paramOnClickListener);
    }
  }
  
  public void setFooterVisibility(int paramInt)
  {
    if (this.mFooterView != null)
    {
      this.mFooterView.setVisibility(paramInt);
      updateLastRow();
    }
  }
  
  public void setShowAllDividers(boolean paramBoolean)
  {
    if (this.mShowAllDividers != paramBoolean)
    {
      this.mShowAllDividers = paramBoolean;
      updateLastRow();
    }
  }
  
  public void setSuggestionClickListener(SuggestionClickListener paramSuggestionClickListener)
  {
    if ((this.mSuggestionClickListener == null) || (paramSuggestionClickListener == null)) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool, "Can't reset click listener");
      this.mSuggestionClickListener = paramSuggestionClickListener;
      return;
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (this.mTitleView != null) {
      this.mTitleView.setText(paramCharSequence);
    }
  }
  
  public void showSuggestions(String paramString, List<Suggestion> paramList, int paramInt, boolean paramBoolean)
  {
    if (paramInt <= paramList.size()) {}
    for (boolean bool1 = true;; bool1 = false)
    {
      Preconditions.checkState(bool1);
      if (paramInt != 0) {
        break;
      }
      setVisibility(8);
      return;
    }
    boolean bool2 = hasDuplicateIcons(paramList);
    int i = getChildCount();
    int j = 0;
    if (j < paramInt)
    {
      boolean bool3;
      label72:
      boolean bool4;
      label85:
      Suggestion localSuggestion;
      int k;
      int m;
      View localView1;
      if ((j + 1) % this.mColumnCount == 0)
      {
        bool3 = true;
        if ((bool2) && (j != 0)) {
          break label179;
        }
        bool4 = true;
        localSuggestion = (Suggestion)paramList.get(j);
        k = this.mFactory.getSuggestionViewType(localSuggestion);
        m = j + this.mNumNonSuggestionViews;
        if (m >= i) {
          break label185;
        }
        localView1 = getChildAt(m);
        label133:
        if ((localView1 == null) || (getViewType(localView1) != k)) {
          break label191;
        }
        prepareSuggestionView(localSuggestion, j, localView1, paramString, bool4, paramBoolean, bool3);
      }
      for (;;)
      {
        j++;
        break;
        bool3 = false;
        break label72;
        label179:
        bool4 = false;
        break label85;
        label185:
        localView1 = null;
        break label133;
        label191:
        if (localView1 != null)
        {
          removeViewAt(m);
          this.mViewRecycler.releaseView(localView1, getViewType(localView1), this.mSuggestionViewTypeCount);
        }
        View localView2 = this.mViewRecycler.getView(k);
        addView(prepareSuggestionView(localSuggestion, j, localView2, paramString, bool4, paramBoolean, bool3), m);
      }
    }
    removeSuggestionsAfterIndex(paramInt);
    updateLastRow();
    setVisibility(0);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SuggestionListView
 * JD-Core Version:    0.7.0.1
 */