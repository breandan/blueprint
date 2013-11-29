package com.google.android.search.shared.ui;

import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.google.android.search.shared.api.VoiceCorrectionSpan;
import com.google.android.shared.util.SpannedCharSequences;
import com.google.android.shared.util.TextChangeWatcher;
import com.google.common.base.Preconditions;

public class VoiceCorrectionUiHelper
{
  private boolean mCorrectionUiEnabled;
  private final SimpleSearchText mSearchBox;
  private int mSelectionEnd;
  private int mSelectionStart;
  private final SelectionState mSelectionState = new SelectionState(null);
  private String mText;
  private TextChangeWatcher mTextChangeWatcher;
  private final Runnable mVoiceCorrectionEndRunnable = new Runnable()
  {
    public void run()
    {
      VoiceCorrectionUiHelper.this.mTextChangeWatcher.onCorrectionEnd();
    }
  };
  
  public VoiceCorrectionUiHelper(SimpleSearchText paramSimpleSearchText)
  {
    this.mSearchBox = paramSimpleSearchText;
  }
  
  public static void copyVoiceCorrectionSpans(Spanned paramSpanned, Spannable paramSpannable)
  {
    int i = paramSpanned.length();
    VoiceCorrectionSpan[] arrayOfVoiceCorrectionSpan = (VoiceCorrectionSpan[])paramSpannable.getSpans(0, i, VoiceCorrectionSpan.class);
    int j = arrayOfVoiceCorrectionSpan.length;
    for (int k = 0; k < j; k++) {
      paramSpannable.removeSpan(arrayOfVoiceCorrectionSpan[k]);
    }
    SpannedCharSequences.copySpansFrom(paramSpanned, 0, i, VoiceCorrectionSpan.class, paramSpannable, 0);
  }
  
  private int[] maybeSelectAWord(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    int i = this.mSearchBox.getOffsetForPosition(f1, f2);
    Editable localEditable = this.mSearchBox.getText();
    int j = localEditable.length();
    if ((i >= 0) && (i < j))
    {
      int k = i;
      int m = i;
      do
      {
        if (localEditable.charAt(k) == ' ') {
          break;
        }
        k--;
      } while (k >= 0);
      int n = k + 1;
      do
      {
        if (localEditable.charAt(m) == ' ') {
          break;
        }
        m++;
      } while (m < j);
      if (n < m) {
        return new int[] { n, m };
      }
    }
    return null;
  }
  
  private void setSelection(int paramInt1, int paramInt2)
  {
    this.mSelectionStart = paramInt1;
    this.mSelectionEnd = paramInt2;
    this.mText = this.mSearchBox.getText().toString();
    this.mSearchBox.setSelection(paramInt1, paramInt2);
  }
  
  public boolean applyCachedSelection()
  {
    if ((this.mText != null) && (TextUtils.equals(this.mSearchBox.getText(), this.mText)) && ((this.mSelectionStart != this.mSearchBox.getSelectionStart()) || (this.mSelectionEnd != this.mSearchBox.getSelectionEnd())))
    {
      this.mSearchBox.setSelection(this.mSelectionStart, this.mSelectionEnd);
      return true;
    }
    return false;
  }
  
  boolean clearSelection(MotionEvent paramMotionEvent)
  {
    int i = this.mSearchBox.getSelectionStart();
    int j = this.mSearchBox.getSelectionEnd();
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    int k = this.mSearchBox.getOffsetForPosition(f1, f2);
    if ((i <= k) && (k <= j))
    {
      forceClearSelection(paramMotionEvent);
      return true;
    }
    return false;
  }
  
  void forceClearSelection(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    int i = this.mSearchBox.getOffsetForPosition(f1, f2);
    setSelection(i, i);
  }
  
  public boolean isEnabled()
  {
    return this.mCorrectionUiEnabled;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    Preconditions.checkArgument(this.mCorrectionUiEnabled);
    boolean bool = somethingSelected();
    switch (paramMotionEvent.getAction())
    {
    }
    do
    {
      for (;;)
      {
        return true;
        this.mSelectionState.snapshot();
        if (bool)
        {
          if (clearSelection(paramMotionEvent))
          {
            this.mSelectionState.forceChanged();
            return false;
          }
          selectMoreWords(paramMotionEvent);
        }
        else
        {
          return selectAWord(paramMotionEvent);
          if (bool) {
            selectMoreWords(paramMotionEvent);
          }
        }
      }
      forceClearSelection(paramMotionEvent);
      bool = false;
      if (this.mSelectionState.hasChanged()) {
        this.mTextChangeWatcher.onTextSelected(this.mSearchBox.getQuery(), bool, this.mSearchBox.getSelectionStart(), this.mSearchBox.getSelectionEnd());
      }
    } while (bool);
    this.mCorrectionUiEnabled = false;
    this.mSearchBox.post(this.mVoiceCorrectionEndRunnable);
    return false;
  }
  
  boolean selectAWord(MotionEvent paramMotionEvent)
  {
    int[] arrayOfInt = maybeSelectAWord(paramMotionEvent);
    if (arrayOfInt != null)
    {
      setSelection(arrayOfInt[0], arrayOfInt[1]);
      return true;
    }
    return false;
  }
  
  void selectMoreWords(MotionEvent paramMotionEvent)
  {
    int i = this.mSearchBox.getSelectionStart();
    int j = this.mSearchBox.getSelectionEnd();
    boolean bool = somethingSelected();
    int[] arrayOfInt = maybeSelectAWord(paramMotionEvent);
    if (arrayOfInt == null) {
      return;
    }
    if (!bool)
    {
      setSelection(arrayOfInt[0], arrayOfInt[1]);
      return;
    }
    setSelection(Math.min(i, arrayOfInt[0]), Math.max(j, arrayOfInt[1]));
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.mCorrectionUiEnabled = paramBoolean;
    if (!paramBoolean) {
      this.mText = null;
    }
  }
  
  public void setTextChangeWatcher(TextChangeWatcher paramTextChangeWatcher)
  {
    this.mTextChangeWatcher = paramTextChangeWatcher;
  }
  
  boolean somethingSelected()
  {
    int i = this.mSearchBox.getSelectionStart();
    int j = this.mSearchBox.getSelectionEnd();
    return (i < j) && (i >= 0) && (j <= this.mSearchBox.getText().length());
  }
  
  private class SelectionState
  {
    private boolean mChanged;
    private int mEnd;
    private boolean mHasSelection;
    private int mStart;
    
    private SelectionState() {}
    
    public void forceChanged()
    {
      this.mChanged = true;
    }
    
    public boolean hasChanged()
    {
      if (this.mChanged) {}
      boolean bool;
      int i;
      int j;
      do
      {
        return true;
        bool = VoiceCorrectionUiHelper.this.somethingSelected();
        i = VoiceCorrectionUiHelper.this.mSearchBox.getSelectionStart();
        j = VoiceCorrectionUiHelper.this.mSearchBox.getSelectionEnd();
      } while ((bool != this.mHasSelection) || (i != this.mStart) || (j != this.mEnd));
      return false;
    }
    
    public void snapshot()
    {
      this.mHasSelection = VoiceCorrectionUiHelper.this.somethingSelected();
      this.mStart = VoiceCorrectionUiHelper.this.mSearchBox.getSelectionStart();
      this.mEnd = VoiceCorrectionUiHelper.this.mSearchBox.getSelectionEnd();
      this.mChanged = false;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.VoiceCorrectionUiHelper
 * JD-Core Version:    0.7.0.1
 */