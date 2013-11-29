package com.google.android.speech.contacts;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class PersonMergeStrategy
{
  @Nullable
  abstract Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2);
  
  public static class MergeById
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if (paramPerson1.getId() != paramPerson2.getId()) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
  
  public static class MergeByNameAndEmailAddress
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if ((!paramPerson1.hasSameName(paramPerson2)) || (!paramPerson1.hasCommonEmailAddress(paramPerson2))) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
  
  public static class MergeByNameAndOrthogonalContactType
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if (!paramPerson1.hasSameName(paramPerson2)) {}
      while (((paramPerson1.hasServerImageUri()) && (paramPerson2.hasServerImageUri())) || ((!paramPerson1.getPhoneNumbers().isEmpty()) && (!paramPerson2.getPhoneNumbers().isEmpty())) || ((!paramPerson1.getEmailAddresses().isEmpty()) && (!paramPerson2.getEmailAddresses().isEmpty())) || ((!paramPerson1.getPostalAddresses().isEmpty()) && (!paramPerson2.getPostalAddresses().isEmpty()))) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
  
  public static class MergeByNameAndPhoneNumber
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if ((!paramPerson1.hasSameName(paramPerson2)) || (!paramPerson1.hasCommonPhoneNumber(paramPerson2))) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
  
  public static class MergeByNameAndServerImageUri
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if ((!paramPerson1.hasSameName(paramPerson2)) || (!paramPerson1.hasSameServerImageUri(paramPerson2))) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
  
  public static class MergeByNameOnly
    extends PersonMergeStrategy
  {
    @Nullable
    public Person maybeMerge(@Nonnull Person paramPerson1, @Nonnull Person paramPerson2)
    {
      if (!paramPerson1.hasSameName(paramPerson2)) {
        return null;
      }
      return paramPerson1.mergePerson(paramPerson2);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.contacts.PersonMergeStrategy
 * JD-Core Version:    0.7.0.1
 */