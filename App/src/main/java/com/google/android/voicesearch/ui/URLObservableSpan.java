package com.google.android.voicesearch.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;

public class URLObservableSpan
  extends URLSpan
{
  private URLSpanListener mListener;
  
  public URLObservableSpan(URLSpan paramURLSpan, URLSpanListener paramURLSpanListener)
  {
    super(paramURLSpan.getURL());
    this.mListener = paramURLSpanListener;
  }
  
  private InputMethodService getInputMethodService(Context paramContext)
  {
    if ((paramContext instanceof InputMethodService)) {
      return (InputMethodService)paramContext;
    }
    while ((paramContext instanceof ContextWrapper))
    {
      paramContext = ((ContextWrapper)paramContext).getBaseContext();
      if ((paramContext instanceof InputMethodService)) {
        return (InputMethodService)paramContext;
      }
    }
    return null;
  }
  
  public static Spanned replace(Spanned paramSpanned, URLSpanListener paramURLSpanListener)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramSpanned.toString());
    Object[] arrayOfObject = paramSpanned.getSpans(0, paramSpanned.length(), Object.class);
    int i = arrayOfObject.length;
    int j = 0;
    if (j < i)
    {
      Object localObject = arrayOfObject[j];
      if ((localObject instanceof URLSpan)) {
        localSpannableStringBuilder.setSpan(new URLObservableSpan((URLSpan)localObject, paramURLSpanListener), paramSpanned.getSpanStart(localObject), paramSpanned.getSpanEnd(localObject), paramSpanned.getSpanFlags(localObject));
      }
      for (;;)
      {
        j++;
        break;
        localSpannableStringBuilder.setSpan(localObject, paramSpanned.getSpanStart(localObject), paramSpanned.getSpanEnd(localObject), paramSpanned.getSpanFlags(localObject));
      }
    }
    return localSpannableStringBuilder;
  }
  
  public void onClick(View paramView)
  {
    this.mListener.onClick(getURL());
    Uri localUri = Uri.parse(getURL());
    Context localContext = paramView.getContext();
    Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
    localIntent.putExtra("com.android.browser.application_id", localContext.getPackageName());
    InputMethodService localInputMethodService = getInputMethodService(localContext);
    if (localInputMethodService != null)
    {
      localIntent.addFlags(268435456);
      localInputMethodService.requestHideSelf(0);
    }
    localContext.startActivity(localIntent);
  }
  
  public static abstract interface URLSpanListener
  {
    public abstract void onClick(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.ui.URLObservableSpan
 * JD-Core Version:    0.7.0.1
 */