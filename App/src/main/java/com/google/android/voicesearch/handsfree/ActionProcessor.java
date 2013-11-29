package com.google.android.voicesearch.handsfree;

import com.google.android.voicesearch.util.PhoneActionUtils;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.ActionV2Protos.PhoneAction;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.majel.proto.PeanutProtos.Peanut;

class ActionProcessor
{
  private final ActionListener mActionListener;
  
  public ActionProcessor(ActionListener paramActionListener)
  {
    this.mActionListener = paramActionListener;
  }
  
  public boolean process(MajelProtos.MajelResponse paramMajelResponse)
  {
    if (paramMajelResponse.getPeanutCount() == 0) {}
    ActionV2Protos.ActionV2 localActionV2;
    ActionV2Protos.PhoneAction localPhoneAction;
    do
    {
      do
      {
        PeanutProtos.Peanut localPeanut;
        do
        {
          return false;
          localPeanut = paramMajelResponse.getPeanut(0);
        } while (localPeanut.getActionV2Count() == 0);
        localActionV2 = localPeanut.getActionV2(0);
      } while (!localActionV2.hasPhoneActionExtension());
      localPhoneAction = localActionV2.getPhoneActionExtension();
    } while (localPhoneAction.getContactCount() == 0);
    if ((localActionV2.hasActionV2Extension()) && (localActionV2.getActionV2Extension().hasPhoneActionExtension())) {
      PhoneActionUtils.mergePhoneAction(localPhoneAction, localActionV2.getActionV2Extension().getActionV2Extension().getPhoneActionExtension());
    }
    this.mActionListener.onPhoneAction(localPhoneAction);
    return true;
  }
  
  static abstract interface ActionListener
  {
    public abstract void onPhoneAction(ActionV2Protos.PhoneAction paramPhoneAction);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.handsfree.ActionProcessor
 * JD-Core Version:    0.7.0.1
 */