package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.Animations;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.util.ExampleContactHelper.Contact;

public class SimpleHelpCard
  extends FrameLayout
  implements SimpleHelpController.Ui
{
  private final View mCard;
  private View mCardPreviews;
  private View mDatePreview;
  private TextView mDatePreviewDay;
  private TextView mDatePreviewDayOfWeek;
  private TextView mDatePreviewMonth;
  private View mDivider;
  private TextView mExample;
  private TextView mHeadline;
  private WebImageView mImagePreviewBack;
  private WebImageView mImagePreviewFront;
  private View mLastShownPreview;
  private Drawable mPlaceholderAvatar;
  private TextView mRefreshExampleButton;
  private View mTimePreview;
  private TextView mTimePreviewHour;
  private TextView mTimePreviewMinute;
  private TextView mTimePreviewSeparator;
  
  public SimpleHelpCard(Context paramContext)
  {
    super(paramContext);
    this.mCard = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2130968715, this, false);
    this.mHeadline = ((TextView)this.mCard.findViewById(2131296682));
    this.mExample = ((TextView)this.mCard.findViewById(2131296683));
    this.mRefreshExampleButton = ((TextView)this.mCard.findViewById(2131296695));
    this.mCardPreviews = this.mCard.findViewById(2131296684);
    this.mImagePreviewFront = ((WebImageView)this.mCard.findViewById(2131296693));
    this.mImagePreviewBack = ((WebImageView)this.mCard.findViewById(2131296694));
    this.mDatePreview = this.mCard.findViewById(2131296685);
    this.mDatePreviewDay = ((TextView)this.mCard.findViewById(2131296687));
    this.mDatePreviewMonth = ((TextView)this.mCard.findViewById(2131296688));
    this.mDatePreviewDayOfWeek = ((TextView)this.mCard.findViewById(2131296686));
    this.mTimePreview = this.mCard.findViewById(2131296689);
    this.mTimePreviewHour = ((TextView)this.mCard.findViewById(2131296690));
    this.mTimePreviewSeparator = ((TextView)this.mCard.findViewById(2131296691));
    this.mTimePreviewMinute = ((TextView)this.mCard.findViewById(2131296692));
    this.mDivider = this.mCard.findViewById(2131296675);
    addView(this.mCard);
  }
  
  private void pushImagePreviewFrontToBack()
  {
    WebImageView localWebImageView = this.mImagePreviewBack;
    this.mImagePreviewBack = this.mImagePreviewFront;
    this.mImagePreviewFront = localWebImageView;
    this.mImagePreviewFront.setVisibility(4);
    showPreview(this.mImagePreviewFront);
  }
  
  private void showPreview(View paramView)
  {
    if (this.mLastShownPreview != paramView)
    {
      if (this.mLastShownPreview == null) {
        break label47;
      }
      Animations.fadeOutAndHide(this.mLastShownPreview, 4).setDuration(500L);
      Animations.showAndFadeIn(paramView).setDuration(500L);
    }
    for (;;)
    {
      this.mLastShownPreview = paramView;
      return;
      label47:
      paramView.setVisibility(0);
    }
  }
  
  public void setExampleQuery(String paramString)
  {
    String str = getContext().getString(2131363315, new Object[] { paramString });
    if (TextUtils.isEmpty(this.mExample.getText()))
    {
      this.mExample.setText(str);
      return;
    }
    Animations.fadeUpdateText(this.mExample, str);
  }
  
  public void setHeadline(String paramString)
  {
    this.mHeadline.setText(paramString);
  }
  
  public void setIntroduction(String paramString1, String paramString2)
  {
    setHeadline(paramString1);
    this.mExample.setText(paramString2);
    this.mDivider.setVisibility(8);
    this.mRefreshExampleButton.setVisibility(8);
    this.mCardPreviews.setVisibility(8);
  }
  
  public void setOnRefreshExample(View.OnClickListener paramOnClickListener)
  {
    this.mRefreshExampleButton.setOnClickListener(paramOnClickListener);
  }
  
  public void setPreviewContact(ExampleContactHelper.Contact paramContact)
  {
    AsyncTask local1 = new AsyncTask()
    {
      protected Bitmap doInBackground(Long... paramAnonymousVarArgs)
      {
        Bitmap localBitmap = ContactLookup.fetchPhotoBitmap(SimpleHelpCard.this.mImagePreviewFront.getContext().getContentResolver(), paramAnonymousVarArgs[0].longValue(), true);
        if (localBitmap != null) {
          return localBitmap;
        }
        if (SimpleHelpCard.this.mPlaceholderAvatar == null) {
          SimpleHelpCard.access$102(SimpleHelpCard.this, SimpleHelpCard.this.getResources().getDrawable(2130837605));
        }
        return null;
      }
      
      protected void onPostExecute(Bitmap paramAnonymousBitmap)
      {
        SimpleHelpCard.this.pushImagePreviewFrontToBack();
        if (paramAnonymousBitmap != null)
        {
          SimpleHelpCard.this.mImagePreviewFront.setImageBitmap(paramAnonymousBitmap);
          return;
        }
        SimpleHelpCard.this.mImagePreviewFront.setImageDrawable(SimpleHelpCard.this.mPlaceholderAvatar);
      }
    };
    Long[] arrayOfLong = new Long[1];
    arrayOfLong[0] = Long.valueOf(paramContact.id);
    local1.execute(arrayOfLong);
  }
  
  public void setPreviewDate(String paramString1, String paramString2, String paramString3)
  {
    if (this.mLastShownPreview == this.mDatePreview)
    {
      ViewPropertyAnimator[] arrayOfViewPropertyAnimator = new ViewPropertyAnimator[3];
      arrayOfViewPropertyAnimator[0] = Animations.fadeScaleUpdateText(this.mDatePreviewDayOfWeek, paramString1, 0.75F);
      arrayOfViewPropertyAnimator[1] = Animations.fadeScaleUpdateText(this.mDatePreviewDay, paramString2, 0.75F);
      arrayOfViewPropertyAnimator[2] = Animations.fadeScaleUpdateText(this.mDatePreviewMonth, paramString3, 0.75F);
      Animations.stagger(100L, 0.5F, arrayOfViewPropertyAnimator);
    }
    for (;;)
    {
      showPreview(this.mDatePreview);
      return;
      this.mDatePreviewDayOfWeek.setText(paramString1);
      this.mDatePreviewDay.setText(paramString2);
      this.mDatePreviewMonth.setText(paramString3);
    }
  }
  
  public void setPreviewTime(String paramString1, String paramString2)
  {
    if (this.mLastShownPreview == this.mTimePreview)
    {
      ViewPropertyAnimator[] arrayOfViewPropertyAnimator = new ViewPropertyAnimator[3];
      arrayOfViewPropertyAnimator[0] = Animations.fadeScaleUpdateText(this.mTimePreviewHour, paramString1, 0.75F);
      arrayOfViewPropertyAnimator[1] = Animations.fadeScaleUpdateText(this.mTimePreviewSeparator, this.mTimePreviewSeparator.getText(), 0.75F);
      arrayOfViewPropertyAnimator[2] = Animations.fadeScaleUpdateText(this.mTimePreviewMinute, paramString2, 0.75F);
      Animations.stagger(100L, 0.5F, arrayOfViewPropertyAnimator);
    }
    for (;;)
    {
      showPreview(this.mTimePreview);
      return;
      this.mTimePreviewHour.setText(paramString1);
      this.mTimePreviewMinute.setText(paramString2);
    }
  }
  
  public void setPreviewUrl(String paramString)
  {
    pushImagePreviewFrontToBack();
    this.mImagePreviewFront.setImageUrl(paramString, VelvetServices.get().getImageLoader());
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.SimpleHelpCard
 * JD-Core Version:    0.7.0.1
 */