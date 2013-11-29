package com.google.android.search.core;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.google.android.gsf.Gservices;
import com.google.android.search.core.debug.DumpUtils;
import com.google.android.search.core.preferences.SharedPreferencesExt;
import com.google.android.search.core.summons.Source;
import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class SearchConfig
{
  public static final Uri RLZ_PROVIDER_URI = Uri.parse("content://com.google.android.partnersetup.rlzappprovider/");
  private final SparseArray<Map<String, String>> mCachedStringMaps;
  private final SparseArray<Set<String>> mCachedStringSets;
  private final Context mContext;
  private String mDeviceCountryOverride;
  private ImmutableMap<String, String> mExtraQueryParams;
  private final SparseArray<String> mGsResourceKeys;
  private final GsaPreferenceController mPrefController;
  private volatile SparseArray<Object> mResourceOverrides;
  
  public SearchConfig()
  {
    this.mContext = null;
    this.mPrefController = null;
    this.mGsResourceKeys = null;
    this.mCachedStringSets = null;
    this.mCachedStringMaps = null;
  }
  
  public SearchConfig(Context paramContext, GsaPreferenceController paramGsaPreferenceController)
  {
    this.mContext = paramContext;
    this.mPrefController = paramGsaPreferenceController;
    this.mGsResourceKeys = new SparseArray(256);
    this.mCachedStringSets = new SparseArray(5);
    this.mCachedStringMaps = new SparseArray(5);
    fillGservicesResourceKeys();
    if (this.mGsResourceKeys.size() <= 256) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      return;
    }
  }
  
  private static Boolean convertBooleanOverride(String paramString)
  {
    if (Gservices.TRUE_PATTERN.matcher(paramString).matches()) {
      return Boolean.TRUE;
    }
    if (Gservices.FALSE_PATTERN.matcher(paramString).matches()) {
      return Boolean.FALSE;
    }
    Log.w("Search.SearchConfig", "Invalid gservices boolean");
    return null;
  }
  
  private static Integer convertIntegerOverride(String paramString)
  {
    try
    {
      Integer localInteger = Integer.valueOf(paramString);
      return localInteger;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.w("Search.SearchConfig", "Invalid gservices int");
    }
    return null;
  }
  
  private static String[] convertStringArrayOverride(String paramString, boolean paramBoolean)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramBoolean) {
        return Util.jsonToStringArray(paramString);
      }
      return TextUtils.split(paramString, ",");
    }
    return null;
  }
  
  private void fillGservicesResourceKeys()
  {
    putResourceKey(2131755022, "allow_ssl_search");
    putResourceKey(2131492893, "background_task_min_periods");
    putResourceKey(2131427389, "background_tasks_period_mins");
    putResourceKey(2131427392, "background_tasks_max_period_days");
    putResourceKey(2131427390, "background_tasks_period_days_of_disuse_squared_multiple_hours");
    putResourceKey(2131427391, "background_tasks_period_days_of_disuse_before_backoff");
    putResourceKey(2131361982, "complete_server_domain_name");
    putResourceKey(2131361983, "complete_server_suggest_path");
    putResourceKey(2131361984, "complete_server_remove_history_path");
    putResourceKey(2131361985, "complete_server_history_refresh_param_name");
    putResourceKey(2131361986, "complete_server_history_refresh_param_value");
    putResourceKey(2131361987, "suggestion_pellet_path");
    putResourceKey(2131492880, "default_source_uris");
    putResourceKey(2131492882, "default_sources");
    putResourceKey(2131492883, "google_search_paths");
    putResourceKey(2131492884, "google_search_logout_redirects");
    putResourceKey(2131492885, "google_utility_paths");
    putResourceKey(2131755071, "native_image_downloads_enabled");
    putResourceKey(2131492890, "full_size_icon_source_suggest_uris");
    putResourceKey(2131427366, "deleted_query_propagation_delay_ms");
    putResourceKey(2131427365, "concurrent_source_queries");
    putResourceKey(2131427367, "source_timeout_millis");
    putResourceKey(2131427371, "max_concurrent_source_queries");
    putResourceKey(2131427373, "max_displayed_summons_in_results_suggest");
    putResourceKey(2131427374, "max_promoted_summons_per_source_initial");
    putResourceKey(2131427375, "max_promoted_summons_per_source_increase");
    putResourceKey(2131427376, "min_web_suggestions");
    putResourceKey(2131427377, "max_web_suggestions");
    putResourceKey(2131427378, "max_total_suggestions");
    putResourceKey(2131427379, "max_results_per_source");
    putResourceKey(2131427380, "max_stat_age_hours");
    putResourceKey(2131427381, "min_clicks_for_source_ranking");
    putResourceKey(2131427382, "new_concurrent_source_query_delay");
    putResourceKey(2131427383, "publish_result_delay_millis");
    putResourceKey(2131427384, "typing_update_suggestions_delay_millis");
    putResourceKey(2131361988, "complete_server_client_id");
    putResourceKey(2131361989, "complete_server_client_external_id");
    putResourceKey(2131492878, "complete_server_extra_params");
    putResourceKey(2131361990, "search_domain_check_pattern");
    putResourceKey(2131361997, "google_suggestions_base_pattern");
    putResourceKey(2131361999, "tos_url_format");
    putResourceKey(2131362035, "default_search_source_param");
    putResourceKey(2131362006, "search_domain_override");
    putResourceKey(2131361992, "text_search_tokentype");
    putResourceKey(2131361991, "voice_search_tokentype");
    putResourceKey(2131361993, "search_history_tokentype");
    putResourceKey(2131361994, "suggestion_tokentype");
    putResourceKey(2131361995, "suggestion_auth_header_prefix");
    putResourceKey(2131427387, "auth_token_cache_ttl");
    putResourceKey(2131362007, "service_personalized_search");
    putResourceKey(2131427388, "refresh_search_parameters_cookie_refresh_days");
    putResourceKey(2131427395, "http_cache_size");
    putResourceKey(2131755026, "suggest_look_ahead_enabled");
    putResourceKey(2131427368, "suggestion_cache_timeout_ms");
    putResourceKey(2131427369, "suggestion_cache_max_values");
    putResourceKey(2131755021, "combine_suggest_and_prefetch");
    putResourceKey(2131362016, "user_agent_param_format");
    putResourceKey(2131427396, "location_expirey_time");
    putResourceKey(2131755027, "word_by_word_enabled");
    putResourceKey(2131427430, "mariner_background_refresh_interval_minutes");
    putResourceKey(2131427431, "mariner_idle_background_refresh_interval_minutes");
    putResourceKey(2131492897, "clicked_result_destination_params");
    putResourceKey(2131362012, "clicked_ad_url_path");
    putResourceKey(2131362011, "clicked_result_url_path");
    putResourceKey(2131492895, "click_ad_url_exception_patterns");
    putResourceKey(2131492896, "click_ad_url_substitutions");
    putResourceKey(2131362013, "corpora_config_uri_24_plus");
    putResourceKey(2131427394, "max_initial_web_corpus_selectors");
    putResourceKey(2131362008, "register_gsa_bridge_javascript");
    putResourceKey(2131427393, "suggestion_view_recycle_bin_size");
    putResourceKey(2131427436, "suggest_num_visible_summons_rows");
    putResourceKey(2131362010, "velvetgsabridge_interface_name");
    putResourceKey(2131362014, "web_corpus_query_param");
    putResourceKey(2131492891, "domain_whitelist");
    putResourceKey(2131427385, "saved_configuration_expiry_seconds");
    putResourceKey(2131427386, "saved_whitelisted_configuration_expiry_seconds");
    putResourceKey(2131427398, "webview_login_load_timeout_ms");
    putResourceKey(2131427399, "webview_login_redirect_timeout_ms");
    putResourceKey(2131427400, "webview_suppress_previous_results_for_ms");
    putResourceKey(2131492898, "gws_cgi_param_query_equal_whitelist");
    putResourceKey(2131492899, "gws_cgi_param_changes_for_back_navigation");
    putResourceKey(2131492900, "gws_path_whitelist_for_back_navigation");
    putResourceKey(2131755028, "correction_spans_enabled");
    putResourceKey(2131427401, "prefetch_ttl_millis");
    putResourceKey(2131427402, "prefetch_cache_entries");
    putResourceKey(2131427403, "prefetch_simultaneous_downloads");
    putResourceKey(2131427404, "prefetch_throttle_period_millis");
    putResourceKey(2131362015, "google_gen_204_pattern");
    putResourceKey(2131755029, "personalized_search_enabled");
    putResourceKey(2131362017, "device_country");
    putResourceKey(2131362018, "gms_disable:com.google.android.ears");
    putResourceKey(2131427405, "max_gws_response_size_bytes");
    putResourceKey(2131362019, "s3_service_voice_actions_override");
    putResourceKey(2131362020, "s3_server_override");
    putResourceKey(2131755030, "enable_spdy");
    putResourceKey(2131427406, "intercepted_request_header_timeout_ms");
    putResourceKey(2131362021, "history_api_lookup_url_pattern");
    putResourceKey(2131362022, "history_api_change_url_pattern");
    putResourceKey(2131362023, "history_api_client_param");
    putResourceKey(2131755031, "action_discovery_enabled");
    putResourceKey(2131427397, "music_detection_timeout_ms");
    putResourceKey(2131755034, "test_platform_logging_enabled");
    putResourceKey(2131755035, "debug_audio_logging_enabled");
    putResourceKey(2131362024, "action_discovery_data_uri");
    putResourceKey(2131755037, "enable_sdch_for_serp");
    putResourceKey(2131755038, "enable_chrome_prerender");
    putResourceKey(2131362025, "extra_query_params");
    putResourceKey(2131492902, "action_discovery_supported_locales");
    putResourceKey(2131427423, "action_discovery_peek_delay_millis");
    putResourceKey(2131427424, "action_discovery_max_instant_peek_count");
    putResourceKey(2131755039, "hotword_from_results");
    putResourceKey(2131755040, "hotword_from_launcher");
    putResourceKey(2131755042, "embedded_parser_enabled");
    putResourceKey(2131427425, "abnf_compiler_num_contacts");
    putResourceKey(2131755043, "content_provider_global_search_enabled");
    putResourceKey(2131755044, "spdy_for_suggestions_enabled");
    putResourceKey(2131755045, "spdy_for_search_result_fetches_enabled");
    putResourceKey(2131755041, "hotword_enabled");
    putResourceKey(2131755032, "bluetooth_enabled");
    putResourceKey(2131362026, "client_experiments_header");
    putResourceKey(2131362027, "client_experiments_param");
    putResourceKey(2131362028, "gservices_experiment_ids");
    putResourceKey(2131755046, "ssl_session_cache_enabled");
    putResourceKey(2131427432, "predictive_idle_user_threshold_minutes");
    putResourceKey(2131427434, "tv_detection_timeout_millis");
    putResourceKey(2131427435, "service_timeout_for_tv_detection_millis");
    putResourceKey(2131492903, "tv_search_query_by_locale");
    putResourceKey(2131427429, "google_analytics_sample_rate_1e3");
    putResourceKey(2131362009, "remote_debug_javascript");
    putResourceKey(2131755048, "gcore_ulr_burst_mode");
    putResourceKey(2131427426, "hide_hotword_hint_after_successes");
    putResourceKey(2131427427, "remember_successful_hotword_usage_for_minutes");
    putResourceKey(2131427428, "show_hotword_hint_for_days");
    putResourceKey(2131755049, "send_query_location");
    putResourceKey(2131755050, "hide_dogfood_indicator");
    putResourceKey(2131427413, "icing_should_log_query_global_ppm");
    putResourceKey(2131755024, "icing_sources_enabled");
    putResourceKey(2131492887, "ignored_icing_source_packages");
    putResourceKey(2131755025, "internal_icing_corpora_enabled");
    putResourceKey(2131492888, "disabled_internal_icing_corpora");
    putResourceKey(2131427407, "icing_apps_corpus_update_all_interval_millis");
    putResourceKey(2131427408, "icing_contacts_corpus_update_all_interval_without_delta_millis");
    putResourceKey(2131427409, "icing_contacts_corpus_update_all_interval_with_delta_millis");
    putResourceKey(2131427410, "icing_contacts_provider_resync_initial_poll_delay_millis");
    putResourceKey(2131427411, "icing_contacts_provider_resync_repoll_period_millis");
    putResourceKey(2131427412, "icing_contacts_provider_resync_max_repoll_attempts");
    putResourceKey(2131427414, "icing_service_app_launch_broadcast_delay_millis");
    putResourceKey(2131427415, "icing_app_launch_full_scores_update_period_millis");
    putResourceKey(2131755033, "icing_app_launch_broadcast_handling_enabled");
    putResourceKey(2131427416, "icing_number_requested_results_in_mixed_suggest");
    putResourceKey(2131427417, "icing_number_requested_results_in_grouped_mode");
    putResourceKey(2131427418, "icing_app_launch_value_halftime_days");
    putResourceKey(2131427420, "icing_new_app_score_percent_of_max");
    putResourceKey(2131427422, "icing_app_launch_log_trust_constant");
    putResourceKey(2131427421, "icing_launch_log_max_age_days");
    putResourceKey(2131492901, "icing_query_corpus_weight_by_canonical_name");
  }
  
  private boolean getBoolean(int paramInt)
  {
    SparseArray localSparseArray = this.mResourceOverrides;
    if (localSparseArray != null)
    {
      Object localObject = localSparseArray.get(paramInt);
      if (localObject != null)
      {
        if ((localObject instanceof Boolean)) {
          return ((Boolean)localObject).booleanValue();
        }
        localSparseArray.put(paramInt, convertBooleanOverride((String)localObject));
        return getBoolean(paramInt);
      }
    }
    else if (this.mGsResourceKeys.get(paramInt) != null)
    {
      loadOverrides();
      return getBoolean(paramInt);
    }
    return this.mContext.getResources().getBoolean(paramInt);
  }
  
  static String[] getGservicesExtraOverrideKeys()
  {
    return new String[] { "device_country", "gms_disable:com.google.android.ears" };
  }
  
  private String getString(int paramInt)
  {
    SparseArray localSparseArray = this.mResourceOverrides;
    if (localSparseArray != null)
    {
      Object localObject = localSparseArray.get(paramInt);
      if (localObject != null) {
        return (String)localObject;
      }
    }
    else if (this.mGsResourceKeys.get(paramInt) != null)
    {
      loadOverrides();
      return getString(paramInt);
    }
    return this.mContext.getResources().getString(paramInt);
  }
  
  private String[] getStringArray(int paramInt, boolean paramBoolean)
  {
    SparseArray localSparseArray = this.mResourceOverrides;
    if (localSparseArray != null)
    {
      Object localObject = localSparseArray.get(paramInt);
      if (localObject != null)
      {
        if ((localObject instanceof String[])) {
          return (String[])localObject;
        }
        localSparseArray.put(paramInt, convertStringArrayOverride((String)localObject, paramBoolean));
        return getStringArray(paramInt, paramBoolean);
      }
    }
    else if (this.mGsResourceKeys.get(paramInt) != null)
    {
      loadOverrides();
      return getStringArray(paramInt, paramBoolean);
    }
    return this.mContext.getResources().getStringArray(paramInt);
  }
  
  /* Error */
  private Map<String, String> getStringMap(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 49	com/google/android/search/core/SearchConfig:mCachedStringMaps	Landroid/util/SparseArray;
    //   6: iload_1
    //   7: invokevirtual 621	android/util/SparseArray:get	(I)Ljava/lang/Object;
    //   10: checkcast 668	java/util/Map
    //   13: astore_3
    //   14: aload_3
    //   15: ifnull +11 -> 26
    //   18: aload_3
    //   19: astore 9
    //   21: aload_0
    //   22: monitorexit
    //   23: aload 9
    //   25: areturn
    //   26: aload_0
    //   27: iload_1
    //   28: iconst_0
    //   29: invokespecial 661	com/google/android/search/core/SearchConfig:getStringArray	(IZ)[Ljava/lang/String;
    //   32: astore 4
    //   34: invokestatic 674	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
    //   37: astore 5
    //   39: aload 4
    //   41: arraylength
    //   42: iconst_2
    //   43: irem
    //   44: istore 6
    //   46: iconst_0
    //   47: istore 7
    //   49: iload 6
    //   51: ifne +6 -> 57
    //   54: iconst_1
    //   55: istore 7
    //   57: iload 7
    //   59: invokestatic 68	com/google/common/base/Preconditions:checkState	(Z)V
    //   62: iconst_0
    //   63: istore 8
    //   65: iload 8
    //   67: iconst_m1
    //   68: aload 4
    //   70: arraylength
    //   71: iadd
    //   72: if_icmpge +29 -> 101
    //   75: aload 5
    //   77: aload 4
    //   79: iload 8
    //   81: aaload
    //   82: aload 4
    //   84: iload 8
    //   86: iconst_1
    //   87: iadd
    //   88: aaload
    //   89: invokeinterface 677 3 0
    //   94: pop
    //   95: iinc 8 2
    //   98: goto -33 -> 65
    //   101: aload_0
    //   102: getfield 49	com/google/android/search/core/SearchConfig:mCachedStringMaps	Landroid/util/SparseArray;
    //   105: iload_1
    //   106: aload 5
    //   108: invokevirtual 632	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   111: aload 5
    //   113: astore 9
    //   115: goto -94 -> 21
    //   118: astore_2
    //   119: aload_0
    //   120: monitorexit
    //   121: aload_2
    //   122: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	123	0	this	SearchConfig
    //   0	123	1	paramInt	int
    //   118	4	2	localObject1	Object
    //   13	6	3	localMap	Map
    //   32	51	4	arrayOfString	String[]
    //   37	75	5	localHashMap	java.util.HashMap
    //   44	6	6	i	int
    //   47	11	7	bool	boolean
    //   63	33	8	j	int
    //   19	95	9	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	14	118	finally
    //   26	46	118	finally
    //   57	62	118	finally
    //   65	95	118	finally
    //   101	111	118	finally
  }
  
  /* Error */
  private Set<String> getStringSet(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 47	com/google/android/search/core/SearchConfig:mCachedStringSets	Landroid/util/SparseArray;
    //   6: iload_1
    //   7: invokevirtual 621	android/util/SparseArray:get	(I)Ljava/lang/Object;
    //   10: checkcast 681	java/util/Set
    //   13: astore_3
    //   14: aload_3
    //   15: ifnull +11 -> 26
    //   18: aload_3
    //   19: astore 5
    //   21: aload_0
    //   22: monitorexit
    //   23: aload 5
    //   25: areturn
    //   26: aload_0
    //   27: iload_1
    //   28: iconst_0
    //   29: invokespecial 661	com/google/android/search/core/SearchConfig:getStringArray	(IZ)[Ljava/lang/String;
    //   32: invokestatic 687	com/google/common/collect/Sets:newHashSet	([Ljava/lang/Object;)Ljava/util/HashSet;
    //   35: astore 4
    //   37: aload_0
    //   38: getfield 47	com/google/android/search/core/SearchConfig:mCachedStringSets	Landroid/util/SparseArray;
    //   41: iload_1
    //   42: aload 4
    //   44: invokevirtual 632	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   47: aload 4
    //   49: astore 5
    //   51: goto -30 -> 21
    //   54: astore_2
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_2
    //   58: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	59	0	this	SearchConfig
    //   0	59	1	paramInt	int
    //   54	4	2	localObject1	Object
    //   13	6	3	localSet	Set
    //   35	13	4	localHashSet	java.util.HashSet
    //   19	31	5	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   2	14	54	finally
    //   26	47	54	finally
  }
  
  static boolean isGmsEnabled(String paramString)
  {
    return "enabled".equals(Util.makeMapFromString(',', ':', paramString).get("0"));
  }
  
  private void loadOverrides()
  {
    for (;;)
    {
      int i;
      try
      {
        if (this.mResourceOverrides == null)
        {
          Map localMap = Util.jsonToStringMap(this.mPrefController.getMainPreferences().getString("gservices_overrides", null));
          SparseArray localSparseArray;
          if (localMap != null)
          {
            this.mDeviceCountryOverride = ((String)localMap.get("device_country"));
            localSparseArray = new SparseArray(localMap.size());
            i = 0;
            if (i < this.mGsResourceKeys.size())
            {
              String str = (String)localMap.get(this.mGsResourceKeys.valueAt(i));
              if (str == null) {
                break label142;
              }
              localSparseArray.put(this.mGsResourceKeys.keyAt(i), str);
              break label142;
            }
          }
          else
          {
            localSparseArray = new SparseArray();
          }
          this.mResourceOverrides = localSparseArray;
        }
        else
        {
          return;
        }
      }
      finally {}
      label142:
      i++;
    }
  }
  
  public void clearCachedValues()
  {
    try
    {
      this.mResourceOverrides = null;
      this.mDeviceCountryOverride = null;
      this.mCachedStringSets.clear();
      this.mCachedStringMaps.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = paramString;
    arrayOfObject1[1] = "mExtraQueryParams: ";
    arrayOfObject1[2] = this.mExtraQueryParams;
    DumpUtils.println(paramPrintWriter, arrayOfObject1);
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = paramString;
    arrayOfObject2[1] = ("disabled icing corpora: " + Arrays.toString(getDisabledInternalIcingCorpora()));
    DumpUtils.println(paramPrintWriter, arrayOfObject2);
  }
  
  public int getAbnfCompilerNumContacts()
  {
    return getInt(2131427425);
  }
  
  public String getActionDiscoveryDataUri()
  {
    return getString(2131362024);
  }
  
  public int getActionDiscoveryMaxInstantPeekCount()
  {
    return getInt(2131427424);
  }
  
  public int getActionDiscoveryPeekDelayMillis()
  {
    return getInt(2131427423);
  }
  
  public String[] getActionDiscoverySupportedLocales()
  {
    return getStringArray(2131492902, false);
  }
  
  public List<Pair<String, String>> getAdClickUrlSubstitutions()
  {
    String[] arrayOfString = getStringArray(2131492896, true);
    ArrayList localArrayList = Lists.newArrayList();
    for (int i = 0; i < -1 + arrayOfString.length; i += 2) {
      localArrayList.add(Pair.create(arrayOfString[i], arrayOfString[(i + 1)]));
    }
    return localArrayList;
  }
  
  public String[] getAllowBackFromUrlWhitelist()
  {
    return getStringArray(2131492900, true);
  }
  
  public int getAuthTokenCacheTtl()
  {
    return getInt(2131427387);
  }
  
  public String[] getBackgroundTaskMinPeriods()
  {
    return getStringArray(2131492893, false);
  }
  
  public String[] getBackgroundTasks()
  {
    return getStringArray(2131492892, false);
  }
  
  public long getBackgroundTasksPeriodDaysOfDisuseSquaredMultipleMs()
  {
    return 3600000L * getInt(2131427390);
  }
  
  public long getBackgroundTasksPeriodMs()
  {
    return 60000L * getInt(2131427389);
  }
  
  public int getBackgroundTasksPeriodOfDaysBeforeBackoff()
  {
    return getInt(2131427391);
  }
  
  public Set<String> getChangingParamsThatAllowBackNavigation()
  {
    return getStringSet(2131492899);
  }
  
  public String getClickedAdUrlPath()
  {
    return getString(2131362012);
  }
  
  public String[] getClickedResultDestinationParams()
  {
    return getStringArray(2131492897, false);
  }
  
  public String getClickedResultUrlPath()
  {
    return getString(2131362011);
  }
  
  public String getClientExperimentsHeader()
  {
    return getString(2131362026);
  }
  
  public String getClientExperimentsParam()
  {
    return getString(2131362027);
  }
  
  public String getCompleteServerClientExternalId()
  {
    return getString(2131361989);
  }
  
  public String getCompleteServerClientId()
  {
    return getString(2131361988);
  }
  
  public String getCompleteServerDomainName()
  {
    return getString(2131361982);
  }
  
  public String[] getCompleteServerExtraParams()
  {
    return getStringArray(2131492878, true);
  }
  
  public String getCompleteServerHistoryRefreshParamName()
  {
    return getString(2131361985);
  }
  
  public String getCompleteServerHistoryRefreshParamValue()
  {
    return getString(2131361986);
  }
  
  public int getConcurrentSourceQueries()
  {
    return getInt(2131427365);
  }
  
  public String getCorporaConfigUri()
  {
    return getString(2131362013);
  }
  
  public String getCountryDomainOverride(String paramString)
  {
    return (String)getStringMap(2131492879).get(paramString);
  }
  
  public int getDeletedQueryPropagationDelayMs()
  {
    return getInt(2131427366);
  }
  
  public String getDeviceCountry()
  {
    return getString(2131362017);
  }
  
  public String[] getDisabledInternalIcingCorpora()
  {
    return getStringArray(2131492888, false);
  }
  
  public String getDogfoodDomainOverride()
  {
    return getString(2131362006);
  }
  
  public String[] getDomainWhitelist()
  {
    return getStringArray(2131492891, false);
  }
  
  public ImmutableMap<String, String> getExtraQueryParams()
  {
    if (this.mExtraQueryParams == null) {
      this.mExtraQueryParams = ImmutableMap.copyOf(Util.makeMapFromString('&', '=', getString(2131362025)));
    }
    return this.mExtraQueryParams;
  }
  
  public String getFixedSearchDomain()
  {
    if (this.mDeviceCountryOverride == null) {
      return null;
    }
    return getCountryDomainOverride(this.mDeviceCountryOverride);
  }
  
  public double getGoogleAnalyticsSampleRate()
  {
    return getInt(2131427429) / 1000.0D;
  }
  
  public String getGoogleGen204Pattern()
  {
    return getString(2131362015);
  }
  
  public String getGservicesExperimentIds()
  {
    return getString(2131362028);
  }
  
  public Set<String> getGwsParamsAffectingQueryEquivalenceForSearch()
  {
    return getStringSet(2131492898);
  }
  
  public boolean getHideDogfoodIndicator()
  {
    return getBoolean(2131755050);
  }
  
  public String getHistoryApiChangeUrlPattern()
  {
    return getString(2131362022);
  }
  
  public String getHistoryApiClientId()
  {
    return getString(2131362023);
  }
  
  public String getHistoryApiLookupUrlPattern()
  {
    return getString(2131362021);
  }
  
  public long getHttpCacheSize()
  {
    return getInt(2131427395);
  }
  
  public int getHttpConnectTimeout()
  {
    return getInt(2131427367);
  }
  
  public int getHttpReadTimeout()
  {
    return getInt(2131427367);
  }
  
  public int getIcingAppBonusHalfTimeDays()
  {
    return getInt(2131427419);
  }
  
  public int getIcingAppLaunchFullScoresUpdatePeriodMillis()
  {
    return getInt(2131427415);
  }
  
  public int getIcingAppLaunchValueHalfTimeDays()
  {
    return getInt(2131427418);
  }
  
  public int getIcingAppsCorpusUpdateAllIntervalMillis()
  {
    return getInt(2131427407);
  }
  
  public int getIcingContactsCorpusUpdateAllIntervalWithDeltaMillis()
  {
    return getInt(2131427409);
  }
  
  public int getIcingContactsCorpusUpdateAllIntervalWithoutDeltaMillis()
  {
    return getInt(2131427408);
  }
  
  public int getIcingContactsProviderResyncMaxRepollAttempts()
  {
    return getInt(2131427412);
  }
  
  public int getIcingContactsProviderResyncRepollPeriodMillis()
  {
    return getInt(2131427411);
  }
  
  public int getIcingContactsProviderResyncStartedInitialPollDelayMillis()
  {
    return getInt(2131427410);
  }
  
  public int getIcingLaunchLogMaxAgeDays()
  {
    return getInt(2131427421);
  }
  
  public int getIcingLaunchLogTrustConstant()
  {
    return getInt(2131427422);
  }
  
  public int getIcingNewAppScorePercentOfMax()
  {
    return getInt(2131427420);
  }
  
  public int getIcingNumberRequestedResultsInGroupedMode()
  {
    return getInt(2131427417);
  }
  
  public int getIcingNumberRequestedResultsInMixedSuggest()
  {
    return getInt(2131427416);
  }
  
  public int getIcingQueryWeightForCanonicalName(String paramString)
  {
    int i = 1;
    String str = (String)getStringMap(2131492901).get(paramString);
    if (str != null) {}
    try
    {
      int j = Integer.parseInt(str);
      i = j;
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.e("Search.SearchConfig", "Read invalid weight for " + paramString, localNumberFormatException);
    }
    return i;
  }
  
  public int getIcingServiceAppLaunchBroadcastDelayMillis()
  {
    return getInt(2131427414);
  }
  
  public int getInt(int paramInt)
  {
    SparseArray localSparseArray = this.mResourceOverrides;
    if (localSparseArray != null)
    {
      Object localObject = localSparseArray.get(paramInt);
      if (localObject != null)
      {
        if ((localObject instanceof Integer)) {
          return ((Integer)localObject).intValue();
        }
        localSparseArray.put(paramInt, convertIntegerOverride((String)localObject));
        return getInt(paramInt);
      }
    }
    else if (this.mGsResourceKeys.get(paramInt) != null)
    {
      loadOverrides();
      return getInt(paramInt);
    }
    return this.mContext.getResources().getInteger(paramInt);
  }
  
  public int getLocationExpiryTimeSeconds()
  {
    return getInt(2131427396);
  }
  
  public int getMarinerBackgroundRefreshIntervalMinutes()
  {
    return getInt(2131427430);
  }
  
  public int getMarinerIdleBackgroundRefreshIntervalMinutes()
  {
    return getInt(2131427431);
  }
  
  public int getMarinerMaximumStaleDataRefreshDistanceMeters()
  {
    return getInt(2131427433);
  }
  
  public long getMaxBackgroundTasksPeriodMs()
  {
    return 86400000L * getInt(2131427392);
  }
  
  public int getMaxConcurrentSourceQueries()
  {
    return getInt(2131427371);
  }
  
  public int getMaxDisplayedSummonsInResultsSuggest()
  {
    return getInt(2131427373);
  }
  
  public int getMaxGwsResponseSizeBytes()
  {
    return getInt(2131427405);
  }
  
  public int getMaxInitialWebCorpusSelectors()
  {
    return getInt(2131427394);
  }
  
  public int getMaxPromotedSummons()
  {
    return getInt(2131427372);
  }
  
  public int getMaxPromotedSummonsPerSourceIncrease()
  {
    return getInt(2131427375);
  }
  
  public int getMaxPromotedSummonsPerSourceInitial()
  {
    return getInt(2131427374);
  }
  
  public int getMaxResultsPerSource()
  {
    return getInt(2131427379);
  }
  
  public long getMaxStatAgeMillis()
  {
    return 3600000L * getInt(2131427380);
  }
  
  public int getMaxTotalSuggestions()
  {
    return getInt(2131427378);
  }
  
  public int getMaxWebSuggestions()
  {
    return getInt(2131427377);
  }
  
  public int getMinClicksForSourceRanking()
  {
    return getInt(2131427381);
  }
  
  public int getMinWebSuggestions()
  {
    return getInt(2131427376);
  }
  
  public int getMusicDetectionTimeoutMs()
  {
    return getInt(2131427397);
  }
  
  public int getNewConcurrentSourceQueryDelay()
  {
    return getInt(2131427382);
  }
  
  public String getPersonalizedSearchService()
  {
    return getString(2131362007);
  }
  
  public int getPredictiveIdleUserThresholdMinutes()
  {
    return getInt(2131427432);
  }
  
  public int getPrefetchCacheEntries()
  {
    return getInt(2131427402);
  }
  
  public int getPrefetchSimultaneousDownloads()
  {
    return getInt(2131427403);
  }
  
  public int getPrefetchThrottlePeriodMillis()
  {
    return getInt(2131427404);
  }
  
  public int getPrefetchTtlMillis()
  {
    return getInt(2131427401);
  }
  
  public int getPublishResultDelayMillis()
  {
    return getInt(2131427383);
  }
  
  public int getQueryThreadPriority()
  {
    return 9;
  }
  
  public long getRefreshSearchParametersCookieRefreshPeriodMs()
  {
    return 86400000L * getInt(2131427388);
  }
  
  public String getRegisterGsaBridgeJavascript()
  {
    String str1 = getString(2131362008);
    String str2 = getVelvetGsaBridgeInterfaceName();
    return String.format(Locale.US, str1, new Object[] { str2 });
  }
  
  public long getRememberHotwordSuccessForMillis()
  {
    return 60000L * getInt(2131427427);
  }
  
  public String getRemoveHistoryPath()
  {
    return getString(2131361984);
  }
  
  public Uri getRlzProviderUri()
  {
    return RLZ_PROVIDER_URI;
  }
  
  public String getS3ServerOverride()
  {
    return getString(2131362020);
  }
  
  public String getSearchDomainCheckPattern()
  {
    return getString(2131361990);
  }
  
  public String getSearchHistoryTokenType()
  {
    return getString(2131361993);
  }
  
  public int getServiceTimeoutForTvDetectionMillis()
  {
    return getInt(2131427435);
  }
  
  public long getShowHotwordHintForMillis()
  {
    return 86400000L * getInt(2131427428);
  }
  
  public boolean getSoundSearchEnabled()
  {
    return isGmsEnabled(getString(2131362018));
  }
  
  public int getSourceTimeoutMillis()
  {
    return getInt(2131427367);
  }
  
  public int getSuccessfulHotwordUsesToHideHint()
  {
    return getInt(2131427426);
  }
  
  public String getSuggestionAuthHeaderPrefix()
  {
    return getString(2131361995);
  }
  
  public int getSuggestionCacheMaxValues()
  {
    return getInt(2131427369);
  }
  
  public int getSuggestionCacheTimeout()
  {
    return getInt(2131427368);
  }
  
  public String getSuggestionPelletPath()
  {
    return getString(2131361987);
  }
  
  public String getSuggestionTokenType()
  {
    return getString(2131361994);
  }
  
  public int getSuggestionViewRecycleBinSize()
  {
    return getInt(2131427393);
  }
  
  public String getSuggestionsUrlFormat()
  {
    return getString(2131361997);
  }
  
  public String getTextSearchTokenType()
  {
    return getString(2131361992);
  }
  
  public String getTosUrlFormat()
  {
    return getString(2131361999);
  }
  
  public int getTvDetectionTimeoutMillis()
  {
    return getInt(2131427434);
  }
  
  public Map<String, String> getTvSearchQueryByLocale()
  {
    return getStringMap(2131492903);
  }
  
  public int getTypingUpdateSuggestionsDelayMillis()
  {
    return getInt(2131427384);
  }
  
  public String getUserAgentPattern()
  {
    return getString(2131362016);
  }
  
  public String getVelvetGsaBridgeInterfaceName()
  {
    return getString(2131362010);
  }
  
  public String getVoiceActionsS3ServiceOverride()
  {
    return getString(2131362019);
  }
  
  public String getVoiceSearchTokenType()
  {
    return getString(2131361991);
  }
  
  public String getWebCorpusQueryParam()
  {
    return getString(2131362014);
  }
  
  public int getWebViewLoginLoadTimeoutMs()
  {
    return getInt(2131427398);
  }
  
  public int getWebViewLoginRedirectTimeoutMs()
  {
    return getInt(2131427399);
  }
  
  public int getWebViewSuppressPreviousResultsForMs()
  {
    return getInt(2131427400);
  }
  
  public String getWeinreJavascriptPattern()
  {
    return getString(2131362009);
  }
  
  public boolean isActionDiscoveryEnabled()
  {
    return getBoolean(2131755031);
  }
  
  public boolean isAdClickUrlException(String paramString)
  {
    String[] arrayOfString = getStringArray(2131492895, false);
    int i = arrayOfString.length;
    for (int j = 0;; j++)
    {
      boolean bool = false;
      if (j < i)
      {
        if (paramString.matches(arrayOfString[j])) {
          bool = true;
        }
      }
      else {
        return bool;
      }
    }
  }
  
  public boolean isBluetoothEnabled()
  {
    return getBoolean(2131755032);
  }
  
  public boolean isContentProviderGlobalSearchEnabled()
  {
    return (Util.SDK_INT < 19) && (getBoolean(2131755043)) && (this.mContext.checkPermission("android.permission.GLOBAL_SEARCH", Process.myPid(), Process.myUid()) == 0);
  }
  
  public boolean isCorrectionsEnabled()
  {
    return getBoolean(2131755028);
  }
  
  public boolean isDebugAudioLoggingEnabled()
  {
    return getBoolean(2131755035);
  }
  
  public boolean isEmbeddedParserEnabled()
  {
    return getBoolean(2131755042);
  }
  
  public boolean isFullSizeIconIcingPackage(String paramString)
  {
    return getStringSet(2131492889).contains(paramString);
  }
  
  public boolean isFullSizeIconSource(@Nullable String paramString)
  {
    return getStringSet(2131492890).contains(paramString);
  }
  
  public boolean isGCoreUlrBurstModeEnabled()
  {
    return getBoolean(2131755048);
  }
  
  public boolean isGoogleSearchLogoutRedirect(String paramString)
  {
    return getStringSet(2131492884).contains(paramString);
  }
  
  public boolean isGoogleSearchUrlPath(String paramString)
  {
    return getStringSet(2131492883).contains(paramString);
  }
  
  public boolean isGoogleUtilityPath(String paramString)
  {
    return getStringSet(2131492885).contains(paramString);
  }
  
  public boolean isHotwordEnabled()
  {
    return getBoolean(2131755041);
  }
  
  public boolean isHotwordFromLauncherEnabled()
  {
    return getBoolean(2131755040);
  }
  
  public boolean isHotwordFromResultsEnabled()
  {
    return getBoolean(2131755039);
  }
  
  public boolean isIcingAppLaunchBroadcastHandlingEnabled()
  {
    return getBoolean(2131755033);
  }
  
  public boolean isIcingSourcePackageIgnored(String paramString)
  {
    return getStringSet(2131492887).contains(paramString);
  }
  
  public boolean isIcingSourcesEnabled()
  {
    return getBoolean(2131755024);
  }
  
  public boolean isInternalIcingCorporaEnabled()
  {
    return getBoolean(2131755025);
  }
  
  public boolean isPersonalizedSearchEnabled()
  {
    return getBoolean(2131755029);
  }
  
  public boolean isSdchEnabledForSerp()
  {
    return getBoolean(2131755037);
  }
  
  public boolean isSourceEnabledByDefault(@Nullable String paramString1, String paramString2)
  {
    if (getStringSet(2131492882).contains(paramString2)) {}
    while (getStringSet(2131492880).contains(paramString1)) {
      return true;
    }
    return false;
  }
  
  /* Error */
  public boolean isSourceEnabledInSuggestMode(com.google.android.search.core.summons.ContentProviderSource paramContentProviderSource)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokeinterface 1033 1 0
    //   8: pop
    //   9: aload_1
    //   10: invokeinterface 1036 1 0
    //   15: astore 4
    //   17: aload_0
    //   18: ldc_w 1037
    //   21: invokespecial 813	com/google/android/search/core/SearchConfig:getStringSet	(I)Ljava/util/Set;
    //   24: aload 4
    //   26: invokeinterface 1009 2 0
    //   31: istore 5
    //   33: iload 5
    //   35: ifeq +11 -> 46
    //   38: iconst_1
    //   39: istore 6
    //   41: aload_0
    //   42: monitorexit
    //   43: iload 6
    //   45: ireturn
    //   46: iconst_0
    //   47: istore 6
    //   49: goto -8 -> 41
    //   52: astore_2
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_2
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	this	SearchConfig
    //   0	57	1	paramContentProviderSource	com.google.android.search.core.summons.ContentProviderSource
    //   52	4	2	localObject	Object
    //   15	10	4	str	String
    //   31	3	5	bool1	boolean
    //   39	9	6	bool2	boolean
    // Exception table:
    //   from	to	target	type
    //   2	33	52	finally
  }
  
  public boolean isSourceIgnored(Source paramSource)
  {
    String str = paramSource.getName();
    return getStringSet(2131492886).contains(str);
  }
  
  public boolean isSpdyEnabled()
  {
    return getBoolean(2131755030);
  }
  
  public boolean isSpdyForSearchResultFetchesEnabled()
  {
    return getBoolean(2131755045);
  }
  
  public boolean isSpdyForSuggestionsEnabled()
  {
    return getBoolean(2131755044);
  }
  
  public boolean isSslSessionCacheEnabled()
  {
    return getBoolean(2131755046);
  }
  
  public boolean isSuggestLookAheadEnabled()
  {
    return getBoolean(2131755026);
  }
  
  public boolean isTestPlatformLoggingEnabled()
  {
    return getBoolean(2131755034);
  }
  
  public boolean isWordByWordEnabled()
  {
    return getBoolean(2131755027);
  }
  
  protected void putResourceKey(int paramInt, String paramString)
  {
    this.mGsResourceKeys.put(paramInt, paramString);
  }
  
  public boolean shouldAllowSslSearch()
  {
    return getBoolean(2131755022);
  }
  
  public boolean shouldCenterResultCardInLandscape()
  {
    return getBoolean(2131755019);
  }
  
  public boolean shouldCombineSuggestAndPrefetch()
  {
    return getBoolean(2131755021);
  }
  
  public int shouldLogIcingQueryPpm()
  {
    return getInt(2131427413);
  }
  
  public boolean shouldMatchPortraitWidthInLandscape()
  {
    return getBoolean(2131755020);
  }
  
  public boolean shouldSendQueryLocation()
  {
    return getBoolean(2131755049);
  }
  
  public boolean shouldUseChromePrerender()
  {
    return getBoolean(2131755038);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.SearchConfig
 * JD-Core Version:    0.7.0.1
 */