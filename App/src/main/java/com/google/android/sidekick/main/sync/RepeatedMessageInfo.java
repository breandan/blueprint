package com.google.android.sidekick.main.sync;

import com.google.common.collect.Maps;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Airport;
import com.google.geo.sidekick.Sidekick.FlightStatusEntry.Flight;
import com.google.geo.sidekick.Sidekick.PhoneNumber;
import com.google.geo.sidekick.Sidekick.RecognizedName;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.News.ExcludedQuery;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.Sports.SportTeamPlayer;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.StockQuotes.StockData;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact;
import com.google.geo.sidekick.Sidekick.SidekickConfiguration.WebsiteUpdate.WebsiteInterest;
import com.google.geo.sidekick.Sidekick.SportScoreEntry.Period;
import com.google.geo.sidekick.Sidekick.SportScoreEntry.SportEntity;
import com.google.geo.sidekick.Sidekick.WeatherEntry.WeatherPoint;
import com.google.majel.proto.ContactProtos.ContactInformation.PhoneNumber;
import com.google.majel.proto.ContactProtos.RecognizedName;
import com.google.protobuf.micro.MessageMicro;
import java.lang.reflect.Field;
import java.util.Map;

public class RepeatedMessageInfo
{
  private static final Map<Class<? extends MessageMicro>, KeyProvider<? extends MessageMicro>> mInfoProviders = ;
  
  static
  {
    try
    {
      addProvider(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer.class, new KeyProvider(Sidekick.SidekickConfiguration.Sports.SportTeamPlayer.class, '*', new String[] { "sport", "freebaseId" }));
      addProvider(Sidekick.SidekickConfiguration.StockQuotes.StockData.class, new KeyProvider(Sidekick.SidekickConfiguration.StockQuotes.StockData.class, '.', new String[] { "gin" }));
      addProvider(Sidekick.SidekickConfiguration.News.ExcludedQuery.class, new KeyProvider(Sidekick.SidekickConfiguration.News.ExcludedQuery.class, '.', new String[] { "query" }));
      addProvider(Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact.class, new KeyProvider(Sidekick.SidekickConfiguration.TrafficCardSharing.LocationSharingContact.class, '.', new String[] { "obfuscatedGaiaId" }));
      addProvider(Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting.class, new KeyProvider(Sidekick.SidekickConfiguration.InternalApiClients.ClientSetting.class, '.', new String[] { "clientId" }));
      addProvider(Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount.class, new KeyProvider(Sidekick.SidekickConfiguration.NextMeeting.CalendarAccount.class, '.', new String[] { "hashedId" }));
      addProvider(Sidekick.WeatherEntry.WeatherPoint.class, new KeyProvider(Sidekick.WeatherEntry.WeatherPoint.class, '.', new String[] { "label" }));
      addProvider(Sidekick.SportScoreEntry.SportEntity.class, new KeyProvider(Sidekick.SportScoreEntry.SportEntity.class, '.', new String[] { "primaryKey" }));
      addProvider(Sidekick.SportScoreEntry.Period.class, new KeyProvider(Sidekick.SportScoreEntry.Period.class, '.', new String[] { "number" }));
      addProvider(Sidekick.SidekickConfiguration.WebsiteUpdate.WebsiteInterest.class, new KeyProvider(Sidekick.SidekickConfiguration.WebsiteUpdate.WebsiteInterest.class, '.', new String[] { "url" }));
      addProvider(Sidekick.SportScoreEntry.Period.class, new KeyProvider(Sidekick.SportScoreEntry.Period.class, '.', new String[0])
      {
        String generateKeyFor(Sidekick.SportScoreEntry.Period paramAnonymousPeriod)
        {
          String str = Integer.toString(paramAnonymousPeriod.getNumber(), 16);
          return "00000000".substring(str.length()) + str;
        }
      });
      addProvider(Sidekick.FlightStatusEntry.Flight.class, new KeyProvider(Sidekick.FlightStatusEntry.Flight.class, '.', new String[0])
      {
        String generateKeyFor(Sidekick.FlightStatusEntry.Flight paramAnonymousFlight)
        {
          return paramAnonymousFlight.getAirlineCode() + '.' + paramAnonymousFlight.getFlightNumber() + '.' + paramAnonymousFlight.getDepartureAirport().getCode();
        }
      });
      addProvider(Sidekick.PhoneNumber.class, new KeyProvider(Sidekick.PhoneNumber.class, '.', new String[] { "value" }));
      addProvider(Sidekick.RecognizedName.class, new KeyProvider(Sidekick.RecognizedName.class, '.', new String[] { "value" }));
      addProvider(ContactProtos.ContactInformation.PhoneNumber.class, new KeyProvider(ContactProtos.ContactInformation.PhoneNumber.class, '.', new String[] { "value" }));
      addProvider(ContactProtos.RecognizedName.class, new KeyProvider(ContactProtos.RecognizedName.class, '.', new String[] { "value" }));
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      throw new RuntimeException(localNoSuchFieldException);
    }
  }
  
  private <T extends MessageMicro> KeyProvider<T> KeyGeneratorFor(T paramT)
    throws RepeatedMessageInfo.NoInfoForMessageException
  {
    KeyProvider localKeyProvider = (KeyProvider)mInfoProviders.get(paramT.getClass());
    if (localKeyProvider == null) {
      throw new NoInfoForMessageException(paramT);
    }
    return localKeyProvider;
  }
  
  private static void addProvider(Class<? extends MessageMicro> paramClass, KeyProvider<? extends MessageMicro> paramKeyProvider)
  {
    mInfoProviders.put(paramClass, paramKeyProvider);
  }
  
  boolean hasPrimaryKeyGenerator(MessageMicro paramMessageMicro)
  {
    return mInfoProviders.containsKey(paramMessageMicro.getClass());
  }
  
  public <T extends MessageMicro> String primaryKeyFor(T paramT)
  {
    return KeyGeneratorFor(paramT).generateKeyFor(paramT);
  }
  
  static class KeyProvider<T extends MessageMicro>
  {
    private final char mDelimiter;
    private final Field[] mFields;
    
    KeyProvider(Class<T> paramClass, char paramChar, String... paramVarArgs)
      throws NoSuchFieldException
    {
      this.mDelimiter = paramChar;
      this.mFields = new Field[paramVarArgs.length];
      for (int i = 0; i < paramVarArgs.length; i++) {
        this.mFields[i] = MessageMicroUtil.getProtoField(paramClass, paramVarArgs[i]);
      }
    }
    
    private static String fieldToString(MessageMicro paramMessageMicro, Field paramField)
    {
      Object localObject = MessageMicroUtil.getFieldValue(paramMessageMicro, paramField);
      if (localObject != null) {
        return localObject.toString();
      }
      return "";
    }
    
    String generateKeyFor(T paramT)
    {
      if (this.mFields.length == 1) {
        return fieldToString(paramT, this.mFields[0]);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < this.mFields.length; i++) {
        localStringBuilder.append(fieldToString(paramT, this.mFields[i])).append(this.mDelimiter);
      }
      localStringBuilder.setLength(-1 + localStringBuilder.length());
      return localStringBuilder.toString();
    }
  }
  
  static class NoInfoForMessageException
    extends RuntimeException
  {
    NoInfoForMessageException(MessageMicro paramMessageMicro)
    {
      super();
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.sync.RepeatedMessageInfo
 * JD-Core Version:    0.7.0.1
 */