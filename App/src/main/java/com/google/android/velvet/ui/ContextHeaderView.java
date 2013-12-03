package com.google.android.velvet.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.search.shared.ui.WebImageView;
import com.google.android.shared.util.LayoutUtils;
import com.google.android.velvet.presenter.ContextHeaderUi;

import javax.annotation.Nullable;

public class ContextHeaderView
        extends FrameLayout
        implements ContextHeaderUi {
    private WebImageView mContextImage;
    private ImageView mGoogleLogo;

    public ContextHeaderView(Context paramContext) {
        super(paramContext);
    }

    public ContextHeaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public ContextHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContextImage = ((WebImageView) findViewById(2131296484));
        this.mGoogleLogo = ((ImageView) findViewById(2131296485));
        int i = LayoutUtils.getContextHeaderSize(getContext()).y;
        ViewGroup.LayoutParams localLayoutParams = this.mContextImage.getLayoutParams();
        if (localLayoutParams.height != i) {
            localLayoutParams.height = i;
            this.mContextImage.setLayoutParams(localLayoutParams);
        }
    }

    public void setContextImageDrawable(Drawable paramDrawable, boolean paramBoolean, @Nullable View.OnClickListener paramOnClickListener) {
        this.mContextImage.setImageDrawable(paramDrawable);
        if (paramBoolean) {
            this.mContextImage.setOnClickListener(paramOnClickListener);
            this.mContextImage.setContentDescription(getResources().getString(2131363299));
            this.mGoogleLogo.setVisibility(4);
            return;
        }
        this.mContextImage.setOnClickListener(null);
        this.mContextImage.setClickable(false);
        this.mContextImage.setContentDescription(null);
        this.mGoogleLogo.setVisibility(0);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.ContextHeaderView

 * JD-Core Version:    0.7.0.1

 */