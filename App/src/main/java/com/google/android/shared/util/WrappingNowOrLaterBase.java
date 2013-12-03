package com.google.android.shared.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Map;

public abstract class WrappingNowOrLaterBase<A, B>
        implements CancellableNowOrLater<B> {
    protected final Map<Consumer<? super B>, Consumer<? super A>> mConsumers;
    protected final NowOrLater<? extends A> mWrapped;

    WrappingNowOrLaterBase(NowOrLater<? extends A> paramNowOrLater) {
        this.mWrapped = paramNowOrLater;
        this.mConsumers = Maps.newHashMap();
    }

    public void cancelGetLater(Consumer<? super B> paramConsumer) {
        Consumer localConsumer = (Consumer) this.mConsumers.remove(paramConsumer);
        if ((localConsumer != null) && ((this.mWrapped instanceof CancellableNowOrLater))) {
            ((CancellableNowOrLater) this.mWrapped).cancelGetLater(localConsumer);
        }
    }

    protected abstract Consumer<A> createConsumer(Consumer<? super B> paramConsumer);

    public void getLater(Consumer<? super B> paramConsumer) {
        boolean bool;
        if (!this.mConsumers.containsKey(paramConsumer)) {
            bool = true;
        }
        for (; ; ) {
            Preconditions.checkArgument(bool);
            Consumer localConsumer = createConsumer(paramConsumer);
            synchronized (this.mConsumers) {
                this.mConsumers.put(paramConsumer, localConsumer);
                this.mWrapped.getLater(localConsumer);
                return;
                bool = false;
            }
        }
    }

    public boolean haveNow() {
        return this.mWrapped.haveNow();
    }

    protected abstract class WrappingConsumerBase
            implements Consumer<A> {
        protected final Consumer<? super B> mWrappedConsumer;

        public WrappingConsumerBase() {
            Object localObject;
            this.mWrappedConsumer = localObject;
        }

        public boolean consume(A paramA) {
            Consumer localConsumer = consumer();
            if (localConsumer == null) {
                return false;
            }
            synchronized (WrappingNowOrLaterBase.this.mConsumers) {
                WrappingNowOrLaterBase.this.mConsumers.remove(localConsumer);
                return doConsume(localConsumer, paramA);
            }
        }

        protected Consumer<? super B> consumer() {
            Consumer localConsumer = (Consumer) WrappingNowOrLaterBase.this.mConsumers.get(this.mWrappedConsumer);
            if ((localConsumer == null) || (localConsumer == this)) {
            }
            for (boolean bool = true; ; bool = false) {
                Preconditions.checkState(bool);
                if (localConsumer != null) {
                    break;
                }
                return null;
            }
            return this.mWrappedConsumer;
        }

        protected abstract boolean doConsume(Consumer<? super B> paramConsumer, A paramA);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.WrappingNowOrLaterBase

 * JD-Core Version:    0.7.0.1

 */