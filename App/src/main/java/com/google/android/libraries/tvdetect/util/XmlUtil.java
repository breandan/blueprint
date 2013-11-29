package com.google.android.libraries.tvdetect.util;

import com.google.android.libraries.tvdetect.Device;
import com.google.android.libraries.tvdetect.Device.Builder;
import com.google.android.libraries.tvdetect.DeviceModel;
import com.google.android.libraries.tvdetect.DeviceModel.Builder;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil
{
  private static String getElementImmediateDescendantValue(Element paramElement, String paramString1, String paramString2)
  {
    NodeList localNodeList = paramElement.getChildNodes();
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      if (((localNode instanceof Element)) && (paramString1.equals(localNode.getLocalName())) && (paramString2.equals(localNode.getNamespaceURI()))) {
        return localNode.getTextContent().trim();
      }
    }
    return null;
  }
  
  public static Device parseDeviceDetails(byte[] paramArrayOfByte, String paramString1, String paramString2, String paramString3)
  {
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    localDocumentBuilderFactory.setNamespaceAware(true);
    try
    {
      NodeList localNodeList = localDocumentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(paramArrayOfByte)).getDocumentElement().getElementsByTagName("device");
      Device.Builder localBuilder = Device.newBuilder();
      if (localNodeList.getLength() == 0)
      {
        L.w("No devices found in DDD.");
        return null;
      }
      Element localElement = (Element)localNodeList.item(0);
      localBuilder.setDeviceDescriptionUrl(paramString1);
      localBuilder.setFriendlyName(getElementImmediateDescendantValue(localElement, "friendlyName", "urn:schemas-upnp-org:device-1-0"));
      localBuilder.setUuid(paramString3);
      localBuilder.setNetworkBssid(paramString2);
      DeviceModel.Builder localBuilder1 = DeviceModel.newBuilder();
      localBuilder1.setManufacturer(getElementImmediateDescendantValue(localElement, "manufacturer", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setManufacturerUrl(getElementImmediateDescendantValue(localElement, "manufacturerURL", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setModelDescription(getElementImmediateDescendantValue(localElement, "modelDescription", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setModelName(getElementImmediateDescendantValue(localElement, "modelName", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setModelId(getElementImmediateDescendantValue(localElement, "X_modelId", "http://schemas.microsoft.com/windows/2008/09/devicefoundation"));
      localBuilder1.setModelNumber(getElementImmediateDescendantValue(localElement, "modelNumber", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setModelUrl(getElementImmediateDescendantValue(localElement, "modelURL", "urn:schemas-upnp-org:device-1-0"));
      localBuilder1.setCategory(getElementImmediateDescendantValue(localElement, "X_deviceCategory", "http://schemas.microsoft.com/windows/2008/09/devicefoundation"));
      localBuilder.setDeviceModel(localBuilder1.build());
      Device localDevice = localBuilder.build();
      return localDevice;
    }
    catch (Throwable localThrowable)
    {
      L.e("Error parsing DDD.");
    }
    return null;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.util.XmlUtil
 * JD-Core Version:    0.7.0.1
 */