package com.google.android.voicesearch.greco3.languagepack;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ZipDownloadReceiver
        extends BroadcastReceiver {
    private boolean startIfZip(Context paramContext, DownloadManager paramDownloadManager, long paramLong) {
        if ("application/zip".equals(paramDownloadManager.getMimeTypeForDownloadedFile(paramLong))) {
            InstallActivity.start(paramContext);
            return true;
        }
        return false;
    }

    public void onReceive(Context paramContext, Intent paramIntent) {
        if ("android.intent.action.DOWNLOAD_COMPLETE".equals(paramIntent.getAction())) {
            ZipDownloadProcessorService.start(paramContext, paramIntent);
        }
        DownloadManager localDownloadManager;
        long l;
        do {
            for (; ; ) {
                return;
                if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(paramIntent.getAction())) {
                    localDownloadManager = (DownloadManager) paramContext.getSystemService("download");
                    long[] arrayOfLong = paramIntent.getLongArrayExtra("extra_click_download_ids");
                    if (arrayOfLong == null) {
                        break;
                    }
                    int i = arrayOfLong.length;
                    for (int j = 0; (j < i) && (!startIfZip(paramContext, localDownloadManager, arrayOfLong[j])); j++) {
                    }
                }
            }
            l = paramIntent.getLongExtra("extra_download_id", -1L);
        } while (l == -1L);
        startIfZip(paramContext, localDownloadManager, l);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.ZipDownloadReceiver

 * JD-Core Version:    0.7.0.1

 */