package com.embryo.android.speech.embedded;

import android.content.Context;
import android.os.AsyncTask;

import com.embryo.android.speech.callback.SimpleCallback;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.Executor;

public class OfflineActionsManager {
    private final Set<Greco3Grammar> mCompilingGrammars = Sets.newHashSet();
    private final Context mContext;
    private final Greco3DataManager mGreco3DataManager;
    private final Executor mMainThread;
    private final com.embryo.android.speech.SpeechSettings mSettings;
    private volatile SimpleCallback<Integer> mCallback;
    private String mGrammarCompilationLocale;

    public OfflineActionsManager(Context paramContext, Greco3DataManager paramGreco3DataManager, com.embryo.android.speech.SpeechSettings paramSpeechSettings, Executor paramExecutor) {
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
            com.embryo.wireless.voicesearch.proto.GstaticConfiguration.Configuration config = mSettings.getConfiguration();
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
        com.embryo.android.shared.util.ExtraPreconditions.checkHoldsLock(this);
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

    private void startGrammarCompilation(Greco3Grammar paramGreco3Grammar) {
        com.embryo.android.shared.util.ExtraPreconditions.checkHoldsLock(this);
        this.mCompilingGrammars.add(paramGreco3Grammar);
        com.embryo.android.speech.grammar.GrammarCompilationService.startCompilationForLocale(this.mContext, this.mGrammarCompilationLocale, paramGreco3Grammar);
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

    public synchronized void startOfflineDataCheck(SimpleCallback<Integer> completionCallback, String bcp47Locale, Greco3Grammar[] grammars) {
        com.embryo.android.shared.util.ExtraPreconditions.checkMainThread();
        if((mCallback == completionCallback) && (!mCompilingGrammars.isEmpty())) {
            return;
        }
        mGrammarCompilationLocale = bcp47Locale;
        mCallback = completionCallback;
        mCompilingGrammars.clear();
        if(mGreco3DataManager.isInitialized()) {
            initGrammars(grammars);
            return;
        }
        mGreco3DataManager.initialize(createInitGrammarCallback(grammars));
    }

    public static final class GrammarCompilationException
            extends com.embryo.android.speech.exception.EmbeddedRecognizeException {
        private static final long serialVersionUID = 1482752135755739456L;

        public GrammarCompilationException() {
            super("Grammar compilation exception");
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     OfflineActionsManager

 * JD-Core Version:    0.7.0.1

 */