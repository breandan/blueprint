package com.google.android.sidekick.shared.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.sidekick.shared.remoteapi.ProtoParcelable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import com.google.geo.sidekick.Sidekick.Action;
import com.google.geo.sidekick.Sidekick.ClickAction;
import com.google.geo.sidekick.Sidekick.ClickAction.Extra;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import com.google.geo.sidekick.Sidekick.GenericCardEntry;
import com.google.geo.sidekick.Sidekick.Photo;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class ProtoUtils
{
  @Nullable
  public static Sidekick.Action findAction(Sidekick.Entry paramEntry, int paramInt, int... paramVarArgs)
  {
    Iterator localIterator = paramEntry.getEntryActionList().iterator();
    label81:
    while (localIterator.hasNext())
    {
      Sidekick.Action localAction = (Sidekick.Action)localIterator.next();
      if (localAction.getType() == paramInt) {
        return localAction;
      }
      if (paramVarArgs.length != 0)
      {
        int i = paramVarArgs.length;
        for (int j = 0;; j++)
        {
          if (j >= i) {
            break label81;
          }
          int k = paramVarArgs[j];
          if (localAction.getType() == k) {
            break;
          }
        }
      }
    }
    return null;
  }
  
  public static Sidekick.Action getActionFromByteArray(byte[] paramArrayOfByte)
  {
    return (Sidekick.Action)getFromByteArray(new Sidekick.Action(), paramArrayOfByte);
  }
  
  @Nullable
  public static Collection<Sidekick.Entry> getEntriesFromIntent(Intent paramIntent, String paramString)
  {
    ArrayList localArrayList1 = paramIntent.getParcelableArrayListExtra(paramString);
    if (localArrayList1 == null) {
      return null;
    }
    ArrayList localArrayList2 = Lists.newArrayListWithCapacity(localArrayList1.size());
    Iterator localIterator = localArrayList1.iterator();
    while (localIterator.hasNext())
    {
      Parcelable localParcelable = (Parcelable)localIterator.next();
      if (!(localParcelable instanceof ProtoParcelable))
      {
        Log.w("ProtoUtils", "Invalid parcelable in " + paramString);
      }
      else
      {
        ProtoParcelable localProtoParcelable = (ProtoParcelable)localParcelable;
        try
        {
          localArrayList2.add(localProtoParcelable.getProto(Sidekick.Entry.class));
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          Log.w("ProtoUtils", "Invalid entry in " + paramString);
        }
      }
    }
    if (localArrayList2.size() > 0) {}
    for (;;)
    {
      return localArrayList2;
      localArrayList2 = null;
    }
  }
  
  public static Sidekick.Entry getEntryFromByteArray(byte[] paramArrayOfByte)
  {
    return (Sidekick.Entry)getFromByteArray(new Sidekick.Entry(), paramArrayOfByte);
  }
  
  public static Sidekick.Entry getEntryFromIntent(Intent paramIntent, String paramString)
  {
    return getEntryFromByteArray(paramIntent.getByteArrayExtra(paramString));
  }
  
  public static long getEntryHash(Sidekick.Entry paramEntry)
  {
    try
    {
      long l = Longs.fromByteArray(MessageDigest.getInstance("MD5").digest(paramEntry.toByteArray()));
      return l;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.w("ProtoUtils", "MD5 not available for entry proto hashes, using array hash code");
    }
    return new ProtoKey(paramEntry).hashCode();
  }
  
  public static Sidekick.EntryTreeNode getEntryTreeNodeFromIntent(Intent paramIntent, String paramString)
  {
    byte[] arrayOfByte = paramIntent.getByteArrayExtra(paramString);
    return (Sidekick.EntryTreeNode)getFromByteArray(new Sidekick.EntryTreeNode(), arrayOfByte);
  }
  
  public static <T extends MessageMicro> T getFromByteArray(T paramT, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {
      try
      {
        paramT.mergeFrom(paramArrayOfByte);
        return paramT;
      }
      catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
      {
        Log.w("ProtoUtils", "Error in parsing proto buffer", localInvalidProtocolBufferMicroException);
      }
    }
    return null;
  }
  
  @Nullable
  public static String getGenericEntryType(@Nullable Sidekick.Entry paramEntry)
  {
    if ((paramEntry != null) && (paramEntry.hasGenericCardEntry()) && (paramEntry.getGenericCardEntry().hasCardType()))
    {
      String str = paramEntry.getGenericCardEntry().getCardType();
      if (!TextUtils.isEmpty(str)) {
        return str;
      }
    }
    return null;
  }
  
  @Nullable
  public static String getPhotoUrl(Sidekick.Photo paramPhoto)
  {
    String str;
    if (!paramPhoto.hasUrl()) {
      str = null;
    }
    do
    {
      return str;
      str = paramPhoto.getUrl();
    } while (!str.startsWith("//"));
    return "https:" + str;
  }
  
  public static <T extends MessageMicro> T getProtoExtra(Bundle paramBundle, String paramString, Class<T> paramClass)
  {
    try
    {
      byte[] arrayOfByte = paramBundle.getByteArray(paramString);
      MessageMicro localMessageMicro = getFromByteArray((MessageMicro)paramClass.newInstance(), arrayOfByte);
      return localMessageMicro;
    }
    catch (InstantiationException localInstantiationException)
    {
      Log.e("ProtoUtils", "Error instantiating proto: " + paramClass.getName(), localInstantiationException);
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.e("ProtoUtils", "Error instantiating proto: " + paramClass.getName(), localIllegalAccessException);
      }
    }
  }
  
  public static Sidekick.ClickAction getViewActionFromIntent(Intent paramIntent)
  {
    Sidekick.ClickAction localClickAction = new Sidekick.ClickAction();
    if (paramIntent.getAction() != null) {
      localClickAction.setAction(paramIntent.getAction());
    }
    if (paramIntent.getData() != null) {
      localClickAction.setUri(paramIntent.getDataString());
    }
    Bundle localBundle = paramIntent.getExtras();
    if (localBundle != null)
    {
      Iterator localIterator = localBundle.keySet().iterator();
      if (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        Object localObject = localBundle.get(str);
        Sidekick.ClickAction.Extra localExtra = new Sidekick.ClickAction.Extra();
        localExtra.setKey(str);
        if (localObject != null)
        {
          if (!(localObject instanceof Boolean)) {
            break label141;
          }
          localExtra.setBoolValue(((Boolean)localObject).booleanValue());
        }
        for (;;)
        {
          localClickAction.addExtra(localExtra);
          break;
          label141:
          if ((localObject instanceof Long)) {
            localExtra.setLongValue(((Number)localObject).longValue());
          } else {
            localExtra.setStringValue(localObject.toString());
          }
        }
      }
    }
    return localClickAction;
  }
  
  public static <T extends MessageMicro> List<T> protosFromStringSet(Class<T> paramClass, Set<String> paramSet)
    throws InstantiationException, IllegalAccessException, InvalidProtocolBufferMicroException
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramSet.size());
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      byte[] arrayOfByte = Base64.decode((String)localIterator.next(), 3);
      localArrayList.add(((MessageMicro)paramClass.newInstance()).mergeFrom(arrayOfByte));
    }
    return localArrayList;
  }
  
  public static void putEntriesInIntent(Intent paramIntent, String paramString, Collection<Sidekick.Entry> paramCollection)
  {
    Preconditions.checkNotNull(paramCollection);
    if (!paramCollection.isEmpty()) {}
    ArrayList localArrayList;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      localArrayList = Lists.newArrayListWithCapacity(paramCollection.size());
      Iterator localIterator = paramCollection.iterator();
      while (localIterator.hasNext()) {
        localArrayList.add(ProtoParcelable.create((Sidekick.Entry)localIterator.next()));
      }
    }
    paramIntent.putParcelableArrayListExtra(paramString, localArrayList);
  }
  
  public static void putProtoExtra(Intent paramIntent, String paramString, @Nullable MessageMicro paramMessageMicro)
  {
    if (paramMessageMicro != null) {
      paramIntent.putExtra(paramString, paramMessageMicro.toByteArray());
    }
  }
  
  public static void removeAction(Sidekick.Entry paramEntry, int paramInt)
  {
    Iterator localIterator = paramEntry.getEntryActionList().iterator();
    while (localIterator.hasNext()) {
      if (((Sidekick.Action)localIterator.next()).getType() == paramInt) {
        localIterator.remove();
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.util.ProtoUtils
 * JD-Core Version:    0.7.0.1
 */