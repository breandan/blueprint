package com.google.wireless.voicesearch.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class CorporaConfig {
    public static final class CorporaConfiguration2
            extends MessageMicro {
        private int cachedSize = -1;
        private List<Corpus> corpus_ = Collections.emptyList();

        public static CorporaConfiguration2 parseFrom(byte[] paramArrayOfByte)
                throws InvalidProtocolBufferMicroException {
            return (CorporaConfiguration2) new CorporaConfiguration2().mergeFrom(paramArrayOfByte);
        }

        public CorporaConfiguration2 addCorpus(Corpus paramCorpus) {
            if (paramCorpus == null) {
                throw new NullPointerException();
            }
            if (this.corpus_.isEmpty()) {
                this.corpus_ = new ArrayList();
            }
            this.corpus_.add(paramCorpus);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public List<Corpus> getCorpusList() {
            return this.corpus_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getCorpusList().iterator();
            while (localIterator.hasNext()) {
                i += CodedOutputStreamMicro.computeMessageSize(1, (Corpus) localIterator.next());
            }
            this.cachedSize = i;
            return i;
        }

        public CorporaConfiguration2 mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                }
                Corpus localCorpus = new Corpus();
                paramCodedInputStreamMicro.readMessage(localCorpus);
                addCorpus(localCorpus);
            }
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getCorpusList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(1, (Corpus) localIterator.next());
            }
        }

        public static final class Corpus
                extends MessageMicro {
            private int cachedSize = -1;
            private String corpusIdentifier_ = "";
            private boolean googleApiIncludeLocation_ = false;
            private String googleApiUrl_ = "";
            private boolean hasCorpusIdentifier;
            private boolean hasGoogleApiIncludeLocation;
            private boolean hasGoogleApiUrl;
            private boolean hasIcon;
            private boolean hasMaximumAppVersion;
            private boolean hasMinimumAppVersion;
            private boolean hasName;
            private boolean hasPrefetchPattern;
            private boolean hasRequiresLocation;
            private boolean hasRequiresSizeParams;
            private boolean hasShowCards;
            private boolean hasUrlAuthority;
            private boolean hasUrlPath;
            private boolean hasWebSearchPattern;
            private String icon_ = "";
            private int maximumAppVersion_ = 0;
            private int minimumAppVersion_ = 0;
            private String name_ = "";
            private String prefetchPattern_ = "";
            private List<CorporaConfig.CorporaConfiguration2.Parameter> queryParams_ = Collections.emptyList();
            private boolean requiresLocation_ = false;
            private boolean requiresSizeParams_ = false;
            private boolean showCards_ = false;
            private List<String> supportedLocale_ = Collections.emptyList();
            private String urlAuthority_ = "";
            private List<CorporaConfig.CorporaConfiguration2.Parameter> urlParams_ = Collections.emptyList();
            private String urlPath_ = "";
            private String webSearchPattern_ = "";

            public Corpus addQueryParams(CorporaConfig.CorporaConfiguration2.Parameter paramParameter) {
                if (paramParameter == null) {
                    throw new NullPointerException();
                }
                if (this.queryParams_.isEmpty()) {
                    this.queryParams_ = new ArrayList();
                }
                this.queryParams_.add(paramParameter);
                return this;
            }

            public Corpus addSupportedLocale(String paramString) {
                if (paramString == null) {
                    throw new NullPointerException();
                }
                if (this.supportedLocale_.isEmpty()) {
                    this.supportedLocale_ = new ArrayList();
                }
                this.supportedLocale_.add(paramString);
                return this;
            }

            public Corpus addUrlParams(CorporaConfig.CorporaConfiguration2.Parameter paramParameter) {
                if (paramParameter == null) {
                    throw new NullPointerException();
                }
                if (this.urlParams_.isEmpty()) {
                    this.urlParams_ = new ArrayList();
                }
                this.urlParams_.add(paramParameter);
                return this;
            }

            public int getCachedSize() {
                if (this.cachedSize < 0) {
                    getSerializedSize();
                }
                return this.cachedSize;
            }

            public String getCorpusIdentifier() {
                return this.corpusIdentifier_;
            }

            public boolean getGoogleApiIncludeLocation() {
                return this.googleApiIncludeLocation_;
            }

            public String getGoogleApiUrl() {
                return this.googleApiUrl_;
            }

            public String getIcon() {
                return this.icon_;
            }

            public int getMaximumAppVersion() {
                return this.maximumAppVersion_;
            }

            public int getMinimumAppVersion() {
                return this.minimumAppVersion_;
            }

            public String getName() {
                return this.name_;
            }

            public String getPrefetchPattern() {
                return this.prefetchPattern_;
            }

            public List<CorporaConfig.CorporaConfiguration2.Parameter> getQueryParamsList() {
                return this.queryParams_;
            }

            public boolean getRequiresLocation() {
                return this.requiresLocation_;
            }

            public boolean getRequiresSizeParams() {
                return this.requiresSizeParams_;
            }

            public int getSerializedSize() {
                boolean bool = hasCorpusIdentifier();
                int i = 0;
                if (bool) {
                    i = 0 + CodedOutputStreamMicro.computeStringSize(1, getCorpusIdentifier());
                }
                if (hasIcon()) {
                    i += CodedOutputStreamMicro.computeStringSize(2, getIcon());
                }
                if (hasName()) {
                    i += CodedOutputStreamMicro.computeStringSize(3, getName());
                }
                if (hasWebSearchPattern()) {
                    i += CodedOutputStreamMicro.computeStringSize(4, getWebSearchPattern());
                }
                Iterator localIterator1 = getQueryParamsList().iterator();
                while (localIterator1.hasNext()) {
                    i += CodedOutputStreamMicro.computeMessageSize(5, (CorporaConfig.CorporaConfiguration2.Parameter) localIterator1.next());
                }
                if (hasUrlPath()) {
                    i += CodedOutputStreamMicro.computeStringSize(6, getUrlPath());
                }
                if (hasUrlAuthority()) {
                    i += CodedOutputStreamMicro.computeStringSize(7, getUrlAuthority());
                }
                Iterator localIterator2 = getUrlParamsList().iterator();
                while (localIterator2.hasNext()) {
                    i += CodedOutputStreamMicro.computeMessageSize(8, (CorporaConfig.CorporaConfiguration2.Parameter) localIterator2.next());
                }
                if (hasGoogleApiIncludeLocation()) {
                    i += CodedOutputStreamMicro.computeBoolSize(9, getGoogleApiIncludeLocation());
                }
                if (hasGoogleApiUrl()) {
                    i += CodedOutputStreamMicro.computeStringSize(10, getGoogleApiUrl());
                }
                if (hasRequiresLocation()) {
                    i += CodedOutputStreamMicro.computeBoolSize(11, getRequiresLocation());
                }
                int j = 0;
                Iterator localIterator3 = getSupportedLocaleList().iterator();
                while (localIterator3.hasNext()) {
                    j += CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator3.next());
                }
                int k = i + j + 1 * getSupportedLocaleList().size();
                if (hasShowCards()) {
                    k += CodedOutputStreamMicro.computeBoolSize(13, getShowCards());
                }
                if (hasPrefetchPattern()) {
                    k += CodedOutputStreamMicro.computeStringSize(14, getPrefetchPattern());
                }
                if (hasMinimumAppVersion()) {
                    k += CodedOutputStreamMicro.computeInt32Size(15, getMinimumAppVersion());
                }
                if (hasMaximumAppVersion()) {
                    k += CodedOutputStreamMicro.computeInt32Size(16, getMaximumAppVersion());
                }
                if (hasRequiresSizeParams()) {
                    k += CodedOutputStreamMicro.computeBoolSize(17, getRequiresSizeParams());
                }
                this.cachedSize = k;
                return k;
            }

            public boolean getShowCards() {
                return this.showCards_;
            }

            public List<String> getSupportedLocaleList() {
                return this.supportedLocale_;
            }

            public String getUrlAuthority() {
                return this.urlAuthority_;
            }

            public List<CorporaConfig.CorporaConfiguration2.Parameter> getUrlParamsList() {
                return this.urlParams_;
            }

            public String getUrlPath() {
                return this.urlPath_;
            }

            public String getWebSearchPattern() {
                return this.webSearchPattern_;
            }

            public boolean hasCorpusIdentifier() {
                return this.hasCorpusIdentifier;
            }

            public boolean hasGoogleApiIncludeLocation() {
                return this.hasGoogleApiIncludeLocation;
            }

            public boolean hasGoogleApiUrl() {
                return this.hasGoogleApiUrl;
            }

            public boolean hasIcon() {
                return this.hasIcon;
            }

            public boolean hasMaximumAppVersion() {
                return this.hasMaximumAppVersion;
            }

            public boolean hasMinimumAppVersion() {
                return this.hasMinimumAppVersion;
            }

            public boolean hasName() {
                return this.hasName;
            }

            public boolean hasPrefetchPattern() {
                return this.hasPrefetchPattern;
            }

            public boolean hasRequiresLocation() {
                return this.hasRequiresLocation;
            }

            public boolean hasRequiresSizeParams() {
                return this.hasRequiresSizeParams;
            }

            public boolean hasShowCards() {
                return this.hasShowCards;
            }

            public boolean hasUrlAuthority() {
                return this.hasUrlAuthority;
            }

            public boolean hasUrlPath() {
                return this.hasUrlPath;
            }

            public boolean hasWebSearchPattern() {
                return this.hasWebSearchPattern;
            }

            public Corpus mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                            setCorpusIdentifier(paramCodedInputStreamMicro.readString());
                            break;
                        case 18:
                            setIcon(paramCodedInputStreamMicro.readString());
                            break;
                        case 26:
                            setName(paramCodedInputStreamMicro.readString());
                            break;
                        case 34:
                            setWebSearchPattern(paramCodedInputStreamMicro.readString());
                            break;
                        case 42:
                            CorporaConfig.CorporaConfiguration2.Parameter localParameter2 = new CorporaConfig.CorporaConfiguration2.Parameter();
                            paramCodedInputStreamMicro.readMessage(localParameter2);
                            addQueryParams(localParameter2);
                            break;
                        case 50:
                            setUrlPath(paramCodedInputStreamMicro.readString());
                            break;
                        case 58:
                            setUrlAuthority(paramCodedInputStreamMicro.readString());
                            break;
                        case 66:
                            CorporaConfig.CorporaConfiguration2.Parameter localParameter1 = new CorporaConfig.CorporaConfiguration2.Parameter();
                            paramCodedInputStreamMicro.readMessage(localParameter1);
                            addUrlParams(localParameter1);
                            break;
                        case 72:
                            setGoogleApiIncludeLocation(paramCodedInputStreamMicro.readBool());
                            break;
                        case 82:
                            setGoogleApiUrl(paramCodedInputStreamMicro.readString());
                            break;
                        case 88:
                            setRequiresLocation(paramCodedInputStreamMicro.readBool());
                            break;
                        case 98:
                            addSupportedLocale(paramCodedInputStreamMicro.readString());
                            break;
                        case 104:
                            setShowCards(paramCodedInputStreamMicro.readBool());
                            break;
                        case 114:
                            setPrefetchPattern(paramCodedInputStreamMicro.readString());
                            break;
                        case 120:
                            setMinimumAppVersion(paramCodedInputStreamMicro.readInt32());
                            break;
                        case 128:
                            setMaximumAppVersion(paramCodedInputStreamMicro.readInt32());
                            break;
                    }
                    setRequiresSizeParams(paramCodedInputStreamMicro.readBool());
                }
            }

            public Corpus setCorpusIdentifier(String paramString) {
                this.hasCorpusIdentifier = true;
                this.corpusIdentifier_ = paramString;
                return this;
            }

            public Corpus setGoogleApiIncludeLocation(boolean paramBoolean) {
                this.hasGoogleApiIncludeLocation = true;
                this.googleApiIncludeLocation_ = paramBoolean;
                return this;
            }

            public Corpus setGoogleApiUrl(String paramString) {
                this.hasGoogleApiUrl = true;
                this.googleApiUrl_ = paramString;
                return this;
            }

            public Corpus setIcon(String paramString) {
                this.hasIcon = true;
                this.icon_ = paramString;
                return this;
            }

            public Corpus setMaximumAppVersion(int paramInt) {
                this.hasMaximumAppVersion = true;
                this.maximumAppVersion_ = paramInt;
                return this;
            }

            public Corpus setMinimumAppVersion(int paramInt) {
                this.hasMinimumAppVersion = true;
                this.minimumAppVersion_ = paramInt;
                return this;
            }

            public Corpus setName(String paramString) {
                this.hasName = true;
                this.name_ = paramString;
                return this;
            }

            public Corpus setPrefetchPattern(String paramString) {
                this.hasPrefetchPattern = true;
                this.prefetchPattern_ = paramString;
                return this;
            }

            public Corpus setRequiresLocation(boolean paramBoolean) {
                this.hasRequiresLocation = true;
                this.requiresLocation_ = paramBoolean;
                return this;
            }

            public Corpus setRequiresSizeParams(boolean paramBoolean) {
                this.hasRequiresSizeParams = true;
                this.requiresSizeParams_ = paramBoolean;
                return this;
            }

            public Corpus setShowCards(boolean paramBoolean) {
                this.hasShowCards = true;
                this.showCards_ = paramBoolean;
                return this;
            }

            public Corpus setUrlAuthority(String paramString) {
                this.hasUrlAuthority = true;
                this.urlAuthority_ = paramString;
                return this;
            }

            public Corpus setUrlPath(String paramString) {
                this.hasUrlPath = true;
                this.urlPath_ = paramString;
                return this;
            }

            public Corpus setWebSearchPattern(String paramString) {
                this.hasWebSearchPattern = true;
                this.webSearchPattern_ = paramString;
                return this;
            }

            public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasCorpusIdentifier()) {
                    paramCodedOutputStreamMicro.writeString(1, getCorpusIdentifier());
                }
                if (hasIcon()) {
                    paramCodedOutputStreamMicro.writeString(2, getIcon());
                }
                if (hasName()) {
                    paramCodedOutputStreamMicro.writeString(3, getName());
                }
                if (hasWebSearchPattern()) {
                    paramCodedOutputStreamMicro.writeString(4, getWebSearchPattern());
                }
                Iterator localIterator1 = getQueryParamsList().iterator();
                while (localIterator1.hasNext()) {
                    paramCodedOutputStreamMicro.writeMessage(5, (CorporaConfig.CorporaConfiguration2.Parameter) localIterator1.next());
                }
                if (hasUrlPath()) {
                    paramCodedOutputStreamMicro.writeString(6, getUrlPath());
                }
                if (hasUrlAuthority()) {
                    paramCodedOutputStreamMicro.writeString(7, getUrlAuthority());
                }
                Iterator localIterator2 = getUrlParamsList().iterator();
                while (localIterator2.hasNext()) {
                    paramCodedOutputStreamMicro.writeMessage(8, (CorporaConfig.CorporaConfiguration2.Parameter) localIterator2.next());
                }
                if (hasGoogleApiIncludeLocation()) {
                    paramCodedOutputStreamMicro.writeBool(9, getGoogleApiIncludeLocation());
                }
                if (hasGoogleApiUrl()) {
                    paramCodedOutputStreamMicro.writeString(10, getGoogleApiUrl());
                }
                if (hasRequiresLocation()) {
                    paramCodedOutputStreamMicro.writeBool(11, getRequiresLocation());
                }
                Iterator localIterator3 = getSupportedLocaleList().iterator();
                while (localIterator3.hasNext()) {
                    paramCodedOutputStreamMicro.writeString(12, (String) localIterator3.next());
                }
                if (hasShowCards()) {
                    paramCodedOutputStreamMicro.writeBool(13, getShowCards());
                }
                if (hasPrefetchPattern()) {
                    paramCodedOutputStreamMicro.writeString(14, getPrefetchPattern());
                }
                if (hasMinimumAppVersion()) {
                    paramCodedOutputStreamMicro.writeInt32(15, getMinimumAppVersion());
                }
                if (hasMaximumAppVersion()) {
                    paramCodedOutputStreamMicro.writeInt32(16, getMaximumAppVersion());
                }
                if (hasRequiresSizeParams()) {
                    paramCodedOutputStreamMicro.writeBool(17, getRequiresSizeParams());
                }
            }
        }

        public static final class Parameter
                extends MessageMicro {
            private int cachedSize = -1;
            private boolean hasName;
            private boolean hasValue;
            private String name_ = "";
            private String value_ = "";

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
                if (hasValue()) {
                    i += CodedOutputStreamMicro.computeStringSize(2, getValue());
                }
                this.cachedSize = i;
                return i;
            }

            public String getValue() {
                return this.value_;
            }

            public boolean hasName() {
                return this.hasName;
            }

            public boolean hasValue() {
                return this.hasValue;
            }

            public Parameter mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                    setValue(paramCodedInputStreamMicro.readString());
                }
            }

            public Parameter setName(String paramString) {
                this.hasName = true;
                this.name_ = paramString;
                return this;
            }

            public Parameter setValue(String paramString) {
                this.hasValue = true;
                this.value_ = paramString;
                return this;
            }

            public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasName()) {
                    paramCodedOutputStreamMicro.writeString(1, getName());
                }
                if (hasValue()) {
                    paramCodedOutputStreamMicro.writeString(2, getValue());
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.wireless.voicesearch.proto.CorporaConfig

 * JD-Core Version:    0.7.0.1

 */