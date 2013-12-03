package com.google.android.speech.internal;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.speech.callback.Callback;
import com.google.android.speech.embedded.Greco3Mode;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.grammar.GrammarBuilder;
import com.google.android.speech.message.S3ResponseBuilder;
import com.google.common.base.Preconditions;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.ContactPhoneNumber;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.speech.s3.S3.S3Response;

import java.util.List;

public class RecognizerEventProcessor {
    private static int INVALID_PHONE_TYPE = -1;
    private final Callback<S3.S3Response, RecognizeException> mCallback;
    private final CombinedResultGenerator mCombinedResultGenerator;
    private final Greco3Mode mMode;

    RecognizerEventProcessor(Greco3Mode paramGreco3Mode, Callback<S3.S3Response, RecognizeException> paramCallback) {
        this.mMode = paramGreco3Mode;
        this.mCallback = ((Callback) Preconditions.checkNotNull(paramCallback));
        this.mCombinedResultGenerator = new CombinedResultGenerator();
    }

    private static RecognizerProtos.RecognitionEvent addCombinedResultsTo(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        RecognizerProtos.RecognitionEvent localRecognitionEvent = new RecognizerProtos.RecognitionEvent();
        try {
            localRecognitionEvent.mergeFrom(paramRecognitionEvent.toByteArray());
            label17:
            localRecognitionEvent.setCombinedResult(paramRecognitionEvent.getResult());
            return localRecognitionEvent;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
            break label17;
        }
    }

    private static ActionV2Protos.PhoneAction buildPhoneAction(String paramString, int paramInt, double paramDouble) {
        ActionV2Protos.ActionContact localActionContact = new ActionV2Protos.ActionContact();
        localActionContact.setName(paramString);
        localActionContact.setParsedName(paramString);
        EmbeddedAction.EmbeddedActionContact localEmbeddedActionContact = new EmbeddedAction.EmbeddedActionContact();
        localEmbeddedActionContact.setGrammarWeight(paramDouble);
        localActionContact.setEmbeddedActionContactExtension(localEmbeddedActionContact);
        Log.i("VS.RecognizerEventProcessor", "n=" + elideContactName(paramString) + ", t=" + paramInt);
        if (paramInt != INVALID_PHONE_TYPE) {
            ActionV2Protos.ContactPhoneNumber localContactPhoneNumber = new ActionV2Protos.ContactPhoneNumber();
            String str = getServerTypeStringFromAndroidType(paramInt);
            if (str != null) {
                localContactPhoneNumber.setType(str);
            }
            localActionContact.addPhone(localContactPhoneNumber);
        }
        ActionV2Protos.PhoneAction localPhoneAction = new ActionV2Protos.PhoneAction();
        localPhoneAction.addContact(localActionContact);
        return localPhoneAction;
    }

    private S3.S3Response dressActionV2InMajelResponseAndThenDressThatInAnS3Response(ActionV2Protos.ActionV2 paramActionV2) {
        PeanutProtos.Peanut localPeanut = new PeanutProtos.Peanut();
        localPeanut.setPrimaryType(6);
        localPeanut.addActionV2(paramActionV2);
        localPeanut.setSearchResultsUnnecessary(true);
        MajelProtos.MajelResponse localMajelResponse = new MajelProtos.MajelResponse();
        localMajelResponse.addPeanut(localPeanut);
        return S3ResponseBuilder.createWithMajel(localMajelResponse);
    }

    private static String elideContactName(String paramString) {
        if (paramString.length() > 2) {
            return paramString.substring(0, 2) + "+" + (-2 + paramString.length());
        }
        return "+" + paramString.length();
    }

    private static final String getServerTypeStringFromAndroidType(int paramInt) {
        if (paramInt == 1) {
            return "home";
        }
        if (paramInt == 3) {
            return "work";
        }
        if (paramInt == 2) {
            return "cell";
        }
        return null;
    }

    private S3.S3Response handleCallCommand(String[] paramArrayOfString, int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder(15);
        String str1 = null;
        double d = 0.0D;
        int i = INVALID_PHONE_TYPE;
        String str2;
        for (int j = paramInt; ; j++) {
            if (j >= paramArrayOfString.length) {
                break label152;
            }
            str2 = paramArrayOfString[j];
            if (str2.startsWith("XX_")) {
                str1 = GrammarBuilder.decodeName(str2);
                d = GrammarBuilder.decodeWeight(str2);
            }
            if (str2.startsWith("_p")) {
            }
            try {
                int k = Integer.parseInt(str2.substring("_p".length()));
                i = k;
            } catch (NumberFormatException localNumberFormatException) {
                for (; ; ) {
                    Log.e("VS.RecognizerEventProcessor", "Invalid semantic tag: " + str2);
                }
            }
            if (str2.startsWith("_d")) {
                localStringBuilder.append(str2.substring("_d".length()));
            }
        }
        label152:
        if (str1 != null) {
            return handleContactName(str1, d, i);
        }
        if (localStringBuilder.length() > 0) {
            return handleSpokenPhoneNumber(localStringBuilder.toString());
        }
        return null;
    }

