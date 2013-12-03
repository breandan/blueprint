package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;

import com.google.android.shared.util.IntentStarter;
import com.google.android.voicesearch.fragments.action.AddEventAction;
import com.google.android.voicesearch.util.CalendarHelper;
import com.google.android.voicesearch.util.CalendarTextHelper;

public class AddEventActionExecutor
        extends IntentActionExecutor<AddEventAction> {
    private final CalendarHelper mCalendarHelper;
    private final CalendarTextHelper mCalendarTextHelper;

    public AddEventActionExecutor(IntentStarter paramIntentStarter, CalendarHelper paramCalendarHelper, CalendarTextHelper paramCalendarTextHelper) {
        super(paramIntentStarter);
        this.mCalendarHelper = paramCalendarHelper;
        this.mCalendarTextHelper = paramCalendarTextHelper;
    }

    private String getDisplaySummary(AddEventAction paramAddEventAction) {
        return this.mCalendarTextHelper.getDisplaySummary(paramAddEventAction.getSummary(), paramAddEventAction.getRecognizedAttendees());
    }

    public boolean execute(AddEventAction paramAddEventAction) {
        if (this.mCalendarHelper.addEvent(null, null, getDisplaySummary(paramAddEventAction), paramAddEventAction.getLocation(), paramAddEventAction.getStartTimeMs(), paramAddEventAction.getEndTimeMs(), null, paramAddEventAction.getReminders())) {
            return true;
        }
        return super.execute(paramAddEventAction);
    }

    protected Intent[] getExecuteIntents(AddEventAction paramAddEventAction) {
        return getOpenExternalAppIntents(paramAddEventAction);
    }

    protected Intent[] getOpenExternalAppIntents(AddEventAction paramAddEventAction) {
        Intent[] arrayOfIntent = new Intent[1];
        arrayOfIntent[0] = this.mCalendarHelper.createAddEventIntent(getDisplaySummary(paramAddEventAction), paramAddEventAction.getLocation(), paramAddEventAction.getStartTimeMs(), paramAddEventAction.getEndTimeMs(), null);
        return arrayOfIntent;
    }

    protected Intent[] getProberIntents(AddEventAction paramAddEventAction) {
        Intent[] arrayOfIntent = new Intent[1];
        arrayOfIntent[0] = CalendarHelper.createViewEventIntent(999999L);
        return arrayOfIntent;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.AddEventActionExecutor

 * JD-Core Version:    0.7.0.1

 */