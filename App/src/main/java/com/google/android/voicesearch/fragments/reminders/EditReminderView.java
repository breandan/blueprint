package com.google.android.voicesearch.fragments.reminders;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.android.datetimepicker.time.TimePickerDialog.OnTimeSetListener;
import com.android.recurrencepicker.EventRecurrence;
import com.android.recurrencepicker.EventRecurrenceFormatter;
import com.android.recurrencepicker.RecurrencePickerDialog;
import com.android.recurrencepicker.RecurrencePickerDialog.OnRecurrenceSetListener;
import com.google.android.search.core.Feature;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.ui.SuggestionDialog;
import com.google.android.shared.util.Util;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.android.velvet.ui.CustomSpinner;
import com.google.android.velvet.ui.CustomSpinner.OnItemSeletedAlwaysListener;
import com.google.android.velvet.ui.util.CustomPairStringArrayAdapter;
import com.google.android.velvet.ui.util.CustomValueArrayAdapter;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Chain;
import com.google.geo.sidekick.Sidekick.ChainId;
import com.google.geo.sidekick.Sidekick.GeostoreFeatureId;
import com.google.majel.proto.EcoutezStructuredResponse.Chain;
import com.google.majel.proto.EcoutezStructuredResponse.ChainId;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;
import com.google.majel.proto.EcoutezStructuredResponse.FeatureIdProto;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Nullable;

