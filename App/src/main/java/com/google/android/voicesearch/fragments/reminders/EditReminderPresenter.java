package com.google.android.voicesearch.fragments.reminders;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.android.recurrencepicker.EventRecurrence;
import com.google.android.search.core.util.FetchMyPlacesTask;
import com.google.android.sidekick.main.inject.NetworkClient;
import com.google.android.sidekick.shared.util.PlaceUtils;
import com.google.android.sidekick.shared.util.TimeUtilities;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.voicesearch.fragments.action.SetReminderAction;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.LocationTrigger;
import com.google.majel.proto.AliasProto.Alias;
import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Nullable;

public class EditReminderPresenter {
    private final SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> mCustomLocationCallback = new SimpleCallback() {
        public void onResult(EcoutezStructuredResponse.EcoutezLocalResult paramAnonymousEcoutezLocalResult) {
            if (paramAnonymousEcoutezLocalResult == null) {
                if (EditReminderPresenter.this.mUi != null) {
                    EditReminderPresenter.this.mUi.showToast(2131363424);
                }
                EditReminderPresenter.this.mReminderAction.setDefaultLocationTrigger();
            }
            for (; ; ) {
                EditReminderPresenter.this.setLocation(EditReminderPresenter.this.mReminderAction.getLocation());
                return;
                EditReminderPresenter.this.mReminderAction.setLocation(paramAnonymousEcoutezLocalResult);
            }
        }
    };
    private final MyPlacesSaver mMyPlacesSaver;
    private final NetworkClient mNetworkClient;
    private SetReminderAction mReminderAction;
    private final ReminderSaver mReminderSaver;
    private final long mSetUpTimeMs;
    @Nullable
    private EditReminderUi mUi;

    public EditReminderPresenter(ReminderSaver paramReminderSaver, NetworkClient paramNetworkClient, MyPlacesSaver paramMyPlacesSaver, SetReminderAction paramSetReminderAction) {
        this.mMyPlacesSaver = paramMyPlacesSaver;
        this.mNetworkClient = paramNetworkClient;
        this.mReminderSaver = paramReminderSaver;
        this.mSetUpTimeMs = System.currentTimeMillis();
        this.mReminderAction = ((SetReminderAction) Preconditions.checkNotNull(paramSetReminderAction));
    }

    private Sidekick.Entry createEditPlaceEntryForAction(int paramInt) {
        return new Sidekick.Entry().setType(1).addEntryAction(new Sidekick.Action().setType(paramInt));
    }

    private void setDate(Calendar paramCalendar) {
        setDate(paramCalendar.get(1), paramCalendar.get(2), paramCalendar.get(5));
    }

    private void setEmbeddedAction(ActionV2Protos.ActionV2 paramActionV2) {
        this.mReminderAction.setEmbeddedAction(paramActionV2);
        if (this.mUi != null) {
            if (paramActionV2 != null) {
                break label29;
            }
            this.mUi.clearEmbeddedAction();
        }
        label29:
        while (!paramActionV2.hasPhoneActionExtension()) {
            return;
        }
        this.mUi.showEmbeddedPhoneCallAction(PhoneActionUtils.getContactName(paramActionV2.getPhoneActionExtension()));
    }

    private void updateHomeWorkAddressInUi() {
        if (this.mUi != null) {
            this.mUi.setHomeAndWorkAddresses(this.mReminderAction.getHomeLocation().getAddress(), this.mReminderAction.getWorkLocation().getAddress());
        }
    }

    public void clearSymbolicDay() {
        if (this.mReminderAction.getSymbolicTime() == SymbolicTime.WEEKEND) {
            setSymbolicTime(SymbolicTime.TIME_UNSPECIFIED);
        }
    }

    public void fetchConfirmationUrlPath() {
        this.mReminderSaver.fetchConfirmationUrlPath();
    }

    public void fetchHomeAndWork() {
        new FetchMyPlacesTask(this.mNetworkClient, new SimpleCallback() {
            public void onResult(Pair<Sidekick.Entry, Sidekick.Entry> paramAnonymousPair) {
                if (paramAnonymousPair != null) {
                    String str1 = PlaceUtils.getConfirmedAddress((Sidekick.Entry) paramAnonymousPair.first);
                    if (str1 != null) {
                        EditReminderPresenter.this.mReminderAction.getHomeLocation().setAddress(str1);
                    }
                    String str2 = PlaceUtils.getConfirmedAddress((Sidekick.Entry) paramAnonymousPair.second);
                    if (str2 != null) {
                        EditReminderPresenter.this.mReminderAction.getWorkLocation().setAddress(str2);
                    }
                }
                EditReminderPresenter.this.updateHomeWorkAddressInUi();
            }
        }).execute(new Void[0]);
    }

