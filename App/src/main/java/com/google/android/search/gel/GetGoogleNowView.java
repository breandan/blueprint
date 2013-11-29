package com.google.android.search.gel;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class GetGoogleNowView
  extends FrameLayout
{
  private GoogleNowPromoController mGoogleNowPromoController;
  
  public GetGoogleNowView(Context paramContext)
  {
    super(paramContext);
  }
  
  public GetGoogleNowView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public GetGoogleNowView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void init(GoogleNowPromoController paramGoogleNowPromoController)
  {
    this.mGoogleNowPromoController = paramGoogleNowPromoController;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    findViewById(2131296668).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        GetGoogleNowView.this.mGoogleNowPromoController.recordPromoClicked();
        Intent localIntent = new Intent();
        localIntent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.velvet.tg.FirstRunActivity");
        localIntent.putExtra("skip_to_end", true);
        GetGoogleNowView.this.getContext().startActivity(localIntent);
      }
    });
    findViewById(2131296669).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        GetGoogleNowView.this.setVisibility(8);
        GetGoogleNowView.this.mGoogleNowPromoController.recordPromoDismissed();
      }
    });
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.gel.GetGoogleNowView
 * JD-Core Version:    0.7.0.1
 */