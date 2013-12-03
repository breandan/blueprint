package com.google.android.voicesearch.fragments.executor;

import android.content.Intent;
import android.util.Log;

import com.google.android.search.core.util.SimpleCallbackFuture;
import com.google.android.shared.util.IntentStarter;
import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.Person;
import com.google.android.speech.contacts.PersonDisambiguation;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.voicesearch.fragments.action.EmailAction;
import com.google.android.voicesearch.util.EmailSender;
import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.common.base.Preconditions;

import java.util.concurrent.ExecutionException;

public class EmailActionExecutor
        extends CommunicationActionExecutor<EmailAction> {
    private final AccountHelper mAccountHelper;
    private final EmailSender mEmailSender;

    public EmailActionExecutor(IntentStarter paramIntentStarter, AccountHelper paramAccountHelper, EmailSender paramEmailSender) {
        super(paramIntentStarter);
        this.mAccountHelper = paramAccountHelper;
        this.mEmailSender = paramEmailSender;
    }

    private Intent[] getIntents(EmailAction paramEmailAction, boolean paramBoolean) {
        EmailSender.Email localEmail = new EmailSender.Email();
        PersonDisambiguation localPersonDisambiguation = paramEmailAction.getRecipient();
        Contact localContact;
        if ((localPersonDisambiguation != null) && (localPersonDisambiguation.isCompleted())) {
            localContact = ((Person) localPersonDisambiguation.get()).getSelectedItem();
        }
        for (; ; ) {
            if (localContact != null) {
                String[] arrayOfString = new String[1];
                arrayOfString[0] = localContact.toRfc822Token();
                localEmail.to = arrayOfString;
            }
            localEmail.subject = paramEmailAction.getSubject();
            localEmail.body = paramEmailAction.getBody();
            SimpleCallbackFuture localSimpleCallbackFuture = new SimpleCallbackFuture();
            this.mAccountHelper.getMainGmailAccount(localSimpleCallbackFuture);
            try {
                str = (String) localSimpleCallbackFuture.get();
                return this.mEmailSender.getIntents(localEmail, paramBoolean, str, false);
                localContact = null;
            } catch (InterruptedException localInterruptedException) {
                for (; ; ) {
                    Log.i("SelfNoteActionExecutor", "Unable to get account");
                    str = null;
                }
            } catch (ExecutionException localExecutionException) {
                for (; ; ) {
                    Log.i("SelfNoteActionExecutor", "Unable to get account");
                    String str = null;
                }
            }
        }
    }

    protected Intent[] getExecuteIntents(EmailAction paramEmailAction) {
        return getIntents(paramEmailAction, true);
    }

    protected Intent[] getOpenExternalAppIntents(EmailAction paramEmailAction) {
        PersonDisambiguation localPersonDisambiguation = paramEmailAction.getRecipient();
        if ((localPersonDisambiguation != null) && (!localPersonDisambiguation.isCompleted()) && (!localPersonDisambiguation.getCandidates().isEmpty())) {
            if (localPersonDisambiguation.getCandidates().size() == 1) {
            }
            for (boolean bool = true; ; bool = false) {
                Preconditions.checkState(bool);
                Intent[] arrayOfIntent = new Intent[1];
                arrayOfIntent[0] = PhoneActionUtils.getEditPersonIntent((Person) localPersonDisambiguation.getCandidates().get(0));
                return arrayOfIntent;
            }
        }
        return getIntents(paramEmailAction, false);
    }

    protected Intent[] getProberIntents(EmailAction paramEmailAction) {
        Intent[] arrayOfIntent = new Intent[1];
        arrayOfIntent[0] = EmailSender.createSimpleEmailIntent();
        return arrayOfIntent;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.executor.EmailActionExecutor

 * JD-Core Version:    0.7.0.1

 */