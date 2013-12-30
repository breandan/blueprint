package com.google.android.shared.util;

public class Whitespace {

    static int indexIn(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
        for (int i = paramInt1; i < paramInt2; i++) {
            if (matches(paramCharSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    static int indexOfNonMatchIn(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
        for (int i = paramInt1; i < paramInt2; i++) {
            if (!matches(paramCharSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    static int lastIndexOfNonMatchIn(CharSequence paramCharSequence, int paramInt1, int paramInt2) {
        for (int i = paramInt2 - 1; i >= paramInt1; i--) {
            if (!matches(paramCharSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean matches(char paramChar) {
        return Character.isWhitespace(paramChar);
    }

}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.shared.util.Whitespace

 * JD-Core Version:    0.7.0.1

 */