package com.google.android.search.shared.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.google.android.search.shared.api.Query;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.shared.util.SpeechLevelSource;
import com.google.android.shared.util.TextChangeWatcher;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Set;

public class SearchPlate
  extends RelativeLayout
  implements PathClippingView
{
  private Callback mCallback;
  private AudioProgressRenderer mCaptureAnimation;
  private ImageButton mClearButton;
  private Path mClipPath;
  private boolean mCommitMode;
  private int mCurrentMode;
  private TextView mDisplayText;
  private View mDummyFocusView;
  private int mEndMargin;
  private String mErrorMessage;
  private boolean mHotwordActive;
  private String mHotwordPrompt;
  private boolean mHotwordPromptDefined;
  private boolean mHotwordSupported;
  private final InputMethodManager mInputManager;
  private ImageView mLauncherSearchButtonLogo;
  private ClipOrFadeLayerDrawable mLauncherSearchButtonLogoDrawable;
  private View mLogo;
  private View mLogoProgressContainer;
  private ModeListener mModeListener;
  private Animator mParentChangeAnimator;
  private Animator mParentChangeInAnimator;
  private Animator mParentChangeOutAnimator;
  private View mProgressIndicator;
  private int mRecognitionState;
  private RelativeLayout.LayoutParams mRecognizerContainerLayoutText;
  private RelativeLayout.LayoutParams mRecognizerContainerLayoutVoice;
  private RelativeLayout.LayoutParams mRecognizerContainerLayoutVoiceResult;
  private int mRecognizerDefaultMargin;
  private RecognizerView mRecognizerView;
  private final Runnable mRetryPendingTransitionsRunnable = new Runnable()
  {
    public void run()
    {
      SearchPlate.access$002(SearchPlate.this, false);
      SearchPlate.TransitionsManager.access$200(SearchPlate.this.mTransitionsManager);
    }
  };
  private boolean mRetryPendingTransitionsRunnablePosted;
  private TextView mSayOkGoogle;
  private AnimatorSet mSayOkGoogleAnimator;
  private SimpleSearchText mSearchBox;
  private ViewGroup.LayoutParams mSearchPlateFollowOnParams;
  private ViewGroup.LayoutParams mSearchPlateShortParams;
  private ViewGroup.LayoutParams mSearchPlateTallParams;
  private SoundLevels mSoundLevels;
  private View mSoundSearchPromotedQuery;
  private View mSpeakNowSpeechLogo;
  private StreamingTextView mStreamingTextView;
  private TextContainer mTextContainer;
  private TransitionsManager mTransitionsManager;
  private Set<View> mViewsToDisplay;
  
  public SearchPlate(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SearchPlate(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SearchPlate(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mInputManager = ((InputMethodManager)paramContext.getSystemService("input_method"));
  }
  
  private void enableAndShowSoundLevels(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mSoundLevels.setEnabled(paramBoolean1);
    if (paramBoolean2)
    {
      showView(this.mSoundLevels);
      return;
    }
    hideView(this.mSoundLevels);
  }
  
  private Animator getParentAnimatorAndSetInterpolator(int paramInt)
  {
    LayoutTransition localLayoutTransition = getLayoutTransition();
    Animator localAnimator = localLayoutTransition.getAnimator(paramInt);
    localAnimator.setInterpolator(BakedBezierInterpolator.INSTANCE);
    localLayoutTransition.setAnimator(paramInt, localAnimator.clone());
    return localAnimator;
  }
  
  private void hideView(View paramView)
  {
    if ((paramView != null) && (paramView.getVisibility() != 8)) {
      paramView.setVisibility(8);
    }
  }
  
  private boolean hotwordPromptAvailable()
  {
    return (this.mHotwordPromptDefined) && (this.mHotwordSupported) && (this.mHotwordPrompt != null);
  }
  
  static int interpretSelection(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 0) || (paramInt1 > paramInt3))
    {
      if (paramInt1 == -2) {
        paramInt1 = paramInt3;
      }
    }
    else {
      return paramInt1;
    }
    return paramInt2;
  }
  
  public static boolean isTallSearchPlateMode(int paramInt)
  {
    return (paramInt == 10) || (paramInt == 3) || (paramInt == 4) || (paramInt == 5) || (paramInt == 6);
  }
  
  private void makeInvisible(View paramView)
  {
    if ((paramView != null) && (paramView.getVisibility() != 4)) {
      paramView.setVisibility(4);
    }
  }
  
  private void maybePostRetryPendingTransitions()
  {
    if (!this.mRetryPendingTransitionsRunnablePosted)
    {
      post(this.mRetryPendingTransitionsRunnable);
      this.mRetryPendingTransitionsRunnablePosted = true;
    }
  }
  
  private boolean needsSoundLevels(int paramInt)
  {
    return (paramInt == 3) || (paramInt == 4) || (paramInt == 9);
  }
  
  private void onlyShowViewsToDisplay()
  {
    int i = 0;
    if (i < getChildCount())
    {
      View localView = getChildAt(i);
      if ((localView == this.mDummyFocusView) || (localView == this.mRecognizerView) || (localView == this.mTextContainer) || (localView == this.mSoundLevels)) {}
      for (;;)
      {
        i++;
        break;
        boolean bool = this.mViewsToDisplay.contains(localView);
        if (localView == this.mLogoProgressContainer) {
          showLogoProgressContainer(bool);
        } else if (localView == this.mClearButton) {
          updateClearButton();
        } else if (bool) {
          showView(localView);
        } else {
          hideView(localView);
        }
      }
    }
  }
  
  private void setInterpolatorOnParentChangeAnimators(TimeInterpolator paramTimeInterpolator)
  {
    this.mParentChangeInAnimator.setInterpolator(paramTimeInterpolator);
    this.mParentChangeOutAnimator.setInterpolator(paramTimeInterpolator);
    this.mParentChangeAnimator.setInterpolator(paramTimeInterpolator);
  }
  
  private void setModeInternal(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    turnOffSoundLevels(paramInt1, paramBoolean);
    if ((paramInt1 == this.mCurrentMode) && ((paramInt2 & 0x2) == 0)) {
      return;
    }
    Object localObject = BakedBezierInterpolator.INSTANCE;
    if ((this.mCurrentMode == 11) && (isTallSearchPlateMode(paramInt1)))
    {
      localObject = HoldInterpolator.INSTANCE;
      setInterpolatorOnParentChangeAnimators((TimeInterpolator)localObject);
      this.mTextContainer.setMode(paramInt1);
      if (this.mModeListener != null) {
        this.mModeListener.onSearchPlateModeChanged(paramInt1, paramBoolean);
      }
      switch (paramInt1)
      {
      case 7: 
      default: 
        this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutText);
        this.mRecognizerView.setState(5);
        updateLayoutParams(this.mSearchPlateShortParams);
        label159:
        this.mViewsToDisplay.clear();
        switch (paramInt1)
        {
        }
        break;
      }
    }
    for (;;)
    {
      onlyShowViewsToDisplay();
      this.mCurrentMode = paramInt1;
      return;
      if ((!isTallSearchPlateMode(this.mCurrentMode)) || (paramInt1 != 11)) {
        break;
      }
      localObject = HoldInterpolator.REVERSE_INSTANCE;
      break;
      TextView localTextView = this.mDisplayText;
      if (hotwordPromptAvailable()) {}
      for (String str = this.mHotwordPrompt;; str = "")
      {
        localTextView.setText(str);
        this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutVoice);
        if (paramInt1 != 10) {
          this.mRecognizerView.setState(1);
        }
        updateLayoutParams(this.mSearchPlateTallParams);
        ((ViewGroup.MarginLayoutParams)this.mSoundLevels.getLayoutParams()).setMargins(0, 0, 0, 0);
        break;
      }
      this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutVoice);
      updateLayoutParams(this.mSearchPlateTallParams);
      break label159;
      this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutVoiceResult);
      this.mRecognizerView.setState(6);
      updateLayoutParams(this.mSearchPlateFollowOnParams);
      break label159;
      if (hotwordPromptAvailable())
      {
        this.mSearchBox.setHintText("");
        this.mStreamingTextView.setFinalRecognizedText(this.mHotwordPrompt);
      }
      this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutVoiceResult);
      this.mRecognizerView.setState(2);
      ((ViewGroup.MarginLayoutParams)this.mSoundLevels.getLayoutParams()).setMargins(0, -getContext().getResources().getDimensionPixelSize(2131689616), 0, 0);
      updateLayoutParams(this.mSearchPlateFollowOnParams);
      break label159;
      this.mRecognizerView.setLayoutParams(this.mRecognizerContainerLayoutText);
      this.mRecognizerView.setState(8);
      updateLayoutParams(this.mSearchPlateShortParams);
      break label159;
      showTextQueryMode();
      this.mViewsToDisplay.add(this.mLauncherSearchButtonLogo);
      this.mViewsToDisplay.add(this.mSayOkGoogle);
      continue;
      showTextQueryMode();
      continue;
      showTextQueryMode();
      this.mRecognitionState = 7;
      continue;
      this.mViewsToDisplay.add(this.mSoundSearchPromotedQuery);
      this.mViewsToDisplay.add(this.mSpeakNowSpeechLogo);
      continue;
      this.mViewsToDisplay.add(this.mSpeakNowSpeechLogo);
      this.mStreamingTextView.reset();
      if (this.mErrorMessage != null) {
        this.mDisplayText.setText(this.mErrorMessage);
      }
      this.mRecognizerView.setState(0);
      continue;
      this.mViewsToDisplay.add(this.mSpeakNowSpeechLogo);
      this.mViewsToDisplay.add(this.mCaptureAnimation);
      continue;
      showVoiceQueryMode();
      this.mViewsToDisplay.add(this.mLogoProgressContainer);
      continue;
      showTextQueryMode();
      this.mViewsToDisplay.add(this.mLogoProgressContainer);
      this.mRecognitionState = 7;
      continue;
      showVoiceQueryMode();
      this.mViewsToDisplay.add(this.mLogoProgressContainer);
      this.mViewsToDisplay.add(this.mSoundLevels);
    }
  }
  
  static void setSelection(EditText paramEditText, int paramInt1, int paramInt2)
  {
    int i = paramEditText.length();
    paramEditText.setSelection(interpretSelection(paramInt1, paramEditText.getSelectionStart(), i), interpretSelection(paramInt2, paramEditText.getSelectionEnd(), i));
  }
  
  private void setTransitionsEnabled(boolean paramBoolean)
  {
    this.mTransitionsManager.setTransitionsEnabled(paramBoolean);
  }
  
  private void showSayHotword(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      TextView localTextView = this.mSayOkGoogle;
      Context localContext = getContext();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.mHotwordPrompt;
      localTextView.setText(localContext.getString(2131363297, arrayOfObject));
      this.mSayOkGoogleAnimator.start();
      return;
    }
    this.mSayOkGoogle.setText("");
    this.mSayOkGoogle.setAlpha(0.0F);
  }
  
  private void showTextQueryMode()
  {
    this.mSearchBox.showTextQueryMode();
  }
  
  private boolean showView(View paramView)
  {
    boolean bool = false;
    if (paramView != null)
    {
      int i = paramView.getVisibility();
      bool = false;
      if (i != 0)
      {
        paramView.setVisibility(0);
        bool = true;
      }
    }
    return bool;
  }
  
  private void showVoiceQueryMode()
  {
    this.mSearchBox.showVoiceQueryMode();
  }
  
  private void showVoiceReady()
  {
    if (this.mSoundSearchPromotedQuery.getVisibility() == 0) {
      this.mDisplayText.setText(2131363310);
    }
    for (;;)
    {
      this.mTextContainer.resetRecognizedText();
      return;
      if ((this.mHotwordSupported) && (this.mHotwordPrompt != null))
      {
        TextView localTextView = this.mDisplayText;
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mHotwordPrompt;
        localTextView.setText(localContext.getString(2131363576, arrayOfObject));
      }
      else
      {
        this.mDisplayText.setText(2131363575);
      }
    }
  }
  
  private void turnOffSoundLevels(int paramInt, boolean paramBoolean)
  {
    if ((needsSoundLevels(this.mCurrentMode)) && (!needsSoundLevels(paramInt)) && (this.mRecognitionState != 8)) {
      showRecognitionState(8, paramBoolean);
    }
  }
  
  private void updateClearButton()
  {
    if (this.mClearButton.getVisibility() == 0) {}
    boolean bool2;
    for (boolean bool1 = true;; bool1 = false)
    {
      bool2 = this.mSearchBox.shouldShowClearButton();
      if (bool1 != bool2) {
        break;
      }
      return;
    }
    if (bool2)
    {
      RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.mClearButton.getLayoutParams();
      int i = this.mEndMargin - this.mRecognizerDefaultMargin;
      if (this.mCurrentMode != 8)
      {
        hideView(this.mRecognizerView);
        i = this.mEndMargin;
      }
      LayoutUtils.setMarginsRelative(localLayoutParams, this.mRecognizerDefaultMargin, this.mRecognizerDefaultMargin, i, this.mRecognizerDefaultMargin);
      this.mClearButton.setLayoutParams(localLayoutParams);
      showView(this.mClearButton);
    }
    for (;;)
    {
      this.mSearchBox.postInvalidate();
      return;
      hideView(this.mClearButton);
      showView(this.mRecognizerView);
    }
  }
  
  private void updateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    ViewGroup.LayoutParams localLayoutParams = (ViewGroup.LayoutParams)Preconditions.checkNotNull(getLayoutParams());
    localLayoutParams.height = paramLayoutParams.height;
    localLayoutParams.width = paramLayoutParams.width;
    setLayoutParams(localLayoutParams);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    if ((this.mClipPath != null) && (paramView != this.mRecognizerView))
    {
      paramCanvas.save();
      if ((paramView == this.mSayOkGoogle) || (paramView == this.mLauncherSearchButtonLogo)) {}
      for (int i = 1;; i = 0)
      {
        if (i != 0) {
          this.mClipPath.toggleInverseFillType();
        }
        paramCanvas.clipPath(this.mClipPath);
        if (i != 0) {
          this.mClipPath.toggleInverseFillType();
        }
        boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
        paramCanvas.restore();
        return bool;
      }
    }
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  public void focusQueryAndShowKeyboard(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mSearchBox.requestFocus();
      this.mDummyFocusView.setFocusable(false);
      this.mDummyFocusView.setFocusableInTouchMode(false);
      updateClearButton();
      this.mInputManager.showSoftInput(this.mSearchBox, 0);
      return;
    }
    this.mTransitionsManager.maybeFocusQueryAndShowKeyboard();
  }
  
  public void focusableViewAvailable(View paramView)
  {
    if (paramView != this.mSearchBox) {
      super.focusableViewAvailable(paramView);
    }
  }
  
  public boolean isTransitionRunning()
  {
    return this.mTransitionsManager.isRunning();
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTextContainer = ((TextContainer)Preconditions.checkNotNull(findViewById(2131296385)));
    this.mSearchBox = ((SimpleSearchText)Preconditions.checkNotNull(findViewById(2131296723)));
    this.mClearButton = ((ImageButton)Preconditions.checkNotNull(findViewById(2131296966)));
    this.mRecognizerView = ((RecognizerView)Preconditions.checkNotNull(findViewById(2131296528)));
    this.mDisplayText = ((TextView)Preconditions.checkNotNull(findViewById(2131296525)));
    this.mStreamingTextView = ((StreamingTextView)Preconditions.checkNotNull(findViewById(2131296526)));
    this.mSoundLevels = ((SoundLevels)Preconditions.checkNotNull(findViewById(2131296715)));
    this.mSoundSearchPromotedQuery = ((View)Preconditions.checkNotNull(findViewById(2131296973)));
    this.mCaptureAnimation = ((AudioProgressRenderer)Preconditions.checkNotNull(findViewById(2131296969)));
    this.mSpeakNowSpeechLogo = ((View)Preconditions.checkNotNull(findViewById(2131296972)));
    this.mSayOkGoogle = ((TextView)Preconditions.checkNotNull(findViewById(2131296971)));
    int i = getResources().getDimensionPixelSize(2131689626);
    int j;
    int k;
    label538:
    int m;
    label568:
    int n;
    label637:
    RelativeLayout.LayoutParams localLayoutParams3;
    if (LayoutUtils.isDefaultLocaleRtl())
    {
      j = -1;
      float f = j * i;
      ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(this.mSayOkGoogle, "translationX", new float[] { f, 0.0F });
      localObjectAnimator1.setInterpolator(BakedBezierInterpolator.INSTANCE);
      ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this.mSayOkGoogle, "alpha", new float[] { 0.0F, 1.0F });
      this.mSayOkGoogleAnimator = new AnimatorSet();
      this.mSayOkGoogleAnimator.playTogether(new Animator[] { localObjectAnimator1, localObjectAnimator2 });
      this.mSayOkGoogleAnimator.setDuration(700L);
      this.mTransitionsManager = new TransitionsManager(this);
      this.mTextContainer.setAnimatorListener(this.mTransitionsManager);
      if (!getContext().getPackageManager().hasSystemFeature("android.hardware.microphone"))
      {
        this.mRecognizerView.setEnabled(false);
        this.mRecognizerView.setVisibility(8);
      }
      this.mProgressIndicator = ((View)Preconditions.checkNotNull(findViewById(2131296974)));
      this.mLogo = ((View)Preconditions.checkNotNull(findViewById(2131296975)));
      this.mLauncherSearchButtonLogo = ((ImageView)findViewById(2131296970));
      this.mLogoProgressContainer = ((View)Preconditions.checkNotNull(findViewById(2131296524)));
      this.mViewsToDisplay = Sets.newHashSet();
      this.mEndMargin = getContext().getResources().getDimensionPixelSize(2131689617);
      this.mLauncherSearchButtonLogoDrawable = new ClipOrFadeLayerDrawable(getResources().getDrawable(2130837764), getResources().getDrawable(2130837760));
      this.mLauncherSearchButtonLogo.setImageDrawable(this.mLauncherSearchButtonLogoDrawable);
      this.mRecognizerView.setScaleType(ImageView.ScaleType.CENTER);
      this.mRecognizerDefaultMargin = getContext().getResources().getDimensionPixelSize(2131689627);
      if (Build.VERSION.SDK_INT < 17) {
        break label1035;
      }
      k = 1;
      this.mRecognizerContainerLayoutText = new RelativeLayout.LayoutParams(-2, -2);
      RelativeLayout.LayoutParams localLayoutParams1 = this.mRecognizerContainerLayoutText;
      if (k == 0) {
        break label1041;
      }
      m = 21;
      localLayoutParams1.addRule(m);
      this.mRecognizerContainerLayoutText.addRule(15);
      LayoutUtils.setMarginsRelative(this.mRecognizerContainerLayoutText, this.mRecognizerDefaultMargin, this.mRecognizerDefaultMargin, this.mEndMargin, this.mRecognizerDefaultMargin);
      this.mRecognizerContainerLayoutVoiceResult = new RelativeLayout.LayoutParams(-2, -2);
      RelativeLayout.LayoutParams localLayoutParams2 = this.mRecognizerContainerLayoutVoiceResult;
      if (k == 0) {
        break label1048;
      }
      n = 21;
      localLayoutParams2.addRule(n);
      this.mRecognizerContainerLayoutVoiceResult.addRule(15);
      LayoutUtils.setMarginsRelative(this.mRecognizerContainerLayoutVoiceResult, 0, getContext().getResources().getDimensionPixelSize(2131689616), this.mEndMargin, 0);
      this.mRecognizerContainerLayoutVoice = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams3 = this.mRecognizerContainerLayoutVoice;
      if (k == 0) {
        break label1055;
      }
    }
    label1035:
    label1041:
    label1048:
    label1055:
    for (int i1 = 21;; i1 = 11)
    {
      localLayoutParams3.addRule(i1);
      LayoutUtils.setMarginsRelative(this.mRecognizerContainerLayoutVoice, 0, getContext().getResources().getDimensionPixelSize(2131689611), this.mEndMargin, 0);
      this.mDummyFocusView = ((View)Preconditions.checkNotNull(findViewById(2131296967)));
      showView(this.mDummyFocusView);
      this.mTransitionsManager.setTransitionsEnabled(true);
      getLayoutTransition().setStartDelay(1, 0L);
      getLayoutTransition().setStartDelay(2, 0L);
      getLayoutTransition().setAnimator(2, new SearchPlateAnimator(true));
      getLayoutTransition().setAnimator(3, new SearchPlateAnimator(false));
      this.mParentChangeInAnimator = getParentAnimatorAndSetInterpolator(0);
      this.mParentChangeOutAnimator = getParentAnimatorAndSetInterpolator(1);
      this.mParentChangeAnimator = getParentAnimatorAndSetInterpolator(4);
      this.mSearchPlateShortParams = new ViewGroup.LayoutParams(-1, -2);
      this.mSearchPlateTallParams = new ViewGroup.LayoutParams(-1, getContext().getResources().getDimensionPixelSize(2131689602));
      this.mSearchPlateFollowOnParams = new ViewGroup.LayoutParams(-1, getContext().getResources().getDimensionPixelSize(2131689620));
      setMode(2, 0, true);
      this.mSearchBox.addQueryTextWatcher(new TextChangeWatcher()
      {
        public void onCorrectionEnd()
        {
          SearchPlate.this.mCallback.onCorrectionEnd();
        }
        
        public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt)
        {
          SearchPlate.this.mCallback.onQueryTextChanged(paramAnonymousCharSequence, paramAnonymousInt, SearchPlate.this.mSearchBox.hasFocusFromKeyboard());
        }
        
        public void onTextEditStarted()
        {
          if (SearchPlate.this.mSearchBox.hasFocusFromKeyboard()) {
            SearchPlate.this.mCallback.onSearchBoxKeyboardFocused();
          }
        }
        
        public void onTextSelected(CharSequence paramAnonymousCharSequence, boolean paramAnonymousBoolean, int paramAnonymousInt1, int paramAnonymousInt2)
        {
          SearchPlate.this.mCallback.onTextSelected(paramAnonymousCharSequence, paramAnonymousBoolean, paramAnonymousInt1, paramAnonymousInt2);
        }
      });
      this.mSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
          return SearchPlate.this.mCallback.onSearchBoxEditorAction(paramAnonymousInt);
        }
      });
      this.mSearchBox.setOnTouchListener(new View.OnTouchListener()
      {
        public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
        {
          if ((paramAnonymousMotionEvent.getActionMasked() == 1) && (paramAnonymousView.isPressed())) {
            SearchPlate.this.mCallback.onSearchBoxTouched();
          }
          return false;
        }
      });
      this.mSearchBox.setOnKeyListener(new View.OnKeyListener()
      {
        public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
          if ((paramAnonymousKeyEvent.getAction() == 0) && (Util.isEnterKey(paramAnonymousKeyEvent)))
          {
            SearchPlate.this.mCallback.onSearchButtonClick();
            return true;
          }
          return false;
        }
      });
      this.mClearButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          SearchPlate.this.mCallback.onClearButtonClick();
        }
      });
      this.mSoundSearchPromotedQuery.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          SearchPlate.this.mCallback.onPromotedSoundSearchClick();
        }
      });
      this.mLauncherSearchButtonLogo.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if (SearchPlate.this.mCallback != null) {
            SearchPlate.this.mCallback.onLauncherSearchButtonClick();
          }
        }
      });
      return;
      j = 1;
      break;
      k = 0;
      break label538;
      m = 11;
      break label568;
      n = 11;
      break label637;
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(SearchPlate.class.getCanonicalName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!this.mTransitionsManager.areLayoutTransitionsEnabled()) {
      this.mTransitionsManager.setTransitionsEnabled(true);
    }
    maybePostRetryPendingTransitions();
  }
  
  public boolean requestFocus(int paramInt, Rect paramRect)
  {
    if (this.mDummyFocusView.isFocusable()) {
      return this.mDummyFocusView.requestFocus();
    }
    return false;
  }
  
  public void setCallback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
    this.mRecognizerView.setCallback(paramCallback);
  }
  
  public void setClipPath(Path paramPath)
  {
    if ((this.mClipPath != null) && (paramPath == null)) {
      maybePostRetryPendingTransitions();
    }
    this.mClipPath = paramPath;
    invalidate();
  }
  
  public void setExternalFlags(int paramInt, String paramString, boolean paramBoolean)
  {
    boolean bool1 = true;
    RecognizerView localRecognizerView1 = this.mRecognizerView;
    boolean bool2;
    boolean bool3;
    label41:
    boolean bool4;
    label57:
    boolean bool5;
    label74:
    String str;
    label98:
    boolean bool6;
    label145:
    boolean bool8;
    label170:
    RecognizerView localRecognizerView2;
    boolean bool7;
    if ((paramInt & 0x20) != 0)
    {
      bool2 = bool1;
      localRecognizerView1.setTtsState(bool2);
      if (!paramBoolean) {
        break label312;
      }
      if ((paramInt & 0x1) == 0) {
        break label216;
      }
      bool3 = bool1;
      this.mHotwordActive = bool3;
      if ((paramInt & 0x2) == 0) {
        break label222;
      }
      bool4 = bool1;
      this.mHotwordSupported = bool4;
      if ((paramInt & 0x10) == 0) {
        break label228;
      }
      bool5 = bool1;
      this.mHotwordPromptDefined = bool5;
      if (paramInt != 8) {
        break label234;
      }
      str = getContext().getString(2131363213);
      if (this.mCurrentMode != 9) {
        this.mSearchBox.setHintText(str);
      }
      if ((this.mHotwordActive) && (this.mCurrentMode == 3)) {
        showVoiceReady();
      }
      if ((paramInt & 0x4) == 0) {
        break label294;
      }
      bool6 = bool1;
      if (this.mCurrentMode == 11)
      {
        if ((!this.mHotwordActive) || (!bool6)) {
          break label300;
        }
        bool8 = bool1;
        showSayHotword(bool8);
      }
      localRecognizerView2 = this.mRecognizerView;
      bool7 = this.mHotwordSupported;
      if ((!this.mHotwordActive) || (!bool6)) {
        break label306;
      }
    }
    for (;;)
    {
      localRecognizerView2.showHotwordIndicator(bool7, bool1);
      return;
      bool2 = false;
      break;
      label216:
      bool3 = false;
      break label41;
      label222:
      bool4 = false;
      break label57;
      label228:
      bool5 = false;
      break label74;
      label234:
      if (this.mHotwordActive)
      {
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[bool1];
        arrayOfObject[0] = paramString;
        str = localContext.getString(2131363296, arrayOfObject);
        this.mHotwordPrompt = paramString;
        break label98;
      }
      str = getContext().getString(2131363298);
      break label98;
      label294:
      bool6 = false;
      break label145;
      label300:
      bool8 = false;
      break label170;
      label306:
      bool1 = false;
    }
    label312:
    this.mTransitionsManager.maybeSetExternalFlags(paramInt, paramString);
  }
  
  public void setFinalRecognizedText(CharSequence paramCharSequence)
  {
    this.mTextContainer.setFinalRecognizedText(paramCharSequence);
  }
  
  public void setMode(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setTransitionsEnabled(false);
      turnOffSoundLevels(paramInt1, true);
      setModeInternal(paramInt1, paramInt2, true);
      requestLayout();
    }
    while (paramInt1 == this.mTransitionsManager.lastModeSet()) {
      return;
    }
    turnOffSoundLevels(paramInt1, false);
    this.mTransitionsManager.maybeSetSearchPlateMode(paramInt1, paramInt2);
  }
  
  public void setModeListener(ModeListener paramModeListener)
  {
    this.mModeListener = paramModeListener;
  }
  
  public void setProximityToNow(float paramFloat)
  {
    int i = (int)(255.0F - 77.0F * paramFloat);
    this.mSayOkGoogle.setTextColor(Color.rgb(i, i, i));
    int j = (int)(140.0F - paramFloat * 140.0F);
    this.mSayOkGoogle.setShadowLayer(this.mSayOkGoogle.getShadowRadius(), this.mSayOkGoogle.getShadowDx(), this.mSayOkGoogle.getShadowDy(), Color.argb(j, 0, 0, 0));
    this.mLauncherSearchButtonLogoDrawable.setAlphaFade(paramFloat);
    this.mRecognizerView.setProximityToNow(paramFloat);
  }
  
  public void setQuery(Query paramQuery, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((paramQuery.getQueryString().isEmpty()) && (this.mCurrentMode == 9)) {
        return;
      }
      this.mTextContainer.setQuery(paramQuery);
      setSelection(this.mSearchBox, paramQuery.getSelectionStart(), paramQuery.getSelectionEnd());
      updateClearButton();
      return;
    }
    this.mTransitionsManager.maybeSetQuery(paramQuery);
  }
  
  public void setSpeechLevelSource(SpeechLevelSource paramSpeechLevelSource)
  {
    this.mSoundLevels.setLevelSource(paramSpeechLevelSource);
    this.mCaptureAnimation.setSpeechLevelSource(paramSpeechLevelSource);
  }
  
  public void setTextQueryCorrections(Spanned paramSpanned, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mSearchBox.setTextQueryCorrections(paramSpanned);
      return;
    }
    this.mTransitionsManager.maybeSetTextQueryCorrections(paramSpanned);
  }
  
  public void showErrorMessage(String paramString)
  {
    this.mErrorMessage = paramString;
    setMode(10, 0, false);
  }
  
  public void showLogoProgressContainer(boolean paramBoolean)
  {
    if (paramBoolean == this.mCommitMode) {
      return;
    }
    if (paramBoolean)
    {
      this.mCommitMode = true;
      showView(this.mLogoProgressContainer);
      return;
    }
    this.mCommitMode = false;
    hideView(this.mLogoProgressContainer);
  }
  
  public void showProgress(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      makeInvisible(this.mLogo);
      showView(this.mProgressIndicator);
      return;
    }
    makeInvisible(this.mProgressIndicator);
    showView(this.mLogo);
  }
  
  public void showRecognitionState(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((paramInt == this.mRecognitionState) || ((this.mCurrentMode != 3) && (this.mCurrentMode != 4) && (this.mCurrentMode != 5) && (this.mCurrentMode != 6) && (this.mCurrentMode != 9))) {}
      do
      {
        do
        {
          return;
          this.mRecognitionState = paramInt;
          switch (this.mRecognitionState)
          {
          default: 
            return;
          case 1: 
            enableAndShowSoundLevels(false, true);
            this.mStreamingTextView.reset();
            if (hotwordPromptAvailable()) {
              this.mDisplayText.setText(this.mHotwordPrompt);
            }
          case 5: 
          case 7: 
          case 3: 
            for (;;)
            {
              this.mStreamingTextView.reset();
              this.mRecognizerView.setState(1);
              return;
              this.mRecognizerView.setState(3);
              enableAndShowSoundLevels(true, true);
              return;
              enableAndShowSoundLevels(false, true);
              showVoiceReady();
              this.mRecognizerView.pauseSpeech();
              return;
              enableAndShowSoundLevels(false, true);
              this.mStreamingTextView.reset();
              if (hotwordPromptAvailable()) {
                this.mDisplayText.setText(this.mHotwordPrompt);
              }
              for (;;)
              {
                this.mRecognizerView.setState(1);
                return;
                this.mDisplayText.setText(2131363577);
              }
              this.mDisplayText.setText(2131363577);
            }
          case 6: 
            enableAndShowSoundLevels(false, true);
            this.mDisplayText.setText(2131363598);
            this.mRecognizerView.setState(4);
            return;
          case 2: 
            if ((this.mCurrentMode == 5) || (this.mCurrentMode == 6))
            {
              this.mCaptureAnimation.stopAnimation();
              return;
            }
            break;
          }
        } while ((this.mCurrentMode != 3) && (this.mCurrentMode != 4) && (this.mCurrentMode != 9));
        enableAndShowSoundLevels(false, true);
        showVoiceReady();
        this.mRecognizerView.setState(0);
        return;
        if (this.mCurrentMode == 5)
        {
          enableAndShowSoundLevels(false, false);
          this.mDisplayText.setText(2131363311);
          this.mRecognizerView.setState(7);
          this.mCaptureAnimation.startAnimation();
          return;
        }
        if (this.mCurrentMode == 6)
        {
          enableAndShowSoundLevels(false, false);
          this.mDisplayText.setText(2131363312);
          this.mRecognizerView.setState(7);
          this.mCaptureAnimation.startAnimation();
          return;
        }
      } while ((this.mCurrentMode != 3) && (this.mCurrentMode != 4) && (this.mCurrentMode != 9));
      enableAndShowSoundLevels(true, true);
      this.mDisplayText.setText(2131363461);
      this.mStreamingTextView.reset();
      this.mRecognizerView.setState(2);
      return;
      enableAndShowSoundLevels(false, false);
      this.mDisplayText.setText("");
      return;
    }
    this.mTransitionsManager.maybeShowRecognitionState(paramInt);
  }
  
  public void unfocusQueryAndHideKeyboard(boolean paramBoolean)
  {
    if (!this.mDummyFocusView.hasFocus())
    {
      this.mDummyFocusView.setFocusable(true);
      this.mDummyFocusView.setFocusableInTouchMode(true);
      this.mDummyFocusView.requestFocus();
    }
    if (paramBoolean)
    {
      updateClearButton();
      this.mInputManager.hideSoftInputFromWindow(this.mSearchBox.getWindowToken(), 2);
      return;
    }
    this.mTransitionsManager.maybeUnfocusQueryAndHideKeyboard();
  }
  
  public void updateRecognizedText(String paramString1, String paramString2)
  {
    this.mTextContainer.updateRecognizedText(paramString1, paramString2);
  }
  
  public static abstract interface Callback
    extends RecognizerView.Callback
  {
    public abstract void onClearButtonClick();
    
    public abstract void onCorrectionEnd();
    
    public abstract void onLauncherSearchButtonClick();
    
    public abstract void onPromotedSoundSearchClick();
    
    public abstract void onQueryTextChanged(CharSequence paramCharSequence, int paramInt, boolean paramBoolean);
    
    public abstract boolean onSearchBoxEditorAction(int paramInt);
    
    public abstract void onSearchBoxKeyboardFocused();
    
    public abstract void onSearchBoxTouched();
    
    public abstract void onSearchButtonClick();
    
    public abstract void onTextSelected(CharSequence paramCharSequence, boolean paramBoolean, int paramInt1, int paramInt2);
  }
  
  public static abstract interface ModeListener
  {
    public abstract void onSearchPlateModeChanged(int paramInt, boolean paramBoolean);
    
    public abstract void onSearchPlateModeTransitionsFinished();
  }
  
  private class TransitionsManager
    extends AnimatorListenerAdapter
    implements LayoutTransition.TransitionListener
  {
    private boolean mFlushingOnTransitionEnd;
    private boolean mFocusQueryAndShowKeyboardPending;
    private boolean mHintTextPending;
    private final LayoutTransition mLayoutTransition;
    private boolean mLayoutTransitionsEnabled;
    private Spanned mPendingCorrections = null;
    private int mPendingHintFlags;
    private String mPendingHintHotwordPrompt;
    private int mPendingMode;
    private int mPendingModeFlags;
    private Query mPendingQuery = null;
    private int mPendingRecognitionState;
    private int mPendingRecognitionStateForNextMode;
    private boolean mRecognitionUpdateForNextModePending;
    private boolean mRecognitionUpdatePending;
    private int mRunningTransitions = 0;
    private boolean mSearchPlateUpdatePending;
    private boolean mUnfocusQueryAndHideKeyboardPending;
    
    public TransitionsManager(ViewGroup paramViewGroup)
    {
      paramViewGroup.getLayoutTransition().addTransitionListener(this);
      this.mLayoutTransition = paramViewGroup.getLayoutTransition();
    }
    
    private int lastModeSet()
    {
      if (this.mSearchPlateUpdatePending) {
        return this.mPendingMode;
      }
      return SearchPlate.this.mCurrentMode;
    }
    
    private void retryPendingTransitions()
    {
      boolean bool = true;
      if ((!isRunning()) && (!this.mFlushingOnTransitionEnd))
      {
        this.mFlushingOnTransitionEnd = bool;
        if ((this.mRecognitionUpdatePending) || (this.mSearchPlateUpdatePending)) {
          break label172;
        }
      }
      for (;;)
      {
        if (this.mRecognitionUpdatePending) {
          maybeShowRecognitionState(this.mPendingRecognitionState);
        }
        if (this.mSearchPlateUpdatePending) {
          maybeSetSearchPlateMode(this.mPendingMode, this.mPendingModeFlags);
        }
        if (this.mPendingQuery != null) {
          maybeSetQuery(this.mPendingQuery);
        }
        if (this.mHintTextPending) {
          maybeSetExternalFlags(this.mPendingHintFlags, this.mPendingHintHotwordPrompt);
        }
        if (this.mUnfocusQueryAndHideKeyboardPending) {
          maybeUnfocusQueryAndHideKeyboard();
        }
        if (this.mFocusQueryAndShowKeyboardPending) {
          maybeFocusQueryAndShowKeyboard();
        }
        if (this.mPendingCorrections != null) {
          maybeSetTextQueryCorrections(this.mPendingCorrections);
        }
        this.mFlushingOnTransitionEnd = false;
        if ((bool) && (SearchPlate.this.mModeListener != null)) {
          SearchPlate.this.mModeListener.onSearchPlateModeTransitionsFinished();
        }
        return;
        label172:
        bool = false;
      }
    }
    
    public boolean areLayoutTransitionsEnabled()
    {
      return this.mLayoutTransitionsEnabled;
    }
    
    public void endTransition(LayoutTransition paramLayoutTransition, ViewGroup paramViewGroup, View paramView, int paramInt)
    {
      this.mRunningTransitions = (-1 + this.mRunningTransitions);
      retryPendingTransitions();
    }
    
    public boolean isRunning()
    {
      return (SearchPlate.this.getLayoutTransition().isRunning()) || (SearchPlate.this.mTextContainer.isAnimatingQueryRewrite()) || (SearchPlate.this.isLayoutRequested()) || (SearchPlate.this.mClipPath != null);
    }
    
    public void maybeFocusQueryAndShowKeyboard()
    {
      this.mUnfocusQueryAndHideKeyboardPending = false;
      if (isRunning())
      {
        this.mFocusQueryAndShowKeyboardPending = true;
        return;
      }
      SearchPlate.this.focusQueryAndShowKeyboard(true);
      this.mFocusQueryAndShowKeyboardPending = false;
    }
    
    public void maybeSetExternalFlags(int paramInt, String paramString)
    {
      if (isRunning())
      {
        this.mHintTextPending = true;
        this.mPendingHintFlags = paramInt;
        this.mPendingHintHotwordPrompt = paramString;
        return;
      }
      SearchPlate.this.setExternalFlags(paramInt, paramString, true);
      this.mHintTextPending = false;
    }
    
    public void maybeSetQuery(Query paramQuery)
    {
      if (isRunning())
      {
        this.mPendingQuery = paramQuery;
        return;
      }
      SearchPlate.this.setQuery(paramQuery, true);
      this.mPendingQuery = null;
    }
    
    public void maybeSetSearchPlateMode(int paramInt1, int paramInt2)
    {
      if (isRunning())
      {
        if (this.mSearchPlateUpdatePending) {
          this.mRecognitionUpdateForNextModePending = false;
        }
        this.mSearchPlateUpdatePending = true;
        this.mPendingMode = paramInt1;
        this.mPendingModeFlags = paramInt2;
      }
      do
      {
        return;
        SearchPlate.this.setModeInternal(paramInt1, paramInt2, false);
        this.mSearchPlateUpdatePending = false;
      } while (!this.mRecognitionUpdateForNextModePending);
      this.mRecognitionUpdatePending = true;
      this.mPendingRecognitionState = this.mPendingRecognitionStateForNextMode;
      this.mRecognitionUpdateForNextModePending = false;
    }
    
    public void maybeSetTextQueryCorrections(Spanned paramSpanned)
    {
      if (isRunning())
      {
        this.mPendingCorrections = paramSpanned;
        return;
      }
      SearchPlate.this.setTextQueryCorrections(paramSpanned, true);
      this.mPendingCorrections = null;
    }
    
    public void maybeShowRecognitionState(int paramInt)
    {
      if (isRunning())
      {
        if (this.mSearchPlateUpdatePending)
        {
          this.mRecognitionUpdateForNextModePending = true;
          this.mPendingRecognitionStateForNextMode = paramInt;
          return;
        }
        this.mRecognitionUpdatePending = true;
        this.mPendingRecognitionState = paramInt;
        return;
      }
      SearchPlate.this.showRecognitionState(paramInt, true);
      this.mRecognitionUpdatePending = false;
    }
    
    public void maybeUnfocusQueryAndHideKeyboard()
    {
      this.mFocusQueryAndShowKeyboardPending = false;
      if (isRunning())
      {
        this.mUnfocusQueryAndHideKeyboardPending = true;
        return;
      }
      SearchPlate.this.unfocusQueryAndHideKeyboard(true);
      this.mUnfocusQueryAndHideKeyboardPending = false;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      retryPendingTransitions();
    }
    
    public void setTransitionsEnabled(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        this.mLayoutTransition.enableTransitionType(4);
        this.mLayoutTransition.enableTransitionType(2);
        this.mLayoutTransition.enableTransitionType(3);
        this.mLayoutTransition.enableTransitionType(0);
        this.mLayoutTransition.enableTransitionType(1);
      }
      for (;;)
      {
        this.mLayoutTransitionsEnabled = paramBoolean;
        return;
        this.mLayoutTransition.disableTransitionType(4);
        this.mLayoutTransition.disableTransitionType(2);
        this.mLayoutTransition.disableTransitionType(3);
        this.mLayoutTransition.disableTransitionType(0);
        this.mLayoutTransition.disableTransitionType(1);
      }
    }
    
    public void startTransition(LayoutTransition paramLayoutTransition, ViewGroup paramViewGroup, View paramView, int paramInt)
    {
      this.mRunningTransitions = (1 + this.mRunningTransitions);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SearchPlate
 * JD-Core Version:    0.7.0.1
 */