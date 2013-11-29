package com.google.android.velvet.gallery;

import android.util.Log;
import com.google.common.base.Preconditions;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ImageMetadataExtractor
  extends DefaultHandler
{
  private final StringBuilder mCharactersBuilder;
  private final ResultHandler mResultHandler;
  private boolean mWantCharacters = false;
  
  public ImageMetadataExtractor(ResultHandler paramResultHandler)
  {
    this.mResultHandler = paramResultHandler;
    this.mCharactersBuilder = new StringBuilder();
  }
  
  private String getCharacters()
  {
    Preconditions.checkState(this.mWantCharacters);
    this.mWantCharacters = false;
    String str = this.mCharactersBuilder.toString();
    this.mCharactersBuilder.setLength(0);
    return str;
  }
  
  private static void maybeLogExceptionDetails(SAXParseException paramSAXParseException, String paramString)
  {
    int i = -1 + paramSAXParseException.getLineNumber();
    int j = paramSAXParseException.getColumnNumber();
    try
    {
      String str = paramString.split("\\n")[i];
      int k = j - 50;
      if (k < 0) {
        k = 0;
      }
      int m = j + 50;
      if (m > str.length()) {
        m = str.length();
      }
      str.substring(k, m);
      return;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      Log.w("ImageMetadataExtractor", "Could not find text which caused the exception on line " + i);
    }
  }
  
  private void startGettingCharacters()
  {
    if (!this.mWantCharacters) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      this.mWantCharacters = true;
      return;
    }
  }
  
  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (this.mWantCharacters) {
      this.mCharactersBuilder.append(paramArrayOfChar, paramInt1, paramInt2);
    }
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3)
  {
    if (this.mWantCharacters)
    {
      String str = getCharacters();
      Log.v("ImageMetadataExtractor", "Extracted JSON: " + str);
      this.mResultHandler.onMetadataSectionExtracted(str);
    }
  }
  
  /* Error */
  public void parseXml(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield 17	com/google/android/velvet/gallery/ImageMetadataExtractor:mWantCharacters	Z
    //   7: invokestatic 120	javax/xml/parsers/SAXParserFactory:newInstance	()Ljavax/xml/parsers/SAXParserFactory;
    //   10: invokevirtual 124	javax/xml/parsers/SAXParserFactory:newSAXParser	()Ljavax/xml/parsers/SAXParser;
    //   13: invokevirtual 130	javax/xml/parsers/SAXParser:getXMLReader	()Lorg/xml/sax/XMLReader;
    //   16: astore 13
    //   18: aload 13
    //   20: aload_0
    //   21: invokeinterface 136 2 0
    //   26: aload 13
    //   28: new 138	org/xml/sax/InputSource
    //   31: dup
    //   32: new 140	java/io/StringReader
    //   35: dup
    //   36: aload_1
    //   37: invokespecial 142	java/io/StringReader:<init>	(Ljava/lang/String;)V
    //   40: invokespecial 145	org/xml/sax/InputSource:<init>	(Ljava/io/Reader;)V
    //   43: invokeinterface 149 2 0
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    //   51: astore 11
    //   53: ldc 69
    //   55: ldc 151
    //   57: aload 11
    //   59: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: goto -15 -> 48
    //   66: astore 4
    //   68: aload_0
    //   69: monitorexit
    //   70: aload 4
    //   72: athrow
    //   73: astore 9
    //   75: ldc 69
    //   77: ldc 156
    //   79: aload 9
    //   81: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   84: pop
    //   85: aload 9
    //   87: aload_1
    //   88: invokestatic 158	com/google/android/velvet/gallery/ImageMetadataExtractor:maybeLogExceptionDetails	(Lorg/xml/sax/SAXParseException;Ljava/lang/String;)V
    //   91: goto -43 -> 48
    //   94: astore 7
    //   96: ldc 69
    //   98: ldc 160
    //   100: aload 7
    //   102: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   105: pop
    //   106: goto -58 -> 48
    //   109: astore 5
    //   111: ldc 69
    //   113: ldc 162
    //   115: aload 5
    //   117: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   120: pop
    //   121: goto -73 -> 48
    //   124: astore_2
    //   125: ldc 69
    //   127: ldc 164
    //   129: aload_2
    //   130: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   133: pop
    //   134: goto -86 -> 48
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	137	0	this	ImageMetadataExtractor
    //   0	137	1	paramString	String
    //   124	6	2	localIllegalStateException	java.lang.IllegalStateException
    //   66	5	4	localObject	Object
    //   109	7	5	localParserConfigurationException	javax.xml.parsers.ParserConfigurationException
    //   94	7	7	localSAXException	org.xml.sax.SAXException
    //   73	13	9	localSAXParseException	SAXParseException
    //   51	7	11	localIOException	java.io.IOException
    //   16	11	13	localXMLReader	org.xml.sax.XMLReader
    // Exception table:
    //   from	to	target	type
    //   2	48	51	java/io/IOException
    //   2	48	66	finally
    //   53	63	66	finally
    //   75	91	66	finally
    //   96	106	66	finally
    //   111	121	66	finally
    //   125	134	66	finally
    //   2	48	73	org/xml/sax/SAXParseException
    //   2	48	94	org/xml/sax/SAXException
    //   2	48	109	javax/xml/parsers/ParserConfigurationException
    //   2	48	124	java/lang/IllegalStateException
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
  {
    if ("gsamd".equals(paramAttributes.getValue("id"))) {
      startGettingCharacters();
    }
  }
  
  public static abstract interface ResultHandler
  {
    public abstract void onMetadataSectionExtracted(String paramString);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.velvet.gallery.ImageMetadataExtractor
 * JD-Core Version:    0.7.0.1
 */