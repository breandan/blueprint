package com.google.android.sidekick.shared.training;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question.Answer;
import javax.annotation.Nullable;

public class YesNoQuestionView
  extends LinearLayout
  implements View.OnClickListener, QuestionView
{
  @Nullable
  private Sidekick.Entry mEntry;
  private QuestionViewListener mListener;
  private View mNoButton;
  private TrainingQuestion mTrainingQuestion;
  private View mYesButton;
  
  public YesNoQuestionView(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  public YesNoQuestionView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init()
  {
    LayoutInflater.from(getContext()).inflate(2130968879, this);
    this.mYesButton = findViewById(2131297133);
    this.mYesButton.setOnClickListener(this);
    this.mNoButton = findViewById(2131297134);
    this.mNoButton.setOnClickListener(this);
  }
  
  private void setAnswer(boolean paramBoolean)
  {
    this.mYesButton.setSelected(paramBoolean);
    View localView = this.mNoButton;
    if (!paramBoolean) {}
    for (boolean bool = true;; bool = false)
    {
      localView.setSelected(bool);
      return;
    }
  }
  
  public void onClick(View paramView)
  {
    if (paramView == this.mYesButton) {}
    for (boolean bool = true;; bool = false)
    {
      setAnswer(bool);
      if (this.mListener != null) {
        this.mListener.onAnswerSelected(this.mTrainingQuestion, new Sidekick.Question.Answer().setYesNoAnswer(bool), this.mEntry);
      }
      return;
    }
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
    this.mTrainingQuestion = paramTrainingQuestion;
    TrainingQuestionViewHelper.setQuestionAndJustification(this, paramTrainingQuestion, this.mEntry);
    Sidekick.Question.Answer localAnswer = paramTrainingQuestion.getAnswer();
    if ((localAnswer != null) && (localAnswer.hasYesNoAnswer())) {
      setAnswer(localAnswer.getYesNoAnswer());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.YesNoQuestionView
 * JD-Core Version:    0.7.0.1
 */