    private S3.S3Response handleContactName(String paramString, double paramDouble, int paramInt) {
        ActionV2Protos.PhoneAction localPhoneAction = buildPhoneAction(paramString, paramInt, paramDouble);
        ActionV2Protos.ActionV2 localActionV2 = new ActionV2Protos.ActionV2();
        localActionV2.setPhoneActionExtension(localPhoneAction).setExecute(true);
        return dressActionV2InMajelResponseAndThenDressThatInAnS3Response(localActionV2);
    }

    private S3.S3Response handleSpokenPhoneNumber(String paramString) {
        ActionV2Protos.ContactPhoneNumber localContactPhoneNumber = new ActionV2Protos.ContactPhoneNumber();
        localContactPhoneNumber.setNumber(paramString);
        return dressActionV2InMajelResponseAndThenDressThatInAnS3Response(new ActionV2Protos.ActionV2().setPhoneActionExtension(new ActionV2Protos.PhoneAction().addContact(new ActionV2Protos.ActionContact().addPhone(localContactPhoneNumber))));
    }

    private void processEventInDictationAndHotwordMode(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        this.mCombinedResultGenerator.update(paramRecognitionEvent);
        if (paramRecognitionEvent.getEventType() == 1) {
            RecognizerProtos.RecognitionEvent localRecognitionEvent = this.mCombinedResultGenerator.getCombinedResultEvent();
            if (localRecognitionEvent != null) {
                this.mCallback.onResult(S3ResponseBuilder.createInProgress(localRecognitionEvent));
            }
            this.mCallback.onResult(S3ResponseBuilder.createDone());
            return;
        }
        this.mCallback.onResult(S3ResponseBuilder.createInProgress(paramRecognitionEvent));
    }

    private void processEventInGrammarMode(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (paramRecognitionEvent.getEventType() == 1) {
            S3.S3Response localS3Response = processSemanticInterpretations(paramRecognitionEvent);
            if (localS3Response != null) {
                this.mCallback.onResult(S3ResponseBuilder.createInProgress(addCombinedResultsTo(paramRecognitionEvent)));
                this.mCallback.onResult(localS3Response);
                this.mCallback.onResult(S3ResponseBuilder.createDone());
            }
        } else {
            return;
        }
        this.mCallback.onError(new Greco3RecognitionEngine.NoMatchesFromEmbeddedRecognizerException());
    }

    private S3.S3Response processSemanticInterpretations(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if (!paramRecognitionEvent.hasResult()) {
        }
        for (; ; ) {
            return null;
            RecognizerProtos.RecognitionResult localRecognitionResult = paramRecognitionEvent.getResult();
            int i = localRecognitionResult.getHypothesisCount();
            if (i >= 1) {
                for (int j = 0; j < i; j++) {
                    RecognizerProtos.Hypothesis localHypothesis = localRecognitionResult.getHypothesis(j);
                    if (localHypothesis.hasSemanticResult()) {
                        return processSemanticInterpretations(localHypothesis.getSemanticResult().getInterpretationList(), paramRecognitionEvent);
                    }
                }
            }
        }
    }

    private S3.S3Response processSemanticInterpretations(List<InterpretationProto.Interpretation> paramList, RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if ((paramList.isEmpty()) || (((InterpretationProto.Interpretation) paramList.get(0)).getSlotCount() == 0)) {
            return null;
        }
        InterpretationProto.Slot localSlot = ((InterpretationProto.Interpretation) paramList.get(0)).getSlot(0);
        if ((!localSlot.hasValue()) || (TextUtils.isEmpty(localSlot.getValue().trim()))) {
            return null;
        }
        String[] arrayOfString = localSlot.getValue().trim().split(" ");
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            if (str != null) {
                if ("_call".equals(str)) {
                    return handleCallCommand(arrayOfString, i + 1);
                }
                if (("_cancel".equals(str) | "_okay".equals(str) | "_call_back".equals(str) | "_respond".equals(str) | "_hotword".equals(str) | "_next".equals(str) | str.startsWith("_select"))) {
                    return S3ResponseBuilder.createWithRecognitionEvent(paramRecognitionEvent);
                }
                if ("_other".equals(str)) {
                    return null;
                }
            }
        }
        return null;
    }

    void process(RecognizerProtos.RecognitionEvent paramRecognitionEvent) {
        if ((this.mMode == Greco3Mode.DICTATION) || (this.mMode == Greco3Mode.HOTWORD) || (this.mMode == Greco3Mode.GRAMMAR)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            if (paramRecognitionEvent.hasEventType()) {
                break;
            }
            Log.w("VS.RecognizerEventProcessor", "Received recognition event without type.");
            return;
        }
        if (paramRecognitionEvent.getStatus() != 0) {
            Log.w("VS.RecognizerEventProcessor", "Error from embedded recognizer.");
            return;
        }
        if (this.mMode == Greco3Mode.GRAMMAR) {
            processEventInGrammarMode(paramRecognitionEvent);
            return;
        }
        processEventInDictationAndHotwordMode(paramRecognitionEvent);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.internal.RecognizerEventProcessor

 * JD-Core Version:    0.7.0.1

 */