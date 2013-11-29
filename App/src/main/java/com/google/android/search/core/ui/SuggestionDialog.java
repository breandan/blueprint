package com.google.android.search.core.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.shared.util.Util;
import com.google.android.speech.callback.SimpleCallback;

public class SuggestionDialog<T>
  extends AlertDialog
  implements TextWatcher, View.OnFocusChangeListener
{
  private final SimpleCallback<T> mCallback;
  private final int mEmptyResultsMessageId;
  private final EditText mFilterEditText;
  private final TextView mNoResultsMessageView;
  private boolean mSelectAllOnFocus;
  private final SuggestionFetcher mSuggestionFetcher;
  private final ListView mSuggestionListView;
  
  public SuggestionDialog(Context paramContext, int paramInt1, SimpleCallback<T> paramSimpleCallback, SuggestionFetcher paramSuggestionFetcher, int paramInt2)
  {
    super(paramContext);
    this.mCallback = paramSimpleCallback;
    this.mSuggestionFetcher = paramSuggestionFetcher;
    View localView = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(paramInt1, null);
    setView(localView);
    this.mFilterEditText = ((EditText)localView.findViewById(2131296300));
    this.mSuggestionListView = ((ListView)localView.findViewById(2131296301));
    this.mNoResultsMessageView = ((TextView)localView.findViewById(2131296302));
    this.mEmptyResultsMessageId = paramInt2;
    this.mFilterEditText.addTextChangedListener(this);
    getWindow().setSoftInputMode(5);
  }
  
  private void showNoResultsMessageView(boolean paramBoolean)
  {
    int i;
    int j;
    if (this.mSuggestionListView.getEmptyView() != null)
    {
      i = 1;
      if (!this.mSuggestionFetcher.hasNetworkError()) {
        break label60;
      }
      j = 2131363518;
      label27:
      this.mNoResultsMessageView.setText(j);
      if ((!paramBoolean) || (i != 0)) {
        break label68;
      }
      this.mSuggestionListView.setEmptyView(this.mNoResultsMessageView);
    }
    label60:
    label68:
    while ((paramBoolean) || (i == 0))
    {
      return;
      i = 0;
      break;
      j = this.mEmptyResultsMessageId;
      break label27;
    }
    this.mNoResultsMessageView.setVisibility(8);
    this.mSuggestionListView.setEmptyView(null);
  }
  
  public void afterTextChanged(Editable paramEditable)
  {
    String str = paramEditable.toString();
    if (TextUtils.isEmpty(str)) {
      showNoResultsMessageView(false);
    }
    this.mSuggestionFetcher.startFetchingSuggestions(str);
  }
  
  public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onAttachedToWindow()
  {
    View localView = findViewById(16908290);
    if (localView != null) {
      localView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          SuggestionDialog.this.dismiss();
        }
      });
    }
  }
  
  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if ((this.mSelectAllOnFocus) && (paramView.equals(this.mFilterEditText)) && (paramBoolean))
    {
      this.mFilterEditText.selectAll();
      this.mSelectAllOnFocus = false;
    }
  }
  
  public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void setAdapter(BaseAdapter paramBaseAdapter)
  {
    paramBaseAdapter.registerDataSetObserver(new DataSetObserver()
    {
      public void onChanged()
      {
        SuggestionDialog localSuggestionDialog = SuggestionDialog.this;
        if (!TextUtils.isEmpty(SuggestionDialog.this.mFilterEditText.getText().toString())) {}
        for (boolean bool = true;; bool = false)
        {
          localSuggestionDialog.showNoResultsMessageView(bool);
          return;
        }
      }
    });
    this.mSuggestionListView.setAdapter(paramBaseAdapter);
    this.mSuggestionListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SuggestionDialog.this.mCallback.onResult(paramAnonymousAdapterView.getAdapter().getItem(paramAnonymousInt));
        Util.hideSoftKeyboard(SuggestionDialog.this.getContext(), SuggestionDialog.this.mFilterEditText);
        SuggestionDialog.this.dismiss();
      }
    });
    String str = this.mFilterEditText.getText().toString();
    this.mSuggestionFetcher.startFetchingSuggestions(str);
  }
  
  public void setFilter(String paramString)
  {
    this.mFilterEditText.setText(paramString);
    this.mFilterEditText.setSelectAllOnFocus(true);
    this.mFilterEditText.setOnFocusChangeListener(this);
    this.mSelectAllOnFocus = true;
    if (!TextUtils.isEmpty(paramString))
    {
      this.mFilterEditText.setSelection(paramString.length());
      this.mFilterEditText.selectAll();
    }
  }
  
  public static abstract interface SuggestionFetcher
  {
    public abstract boolean hasNetworkError();
    
    public abstract void startFetchingSuggestions(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.ui.SuggestionDialog
 * JD-Core Version:    0.7.0.1
 */