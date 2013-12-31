package com.embryo.speech.grammar.pumpkin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserValidators {
    private Map<String, com.embryo.speech.grammar.pumpkin.Validator> javaValidators = new HashMap();
    private long nativeUserValidators;

    public UserValidators(com.embryo.speech.grammar.pumpkin.PumpkinConfigProto.PumpkinConfig paramPumpkinConfig) {
        this(paramPumpkinConfig.toByteArray());
    }

    public UserValidators(byte[] paramArrayOfByte) {
        nativeUserValidators = nativeCreate(paramArrayOfByte);
        if (this.nativeUserValidators == 0L) {
            throw new NullPointerException("Couldn't create UserValidator native object from the provided config");
        }
    }

    private synchronized void delete() {
        nativeDelete(this.nativeUserValidators);
    }

    private native void nativeAddJavaValidator(long paramLong, String paramString);

    private native void nativeAddMapBasedValidator(long paramLong, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2);

    private native void nativeAddUserValidator(long paramLong, String paramString, String[] paramArrayOfString);

    private native long nativeCreate(byte[] paramArrayOfByte);

    private native void nativeDelete(long paramLong);

    private native void nativeSetContacts(long paramLong, String[] paramArrayOfString);

    public synchronized void addUserValidator(String paramString, String[] paramArrayOfString) {
        nativeAddUserValidator(this.nativeUserValidators, paramString, paramArrayOfString);
    }

    public synchronized void addUserValidatorFromMap(String paramString, Map<String, String> paramMap) {
        String[] arrayOfString1 = new String[paramMap.size()];
        String[] arrayOfString2 = new String[paramMap.size()];
        int i = 0;
        Iterator localIterator = paramMap.entrySet().iterator();
        while (localIterator.hasNext()) {
            Map.Entry localEntry = (Map.Entry) localIterator.next();
            arrayOfString1[i] = ((String) localEntry.getKey());
            arrayOfString2[i] = ((String) localEntry.getValue());
            i++;
        }
        nativeAddMapBasedValidator(this.nativeUserValidators, paramString, arrayOfString1, arrayOfString2);
    }

    public synchronized void addValidator(String paramString, com.embryo.speech.grammar.pumpkin.Validator paramValidator) {
        paramValidator.init();
        nativeAddJavaValidator(this.nativeUserValidators, paramString);
        this.javaValidators.put(paramString, paramValidator);
    }

    public String canonicalize(String paramString1, String paramString2) {
        com.embryo.speech.grammar.pumpkin.Validator localValidator = (com.embryo.speech.grammar.pumpkin.Validator) this.javaValidators.get(paramString1);
        if (localValidator == null) {
            throw new NullPointerException("Java validator should exist at this point.");
        }
        return localValidator.canonicalizeArgument(paramString2);
    }

    protected void finalize() {
        delete();
    }

    public long getNativeUserValidators() {
        return this.nativeUserValidators;
    }

    public float getPosterior(String paramString1, String paramString2) {
        com.embryo.speech.grammar.pumpkin.Validator localValidator = (com.embryo.speech.grammar.pumpkin.Validator) this.javaValidators.get(paramString1);
        if (localValidator == null) {
            throw new NullPointerException("Java validator should exist at this point.");
        }
        return localValidator.getPosterior(paramString2);
    }

    public synchronized void setContacts(String[] paramArrayOfString) {
        nativeSetContacts(this.nativeUserValidators, paramArrayOfString);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     UserValidators

 * JD-Core Version:    0.7.0.1

 */