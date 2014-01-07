package com.embryo.android.speech.params;

import android.location.Location;

import com.embryo.android.speech.SpeechSettings;
import com.embryo.android.speech.embedded.Greco3Grammar;
import com.embryo.android.speech.embedded.Greco3Mode;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.embryo.speech.common.proto.RecognitionContextProto;
import com.embryo.wireless.voicesearch.proto.GstaticConfiguration;

import javax.annotation.Nullable;

public class SessionParams {
    private final boolean mAlternatesEnabled;
    private final String mApplicationId;
    private final AudioInputParams mAudioInputParams;
    private final Greco3Grammar mGreco3Grammar;
    private final Greco3Mode mGreco3Mode;
    @Nullable
    private final Location mLocationOverride;
    private final int mMaxNbest;
    private final int mMode;
    private final boolean mNoSpeechDetectedEnabled;
    private final boolean mPartialResultsEnabled;
    private final boolean mProfanityFilterEnabled;
    @Nullable
    private final RecognitionContextProto.RecognitionContext mRecognitionContext;
    private final Supplier<String> mRequestIdSupplier;
    private final boolean mResendingAudio;
    private final boolean mServerEndpointingEnabled;
    private final String mService;
    private final boolean mSoundSearchTtsEnabled;
    private final String mSpokenBcp47Locale;
    private final boolean mStopOnEndOfSpeech;
    private final boolean mSuggestionsEnabled;
    @Nullable
    private final String mTriggerApplication;
    private final boolean mUseMusicHotworder;

    private SessionParams(int paramInt1, AudioInputParams paramAudioInputParams, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString1, String paramString2, Greco3Grammar paramGreco3Grammar, Greco3Mode paramGreco3Mode, boolean paramBoolean4, boolean paramBoolean5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, int paramInt2, Location paramLocation, RecognitionContextProto.RecognitionContext paramRecognitionContext, String paramString3, Supplier<String> paramSupplier, String paramString4, boolean paramBoolean9, boolean paramBoolean10) {
        this.mMode = paramInt1;
        this.mAudioInputParams = paramAudioInputParams;
        this.mAlternatesEnabled = paramBoolean1;
        this.mResendingAudio = paramBoolean2;
        this.mStopOnEndOfSpeech = paramBoolean3;
        this.mSpokenBcp47Locale = paramString1;
        this.mTriggerApplication = paramString2;
        this.mGreco3Grammar = paramGreco3Grammar;
        this.mGreco3Mode = paramGreco3Mode;
        this.mUseMusicHotworder = paramBoolean4;
        this.mNoSpeechDetectedEnabled = paramBoolean5;
        this.mPartialResultsEnabled = paramBoolean6;
        this.mProfanityFilterEnabled = paramBoolean7;
        this.mSuggestionsEnabled = paramBoolean8;
        this.mMaxNbest = paramInt2;
        this.mLocationOverride = paramLocation;
        this.mRecognitionContext = paramRecognitionContext;
        this.mApplicationId = paramString3;
        this.mRequestIdSupplier = paramSupplier;
        this.mService = paramString4;
        this.mSoundSearchTtsEnabled = paramBoolean9;
        this.mServerEndpointingEnabled = paramBoolean10;
    }

    public AudioInputParams getAudioInputParams() {
        return this.mAudioInputParams;
    }

    public int getEmbeddedFallbackTimeout(SpeechSettings paramSpeechSettings) {
        if (this.mGreco3Mode == Greco3Mode.DICTATION) {
            return paramSpeechSettings.getConfiguration().getDictation().getEmbeddedRecognizerFallbackTimeout();
        }
        if (this.mGreco3Mode == Greco3Mode.GRAMMAR) {
            return paramSpeechSettings.getConfiguration().getVoiceSearch().getEmbeddedRecognizerFallbackTimeout();
        }
        return -1;
    }

    @Nullable
    public GstaticConfiguration.EndpointerParams getEndpointerParams(SpeechSettings speechSettings) {
        GstaticConfiguration.EndpointerParams endpointerParams = new GstaticConfiguration.EndpointerParams();
//        switch (mMode) {
//            case 3:
//                endpointerParams = speechSettings.getConfiguration().getDictation().getEndpointerParams();
//            case 0:
//                endpointerParams = speechSettings.getConfiguration().getIntentApi().getEndpointerParams();
//            case 1:
//                endpointerParams = mStopOnEndOfSpeech ? speechSettings.getConfiguration().getServiceApi().getEndpointerParams() : speechSettings.getConfiguration().getDictation().getEndpointerParams();
//            case 2:
//                endpointerParams = speechSettings.getConfiguration().getVoiceSearch().getEndpointerParams();
//        }

//        if (!mNoSpeechDetectedEnabled) {
//            endpointerParams = speechSettings.getConfiguration().getVoiceSearch().getEndpointerParams();
//            endpointerParams.setNoSpeechDetectedTimeoutMsec(0x4e20);
//        }

        return endpointerParams;
    }

