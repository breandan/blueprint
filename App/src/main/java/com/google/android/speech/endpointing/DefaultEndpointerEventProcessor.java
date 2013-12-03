package com.google.android.speech.endpointing;

import android.util.Log;

import com.google.android.shared.util.StateMachine;
import com.google.android.speech.listeners.RecognitionEventListener;
import com.google.common.base.Preconditions;

public class DefaultEndpointerEventProcessor
        implements EndpointerEventProcessor {
    private long mEndOfSpeechTriggerMs;
    private final RecognitionEventListener mEndpointerListener;
    private GstaticConfiguration.EndpointerParams mEndpointerParams;
    private final StateMachine<State> mStateMachine = StateMachine.newBuilder("VS.DefaultEndpointerEventProcessor", State.NO_SPEECH_DETECTED).addTransition(State.NO_SPEECH_DETECTED, State.SPEECH_DETECTED).addTransition(State.NO_SPEECH_DETECTED, State.END_OF_SPEECH).addTransition(State.SPEECH_DETECTED, State.DELAY_END_OF_SPEECH).addTransition(State.SPEECH_DETECTED, State.END_OF_SPEECH).addTransition(State.DELAY_END_OF_SPEECH, State.SPEECH_DETECTED).addTransition(State.DELAY_END_OF_SPEECH, State.END_OF_SPEECH).setDebug(false).build();

    public DefaultEndpointerEventProcessor(RecognitionEventListener paramRecognitionEventListener, GstaticConfiguration.EndpointerParams paramEndpointerParams) {
        this.mEndpointerListener = ((RecognitionEventListener) Preconditions.checkNotNull(paramRecognitionEventListener));
        this.mEndpointerParams = paramEndpointerParams;
    }

    /* Error */
    private boolean processEndOfAudioAsEndOfSpeech() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   6: getstatic 37	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   9: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   12: ifeq +19 -> 31
        //   15: aload_0
        //   16: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   19: getstatic 46	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   22: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   25: iconst_1
        //   26: istore_2
        //   27: aload_0
        //   28: monitorexit
        //   29: iload_2
        //   30: ireturn
        //   31: iconst_0
        //   32: istore_2
        //   33: goto -6 -> 27
        //   36: astore_1
        //   37: aload_0
        //   38: monitorexit
        //   39: aload_1
        //   40: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	41	0	this	DefaultEndpointerEventProcessor
        //   36	4	1	localObject	Object
        //   26	7	2	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	25	36	finally
    }

    /* Error */
    private boolean processEndOfAudioAsNoSpeechDetected() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   6: getstatic 28	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:NO_SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   9: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   12: ifeq +19 -> 31
        //   15: aload_0
        //   16: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   19: getstatic 46	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   22: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   25: iconst_1
        //   26: istore_2
        //   27: aload_0
        //   28: monitorexit
        //   29: iload_2
        //   30: ireturn
        //   31: iconst_0
        //   32: istore_2
        //   33: goto -6 -> 27
        //   36: astore_1
        //   37: aload_0
        //   38: monitorexit
        //   39: aload_1
        //   40: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	41	0	this	DefaultEndpointerEventProcessor
        //   36	4	1	localObject	Object
        //   26	7	2	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	25	36	finally
    }

    /* Error */
    private boolean processEndOfSpeech(long paramLong) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 71	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mEndpointerParams	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams;
        //   6: invokevirtual 90	com/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams:getExtraSilenceAfterEndOfSpeechMsec	()I
        //   9: ifle +35 -> 44
        //   12: aload_0
        //   13: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   16: getstatic 49	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:DELAY_END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   19: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   22: aload_0
        //   23: lload_1
        //   24: aload_0
        //   25: getfield 71	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mEndpointerParams	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams;
        //   28: invokevirtual 90	com/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams:getExtraSilenceAfterEndOfSpeechMsec	()I
        //   31: i2l
        //   32: ladd
        //   33: invokespecial 94	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:setEndOfSpeechTriggerPoint	(J)V
        //   36: iconst_0
        //   37: istore 4
        //   39: aload_0
        //   40: monitorexit
        //   41: iload 4
        //   43: ireturn
        //   44: aload_0
        //   45: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   48: getstatic 46	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   51: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   54: iconst_1
        //   55: istore 4
        //   57: goto -18 -> 39
        //   60: astore_3
        //   61: aload_0
        //   62: monitorexit
        //   63: aload_3
        //   64: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	65	0	this	DefaultEndpointerEventProcessor
        //   0	65	1	paramLong	long
        //   60	4	3	localObject	Object
        //   37	19	4	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	36	60	finally
        //   44	54	60	finally
    }

    /* Error */
    private boolean processStartOfSpeech() {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   6: getstatic 28	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:NO_SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   9: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   12: ifeq +19 -> 31
        //   15: aload_0
        //   16: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   19: getstatic 37	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   22: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   25: iconst_1
        //   26: istore_3
        //   27: aload_0
        //   28: monitorexit
        //   29: iload_3
        //   30: ireturn
        //   31: aload_0
        //   32: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   35: getstatic 49	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:DELAY_END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   38: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   41: istore_2
        //   42: iconst_0
        //   43: istore_3
        //   44: iload_2
        //   45: ifeq -18 -> 27
        //   48: aload_0
        //   49: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   52: getstatic 37	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   55: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   58: iconst_0
        //   59: istore_3
        //   60: goto -33 -> 27
        //   63: astore_1
        //   64: aload_0
        //   65: monitorexit
        //   66: aload_1
        //   67: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	68	0	this	DefaultEndpointerEventProcessor
        //   63	4	1	localObject	Object
        //   41	4	2	bool1	boolean
        //   26	34	3	bool2	boolean
        // Exception table:
        //   from	to	target	type
        //   2	25	63	finally
        //   31	42	63	finally
        //   48	58	63	finally
    }

    private void setEndOfSpeechTriggerPoint(long paramLong) {
        try {
            this.mEndOfSpeechTriggerMs = paramLong;
            return;
        } finally {
            localObject =finally;
            throw localObject;
        }
    }

    /* Error */
    private boolean shouldTriggerEndOfSpeech(long paramLong) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: lload_1
        //   3: aload_0
        //   4: getfield 97	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mEndOfSpeechTriggerMs	J
        //   7: lcmp
        //   8: ifle +34 -> 42
        //   11: aload_0
        //   12: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   15: getstatic 49	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:DELAY_END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   18: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   21: ifeq +21 -> 42
        //   24: aload_0
        //   25: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   28: getstatic 46	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   31: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   34: iconst_1
        //   35: istore 4
        //   37: aload_0
        //   38: monitorexit
        //   39: iload 4
        //   41: ireturn
        //   42: iconst_0
        //   43: istore 4
        //   45: goto -8 -> 37
        //   48: astore_3
        //   49: aload_0
        //   50: monitorexit
        //   51: aload_3
        //   52: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	53	0	this	DefaultEndpointerEventProcessor
        //   0	53	1	paramLong	long
        //   48	4	3	localObject	Object
        //   35	9	4	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	34	48	finally
    }

    /* Error */
    private boolean shouldTriggerNoSpeechDetected(long paramLong) {
        // Byte code:
        //   0: aload_0
        //   1: monitorenter
        //   2: aload_0
        //   3: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   6: getstatic 28	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:NO_SPEECH_DETECTED	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   9: invokevirtual 77	com/google/android/shared/util/StateMachine:isIn	(Ljava/lang/Enum;)Z
        //   12: ifeq +34 -> 46
        //   15: lload_1
        //   16: aload_0
        //   17: getfield 71	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mEndpointerParams	Lcom/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams;
        //   20: invokevirtual 102	com/google/wireless/voicesearch/proto/GstaticConfiguration$EndpointerParams:getNoSpeechDetectedTimeoutMsec	()I
        //   23: i2l
        //   24: lcmp
        //   25: ifle +21 -> 46
        //   28: aload_0
        //   29: getfield 59	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor:mStateMachine	Lcom/google/android/shared/util/StateMachine;
        //   32: getstatic 46	com/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State:END_OF_SPEECH	Lcom/google/android/speech/endpointing/DefaultEndpointerEventProcessor$State;
        //   35: invokevirtual 81	com/google/android/shared/util/StateMachine:moveTo	(Ljava/lang/Enum;)V
        //   38: iconst_1
        //   39: istore 4
        //   41: aload_0
        //   42: monitorexit
        //   43: iload 4
        //   45: ireturn
        //   46: iconst_0
        //   47: istore 4
        //   49: goto -8 -> 41
        //   52: astore_3
        //   53: aload_0
        //   54: monitorexit
        //   55: aload_3
        //   56: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	57	0	this	DefaultEndpointerEventProcessor
        //   0	57	1	paramLong	long
        //   52	4	3	localObject	Object
        //   39	9	4	bool	boolean
        // Exception table:
        //   from	to	target	type
        //   2	38	52	finally
    }

    public void process(RecognizerProtos.EndpointerEvent paramEndpointerEvent) {
        if ((paramEndpointerEvent == null) || (!paramEndpointerEvent.hasEventType())) {
        }
        label19:
        do {
            int i;
            do {
                do {
                    do {
                        Log.w("VS.DefaultEndpointerEventProcessor", "Received EP event without type.");
                        break label19;
                        break label19;
                        do {
                            return;
                        } while (this.mStateMachine.isIn(State.END_OF_SPEECH));
                        i = paramEndpointerEvent.getEventType();
                        if (i != 0) {
                            break;
                        }
                    } while (!processStartOfSpeech());
                    this.mEndpointerListener.onBeginningOfSpeech(paramEndpointerEvent.getTimeUsec() / 1000L);
                    return;
                    if (i != 1) {
                        break;
                    }
                } while (!processEndOfSpeech(paramEndpointerEvent.getTimeUsec() / 1000L));
                this.mEndpointerListener.onEndOfSpeech();
                return;
            } while (i != 2);
            if (processEndOfAudioAsEndOfSpeech()) {
                this.mEndpointerListener.onEndOfSpeech();
            }
        } while (!processEndOfAudioAsNoSpeechDetected());
        this.mEndpointerListener.onNoSpeechDetected();
    }

    public void updateProgress(int paramInt, long paramLong) {
        try {
            if (shouldTriggerEndOfSpeech(paramLong)) {
                this.mEndpointerListener.onEndOfSpeech();
            }
            if (shouldTriggerNoSpeechDetected(paramLong)) {
                this.mEndpointerListener.onNoSpeechDetected();
            }
            return;
        } finally {
        }
    }

    private static enum State {
        static {
            DELAY_END_OF_SPEECH = new State("DELAY_END_OF_SPEECH", 2);
            END_OF_SPEECH = new State("END_OF_SPEECH", 3);
            State[] arrayOfState = new State[4];
            arrayOfState[0] = NO_SPEECH_DETECTED;
            arrayOfState[1] = SPEECH_DETECTED;
            arrayOfState[2] = DELAY_END_OF_SPEECH;
            arrayOfState[3] = END_OF_SPEECH;
            $VALUES = arrayOfState;
        }

        private State() {
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.endpointing.DefaultEndpointerEventProcessor

 * JD-Core Version:    0.7.0.1

 */