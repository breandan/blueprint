package com.google.android.velvet.gallery;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import com.google.android.velvet.VelvetServices;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ImageProvider
        extends ContentProvider {
    private static final Map<String, Integer> COLUMN_INDEXES = new HashMap();
    public static final Uri CONTENT_URI;
    private static final Object[] EMPTY_LOADING_ROW;
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    static {
        CONTENT_URI = Uri.parse("content://com.google.android.velvet.gallery.ImageProvider/images");
        COLUMN_INDEXES.put("uri", Integer.valueOf(0));
        COLUMN_INDEXES.put("_display_name", Integer.valueOf(1));
        COLUMN_INDEXES.put("contentUri", Integer.valueOf(2));
        COLUMN_INDEXES.put("thumbnailUri", Integer.valueOf(3));
        COLUMN_INDEXES.put("contentType", Integer.valueOf(4));
        COLUMN_INDEXES.put("loadingIndicator", Integer.valueOf(5));
        COLUMN_INDEXES.put("domain", Integer.valueOf(6));
        COLUMN_INDEXES.put("width", Integer.valueOf(7));
        COLUMN_INDEXES.put("height", Integer.valueOf(8));
        COLUMN_INDEXES.put("source", Integer.valueOf(9));
        COLUMN_INDEXES.put("id", Integer.valueOf(10));
        COLUMN_INDEXES.put("sectionNumber", Integer.valueOf(11));
        COLUMN_INDEXES.put("nav_uri", Integer.valueOf(12));
        URI_MATCHER.addURI("com.google.android.velvet.gallery.ImageProvider", "images", 1);
        URI_MATCHER.addURI("com.google.android.velvet.gallery.ImageProvider", "images/#", 2);
        URI_MATCHER.addURI("com.google.android.velvet.gallery.ImageProvider", "images/loading", 3);
        Object[] arrayOfObject = new Object[13];
        arrayOfObject[0] = (CONTENT_URI + "/loading");
        arrayOfObject[1] = "";
        arrayOfObject[2] = null;
        arrayOfObject[3] = null;
        arrayOfObject[4] = "com.google.android.velvet.gallery/image";
        arrayOfObject[5] = Boolean.valueOf(true);
        arrayOfObject[6] = "";
        arrayOfObject[7] = Integer.valueOf(0);
        arrayOfObject[8] = Integer.valueOf(0);
        arrayOfObject[9] = "";
        arrayOfObject[10] = "";
        arrayOfObject[11] = Integer.valueOf(0);
        arrayOfObject[12] = "";
        EMPTY_LOADING_ROW = arrayOfObject;
    }

    private ImageMetadataController getController() {
        return VelvetServices.get().getCoreServices().getImageMetadataController();
    }

    private Object[] objectFromInfo(int paramInt, VelvetImage paramVelvetImage) {
        Object[] arrayOfObject = new Object[13];
        arrayOfObject[0] = (CONTENT_URI + "/" + paramInt);
        arrayOfObject[1] = Strings.nullToEmpty(paramVelvetImage.getName());
        arrayOfObject[2] = Strings.nullToEmpty(paramVelvetImage.getUri());
        arrayOfObject[3] = Strings.nullToEmpty(paramVelvetImage.getThumbnailUri());
        arrayOfObject[4] = "com.google.android.velvet.gallery/image";
        arrayOfObject[5] = Boolean.valueOf(false);
        arrayOfObject[6] = Strings.nullToEmpty(paramVelvetImage.getDomain());
        arrayOfObject[7] = Integer.valueOf(paramVelvetImage.getWidth());
        arrayOfObject[8] = Integer.valueOf(paramVelvetImage.getHeight());
        arrayOfObject[9] = Strings.nullToEmpty(paramVelvetImage.getSourceUri());
        arrayOfObject[10] = Strings.nullToEmpty(paramVelvetImage.getId());
        arrayOfObject[11] = Integer.valueOf(0);
        arrayOfObject[12] = Strings.nullToEmpty(paramVelvetImage.getNavigationUri());
        return arrayOfObject;
    }

    private Object[] project(Object[] paramArrayOfObject, String[] paramArrayOfString) {
        Object[] arrayOfObject = new Object[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++) {
            arrayOfObject[i] = paramArrayOfObject[((Integer) COLUMN_INDEXES.get(paramArrayOfString[i])).intValue()];
        }
        return arrayOfObject;
    }

    public static void reportMoreImagesAvailable(Context paramContext, int paramInt) {
        Uri localUri = ContentUris.withAppendedId(CONTENT_URI, paramInt);
        paramContext.getContentResolver().notifyChange(localUri, null);
    }

    public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
        throw new UnsupportedOperationException("ImageProvider does not support delete");
    }

    public String getType(Uri paramUri) {
        return "com.google.android.velvet.gallery/image";
    }

    public Uri insert(Uri paramUri, ContentValues paramContentValues) {
        throw new UnsupportedOperationException("ImageProvider does not support insert");
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
        MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString1);
        int j;
        Iterator localIterator;
        switch (URI_MATCHER.match(paramUri)) {
            default:
                throw new IllegalArgumentException("Invalid uri: " + paramUri.toString());
            case 1:
                j = 0;
                localIterator = getController().getImages().iterator();
            case 2:
                while (localIterator.hasNext()) {
                    localMatrixCursor.addRow(project(objectFromInfo(j, (VelvetImage) localIterator.next()), paramArrayOfString1));
                    j++;
                    continue;
                    int i = Integer.parseInt(paramUri.getLastPathSegment());
                    VelvetImage localVelvetImage = getController().getImage(i);
                    if (localVelvetImage != null) {
                        localMatrixCursor.addRow(objectFromInfo(i, localVelvetImage));
                    }
                }
        }
        for (; ; ) {
            localMatrixCursor.setNotificationUri(getContext().getContentResolver(), paramUri);
            return localMatrixCursor;
            localMatrixCursor.addRow(project(EMPTY_LOADING_ROW, paramArrayOfString1));
        }
    }

    public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
        throw new UnsupportedOperationException("ImageProvider does not support update()");
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.ImageProvider

 * JD-Core Version:    0.7.0.1

 */