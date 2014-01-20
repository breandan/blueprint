package com.app.me;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.embryo.android.search.core.AsyncServicesImpl;
import com.embryo.android.speech.callback.SimpleCallback;
import com.embryo.android.speech.embedded.Greco3Grammar;
import com.embryo.android.speech.grammar.GrammarCompilationService;
import com.embryo.android.voicesearch.VoiceSearchServices;
import com.embryo.android.voicesearch.hotword.HotwordDetector;
import com.embryo.android.search.core.GsaPreferenceController;
import com.google.speech.embedded.Greco3Recognizer;
import com.google.speech.embedded.OfflineActionsManager;

import java.io.InputStream;

public class MainActivity extends Activity {
    private static Resources res;
    private static String packName;

    public static InputStream getRawResource(String name) {
        return res.openRawResource(res.getIdentifier(name, "raw", packName));
    }

    private void startNewVoiceSearch(VoiceSearchServices vss)
    {
        Greco3Recognizer.maybeLoadSharedLibrary();
        OfflineActionsManager localOfflineActionsManager = vss.getOfflineActionsManager();
        vss.getGrammarCompilationService().onCreate(vss.getGreco3Container(), localOfflineActionsManager);
//        Greco3Grammar[] arrayOfGreco3Grammar = new Greco3Grammar[2];
//        arrayOfGreco3Grammar[0] = Greco3Grammar.CONTACT_DIALING;
//        arrayOfGreco3Grammar[1] = Greco3Grammar.HANDS_FREE_COMMANDS;
//        vss.getGrammarCompilationService().doCompile("en-US", Greco3Grammar.HANDS_FREE_COMMANDS, getResources(), getPackageName());
//        localOfflineActionsManager.startOfflineDataCheck(vss.getGrammarCompilationService(), new SimpleCallback<Integer>() {
//            @Override
//            public void onResult(Integer paramT) {}
//       }, "en-US", arrayOfGreco3Grammar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getApplicationContext().getResources();
        packName = getPackageName();
        setContentView(R.layout.activity_scroll);
        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        VoiceSearchServices vss = new VoiceSearchServices(getApplication(), new AsyncServicesImpl(), new GsaPreferenceController(getApplication()), this);
        startNewVoiceSearch(vss);

        HotwordDetector hd = vss.getHotwordDetector();
        hd.start(new HotwordDetector.HotwordListener() {
            @Override
            public void onHotword(long paramLong) {
                Log.i("MainActivity", "#onHotword");
                textView.setText("#onHotword");
            }

            @Override
            public void onHotwordDetectorNotStarted() {
                Log.i("SearchController", "#onHotwordDetectorNotStarted");
                textView.setText("#onHotwordDetectorNotStarted");
            }

            @Override
            public void onHotwordDetectorStarted() {
                Log.i("SearchController", "#onHotwordDetectorStarted");
                textView.setText("#onHotwordDetectorStarted");
            }

            @Override
            public void onHotwordDetectorStopped(boolean paramBoolean) {
                Log.i("SearchController", "#onHotwordDetectorStarted");
                textView.setText("#onHotwordDetectorStarted");
            }

            @Override
            public void onMusicDetected() {
                Log.i("SearchController", "#onHotwordDetectorStopped");
                textView.setText("#onHotwordDetectorStopped");
            }
        });

//        AudioInputStreamFactory aisf = new AudioInputStreamFactory();

//        String red = "统计自然语言处理运用了推测学、机率、统计的方法来解决上述，尤其是针对容易高度模糊的长串句子，当套用实际文法进行分析产生出成千上万笔可能性时所引发之难题。处理这些高度模糊句子所采用消歧的方法通常运用到语料库以及马可夫模型。统计自然语言处理的技术主要由同样自人工智能下与学习行为相关的子领域：机器学习及资料采掘所演进而成。";
//        try {
//            CWSTagger tag = new CWSTagger("seg");
//            red = tag.tag(red);
//        } catch (LoadModelException e) {
//            Log.w("lincoln", e.getMessage());
//        }
//
//        final SpannableString redSpannable = new SpannableString(red);
//        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
//
//        class PlainClickableSpan extends ClickableSpan {
//            private boolean clicked = false;
//
//            public PlainClickableSpan() {
//                super();
//            }
//
//            public void updateDrawState(TextPaint ds) {// override updateDrawState
//                ds.setUnderlineText(false); // set to false to remove underline
//                if (clicked)
//                    ds.setColor(Color.GREEN);
//            }
//
//            @Override
//            public void onClick(View widget) {
//                clicked ^= true;
//                Log.w("lincoln", widget.getClass().getName());
//            }
//        }
//
//        int lastIndex = 0;
//        boolean spaceMark = false;
//        for (int i = 0; i < red.length(); i++) {
//            if (spaceMark = (' ' == red.charAt(i))) {
//                redSpannable.setSpan(new PlainClickableSpan(), lastIndex, i, 0);
//                lastIndex = i + 1;
//            }
//        }
//
//        redSpannable.setSpan(new PlainClickableSpan(), lastIndex, red.length(), 0);
//        textView.setText(redSpannable, TextView.BufferType.SPANNABLE);
    }
}
