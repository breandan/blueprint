package com.google.android.velvet.presenter.inappwebpage;

import android.net.Uri;

import com.google.android.search.core.prefetch.WebPage;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;

import java.io.Closeable;

import javax.annotation.Nullable;

public class Content {
    private final Uri mResolvedUri;
    @Nullable
    private final Closeable mSource;
    private final WebPage mWebPage;

    public Content(Uri paramUri, WebPage paramWebPage, @Nullable Closeable paramCloseable) {
        this.mResolvedUri = ((Uri) Preconditions.checkNotNull(paramUri));
        this.mWebPage = ((WebPage) Preconditions.checkNotNull(paramWebPage));
        this.mSource = paramCloseable;
    }

    public Uri getResolvedUri() {
        return this.mResolvedUri;
    }

    public WebPage getWebPage() {
        return this.mWebPage;
    }

    public void release() {
        Closeables.closeQuietly(this.mSource);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.Content

 * JD-Core Version:    0.7.0.1

 */