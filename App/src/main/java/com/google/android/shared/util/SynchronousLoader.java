package com.google.android.shared.util;

import android.net.Uri;

public abstract class SynchronousLoader<A>
        implements UriLoader<A> {
    public final CancellableNowOrLater<A> load(Uri paramUri) {
        return Now.returnThis(loadNow(paramUri));
    }

    public abstract A loadNow(Uri paramUri);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.SynchronousLoader

 * JD-Core Version:    0.7.0.1

 */