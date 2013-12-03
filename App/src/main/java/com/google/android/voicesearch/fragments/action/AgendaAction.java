package com.google.android.voicesearch.fragments.action;

import android.os.Parcel;

import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.common.collect.Lists;
import com.google.majel.proto.CalendarProtos.AgendaItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AgendaAction
        implements VoiceAction {
    public static final Parcelable.Creator<AgendaAction> CREATOR = new Parcelable.Creator() {
        public AgendaAction createFromParcel(Parcel paramAnonymousParcel) {
            int i = paramAnonymousParcel.readInt();
            ArrayList localArrayList = Lists.newArrayListWithCapacity(i);
            for (int j = 0; j < i; j++) {
                localArrayList.add(ProtoParcelable.readProtoFromParcel(paramAnonymousParcel, CalendarProtos.AgendaItem.class));
            }
            boolean bool1;
            int k;
            long l1;
            long l2;
            if (paramAnonymousParcel.readByte() != 0) {
                bool1 = true;
                k = paramAnonymousParcel.readInt();
                l1 = paramAnonymousParcel.readLong();
                l2 = paramAnonymousParcel.readLong();
                if (paramAnonymousParcel.readByte() == 0) {
                    break label101;
                }
            }
            label101:
            for (boolean bool2 = true; ; bool2 = false) {
                return new AgendaAction(localArrayList, bool1, k, l1, l2, bool2);
                bool1 = false;
                break;
            }
        }

        public AgendaAction[] newArray(int paramAnonymousInt) {
            return new AgendaAction[paramAnonymousInt];
        }
    };
    private final List<CalendarProtos.AgendaItem> mAgendaItems;
    private final boolean mAutoExpandFirst;
    private final int mBatchSize;
    private final long mEndTime;
    private final boolean mSortReverse;
    private final long mStartTime;

    public AgendaAction(List<CalendarProtos.AgendaItem> paramList, boolean paramBoolean1, int paramInt, long paramLong1, long paramLong2, boolean paramBoolean2) {
        this.mAgendaItems = paramList;
        this.mAutoExpandFirst = paramBoolean1;
        this.mBatchSize = paramInt;
        this.mStartTime = paramLong1;
        this.mEndTime = paramLong2;
        this.mSortReverse = paramBoolean2;
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public List<CalendarProtos.AgendaItem> getAgendaItems() {
        return this.mAgendaItems;
    }

    public int getBatchSize() {
        return this.mBatchSize;
    }

    public long getEndTime() {
        return this.mEndTime;
    }

    public boolean getSortReverse() {
        return this.mSortReverse;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public boolean shouldAutoExpandFirst() {
        return this.mAutoExpandFirst;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        int i = 1;
        paramParcel.writeInt(this.mAgendaItems.size());
        Iterator localIterator = this.mAgendaItems.iterator();
        while (localIterator.hasNext()) {
            ProtoParcelable.writeProtoToParcel((CalendarProtos.AgendaItem) localIterator.next(), paramParcel);
        }
        int j;
        if (this.mAutoExpandFirst) {
            j = i;
            paramParcel.writeByte((byte) j);
            paramParcel.writeInt(this.mBatchSize);
            paramParcel.writeLong(this.mStartTime);
            paramParcel.writeLong(this.mEndTime);
            if (!this.mSortReverse) {
                break label114;
            }
        }
        for (; ; ) {
            paramParcel.writeByte((byte) i);
            return;
            j = 0;
            break;
            label114:
            i = 0;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.AgendaAction

 * JD-Core Version:    0.7.0.1

 */