package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.shared.util.IntentStarter;
import com.google.android.shared.util.IntentStarter.ResultCallback;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import javax.annotation.Nullable;

public class GooglePlayServicesCardAdapter
  extends BaseEntryAdapter
{
  private final String mActionString;
  private final String mErrorString;
  private final IntentStarter mIntentStarter;
  private final Intent mRecoveryIntent;
  
  public GooglePlayServicesCardAdapter(String paramString1, String paramString2, @Nullable Intent paramIntent, ActivityHelper paramActivityHelper, IntentStarter paramIntentStarter)
  {
    super(new Sidekick.Entry(), paramActivityHelper);
    this.mIntentStarter = paramIntentStarter;
    this.mErrorString = paramString1;
    this.mActionString = paramString2;
    this.mRecoveryIntent = paramIntent;
  }
  
  private Button createActionButton(ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    View localView = paramLayoutInflater.inflate(2130968622, paramViewGroup, false);
    Button localButton = (Button)localView.findViewById(2131296449);
    paramViewGroup.addView(localView);
    return localButton;
  }
  
  public View getView(Context paramContext, final PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = (ViewGroup)paramLayoutInflater.inflate(2130968700, paramViewGroup, false);
    TextView localTextView1 = (TextView)localViewGroup.findViewById(2131296451);
    localTextView1.setText(2131362794);
    localTextView1.setVisibility(0);
    TextView localTextView2 = (TextView)localViewGroup.findViewById(2131296653);
    localTextView2.setText(this.mErrorString);
    localTextView2.setVisibility(0);
    ((ViewGroup)localViewGroup.findViewById(2131296450)).removeView(localViewGroup.findViewById(2131296461));
    if ((this.mRecoveryIntent != null) && (!TextUtils.isEmpty(this.mActionString)))
    {
      Button localButton = createActionButton(localViewGroup, paramLayoutInflater);
      localButton.setText(this.mActionString);
      localButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          GooglePlayServicesCardAdapter.this.mIntentStarter.startActivityForResult(GooglePlayServicesCardAdapter.this.mRecoveryIntent, new IntentStarter.ResultCallback()
          {
            public void onResult(int paramAnonymous2Int, Intent paramAnonymous2Intent, Context paramAnonymous2Context)
            {
              GooglePlayServicesCardAdapter.1.this.val$cardContainer.invalidateEntries();
            }
          });
        }
      });
    }
    return localViewGroup;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.GooglePlayServicesCardAdapter
 * JD-Core Version:    0.7.0.1
 */