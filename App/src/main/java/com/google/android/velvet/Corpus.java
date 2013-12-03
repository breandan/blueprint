package com.google.android.velvet;

import android.net.Uri;
import android.text.TextUtils;

import java.util.Map;

import javax.annotation.Nullable;

public class Corpus {
    @Nullable
    private final Corpora mCorpora;
    private final Uri mIconUri;
    private final String mId;
    private final Uri mNameUri;
    @Nullable
    private final Corpus mParent;
    @Nullable
    private final Map<String, String> mQueryParams;
    private final int mSelectorLayoutId;

    public Corpus() {
        this(null, null, null, 0, null, null, null);
    }

    public Corpus(String paramString, Uri paramUri1, Uri paramUri2, int paramInt, @Nullable Corpus paramCorpus, @Nullable Corpora paramCorpora, @Nullable Map<String, String> paramMap) {
        this.mId = paramString;
        this.mIconUri = paramUri1;
        this.mNameUri = paramUri2;
        this.mSelectorLayoutId = paramInt;
        this.mParent = paramCorpus;
        this.mCorpora = paramCorpora;
        this.mQueryParams = paramMap;
    }

    public final boolean equals(Object paramObject) {
        boolean bool1 = paramObject instanceof Corpus;
        boolean bool2 = false;
        if (bool1) {
            Corpus localCorpus = (Corpus) paramObject;
            bool2 = false;
            if (this != null) {
                bool2 = false;
                if (localCorpus != null) {
                    boolean bool3 = TextUtils.equals(getIdentifier(), localCorpus.getIdentifier());
                    bool2 = false;
                    if (bool3) {
                        bool2 = true;
                    }
                }
            }
        }
        return bool2;
    }

    public final Uri getIconUri() {
        return this.mIconUri;
    }

    public final String getIdentifier() {
        return this.mId;
    }

    public final Uri getNameUri() {
        return this.mNameUri;
    }

    @Nullable
    public final Corpus getParent() {
        return this.mParent;
    }

    @Nullable
    public final Map<String, String> getQueryParams() {
        return this.mQueryParams;
    }

    public final int getSelectorLayoutId() {
        return this.mSelectorLayoutId;
    }

    public final int hashCode() {
        return this.mId.hashCode();
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isTopLevelWebCorpus() {
        return this.mId.equals("web");
    }

    public String toString() {
        return "Corpus[" + this.mId + "]";
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.Corpus

 * JD-Core Version:    0.7.0.1

 */