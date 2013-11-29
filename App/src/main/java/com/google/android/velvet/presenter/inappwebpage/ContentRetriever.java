package com.google.android.velvet.presenter.inappwebpage;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.prefetch.AsyncHttpResponse;
import com.google.android.search.core.prefetch.AsyncHttpResponse.ResponseListener;
import com.google.android.search.core.prefetch.AsyncHttpResponseFetcher;
import com.google.android.search.core.prefetch.WebPage;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.search.core.util.HttpHelper.HttpRedirectException;
import com.google.android.search.core.util.LazyString;
import com.google.android.velvet.Cookies;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

public class ContentRetriever
{
  private final AsyncHttpResponseFetcher mAsyncHttpFetcher;
  private final Executor mBackgroundExecutor;
  private final Cookies mCookies;
  private final GsaConfigFlags mFlags;
  private final Executor mSameThreadExecutor = MoreExecutors.sameThreadExecutor();
  private final SearchUrlHelper mSearchUrlHelper;
  
  public ContentRetriever(Executor paramExecutor, Cookies paramCookies, GsaConfigFlags paramGsaConfigFlags, SearchUrlHelper paramSearchUrlHelper, AsyncHttpResponseFetcher paramAsyncHttpResponseFetcher)
  {
    this.mBackgroundExecutor = paramExecutor;
    this.mCookies = paramCookies;
    this.mFlags = paramGsaConfigFlags;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mAsyncHttpFetcher = paramAsyncHttpResponseFetcher;
  }
  
  private void cancelResponseIfFutureCancelled(final ListenableFuture<?> paramListenableFuture, final AsyncHttpResponse paramAsyncHttpResponse)
  {
    paramListenableFuture.addListener(new Runnable()
    {
      public void run()
      {
        if (paramListenableFuture.isCancelled()) {
          paramAsyncHttpResponse.close();
        }
      }
    }, this.mSameThreadExecutor);
  }
  
  private HttpHelper.GetRequest createGetRequest(Uri paramUri, String paramString)
  {
    HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest(paramUri.toString());
    localGetRequest.setFollowRedirects(false);
    localGetRequest.setRewriteUrl(false);
    localGetRequest.setUseSpdy(this.mFlags.isSpdyEnabledForInAppWebPage());
    localGetRequest.setUseCaches(false);
    if (!TextUtils.isEmpty(paramString)) {
      localGetRequest.setHeader("Cookie", paramString);
    }
    return localGetRequest;
  }
  
  private void log(@Nullable Listener paramListener, Object paramObject)
  {
    if (paramListener != null) {
      paramListener.log(paramObject);
    }
  }
  
  public ListenableFuture<Content> retrieveContent(Uri paramUri, @Nullable Listener paramListener)
  {
    SettableFuture localSettableFuture = SettableFuture.create();
    if (paramUri.getScheme().equals("file"))
    {
      FileRetrieval localFileRetrieval = new FileRetrieval(localSettableFuture, paramUri, paramListener);
      this.mBackgroundExecutor.execute(localFileRetrieval);
      return localSettableFuture;
    }
    new HttpRetrieval(localSettableFuture, paramUri, paramListener).go();
    return localSettableFuture;
  }
  
