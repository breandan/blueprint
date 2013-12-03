package com.google.android.voicesearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.search.shared.ui.WebImageView;
import com.google.android.voicesearch.ui.ActionEditorView;

public class PlayMusicCard
        extends PlayMediaCard<PlayMusicController>
        implements PlayMusicController.Ui {
    private TextView mAlbumView;
    private WebImageView mArtistImageView;
    private ViewGroup mArtistInfoContainer;
    private TextView mArtistTitleView;
    private TextView mArtistView;
    private TextView mExplicitView;
    private ViewGroup mGeneralInfoContainer;
    private TextView mHeadLineView;

    public PlayMusicCard(Context paramContext) {
        super(paramContext);
    }

    public View onCreateView(Context paramContext, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        ActionEditorView localActionEditorView = createMediaActionEditor(paramContext, paramLayoutInflater, paramViewGroup, paramBundle, 2130968785, 2131363449);
        this.mArtistInfoContainer = ((ViewGroup) localActionEditorView.findViewById(2131296878));
        this.mArtistTitleView = ((TextView) localActionEditorView.findViewById(2131296879));
        this.mArtistImageView = ((WebImageView) localActionEditorView.findViewById(2131296880));
        this.mGeneralInfoContainer = ((ViewGroup) localActionEditorView.findViewById(2131296881));
        this.mHeadLineView = ((TextView) localActionEditorView.findViewById(2131296882));
        this.mAlbumView = ((TextView) localActionEditorView.findViewById(2131296884));
        this.mArtistView = ((TextView) localActionEditorView.findViewById(2131296883));
        this.mExplicitView = ((TextView) localActionEditorView.findViewById(2131296885));
        return localActionEditorView;
    }

    public void setImageOnLayoutChangeListener(View.OnLayoutChangeListener paramOnLayoutChangeListener) {
        if (this.mImageView != null) {
            this.mImageView.addOnLayoutChangeListener(paramOnLayoutChangeListener);
        }
    }

    public void showAlbum(String paramString1, String paramString2) {
        this.mArtistInfoContainer.setVisibility(8);
        this.mGeneralInfoContainer.setVisibility(0);
        this.mHeadLineView.setText(paramString1);
        showTextIfNonEmpty(this.mArtistView, paramString2);
    }

    public void showArtist(String paramString) {
        this.mGeneralInfoContainer.setVisibility(8);
        this.mArtistInfoContainer.setVisibility(0);
        this.mArtistTitleView.setText(paramString);
        this.mImageView = this.mArtistImageView;
    }

    public void showIsExplicit(boolean paramBoolean) {
        TextView localTextView = this.mExplicitView;
        if (paramBoolean) {
        }
        for (int i = 0; ; i = 8) {
            localTextView.setVisibility(i);
            return;
        }
    }

    public void showMisc(String paramString) {
        this.mArtistInfoContainer.setVisibility(8);
        this.mGeneralInfoContainer.setVisibility(0);
        this.mHeadLineView.setText(paramString);
    }

    public void showPreview(boolean paramBoolean) {
        TextView localTextView = this.mHeadLineView;
        if (paramBoolean) {
        }
        for (int i = 1; ; i = 2) {
            localTextView.setMaxLines(i);
            this.mHeadLineView.setHorizontallyScrolling(paramBoolean);
            super.showPreview(paramBoolean);
            return;
        }
    }

    public void showSong(String paramString1, String paramString2, String paramString3) {
        this.mArtistInfoContainer.setVisibility(8);
        this.mGeneralInfoContainer.setVisibility(0);
        this.mHeadLineView.setText(paramString1);
        showTextIfNonEmpty(this.mAlbumView, paramString2);
        showTextIfNonEmpty(this.mArtistView, paramString3);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMusicCard

 * JD-Core Version:    0.7.0.1

 */