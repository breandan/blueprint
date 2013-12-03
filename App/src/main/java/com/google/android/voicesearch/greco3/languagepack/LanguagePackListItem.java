package com.google.android.voicesearch.greco3.languagepack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.speech.utils.SpokenLanguageUtils;

import javax.annotation.Nullable;

public class LanguagePackListItem
        extends RelativeLayout {
    static final int CANCEL_DOWNLOAD = 1;
    static final int CANCEL_UPGRADE = 2;
    static final int DOWNLOAD = 3;
    static final int PREINSTALLED = 4;
    static final int UNINSTALL = 5;
    static final int UPGRADE = 6;
    static final int UPGRADE_OR_UNINSTALL = 7;
    private GstaticConfiguration.Configuration mConfiguration;
    private LanguagePackUpdateController mController;
    private GstaticConfiguration.LanguagePack mLanguagePack;
    @Nullable
    private GstaticConfiguration.LanguagePack mUpgrade;

    public LanguagePackListItem(Context paramContext) {
        super(paramContext);
    }

    public LanguagePackListItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public LanguagePackListItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    private AlertDialog cancelDownload(final GstaticConfiguration.LanguagePack paramLanguagePack) {
        DialogInterface.OnClickListener local4 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                LanguagePackListItem.this.mController.cancelDownload(paramLanguagePack);
            }
        };
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramLanguagePack.getVersion());
        arrayOfObject[1] = formatSize(paramLanguagePack);
        return makeDialog(localContext.getString(2131363637, arrayOfObject), 2131363642, local4).setNeutralButton(2131363641, null).create();
    }

    private AlertDialog delete() {
        DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                LanguagePackListItem.this.mController.doDelete(LanguagePackListItem.this.mLanguagePack);
            }
        };
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(this.mLanguagePack.getVersion());
        arrayOfObject[1] = formatSize(this.mLanguagePack);
        return makeDialog(localContext.getString(2131363636, arrayOfObject), 2131363640, local3).setNeutralButton(2131363226, null).create();
    }

    private AlertDialog download(final GstaticConfiguration.LanguagePack paramLanguagePack) {
        DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                LanguagePackListItem.this.mController.doDownload(paramLanguagePack, true);
            }
        };
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(paramLanguagePack.getVersion());
        arrayOfObject[1] = formatSize(paramLanguagePack);
        return makeDialog(localContext.getString(2131363637, arrayOfObject), 2131363638, local2).setNeutralButton(2131363226, null).create();
    }

    private AlertDialog preinstalled() {
        Context localContext = getContext();
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = Integer.valueOf(this.mLanguagePack.getVersion());
        arrayOfObject[1] = formatSize(this.mLanguagePack);
        return makeDialog(localContext.getString(2131363635, arrayOfObject), 2131363643, null).create();
    }

    private AlertDialog updateOrUninstall() {
        DialogInterface.OnClickListener local5 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                LanguagePackListItem.this.mController.doDownload(LanguagePackListItem.this.mUpgrade, true);
            }
        };
        DialogInterface.OnClickListener local6 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                LanguagePackListItem.this.mController.doDelete(LanguagePackListItem.this.mLanguagePack);
            }
        };
        StringBuilder localStringBuilder = new StringBuilder();
        Context localContext1 = getContext();
        Object[] arrayOfObject1 = new Object[2];
        arrayOfObject1[0] = Integer.valueOf(this.mLanguagePack.getVersion());
        arrayOfObject1[1] = formatSize(this.mLanguagePack);
        localStringBuilder.append(localContext1.getString(2131363636, arrayOfObject1));
        localStringBuilder.append('\n');
        Context localContext2 = getContext();
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = Integer.valueOf(this.mUpgrade.getVersion());
        arrayOfObject2[1] = formatSize(this.mUpgrade);
        localStringBuilder.append(localContext2.getString(2131363637, arrayOfObject2));
        return makeDialog(localStringBuilder.toString(), 2131363638, local5).setNegativeButton(2131363226, null).setNeutralButton(2131363640, local6).create();
    }

    protected View findViewByIdInternal(int paramInt) {
        return findViewById(paramInt);
    }

    String formatSize(GstaticConfiguration.LanguagePack paramLanguagePack) {
        return Formatter.formatFileSize(getContext(), 1024 * paramLanguagePack.getSizeKb());
    }

    String formatSizeShort(GstaticConfiguration.LanguagePack paramLanguagePack) {
        return Formatter.formatShortFileSize(getContext(), 1024 * paramLanguagePack.getSizeKb());
    }

    int getItemAction() {
        int i = 7;
        if (this.mController.isActiveDownload(this.mLanguagePack)) {
            i = 1;
        }
        label113:
        do {
            do {
                return i;
                if ((this.mUpgrade != null) && (this.mController.isActiveDownload(this.mUpgrade))) {
                    return 2;
                }
                if (!this.mController.isInstalled(this.mLanguagePack.getBcp47Locale())) {
                    break label122;
                }
                if (!this.mController.isInstalledInSystemPartition(this.mLanguagePack)) {
                    break label113;
                }
                if (!this.mController.isUsingDownloadedData(this.mLanguagePack.getBcp47Locale())) {
                    break;
                }
            } while (this.mUpgrade != null);
            return 5;
            if (this.mUpgrade != null) {
                return 6;
            }
            return 4;
        } while (this.mUpgrade != null);
        return 5;
        label122:
        return 3;
    }

    protected AlertDialog.Builder makeDialog(String paramString, int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
        String str = SpokenLanguageUtils.getDisplayName(this.mConfiguration, this.mLanguagePack.getBcp47Locale());
        return new AlertDialog.Builder(getContext()).setTitle(str).setMessage(paramString).setPositiveButton(paramInt, paramOnClickListener);
    }

    void populateView(int paramInt) {
        setText((TextView) findViewByIdInternal(2131296738), SpokenLanguageUtils.getDisplayName(this.mConfiguration, this.mLanguagePack.getBcp47Locale()));
        TextView localTextView = (TextView) findViewByIdInternal(2131296739);
        final AlertDialog localAlertDialog;
        switch (paramInt) {
            default:
                throw new IllegalStateException("Unknown action " + paramInt);
            case 1:
                setText(localTextView, 2131363651);
                localAlertDialog = cancelDownload(this.mLanguagePack);
        }
        for (; ; ) {
            setClickable(true);
            setFocusable(true);
            setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    localAlertDialog.show();
                }
            });
            return;
            setText(localTextView, 2131363651);
            localAlertDialog = cancelDownload(this.mUpgrade);
            continue;
            Context localContext4 = getContext();
            Object[] arrayOfObject4 = new Object[1];
            arrayOfObject4[0] = formatSizeShort(this.mUpgrade);
            setText(localTextView, localContext4.getString(2131363646, arrayOfObject4));
            localAlertDialog = updateOrUninstall();
            continue;
            Context localContext3 = getContext();
            Object[] arrayOfObject3 = new Object[1];
            arrayOfObject3[0] = formatSizeShort(this.mLanguagePack);
            setText(localTextView, localContext3.getString(2131363648, arrayOfObject3));
            localAlertDialog = delete();
            continue;
            Context localContext2 = getContext();
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = formatSizeShort(this.mUpgrade);
            setText(localTextView, localContext2.getString(2131363647, arrayOfObject2));
            localAlertDialog = download(this.mUpgrade);
            continue;
            setText(localTextView, 2131363650);
            localAlertDialog = preinstalled();
            continue;
            Context localContext1 = getContext();
            Object[] arrayOfObject1 = new Object[1];
            arrayOfObject1[0] = formatSizeShort(this.mLanguagePack);
            setText(localTextView, localContext1.getString(2131363649, arrayOfObject1));
            localAlertDialog = download(this.mLanguagePack);
        }
    }

    protected void setText(TextView paramTextView, int paramInt) {
        paramTextView.setText(paramInt);
    }

    protected void setText(TextView paramTextView, String paramString) {
        paramTextView.setText(paramString);
    }

    public void update(LanguagePackUpdateController paramLanguagePackUpdateController, GstaticConfiguration.Configuration paramConfiguration, GstaticConfiguration.LanguagePack paramLanguagePack) {
        this.mConfiguration = paramConfiguration;
        this.mController = paramLanguagePackUpdateController;
        this.mLanguagePack = paramLanguagePack;
        this.mUpgrade = this.mController.getUpgrade(this.mLanguagePack);
        populateView(getItemAction());
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.greco3.languagepack.LanguagePackListItem

 * JD-Core Version:    0.7.0.1

 */