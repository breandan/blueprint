package com.app.me;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

/**
 * Created by breandan on 11/18/13.
 */

public class ScrollTextActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        String red = "this is red";
        final SpannableString redSpannable = new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);

        class PlainClickableSpan extends ClickableSpan {
            private boolean clicked = false;

            public PlainClickableSpan() {
                super();
            }

            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
                if (clicked)
                    ds.setColor(Color.GREEN);
            }

            @Override
            public void onClick(View widget) {
                clicked ^= true;
            }
        }

        int lastIndex = 0;
        boolean spaceMark = false;
        for (int i = 0; i < red.length(); i++) {
            if (spaceMark = (' ' == red.charAt(i))) {
                redSpannable.setSpan(new PlainClickableSpan(), lastIndex + 1, i, 0);
                lastIndex = i;
            }
        }

        redSpannable.setSpan(new PlainClickableSpan(), lastIndex, red.length(), 0);
        textView.setText(redSpannable, TextView.BufferType.SPANNABLE);
    }

}
