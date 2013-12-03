package com.google.android.voicesearch.util;

import android.content.Intent;
import android.net.Uri;

import com.google.majel.proto.EcoutezStructuredResponse.EcoutezLocalResult;

public class MapUtil {
    public static final Uri MAPS_URI = new Uri.Builder().scheme("http").authority("maps.google.com").build();

    private static Uri.Builder createMapsUriBuilder(int paramInt1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult2, int paramInt2) {
        Uri.Builder localBuilder = MAPS_URI.buildUpon().appendQueryParameter("sll", getLatLngParameter(paramEcoutezLocalResult2.getLatDegrees(), paramEcoutezLocalResult2.getLngDegrees())).appendQueryParameter("q", paramEcoutezLocalResult2.getTitle()).appendQueryParameter("near", paramEcoutezLocalResult2.getAddress());
        if (paramEcoutezLocalResult2.hasClusterId()) {
            localBuilder.appendQueryParameter("cid", paramEcoutezLocalResult2.getClusterId());
        }
        if ((2 == paramInt1) || (3 == paramInt1)) {
            if (paramEcoutezLocalResult1 != null) {
                localBuilder.appendQueryParameter("saddr", getLatLngParameter(paramEcoutezLocalResult1.getLatDegrees(), paramEcoutezLocalResult1.getLngDegrees()));
            }
            localBuilder.appendQueryParameter("daddr", paramEcoutezLocalResult2.getTitle());
            localBuilder.appendQueryParameter("dirflg", dirflagFromTransportationMethod(paramInt2));
            if (3 == paramInt1) {
                localBuilder.appendQueryParameter("noconfirm", "1");
                localBuilder.appendQueryParameter("nav", "1");
            }
        }
        return localBuilder;
    }

    private static String dirflagFromTransportationMethod(int paramInt) {
        switch (paramInt) {
            default:
                return "d";
            case 2:
                return "b";
            case 3:
                return "w";
        }
        return "r";
    }

    private static String getLatLngParameter(double paramDouble1, double paramDouble2) {
        return paramDouble1 + "," + paramDouble2;
    }

    public static Intent[] getMapsIntents(int paramInt1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult2, int paramInt2) {
        String str = getUrlForAction(paramInt1, paramEcoutezLocalResult2, paramInt2);
        if ((str != null) && (!str.isEmpty())) {
        }
        for (Uri.Builder localBuilder = Uri.parse(str).buildUpon(); ; localBuilder = createMapsUriBuilder(paramInt1, paramEcoutezLocalResult1, paramEcoutezLocalResult2, paramInt2)) {
            return getMapsIntentsHelper(localBuilder.build());
        }
    }

    public static Intent[] getMapsIntents(String paramString) {
        return getMapsIntentsHelper(Uri.parse(paramString).buildUpon().build());
    }

    private static Intent[] getMapsIntentsHelper(Uri paramUri) {
        Intent localIntent = new Intent("android.intent.action.VIEW").setData(paramUri);
        return new Intent[]{localIntent, new Intent(localIntent)};
    }

    public static Intent[] getMapsSearchIntents(String paramString) {
        Uri localUri = Uri.parse("http://maps.google.com/").buildUpon().appendQueryParameter("q", paramString).build();
        Intent localIntent = new Intent("android.intent.action.VIEW").setData(localUri);
        return new Intent[]{localIntent, new Intent(localIntent)};
    }

    public static Intent[] getProbeIntents() {
        return getMapsIntentsHelper(MAPS_URI);
    }

    private static String getUrlForAction(int paramInt1, EcoutezStructuredResponse.EcoutezLocalResult paramEcoutezLocalResult, int paramInt2) {
        if (4 == paramInt1) {
            return null;
        }
        switch (paramInt2) {
            default:
                return paramEcoutezLocalResult.getActionDrivingUrl();
            case 4:
                return paramEcoutezLocalResult.getActionTransitUrl();
            case 2:
                return paramEcoutezLocalResult.getActionBikingUrl();
        }
        return paramEcoutezLocalResult.getActionWalkingUrl();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.MapUtil

 * JD-Core Version:    0.7.0.1

 */