package com.google.android.velvet.gallery;

import android.text.Html;
import android.util.JsonReader;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.common.io.Closeables;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ImageMetadataParser {
    private VelvetImage readImage(JsonReader paramJsonReader)
            throws IOException {
        VelvetImage localVelvetImage = new VelvetImage();
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext()) {
            String str = paramJsonReader.nextName();
            if (str.equals("id")) {
                localVelvetImage.setId(paramJsonReader.nextString());
            } else if (str.equals("oh")) {
                localVelvetImage.setHeight(paramJsonReader.nextInt());
            } else if (str.equals("ow")) {
                localVelvetImage.setWidth(paramJsonReader.nextInt());
            } else if (str.equals("ou")) {
                localVelvetImage.setUri(paramJsonReader.nextString());
            } else if (str.equals("rh")) {
                localVelvetImage.setDomain(paramJsonReader.nextString());
            } else if (str.equals("ru")) {
                localVelvetImage.setSourceUri(paramJsonReader.nextString());
            } else if (str.equals("s")) {
                localVelvetImage.setSnippet(paramJsonReader.nextString());
            } else if (str.equals("pt")) {
                localVelvetImage.setName(Html.fromHtml(paramJsonReader.nextString()).toString());
            } else if (str.equals("th")) {
                List localList = readThumbnailArray(paramJsonReader);
                if (localList.size() > 0) {
                    Thumbnail localThumbnail = (Thumbnail) localList.get(-1 + localList.size());
                    localVelvetImage.setThumbnailHeight(localThumbnail.height);
                    localVelvetImage.setThumbnailWidth(localThumbnail.width);
                    localVelvetImage.setThumbnailUri(localThumbnail.url);
                }
            } else {
                paramJsonReader.skipValue();
            }
        }
        paramJsonReader.endObject();
        if ((localVelvetImage.getUri() == null) || (localVelvetImage.getThumbnailUri() == null)) {
            Log.v("ImageMetadataParser", "Rejecting image " + localVelvetImage.toString() + ", has empty url or thumbnail url");
            localVelvetImage = null;
        }
        while ((localVelvetImage.getName() != null) && (!localVelvetImage.getName().isEmpty())) {
            return localVelvetImage;
        }
        Log.v("ImageMetadataParser", "Image has no name, using domain instead");
        localVelvetImage.setName(localVelvetImage.getDomain());
        return localVelvetImage;
    }

    private List<VelvetImage> readImageArray(JsonReader paramJsonReader)
            throws IOException {
        ArrayList localArrayList = Lists.newArrayList();
        try {
            paramJsonReader.beginArray();
            while (paramJsonReader.hasNext()) {
                VelvetImage localVelvetImage = readImage(paramJsonReader);
                if (localVelvetImage != null) {
                    localArrayList.add(localVelvetImage);
                }
            }
            paramJsonReader.endArray();
        } catch (IllegalStateException localIllegalStateException) {
            Log.e("ImageMetadataParser", "Error whilst parsing metadata:", localIllegalStateException);
            return localArrayList;
        }
        return localArrayList;
    }

    private Thumbnail readThumbnail(JsonReader paramJsonReader)
            throws IOException {
        Thumbnail localThumbnail = new Thumbnail(null);
        paramJsonReader.beginObject();
        while (paramJsonReader.hasNext()) {
            String str = paramJsonReader.nextName();
            if (str.equals("h")) {
                localThumbnail.height = paramJsonReader.nextInt();
            } else if (str.equals("w")) {
                localThumbnail.width = paramJsonReader.nextInt();
            } else if (str.equals("u")) {
                localThumbnail.url = paramJsonReader.nextString();
            } else {
                paramJsonReader.skipValue();
            }
        }
        paramJsonReader.endObject();
        return localThumbnail;
    }

    private List<Thumbnail> readThumbnailArray(JsonReader paramJsonReader)
            throws IOException {
        ArrayList localArrayList = Lists.newArrayList();
        paramJsonReader.beginArray();
        while (paramJsonReader.hasNext()) {
            localArrayList.add(readThumbnail(paramJsonReader));
        }
        paramJsonReader.endArray();
        return localArrayList;
    }

    public List<VelvetImage> readJson(String paramString) {
        JsonReader localJsonReader = new JsonReader(new StringReader(paramString));
        try {
            List localList = readImageArray(localJsonReader);
            return localList;
        } catch (IOException localIOException) {
            Log.e("ImageMetadataParser", "Error whilst parsing metadata:", localIOException);
            return null;
        } finally {
            Closeables.closeQuietly(localJsonReader);
        }
    }

    private class Thumbnail {
        public int height;
        public String url;
        public int width;

        private Thumbnail() {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.ImageMetadataParser

 * JD-Core Version:    0.7.0.1

 */