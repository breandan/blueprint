package com.google.android.sidekick.shared.training;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion.Option;
import com.google.android.sidekick.shared.util.Tag;
import com.google.geo.sidekick.Sidekick.Entry;
import java.util.Iterator;
import java.util.Locale;

public class MultipleClientActionQuestionView
  extends LinearLayout
  implements QuestionView
{
  private static final String TAG = Tag.getTag(MultipleClientActionQuestionView.class);
  private LayoutInflater mLayoutInflater;
  private QuestionViewListener mListener;
  
  public MultipleClientActionQuestionView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public MultipleClientActionQuestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init()
  {
    this.mLayoutInflater = LayoutInflater.from(getContext());
    this.mLayoutInflater.inflate(2130968875, this);
  }
  
  public void setEntry(Sidekick.Entry paramEntry) {}
  
  public void setListener(QuestionViewListener paramQuestionViewListener)
  {
    this.mListener = paramQuestionViewListener;
  }
  
  public void setTrainingQuestion(TrainingQuestion paramTrainingQuestion)
  {
    TrainingQuestionViewHelper.setQuestionAndJustification(this, paramTrainingQuestion, null);
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131297132);
    localViewGroup.removeAllViews();
    int i = getResources().getInteger(2131427467);
    TableRow localTableRow = null;
    int j = 0;
    Iterator localIterator = paramTrainingQuestion.getClientActionOptions().iterator();
    while (localIterator.hasNext())
    {
      TrainingQuestion.Option localOption = (TrainingQuestion.Option)localIterator.next();
      if ((localTableRow == null) || (j == i))
      {
        j = 0;
        localTableRow = new TableRow(getContext());
        localViewGroup.addView(localTableRow);
      }
      Integer localInteger = localOption.getClientAction();
      if (localInteger == null)
      {
        Log.e(TAG, "Option missing client action: " + localOption.getDisplayString());
      }
      else
      {
        Button localButton = (Button)this.mLayoutInflater.inflate(2130968876, localTableRow, false);
        if (localOption.getDisplayString() != null) {
          localButton.setText(localOption.getDisplayString().toUpperCase(Locale.getDefault()));
        }
        int k = TrainingQuestionViewHelper.getIconResourceId(localOption.getIconType());
        if (k != 0) {
          LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, 0, k, 0, 0);
        }
        localButton.setOnClickListener(new ClientActionClickListener(paramTrainingQuestion, localInteger.intValue()));
        localTableRow.addView(localButton);
        j++;
      }
    }
  }
  
  private class ClientActionClickListener
    implements View.OnClickListener
  {
    private final int mClientAction;
    private final TrainingQuestion mTrainingQuestion;
    
    public ClientActionClickListener(TrainingQuestion paramTrainingQuestion, int paramInt)
    {
      this.mTrainingQuestion = paramTrainingQuestion;
      this.mClientAction = paramInt;
    }
    
    public void onClick(View paramView)
    {
      if (MultipleClientActionQuestionView.this.mListener != null) {
        MultipleClientActionQuestionView.this.mListener.onClientActionSelected(this.mTrainingQuestion, this.mClientAction);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.MultipleClientActionQuestionView
 * JD-Core Version:    0.7.0.1
 */