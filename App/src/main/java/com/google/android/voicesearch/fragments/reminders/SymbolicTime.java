package com.google.android.voicesearch.fragments.reminders;

import com.google.android.search.core.Feature;

public enum SymbolicTime {
    public final int actionV2Symbol;
    public final int defaultHour;
    public final int defaultResId;
    public final int sameDayResId;
    public final int sameWeekResId;

    static {
        AFTERNOON = new SymbolicTime("AFTERNOON", 1, 2131363523, 2131363527, 2131363531, 13, 1);
        EVENING = new SymbolicTime("EVENING", 2, 2131363524, 2131363528, 2131363532, 18, 2);
        NIGHT = new SymbolicTime("NIGHT", 3, 2131363525, 2131363529, 2131363533, 20, 3);
        TIME_UNSPECIFIED = new SymbolicTime("TIME_UNSPECIFIED", 4, 0, 0, 0, 9, 4);
        WEEKEND = new SymbolicTime("WEEKEND", 5, 0, 0, 0, 9, 5);
        SymbolicTime[] arrayOfSymbolicTime = new SymbolicTime[6];
        arrayOfSymbolicTime[0] = MORNING;
        arrayOfSymbolicTime[1] = AFTERNOON;
        arrayOfSymbolicTime[2] = EVENING;
        arrayOfSymbolicTime[3] = NIGHT;
        arrayOfSymbolicTime[4] = TIME_UNSPECIFIED;
        arrayOfSymbolicTime[5] = WEEKEND;
        $VALUES = arrayOfSymbolicTime;
    }

    private SymbolicTime(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        this.sameDayResId = paramInt1;
        this.sameWeekResId = paramInt2;
        this.defaultResId = paramInt3;
        this.defaultHour = paramInt4;
        this.actionV2Symbol = paramInt5;
    }

    public static SymbolicTime fromActionV2Symbol(int paramInt) {
        for (SymbolicTime localSymbolicTime :) {
            if ((localSymbolicTime.actionV2Symbol == paramInt) && (localSymbolicTime.isEnabled())) {
                return localSymbolicTime;
            }
        }
        return null;
    }

    private boolean isEnabled() {
        return (this != WEEKEND) || (Feature.REMINDERS_WEEKEND.isEnabled());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.reminders.SymbolicTime

 * JD-Core Version:    0.7.0.1

 */