package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.SetAlarmAction;

import java.text.DateFormat;
import java.util.Calendar;

public class SetAlarmController
        extends AbstractCardController<SetAlarmAction, Ui> {
    private final DateFormat mDateFormat;

    public SetAlarmController(CardController paramCardController, DateFormat paramDateFormat) {
        super(paramCardController);
        this.mDateFormat = paramDateFormat;
    }

    private String getFormattedTime(SetAlarmAction paramSetAlarmAction) {
        if (paramSetAlarmAction.hasTime()) {
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(11, paramSetAlarmAction.getHour());
            localCalendar.set(12, paramSetAlarmAction.getMinute());
            return this.mDateFormat.format(localCalendar.getTime());
        }
        return null;
    }

    public void initUi() {
        SetAlarmAction localSetAlarmAction = (SetAlarmAction) getVoiceAction();
        Ui localUi = (Ui) getUi();
        localUi.setLabel(localSetAlarmAction.getLabel());
        localUi.setTime(getFormattedTime(localSetAlarmAction));
        if (localSetAlarmAction.canExecute()) {
        }
        for (int i = 2131363493; ; i = 2131363494) {
            localUi.setConfirmText(i);
            return;
        }
    }

    public static abstract interface Ui
            extends BaseCardUi, CountDownUi {
        public abstract void setConfirmText(int paramInt);

        public abstract void setLabel(String paramString);

        public abstract void setTime(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SetAlarmController

 * JD-Core Version:    0.7.0.1

 */