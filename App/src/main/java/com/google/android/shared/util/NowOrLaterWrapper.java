package com.google.android.shared.util;

public abstract class NowOrLaterWrapper<A, B>
        extends WrappingNowOrLaterBase<A, B> {
    public NowOrLaterWrapper(NowOrLater<? extends A> paramNowOrLater) {
        super(paramNowOrLater);
    }

    protected Consumer<A> createConsumer(Consumer<? super B> paramConsumer) {
        return new ConvertingConsumer(paramConsumer);
    }

    public abstract B get(A paramA);

    public B getNow() {
        return get(this.mWrapped.getNow());
    }

    public class ConvertingConsumer
            extends WrappingNowOrLaterBase.WrappingConsumerBase {
        public ConvertingConsumer() {
            super(localConsumer);
        }

        protected boolean doConsume(Consumer<? super B> paramConsumer, A paramA) {
            return paramConsumer.consume(NowOrLaterWrapper.this.get(paramA));
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.NowOrLaterWrapper

 * JD-Core Version:    0.7.0.1

 */