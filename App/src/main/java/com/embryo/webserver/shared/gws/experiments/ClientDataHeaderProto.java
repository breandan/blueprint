package com.google.webserver.shared.gws.experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ClientDataHeaderProto {
    public static final class ClientDataHeader
            extends com.google.protobuf.micro.MessageMicro {
        private int cachedSize = -1;
        private long configTimeUsec_ = 0L;
        private List<Integer> experimentId_ = Collections.emptyList();
        private boolean hasConfigTimeUsec;

        public ClientDataHeader addExperimentId(int paramInt) {
            if (this.experimentId_.isEmpty()) {
                this.experimentId_ = new ArrayList();
            }
            this.experimentId_.add(Integer.valueOf(paramInt));
            return this;
        }

        public int getCachedSize() {
            if (this.cachedSize < 0) {
                getSerializedSize();
            }
            return this.cachedSize;
        }

        public long getConfigTimeUsec() {
            return this.configTimeUsec_;
        }

        public int getExperimentId(int paramInt) {
            return ((Integer) this.experimentId_.get(paramInt)).intValue();
        }

        public int getExperimentIdCount() {
            return this.experimentId_.size();
        }

        public List<Integer> getExperimentIdList() {
            return this.experimentId_;
        }

        public int getSerializedSize() {
            int i = 0;
            Iterator localIterator = getExperimentIdList().iterator();
            while (localIterator.hasNext()) {
                i += com.google.protobuf.micro.CodedOutputStreamMicro.computeInt32SizeNoTag(((Integer) localIterator.next()).intValue());
            }
            int j = 0 + i + 1 * getExperimentIdList().size();
            if (hasConfigTimeUsec()) {
                j += com.google.protobuf.micro.CodedOutputStreamMicro.computeInt64Size(2, getConfigTimeUsec());
            }
            this.cachedSize = j;
            return j;
        }

        public boolean hasConfigTimeUsec() {
            return this.hasConfigTimeUsec;
        }

        public ClientDataHeader mergeFrom(com.google.protobuf.micro.CodedInputStreamMicro paramCodedInputStreamMicro)
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
                        addExperimentId(paramCodedInputStreamMicro.readInt32());
                        break;
                }
                setConfigTimeUsec(paramCodedInputStreamMicro.readInt64());
            }
        }

        public ClientDataHeader setConfigTimeUsec(long paramLong) {
            this.hasConfigTimeUsec = true;
            this.configTimeUsec_ = paramLong;
            return this;
        }

        public void writeTo(com.google.protobuf.micro.CodedOutputStreamMicro paramCodedOutputStreamMicro)
                throws IOException {
            Iterator localIterator = getExperimentIdList().iterator();
            while (localIterator.hasNext()) {
                paramCodedOutputStreamMicro.writeInt32(1, ((Integer) localIterator.next()).intValue());
            }
            if (hasConfigTimeUsec()) {
                paramCodedOutputStreamMicro.writeInt64(2, getConfigTimeUsec());
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     ClientDataHeaderProto

 * JD-Core Version:    0.7.0.1

 */