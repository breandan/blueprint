package com.google.android.sidekick.shared.ui;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.search.shared.ui.RoundedCornerWebImageView;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class ListCardView
  extends DismissableLinearLayout
{
  private Button mActionButton;
  private CardTableLayout mCardTableLayout;
  @Nullable
  private GroupNodeListAdapter mEntryListAdapter;
  @Nullable
  private ExpandListener mExpandListener;
  private TextView mSmallSummaryView;
  private TextView mSummaryView;
  private TextView mTitleView;
  
  public ListCardView(Context paramContext)
  {
    super(paramContext);
  }
  
  public ListCardView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ListCardView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void addCustomSummaryView(View paramView, boolean paramBoolean)
  {
    addView(paramView, 1 + indexOfChild(this.mSmallSummaryView));
    if (paramBoolean) {
      hideTopListDivider();
    }
  }
  
  @Nullable
  public GroupNodeListAdapter getEntryListAdapter()
  {
    return this.mEntryListAdapter;
  }
  
  public int getRowCount()
  {
    return getDismissableChildCount();
  }
  
  public void hideHeader()
  {
    findViewById(2131296450).setVisibility(8);
    hideTopListDivider();
  }
  
  public void hideTopListDivider()
  {
    findViewById(2131296761).setVisibility(8);
    this.mCardTableLayout.setShowDividers(0xFFFFFFFE & this.mCardTableLayout.getShowDividers());
  }
  
  public boolean isExpanded()
  {
    return this.mCardTableLayout.getVisibility() == 0;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitleView = ((TextView)findViewById(2131296451));
    this.mSummaryView = ((TextView)findViewById(2131296759));
    this.mSmallSummaryView = ((TextView)findViewById(2131296760));
    this.mCardTableLayout = ((CardTableLayout)findViewById(2131296762));
    this.mActionButton = ((Button)findViewById(2131296375));
    setDismissableContainer(this.mCardTableLayout);
    setTag(2131296283, Boolean.valueOf(false));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    Bundle localBundle = (Bundle)paramParcelable;
    super.onRestoreInstanceState(localBundle.getParcelable("parent_state"));
    if (this.mEntryListAdapter != null)
    {
      int i = localBundle.getInt("row_count", -1);
      if (i != -1) {
        this.mEntryListAdapter.setEntriesToShow(i);
      }
    }
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("parent_state", super.onSaveInstanceState());
    if (this.mEntryListAdapter != null) {
      localBundle.putInt("row_count", this.mEntryListAdapter.getCount());
    }
    return localBundle;
  }
  
  public void setEntryListAdapter(GroupNodeListAdapter paramGroupNodeListAdapter)
  {
    this.mEntryListAdapter = paramGroupNodeListAdapter;
    this.mEntryListAdapter.registerDataSetObserver(new EntryListObserver(null));
    updateEntryList();
  }
  
  public void setExpandListener(ExpandListener paramExpandListener)
  {
    this.mExpandListener = paramExpandListener;
  }
  
  public void setHeaderClickListener(View.OnClickListener paramOnClickListener)
  {
    this.mTitleView.setOnClickListener(paramOnClickListener);
    this.mSummaryView.setOnClickListener(paramOnClickListener);
  }
  
  public void setSmallSummary(@Nullable CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      this.mSmallSummaryView.setText(paramCharSequence);
      this.mSmallSummaryView.setVisibility(0);
      return;
    }
    this.mSmallSummaryView.setVisibility(8);
  }
  
  public void setSummary(@Nullable CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      this.mSummaryView.setText(paramCharSequence);
      this.mSummaryView.setVisibility(0);
      return;
    }
    this.mSummaryView.setVisibility(8);
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitleView.setText(paramCharSequence);
  }
  
  public void showActionButton(CharSequence paramCharSequence, int paramInt, View.OnClickListener paramOnClickListener)
  {
    this.mActionButton.setText(paramCharSequence);
    this.mActionButton.setOnClickListener(paramOnClickListener);
    LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(this.mActionButton, paramInt, 0, 0, 0);
    this.mActionButton.setVisibility(0);
    this.mCardTableLayout.setShowDividers(0x4 | this.mCardTableLayout.getShowDividers());
    updateItemCorners();
  }
  
  void updateEntryList()
  {
    boolean bool1 = isExpanded();
    this.mCardTableLayout.removeAllViews();
    if (this.mEntryListAdapter == null) {
      return;
    }
    int i = this.mEntryListAdapter.getCount();
    label134:
    CardTableLayout localCardTableLayout;
    if (i > 0)
    {
      ArrayList localArrayList = Lists.newArrayListWithCapacity(i);
      for (int k = 0; k < i; k++)
      {
        View localView = this.mEntryListAdapter.getView(k, null, this.mCardTableLayout);
        this.mCardTableLayout.addView(localView);
        localView.setTag(2131296280, this.mEntryListAdapter.getEntry(k));
        localArrayList.add(localView);
      }
      updateItemCorners();
      setTag(2131296281, localArrayList);
      if (getLayoutTransition() == null) {
        setLayoutTransition(new LayoutTransition());
      }
      localCardTableLayout = this.mCardTableLayout;
      if (i <= 0) {
        break label205;
      }
    }
    label205:
    for (int j = 0;; j = 8)
    {
      localCardTableLayout.setVisibility(j);
      boolean bool2 = isExpanded();
      setTag(2131296283, Boolean.valueOf(bool2));
      if ((this.mExpandListener == null) || (bool1) || (!bool2)) {
        break;
      }
      this.mExpandListener.onExpand();
      return;
      setTag(2131296281, null);
      break label134;
    }
  }
  
  public void updateItemCorners()
  {
    int i = getRowCount();
    if (i == 0) {
      return;
    }
    int j;
    label22:
    label43:
    int k;
    int m;
    label48:
    View localView1;
    if (this.mActionButton.getVisibility() != 0)
    {
      j = 1;
      if (j == 0) {
        break label92;
      }
      this.mCardTableLayout.setBackground(getResources().getDrawable(2130837534));
      k = 0;
      m = 0;
      if (m < this.mCardTableLayout.getChildCount())
      {
        localView1 = this.mCardTableLayout.getChildAt(m);
        if (localView1.getVisibility() != 8) {
          break label112;
        }
      }
    }
    label92:
    label112:
    int n;
    label122:
    int i1;
    label139:
    do
    {
      m++;
      break label48;
      break;
      j = 0;
      break label22;
      this.mCardTableLayout.setBackgroundColor(getResources().getColor(2131230857));
      break label43;
      if (k != i - 1) {
        break label243;
      }
      n = 1;
      k++;
      if ((j == 0) || (n == 0)) {
        break label249;
      }
      i1 = 2130837541;
      localView1.setBackground(getResources().getDrawable(i1));
    } while (!(localView1 instanceof ViewGroup));
    ViewGroup localViewGroup = (ViewGroup)localView1;
    int i2 = localViewGroup.getChildCount();
    int i3 = getResources().getDimensionPixelSize(2131689748);
    int i4 = 0;
    label190:
    View localView2;
    if (i4 < i2)
    {
      localView2 = localViewGroup.getChildAt(i4);
      if ((localView2 instanceof RoundedCornerWebImageView))
      {
        if ((j == 0) || (n == 0)) {
          break label257;
        }
        ((RoundedCornerWebImageView)localView2).setCornerRadii(0.0F, 0.0F, i3, 0.0F);
      }
    }
    for (;;)
    {
      i4++;
      break label190;
      break;
      label243:
      n = 0;
      break label122;
      label249:
      i1 = 2130837542;
      break label139;
      label257:
      ((RoundedCornerWebImageView)localView2).setCornerRadii(0.0F, 0.0F, 0.0F, 0.0F);
    }
  }
  
  protected void updateItems()
  {
    super.updateItems();
    updateItemCorners();
  }
  
  private class EntryListObserver
    extends DataSetObserver
  {
    private EntryListObserver() {}
    
    public void onChanged()
    {
      ListCardView.this.updateEntryList();
    }
    
    public void onInvalidated()
    {
      ListCardView.this.updateEntryList();
    }
  }
  
  public static abstract interface ExpandListener
  {
    public abstract void onExpand();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.ListCardView
 * JD-Core Version:    0.7.0.1
 */