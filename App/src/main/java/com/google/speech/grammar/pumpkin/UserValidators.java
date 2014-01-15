package com.google.speech.grammar.pumpkin;

import com.google.common.collect.Maps;

import java.util.Map;

public class UserValidators {
    private Map<String, com.google.speech.grammar.pumpkin.Validator> javaValidators = Maps.newHashMap();
    private long nativeUserValidators;

    public UserValidators(com.google.speech.grammar.pumpkin.PumpkinConfigProto.PumpkinConfig paramPumpkinConfig) {
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

    public synchronized void addUserValidatorFromMap(String variable, Map<String, String> arguments) {
        String[] keys = new String[arguments.size()];
        String[] values = new String[arguments.size()];
        int i = 0x0;
        for(Map.Entry<String, String> arg : arguments.entrySet()) {
            keys[i] = arg.getKey();
            values[i] = arg.getValue();
            i = i + 0x1;
        }
        nativeAddMapBasedValidator(nativeUserValidators, variable, keys, values);
    }

    public synchronized void addValidator(String paramString, com.google.speech.grammar.pumpkin.Validator paramValidator) {
        paramValidator.init();
        nativeAddJavaValidator(this.nativeUserValidators, paramString);
        this.javaValidators.put(paramString, paramValidator);
    }

    public String canonicalize(String paramString1, String paramString2) {
        com.google.speech.grammar.pumpkin.Validator localValidator = this.javaValidators.get(paramString1);
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
        com.google.speech.grammar.pumpkin.Validator localValidator = this.javaValidators.get(paramString1);
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