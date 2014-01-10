package com.embryo.android.search.core.preferences;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.embryo.protobuf.micro.ByteStringMicro;
import com.embryo.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;

import java.io.File;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class SharedPreferencesProto
        implements SharedPreferencesExt {
    private final File mBackupFile;
    private final File mFile;
    private final Set<SharedPreferences.OnSharedPreferenceChangeListener> mListeners;
    private final Object mLock = new Object();
    private final Object mRemoveMarker = this.mLock;
    private boolean mLoaded;
    private Map<String, Object> mMap;
    private WriteData mWriteData;
    private int mWriteDelayCounter;

    public SharedPreferencesProto(File paramFile) {
        this.mFile = paramFile;
        this.mBackupFile = new File(paramFile.getPath() + ".bak");
        this.mListeners = Sets.newHashSet();
        this.mMap = Maps.newHashMap();
        this.mWriteData = new WriteData();
        new Thread("Search.SharedPreferencesProto") {
            public void run() {
                SharedPreferencesProto.this.loadFromFile();
            }
        }.start();
    }

    private static Map<String, Object> dataToMap(SharedPreferencesData data) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        for (SharedPreferencesData.SharedPreferenceEntry entry : data.getEntryList()) {
            String key = entry.hasKey() ? entry.getKey() : null;
            if (entry.hasBoolValue()) {
                result.put(key, Boolean.valueOf(entry.getBoolValue()));
            }
            if (entry.hasFloatValue()) {
                result.put(key, Float.valueOf(entry.getFloatValue()));
            }
            if (entry.hasIntValue()) {
                result.put(key, Integer.valueOf(entry.getIntValue()));
            }
            if (entry.hasLongValue()) {
                result.put(key, Long.valueOf(entry.getLongValue()));
            }
            if (entry.hasStringValue()) {
                result.put(key, entry.getStringValue());
            }
            if (entry.getStringSetValueCount() != 0) {
                Set<String> stringSet = Sets.newHashSet();
                String nullTag = entry.getStringSetValue(0x0);
                if (!nullTag.isEmpty()) {
                    if (nullTag.equals("null")) {
                        stringSet.add(null);
                    } else {
                        Log.e("Search.SharedPreferencesProto", "dataToMap: invalid nullTag: " + key + "->" + nullTag);
                        break;
                    }
                }
                int count = entry.getStringSetValueCount();
                for (int i = 0x1; i != count; i = i + 0x1) {
                    stringSet.add(entry.getStringSetValue(i));
                }
                result.put(key, stringSet);
            }
            if (entry.hasBytesValue()) {
                result.put(key, entry.getBytesValue());
            }
            result.put(key, 0x0);
            break;
        }
        return result;
    }

    private static SharedPreferencesData mapToData(Map<String, Object> map) {
        SharedPreferencesData result = new SharedPreferencesData();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            SharedPreferencesData.SharedPreferenceEntry dataEntry = new SharedPreferencesData.SharedPreferenceEntry();
            String key = (String) entry.getKey();
            if (key != null) {
                dataEntry.setKey(key);
            }
            Object value = entry.getValue();
            if (value == null) {
            } else if (value instanceof Boolean) {
                dataEntry.setBoolValue(((Boolean) value).booleanValue());
            } else if (value instanceof Float) {
                dataEntry.setFloatValue(((Float) value).floatValue());
            } else if (value instanceof Integer) {
                dataEntry.setIntValue(((Integer) value).intValue());
            } else if (value instanceof Long) {
                dataEntry.setLongValue(((Long) value).longValue());
            } else if (value instanceof String) {
                dataEntry.setStringValue((String) value);
            } else if (value instanceof Set) {
                Set<String> stringSet = (Set<String>) value;
                dataEntry.addStringSetValue("");
                for (String s : stringSet) {
                    if (s != null) {
                        dataEntry.addStringSetValue(s);
                    } else if (value instanceof ByteStringMicro) {
                        dataEntry.setBytesValue((ByteStringMicro) value);
                    } else {
                        Log.w("Search.SharedPreferencesProto", "mapToData: invalid entry class = " + value.getClass().getName());
                        break;
                    }
                }
            }
            result.addEntry(dataEntry);
        }
        return result;
    }

    private boolean commitLoadedMapLocked(Map<String, Object> paramMap) {
//        boolean bool = this.mLoaded;
//        if (!this.mLoaded) {
//            if (paramMap == null) {
//                break label189;
//            }
//            Iterator localIterator = this.mMap.entrySet().iterator();
//            while (localIterator.hasNext()) {
//                Map.Entry localEntry = (Map.Entry) localIterator.next();
//                String str = (String) localEntry.getKey();
//                Object localObject = localEntry.getValue();
//                if (localObject == this.mRemoveMarker) {
//                    if (paramMap.containsKey(str)) {
//                        paramMap.remove(str);
//                        bool = true;
//                    }
//                } else if ((!paramMap.containsKey(str)) || ((localObject == null) && (paramMap.get(str) != null)) || ((localObject != null) && (!localObject.equals(paramMap.get(str))))) {
//                    paramMap.put(str, localObject);
//                    bool = true;
//                }
//            }
//            this.mMap = paramMap;
//        }
//        for (; ; ) {
//            this.mLoaded = true;
//            this.mLock.notifyAll();
//            return bool;
//            label189:
//            Maps.filterValues(this.mMap, Predicates.equalTo(this.mRemoveMarker)).clear();
//            bool = true;
//        }
        return false;
    }

    private Object doGet(String paramString) {
//        for (; ; ) {
//            Object localObject4;
//            synchronized (this.mLock) {
//                if (this.mLoaded) {
//                    Object localObject6 = this.mMap.get(paramString);
//                    return localObject6;
//                }
//                if (!this.mMap.containsKey(paramString)) {
//                    break;
//                }
//                localObject4 = this.mMap.get(paramString);
//                if (localObject4 == this.mRemoveMarker) {
//                    localObject5 = null;
//                    return localObject5;
//                }
//            }
//            Object localObject5 = localObject4;
//        }
//        waitForLoadLocked();
//        Object localObject3 = this.mMap.get(paramString);
//        return localObject3;
        return null;
    }

    private Map doLoadPreferenceMap() {
        if (mBackupFile.exists()) {
            mFile.delete();
            if (!mBackupFile.renameTo(mFile)) {
                Log.e("Search.SharedPreferencesProto", "Failed to rename backup file to " + mFile);
                return null;
            }
        }
        FileInputStream in = null;
        try {
            in = createFileInputStream(mFile);
            int available = in.available();
            int length = 0x0;
            byte[] rawData = new byte[Math.max((available + 0x1), 0x800)];
            int bytesRead = in.read(rawData);
            while (bytesRead >= 0) {
                length += bytesRead;
                if (length == rawData.length) {
                    rawData = Arrays.copyOf(rawData, (rawData.length * 0x2));
                }
                bytesRead = in.read(rawData, length, (rawData.length - length));
            }
            SharedPreferencesData preferenceData = new SharedPreferencesData();
            preferenceData.mergeFrom(rawData, 0x0, length);
            return dataToMap(preferenceData);
        } catch (FileNotFoundException e) {
            Log.i("Search.SharedPreferencesProto", "load shared preferences: file not found");
            HashMap localHashMap2 = new HashMap();
        } catch (InvalidProtocolBufferMicroException e) {
            Log.w("Search.SharedPreferencesProto", "load shared preferences", e);
        } catch (IOException e) {
            Log.w("Search.SharedPreferencesProto", "load shared preferences", e);
        } finally {
            Closeables.closeQuietly(in);
        }
        return null;
    }

    private void doWritePreferenceMap(Map<String, Object> map) {
        if (mFile.exists()) {
            if (mBackupFile.exists()) {
                mFile.delete();
            }
            if (!mFile.renameTo(mBackupFile)) {
                Log.e("Search.SharedPreferencesProto", "Failed to rename to backup file " + mBackupFile);
                return;
            }
        }
        File parent = mFile.getParentFile();
        if ((!parent.exists()) && (!parent.mkdir())) {
            Log.e("Search.SharedPreferencesProto", "Failed to create shared preferences directory " + parent);
            return;
        }
        SharedPreferencesData preferenceData = mapToData(map);
        byte[] rawData = preferenceData.toByteArray();
        FileOutputStream out = null;
        try {
            out = createFileOutputStream(mFile);
            out.write(rawData);
            out.flush();
            out.getFD().sync();
            out.close();
            mBackupFile.delete();
        } catch (FileNotFoundException e) {
            Log.e("Search.SharedPreferencesProto", "exception while writing to file: ", e);
        } catch (IOException e) {
            Log.e("Search.SharedPreferencesProto", "exception while writing to file: ", e);
        } finally {
            Closeables.closeQuietly(out);
        }
    }

    private void loadFromFile() {
//        Map localMap;
//        synchronized (this.mLock) {
//            boolean bool1 = this.mLoaded;
//            localMap = null;
//            if (!bool1) {
//            }
//        }
//        synchronized (this.mLock) {
//            boolean bool2 = commitLoadedMapLocked(localMap);
//            if (!bool2) {
//                Preconditions.checkNotNull(this.mWriteData);
//                this.mWriteData.mWriteResult = true;
//                this.mWriteData.mWaitLatch.countDown();
//                this.mWriteData = null;
//            }
//            if (bool2) {
//                writeToFile();
//            }
//            return;
//            localObject2 =finally;
//            throw localObject2;
//            localMap = doLoadPreferenceMap();
//        }
    }

    private void notifyListeners(Collection<SharedPreferences.OnSharedPreferenceChangeListener> listeners, Set<String> modifiedKeys) {
//        Preconditions.checkArgument(((listeners != null) && (!listeners.isEmpty())));
//        Preconditions.checkArgument(((modifiedKeys != null) && (!modifiedKeys.isEmpty())));
//        Looper mainLooper = Looper.getMainLooper();
//        if (mainLooper.getThread() == Thread.currentThread()) {
//            if (modifiedKeys.iterator().hasNext()) {
//                String key = (String) modifiedKeys.iterator().next();
//                for (SharedPreferences.OnSharedPreferenceChangeListener listener : listeners) {
//                    listener.onSharedPreferenceChanged(this, key);
//                }
//            }
//            new Handler(mainLooper).post(new SharedPreferencesProto(listeners, modifiedKeys));
//        }
    }

    private void waitForLoadLocked() {
        Preconditions.checkState((!mLoaded));
        boolean interrupted = false;
        while (!mLoaded) {
            try {
                mLock.wait();
                continue;
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private void writeToFile() {
//        WriteData localWriteData = null;
//        boolean bool1 = false;
//        Map localMap = null;
//        Object localObject1 = this.mLock;
//        if (localWriteData != null) {
//        }
//        for (; ; ) {
//            try {
//                if (localWriteData.mWrittenMap == localMap) {
//                    bool3 = true;
//                    Preconditions.checkArgument(bool3);
//                    localWriteData.mWrittenMap = null;
//                    localWriteData.mWriteResult = bool1;
//                    localWriteData.mWaitLatch.countDown();
//                    if (localWriteData == this.mWriteData) {
//                        this.mWriteData = null;
//                        return;
//                    }
//                    int i = this.mWriteDelayCounter;
//                    if (i != 0) {
//                        try {
//                            this.mLock.wait();
//                        } catch (InterruptedException localInterruptedException) {
//                        }
//                        continue;
//                    }
//                    Preconditions.checkNotNull(this.mWriteData);
//                    if (this.mWriteData.mWrittenMap == null) {
//                        bool2 = true;
//                        Preconditions.checkArgument(bool2);
//                        localWriteData = this.mWriteData;
//                        localMap = this.mMap;
//                        this.mWriteData.mWrittenMap = localMap;
//                        bool1 = doWritePreferenceMap(localMap);
//                        break;
//                    }
//                    boolean bool2 = false;
//                    continue;
//                }
//                boolean bool3 = false;
//            } finally {
//            }
//        }
    }

    public void allowWrites() {
        synchronized (this.mLock) {
            this.mWriteDelayCounter = (-1 + this.mWriteDelayCounter);
            if (this.mWriteDelayCounter == 0) {
                this.mLock.notifyAll();
            }
            return;
        }
    }

    public boolean contains(String paramString) {
        return doGet(paramString) != null;
    }

    protected FileInputStream createFileInputStream(File paramFile)
            throws FileNotFoundException {
        return new FileInputStream(paramFile);
    }

    protected FileOutputStream createFileOutputStream(File paramFile)
            throws FileNotFoundException {
        return new FileOutputStream(paramFile);
    }

    public void delayWrites() {
        synchronized (this.mLock) {
            this.mWriteDelayCounter = (1 + this.mWriteDelayCounter);
            return;
        }
    }

    public SharedPreferencesExt.Editor edit() {
        return new EditorImpl();
    }

    public Map<String, ?> getAll() {
        synchronized (this.mLock) {
            if (!this.mLoaded) {
                waitForLoadLocked();
            }
            ImmutableMap localImmutableMap = ImmutableMap.copyOf(this.mMap);
            return localImmutableMap;
        }
    }

    public Map<String, ?> getAllByKeyPrefix(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            throw new IllegalArgumentException("keyPrefix must be non-empty");
        }
        HashMap localHashMap;
        synchronized (this.mLock) {
            if (!this.mLoaded) {
                waitForLoadLocked();
            }
            localHashMap = Maps.newHashMap();
            Iterator localIterator = this.mMap.entrySet().iterator();
            while (localIterator.hasNext()) {
                Map.Entry localEntry = (Map.Entry) localIterator.next();
                String str = (String) localEntry.getKey();
                if ((!TextUtils.isEmpty(str)) && (str.startsWith(paramString))) {
                    localHashMap.put(str, localEntry.getValue());
                }
            }
        }
        return localHashMap;
    }

    public boolean getBoolean(String paramString, boolean paramBoolean) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            paramBoolean = ((Boolean) localObject).booleanValue();
        }
        return paramBoolean;
    }

    public byte[] getBytes(String paramString, byte[] paramArrayOfByte) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            paramArrayOfByte = ((ByteStringMicro) localObject).toByteArray();
        }
        return paramArrayOfByte;
    }

    public float getFloat(String paramString, float paramFloat) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            paramFloat = ((Float) localObject).floatValue();
        }
        return paramFloat;
    }

    public int getInt(String paramString, int paramInt) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            paramInt = ((Integer) localObject).intValue();
        }
        return paramInt;
    }

    public long getLong(String paramString, long paramLong) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            paramLong = ((Long) localObject).longValue();
        }
        return paramLong;
    }

    public String getString(String paramString1, String paramString2) {
        Object localObject = doGet(paramString1);
        if (localObject != null) {
            return (String) localObject;
        }
        return paramString2;
    }

    public Set<String> getStringSet(String paramString, Set<String> paramSet) {
        Object localObject = doGet(paramString);
        if (localObject != null) {
            return (Set) localObject;
        }
        return paramSet;
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener) {
        Preconditions.checkNotNull(paramOnSharedPreferenceChangeListener);
        synchronized (this.mLock) {
            this.mListeners.add(paramOnSharedPreferenceChangeListener);
            return;
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener) {
        synchronized (this.mLock) {
            this.mListeners.remove(paramOnSharedPreferenceChangeListener);
            return;
        }
    }

    public class EditorImpl
            implements SharedPreferencesExt.Editor {
        private final Object mEditorLock = new Object();
        private boolean mClear;
        private Map<String, Object> mModified = new HashMap();

        public EditorImpl() {
        }

        private boolean doCommit(boolean paramBoolean) {
//            Object localObject3;
//            synchronized (SharedPreferencesProto.this.mLock) {
//                localObject3 = SharedPreferencesProto.this.mMap;
//                if ((SharedPreferencesProto.this.mWriteData != null) && (SharedPreferencesProto.this.mWriteData.mWrittenMap == localObject3)) {
//                    localObject3 = new HashMap((Map) localObject3);
//                }
//            }
//            synchronized (this.mEditorLock) {
//                Object localObject7;
//                if (this.mClear) {
//                    Maps.filterValues(this.mModified, Predicates.equalTo(SharedPreferencesProto.this.mRemoveMarker)).clear();
//                    if ((((Map) localObject3).isEmpty()) && (this.mModified.isEmpty())) {
//                        boolean bool5 = SharedPreferencesProto.this.mLoaded;
//                        i = 0;
//                        localObject7 = null;
//                        if (bool5) {
//                        }
//                    } else {
//                        i = 1;
//                        localHashSet = new HashSet(this.mModified.keySet());
//                    }
//                }
//                SharedPreferencesProto.WriteData localWriteData;
//                boolean bool2;
//                try {
//                    localObject3 = this.mModified;
//                    this.mModified = new HashMap();
//                    SharedPreferencesProto.access$702(SharedPreferencesProto.this, true);
//                    this.mEditorLock.notifyAll();
//                    localObject7 = localHashSet;
//                    this.mClear = false;
//                    ArrayList localArrayList = null;
//                    if (localObject7 != null) {
//                        boolean bool3 = localObject7.isEmpty();
//                        localArrayList = null;
//                        if (!bool3) {
//                            boolean bool4 = SharedPreferencesProto.this.mListeners.isEmpty();
//                            localArrayList = null;
//                            if (!bool4) {
//                                localArrayList = new ArrayList(SharedPreferencesProto.this.mListeners);
//                            }
//                        }
//                    }
//                    j = 0;
//                    if (i != 0) {
//                        if (SharedPreferencesProto.this.mWriteData == null) {
//                            j = 1;
//                            if ((SharedPreferencesProto.this.mWriteData == null) || (SharedPreferencesProto.this.mWriteData.mWrittenMap == SharedPreferencesProto.this.mMap)) {
//                                SharedPreferencesProto.access$602(SharedPreferencesProto.this, new SharedPreferencesProto.WriteData(SharedPreferencesProto.this, null));
//                            }
//                            SharedPreferencesProto.access$502(SharedPreferencesProto.this, (Map) localObject3);
//                        }
//                    } else {
//                        localWriteData = null;
//                        if (paramBoolean) {
//                            localWriteData = SharedPreferencesProto.this.mWriteData;
//                            if (SharedPreferencesProto.this.mWriteDelayCounter != 0) {
//                                Log.w("Search.SharedPreferencesProto", "potential deadlock: commit while delayWrites", new Throwable());
//                            }
//                        }
//                        if (j != 0) {
//                            Thread local1 = new Thread("Search.SharedPreferencesProto") {
//                                public void run() {
//                                    SharedPreferencesProto.this.writeToFile();
//                                }
//                            };
//                            local1.start();
//                        }
//                        if (localArrayList != null) {
//                            SharedPreferencesProto.this.notifyListeners(localArrayList, localObject7);
//                        }
//                        bool2 = true;
//                        if (localWriteData == null) {
//                        }
//                    }
//                } finally {
//                }
//                try {
//                    localWriteData.mWaitLatch.await();
//                    bool2 = localWriteData.mWriteResult;
//                    return bool2;
//                } catch (InterruptedException localInterruptedException) {
//                    boolean bool1;
//                    Thread.currentThread().interrupt();
//                    return false;
//                }
//                HashSet localHashSet = new HashSet();
//                Iterator localIterator = this.mModified.entrySet().iterator();
//                while (localIterator.hasNext()) {
//                    Map.Entry localEntry = (Map.Entry) localIterator.next();
//                    String str = (String) localEntry.getKey();
//                    Object localObject8 = localEntry.getValue();
//                    Object localObject9 = ((Map) localObject3).get(str);
//                    if ((SharedPreferencesProto.this.mLoaded) && (localObject8 == SharedPreferencesProto.this.mRemoveMarker)) {
//                        if (((Map) localObject3).containsKey(str)) {
//                            ((Map) localObject3).remove(str);
//                            localHashSet.add(str);
//                            continue;
//                            throw localObject5;
//                            localObject2 =finally;
//                            throw localObject2;
//                        }
//                    } else if ((!((Map) localObject3).containsKey(str)) || ((localObject8 == null) && (((Map) localObject3).get(str) != null)) || ((localObject8 != null) && (!localObject8.equals(localObject9)))) {
//                        ((Map) localObject3).put(str, localObject8);
//                        localHashSet.add(str);
//                    }
//                }
//                this.mModified.clear();
//                bool1 = localHashSet.isEmpty();
//                if (!bool1) {
//                }
//                for (int i = 1; ; i = 0) {
//                    localObject7 = localHashSet;
//                    break;
//                }
//                int j = 0;
//            }
            return false;
        }

        private void doPut(String paramString, Object paramObject) {
            synchronized (this.mEditorLock) {
                this.mModified.put(paramString, paramObject);
                return;
            }
        }

        public void apply() {
            doCommit(false);
        }

        public SharedPreferencesExt.Editor clear() {
            synchronized (this.mEditorLock) {
                this.mClear = true;
                return this;
            }
        }

        public boolean commit() {
            return doCommit(true);
        }

        public SharedPreferencesExt.Editor putBoolean(String paramString, boolean paramBoolean) {
            doPut(paramString, Boolean.valueOf(paramBoolean));
            return this;
        }

        public SharedPreferencesExt.Editor putBytes(String paramString, byte[] paramArrayOfByte) {
            if (paramArrayOfByte != null) {
                doPut(paramString, ByteStringMicro.copyFrom(paramArrayOfByte));
            }
            return this;
        }

        public SharedPreferencesExt.Editor putFloat(String paramString, float paramFloat) {
            doPut(paramString, Float.valueOf(paramFloat));
            return this;
        }

        public SharedPreferencesExt.Editor putInt(String paramString, int paramInt) {
            doPut(paramString, Integer.valueOf(paramInt));
            return this;
        }

        public SharedPreferencesExt.Editor putLong(String paramString, long paramLong) {
            doPut(paramString, Long.valueOf(paramLong));
            return this;
        }

        public SharedPreferencesExt.Editor putString(String paramString1, String paramString2) {
            doPut(paramString1, paramString2);
            return this;
        }

        public SharedPreferencesExt.Editor putStringSet(String paramString, Set<String> paramSet) {
            if (paramSet == null) {
            }
            for (Object localObject = null; ; localObject = new HashSet(paramSet)) {
                doPut(paramString, localObject);
                return this;
            }
        }

        public SharedPreferencesExt.Editor remove(String paramString) {
            doPut(paramString, SharedPreferencesProto.this.mRemoveMarker);
            return this;
        }
    }

    private class WriteData {
        public final CountDownLatch mWaitLatch = new CountDownLatch(1);
        public boolean mWriteResult;
        public Map<String, Object> mWrittenMap;

        private WriteData() {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.search.core.preferences.SharedPreferencesProto

 * JD-Core Version:    0.7.0.1

 */