package com.android.launcher3;

import android.os.AsyncTask;
import android.os.Process;

class AppsCustomizeAsyncTask
  extends AsyncTask<AsyncTaskPageData, Void, AsyncTaskPageData>
{
  AsyncTaskPageData.Type dataType;
  int page;
  int threadPriority;
  
  AppsCustomizeAsyncTask(int paramInt, AsyncTaskPageData.Type paramType)
  {
    this.page = paramInt;
    this.threadPriority = 0;
    this.dataType = paramType;
  }
  
  protected AsyncTaskPageData doInBackground(AsyncTaskPageData... paramVarArgs)
  {
    if (paramVarArgs.length != 1) {
      return null;
    }
    paramVarArgs[0].doInBackgroundCallback.run(this, paramVarArgs[0]);
    return paramVarArgs[0];
  }
  
  protected void onPostExecute(AsyncTaskPageData paramAsyncTaskPageData)
  {
    paramAsyncTaskPageData.postExecuteCallback.run(this, paramAsyncTaskPageData);
  }
  
  void setThreadPriority(int paramInt)
  {
    this.threadPriority = paramInt;
  }
  
  void syncThreadPriority()
  {
    Process.setThreadPriority(this.threadPriority);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.launcher3.AppsCustomizeAsyncTask
 * JD-Core Version:    0.7.0.1
 */