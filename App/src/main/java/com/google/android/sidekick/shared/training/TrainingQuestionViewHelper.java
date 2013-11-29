package com.google.android.sidekick.shared.training;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.sidekick.shared.remoteapi.TrainingQuestion;
import com.google.android.sidekick.shared.util.CardTextUtil;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.Question;
import javax.annotation.Nullable;

public class TrainingQuestionViewHelper
{
  private static final String TAG = Tag.getTag(TrainingQuestionViewHelper.class);
  
  public static int getIconResourceId(int paramInt)
  {
    if (paramInt == 0) {
      return 0;
    }
    switch (paramInt)
    {
    case 12: 
    default: 
      String str = TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      Log.e(str, String.format("Missing resource mapping for icon %d", arrayOfObject));
      return 0;
    case 1: 
      return 2130837975;
    case 2: 
      return 2130837941;
    case 3: 
      return 2130837947;
    case 4: 
      return 2130837963;
    case 5: 
      return 2130837966;
    case 6: 
      return 2130837970;
    case 7: 
      return 2130837936;
    case 8: 
      return 2130837948;
    case 9: 
      return 2130837958;
    case 10: 
      return 2130837951;
    case 11: 
      return 2130837967;
    }
    return 2130837966;
  }
  
  @Nullable
  public static Integer getQuestionViewResourceId(int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
    case 4: 
    default: 
      return null;
    case 1: 
      return Integer.valueOf(2130968878);
    case 2: 
      return Integer.valueOf(2130968874);
    }
    return Integer.valueOf(2130968877);
  }
  
  public static int getSportIconResourceId(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 0: 
      return 2130837912;
    case 1: 
      return 2130837915;
    case 2: 
      return 2130837909;
    case 3: 
      return 2130837918;
    case 4: 
      return 2130837925;
    }
    return 2130837921;
  }
  
  public static void setClosetTrainingQuestionBackground(View paramView, Resources paramResources)
  {
    int i = paramView.getPaddingTop();
    int j = paramView.getPaddingBottom();
    int k = paramResources.getDimensionPixelSize(2131689839);
    paramView.setBackgroundResource(2130837538);
    paramView.setPadding(k, i, k, j);
  }
  
  public static void setQuestionAndJustification(View paramView, TrainingQuestion paramTrainingQuestion, @Nullable Sidekick.Entry paramEntry)
  {
    ((TextView)paramView.findViewById(2131297127)).setText(paramTrainingQuestion.getQuestionString());
    Sidekick.Question localQuestion = paramTrainingQuestion.getQuestion();
    String str1;
    TextView localTextView;
    if ((localQuestion.hasAnswer()) && (localQuestion.hasAnswerTimestampMillis()))
    {
      str1 = TimeUtilities.getRelativeElapsedString(paramView.getContext(), 2131362822, System.currentTimeMillis() - localQuestion.getAnswerTimestampMillis(), true);
      if (str1 != null)
      {
        localTextView = (TextView)paramView.findViewById(2131296353);
        if ((paramEntry == null) || ((paramEntry.getType() != 45) && (paramEntry.getType() != 46))) {
          break label193;
        }
        String str2 = paramView.getContext().getString(2131362682).replace(" ", "&nbsp;");
        localTextView.setText(Html.fromHtml(CardTextUtil.hyphenate(Lists.newArrayList(new String[] { str1, "<a href=\"https://support.google.com/chrome/?p=mobile_google_now\">" + str2 + "</a>" })).toString()));
        localTextView.setMovementMethod(new LinkMovementMethod()
        {
          public void onTakeFocus(TextView paramAnonymousTextView, Spannable paramAnonymousSpannable, int paramAnonymousInt)
          {
            super.onTakeFocus(paramAnonymousTextView, paramAnonymousSpannable, paramAnonymousInt);
            down(paramAnonymousTextView, paramAnonymousSpannable);
          }
        });
      }
    }
    for (;;)
    {
      localTextView.setVisibility(0);
      return;
      str1 = paramTrainingQuestion.getJustificationString();
      break;
      label193:
      localTextView.setText(str1);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.training.TrainingQuestionViewHelper
 * JD-Core Version:    0.7.0.1
 */