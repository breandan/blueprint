package com.google.android.speech.embedded;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.SpeechSettings;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.exception.EmbeddedRecognizeException;
import com.google.android.speech.grammar.GrammarCompilationService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.wireless.voicesearch.proto.GstaticConfiguration;

import java.util.Set;
import java.util.concurrent.Executor;

public class OfflineActionsManager {
    private final Set<Greco3Grammar> mCompilingGrammars = Sets.newHashSet();
    private final Context mContext;
    private final Greco3DataManager mGreco3DataManager;
    private final Executor mMainThread;
    private final SpeechSettings mSettings;
    private volatile SimpleCallback<Integer> mCallback;
    private String mGrammarCompilationLocale;

    public OfflineActionsManager(Context paramContext, Greco3DataManager paramGreco3DataManager, SpeechSettings paramSpeechSettings, Executor paramExecutor) {
        this.mContext = paramContext;
        this.mGreco3DataManager = paramGreco3DataManager;
        this.mSettings = paramSpeechSettings;
        this.mMainThread = paramExecutor;
    }

    private boolean canCompileGrammar() {
        return this.mGreco3DataManager.hasResourcesForCompilation(this.mGrammarCompilationLocale);
    }

    private Runnable createInitGrammarCallback(final Greco3Grammar... paramVarArgs) {
        return new Runnable() {
            public void run() {
                OfflineActionsManager.this.initGrammars(paramVarArgs);
            }
        };
    }

    private void dispatchCallbackOnMainThread(final int paramInt) {
        this.mMainThread.execute(new Runnable() {
            public void run() {
                synchronized (OfflineActionsManager.this) {
                    if (OfflineActionsManager.this.mCallback != null) {
                        OfflineActionsManager.this.mCallback.onResult(Integer.valueOf(paramInt));
                        mCallback = null;
                    }
                    return;
                }
            }
        });
    }

    private long getGrammarCompilationFrequency(Greco3Grammar greco3Grammar) {
        if (greco3Grammar == Greco3Grammar.CONTACT_DIALING) {
            GstaticConfiguration.Configuration config = mSettings.getConfiguration();
            if ((config.hasEmbeddedRecognizer()) && (config.getEmbeddedRecognizer().hasGrammarCompilationFrequencyMs())) {
                return (long) config.getEmbeddedRecognizer().getGrammarCompilationFrequencyMs();
            }
        }
        return 0xffffffff;
    }

    private boolean hasCompiledGrammar(Greco3Grammar paramGreco3Grammar) {
        return this.mGreco3DataManager.hasCompiledGrammar(this.mGrammarCompilationLocale, paramGreco3Grammar);
    }

    private int initGrammar(Greco3Grammar paramGreco3Grammar) {
        Preconditions.checkState(this.mGreco3DataManager.isInitialized());
        ExtraPreconditions.checkHoldsLock(this);
        if (canCompileGrammar()) {
            if (hasCompiledGrammar(paramGreco3Grammar)) {
                return 1;
            }
            startGrammarCompilation(paramGreco3Grammar);
            return 2;
        }
        return 3;
    }

    private void initGrammars(Greco3Grammar... paramVarArgs) {
        for (int j = 0; j < paramVarArgs.length; j++) {
            try {
                SimpleCallback localSimpleCallback = this.mCallback;
                if (localSimpleCallback == null) {
                    return;
                }
                Greco3Grammar localGreco3Grammar = paramVarArgs[j];
                switch (initGrammar(localGreco3Grammar)) {
                    case 2:
                        this.mCompilingGrammars.add(localGreco3Grammar);
                }
            } finally {
                this.mCallback.onResult(Integer.valueOf(3));
                continue;
            }
        }

        if (this.mCompilingGrammars.isEmpty()) {
            this.mCallback.onResult(Integer.valueOf(1));
        }
    }

    private void internalMaybeScheduleGrammarCompilation(final String paramString, Executor paramExecutor, final Greco3Grammar paramGreco3Grammar) {
        if ((GrammarCompilationService.isGrammarCompilationAlarmSet()) || (!this.mGreco3DataManager.isInitialized())) {
            return;
        }
        paramExecutor.execute(new Runnable() {
            public void run() {
                GrammarCompilationService.maybeSchedulePeriodicCompilation(OfflineActionsManager.this.mGreco3DataManager.getRevisionForGrammar(paramString, paramGreco3Grammar), OfflineActionsManager.this.mContext, paramString, paramGreco3Grammar, OfflineActionsManager.this.getGrammarCompilationFrequency(paramGreco3Grammar));
            }
        });
    }

    private void startGrammarCompilation(Greco3Grammar paramGreco3Grammar) {
        ExtraPreconditions.checkHoldsLock(this);
        this.mCompilingGrammars.add(paramGreco3Grammar);
        GrammarCompilationService.startCompilationForLocale(this.mContext, this.mGrammarCompilationLocale, paramGreco3Grammar);
    }

    public synchronized void detach(SimpleCallback<Integer> paramSimpleCallback) {
        ExtraPreconditions.checkMainThread();
        Preconditions.checkState((paramSimpleCallback != this.mCallback) || (this.mCallback == null));
        this.mCallback = null;
    }

    public void maybeScheduleGrammarCompilation() {
        try {
            String str = this.mSettings.getSpokenLocaleBcp47();
            Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            Greco3Grammar[] arrayOfGreco3Grammar = Greco3Grammar.values();
            int i = arrayOfGreco3Grammar.length;
            for (int j = 0; j < i; j++) {
                internalMaybeScheduleGrammarCompilation(str, localExecutor, arrayOfGreco3Grammar[j]);
            }
            return;
        } finally {
        }
    }

    public void notifyDone(Greco3Grammar paramGreco3Grammar, boolean paramBoolean) {
        for (; ; ) {
            try {
                if (this.mCompilingGrammars.contains(paramGreco3Grammar)) {
                    SimpleCallback localSimpleCallback = this.mCallback;
                    if (localSimpleCallback != null) {
                    }
                } else {
                    return;
                }
                if (!paramBoolean) {
                    this.mCompilingGrammars.clear();
                    dispatchCallbackOnMainThread(4);
                    this.mCallback = null;
                    continue;
                }
                this.mCompilingGrammars.remove(paramGreco3Grammar);
            } finally {
            }
            if (this.mCompilingGrammars.isEmpty()) {
                dispatchCallbackOnMainThread(1);
            }
        }
    }

    public void notifyStart(Greco3Grammar paramGreco3Grammar) {
    }

    public void startOfflineDataCheck(SimpleCallback<Integer> paramSimpleCallback, String paramString, Greco3Grammar... paramVarArgs) {
        for (; ; ) {
            try {
                ExtraPreconditions.checkMainThread();
                if (this.mCallback == paramSimpleCallback) {
                    boolean bool = this.mCompilingGrammars.isEmpty();
                    if (!bool) {
                        return;
                    }
                }
                this.mGrammarCompilationLocale = paramString;
                this.mCallback = paramSimpleCallback;
                this.mCompilingGrammars.clear();
                if (this.mGreco3DataManager.isInitialized()) {
                    initGrammars(paramVarArgs);
                } else {
                    this.mGreco3DataManager.initialize(createInitGrammarCallback(paramVarArgs));
                }
            } finally {
            }
        }
    }

    public static final class GrammarCompilationException
            extends EmbeddedRecognizeException {
        private static final long serialVersionUID = 1482752135755739456L;

        public GrammarCompilationException() {
            super("Grammar compilation exception");
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.embedded.OfflineActionsManager

 * JD-Core Version:    0.7.0.1

 */