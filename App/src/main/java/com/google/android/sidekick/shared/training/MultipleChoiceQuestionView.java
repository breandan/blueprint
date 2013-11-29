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
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;

public class MultipleChoiceQuestionView
  extends LinearLayout
  implements QuestionView
{
  private static final String TAG = Tag.getTag(MultipleChoiceQuestionView.class);
  @Nullable
  private Sidekick.Entry mEntry;
  private LayoutInflater mLayoutInflater;
  private QuestionViewListener mListener;
  
  public MultipleChoiceQuestionView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public MultipleChoiceQuestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init()
  {
    this.mLayoutInflater = LayoutInflater.from(getContext());
    this.mLayoutInflater.inflate(2130968875, this);
  }
  
  public void setEntry(@Nullable Sidekick.Entry paramEntry)
  {
    this.mEntry = paramEntry;
  }
  
  public void setListener(QuestionViewListener paramQuestionViewListener)
  {
    this.mListener = paramQuestionViewListener;
  }
  
  public void setTrainingQuestion(TrainingQuestion paramTrainingQuestion)
  {
    TrainingQuestionViewHelper.setQuestionAndJustification(this, paramTrainingQuestion, this.mEntry);
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131297132);
    localViewGroup.removeAllViews();
    ArrayList localArrayList = Lists.newArrayList();
    int i = getResources().getInteger(2131427467);
    TableRow localTableRow = null;
    int j = 0;
    Iterator localIterator1 = paramTrainingQuestion.getMultipleChoiceOptions().iterator();
    while (localIterator1.hasNext())
    {
      TrainingQuestion.Option localOption = (TrainingQuestion.Option)localIterator1.next();
      if ((localTableRow == null) || (j == i))
      {
        j = 0;
        localTableRow = new TableRow(getContext());
        localViewGroup.addView(localTableRow);
      }
      Button localButton = (Button)this.mLayoutInflater.inflate(2130968876, localTableRow, false);
      if (localOption.getDisplayString() != null) {
        localButton.setText(localOption.getDisplayString().toUpperCase(Locale.getDefault()));
      }
      int n = TrainingQuestionViewHelper.getIconResourceId(localOption.getIconType());
      if (n != 0) {
        LayoutUtils.setCompoundDrawablesRelativeWithIntrinsicBounds(localButton, 0, n, 0, 0);
      }
      localArrayList.add(localButton);
      localTableRow.addView(localButton);
      j++;
    }
    int k = 0;
    Iterator localIterator2 = localArrayList.iterator();
    while (localIterator2.hasNext())
    {
      ((Button)localIterator2.next()).setOnClickListener(new MultipleChoiceClickListener(paramTrainingQuestion, k, localArrayList));
      k++;
    }
    int m;
    if (paramTrainingQuestion.getAnswer() != null)
    {
      m = paramTrainingQuestion.getAnswer().getMultipleChoiceAnswer();
      if ((m >= 0) && (m < localArrayList.size())) {
        ((Button)localArrayList.get(m)).setSelected(true);
      }
    }
    else
    {
      return;
    }
    String str = TAG;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = Long.valueOf(paramTrainingQuestion.getQuestion().getTemplateId());
    arrayOfObject[1] = Integer.valueOf(m);
    Log.e(str, String.format("Invalid multiple choice answer for question %d: %d", arrayOfObject));
  }
  
  private class MultipleChoiceClickListener
    implements View.OnClickListener
  {
    private final int mAnswer;
    private final List<Button> mOptionViews;
    private final TrainingQuestion mTrainingQuestion;
    
    MultipleChoiceClickListener(int paramInt, List<Button> paramList)
    {
      this.mTrainingQuestion = paramInt;
      this.mAnswer = paramList;
      Object localObject;
      this.mOptionViews = localObject;
    }
    
    public void onClick(View paramView)
    {
      int i = 0;
      if (i < this.mOptionViews.size())
      {
        Button localButton = (Button)this.mOptionViews.get(i);
        if (i == this.mAnswer) {}
        for (boolean bool = true;; bool = false)
        {
          localButton.setSelected(bool);
          i++;
          break;
        }
      }
      if (MultipleChoiceQuestionView.this.mListener != null) {
        MultipleChoiceQuestionView.this.mListener.onAnswerSelected(this.mTrainingQuestion, new Sidekick.Question.Answer().setMultipleChoiceAnswer(this.mAnswer), MultipleChoiceQuestionView.this.mEntry);
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.MultipleChoiceQuestionView
 * JD-Core Version:    0.7.0.1
 */