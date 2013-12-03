package com.google.android.shared.util;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class Consumers {
    public static <A> void addFutureConsumer(ListenableFuture<A> paramListenableFuture, final Consumer<A> paramConsumer, Executor paramExecutor) {
        paramListenableFuture.addListener(new Runnable() {
            public void run() {
                try {
                    Object localObject = this.val$future.get();
                    paramConsumer.consume(localObject);
                    return;
                } catch (InterruptedException localInterruptedException) {
                    Thread.currentThread().interrupt();
                    paramConsumer.consume(null);
                    return;
                } catch (ExecutionException localExecutionException) {
                    label31:
                    break label31;
                }
            }
        }, paramExecutor);
    }

    public static <A> void consumeAsync(Executor paramExecutor, Consumer<A> paramConsumer, final A paramA) {
        if (paramExecutor == null) {
            paramConsumer.consume(paramA);
            return;
        }
        paramExecutor.execute(new Runnable() {
            public void run() {
                this.val$consumer.consume(paramA);
            }
        });
    }

    public static <A> Consumer<A> createAsyncConsumer(Executor paramExecutor, final Consumer<A> paramConsumer) {
        new Consumer() {
            public boolean consume(A paramAnonymousA) {
                Consumers.consumeAsync(this.val$executor, paramConsumer, paramAnonymousA);
                return true;
            }
        };
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.Consumers

 * JD-Core Version:    0.7.0.1

 */