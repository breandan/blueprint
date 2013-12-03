package com.google.android.speech.logs;

import com.google.android.search.core.util.NetworkUtils;
import com.google.android.speech.logger.LogSender;
import com.google.android.speech.message.S3RequestStream;
import com.google.android.speech.message.S3RequestUtils;
import com.google.android.speech.message.S3ResponseStream;
import com.google.android.speech.network.ConnectionFactory;
import com.google.android.voicesearch.settings.Settings;
import com.google.common.io.Closeables;
import com.google.speech.s3.ClientLogRequestProto.ClientLogRequest;
import com.google.speech.s3.S3.S3Request;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class S3LogSender
        implements LogSender {
    private final ConnectionFactory mConnectionFactory;
    private final Settings mSettings;

    public S3LogSender(Settings paramSettings, ConnectionFactory paramConnectionFactory) {
        this.mSettings = paramSettings;
        this.mConnectionFactory = paramConnectionFactory;
    }

    private static S3.S3Request createClientLogRequest(VoicesearchClientLogProto.VoiceSearchClientLog paramVoiceSearchClientLog) {
        return S3RequestUtils.createBaseS3Request().setClientLogRequestExtension(new ClientLogRequestProto.ClientLogRequest().setVoiceSearch(paramVoiceSearchClientLog));
    }

    private static S3.S3Request createInitLogRequest() {
        return S3RequestUtils.createBaseS3Request().setService("clientlog");
    }

    public void send(ArrayList<VoicesearchClientLogProto.VoiceSearchClientLog> paramArrayList)
            throws IOException {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = paramArrayList.iterator();
        while (localIterator.hasNext()) {
            localArrayList.add(createClientLogRequest((VoicesearchClientLogProto.VoiceSearchClientLog) localIterator.next()));
        }
        sendInner(createInitLogRequest(), localArrayList);
    }

    void sendInner(S3.S3Request paramS3Request, ArrayList<S3.S3Request> paramArrayList)
            throws IOException {
        HttpURLConnection localHttpURLConnection = null;
        localObject1 = null;
        if (paramArrayList.isEmpty()) {
            return;
        }
        for (; ; ) {
            try {
                GstaticConfiguration.HttpServerInfo localHttpServerInfo = this.mSettings.getConfiguration().getSingleHttpServerInfo();
                localURL = new URL(localHttpServerInfo.getUrl());
                localHttpURLConnection = this.mConnectionFactory.openHttpConnection(localHttpServerInfo);
                localHttpURLConnection.setDoOutput(true);
                localHttpURLConnection.setDoInput(true);
                NetworkUtils.connect(localHttpURLConnection);
                localS3RequestStream2 = new S3RequestStream(localHttpURLConnection.getOutputStream(), localHttpServerInfo.getHeader(), false);
                try {
                    localS3RequestStream2.writeHeader(paramS3Request);
                    Iterator localIterator = paramArrayList.iterator();
                    if (!localIterator.hasNext()) {
                        continue;
                    }
                    localS3RequestStream2.write((S3.S3Request) localIterator.next());
                    continue;
                    Closeables.closeQuietly(localS3RequestStream1);
                } finally {
                    localS3RequestStream1 = localS3RequestStream2;
                }
            } finally {
                URL localURL;
                S3RequestStream localS3RequestStream2;
                int i;
                S3ResponseStream localS3ResponseStream;
                S3RequestStream localS3RequestStream1 = null;
                localObject1 = null;
                continue;
            }
            Closeables.closeQuietly((Closeable) localObject1);
            if (localHttpURLConnection != null) {
                localHttpURLConnection.disconnect();
            }
            throw localObject2;
            localS3RequestStream2.write(S3RequestUtils.createEndOfData());
            localS3RequestStream2.flush();
            localS3RequestStream2.close();
            i = localHttpURLConnection.getResponseCode();
            if (i != 200) {
                throw new IOException("Http " + i);
            }
            if (!localURL.getHost().equals(localHttpURLConnection.getURL().getHost())) {
                throw new IOException("Redirect to " + localHttpURLConnection.getURL());
            }
            localS3ResponseStream = new S3ResponseStream(localHttpURLConnection.getInputStream());
            try {
                if (localS3ResponseStream.read().getStatus() == 1) {
                    continue;
                }
                throw new IOException("Wrong response");
            } finally {
                localObject1 = localS3ResponseStream;
            }
        }
        Closeables.closeQuietly(localS3RequestStream2);
        Closeables.closeQuietly(localS3ResponseStream);
        if (localHttpURLConnection != null) {
            localHttpURLConnection.disconnect();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.speech.logs.S3LogSender

 * JD-Core Version:    0.7.0.1

 */