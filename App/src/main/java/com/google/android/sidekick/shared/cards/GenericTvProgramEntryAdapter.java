package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.search.shared.ui.WebImageView;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.SimpleEntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.android.sidekick.shared.util.PhotoWithAttributionDecorator;
import com.google.geo.sidekick.Sidekick.CastMember;
import com.google.geo.sidekick.Sidekick.CrewMember;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GenericTvProgramEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class GenericTvProgramEntryAdapter
  extends BaseEntryAdapter
{
  private final PhotoWithAttributionDecorator mPhotoWithAttributionDecorator;
  private final Sidekick.GenericTvProgramEntry mProgram;
  
  public GenericTvProgramEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, PhotoWithAttributionDecorator paramPhotoWithAttributionDecorator)
  {
    super(paramEntry, paramActivityHelper);
    this.mPhotoWithAttributionDecorator = paramPhotoWithAttributionDecorator;
    this.mProgram = paramEntry.getGenericTvProgramEntry();
  }
  
  private static void makeCastSectionVisible(View paramView)
  {
    paramView.findViewById(2131296662).setVisibility(0);
    paramView.findViewById(2131296663).setVisibility(0);
    paramView.findViewById(2131296665).setVisibility(0);
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView1 = paramLayoutInflater.inflate(2130968702, paramViewGroup, false);
    ((TextView)localView1.findViewById(2131296658)).setText(this.mProgram.getName());
    if (this.mProgram.hasPhoto())
    {
      WebImageView localWebImageView2 = (WebImageView)localView1.findViewById(2131296657);
      localWebImageView2.setImageUri(this.mPhotoWithAttributionDecorator.getPhotoUri(paramContext, this.mProgram.getPhoto(), 2131689763, 2131689764), paramPredictiveCardContainer.getImageLoader());
      localWebImageView2.setVisibility(0);
      if (this.mProgram.getPhoto().hasClickAction()) {
        localWebImageView2.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 136, this.mProgram.getPhoto().getClickAction()));
      }
    }
    if (this.mProgram.getMetaInfoCount() > 0)
    {
      LinearLayout localLinearLayout4 = (LinearLayout)localView1.findViewById(2131296659);
      Iterator localIterator = this.mProgram.getMetaInfoList().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        TextView localTextView4 = (TextView)paramLayoutInflater.inflate(2130968902, localLinearLayout4, false);
        localTextView4.setText(str);
        localLinearLayout4.addView(localTextView4);
      }
      localLinearLayout4.setVisibility(0);
    }
    if (this.mProgram.hasDescription())
    {
      TextView localTextView3 = (TextView)localView1.findViewById(2131296660);
      localTextView3.setText(this.mProgram.getDescription());
      localTextView3.setVisibility(0);
    }
    if (this.mProgram.getCrewMemberCount() > 0)
    {
      LinearLayout localLinearLayout3 = (LinearLayout)localView1.findViewById(2131296661);
      Sidekick.CrewMember localCrewMember = this.mProgram.getCrewMember(0);
      View localView3 = paramLayoutInflater.inflate(2130968894, localLinearLayout3);
      ((TextView)localView3.findViewById(2131297162)).setText(localCrewMember.getRole());
      TextView localTextView2 = (TextView)localView3.findViewById(2131297163);
      localTextView2.setText(localCrewMember.getName());
      if (localCrewMember.hasNameAction()) {
        localTextView2.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 133, localCrewMember.getNameAction()));
      }
      localLinearLayout3.setVisibility(0);
    }
    int i = this.mProgram.getCastMemberCount();
    if (i > 0)
    {
      ((TextView)localView1.findViewById(2131296663)).setText(this.mProgram.getCastLabel());
      int j;
      int k;
      label456:
      LinearLayout localLinearLayout1;
      int m;
      label484:
      Sidekick.CastMember localCastMember;
      int n;
      label517:
      LinearLayout localLinearLayout2;
      WebImageView localWebImageView1;
      PhotoWithAttributionDecorator localPhotoWithAttributionDecorator;
      Sidekick.Photo localPhoto;
      int i1;
      if (i < 3)
      {
        j = 1;
        ViewStub localViewStub = (ViewStub)localView1.findViewById(2131296664);
        if (j == 0) {
          break label760;
        }
        k = 2130968891;
        localViewStub.setLayoutResource(k);
        localViewStub.inflate();
        localLinearLayout1 = (LinearLayout)localView1.findViewById(2131296665);
        m = 0;
        if ((m >= i) || (m >= 4)) {
          break label788;
        }
        localCastMember = this.mProgram.getCastMember(m);
        if (j == 0) {
          break label767;
        }
        n = 2130968890;
        localLinearLayout2 = (LinearLayout)paramLayoutInflater.inflate(n, localLinearLayout1, false);
        if (localCastMember.hasClickAction()) {
          localLinearLayout2.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 134, localCastMember.getClickAction()));
        }
        if (localCastMember.hasPhoto())
        {
          localWebImageView1 = (WebImageView)localLinearLayout2.findViewById(2131297159);
          localPhotoWithAttributionDecorator = this.mPhotoWithAttributionDecorator;
          localPhoto = localCastMember.getPhoto();
          if (j == 0) {
            break label774;
          }
          i1 = 2131689767;
          label608:
          if (j == 0) {
            break label781;
          }
        }
      }
      label774:
      label781:
      for (int i2 = 2131689768;; i2 = 2131689770)
      {
        localWebImageView1.setImageUri(localPhotoWithAttributionDecorator.getPhotoUri(paramContext, localPhoto, i1, i2), paramPredictiveCardContainer.getImageLoader());
        if (localCastMember.getPhoto().hasClickAction()) {
          localWebImageView1.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 134, localCastMember.getPhoto().getClickAction()));
        }
        localWebImageView1.setVisibility(0);
        if (localCastMember.hasCharacter())
        {
          TextView localTextView1 = (TextView)localLinearLayout2.findViewById(2131297161);
          localTextView1.setText(localCastMember.getCharacter());
          localTextView1.setVisibility(0);
        }
        ((TextView)localLinearLayout2.findViewById(2131297160)).setText(localCastMember.getName());
        localLinearLayout1.addView(localLinearLayout2);
        m++;
        break label484;
        j = 0;
        break;
        label760:
        k = 2130968892;
        break label456;
        label767:
        n = 2130968893;
        break label517;
        i1 = 2131689769;
        break label608;
      }
      label788:
      makeCastSectionVisible(localView1);
    }
    if (this.mProgram.hasClickAction())
    {
      View localView2 = localView1.findViewById(2131296666);
      localView2.setOnClickListener(new SimpleEntryClickListener(paramContext, paramPredictiveCardContainer, this, getEntry(), 135, this.mProgram.getClickAction()));
      localView2.setVisibility(0);
    }
    return localView1;
  }
  
  @Nullable
  protected View getViewToFocusForDetails(View paramView)
  {
    return paramView.findViewById(2131296656);
  }
  
  public void launchDetails(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView)
  {
    if (this.mProgram.hasClickAction()) {
      handleClickAction(paramContext, paramPredictiveCardContainer, this.mProgram.getClickAction());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GenericTvProgramEntryAdapter
 * JD-Core Version:    0.7.0.1
 */