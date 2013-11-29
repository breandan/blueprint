package com.google.android.sidekick.main.actions;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.main.inject.SidekickInjector;
import com.google.android.sidekick.main.notifications.NotificationRefreshService;
import com.google.android.sidekick.shared.util.ProtoUtils;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetFactory;
import com.google.android.velvet.VelvetServices;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.fragments.reminders.EditReminderPresenter;
import com.google.android.voicesearch.fragments.reminders.EditReminderView;
import com.google.android.voicesearch.fragments.reminders.MyPlacesSaver;
import com.google.android.voicesearch.fragments.reminders.RecurrenceHelper;
import com.google.android.voicesearch.fragments.reminders.SymbolicTime;
import com.google.common.collect.ImmutableSet;
import com.google.geo.sidekick.Sidekick.Chain;
import com.google.geo.sidekick.Sidekick.ChainId;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.GeostoreFeatureId;
import com.google.geo.sidekick.Sidekick.Location;
import com.google.geo.sidekick.Sidekick.ReminderEntry;
import com.google.majel.proto.EcoutezStructuredResponse.Chain;
import com.google.majel.proto.EcoutezStructuredResponse.ChainId;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import com.google.majel.proto.EcoutezStructuredResponse.FeatureIdProto;

public class EditReminderDialogFragment
  extends BaseEditDialogFragment
  implements DeleteReminderDialogFragment.DeleteReminderDialogListener
{
  private Sidekick.Entry mEntry;
  private EditReminderDialogListener mListener;
  private EditReminderPresenter mPresenter;
  
  private void clearNotifications(Sidekick.Entry paramEntry, Context paramContext)
  {
    paramContext.startService(NotificationRefreshService.getDeleteNotificationIntent(paramContext, ImmutableSet.of(paramEntry)));
  }
  
  public static SetReminderAction createVoiceAction(Sidekick.ReminderEntry paramReminderEntry)
  {
    SetReminderAction localSetReminderAction = new SetReminderAction();
    localSetReminderAction.setOriginalTaskId(paramReminderEntry.getTaskId());
    localSetReminderAction.setLabel(paramReminderEntry.getReminderMessage());
    int i;
    SymbolicTime localSymbolicTime;
    if (paramReminderEntry.hasTriggerTimeSeconds())
    {
      i = 1;
      localSetReminderAction.setDefaultLocationTrigger();
      if (paramReminderEntry.getResolution() == 3)
      {
        localSymbolicTime = SymbolicTime.TIME_UNSPECIFIED;
        localSetReminderAction.setSymbolicTime(localSymbolicTime);
        localSetReminderAction.setDateTimeMs(1000L * paramReminderEntry.getTriggerTimeSeconds());
      }
    }
    for (;;)
    {
      localSetReminderAction.setTriggerType(i);
      if ((VelvetServices.get().getGsaConfigFlags().getAddReminderRecurrenceEnabledVersion()) && (paramReminderEntry.hasRecurrenceInfo())) {
        RecurrenceHelper.parseSidekickRecurrenceInfo(paramReminderEntry.getRecurrenceInfo(), localSetReminderAction);
      }
      return localSetReminderAction;
      if (paramReminderEntry.getResolution() == 4)
      {
        localSymbolicTime = SymbolicTime.WEEKEND;
        break;
      }
      int j = paramReminderEntry.getResolution();
      localSymbolicTime = null;
      if (j != 2) {
        break;
      }
      boolean bool = paramReminderEntry.hasDayPart();
      localSymbolicTime = null;
      if (!bool) {
        break;
      }
      switch (paramReminderEntry.getDayPart())
      {
      default: 
        localSymbolicTime = null;
        break;
      case 1: 
        localSymbolicTime = SymbolicTime.MORNING;
        break;
      case 2: 
        localSymbolicTime = SymbolicTime.AFTERNOON;
        break;
      case 3: 
        localSymbolicTime = SymbolicTime.EVENING;
        break;
      case 4: 
        localSymbolicTime = SymbolicTime.NIGHT;
        break;
        if (paramReminderEntry.hasLocation())
        {
          i = 2;
          localSetReminderAction.setDefaultDateTime();
          EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult;
          if (paramReminderEntry.getLocation().hasAlias()) {
            switch (paramReminderEntry.getLocation().getAlias())
            {
            default: 
              localEcoutezLocalResult = localSetReminderAction.getHomeLocation();
            }
          }
          while (localEcoutezLocalResult != null)
          {
            localSetReminderAction.setLocation(localEcoutezLocalResult);
            break;
            localEcoutezLocalResult = localSetReminderAction.getWorkLocation();
            continue;
            Sidekick.Location localLocation = paramReminderEntry.getLocation();
            if ((localLocation.hasChain()) && (localLocation.getChain().hasChainId()) && (localLocation.getChain().getChainId().hasFeatureId()) && (localLocation.getChain().hasDisplayName()))
            {
              Sidekick.GeostoreFeatureId localGeostoreFeatureId = localLocation.getChain().getChainId().getFeatureId();
              EcoutezStructuredResponse.FeatureIdProto localFeatureIdProto = new EcoutezStructuredResponse.FeatureIdProto().setCellId(localGeostoreFeatureId.getCellId()).setFprint(localGeostoreFeatureId.getFprint());
              EcoutezStructuredResponse.ChainId localChainId = new EcoutezStructuredResponse.ChainId().setFeatureId(localFeatureIdProto);
              localEcoutezLocalResult = new EcoutezStructuredResponse.EcoutezLocalResult().setTitle(localLocation.getChain().getDisplayName()).setChain(new EcoutezStructuredResponse.Chain().setChainId(localChainId).setDisplayName(localLocation.getChain().getDisplayName()));
            }
            else
            {
              localEcoutezLocalResult = new EcoutezStructuredResponse.EcoutezLocalResult().setTitle(paramReminderEntry.getLocation().getName()).setAddress(paramReminderEntry.getLocation().getAddress()).setLatDegrees(paramReminderEntry.getLocation().getLat()).setLngDegrees(paramReminderEntry.getLocation().getLng());
            }
          }
        }
        localSetReminderAction.setDefaultLocationTrigger();
        localSetReminderAction.setDefaultDateTime();
        i = 1;
      }
    }
  }
  
  private void deleteReminder(Sidekick.Entry paramEntry)
  {
    DeleteReminderDialogFragment.newInstance(getTargetFragment(), paramEntry).show(getFragmentManager().beginTransaction().addToBackStack("delete_reminder_dialog"), "delete_dialog");
  }
  
  public static EditReminderDialogFragment newInstance(Fragment paramFragment, Sidekick.Entry paramEntry)
  {
    Bundle localBundle = new Bundle();
    localBundle.putByteArray("entry", paramEntry.toByteArray());
    EditReminderDialogFragment localEditReminderDialogFragment = new EditReminderDialogFragment();
    localEditReminderDialogFragment.setArguments(localBundle);
    localEditReminderDialogFragment.setTargetFragment(paramFragment, 0);
    return localEditReminderDialogFragment;
  }
  
  private void showToast(Context paramContext, int paramInt)
  {
    Toast.makeText(paramContext, paramInt, 0).show();
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    VelvetServices localVelvetServices = VelvetServices.get();
    SidekickInjector localSidekickInjector = localVelvetServices.getSidekickInjector();
    NetworkClient localNetworkClient = localSidekickInjector.getNetworkClient();
    if (paramBundle != null) {
      this.mEntry = ProtoUtils.getEntryFromByteArray(paramBundle.getByteArray("entry"));
    }
    for (SetReminderAction localSetReminderAction = (SetReminderAction)paramBundle.getParcelable("reminder_action");; localSetReminderAction = createVoiceAction(this.mEntry.getReminderEntry()))
    {
      this.mPresenter = new EditReminderPresenter(localVelvetServices.getFactory().createReminderSaver(), localNetworkClient, new MyPlacesSaver(getActivity(), localNetworkClient, localSidekickInjector.getEntryProvider(), localVelvetServices.getCoreServices().getClock()), localSetReminderAction);
      this.mPresenter.fetchHomeAndWork();
      this.mPresenter.fetchConfirmationUrlPath();
      Fragment localFragment = getTargetFragment();
      if ((localFragment instanceof EditReminderDialogListener)) {
        this.mListener = ((EditReminderDialogListener)localFragment);
      }
      final EditReminderView localEditReminderView = (EditReminderView)getActivity().getLayoutInflater().inflate(2130968664, null);
      this.mPresenter.setUi(localEditReminderView);
      localEditReminderView.setPresenter(this.mPresenter);
      this.mPresenter.initUi();
      final Context localContext = getActivity().getApplicationContext();
      final FragmentLaunchingAlertDialog localFragmentLaunchingAlertDialog = new FragmentLaunchingAlertDialog(getActivity(), getFragmentManager(), 2131362729);
      localFragmentLaunchingAlertDialog.setView(localEditReminderView);
      localFragmentLaunchingAlertDialog.setPositiveButton(2131361820, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EditReminderDialogFragment.this.mPresenter.setAliasLocationIfRequired(new SimpleCallback()
          {
            public void onResult(Boolean paramAnonymous2Boolean)
            {
              if (paramAnonymous2Boolean.booleanValue())
              {
                EditReminderDialogFragment.this.hideSoftKeyboard(EditReminderDialogFragment.1.this.val$editView.findViewById(2131296532));
                EditReminderDialogFragment.this.showToast(EditReminderDialogFragment.1.this.val$context, 2131363515);
                EditReminderDialogFragment.this.mPresenter.saveReminder(new SimpleCallback()
                {
                  public void onResult(Boolean paramAnonymous3Boolean)
                  {
                    if (!paramAnonymous3Boolean.booleanValue()) {
                      EditReminderDialogFragment.this.showToast(EditReminderDialogFragment.1.this.val$context, 2131363424);
                    }
                    for (;;)
                    {
                      EditReminderDialogFragment.1.this.val$dialog.dismiss();
                      return;
                      EditReminderDialogFragment.this.showToast(EditReminderDialogFragment.1.this.val$context, 2131363516);
                      EditReminderDialogFragment.this.clearNotifications(EditReminderDialogFragment.this.mEntry, EditReminderDialogFragment.1.this.val$context);
                      if (EditReminderDialogFragment.this.mListener != null) {
                        EditReminderDialogFragment.this.mListener.onReminderEdited();
                      }
                    }
                  }
                });
                return;
              }
              EditReminderDialogFragment.this.showToast(EditReminderDialogFragment.1.this.val$context, 2131363424);
            }
          });
        }
      });
      localFragmentLaunchingAlertDialog.setNegativeButton(2131362735, new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          EditReminderDialogFragment.this.deleteReminder(EditReminderDialogFragment.this.mEntry);
          localFragmentLaunchingAlertDialog.hide();
        }
      });
      return localFragmentLaunchingAlertDialog;
      this.mEntry = ProtoUtils.getEntryFromByteArray(getArguments().getByteArray("entry"));
    }
  }
  
  public void onReminderDeleted(String paramString)
  {
    if (this.mListener != null) {
      this.mListener.onReminderDeleted(paramString);
    }
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putParcelable("reminder_action", this.mPresenter.getReminderAction());
    paramBundle.putByteArray("entry", this.mEntry.toByteArray());
  }
  
  public static abstract interface EditReminderDialogListener
    extends DeleteReminderDialogFragment.DeleteReminderDialogListener
  {
    public abstract void onReminderEdited();
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.actions.EditReminderDialogFragment
 * JD-Core Version:    0.7.0.1
 */