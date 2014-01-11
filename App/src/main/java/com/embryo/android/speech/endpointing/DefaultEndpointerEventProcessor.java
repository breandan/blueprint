package com.embryo.android.speech.endpointing;

import android.util.Log;

import com.embryo.android.shared.util.StateMachine;
import com.embryo.android.speech.listeners.RecognitionEventListener;
import com.embryo.speech.recognizer.api.RecognizerProtos;
import com.google.common.base.Preconditions;

public class DefaultEndpointerEventProcessor
        implements EndpointerEventProcessor {
    private final RecognitionEventListener mEndpointerListener;
    private final StateMachine<DefaultEndpointerEventProcessor.State> mStateMachine = StateMachine.newBuilder("VS.DefaultEndpointerEventProcessor", DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED).addTransition(DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED, DefaultEndpointerEventProcessor.State.SPEECH_DETECTED).addTransition(DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED, DefaultEndpointerEventProcessor.State.END_OF_SPEECH).addTransition(DefaultEndpointerEventProcessor.State.SPEECH_DETECTED, DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH).addTransition(DefaultEndpointerEventProcessor.State.SPEECH_DETECTED, DefaultEndpointerEventProcessor.State.END_OF_SPEECH).addTransition(DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH, DefaultEndpointerEventProcessor.State.SPEECH_DETECTED).addTransition(DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH, DefaultEndpointerEventProcessor.State.END_OF_SPEECH).setDebug(false).build();
    private long mEndOfSpeechTriggerMs;
    private com.embryo.wireless.voicesearch.proto.GstaticConfiguration.EndpointerParams mEndpointerParams;

    public DefaultEndpointerEventProcessor(RecognitionEventListener epListener, com.embryo.wireless.voicesearch.proto.GstaticConfiguration.EndpointerParams endpointerParams) {
        mEndpointerListener = Preconditions.checkNotNull(epListener);
        mEndpointerParams = endpointerParams;
    }

    public void process(RecognizerProtos.EndpointerEvent event) {
        if ((event == null) || (!event.hasEventType())) {
            Log.w("VS.DefaultEndpointerEventProcessor", "Received EP event without type.");
            return;
        }
        if (!mStateMachine.isIn(DefaultEndpointerEventProcessor.State.END_OF_SPEECH)) {
            int type = event.getEventType();
            if (type == 0) {
                if (processStartOfSpeech()) {
                    mEndpointerListener.onBeginningOfSpeech((event.getTimeUsec() / 0x3e8));
                }
                return;
            }
            if (type == 0x1) {
                if (processEndOfSpeech((event.getTimeUsec() / 0x3e8))) {
                    mEndpointerListener.onEndOfSpeech();
                }
                return;
            }
            if (type == 0x2) {
                if (processEndOfAudioAsEndOfSpeech()) {
                    mEndpointerListener.onEndOfSpeech();
                }
                if (processEndOfAudioAsNoSpeechDetected()) {
                    mEndpointerListener.onNoSpeechDetected();
                }
            }
        }
    }

    public synchronized void updateProgress(int engine, long progressMs) {
        if (shouldTriggerEndOfSpeech(progressMs)) {
            mEndpointerListener.onEndOfSpeech();
        }
        if (shouldTriggerNoSpeechDetected(progressMs)) {
            mEndpointerListener.onNoSpeechDetected();
        }
    }

    private synchronized boolean processStartOfSpeech() {
        if (mStateMachine.isIn(DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED)) {
            mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.SPEECH_DETECTED);
            return true;
        }
        if (mStateMachine.isIn(DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH)) {
            mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.SPEECH_DETECTED);
        }
        return false;
    }

    private synchronized boolean processEndOfSpeech(long timeMs) {
        if (mEndpointerParams.getExtraSilenceAfterEndOfSpeechMsec() > 0) {
            mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH);
            setEndOfSpeechTriggerPoint(((long) mEndpointerParams.getExtraSilenceAfterEndOfSpeechMsec() + timeMs));
            return false;
        }
        mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.END_OF_SPEECH);
        return true;
    }

    private synchronized boolean processEndOfAudioAsEndOfSpeech() {
        if (!mStateMachine.isIn(DefaultEndpointerEventProcessor.State.SPEECH_DETECTED)) {
            return false;
        }
        mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.END_OF_SPEECH);
        return true;
    }

    private synchronized boolean processEndOfAudioAsNoSpeechDetected() {
        if (!mStateMachine.isIn(DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED)) {
            return false;
        }
        mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.END_OF_SPEECH);
        return true;
    }

    private synchronized void setEndOfSpeechTriggerPoint(long timeMs) {
        mEndOfSpeechTriggerMs = timeMs;
    }

    private synchronized boolean shouldTriggerEndOfSpeech(long progressMs) {
        if ((progressMs <= mEndOfSpeechTriggerMs) || (!mStateMachine.isIn(DefaultEndpointerEventProcessor.State.DELAY_END_OF_SPEECH))) {
            return false;
        }
        mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.END_OF_SPEECH);
        return true;
    }

    private synchronized boolean shouldTriggerNoSpeechDetected(long progressMs) {
        if ((!mStateMachine.isIn(DefaultEndpointerEventProcessor.State.NO_SPEECH_DETECTED)) || (progressMs <= (long) mEndpointerParams.getNoSpeechDetectedTimeoutMsec())) {
            return false;
        }
        mStateMachine.moveTo(DefaultEndpointerEventProcessor.State.END_OF_SPEECH);
        return true;
    }

    private static enum State {
        DELAY_END_OF_SPEECH, END_OF_SPEECH, NO_SPEECH_DETECTED, SPEECH_DETECTED
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     DefaultEndpointerEventProcessor

 * JD-Core Version:    0.7.0.1

 */
