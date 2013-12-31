package com.embryo.android.speech.network.request;

import java.util.concurrent.Callable;

import javax.annotation.Nullable;

/**
 * Created by breandan on 12/31/13.
 */
public abstract class BaseRequestBuilderTask<T>
        implements Callable<T> {
    private final String mTag;

    protected BaseRequestBuilderTask(String paramString) {
        this.mTag = paramString;
    }

    @Nullable
    protected abstract T build();

    @Nullable
    public T call() {
        return build();
    }
}

