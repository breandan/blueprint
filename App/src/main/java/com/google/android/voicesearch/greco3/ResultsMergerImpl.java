package com.google.android.voicesearch.greco3;

import android.util.Log;

import com.google.android.shared.util.Clock;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.shared.util.StateMachine;
import com.google.android.speech.EngineSelector;
import com.google.android.speech.RecognitionResponse;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.dispatcher.RecognitionDispatcher;
import com.google.android.speech.exception.EmbeddedRecognizeException;
import com.google.android.speech.exception.NetworkRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.speech.recognizer.api.RecognizerProtos;
import com.google.speech.s3.S3;
import com.google.speech.s3.S3.S3Response;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class ResultsMergerImpl
        implements RecognitionDispatcher.ResultsMerger {
    private final Clock mClock;
    private final boolean mEmbeddedEndpointingEnabled;
    private final ExecutorService mExecutor;
    private boolean mHasAlreadyLoggedEngine;
    private boolean mInvalid;
    private long mLastNetworkActivity = -1L;
    private final SpeechLibLogger mLogger;
    private final PhoneActionsMerger mPhoneActionsMerger;
    private final int mPrimaryEngine;
    private RecognizeException mPrimaryException;
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final RecognitionEngineCallback mRecognitionEngineCallback;
    @Nullable
    private ScheduledFuture<?> mScheduledNetworkActivityTimeout;
    private final int mSecondaryEngine;
    private final List<RecognitionResponse> mSecondaryEngineResponseQueue = Lists.newArrayList();
    private RecognizeException mSecondaryException;
    private int mSelectedEndpointingEngine = 0;
    private final long mServerEndpointingActivityTimeoutMs;
    private final boolean mServerEndpointingEnabled;
    private boolean mSpeechDetected;
    private final StateMachine<State> mStateMachine = StateMachine.newBuilder("VS.ResultsMerger", State.WAITING).addTransition(State.WAITING, State.USE_PRIMARY).addTransition(State.WAITING, State.USE_SECONDARY).setStrictMode(true).setSingleThreadOnly(true).setDebug(false).build();
    private final boolean mStopMusicDetectorOnStartOfSpeech;
    private final long mSwitchTimeoutMs;
    private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();
    private final ScheduledExecutorService mTimeoutExecutor;

    public ResultsMergerImpl(Clock paramClock, RecognitionDispatcher paramRecognitionDispatcher, EngineSelector paramEngineSelector, RecognitionEngineCallback paramRecognitionEngineCallback, ExecutorService paramExecutorService, ScheduledExecutorService paramScheduledExecutorService, long paramLong, boolean paramBoolean, SpeechSettings paramSpeechSettings, SpeechLibLogger paramSpeechLibLogger) {
        this.mPrimaryEngine = paramEngineSelector.getPrimaryEngine();
        this.mSecondaryEngine = paramEngineSelector.getSecondaryEngine();
        this.mRecognitionDispatcher = paramRecognitionDispatcher;
        this.mRecognitionEngineCallback = paramRecognitionEngineCallback;
        this.mTimeoutExecutor = paramScheduledExecutorService;
        this.mExecutor = paramExecutorService;
        this.mClock = paramClock;
        this.mSwitchTimeoutMs = paramLong;
        this.mStopMusicDetectorOnStartOfSpeech = paramBoolean;
        this.mPhoneActionsMerger = new PhoneActionsMerger();
        this.mEmbeddedEndpointingEnabled = paramSpeechSettings.isEmbeddedEndpointingEnabled();
        this.mServerEndpointingEnabled = paramSpeechSettings.isServerEndpointingEnabled();
        this.mServerEndpointingActivityTimeoutMs = paramSpeechSettings.getServerEndpointingActivityTimeoutMs();
        this.mLogger = paramSpeechLibLogger;
    }

    private State asState(int paramInt) {
        if (paramInt == this.mPrimaryEngine) {
            return State.USE_PRIMARY;
        }
        return State.USE_SECONDARY;
    }

    private boolean isEndpointingEngineEnabled(int paramInt) {
        if (paramInt == 1) {
            return this.mEmbeddedEndpointingEnabled;
        }
        if (paramInt == 2) {
            return this.mServerEndpointingEnabled;
        }
        return false;
    }

    private void maybeLogUsingResultsFrom(int paramInt) {
        if (this.mHasAlreadyLoggedEngine) {
            return;
        }
        this.mHasAlreadyLoggedEngine = true;
        if (paramInt == 1) {
            this.mLogger.logUsingResultsFromEmbedded();
            return;
        }
        if (paramInt == 2) {
            this.mLogger.logUsingResultsFromNetwork();
            return;
        }
        Log.w("VS.ResultsMerger", "Using results from unknown recognition engine: " + paramInt);
    }

    private void maybeStartWaitingForPrimaryEngine() {
        if (this.mStateMachine.isIn(State.WAITING)) {
            this.mTimeoutExecutor.schedule(new Runnable() {
                public void run() {
                    ResultsMergerImpl.this.mExecutor.execute(new Runnable() {
                        public void run() {
                            ResultsMergerImpl.this.handlePrimaryEngineTimeout();
                        }
                    });
                }
            }, this.mSwitchTimeoutMs, TimeUnit.MILLISECONDS);
        }
    }

    private void mergeRecognitionResponse(RecognitionResponse paramRecognitionResponse) {
        int i = paramRecognitionResponse.getEngine();
        S3.S3Response localS3Response = (S3.S3Response) paramRecognitionResponse.get(1);
        if (this.mStateMachine.isIn(asState(i))) {
            this.mPhoneActionsMerger.mergeWithEmbeddedResponses(localS3Response);
            maybeLogUsingResultsFrom(i);
            this.mRecognitionEngineCallback.onResult(paramRecognitionResponse);
        }
        while (i != this.mSecondaryEngine) {
            return;
        }
        if (this.mStateMachine.isIn(State.WAITING)) {
            this.mSecondaryEngineResponseQueue.add(paramRecognitionResponse);
        }
        this.mPhoneActionsMerger.process(localS3Response);
    }

    private void onEngineActivity(int paramInt) {
        if (paramInt == 2) {
            this.mLastNetworkActivity = this.mClock.uptimeMillis();
        }
    }

    private void setSpeechRelatedState(RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
        if (paramEndpointerEvent.getEventType() == 0) {
            if ((this.mStopMusicDetectorOnStartOfSpeech) && (!this.mSpeechDetected)) {
                this.mRecognitionDispatcher.stopEngine(3);
                this.mSpeechDetected = true;
            }
        }
        while ((paramEndpointerEvent.getEventType() != 1) && (paramEndpointerEvent.getEventType() != 2)) {
            return;
        }
        maybeStartWaitingForPrimaryEngine();
    }

    private boolean shouldProcessProgressUpdate(int paramInt) {
        return (this.mSelectedEndpointingEngine == 0) || (this.mSelectedEndpointingEngine == paramInt);
    }

    private void switchTo(State paramState) {
        boolean bool;
        if (paramState != State.WAITING) {
            bool = true;
            Preconditions.checkArgument(bool);
            this.mStateMachine.moveTo(paramState);
            if (paramState == State.USE_SECONDARY) {
                if (this.mSecondaryException == null) {
                    break label64;
                }
                this.mRecognitionDispatcher.cancel();
                this.mRecognitionEngineCallback.onError(Preconditions.checkNotNull(this.mPrimaryException));
            }
        }
        for (; ; ) {
            return;
            bool = false;
            break;
            label64:
            if (!this.mSecondaryEngineResponseQueue.isEmpty()) {
                maybeLogUsingResultsFrom(this.mSecondaryEngine);
            }
            Iterator localIterator = this.mSecondaryEngineResponseQueue.iterator();
            while (localIterator.hasNext()) {
                RecognitionResponse localRecognitionResponse = (RecognitionResponse) localIterator.next();
                this.mRecognitionEngineCallback.onResult(localRecognitionResponse);
            }
        }
    }

    void handlePrimaryEngineTimeout() {
        this.mThreadCheck.check();
        if (this.mPrimaryEngine == 1) {
            this.mPrimaryException = new EmbeddedRecognizerTimeoutException();
        }
        for (; ; ) {
            if (this.mStateMachine.isIn(State.WAITING)) {
                switchTo(State.USE_SECONDARY);
            }
            return;
            if (this.mPrimaryEngine == 2) {
                this.mPrimaryException = new NetworkRecognizeException("Timed out waiting for NetworkRecognitionEngine response.");
            }
        }
    }

    public void invalidate() {
        this.mThreadCheck.check();
        if (this.mScheduledNetworkActivityTimeout != null) {
            this.mScheduledNetworkActivityTimeout.cancel(true);
        }
        this.mInvalid = true;
    }

    public void onError(RecognizeException paramRecognizeException) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
        }
        int i;
        do {
            do {
                return;
                if ((paramRecognizeException instanceof NetworkRecognizeException)) {
                    this.mLogger.logS3ConnectionError();
                }
                i = paramRecognizeException.getEngine();
                if (!this.mStateMachine.isIn(State.WAITING)) {
                    break;
                }
                if (i == this.mPrimaryEngine) {
                    this.mPrimaryException = paramRecognizeException;
                    if (this.mSecondaryEngine != 0) {
                        Log.w("VS.ResultsMerger", "Primary engine error", paramRecognizeException);
                        switchTo(State.USE_SECONDARY);
                        return;
                    }
                    this.mRecognitionDispatcher.cancel();
                    this.mRecognitionEngineCallback.onError(paramRecognizeException);
                    return;
                }
            } while (i != this.mSecondaryEngine);
            this.mSecondaryException = paramRecognizeException;
            return;
        } while (!this.mStateMachine.isIn(asState(i)));
        this.mRecognitionDispatcher.cancel();
        if (i == this.mSecondaryEngine) {
            paramRecognizeException = (RecognizeException) Preconditions.checkNotNull(this.mPrimaryException);
        }
        this.mRecognitionEngineCallback.onError(paramRecognizeException);
    }

    public void onProgressUpdate(int paramInt, long paramLong) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
        }
        do {
            return;
            onEngineActivity(paramInt);
        } while (!shouldProcessProgressUpdate(paramInt));
        this.mRecognitionEngineCallback.onProgressUpdate(paramInt, paramLong);
    }

    public void onResult(RecognitionResponse paramRecognitionResponse) {
        this.mThreadCheck.check();
        if (this.mInvalid) {
        }
        int j;
        do {
            return;
            int i = paramRecognitionResponse.getEngine();
            onEngineActivity(i);
            j = paramRecognitionResponse.getType();
            if ((this.mStateMachine.isIn(State.WAITING)) && (i == this.mPrimaryEngine) && (j != 2)) {
                switchTo(State.USE_PRIMARY);
            }
            if (j == 1) {
                mergeRecognitionResponse(paramRecognitionResponse);
                return;
            }
            if (j == 3) {
                this.mRecognitionEngineCallback.onResult(paramRecognitionResponse);
                return;
            }
        } while ((j != 2) || (!shouldProcessEndpointingEvent(paramRecognitionResponse)));
        setSpeechRelatedState((RecognizerProtos.EndpointerEvent) paramRecognitionResponse.get(2));
        this.mRecognitionEngineCallback.onResult(paramRecognitionResponse);
    }

    boolean shouldProcessEndpointingEvent(RecognitionResponse paramRecognitionResponse) {
        boolean bool = true;
        if (!isEndpointingEngineEnabled(paramRecognitionResponse.getEngine())) {
            Log.w("VS.ResultsMerger", "Ignoring " + paramRecognitionResponse.getEngineName() + " endpointing event");
            bool = false;
        }
        do {
            do {
                return bool;
                if (this.mSelectedEndpointingEngine != 0) {
                    break;
                }
                this.mSelectedEndpointingEngine = paramRecognitionResponse.getEngine();
            } while (this.mSelectedEndpointingEngine != 2);
            new ServerEndpointingTimeoutProcessor(null).run();
            return bool;
        } while (paramRecognitionResponse.getEngine() == this.mSelectedEndpointingEngine);
        return false;
    }

    public static final class EmbeddedRecognizerTimeoutException
            extends EmbeddedRecognizeException {
        public EmbeddedRecognizerTimeoutException() {
            super();
        }
    }

    private class ServerEndpointingTimeoutProcessor
            implements Runnable {
        private ServerEndpointingTimeoutProcessor() {
        }

        public void run() {
            if (ResultsMergerImpl.this.mInvalid) {
                return;
            }
            long l1 = ResultsMergerImpl.this.mClock.uptimeMillis() - ResultsMergerImpl.this.mLastNetworkActivity;
            long l2 = ResultsMergerImpl.this.mServerEndpointingActivityTimeoutMs - l1;
            if (l2 <= 0L) {
                Log.w("VS.ResultsMerger", "Timed out waiting for server activity");
                final NetworkRecognizeException localNetworkRecognizeException = new NetworkRecognizeException("Using the network for endpointing and have had no network response in " + ResultsMergerImpl.this.mServerEndpointingActivityTimeoutMs + "ms");
                localNetworkRecognizeException.setEngine(2);
                ResultsMergerImpl.this.mExecutor.execute(new Runnable() {
                    public void run() {
                        ResultsMergerImpl.this.mRecognitionDispatcher.cancel();
                        ResultsMergerImpl.this.mRecognitionEngineCallback.onError(localNetworkRecognizeException);
                    }
                });
                return;
            }
            ResultsMergerImpl.access$802(ResultsMergerImpl.this, ResultsMergerImpl.this.mTimeoutExecutor.schedule(this, Math.max(l2, 10L), TimeUnit.MILLISECONDS));
        }
    }

    private static enum State {
        WAITING, USE_PRIMARY, USE_SECONDARY;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.ResultsMergerImpl

 * JD-Core Version:    0.7.0.1

 */