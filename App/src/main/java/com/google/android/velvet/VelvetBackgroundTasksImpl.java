package com.google.android.velvet;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.search.core.SearchConfig;
import com.google.android.search.core.SearchSettings;
import com.google.android.search.core.util.AlarmHelper;
import com.google.android.shared.util.Clock;
import com.google.android.sidekick.main.inject.PendingIntentFactory;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

public class VelvetBackgroundTasksImpl
        implements VelvetBackgroundTasks {
    static final long FORCE_RUN_NOW_WITH_INTERRUPT = -1L;
    static final long UNSET;
    private final AlarmHelper mAlarmHelper;
    private final Context mAppContext;
    private final Executor mBgExecutor;
    private final Clock mClock;
    private final SearchConfig mConfig;
    private final VelvetFactory mFactory;
    private final Object mLock;
    private final PendingIntentFactory mPendingIntentFactory;
    private final Map<String, FutureTask<Void>> mReadyTasks;
    private final Runnable mScheduleTasksRunnable = new Runnable() {
        public void run() {
            VelvetBackgroundTasksImpl.this.scheduleTasks();
        }
    };
    private boolean mServiceRunning;
    private final SearchSettings mSettings;
    private final Map<String, TaskParams> mTasks;

    public VelvetBackgroundTasksImpl(VelvetServices paramVelvetServices, Clock paramClock, SearchConfig paramSearchConfig, SearchSettings paramSearchSettings, Executor paramExecutor, VelvetFactory paramVelvetFactory, AlarmHelper paramAlarmHelper, PendingIntentFactory paramPendingIntentFactory, Context paramContext) {
        this.mClock = paramClock;
        this.mConfig = paramSearchConfig;
        this.mSettings = paramSearchSettings;
        this.mBgExecutor = paramExecutor;
        this.mFactory = paramVelvetFactory;
        this.mAlarmHelper = paramAlarmHelper;
        this.mPendingIntentFactory = paramPendingIntentFactory;
        this.mAppContext = paramContext;
        this.mLock = new Object();
        this.mTasks = Maps.newHashMap();
        this.mReadyTasks = Maps.newHashMap();
        loadTasks();
    }

    private void forceRunInternal(String paramString, long paramLong) {
        synchronized (this.mLock) {
            ((TaskParams) Preconditions.checkNotNull(this.mTasks.get(paramString))).setForcedRun(paramLong);
            this.mBgExecutor.execute(this.mScheduleTasksRunnable);
            return;
        }
    }

    private static long getEarliest(long paramLong1, long paramLong2) {
        if (paramLong1 == 0L) {
            return paramLong2;
        }
        if (paramLong2 == 0L) {
            return paramLong1;
        }
        return Math.min(paramLong1, paramLong2);
    }

    private void loadTasks() {
        String[] arrayOfString1 = this.mConfig.getBackgroundTasks();
        String[] arrayOfString2 = this.mConfig.getBackgroundTaskMinPeriods();
        int i = 0;
        String str;
        long l;
        if (i < arrayOfString1.length) {
            str = arrayOfString1[i];
            l = 0L;
        }
        for (int j = 0; ; j += 2) {
            try {
                if (j < -1 + arrayOfString2.length) {
                    if (!arrayOfString2[j].equals(str)) {
                        continue;
                    }
                    l = parseMinPeriod(arrayOfString2[(j + 1)]);
                    continue;
                }
                TaskParams localTaskParams = new TaskParams(str, l, null);
                this.mTasks.put(str, localTaskParams);
                i++;
            } catch (NumberFormatException localNumberFormatException) {
                for (; ; ) {
                    Log.w("Velvet.VelvetBackgroundTasksImpl", "Exception parsing min period of " + str + ": ", localNumberFormatException);
                }
            }
            break;
            return;
        }
    }

    private long parseMinPeriod(String paramString)
            throws NumberFormatException {
        if ((paramString == null) || (paramString.length() < 2)) {
            throw new NumberFormatException();
        }
        long l = Long.parseLong(paramString.substring(0, -1 + paramString.length()));
        switch (Character.toLowerCase(paramString.charAt(-1 + paramString.length()))) {
            default:
                throw new NumberFormatException();
            case 'm':
                return 60000L * l;
            case 'h':
                return 3600000L * l;
        }
        return 86400000L * l;
    }

    private void scheduleAlarm(long paramLong, Intent paramIntent) {
        PendingIntent localPendingIntent = this.mPendingIntentFactory.getService(0, paramIntent, 0);
        this.mAlarmHelper.cancel(localPendingIntent);
        this.mAlarmHelper.setInexact(0, paramLong, localPendingIntent);
    }

    private void serviceTasks() {
        boolean bool1 = true;
        for (; ; ) {
            boolean bool2;
            HashMap localHashMap;
            synchronized (this.mLock) {
                if (!this.mServiceRunning) {
                    bool2 = bool1;
                    Preconditions.checkState(bool2);
                    this.mServiceRunning = true;
                    localHashMap = Maps.newHashMap();
                    ArrayList localArrayList = Lists.newArrayList();
                    startReadyTasks(localHashMap);
                    if (cancelPreemptedTasks(localHashMap, localArrayList)) {
                        continue;
                    }
                    waitForTasks();
                    retireCompletedTasks(localHashMap, localArrayList);
                }
            }
            synchronized (this.mLock) {
                if (!scheduleTasks()) {
                    if (localHashMap.size() == 0) {
                    }
                    for (; ; ) {
                        Preconditions.checkState(bool1);
                        this.mServiceRunning = false;
                        return;
                        bool2 = false;
                        break;
                        localObject2 =finally;
                        throw localObject2;
                        bool1 = false;
                    }
                }
            }
        }
    }

    private void startService(Intent paramIntent) {
        this.mAppContext.startService(paramIntent);
    }

    private void waitForTasks() {
        synchronized (this.mLock) {
            if (!this.mReadyTasks.isEmpty()) {
                Iterator localIterator = this.mReadyTasks.entrySet().iterator();
                while (localIterator.hasNext()) {
                    if (((FutureTask) ((Map.Entry) localIterator.next()).getValue()).isDone()) {
                        return;
                    }
                }
            }
        }
        try {
            this.mLock.wait();
            label76:
            return;
            localObject2 =finally;
            throw localObject2;
        } catch (InterruptedException localInterruptedException) {
            break label76;
        }
    }

    boolean cancelPreemptedTasks(Map<String, FutureTask<Void>> paramMap, List<String> paramList) {
        boolean bool = false;
        Iterator localIterator1 = paramMap.entrySet().iterator();
        while (localIterator1.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator1.next();
            String str = (String) localEntry.getKey();
            FutureTask localFutureTask1 = (FutureTask) localEntry.getValue();
            synchronized (this.mLock) {
                FutureTask localFutureTask2 = (FutureTask) this.mReadyTasks.get(str);
                if ((localFutureTask2 != null) && (localFutureTask2 != localFutureTask1)) {
                    localFutureTask1.cancel(true);
                    paramList.add(str);
                    bool = true;
                }
            }
        }
        if (bool) {
            Iterator localIterator2 = paramList.iterator();
            while (localIterator2.hasNext()) {
                paramMap.remove((String) localIterator2.next());
            }
            paramList.clear();
        }
        return bool;
    }

    public void forceRun(String paramString, long paramLong) {
        forceRunInternal(paramString, paramLong + this.mClock.currentTimeMillis());
    }

    public void forceRunInterruptingOngoing(String paramString) {
        forceRunInternal(paramString, -1L);
    }

    Map<String, FutureTask<Void>> getReadyTasksForTesting() {
        return this.mReadyTasks;
    }

    public void maybeStartTasks() {
        this.mBgExecutor.execute(this.mScheduleTasksRunnable);
    }

    public void notifyUiLaunched() {
        this.mSettings.setLastApplicationLaunch(this.mClock.currentTimeMillis());
    }

    void retireCompletedTasks(Map<String, FutureTask<Void>> paramMap, List<String> paramList) {
        synchronized (this.mLock) {
            Iterator localIterator1 = this.mReadyTasks.entrySet().iterator();
            while (localIterator1.hasNext()) {
                Map.Entry localEntry = (Map.Entry) localIterator1.next();
                String str2 = (String) localEntry.getKey();
                if (((FutureTask) localEntry.getValue()).isDone()) {
                    paramList.add(str2);
                }
            }
        }
        Iterator localIterator2 = paramList.iterator();
        String str1;
        if (localIterator2.hasNext()) {
            str1 = (String) localIterator2.next();
            if (this.mReadyTasks.get(str1) != paramMap.get(str1)) {
                break label222;
            }
        }
        label222:
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            Preconditions.checkState(((FutureTask) this.mReadyTasks.get(str1)).isDone());
            this.mReadyTasks.remove(str1);
            paramMap.remove(str1);
            ((TaskParams) this.mTasks.get(str1)).commitParamsOnTaskCompletion();
            break;
            paramList.clear();
            return;
        }
    }

    boolean scheduleTasks() {
        Object localObject1 = this.mLock;
        long l1 = 0L;
        long l3;
        TaskParams localTaskParams;
        for (long l2 = 0L; ; l2 = getEarliest(l2, localTaskParams.mForcedRunTimeMs)) {
            try {
                l3 = this.mClock.currentTimeMillis();
                Iterator localIterator = this.mTasks.entrySet().iterator();
                for (; ; ) {
                    if (!localIterator.hasNext()) {
                        break label210;
                    }
                    Map.Entry localEntry = (Map.Entry) localIterator.next();
                    String str = (String) localEntry.getKey();
                    localTaskParams = (TaskParams) localEntry.getValue();
                    if (!localTaskParams.isReadyToRun(l3)) {
                        break;
                    }
                    if ((localTaskParams.mForcedRunTimeMs == -1L) || (!this.mReadyTasks.containsKey(str))) {
                        Map localMap = this.mReadyTasks;
                        NotifyOnDoneFutureTask localNotifyOnDoneFutureTask = new NotifyOnDoneFutureTask(str, this.mFactory.createBackgroundTask(str, localTaskParams.isForcedRun(l3)), null);
                        localMap.put(str, localNotifyOnDoneFutureTask);
                        localTaskParams.updateParamsOnTaskStart(l3);
                    }
                }
                l1 = getEarliest(l1, localTaskParams.mEarliestNextRunTimeMs);
            } finally {
            }
        }
        label210:
        if (this.mReadyTasks.size() != 0) {
        }
        for (boolean bool = true; ; bool = false) {
            if (bool) {
                if (this.mServiceRunning) {
                    this.mLock.notifyAll();
                }
            }
            for (; ; ) {
                return bool;
                Intent localIntent1 = new Intent(this.mAppContext, Service.class);
                startService(localIntent1);
                continue;
                long l4 = getEarliest(throttleAlarm(l3, l1), l2);
                if (l4 != 0L) {
                    Intent localIntent2 = new Intent(this.mAppContext, Service.class);
                    scheduleAlarm(l4, localIntent2);
                }
            }
        }
    }

    void startReadyTasks(Map<String, FutureTask<Void>> paramMap) {
        synchronized (this.mLock) {
            this.mClock.currentTimeMillis();
            Iterator localIterator = this.mReadyTasks.entrySet().iterator();
            while (localIterator.hasNext()) {
                Map.Entry localEntry = (Map.Entry) localIterator.next();
                String str = (String) localEntry.getKey();
                FutureTask localFutureTask = (FutureTask) localEntry.getValue();
                if (!paramMap.containsKey(str)) {
                    paramMap.put(str, localFutureTask);
                    this.mBgExecutor.execute(localFutureTask);
                }
            }
        }
    }

    long throttleAlarm(long paramLong1, long paramLong2) {
        if (paramLong2 == 0L) {
            return paramLong2;
        }
        long l1 = this.mConfig.getBackgroundTasksPeriodMs();
        long l2 = (paramLong1 - this.mSettings.getLastApplicationLaunch()) / 86400000L - this.mConfig.getBackgroundTasksPeriodOfDaysBeforeBackoff();
        if (l2 > 0L) {
            l1 = Math.min(this.mConfig.getMaxBackgroundTasksPeriodMs(), l1 + this.mConfig.getBackgroundTasksPeriodDaysOfDisuseSquaredMultipleMs() * (l2 * l2));
        }
        return Math.max(paramLong2, paramLong1 + l1);
    }

    private class NotifyOnDoneFutureTask
            extends FutureTask<Void> {
        private final String mTaskName;

        private NotifyOnDoneFutureTask(Callable<Void> paramCallable) {
            super();
            this.mTaskName = paramCallable;
        }

        public void done() {
            super.done();
            synchronized (VelvetBackgroundTasksImpl.this.mLock) {
                VelvetBackgroundTasksImpl.this.mLock.notifyAll();
                return;
            }
        }

        protected void setException(Throwable paramThrowable) {
            super.setException(paramThrowable);
            Log.e("Velvet.VelvetBackgroundTasksImpl", "Background task " + this.mTaskName + " failed", paramThrowable);
        }

        public String toString() {
            return "NotifyOnDoneFutureTask[" + this.mTaskName + "]";
        }
    }

    public static class Service
            extends IntentService {
        public Service() {
            super();
            setIntentRedelivery(true);
        }

        public void onHandleIntent(Intent paramIntent) {
            ((VelvetBackgroundTasksImpl) VelvetServices.get().getCoreServices().getBackgroundTasks()).serviceTasks();
        }
    }

    private class TaskParams {
        private long mEarliestNextRunTimeMs;
        private long mForcedRunTimeMs;
        private final long mMinPeriodMs;
        private final String mTaskName;

        private TaskParams(String paramString, long paramLong) {
            this.mTaskName = paramString;
            this.mMinPeriodMs = paramLong;
            this.mEarliestNextRunTimeMs = VelvetBackgroundTasksImpl.this.mSettings.getBackgroundTaskEarliestNextRun(paramString);
            this.mForcedRunTimeMs = VelvetBackgroundTasksImpl.this.mSettings.getBackgroundTaskForcedRun(paramString);
            maybeFixupInvalidTimes();
        }

        private void commitParamsOnTaskCompletion() {
            VelvetBackgroundTasksImpl.this.mSettings.setBackgroundTaskEarliestNextRun(this.mTaskName, this.mEarliestNextRunTimeMs);
            if (this.mForcedRunTimeMs == 0L) {
                VelvetBackgroundTasksImpl.this.mSettings.setBackgroundTaskForcedRun(this.mTaskName, 0L);
            }
        }

        private boolean isForcedRun(long paramLong) {
            return (this.mForcedRunTimeMs != 0L) && (this.mForcedRunTimeMs <= paramLong);
        }

        private boolean isReadyToRun(long paramLong) {
            return ((this.mEarliestNextRunTimeMs != 0L) && (this.mEarliestNextRunTimeMs <= paramLong)) || ((this.mForcedRunTimeMs != 0L) && (this.mForcedRunTimeMs <= paramLong));
        }

        private void maybeFixupInvalidTimes() {
            long l = VelvetBackgroundTasksImpl.this.mClock.currentTimeMillis();
            if ((this.mMinPeriodMs != 0L) && ((this.mEarliestNextRunTimeMs == 0L) || (this.mEarliestNextRunTimeMs > l + this.mMinPeriodMs))) {
                this.mEarliestNextRunTimeMs = (l + this.mMinPeriodMs);
                VelvetBackgroundTasksImpl.this.mSettings.setBackgroundTaskEarliestNextRun(this.mTaskName, this.mEarliestNextRunTimeMs);
            }
            if (this.mForcedRunTimeMs > l + this.mMinPeriodMs) {
                Log.d("Velvet.VelvetBackgroundTasksImpl", "Invalid forced run for " + this + " set it to " + l);
                this.mForcedRunTimeMs = l;
                VelvetBackgroundTasksImpl.this.mSettings.setBackgroundTaskForcedRun(this.mTaskName, l);
            }
        }

        private void setForcedRun(long paramLong) {
            this.mForcedRunTimeMs = paramLong;
            VelvetBackgroundTasksImpl.this.mSettings.setBackgroundTaskForcedRun(this.mTaskName, paramLong);
        }

        private void updateParamsOnTaskStart(long paramLong) {
            if (this.mMinPeriodMs != 0L) {
                this.mEarliestNextRunTimeMs = (paramLong + this.mMinPeriodMs);
            }
            this.mForcedRunTimeMs = 0L;
        }

        public String toString() {
            return super.toString();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.VelvetBackgroundTasksImpl

 * JD-Core Version:    0.7.0.1

 */