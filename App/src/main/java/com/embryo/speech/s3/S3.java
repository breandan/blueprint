package com.embryo.speech.s3;

import com.embryo.protobuf.micro.CodedInputStreamMicro;
import com.embryo.protobuf.micro.CodedOutputStreamMicro;
import com.embryo.protobuf.micro.MessageMicro;
import com.embryo.speech.speech.s3.Recognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class S3 {
    public static final class Locale
            extends MessageMicro {
        private int cachedSize = -1;
        private int format_ = 0;
        private boolean hasFormat;
        private boolean hasLocale;
        private String locale_ = "";

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public int getFormat() {
            return this.format_;
        }

        public Locale setFormat(int paramInt) {
            this.hasFormat = true;
            this.format_ = paramInt;
            return this;
        }

        public String getLocale() {
            return this.locale_;
        }

        public Locale setLocale(String paramString) {
            this.hasLocale = true;
            this.locale_ = paramString;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasLocale();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getLocale());
            }
            if (hasFormat()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getFormat());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasFormat() {
            return this.hasFormat;
        }

        public boolean hasLocale() {
            return this.hasLocale;
        }

        public Locale mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setLocale(paramCodedInputStreamMicro.readString());
                        break;
                }
                setFormat(paramCodedInputStreamMicro.readInt32());
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasLocale()) {
                paramCodedOutputStreamMicro.writeString(1, getLocale());
            }
            if (hasFormat()) {
                paramCodedOutputStreamMicro.writeInt32(2, getFormat());
            }
        }
    }

    public static final class S3Response
            extends MessageMicro {
        private int cachedSize = -1;
        private List<String> debugLine_ = Collections.emptyList();
        private int errorCode_ = 0;
        private String errorDescription_ = "";
        private boolean hasErrorCode;
        private boolean hasErrorDescription;
        private boolean hasGogglesStreamResponseExtension;
        private boolean hasMajelServiceEventExtension;
        private boolean hasRecognizerEventExtension;
        private boolean hasSoundSearchServiceEventExtension;
        private boolean hasStatus;
        private boolean hasTtsCapabilitiesResponseExtension;
        private boolean hasTtsServiceEventExtension;
        private Recognizer.RecognizerEvent recognizerEventExtension_ = null;
        private int status_ = 0;

        public S3Response addDebugLine(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.debugLine_.isEmpty()) {
                this.debugLine_ = new ArrayList();
            }
            this.debugLine_.add(paramString);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<String> getDebugLineList() {
            return this.debugLine_;
        }

        public int getErrorCode() {
            return this.errorCode_;
        }

        public S3Response setErrorCode(int paramInt) {
            this.hasErrorCode = true;
            this.errorCode_ = paramInt;
            return this;
        }

        public String getErrorDescription() {
            return this.errorDescription_;
        }

        public S3Response setErrorDescription(String paramString) {
            this.hasErrorDescription = true;
            this.errorDescription_ = paramString;
            return this;
        }

        public Recognizer.RecognizerEvent getRecognizerEventExtension() {
            return this.recognizerEventExtension_;
        }

        public S3Response setRecognizerEventExtension(Recognizer.RecognizerEvent paramRecognizerEvent) {
            if (paramRecognizerEvent == null) {
                throw new NullPointerException();
            }
            this.hasRecognizerEventExtension = true;
            this.recognizerEventExtension_ = paramRecognizerEvent;
            return this;
        }

        public int getSerializedSize() {
            boolean bool = hasStatus();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeInt32Size(1, getStatus());
            }
            if (hasErrorCode()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getErrorCode());
            }
            if (hasErrorDescription()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getErrorDescription());
            }
            int j = 0;
            Iterator localIterator = getDebugLineList().iterator();
            while (localIterator.hasNext()) {
                j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator.next());
            }
            int k = i + j + 1 * getDebugLineList().size();
            if (hasRecognizerEventExtension()) {
                k += CodedOutputStreamMicro.computeMessageSize(1253625, getRecognizerEventExtension());
            }
            this.cachedSize = k;
            return k;
        }

        public int getStatus() {
            return this.status_;
        }

        public S3Response setStatus(int paramInt) {
            this.hasStatus = true;
            this.status_ = paramInt;
            return this;
        }

        public boolean hasErrorCode() {
            return this.hasErrorCode;
        }

        public boolean hasErrorDescription() {
            return this.hasErrorDescription;
        }

        public boolean hasRecognizerEventExtension() {
            return this.hasRecognizerEventExtension;
        }

        public boolean hasStatus() {
            return this.hasStatus;
        }

        public S3Response mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                    case 8:
                        setStatus(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 16:
                        setErrorCode(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setErrorDescription(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        addDebugLine(paramCodedInputStreamMicro.readString());
                        break;
                    case 10029002:
                        Recognizer.RecognizerEvent localRecognizerEvent = new Recognizer.RecognizerEvent();
                        paramCodedInputStreamMicro.readMessage(localRecognizerEvent);
                        setRecognizerEventExtension(localRecognizerEvent);
                        break;
                }
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasStatus()) {
                paramCodedOutputStreamMicro.writeInt32(1, getStatus());
            }
            if (hasErrorCode()) {
                paramCodedOutputStreamMicro.writeInt32(2, getErrorCode());
            }
            if (hasErrorDescription()) {
                paramCodedOutputStreamMicro.writeString(3, getErrorDescription());
            }
            Iterator localIterator = getDebugLineList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeString(4, (String) localIterator.next());
            }
            if (hasRecognizerEventExtension()) {
                paramCodedOutputStreamMicro.writeMessage(1253625, getRecognizerEventExtension());
            }
        }
    }

}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.speech.s3.S3
 * JD-Core Version:    0.7.0.1
 */