    public Greco3Grammar getGreco3Grammar() {
        return this.mGreco3Grammar;
    }

    public Greco3Mode getGreco3Mode() {
        return this.mGreco3Mode;
    }

    public int getMode() {
        return this.mMode;
    }

    public String getRequestId() {
        return (String) this.mRequestIdSupplier.get();
    }

    public String getSpokenBcp47Locale() {
        return this.mSpokenBcp47Locale;
    }

    public boolean isAlternatesEnabled() {
        return this.mAlternatesEnabled;
    }

    public boolean isPartialResultsEnabled() {
        return this.mPartialResultsEnabled;
    }

    public boolean isProfanityFilterEnabled() {
        return this.mProfanityFilterEnabled;
    }

    public boolean stopOnEndOfSpeech() {
        return this.mStopOnEndOfSpeech;
    }

    public static class Builder {
        private boolean mAlternatesEnabled = true;
        private String mApplicationId;
        private AudioInputParams mAudioInputParams;
        private Greco3Grammar mGreco3Grammar = Greco3Grammar.CONTACT_DIALING;
        private Greco3Mode mGreco3Mode = Greco3Mode.ENDPOINTER_VOICESEARCH;
        private int mMaxNbest = 5;
        private int mMode = 2;
        private boolean mNoSpeechDetectedEnabled = false;
        private boolean mPartialResultsEnabled = true;
        private Location mLocationOverride = null;
        private boolean mProfanityFilterEnabled = false;
        private RecognitionContextProto.RecognitionContext mRecognitionContext;
        private boolean mResendingAudio = false;
        private String mService;
        private boolean mSoundSearchTtsEnabled = false;
        private String mSpokenBcp47Locale = "en-US";
        private boolean mStopOnEndOfSpeech = false; //Interesting
        private boolean mSuggestionsEnabled = true;
        private String mTriggerApplication;
        private boolean mUseMusicHotworder = false;
        private boolean mServerEndpointingEnabled = false;

        private static String getDefaultApplicationId(int paramInt) {
            switch (paramInt) {
                default:
                    throw new IllegalStateException("Unknown mode " + paramInt);
                case 0:
                    return "intent-api";
                case 1:
                    return "service-api";
                case 2:
                    return "voice-search";
                case 3:
                    return "voice-ime";
                case 4:
                    return "hands-free";
                case 5:
                    return "now-tv";
            }
        }

        private static String getDefaultService(int paramInt) {
            switch (paramInt) {
                default:
                    throw new IllegalStateException("Unknown mode " + paramInt);
                case 0:
                case 1:
                    return "recognizer";
                case 4:
                    return "voicesearch";
                case 2:
                    return "voicesearch-web";
                case 3:
                case 5:
                case 6:
                    return "recognizer";
                case 7:
                    return "sound-search";
                case 8:
                    return "sound-search-tv";
            }
        }

        private Supplier<String> createNewRequestId() {
            return Suppliers.memoize(new Supplier() {
                public String get() {
                    return RequestIdGenerator.INSTANCE.newRequestId();
                }
            });
        }

        public SessionParams build() {
            if (this.mAudioInputParams == null) {
                this.mAudioInputParams = new AudioInputParams.Builder().build();
            }
            if (this.mApplicationId == null) {
                this.mApplicationId = getDefaultApplicationId(this.mMode);
            }
            if (this.mService == null) {
                this.mService = getDefaultService(this.mMode);
            }
            return new SessionParams(this.mMode, this.mAudioInputParams, this.mAlternatesEnabled, this.mResendingAudio, this.mStopOnEndOfSpeech, this.mSpokenBcp47Locale, this.mTriggerApplication, this.mGreco3Grammar, this.mGreco3Mode, this.mUseMusicHotworder, this.mNoSpeechDetectedEnabled, this.mPartialResultsEnabled, this.mProfanityFilterEnabled, this.mSuggestionsEnabled, this.mMaxNbest, this.mLocationOverride, this.mRecognitionContext, this.mApplicationId, createNewRequestId(), this.mService, this.mSoundSearchTtsEnabled, this.mServerEndpointingEnabled);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     SessionParams

 * JD-Core Version:    0.7.0.1

 */