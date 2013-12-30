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
    private final SpeechLibLogger mLogger;
    private final PhoneActionsMerger mPhoneActionsMerger;
    private final int mPrimaryEngine;
    private final RecognitionDispatcher mRecognitionDispatcher;
    private final RecognitionEngineCallback mRecognitionEngineCallback;
    private final int mSecondaryEngine;
    private final List<RecognitionResponse> mSecondaryEngineResponseQueue = Lists.newArrayList();
    private final long mServerEndpointingActivityTimeoutMs;
    private final boolean mServerEndpointingEnabled;
    private final StateMachine<State> mStateMachine = StateMachine.newBuilder("VS.ResultsMerger", State.WAITING).addTransition(State.WAITING, State.USE_PRIMARY).addTransition(State.WAITING, State.USE_SECONDARY).setStrictMode(true).setSingleThreadOnly(true).setDebug(false).build();
    private final boolean mStopMusicDetectorOnStartOfSpeech;
    private final long mSwitchTimeoutMs;
    private final ExtraPreconditions.ThreadCheck mThreadCheck = ExtraPreconditions.createSameThreadCheck();
    private final ScheduledExecutorService mTimeoutExecutor;
    private boolean mHasAlreadyLoggedEngine;
    private boolean mInvalid;
    private long mLastNetworkActivity = -1L;
    private RecognizeException mPrimaryException;
    @Nullable
    private ScheduledFuture<?> mScheduledNetworkActivityTimeout;
    private RecognizeException mSecondaryException;
    private int mSelectedEndpointingEngine = 0;
    private boolean mSpeechDetected;

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
        S3.S3Response localS3Response = paramRecognitionResponse.get(1);
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

    private void switchTo(ResultsMergerImpl.State state) {
        Preconditions.checkArgument((state != ResultsMergerImpl.State.WAITING));
        mStateMachine.moveTo(state);
        if (state == ResultsMergerImpl.State.USE_SECONDARY) {
            if (mSecondaryException != null) {
                mRecognitionDispatcher.cancel();
                mRecognitionEngineCallback.onError(Preconditions.checkNotNull(mPrimaryException));
            }
            if (!mSecondaryEngineResponseQueue.isEmpty()) {
                maybeLogUsingResultsFrom(mSecondaryEngine);
            }
            for (RecognitionResponse response : mSecondaryEngineResponseQueue) {
                mRecognitionEngineCallback.onResult(response);
            }
        }
    }

    void handlePrimaryEngineTimeout() {
        mThreadCheck.check();
        if (mPrimaryEngine == 0x1) {
            mPrimaryException = new ResultsMergerImpl.EmbeddedRecognizerTimeoutException();
        } else if (mPrimaryEngine == 0x2) {
            mPrimaryException = new NetworkRecognizeException("Timed out waiting for NetworkRecognitionEngine response.");
        }
        if (mStateMachine.isIn(ResultsMergerImpl.State.WAITING)) {
            switchTo(ResultsMergerImpl.State.USE_SECONDARY);
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
            return;
        }
        int i;
        do {
            do {
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
        } while (!this.mStateMachine.isIn(asState(i)));
        this.mRecognitionDispatcher.cancel();
        if (i == this.mSecondaryEngine) {
            paramRecognizeException = Preconditions.checkNotNull(this.mPrimaryException);
        }
        this.mRecognitionEngineCallback.onError(paramRecognizeException);
    }

    public void onProgressUpdate(int engine, long progressMs) {
        mThreadCheck.check();
        if (mInvalid) {
            return;
        }
        onEngineActivity(engine);
        if (shouldProcessProgressUpdate(engine)) {
            mRecognitionEngineCallback.onProgressUpdate(engine, progressMs);
        }
    }

    public void onResult(RecognitionResponse response) {
        mThreadCheck.check();
        if (mInvalid) {
            return;
        }
        int engine = response.getEngine();
        onEngineActivity(engine);
        int type = response.getType();
        if ((mStateMachine.isIn(ResultsMergerImpl.State.WAITING)) && (engine == mPrimaryEngine) && (type != 0x2)) {
            switchTo(ResultsMergerImpl.State.USE_PRIMARY);
        }
        if (type == 0x1) {
            mergeRecognitionResponse(response);
            return;
        }
        if (type == 0x3) {
            mRecognitionEngineCallback.onResult(response);
            return;
        }
        if (type == 0x2) {
            if (shouldProcessEndpointingEvent(response)) {
                setSpeechRelatedState((RecognizerProtos.EndpointerEvent) response.get(0x2));
                mRecognitionEngineCallback.onResult(response);
            }
        }
    }

    boolean shouldProcessEndpointingEvent(RecognitionResponse response) {
        if (!isEndpointingEngineEnabled(response.getEngine())) {
            Log.w("VS.ResultsMerger", "Ignoring " + response.getEngineName() + " endpointing event");
            return false;
        }
        if (mSelectedEndpointingEngine == 0) {
            mSelectedEndpointingEngine = response.getEngine();
            if (mSelectedEndpointingEngine == 0x2) {
                new ResultsMergerImpl.ServerEndpointingTimeoutProcessor().run();
            }
            return true;
        }
        if (response.getEngine() != mSelectedEndpointingEngine) {
            return false;
        }
        return true;
    }

    private static enum State {
        WAITING, USE_PRIMARY, USE_SECONDARY;
    }

    public static final class EmbeddedRecognizerTimeoutException
            extends EmbeddedRecognizeException {
        public EmbeddedRecognizerTimeoutException() {
            super("Timed out waiting for Greco3RecognitionEngine response.");
        }
    }

    private class ServerEndpointingTimeoutProcessor
            implements Runnable {
        public void run() {
            if (mInvalid) {
                return;
            }
            long elapsedMs = mClock.uptimeMillis() - mLastNetworkActivity;
            long remainingMs = mServerEndpointingActivityTimeoutMs - elapsedMs;
            if (remainingMs <= 0x0) {
                Log.w("VS.ResultsMerger", "Timed out waiting for server activity");
                final NetworkRecognizeException timeout = new NetworkRecognizeException("Using the network for endpointing and have had no network response in " + mServerEndpointingActivityTimeoutMs + "ms");
                timeout.setEngine(0x2);
                mExecutor.execute(new Runnable() {
                    public void run() {
                        mRecognitionDispatcher.cancel();
                        mRecognitionEngineCallback.onError(timeout);
                    }
                });
                return;
            }
            mScheduledNetworkActivityTimeout = mTimeoutExecutor.schedule(this, Math.max(remainingMs, 10), TimeUnit.MILLISECONDS);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.ResultsMergerImpl

 * JD-Core Version:    0.7.0.1

 */