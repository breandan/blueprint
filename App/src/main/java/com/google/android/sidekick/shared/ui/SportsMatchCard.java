package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.android.search.shared.ui.ClippedWebImageView;
import com.google.android.shared.util.UriLoader;
import com.google.common.base.Strings;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;

public class SportsMatchCard
  extends LinearLayout
{
  private List<View> mActionViews;
  private SportsDetailsView mDetailsView;
  private ClippedWebImageView mLeftLogo;
  private TextView mLeftName;
  private TextView mLeftRank;
  private SportScoreView mLeftScore;
  private ClippedWebImageView mRightLogo;
  private TextView mRightName;
  private TextView mRightRank;
  private SportScoreView mRightScore;
  private TextView mStatusText;
  private TextView mTitle;
  
  public SportsMatchCard(Context paramContext)
  {
    super(paramContext);
    init();
  }
  
  private static int goneIfEmpty(CharSequence paramCharSequence)
  {
    if (TextUtils.isEmpty(paramCharSequence)) {
      return 8;
    }
    return 0;
  }
  
  private void init()
  {
    setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
    setOrientation(1);
    setBackgroundResource(2130837532);
    LayoutInflater.from(getContext()).inflate(2130968841, this);
    this.mTitle = ((TextView)findViewById(2131297044));
    this.mStatusText = ((TextView)findViewById(2131296308));
    this.mLeftLogo = ((ClippedWebImageView)findViewById(2131297036));
    this.mLeftScore = ((SportScoreView)findViewById(2131297047));
    this.mLeftName = ((TextView)findViewById(2131297035));
    this.mLeftRank = ((TextView)findViewById(2131297046));
    this.mRightLogo = ((ClippedWebImageView)findViewById(2131297040));
    this.mRightScore = ((SportScoreView)findViewById(2131297049));
    this.mRightName = ((TextView)findViewById(2131297039));
    this.mRightRank = ((TextView)findViewById(2131297048));
    this.mActionViews = new LinkedList();
  }
  
  private static void setContestantLogo(UriLoader<Drawable> paramUriLoader, @Nullable Uri paramUri, ClippedWebImageView paramClippedWebImageView, Rect paramRect)
  {
    if (paramUri != null)
    {
      paramClippedWebImageView.setClipRect(paramRect);
      paramClippedWebImageView.setImageUri(paramUri, paramUriLoader);
      paramClippedWebImageView.setVisibility(0);
    }
  }
  
  private static void setContestantRank(TextView paramTextView, @Nullable String paramString)
  {
    if (paramString != null)
    {
      paramTextView.setText(paramString);
      paramTextView.setVisibility(0);
    }
  }
  
  private static void setContestantScore(SportScoreView paramSportScoreView, String paramString1, @Nullable String paramString2)
  {
    if (Strings.isNullOrEmpty(paramString1)) {
      paramSportScoreView.setScore("", false);
    }
    for (;;)
    {
      paramSportScoreView.setVisibility(goneIfEmpty(paramString1));
      return;
      if (!paramString1.equals(paramString2))
      {
        paramSportScoreView.setScore(paramString2, false);
        paramSportScoreView.setScore(paramString1, true);
      }
      else
      {
        paramSportScoreView.setScore(paramString1, false);
      }
    }
  }
  
  private void setDetailsView(@Nullable SportsDetailsView.Builder paramBuilder)
  {
    if (this.mDetailsView != null)
    {
      removeView(this.mDetailsView);
      this.mDetailsView = null;
    }
    if (paramBuilder == null) {
      return;
    }
    this.mDetailsView = paramBuilder.build(getContext());
    addView(this.mDetailsView);
  }
  
  private void setLeftContestant(UriLoader<Drawable> paramUriLoader, String paramString1, Uri paramUri, Rect paramRect, String paramString2, @Nullable String paramString3, @Nullable String paramString4)
  {
    this.mLeftName.setText(paramString1);
    setContestantLogo(paramUriLoader, paramUri, this.mLeftLogo, paramRect);
    setContestantScore(this.mLeftScore, paramString2, paramString3);
    setContestantRank(this.mLeftRank, paramString4);
  }
  
  private void setMatchInfo(String paramString, CharSequence paramCharSequence)
  {
    this.mTitle.setText(paramString);
    this.mTitle.setVisibility(goneIfEmpty(paramString));
    this.mStatusText.setText(paramCharSequence);
    this.mStatusText.setVisibility(goneIfEmpty(paramCharSequence));
  }
  
  private void setRightContestant(UriLoader<Drawable> paramUriLoader, String paramString1, @Nullable Uri paramUri, Rect paramRect, String paramString2, @Nullable String paramString3, @Nullable String paramString4)
  {
    this.mRightName.setText(paramString1);
    setContestantLogo(paramUriLoader, paramUri, this.mRightLogo, paramRect);
    setContestantScore(this.mRightScore, paramString2, paramString3);
    setContestantRank(this.mRightRank, paramString4);
  }
  
  public void addAction(String paramString, View.OnClickListener paramOnClickListener)
  {
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    if ((this.mActionViews.isEmpty()) && (this.mDetailsView != null)) {}
    for (int i = 2130968623;; i = 2130968622)
    {
      View localView = localLayoutInflater.inflate(i, this, false);
      Button localButton = (Button)localView.findViewById(2131296449);
      localButton.setText(paramString);
      localButton.setOnClickListener(paramOnClickListener);
      addView(localView);
      this.mActionViews.add(localView);
      return;
    }
  }
  
  public static class Builder
  {
    private SportsDetailsView.Builder mDetailsBuilder;
    private final SportsMatchCard.ContestantBuilder mLeftContestant;
    private final SportsMatchCard.ContestantBuilder mRightContestant;
    private CharSequence mStatus;
    private String mTitle;
    
    public Builder(UriLoader<Drawable> paramUriLoader)
    {
      this.mLeftContestant = new SportsMatchCard.ContestantBuilder(paramUriLoader);
      this.mRightContestant = new SportsMatchCard.ContestantBuilder(paramUriLoader);
    }
    
    public SportsMatchCard build(Context paramContext)
    {
      SportsMatchCard localSportsMatchCard = new SportsMatchCard(paramContext);
      update(localSportsMatchCard);
      return localSportsMatchCard;
    }
    
    public SportsMatchCard.ContestantBuilder leftContestant()
    {
      return this.mLeftContestant;
    }
    
    public SportsMatchCard.ContestantBuilder rightContestant()
    {
      return this.mRightContestant;
    }
    
    public Builder setSportsDetailsBuilder(SportsDetailsView.Builder paramBuilder)
    {
      this.mDetailsBuilder = paramBuilder;
      return this;
    }
    
    public Builder setStatus(CharSequence paramCharSequence)
    {
      this.mStatus = paramCharSequence;
      return this;
    }
    
    public void update(SportsMatchCard paramSportsMatchCard)
    {
      paramSportsMatchCard.setMatchInfo(this.mTitle, this.mStatus);
      SportsMatchCard.ContestantBuilder.access$100(this.mLeftContestant, paramSportsMatchCard, true);
      SportsMatchCard.ContestantBuilder.access$100(this.mRightContestant, paramSportsMatchCard, false);
      paramSportsMatchCard.setDetailsView(this.mDetailsBuilder);
    }
  }
  
  public static class ContestantBuilder
  {
    private final UriLoader<Drawable> mImageLoader;
    private Uri mLogo;
    private Rect mLogoClip;
    private String mName;
    private String mPreviousScore;
    private String mRank;
    private String mScore;
    
    ContestantBuilder(UriLoader<Drawable> paramUriLoader)
    {
      this.mImageLoader = paramUriLoader;
    }
    
    private void update(SportsMatchCard paramSportsMatchCard, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        paramSportsMatchCard.setLeftContestant(this.mImageLoader, this.mName, this.mLogo, this.mLogoClip, this.mScore, this.mPreviousScore, this.mRank);
        return;
      }
      paramSportsMatchCard.setRightContestant(this.mImageLoader, this.mName, this.mLogo, this.mLogoClip, this.mScore, this.mPreviousScore, this.mRank);
    }
    
    public ContestantBuilder setLogo(Uri paramUri, Rect paramRect)
    {
      this.mLogo = paramUri;
      this.mLogoClip = paramRect;
      return this;
    }
    
    public ContestantBuilder setName(String paramString)
    {
      this.mName = paramString;
      return this;
    }
    
    public ContestantBuilder setPreviousScore(String paramString)
    {
      this.mPreviousScore = paramString;
      return this;
    }
    
    public ContestantBuilder setRank(String paramString)
    {
      this.mRank = paramString;
      return this;
    }
    
    public ContestantBuilder setScore(String paramString)
    {
      this.mScore = paramString;
      return this;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.SportsMatchCard
 * JD-Core Version:    0.7.0.1
 */