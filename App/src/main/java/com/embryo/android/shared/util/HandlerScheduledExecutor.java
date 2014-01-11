/**
 * Generated by smali2java 1.0.0.558
 * Copyright (C) 2013 Hensence.com
 */

package com.embryo.android.shared.util;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

import com.google.common.collect.Maps;

import java.util.Map;

public class HandlerScheduledExecutor extends HandlerExecutor implements ScheduledSingleThreadedExecutor {
    private final MessageQueue mQueue;
    private final Map<Runnable, MessageQueue.IdleHandler> mQueuedIdleHandlers;

    public HandlerScheduledExecutor(Handler handler, MessageQueue queue) {
        super(handler);
        mQueue = queue;
        mQueuedIdleHandlers = Maps.newHashMap();
    }

    public void cancelExecute(Runnable command) {
        super.cancelExecute(command);
        mQueue.removeIdleHandler(dequeueIdleHandler(command));
    }

    public void executeDelayed(Runnable command, long delayMillis) {
        mHandler.postDelayed(command, delayMillis);
    }

    public void executeOnIdle(Runnable command) {
        HandlerScheduledExecutor.ExecuteOnIdle h = new HandlerScheduledExecutor.ExecuteOnIdle(this, command);
        synchronized (mQueuedIdleHandlers) {
            mQueuedIdleHandlers.put(command, h);
            mQueue.addIdleHandler(h);
        }
    }

    public boolean isThisThread() {
        return (Looper.myLooper() == mHandler.getLooper());
    }

    public Handler getHandler() {
        return mHandler;
    }

    private MessageQueue.IdleHandler dequeueIdleHandler(Runnable command) {
        synchronized (mQueuedIdleHandlers) {
            if (mQueuedIdleHandlers.containsKey(command)) {
                return mQueuedIdleHandlers.remove(command);
            }
        }
        return null;
    }

    class ExecuteOnIdle implements MessageQueue.IdleHandler {
        private final Runnable mCommand;

        public ExecuteOnIdle(HandlerScheduledExecutor p1, Runnable command) {
            mCommand = command;
        }

        public boolean queueIdle() {
            execute(mCommand);
            return false;
        }
    }
}
