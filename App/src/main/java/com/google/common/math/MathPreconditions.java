package com.google.common.math;

final class MathPreconditions
{
  static void checkInRange(boolean paramBoolean)
  {
    if (!paramBoolean) {
      throw new ArithmeticException("not in range");
    }
  }
  
  static void checkRoundingUnnecessary(boolean paramBoolean)
  {
    if (!paramBoolean) {
      throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.math.MathPreconditions
 * JD-Core Version:    0.7.0.1
 */