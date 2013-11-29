package com.google.android.search.core.google;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import com.google.android.search.core.prefetch.SearchResult.SrpMetadata;
import com.google.android.search.core.util.ChunkProducer.DataChunk;
import com.google.android.shared.util.Consumer;
import com.google.android.shared.util.Util;
import com.google.android.velvet.ActionData;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.protobuf.micro.InvalidProtocolBufferMicroException;
import com.google.quality.genie.proto.QueryAlternativesProto.QueryAlternatives;
import com.google.wireless.voicesearch.proto.CardMetdataProtos.CardMetadata;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;

public class PelletDemultiplexer
  implements Consumer<PelletParser.Pellet>
{
  private static final Charset HTML_CHARSET = Util.UTF_8;
  @Nonnull
  private final String mBaseUri;
  private final Map<String, ChunkedCard> mCards = Maps.newHashMap();
  private final AtomicInteger mDataChunkSerialNum = new AtomicInteger();
  @Nonnull
  private final ExtrasConsumer mExtrasConsumer;
  private boolean mNoMoreCards;
  private final StringBuilder mSrp;
  @Nonnull
  private final Consumer<ChunkProducer.DataChunk> mSrpConsumer;
  private String mSrpQuery;
  @Nonnull
  private final String mSuggestionPelletPath;
  private StringBuilder mSuggestionsString;
  
  public PelletDemultiplexer(@Nonnull Consumer<ChunkProducer.DataChunk> paramConsumer, @Nonnull ExtrasConsumer paramExtrasConsumer, @Nonnull String paramString1, @Nonnull String paramString2)
  {
    this.mSrpConsumer = ((Consumer)Preconditions.checkNotNull(paramConsumer));
    this.mExtrasConsumer = ((ExtrasConsumer)Preconditions.checkNotNull(paramExtrasConsumer));
    this.mBaseUri = ((String)Preconditions.checkNotNull(paramString1));
    this.mSuggestionPelletPath = paramString2;
    this.mSrp = ((StringBuilder)null);
  }
  
  private void addSuggestionString(PelletParser.Pellet paramPellet)
  {
    String str = new String(paramPellet.mData, Util.UTF_8);
    if (paramPellet.mMorePelletsToFollow)
    {
      if (this.mSuggestionsString == null) {
        this.mSuggestionsString = new StringBuilder();
      }
      this.mSuggestionsString.append(str);
      return;
    }
    if (this.mSuggestionsString == null)
    {
      this.mExtrasConsumer.onSuggestions(paramPellet.mId, str);
      return;
    }
    this.mSuggestionsString.append(str);
    this.mExtrasConsumer.onSuggestions(paramPellet.mId, this.mSuggestionsString.toString());
    this.mSuggestionsString = null;
  }
  
  static ActionData makeAction(ChunkedCard paramChunkedCard, String paramString)
    throws InvalidProtocolBufferMicroException
  {
    byte[] arrayOfByte = Base64.decode(paramChunkedCard.getData(), 8);
    PeanutProtos.Peanut localPeanut = new PeanutProtos.Peanut();
    localPeanut.mergeFrom(arrayOfByte);
    return ActionData.fromPeanut(localPeanut, makeCardMetadata(paramChunkedCard.getCardMetadataBytes()), paramString);
  }
  
  private static ActionData makeAnswer(String paramString, ChunkedCard paramChunkedCard)
    throws InvalidProtocolBufferMicroException
  {
    return ActionData.fromHtml(paramString, new String(paramChunkedCard.getData(), HTML_CHARSET), makeCardMetadata(paramChunkedCard.getCardMetadataBytes()));
  }
  
  static CardMetdataProtos.CardMetadata makeCardMetadata(byte[] paramArrayOfByte)
    throws InvalidProtocolBufferMicroException
  {
    CardMetdataProtos.CardMetadata localCardMetadata = new CardMetdataProtos.CardMetadata();
    if (paramArrayOfByte != null)
    {
      localCardMetadata.mergeFrom(Base64.decode(paramArrayOfByte, 8));
      return localCardMetadata;
    }
    Log.w("Search.PelletDemultiplexer", "No card metadata received - this affects logging.");
    return localCardMetadata;
  }
  
  private void noMoreCards(boolean paramBoolean)
  {
    if (!this.mNoMoreCards)
    {
      this.mNoMoreCards = true;
      this.mExtrasConsumer.onActionDataFinished(paramBoolean);
    }
  }
  
  private void produceCard(String paramString1, String paramString2, ChunkedCard paramChunkedCard, String paramString3)
  {
    boolean bool1 = paramString2.startsWith("ans");
    boolean bool2 = paramString2.startsWith("act");
    if ((bool1) || (bool2))
    {
      if (bool1) {}
      for (;;)
      {
        try
        {
          localObject = makeAnswer(paramString1, paramChunkedCard);
          this.mExtrasConsumer.onActionDataReceived((ActionData)localObject);
          return;
        }
        catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
        {
          Object localObject;
          ActionData localActionData;
          Log.e("Search.PelletDemultiplexer", "Could not make card", localInvalidProtocolBufferMicroException);
          return;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          Log.e("Search.PelletDemultiplexer", "Could not make card", localIllegalArgumentException);
        }
        localActionData = makeAction(paramChunkedCard, paramString3);
        localObject = localActionData;
      }
    }
  }
  
  public boolean consume(@Nonnull PelletParser.Pellet paramPellet)
  {
    Preconditions.checkNotNull(paramPellet);
    Uri localUri = Uri.parse(paramPellet.mUrl);
    String str1 = localUri.getQueryParameter("ect");
    if (this.mSuggestionPelletPath.equals(localUri.getPath())) {
      addSuggestionString(paramPellet);
    }
    label358:
    for (;;)
    {
      return true;
      if (str1 == null)
      {
        if (this.mSrpQuery == null)
        {
          this.mSrpQuery = localUri.getQueryParameter("q");
          if (this.mSrpQuery != null) {
            this.mExtrasConsumer.onSrpQuery(this.mSrpQuery);
          }
        }
        int i = this.mDataChunkSerialNum.getAndIncrement();
        this.mSrpConsumer.consume(new ChunkProducer.DataChunk(paramPellet.mData, i));
      }
      else
      {
        Object localObject;
        if ("eoc".equals(str1))
        {
          byte[] arrayOfByte = paramPellet.mCardMetadata;
          String str2 = null;
          localObject = null;
          if (arrayOfByte != null) {}
          try
          {
            CardMetdataProtos.CardMetadata localCardMetadata = makeCardMetadata(paramPellet.mCardMetadata);
            boolean bool1 = localCardMetadata.hasRewrittenQuery();
            str2 = null;
            if (bool1) {
              str2 = localCardMetadata.getRewrittenQuery();
            }
            boolean bool2 = localCardMetadata.hasQueryAlternatives();
            localObject = null;
            if (bool2)
            {
              QueryAlternativesProto.QueryAlternatives localQueryAlternatives = localCardMetadata.getQueryAlternatives();
              localObject = localQueryAlternatives;
            }
          }
          catch (InvalidProtocolBufferMicroException localInvalidProtocolBufferMicroException)
          {
            for (;;)
            {
              Log.w("Search.PelletDemultiplexer", localInvalidProtocolBufferMicroException);
              localObject = null;
            }
          }
          this.mExtrasConsumer.onSrpMetadata(new SearchResult.SrpMetadata(paramPellet.mId, str2, localObject));
          noMoreCards(paramPellet.mAnswerInSrp);
          continue;
        }
        ChunkedCard localChunkedCard = (ChunkedCard)this.mCards.get(str1);
        if (localChunkedCard == null)
        {
          localChunkedCard = new ChunkedCard(paramPellet.mData, paramPellet.mCardMetadata);
          this.mCards.put(str1, localChunkedCard);
        }
        for (;;)
        {
          if (paramPellet.mMorePelletsToFollow) {
            break label358;
          }
          produceCard(this.mBaseUri, str1, localChunkedCard, paramPellet.mId);
          this.mCards.remove(str1);
          break;
          localChunkedCard.append(paramPellet.mData, paramPellet.mCardMetadata);
        }
      }
    }
  }
  
  void onEndOfResponse()
  {
    noMoreCards(false);
  }
  
  static final class ChunkedCard
  {
    private final List<byte[]> mChunks = new ArrayList(4);
    private byte[] mMetadata;
    
    ChunkedCard(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      append(paramArrayOfByte1, paramArrayOfByte2);
    }
    
    void append(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      this.mChunks.add(paramArrayOfByte1);
      if (paramArrayOfByte2 != null) {
        this.mMetadata = paramArrayOfByte2;
      }
    }
    
    byte[] getCardMetadataBytes()
    {
      return this.mMetadata;
    }
    
    byte[] getData()
    {
      if (this.mChunks.size() == 1) {
        return (byte[])this.mChunks.get(0);
      }
      int i = 0;
      Iterator localIterator1 = this.mChunks.iterator();
      while (localIterator1.hasNext()) {
        i += ((byte[])localIterator1.next()).length;
      }
      byte[] arrayOfByte1 = new byte[i];
      int j = 0;
      Iterator localIterator2 = this.mChunks.iterator();
      while (localIterator2.hasNext())
      {
        byte[] arrayOfByte2 = (byte[])localIterator2.next();
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, j, arrayOfByte2.length);
        j += arrayOfByte2.length;
      }
      return arrayOfByte1;
    }
  }
  
  public static abstract interface ExtrasConsumer
  {
    public abstract void onActionDataFinished(boolean paramBoolean);
    
    public abstract void onActionDataReceived(ActionData paramActionData);
    
    public abstract void onSrpMetadata(SearchResult.SrpMetadata paramSrpMetadata);
    
    public abstract void onSrpQuery(String paramString);
    
    public abstract void onSuggestions(String paramString1, String paramString2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.google.PelletDemultiplexer
 * JD-Core Version:    0.7.0.1
 */