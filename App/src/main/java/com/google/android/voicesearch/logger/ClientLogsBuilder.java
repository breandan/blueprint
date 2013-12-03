package com.google.android.voicesearch.logger;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;

import com.google.android.search.core.GooglePlayServicesHelper.LogData;
import com.google.android.search.core.GsaConfigFlags;
import com.google.android.search.core.google.SearchBoxLogging;
import com.google.android.search.core.suggest.SuggestionLauncher.SummonsLogData;
import com.google.android.search.core.suggest.presenter.IcingInitialization.DiagnosticsLogData;
import com.google.android.search.core.summons.icing.InternalCorpus;
import com.google.android.search.core.util.LatencyTracker.Event;
import com.google.android.search.core.util.LatencyTracker.EventList;
import com.google.android.speech.utils.HexUtils;
import com.google.android.voicesearch.contacts.ContactSelectData;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

class ClientLogsBuilder {
    private static final int[] EMPTY_BREAKDOWN_EVENTS = new int[0];
    private static final Object[] EMPTY_REQUEST_DATA = new Object[0];
    private final VoicesearchClientLogProto.VoiceSearchClientLog mBaseClientLog;
    private VoicesearchClientLogProto.VoiceSearchClientLog mClientLog;
    private final ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> mClientLogs;
    private final GsaConfigFlags mConfigFlags;
    private final EventProcessor mEventProcessor;
    private String mRequestId;
    private int mRequestType;
    private final SublatencyCalculator mSublatencyCalculator;

    public ClientLogsBuilder(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, List<String> paramList, GsaConfigFlags paramGsaConfigFlags) {
        this.mBaseClientLog = createBaseClientLog(paramString1, paramString2, paramString3, paramString4, paramInt1, paramInt2, paramList, paramGsaConfigFlags.getExperimentIds());
        this.mClientLogs = new ArrayList();
        this.mClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
        try {
            this.mClientLog.mergeFrom(this.mBaseClientLog.toByteArray());
            this.mSublatencyCalculator = new SublatencyCalculator();
            this.mConfigFlags = paramGsaConfigFlags;
            this.mEventProcessor = new TokenFetchProcessor(new ContactDisplayInfoProcessor(new IcingCorporaDiagnosedProcessor(new ContactLookupInfoProcessor(new GooglePlayServicesProcessor(new OnDeviceSourceProcessor(new AudioInputDeviceProcessor(new BluetoothProcessor(new EmbeddedRecognizerProcessor(new ActionTypeProcessor(new RequestDataProcessor(new ShowCardProcessor(new FilterOutLatencyProcessor(new LatencyProcessor(new GrammarCompilerProcessor(new ApplicationNameProcessor(new EventTypeProcessor(null), null)))), null), null), null))))))))));
            return;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
            for (; ; ) {
                this.mClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
            }
        }
    }

    private void addClientEvent(VoicesearchClientLogProto.ClientEvent paramClientEvent) {
        this.mClientLog.addBundledClientEvents(paramClientEvent);
    }

