package com.google.android.velvet.presenter.inappwebpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.search.core.google.SearchUrlHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.Iterator;

import javax.annotation.Nullable;

public final class Request
        implements Parcelable {
    public static final Parcelable.Creator<Request> CREATOR = new Creator(null);
    private final ImmutableSet<String> mInitialInAppUriPatterns;
    private final Uri mUri;

    public Request(Uri paramUri, ImmutableSet<String> paramImmutableSet) {
        this.mUri = ((Uri) Preconditions.checkNotNull(paramUri));
        this.mInitialInAppUriPatterns = ((ImmutableSet) Preconditions.checkNotNull(paramImmutableSet));
    }

    @Nullable
    public static Request forIntent(Intent paramIntent) {
        if (paramIntent.getData() != null) {
            return new Request(paramIntent.getData(), ImmutableSet.of());
        }
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(@Nullable Object paramObject) {
        boolean bool1 = paramObject instanceof Request;
        boolean bool2 = false;
        if (bool1) {
            Request localRequest = (Request) paramObject;
            boolean bool3 = Objects.equal(this.mUri, localRequest.mUri);
            bool2 = false;
            if (bool3) {
                boolean bool4 = Objects.equal(this.mInitialInAppUriPatterns, localRequest.mInitialInAppUriPatterns);
                bool2 = false;
                if (bool4) {
                    bool2 = true;
                }
            }
        }
        return bool2;
    }

    public ImmutableSet<String> getInitialInAppUriPatterns() {
        return this.mInitialInAppUriPatterns;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public int hashCode() {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = this.mUri;
        arrayOfObject[1] = this.mInitialInAppUriPatterns;
        return Objects.hashCode(arrayOfObject);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("mUri", SearchUrlHelper.safeLogUrl(this.mUri)).add("mInitialInAppUriPatterns", this.mInitialInAppUriPatterns).toString();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(this.mUri, paramInt);
        paramParcel.writeInt(this.mInitialInAppUriPatterns.size());
        Iterator localIterator = this.mInitialInAppUriPatterns.iterator();
        while (localIterator.hasNext()) {
            paramParcel.writeString((String) localIterator.next());
        }
    }

    private static class Creator
            implements Parcelable.Creator<Request> {
        public Request createFromParcel(Parcel paramParcel) {
            Uri localUri = (Uri) paramParcel.readParcelable(null);
            int i = paramParcel.readInt();
            ImmutableSet.Builder localBuilder = ImmutableSet.builder();
            for (int j = 0; j < i; j++) {
                localBuilder.add(paramParcel.readString());
            }
            return new Request(localUri, localBuilder.build());
        }

        public Request[] newArray(int paramInt) {
            return new Request[paramInt];
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.Request

 * JD-Core Version:    0.7.0.1

 */