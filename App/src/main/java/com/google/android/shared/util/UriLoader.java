package com.google.android.shared.util;

import android.net.Uri;

public abstract interface UriLoader<A> {
    public abstract void clearCache();

    public abstract CancellableNowOrLater<? extends A> load(Uri paramUri);

    public abstract boolean supportsUri(Uri paramUri);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.UriLoader

 * JD-Core Version:    0.7.0.1

 */