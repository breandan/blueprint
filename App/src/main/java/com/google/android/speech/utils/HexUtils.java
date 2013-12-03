package com.google.android.speech.utils;

public class HexUtils {
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] paramArrayOfByte) {
        char[] arrayOfChar = new char[2 * paramArrayOfByte.length];
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            int j = paramArrayOfByte[i];
            int k = HEX_CHARS[(0xF & j >> 4)];
            int m = HEX_CHARS[(j & 0xF)];
            arrayOfChar[(i * 2)] = k;
            arrayOfChar[(1 + i * 2)] = m;
        }
        return new String(arrayOfChar);
    }

    public static byte[] hexToBytes(CharSequence paramCharSequence) {
        byte[] arrayOfByte = new byte[(1 + paramCharSequence.length()) / 2];
        if (paramCharSequence.length() == 0) {
            return arrayOfByte;
        }
        arrayOfByte[0] = 0;
        int i = paramCharSequence.length() % 2;
        int j = 0;
        label39:
        char c;
        if (j < paramCharSequence.length()) {
            c = paramCharSequence.charAt(j);
            if (!isHex(c)) {
                throw new IllegalArgumentException("string contains non-hex chars");
            }
            if (i % 2 != 0) {
                break label104;
            }
            arrayOfByte[(i >> 1)] = ((byte) (hexValue(c) << 4));
        }
        for (; ; ) {
            i++;
            j++;
            break label39;
            break;
            label104:
            int k = i >> 1;
            arrayOfByte[k] = ((byte) (arrayOfByte[k] + (byte) hexValue(c)));
        }
    }

    private static int hexValue(char paramChar) {
        if ((paramChar >= '0') && (paramChar <= '9')) {
            return paramChar - '0';
        }
        if ((paramChar >= 'a') && (paramChar <= 'f')) {
            return 10 + (paramChar - 'a');
        }
        return 10 + (paramChar - 'A');
    }

    private static boolean isHex(char paramChar) {
        return ((paramChar >= '0') && (paramChar <= '9')) || ((paramChar >= 'a') && (paramChar <= 'f')) || ((paramChar >= 'A') && (paramChar <= 'F'));
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.utils.HexUtils

 * JD-Core Version:    0.7.0.1

 */