    public void getCustomLocationDetails(PlacesApiFetcher.PlaceSuggestion paramPlaceSuggestion, SimpleCallback<EcoutezStructuredResponse.EcoutezLocalResult> paramSimpleCallback) {
        new PlacesApiFetcher.PlaceDetailsTask(paramSimpleCallback, paramPlaceSuggestion, this.mNetworkClient).execute(new Void[0]);
    }

    String getLabel() {
        return this.mReminderAction.getLabel();
    }

    public EventRecurrence getRecurrence() {
        return this.mReminderAction.getRecurrence();
    }

    public SetReminderAction getReminderAction() {
        return this.mReminderAction;
    }

    @Nullable
    Pair<Sidekick.Entry, Integer> getRequiredAliasChange() {
        int i;
        if (this.mReminderAction.getTriggerType() == 2) {
            EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult = this.mReminderAction.getLocation();
            if ((TextUtils.isEmpty(localEcoutezLocalResult.getAddress())) && (localEcoutezLocalResult.hasAlias())) {
                i = localEcoutezLocalResult.getAlias().getAliasType();
            }
        }
        switch (i) {
            default:
                Log.w("EditReminderPresenter", "#getRequiredAliasChange: unknown alias " + i);
                return null;
            case 0:
                return Pair.create(createEditPlaceEntryForAction(17), Integer.valueOf(2131363506));
        }
        return Pair.create(createEditPlaceEntryForAction(18), Integer.valueOf(2131363508));
    }

    public void initUi() {
        this.mUi.updateDateSpinner(this.mSetUpTimeMs);
        setLabel(this.mReminderAction.getLabel());
        setEmbeddedAction(this.mReminderAction.getEmbeddedAction());
        setDateTime(this.mReminderAction.getDateTimeMs(), this.mReminderAction.getSymbolicTime());
        setLocationTriggerType(this.mReminderAction.getLocationTriggerType());
        setLocation(this.mReminderAction.getLocation());
        setTriggerType(this.mReminderAction.getTriggerType());
        setRecurrence(this.mReminderAction.getRecurrence());
        updateHomeWorkAddressInUi();
    }

    public void saveReminder(SimpleCallback<Boolean> paramSimpleCallback) {
        this.mReminderSaver.saveReminder(this.mReminderAction, paramSimpleCallback);
    }

    public void setAliasLocationIfRequired(final SimpleCallback<Boolean> paramSimpleCallback) {
        final Pair localPair = getRequiredAliasChange();
        if ((localPair != null) && (this.mUi != null)) {
            this.mUi.pickLocation(null, ((Integer) localPair.second).intValue(), new SimpleCallback() {
                public void onResult(EcoutezStructuredResponse.EcoutezLocalResult paramAnonymousEcoutezLocalResult) {
                    if (paramAnonymousEcoutezLocalResult != null) {
                        EditReminderPresenter.this.mMyPlacesSaver.save((Sidekick.Entry) localPair.first, paramAnonymousEcoutezLocalResult);
                        paramSimpleCallback.onResult(Boolean.valueOf(true));
                    }
                }
            }, null);
            return;
        }
        paramSimpleCallback.onResult(Boolean.valueOf(true));
    }

    public void setCustomDate() {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(this.mReminderAction.getDateTimeMs());
        if (this.mUi != null) {
            this.mUi.pickDate(localCalendar.get(1), localCalendar.get(2), localCalendar.get(5));
        }
    }

    public void setCustomTime() {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(this.mReminderAction.getDateTimeMs());
        if (this.mUi != null) {
            this.mUi.pickTime(localCalendar.get(11), localCalendar.get(12));
        }
    }

    public void setDate(int paramInt1, int paramInt2, int paramInt3) {
        this.mReminderAction.setDate(paramInt1, paramInt2, paramInt3);
        long l;
        SymbolicTime localSymbolicTime;
        if (this.mUi != null) {
            l = this.mReminderAction.getDateTimeMs();
            localSymbolicTime = this.mReminderAction.getSymbolicTime();
            this.mUi.setCustomDate(l);
            if (localSymbolicTime != SymbolicTime.WEEKEND) {
                if ((!TimeUtilities.isToday(l)) || (!DateTimePickerHelper.isDateTodayAvailable(this.mSetUpTimeMs))) {
                    break label118;
                }
                this.mUi.showDateToday();
            }
        }
        for (; ; ) {
            setSymbolicTime(localSymbolicTime);
            EventRecurrence localEventRecurrence = this.mReminderAction.getRecurrence();
            if (RecurrenceHelper.maybeUpdateNthDayOfWeekForMonthly(l, localEventRecurrence)) {
                this.mUi.setRecurrence(localEventRecurrence);
            }
            return;
            label118:
            if (TimeUtilities.isTomorrow(l)) {
                this.mUi.showDateTomorrow();
            } else {
                this.mUi.showCustomDate();
            }
        }
    }

