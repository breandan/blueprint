package com.android.launcher3;

import android.graphics.Bitmap;
import java.util.ArrayList;

class AsyncTaskPageData
{
  AsyncTaskCallback doInBackgroundCallback;
  ArrayList<Bitmap> generatedImages;
  ArrayList<Object> items;
  int maxImageHeight;
  int maxImageWidth;
  int page;
  AsyncTaskCallback postExecuteCallback;
  WidgetPreviewLoader widgetPreviewLoader;
  
  AsyncTaskPageData(int paramInt1, ArrayList<Object> paramArrayList, int paramInt2, int paramInt3, AsyncTaskCallback paramAsyncTaskCallback1, AsyncTaskCallback paramAsyncTaskCallback2, WidgetPreviewLoader paramWidgetPreviewLoader)
  {
    this.page = paramInt1;
    this.items = paramArrayList;
    this.generatedImages = new ArrayList();
    this.maxImageWidth = paramInt2;
    this.maxImageHeight = paramInt3;
    this.doInBackgroundCallback = paramAsyncTaskCallback1;
    this.postExecuteCallback = paramAsyncTaskCallback2;
    this.widgetPreviewLoader = paramWidgetPreviewLoader;
  }
  
  void cleanup(boolean paramBoolean)
  {
    if (this.generatedImages != null)
    {
      if (paramBoolean) {
        for (int i = 0; i < this.generatedImages.size(); i++) {
          this.widgetPreviewLoader.recycleBitmap(this.items.get(i), (Bitmap)this.generatedImages.get(i));
        }
      }
      this.generatedImages.clear();
    }
  }
  
  static enum Type
  {
    static
    {
      Type[] arrayOfType = new Type[1];
      arrayOfType[0] = LoadWidgetPreviewData;
      $VALUES = arrayOfType;
    }
    
    private Type() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AsyncTaskPageData
 * JD-Core Version:    0.7.0.1
 */