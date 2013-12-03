package com.google.android.velvet.ui.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.Lists;

import java.util.List;

public class CustomPairStringArrayAdapter
        extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final List<Item> mItems;
    private final int mLastIndex;

    public CustomPairStringArrayAdapter(Context paramContext, String[] paramArrayOfString) {
        this.mItems = Lists.newArrayListWithCapacity(paramArrayOfString.length);
        int i = paramArrayOfString.length;
        for (int j = 0; j < i; j++) {
            String str = paramArrayOfString[j];
            this.mItems.add(new Item(str, null));
        }
        this.mLastIndex = (-1 + this.mItems.size());
        this.mInflater = ((LayoutInflater) paramContext.getSystemService("layout_inflater"));
    }

    public int getCount() {
        return this.mItems.size();
    }

    public int getCustomValuePosition() {
        return this.mLastIndex;
    }

    public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null) {
            paramView = this.mInflater.inflate(2130968665, paramViewGroup, false);
        }
        Item localItem = getItem(paramInt);
        TextView localTextView1 = (TextView) paramView.findViewById(16908308);
        TextView localTextView2 = (TextView) paramView.findViewById(16908309);
        localTextView1.setText(localItem.getTitle());
        if (localItem.getSubtitle() != null) {
            localTextView2.setText(localItem.getSubtitle());
            localTextView2.setVisibility(0);
            return paramView;
        }
        localTextView2.setVisibility(8);
        return paramView;
    }

    public Item getItem(int paramInt) {
        return (Item) this.mItems.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null) {
            paramView = this.mInflater.inflate(2130968665, paramViewGroup, false);
        }
        Item localItem = getItem(paramInt);
        TextView localTextView1 = (TextView) paramView.findViewById(16908308);
        TextView localTextView2 = (TextView) paramView.findViewById(16908309);
        localTextView1.setText(localItem.getButtonTitle());
        CharSequence localCharSequence = localItem.getButtonSubtitle();
        if (localCharSequence != null) {
            localTextView2.setText(localCharSequence);
            localTextView2.setVisibility(0);
            localTextView1.setMaxLines(1);
            return paramView;
        }
        localTextView2.setVisibility(8);
        localTextView1.setMaxLines(2);
        return paramView;
    }

    public void setCustomValue(String paramString1, String paramString2) {
        Item localItem = getItem(getCustomValuePosition());
        if ((localItem.setButtonTitleOverride(paramString1) | localItem.setButtonSubtitleOverride(paramString2))) {
            notifyDataSetChanged();
        }
    }

    public void updateItem(int paramInt, String paramString1, String paramString2) {
        Item localItem = getItem(paramInt);
        if ((localItem.setTitle(paramString1) | localItem.setSubtitle(paramString2))) {
            notifyDataSetChanged();
        }
    }

    public static class Item {
        private CharSequence mButtonSubtitleOverride;
        private CharSequence mButtonTitleOverride;
        private CharSequence mSubtitle;
        private CharSequence mTitle;

        public Item(CharSequence paramCharSequence1, CharSequence paramCharSequence2) {
            setTitle(paramCharSequence1);
            setSubtitle(paramCharSequence2);
        }

        CharSequence getButtonSubtitle() {
            if (this.mButtonSubtitleOverride != null) {
                return this.mButtonSubtitleOverride;
            }
            return this.mSubtitle;
        }

        CharSequence getButtonTitle() {
            if (this.mButtonTitleOverride != null) {
                return this.mButtonTitleOverride;
            }
            return this.mTitle;
        }

        CharSequence getSubtitle() {
            return this.mSubtitle;
        }

        CharSequence getTitle() {
            return this.mTitle;
        }

        boolean setButtonSubtitleOverride(CharSequence paramCharSequence) {
            if (!TextUtils.equals(this.mButtonSubtitleOverride, paramCharSequence)) {
            }
            for (boolean bool = true; ; bool = false) {
                this.mButtonSubtitleOverride = paramCharSequence;
                return bool;
            }
        }

        boolean setButtonTitleOverride(CharSequence paramCharSequence) {
            if (!TextUtils.equals(this.mButtonTitleOverride, paramCharSequence)) {
            }
            for (boolean bool = true; ; bool = false) {
                this.mButtonTitleOverride = paramCharSequence;
                return bool;
            }
        }

        boolean setSubtitle(CharSequence paramCharSequence) {
            if (!TextUtils.equals(this.mSubtitle, paramCharSequence)) {
            }
            for (boolean bool = true; ; bool = false) {
                this.mSubtitle = paramCharSequence;
                return bool;
            }
        }

        boolean setTitle(CharSequence paramCharSequence) {
            if (!TextUtils.equals(this.mTitle, paramCharSequence)) {
            }
            for (boolean bool = true; ; bool = false) {
                this.mTitle = paramCharSequence;
                return bool;
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.util.CustomPairStringArrayAdapter

 * JD-Core Version:    0.7.0.1

 */