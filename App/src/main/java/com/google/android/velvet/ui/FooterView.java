package com.google.android.velvet.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.google.android.shared.util.Animations;
import com.google.android.velvet.Corpus;
import com.google.android.velvet.presenter.FooterPresenter;
import com.google.android.velvet.presenter.FooterUi;
import com.google.android.velvet.ui.widget.CorpusBar;
import com.google.common.base.Preconditions;

public class FooterView
  extends LinearLayout
  implements FooterUi
{
  private CorpusBar mCorpusBar;
  private View mCorpusBarContainer;
  private ViewStub mCorpusBarStub;
  private View mFooterDividerCorpora;
  private View mFooterDividerTgButton;
  private View mNowButtonBar;
  private ViewStub mNowButtonBarStub;
  private FooterPresenter mPresenter;
  private ImageButton mRemindersButton;
  private boolean mShowingCorpusBar;
  private View mTgOverflowButton;
  private ImageButton mTrainingButton;
  
  public FooterView(Context paramContext)
  {
    super(paramContext);
  }
  
  public FooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public FooterView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void inflateCorpusBar()
  {
    if (!isCorpusBarLoaded()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mCorpusBarContainer = this.mCorpusBarStub.inflate();
      this.mCorpusBar = ((CorpusBar)this.mCorpusBarContainer.findViewById(2131296487));
      this.mCorpusBar.setPresenter(this.mPresenter);
      if (ViewConfiguration.get(this.mCorpusBarContainer.getContext()).hasPermanentMenuKey()) {
        this.mCorpusBarContainer.findViewById(2131296488).setVisibility(8);
      }
      this.mPresenter.onCorpusBarLoaded();
      return;
    }
  }
  
  private void inflateNowButtonBar()
  {
    if (!isNowBarLoaded()) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mNowButtonBar = this.mNowButtonBarStub.inflate();
      this.mRemindersButton = ((ImageButton)this.mNowButtonBar.findViewById(2131296719));
      this.mRemindersButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          FooterView.this.mPresenter.onRemindersButtonPressed();
        }
      });
      this.mTrainingButton = ((ImageButton)this.mNowButtonBar.findViewById(2131296720));
      this.mTrainingButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          FooterView.this.mPresenter.onTrainingButtonPressed();
        }
      });
      this.mTgOverflowButton = this.mNowButtonBar.findViewById(2131296722);
      if (!ViewConfiguration.get(this.mNowButtonBar.getContext()).hasPermanentMenuKey()) {
        break;
      }
      this.mTgOverflowButton.setVisibility(8);
      this.mNowButtonBar.findViewById(2131296721).setVisibility(8);
      return;
    }
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        FooterView.this.mPresenter.onMenuButtonClick(paramAnonymousView);
      }
    };
    this.mTgOverflowButton.setOnClickListener(local3);
  }
  
  private boolean isNowBarLoaded()
  {
    return this.mNowButtonBar != null;
  }
  
  public void addCorpusSelector(Corpus paramCorpus)
  {
    Preconditions.checkState(isCorpusBarLoaded());
    this.mCorpusBar.addCorpusSelector(paramCorpus);
  }
  
  public boolean isCorpusBarLoaded()
  {
    return this.mCorpusBarContainer != null;
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mCorpusBarStub = ((ViewStub)findViewById(2131296643));
    this.mNowButtonBarStub = ((ViewStub)findViewById(2131296644));
    this.mFooterDividerTgButton = findViewById(2131296642);
    this.mFooterDividerCorpora = findViewById(2131296641);
  }
  
  public void removeCorpusSelectors(Corpus paramCorpus)
  {
    Preconditions.checkState(isCorpusBarLoaded());
    this.mCorpusBar.removeCorpusSelectors(paramCorpus);
  }
  
  public void resetShowMoreCorpora()
  {
    Preconditions.checkState(isCorpusBarLoaded());
    this.mCorpusBar.resetSelectedCorpus();
  }
  
  public void setPresenter(FooterPresenter paramFooterPresenter)
  {
    this.mPresenter = paramFooterPresenter;
  }
  
  public void setRemindersButtonVisibility(int paramInt)
  {
    this.mRemindersButton.setVisibility(paramInt);
  }
  
  public void setSelectedCorpus(Corpus paramCorpus)
  {
    Preconditions.checkState(isCorpusBarLoaded());
    this.mCorpusBar.setSelectedCorpus(paramCorpus);
  }
  
  public void setShowCorpusBar(boolean paramBoolean)
  {
    View localView;
    if (paramBoolean)
    {
      if (!isCorpusBarLoaded()) {
        inflateCorpusBar();
      }
      localView = this.mFooterDividerCorpora;
      if (!paramBoolean) {
        break label63;
      }
    }
    label63:
    for (int i = 0;; i = 8)
    {
      localView.setVisibility(i);
      this.mShowingCorpusBar = showOrHideView(paramBoolean, this.mShowingCorpusBar, this.mCorpusBarContainer);
      return;
      if (isNowBarLoaded()) {
        break;
      }
      inflateNowButtonBar();
      break;
    }
  }
  
  public void setShowTgFooterButton(boolean paramBoolean)
  {
    View localView = this.mFooterDividerTgButton;
    int i;
    int j;
    label29:
    ImageButton localImageButton2;
    int k;
    if (paramBoolean)
    {
      i = 0;
      localView.setVisibility(i);
      ImageButton localImageButton1 = this.mRemindersButton;
      if (!paramBoolean) {
        break label63;
      }
      j = 0;
      localImageButton1.setVisibility(j);
      localImageButton2 = this.mTrainingButton;
      k = 0;
      if (!paramBoolean) {
        break label70;
      }
    }
    for (;;)
    {
      localImageButton2.setVisibility(k);
      return;
      i = 8;
      break;
      label63:
      j = 8;
      break label29;
      label70:
      k = 8;
    }
  }
  
  protected boolean showOrHideView(boolean paramBoolean1, boolean paramBoolean2, View paramView)
  {
    if ((paramBoolean1 != paramBoolean2) && (paramView != null))
    {
      if (paramBoolean1) {
        Animations.showAndFadeIn(paramView);
      }
    }
    else {
      return paramBoolean1;
    }
    Animations.fadeOutAndHide(paramView, 4);
    return paramBoolean1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.ui.FooterView
 * JD-Core Version:    0.7.0.1
 */