package com.google.android.sidekick.shared.cards;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.shared.util.ScheduledSingleThreadedExecutor;
import com.google.android.sidekick.shared.client.PredictiveCardContainer;
import com.google.android.sidekick.shared.ui.EntryClickListener;
import com.google.android.sidekick.shared.util.ActivityHelper;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.TranslateEntry;

public class TranslateEntryAdapter
  extends BaseEntryAdapter
{
  private static final Uri TRANSLATE_URI = Uri.parse("http://translate.google.com/m/translate");
  private final Sidekick.TranslateEntry mTranslateEntry;
  private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
  
  public TranslateEntryAdapter(Sidekick.Entry paramEntry, ActivityHelper paramActivityHelper, ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor)
  {
    super(paramEntry, paramActivityHelper);
    this.mTranslateEntry = paramEntry.getTranslateEntry();
    this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
  }
  
  private void setTextWatcher(final EditText paramEditText, final TextWatcher paramTextWatcher, final String paramString)
  {
    paramEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean)
        {
          paramEditText.addTextChangedListener(paramTextWatcher);
          paramEditText.setHint("");
          return;
        }
        paramEditText.removeTextChangedListener(paramTextWatcher);
        paramEditText.setHint(paramString);
      }
    });
  }
  
  public View getView(Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup)
  {
    View localView = paramLayoutInflater.inflate(2130968888, paramViewGroup, false);
    ((TextView)localView.findViewById(2131297154)).setText(this.mTranslateEntry.getSourceLanguage());
    EditText localEditText1 = (EditText)localView.findViewById(2131297155);
    localEditText1.setHint(this.mTranslateEntry.getSourceText());
    ((TextView)localView.findViewById(2131297156)).setText(this.mTranslateEntry.getTargetLanguage());
    EditText localEditText2 = (EditText)localView.findViewById(2131297157);
    localEditText2.setHint(this.mTranslateEntry.getTargetText());
    TranslateTextWatcher localTranslateTextWatcher1 = new TranslateTextWatcher(this.mUiThreadExecutor, paramPredictiveCardContainer, localEditText2, this.mTranslateEntry.getSourceLanguageCode(), this.mTranslateEntry.getTargetLanguageCode());
    TranslateTextWatcher localTranslateTextWatcher2 = new TranslateTextWatcher(this.mUiThreadExecutor, paramPredictiveCardContainer, localEditText1, this.mTranslateEntry.getTargetLanguageCode(), this.mTranslateEntry.getSourceLanguageCode());
    setTextWatcher(localEditText1, localTranslateTextWatcher1, this.mTranslateEntry.getSourceText());
    setTextWatcher(localEditText2, localTranslateTextWatcher2, this.mTranslateEntry.getTargetText());
    setTranslateAction(paramContext, paramPredictiveCardContainer, localView.findViewById(2131297158), localEditText1, localEditText2);
    return localView;
  }
  
  void setTranslateAction(final Context paramContext, PredictiveCardContainer paramPredictiveCardContainer, View paramView, final EditText paramEditText1, final EditText paramEditText2)
  {
    paramView.setOnClickListener(new EntryClickListener(paramPredictiveCardContainer, getEntry(), 105)
    {
      public void onEntryClick(View paramAnonymousView)
      {
        Object localObject1 = TranslateEntryAdapter.this.mTranslateEntry.getSourceLanguageCode();
        Object localObject2 = TranslateEntryAdapter.this.mTranslateEntry.getTargetLanguageCode();
        String str = paramEditText1.getText().toString();
        if (TextUtils.isEmpty(str))
        {
          if (paramEditText2.getText().toString().isEmpty()) {
            break label122;
          }
          str = paramEditText2.getText().toString();
          Object localObject3 = localObject2;
          localObject2 = localObject1;
          localObject1 = localObject3;
        }
        for (;;)
        {
          Uri localUri = TranslateEntryAdapter.TRANSLATE_URI.buildUpon().appendQueryParameter("sl", (String)localObject1).appendQueryParameter("tl", (String)localObject2).appendQueryParameter("q", str).build();
          TranslateEntryAdapter.this.openUrl(paramContext, localUri);
          return;
          label122:
          if (TranslateEntryAdapter.this.mTranslateEntry.hasSourceText()) {
            str = TranslateEntryAdapter.this.mTranslateEntry.getSourceText();
          }
        }
      }
    });
  }
  
  private static class InPlaceTranslateTask
    extends AsyncTask<String, Integer, String>
  {
    private final PredictiveCardContainer mCardContainer;
    private final String mFrom;
    private final EditText mTargetEditText;
    private final String mTo;
    
    InPlaceTranslateTask(PredictiveCardContainer paramPredictiveCardContainer, EditText paramEditText, String paramString1, String paramString2)
    {
      this.mCardContainer = paramPredictiveCardContainer;
      this.mTargetEditText = paramEditText;
      this.mFrom = paramString1;
      this.mTo = paramString2;
    }
    
    protected String doInBackground(String... paramVarArgs)
    {
      String str = paramVarArgs[0];
      return this.mCardContainer.translateInPlace(str, this.mFrom, this.mTo);
    }
    
    protected void onPostExecute(String paramString)
    {
      if (paramString != null) {
        this.mTargetEditText.setText(paramString);
      }
    }
  }
  
  private static class TranslateRunnable
    implements Runnable
  {
    private TranslateEntryAdapter.InPlaceTranslateTask mInPlaceTranslateTask;
    private String mQueryText;
    
    TranslateRunnable(String paramString, TranslateEntryAdapter.InPlaceTranslateTask paramInPlaceTranslateTask)
    {
      this.mQueryText = paramString;
      this.mInPlaceTranslateTask = paramInPlaceTranslateTask;
    }
    
    public void run()
    {
      if ((this.mInPlaceTranslateTask.isCancelled()) || (this.mInPlaceTranslateTask.getStatus() != AsyncTask.Status.PENDING)) {
        return;
      }
      TranslateEntryAdapter.InPlaceTranslateTask localInPlaceTranslateTask = this.mInPlaceTranslateTask;
      String[] arrayOfString = new String[1];
      arrayOfString[0] = this.mQueryText;
      localInPlaceTranslateTask.execute(arrayOfString);
    }
  }
  
  private static class TranslateTextWatcher
    implements TextWatcher
  {
    private final PredictiveCardContainer mCardContainer;
    private final String mFrom;
    private TranslateEntryAdapter.InPlaceTranslateTask mInPlaceTranslateTask;
    private final EditText mOtherTextView;
    private final String mTo;
    private final ScheduledSingleThreadedExecutor mUiThreadExecutor;
    private String queryText;
    
    TranslateTextWatcher(ScheduledSingleThreadedExecutor paramScheduledSingleThreadedExecutor, PredictiveCardContainer paramPredictiveCardContainer, EditText paramEditText, String paramString1, String paramString2)
    {
      this.mUiThreadExecutor = paramScheduledSingleThreadedExecutor;
      this.mCardContainer = paramPredictiveCardContainer;
      this.mOtherTextView = paramEditText;
      this.mFrom = paramString1;
      this.mTo = paramString2;
    }
    
    public void afterTextChanged(Editable paramEditable)
    {
      if ((this.mInPlaceTranslateTask != null) && (this.mInPlaceTranslateTask.getStatus() != AsyncTask.Status.FINISHED))
      {
        this.mInPlaceTranslateTask.cancel(true);
        this.mInPlaceTranslateTask = null;
      }
      this.queryText = paramEditable.toString();
      if (this.queryText.length() == 0)
      {
        this.mOtherTextView.setText("");
        return;
      }
      this.mInPlaceTranslateTask = new TranslateEntryAdapter.InPlaceTranslateTask(this.mCardContainer, this.mOtherTextView, this.mFrom, this.mTo);
      this.mUiThreadExecutor.executeDelayed(new TranslateEntryAdapter.TranslateRunnable(this.queryText, this.mInPlaceTranslateTask), 300L);
    }
    
    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      if (!this.mOtherTextView.getText().toString().endsWith("…")) {
        this.mOtherTextView.setText(this.mOtherTextView.getText().append("…"));
      }
    }
    
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.cards.TranslateEntryAdapter
 * JD-Core Version:    0.7.0.1
 */