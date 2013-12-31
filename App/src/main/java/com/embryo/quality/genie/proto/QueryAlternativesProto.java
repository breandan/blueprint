package com.embryo.quality.genie.proto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class QueryAlternativesProto {
    public static final class QueryAlternatives
            extends com.embryo.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private List<QuerySegmentAlternatives> querySegmentAlternatives_ = Collections.emptyList();
        private List<String> queryToken_ = Collections.emptyList();

        public QueryAlternatives addQuerySegmentAlternatives(QuerySegmentAlternatives paramQuerySegmentAlternatives) {
            if (paramQuerySegmentAlternatives == null) {
                throw new NullPointerException();
            }
            if (this.querySegmentAlternatives_.isEmpty()) {
                this.querySegmentAlternatives_ = new ArrayList();
            }
            this.querySegmentAlternatives_.add(paramQuerySegmentAlternatives);
            return this;
        }

        public QueryAlternatives addQueryToken(String paramString) {
            if (paramString == null) {
                throw new NullPointerException();
            }
            if (this.queryToken_.isEmpty()) {
                this.queryToken_ = new ArrayList();
            }
            this.queryToken_.add(paramString);
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public QuerySegmentAlternatives getQuerySegmentAlternatives(int paramInt) {
            return (QuerySegmentAlternatives) this.querySegmentAlternatives_.get(paramInt);
        }

        public int getQuerySegmentAlternativesCount() {
            return this.querySegmentAlternatives_.size();
        }

        public List<QuerySegmentAlternatives> getQuerySegmentAlternativesList() {
            return this.querySegmentAlternatives_;
        }

        public int getQueryTokenCount() {
            return this.queryToken_.size();
        }

        public List<String> getQueryTokenList() {
            return this.queryToken_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator1 = getQueryTokenList().iterator();
            while (localIterator1.hasNext()) {
                i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator1.next());
            }
            int j = 0 + i + 1 * getQueryTokenList().size();
            Iterator localIterator2 = getQuerySegmentAlternativesList().iterator();
            while (localIterator2.hasNext()) {
                j += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(2, (QuerySegmentAlternatives) localIterator2.next());
            }
            this.cachedSize = j;
            return j;
        }

        public QueryAlternatives mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        addQueryToken(paramCodedInputStreamMicro.readString());
                        break;
                }
                QuerySegmentAlternatives localQuerySegmentAlternatives = new QuerySegmentAlternatives();
                paramCodedInputStreamMicro.readMessage(localQuerySegmentAlternatives);
                addQuerySegmentAlternatives(localQuerySegmentAlternatives);
            }
        }

        public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator1 = getQueryTokenList().iterator();
            while (localIterator1.hasNext()) {
                paramCodedOutputStreamMicro.writeString(1, (String) localIterator1.next());
            }
            Iterator localIterator2 = getQuerySegmentAlternativesList().iterator();
            while (localIterator2.hasNext()) {
                paramCodedOutputStreamMicro.writeMessage(2, (QuerySegmentAlternatives) localIterator2.next());
            }
        }

        public static final class QuerySegment
                extends com.embryo.protobuf.micro.MessageMicro {
            private int cachedSize = -1;
            private int endToken_ = 0;
            private boolean hasEndToken;
            private boolean hasStartToken;
            private int startToken_ = 0;

            public int getCachedSize() {
                if (this.cachedSize < 0) {
                    getSerializedSize();
                }
                return this.cachedSize;
            }

            public int getEndToken() {
                return this.endToken_;
            }

            public int getSerializedSize() {
                boolean bool = hasStartToken();
                int i = 0;
                if (bool) {
                    i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(1, getStartToken());
                }
                if (hasEndToken()) {
                    i += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeInt32Size(2, getEndToken());
                }
                this.cachedSize = i;
                return i;
            }

            public int getStartToken() {
                return this.startToken_;
            }

            public boolean hasEndToken() {
                return this.hasEndToken;
            }

            public boolean hasStartToken() {
                return this.hasStartToken;
            }

            public QuerySegment mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                            setStartToken(paramCodedInputStreamMicro.readInt32());
                            break;
                    }
                    setEndToken(paramCodedInputStreamMicro.readInt32());
                }
            }

            public QuerySegment setEndToken(int paramInt) {
                this.hasEndToken = true;
                this.endToken_ = paramInt;
                return this;
            }

            public QuerySegment setStartToken(int paramInt) {
                this.hasStartToken = true;
                this.startToken_ = paramInt;
                return this;
            }

            public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasStartToken()) {
                    paramCodedOutputStreamMicro.writeInt32(1, getStartToken());
                }
                if (hasEndToken()) {
                    paramCodedOutputStreamMicro.writeInt32(2, getEndToken());
                }
            }
        }

        public static final class QuerySegmentAlternatives
                extends com.embryo.protobuf.micro.MessageMicro {
            private List<String> alternative_ = Collections.emptyList();
            private int cachedSize = -1;
            private boolean hasQuerySegment;
            private QueryAlternativesProto.QueryAlternatives.QuerySegment querySegment_ = null;

            public QuerySegmentAlternatives addAlternative(String paramString) {
                if (paramString == null) {
                    throw new NullPointerException();
                }
                if (this.alternative_.isEmpty()) {
                    this.alternative_ = new ArrayList();
                }
                this.alternative_.add(paramString);
                return this;
            }

            public List<String> getAlternativeList() {
                return this.alternative_;
            }

            public int getCachedSize() {
                if (this.cachedSize < 0) {
                    getSerializedSize();
                }
                return this.cachedSize;
            }

            public QueryAlternativesProto.QueryAlternatives.QuerySegment getQuerySegment() {
                return this.querySegment_;
            }

            public int getSerializedSize() {
                boolean bool = hasQuerySegment();
                int i = 0;
                if (bool) {
                    i = 0 + com.embryo.protobuf.micro.CodedOutputStreamMicro.computeMessageSize(1, getQuerySegment());
                }
                int j = 0;
                Iterator localIterator = getAlternativeList().iterator();
                while (localIterator.hasNext()) {
                    j += com.embryo.protobuf.micro.CodedOutputStreamMicro.computeStringSizeNoTag((String) localIterator.next());
                }
                int k = i + j + 1 * getAlternativeList().size();
                this.cachedSize = k;
                return k;
            }

            public boolean hasQuerySegment() {
                return this.hasQuerySegment;
            }

            public QuerySegmentAlternatives mergeFrom(com.embryo.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                            QueryAlternativesProto.QueryAlternatives.QuerySegment localQuerySegment = new QueryAlternativesProto.QueryAlternatives.QuerySegment();
                            paramCodedInputStreamMicro.readMessage(localQuerySegment);
                            setQuerySegment(localQuerySegment);
                            break;
                    }
                    addAlternative(paramCodedInputStreamMicro.readString());
                }
            }

            public QuerySegmentAlternatives setQuerySegment(QueryAlternativesProto.QueryAlternatives.QuerySegment paramQuerySegment) {
                if (paramQuerySegment == null) {
                    throw new NullPointerException();
                }
                this.hasQuerySegment = true;
                this.querySegment_ = paramQuerySegment;
                return this;
            }

            public void writeTo(com.embryo.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                    throws IOException {
                if (hasQuerySegment()) {
                    paramCodedOutputStreamMicro.writeMessage(1, getQuerySegment());
                }
                Iterator localIterator = getAlternativeList().iterator();
                while (localIterator.hasNext()) {
                    paramCodedOutputStreamMicro.writeString(2, (String) localIterator.next());
                }
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     QueryAlternativesProto

 * JD-Core Version:    0.7.0.1

 */