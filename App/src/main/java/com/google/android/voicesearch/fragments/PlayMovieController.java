package com.google.android.voicesearch.fragments;

import com.google.android.voicesearch.CardController;
import com.google.android.voicesearch.fragments.action.PlayMediaAction;
import com.google.majel.proto.ActionDateTimeProtos.ActionDate;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction;
import com.google.majel.proto.ActionV2Protos.PlayMediaAction.MovieItem;

public class PlayMovieController
  extends PlayMediaController<Ui>
{
  public PlayMovieController(CardController paramCardController)
  {
    super(paramCardController);
  }
  
  public void initUi()
  {
    super.initUi();
    Ui localUi = (Ui)getUi();
    ActionV2Protos.PlayMediaAction localPlayMediaAction = ((PlayMediaAction)getVoiceAction()).getActionV2();
    localUi.setTitle(localPlayMediaAction.getMovieItem().getTitle());
    localUi.setGenre(localPlayMediaAction.getMovieItem().getGenre());
    boolean bool = localPlayMediaAction.getMovieItem().hasReleaseDate();
    int i = 0;
    if (bool) {
      i = localPlayMediaAction.getMovieItem().getReleaseDate().getYear();
    }
    localUi.setReleaseAndRuntimeInfo(i, localPlayMediaAction.getMovieItem().getPlayTimeMinutes(), (int)localPlayMediaAction.getItemRemainingRentalSeconds() / 60);
    uiReady();
  }
  
  public static abstract interface Ui
    extends PlayMediaController.Ui
  {
    public abstract void setGenre(String paramString);
    
    public abstract void setReleaseAndRuntimeInfo(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract void setTitle(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.fragments.PlayMovieController
 * JD-Core Version:    0.7.0.1
 */