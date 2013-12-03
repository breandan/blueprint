package com.google.wireless.voicesearch.proto;

import com.google.protobuf.micro.CodedInputStreamMicro;
import com.google.protobuf.micro.CodedOutputStreamMicro;
import com.google.protobuf.micro.MessageMicro;

import java.io.IOException;

public final class CardMetdataProtos {
    public static final class CardMetadata
            extends MessageMicro {
        private int answerType_ = 0;
        private int cachedSize = -1;
        private boolean hasAnswerType;
        private boolean hasLoggingUrls;
        private boolean hasQueryAlternatives;
        private boolean hasRewrittenQuery;
        private CardMetdataProtos.LoggingUrls loggingUrls_ = null;
        private QueryAlternativesProto.QueryAlternatives queryAlternatives_ = null;
        private String rewrittenQuery_ = "";

        public int getAnswerType() {
            return this.answerType_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public CardMetdataProtos.LoggingUrls getLoggingUrls() {
            return this.loggingUrls_;
        }

        public QueryAlternativesProto.QueryAlternatives getQueryAlternatives() {
            return this.queryAlternatives_;
        }

        public String getRewrittenQuery() {
            return this.rewrittenQuery_;
        }

        public int getSerializedSize() {
            boolean bool = hasLoggingUrls();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeMessageSize(1, getLoggingUrls());
            }
            if (hasAnswerType()) {
                i += CodedOutputStreamMicro.computeInt32Size(2, getAnswerType());
            }
            if (hasRewrittenQuery()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getRewrittenQuery());
            }
            if (hasQueryAlternatives()) {
                i += CodedOutputStreamMicro.computeMessageSize(4, getQueryAlternatives());
            }
            this.cachedSize = i;
            return i;
        }

        public boolean hasAnswerType() {
            return this.hasAnswerType;
        }

        public boolean hasLoggingUrls() {
            return this.hasLoggingUrls;
        }

        public boolean hasQueryAlternatives() {
            return this.hasQueryAlternatives;
        }

        public boolean hasRewrittenQuery() {
            return this.hasRewrittenQuery;
        }

