package com.google.android.search.shared.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.SuggestionSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.BaseSavedState;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import com.google.android.search.shared.api.CorrectionSpan;
import com.google.android.search.shared.api.Query;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.android.shared.util.SpannedCharSequences;
import com.google.android.shared.util.TextChangeWatcher;
import com.google.common.base.Objects;

public class SimpleSearchText
  extends EditText
{
  boolean mCallbacksSuspended = false;
  private boolean mEditStartedInTouchMode;
  private CharSequence mHint;
  private boolean mIsVoiceQueryMode;
  CorrectionSpan mSelectedQueryCorrectionSpan;
  CharSequence mSpannedQuery;
  TextChangeWatcher mTextChangeWatcher;
  private final VoiceCorrectionUiHelper mVoiceCorrectionUiHelper = new VoiceCorrectionUiHelper(this);
  
  public SimpleSearchText(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  private CorrectionSpan getTouchedCorrectionSpan(MotionEvent paramMotionEvent)
  {
    int i = getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY());
    int j = getText().length();
    CorrectionSpan localCorrectionSpan = null;
    if (i < j)
    {
      CorrectionSpan[] arrayOfCorrectionSpan = (CorrectionSpan[])getText().getSpans(i, i, CorrectionSpan.class);
      int k = arrayOfCorrectionSpan.length;
      localCorrectionSpan = null;
      if (k == 1) {
        localCorrectionSpan = arrayOfCorrectionSpan[0];
      }
    }
    return localCorrectionSpan;
  }
  
  private void removeUnwantedSpans()
  {
    for (URLSpan localURLSpan : (URLSpan[])getText().getSpans(0, getText().length(), URLSpan.class)) {
      getText().removeSpan(localURLSpan);
    }
  }
  
  private void updateHintHack()
  {
    if (TextUtils.isEmpty(getText())) {}
    for (CharSequence localCharSequence = this.mHint;; localCharSequence = null)
    {
      if (!Objects.equal(getHint(), localCharSequence)) {
        setHint(localCharSequence);
      }
      return;
    }
  }
  
  public void addQueryTextWatcher(TextChangeWatcher paramTextChangeWatcher)
  {
    this.mTextChangeWatcher = paramTextChangeWatcher;
    this.mVoiceCorrectionUiHelper.setTextChangeWatcher(paramTextChangeWatcher);
  }
  
  public CharSequence getQuery()
  {
    Object localObject = getText();
    if ((localObject instanceof Spanned))
    {
      SpannableString localSpannableString = new SpannableString((CharSequence)localObject);
      SpannedCharSequences.removeSystemSpans(localSpannableString);
      localObject = localSpannableString;
    }
    return localObject;
  }
  
  public boolean hasFocusFromKeyboard()
  {
    return (!this.mEditStartedInTouchMode) && (!isInTouchMode()) && (hasFocus());
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    InputConnection localInputConnection = super.onCreateInputConnection(paramEditorInfo);
    paramEditorInfo.imeOptions = (0xBFFFFFFF & paramEditorInfo.imeOptions);
    paramEditorInfo.imeOptions = (0xFFFFFF00 & paramEditorInfo.imeOptions);
    paramEditorInfo.imeOptions = (0x3 | paramEditorInfo.imeOptions);
    return localInputConnection;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mHint = getHint();
    updateHintHack();
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (paramBoolean)
    {
      this.mEditStartedInTouchMode = isInTouchMode();
      if (this.mTextChangeWatcher != null) {
        this.mTextChangeWatcher.onTextEditStarted();
      }
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(SimpleSearchText.class.getCanonicalName());
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (localSavedState.mSpannedText != null) {
      this.mSpannedQuery = localSavedState.mSpannedText;
    }
    setSelection(localSavedState.mStartSpan);
    this.mTextChangeWatcher.onTextChanged(getQuery(), localSavedState.mStartSpan);
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    localSavedState.mSpannedText = this.mSpannedQuery;
    localSavedState.mStartSpan = getSelectionStart();
    return localSavedState;
  }
  
  protected void onSelectionChanged(int paramInt1, int paramInt2)
  {
    if ((this.mVoiceCorrectionUiHelper != null) && (this.mVoiceCorrectionUiHelper.isEnabled()) && (this.mVoiceCorrectionUiHelper.applyCachedSelection()))
    {
      super.onSelectionChanged(getSelectionStart(), getSelectionEnd());
      return;
    }
    super.onSelectionChanged(paramInt1, paramInt2);
  }
  
  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    if ((this.mTextChangeWatcher != null) && (!this.mCallbacksSuspended) && (!getText().toString().equals(paramCharSequence)))
    {
      if ((paramCharSequence instanceof Spanned))
      {
        SpannableString localSpannableString = new SpannableString(paramCharSequence);
        SpannedCharSequences.removeSystemSpans(localSpannableString);
        paramCharSequence = localSpannableString;
      }
      this.mTextChangeWatcher.onTextChanged(paramCharSequence, getSelectionStart());
    }
    updateHintHack();
  }
  
  public boolean onTextContextMenuItem(int paramInt)
  {
    boolean bool = super.onTextContextMenuItem(paramInt);
    if (paramInt == 16908322) {
      removeUnwantedSpans();
    }
    return bool;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mVoiceCorrectionUiHelper.isEnabled())
    {
      boolean bool1 = super.onTouchEvent(paramMotionEvent);
      boolean bool2 = this.mVoiceCorrectionUiHelper.onTouchEvent(paramMotionEvent);
      boolean bool3 = false;
      if (!bool2) {
        bool3 = true;
      }
      setCursorVisible(bool3);
      return bool1;
    }
    switch (paramMotionEvent.getAction())
    {
    }
    for (;;)
    {
      return super.onTouchEvent(paramMotionEvent);
      this.mSelectedQueryCorrectionSpan = getTouchedCorrectionSpan(paramMotionEvent);
      continue;
      CorrectionSpan localCorrectionSpan = getTouchedCorrectionSpan(paramMotionEvent);
      if ((localCorrectionSpan != null) && (this.mSelectedQueryCorrectionSpan == localCorrectionSpan))
      {
        String str = localCorrectionSpan.getCorrection();
        Editable localEditable = getText();
        localEditable.replace(localEditable.getSpanStart(localCorrectionSpan), localEditable.getSpanEnd(localCorrectionSpan), str, 0, str.length());
        localEditable.removeSpan(localCorrectionSpan);
      }
      this.mSelectedQueryCorrectionSpan = null;
      continue;
      this.mSelectedQueryCorrectionSpan = null;
    }
  }
  
  public void setHintText(CharSequence paramCharSequence)
  {
    this.mHint = paramCharSequence;
    updateHintHack();
  }
  
  public void setQuery(Query paramQuery)
  {
    Object localObject;
    if ((paramQuery.hasSpans(SuggestionSpan.class)) || (paramQuery.hasSpans(VoiceCorrectionSpan.class)))
    {
      localObject = paramQuery.getQueryChars();
      this.mSpannedQuery = ((CharSequence)localObject);
      if (TextUtils.equals(getText(), (CharSequence)localObject)) {
        break label84;
      }
      this.mCallbacksSuspended = true;
      setText((CharSequence)localObject);
      setSelection(length());
      this.mCallbacksSuspended = false;
    }
    for (;;)
    {
      this.mVoiceCorrectionUiHelper.setEnabled(paramQuery.needVoiceCorrection());
      return;
      localObject = paramQuery.getQueryString();
      break;
      label84:
      if (((localObject instanceof Spanned)) && (paramQuery.needVoiceCorrection())) {
        VoiceCorrectionUiHelper.copyVoiceCorrectionSpans((Spanned)localObject, getText());
      }
    }
  }
  
  public void setSuggestionsEnabled(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setInputType(0xFFF7FFFF & getInputType());
      return;
    }
    setInputType(0x80000 | getInputType());
  }
  
  public void setTextQueryCorrections(Spanned paramSpanned)
  {
    CorrectionSpan[] arrayOfCorrectionSpan1 = (CorrectionSpan[])paramSpanned.getSpans(0, paramSpanned.length(), CorrectionSpan.class);
    Editable localEditable = getText();
    CorrectionSpan[] arrayOfCorrectionSpan2 = (CorrectionSpan[])localEditable.getSpans(0, localEditable.length(), CorrectionSpan.class);
    int i = arrayOfCorrectionSpan2.length;
    for (int j = 0; j < i; j++) {
      localEditable.removeSpan(arrayOfCorrectionSpan2[j]);
    }
    for (int k = 0; k < arrayOfCorrectionSpan1.length; k++)
    {
      CorrectionSpan localCorrectionSpan = arrayOfCorrectionSpan1[k];
      int m = paramSpanned.getSpanStart(localCorrectionSpan);
      int n = paramSpanned.getSpanEnd(localCorrectionSpan);
      localCorrectionSpan.getCorrection();
      String str = paramSpanned.subSequence(m, n).toString();
      if ((localEditable.length() >= n) && (localEditable.subSequence(m, n).toString().equals(str))) {
        localEditable.setSpan(localCorrectionSpan, m, n, 33);
      }
    }
  }
  
  public boolean shouldShowClearButton()
  {
    boolean bool = true;
    if (!hasFocus()) {}
    while (TextUtils.isEmpty(getText())) {
      return false;
    }
    if (!this.mIsVoiceQueryMode) {
      return bool;
    }
    if (!this.mVoiceCorrectionUiHelper.isEnabled()) {}
    for (;;)
    {
      return bool;
      bool = false;
    }
  }
  
  public void showTextQueryMode()
  {
    if (this.mIsVoiceQueryMode)
    {
      this.mCallbacksSuspended = true;
      setSingleLine(true);
      Editable localEditable = getText();
      setSuggestionsEnabled(false);
      setText(this.mSpannedQuery);
      setTextQueryCorrections(localEditable);
      this.mCallbacksSuspended = false;
      this.mIsVoiceQueryMode = false;
    }
  }
  
  public void showVoiceQueryMode()
  {
    if (!this.mIsVoiceQueryMode)
    {
      this.mCallbacksSuspended = true;
      setSingleLine(false);
      setSuggestionsEnabled(true);
      setText(this.mSpannedQuery);
      this.mCallbacksSuspended = false;
      this.mIsVoiceQueryMode = true;
    }
  }
  
  public static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public SimpleSearchText.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SimpleSearchText.SavedState(paramAnonymousParcel, null);
      }
      
      public SimpleSearchText.SavedState[] newArray(int paramAnonymousInt)
      {
        return new SimpleSearchText.SavedState[paramAnonymousInt];
      }
    };
    CharSequence mSpannedText;
    int mStartSpan;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      this.mSpannedText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      this.mStartSpan = paramParcel.readInt();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      TextUtils.writeToParcel(this.mSpannedText, paramParcel, paramInt);
      paramParcel.writeInt(this.mStartSpan);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.SimpleSearchText
 * JD-Core Version:    0.7.0.1
 */