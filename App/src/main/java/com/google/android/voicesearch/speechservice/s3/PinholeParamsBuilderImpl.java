package com.google.android.voicesearch.speechservice.s3;

import android.net.Uri;
import android.util.Log;

import com.google.android.search.core.Feature;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.google.SearchUrlHelper.Builder;
import com.google.android.search.core.util.UriRequest;
import com.google.android.search.shared.api.Query;
import com.google.android.speech.network.request.BaseRequestBuilderTask;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.speech.s3.PinholeStream.PinholeCgiParam;
import com.google.speech.s3.PinholeStream.PinholeHeader;
import com.google.speech.s3.PinholeStream.PinholeParams;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

public class PinholeParamsBuilderImpl
        implements PinholeParamsBuilder {
    private Query mQuery;
    private final SearchUrlHelper mSearchUrlHelper;
    private final Supplier<String> mUserAgentSupplier;

    public PinholeParamsBuilderImpl(SearchUrlHelper paramSearchUrlHelper, Supplier<String> paramSupplier) {
        this.mSearchUrlHelper = paramSearchUrlHelper;
        this.mUserAgentSupplier = paramSupplier;
    }

    PinholeStream.PinholeParams buildParams(String paramString) {
        Query localQuery = this.mQuery;
        boolean bool;
        Uri localUri;
        String str1;
        String str2;
        PinholeStream.PinholeParams localPinholeParams;
        if (localQuery != null) {
            bool = true;
            Preconditions.checkState(bool);
            localUri = Uri.parse(this.mSearchUrlHelper.getWebSearchBaseUrl());
            str1 = localUri.getAuthority();
            str2 = localUri.getPath();
            if ((str1 != null) && (str2 != null)) {
                break label88;
            }
            Log.e("VS.PinholeParamsBuilder", "Invalid request URL: " + localUri);
            localPinholeParams = null;
        }
        for (; ; ) {
            return localPinholeParams;
            bool = false;
            break;
            label88:
            SearchUrlHelper.Builder localBuilder = this.mSearchUrlHelper.getVoiceSearchUrlBuilder(localQuery, localUri, paramString);
            localBuilder.setHostHeader(str1);
            localBuilder.setUserAgent((String) this.mUserAgentSupplier.get());
            if (Feature.DISCOURSE_CONTEXT.isEnabled()) {
                localBuilder.setIncludeDiscourseContext();
            }
            localPinholeParams = new PinholeStream.PinholeParams();
            localPinholeParams.setUrlPath(str2);
            UriRequest localUriRequest = localBuilder.build();
            Iterator localIterator1 = localUriRequest.getUniqueParams().entrySet().iterator();
            while (localIterator1.hasNext()) {
                Map.Entry localEntry2 = (Map.Entry) localIterator1.next();
                Preconditions.checkNotNull(localEntry2.getValue(), "Null parameter: " + (String) localEntry2.getKey());
                PinholeStream.PinholeCgiParam localPinholeCgiParam = new PinholeStream.PinholeCgiParam();
                localPinholeCgiParam.setKey((String) localEntry2.getKey());
                localPinholeCgiParam.setValue((String) localEntry2.getValue());
                localPinholeParams.addCgiParams(localPinholeCgiParam);
            }
            Iterator localIterator2 = localUriRequest.getHeaders().entrySet().iterator();
            while (localIterator2.hasNext()) {
                Map.Entry localEntry1 = (Map.Entry) localIterator2.next();
                Preconditions.checkNotNull(localEntry1.getValue(), "Null header: " + (String) localEntry1.getKey());
                PinholeStream.PinholeHeader localPinholeHeader = new PinholeStream.PinholeHeader();
                localPinholeHeader.setKey((String) localEntry1.getKey());
                localPinholeHeader.setValue((String) localEntry1.getValue());
                localPinholeParams.addHeaders(localPinholeHeader);
            }
        }
    }

    public Callable<PinholeStream.PinholeParams> getPinholeParamsCallable(final String paramString) {
        new BaseRequestBuilderTask("PinholeParamsBuilderTask") {
            protected PinholeStream.PinholeParams build() {
                return PinholeParamsBuilderImpl.this.buildParams(paramString);
            }
        };
    }

    public void setVoiceSearchQueryForLogging(Query paramQuery) {
        this.mQuery = paramQuery;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.speechservice.s3.PinholeParamsBuilderImpl

 * JD-Core Version:    0.7.0.1

 */