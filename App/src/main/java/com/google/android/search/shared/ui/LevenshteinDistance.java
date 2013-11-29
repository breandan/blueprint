package com.google.android.search.shared.ui;

import java.lang.reflect.Array;

class LevenshteinDistance
{
  private final int[][] mDistanceTable;
  private final int[][] mEditTypeTable;
  private final Token[] mSource;
  private final Token[] mTarget;
  
  public LevenshteinDistance(Token[] paramArrayOfToken1, Token[] paramArrayOfToken2)
  {
    int i = paramArrayOfToken1.length;
    int j = paramArrayOfToken2.length;
    int[] arrayOfInt1 = { i + 1, j + 1 };
    int[][] arrayOfInt2 = (int[][])Array.newInstance(Integer.TYPE, arrayOfInt1);
    int[] arrayOfInt3 = { i + 1, j + 1 };
    int[][] arrayOfInt4 = (int[][])Array.newInstance(Integer.TYPE, arrayOfInt3);
    arrayOfInt2[0][0] = 3;
    arrayOfInt4[0][0] = 0;
    for (int k = 1; k <= i; k++)
    {
      arrayOfInt2[k][0] = 0;
      arrayOfInt4[k][0] = k;
    }
    for (int m = 1; m <= j; m++)
    {
      arrayOfInt2[0][m] = 1;
      arrayOfInt4[0][m] = m;
    }
    this.mEditTypeTable = arrayOfInt2;
    this.mDistanceTable = arrayOfInt4;
    this.mSource = paramArrayOfToken1;
    this.mTarget = paramArrayOfToken2;
  }
  
  public int calculate()
  {
    Token[] arrayOfToken1 = this.mSource;
    Token[] arrayOfToken2 = this.mTarget;
    int i = arrayOfToken1.length;
    int j = arrayOfToken2.length;
    int[][] arrayOfInt1 = this.mDistanceTable;
    int[][] arrayOfInt2 = this.mEditTypeTable;
    for (int k = 1; k <= i; k++)
    {
      Token localToken = arrayOfToken1[(k - 1)];
      int m = 1;
      if (m <= j)
      {
        int n;
        label73:
        int i1;
        if (localToken.prefixOf(arrayOfToken2[(m - 1)]))
        {
          n = 0;
          i1 = 1 + arrayOfInt1[(k - 1)][m];
          int i2 = arrayOfInt1[k][(m - 1)];
          int i3 = i2 + 1;
          i4 = 0;
          if (i3 < i1)
          {
            i1 = i2 + 1;
            i4 = 1;
          }
          int i5 = arrayOfInt1[(k - 1)][(m - 1)];
          if (i5 + n < i1)
          {
            i1 = i5 + n;
            if (n != 0) {
              break label195;
            }
          }
        }
        label195:
        for (int i4 = 3;; i4 = 2)
        {
          arrayOfInt1[k][m] = i1;
          arrayOfInt2[k][m] = i4;
          m++;
          break;
          n = 1;
          break label73;
        }
      }
    }
    return arrayOfInt1[i][j];
  }
  
  public EditOperation[] getTargetOperations()
  {
    int i = this.mTarget.length;
    EditOperation[] arrayOfEditOperation = new EditOperation[i];
    int j = i;
    int k = this.mSource.length;
    int[][] arrayOfInt = this.mEditTypeTable;
    while (j > 0)
    {
      int m = arrayOfInt[k][j];
      switch (m)
      {
      default: 
        break;
      case 0: 
        k--;
        break;
      case 1: 
        j--;
        arrayOfEditOperation[j] = new EditOperation(m, k);
        break;
      case 2: 
      case 3: 
        j--;
        k--;
        arrayOfEditOperation[j] = new EditOperation(m, k);
      }
    }
    return arrayOfEditOperation;
  }
  
  public static final class EditOperation
  {
    private final int mPosition;
    private final int mType;
    
    public EditOperation(int paramInt1, int paramInt2)
    {
      this.mType = paramInt1;
      this.mPosition = paramInt2;
    }
    
    public int getPosition()
    {
      return this.mPosition;
    }
    
    public int getType()
    {
      return this.mType;
    }
  }
  
  public static final class Token
    implements CharSequence
  {
    private final char[] mContainer;
    public final int mEnd;
    public final int mStart;
    
    public Token(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      this.mContainer = paramArrayOfChar;
      this.mStart = paramInt1;
      this.mEnd = paramInt2;
    }
    
    public char charAt(int paramInt)
    {
      return this.mContainer[(paramInt + this.mStart)];
    }
    
    public int length()
    {
      return this.mEnd - this.mStart;
    }
    
    public boolean prefixOf(Token paramToken)
    {
      int i = length();
      if (i > paramToken.length()) {
        return false;
      }
      int j = this.mStart;
      int k = paramToken.mStart;
      char[] arrayOfChar1 = this.mContainer;
      char[] arrayOfChar2 = paramToken.mContainer;
      for (int m = 0;; m++)
      {
        if (m >= i) {
          break label77;
        }
        if (Character.toLowerCase(arrayOfChar1[(j + m)]) != Character.toLowerCase(arrayOfChar2[(k + m)])) {
          break;
        }
      }
      label77:
      return true;
    }
    
    public String subSequence(int paramInt1, int paramInt2)
    {
      return new String(this.mContainer, paramInt1 + this.mStart, length());
    }
    
    public String toString()
    {
      return subSequence(0, length());
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.shared.ui.LevenshteinDistance
 * JD-Core Version:    0.7.0.1
 */