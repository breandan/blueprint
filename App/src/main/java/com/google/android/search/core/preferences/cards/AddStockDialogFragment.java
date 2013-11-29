package com.google.android.search.core.preferences.cards;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import com.google.android.search.core.CoreSearchServices;
import com.google.android.search.core.ui.HighlightingArrayAdapter;
import com.google.android.search.core.ui.SuggestionDialog;
import com.google.android.search.core.ui.SuggestionDialog.SuggestionFetcher;
import com.google.android.search.core.util.HttpHelper;
import com.google.android.search.core.util.HttpHelper.GetRequest;
import com.google.android.sidekick.shared.util.Tag;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.velvet.VelvetServices;
import com.google.common.collect.Lists;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.StockQuotes.StockData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddStockDialogFragment
  extends DialogFragment
  implements SimpleCallback<StockDataWithName>
{
  private static final String TAG = Tag.getTag(AddStockDialogFragment.class);
  private SuggestionDialog<StockDataWithName> mDialog;
  private Integer mListPosition;
  private TickerFetcherFragment mWorkerFragment;
  
  public static AddStockDialogFragment newInstance(Fragment paramFragment, String paramString, @Nullable Integer paramInteger)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("WORKER_FRAGMENT_TAG_KEY", paramString);
    if (paramInteger != null) {
      localBundle.putInt("LIST_POSITION_KEY", paramInteger.intValue());
    }
    AddStockDialogFragment localAddStockDialogFragment = new AddStockDialogFragment();
    localAddStockDialogFragment.setArguments(localBundle);
    localAddStockDialogFragment.setTargetFragment(paramFragment, 0);
    return localAddStockDialogFragment;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setAdapter(this.mWorkerFragment.getAdapter());
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    this.mWorkerFragment = ((TickerFetcherFragment)getActivity().getFragmentManager().findFragmentByTag(getArguments().getString("WORKER_FRAGMENT_TAG_KEY")));
    this.mListPosition = Integer.valueOf(getArguments().getInt("LIST_POSITION_KEY"));
    this.mDialog = new SuggestionDialog(getActivity(), 2130968584, this, this.mWorkerFragment, 2131362693);
    this.mDialog.getWindow().setSoftInputMode(5);
    return this.mDialog;
  }
  
  public void onDestroy()
  {
    super.onDestroy();
    this.mWorkerFragment.detachStockDialog();
  }
  
  public void onResult(StockDataWithName paramStockDataWithName)
  {
    ((StockFragment)getTargetFragment()).addStock(paramStockDataWithName, this.mListPosition);
    dismiss();
  }
  
  public void onStart()
  {
    super.onStart();
    Dialog localDialog = getDialog();
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
    localLayoutParams.copyFrom(localDialog.getWindow().getAttributes());
    localLayoutParams.width = -1;
    localLayoutParams.height = -1;
    localDialog.getWindow().setAttributes(localLayoutParams);
  }
  
  public void setAdapter(BaseAdapter paramBaseAdapter)
  {
    this.mDialog.setAdapter(paramBaseAdapter);
  }
  
  public static class StockDataWithName
  {
    private final Sidekick.SidekickConfiguration.StockQuotes.StockData stockData;
    private final String symbol;
    
    StockDataWithName(String paramString, Sidekick.SidekickConfiguration.StockQuotes.StockData paramStockData)
    {
      this.symbol = paramString;
      this.stockData = paramStockData;
    }
    
    public Sidekick.SidekickConfiguration.StockQuotes.StockData getStockData()
    {
      return this.stockData;
    }
    
    public String toString()
    {
      return this.symbol + " : " + this.stockData.getDescription() + " (" + this.stockData.getExchange() + ")";
    }
  }
  
  public static abstract interface StockFragment
  {
    public abstract void addStock(AddStockDialogFragment.StockDataWithName paramStockDataWithName, @Nullable Integer paramInteger);
  }
  
  public static class TickerFetcherFragment
    extends Fragment
    implements SuggestionDialog.SuggestionFetcher
  {
    private HighlightingArrayAdapter<AddStockDialogFragment.StockDataWithName> mAdapter;
    private volatile boolean mHasNetworkError;
    
    void detachStockDialog()
    {
      this.mAdapter.clear();
    }
    
    public ArrayAdapter<AddStockDialogFragment.StockDataWithName> getAdapter()
    {
      return this.mAdapter;
    }
    
    public boolean hasNetworkError()
    {
      return this.mHasNetworkError;
    }
    
    public void onActivityCreated(Bundle paramBundle)
    {
      super.onActivityCreated(paramBundle);
      ArrayList localArrayList = Lists.newArrayList();
      this.mAdapter = new HighlightingArrayAdapter(getActivity(), localArrayList);
    }
    
    public void onCreate(Bundle paramBundle)
    {
      super.onCreate(paramBundle);
      setRetainInstance(true);
    }
    
    public void startFetchingSuggestions(String paramString)
    {
      if (paramString.isEmpty())
      {
        this.mAdapter.clear();
        return;
      }
      new StockSymbolSuggestTask(paramString).execute(new Void[0]);
    }
    
    class StockSymbolSuggestTask
      extends AsyncTask<Void, Void, List<Sidekick.SidekickConfiguration.StockQuotes.StockData>>
    {
      private final HttpHelper mHttpHelper;
      private final String symbol;
      
      StockSymbolSuggestTask(String paramString)
      {
        this.symbol = paramString;
        this.mHttpHelper = VelvetServices.get().getCoreServices().getHttpHelper();
      }
      
      private List<Sidekick.SidekickConfiguration.StockQuotes.StockData> getStockData(JSONObject paramJSONObject)
      {
        localArrayList = Lists.newArrayList();
        try
        {
          JSONArray localJSONArray1 = paramJSONObject.getJSONArray("matches");
          int i = 0;
          if (i < localJSONArray1.length())
          {
            JSONObject localJSONObject = localJSONArray1.getJSONObject(i);
            JSONArray localJSONArray2 = localJSONObject.names();
            Sidekick.SidekickConfiguration.StockQuotes.StockData localStockData = new Sidekick.SidekickConfiguration.StockQuotes.StockData();
            for (int j = 0;; j++) {
              if (j < localJSONArray2.length())
              {
                String str1 = localJSONArray2.getString(j);
                String str2 = localJSONObject.getString(str1);
                if (!str2.isEmpty()) {
                  if (str1.equals("id")) {
                    localStockData.setGin(Long.valueOf(str2).longValue());
                  } else if (str1.equals("t")) {
                    localStockData.setSymbol(str2);
                  } else if (str1.equals("e")) {
                    localStockData.setExchange(str2);
                  } else if (str1.equals("n")) {
                    localStockData.setDescription(str2);
                  }
                }
              }
              else
              {
                if ((localStockData.hasGin()) && (localStockData.hasDescription()) && (localStockData.hasExchange()) && (localStockData.hasSymbol())) {
                  localArrayList.add(localStockData);
                }
                i++;
                break;
              }
            }
          }
          return localArrayList;
        }
        catch (JSONException localJSONException) {}
      }
      
      private JSONObject sendRequest(String paramString)
      {
        HttpHelper.GetRequest localGetRequest = new HttpHelper.GetRequest("http://www.google.com/finance/match?q=" + paramString);
        try
        {
          byte[] arrayOfByte = this.mHttpHelper.rawGet(localGetRequest, 8);
          if (arrayOfByte == null) {
            return null;
          }
          long l1 = System.currentTimeMillis();
          try
          {
            JSONObject localJSONObject = new JSONObject(new String(arrayOfByte));
            long l2 = System.currentTimeMillis();
            (l2 - l1);
            return localJSONObject;
          }
          catch (JSONException localJSONException)
          {
            return null;
          }
          return null;
        }
        catch (IOException localIOException)
        {
          Log.w(AddStockDialogFragment.TAG, "Network error: " + localIOException);
        }
      }
      
      protected List<Sidekick.SidekickConfiguration.StockQuotes.StockData> doInBackground(Void... paramVarArgs)
      {
        JSONObject localJSONObject = sendRequest(this.symbol);
        AddStockDialogFragment.TickerFetcherFragment localTickerFetcherFragment = AddStockDialogFragment.TickerFetcherFragment.this;
        if (localJSONObject == null) {}
        for (boolean bool = true;; bool = false)
        {
          AddStockDialogFragment.TickerFetcherFragment.access$002(localTickerFetcherFragment, bool);
          if (!AddStockDialogFragment.TickerFetcherFragment.this.mHasNetworkError) {
            break;
          }
          return Lists.newArrayList();
        }
        return getStockData(localJSONObject);
      }
      
      protected void onPostExecute(List<Sidekick.SidekickConfiguration.StockQuotes.StockData> paramList)
      {
        ArrayList localArrayList = Lists.newArrayList();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          Sidekick.SidekickConfiguration.StockQuotes.StockData localStockData = (Sidekick.SidekickConfiguration.StockQuotes.StockData)localIterator.next();
          localArrayList.add(new AddStockDialogFragment.StockDataWithName(localStockData.getSymbol(), localStockData));
        }
        AddStockDialogFragment.TickerFetcherFragment.this.mAdapter.update(localArrayList, this.symbol);
        AddStockDialogFragment.TickerFetcherFragment.this.mAdapter.notifyDataSetChanged();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.cards.AddStockDialogFragment
 * JD-Core Version:    0.7.0.1
 */