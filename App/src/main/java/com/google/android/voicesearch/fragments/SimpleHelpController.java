package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.fragments.action.HelpAction;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.majel.proto.ActionDateTimeProtos.ActionTime;
import com.google.majel.proto.ActionV2Protos.HelpAction.Feature.Example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimpleHelpController {
    private static final SimpleDateFormat DAY_FORMAT;
    private static final SimpleDateFormat DAY_OF_WEEK_FORMAT = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat HOUR_12_FORMAT = new SimpleDateFormat("KK");
    private static final SimpleDateFormat HOUR_24_FORMAT;
    private static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("mm");
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat("MMMM");
    private boolean isIntroductionCard;
    private Map<Integer, Iterator<ExampleContactHelper.Contact>> mExampleContacts;
    private int mExampleIndex = 0;
    private List<ActionV2Protos.HelpAction.Feature.Example> mExamples;
    private String mHeadline;
    private final SimpleDateFormat mHourFormat;
    private String mIntroductionMessage;
    private Ui mUi;

    static {
        DAY_FORMAT = new SimpleDateFormat("dd");
        HOUR_24_FORMAT = new SimpleDateFormat("HH");
    }

    public SimpleHelpController(HelpAction paramHelpAction, boolean paramBoolean) {
        if (paramBoolean) {
        }
        for (SimpleDateFormat localSimpleDateFormat = HOUR_24_FORMAT; ; localSimpleDateFormat = HOUR_12_FORMAT) {
            this.mHourFormat = localSimpleDateFormat;
            if (paramHelpAction.isIntroduction()) {
                break;
            }
            initialize(paramHelpAction.getHeadline(), paramHelpAction.getExampleList(), paramHelpAction.getExampleContacts());
            return;
        }
        initialize(paramHelpAction.getHeadline(), paramHelpAction.getIntroductionMessage());
    }

    private String formatQuery(String paramString, List<Integer> paramList, Calendar paramCalendar, ExampleContactHelper.Contact paramContact) {
        String str = paramString;
        int i = 0;
        Iterator localIterator = paramList.iterator();
        if (localIterator.hasNext()) {
            switch (((Integer) localIterator.next()).intValue()) {
            }
            for (; ; ) {
                i++;
                break;
                if (paramCalendar != null) {
                    str = substitutePlaceholder(str, i, DAY_OF_WEEK_FORMAT.format(paramCalendar.getTime()));
                    continue;
                    if (paramCalendar != null) {
                        str = substitutePlaceholder(str, i, DAY_FORMAT.format(paramCalendar.getTime()));
                        continue;
                        if (paramCalendar != null) {
                            str = substitutePlaceholder(str, i, MONTH_FORMAT.format(paramCalendar.getTime()));
                            continue;
                            if (paramContact != null) {
                                str = substitutePlaceholder(str, i, paramContact.firstName);
                            }
                        }
                    }
                }
            }
        }
        return str.replaceAll("%%", "%");
    }

    private void initialize(String paramString1, String paramString2) {
        this.isIntroductionCard = true;
        this.mHeadline = paramString1;
        this.mIntroductionMessage = paramString2;
    }

    private void initialize(String paramString, List<ActionV2Protos.HelpAction.Feature.Example> paramList, Map<Integer, List<ExampleContactHelper.Contact>> paramMap) {
        this.isIntroductionCard = false;
        this.mHeadline = paramString;
        this.mExamples = paramList;
        if (paramMap.size() > 0) {
            this.mExampleContacts = Maps.newHashMapWithExpectedSize(paramMap.size());
            Iterator localIterator = paramMap.keySet().iterator();
            while (localIterator.hasNext()) {
                Integer localInteger = (Integer) localIterator.next();
                this.mExampleContacts.put(localInteger, Iterators.cycle((Iterable) paramMap.get(localInteger)));
            }
        }
    }

    private ExampleContactHelper.Contact selectContactFor(ActionV2Protos.HelpAction.Feature.Example paramExample) {
        int i = HelpAction.getContactSubstitutionType(paramExample);
        if (i != 0) {
            return (ExampleContactHelper.Contact) ((Iterator) this.mExampleContacts.get(Integer.valueOf(i))).next();
        }
        return null;
    }

    private void showExample(ActionV2Protos.HelpAction.Feature.Example paramExample) {
        String str = paramExample.getQuery();
        Calendar localCalendar = null;
        Date localDate1 = new Date();
        int i = 0;
        ExampleContactHelper.Contact localContact = selectContactFor(paramExample);
        if (paramExample.hasImageUrl()) {
            this.mUi.setPreviewUrl(paramExample.getImageUrl());
        }
        while (paramExample.getSubstitutionCount() > 0) {
            this.mUi.setExampleQuery(formatQuery(str, paramExample.getSubstitutionList(), localCalendar, localContact));
            return;
            if (localContact != null) {
                this.mUi.setPreviewContact(localContact);
                localCalendar = null;
            } else if ((!paramExample.hasRelativeDays()) && (!paramExample.hasRelativeSeconds())) {
                boolean bool2 = paramExample.hasTime();
                localCalendar = null;
                if (!bool2) {
                }
            } else {
                localCalendar = Calendar.getInstance();
                localCalendar.setTime(localDate1);
                if (paramExample.hasRelativeSeconds()) {
                    localCalendar.add(13, paramExample.getRelativeSeconds());
                }
                for (; ; ) {
                    if (paramExample.hasTime()) {
                        ActionDateTimeProtos.ActionTime localActionTime = paramExample.getTime();
                        localCalendar.set(11, localActionTime.getHour());
                        localCalendar.set(12, localActionTime.getMinute());
                        if ((!paramExample.hasRelativeDays()) && (localCalendar.getTime().before(localDate1))) {
                            localCalendar.add(5, 1);
                        }
                    }
                    if (i == 0) {
                        break label286;
                    }
                    Date localDate3 = localCalendar.getTime();
                    this.mUi.setPreviewDate(DAY_OF_WEEK_FORMAT.format(localDate3), DAY_FORMAT.format(localDate3), MONTH_FORMAT.format(localDate3));
                    break;
                    boolean bool1 = paramExample.hasRelativeDays();
                    i = 0;
                    if (bool1) {
                        i = 1;
                        localCalendar.add(5, paramExample.getRelativeDays());
                    }
                }
                label286:
                Date localDate2 = localCalendar.getTime();
                this.mUi.setPreviewTime(this.mHourFormat.format(localDate2), MINUTE_FORMAT.format(localDate2));
            }
        }
        this.mUi.setExampleQuery(str);
    }

    static String substitutePlaceholder(String paramString1, int paramInt, String paramString2) {
        String str = paramString2.replace("\\", "\\\\").replace("$", "\\$");
        return paramString1.replaceAll("(?<!%)%" + (paramInt + 1) + "(?![0-9])", str);
    }

    public void attachUi(Ui paramUi) {
        if (this.isIntroductionCard) {
            paramUi.setIntroduction(this.mHeadline, this.mIntroductionMessage);
            return;
        }
        this.mUi = paramUi;
        paramUi.setHeadline(this.mHeadline);
        this.mExampleIndex = 0;
        showExample((ActionV2Protos.HelpAction.Feature.Example) this.mExamples.get(this.mExampleIndex));
    }

    public void showNextExample() {
        if (!this.isIntroductionCard) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mExampleIndex = (1 + this.mExampleIndex);
            if (this.mExampleIndex >= this.mExamples.size()) {
                this.mExampleIndex = 0;
            }
            showExample((ActionV2Protos.HelpAction.Feature.Example) this.mExamples.get(this.mExampleIndex));
            return;
        }
    }

    public static abstract interface Ui {
        public abstract void setExampleQuery(String paramString);

        public abstract void setHeadline(String paramString);

        public abstract void setIntroduction(String paramString1, String paramString2);

        public abstract void setPreviewContact(ExampleContactHelper.Contact paramContact);

        public abstract void setPreviewDate(String paramString1, String paramString2, String paramString3);

        public abstract void setPreviewTime(String paramString1, String paramString2);

        public abstract void setPreviewUrl(String paramString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.fragments.SimpleHelpController

 * JD-Core Version:    0.7.0.1

 */