        public CardMetadata mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        CardMetdataProtos.LoggingUrls localLoggingUrls = new CardMetdataProtos.LoggingUrls();
                        paramCodedInputStreamMicro.readMessage(localLoggingUrls);
                        setLoggingUrls(localLoggingUrls);
                        break;
                    case 16:
                        setAnswerType(paramCodedInputStreamMicro.readInt32());
                        break;
                    case 26:
                        setRewrittenQuery(paramCodedInputStreamMicro.readString());
                        break;
                }
                QueryAlternativesProto.QueryAlternatives localQueryAlternatives = new QueryAlternativesProto.QueryAlternatives();
                paramCodedInputStreamMicro.readMessage(localQueryAlternatives);
                setQueryAlternatives(localQueryAlternatives);
            }
        }

        public CardMetadata setAnswerType(int paramInt) {
            this.hasAnswerType = true;
            this.answerType_ = paramInt;
            return this;
        }

        public CardMetadata setLoggingUrls(CardMetdataProtos.LoggingUrls paramLoggingUrls) {
            if (paramLoggingUrls == null) {
                throw new NullPointerException();
            }
            this.hasLoggingUrls = true;
            this.loggingUrls_ = paramLoggingUrls;
            return this;
        }

        public CardMetadata setQueryAlternatives(QueryAlternativesProto.QueryAlternatives paramQueryAlternatives) {
            if (paramQueryAlternatives == null) {
                throw new NullPointerException();
            }
            this.hasQueryAlternatives = true;
            this.queryAlternatives_ = paramQueryAlternatives;
            return this;
        }

        public CardMetadata setRewrittenQuery(String paramString) {
            this.hasRewrittenQuery = true;
            this.rewrittenQuery_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasLoggingUrls()) {
                paramCodedOutputStreamMicro.writeMessage(1, getLoggingUrls());
            }
            if (hasAnswerType()) {
                paramCodedOutputStreamMicro.writeInt32(2, getAnswerType());
            }
            if (hasRewrittenQuery()) {
                paramCodedOutputStreamMicro.writeString(3, getRewrittenQuery());
            }
            if (hasQueryAlternatives()) {
                paramCodedOutputStreamMicro.writeMessage(4, getQueryAlternatives());
            }
        }
    }

    public static final class LoggingUrls
            extends MessageMicro {
        private String acceptFromTimerUrl_ = "";
        private String acceptUrl_ = "";
        private String actionIsIncompleteUrl_ = "";
        private String bailOutUrl_ = "";
        private int cachedSize = -1;
        private boolean hasAcceptFromTimerUrl;
        private boolean hasAcceptUrl;
        private boolean hasActionIsIncompleteUrl;
        private boolean hasBailOutUrl;
        private boolean hasRejectByHittingBackUrl;
        private boolean hasRejectByScrollingDownUrl;
        private boolean hasRejectBySwipingCardUrl;
        private boolean hasRejectTimerUrl;
        private boolean hasShowCardUrl;
        private String rejectByHittingBackUrl_ = "";
        private String rejectByScrollingDownUrl_ = "";
        private String rejectBySwipingCardUrl_ = "";
        private String rejectTimerUrl_ = "";
        private String showCardUrl_ = "";

        public String getAcceptFromTimerUrl() {
            return this.acceptFromTimerUrl_;
        }

        public String getAcceptUrl() {
            return this.acceptUrl_;
        }

        public String getActionIsIncompleteUrl() {
            return this.actionIsIncompleteUrl_;
        }

        public String getBailOutUrl() {
            return this.bailOutUrl_;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public String getRejectByHittingBackUrl() {
            return this.rejectByHittingBackUrl_;
        }

        public String getRejectByScrollingDownUrl() {
            return this.rejectByScrollingDownUrl_;
        }

        public String getRejectBySwipingCardUrl() {
            return this.rejectBySwipingCardUrl_;
        }

        public String getRejectTimerUrl() {
            return this.rejectTimerUrl_;
        }

        public int getSerializedSize() {
            boolean bool = hasAcceptUrl();
            int i = 0;
            if (bool) {
                i = 0 + CodedOutputStreamMicro.computeStringSize(1, getAcceptUrl());
            }
            if (hasBailOutUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(2, getBailOutUrl());
            }
            if (hasAcceptFromTimerUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(3, getAcceptFromTimerUrl());
            }
            if (hasRejectBySwipingCardUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(4, getRejectBySwipingCardUrl());
            }
            if (hasRejectByHittingBackUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(5, getRejectByHittingBackUrl());
            }
            if (hasRejectByScrollingDownUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(6, getRejectByScrollingDownUrl());
            }
            if (hasRejectTimerUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(7, getRejectTimerUrl());
            }
            if (hasShowCardUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(8, getShowCardUrl());
            }
            if (hasActionIsIncompleteUrl()) {
                i += CodedOutputStreamMicro.computeStringSize(9, getActionIsIncompleteUrl());
            }
            this.cachedSize = i;
            return i;
        }

        public String getShowCardUrl() {
            return this.showCardUrl_;
        }

        public boolean hasAcceptFromTimerUrl() {
            return this.hasAcceptFromTimerUrl;
        }

        public boolean hasAcceptUrl() {
            return this.hasAcceptUrl;
        }

        public boolean hasActionIsIncompleteUrl() {
            return this.hasActionIsIncompleteUrl;
        }

        public boolean hasBailOutUrl() {
            return this.hasBailOutUrl;
        }

        public boolean hasRejectByHittingBackUrl() {
            return this.hasRejectByHittingBackUrl;
        }

        public boolean hasRejectByScrollingDownUrl() {
            return this.hasRejectByScrollingDownUrl;
        }

        public boolean hasRejectBySwipingCardUrl() {
            return this.hasRejectBySwipingCardUrl;
        }

        public boolean hasRejectTimerUrl() {
            return this.hasRejectTimerUrl;
        }

        public boolean hasShowCardUrl() {
            return this.hasShowCardUrl;
        }

        public LoggingUrls mergeFrom(CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        setAcceptUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 18:
                        setBailOutUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 26:
                        setAcceptFromTimerUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 34:
                        setRejectBySwipingCardUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 42:
                        setRejectByHittingBackUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 50:
                        setRejectByScrollingDownUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 58:
                        setRejectTimerUrl(paramCodedInputStreamMicro.readString());
                        break;
                    case 66:
                        setShowCardUrl(paramCodedInputStreamMicro.readString());
                        break;
                }
                setActionIsIncompleteUrl(paramCodedInputStreamMicro.readString());
            }
        }

        public LoggingUrls setAcceptFromTimerUrl(String paramString) {
            this.hasAcceptFromTimerUrl = true;
            this.acceptFromTimerUrl_ = paramString;
            return this;
        }

        public LoggingUrls setAcceptUrl(String paramString) {
            this.hasAcceptUrl = true;
            this.acceptUrl_ = paramString;
            return this;
        }

        public LoggingUrls setActionIsIncompleteUrl(String paramString) {
            this.hasActionIsIncompleteUrl = true;
            this.actionIsIncompleteUrl_ = paramString;
            return this;
        }

        public LoggingUrls setBailOutUrl(String paramString) {
            this.hasBailOutUrl = true;
            this.bailOutUrl_ = paramString;
            return this;
        }

        public LoggingUrls setRejectByHittingBackUrl(String paramString) {
            this.hasRejectByHittingBackUrl = true;
            this.rejectByHittingBackUrl_ = paramString;
            return this;
        }

        public LoggingUrls setRejectByScrollingDownUrl(String paramString) {
            this.hasRejectByScrollingDownUrl = true;
            this.rejectByScrollingDownUrl_ = paramString;
            return this;
        }

        public LoggingUrls setRejectBySwipingCardUrl(String paramString) {
            this.hasRejectBySwipingCardUrl = true;
            this.rejectBySwipingCardUrl_ = paramString;
            return this;
        }

        public LoggingUrls setRejectTimerUrl(String paramString) {
            this.hasRejectTimerUrl = true;
            this.rejectTimerUrl_ = paramString;
            return this;
        }

        public LoggingUrls setShowCardUrl(String paramString) {
            this.hasShowCardUrl = true;
            this.showCardUrl_ = paramString;
            return this;
        }

        public void writeTo(CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            if (hasAcceptUrl()) {
                paramCodedOutputStreamMicro.writeString(1, getAcceptUrl());
            }
            if (hasBailOutUrl()) {
                paramCodedOutputStreamMicro.writeString(2, getBailOutUrl());
            }
            if (hasAcceptFromTimerUrl()) {
                paramCodedOutputStreamMicro.writeString(3, getAcceptFromTimerUrl());
            }
            if (hasRejectBySwipingCardUrl()) {
                paramCodedOutputStreamMicro.writeString(4, getRejectBySwipingCardUrl());
            }
            if (hasRejectByHittingBackUrl()) {
                paramCodedOutputStreamMicro.writeString(5, getRejectByHittingBackUrl());
            }
            if (hasRejectByScrollingDownUrl()) {
                paramCodedOutputStreamMicro.writeString(6, getRejectByScrollingDownUrl());
            }
            if (hasRejectTimerUrl()) {
                paramCodedOutputStreamMicro.writeString(7, getRejectTimerUrl());
            }
            if (hasShowCardUrl()) {
                paramCodedOutputStreamMicro.writeString(8, getShowCardUrl());
            }
            if (hasActionIsIncompleteUrl()) {
                paramCodedOutputStreamMicro.writeString(9, getActionIsIncompleteUrl());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.wireless.voicesearch.proto.CardMetdataProtos

 * JD-Core Version:    0.7.0.1

 */