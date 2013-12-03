package com.google.android.velvet.gallery;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.search.core.google.SearchUrlHelper;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.search.shared.api.Query;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ImageMetadataController {
    protected static final long TIMER_DURATION_SECONDS = 2L;
    private final Executor mBgExecutor;
    private final Context mContext;
    private ImageMetadataLoader mCurrentLoader;
    private ScheduledFuture<?> mCurrentTimer;
    private final HttpHelper mHttpHelper;
    private final List<VelvetImage> mInitialImageData;
    private boolean mIsParsing;
    private final List<VelvetImage> mLoadedImageData;
    private boolean mNoMoreImages;
    private final ImageMetadataParser mParser;
    private int mPulledCount;
    private Query mQuery = Query.EMPTY;
    private final ScheduledExecutorService mTimerExecutor;
    private final Runnable mTimerRunnable = new Runnable() {
        public void run() {
            synchronized (ImageMetadataController.this) {
                ImageMetadataController.access$002(ImageMetadataController.this, null);
                ImageProvider.reportMoreImagesAvailable(ImageMetadataController.this.mContext, ImageMetadataController.this.mLoadedImageData.size());
                return;
            }
        }
    };
    private final SearchUrlHelper mUrlHelper;
    private String mWaitingForId;

    public ImageMetadataController(Context paramContext, HttpHelper paramHttpHelper, SearchUrlHelper paramSearchUrlHelper, Executor paramExecutor, ScheduledExecutorService paramScheduledExecutorService, ImageMetadataParser paramImageMetadataParser) {
        this.mContext = paramContext;
        this.mHttpHelper = paramHttpHelper;
        this.mUrlHelper = paramSearchUrlHelper;
        this.mBgExecutor = paramExecutor;
        this.mParser = paramImageMetadataParser;
        this.mTimerExecutor = paramScheduledExecutorService;
        this.mLoadedImageData = Lists.newArrayList();
        this.mInitialImageData = Lists.newArrayList();
    }

    private void cancelCurrentLoad() {
        this.mCurrentLoader = null;
        this.mIsParsing = false;
    }

    private void cancelWaitingTimeout() {
        if (this.mCurrentTimer != null) {
            this.mCurrentTimer.cancel(true);
        }
    }

    private void setQueryInternal(Query paramQuery, String paramString, boolean paramBoolean) {
        Preconditions.checkNotNull(this.mQuery);
        if ((paramBoolean) || (this.mQuery == null) || (!this.mUrlHelper.equivalentForSearch(this.mQuery, paramQuery)) || ((paramString != null) && (!this.mLoadedImageData.contains(new VelvetImage(paramString))))) {
            this.mLoadedImageData.clear();
            this.mWaitingForId = paramString;
            cancelWaitingTimeout();
            if (paramString != null) {
                startWaitingTimeout();
            }
            cancelCurrentLoad();
            this.mNoMoreImages = false;
            this.mPulledCount = 0;
        }
        this.mQuery = paramQuery;
    }

    private void startWaitingTimeout() {
        this.mCurrentTimer = this.mTimerExecutor.schedule(this.mTimerRunnable, 2L, TimeUnit.SECONDS);
    }

    public void fetchMoreImages() {
        try {
            if ((this.mCurrentLoader == null) && (!this.mIsParsing) && (!this.mNoMoreImages) && (this.mQuery.isTextOrVoiceWebSearchWithQueryChars())) {
                this.mCurrentLoader = new ImageMetadataLoader(this.mPulledCount, null);
                this.mBgExecutor.execute(this.mCurrentLoader);
            }
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    protected String fetchXml(Query paramQuery, int paramInt)
            throws IOException {
        Uri localUri = this.mUrlHelper.getImageMetadataUrl(this.mQuery, paramInt);
        Log.v("Velvet.ImageMetadataController", "fetching metadata from: " + localUri.toString());
        return this.mHttpHelper.get(new HttpHelper.GetRequest(localUri.toString()), 7);
    }

    protected ImageMetadataLoader getCurrentLoader() {
        return this.mCurrentLoader;
    }

    /* Error */
    public VelvetImage getImage(int paramInt) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: iload_1
        //   3: iflt +41 -> 44
        //   6: iload_1
        //   7: aload_0
        //   8: getfield 79	com/google/android/velvet/gallery/ImageMetadataController:mLoadedImageData	Ljava/util/List;
        //   11: invokeinterface 225 1 0
        //   16: if_icmpge +28 -> 44
        //   19: aload_0
        //   20: getfield 85	com/google/android/velvet/gallery/ImageMetadataController:mWaitingForId	Ljava/lang/String;
        //   23: ifnonnull +21 -> 44
        //   26: aload_0
        //   27: getfield 79	com/google/android/velvet/gallery/ImageMetadataController:mLoadedImageData	Ljava/util/List;
        //   30: iload_1
        //   31: invokeinterface 228 2 0
        //   36: checkcast 127	com/google/android/velvet/gallery/VelvetImage
        //   39: astore_2
        //   40: aload_0
        //   41: monitorexit
        //   42: aload_2
        //   43: areturn
        //   44: aconst_null
        //   45: astore_2
        //   46: goto -6 -> 40
        //   49: astore_3
        //   50: aload_0
        //   51: monitorexit
        //   52: aload_3
        //   53: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	54	0	this	ImageMetadataController
        //   0	54	1	paramInt	int
        //   39	7	2	localVelvetImage	VelvetImage
        //   49	4	3	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   6	40	49	finally
    }

    /* Error */
    public List<VelvetImage> getImages() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 85	com/google/android/velvet/gallery/ImageMetadataController:mWaitingForId	Ljava/lang/String;
        //   6: ifnonnull +19 -> 25
        //   9: aload_0
        //   10: getfield 79	com/google/android/velvet/gallery/ImageMetadataController:mLoadedImageData	Ljava/util/List;
        //   13: invokestatic 236	com/google/common/collect/ImmutableList:copyOf	(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;
        //   16: astore 4
        //   18: aload 4
        //   20: astore_3
        //   21: aload_0
        //   22: monitorexit
        //   23: aload_3
        //   24: areturn
        //   25: invokestatic 240	com/google/common/collect/ImmutableList:of	()Lcom/google/common/collect/ImmutableList;
        //   28: astore_2
        //   29: aload_2
        //   30: astore_3
        //   31: goto -10 -> 21
        //   34: astore_1
        //   35: aload_0
        //   36: monitorexit
        //   37: aload_1
        //   38: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	39	0	this	ImageMetadataController
        //   34	4	1	localObject1	Object
        //   28	2	2	localImmutableList1	com.google.common.collect.ImmutableList
        //   20	11	3	localObject2	Object
        //   16	3	4	localImmutableList2	com.google.common.collect.ImmutableList
        // Exception table:
        //   from	to	target	type
        //   2	18	34	finally
        //   25	29	34	finally
    }

    protected int getPulledCount() {
        return this.mPulledCount;
    }

    protected void loadComplete(ImageMetadataLoader paramImageMetadataLoader, List<VelvetImage> paramList, boolean paramBoolean) {
        try {
            if (paramImageMetadataLoader != this.mCurrentLoader) {
                break label286;
            }
            Log.v("Velvet.ImageMetadataController", "Loading complete. Number of images: " + paramList.size());
            if (this.mWaitingForId != null) {
                Log.v("Velvet.ImageMetadataController", "Loading complete, but waiting for ID: " + this.mWaitingForId);
                Iterator localIterator = paramList.iterator();
                while (localIterator.hasNext()) {
                    if (((VelvetImage) localIterator.next()).getId().equals(this.mWaitingForId)) {
                        Log.v("Velvet.ImageMetadataController", "Found ID, cancelling wait");
                        this.mWaitingForId = null;
                        cancelWaitingTimeout();
                    }
                }
            }
            if (!paramBoolean) {
                break label289;
            }
        } finally {
        }
        if (paramList.size() > 0) {
            int i = paramList.size();
            paramList.removeAll(this.mLoadedImageData);
            int j = i - paramList.size();
            Log.v("Velvet.ImageMetadataController", "Deduped " + j + " images");
            this.mLoadedImageData.addAll(paramList);
            Log.v("Velvet.ImageMetadataController", "Total number of images: " + this.mLoadedImageData.size());
            ImageProvider.reportMoreImagesAvailable(this.mContext, this.mLoadedImageData.size());
        }
        for (; ; ) {
            this.mCurrentLoader = null;
            label286:
            return;
            label289:
            Log.v("Velvet.ImageMetadataController", "Loading failed. success=" + paramBoolean);
            this.mNoMoreImages = true;
        }
    }

    public void setImages(List<VelvetImage> paramList) {
        try {
            this.mLoadedImageData.clear();
            this.mLoadedImageData.addAll(paramList);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void setJson(String paramString, Query paramQuery) {
        for (; ; ) {
            try {
                if (!this.mUrlHelper.equivalentForSearch(this.mQuery, paramQuery)) {
                    Log.e("Velvet.ImageMetadataController", "Can't set JSON: wrong query.");
                    return;
                }
                cancelCurrentLoad();
                this.mIsParsing = true;
                List localList = new ImageMetadataParser().readJson(paramString);
                if ((localList != null) && (localList.size() > 0)) {
                    this.mInitialImageData.addAll(localList);
                    this.mLoadedImageData.addAll(localList);
                    this.mWaitingForId = null;
                    ImageProvider.reportMoreImagesAvailable(this.mContext, this.mLoadedImageData.size());
                    this.mIsParsing = false;
                } else {
                    this.mNoMoreImages = true;
                }
            } finally {
            }
        }
    }

    public void setQuery(Query paramQuery, String paramString) {
        try {
            setQueryInternal(paramQuery, paramString, false);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void setQueryWithImages(Query paramQuery, String paramString, List<VelvetImage> paramList) {
        try {
            setQueryInternal(paramQuery, paramString, true);
            this.mLoadedImageData.addAll(paramList);
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    private class ImageMetadataLoader
            implements ImageMetadataExtractor.ResultHandler, Runnable {
        private final ImageMetadataExtractor mExtractor;
        private final List<VelvetImage> mImages;
        private final int mStartIndex;
        private String mXml;

        private ImageMetadataLoader(int paramInt) {
            this.mStartIndex = paramInt;
            this.mImages = Lists.newArrayList();
            this.mExtractor = new ImageMetadataExtractor(this);
        }

        public void onMetadataSectionExtracted(String paramString) {
            List localList = ImageMetadataController.this.mParser.readJson(paramString);
            ImageMetadataController.access$612(ImageMetadataController.this, localList.size());
            this.mImages.addAll(localList);
        }

        public void run() {
            try {
                String str = this.mXml;
                this.mImages.clear();
                if (str == null) {
                    str = ImageMetadataController.this.fetchXml(ImageMetadataController.this.mQuery, this.mStartIndex);
                }
                i = 0;
                if (str != null) {
                    this.mExtractor.parseXml(str);
                    i = 1;
                }
            } catch (IOException localIOException) {
                for (; ; ) {
                    Iterator localIterator;
                    Log.w("Velvet.ImageMetadataController", "IOException fetching image metadata", localIOException);
                    i = 0;
                }
            } catch (IllegalStateException localIllegalStateException) {
                for (; ; ) {
                    Log.w("Velvet.ImageMetadataController", "IllegalStateException fetching image metadata", localIllegalStateException);
                    int i = 0;
                }
                if (this.mXml == null) {
                    break label171;
                }
                this.mXml = null;
                run();
                return;
                label171:
                ImageMetadataController.this.loadComplete(this, this.mImages, false);
            }
            if (i != 0) {
                localIterator = this.mImages.iterator();
            }
            for (; ; ) {
                if (localIterator.hasNext()) {
                    if (TextUtils.isEmpty(((VelvetImage) localIterator.next()).getUri())) {
                        i = 0;
                    }
                } else if (i != 0) {
                    ImageMetadataController.this.loadComplete(this, this.mImages, true);
                    return;
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.ImageMetadataController

 * JD-Core Version:    0.7.0.1

 */