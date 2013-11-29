package com.google.android.speech.network.request;

import com.google.common.base.Supplier;
import com.google.speech.speech.s3.Recognizer.RecognizerVocabularyContext;
import com.google.speech.util.Contacts.TopContact;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.Nonnull;

public class RecognizerVocabularyContextBuilderTask
  extends BaseRequestBuilderTask<Recognizer.RecognizerVocabularyContext>
{
  private final Supplier<List<String>> mTopContactNamesSupplier;
  
  RecognizerVocabularyContextBuilderTask(@Nonnull Supplier<List<String>> paramSupplier)
  {
    super("RecognizerVocabularyContextBuilderTask");
    this.mTopContactNamesSupplier = paramSupplier;
  }
  
  public static Callable<Recognizer.RecognizerVocabularyContext> getBuilder(@Nonnull Supplier<List<String>> paramSupplier)
  {
    return new RecognizerVocabularyContextBuilderTask(paramSupplier);
  }
  
  protected Recognizer.RecognizerVocabularyContext build()
  {
    Recognizer.RecognizerVocabularyContext localRecognizerVocabularyContext = new Recognizer.RecognizerVocabularyContext();
    List localList = (List)this.mTopContactNamesSupplier.get();
    if ((localList == null) || (localList.isEmpty())) {}
    for (;;)
    {
      return localRecognizerVocabularyContext;
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localRecognizerVocabularyContext.addTopContact(new Contacts.TopContact().setName(str));
      }
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.speech.network.request.RecognizerVocabularyContextBuilderTask
 * JD-Core Version:    0.7.0.1
 */