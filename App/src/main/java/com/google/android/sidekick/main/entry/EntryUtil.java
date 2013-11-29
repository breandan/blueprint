package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.util.ProtoKey;
import com.google.geo.sidekick.Sidekick.Entry;

public class EntryUtil
{
  public static boolean isSameEntry(ProtoKey<Sidekick.Entry> paramProtoKey1, ProtoKey<Sidekick.Entry> paramProtoKey2)
  {
    Sidekick.Entry localEntry1 = (Sidekick.Entry)paramProtoKey1.getProto();
    Sidekick.Entry localEntry2 = (Sidekick.Entry)paramProtoKey2.getProto();
    if (localEntry1 == localEntry2) {}
    do
    {
      return true;
      if (localEntry1.getType() != localEntry2.getType()) {
        return false;
      }
      if ((!localEntry1.hasEntryUpdateId()) || (!localEntry2.hasEntryUpdateId())) {
        break;
      }
    } while (localEntry1.getEntryUpdateId() == localEntry2.getEntryUpdateId());
    return false;
    return paramProtoKey1.equals(paramProtoKey2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.EntryUtil
 * JD-Core Version:    0.7.0.1
 */