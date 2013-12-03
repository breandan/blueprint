package com.google.android.voicesearch.contacts;

public enum ContactSelectMode {
    private final int mActionDescResourceId;
    private final int mActionIconResourceId;
    private final int mBailOutIconResourceId;
    private final int mBailOutStringResourceId;
    private final ContactLookup.Mode mContactLookupMode;
    private final int mDetailLayoutResourceId;
    private final int mParentActionType;

    static {
        CALL_CONTACT = new ContactSelectMode("CALL_CONTACT", 1, 2130968636, 0, 2131363608, 0, 0, 10, ContactLookup.Mode.PHONE_NUMBER);
        CALL_NUMBER = new ContactSelectMode("CALL_NUMBER", 2, 2130968636, 0, 2131363608, 2130837636, 2131363597, 28, ContactLookup.Mode.PHONE_NUMBER);
        SHOW_CONTACT_INFO = new ContactSelectMode("SHOW_CONTACT_INFO", 3, 0, 0, 0, 0, 0, 33, ContactLookup.Mode.PERSON);
        SMS = new ContactSelectMode("SMS", 4, 2130968636, 0, 2131363610, 2130837642, 2131363600, 1, ContactLookup.Mode.PHONE_NUMBER);
        CALENDAR = new ContactSelectMode("CALENDAR", 5, 0, 0, 0, 2130837648, 2131363538, 12, ContactLookup.Mode.EMAIL);
        ContactSelectMode[] arrayOfContactSelectMode = new ContactSelectMode[6];
        arrayOfContactSelectMode[0] = EMAIL;
        arrayOfContactSelectMode[1] = CALL_CONTACT;
        arrayOfContactSelectMode[2] = CALL_NUMBER;
        arrayOfContactSelectMode[3] = SHOW_CONTACT_INFO;
        arrayOfContactSelectMode[4] = SMS;
        arrayOfContactSelectMode[5] = CALENDAR;
        $VALUES = arrayOfContactSelectMode;
    }

    private ContactSelectMode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ContactLookup.Mode paramMode) {
        this.mDetailLayoutResourceId = paramInt1;
        this.mActionIconResourceId = paramInt2;
        this.mActionDescResourceId = paramInt3;
        this.mBailOutIconResourceId = paramInt4;
        this.mBailOutStringResourceId = paramInt5;
        this.mParentActionType = paramInt6;
        this.mContactLookupMode = paramMode;
    }

    public ContactLookup.Mode getContactLookupMode() {
        return this.mContactLookupMode;
    }

    public int getDetailLayoutResourceId() {
        return this.mDetailLayoutResourceId;
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.contacts.ContactSelectMode

 * JD-Core Version:    0.7.0.1

 */