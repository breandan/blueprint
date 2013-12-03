package com.google.android.voicesearch.ui;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.android.speech.contacts.ContactLookup;
import com.google.common.base.Preconditions;

public class ActionEditorSetContactPictureTask
        extends AsyncTask<Long, Void, Bitmap> {
    private final ImageView mContactPictureView;

    public ActionEditorSetContactPictureTask(ImageView paramImageView) {
        this.mContactPictureView = ((ImageView) Preconditions.checkNotNull(paramImageView));
    }

    protected Bitmap doInBackground(Long... paramVarArgs) {
        return ContactLookup.fetchPhotoBitmap(this.mContactPictureView.getContext().getContentResolver(), paramVarArgs[0].longValue(), true);
    }

    protected void onContactPictureSet() {
    }

    protected void onPostExecute(Bitmap paramBitmap) {
        if (paramBitmap != null) {
            this.mContactPictureView.setImageBitmap(paramBitmap);
            this.mContactPictureView.setVisibility(0);
        }
        for (; ; ) {
            onContactPictureSet();
            return;
            this.mContactPictureView.setVisibility(8);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.ui.ActionEditorSetContactPictureTask

 * JD-Core Version:    0.7.0.1

 */