package com.google.android.e100;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.speech.contacts.ContactLookup;
import com.google.android.speech.contacts.Person;
import java.util.List;

public class MessageUtil
{
  public static String getAnnouncementStringForSender(String paramString, ContactLookup paramContactLookup, Context paramContext)
  {
    List localList = paramContactLookup.findAllByPhoneNumber(paramString);
    StringBuilder localStringBuilder;
    if (localList.isEmpty())
    {
      localStringBuilder = new StringBuilder();
      for (int i = 0; i < paramString.length(); i++)
      {
        localStringBuilder.append(paramString.substring(i, i + 1));
        localStringBuilder.append(" ");
      }
    }
    for (String str = localStringBuilder.toString().trim();; str = ((Person)localList.get(0)).getName()) {
      return String.format(paramContext.getResources().getString(2131363680), new Object[] { str });
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.e100.MessageUtil
 * JD-Core Version:    0.7.0.1
 */