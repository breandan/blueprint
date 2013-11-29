package com.google.android.voicesearch.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.google.android.search.core.util.LoggingIntentStarter;
import com.google.android.shared.util.ActivityIntentStarter;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.CardFactory;
import com.google.android.voicesearch.DialogCardController;
import com.google.android.voicesearch.fragments.action.VoiceAction;

public class VoiceActionDialogFragment
  extends DialogFragment
{
  private ActivityIntentStarter mIntentStarter;
  private VoiceAction mVoiceAction;
  
  public static VoiceActionDialogFragment newInstance(VoiceAction paramVoiceAction)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("voice_action_key", paramVoiceAction);
    VoiceActionDialogFragment localVoiceActionDialogFragment = new VoiceActionDialogFragment();
    localVoiceActionDialogFragment.setArguments(localBundle);
    return localVoiceActionDialogFragment;
  }
  
  public VoiceAction getVoiceAction()
  {
    return this.mVoiceAction;
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mIntentStarter.onActivityResultDelegate(paramInt1, paramInt2, paramIntent);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mIntentStarter = new LoggingIntentStarter(getActivity(), 100);
    if (paramBundle != null)
    {
      this.mVoiceAction = ((VoiceAction)paramBundle.getParcelable("voice_action_key"));
      this.mIntentStarter.restoreInstanceState(paramBundle);
      return;
    }
    this.mVoiceAction = ((VoiceAction)getArguments().getParcelable("voice_action_key"));
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Dialog localDialog = super.onCreateDialog(paramBundle);
    localDialog.getWindow().requestFeature(1);
    int i = 2 * (int)getActivity().getResources().getDimension(2131689633);
    int j = LayoutUtils.getCardWidth(getActivity()) - i;
    localDialog.getWindow().setLayout(j, -2);
    return localDialog;
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    CardFactory localCardFactory = new CardFactory(getActivity());
    VelvetFactory localVelvetFactory = VelvetServices.get().getFactory();
    DialogCardController localDialogCardController = localVelvetFactory.createDialogCardController();
    AbstractCardController localAbstractCardController = localVelvetFactory.createControllerFactory(this.mIntentStarter).createController(this.mVoiceAction, localDialogCardController);
    localAbstractCardController.setVoiceAction(this.mVoiceAction);
    AbstractCardView localAbstractCardView = localCardFactory.createCard(localAbstractCardController);
    localAbstractCardController.start();
    localDialogCardController.setFragment(this);
    return localAbstractCardView;
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("voice_action_key", this.mVoiceAction);
    this.mIntentStarter.onSaveInstanceState(paramBundle);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.VoiceActionDialogFragment
 * JD-Core Version:    0.7.0.1
 */