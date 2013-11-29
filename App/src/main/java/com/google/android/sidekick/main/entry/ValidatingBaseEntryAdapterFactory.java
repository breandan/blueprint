package com.google.android.sidekick.main.entry;

import com.google.android.sidekick.shared.cards.BaseEntryAdapterFactory;
import com.google.geo.sidekick.Sidekick.Entry;
import com.google.geo.sidekick.Sidekick.EntryTreeNode;
import javax.annotation.Nullable;

public abstract class ValidatingBaseEntryAdapterFactory<T>
  extends BaseEntryAdapterFactory<T>
{
  private final EntryValidator mEntryValidator;
  
  protected ValidatingBaseEntryAdapterFactory(EntryValidator paramEntryValidator)
  {
    this.mEntryValidator = paramEntryValidator;
  }
  
  @Nullable
  public T create(Sidekick.Entry paramEntry)
  {
    if (!isValid(paramEntry)) {
      return null;
    }
    return super.create(paramEntry);
  }
  
  @Nullable
  public T createForGroup(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    if (!isGroupValid(paramEntryTreeNode)) {
      return null;
    }
    return super.createForGroup(paramEntryTreeNode);
  }
  
  protected boolean isGroupValid(Sidekick.EntryTreeNode paramEntryTreeNode)
  {
    return this.mEntryValidator.validateGroup(paramEntryTreeNode);
  }
  
  protected boolean isValid(Sidekick.Entry paramEntry)
  {
    return this.mEntryValidator.validate(paramEntry);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.entry.ValidatingBaseEntryAdapterFactory
 * JD-Core Version:    0.7.0.1
 */