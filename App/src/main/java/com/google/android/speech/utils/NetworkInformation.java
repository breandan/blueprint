package com.google.android.speech.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NetworkInformation {
    @Nullable
    private final ConnectivityManager mConnectivityManager;
    @Nullable
    private final TelephonyManager mTelephonyManager;

    public NetworkInformation(@Nullable TelephonyManager paramTelephonyManager, @Nullable ConnectivityManager paramConnectivityManager) {
        this.mTelephonyManager = paramTelephonyManager;
        this.mConnectivityManager = paramConnectivityManager;
    }

    private static int tryParse(String paramString, int paramInt) {
        try {
            int i = Integer.parseInt(paramString);
            return i;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramInt;
    }

    public int getConnectionId() {
        if (mConnectivityManager == null) {
            return -0x1;
        }
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if ((networkInfo == null) || (!networkInfo.isConnected())) {
            return 0x0;
        }
        if (networkInfo.getType() == 0x1) {
            return 0x1;
        }
        if (networkInfo.getType() == 0x6) {
            return 0x13;
        }
        if (networkInfo.getType() == 0x7) {
            return 0x11;
        }
        if (networkInfo.getType() == 0x9) {
            return 0x12;
        }
        if (networkInfo.getType() == 0) {
            if (mTelephonyManager != null) {
                switch (mTelephonyManager.getNetworkType()) {
                    case 11: {
                        return 0x2;
                    }
                    case 8: {
                        return 0x3;
                    }
                    case 6: {
                        return 0x4;
                    }
                    case 18: {
                        return 0x5;
                    }
                    case 9: {
                        return 0x6;
                    }
                    case 10: {
                        return 0x7;
                    }
                    case 16: {
                        return 0x8;
                    }
                    case 5: {
                        return 0x9;
                    }
                    case 12: {
                        return 0xa;
                    }
                    case 14: {
                        return 0xb;
                    }
                    case 19: {
                        return 0xc;
                    }
                    case 13: {
                        return 0xd;
                    }
                    case 15: {
                        return 0xe;
                    }
                    case 17: {
                        return 0xf;
                    }
                    case 7: {
                        return 0x10;
                    }
                }
            }
        }
        return 0x0;
    }

    public int getSimMcc() {
        if(mTelephonyManager == null) {
            return -0x1;
        }
        String simOperator = mTelephonyManager.getSimOperator();
        if((simOperator != null) && (simOperator.length() > 0x3)) {
            return tryParse(simOperator.substring(0x0, 0x3), -0x1);
        }
        return -0x1;
    }

    public boolean isConnected() {
        if (mConnectivityManager == null) {
            return false;
        }
        NetworkInfo ni = mConnectivityManager.getActiveNetworkInfo();
        if ((ni == null) || (!ni.isConnected())) {
            return true;
        }
        return true;
    }

    public boolean isConnectedUnmetered() {
        boolean bool;
        if (Build.VERSION.SDK_INT >= 16) {
            bool = this.mConnectivityManager.isActiveNetworkMetered();
            if ((isConnected()) && (!bool)) {
                return true;
            }
        } else {
            NetworkInfo localNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            if ((localNetworkInfo != null) && (localNetworkInfo.getType() != 1) && (localNetworkInfo.getType() != 6)) {
                return true;
            }
        }
        return false;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.utils.NetworkInformation

 * JD-Core Version:    0.7.0.1

 */