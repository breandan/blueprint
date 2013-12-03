package com.google.android.shared.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class CachedLater<A>
        implements CancellableNowOrLater<A> {
    private boolean mCancelled;
    private boolean mCreating;
    private final Object mLock = new Object();
    private boolean mValid;
    private A mValue;
    private List<Consumer<? super A>> mWaitingConsumers;

    private void storeInternal(A paramA) {
        storeInternal(paramA, false);
    }

    private void storeInternal(A paramA, boolean paramBoolean) {
        Object localObject1 = this.mLock;
        if (!paramBoolean) {
        }
        for (; ; ) {
            try {
                this.mValue = paramA;
                this.mValid = true;
                this.mCreating = false;
                List localList = this.mWaitingConsumers;
                this.mWaitingConsumers = null;
                if (localList == null) {
                    break;
                }
                Iterator localIterator = localList.iterator();
                if (!localIterator.hasNext()) {
                    break;
                }
                Consumer localConsumer = (Consumer) localIterator.next();
                Object localObject3;
                if (paramBoolean) {
                    localObject3 = null;
                    localConsumer.consume(localObject3);
                } else {
                    localObject3 = paramA;
                }
            } finally {
            }
        }
    }

    protected boolean cancel() {
        return false;
    }

    public void cancelGetLater(Consumer<? super A> paramConsumer) {
        synchronized (this.mLock) {
            if ((this.mWaitingConsumers != null) && (this.mWaitingConsumers.contains(paramConsumer))) {
                this.mWaitingConsumers.remove(paramConsumer);
            }
            if (((this.mWaitingConsumers == null) || (this.mWaitingConsumers.size() == 0)) && (this.mCreating) && (cancel())) {
                this.mCreating = false;
            }
            return;
        }
    }

    public void clear() {
        synchronized (this.mLock) {
            this.mValue = null;
            this.mValid = false;
            this.mCancelled = false;
            return;
        }
    }

    protected abstract void create();

    public void getLater(Consumer<? super A> paramConsumer) {
        for (; ; ) {
            synchronized (this.mLock) {
                boolean bool1 = this.mValid;
                Object localObject3 = this.mValue;
                if ((!bool1) && (paramConsumer != null)) {
                    if (this.mWaitingConsumers == null) {
                        this.mWaitingConsumers = new ArrayList();
                    }
                    this.mWaitingConsumers.add(paramConsumer);
                }
                if (bool1) {
                    if (paramConsumer != null) {
                        paramConsumer.consume(localObject3);
                    }
                    return;
                }
            }
            synchronized (this.mLock) {
                boolean bool2 = this.mCreating;
                int i = 0;
                if (!bool2) {
                    this.mCreating = true;
                    i = 1;
                }
                if (i == 0) {
                    continue;
                }
                create();
                return;
            }
        }
    }

    public A getNow() {
        try {
            synchronized (this.mLock) {
                if (!haveNow()) {
                    throw new IllegalStateException("getNow() called when haveNow() is false");
                }
            }
            localObject4 = this.mValue;
        } finally {
        }
        Object localObject4;
        return localObject4;
    }

    public boolean haveNow() {
        synchronized (this.mLock) {
            boolean bool = this.mValid;
            return bool;
        }
    }

    public final boolean isCancelled() {
        synchronized (this.mLock) {
            boolean bool = this.mCancelled;
            return bool;
        }
    }

    public void prefetch() {
        getLater(null);
    }

    protected final void setCancelled() {
        synchronized (this.mLock) {
            this.mCancelled = true;
            return;
        }
    }

    protected void store(A paramA) {
        storeInternal(paramA);
    }

    protected void storeNothing() {
        storeInternal(null, true);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.CachedLater

 * JD-Core Version:    0.7.0.1

 */