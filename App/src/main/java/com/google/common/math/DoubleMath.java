package com.google.common.math;

import java.math.RoundingMode;

public final class DoubleMath
{
  static final double[] EVERY_SIXTEENTH_FACTORIAL = { 1.0D, 20922789888000.0D, 2.631308369336935E+035D, 1.241391559253607E+061D, 1.268869321858842E+089D, 7.156945704626381E+118D, 9.916779348709497E+149D, 1.974506857221074E+182D, 3.856204823625804E+215D, 5.550293832739304E+249D, 4.714723635992062E+284D };
  private static final double LN_2 = Math.log(2.0D);
  static final int MAX_FACTORIAL = 170;
  
  public static boolean isMathematicalInteger(double paramDouble)
  {
    return (DoubleUtils.isFinite(paramDouble)) && ((paramDouble == 0.0D) || (52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(paramDouble)) <= DoubleUtils.getExponent(paramDouble)));
  }
  
  static double roundIntermediate(double paramDouble, RoundingMode paramRoundingMode)
  {
    if (!DoubleUtils.isFinite(paramDouble)) {
      throw new ArithmeticException("input is infinite or NaN");
    }
    switch (1.$SwitchMap$java$math$RoundingMode[paramRoundingMode.ordinal()])
    {
    default: 
      throw new AssertionError();
    case 1: 
      MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(paramDouble));
    }
    double d1;
    do
    {
      double d2;
      do
      {
        do
        {
          do
          {
            do
            {
              do
              {
                return paramDouble;
              } while (paramDouble >= 0.0D);
              return Math.floor(paramDouble);
            } while (paramDouble < 0.0D);
            return Math.ceil(paramDouble);
            if (paramDouble >= 0.0D) {}
            for (double d4 = Math.ceil(paramDouble);; d4 = Math.floor(paramDouble)) {
              return d4;
            }
            return Math.rint(paramDouble);
          } while (isMathematicalInteger(paramDouble));
          if (paramDouble >= 0.0D) {}
          for (double d3 = paramDouble + 0.5D;; d3 = paramDouble - 0.5D) {
            return d3;
          }
        } while (isMathematicalInteger(paramDouble));
        if (paramDouble < 0.0D) {
          break;
        }
        d2 = paramDouble + 0.5D;
      } while (d2 == paramDouble);
      return DoubleUtils.next(d2, false);
      d1 = paramDouble - 0.5D;
    } while (d1 == paramDouble);
    return DoubleUtils.next(d1, true);
  }
  
  public static int roundToInt(double paramDouble, RoundingMode paramRoundingMode)
  {
    int i = 1;
    double d = roundIntermediate(paramDouble, paramRoundingMode);
    int j;
    if (d > -2147483649.0D)
    {
      j = i;
      if (d >= 2147483648.0D) {
        break label47;
      }
    }
    for (;;)
    {
      MathPreconditions.checkInRange(i & j);
      return (int)d;
      j = 0;
      break;
      label47:
      i = 0;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.common.math.DoubleMath
 * JD-Core Version:    0.7.0.1
 */