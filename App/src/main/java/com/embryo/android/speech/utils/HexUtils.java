package com.embryo.android.speech.utils;

public class HexUtils {
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] chars = new char[(bytes.length * 0x2)];
        for(int i = 0x0; i < bytes.length; i = i + 0x1) {
            byte b = bytes[i];
            char c1 = HEX_CHARS[((b >> 0x4) & 0xf)];
            char c2 = HEX_CHARS[(b & 0xf)];
            chars[(i * 0x2)] = c1;
            chars[((i * 0x2) + 0x1)] = c2;
        }
        return new String(chars);
    }

    public static byte[] hexToBytes(CharSequence str) {
        byte[] bytes = new byte[((str.length() + 0x1) / 0x2)];
        if(str.length() == 0) {
            return bytes;
        }
        bytes[0x0] = 0x0;
        int nibbleIdx = str.length() % 0x2;
        for(int i = 0x0; i < str.length(); i = i + 0x1) {
            char c = str.charAt(i);
            if(!isHex(c)) {
                throw new IllegalArgumentException("string contains non-hex chars");
            }
            if((nibbleIdx % 0x2) == 0) {
                bytes[(nibbleIdx >> 0x1)] = (byte)(hexValue(c) << 0x4);
            } else {
                bytes[(nibbleIdx >> 0x1)] = (byte)(bytes[(nibbleIdx >> 0x1)] + (byte)hexValue(c));
            }
            nibbleIdx = nibbleIdx + 0x1;
        }
        return bytes;
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

 * Qualified Name:     HexUtils

 * JD-Core Version:    0.7.0.1

 */