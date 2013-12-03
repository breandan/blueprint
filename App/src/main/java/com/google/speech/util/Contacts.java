package com.google.speech.util;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;

public final class Contacts {
    public static final class TopContact
            extends MessageMicro {
        private float affinity_ = 0.01F;
        private int cachedSize = -1;
        private boolean hasAffinity;
        private boolean hasName;
        private String name_ = "";

        public float getAffinity() {
            return this.affinity_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getName() {
            return this.name_;
        }

        public int getSerializedSize() {
            boolean bool = hasName();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getName());
            }
            if (hasAffinity()) {
                i += CodedOutputStreamMicro.computeFloatSize(2, getAffinity());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasAffinity() {
            return this.hasAffinity;
        }

        public boolean hasName() {
            return this.hasName;
        }

        public TopContact mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
                throws IOException {
            for (; ; ) {
                int i = paramCodedInputStreamMicro.readTag();
                switch (i) {
                    default:
                        if (parseUnknownField(paramCodedInputStreamMicro, i)) {
                            continue;
                        }
                    case 0:
                        return this;
                    case 10:
                        setName(paramCodedInputStreamMicro.readString());
                        break;
                }
                setAffinity(paramCodedInputStreamMicro.readFloat());
            }
        }

        public TopContact setAffinity(float paramFloat) {
            this.hasAffinity = true;
            this.affinity_ = paramFloat;
            return this;
        }

        public TopContact setName(String paramString) {
            this.hasName = true;
            this.name_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasName()) {
                paramCodedOutputStreamMicro.writeString(1, getName());
            }
            if (hasAffinity()) {
                paramCodedOutputStreamMicro.writeFloat(2, getAffinity());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.speech.util.Contacts

 * JD-Core Version:    0.7.0.1

 */