package com.google.android.search.core;

import android.content.SharedPreferences;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

public enum Feature
{
  private boolean mEnabled;
  private final boolean mEnabledByDefault;
  
  static
  {
    CONTACT_LOOKUP_BY_RELATIONSHIP_NAME = new Feature("CONTACT_LOOKUP_BY_RELATIONSHIP_NAME", 5, false);
    DETECT_BT_DEVICE_AS_CAR = new Feature("DETECT_BT_DEVICE_AS_CAR", 6, false);
    DISCOURSE_CONTEXT_CONTACTS = new Feature("DISCOURSE_CONTEXT_CONTACTS", 7, false);
    DISCOURSE_CONTEXT = new Feature("DISCOURSE_CONTEXT", 8, true);
    EDIT_MESSAGE_TEXT = new Feature("EDIT_MESSAGE_TEXT", 9, false);
    EXTENSIVE_ICING_LOGGING = new Feature("EXTENSIVE_ICING_LOGGING", 10, false);
    EYES_FREE = new Feature("EYES_FREE", 11, false);
    FOLLOW_ON = new Feature("FOLLOW_ON", 12, true);
    HOTWORD_ON_SRP_UI = new Feature("HOTWORD_ON_SRP_UI", 13, true);
    HOTWORD_WHEN_CHARGING = new Feature("HOTWORD_WHEN_CHARGING", 14, false);
    ICING_CONTACT_LOOKUP = new Feature("ICING_CONTACT_LOOKUP", 15, true);
    INCREMENTAL_LANGUAGE_PACK_UPDATES = new Feature("INCREMENTAL_LANGUAGE_PACK_UPDATES", 16, false);
    LANGUAGE_PACK_AUTO_DOWNLOAD = new Feature("LANGUAGE_PACK_AUTO_DOWNLOAD", 17, true);
    LOG_CONTACT_DATA = new Feature("LOG_CONTACT_DATA", 18, true);
    PRERENDER_IN_CHROME = new Feature("PRERENDER_IN_CHROME", 19, false);
    REMINDERS_LEAVING_TRIGGER = new Feature("REMINDERS_LEAVING_TRIGGER", 20, false);
    REMINDERS_WEEKEND = new Feature("REMINDERS_WEEKEND", 21, false);
    SEARCH_HISTORY_IN_APP = new Feature("SEARCH_HISTORY_IN_APP", 22, false);
    SEARCH_ON_WAVE_GESTURE = new Feature("SEARCH_ON_WAVE_GESTURE", 23, false);
    SHOW_LOGGING_TOASTS = new Feature("SHOW_LOGGING_TOASTS", 24, false);
    TAG_N_BEST_HYPOTHESES = new Feature("TAG_N_BEST_HYPOTHESES", 25, false);
    TEST_FEATURE = new Feature("TEST_FEATURE", 26, false);
    Feature[] arrayOfFeature = new Feature[27];
    arrayOfFeature[0] = CALL_NO_COUNTDOWN;
    arrayOfFeature[1] = CAR_SMS_NOTIFICATIONS;
    arrayOfFeature[2] = CONNECTION_ERROR_CARDS;
    arrayOfFeature[3] = CONTACT_REFERENCE;
    arrayOfFeature[4] = CONTACT_REFERENCE_MERGE_DETAILS;
    arrayOfFeature[5] = CONTACT_LOOKUP_BY_RELATIONSHIP_NAME;
    arrayOfFeature[6] = DETECT_BT_DEVICE_AS_CAR;
    arrayOfFeature[7] = DISCOURSE_CONTEXT_CONTACTS;
    arrayOfFeature[8] = DISCOURSE_CONTEXT;
    arrayOfFeature[9] = EDIT_MESSAGE_TEXT;
    arrayOfFeature[10] = EXTENSIVE_ICING_LOGGING;
    arrayOfFeature[11] = EYES_FREE;
    arrayOfFeature[12] = FOLLOW_ON;
    arrayOfFeature[13] = HOTWORD_ON_SRP_UI;
    arrayOfFeature[14] = HOTWORD_WHEN_CHARGING;
    arrayOfFeature[15] = ICING_CONTACT_LOOKUP;
    arrayOfFeature[16] = INCREMENTAL_LANGUAGE_PACK_UPDATES;
    arrayOfFeature[17] = LANGUAGE_PACK_AUTO_DOWNLOAD;
    arrayOfFeature[18] = LOG_CONTACT_DATA;
    arrayOfFeature[19] = PRERENDER_IN_CHROME;
    arrayOfFeature[20] = REMINDERS_LEAVING_TRIGGER;
    arrayOfFeature[21] = REMINDERS_WEEKEND;
    arrayOfFeature[22] = SEARCH_HISTORY_IN_APP;
    arrayOfFeature[23] = SEARCH_ON_WAVE_GESTURE;
    arrayOfFeature[24] = SHOW_LOGGING_TOASTS;
    arrayOfFeature[25] = TAG_N_BEST_HYPOTHESES;
    arrayOfFeature[26] = TEST_FEATURE;
    $VALUES = arrayOfFeature;
  }
  
  private Feature(boolean paramBoolean)
  {
    this.mEnabledByDefault = paramBoolean;
    this.mEnabled = paramBoolean;
  }
  
  public static Set<Feature> configurableFeatures()
  {
    HashSet localHashSet = Sets.newHashSet();
    for (Feature localFeature : values()) {
      if ((!localFeature.mEnabledByDefault) && (localFeature != TEST_FEATURE)) {
        localHashSet.add(localFeature);
      }
    }
    return localHashSet;
  }
  
  static void resetAllForTestInternal()
  {
    for (Feature localFeature : ) {
      localFeature.mEnabled = localFeature.mEnabledByDefault;
    }
  }
  
  public static void setFeatures(SharedPreferences paramSharedPreferences)
  {
    Preconditions.checkState(false);
    Set localSet = paramSharedPreferences.getStringSet("enabled_features", ImmutableSet.of());
    Feature[] arrayOfFeature = values();
    int i = arrayOfFeature.length;
    int j = 0;
    if (j < i)
    {
      Feature localFeature = arrayOfFeature[j];
      if ((localFeature.mEnabledByDefault) || (localSet.contains(localFeature.name()))) {}
      for (boolean bool = true;; bool = false)
      {
        localFeature.mEnabled = bool;
        j++;
        break;
      }
    }
  }
  
  static void setFeaturesForTest(boolean paramBoolean, Feature... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      paramVarArgs[j].mEnabled = paramBoolean;
    }
  }
  
  public boolean isEnabled()
  {
    return this.mEnabled;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("Feature[").append(name()).append(" : ");
    String str1;
    StringBuilder localStringBuilder2;
    if (this.mEnabled)
    {
      str1 = "ENABLED";
      localStringBuilder2 = localStringBuilder1.append(str1);
      if (this.mEnabled != this.mEnabledByDefault) {
        break label77;
      }
    }
    label77:
    for (String str2 = " (DEFAULT)";; str2 = "")
    {
      return str2 + "]";
      str1 = "DISABLED";
      break;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.Feature
 * JD-Core Version:    0.7.0.1
 */