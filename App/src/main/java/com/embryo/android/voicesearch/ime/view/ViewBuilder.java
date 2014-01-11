package com.embryo.android.voicesearch.ime.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class ViewBuilder {
    private final int mLandHeight;
    private final int mLandLayout;
    private final int mPortHeight;
    private final int mPortLayout;

    public ViewBuilder(DisplayMetrics paramDisplayMetrics) {
        int i = (int) (paramDisplayMetrics.widthPixels / paramDisplayMetrics.density);
        int j = (int) (paramDisplayMetrics.heightPixels / paramDisplayMetrics.density);
        if (i > j) {
            int k = i;
            i = j;
            j = k;
        }
        if ((i >= 750) && (j >= 1200)) {
            this.mPortHeight = ((int) (327.0F * paramDisplayMetrics.density));
            this.mLandHeight = ((int) (386.0F * paramDisplayMetrics.density));
            this.mPortLayout = 2130968721;
            this.mLandLayout = 2130968724;
            return;
        }
        if ((i >= 550) && (j >= 900)) {
            this.mPortHeight = ((int) (346.0F * paramDisplayMetrics.density));
            this.mLandHeight = ((int) (298.0F * paramDisplayMetrics.density));
            this.mPortLayout = 2130968721;
            this.mLandLayout = 2130968721;
            return;
        }
        if ((i >= 360) && (j >= 590)) {
            this.mPortHeight = ((int) (262.0F * paramDisplayMetrics.density));
            this.mLandHeight = ((int) (201.0F * paramDisplayMetrics.density));
            this.mPortLayout = 2130968721;
            this.mLandLayout = 2130968726;
            return;
        }
        if ((i >= 320) && (j >= 530)) {
            this.mPortHeight = ((int) (240.0F * paramDisplayMetrics.density));
            this.mLandHeight = ((int) (180.0F * paramDisplayMetrics.density));
            this.mPortLayout = 2130968721;
            this.mLandLayout = 2130968726;
            return;
        }
        this.mPortHeight = -2;
        this.mLandHeight = -2;
        this.mPortLayout = 2130968722;
        this.mLandLayout = 2130968726;
    }

    public static ViewBuilder create(Context paramContext) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) paramContext.getSystemService("window")).getDefaultDisplay().getMetrics(localDisplayMetrics);
        return new ViewBuilder(localDisplayMetrics);
    }

    private int getHeight(boolean paramBoolean) {
        if (paramBoolean) {
            return this.mLandHeight;
        }
        return this.mPortHeight;
    }

    private int getLayout(boolean paramBoolean) {
        if (paramBoolean) {
            return this.mLandLayout;
        }
        return this.mPortLayout;
    }

    public View createView(Context paramContext) {
        LayoutInflater localLayoutInflater = (LayoutInflater) new ContextThemeWrapper(paramContext, 2131624124).getSystemService("layout_inflater");
        if (paramContext.getResources().getConfiguration().orientation == 2) {
        }
        for (boolean bool = true; ; bool = false) {
            View localView = localLayoutInflater.inflate(getLayout(bool), null);
            localView.findViewById(2131296706).getLayoutParams().height = getHeight(bool);
            return localView;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ViewBuilder

 * JD-Core Version:    0.7.0.1

 */