package com.google.android.search.core.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.core.SearchError;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;

public class ErrorView
  extends LinearLayout
{
  private Runnable mTryAgainClickListener;
  
  public ErrorView(Context paramContext)
  {
    super(paramContext);
  }
  
  public ErrorView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public ErrorView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void setError(Query paramQuery, SearchError paramSearchError)
  {
    TextView localTextView = (TextView)Preconditions.checkNotNull(findViewById(2131296549));
    ImageView localImageView = (ImageView)findViewById(2131296548);
    Button localButton = (Button)findViewById(2131296550);
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (ErrorView.this.mTryAgainClickListener != null) {
          ErrorView.this.mTryAgainClickListener.run();
        }
      }
    });
    if (paramSearchError == null)
    {
      localTextView.setText("");
      return;
    }
    int i = paramSearchError.getErrorMessageResId();
    if (i == 0)
    {
      localTextView.setText(paramSearchError.getErrorMessage());
      if (paramSearchError.getErrorImageResId() == 0) {
        break label127;
      }
      localImageView.setImageResource(paramSearchError.getErrorImageResId());
    }
    for (;;)
    {
      if (paramSearchError.isRetriable())
      {
        if (paramSearchError.getButtonTextId() != 0)
        {
          localButton.setText(paramSearchError.getButtonTextId());
          return;
          localTextView.setText(i);
          break;
          label127:
          if (TextUtils.equals(paramQuery.getQueryString(), " Frown Clown "))
          {
            localImageView.setImageResource(2130837506);
            continue;
          }
          localImageView.setImageResource(2130837603);
          continue;
        }
        localButton.setText(2131363288);
        return;
      }
    }
    localButton.setVisibility(4);
  }
  
  public void setTryAgainClickListener(Runnable paramRunnable)
  {
    this.mTryAgainClickListener = paramRunnable;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ui.ErrorView
 * JD-Core Version:    0.7.0.1
 */