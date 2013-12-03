package com.google.android.voicesearch.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.helper.AccountHelper;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class EmailSender {
    private final AccountHelper mAccountHelper;
    private final PackageManager mPackageManager;

    public EmailSender(AccountHelper paramAccountHelper, PackageManager paramPackageManager) {
        this.mAccountHelper = paramAccountHelper;
        this.mPackageManager = paramPackageManager;
    }

    private void addCommonExtras(Email paramEmail, Intent paramIntent) {
        if (paramEmail.to != null) {
            paramIntent.putExtra("android.intent.extra.EMAIL", paramEmail.to);
        }
        if (paramEmail.cc != null) {
            paramIntent.putExtra("android.intent.extra.CC", paramEmail.cc);
        }
        if (paramEmail.bcc != null) {
            paramIntent.putExtra("android.intent.extra.BCC", paramEmail.bcc);
        }
        if (paramEmail.subject != null) {
            paramIntent.putExtra("android.intent.extra.SUBJECT", paramEmail.subject.toString());
        }
        if (paramEmail.body != null) {
            paramIntent.putExtra("android.intent.extra.TEXT", paramEmail.body.toString());
        }
        if (paramEmail.attachment != null) {
            paramIntent.putExtra("android.intent.extra.STREAM", paramEmail.attachment);
        }
    }

    private Intent createGMailIntent(Email paramEmail, boolean paramBoolean1, String paramString, boolean paramBoolean2) {
        Intent localIntent = createSimpleGmailIntent(paramBoolean1, paramBoolean2);
        localIntent.putExtra("com.google.android.gm.extra.ACCOUNT", paramString);
        localIntent.putExtra("account", paramString);
        addCommonExtras(paramEmail, localIntent);
        return localIntent;
    }

    public static Intent createSimpleEmailIntent() {
        Intent localIntent = new Intent("android.intent.action.SENDTO");
        localIntent.setData(Uri.fromParts("mailto", "", null));
        return localIntent;
    }

    private Intent createSimpleGmailIntent(boolean paramBoolean1, boolean paramBoolean2) {
        String str;
        Intent localIntent;
        if (paramBoolean1) {
            str = "com.google.android.gm.action.AUTO_SEND";
            localIntent = new Intent(str);
            if (!paramBoolean2) {
                break label68;
            }
            if (installedGmailSupportsNoteCategory()) {
                break label53;
            }
            localIntent.setPackage("com.google.android.gm");
        }
        for (; ; ) {
            localIntent.setType("text/plain");
            return localIntent;
            str = "android.intent.action.SEND";
            break;
            label53:
            if (!paramBoolean1) {
                localIntent.addCategory("com.google.android.voicesearch.SELF_NOTE");
                continue;
                label68:
                localIntent.setPackage("com.google.android.gm");
            }
        }
    }

    private boolean installedGmailSupportsNoteCategory() {
        Intent localIntent = new Intent("android.intent.action.SEND");
        localIntent.setType("text/plain");
        localIntent.addCategory("com.google.android.voicesearch.SELF_NOTE");
        localIntent.setPackage("com.google.android.gm");
        ResolveInfo localResolveInfo = this.mPackageManager.resolveActivity(localIntent, 0);
        boolean bool = false;
        if (localResolveInfo != null) {
            bool = true;
        }
        return bool;
    }

    private void sendEmail(Email paramEmail, boolean paramBoolean1, String paramString, boolean paramBoolean2, IntentStarter paramIntentStarter, SimpleCallback<Boolean> paramSimpleCallback) {
        boolean bool = sendEmail(paramEmail, paramBoolean1, paramString, paramBoolean2, paramIntentStarter);
        if (paramSimpleCallback != null) {
            paramSimpleCallback.onResult(Boolean.valueOf(bool));
        }
    }

    private boolean sendEmail(Email paramEmail, boolean paramBoolean1, String paramString, boolean paramBoolean2, IntentStarter paramIntentStarter) {
        return paramIntentStarter.startActivity(getIntents(paramEmail, paramBoolean1, paramString, paramBoolean2));
    }

    public Intent createEmailIntent(Email paramEmail) {
        Intent localIntent = createSimpleEmailIntent();
        addCommonExtras(paramEmail, localIntent);
        return localIntent;
    }

    public Intent[] createSelfNoteProbeIntents() {
        Intent[] arrayOfIntent = new Intent[2];
        arrayOfIntent[0] = createSimpleGmailIntent(true, true);
        arrayOfIntent[1] = createSimpleGmailIntent(false, true);
        return arrayOfIntent;
    }

    public Intent[] getIntents(Email paramEmail, boolean paramBoolean1, String paramString, boolean paramBoolean2) {
        ArrayList localArrayList = Lists.newArrayList();
        if ((paramBoolean1) && (paramString != null) && (paramEmail.to != null)) {
            localArrayList.add(createGMailIntent(paramEmail, true, paramString, paramBoolean2));
        }
        localArrayList.add(createGMailIntent(paramEmail, false, paramString, paramBoolean2));
        localArrayList.add(createEmailIntent(paramEmail));
        return (Intent[]) localArrayList.toArray(new Intent[localArrayList.size()]);
    }

    public void sendEmail(final Email paramEmail, final boolean paramBoolean, final IntentStarter paramIntentStarter, @Nullable final SimpleCallback<Boolean> paramSimpleCallback) {
        this.mAccountHelper.getMainGmailAccount(new SimpleCallback() {
            public void onResult(String paramAnonymousString) {
                EmailSender.this.sendEmail(paramEmail, paramBoolean, paramAnonymousString, false, paramIntentStarter, paramSimpleCallback);
            }
        });
    }

    public static class Email {
        public Uri attachment;
        public String[] bcc;
        public CharSequence body;
        public String[] cc;
        public CharSequence subject;
        public String[] to;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.EmailSender

 * JD-Core Version:    0.7.0.1

 */