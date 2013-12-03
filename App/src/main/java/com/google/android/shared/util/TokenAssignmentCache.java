package com.google.android.shared.util;

import android.util.SparseArray;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public abstract class TokenAssignmentCache<T> {
    private T mCachedItem;
    private int mNextToken = 1;
    private final SparseArray<Boolean> mValidTokens = new SparseArray(2);

    public int acquireToken() {
        try {
            if (this.mCachedItem == null) {
                this.mCachedItem = Preconditions.checkNotNull(createItem());
            }
            int i = this.mNextToken;
            this.mNextToken = (1 + this.mNextToken);
            this.mValidTokens.put(i, Boolean.TRUE);
            return i;
        } finally {
        }
    }

    @Nonnull
    protected abstract T createItem();

    protected void destroyItem(@Nonnull T paramT) {
    }

    /* Error */
    public T get(int paramInt) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 24	com/google/android/shared/util/TokenAssignmentCache:mValidTokens	Landroid/util/SparseArray;
        //   6: iload_1
        //   7: invokevirtual 57	android/util/SparseArray:get	(I)Ljava/lang/Object;
        //   10: getstatic 46	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
        //   13: if_acmpne +12 -> 25
        //   16: aload_0
        //   17: getfield 30	com/google/android/shared/util/TokenAssignmentCache:mCachedItem	Ljava/lang/Object;
        //   20: astore_3
        //   21: aload_0
        //   22: monitorexit
        //   23: aload_3
        //   24: areturn
        //   25: aconst_null
        //   26: astore_3
        //   27: goto -6 -> 21
        //   30: astore_2
        //   31: aload_0
        //   32: monitorexit
        //   33: aload_2
        //   34: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	35	0	this	TokenAssignmentCache
        //   0	35	1	paramInt	int
        //   30	4	2	localObject1	Object
        //   20	7	3	localObject2	Object
        // Exception table:
        //   from	to	target	type
        //   2	21	30	finally
    }

    /* Error */
    public void releaseToken(int paramInt) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 24	com/google/android/shared/util/TokenAssignmentCache:mValidTokens	Landroid/util/SparseArray;
        //   6: iload_1
        //   7: invokevirtual 57	android/util/SparseArray:get	(I)Ljava/lang/Object;
        //   10: getstatic 46	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
        //   13: if_acmpne +43 -> 56
        //   16: iconst_1
        //   17: istore_3
        //   18: iload_3
        //   19: invokestatic 62	com/google/common/base/Preconditions:checkState	(Z)V
        //   22: aload_0
        //   23: getfield 24	com/google/android/shared/util/TokenAssignmentCache:mValidTokens	Landroid/util/SparseArray;
        //   26: iload_1
        //   27: invokevirtual 65	android/util/SparseArray:remove	(I)V
        //   30: aload_0
        //   31: getfield 24	com/google/android/shared/util/TokenAssignmentCache:mValidTokens	Landroid/util/SparseArray;
        //   34: invokevirtual 68	android/util/SparseArray:size	()I
        //   37: ifne +16 -> 53
        //   40: aload_0
        //   41: aload_0
        //   42: getfield 30	com/google/android/shared/util/TokenAssignmentCache:mCachedItem	Ljava/lang/Object;
        //   45: invokevirtual 70	com/google/android/shared/util/TokenAssignmentCache:destroyItem	(Ljava/lang/Object;)V
        //   48: aload_0
        //   49: aconst_null
        //   50: putfield 30	com/google/android/shared/util/TokenAssignmentCache:mCachedItem	Ljava/lang/Object;
        //   53: aload_0
        //   54: monitorexit
        //   55: return
        //   56: iconst_0
        //   57: istore_3
        //   58: goto -40 -> 18
        //   61: astore_2
        //   62: aload_0
        //   63: monitorexit
        //   64: aload_2
        //   65: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	66	0	this	TokenAssignmentCache
        //   0	66	1	paramInt	int
        //   61	4	2	localObject	Object
        //   17	41	3	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	16	61	finally
        //   18	53	61	finally
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.TokenAssignmentCache

 * JD-Core Version:    0.7.0.1

 */