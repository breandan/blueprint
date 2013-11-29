package com.google.android.search.core.google.gaia;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.accounts.AuthenticatorException;
import android.accounts.OnAccountsUpdateListener;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.android.gsf.GoogleLoginServiceConstants;
import com.google.android.search.core.NowOptInSettings;
import com.google.android.search.core.SearchSettings;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.ExtraPreconditions;
import com.google.android.speech.callback.SimpleCallback;
import com.google.android.speech.helper.AccountHelper;
import com.google.android.speech.helper.AuthTokenHelper;
import com.google.android.velvet.VelvetStrictMode;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.android.voicesearch.logger.EventLogger;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LoginHelper
  implements AccountHelper, AuthTokenHelper
{
  @Nullable
  private Account mAccount;
  private final AccountManager mAccountManager;
  @Nullable
  private Account[] mAccounts;
  private final OnAccountsUpdateListener mAccountsListener;
  private final GoogleAuthAdapter mAuthAdapter;
  private final Executor mBackgroundExecutor;
  private final Context mContext;
  private final DataSetObservable mDataSetObservable = new DataSetObservable();
  private final Supplier<NowOptInSettings> mNowOptInSettingsSupplier;
  private final SearchSettings mSettings;
  
  public LoginHelper(Context paramContext, SearchSettings paramSearchSettings, Executor paramExecutor, GoogleAuthAdapter paramGoogleAuthAdapter, AccountManager paramAccountManager, Supplier<NowOptInSettings> paramSupplier)
  {
    this.mContext = paramContext;
    this.mSettings = paramSearchSettings;
    this.mAccountManager = paramAccountManager;
    this.mNowOptInSettingsSupplier = paramSupplier;
    this.mAccountsListener = new OnAccountsUpdateListener()
    {
      public void onAccountsUpdated(Account[] paramAnonymousArrayOfAccount)
      {
        LoginHelper.this.refresh();
      }
    };
    this.mBackgroundExecutor = paramExecutor;
    this.mAuthAdapter = paramGoogleAuthAdapter;
    if (this.mAccountManager == null)
    {
      Log.w("Search.LoginHelper", "Missing account manager.");
      return;
    }
    this.mAccountManager.addOnAccountsUpdatedListener(this.mAccountsListener, null, false);
  }
  
  private Future<List<String>> getAllTokens(String paramString)
  {
    Account[] arrayOfAccount = getAllAccounts();
    ArrayList localArrayList = Lists.newArrayList();
    int i = arrayOfAccount.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(getToken(paramString, arrayOfAccount[j].name));
    }
    return Futures.successfulAsList(localArrayList);
  }
  
  @Nullable
  private String getChecked(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString2)) {}
    for (;;)
    {
      return null;
      int i = 0;
      while (i <= 1) {
        try
        {
          SystemClock.uptimeMillis();
          String str = this.mAuthAdapter.getTokenWithNotification(this.mContext, paramString2, paramString1, null);
          return str;
        }
        catch (UserRecoverableNotifiedException localUserRecoverableNotifiedException)
        {
          Log.w("Search.LoginHelper", "User recoverable exception for scope: " + paramString1);
          EventLogger.recordClientEvent(122, Integer.valueOf(1));
          return null;
        }
        catch (GoogleAuthException localGoogleAuthException)
        {
          Log.w("Search.LoginHelper", "Google auth exception for scope: " + paramString1);
          EventLogger.recordClientEvent(122, Integer.valueOf(2));
          return null;
        }
        catch (IOException localIOException)
        {
          Log.w("Search.LoginHelper", "IO exception for scope: " + paramString1);
          EventLogger.recordClientEvent(122, Integer.valueOf(3));
          i++;
        }
      }
    }
  }
  
  private ListenableFuture<String> getToken(final String paramString1, final String paramString2)
  {
    EventLogger.recordLatencyStart(10);
    ListenableFutureTask localListenableFutureTask = ListenableFutureTask.create(new Callable()
    {
      public String call()
      {
        return LoginHelper.this.getChecked(paramString1, paramString2);
      }
    });
    this.mBackgroundExecutor.execute(localListenableFutureTask);
    return localListenableFutureTask;
  }
  
  /* Error */
  private void handleAccounts(@Nonnull Account[] paramArrayOfAccount)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokestatic 202	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: aload_1
    //   9: putfield 204	com/google/android/search/core/google/gaia/LoginHelper:mAccounts	[Landroid/accounts/Account;
    //   12: aload_1
    //   13: ifnull +8 -> 21
    //   16: aload_1
    //   17: arraylength
    //   18: ifne +15 -> 33
    //   21: aload_0
    //   22: aconst_null
    //   23: invokespecial 208	com/google/android/search/core/google/gaia/LoginHelper:setAccount	(Landroid/accounts/Account;)V
    //   26: aload_0
    //   27: invokespecial 211	com/google/android/search/core/google/gaia/LoginHelper:notifyChanged	()V
    //   30: aload_0
    //   31: monitorexit
    //   32: return
    //   33: aload_0
    //   34: aload_0
    //   35: getfield 44	com/google/android/search/core/google/gaia/LoginHelper:mSettings	Lcom/google/android/search/core/SearchSettings;
    //   38: invokeinterface 216 1 0
    //   43: invokevirtual 220	com/google/android/search/core/google/gaia/LoginHelper:findAccountByName	(Ljava/lang/String;)Landroid/accounts/Account;
    //   46: astore 4
    //   48: aload_0
    //   49: getfield 44	com/google/android/search/core/google/gaia/LoginHelper:mSettings	Lcom/google/android/search/core/SearchSettings;
    //   52: invokeinterface 224 1 0
    //   57: ifne +13 -> 70
    //   60: aload 4
    //   62: ifnonnull +8 -> 70
    //   65: aload_1
    //   66: iconst_0
    //   67: aaload
    //   68: astore 4
    //   70: aload_0
    //   71: aload 4
    //   73: invokespecial 208	com/google/android/search/core/google/gaia/LoginHelper:setAccount	(Landroid/accounts/Account;)V
    //   76: goto -46 -> 30
    //   79: astore_2
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_2
    //   83: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	84	0	this	LoginHelper
    //   0	84	1	paramArrayOfAccount	Account[]
    //   79	4	2	localObject	Object
    //   46	26	4	localAccount	Account
    // Exception table:
    //   from	to	target	type
    //   2	12	79	finally
    //   16	21	79	finally
    //   21	30	79	finally
    //   33	60	79	finally
    //   65	70	79	finally
    //   70	76	79	finally
  }
  
  private void maybePopulateAccounts()
  {
    if (this.mAccounts == null) {
      refresh();
    }
  }
  
  private void notifyChanged()
  {
    this.mDataSetObservable.notifyChanged();
  }
  
  private void refresh()
  {
    try
    {
      handleAccounts(this.mAccountManager.getAccountsByType("com.google"));
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  private void setAccount(@Nullable Account paramAccount)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 236	com/google/android/search/core/google/gaia/LoginHelper:mAccount	Landroid/accounts/Account;
    //   7: aload_0
    //   8: getfield 44	com/google/android/search/core/google/gaia/LoginHelper:mSettings	Lcom/google/android/search/core/SearchSettings;
    //   11: invokeinterface 216 1 0
    //   16: astore_3
    //   17: aload_1
    //   18: ifnull +25 -> 43
    //   21: aload_1
    //   22: getfield 103	android/accounts/Account:name	Ljava/lang/String;
    //   25: astore 4
    //   27: aload_3
    //   28: aload 4
    //   30: invokestatic 242	com/google/common/base/Objects:equal	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   33: istore 5
    //   35: iload 5
    //   37: ifeq +12 -> 49
    //   40: aload_0
    //   41: monitorexit
    //   42: return
    //   43: aconst_null
    //   44: astore 4
    //   46: goto -19 -> 27
    //   49: aload_0
    //   50: getfield 44	com/google/android/search/core/google/gaia/LoginHelper:mSettings	Lcom/google/android/search/core/SearchSettings;
    //   53: aload 4
    //   55: invokeinterface 246 2 0
    //   60: aload_0
    //   61: getfield 48	com/google/android/search/core/google/gaia/LoginHelper:mNowOptInSettingsSupplier	Lcom/google/common/base/Supplier;
    //   64: invokeinterface 252 1 0
    //   69: checkcast 254	com/google/android/search/core/NowOptInSettings
    //   72: aload_1
    //   73: invokeinterface 257 2 0
    //   78: new 259	android/content/Intent
    //   81: dup
    //   82: ldc_w 261
    //   85: invokespecial 263	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   88: astore 6
    //   90: aload 6
    //   92: ldc_w 265
    //   95: aload 4
    //   97: invokevirtual 269	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   100: pop
    //   101: aload_0
    //   102: getfield 42	com/google/android/search/core/google/gaia/LoginHelper:mContext	Landroid/content/Context;
    //   105: aload 6
    //   107: ldc_w 271
    //   110: invokevirtual 277	android/content/Context:sendBroadcast	(Landroid/content/Intent;Ljava/lang/String;)V
    //   113: goto -73 -> 40
    //   116: astore_2
    //   117: aload_0
    //   118: monitorexit
    //   119: aload_2
    //   120: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	121	0	this	LoginHelper
    //   0	121	1	paramAccount	Account
    //   116	4	2	localObject	Object
    //   16	12	3	str1	String
    //   25	71	4	str2	String
    //   33	3	5	bool	boolean
    //   88	18	6	localIntent	android.content.Intent
    // Exception table:
    //   from	to	target	type
    //   2	17	116	finally
    //   21	27	116	finally
    //   27	35	116	finally
    //   49	113	116	finally
  }
  
  private void setAccountToUse(@Nonnull Account paramAccount)
  {
    try
    {
      Preconditions.checkNotNull(paramAccount);
      Account localAccount = getAccount();
      if ((localAccount != null) && (!localAccount.equals(paramAccount))) {
        ((NowOptInSettings)this.mNowOptInSettingsSupplier.get()).disableForAccount(localAccount);
      }
      this.mSettings.setSignedOut(false);
      setAccount(paramAccount);
      return;
    }
    finally {}
  }
  
  @Nullable
  private <T> T waitForTokenFuture(Future<T> paramFuture, long paramLong, boolean paramBoolean)
  {
    try
    {
      Object localObject = paramFuture.get(paramLong, TimeUnit.MILLISECONDS);
      return localObject;
    }
    catch (InterruptedException localInterruptedException)
    {
      Log.w("Search.LoginHelper", "InterruptedException while waiting for token.");
      EventLogger.recordClientEvent(122, Integer.valueOf(5));
      if (paramBoolean) {
        paramFuture.cancel(true);
      }
      return null;
    }
    catch (ExecutionException localExecutionException)
    {
      for (;;)
      {
        VelvetStrictMode.logW("Search.LoginHelper", "ExecutionException while waiting for token.");
        BugLogger.record(10070063);
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      for (;;)
      {
        EventLogger.recordClientEvent(122, Integer.valueOf(4));
        Log.w("Search.LoginHelper", "TimeoutException while waiting for token.");
        if (paramBoolean) {
          paramFuture.cancel(true);
        }
      }
    }
  }
  
  public Collection<String> blockingGetAllTokens(String paramString, long paramLong)
  {
    ExtraPreconditions.checkNotMainThread();
    Object localObject = (List)waitForTokenFuture(getAllTokens(paramString), paramLong, false);
    if (localObject != null)
    {
      Iterator localIterator = ((List)localObject).iterator();
      while (localIterator.hasNext()) {
        if (TextUtils.isEmpty((CharSequence)localIterator.next())) {
          localIterator.remove();
        }
      }
    }
    localObject = ImmutableList.of();
    return localObject;
  }
  
  @Deprecated
  @Nullable
  public AuthToken blockingGetAuthTokenForAccount(@Nullable Account paramAccount, String paramString, long paramLong)
  {
    String str = blockingGetTokenForAccount(paramAccount, paramString, paramLong);
    if ((str != null) && (paramAccount != null)) {
      return new AuthToken(paramAccount.name, str);
    }
    return null;
  }
  
  public Uri blockingGetGaiaWebLoginLink(Uri paramUri, String paramString)
  {
    ExtraPreconditions.checkNotMainThread();
    Account localAccount = getAccount();
    if (localAccount == null)
    {
      Log.w("Search.LoginHelper", "blockingGetGaiaWebLoginLink: account null, returning.");
      return null;
    }
    String str1 = URLEncoder.encode(paramUri.toString());
    if (paramString == null) {}
    for (String str2 = "weblogin:" + URLEncoder.encode(new StringBuilder().append("continue=").append(str1).toString()) + "&de=1";; str2 = "weblogin:" + URLEncoder.encode(new StringBuilder().append("service=").append(paramString).append("&continue=").append(str1).toString()) + "&de=1")
    {
      String str3 = blockingGetTokenForAccount(localAccount, str2, 5000L);
      if (str3 == null) {
        break;
      }
      return Uri.parse(str3);
    }
  }
  
  @Nullable
  public String blockingGetToken(String paramString, long paramLong)
  {
    return blockingGetTokenForAccount(getAccount(), paramString, paramLong);
  }
  
  @Nullable
  public String blockingGetTokenForAccount(@Nullable Account paramAccount, String paramString, long paramLong)
  {
    
    if (paramAccount == null) {
      return null;
    }
    return (String)waitForTokenFuture(getToken(paramString, paramAccount.name), paramLong, true);
  }
  
  public boolean canUserOptIntoGoogleNow()
  {
    Account localAccount = getAccount();
    if (localAccount != null) {
      return ((NowOptInSettings)this.mNowOptInSettingsSupplier.get()).canAccountRunNow(localAccount) == 1;
    }
    return false;
  }
  
  public void credentialsUpdated()
  {
    this.mAuthAdapter.phoneCredentialsUpdated();
  }
  
  /* Error */
  public Account findAccountByName(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 422	com/google/android/search/core/google/gaia/LoginHelper:maybePopulateAccounts	()V
    //   6: aload_1
    //   7: ifnonnull +11 -> 18
    //   10: aconst_null
    //   11: astore 6
    //   13: aload_0
    //   14: monitorexit
    //   15: aload 6
    //   17: areturn
    //   18: aload_0
    //   19: getfield 204	com/google/android/search/core/google/gaia/LoginHelper:mAccounts	[Landroid/accounts/Account;
    //   22: astore_3
    //   23: aload_3
    //   24: arraylength
    //   25: istore 4
    //   27: iconst_0
    //   28: istore 5
    //   30: iload 5
    //   32: iload 4
    //   34: if_icmpge +31 -> 65
    //   37: aload_3
    //   38: iload 5
    //   40: aaload
    //   41: astore 6
    //   43: aload 6
    //   45: getfield 103	android/accounts/Account:name	Ljava/lang/String;
    //   48: aload_1
    //   49: invokestatic 425	android/text/TextUtils:equals	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Z
    //   52: istore 7
    //   54: iload 7
    //   56: ifne -43 -> 13
    //   59: iinc 5 1
    //   62: goto -32 -> 30
    //   65: aconst_null
    //   66: astore 6
    //   68: goto -55 -> 13
    //   71: astore_2
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_2
    //   75: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	this	LoginHelper
    //   0	76	1	paramString	String
    //   71	4	2	localObject	Object
    //   22	16	3	arrayOfAccount	Account[]
    //   25	10	4	i	int
    //   28	32	5	j	int
    //   11	56	6	localAccount	Account
    //   52	3	7	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	6	71	finally
    //   18	27	71	finally
    //   37	54	71	finally
  }
  
  @Nullable
  public Account getAccount()
  {
    try
    {
      maybePopulateAccounts();
      Account localAccount = this.mAccount;
      return localAccount;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Nullable
  public String getAccountName()
  {
    Account localAccount = getAccount();
    if (localAccount != null) {
      return localAccount.name;
    }
    return null;
  }
  
  public String[] getAllAccountNames()
  {
    try
    {
      maybePopulateAccounts();
      String[] arrayOfString = new String[this.mAccounts.length];
      for (int i = 0; i < this.mAccounts.length; i++) {
        arrayOfString[i] = this.mAccounts[i].name;
      }
      return arrayOfString;
    }
    finally {}
  }
  
  public Account[] getAllAccounts()
  {
    try
    {
      maybePopulateAccounts();
      Account[] arrayOfAccount = this.mAccounts;
      return arrayOfAccount;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void getGaiaWebLoginLink(final Uri paramUri, final String paramString, final Consumer<Uri> paramConsumer)
  {
    final Account localAccount = getAccount();
    this.mBackgroundExecutor.execute(new Runnable()
    {
      public void run()
      {
        Uri localUri = LoginHelper.this.blockingGetGaiaWebLoginLink(paramUri, paramString);
        Consumer localConsumer = paramConsumer;
        if (Objects.equal(localAccount, LoginHelper.this.getAccount())) {}
        for (;;)
        {
          localConsumer.consume(localUri);
          return;
          localUri = null;
        }
      }
    });
  }
  
  public void getMainGmailAccount(final SimpleCallback<String> paramSimpleCallback)
  {
    AccountManager localAccountManager = this.mAccountManager;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = GoogleLoginServiceConstants.featureForService("mail");
    localAccountManager.getAccountsByTypeAndFeatures("com.google", arrayOfString, new AccountManagerCallback()
    {
      public void run(AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture)
      {
        try
        {
          arrayOfAccount = (Account[])paramAnonymousAccountManagerFuture.getResult();
          if ((arrayOfAccount == null) || (arrayOfAccount.length <= 0)) {
            break label103;
          }
          str = LoginHelper.this.getAccountName();
          i = arrayOfAccount.length;
          j = 0;
        }
        catch (OperationCanceledException localOperationCanceledException)
        {
          Account[] arrayOfAccount;
          String str;
          int i;
          Log.e("Search.LoginHelper", "Retreiving Google accounts failed", localOperationCanceledException);
          paramSimpleCallback.onResult(null);
          return;
        }
        catch (AuthenticatorException localAuthenticatorException)
        {
          for (;;)
          {
            Log.e("Search.LoginHelper", "Retreiving Google accounts failed", localAuthenticatorException);
          }
        }
        catch (IOException localIOException)
        {
          for (;;)
          {
            int j;
            label103:
            Log.e("Search.LoginHelper", "Retreiving Google accounts failed", localIOException);
            continue;
            j++;
          }
        }
        if (j < i)
        {
          if (arrayOfAccount[j].name.equalsIgnoreCase(str)) {
            paramSimpleCallback.onResult(str);
          }
        }
        else
        {
          paramSimpleCallback.onResult(arrayOfAccount[0].name);
          return;
        }
      }
    }, null);
  }
  
  /* Error */
  public boolean hasAccount()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual 91	com/google/android/search/core/google/gaia/LoginHelper:getAllAccounts	()[Landroid/accounts/Account;
    //   6: arraylength
    //   7: istore_2
    //   8: iload_2
    //   9: ifle +9 -> 18
    //   12: iconst_1
    //   13: istore_3
    //   14: aload_0
    //   15: monitorexit
    //   16: iload_3
    //   17: ireturn
    //   18: iconst_0
    //   19: istore_3
    //   20: goto -6 -> 14
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	28	0	this	LoginHelper
    //   23	4	1	localObject	Object
    //   7	2	2	i	int
    //   13	7	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   2	8	23	finally
  }
  
  public void invalidateToken(String paramString)
  {
    this.mAuthAdapter.invalidateToken(this.mContext, paramString);
  }
  
  public void invalidateTokens(Collection<String> paramCollection)
  {
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext()) {
      invalidateToken((String)localIterator.next());
    }
  }
  
  public boolean isUserOptedIntoGoogleNow()
  {
    if ((getAccount() != null) && (canUserOptIntoGoogleNow())) {
      return ((NowOptInSettings)this.mNowOptInSettingsSupplier.get()).isAccountOptedIn(getAccount());
    }
    return false;
  }
  
  public void logOut()
  {
    try
    {
      maybePopulateAccounts();
      this.mSettings.setSignedOut(true);
      setAccount(null);
      notifyChanged();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.registerObserver(paramDataSetObserver);
  }
  
  public void setAccountToUseByName(String paramString)
    throws AccountsException
  {
    try
    {
      Account localAccount = findAccountByName(paramString);
      if (localAccount != null)
      {
        setAccountToUse(localAccount);
        return;
      }
      throw new AccountsException("setAccountToUseByName: Account name not found.");
    }
    finally {}
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    this.mDataSetObservable.unregisterObserver(paramDataSetObserver);
  }
  
  public static class AuthToken
  {
    private final String mAccount;
    private final String mToken;
    
    public AuthToken(String paramString1, String paramString2)
    {
      this.mAccount = paramString1;
      this.mToken = paramString2;
    }
    
    public String getAccount()
    {
      return this.mAccount;
    }
    
    public String getToken()
    {
      return this.mToken;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.gaia.LoginHelper
 * JD-Core Version:    0.7.0.1
 */