public class EditReminderView
  extends LinearLayout
  implements EditReminderUi
{
  private CustomSpinner mDateSpinner;
  private CustomValueArrayAdapter<DateTimePickerHelper.DropDownElement<Integer>> mDateSpinnerAdapter;
  private DateTimePickerHelper mDateTimePickerHelper;
  private final View.OnTouchListener mKeyboardHidingOnTouchListener = new View.OnTouchListener()
  {
    public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
    {
      if (EditReminderView.this.mLabelView.hasFocus()) {
        EditReminderView.this.mLabelView.clearFocus();
      }
      return false;
    }
  };
  private final View.OnFocusChangeListener mLabelFocusListener = new View.OnFocusChangeListener()
  {
    public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
    {
      if (!paramAnonymousBoolean) {
        Util.hideSoftKeyboard(EditReminderView.this.getContext(), EditReminderView.this.mLabelView);
      }
    }
  };
  private final TextWatcher mLabelTextWatcher = new TextWatcher()
  {
    public void afterTextChanged(Editable paramAnonymousEditable)
    {
      if (EditReminderView.this.mPresenter != null) {
        EditReminderView.this.mPresenter.setLabel(paramAnonymousEditable.toString());
      }
    }
    
    public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
    
    public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
  };
  private EditText mLabelView;
  private final CustomSpinner.OnItemSeletedAlwaysListener mLocationAliasListener = new CustomSpinner.OnItemSeletedAlwaysListener()
  {
    public void onItemSelected(int paramAnonymousInt)
    {
      if (EditReminderView.this.mPresenter != null) {}
      switch (paramAnonymousInt)
      {
      default: 
        return;
      case 0: 
      case 1: 
        EditReminderView.this.mPresenter.setLocationAlias(EditReminderView.getActionV2LocationAlias(paramAnonymousInt));
        return;
      }
      EditReminderView.this.mPresenter.showCustomLocationPicker();
    }
  };
  private CustomSpinner mLocationAliasSpinner;
  private CustomPairStringArrayAdapter mLocationAliasSpinnerAdapter;
  private final AdapterView.OnItemSelectedListener mLocationTriggerTypeListener = new AdapterView.OnItemSelectedListener()
  {
    public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      if (EditReminderView.this.mPresenter != null) {
        EditReminderView.this.mPresenter.setLocationTriggerType(EditReminderView.getActionV2LocationTriggerType(paramAnonymousInt));
      }
    }
    
    public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {}
  };
  private Spinner mLocationTriggerTypeSpinner;
  private RadioButton mLocationTriggerView;
  private final CustomSpinner.OnItemSeletedAlwaysListener mOnDateSelectedListener = new CustomSpinner.OnItemSeletedAlwaysListener()
  {
    public void onItemSelected(int paramAnonymousInt)
    {
      DateTimePickerHelper.DropDownElement localDropDownElement;
      if (EditReminderView.this.mPresenter != null)
      {
        localDropDownElement = (DateTimePickerHelper.DropDownElement)EditReminderView.this.mDateSpinnerAdapter.getItem(paramAnonymousInt);
        if (!localDropDownElement.isPrompt()) {
          break label43;
        }
        EditReminderView.this.mPresenter.setCustomDate();
      }
      label43:
      while (localDropDownElement.object == null) {
        return;
      }
      switch (((Integer)localDropDownElement.object).intValue())
      {
      default: 
        return;
      case 0: 
        EditReminderView.this.mPresenter.setDateToday();
        return;
      case 1: 
        EditReminderView.this.mPresenter.setDateTomorrow();
        return;
      }
      EditReminderView.this.mPresenter.setDateWeekend();
    }
  };
  private final DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener()
  {
    public void onDateSet(DatePickerDialog paramAnonymousDatePickerDialog, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      if (EditReminderView.this.mPresenter != null)
      {
        EditReminderView.this.mPresenter.clearSymbolicDay();
        EditReminderView.this.mPresenter.setDate(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
      }
    }
  };
  private final DialogInterface.OnDismissListener mOnDialogDismissListener = new DialogInterface.OnDismissListener()
  {
    public void onDismiss(DialogInterface paramAnonymousDialogInterface)
    {
      EditReminderView.access$902(EditReminderView.this, null);
    }
  };
  private final RecurrencePickerDialog.OnRecurrenceSetListener mOnRecurrenceSetListener = new RecurrencePickerDialog.OnRecurrenceSetListener()
  {
    public void onRecurrenceSet(String paramAnonymousString)
    {
      if (paramAnonymousString != null)
      {
        EventRecurrence localEventRecurrence = new EventRecurrence();
        localEventRecurrence.parse(paramAnonymousString);
        EditReminderView.this.mPresenter.setRecurrence(localEventRecurrence);
        Time localTime = RecurrenceHelper.getUpdatedDateForWeeklyReminder(localEventRecurrence, EditReminderView.this.mPresenter.getReminderAction().getSetUpTimeMs());
        if (localTime != null) {
          EditReminderView.this.mPresenter.setDate(localTime.year, localTime.month, localTime.monthDay);
        }
        return;
      }
      EditReminderView.this.mPresenter.setRecurrence(null);
    }
  };
  private final CustomSpinner.OnItemSeletedAlwaysListener mOnTimeSelectedListener = new CustomSpinner.OnItemSeletedAlwaysListener()
  {
    public void onItemSelected(int paramAnonymousInt)
    {
      DateTimePickerHelper.DropDownElement localDropDownElement;
      if (EditReminderView.this.mPresenter != null)
      {
        localDropDownElement = (DateTimePickerHelper.DropDownElement)EditReminderView.this.mTimeSpinnerAdapter.getItem(paramAnonymousInt);
        if (!localDropDownElement.isPrompt()) {
          break label43;
        }
        EditReminderView.this.mPresenter.setCustomTime();
      }
      label43:
      while (localDropDownElement.object == null) {
        return;
      }
      EditReminderView.this.mPresenter.setSymbolicTime((SymbolicTime)localDropDownElement.object);
    }
  };
  private final TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener()
  {
    public void onTimeSet(RadialPickerLayout paramAnonymousRadialPickerLayout, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      if (EditReminderView.this.mPresenter != null)
      {
        EditReminderView.this.mPresenter.setSymbolicTime(null);
        EditReminderView.this.mPresenter.setTime(paramAnonymousInt1, paramAnonymousInt2);
      }
    }
  };
  @Nullable
  private Dialog mOpenDialog;
  private EditReminderPresenter mPresenter;
  private Button mRecurrenceRuleButton;
  private final View.OnClickListener mRecurrenceRuleButtonListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      Bundle localBundle = new Bundle();
      localBundle.putLong("bundle_event_start_time", EditReminderView.this.mPresenter.getReminderAction().getDateTimeMs());
      localBundle.putString("bundle_event_time_zone", Time.getCurrentTimezone());
      if (EditReminderView.this.mPresenter.getRecurrence() != null) {}
      for (String str = EditReminderView.this.mPresenter.getRecurrence().toString();; str = null)
      {
        localBundle.putString("bundle_event_rrule", str);
        RecurrencePickerDialog localRecurrencePickerDialog = new RecurrencePickerDialog();
        localRecurrencePickerDialog.setArguments(localBundle);
        localRecurrencePickerDialog.setOnRecurrenceSetListener(EditReminderView.this.mOnRecurrenceSetListener);
        EditReminderView.this.showDialog(localRecurrencePickerDialog, "recurrencepicker_tag");
        return;
      }
    }
  };
  private CustomSpinner mTimeSpinner;
  private CustomValueArrayAdapter<DateTimePickerHelper.DropDownElement<SymbolicTime>> mTimeSpinnerAdapter;
  private RadioButton mTimeTriggerView;
  private final View.OnClickListener mTriggerToggleListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      EditReminderPresenter localEditReminderPresenter;
      if (EditReminderView.this.mPresenter != null)
      {
        localEditReminderPresenter = EditReminderView.this.mPresenter;
        if (paramAnonymousView != EditReminderView.this.mTimeTriggerView) {
          break label37;
        }
      }
      label37:
      for (int i = 1;; i = 2)
      {
        localEditReminderPresenter.setTriggerType(i);
        return;
      }
    }
  };
  
  public EditReminderView(Context paramContext)
  {
    super(paramContext);
  }
  
  public EditReminderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public EditReminderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private static int getActionV2LocationAlias(int paramInt)
  {
    if (paramInt == 0) {
      return 0;
    }
    return 1;
  }
  
  private static int getActionV2LocationTriggerType(int paramInt)
  {
    if (paramInt == 0) {
      return 0;
    }
    return 2;
  }
  
  @Nullable
  private Activity getActivity()
  {
    Context localContext = getContext();
    if ((localContext instanceof Activity)) {}
    for (Activity localActivity = (Activity)localContext;; localActivity = (Activity)((ContextThemeWrapper)localContext).getBaseContext())
    {
      return localActivity;
      if (!(localContext instanceof ContextThemeWrapper)) {
        break;
      }
    }
    Log.e("EditReminderView", "Unable to get activity from context");
    return null;
  }
  
  @Nullable
  private FragmentManager getFragmentManager()
  {
    Activity localActivity = getActivity();
    if (localActivity == null) {
      return null;
    }
    return localActivity.getFragmentManager();
  }
  
  private void setDateSpinnerSelection(DateTimePickerHelper.DropDownElement<Integer> paramDropDownElement)
  {
    int i = this.mDateSpinnerAdapter.getPosition(paramDropDownElement);
    if (i != -1) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mDateSpinner.setSelectionNoCallback(i);
      return;
    }
  }
  
  private void setTimeSpinnerSelection(DateTimePickerHelper.DropDownElement<SymbolicTime> paramDropDownElement)
  {
    int i = this.mTimeSpinnerAdapter.getPosition(paramDropDownElement);
    if (i != -1) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mTimeSpinner.setSelectionNoCallback(i);
      return;
    }
  }
  
  private void showDialog(Dialog paramDialog)
  {
    paramDialog.setOnDismissListener(this.mOnDialogDismissListener);
    this.mOpenDialog = paramDialog;
    paramDialog.show();
  }
  
  private void showDialog(DialogFragment paramDialogFragment, String paramString)
  {
    this.mOpenDialog = null;
    FragmentManager localFragmentManager = getFragmentManager();
    if (localFragmentManager != null) {
      paramDialogFragment.show(localFragmentManager, paramString);
    }
  }
  
  public void clearEmbeddedAction() {}
  
  protected void onFinishInflate()
  {
    this.mDateTimePickerHelper = new DateTimePickerHelper(getContext());
    this.mLabelView = ((EditText)findViewById(2131296532));
    this.mLabelView.addTextChangedListener(this.mLabelTextWatcher);
    this.mLabelView.setOnFocusChangeListener(this.mLabelFocusListener);
    this.mTimeTriggerView = ((RadioButton)findViewById(2131296533));
    this.mLocationTriggerView = ((RadioButton)findViewById(2131296534));
    this.mDateSpinner = ((CustomSpinner)findViewById(2131296536));
    this.mTimeSpinner = ((CustomSpinner)findViewById(2131296305));
    this.mLocationTriggerTypeSpinner = ((Spinner)findViewById(2131296537));
    this.mLocationAliasSpinner = ((CustomSpinner)findViewById(2131296539));
    Context localContext = getContext();
    this.mDateSpinnerAdapter = new CustomValueArrayAdapter(localContext, 2130968666, DateTimePickerHelper.safeListToArray(this.mDateTimePickerHelper.getAvailableDateElements(Calendar.getInstance().getTimeInMillis())), null);
    this.mDateSpinner.setAdapter(this.mDateSpinnerAdapter);
    this.mDateSpinner.setOnItemSelectedAlwaysListener(this.mOnDateSelectedListener);
    this.mDateSpinner.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mTimeSpinnerAdapter = new CustomValueArrayAdapter(localContext, 2130968666, DateTimePickerHelper.safeListToArray(this.mDateTimePickerHelper.getAvailableTimeElements(DateTimePickerHelper.getAvailableTimes(System.currentTimeMillis()))), null);
    this.mTimeSpinner.setAdapter(this.mTimeSpinnerAdapter);
    this.mTimeSpinner.setOnItemSelectedAlwaysListener(this.mOnTimeSelectedListener);
    this.mTimeSpinner.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mLocationTriggerTypeSpinner.setAdapter(new ArrayAdapter(localContext, 2130968666, getResources().getStringArray(2131492940)));
    this.mLocationTriggerTypeSpinner.setOnItemSelectedListener(this.mLocationTriggerTypeListener);
    this.mLocationTriggerTypeSpinner.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mLocationAliasSpinnerAdapter = new CustomPairStringArrayAdapter(localContext, getResources().getStringArray(2131492941));
    this.mLocationAliasSpinner.setAdapter(this.mLocationAliasSpinnerAdapter);
    this.mLocationAliasSpinner.setOnItemSelectedAlwaysListener(this.mLocationAliasListener);
    this.mLocationAliasSpinner.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mTimeTriggerView.setOnClickListener(this.mTriggerToggleListener);
    this.mTimeTriggerView.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mLocationTriggerView.setOnClickListener(this.mTriggerToggleListener);
    this.mLocationTriggerView.setOnTouchListener(this.mKeyboardHidingOnTouchListener);
    this.mRecurrenceRuleButton = ((Button)findViewById(2131296538));
    this.mRecurrenceRuleButton.setOnClickListener(this.mRecurrenceRuleButtonListener);
    this.mRecurrenceRuleButton.setEnabled(true);
    super.onFinishInflate();
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    if ((paramInt != 0) && (this.mOpenDialog != null)) {
      this.mOpenDialog.cancel();
    }
  }
  
  public void pickDate(int paramInt1, int paramInt2, int paramInt3)
  {
    showCustomDate();
    showDialog(DatePickerDialog.newInstance(this.mOnDateSetListener, paramInt1, paramInt2, paramInt3), "datepicker_tag");
  }
  
  public void pickLocation(@Nullable String paramString, int paramInt, SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> paramSimpleCallback, @Nullable DialogInterface.OnCancelListener paramOnCancelListener)
  {
    PlacesApiFetcher localPlacesApiFetcher = new PlacesApiFetcher(getContext());
    SuggestionDialog localSuggestionDialog = new SuggestionDialog(getContext(), 2130968583, new PlacesSuggestionCallback(paramSimpleCallback), localPlacesApiFetcher, 2131363510);
    localSuggestionDialog.setTitle(paramInt);
    localSuggestionDialog.setAdapter(localPlacesApiFetcher.getAdapter());
    if (paramString != null) {
      localSuggestionDialog.setFilter(paramString);
    }
    localSuggestionDialog.getWindow().setSoftInputMode(5);
    if (paramOnCancelListener != null) {
      localSuggestionDialog.setOnCancelListener(paramOnCancelListener);
    }
    showDialog(localSuggestionDialog);
  }
  
  public void pickTime(int paramInt1, int paramInt2)
  {
    showCustomTime();
    showDialog(TimePickerDialog.newInstance(this.mOnTimeSetListener, paramInt1, paramInt2, DateFormat.is24HourFormat(getContext())), "timepicker_tag");
  }
  
  public void revertLocationSelection()
  {
    this.mLocationAliasSpinner.revertToPreviousSelection();
  }
  
  public void setCustomDate(long paramLong)
  {
    this.mDateSpinnerAdapter.setCustomValue(DateUtils.formatDateTime(getContext(), paramLong, 2578));
  }
  
  public void setCustomLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult)
  {
    String str1 = paramEcoutezLocalResult.getTitle();
    if (paramEcoutezLocalResult.hasChain()) {
      str1 = paramEcoutezLocalResult.getChain().getDisplayName();
    }
    for (String str2 = getContext().getString(2131363135);; str2 = paramEcoutezLocalResult.getAddress())
    {
      if (str1.equals(str2)) {
        str2 = null;
      }
      this.mLocationAliasSpinnerAdapter.setCustomValue(str1, str2);
      return;
    }
  }
  
  public void setCustomTime(long paramLong)
  {
    this.mTimeSpinnerAdapter.setCustomValue(DateUtils.formatDateTime(getContext(), paramLong, 2561));
  }
  
  public void setHomeAndWorkAddresses(@Nullable String paramString1, @Nullable String paramString2)
  {
    if (paramString1 != null) {
      this.mLocationAliasSpinnerAdapter.updateItem(0, getContext().getString(2131361860), paramString1);
    }
    if (paramString2 != null) {
      this.mLocationAliasSpinnerAdapter.updateItem(1, getContext().getString(2131362214), paramString2);
    }
  }
  
  public void setLabel(String paramString)
  {
    if (!TextUtils.equals(paramString, this.mLabelView.getText().toString()))
    {
      this.mLabelView.setText(paramString);
      if (!TextUtils.isEmpty(paramString)) {
        this.mLabelView.setSelection(paramString.length());
      }
    }
  }
  
  public void setLocationAlias(int paramInt)
  {
    CustomSpinner localCustomSpinner = this.mLocationAliasSpinner;
    if (paramInt == 0) {}
    for (int i = 0;; i = 1)
    {
      localCustomSpinner.setSelectionNoCallback(i);
      return;
    }
  }
  
  public void setLocationTriggerType(int paramInt)
  {
    Spinner localSpinner = this.mLocationTriggerTypeSpinner;
    if (paramInt == 0) {}
    for (int i = 0;; i = 1)
    {
      localSpinner.setSelection(i);
      return;
    }
  }
  
  public void setPresenter(EditReminderPresenter paramEditReminderPresenter)
  {
    this.mPresenter = paramEditReminderPresenter;
    FragmentManager localFragmentManager = getFragmentManager();
    if (localFragmentManager != null)
    {
      DatePickerDialog localDatePickerDialog = (DatePickerDialog)localFragmentManager.findFragmentByTag("datepicker_tag");
      if (localDatePickerDialog != null) {
        localDatePickerDialog.setOnDateSetListener(this.mOnDateSetListener);
      }
      TimePickerDialog localTimePickerDialog = (TimePickerDialog)localFragmentManager.findFragmentByTag("timepicker_tag");
      if (localTimePickerDialog != null) {
        localTimePickerDialog.setOnTimeSetListener(this.mOnTimeSetListener);
      }
      RecurrencePickerDialog localRecurrencePickerDialog = (RecurrencePickerDialog)localFragmentManager.findFragmentByTag("recurrencepicker_tag");
      if (localRecurrencePickerDialog != null) {
        localRecurrencePickerDialog.setOnRecurrenceSetListener(this.mOnRecurrenceSetListener);
      }
    }
  }
  
  public void setRecurrence(EventRecurrence paramEventRecurrence)
  {
    Resources localResources = getResources();
    String str;
    boolean bool;
    if (paramEventRecurrence != null)
    {
      str = EventRecurrenceFormatter.getRepeatString(getContext(), localResources, paramEventRecurrence, true, this.mPresenter.getReminderAction().getDateTimeMs());
      if (str == null)
      {
        str = localResources.getString(2131361831);
        Log.e("EditReminderView", "Can't generate display string for recurrence: " + paramEventRecurrence);
        bool = false;
      }
    }
    for (;;)
    {
      this.mRecurrenceRuleButton.setText(str);
      this.mRecurrenceRuleButton.setEnabled(bool);
      return;
      bool = RecurrencePickerDialog.canHandleRecurrenceRule(paramEventRecurrence);
      if (!bool)
      {
        Log.e("EditReminderView", "UI can't handle recurrence:" + paramEventRecurrence);
        continue;
        str = localResources.getString(2131363117);
        bool = true;
      }
    }
  }
  
  public void setSymbolicTimeAndAvailableOptions(SymbolicTime paramSymbolicTime, List<SymbolicTime> paramList)
  {
    if (paramSymbolicTime == SymbolicTime.WEEKEND)
    {
      this.mTimeSpinner.setVisibility(8);
      setDateSpinnerSelection(this.mDateTimePickerHelper.getDateWeekendElement());
      return;
    }
    this.mTimeSpinner.setVisibility(0);
    this.mTimeSpinnerAdapter.clear();
    List localList = this.mDateTimePickerHelper.getAvailableTimeElements(paramList);
    this.mTimeSpinnerAdapter.addAll(localList);
    this.mTimeSpinnerAdapter.notifyDataSetChanged();
    if (paramSymbolicTime == null) {}
    for (DateTimePickerHelper.DropDownElement localDropDownElement = this.mDateTimePickerHelper.getTimePickerPromptValueElement();; localDropDownElement = this.mDateTimePickerHelper.getTimeElement(paramSymbolicTime))
    {
      int i = localList.indexOf(localDropDownElement);
      this.mTimeSpinner.setSelectionNoCallback(i);
      return;
    }
  }
  
  public void setTriggerType(int paramInt)
  {
    if (paramInt == 1)
    {
      this.mTimeTriggerView.setChecked(true);
      this.mLocationTriggerView.setChecked(false);
      this.mDateSpinner.setVisibility(0);
      if (this.mDateSpinnerAdapter.getItem(this.mDateSpinner.getSelectedItemPosition()) != this.mDateTimePickerHelper.getDateWeekendElement()) {
        this.mTimeSpinner.setVisibility(0);
      }
      if (Feature.REMINDERS_LEAVING_TRIGGER.isEnabled()) {
        this.mLocationTriggerTypeSpinner.setVisibility(8);
      }
      if (VelvetServices.get().getGsaConfigFlags().getAddReminderRecurrenceEnabledVersion()) {
        this.mRecurrenceRuleButton.setVisibility(0);
      }
      for (;;)
      {
        this.mLocationAliasSpinner.setVisibility(8);
        return;
        this.mRecurrenceRuleButton.setVisibility(8);
      }
    }
    if (paramInt == 2)
    {
      this.mLocationTriggerView.setChecked(true);
      this.mTimeTriggerView.setChecked(false);
      if (Feature.REMINDERS_LEAVING_TRIGGER.isEnabled()) {
        this.mLocationTriggerTypeSpinner.setVisibility(0);
      }
      this.mLocationAliasSpinner.setVisibility(0);
      this.mDateSpinner.setVisibility(8);
      this.mTimeSpinner.setVisibility(8);
      this.mRecurrenceRuleButton.setVisibility(8);
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public void showCustomDate()
  {
    setDateSpinnerSelection(this.mDateTimePickerHelper.getDatePickerPromptValueElement());
  }
  
  public void showCustomLocation()
  {
    this.mLocationAliasSpinner.setSelectionNoCallback(this.mLocationAliasSpinnerAdapter.getCustomValuePosition());
  }
  
  public void showCustomTime()
  {
    setTimeSpinnerSelection(this.mDateTimePickerHelper.getTimePickerPromptValueElement());
  }
  
  public void showDateToday()
  {
    setDateSpinnerSelection(this.mDateTimePickerHelper.getDateTodayElement());
  }
  
  public void showDateTomorrow()
  {
    setDateSpinnerSelection(this.mDateTimePickerHelper.getDateTomorrowElement());
  }
  
  public void showEmbeddedPhoneCallAction(String paramString) {}
  
  public void showToast(int paramInt)
  {
    Toast.makeText(getContext(), paramInt, 0).show();
  }
  
  public void updateDateSpinner(long paramLong)
  {
    this.mDateSpinnerAdapter.clear();
    this.mDateSpinnerAdapter.addAll(DateTimePickerHelper.safeListToArray(this.mDateTimePickerHelper.getAvailableDateElements(paramLong)));
  }
  
  final class PlacesSuggestionCallback
    implements SimpleCallback<PlacesApiFetcher.PlaceSuggestion>
  {
    private final SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> mCallback;
    
    PlacesSuggestionCallback()
    {
      Object localObject;
      this.mCallback = localObject;
    }
    
    public void onResult(PlacesApiFetcher.PlaceSuggestion paramPlaceSuggestion)
    {
      if (EditReminderView.this.mPresenter != null)
      {
        if (paramPlaceSuggestion.getChainData() == null) {
          EditReminderView.this.mPresenter.getCustomLocationDetails(paramPlaceSuggestion, this.mCallback);
        }
      }
      else {
        return;
      }
      EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = new EcoutezStructuredResponse.EcoutezLocalResult();
      localEcoutezLocalResult.setTitle(paramPlaceSuggestion.getDescription());
      localEcoutezLocalResult.setIsChain(true);
      Sidekick.Chain localChain = paramPlaceSuggestion.getChainData();
      Sidekick.ChainId localChainId = localChain.getChainId();
      EcoutezStructuredResponse.Chain localChain1 = new EcoutezStructuredResponse.Chain();
      EcoutezStructuredResponse.ChainId localChainId1 = new EcoutezStructuredResponse.ChainId();
      localChain1.setChainId(localChainId1);
      if (localChainId.hasFeatureId())
      {
        Sidekick.GeostoreFeatureId localGeostoreFeatureId = localChainId.getFeatureId();
        EcoutezStructuredResponse.FeatureIdProto localFeatureIdProto = new EcoutezStructuredResponse.FeatureIdProto();
        if ((localGeostoreFeatureId.hasCellId()) && (localGeostoreFeatureId.hasFprint()))
        {
          localFeatureIdProto.setCellId(localGeostoreFeatureId.getCellId());
          localFeatureIdProto.setFprint(localGeostoreFeatureId.getFprint());
          localChainId1.setFeatureId(localFeatureIdProto);
        }
      }
      if (localChain.hasDisplayName()) {
        localChain1.setDisplayName(localChain.getDisplayName());
      }
      localEcoutezLocalResult.setChain(localChain1);
      this.mCallback.onResult(localEcoutezLocalResult);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.EditReminderView
 * JD-Core Version:    0.7.0.1
 */