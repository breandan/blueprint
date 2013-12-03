package com.google.android.voicesearch.fragments.action;

import android.net.Uri;
import android.os.Parcel;

import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

public class PlayMediaAction
        implements VoiceAction {
    public static final Parcelable.Creator<PlayMediaAction> CREATOR = new Parcelable.Creator() {
        public PlayMediaAction createFromParcel(Parcel paramAnonymousParcel) {
            ProtoParcelable localProtoParcelable = (ProtoParcelable) paramAnonymousParcel.readParcelable(getClass().getClassLoader());
            try {
                localPlayMediaAction = (ActionV2Protos.PlayMediaAction) localProtoParcelable.getProto(ActionV2Protos.PlayMediaAction.class);
                return new PlayMediaAction(localPlayMediaAction);
            } catch (IllegalArgumentException localIllegalArgumentException) {
                for (; ; ) {
                    ActionV2Protos.PlayMediaAction localPlayMediaAction = new ActionV2Protos.PlayMediaAction();
                }
            }
        }

        public PlayMediaAction[] newArray(int paramAnonymousInt) {
            return new PlayMediaAction[paramAnonymousInt];
        }
    };
    private final ActionV2Protos.PlayMediaAction mActionV2;
    private ImmutableList<AppSelectionHelper.App> mApps;
    @Nullable
    protected AppSelectionHelper.App mGoogleContentApp;
    private Uri mImageUri;
    protected List<AppSelectionHelper.App> mLocalResults;
    @Nullable
    protected AppSelectionHelper.App mPlayStoreLink;
    private boolean mPreviewEnabled;
    private AppSelectionHelper.App mSelectedApp;

    public PlayMediaAction(ActionV2Protos.PlayMediaAction paramPlayMediaAction) {
        this.mActionV2 = ((ActionV2Protos.PlayMediaAction) Preconditions.checkNotNull(paramPlayMediaAction));
        this.mPreviewEnabled = false;
        this.mImageUri = null;
        this.mPlayStoreLink = null;
        this.mLocalResults = ImmutableList.of();
    }

    public <T> T accept(VoiceActionVisitor<T> paramVoiceActionVisitor) {
        return paramVoiceActionVisitor.visit(this);
    }

    public boolean canExecute() {
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public ActionV2Protos.PlayMediaAction getActionV2() {
        return this.mActionV2;
    }

    public List<AppSelectionHelper.App> getApps() {
        return this.mApps;
    }

    public AppSelectionHelper.App getGoogleContentApp() {
        return this.mGoogleContentApp;
    }

    public Uri getImageUri() {
        return this.mImageUri;
    }

    public List<AppSelectionHelper.App> getLocalResults() {
        return this.mLocalResults;
    }

    @Nullable
    public String getMimeType() {
        if (isPlayMovieAction()) {
            return "video/movie";
        }
        if (isPlayMusicAction()) {
            return "audio/music";
        }
        if (isOpenBookAction()) {
            return "text/book";
        }
        return null;
    }

    public AppSelectionHelper.App getPlayStoreLink() {
        return this.mPlayStoreLink;
    }

    public AppSelectionHelper.App getSelectedApp() {
        return this.mSelectedApp;
    }

    public boolean isOpenAppAction() {
        return this.mActionV2.hasAppItem();
    }

    public boolean isOpenBookAction() {
        return this.mActionV2.hasBookItem();
    }

    public boolean isPlayMovieAction() {
        return this.mActionV2.hasMovieItem();
    }

    public boolean isPlayMusicAction() {
        return this.mActionV2.hasMusicItem();
    }

    public boolean isPreviewEnabled() {
        return this.mPreviewEnabled;
    }

    public void selectApp(AppSelectionHelper.App paramApp) {
        this.mSelectedApp = paramApp;
    }

    public void setApps(Collection<AppSelectionHelper.App> paramCollection) {
        this.mApps = ImmutableList.copyOf(paramCollection);
    }

    public void setGoogleContentApp(AppSelectionHelper.App paramApp) {
        this.mGoogleContentApp = paramApp;
    }

    public void setImageUri(Uri paramUri) {
        this.mImageUri = paramUri;
    }

    public void setLocalResults(List<AppSelectionHelper.App> paramList) {
        this.mLocalResults = paramList;
    }

    public void setPlayStoreLink(AppSelectionHelper.App paramApp) {
        this.mPlayStoreLink = paramApp;
    }

    public void setPreviewEnabled(boolean paramBoolean) {
        this.mPreviewEnabled = paramBoolean;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeParcelable(ProtoParcelable.create(this.mActionV2), paramInt);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.action.PlayMediaAction

 * JD-Core Version:    0.7.0.1

 */