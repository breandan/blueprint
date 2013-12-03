package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class AddEventAction
        implements VoiceAction {
    public static final Parcelable.Creator<AddEventAction> CREATOR = new Parcelable.Creator() {
        public AddEventAction createFromParcel(Parcel paramAnonymousParcel) {
            int i = 1;
            ClassLoader localClassLoader = getClass().getClassLoader();
            String str1 = paramAnonymousParcel.readString();
            String str2 = paramAnonymousParcel.readString();
            ArrayList localArrayList1 = paramAnonymousParcel.readArrayList(localClassLoader);
            ArrayList localArrayList2 = paramAnonymousParcel.readArrayList(localClassLoader);
            long l = paramAnonymousParcel.readLong();
            if (paramAnonymousParcel.readByte() == i) {
            }
            for (; ; ) {
                return new AddEventAction(str1, str2, localArrayList1, localArrayList2, l, i, paramAnonymousParcel.readLong());
                int j = 0;
            }
        }

        public AddEventAction[] newArray(int paramAnonymousInt) {
            return new AddEventAction[paramAnonymousInt];
        }
    };
    private final long mEndTimeMs;
    @Nullable
    private final String mLocation;
    private final List<String> mRecognizedAttendees;
    private final List<CalendarHelper.Reminder> mReminders;
    private final long mStartTimeMs;
    private final boolean mStartTimeSpecified;
    @Nullable
    private final String mSummary;

    public AddEventAction(String paramString1, String paramString2, List<String> paramList, List<CalendarHelper.Reminder> paramList1, long paramLong1, boolean paramBoolean, long paramLong2) {
        this.mSummary = paramString1;
        this.mLocation = paramString2;
        this.mRecognizedAttendees = paramList;
        this.mReminders = paramList1;
        this.mStartTimeMs = paramLong1;
        this.mStartTimeSpecified = paramBoolean;
        this.mEndTimeMs = paramLong2;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return (this.mStartTimeSpecified) && (!TextUtils.isEmpty(this.mSummary));
    }

    public int describeContents() {
        return 0;
    }

    public long getEndTimeMs() {
        return this.mEndTimeMs;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public List<String> getRecognizedAttendees() {
        return this.mRecognizedAttendees;
    }

    public List<CalendarHelper.Reminder> getReminders() {
        return this.mReminders;
    }

    public long getStartTimeMs() {
        return this.mStartTimeMs;
    }

    public String getSummary() {
        return this.mSummary;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeString(this.mSummary);
        paramParcel.writeString(this.mLocation);
        paramParcel.writeList(this.mRecognizedAttendees);
        paramParcel.writeList(this.mReminders);
        paramParcel.writeLong(this.mStartTimeMs);
        if (this.mStartTimeSpecified) {
        }
        for (int i = 1; ; i = 0) {
            paramParcel.writeByte((byte) i);
            paramParcel.writeLong(this.mEndTimeMs);
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.AddEventAction

 * JD-Core Version:    0.7.0.1

 */