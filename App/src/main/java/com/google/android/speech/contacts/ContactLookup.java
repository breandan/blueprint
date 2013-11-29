package com.google.android.speech.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import com.google.android.search.core.Feature;
import com.google.android.velvet.VelvetServices;
import com.google.common.io.Closeables;
import com.google.majel.proto.ActionV2Protos.ActionContact;
import com.google.majel.proto.ContactProtos.ContactQuery;
import java.io.InputStream;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ContactLookup
{
  @Nullable
  public static Bitmap fetchPhotoBitmap(ContentResolver paramContentResolver, long paramLong, boolean paramBoolean)
  {
    Uri localUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, paramLong);
    InputStream localInputStream = null;
    try
    {
      localInputStream = ContactsContract.Contacts.openContactPhotoInputStream(paramContentResolver, localUri, paramBoolean);
      Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream);
      return localBitmap;
    }
    finally
    {
      Closeables.closeQuietly(localInputStream);
    }
  }
  
  public static ContactLookup newInstance(Context paramContext)
  {
    if (Feature.ICING_CONTACT_LOOKUP.isEnabled()) {
      return new IcingContactLookup(paramContext, VelvetServices.get().getRelationshipNameLookup(), VelvetServices.get().getRelationshipManager());
    }
    return new ProviderContactLookup(paramContext.getContentResolver(), VelvetServices.get().getRelationshipNameLookup(), VelvetServices.get().getRelationshipManager());
  }
  
  public abstract Person fetchContactInfo(long paramLong);
  
  public abstract List<Contact> fetchEmailAddresses(long paramLong, String paramString);
  
  public abstract List<Contact> fetchPhoneNumbers(long paramLong, String paramString);
  
  public abstract List<Contact> fetchPostalAddresses(long paramLong, String paramString);
  
  public abstract List<Person> findAllByDisplayName(@Nonnull Mode paramMode, List<ActionV2Protos.ActionContact> paramList, @Nullable String paramString);
  
  public abstract List<Person> findAllByDisplayName(ContactProtos.ContactQuery paramContactQuery);
  
  public abstract List<Person> findAllByPhoneNumber(String paramString);
  
  public abstract List<String> findFavoriteContactNames(int paramInt1, int paramInt2);
  
  public abstract boolean hasMatchingContacts(String paramString);
  
  @Nullable
  public String maybeNormalizeName(@Nullable String paramString)
  {
    if ((paramString == null) || (!paramString.endsWith("'s"))) {
      return paramString;
    }
    return paramString.substring(0, -2 + paramString.length());
  }
  
  public static enum Mode
  {
    public final int typeHome;
    public final int typeMain;
    public final int typeMobile;
    public final int typeOther;
    public final int typeWork;
    
    static
    {
      PERSON = new Mode("PERSON", 3, 0, 0, 0, 0, 0);
      Mode[] arrayOfMode = new Mode[4];
      arrayOfMode[0] = EMAIL;
      arrayOfMode[1] = PHONE_NUMBER;
      arrayOfMode[2] = POSTAL_ADDRESS;
      arrayOfMode[3] = PERSON;
      $VALUES = arrayOfMode;
    }
    
    private Mode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      this.typeHome = paramInt1;
      this.typeWork = paramInt2;
      this.typeMobile = paramInt3;
      this.typeMain = paramInt4;
      this.typeOther = paramInt5;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.ContactLookup
 * JD-Core Version:    0.7.0.1
 */