  private class FileRetrieval
    implements Runnable
  {
    @Nullable
    private final ContentRetriever.Listener mListener;
    private final SettableFuture<Content> mOutput;
    private final Uri mUri;
    
    public FileRetrieval(Uri paramUri, ContentRetriever.Listener paramListener)
    {
      this.mOutput = ((SettableFuture)Preconditions.checkNotNull(paramUri));
      this.mUri = ((Uri)Preconditions.checkNotNull(paramListener));
      Object localObject;
      this.mListener = localObject;
    }
    
    public void run()
    {
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(this.mUri.getPath());
        ContentRetriever localContentRetriever2 = ContentRetriever.this;
        ContentRetriever.Listener localListener2 = this.mListener;
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = this.mUri;
        localContentRetriever2.log(localListener2, new LazyString("Got InputStream for %s", arrayOfObject2));
        WebPage localWebPage = new WebPage(ImmutableMap.of(), localFileInputStream);
        this.mOutput.set(new Content(this.mUri, localWebPage, localFileInputStream));
        return;
      }
      catch (IOException localIOException)
      {
        ContentRetriever localContentRetriever1 = ContentRetriever.this;
        ContentRetriever.Listener localListener1 = this.mListener;
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = localIOException.getMessage();
        localContentRetriever1.log(localListener1, new LazyString("Exception reading file: %s", arrayOfObject1));
        this.mOutput.setException(localIOException);
      }
    }
  }
  
  private class HttpRetrieval
  {
    private final ImmutableSet<String> mAuthFailureUris;
    private int mAuthFailures = 0;
    @Nullable
    private final ContentRetriever.Listener mListener;
    private final Uri mOriginalUri;
    private final SettableFuture<Content> mOutput;
    private int mRedirects = 0;
    
    public HttpRetrieval(Uri paramUri, ContentRetriever.Listener paramListener)
    {
      this.mOutput = ((SettableFuture)Preconditions.checkNotNull(paramUri));
      this.mOriginalUri = ((Uri)Preconditions.checkNotNull(paramListener));
      Object localObject;
      this.mListener = localObject;
      this.mAuthFailureUris = ImmutableSet.copyOf(ContentRetriever.this.mFlags.getInAppWebPageAuthFailureUris());
    }
    
    public void go()
    {
      ContentRetriever localContentRetriever = ContentRetriever.this;
      ContentRetriever.Listener localListener = this.mListener;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = SearchUrlHelper.safeLogUrl(this.mOriginalUri);
      localContentRetriever.log(localListener, new LazyString("Loading %s", arrayOfObject));
      ContentRetriever.this.mBackgroundExecutor.execute(new Attempt(this.mOriginalUri));
    }
    
    private class Attempt
      implements AsyncHttpResponse.ResponseListener, Runnable
    {
      private final AtomicBoolean mHandled = new AtomicBoolean(false);
      @Nullable
      private AsyncHttpResponse mResponse;
      private Uri mUri;
      
      public Attempt(Uri paramUri)
      {
        this.mUri = paramUri;
      }
      
      private void handleAuthFailure()
      {
        if (ContentRetriever.HttpRetrieval.this.mAuthFailures >= 0)
        {
          ContentRetriever.this.log(ContentRetriever.HttpRetrieval.this.mListener, "Too many auth failures");
          ContentRetriever.HttpRetrieval.this.mOutput.setException(new Exception("Too many auth failures"));
          return;
        }
        ContentRetriever.this.log(ContentRetriever.HttpRetrieval.this.mListener, "Retrying after auth failure");
        ContentRetriever.HttpRetrieval.access$1108(ContentRetriever.HttpRetrieval.this);
        ContentRetriever.HttpRetrieval.access$902(ContentRetriever.HttpRetrieval.this, 0);
        ContentRetriever.this.mBackgroundExecutor.execute(new Attempt(ContentRetriever.HttpRetrieval.this, ContentRetriever.HttpRetrieval.this.mOriginalUri));
      }
      
      private void handleFailure(IOException paramIOException)
      {
        ContentRetriever.HttpRetrieval.this.mOutput.setException(paramIOException);
      }
      
      private void handleIoException(IOException paramIOException)
      {
        if ((paramIOException instanceof HttpHelper.HttpRedirectException))
        {
          storeCookies();
          int j = ((HttpHelper.HttpRedirectException)paramIOException).getStatusCode();
          ContentRetriever localContentRetriever2 = ContentRetriever.this;
          ContentRetriever.Listener localListener2 = ContentRetriever.HttpRetrieval.this.mListener;
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = Integer.valueOf(j);
          localContentRetriever2.log(localListener2, new LazyString("Redirect status code %d", arrayOfObject2));
          handleRedirect((HttpHelper.HttpRedirectException)paramIOException);
          return;
        }
        if ((paramIOException instanceof HttpHelper.HttpException))
        {
          storeCookies();
          int i = ((HttpHelper.HttpException)paramIOException).getStatusCode();
          ContentRetriever localContentRetriever1 = ContentRetriever.this;
          ContentRetriever.Listener localListener1 = ContentRetriever.HttpRetrieval.this.mListener;
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = Integer.valueOf(i);
          localContentRetriever1.log(localListener1, new LazyString("Status code %d", arrayOfObject1));
          if ((i == 403) || (i == 401))
          {
            handleAuthFailure();
            return;
          }
        }
        handleFailure(paramIOException);
      }
      
      private void handleRedirect(HttpHelper.HttpRedirectException paramHttpRedirectException)
      {
        if (ContentRetriever.HttpRetrieval.this.mRedirects >= 10)
        {
          ContentRetriever.this.log(ContentRetriever.HttpRetrieval.this.mListener, "Too many redirects");
          ContentRetriever.HttpRetrieval.this.mOutput.setException(new Exception("Too many redirects"));
          return;
        }
        int i = paramHttpRedirectException.getStatusCode();
        Uri localUri = Uri.parse(paramHttpRedirectException.getRedirectLocation());
        if (localUri.isRelative()) {
          localUri = localUri.buildUpon().scheme(this.mUri.getScheme()).authority(this.mUri.getAuthority()).build();
        }
        if (!ContentRetriever.this.mSearchUrlHelper.isSecureGoogleUri(localUri))
        {
          ContentRetriever localContentRetriever3 = ContentRetriever.this;
          ContentRetriever.Listener localListener3 = ContentRetriever.HttpRetrieval.this.mListener;
          Object[] arrayOfObject3 = new Object[2];
          arrayOfObject3[0] = Integer.valueOf(i);
          arrayOfObject3[1] = SearchUrlHelper.safeLogUrl(localUri);
          localContentRetriever3.log(localListener3, new LazyString("$d redirect to insecure URI %s", arrayOfObject3));
          ContentRetriever.HttpRetrieval.this.mOutput.setException(new Exception("Redirect to insecure URI"));
          return;
        }
        if (isAuthFailureUri(localUri))
        {
          ContentRetriever localContentRetriever2 = ContentRetriever.this;
          ContentRetriever.Listener localListener2 = ContentRetriever.HttpRetrieval.this.mListener;
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = Integer.valueOf(i);
          arrayOfObject2[1] = SearchUrlHelper.safeLogUrl(localUri);
          localContentRetriever2.log(localListener2, new LazyString("%d redirect to auth failure URI %s", arrayOfObject2));
          handleAuthFailure();
          return;
        }
        ContentRetriever localContentRetriever1 = ContentRetriever.this;
        ContentRetriever.Listener localListener1 = ContentRetriever.HttpRetrieval.this.mListener;
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Integer.valueOf(i);
        arrayOfObject1[1] = SearchUrlHelper.safeLogUrl(localUri);
        localContentRetriever1.log(localListener1, new LazyString("%d redirect to %s", arrayOfObject1));
        ContentRetriever.HttpRetrieval.access$908(ContentRetriever.HttpRetrieval.this);
        ContentRetriever.this.mBackgroundExecutor.execute(new Attempt(ContentRetriever.HttpRetrieval.this, localUri));
      }
      
      private void handleSuccess()
      {
        ContentRetriever.this.log(ContentRetriever.HttpRetrieval.this.mListener, "Successful response ready");
        storeCookies();
        WebPage localWebPage = new WebPage(this.mResponse.getHeaders(), this.mResponse.getInputStream());
        Content localContent = new Content(this.mUri, localWebPage, this.mResponse);
        ContentRetriever.HttpRetrieval.this.mOutput.set(localContent);
      }
      
      private boolean isAuthFailureUri(Uri paramUri)
      {
        Uri localUri = paramUri.buildUpon().clearQuery().build();
        return ContentRetriever.HttpRetrieval.this.mAuthFailureUris.contains(localUri.toString());
      }
      
      private void storeCookies()
      {
        ContentRetriever.this.mCookies.setCookiesFromHeaders(this.mUri.toString(), this.mResponse.getHeaders());
      }
      
      private boolean takeHandleResponse()
      {
        return this.mHandled.compareAndSet(false, true);
      }
      
      public void onResponseChanged()
      {
        if (ContentRetriever.HttpRetrieval.this.mOutput.isDone()) {}
        do
        {
          return;
          if ((this.mResponse.hasIoException()) && (takeHandleResponse()))
          {
            handleIoException(this.mResponse.getIoException());
            return;
          }
        } while ((!this.mResponse.hasHeaders()) || (!takeHandleResponse()));
        handleSuccess();
      }
      
      public void run()
      {
        if (ContentRetriever.HttpRetrieval.this.mOutput.isDone()) {
          return;
        }
        String str = ContentRetriever.this.mCookies.getCookie(this.mUri.toString());
        HttpHelper.GetRequest localGetRequest = ContentRetriever.this.createGetRequest(this.mUri, str);
        try
        {
          this.mResponse = ContentRetriever.this.mAsyncHttpFetcher.get(localGetRequest, 15);
          this.mResponse.setListener(this);
          ContentRetriever.this.cancelResponseIfFutureCancelled(ContentRetriever.HttpRetrieval.this.mOutput, this.mResponse);
          return;
        }
        catch (IOException localIOException)
        {
          ContentRetriever.HttpRetrieval.this.mOutput.setException(localIOException);
        }
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void log(Object paramObject);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.presenter.inappwebpage.ContentRetriever
 * JD-Core Version:    0.7.0.1
 */