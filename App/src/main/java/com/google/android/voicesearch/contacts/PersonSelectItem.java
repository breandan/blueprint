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
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.google.android.speech.contacts.Contact;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.Person;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PersonSelectItem
        extends LinearLayout {
    private static final Drawable.ConstantState NULL_THUMBNAIL = new Drawable.ConstantState() {
        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable newDrawable() {
            return null;
        }
    };
    private static final LruCache<Long, Drawable.ConstantState> sPersonIdToThumbnail = new LruCache(20);
    private final ContentResolver mContentResolver;
    private AsyncTask<?, ?, ?> mOutstandingTask;
    private Person mPerson;
    private final Resources mResources;
    private Contact mSingleContact;

    public PersonSelectItem(Context paramContext) {
        super(paramContext);
        this.mResources = paramContext.getResources();
        this.mContentResolver = paramContext.getContentResolver();
    }

    public PersonSelectItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mResources = paramContext.getResources();
        this.mContentResolver = paramContext.getContentResolver();
    }

    public PersonSelectItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
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

    private void setQuickContactBadgeImage(QuickContactBadge paramQuickContactBadge, Person paramPerson, @Nullable Runnable paramRunnable) {
        Drawable.ConstantState localConstantState = (Drawable.ConstantState) sPersonIdToThumbnail.get(Long.valueOf(paramPerson.getId()));
        if (localConstantState != null) {
            setThumbnail(paramQuickContactBadge, localConstantState);
            if (paramRunnable != null) {
                paramRunnable.run();
            }
            return;
        }
        paramQuickContactBadge.setImageToDefault();
        cancelOutstandingTask();
        FetchThumbnailForBadgeTask localFetchThumbnailForBadgeTask = new FetchThumbnailForBadgeTask(paramQuickContactBadge, paramRunnable);
        Long[] arrayOfLong = new Long[1];
        arrayOfLong[0] = Long.valueOf(paramPerson.getId());
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

    public Person getPerson() {
        return this.mPerson;
    }

    public void hideContactDetail() {
        hideView(2131296476);
        hideView(2131296475);
    }

    protected void onDetachedFromWindow() {
        cancelOutstandingTask();
        super.onDetachedFromWindow();
    }

    public void setActionButtonOnClickListener(ContactDisambiguationView.Callback paramCallback) {
    }

    public void setPerson(@Nonnull Person paramPerson, @Nullable List<Contact> paramList, @Nullable Runnable paramRunnable) {
        this.mPerson = paramPerson;
        setTextViewText(2131296464, paramPerson.getName());
        Object localObject1 = null;
        Object localObject2 = null;
        if (paramList == null) {
            if (!TextUtils.isEmpty((CharSequence) localObject2)) {
                break label219;
            }
            hideView(2131296476);
            if (!TextUtils.isEmpty((CharSequence) localObject1)) {
                break label208;
            }
            hideView(2131296475);
            label53:
            QuickContactBadge localQuickContactBadge = (QuickContactBadge) findViewById(2131296473);
            if (localQuickContactBadge == null) {
                break label236;
            }
            setQuickContactBadgeImage(localQuickContactBadge, paramPerson, paramRunnable);
            localQuickContactBadge.assignContactUri(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, paramPerson.getId()));
        }
        label208:
        label219:
        label236:
        while (paramRunnable == null) {
            return;
            if (paramList.size() == 1) {
                this.mSingleContact = ((Contact) paramList.get(0));
                localObject2 = this.mSingleContact.getValue();
                localObject1 = this.mSingleContact.getLabel(this.mResources);
                break;
            }
            LinkedHashSet localLinkedHashSet = Sets.newLinkedHashSet();
            Iterator localIterator = paramList.iterator();
            while (localIterator.hasNext()) {
                localLinkedHashSet.add(((Contact) localIterator.next()).getLabel(this.mResources));
            }
            localObject1 = TextUtils.join(", ", localLinkedHashSet);
            localObject2 = null;
            break;
            setTextViewText(2131296475, (CharSequence) localObject1);
            break label53;
            setTextViewText(2131296476, (CharSequence) localObject2);
            hideView(2131296475);
            break label53;
        }
        paramRunnable.run();
    }

    private class FetchThumbnailForBadgeTask
            extends AsyncTask<Long, Void, Drawable.ConstantState> {
        private final QuickContactBadge mBadge;
        @Nullable
        private final Runnable mCallback;

        public FetchThumbnailForBadgeTask(QuickContactBadge paramQuickContactBadge, @Nullable Runnable paramRunnable) {
            this.mBadge = paramQuickContactBadge;
            this.mCallback = paramRunnable;
        }

        protected Drawable.ConstantState doInBackground(Long... paramVarArgs) {
            Long localLong = (Long) Preconditions.checkNotNull(paramVarArgs[0]);
            Bitmap localBitmap = ContactLookup.fetchPhotoBitmap(PersonSelectItem.this.mContentResolver, localLong.longValue(), false);
            if (localBitmap == null) {
                PersonSelectItem.sPersonIdToThumbnail.put(localLong, PersonSelectItem.NULL_THUMBNAIL);
                return PersonSelectItem.NULL_THUMBNAIL;
            }
            Drawable.ConstantState localConstantState = new BitmapDrawable(PersonSelectItem.this.mResources, localBitmap).getConstantState();
            PersonSelectItem.sPersonIdToThumbnail.put(localLong, localConstantState);
            return localConstantState;
        }

        protected void onPostExecute(Drawable.ConstantState paramConstantState) {
            PersonSelectItem.this.setThumbnail(this.mBadge, paramConstantState);
            if (this.mCallback != null) {
                this.mCallback.run();
            }
            PersonSelectItem.access$502(PersonSelectItem.this, null);
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.contacts.PersonSelectItem

 * JD-Core Version:    0.7.0.1

 */