    public void setDateTime(long paramLong, @Nullable SymbolicTime paramSymbolicTime) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.setTimeInMillis(paramLong);
        this.mReminderAction.setDateTimeMs(paramLong);
        setSymbolicTime(paramSymbolicTime);
        if (paramSymbolicTime == null) {
            setTime(localCalendar.get(11), localCalendar.get(12));
        }
        for (; ; ) {
            setDate(localCalendar);
            return;
            setTime(paramSymbolicTime.defaultHour, 0);
        }
    }

    public void setDateToday() {
        clearSymbolicDay();
        setDate(Calendar.getInstance());
    }

    public void setDateTomorrow() {
        clearSymbolicDay();
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(6, 1);
        setDate(localCalendar);
    }

    public void setDateWeekend() {
        if (DateTimePickerHelper.isDateWeekendAvailable(this.mSetUpTimeMs)) {
            setSymbolicTime(SymbolicTime.WEEKEND);
        }
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(6, (14 - localCalendar.get(7)) % 7);
        setDate(localCalendar);
    }

    void setHomeWork(ActionV2Protos.LocationTrigger paramLocationTrigger) {
        this.mReminderAction.setHomeWork(paramLocationTrigger);
        updateHomeWorkAddressInUi();
    }

    public void setLabel(String paramString) {
        this.mReminderAction.setLabel(paramString);
        if (this.mUi != null) {
            this.mUi.setLabel(paramString);
        }
    }

    public void setLocation(EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult) {
        this.mReminderAction.setLocation(paramEcoutezLocalResult);
        if (this.mUi != null) {
            if (paramEcoutezLocalResult.hasAlias()) {
                AliasProto.Alias localAlias = paramEcoutezLocalResult.getAlias();
                if (localAlias.hasAliasType()) {
                    this.mUi.setLocationAlias(localAlias.getAliasType());
                }
            }
        } else {
            return;
        }
        this.mUi.setCustomLocation(this.mReminderAction.getLocation());
        this.mUi.showCustomLocation();
    }

    public void setLocationAlias(int paramInt) {
        switch (paramInt) {
            default:
                setLocation(this.mReminderAction.getHomeLocation());
                return;
        }
        setLocation(this.mReminderAction.getWorkLocation());
    }

    public void setLocationTriggerType(int paramInt) {
        this.mReminderAction.setLocationTriggerType(paramInt);
        if (this.mUi != null) {
            this.mUi.setLocationTriggerType(paramInt);
        }
    }

    public void setRecurrence(EventRecurrence paramEventRecurrence) {
        this.mReminderAction.setRecurrence(paramEventRecurrence);
        if (this.mUi != null) {
            this.mUi.setRecurrence(paramEventRecurrence);
        }
    }

    void setSetupDateTimeMs(long paramLong) {
        this.mReminderAction.setSetUpTimeMs(paramLong);
    }

    public void setSymbolicTime(SymbolicTime paramSymbolicTime) {
        List localList = DateTimePickerHelper.getAvailableTimes(this.mReminderAction.isSupportedRepeatedReminder(), this.mReminderAction.getDateTimeMs(), this.mSetUpTimeMs);
        this.mReminderAction.setSymbolicTime(paramSymbolicTime, localList);
        if (this.mUi != null) {
            this.mUi.setSymbolicTimeAndAvailableOptions(this.mReminderAction.getSymbolicTime(), localList);
        }
    }

    public void setTime(int paramInt1, int paramInt2) {
        this.mReminderAction.setTime(paramInt1, paramInt2);
        if (this.mUi != null) {
            this.mUi.setCustomTime(this.mReminderAction.getDateTimeMs());
            if (this.mReminderAction.getSymbolicTime() == null) {
                this.mUi.showCustomTime();
            }
        }
    }

    public void setTriggerType(int paramInt) {
        int i = 1;
        if ((paramInt == i) || (paramInt == 2)) {
        }
        for (; ; ) {
            Preconditions.checkArgument(i);
            this.mReminderAction.setTriggerType(paramInt);
            if (this.mUi != null) {
                this.mUi.setTriggerType(paramInt);
            }
            return;
            i = 0;
        }
    }

    public void setUi(EditReminderUi paramEditReminderUi) {
        this.mUi = paramEditReminderUi;
    }

    public void showCustomLocationPicker() {
        EcoutezStructuredResponse.EcoutezLocalResult localEcoutezLocalResult;
        if (this.mUi != null) {
            this.mUi.showCustomLocation();
            localEcoutezLocalResult = this.mReminderAction.getLocation();
            if (localEcoutezLocalResult != null) {
                break label58;
            }
        }
        label58:
        for (String str = null; ; str = localEcoutezLocalResult.getTitle()) {
            DialogInterface.OnCancelListener local3 = new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                    EditReminderPresenter.this.mUi.revertLocationSelection();
                }
            };
            this.mUi.pickLocation(str, 2131363505, this.mCustomLocationCallback, local3);
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.EditReminderPresenter

 * JD-Core Version:    0.7.0.1

 */