    private void addCurrentClientLog() {
        if (this.mClientLog.getBundledClientEventsCount() > 0) {
            this.mClientLogs.add(this.mClientLog);
        }
        this.mClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
        try {
            this.mClientLog.mergeFrom(this.mBaseClientLog.toByteArray());
            return;
        } catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException) {
            this.mClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
        }
    }

    private static VoicesearchClientLogProto.VoiceSearchClientLog createBaseClientLog(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, List<String> paramList, Integer[] paramArrayOfInteger) {
        String str1 = Build.MODEL;
        String str2 = Build.DISPLAY;
        VoicesearchClientLogProto.VoiceSearchClientLog localVoiceSearchClientLog = new VoicesearchClientLogProto.VoiceSearchClientLog();
        localVoiceSearchClientLog.setApplicationVersion(paramString1);
        localVoiceSearchClientLog.setApplicationVersionName(paramString2);
        localVoiceSearchClientLog.setDeviceModel(str1);
        localVoiceSearchClientLog.setInstallId(paramString3);
        localVoiceSearchClientLog.setLocale(Locale.getDefault().toString());
        localVoiceSearchClientLog.setPlatformId("Android");
        localVoiceSearchClientLog.setPlatformVersion(str2);
        localVoiceSearchClientLog.setPackageId(paramString4);
        localVoiceSearchClientLog.setImeLangCount(paramInt1);
        localVoiceSearchClientLog.setVoicesearchLangCount(paramInt2);
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            localVoiceSearchClientLog.addExperimentId((String) localIterator.next());
        }
        int i = paramArrayOfInteger.length;
        for (int j = 0; j < i; j++) {
            localVoiceSearchClientLog.addGsaConfigExperimentId(paramArrayOfInteger[j].intValue());
        }
        return localVoiceSearchClientLog;
    }

    private static int getIntentType(String paramString) {
        if (paramString == null) {
        }
        do {
            return -1;
            if ("android.intent.action.MAIN".equals(paramString)) {
                return 0;
            }
            if ("android.intent.action.ASSIST".equals(paramString)) {
                return 1;
            }
            if ("android.search.action.GLOBAL_SEARCH".equals(paramString)) {
                return 2;
            }
            if ("android.intent.action.SEARCH_LONG_PRESS".equals(paramString)) {
                return 3;
            }
            if ("android.intent.action.SEND".equals(paramString)) {
                return 4;
            }
            if ("android.intent.action.WEB_SEARCH".equals(paramString)) {
                return 5;
            }
            if ("android.nfc.action.NDEF_DISCOVERED".equals(paramString)) {
                return 6;
            }
            if ("android.speech.action.WEB_SEARCH".equals(paramString)) {
                return 7;
            }
            if ("com.google.android.googlequicksearchbox.VOICE_SEARCH_RECORDED_AUDIO".equals(paramString)) {
                return 8;
            }
        } while (!"android.intent.action.VOICE_ASSIST".equals(paramString));
        return 9;
    }

    private VoicesearchClientLogProto.LatencyData toLatencyData(LatencyTracker.EventList paramEventList) {
        Preconditions.checkNotNull(paramEventList);
        VoicesearchClientLogProto.LatencyData localLatencyData = new VoicesearchClientLogProto.LatencyData();
        Iterator localIterator = paramEventList.events.iterator();
        while (localIterator.hasNext()) {
            LatencyTracker.Event localEvent = (LatencyTracker.Event) localIterator.next();
            VoicesearchClientLogProto.LatencyBreakdownEvent localLatencyBreakdownEvent = new VoicesearchClientLogProto.LatencyBreakdownEvent();
            localLatencyBreakdownEvent.setEvent(localEvent.type);
            localLatencyBreakdownEvent.setOffsetMsec(localEvent.latency);
            localLatencyData.addBreakdown(localLatencyBreakdownEvent);
        }
        localLatencyData.setDurationMsec((int) (paramEventList.endTimestamp - paramEventList.startTimestamp));
        this.mSublatencyCalculator.addBreakDownSublatency(localLatencyData);
        return localLatencyData;
    }

    private void updateApplicationIds(String paramString1, String paramString2) {
        addCurrentClientLog();
        if (paramString1 != null) {
            this.mClientLog.setApplicationId(paramString1);
        }
        if (paramString2 != null) {
            this.mClientLog.setTriggerApplicationId(paramString2);
        }
    }

    public ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> build(MainEventLoggerStore.Results paramResults) {
        LoggedEvent localLoggedEvent = new LoggedEvent();
        long l = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        if (paramResults.size() > 0) {
            VoicesearchClientLogProto.ClientEvent localClientEvent = new VoicesearchClientLogProto.ClientEvent();
            for (int i = 0; i < paramResults.size(); i++) {
                localLoggedEvent.setValues(paramResults.getEvent(i), l + paramResults.getTime(i), paramResults.getData(i));
                if (this.mEventProcessor.process(localLoggedEvent, localClientEvent)) {
                    localClientEvent = new VoicesearchClientLogProto.ClientEvent();
                }
            }
            addCurrentClientLog();
        }
        return this.mClientLogs;
    }

    private static class ActionTypeProcessor
            extends ClientLogsBuilder.EventProcessor {
        private final LongSparseArray<Boolean> mAddActionType = new LongSparseArray();

        private ActionTypeProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
            this.mAddActionType.put(268435549L, Boolean.TRUE);
            this.mAddActionType.put(268435550L, Boolean.TRUE);
            this.mAddActionType.put(268435580L, Boolean.TRUE);
            this.mAddActionType.put(268435564L, Boolean.TRUE);
            this.mAddActionType.put(268435579L, Boolean.TRUE);
            this.mAddActionType.put(268435469L, Boolean.TRUE);
            this.mAddActionType.put(268435506L, Boolean.TRUE);
            this.mAddActionType.put(268435470L, Boolean.TRUE);
            this.mAddActionType.put(268435528L, Boolean.TRUE);
            this.mAddActionType.put(268435581L, Boolean.TRUE);
            this.mAddActionType.put(268435500L, Boolean.TRUE);
            this.mAddActionType.put(268435501L, Boolean.TRUE);
            this.mAddActionType.put(268435547L, Boolean.TRUE);
            this.mAddActionType.put(268435569L, Boolean.TRUE);
            this.mAddActionType.put(268435570L, Boolean.TRUE);
            this.mAddActionType.put(268435568L, Boolean.TRUE);
            this.mAddActionType.put(268435571L, Boolean.TRUE);
            this.mAddActionType.put(268435575L, Boolean.TRUE);
            this.mAddActionType.put(268435572L, Boolean.TRUE);
            this.mAddActionType.put(268435574L, Boolean.TRUE);
            this.mAddActionType.put(268435573L, Boolean.TRUE);
            this.mAddActionType.put(268435594L, Boolean.TRUE);
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) == 268435456) && (this.mAddActionType.get(ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) | ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent)) != null)) {
                if (!(ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof Integer)) {
                    break label56;
                }
                paramClientEvent.setCardType(((Integer) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)).intValue());
            }
            for (; ; ) {
                return false;
                label56:
                if ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof ContactSelectData)) {
                    ContactSelectData localContactSelectData = (ContactSelectData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                    paramClientEvent.setCardType(localContactSelectData.actionType);
                    VoicesearchClientLogProto.ContactSelectInfo localContactSelectInfo = new VoicesearchClientLogProto.ContactSelectInfo();
                    localContactSelectInfo.setContactSelectPosition(localContactSelectData.position);
                    paramClientEvent.setContactSelectInfo(localContactSelectInfo);
                } else if (paramLoggedEvent.is(268435456, 123)) {
                    paramClientEvent.setCardType(10);
                    paramClientEvent.setEmbeddedParserDetails((VoicesearchClientLogProto.EmbeddedParserDetails) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                } else if ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof SearchCardController.EmbeddedParserLogData)) {
                    SearchCardController.EmbeddedParserLogData localEmbeddedParserLogData = (SearchCardController.EmbeddedParserLogData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                    paramClientEvent.setCardType(localEmbeddedParserLogData.actionType);
                    paramClientEvent.setEmbeddedParserDetails(localEmbeddedParserLogData.embeddedParserDetails);
                } else if ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof VoicesearchClientLogProto.GwsCorrectionData)) {
                    paramClientEvent.setGwsCorrection((VoicesearchClientLogProto.GwsCorrectionData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                }
            }
        }
    }

    private class ApplicationNameProcessor
            extends ClientLogsBuilder.EventProcessor {
        private ApplicationNameProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(268435456, 1)) {
                ClientLogsBuilder.this.updateApplicationIds("voice-search", null);
                paramClientEvent.setEventType(ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent)).setClientTimeMs(ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent));
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 2)) {
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                ClientLogsBuilder.this.updateApplicationIds(null, null);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 35)) {
                ClientLogsBuilder.this.updateApplicationIds("voice-ime", (String) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 41)) {
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                ClientLogsBuilder.this.updateApplicationIds(null, null);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 55)) {
                ClientLogsBuilder.this.updateApplicationIds("service-api", (String) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                return true;
            }
            if ((paramLoggedEvent.is(268435456, 59)) || (paramLoggedEvent.is(268435456, 60))) {
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                ClientLogsBuilder.this.updateApplicationIds(null, null);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 61)) {
                ClientLogsBuilder.this.updateApplicationIds("intent-api", (String) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 62)) {
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                ClientLogsBuilder.this.updateApplicationIds(null, null);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 77)) {
                ClientLogsBuilder.this.updateApplicationIds("hands-free", (String) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                return true;
            }
            if (paramLoggedEvent.is(268435456, 78)) {
                paramLoggedEvent.populate(paramClientEvent);
                ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                ClientLogsBuilder.this.updateApplicationIds(null, null);
                return true;
            }
            return false;
        }
    }

    private static class AudioInputDeviceProcessor
            extends ClientLogsBuilder.EventProcessor {
        public AudioInputDeviceProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((paramLoggedEvent.is(268435456, 76)) && ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof SpeechLibLogger.LogData))) {
                paramClientEvent.setAudioInputDevice(((SpeechLibLogger.LogData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)).audioPath);
                paramClientEvent.setNetworkType(((SpeechLibLogger.LogData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)).networkType);
            }
            return false;
        }
    }

    class BluetoothProcessor
            extends ClientLogsBuilder.EventProcessor {
        public BluetoothProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        VoicesearchClientLogProto.BluetoothDevice buildDeviceToLog(String[] paramArrayOfString) {
            VoicesearchClientLogProto.BluetoothDevice localBluetoothDevice = new VoicesearchClientLogProto.BluetoothDevice();
            String str1 = paramArrayOfString[0];
            if (str1 != null) {
                localBluetoothDevice.setDeviceName(str1);
            }
            try {
                localBluetoothDevice.setDeviceNameHash(HexUtils.bytesToHex(MessageDigest.getInstance("MD5").digest(str1.getBytes(Charsets.UTF_8))));
                String str2 = paramArrayOfString[1];
                if (str2 != null) {
                    String str3 = str2.replaceAll(":", "");
                    if (str3.length() >= 12) {
                        localBluetoothDevice.setOrgIdentifier(str3.substring(0, 6));
                    }
                }
                return localBluetoothDevice;
            } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
                for (; ; ) {
                    Log.w("ClientLogsBuilder", "MD5 not available");
                }
            }
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (((paramLoggedEvent.is(268435456, 106)) || (paramLoggedEvent.is(268435456, 107))) && (ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) != null) && (ClientLogsBuilder.this.mConfigFlags.isBluetoothDeviceLoggingEnabled())) {
                paramClientEvent.setBluetoothDevice(buildDeviceToLog((String[]) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)));
            }
            return false;
        }
    }

    private class ContactDisplayInfoProcessor
            extends ClientLogsBuilder.EventProcessor {
        public ContactDisplayInfoProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((paramLoggedEvent.is(268435456, 125)) && ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof VoicesearchClientLogProto.ContactDisplayInfo))) {
                paramClientEvent.setContactDisplayInfo((VoicesearchClientLogProto.ContactDisplayInfo) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
            }
            return false;
        }
    }

    private class ContactLookupInfoProcessor
            extends ClientLogsBuilder.EventProcessor {
        public ContactLookupInfoProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((paramLoggedEvent.is(268435456, 124)) && ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof VoicesearchClientLogProto.ContactLookupInfo))) {
                paramClientEvent.setContactLookupInfo((VoicesearchClientLogProto.ContactLookupInfo) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
            }
            return false;
        }
    }

    public static class EmbeddedRecognizerProcessor
            extends ClientLogsBuilder.EventProcessor {
        public EmbeddedRecognizerProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(268435456, 52)) {
                paramClientEvent.setEmbeddedRecognizerLog((RecognizerOuterClass.RecognizerLog) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
            }
            for (; ; ) {
                return false;
                if (paramLoggedEvent.is(268435456, 89)) {
                    RecognizerOuterClass.LanguagePackLog localLanguagePackLog = ((RecognizerOuterClass.RecognizerLog) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)).getLangPack();
                    RecognizerOuterClass.RecognizerLog localRecognizerLog = new RecognizerOuterClass.RecognizerLog();
                    localRecognizerLog.setLangPack(localLanguagePackLog);
                    paramClientEvent.setEmbeddedRecognizerLog(localRecognizerLog);
                }
            }
        }
    }

    private static abstract class EventProcessor {
        private final EventProcessor mNextEventProcessor;

        public EventProcessor(EventProcessor paramEventProcessor) {
            this.mNextEventProcessor = paramEventProcessor;
        }

        abstract boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent);

        public boolean process(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (internalProcess(paramLoggedEvent, paramClientEvent)) {
                return true;
            }
            if (this.mNextEventProcessor != null) {
                return this.mNextEventProcessor.process(paramLoggedEvent, paramClientEvent);
            }
            return false;
        }
    }

    private class EventTypeProcessor
            extends ClientLogsBuilder.EventProcessor {
        public EventTypeProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) == 268435456) {
                paramLoggedEvent.populate(paramClientEvent);
                switch (ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent)) {
                }
                for (; ; ) {
                    ClientLogsBuilder.this.addClientEvent(paramClientEvent);
                    return true;
                    VoicesearchClientLogProto.BugReport localBugReport = new VoicesearchClientLogProto.BugReport();
                    if (ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) == null) {
                    }
                    for (Object localObject = Integer.valueOf(-1); ; localObject = ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)) {
                        paramClientEvent.setBugReport(localBugReport.setBugNumber(((Integer) localObject).intValue()));
                        break;
                    }
                    if (ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) != null) {
                        SuggestionLogger.SuggestionData localSuggestionData = (SuggestionLogger.SuggestionData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                        paramClientEvent.setAlternateCorrection(localSuggestionData.getProto());
                        paramClientEvent.setRequestId(localSuggestionData.getRequestId());
                        continue;
                        Bundle localBundle = (Bundle) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                        Preconditions.checkState(localBundle.containsKey("com.google.android.speech.REQUEST_ID"));
                        Preconditions.checkState(localBundle.containsKey("com.google.android.speech.SEGMENT_ID"));
                        paramClientEvent.setRequestId(localBundle.getString("com.google.android.speech.REQUEST_ID"));
                        VoicesearchClientLogProto.TypingCorrection localTypingCorrection = new VoicesearchClientLogProto.TypingCorrection();
                        localTypingCorrection.setRecognizerSegmentIndex(localBundle.getInt("com.google.android.speech.SEGMENT_ID"));
                        paramClientEvent.setTypingCorrection(localTypingCorrection);
                    }
                }
            }
            return false;
        }
    }

    public static class FilterOutLatencyProcessor
            extends ClientLogsBuilder.EventProcessor {
        private long mActivityCreateTimestamp;
        private long mActivityRestartTimestamp;
        private long mApplicationCreateTimestamp;

        public FilterOutLatencyProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(1073741824, 3)) {
                this.mApplicationCreateTimestamp = ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent);
            }
            if (paramLoggedEvent.is(1073741824, 4)) {
                this.mActivityCreateTimestamp = ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent);
                if (5000L + this.mApplicationCreateTimestamp > ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent)) {
                    this.mApplicationCreateTimestamp = 0L;
                    return true;
                }
            }
            if (paramLoggedEvent.is(1073741824, 7)) {
                this.mActivityRestartTimestamp = ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent);
                if (5000L + this.mActivityCreateTimestamp > ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent)) {
                    this.mActivityCreateTimestamp = 0L;
                    return true;
                }
            }
            if ((paramLoggedEvent.is(1073741824, 8)) && ((5000L + this.mActivityCreateTimestamp > ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent)) || (5000L + this.mActivityRestartTimestamp > ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent)))) {
                this.mActivityCreateTimestamp = 0L;
                this.mActivityRestartTimestamp = 0L;
                return true;
            }
            return false;
        }
    }

    private final class GooglePlayServicesProcessor
            extends ClientLogsBuilder.EventProcessor {
        public GooglePlayServicesProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((paramLoggedEvent.is(268435456, 109)) && ((ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent) instanceof GooglePlayServicesHelper.LogData))) {
                GooglePlayServicesHelper.LogData localLogData = (GooglePlayServicesHelper.LogData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                VoicesearchClientLogProto.GmsCoreData localGmsCoreData = new VoicesearchClientLogProto.GmsCoreData();
                localGmsCoreData.setGooglePlayServicesAvailability(localLogData.googlePlayServicesAvailability);
                localGmsCoreData.setGooglePlayServicesVersionCode(localLogData.googlePlayServicesVersion);
                paramClientEvent.setGmsCoreData(localGmsCoreData);
            }
            return false;
        }
    }

    private static class GrammarCompilerProcessor
            extends ClientLogsBuilder.EventProcessor {
        private int mContactsCount = -1;

        public GrammarCompilerProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(536870912, 14)) {
                this.mContactsCount = ((Integer) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent)).intValue();
            }
            if ((this.mContactsCount > 0) && (paramLoggedEvent.is(268435456, 82))) {
                if (!paramClientEvent.hasLatency()) {
                    paramClientEvent.setLatency(new VoicesearchClientLogProto.LatencyData());
                }
                paramClientEvent.getLatency().setFactor(this.mContactsCount);
                this.mContactsCount = -1;
            }
            return false;
        }
    }

    private static final class IcingCorporaDiagnosedProcessor
            extends ClientLogsBuilder.EventProcessor {
        public IcingCorporaDiagnosedProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(268435456, 121)) {
                IcingInitialization.DiagnosticsLogData localDiagnosticsLogData = (IcingInitialization.DiagnosticsLogData) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent);
                VoicesearchClientLogProto.IcingCorpusDiagnostics localIcingCorpusDiagnostics = new VoicesearchClientLogProto.IcingCorpusDiagnostics();
                Iterator localIterator = localDiagnosticsLogData.diagnosticsBundle.keySet().iterator();
                label93:
                label227:
                while (localIterator.hasNext()) {
                    String str = (String) localIterator.next();
                    int i = localDiagnosticsLogData.diagnosticsBundle.getInt(str, -1);
                    int j;
                    int k;
                    if (InternalCorpus.APPLICATIONS.getCorpusName().equals(str)) {
                        j = 0;
                        if (i != 0) {
                            break label175;
                        }
                        k = 0;
                    }
                    for (; ; ) {
                        if ((j == -1) || (k == -1)) {
                            break label227;
                        }
                        VoicesearchClientLogProto.IcingCorpusDiagnostics.IcingCorpusDiagnostic localIcingCorpusDiagnostic = new VoicesearchClientLogProto.IcingCorpusDiagnostics.IcingCorpusDiagnostic();
                        localIcingCorpusDiagnostic.setCorpus(j);
                        localIcingCorpusDiagnostic.setDiagnostic(k);
                        localIcingCorpusDiagnostics.addDiagnostics(localIcingCorpusDiagnostic);
                        break;
                        if (InternalCorpus.CONTACTS.getCorpusName().equals(str)) {
                            j = 1;
                            break label93;
                        }
                        j = -1;
                        break label93;
                        if (i == 1) {
                            k = 1;
                        } else if (i == 2) {
                            k = 2;
                        } else if (i == 3) {
                            k = 3;
                        } else if (i == 4) {
                            k = 4;
                        } else {
                            k = -1;
                        }
                    }
                }
                label175:
                localIcingCorpusDiagnostics.setDeviceStorageLow(localDiagnosticsLogData.deviceStorageLow);
                paramClientEvent.setIcingCorpusDiagnostic(localIcingCorpusDiagnostics);
            }
            return false;
        }
    }

    private class LatencyProcessor
            extends ClientLogsBuilder.EventProcessor {
        private final ArrayList<ClientLogsBuilder.LoggedEvent> mBreakDownEvents = Lists.newArrayList();
        private final SparseArray<Boolean> mFromEvents = new SparseArray();
        private final SparseArray<Long> mPastEvents = new SparseArray();
        private final SparseArray<List<LatencyEvent>> mToEvents = new SparseArray();

        public LatencyProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
            ArrayList localArrayList = new ArrayList();
            localArrayList.add(new LatencyEvent(268435463, 268435464));
            localArrayList.add(new LatencyEvent(1073741825, 268435459));
            localArrayList.add(new LatencyEvent(1073741836, 268435587));
            localArrayList.add(new LatencyEvent(268435479, 268435467, 268435487));
            localArrayList.add(new LatencyEvent(268435467, 268435468, 268435488));
            localArrayList.add(new LatencyEvent(268435475, 268435468, 268435529));
            localArrayList.add(new LatencyEvent(268435468, 268435486, 268435489));
            localArrayList.add(new LatencyEvent(1073741826, 268435538));
            localArrayList.add(new LatencyEvent(1073741829, 268435553));
            localArrayList.add(new LatencyEvent(1073741830, 268435552));
            localArrayList.add(new LatencyEvent(1073741833, 268435562));
            localArrayList.add(new LatencyEvent(1073741833, 268435563));
            localArrayList.add(new LatencyEvent(1073741834, 268435578));
            localArrayList.add(new LatencyEvent(1073741837, 268435588));
            localArrayList.add(new LatencyEvent(1073741838, 268435589));
            localArrayList.add(new LatencyEvent(1073741827, 1342177312, 268435555, new int[]{16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 40, 28, 32}));
            localArrayList.add(new LatencyEvent(1073741828, 1342177312, 268435556, new int[]{18, 19, 20, 21, 22, 23, 24, 25, 26, 40, 28, 32}));
            localArrayList.add(new LatencyEvent(1073741831, 1342177312, 268435557, new int[]{20, 21, 22, 23, 24, 25, 26, 27, 40, 28, 32}));
            localArrayList.add(new LatencyEvent(1073741832, 1342177312, 268435558, new int[]{20, 23, 24, 25, 26, 40, 28, 32}));
            localArrayList.add(new LatencyEvent(1073741835, 1342177334, 268435586, new int[]{49, 50, 51, 52, 53, 54}));
            Iterator localIterator = localArrayList.iterator();
            while (localIterator.hasNext()) {
                LatencyEvent localLatencyEvent = (LatencyEvent) localIterator.next();
                this.mFromEvents.put(localLatencyEvent.mFromEvent, Boolean.TRUE);
                Object localObject = (List) this.mToEvents.get(localLatencyEvent.mToEvent);
                if (localObject == null) {
                    localObject = new ArrayList();
                    this.mToEvents.put(localLatencyEvent.mToEvent, localObject);
                }
                ((List) localObject).add(localLatencyEvent);
            }
        }

        private void addBreakDownData(long paramLong1, long paramLong2, LatencyEvent paramLatencyEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            Preconditions.checkNotNull(paramClientEvent.getLatency());
            Iterator localIterator = this.mBreakDownEvents.iterator();
            while (localIterator.hasNext()) {
                ClientLogsBuilder.LoggedEvent localLoggedEvent = (ClientLogsBuilder.LoggedEvent) localIterator.next();
                if ((ClientLogsBuilder.LoggedEvent.access$900(localLoggedEvent) >= paramLong1) && (ClientLogsBuilder.LoggedEvent.access$900(localLoggedEvent) <= paramLong2) && (paramLatencyEvent.hasBreakDownEvent(ClientLogsBuilder.LoggedEvent.access$500(localLoggedEvent)))) {
                    VoicesearchClientLogProto.LatencyBreakdownEvent localLatencyBreakdownEvent = new VoicesearchClientLogProto.LatencyBreakdownEvent();
                    localLatencyBreakdownEvent.setEvent(ClientLogsBuilder.LoggedEvent.access$500(localLoggedEvent));
                    localLatencyBreakdownEvent.setOffsetMsec((int) (ClientLogsBuilder.LoggedEvent.access$900(localLoggedEvent) - paramLong1));
                    paramClientEvent.getLatency().addBreakdown(localLatencyBreakdownEvent);
                }
            }
            ClientLogsBuilder.this.mSublatencyCalculator.addBreakDownSublatency(paramClientEvent.getLatency());
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) == 1342177280) {
                this.mBreakDownEvents.add(paramLoggedEvent.clone());
            }
            if (this.mFromEvents.get(ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) | ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent)) != null) {
                this.mPastEvents.put(ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) | ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent), Long.valueOf(ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent)));
            }
            Object localObject;
            long l1;
            long l2;
            int i;
            if (this.mToEvents.get(ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) | ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent)) != null) {
                localObject = null;
                Iterator localIterator = ((List) this.mToEvents.get(ClientLogsBuilder.LoggedEvent.access$400(paramLoggedEvent) + ClientLogsBuilder.LoggedEvent.access$500(paramLoggedEvent))).iterator();
                while (localIterator.hasNext()) {
                    LatencyEvent localLatencyEvent = (LatencyEvent) localIterator.next();
                    if ((this.mPastEvents.get(localLatencyEvent.mFromEvent) != null) && ((localObject == null) || (((Long) this.mPastEvents.get(localLatencyEvent.mFromEvent)).longValue() > ((Long) this.mPastEvents.get(localObject.mFromEvent)).longValue()))) {
                        localObject = localLatencyEvent;
                    }
                }
                if (localObject != null) {
                    l1 = ((Long) this.mPastEvents.get(localObject.mFromEvent)).longValue();
                    l2 = ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent);
                    i = (int) (l2 - l1);
                    if (localObject.mEventType != localObject.mToEvent) {
                        break label263;
                    }
                    paramClientEvent.setLatency(new VoicesearchClientLogProto.LatencyData().setDurationMsec(i));
                }
            }
            for (; ; ) {
                return false;
                label263:
                VoicesearchClientLogProto.LatencyData localLatencyData = new VoicesearchClientLogProto.LatencyData().setDurationMsec(i);
                VoicesearchClientLogProto.ClientEvent localClientEvent = new VoicesearchClientLogProto.ClientEvent().setEventType(EventUtils.getId(localObject.mEventType)).setLatency(localLatencyData).setClientTimeMs(ClientLogsBuilder.LoggedEvent.access$900(paramLoggedEvent));
                if (paramClientEvent.hasRequestId()) {
                    localClientEvent.setRequestId(paramClientEvent.getRequestId());
                }
                if (paramClientEvent.hasCardType()) {
                    localClientEvent.setCardType(paramClientEvent.getCardType());
                }
                if (paramLoggedEvent.is(1342177280, 32)) {
                    int j = ClientLogsBuilder.getIntentType((String) ClientLogsBuilder.LoggedEvent.access$600(paramLoggedEvent));
                    if (j != -1) {
                        localClientEvent.setIntentType(j);
                    }
                }
                addBreakDownData(l1, l2, localObject, localClientEvent);
                ClientLogsBuilder.this.addClientEvent(localClientEvent);
            }
        }

        public class LatencyEvent {
            final int[] mBreakdownEvents;
            final int mEventType;
            final int mFromEvent;
            final int mToEvent;

            public LatencyEvent(int paramInt1, int paramInt2) {
                this(paramInt1, paramInt2, paramInt2);
            }

            public LatencyEvent(int paramInt1, int paramInt2, int paramInt3) {
                this(paramInt1, paramInt2, paramInt3, ClientLogsBuilder.EMPTY_BREAKDOWN_EVENTS);
            }

            public LatencyEvent(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt) {
                boolean bool2;
                boolean bool3;
                if (EventUtils.getGroup(paramInt1) != 0) {
                    bool2 = bool1;
                    Preconditions.checkArgument(bool2);
                    if (EventUtils.getGroup(paramInt2) == 0) {
                        break label94;
                    }
                    bool3 = bool1;
                    label39:
                    Preconditions.checkArgument(bool3);
                    if (EventUtils.getGroup(paramInt3) != 268435456) {
                        break label100;
                    }
                }
                for (; ; ) {
                    Preconditions.checkArgument(bool1);
                    this.mFromEvent = paramInt1;
                    this.mToEvent = paramInt2;
                    this.mEventType = paramInt3;
                    this.mBreakdownEvents = ((int[]) Preconditions.checkNotNull(paramArrayOfInt));
                    return;
                    bool2 = false;
                    break;
                    label94:
                    bool3 = false;
                    break label39;
                    label100:
                    bool1 = false;
                }
            }

            private boolean hasBreakDownEvent(int paramInt) {
                boolean bool;
                int[] arrayOfInt;
                int i;
                if (EventUtils.getId(paramInt) == paramInt) {
                    bool = true;
                    Preconditions.checkArgument(bool);
                    arrayOfInt = this.mBreakdownEvents;
                    i = arrayOfInt.length;
                }
                for (int j = 0; ; j++) {
                    if (j >= i) {
                        break label54;
                    }
                    if (paramInt == arrayOfInt[j]) {
                        return true;
                        bool = false;
                        break;
                    }
                }
                label54:
                return false;
            }
        }
    }

    final class LoggedEvent {
        private Object data;
        private int group;
        private int source;
        private long timestamp;
        private int type;

        LoggedEvent() {
        }

        public LoggedEvent clone() {
            LoggedEvent localLoggedEvent = new LoggedEvent(ClientLogsBuilder.this);
            localLoggedEvent.group = this.group;
            localLoggedEvent.source = this.source;
            localLoggedEvent.type = this.type;
            localLoggedEvent.timestamp = this.timestamp;
            localLoggedEvent.data = this.data;
            return localLoggedEvent;
        }

        public boolean in(SparseArray<Boolean> paramSparseArray) {
            return paramSparseArray.get(this.group | this.type) == Boolean.TRUE;
        }

        public boolean is(int paramInt1, int paramInt2) {
            return (this.group == paramInt1) && (this.type == paramInt2);
        }

        public void populate(VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            paramClientEvent.setEventType(this.type).setClientTimeMs(this.timestamp);
            if (this.source != 0) {
                paramClientEvent.setEventSource(this.source);
            }
            if ((this.data instanceof LatencyTracker.EventList)) {
                LatencyTracker.EventList localEventList = (LatencyTracker.EventList) this.data;
                paramClientEvent.setLatency(ClientLogsBuilder.this.toLatencyData(localEventList));
                if (localEventList.error > 0) {
                    paramClientEvent.setErrorType(localEventList.error);
                }
                paramClientEvent.setNetworkType(localEventList.networkType);
            }
            if ((this.data instanceof VoicesearchClientLogProto.LatencyData)) {
                paramClientEvent.setLatency((VoicesearchClientLogProto.LatencyData) this.data);
            }
        }

        void setValues(int paramInt1, int paramInt2, int paramInt3, long paramLong, Object paramObject) {
            this.group = paramInt1;
            this.source = paramInt2;
            this.type = paramInt3;
            this.timestamp = paramLong;
            this.data = paramObject;
        }

        public void setValues(int paramInt, long paramLong, Object paramObject) {
            setValues(paramInt & 0xF0000000, paramInt & 0xF000000, paramInt & 0xFFFFFF, paramLong, paramObject);
        }

        public String toString() {
            return "LoggedEvent[g=" + this.group + ",s=" + this.source + ",t=" + this.type + ",ts=" + this.timestamp + "]";
        }
    }

    private static class OnDeviceSourceProcessor
            extends ClientLogsBuilder.EventProcessor {
        public OnDeviceSourceProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            SuggestionLauncher.SummonsLogData localSummonsLogData;
            int i;
            if (paramLoggedEvent.is(268435456, 105)) {
                localSummonsLogData = (SuggestionLauncher.SummonsLogData) paramLoggedEvent.data;
                i = SearchBoxLogging.getCanonicalSourceEnum(localSummonsLogData.packageName, localSummonsLogData.canonicalName);
                if (!localSummonsLogData.isFromIcing) {
                    break label139;
                }
            }
            label139:
            for (int j = 1; ; j = 0) {
                VoicesearchClientLogProto.OnDeviceSource localOnDeviceSource = new VoicesearchClientLogProto.OnDeviceSource();
                localOnDeviceSource.setPackageName(localSummonsLogData.packageName);
                localOnDeviceSource.setCanonicalSource(i);
                localOnDeviceSource.setSourceType(j);
                paramClientEvent.setOnDeviceSource(localOnDeviceSource);
                if ((paramLoggedEvent.is(268435456, 120)) && ((paramLoggedEvent.data instanceof Integer))) {
                    Integer localInteger = (Integer) paramLoggedEvent.data;
                    paramClientEvent.setOnDeviceSource(new VoicesearchClientLogProto.OnDeviceSource().setCanonicalSource(localInteger.intValue()));
                }
                return false;
            }
        }
    }

    private class RequestDataProcessor
            extends ClientLogsBuilder.EventProcessor {
        private final SparseArray<Boolean> mAddRequestIds = new SparseArray(70);
        private final HashMap<Integer, Object[]> mCacheEventRequestDatas = Maps.newHashMap();
        private final HashMap<Integer, Integer> mOverrideEventRequestIds = Maps.newHashMap();

        private RequestDataProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
            addClientEvent(93);
            addClientEvent(94);
            addClientEvent(124);
            addClientEvent(76);
            addClientEvent(106);
            addClientEvent(107);
            addClientEvent(74);
            addClientEvent(40);
            addClientEvent(38);
            addClientEvent(41);
            addClientEvent(42);
            addClientEvent(36);
            addClientEvent(39);
            addClientEvent(75);
            addClientEvent(35);
            addClientEvent(130);
            addClientEvent(96);
            addClientEvent(97);
            addClientEvent(98);
            addClientEvent(70);
            addClientEvent(71);
            addClientEvent(90);
            addClientEvent(108);
            addClientEvent(85);
            addClientEvent(86);
            addClientEvent(104);
            addClientEvent(51);
            addClientEvent(10);
            addClientEvent(9);
            addClientEvent(8);
            addClientEvent(7);
            addClientEvent(12);
            addClientEvent(27);
            addClientEvent(28);
            addClientEvent(32);
            addClientEvent(31);
            addClientEvent(33);
            addClientEvent(11);
            addClientEvent(26);
            addClientEvent(25);
            addClientEvent(23);
            addClientEvent(22);
            addClientEvent(24);
            addClientEvent(30);
            addClientEvent(50);
            addClientEvent(14);
            addClientEvent(72);
            addClientEvent(13);
            addClientEvent(18);
            addClientEvent(105);
            addClientEvent(125);
            addClientEvent(44);
            addClientEvent(45);
            addClientEvent(134);
            addClientEvent(136);
            addClientEvent(135);
            addClientEvent(137);
            addClientEvent(37);
            addClientEvent(63);
            addClientEvent(17);
            addClientEvent(123);
            addClientEvent(113);
            addClientEvent(114);
            addClientEvent(112);
            addClientEvent(115);
            addClientEvent(119);
            addClientEvent(116);
            addClientEvent(118);
            addClientEvent(117);
            this.mOverrideEventRequestIds.put(Integer.valueOf(268435508), Integer.valueOf(268435507));
            Iterator localIterator = this.mOverrideEventRequestIds.values().iterator();
            while (localIterator.hasNext()) {
                int i = ((Integer) localIterator.next()).intValue();
                this.mCacheEventRequestDatas.put(Integer.valueOf(i), ClientLogsBuilder.EMPTY_REQUEST_DATA);
            }
        }

        private void addClientEvent(int paramInt) {
            this.mAddRequestIds.put(0x10000000 | paramInt, Boolean.TRUE);
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(536870912, 3)) {
                ClientLogsBuilder.access$1102(ClientLogsBuilder.this, (String) paramLoggedEvent.data);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 8)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 1);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 9)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 6);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 10)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 5);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 15)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 7);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 11)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 2);
                return true;
            }
            if (paramLoggedEvent.is(536870912, 12)) {
                ClientLogsBuilder.access$1202(ClientLogsBuilder.this, 3);
                return true;
            }
            if ((ClientLogsBuilder.this.mRequestId != null) && (paramLoggedEvent.in(this.mAddRequestIds))) {
                paramClientEvent.setRequestId(ClientLogsBuilder.this.mRequestId);
                paramClientEvent.setRequestType(ClientLogsBuilder.this.mRequestType);
            }
            if ((ClientLogsBuilder.this.mRequestId != null) && (this.mCacheEventRequestDatas.containsKey(Integer.valueOf(paramLoggedEvent.group | paramLoggedEvent.type)))) {
                HashMap localHashMap = this.mCacheEventRequestDatas;
                Integer localInteger = Integer.valueOf(paramLoggedEvent.group | paramLoggedEvent.type);
                Object[] arrayOfObject2 = new Object[2];
                arrayOfObject2[0] = ClientLogsBuilder.this.mRequestId;
                arrayOfObject2[1] = Integer.valueOf(ClientLogsBuilder.this.mRequestType);
                localHashMap.put(localInteger, arrayOfObject2);
            }
            if (this.mOverrideEventRequestIds.containsKey(Integer.valueOf(paramLoggedEvent.group | paramLoggedEvent.type))) {
                Object[] arrayOfObject1 = (Object[]) this.mCacheEventRequestDatas.get(this.mOverrideEventRequestIds.get(Integer.valueOf(paramLoggedEvent.group | paramLoggedEvent.type)));
                if (arrayOfObject1 != ClientLogsBuilder.EMPTY_REQUEST_DATA) {
                    paramClientEvent.setRequestId((String) arrayOfObject1[0]);
                    paramClientEvent.setRequestType(((Integer) arrayOfObject1[1]).intValue());
                }
            }
            return false;
        }
    }

    private class ShowCardProcessor
            extends ClientLogsBuilder.EventProcessor {
        private ShowCardProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if ((paramLoggedEvent.is(268435456, 1)) || (paramLoggedEvent.is(268435456, 2)) || (paramLoggedEvent.is(268435456, 35)) || (paramLoggedEvent.is(268435456, 41)) || (paramLoggedEvent.is(268435456, 55)) || (paramLoggedEvent.is(268435456, 59)) || (paramLoggedEvent.is(268435456, 60)) || (paramLoggedEvent.is(268435456, 62)) || (paramLoggedEvent.is(268435456, 61))) {
            }
            while (!paramLoggedEvent.is(268435456, 4)) {
                return false;
            }
            paramClientEvent.setEventType(4);
            paramClientEvent.setClientTimeMs(paramLoggedEvent.timestamp);
            if ((paramLoggedEvent.data instanceof Integer)) {
                paramClientEvent.setCardType(((Integer) paramLoggedEvent.data).intValue());
            }
            if (ClientLogsBuilder.this.mRequestId != null) {
                paramClientEvent.setRequestId(ClientLogsBuilder.this.mRequestId);
            }
            ClientLogsBuilder.this.addClientEvent(paramClientEvent);
            return true;
        }
    }

    private static class TokenFetchProcessor
            extends ClientLogsBuilder.EventProcessor {
        public TokenFetchProcessor(ClientLogsBuilder.EventProcessor paramEventProcessor) {
            super();
        }

        public boolean internalProcess(ClientLogsBuilder.LoggedEvent paramLoggedEvent, VoicesearchClientLogProto.ClientEvent paramClientEvent) {
            if (paramLoggedEvent.is(268435456, 122)) {
                int i = ((Integer) paramLoggedEvent.data).intValue();
                paramClientEvent.setAuthTokenStatus(new VoicesearchClientLogProto.AuthTokenStatus().setStatusCode(i));
            }
            return false;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.logger.ClientLogsBuilder

 * JD-Core Version:    0.7.0.1

 */