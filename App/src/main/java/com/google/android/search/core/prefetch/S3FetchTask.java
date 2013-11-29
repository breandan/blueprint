package com.google.android.search.core.prefetch;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.search.core.google.PelletChunkProducer;
import com.google.android.search.core.google.PelletDemultiplexer.ExtrasConsumer;
import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.util.EagerBufferedInputStream;
import com.google.android.search.core.util.EagerBufferedInputStream.BufferTaskListener;
import com.google.android.search.core.util.HttpHelper.HttpException;
import com.google.android.shared.util.Consumer;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.exception.ResponseRecognizeException;
import com.google.android.velvet.ActionData;
import com.google.android.velvet.VelvetStrictMode;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.speech.s3.PinholeStream.PinholeOutput;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public class S3FetchTask
  extends SearchResultFetcher.FetchTask
  implements PelletDemultiplexer.ExtrasConsumer, Consumer<Map<String, List<String>>>
{
  private static final RecognizeException CANCELLED = new ResponseRecognizeException("cancelled");
  private final ExecutorService mBufferingExecutor;
  private final S3HeaderProcessor mHeaderProcessor;
  private final int mMaxGwsResponseSizeBytes;
  private EagerBufferedInputStream mResponseInputStream;
  private ForwardingChunkProducer mResponseProducer;
  private final SearchUrlHelper mSearchUrlHelper;
  private final AtomicInteger mState = new AtomicInteger(0);
  private final String mSuggestionPelletPath;
  private EagerBufferedInputStream mWebResourceContentStream;
  
  public S3FetchTask(ExecutorService paramExecutorService, int paramInt, String paramString, SearchUrlHelper paramSearchUrlHelper)
  {
    this(paramExecutorService, paramInt, paramString, paramSearchUrlHelper, new S3HeaderProcessor());
  }
  
  S3FetchTask(ExecutorService paramExecutorService, int paramInt, String paramString, SearchUrlHelper paramSearchUrlHelper, S3HeaderProcessor paramS3HeaderProcessor)
  {
    this.mBufferingExecutor = paramExecutorService;
    this.mMaxGwsResponseSizeBytes = paramInt;
    this.mSuggestionPelletPath = paramString;
    this.mSearchUrlHelper = paramSearchUrlHelper;
    this.mHeaderProcessor = paramS3HeaderProcessor;
    this.mHeaderProcessor.setConsumer(this);
  }
  
  private boolean isComplete()
  {
    return this.mState.get() == 2;
  }
  
  private boolean isFailed()
  {
    return this.mState.get() == 1;
  }
  
  private void maybeInitializeResponseStreamLocked()
  {
    this.mResponseProducer = newResponseProducer(WebPage.JSON_CHARSET, this.mMaxGwsResponseSizeBytes);
    EagerBufferedInputStream.BufferTaskListener local1 = new EagerBufferedInputStream.BufferTaskListener()
    {
      public void onComplete() {}
      
      public void onFailed()
      {
        S3FetchTask.this.setFailed(new SearchResultFetcher.ResponseFetchException("ForwardingChunkProducer failed"));
      }
    };
    this.mResponseInputStream = EagerBufferedInputStream.newStream(this.mResponseProducer, local1);
    this.mWebResourceContentStream = EagerBufferedInputStream.newStream(new PelletChunkProducer(this.mResponseInputStream, this.mBufferingExecutor, this.mMaxGwsResponseSizeBytes, this, this.mSearchUrlHelper.getWebSearchBaseUrl(), this.mSuggestionPelletPath), new EagerBufferedInputStream.BufferTaskListener()
    {
      public void onComplete() {}
      
      public void onFailed()
      {
        S3FetchTask.this.setFailed(new SearchResultFetcher.ResponseFetchException("PelletChunkProducer failed"));
      }
    });
  }
  
  private boolean setComplete()
  {
    boolean bool1 = this.mState.compareAndSet(0, 2);
    boolean bool2 = false;
    if (bool1)
    {
      getConsumer().setComplete();
      bool2 = true;
    }
    return bool2;
  }
  
  private boolean setFailed(@Nonnull IOException paramIOException)
  {
    String str = paramIOException.getMessage();
    if (!TextUtils.isEmpty(str)) {
      Log.e("Velvet.S3FetchTask", str, paramIOException);
    }
    if (this.mState.compareAndSet(0, 1))
    {
      getConsumer().setFailed(paramIOException);
      return true;
    }
    return false;
  }
  
  public void cancel()
  {
    if (setFailed(new SearchResultFetcher.ResponseFetchException("Cancelled."))) {
      this.mHeaderProcessor.reportError(CANCELLED);
    }
    try
    {
      ForwardingChunkProducer localForwardingChunkProducer = this.mResponseProducer;
      if (localForwardingChunkProducer != null) {
        localForwardingChunkProducer.markComplete();
      }
      return;
    }
    finally {}
  }
  
  public boolean consume(Map<String, List<String>> paramMap)
  {
    for (;;)
    {
      EagerBufferedInputStream localEagerBufferedInputStream1;
      EagerBufferedInputStream localEagerBufferedInputStream2;
      try
      {
        localEagerBufferedInputStream1 = this.mResponseInputStream;
        localEagerBufferedInputStream2 = this.mWebResourceContentStream;
        if (this.mHeaderProcessor.getResponseCode() != 200)
        {
          setFailed(new HttpHelper.HttpException(this.mHeaderProcessor.getResponseCode(), "Bad HTTP response code."));
          return true;
        }
      }
      finally {}
      if ((localEagerBufferedInputStream1 == null) || (localEagerBufferedInputStream2 == null))
      {
        setFailed(new SearchResultFetcher.ResponseFetchException("No response body received."));
      }
      else
      {
        WebPage localWebPage = new WebPage(paramMap, localEagerBufferedInputStream2);
        getConsumer().setWebPage(localWebPage);
      }
    }
  }
  
  public boolean isFailedOrComplete()
  {
    int i = this.mState.get();
    return (i == 1) || (i == 2);
  }
  
  protected ForwardingChunkProducer newResponseProducer(Charset paramCharset, int paramInt)
  {
    return new ForwardingChunkProducer(paramCharset, paramInt);
  }
  
  public void offerPinholeResult(PinholeStream.PinholeOutput paramPinholeOutput)
  {
    for (;;)
    {
      try
      {
        boolean bool = isFailedOrComplete();
        if (bool) {
          return;
        }
        if ((paramPinholeOutput.hasGwsHeaderFragment()) && (!TextUtils.isEmpty(paramPinholeOutput.getGwsHeaderFragment()))) {
          this.mHeaderProcessor.appendHeaderFragment(paramPinholeOutput.getGwsHeaderFragment());
        }
        if (paramPinholeOutput.getGwsHeaderComplete())
        {
          if (this.mResponseInputStream == null)
          {
            this.mHeaderProcessor.processHeaders();
            maybeInitializeResponseStreamLocked();
            this.mHeaderProcessor.notifyConsumerIfFirstTime();
          }
          if (isFailed()) {
            continue;
          }
        }
        if ((paramPinholeOutput.hasGwsBodyFragment()) && (paramPinholeOutput.getGwsBodyFragment() != null) && (!paramPinholeOutput.getGwsBodyFragment().isEmpty()))
        {
          if (this.mResponseProducer == null)
          {
            setFailed(new SearchResultFetcher.ResponseFetchException("Missing response producer. (Out of order message ?)"));
            continue;
          }
          str = paramPinholeOutput.getGwsBodyFragment().toStringUtf8();
        }
      }
      finally {}
      try
      {
        String str;
        this.mResponseProducer.offerChunk(str);
        if (paramPinholeOutput.getGwsResponseComplete()) {
          if (this.mResponseProducer == null) {
            setFailed(new SearchResultFetcher.ResponseFetchException("Missing response producer. (Out of order message ?)"));
          }
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          setFailed(localIOException);
        }
        this.mResponseProducer.markComplete();
        setComplete();
      }
    }
  }
  
  public void onActionDataFinished(boolean paramBoolean)
  {
    if (paramBoolean) {}
    for (ActionData localActionData = ActionData.ANSWER_IN_SRP;; localActionData = ActionData.NONE)
    {
      onCardReceived(localActionData);
      return;
    }
  }
  
  public void onActionDataReceived(ActionData paramActionData)
  {
    onCardReceived(paramActionData);
  }
  
  public void onSrpMetadata(SearchResult.SrpMetadata paramSrpMetadata)
  {
    getConsumer().setSrpMetadata(paramSrpMetadata);
  }
  
  public void onSrpQuery(String paramString) {}
  
  public void onSuggestions(String paramString1, String paramString2)
  {
    VelvetStrictMode.logW("Velvet.S3FetchTask", "Received suggestions from S3, which is not expected");
  }
  
  public void reportError(RecognizeException paramRecognizeException)
  {
    Log.e("Velvet.S3FetchTask", "Failed S3ResultPage: " + paramRecognizeException.getMessage());
    if (isFailedOrComplete()) {}
    for (;;)
    {
      return;
      if (!setFailed(new SearchResultFetcher.ResponseFetchException(paramRecognizeException.getMessage()))) {
        continue;
      }
      this.mHeaderProcessor.reportError(paramRecognizeException);
      try
      {
        ForwardingChunkProducer localForwardingChunkProducer = this.mResponseProducer;
        if (localForwardingChunkProducer == null) {
          continue;
        }
        localForwardingChunkProducer.reportError(paramRecognizeException);
        return;
      }
      finally {}
    }
  }
  
  public String toString()
  {
    return "S3FetchTask{complete=" + isComplete() + ", failed=" + isFailed() + "}";
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.prefetch.S3FetchTask
 * JD-Core Version:    0.7.0.1
 */