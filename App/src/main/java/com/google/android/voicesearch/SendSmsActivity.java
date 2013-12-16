package com.google.android.voicesearch;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.voicesearch.watchdog.TimeoutWatchdog;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class SendSmsActivity
        extends Activity {
    private SmsStatusReceiver mStatusReceiver;
    private TimeoutWatchdog mTimeoutWatchdog;

    private void fireFailure(Exception paramException, int paramInt) {
        if (paramException == null) {
            Log.e("SendSmsActivity", "failure sending sms, status:" + paramInt);
        }
        for (; ; ) {
            if (this.mTimeoutWatchdog != null) {
                this.mTimeoutWatchdog.stop();
            }
            showDialog(1);
            return;
            Log.e("SendSmsActivity", "failure sending sms, status:" + paramInt, paramException);
        }
    }

    private void fireSuccess() {
        setResult(-1);
        if (this.mTimeoutWatchdog != null) {
            this.mTimeoutWatchdog.stop();
        }
        finish();
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        Intent localIntent1 = getIntent();
        Uri localUri = localIntent1.getData();
        if (!"smsto".equals(localUri.getScheme())) {
            Log.e("SendSmsActivity", "unexpected data scheme, requires 'smsto'");
            finish();
        }
        for (; ; ) {
            return;
            localSmsManager = SmsManager.getDefault();
            String str1 = localUri.toString().substring(1 + "smsto".length());
            try {
                String str2 = URLDecoder.decode(str1, "UTF-8");
                String[] arrayOfString = str2.split(",");
                String str3 = localIntent1.getStringExtra("android.intent.extra.TEXT");
                if (str3 == null) {
                    str3 = "";
                }
                ArrayList localArrayList1 = localSmsManager.divideMessage(str3);
                int i = localArrayList1.size();
                this.mTimeoutWatchdog = new TimeoutWatchdog(60000, new Runnable() {
                    public void run() {
                        SendSmsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                SendSmsActivity.this.fireFailure(new TimeoutException("Send SMS timeout"), -1);
                            }
                        });
                    }
                });
                this.mTimeoutWatchdog.start();
                IntentFilter localIntentFilter = new IntentFilter("com.google.android.voicesearch.action.SMS_STATUS");
                this.mStatusReceiver = new SmsStatusReceiver(i * arrayOfString.length);
                registerReceiver(this.mStatusReceiver, localIntentFilter);
                Toast.makeText(this, 2131363463, 0).show();
                int j = arrayOfString.length;
                int k = 0;
                while (k < j) {
                    String str4 = arrayOfString[k];
                    Intent localIntent2 = new Intent("com.google.android.voicesearch.action.SMS_STATUS");
                    localIntent2.setPackage(getPackageName());
                    localIntent2.putExtra("com.google.android.voicesearch.extras.SMS_RECIPIENTS", str2);
                    localIntent2.putExtra("com.google.android.voicesearch.extras.SMS_MESSAGE", str3);
                    ArrayList localArrayList2 = new ArrayList(i);
                    for (int m = 0; m < i; m++) {
                        localArrayList2.add(PendingIntent.getBroadcast(this, 0, localIntent2, 1073741824));
                    }
                    try {
                        localSmsManager.sendMultipartTextMessage(str4, null, localArrayList1, localArrayList2, null);
                        k++;
                    } catch (Exception localException) {
                        for (; ; ) {
                            fireFailure(localException, -1);
                        }
                    }
                }
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
                Log.e("SendSmsActivity", "error decoding recipients");
                fireFailure(localUnsupportedEncodingException, -1);
                return;
            }
        }
    }

    protected Dialog onCreateDialog(int paramInt) {
        switch (paramInt) {
            default:
                return super.onCreateDialog(paramInt);
        }
        new AlertDialog.Builder(this, 2).setMessage(2131363464).setCancelable(false).setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface paramAnonymousDialogInterface) {
                paramAnonymousDialogInterface.dismiss();
                SendSmsActivity.this.setResult(0);
                SendSmsActivity.this.finish();
            }
        }).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                SendSmsActivity.this.setResult(0);
                SendSmsActivity.this.finish();
            }
        }).create();
    }

    public void onPause() {
        if (this.mStatusReceiver != null) {
            unregisterReceiver(this.mStatusReceiver);
            this.mStatusReceiver = null;
        }
        if (this.mTimeoutWatchdog != null) {
            this.mTimeoutWatchdog.stop();
        }
        super.onPause();
    }

    private class SmsStatusReceiver
            extends BroadcastReceiver {
        private int mNumMessages;

        public SmsStatusReceiver(int paramInt) {
            this.mNumMessages = paramInt;
        }

        private synchronized void registerSuccessfulMessage(Context paramContext, Intent paramIntent) {
                this.mNumMessages = (-1 + this.mNumMessages);
                SendSmsActivity.this.mTimeoutWatchdog.extend();
                if (this.mNumMessages < 1) {
                    String str = paramIntent.getStringExtra("com.google.android.voicesearch.extras.SMS_RECIPIENTS");
                    SmsUtils.insertSentSmsIntoDatabase(paramContext, paramIntent.getStringExtra("com.google.android.voicesearch.extras.SMS_MESSAGE"), str);
                    SendSmsActivity.this.fireSuccess();
                }
        }

        public void onReceive(Context paramContext, Intent paramIntent) {
            if (!"com.google.android.voicesearch.action.SMS_STATUS".equals(paramIntent.getAction())) {
                return;
            }
            int i = getResultCode();
            if (i != -1) {
                SendSmsActivity.this.fireFailure(null, i);
                return;
            }
            registerSuccessfulMessage(paramContext, paramIntent);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.SendSmsActivity

 * JD-Core Version:    0.7.0.1

 */
