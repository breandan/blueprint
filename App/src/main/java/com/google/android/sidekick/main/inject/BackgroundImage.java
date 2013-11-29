package com.google.android.sidekick.main.inject;

import com.google.geo.sidekick.Sidekick.BackgroundPhotoDescriptor;
import com.google.geo.sidekick.Sidekick.Photo;

public class BackgroundImage
{
  private final Sidekick.BackgroundPhotoDescriptor mDescriptor;
  private final Sidekick.Photo mPhoto;
  
  public BackgroundImage(Sidekick.Photo paramPhoto, Sidekick.BackgroundPhotoDescriptor paramBackgroundPhotoDescriptor)
  {
    this.mPhoto = paramPhoto;
    this.mDescriptor = paramBackgroundPhotoDescriptor;
  }
  
  public Sidekick.BackgroundPhotoDescriptor getDescriptor()
  {
    return this.mDescriptor;
  }
  
  public Sidekick.Photo getPhoto()
  {
    return this.mPhoto;
  }
  
  public boolean isDoodle()
  {
    return (getDescriptor() != null) && (getDescriptor().getSource() == 1);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.main.inject.BackgroundImage
 * JD-Core Version:    0.7.0.1
 */