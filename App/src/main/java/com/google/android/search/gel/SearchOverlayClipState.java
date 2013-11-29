package com.google.android.search.gel;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import com.google.android.search.shared.ui.SearchPlate;
import com.google.android.shared.util.LayoutUtils;

public class SearchOverlayClipState
{
  private final SearchOverlayClipAnimation mAnimation;
  private final SearchOverlayLayout mContainer;
  private final GoogleNowPromoController mNowPromoController;
  private final int mNowPromoHeight;
  private final View mRecognizerView;
  private final Rect mRect;
  private final GelSearchPlateContainer mSearchPlateContainer;
  private final GelSuggestionsContainer mSuggestionsContainer;
  private final GelSuggestionsController mSuggestionsController;
  private final int mSuggestionsPadding;
  private final int mVoiceSearchModeHeight;
  private final int mWebSuggestionsHeight;
  
  public SearchOverlayClipState(SearchOverlayLayout paramSearchOverlayLayout, GelSearchPlateContainer paramGelSearchPlateContainer, GelSuggestionsContainer paramGelSuggestionsContainer, View paramView, int paramInt1, int paramInt2, int paramInt3, GelSuggestionsController paramGelSuggestionsController, SearchOverlayClipAnimation paramSearchOverlayClipAnimation, GoogleNowPromoController paramGoogleNowPromoController, int paramInt4)
  {
    this.mContainer = paramSearchOverlayLayout;
    this.mSearchPlateContainer = paramGelSearchPlateContainer;
    this.mSuggestionsContainer = paramGelSuggestionsContainer;
    this.mSuggestionsController = paramGelSuggestionsController;
    this.mRecognizerView = paramView;
    this.mVoiceSearchModeHeight = paramInt1;
    this.mWebSuggestionsHeight = paramInt2;
    this.mSuggestionsPadding = paramInt3;
    this.mAnimation = paramSearchOverlayClipAnimation;
    this.mNowPromoController = paramGoogleNowPromoController;
    this.mNowPromoHeight = paramInt4;
    this.mRect = new Rect();
  }
  
  private void centerOnLogo(Rect paramRect)
  {
    int i = this.mSearchPlateContainer.getHeight() / 2;
    if (LayoutUtils.isLayoutRtl(this.mContainer)) {}
    for (int j = paramRect.right - i;; j = i + paramRect.left)
    {
      this.mAnimation.setupCenter(j, i + paramRect.top);
      return;
    }
  }
  
  private Rect getRectOfView(View paramView)
  {
    this.mRect.set(0, 0, paramView.getWidth(), paramView.getHeight());
    this.mContainer.offsetDescendantRectToMyCoords(paramView, this.mRect);
    return this.mRect;
  }
  
  private void setupCircleCenterWithContainerLastTouch()
  {
    int i = this.mContainer.getLastTouch().x;
    int j = this.mContainer.getLastTouch().y;
    Rect localRect = getRectOfView(this.mSearchPlateContainer);
    if (localRect.contains(i, j))
    {
      this.mAnimation.setupCenter(i, j);
      return;
    }
    centerOnLogo(localRect);
  }
  
  public void setMode(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean) {
      return;
    }
    int i = 0;
    View localView;
    int k;
    label33:
    int j;
    boolean bool;
    if (paramInt1 == 11) {
      if (SearchPlate.isTallSearchPlateMode(paramInt2))
      {
        localView = this.mRecognizerView;
        k = this.mVoiceSearchModeHeight;
        j = 1;
        bool = false;
        label39:
        if (j == 0) {
          break label194;
        }
        if (i == 0) {
          break label281;
        }
        this.mAnimation.addView(this.mSuggestionsContainer, this.mSuggestionsContainer);
      }
    }
    for (;;)
    {
      this.mAnimation.start(bool, this.mSearchPlateContainer.getWidth(), k, localView);
      return;
      centerOnLogo(getRectOfView(this.mSearchPlateContainer));
      i = 1;
      k = this.mSuggestionsContainer.getChildrenHeight() + this.mSuggestionsContainer.getTop();
      localView = null;
      break label33;
      localView = null;
      j = 0;
      i = 0;
      k = 0;
      bool = false;
      if (paramInt2 != 11) {
        break label39;
      }
      if (SearchPlate.isTallSearchPlateMode(paramInt1))
      {
        Rect localRect = getRectOfView(this.mRecognizerView);
        this.mAnimation.setupCenter(localRect.centerX(), localRect.centerY());
        k = this.mVoiceSearchModeHeight;
      }
      label279:
      for (;;)
      {
        j = 1;
        bool = true;
        localView = null;
        break label39;
        label194:
        break;
        setupCircleCenterWithContainerLastTouch();
        int m = this.mSuggestionsController.getZeroPrefixSuggestionsCount();
        if (m > 0)
        {
          i = 1;
          k = m * this.mWebSuggestionsHeight + this.mSuggestionsPadding + this.mSuggestionsContainer.getTop();
        }
        for (;;)
        {
          if (!this.mNowPromoController.shouldShowPromo()) {
            break label279;
          }
          k += this.mNowPromoHeight + this.mSuggestionsPadding;
          break;
          k = this.mSearchPlateContainer.getHeight();
          i = 0;
        }
      }
      label281:
      this.mAnimation.removeView(this.mSuggestionsContainer);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.SearchOverlayClipState
 * JD-Core Version:    0.7.0.1
 */