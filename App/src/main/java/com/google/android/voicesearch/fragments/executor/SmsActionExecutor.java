package com.google.android.voicesearch.fragments.executor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.velvet.actions.Disambiguation;
import com.google.android.voicesearch.fragments.action.SmsAction;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;

public class SmsActionExecutor
        extends CommunicationActionExecutor<SmsAction> {
    private final String mGoogleSearchPackage;
    private final IntentStarter.ResultCallback mSendResultCallback = new IntentStarter.ResultCallback() {
        public void onResult(int paramAnonymousInt, Intent paramAnonymousIntent, Context paramAnonymousContext) {
        }
    };

    public SmsActionExecutor(IntentStarter paramIntentStarter, String paramString) {
        super(paramIntentStarter);
        this.mGoogleSearchPackage = paramString;
    }

    public boolean execute(SmsAction paramSmsAction) {
        return this.mIntentStarter.startActivityForResult(getExecuteIntents(paramSmsAction)[0], this.mSendResultCallback);
    }

    protected Intent[] getExecuteIntents(SmsAction paramSmsAction) {
        Intent localIntent = new Intent();
        localIntent.setAction("com.google.android.apps.googlevoice.action.AUTO_SEND");
        PersonDisambiguation localPersonDisambiguation = paramSmsAction.getRecipient();
        String[] arrayOfString;
        if (Disambiguation.isCompleted(localPersonDisambiguation)) {
            arrayOfString = new String[1];
            arrayOfString[0] = ((Person) localPersonDisambiguation.get()).getSelectedItem().getValue();
        }
        for (; ; ) {
            localIntent.setData(Uri.fromParts("smsto", TextUtils.join(",", arrayOfString), null));
            localIntent.putExtra("android.intent.extra.TEXT", paramSmsAction.getBody());
            if (Build.VERSION.SDK_INT >= 19) {
                localIntent.setPackage(this.mGoogleSearchPackage);
            }
            return new Intent[]{localIntent};
            arrayOfString = new String[0];
        }
    }

    protected Intent[] getOpenExternalAppIntents(SmsAction paramSmsAction) {
        PersonDisambiguation localPersonDisambiguation = paramSmsAction.getRecipient();
        if (Disambiguation.isCompleted(localPersonDisambiguation)) {
            Intent[] arrayOfIntent3 = new Intent[1];
            arrayOfIntent3[0] = PhoneActionUtils.getSendSmsIntent(((Person) localPersonDisambiguation.get()).getSelectedItem().getValue(), paramSmsAction.getBody());
            return arrayOfIntent3;
        }
        if ((localPersonDisambiguation != null) && (!localPersonDisambiguation.getCandidates().isEmpty())) {
            if (localPersonDisambiguation.getCandidates().size() == 1) {
            }
            for (boolean bool = true; ; bool = false) {
                Preconditions.checkState(bool);
                Intent[] arrayOfIntent2 = new Intent[1];
                arrayOfIntent2[0] = PhoneActionUtils.getEditPersonIntent((Person) localPersonDisambiguation.getCandidates().get(0));
                return arrayOfIntent2;
            }
        }
        Intent[] arrayOfIntent1 = new Intent[1];
        if (Disambiguation.isCompleted(localPersonDisambiguation)) {
        }
        for (String str = ((Person) localPersonDisambiguation.get()).getSelectedItem().getValue(); ; str = null) {
            arrayOfIntent1[0] = PhoneActionUtils.getSendSmsIntent(str, paramSmsAction.getBody());
            return arrayOfIntent1;
        }
    }

    protected Intent[] getProberIntents(SmsAction paramSmsAction) {
        Intent[] arrayOfIntent = new Intent[1];
        arrayOfIntent[0] = new Intent().setAction("com.google.android.apps.googlevoice.action.AUTO_SEND").setData(Uri.fromParts("smsto", "0123456789", null));
        return arrayOfIntent;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.SmsActionExecutor

 * JD-Core Version:    0.7.0.1

 */