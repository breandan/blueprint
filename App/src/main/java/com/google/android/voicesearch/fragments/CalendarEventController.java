package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.AddEventAction;
import com.google.android.voicesearch.util.CalendarTextHelper;

public class CalendarEventController
        extends AbstractCardController<AddEventAction, Ui> {
    private final CalendarTextHelper mCalendarTextHelper;

    public CalendarEventController(CardController paramCardController, CalendarTextHelper paramCalendarTextHelper) {
        super(paramCardController);
        this.mCalendarTextHelper = paramCalendarTextHelper;
    }

    protected void initUi() {
        Ui localUi = (Ui) getUi();
        AddEventAction localAddEventAction = (AddEventAction) getVoiceAction();
        localUi.setTitle(this.mCalendarTextHelper.getDisplaySummary(localAddEventAction.getSummary(), localAddEventAction.getRecognizedAttendees()));
        localUi.setTime(this.mCalendarTextHelper.formatDisplayTime(localAddEventAction.getStartTimeMs(), localAddEventAction.getEndTimeMs(), true));
        localUi.setLocation(localAddEventAction.getLocation());
        if (localAddEventAction.canExecute()) {
            localUi.showCreateEvent();
            return;
        }
        localUi.showEditEvent();
    }

    protected void onExecuteError() {
        getCardController().showToast(2131363540);
    }

    protected void onPreExecute() {
        getCardController().showToast(2131363539);
    }

    public static abstract interface Ui
            extends BaseCardUi, CountDownUi {
        public abstract void setLocation(String paramString);

        public abstract void setTime(String paramString);

        public abstract void setTitle(String paramString);

        public abstract void showCreateEvent();

        public abstract void showEditEvent();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.CalendarEventController

 * JD-Core Version:    0.7.0.1

 */