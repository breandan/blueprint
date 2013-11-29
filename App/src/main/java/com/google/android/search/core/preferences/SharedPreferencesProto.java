package com.google.android.search.core.preferences;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.google.protobuf.micro.ByteStringMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class SharedPreferencesProto
  implements SharedPreferencesExt
{
  private final File mBackupFile;
  private final File mFile;
  private final Set<SharedPreferences.OnSharedPreferenceChangeListener> mListeners;
  private boolean mLoaded;
  private final Object mLock = new Object();
  private Map<String, Object> mMap;
  private final Object mRemoveMarker = this.mLock;
  private WriteData mWriteData;
  private int mWriteDelayCounter;
  
  public SharedPreferencesProto(File paramFile)
  {
    this.mFile = paramFile;
    this.mBackupFile = new File(paramFile.getPath() + ".bak");
    this.mListeners = Sets.newHashSet();
    this.mMap = Maps.newHashMap();
    this.mWriteData = new WriteData(null);
    new Thread("Search.SharedPreferencesProto")
    {
      public void run()
      {
        SharedPreferencesProto.this.loadFromFile();
      }
    }.start();
  }
  
  private boolean commitLoadedMapLocked(Map<String, Object> paramMap)
  {
    boolean bool = this.mLoaded;
    if (!this.mLoaded)
    {
      if (paramMap == null) {
        break label189;
      }
      Iterator localIterator = this.mMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = (String)localEntry.getKey();
        Object localObject = localEntry.getValue();
        if (localObject == this.mRemoveMarker)
        {
          if (paramMap.containsKey(str))
          {
            paramMap.remove(str);
            bool = true;
          }
        }
        else if ((!paramMap.containsKey(str)) || ((localObject == null) && (paramMap.get(str) != null)) || ((localObject != null) && (!localObject.equals(paramMap.get(str)))))
        {
          paramMap.put(str, localObject);
          bool = true;
        }
      }
      this.mMap = paramMap;
    }
    for (;;)
    {
      this.mLoaded = true;
      this.mLock.notifyAll();
      return bool;
      label189:
      Maps.filterValues(this.mMap, Predicates.equalTo(this.mRemoveMarker)).clear();
      bool = true;
    }
  }
  
  private static Map<String, Object> dataToMap(SharedPreferencesData paramSharedPreferencesData)
  {
    HashMap localHashMap = new HashMap();
    Iterator localIterator = paramSharedPreferencesData.getEntryList().iterator();
    while (localIterator.hasNext())
    {
      SharedPreferencesData.SharedPreferenceEntry localSharedPreferenceEntry = (SharedPreferencesData.SharedPreferenceEntry)localIterator.next();
      if (localSharedPreferenceEntry.hasKey()) {}
      for (String str1 = localSharedPreferenceEntry.getKey();; str1 = null)
      {
        if (!localSharedPreferenceEntry.hasBoolValue()) {
          break label80;
        }
        localHashMap.put(str1, Boolean.valueOf(localSharedPreferenceEntry.getBoolValue()));
        break;
      }
      label80:
      if (localSharedPreferenceEntry.hasFloatValue())
      {
        localHashMap.put(str1, Float.valueOf(localSharedPreferenceEntry.getFloatValue()));
      }
      else if (localSharedPreferenceEntry.hasIntValue())
      {
        localHashMap.put(str1, Integer.valueOf(localSharedPreferenceEntry.getIntValue()));
      }
      else if (localSharedPreferenceEntry.hasLongValue())
      {
        localHashMap.put(str1, Long.valueOf(localSharedPreferenceEntry.getLongValue()));
      }
      else if (localSharedPreferenceEntry.hasStringValue())
      {
        localHashMap.put(str1, localSharedPreferenceEntry.getStringValue());
      }
      else if (localSharedPreferenceEntry.getStringSetValueCount() != 0)
      {
        HashSet localHashSet = Sets.newHashSet();
        String str2 = localSharedPreferenceEntry.getStringSetValue(0);
        if (!str2.isEmpty())
        {
          if (str2.equals("null")) {
            localHashSet.add(null);
          }
        }
        else
        {
          int i = localSharedPreferenceEntry.getStringSetValueCount();
          for (int j = 1; j != i; j++) {
            localHashSet.add(localSharedPreferenceEntry.getStringSetValue(j));
          }
        }
        Log.e("Search.SharedPreferencesProto", "dataToMap: invalid nullTag: " + str1 + "->" + str2);
        continue;
        localHashMap.put(str1, localHashSet);
      }
      else if (localSharedPreferenceEntry.hasBytesValue())
      {
        localHashMap.put(str1, localSharedPreferenceEntry.getBytesValue());
      }
      else
      {
        localHashMap.put(str1, null);
      }
    }
    return localHashMap;
  }
  
  private Object doGet(String paramString)
  {
    for (;;)
    {
      Object localObject4;
      synchronized (this.mLock)
      {
        if (this.mLoaded)
        {
          Object localObject6 = this.mMap.get(paramString);
          return localObject6;
        }
        if (!this.mMap.containsKey(paramString)) {
          break;
        }
        localObject4 = this.mMap.get(paramString);
        if (localObject4 == this.mRemoveMarker)
        {
          localObject5 = null;
          return localObject5;
        }
      }
      Object localObject5 = localObject4;
    }
    waitForLoadLocked();
    Object localObject3 = this.mMap.get(paramString);
    return localObject3;
  }
  
  private Map<String, Object> doLoadPreferenceMap()
  {
    if (this.mBackupFile.exists())
    {
      this.mFile.delete();
      if (!this.mBackupFile.renameTo(this.mFile))
      {
        Log.e("Search.SharedPreferencesProto", "Failed to rename backup file to " + this.mFile);
        return null;
      }
    }
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = createFileInputStream(this.mFile);
      int i = localFileInputStream.available();
      int j = 0;
      byte[] arrayOfByte = new byte[Math.max(i + 1, 2048)];
      for (int k = localFileInputStream.read(arrayOfByte); k >= 0; k = localFileInputStream.read(arrayOfByte, j, arrayOfByte.length - j))
      {
        j += k;
        if (j == arrayOfByte.length) {
          arrayOfByte = Arrays.copyOf(arrayOfByte, 2 * arrayOfByte.length);
        }
      }
      SharedPreferencesData localSharedPreferencesData = new SharedPreferencesData();
      localSharedPreferencesData.mergeFrom(arrayOfByte, 0, j);
      Map localMap = dataToMap(localSharedPreferencesData);
      return localMap;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Log.i("Search.SharedPreferencesProto", "load shared preferences: file not found");
      HashMap localHashMap = new HashMap();
      return localHashMap;
    }
    catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
    {
      Log.w("Search.SharedPreferencesProto", "load shared preferences", localInvalidProtocolBufferMicroException);
      return null;
    }
    catch (IOException localIOException)
    {
      Log.w("Search.SharedPreferencesProto", "load shared preferences", localIOException);
      return null;
    }
    finally
    {
      Closeables.closeQuietly(localFileInputStream);
    }
  }
  
  private boolean doWritePreferenceMap(Map<String, Object> paramMap)
  {
    if (this.mFile.exists()) {
      if (this.mBackupFile.exists()) {
        this.mFile.delete();
      }
    }
    do
    {
      do
      {
        byte[] arrayOfByte = mapToData(paramMap).toByteArray();
        FileOutputStream localFileOutputStream = null;
        try
        {
          localFileOutputStream = createFileOutputStream(this.mFile);
          localFileOutputStream.write(arrayOfByte);
          localFileOutputStream.flush();
          localFileOutputStream.getFD().sync();
          localFileOutputStream.close();
          this.mBackupFile.delete();
          return true;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          File localFile;
          Log.e("Search.SharedPreferencesProto", "exception while writing to file: ", localFileNotFoundException);
          return false;
        }
        catch (IOException localIOException)
        {
          Log.e("Search.SharedPreferencesProto", "exception while writing to file: ", localIOException);
          return false;
        }
        finally
        {
          Closeables.closeQuietly(localFileOutputStream);
        }
      } while (this.mFile.renameTo(this.mBackupFile));
      Log.e("Search.SharedPreferencesProto", "Failed to rename to backup file " + this.mBackupFile);
      return false;
      localFile = this.mFile.getParentFile();
    } while ((localFile.exists()) || (localFile.mkdir()));
    Log.e("Search.SharedPreferencesProto", "Failed to create shared preferences directory " + localFile);
    return false;
  }
  
  private void loadFromFile()
  {
    Map localMap;
    synchronized (this.mLock)
    {
      boolean bool1 = this.mLoaded;
      localMap = null;
      if (!bool1) {}
    }
    synchronized (this.mLock)
    {
      boolean bool2 = commitLoadedMapLocked(localMap);
      if (!bool2)
      {
        Preconditions.checkNotNull(this.mWriteData);
        this.mWriteData.mWriteResult = true;
        this.mWriteData.mWaitLatch.countDown();
        this.mWriteData = null;
      }
      if (bool2) {
        writeToFile();
      }
      return;
      localObject2 = finally;
      throw localObject2;
      localMap = doLoadPreferenceMap();
    }
  }
  
  private static SharedPreferencesData mapToData(Map<String, Object> paramMap)
  {
    SharedPreferencesData localSharedPreferencesData = new SharedPreferencesData();
    Iterator localIterator1 = paramMap.entrySet().iterator();
    if (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      SharedPreferencesData.SharedPreferenceEntry localSharedPreferenceEntry = new SharedPreferencesData.SharedPreferenceEntry();
      String str1 = (String)localEntry.getKey();
      if (str1 != null) {
        localSharedPreferenceEntry.setKey(str1);
      }
      Object localObject = localEntry.getValue();
      if (localObject == null) {}
      for (;;)
      {
        localSharedPreferencesData.addEntry(localSharedPreferenceEntry);
        break;
        if ((localObject instanceof Boolean))
        {
          localSharedPreferenceEntry.setBoolValue(((Boolean)localObject).booleanValue());
        }
        else if ((localObject instanceof Float))
        {
          localSharedPreferenceEntry.setFloatValue(((Float)localObject).floatValue());
        }
        else if ((localObject instanceof Integer))
        {
          localSharedPreferenceEntry.setIntValue(((Integer)localObject).intValue());
        }
        else if ((localObject instanceof Long))
        {
          localSharedPreferenceEntry.setLongValue(((Long)localObject).longValue());
        }
        else if ((localObject instanceof String))
        {
          localSharedPreferenceEntry.setStringValue((String)localObject);
        }
        else
        {
          if ((localObject instanceof Set))
          {
            Set localSet = (Set)localObject;
            if (localSet.contains(null)) {}
            for (String str2 = "null";; str2 = "")
            {
              localSharedPreferenceEntry.addStringSetValue(str2);
              Iterator localIterator2 = localSet.iterator();
              while (localIterator2.hasNext())
              {
                String str3 = (String)localIterator2.next();
                if (str3 != null) {
                  localSharedPreferenceEntry.addStringSetValue(str3);
                }
              }
              break;
            }
          }
          if ((localObject instanceof ByteStringMicro)) {
            localSharedPreferenceEntry.setBytesValue((ByteStringMicro)localObject);
          } else {
            Log.w("Search.SharedPreferencesProto", "mapToData: invalid entry class = " + localObject.getClass().getName());
          }
        }
      }
    }
    return localSharedPreferencesData;
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
        break label131;
      }
    }
    Looper localLooper;
    for (;;)
    {
      Preconditions.checkArgument(bool1);
      localLooper = Looper.getMainLooper();
      if (localLooper.getThread() != Thread.currentThread()) {
        break label136;
      }
      Iterator localIterator1 = paramSet.iterator();
      while (localIterator1.hasNext())
      {
        String str = (String)localIterator1.next();
        Iterator localIterator2 = paramCollection.iterator();
        while (localIterator2.hasNext()) {
          ((SharedPreferences.OnSharedPreferenceChangeListener)localIterator2.next()).onSharedPreferenceChanged(this, str);
        }
      }
      bool2 = false;
      break;
      label131:
      bool1 = false;
    }
    label136:
    new Handler(localLooper).post(new Runnable()
    {
      public void run()
      {
        SharedPreferencesProto.this.notifyListeners(paramCollection, paramSet);
      }
    });
  }
  
  private void waitForLoadLocked()
  {
    if (!this.mLoaded) {}
    int i;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      i = 0;
      while (!this.mLoaded) {
        try
        {
          this.mLock.wait();
        }
        catch (InterruptedException localInterruptedException)
        {
          i = 1;
        }
      }
    }
    if (i != 0) {
      Thread.currentThread().interrupt();
    }
  }
  
  private void writeToFile()
  {
    WriteData localWriteData = null;
    boolean bool1 = false;
    Map localMap = null;
    Object localObject1 = this.mLock;
    if (localWriteData != null) {}
    for (;;)
    {
      try
      {
        if (localWriteData.mWrittenMap == localMap)
        {
          bool3 = true;
          Preconditions.checkArgument(bool3);
          localWriteData.mWrittenMap = null;
          localWriteData.mWriteResult = bool1;
          localWriteData.mWaitLatch.countDown();
          if (localWriteData == this.mWriteData)
          {
            this.mWriteData = null;
            return;
          }
          int i = this.mWriteDelayCounter;
          if (i != 0)
          {
            try
            {
              this.mLock.wait();
            }
            catch (InterruptedException localInterruptedException) {}
            continue;
          }
          Preconditions.checkNotNull(this.mWriteData);
          if (this.mWriteData.mWrittenMap == null)
          {
            bool2 = true;
            Preconditions.checkArgument(bool2);
            localWriteData = this.mWriteData;
            localMap = this.mMap;
            this.mWriteData.mWrittenMap = localMap;
            bool1 = doWritePreferenceMap(localMap);
            break;
          }
          boolean bool2 = false;
          continue;
        }
        boolean bool3 = false;
      }
      finally {}
    }
  }
  
  public void allowWrites()
  {
    synchronized (this.mLock)
    {
      this.mWriteDelayCounter = (-1 + this.mWriteDelayCounter);
      if (this.mWriteDelayCounter == 0) {
        this.mLock.notifyAll();
      }
      return;
    }
  }
  
  public boolean contains(String paramString)
  {
    return doGet(paramString) != null;
  }
  
  protected FileInputStream createFileInputStream(File paramFile)
    throws FileNotFoundException
  {
    return new FileInputStream(paramFile);
  }
  
  protected FileOutputStream createFileOutputStream(File paramFile)
    throws FileNotFoundException
  {
    return new FileOutputStream(paramFile);
  }
  
  public void delayWrites()
  {
    synchronized (this.mLock)
    {
      this.mWriteDelayCounter = (1 + this.mWriteDelayCounter);
      return;
    }
  }
  
  public SharedPreferencesExt.Editor edit()
  {
    return new EditorImpl();
  }
  
  public Map<String, ?> getAll()
  {
    synchronized (this.mLock)
    {
      if (!this.mLoaded) {
        waitForLoadLocked();
      }
      ImmutableMap localImmutableMap = ImmutableMap.copyOf(this.mMap);
      return localImmutableMap;
    }
  }
  
  public Map<String, ?> getAllByKeyPrefix(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      throw new IllegalArgumentException("keyPrefix must be non-empty");
    }
    HashMap localHashMap;
    synchronized (this.mLock)
    {
      if (!this.mLoaded) {
        waitForLoadLocked();
      }
      localHashMap = Maps.newHashMap();
      Iterator localIterator = this.mMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        String str = (String)localEntry.getKey();
        if ((!TextUtils.isEmpty(str)) && (str.startsWith(paramString))) {
          localHashMap.put(str, localEntry.getValue());
        }
      }
    }
    return localHashMap;
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      paramBoolean = ((Boolean)localObject).booleanValue();
    }
    return paramBoolean;
  }
  
  public byte[] getBytes(String paramString, byte[] paramArrayOfByte)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      paramArrayOfByte = ((ByteStringMicro)localObject).toByteArray();
    }
    return paramArrayOfByte;
  }
  
  public float getFloat(String paramString, float paramFloat)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      paramFloat = ((Float)localObject).floatValue();
    }
    return paramFloat;
  }
  
  public int getInt(String paramString, int paramInt)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      paramInt = ((Integer)localObject).intValue();
    }
    return paramInt;
  }
  
  public long getLong(String paramString, long paramLong)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      paramLong = ((Long)localObject).longValue();
    }
    return paramLong;
  }
  
  public String getString(String paramString1, String paramString2)
  {
    Object localObject = doGet(paramString1);
    if (localObject != null) {
      return (String)localObject;
    }
    return paramString2;
  }
  
  public Set<String> getStringSet(String paramString, Set<String> paramSet)
  {
    Object localObject = doGet(paramString);
    if (localObject != null) {
      return (Set)localObject;
    }
    return paramSet;
  }
  
  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    Preconditions.checkNotNull(paramOnSharedPreferenceChangeListener);
    synchronized (this.mLock)
    {
      this.mListeners.add(paramOnSharedPreferenceChangeListener);
      return;
    }
  }
  
  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (this.mLock)
    {
      this.mListeners.remove(paramOnSharedPreferenceChangeListener);
      return;
    }
  }
  
  public class EditorImpl
    implements SharedPreferencesExt.Editor
  {
    private boolean mClear;
    private final Object mEditorLock = new Object();
    private Map<String, Object> mModified = new HashMap();
    
    public EditorImpl() {}
    
    private boolean doCommit(boolean paramBoolean)
    {
      Object localObject3;
      synchronized (SharedPreferencesProto.this.mLock)
      {
        localObject3 = SharedPreferencesProto.this.mMap;
        if ((SharedPreferencesProto.this.mWriteData != null) && (SharedPreferencesProto.this.mWriteData.mWrittenMap == localObject3)) {
          localObject3 = new HashMap((Map)localObject3);
        }
      }
      synchronized (this.mEditorLock)
      {
        Object localObject7;
        if (this.mClear)
        {
          Maps.filterValues(this.mModified, Predicates.equalTo(SharedPreferencesProto.this.mRemoveMarker)).clear();
          if ((((Map)localObject3).isEmpty()) && (this.mModified.isEmpty()))
          {
            boolean bool5 = SharedPreferencesProto.this.mLoaded;
            i = 0;
            localObject7 = null;
            if (bool5) {}
          }
          else
          {
            i = 1;
            localHashSet = new HashSet(this.mModified.keySet());
          }
        }
        SharedPreferencesProto.WriteData localWriteData;
        boolean bool2;
        try
        {
          localObject3 = this.mModified;
          this.mModified = new HashMap();
          SharedPreferencesProto.access$702(SharedPreferencesProto.this, true);
          this.mEditorLock.notifyAll();
          localObject7 = localHashSet;
          this.mClear = false;
          ArrayList localArrayList = null;
          if (localObject7 != null)
          {
            boolean bool3 = localObject7.isEmpty();
            localArrayList = null;
            if (!bool3)
            {
              boolean bool4 = SharedPreferencesProto.this.mListeners.isEmpty();
              localArrayList = null;
              if (!bool4) {
                localArrayList = new ArrayList(SharedPreferencesProto.this.mListeners);
              }
            }
          }
          j = 0;
          if (i != 0)
          {
            if (SharedPreferencesProto.this.mWriteData == null)
            {
              j = 1;
              if ((SharedPreferencesProto.this.mWriteData == null) || (SharedPreferencesProto.this.mWriteData.mWrittenMap == SharedPreferencesProto.this.mMap)) {
                SharedPreferencesProto.access$602(SharedPreferencesProto.this, new SharedPreferencesProto.WriteData(SharedPreferencesProto.this, null));
              }
              SharedPreferencesProto.access$502(SharedPreferencesProto.this, (Map)localObject3);
            }
          }
          else
          {
            localWriteData = null;
            if (paramBoolean)
            {
              localWriteData = SharedPreferencesProto.this.mWriteData;
              if (SharedPreferencesProto.this.mWriteDelayCounter != 0) {
                Log.w("Search.SharedPreferencesProto", "potential deadlock: commit while delayWrites", new Throwable());
              }
            }
            if (j != 0)
            {
              Thread local1 = new Thread("Search.SharedPreferencesProto")
              {
                public void run()
                {
                  SharedPreferencesProto.this.writeToFile();
                }
              };
              local1.start();
            }
            if (localArrayList != null) {
              SharedPreferencesProto.this.notifyListeners(localArrayList, localObject7);
            }
            bool2 = true;
            if (localWriteData == null) {}
          }
        }
        finally {}
        try
        {
          localWriteData.mWaitLatch.await();
          bool2 = localWriteData.mWriteResult;
          return bool2;
        }
        catch (InterruptedException localInterruptedException)
        {
          boolean bool1;
          Thread.currentThread().interrupt();
          return false;
        }
        HashSet localHashSet = new HashSet();
        Iterator localIterator = this.mModified.entrySet().iterator();
        while (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          String str = (String)localEntry.getKey();
          Object localObject8 = localEntry.getValue();
          Object localObject9 = ((Map)localObject3).get(str);
          if ((SharedPreferencesProto.this.mLoaded) && (localObject8 == SharedPreferencesProto.this.mRemoveMarker))
          {
            if (((Map)localObject3).containsKey(str))
            {
              ((Map)localObject3).remove(str);
              localHashSet.add(str);
              continue;
              throw localObject5;
              localObject2 = finally;
              throw localObject2;
            }
          }
          else if ((!((Map)localObject3).containsKey(str)) || ((localObject8 == null) && (((Map)localObject3).get(str) != null)) || ((localObject8 != null) && (!localObject8.equals(localObject9))))
          {
            ((Map)localObject3).put(str, localObject8);
            localHashSet.add(str);
          }
        }
        this.mModified.clear();
        bool1 = localHashSet.isEmpty();
        if (!bool1) {}
        for (int i = 1;; i = 0)
        {
          localObject7 = localHashSet;
          break;
        }
        int j = 0;
      }
    }
    
    private void doPut(String paramString, Object paramObject)
    {
      synchronized (this.mEditorLock)
      {
        this.mModified.put(paramString, paramObject);
        return;
      }
    }
    
    public void apply()
    {
      doCommit(false);
    }
    
    public SharedPreferencesExt.Editor clear()
    {
      synchronized (this.mEditorLock)
      {
        this.mClear = true;
        return this;
      }
    }
    
    public boolean commit()
    {
      return doCommit(true);
    }
    
    public SharedPreferencesExt.Editor putBoolean(String paramString, boolean paramBoolean)
    {
      doPut(paramString, Boolean.valueOf(paramBoolean));
      return this;
    }
    
    public SharedPreferencesExt.Editor putBytes(String paramString, byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null) {}
      for (Object localObject = null;; localObject = ByteStringMicro.copyFrom(paramArrayOfByte))
      {
        doPut(paramString, localObject);
        return this;
      }
    }
    
    public SharedPreferencesExt.Editor putFloat(String paramString, float paramFloat)
    {
      doPut(paramString, Float.valueOf(paramFloat));
      return this;
    }
    
    public SharedPreferencesExt.Editor putInt(String paramString, int paramInt)
    {
      doPut(paramString, Integer.valueOf(paramInt));
      return this;
    }
    
    public SharedPreferencesExt.Editor putLong(String paramString, long paramLong)
    {
      doPut(paramString, Long.valueOf(paramLong));
      return this;
    }
    
    public SharedPreferencesExt.Editor putString(String paramString1, String paramString2)
    {
      doPut(paramString1, paramString2);
      return this;
    }
    
    public SharedPreferencesExt.Editor putStringSet(String paramString, Set<String> paramSet)
    {
      if (paramSet == null) {}
      for (Object localObject = null;; localObject = new HashSet(paramSet))
      {
        doPut(paramString, localObject);
        return this;
      }
    }
    
    public SharedPreferencesExt.Editor remove(String paramString)
    {
      doPut(paramString, SharedPreferencesProto.this.mRemoveMarker);
      return this;
    }
  }
  
  private class WriteData
  {
    public final CountDownLatch mWaitLatch = new CountDownLatch(1);
    public boolean mWriteResult;
    public Map<String, Object> mWrittenMap;
    
    private WriteData() {}
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.preferences.SharedPreferencesProto
 * JD-Core Version:    0.7.0.1
 */