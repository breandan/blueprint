package com.google.android.speech.grammar;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class ContactsGrammarBuilder
        extends GrammarBuilder {
    private final ArrayList<GrammarContact> mGrammarContactList = Lists.newArrayList();

    private void calculateWeight() {
        if(mGrammarContactList.isEmpty()) {
            return;
        }
        int maxTimesContacted = mGrammarContactList.get(0x0).timesContacted;
        for(GrammarContact grammarContact : mGrammarContactList) {
            grammarContact.weight = getWeight((double)grammarContact.timesContacted, (double)maxTimesContacted, (double)grammarContact.lastTimeContactedElapsed);
        }
    }

    public static double getWeight(double paramDouble1, double paramDouble2, double paramDouble3) {
        return 0.5D * ((1.0D + paramDouble1) / (1.0D + paramDouble2) + Math.pow(0.5D, Math.min(paramDouble3, 15552000000.0D) / 864000000.0D));
    }

    public void addContact(GrammarContact paramGrammarContact) {
        this.mGrammarContactList.add(paramGrammarContact);
    }

    protected void appendBeforeDisjunctionRules(StringBuilder paramStringBuilder) {
        paramStringBuilder.append("$TARGET = $CONTACT;\n");
        paramStringBuilder.append("$VOICE_DIALING = $CONTACT_AND_DIGIT_DIALING;\n");
    }

    protected void appendDisjunctionAssignment(StringBuilder paramStringBuilder) {
        paramStringBuilder.append("$CONTACT = ");
    }

    protected void appendEmptyTokensRules(StringBuilder paramStringBuilder) {
        paramStringBuilder.append("$TARGET = $VOID;\n");
        paramStringBuilder.append("$VOICE_DIALING = $DIGIT_DIALING;\n");
    }

    protected Collection<GrammarToken> getGrammarTokens() {
        calculateWeight();
        HashMap localHashMap = Maps.newHashMap();
        Iterator localIterator = this.mGrammarContactList.iterator();
        while (localIterator.hasNext()) {
            ((GrammarContact) localIterator.next()).addTokens(localHashMap);
        }
        return localHashMap.values();
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.grammar.ContactsGrammarBuilder

 * JD-Core Version:    0.7.0.1

 */