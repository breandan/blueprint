package com.google.android.velvet.gallery;

public class VelvetImage {
    private String mDomain;
    private int mHeight;
    private String mId;
    private String mName;
    private String mNavigationUri;
    private String mSnippet;
    private String mSourceUri;
    private int mThumbnailHeight;
    private String mThumbnailUri;
    private int mThumbnailWidth;
    private String mUri;
    private int mWidth;

    public VelvetImage() {
    }

    public VelvetImage(String paramString) {
        this.mId = paramString;
    }

    public boolean equals(Object paramObject) {
        if (paramObject == this) {
        }
        do {
            return true;
            if (!(paramObject instanceof VelvetImage)) {
                return false;
            }
        } while (((VelvetImage) paramObject).getId().equals(this.mId));
        return false;
    }

    public String getDomain() {
        return this.mDomain;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public String getNavigationUri() {
        return this.mNavigationUri;
    }

    public String getSnippet() {
        return this.mSnippet;
    }

    public String getSourceUri() {
        return this.mSourceUri;
    }

    public int getThumbnailHeight() {
        return this.mThumbnailHeight;
    }

    public String getThumbnailUri() {
        return this.mThumbnailUri;
    }

    public int getThumbnailWidth() {
        return this.mThumbnailWidth;
    }

    public String getUri() {
        return this.mUri;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int hashCode() {
        if (this.mId != null) {
            return this.mId.hashCode();
        }
        return super.hashCode();
    }

    public void setDomain(String paramString) {
        this.mDomain = paramString;
    }

    public void setHeight(int paramInt) {
        this.mHeight = paramInt;
    }

    public void setId(String paramString) {
        this.mId = paramString;
    }

    public void setName(String paramString) {
        this.mName = paramString;
    }

    public void setNavigationUri(String paramString) {
        this.mNavigationUri = paramString;
    }

    public void setSnippet(String paramString) {
        this.mSnippet = paramString;
    }

    public void setSourceUri(String paramString) {
        this.mSourceUri = paramString;
    }

    public void setThumbnailHeight(int paramInt) {
        this.mThumbnailHeight = paramInt;
    }

    public void setThumbnailUri(String paramString) {
        this.mThumbnailUri = paramString;
    }

    public void setThumbnailWidth(int paramInt) {
        this.mThumbnailWidth = paramInt;
    }

    public void setUri(String paramString) {
        this.mUri = paramString;
    }

    public void setWidth(int paramInt) {
        this.mWidth = paramInt;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.VelvetImage

 * JD-Core Version:    0.7.0.1

 */