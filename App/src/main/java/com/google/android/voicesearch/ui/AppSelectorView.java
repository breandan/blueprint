package com.google.android.voicesearch.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class AppSelectorView
        extends LinearLayout {
    private Spinner mAppSelectorSpinner;
    private OnAppSelectedListener mOnAppSelectedListener;

    public AppSelectorView(Context paramContext) {
        this(paramContext, null);
    }

    public AppSelectorView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public AppSelectorView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onFinishInflate() {
        this.mAppSelectorSpinner = ((Spinner) findViewById(2131296886));
    }

    public void setAppSelectorOnTouchListener(View.OnTouchListener paramOnTouchListener) {
        this.mAppSelectorSpinner.setOnTouchListener(paramOnTouchListener);
    }

    public void setOnAppSelectedListener(OnAppSelectedListener paramOnAppSelectedListener) {
        this.mOnAppSelectedListener = paramOnAppSelectedListener;
    }

    public void setSelectorApps(final List<AppSelectionHelper.App> paramList, int paramInt) {
        this.mAppSelectorSpinner.setAdapter(new AppSelectorAdapter(getContext(), getId(), paramList, (LayoutInflater) getContext().getSystemService("layout_inflater")));
        this.mAppSelectorSpinner.setSelection(paramInt);
        this.mAppSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                AppSelectorView.this.mOnAppSelectedListener.onAppSelected((AppSelectionHelper.App) paramList.get(paramAnonymousInt));
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
    }

    class AppSelectorAdapter
            extends ArrayAdapter<AppSelectionHelper.App> {
        private final LayoutInflater mInflater;

        public AppSelectorAdapter(int paramInt, List<AppSelectionHelper.App> paramList, LayoutInflater paramLayoutInflater) {
            super(paramList, paramLayoutInflater);
            Object localObject;
            this.mInflater = localObject;
        }

        private View createView(int paramInt, View paramView, boolean paramBoolean) {
            AppSelectionHelper.App localApp = (AppSelectionHelper.App) getItem(paramInt);
            LinearLayout localLinearLayout;
            ImageView localImageView;
            if (paramView != null) {
                localLinearLayout = (LinearLayout) paramView;
                localImageView = (ImageView) localLinearLayout.getChildAt(0);
            }
            for (TextView localTextView = (TextView) localLinearLayout.getChildAt(1); ; localTextView = (TextView) localLinearLayout.findViewById(2131296889)) {
                localImageView.setImageDrawable(localApp.getIcon());
                localTextView.setText(localApp.getLabel());
                if (!paramBoolean) {
                    break;
                }
                localTextView.setVisibility(0);
                return localLinearLayout;
                localLinearLayout = (LinearLayout) this.mInflater.inflate(2130968787, null);
                localImageView = (ImageView) localLinearLayout.findViewById(2131296888);
            }
            localTextView.setVisibility(8);
            return localLinearLayout;
        }

        public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            return createView(paramInt, paramView, true);
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            return createView(paramInt, paramView, false);
        }
    }

    public static abstract interface OnAppSelectedListener {
        public abstract void onAppSelected(AppSelectionHelper.App paramApp);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ui.AppSelectorView

 * JD-Core Version:    0.7.0.1

 */