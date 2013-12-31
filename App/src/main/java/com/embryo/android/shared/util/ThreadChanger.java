package com.embryo.android.shared.util;

import com.google.common.base.Preconditions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executor;

public class ThreadChanger {
    static final boolean DEBUG = false;

    private static Throwable addCallerStackTrace(Throwable paramThrowable1, Throwable paramThrowable2) {
        if (paramThrowable2 == null) {
            return paramThrowable1;
        }
        Throwable localThrowable = getOriginalCause(paramThrowable1);
        StackTraceElement[] arrayOfStackTraceElement1 = localThrowable.getStackTrace();
        StackTraceElement[] arrayOfStackTraceElement2 = paramThrowable2.getStackTrace();
        StackTraceElement[] arrayOfStackTraceElement3 = new StackTraceElement[arrayOfStackTraceElement1.length + arrayOfStackTraceElement2.length];
        System.arraycopy(arrayOfStackTraceElement1, 0, arrayOfStackTraceElement3, 0, arrayOfStackTraceElement1.length);
        System.arraycopy(arrayOfStackTraceElement2, 0, arrayOfStackTraceElement3, arrayOfStackTraceElement1.length, arrayOfStackTraceElement2.length);
        localThrowable.setStackTrace(arrayOfStackTraceElement3);
        return paramThrowable1;
    }

    public static final <T> T createNonBlockingThreadChangeProxy(final Executor paramExecutor, Class<T> type, final T delegate) {
        Preconditions.checkNotNull(paramExecutor);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(delegate);
        InvocationHandler local1 = new InvocationHandler() {
            public Object invoke(final Object paramAnonymousObject, final Method paramAnonymousMethod, final Object[] paramAnonymousArrayOfObject) {
                Runnable local1 = new Runnable() {
                    public void run() {
                        try {
                            paramAnonymousMethod.invoke(delegate, paramAnonymousArrayOfObject);
                        } catch (IllegalAccessException localIllegalAccessException) {
                            throw new IllegalStateException(localIllegalAccessException);
                        } catch (InvocationTargetException localInvocationTargetException) {
                            Throwable localThrowable = ThreadChanger.addCallerStackTrace(localInvocationTargetException.getCause(), localInvocationTargetException);
                            if ((localThrowable instanceof RuntimeException)) {
                                throw ((RuntimeException) localThrowable);
                            }
                            if ((localThrowable instanceof Error)) {
                                throw ((Error) localThrowable);
                            }
                            throw new IllegalStateException(localThrowable);
                        }
                    }
                };
                paramExecutor.execute(local1);
                return null;
            }
        };
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, local1);
    }

    public static <T> T createNonBlockingThreadChangeProxy(Executor paramExecutor, T paramT) {
        Class[] arrayOfClass = paramT.getClass().getInterfaces();
        Preconditions.checkArgument(arrayOfClass.length == 1, "Delegate must implement a single interface");
        return (T) createNonBlockingThreadChangeProxy(paramExecutor, arrayOfClass[0], paramT);
    }

    private static Throwable getOriginalCause(Throwable paramThrowable) {
        while (paramThrowable.getCause() != null) {
            paramThrowable = paramThrowable.getCause();
        }
        return paramThrowable;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ThreadChanger

 * JD-Core Version:    0.7.0.1

 */