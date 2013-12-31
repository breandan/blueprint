package com.embryo.android.speech.callback;

public abstract interface Callback<T, K>
        extends SimpleCallback<T> {
    public abstract void onError(K paramK);
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     Callback

 * JD-Core Version:    0.7.0.1

 */