package com.embryo.android.shared.util;

import android.util.Log;

import com.google.common.collect.HashMultimap;

public class StateMachine<T extends Enum<T>> {
    private final boolean mDebug;
    private final HashMultimap<T, T> mLegalTransitions;
    private final boolean mStrictMode;
    private final String mTag;
    private final ExtraPreconditions.ThreadCheck mThreadCheck;
    private T mCurrentState;

    private StateMachine(String paramString, T paramT, boolean paramBoolean1, boolean paramBoolean2, HashMultimap<T, T> paramHashMultimap, boolean paramBoolean3) {
        mTag = paramString;
        mStrictMode = paramBoolean1;
        mThreadCheck = ExtraPreconditions.createSameThreadCheck();
        mDebug = paramBoolean3;
        mLegalTransitions = paramHashMultimap;
    }

    public static <T extends Enum<T>> Builder<T> newBuilder(String paramString, T paramT) {
        return new Builder(paramString, paramT);
    }

    private void error(String paramString) {
        if (this.mStrictMode) {
            throw new IllegalStateException(this.mTag + ":  " + paramString);
        }
        Log.e(this.mTag, paramString);
    }

    public void checkIn(T paramT) {
        this.mThreadCheck.check();
        if (this.mCurrentState != paramT) {
            error("Current state is " + this.mCurrentState + ", expected " + paramT);
        }
    }

    public boolean isIn(T paramT) {
        this.mThreadCheck.check();
        return this.mCurrentState == paramT;
    }

    public void moveTo(T paramT) {
        this.mThreadCheck.check();
        if ((!this.mLegalTransitions.containsKey(this.mCurrentState)) || (!this.mLegalTransitions.get(this.mCurrentState).contains(paramT))) {
            error("Illegal transation " + this.mCurrentState + "->" + paramT);
        }
        if (this.mDebug) {
            Log.d(this.mTag, this.mCurrentState + "->" + paramT);
        }
        this.mCurrentState = paramT;
    }

    public boolean notIn(T paramT) {
        this.mThreadCheck.check();
        return !isIn(paramT);
    }

    public String toString() {
        return "Current state=" + this.mCurrentState;
    }

    public static class Builder<T extends Enum<T>> {
        private final T mInitialState;
        private final HashMultimap<T, T> mLegalTransitions = HashMultimap.create();
        private final String mTag;
        private boolean mDebug = false;
        private boolean mOneThread = false;
        private boolean mStrictMode = false;

        public Builder(String paramString, T paramT) {
            this.mInitialState = paramT;
            this.mTag = paramString;
        }

        public Builder<T> addTransition(T paramT1, T paramT2) {
            this.mLegalTransitions.put(paramT1, paramT2);
            return this;
        }

        public StateMachine<T> build() {
            return new StateMachine(this.mTag, this.mInitialState, this.mStrictMode, this.mOneThread, this.mLegalTransitions, this.mDebug);
        }

        public Builder<T> setDebug(boolean paramBoolean) {
            this.mDebug = paramBoolean;
            return this;
        }

        public Builder<T> setSingleThreadOnly(boolean paramBoolean) {
            this.mOneThread = paramBoolean;
            return this;
        }

        public Builder<T> setStrictMode(boolean paramBoolean) {
            this.mStrictMode = paramBoolean;
            return this;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     StateMachine

 * JD-Core Version:    0.7.0.1

 */