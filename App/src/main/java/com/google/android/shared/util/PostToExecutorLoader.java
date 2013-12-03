package com.google.android.shared.util;

import android.net.Uri;

import java.util.concurrent.Executor;

public class PostToExecutorLoader<A>
        implements UriLoader<A> {
    private final Executor mExecutor;
    private final UriLoader<A> mWrapped;

    public PostToExecutorLoader(Executor paramExecutor, UriLoader<A> paramUriLoader) {
        this.mExecutor = paramExecutor;
        this.mWrapped = paramUriLoader;
    }

    public void clearCache() {
        this.mWrapped.clearCache();
    }

    public CancellableNowOrLater<A> load(Uri paramUri) {
        return new PostToExecutorLater(this.mExecutor, this.mWrapped.load(paramUri));
    }

    public boolean supportsUri(Uri paramUri) {
        return this.mWrapped.supportsUri(paramUri);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.PostToExecutorLoader

 * JD-Core Version:    0.7.0.1

 */