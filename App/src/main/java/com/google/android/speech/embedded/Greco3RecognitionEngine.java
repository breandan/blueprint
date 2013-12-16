package com.google.android.speech.embedded;

import com.google.android.speech.SpeechSettings;
import com.google.android.speech.audio.AudioInputStreamFactory;
import com.google.android.speech.callback.RecognitionEngineCallback;
import com.google.android.speech.engine.RecognitionEngine;
import com.google.android.speech.exception.AudioRecognizeException;
import com.google.android.speech.exception.EmbeddedRecognizeException;
import com.google.android.speech.exception.NoMatchRecognizeException;
import com.google.android.speech.exception.RecognizeException;
import com.google.android.speech.logger.SpeechLibLogger;
import com.google.android.speech.network.request.RecognizerSessionParamsBuilderTask;
import com.google.android.speech.params.SessionParams;
import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;import com.google.speech.recognizer.api.RecognizerSessionParamsProto;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Greco3RecognitionEngine
        implements RecognitionEngine {
    private final int mBytesPerSample;
    @Nonnull
    private final Greco3CallbackFactory mCallbackFactory;
    private Greco3Recognizer mCurrentRecognition;
    private Greco3EngineManager.Resources mCurrentResources;
    @Nonnull
    private final Greco3EngineManager mGreco3EngineManager;
    @Nonnull
    private final GrecoEventLogger.Factory mGrecoEventLoggerFactory;
    private InputStream mInput;
    @Nonnull
    private final Greco3ModeSelector mModeSelector;
    private final int mSamplingRate;
    @Nonnull
    private final SpeechLibLogger mSpeechLibLogger;
    @Nonnull
    private final SpeechSettings mSpeechSettings;

    public Greco3RecognitionEngine(@Nonnull Greco3EngineManager paramGreco3EngineManager, int paramInt1, int paramInt2, @Nonnull SpeechSettings paramSpeechSettings, @Nonnull Greco3CallbackFactory paramGreco3CallbackFactory, @Nonnull Greco3ModeSelector paramGreco3ModeSelector, @Nonnull GrecoEventLogger.Factory paramFactory, @Nonnull SpeechLibLogger paramSpeechLibLogger) {
        this.mGreco3EngineManager = paramGreco3EngineManager;
        this.mSamplingRate = paramInt1;
        this.mBytesPerSample = paramInt2;
        this.mSpeechSettings = paramSpeechSettings;
        this.mCallbackFactory = paramGreco3CallbackFactory;
        this.mModeSelector = paramGreco3ModeSelector;
        this.mGrecoEventLoggerFactory = paramFactory;
        this.mSpeechLibLogger = paramSpeechLibLogger;
    }

    private void cleanupAndDispatchStartError(Greco3Callback paramGreco3Callback, RecognizeException paramRecognizeException) {
        this.mCurrentRecognition = null;
        this.mCurrentResources = null;
        paramGreco3Callback.handleError(paramRecognizeException);
    }

    private RecognizerSessionParamsProto.RecognizerSessionParams getEmbeddedRecognizerParams(SessionParams paramSessionParams) {
        return new RecognizerSessionParamsBuilderTask(this.mSpeechSettings, paramSessionParams.getAudioInputParams().getSamplingRate(), paramSessionParams.isPartialResultsEnabled(), paramSessionParams.isAlternatesEnabled(), paramSessionParams.isProfanityFilterEnabled()).call();
    }

    public void close() {
        if (this.mCurrentRecognition != null) {
            this.mGreco3EngineManager.release(this.mCurrentRecognition);
            this.mCurrentRecognition = null;
        }
        Closeables.closeQuietly(this.mInput);
        this.mInput = null;
    }

    @Nullable
    Greco3Recognizer createRecognizer(Greco3EngineManager.Resources paramResources) {
        Greco3Recognizer localGreco3Recognizer = Greco3Recognizer.create(paramResources, this.mSamplingRate, this.mBytesPerSample);
        if (localGreco3Recognizer == null) {
            this.mSpeechLibLogger.logBug(9067534);
        }
        return localGreco3Recognizer;
    }

    Greco3Mode createRecognizerFor(String bcp47Locale, Greco3Mode requested, Greco3Grammar grammarType) {
        mGreco3EngineManager.maybeInitialize();
        Greco3Mode primary = mModeSelector.getMode(requested, grammarType);
        Greco3Mode fallback = mModeSelector.getFallbackMode(requested, grammarType);
        Greco3Mode selected = null;
        if(primary == null) {
            return null;
        }
        Greco3EngineManager.Resources resources = mGreco3EngineManager.getResources(bcp47Locale, primary, grammarType);
        if((resources == null) && (primary == Greco3Mode.GRAMMAR) && (!"en-US".equals(bcp47Locale))) {
            resources = mGreco3EngineManager.getResources("en-US", primary, grammarType);
        }
        if(resources == null) {
            if(fallback != null) {
                resources = mGreco3EngineManager.getResources(bcp47Locale, fallback, null);
                if(resources != null) {
                    selected = fallback;
                }
            }
        } else {
            selected = primary;
        }
        mCurrentRecognition = createRecognizer(resources);
        if(mCurrentRecognition == null) {
            mCurrentResources = null;
            return null;
        }
        mCurrentResources = resources;
        return selected;
    }

    public void startRecognition(AudioInputStreamFactory inputFactory, RecognitionEngineCallback callback, SessionParams sessionParams) {
        Preconditions.checkNotNull(callback);
        mCurrentRecognition = null;
        mCurrentResources = null;
        Greco3Recognizer.maybeLoadSharedLibrary();
        Greco3Mode requestedMode = sessionParams.getGreco3Mode();
        Greco3Mode selected = createRecognizerFor(sessionParams.getSpokenBcp47Locale(), requestedMode, sessionParams.getGreco3Grammar());
        Greco3Callback g3Callback = mCallbackFactory.create(callback, selected);
        if(selected == null) {
            cleanupAndDispatchStartError(g3Callback, new Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException());
            return;
        }
        try {
            mInput = inputFactory.createInputStream();
        } catch(IOException ioe) {
            cleanupAndDispatchStartError(g3Callback, new AudioRecognizeException("Unable to create stream", ioe));
            return;
        }
        mGreco3EngineManager.startRecognition(mCurrentRecognition, mInput, g3Callback, getEmbeddedRecognizerParams(sessionParams), mGrecoEventLoggerFactory.getEventLoggerForMode(selected), mCurrentResources.languagePack);
        if((selected.isEndpointerMode()) && (!requestedMode.isEndpointerMode())) {
            g3Callback.handleError(new Greco3RecognitionEngine.EmbeddedRecognizerUnavailableException());
        }
    }

    public static final class EmbeddedRecognizerUnavailableException
            extends EmbeddedRecognizeException {
        public EmbeddedRecognizerUnavailableException() {
            super("Embedded recognizer unavailable");
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.Greco3RecognitionEngine

 * JD-Core Version:    0.7.0.1

 */