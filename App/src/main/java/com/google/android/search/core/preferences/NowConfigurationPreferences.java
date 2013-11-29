package com.google.android.search.core.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.google.android.sidekick.main.sync.MessageMicroUtil;
import com.google.android.sidekick.main.sync.RepeatedMessageInfo;
import com.google.android.sidekick.shared.cards.SharedTrafficEntryAdapter;
import com.google.android.sidekick.shared.util.Tag;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

public class NowConfigurationPreferences
  implements SharedPreferences
{
  static final String DIRTY_BIT_KEY = "NowConfigurationPreferences.dirty";
  public static final String SIDEKICK_PREFIX = "sidekick.";
  private static final String TAG = Tag.getTag(NowConfigurationPreferences.class);
  private static Sidekick.SidekickConfiguration backingConfigurationCache;
  private static SharedPreferences backingConfigurationCacheKey;
  private static final Object sBackingConfigurationCacheLock = new Object();
  private final String mBackingConfigurationKey;
  private final SharedPreferences mBackingPreferences;
  private final Sidekick.SidekickConfiguration mDetachedConfiguration = new Sidekick.SidekickConfiguration();
  private final Set<SharedPreferences.OnSharedPreferenceChangeListener> mListeners;
  private final Object mLock = new Object();
  private final RepeatedMessageInfo mPrimaryKeyFactory;
  
  NowConfigurationPreferences(RepeatedMessageInfo paramRepeatedMessageInfo, SharedPreferences paramSharedPreferences, String paramString)
  {
    Preconditions.checkNotNull(paramSharedPreferences);
    this.mListeners = new HashSet();
    this.mPrimaryKeyFactory = paramRepeatedMessageInfo;
    this.mBackingPreferences = paramSharedPreferences;
    this.mBackingConfigurationKey = paramString;
  }
  
  @Nullable
  private static String backingConfigurationPath(String paramString, boolean paramBoolean)
  {
    if (paramString.startsWith("sidekick.")) {}
    for (String str = paramString.substring("sidekick.".length());; str = null)
    {
      if (paramBoolean) {
        Preconditions.checkNotNull(str, paramString + " does not begin with " + "sidekick.");
      }
      return str;
    }
  }
  
  private <T> T doGetLocked(String paramString, T paramT)
  {
    FieldWithTarget localFieldWithTarget;
    synchronized (this.mLock)
    {
      Sidekick.SidekickConfiguration localSidekickConfiguration = getBackingConfiguration();
      if (localSidekickConfiguration == null) {
        localSidekickConfiguration = this.mDetachedConfiguration;
      }
      localFieldWithTarget = traverseTo(localSidekickConfiguration, paramString);
      if (localFieldWithTarget == null) {
        throw new BadPathException(paramString, null);
      }
    }
    boolean bool;
    if (MessageMicroUtil.isRepeatedField(localFieldWithTarget.mField))
    {
      if (MessageMicroUtil.getRepeatedFieldCount(localFieldWithTarget.mTarget, localFieldWithTarget.mField) <= 0) {
        break label115;
      }
      bool = true;
    }
    for (;;)
    {
      if (bool)
      {
        Object localObject3 = localFieldWithTarget.get();
        return localObject3;
        bool = localFieldWithTarget.has();
      }
      else
      {
        return paramT;
        label115:
        bool = false;
      }
    }
  }
  
  private void notifyListeners(final Collection<SharedPreferences.OnSharedPreferenceChangeListener> paramCollection, final Set<String> paramSet)
  {
    boolean bool1 = true;
    boolean bool2;
    if ((paramCollection != null) && (!paramCollection.isEmpty()))
    {
      bool2 = bool1;
      Preconditions.checkArgument(bool2);
      if ((paramSet == null) || (paramSet.isEmpty())) {
        break label149;
      }
    }
    Looper localLooper;
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      localLooper = Looper.getMainLooper();
      if (localLooper.getThread() != Thread.currentThread()) {
        break label154;
      }
      Iterator localIterator1 = paramSet.iterator();
      while (localIterator1.hasNext())
      {
        String str = (String)localIterator1.next();
        Iterator localIterator2 = paramCollection.iterator();
        while (localIterator2.hasNext()) {
          ((SharedPreferences.OnSharedPreferenceChangeListener)localIterator2.next()).onSharedPreferenceChanged(this, "sidekick." + str);
        }
      }
      bool2 = false;
      break;
      label149:
      bool1 = false;
    }
    label154:
    new Handler(localLooper).post(new Runnable()
    {
      public void run()
      {
        NowConfigurationPreferences.this.notifyListeners(paramCollection, paramSet);
      }
    });
  }
  
  @Nullable
  private static FieldWithTarget traverseTo(MessageMicro paramMessageMicro, String paramString)
  {
    MessageMicro localMessageMicro = paramMessageMicro;
    int i = 0;
    try
    {
      for (int j = paramString.indexOf('.', 0); j != -1; j = paramString.indexOf('.', i))
      {
        Field localField1 = MessageMicroUtil.getProtoField(localMessageMicro, paramString.substring(i, j));
        if (!MessageMicro.class.isAssignableFrom(localField1.getType())) {
          return null;
        }
        localMessageMicro = (MessageMicro)MessageMicroUtil.getFieldBuilder(localMessageMicro, localField1);
        i = j + 1;
      }
      Field localField2 = MessageMicroUtil.getProtoField(localMessageMicro, paramString.substring(i));
      if (MessageMicro.class.isAssignableFrom(localField2.getType())) {
        return null;
      }
      FieldWithTarget localFieldWithTarget = new FieldWithTarget(localMessageMicro, localField2);
      return localFieldWithTarget;
    }
    catch (NoSuchFieldException localNoSuchFieldException) {}
    return null;
  }
  
  public void clearBackingConfiguration()
  {
    synchronized (this.mLock)
    {
      this.mBackingPreferences.edit().remove(this.mBackingConfigurationKey).apply();
      synchronized (sBackingConfigurationCacheLock)
      {
        backingConfigurationCache = null;
        return;
      }
    }
  }
  
  public boolean contains(String paramString)
  {
    String str = backingConfigurationPath(paramString, false);
    boolean bool;
    if (str == null) {
      bool = this.mBackingPreferences.contains(paramString);
    }
    Object localObject;
    do
    {
      return bool;
      localObject = doGetLocked(str, null);
      bool = false;
    } while (localObject == null);
    return true;
  }
  
  public SharedPreferences.Editor edit()
  {
    return new ConfigurationEditor(this.mBackingPreferences.edit(), null);
  }
  
  public ConfigurationEditor editConfiguration()
  {
    return new ConfigurationEditor(this.mBackingPreferences.edit(), null);
  }
  
  public Map<String, ?> getAll()
  {
    throw new UnsupportedOperationException();
  }
  
  /* Error */
  @Nullable
  public Sidekick.SidekickConfiguration getBackingConfiguration()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 47	com/google/android/search/core/preferences/NowConfigurationPreferences:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: getstatic 44	com/google/android/search/core/preferences/NowConfigurationPreferences:sBackingConfigurationCacheLock	Ljava/lang/Object;
    //   10: astore_3
    //   11: aload_3
    //   12: monitorenter
    //   13: getstatic 274	com/google/android/search/core/preferences/NowConfigurationPreferences:backingConfigurationCache	Lcom/google/geo/sidekick/Sidekick$SidekickConfiguration;
    //   16: ifnull +25 -> 41
    //   19: getstatic 295	com/google/android/search/core/preferences/NowConfigurationPreferences:backingConfigurationCacheKey	Landroid/content/SharedPreferences;
    //   22: aload_0
    //   23: getfield 67	com/google/android/search/core/preferences/NowConfigurationPreferences:mBackingPreferences	Landroid/content/SharedPreferences;
    //   26: if_acmpne +15 -> 41
    //   29: getstatic 274	com/google/android/search/core/preferences/NowConfigurationPreferences:backingConfigurationCache	Lcom/google/geo/sidekick/Sidekick$SidekickConfiguration;
    //   32: astore 12
    //   34: aload_3
    //   35: monitorexit
    //   36: aload_1
    //   37: monitorexit
    //   38: aload 12
    //   40: areturn
    //   41: aload_3
    //   42: monitorexit
    //   43: aload_0
    //   44: getfield 67	com/google/android/search/core/preferences/NowConfigurationPreferences:mBackingPreferences	Landroid/content/SharedPreferences;
    //   47: aload_0
    //   48: getfield 69	com/google/android/search/core/preferences/NowConfigurationPreferences:mBackingConfigurationKey	Ljava/lang/String;
    //   51: aconst_null
    //   52: invokeinterface 299 3 0
    //   57: astore 5
    //   59: aload 5
    //   61: ifnonnull +19 -> 80
    //   64: aload_1
    //   65: monitorexit
    //   66: aconst_null
    //   67: areturn
    //   68: astore_2
    //   69: aload_1
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: astore 4
    //   75: aload_3
    //   76: monitorexit
    //   77: aload 4
    //   79: athrow
    //   80: getstatic 44	com/google/android/search/core/preferences/NowConfigurationPreferences:sBackingConfigurationCacheLock	Ljava/lang/Object;
    //   83: astore 6
    //   85: aload 6
    //   87: monitorenter
    //   88: new 49	com/google/geo/sidekick/Sidekick$SidekickConfiguration
    //   91: dup
    //   92: invokespecial 50	com/google/geo/sidekick/Sidekick$SidekickConfiguration:<init>	()V
    //   95: astore 7
    //   97: aload 7
    //   99: aload 5
    //   101: invokestatic 303	com/google/android/sidekick/main/sync/MessageMicroUtil:decodeFromString	(Lcom/google/protobuf/micro/MessageMicro;Ljava/lang/String;)Lcom/google/protobuf/micro/MessageMicro;
    //   104: pop
    //   105: aload_0
    //   106: getfield 67	com/google/android/search/core/preferences/NowConfigurationPreferences:mBackingPreferences	Landroid/content/SharedPreferences;
    //   109: putstatic 295	com/google/android/search/core/preferences/NowConfigurationPreferences:backingConfigurationCacheKey	Landroid/content/SharedPreferences;
    //   112: aload 7
    //   114: putstatic 274	com/google/android/search/core/preferences/NowConfigurationPreferences:backingConfigurationCache	Lcom/google/geo/sidekick/Sidekick$SidekickConfiguration;
    //   117: aload 6
    //   119: monitorexit
    //   120: aload_1
    //   121: monitorexit
    //   122: aload 7
    //   124: areturn
    //   125: astore 9
    //   127: aload 9
    //   129: invokestatic 309	com/google/common/base/Throwables:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   132: pop
    //   133: goto -28 -> 105
    //   136: astore 8
    //   138: aload 6
    //   140: monitorexit
    //   141: aload 8
    //   143: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	144	0	this	NowConfigurationPreferences
    //   4	117	1	localObject1	Object
    //   68	4	2	localObject2	Object
    //   73	5	4	localObject4	Object
    //   57	43	5	str	String
    //   95	28	7	localSidekickConfiguration1	Sidekick.SidekickConfiguration
    //   136	6	8	localObject6	Object
    //   125	3	9	localInvalidProtocolBufferMicroException	InvalidProtocolBufferMicroException
    //   32	7	12	localSidekickConfiguration2	Sidekick.SidekickConfiguration
    // Exception table:
    //   from	to	target	type
    //   7	13	68	finally
    //   36	38	68	finally
    //   43	59	68	finally
    //   64	66	68	finally
    //   69	71	68	finally
    //   77	80	68	finally
    //   80	88	68	finally
    //   120	122	68	finally
    //   141	144	68	finally
    //   13	36	73	finally
    //   41	43	73	finally
    //   75	77	73	finally
    //   97	105	125	com/google/protobuf/micro/InvalidProtocolBufferMicroException
    //   88	97	136	finally
    //   97	105	136	finally
    //   105	120	136	finally
    //   127	133	136	finally
    //   138	141	136	finally
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    String str = backingConfigurationPath(paramString, false);
    if (str == null) {
      return this.mBackingPreferences.getBoolean(paramString, paramBoolean);
    }
    return ((Boolean)doGetLocked(str, Boolean.valueOf(paramBoolean))).booleanValue();
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    String str = backingConfigurationPath(paramString, false);
    if (str == null) {
      return this.mBackingPreferences.getFloat(paramString, paramFloat);
    }
    return ((Float)doGetLocked(str, Float.valueOf(paramFloat))).floatValue();
  }
  
  public int getInt(String paramString, int paramInt)
  {
    String str = backingConfigurationPath(paramString, false);
    if (str == null) {
      return this.mBackingPreferences.getInt(paramString, paramInt);
    }
    return ((Integer)doGetLocked(str, Integer.valueOf(paramInt))).intValue();
  }
  
  public long getLong(String paramString, long paramLong)
  {
    String str = backingConfigurationPath(paramString, false);
    if (str == null) {
      return this.mBackingPreferences.getLong(paramString, paramLong);
    }
    return ((Long)doGetLocked(str, Long.valueOf(paramLong))).longValue();
  }
  
  @Nullable
  public <T extends MessageMicro> T getMessage(String paramString1, String paramString2)
  {
    Iterator localIterator = ((List)doGetLocked(backingConfigurationPath(paramString1, true), Collections.emptyList())).iterator();
    while (localIterator.hasNext())
    {
      MessageMicro localMessageMicro = (MessageMicro)localIterator.next();
      if (paramString2.equals(this.mPrimaryKeyFactory.primaryKeyFor(localMessageMicro))) {
        return localMessageMicro;
      }
    }
    return null;
  }
  
  public <T extends MessageMicro> List<T> getMessages(String paramString)
  {
    return (List)doGetLocked(backingConfigurationPath(paramString, true), Collections.emptyList());
  }
  
  public String getString(String paramString1, String paramString2)
  {
    String str = backingConfigurationPath(paramString1, false);
    if (str == null) {
      return this.mBackingPreferences.getString(paramString1, paramString2);
    }
    Object localObject = doGetLocked(str, paramString2);
    if ((localObject instanceof Integer)) {
      return Integer.toString(((Integer)localObject).intValue());
    }
    return (String)localObject;
  }
  
  public Set<String> getStringSet(String paramString, Set<String> paramSet)
  {
    String str = backingConfigurationPath(paramString, false);
    Object localObject;
    if (str == null) {
      localObject = this.mBackingPreferences.getStringSet(paramString, paramSet);
    }
    for (;;)
    {
      return localObject;
      List localList = (List)doGetLocked(str, Collections.emptyList());
      localObject = Sets.newHashSet();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        MessageMicro localMessageMicro = (MessageMicro)localIterator.next();
        ((Set)localObject).add(this.mPrimaryKeyFactory.primaryKeyFor(localMessageMicro));
      }
    }
  }
  
  public boolean hasBackingConfiguration()
  {
    for (;;)
    {
      synchronized (sBackingConfigurationCacheLock)
      {
        if (backingConfigurationCache != null)
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
    }
  }
  
  public boolean isDirty()
  {
    return this.mBackingPreferences.getBoolean("NowConfigurationPreferences.dirty", true);
  }
  
  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.mBackingPreferences.registerOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
    synchronized (this.mLock)
    {
      this.mListeners.add(paramOnSharedPreferenceChangeListener);
      return;
    }
  }
  
  public void setBackingConfiguration(Sidekick.SidekickConfiguration paramSidekickConfiguration)
  {
    synchronized (this.mLock)
    {
      Preconditions.checkNotNull(paramSidekickConfiguration);
    }
    try
    {
      paramSidekickConfiguration.mergeFrom(this.mDetachedConfiguration.toByteArray());
      label24:
      String str = MessageMicroUtil.encodeAsString(paramSidekickConfiguration);
      this.mBackingPreferences.edit().putString(this.mBackingConfigurationKey, str).putBoolean("NowConfigurationPreferences.dirty", false).apply();
      synchronized (sBackingConfigurationCacheLock)
      {
        backingConfigurationCache = null;
        return;
      }
      localObject2 = finally;
      throw localObject2;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      break label24;
    }
  }
  
  public void setTrafficSharerHiddenState(Context paramContext, long paramLong, boolean paramBoolean)
  {
    String str1 = paramContext.getString(2131362091);
    String str2 = Long.toString(paramLong);
    String str3 = SharedTrafficEntryAdapter.confirmationPresentationCountKey(str2);
    Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact localLocationSharingContact = (Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact)getMessage(str1, str2);
    if (localLocationSharingContact != null)
    {
      localLocationSharingContact.setHide(paramBoolean);
      editConfiguration().updateMessage(str1, localLocationSharingContact).remove(str3).apply();
      return;
    }
    Log.w(TAG, "Location sharer to update was not found in settings: " + str2);
  }
  
  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    this.mBackingPreferences.unregisterOnSharedPreferenceChangeListener(paramOnSharedPreferenceChangeListener);
    synchronized (this.mLock)
    {
      this.mListeners.remove(paramOnSharedPreferenceChangeListener);
      return;
    }
  }
  
  public static class BadPathException
    extends RuntimeException
  {
    private BadPathException(String paramString)
    {
      super();
    }
  }
  
  public class ConfigurationEditor
    implements SharedPreferences.Editor
  {
    boolean hasDirtyEdits;
    final Map<String, List<MessageMicro>> mAdds = Maps.newHashMap();
    final SharedPreferences.Editor mBackingEditor;
    final Map<String, Set<String>> mDeletes = Maps.newHashMap();
    private final Object mEditorLock = new Object();
    final Map<String, Object> mModified = Maps.newHashMap();
    
    private ConfigurationEditor(SharedPreferences.Editor paramEditor)
    {
      this.mBackingEditor = paramEditor;
    }
    
    private void doCommit()
    {
      Sidekick.SidekickConfiguration localSidekickConfiguration;
      int i;
      HashSet localHashSet;
      Map.Entry localEntry3;
      String str4;
      NowConfigurationPreferences.FieldWithTarget localFieldWithTarget3;
      for (;;)
      {
        synchronized (NowConfigurationPreferences.this.mLock)
        {
          localSidekickConfiguration = NowConfigurationPreferences.this.getBackingConfiguration();
          synchronized (this.mEditorLock)
          {
            if ((!this.mModified.isEmpty()) || (!this.mAdds.isEmpty())) {
              break label812;
            }
            if (!this.mDeletes.isEmpty())
            {
              break label812;
              if ((i != 0) && (localSidekickConfiguration == null)) {
                localSidekickConfiguration = NowConfigurationPreferences.this.mDetachedConfiguration;
              }
              localHashSet = Sets.newHashSet();
              Iterator localIterator1 = this.mModified.entrySet().iterator();
              if (!localIterator1.hasNext()) {
                break label290;
              }
              localEntry3 = (Map.Entry)localIterator1.next();
              str4 = (String)localEntry3.getKey();
              localFieldWithTarget3 = NowConfigurationPreferences.traverseTo(localSidekickConfiguration, str4);
              if (localFieldWithTarget3 != null) {
                break;
              }
              NowConfigurationPreferences.BadPathException localBadPathException3 = new NowConfigurationPreferences.BadPathException(str4, null);
              throw localBadPathException3;
            }
          }
        }
        i = 0;
      }
      if (localFieldWithTarget3.has()) {}
      for (Object localObject5 = localFieldWithTarget3.get();; localObject5 = null)
      {
        if ((localFieldWithTarget3.mField.getType().equals(Integer.TYPE)) && ((localEntry3.getValue() instanceof String))) {}
        for (Object localObject6 = Integer.valueOf(Integer.parseInt((String)localEntry3.getValue())); !Objects.equal(localObject5, localObject6); localObject6 = localEntry3.getValue())
        {
          localFieldWithTarget3.set(localObject6);
          localHashSet.add(str4);
          break;
        }
        label290:
        Iterator localIterator2 = this.mDeletes.entrySet().iterator();
        while (localIterator2.hasNext())
        {
          Map.Entry localEntry2 = (Map.Entry)localIterator2.next();
          String str3 = (String)localEntry2.getKey();
          NowConfigurationPreferences.FieldWithTarget localFieldWithTarget2 = NowConfigurationPreferences.traverseTo(localSidekickConfiguration, str3);
          if (localFieldWithTarget2 == null)
          {
            NowConfigurationPreferences.BadPathException localBadPathException2 = new NowConfigurationPreferences.BadPathException(str3, null);
            throw localBadPathException2;
          }
          List localList = MessageMicroUtil.getRepeatedFieldAsList(localFieldWithTarget2.mTarget, localFieldWithTarget2.mField);
          HashMap localHashMap = Maps.newHashMap();
          Iterator localIterator5 = localList.iterator();
          while (localIterator5.hasNext())
          {
            MessageMicro localMessageMicro3 = (MessageMicro)localIterator5.next();
            localHashMap.put(NowConfigurationPreferences.this.mPrimaryKeyFactory.primaryKeyFor(localMessageMicro3), localMessageMicro3);
          }
          Iterator localIterator6 = ((Set)localEntry2.getValue()).iterator();
          while (localIterator6.hasNext())
          {
            MessageMicro localMessageMicro2 = (MessageMicro)localHashMap.get((String)localIterator6.next());
            if (localMessageMicro2 != null)
            {
              localHashSet.add(str3);
              localList.remove(localMessageMicro2);
            }
          }
        }
        Iterator localIterator3 = this.mAdds.entrySet().iterator();
        while (localIterator3.hasNext())
        {
          Map.Entry localEntry1 = (Map.Entry)localIterator3.next();
          String str2 = (String)localEntry1.getKey();
          localHashSet.add(str2);
          NowConfigurationPreferences.FieldWithTarget localFieldWithTarget1 = NowConfigurationPreferences.traverseTo(localSidekickConfiguration, str2);
          if (localFieldWithTarget1 == null)
          {
            NowConfigurationPreferences.BadPathException localBadPathException1 = new NowConfigurationPreferences.BadPathException(str2, null);
            throw localBadPathException1;
          }
          Iterator localIterator4 = ((List)localEntry1.getValue()).iterator();
          while (localIterator4.hasNext())
          {
            MessageMicro localMessageMicro1 = (MessageMicro)localIterator4.next();
            MessageMicroUtil.addRepeatedField(localFieldWithTarget1.mTarget, localFieldWithTarget1.mField, localMessageMicro1);
          }
        }
        this.mModified.clear();
        if (!localHashSet.isEmpty()) {}
        for (int j = 1;; j = 0)
        {
          ArrayList localArrayList = null;
          if (j != 0)
          {
            boolean bool = NowConfigurationPreferences.this.mListeners.isEmpty();
            localArrayList = null;
            if (!bool) {
              localArrayList = new ArrayList(NowConfigurationPreferences.this.mListeners);
            }
          }
          if (this.hasDirtyEdits) {
            this.mBackingEditor.putBoolean("NowConfigurationPreferences.dirty", true);
          }
          if (j != 0)
          {
            String str1 = MessageMicroUtil.encodeAsString(localSidekickConfiguration);
            this.mBackingEditor.putString(NowConfigurationPreferences.this.mBackingConfigurationKey, str1);
          }
          if (localArrayList != null) {
            NowConfigurationPreferences.this.notifyListeners(localArrayList, localHashSet);
          }
          return;
        }
        label812:
        i = 1;
        break;
      }
    }
    
    public ConfigurationEditor addMessage(String paramString, MessageMicro paramMessageMicro)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, true);
      synchronized (this.mEditorLock)
      {
        Object localObject3 = (List)this.mAdds.get(str);
        if (localObject3 == null)
        {
          localObject3 = Lists.newArrayList();
          this.mAdds.put(str, localObject3);
        }
        ((List)localObject3).add(paramMessageMicro);
        this.hasDirtyEdits = true;
        return this;
      }
    }
    
    public void apply()
    {
      doCommit();
      this.mBackingEditor.apply();
    }
    
    public SharedPreferences.Editor clear()
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean commit()
    {
      doCommit();
      return this.mBackingEditor.commit();
    }
    
    public SharedPreferences.Editor putBoolean(String paramString, boolean paramBoolean)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, false);
      if (str == null)
      {
        this.mBackingEditor.putBoolean(paramString, paramBoolean);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, Boolean.valueOf(paramBoolean));
        return this;
      }
    }
    
    public SharedPreferences.Editor putFloat(String paramString, float paramFloat)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, false);
      if (str == null)
      {
        this.mBackingEditor.putFloat(paramString, paramFloat);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, Float.valueOf(paramFloat));
        return this;
      }
    }
    
    public SharedPreferences.Editor putInt(String paramString, int paramInt)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, false);
      if (str == null)
      {
        this.mBackingEditor.putInt(paramString, paramInt);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, Integer.valueOf(paramInt));
        return this;
      }
    }
    
    public SharedPreferences.Editor putLong(String paramString, long paramLong)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, false);
      if (str == null)
      {
        this.mBackingEditor.putLong(paramString, paramLong);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, Long.valueOf(paramLong));
        return this;
      }
    }
    
    public SharedPreferences.Editor putString(String paramString1, String paramString2)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString1, false);
      if (str == null)
      {
        this.mBackingEditor.putString(paramString1, paramString2);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, paramString2);
        return this;
      }
    }
    
    public SharedPreferences.Editor putStringSet(String paramString, Set<String> paramSet)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString, false);
      if (str == null)
      {
        this.mBackingEditor.putStringSet(paramString, paramSet);
        return this;
      }
      synchronized (this.mEditorLock)
      {
        this.hasDirtyEdits = true;
        this.mModified.put(str, paramSet);
        return this;
      }
    }
    
    public SharedPreferences.Editor remove(String paramString)
    {
      if (NowConfigurationPreferences.backingConfigurationPath(paramString, false) == null)
      {
        this.mBackingEditor.remove(paramString);
        return this;
      }
      throw new UnsupportedOperationException();
    }
    
    public ConfigurationEditor removeMessage(String paramString, MessageMicro paramMessageMicro)
    {
      return removeMessage(paramString, NowConfigurationPreferences.this.mPrimaryKeyFactory.primaryKeyFor(paramMessageMicro));
    }
    
    public ConfigurationEditor removeMessage(String paramString1, String paramString2)
    {
      String str = NowConfigurationPreferences.backingConfigurationPath(paramString1, true);
      synchronized (this.mEditorLock)
      {
        Object localObject3 = (Set)this.mDeletes.get(str);
        if (localObject3 == null)
        {
          localObject3 = Sets.newHashSet();
          this.mDeletes.put(str, localObject3);
        }
        ((Set)localObject3).add(paramString2);
        this.hasDirtyEdits = true;
        return this;
      }
    }
    
    public ConfigurationEditor updateMessage(String paramString, MessageMicro paramMessageMicro)
    {
      removeMessage(paramString, NowConfigurationPreferences.this.mPrimaryKeyFactory.primaryKeyFor(paramMessageMicro));
      addMessage(paramString, paramMessageMicro);
      return this;
    }
  }
  
  private static final class FieldWithTarget
  {
    final Field mField;
    final MessageMicro mTarget;
    
    FieldWithTarget(MessageMicro paramMessageMicro, Field paramField)
    {
      this.mTarget = paramMessageMicro;
      this.mField = paramField;
    }
    
    Object get()
    {
      return MessageMicroUtil.getFieldValue(this.mTarget, this.mField);
    }
    
    boolean has()
    {
      return MessageMicroUtil.hasField(this.mTarget, this.mField);
    }
    
    void set(Object paramObject)
    {
      MessageMicroUtil.setFieldValue(this.mTarget, this.mField, paramObject);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.NowConfigurationPreferences
 * JD-Core Version:    0.7.0.1
 */