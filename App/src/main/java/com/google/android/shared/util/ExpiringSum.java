package com.google.android.shared.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class ExpiringSum {
    private int[] mBuckets;
    private boolean mChanged;
    private final Clock mClock;
    private final long mGranularity;
    private final long mMaxAgeMillis;
    private long mMostRecentBucket;
    private int mTotal;

    public ExpiringSum(Clock paramClock, long paramLong1, long paramLong2) {
        this.mClock = paramClock;
        this.mMaxAgeMillis = paramLong1;
        this.mGranularity = paramLong2;
        this.mBuckets = new int[(int) (this.mMaxAgeMillis / this.mGranularity)];
    }

    public ExpiringSum(Clock paramClock, long paramLong1, long paramLong2, String paramString) {
        this(paramClock, paramLong1, paramLong2);
        if (!TextUtils.isEmpty(paramString)) {
            loadJson(paramString);
        }
    }

    public ExpiringSum(Clock paramClock, long paramLong1, long paramLong2, String paramString, boolean paramBoolean) {
        this(paramClock, paramLong1, paramLong2, paramString);
        this.mChanged = true;
    }

    private void expireBuckets() {
        long l1 = this.mClock.currentTimeMillis() / this.mGranularity;
        if (l1 > this.mMostRecentBucket) {
            this.mTotal = 0;
            long l2 = l1 - this.mMostRecentBucket;
            int i = -1 + this.mBuckets.length;
            if (i >= 0) {
                if (i - l2 < 0L) {
                    this.mBuckets[i] = 0;
                }
                for (; ; ) {
                    i--;
                    break;
                    this.mBuckets[i] = this.mBuckets[((int) (i - l2))];
                    this.mTotal += this.mBuckets[i];
                }
            }
            this.mMostRecentBucket = l1;
        }
    }

    private void loadJson(String paramString) {
        try {
            JSONArray localJSONArray = new JSONArray(paramString);
            this.mMostRecentBucket = localJSONArray.optLong(0);
            for (int i = 0; i < this.mBuckets.length; i++) {
                this.mBuckets[i] = localJSONArray.optInt(i + 1);
                this.mTotal += this.mBuckets[i];
            }
            return;
        } catch (JSONException localJSONException) {
            Log.w("QSB.ExpiringSum", "Error reading expiring sum from " + paramString);
            expireBuckets();
        }
    }

    public int[] getBucketValues(int[] paramArrayOfInt) {
        try {
            expireBuckets();
            if (paramArrayOfInt.length != this.mBuckets.length) {
                paramArrayOfInt = new int[this.mBuckets.length];
            }
            System.arraycopy(this.mBuckets, 0, paramArrayOfInt, 0, this.mBuckets.length);
            return paramArrayOfInt;
        } finally {
        }
    }

    /* Error */
    public String getJson() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 58	com/google/android/shared/util/ExpiringSum:mTotal	I
        //   6: ifne +10 -> 16
        //   9: ldc 106
        //   11: astore_2
        //   12: aload_0
        //   13: monitorexit
        //   14: aload_2
        //   15: areturn
        //   16: new 62	org/json/JSONArray
        //   19: dup
        //   20: invokespecial 107	org/json/JSONArray:<init>	()V
        //   23: astore_3
        //   24: aload_3
        //   25: aload_0
        //   26: getfield 56	com/google/android/shared/util/ExpiringSum:mMostRecentBucket	J
        //   29: invokevirtual 111	org/json/JSONArray:put	(J)Lorg/json/JSONArray;
        //   32: pop
        //   33: iconst_0
        //   34: istore 5
        //   36: iload 5
        //   38: aload_0
        //   39: getfield 29	com/google/android/shared/util/ExpiringSum:mBuckets	[I
        //   42: arraylength
        //   43: if_icmpge +21 -> 64
        //   46: aload_3
        //   47: aload_0
        //   48: getfield 29	com/google/android/shared/util/ExpiringSum:mBuckets	[I
        //   51: iload 5
        //   53: iaload
        //   54: invokevirtual 114	org/json/JSONArray:put	(I)Lorg/json/JSONArray;
        //   57: pop
        //   58: iinc 5 1
        //   61: goto -25 -> 36
        //   64: aload_3
        //   65: invokevirtual 115	org/json/JSONArray:toString	()Ljava/lang/String;
        //   68: astore 6
        //   70: aload 6
        //   72: astore_2
        //   73: goto -61 -> 12
        //   76: astore_1
        //   77: aload_0
        //   78: monitorexit
        //   79: aload_1
        //   80: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	81	0	this	ExpiringSum
        //   76	4	1	localObject1	Object
        //   11	62	2	localObject2	Object
        //   23	42	3	localJSONArray	JSONArray
        //   34	25	5	i	int
        //   68	3	6	str	String
        // Exception table:
        //   from	to	target	type
        //   2	9	76	finally
        //   16	33	76	finally
        //   36	58	76	finally
        //   64	70	76	finally
    }

    /* Error */
    public String getJsonIfChanged() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 47	com/google/android/shared/util/ExpiringSum:mChanged	Z
        //   6: ifeq +19 -> 25
        //   9: aload_0
        //   10: iconst_0
        //   11: putfield 47	com/google/android/shared/util/ExpiringSum:mChanged	Z
        //   14: aload_0
        //   15: invokevirtual 118	com/google/android/shared/util/ExpiringSum:getJson	()Ljava/lang/String;
        //   18: astore_3
        //   19: aload_3
        //   20: astore_2
        //   21: aload_0
        //   22: monitorexit
        //   23: aload_2
        //   24: areturn
        //   25: aconst_null
        //   26: astore_2
        //   27: goto -6 -> 21
        //   30: astore_1
        //   31: aload_0
        //   32: monitorexit
        //   33: aload_1
        //   34: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	35	0	this	ExpiringSum
        //   30	4	1	localObject	Object
        //   20	7	2	str1	String
        //   18	2	3	str2	String
        // Exception table:
        //   from	to	target	type
        //   2	19	30	finally
    }

    public int getTotal() {
        try {
            expireBuckets();
            int i = this.mTotal;
            return i;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void increment() {
        try {
            incrementAtTime(this.mClock.currentTimeMillis());
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    public void incrementAtTime(long paramLong) {
        try {
            expireBuckets();
            long l = this.mMostRecentBucket - paramLong / this.mGranularity;
            if ((l >= 0L) && (l < this.mBuckets.length)) {
                int[] arrayOfInt = this.mBuckets;
                int i = (int) l;
                arrayOfInt[i] = (1 + arrayOfInt[i]);
                this.mTotal = (1 + this.mTotal);
                this.mChanged = true;
            }
            return;
        } finally {
        }
    }

    public String toString() {
        return "ExpiringSum[" + Arrays.toString(this.mBuckets) + "]";
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.ExpiringSum

 * JD-Core Version:    0.7.0.1

 */