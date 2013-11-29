package com.google.android.voicesearch.greco3;

import com.google.android.speech.message.GsaS3ResponseProcessor;
import com.google.majel.proto.ActionV2Protos.ActionV2;
import com.google.majel.proto.MajelProtos.MajelResponse;
import com.google.majel.proto.PeanutProtos.Peanut;
import com.google.speech.s3.S3.S3Response;
import com.google.speech.speech.s3.Majel.MajelServiceEvent;

class PhoneActionsMerger
{
  private ActionV2Protos.ActionV2 mergableAction;
  
  private ActionV2Protos.ActionV2 getActionV2(S3.S3Response paramS3Response)
  {
    if (!paramS3Response.hasMajelServiceEventExtension()) {}
    MajelProtos.MajelResponse localMajelResponse;
    do
    {
      do
      {
        return null;
        GsaS3ResponseProcessor.processMajelServiceEvent(paramS3Response.getMajelServiceEventExtension());
      } while (!paramS3Response.getMajelServiceEventExtension().hasMajel());
      localMajelResponse = paramS3Response.getMajelServiceEventExtension().getMajel();
    } while ((localMajelResponse.getPeanutCount() <= 0) || (localMajelResponse.getPeanut(0).getActionV2Count() <= 0));
    return localMajelResponse.getPeanut(0).getActionV2(0);
  }
  
  private ActionV2Protos.ActionV2 getMergableAction(S3.S3Response paramS3Response)
  {
    ActionV2Protos.ActionV2 localActionV2 = getActionV2(paramS3Response);
    if ((localActionV2 != null) && ((localActionV2.hasPhoneActionExtension()) || (localActionV2.hasCallBusinessActionExtension()))) {
      return localActionV2;
    }
    return null;
  }
  
  void mergeWithEmbeddedResponses(S3.S3Response paramS3Response)
  {
    if (this.mergableAction == null) {}
    ActionV2Protos.ActionV2 localActionV2;
    do
    {
      return;
      localActionV2 = getMergableAction(paramS3Response);
    } while (localActionV2 == null);
    localActionV2.setActionV2Extension(this.mergableAction);
  }
  
  void process(S3.S3Response paramS3Response)
  {
    this.mergableAction = getMergableAction(paramS3Response);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.voicesearch.greco3.PhoneActionsMerger
 * JD-Core Version:    0.7.0.1
 */