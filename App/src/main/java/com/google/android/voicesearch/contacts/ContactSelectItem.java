package com.google.android.voicesearch.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.common.base.Preconditions;

public class ContactSelectItem
        extends RelativeLayout {
    private static final Drawable.ConstantState NULL_THUMBNAIL = new Drawable.ConstantState() {
        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable newDrawable() {
            return null;
        }
    };
    private static final LruCache<Long, Drawable.ConstantState> sContactIdToThumbnail = new LruCache(20);
    private Contact mContact;
    private final ContentResolver mContentResolver;
    private AsyncTask<?, ?, ?> mOutstandingTask;
    private final Resources mResources;

    public ContactSelectItem(Context paramContext) {
        super(paramContext);
        this.mResources = paramContext.getResources();
        this.mContentResolver = paramContext.getContentResolver();
    }

    public ContactSelectItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mResources = paramContext.getResources();
        this.mContentResolver = paramContext.getContentResolver();
    }

    public ContactSelectItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mResources = paramContext.getResources();
        this.mContentResolver = paramContext.getContentResolver();
    }

    private void cancelOutstandingTask() {
        if (this.mOutstandingTask != null) {
            this.mOutstandingTask.cancel(false);
            this.mOutstandingTask = null;
        }
    }

    private void hideView(int paramInt) {
        View localView = findViewById(paramInt);
        if (localView != null) {
            localView.setVisibility(8);
        }
    }

    private void setQuickContactBadgeImage(QuickContactBadge paramQuickContactBadge, Contact paramContact) {
        Drawable.ConstantState localConstantState = (Drawable.ConstantState) sContactIdToThumbnail.get(Long.valueOf(paramContact.getId()));
        if (localConstantState != null) {
            setThumbnail(paramQuickContactBadge, localConstantState);
            return;
        }
        paramQuickContactBadge.setImageToDefault();
        cancelOutstandingTask();
        FetchThumbnailForBadgeTask localFetchThumbnailForBadgeTask = new FetchThumbnailForBadgeTask(paramQuickContactBadge);
        Long[] arrayOfLong = new Long[1];
        arrayOfLong[0] = Long.valueOf(paramContact.getId());
        this.mOutstandingTask = localFetchThumbnailForBadgeTask.execute(arrayOfLong);
    }

    private void setTextViewText(int paramInt, CharSequence paramCharSequence) {
        TextView localTextView = (TextView) findViewById(paramInt);
        if (localTextView != null) {
            localTextView.setVisibility(0);
            localTextView.setText(paramCharSequence);
        }
    }

    private void setThumbnail(QuickContactBadge paramQuickContactBadge, Drawable.ConstantState paramConstantState) {
        if (paramConstantState == NULL_THUMBNAIL) {
            paramQuickContactBadge.setImageToDefault();
            return;
        }
        paramQuickContactBadge.setImageDrawable(paramConstantState.newDrawable());
    }

    public Contact getContact() {
        return this.mContact;
    }

    protected void onDetachedFromWindow() {
        cancelOutstandingTask();
        super.onDetachedFromWindow();
    }

    public void setActionButtonOnClickListener(final View.OnClickListener paramOnClickListener) {
        findViewById(2131296474).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                paramOnClickListener.onClick(ContactSelectItem.this);
            }
        });
    }

    public void setContact(Contact paramContact, int paramInt1, int paramInt2) {
        this.mContact = paramContact;
        setTextViewText(2131296464, paramContact.getName());
        ImageView localImageView;
        View localView;
        if (!paramContact.hasValue()) {
            hideView(2131296476);
            hideView(2131296475);
            localImageView = (ImageView) findViewById(2131296474);
            localView = findViewById(2131297003);
            if ((paramInt1 != 0) && (paramContact.hasValue())) {
                break label170;
            }
            localImageView.setVisibility(8);
            if (localView != null) {
                localView.setVisibility(8);
            }
        }
        for (; ; ) {
            QuickContactBadge localQuickContactBadge = (QuickContactBadge) findViewById(2131296473);
            if (localQuickContactBadge != null) {
                setQuickContactBadgeImage(localQuickContactBadge, paramContact);
                localQuickContactBadge.assignContactUri(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, paramContact.getId()));
            }
            return;
            setTextViewText(2131296476, paramContact.getFormattedValue());
            String str = paramContact.getLabel(getResources());
            if (TextUtils.isEmpty(str)) {
                hideView(2131296475);
                break;
            }
            setTextViewText(2131296475, str);
            break;
            label170:
            localImageView.setImageResource(paramInt1);
            localImageView.setContentDescription(this.mResources.getText(paramInt2));
            localImageView.setVisibility(0);
            if (localView != null) {
                localView.setVisibility(0);
            }
        }
    }

    private class FetchThumbnailForBadgeTask
            extends AsyncTask<Long, Void, Drawable.ConstantState> {
        private final QuickContactBadge mBadge;

        public FetchThumbnailForBadgeTask(QuickContactBadge paramQuickContactBadge) {
            this.mBadge = paramQuickContactBadge;
        }

        protected Drawable.ConstantState doInBackground(Long... paramVarArgs) {
            Long localLong = (Long) Preconditions.checkNotNull(paramVarArgs[0]);
            Bitmap localBitmap = ContactLookup.fetchPhotoBitmap(ContactSelectItem.this.mContentResolver, localLong.longValue(), false);
            if (localBitmap == null) {
                ContactSelectItem.sContactIdToThumbnail.put(localLong, ContactSelectItem.NULL_THUMBNAIL);
                return ContactSelectItem.NULL_THUMBNAIL;
            }
            Drawable.ConstantState localConstantState = new BitmapDrawable(ContactSelectItem.this.mResources, localBitmap).getConstantState();
            ContactSelectItem.sContactIdToThumbnail.put(localLong, localConstantState);
            return localConstantState;
        }

        protected void onPostExecute(Drawable.ConstantState paramConstantState) {
            ContactSelectItem.this.setThumbnail(this.mBadge, paramConstantState);
            ContactSelectItem.access$502(ContactSelectItem.this, null);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.contacts.ContactSelectItem

 * JD-Core Version:    0.7.0.1

 */