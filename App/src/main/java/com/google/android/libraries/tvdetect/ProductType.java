package com.google.android.libraries.tvdetect;

public enum ProductType
{
  static
  {
    TV = new ProductType("TV", 1);
    GAME_CONSOLE = new ProductType("GAME_CONSOLE", 2);
    BLURAY_PLAYER = new ProductType("BLURAY_PLAYER", 3);
    ProductType[] arrayOfProductType = new ProductType[4];
    arrayOfProductType[0] = UNKNOWN;
    arrayOfProductType[1] = TV;
    arrayOfProductType[2] = GAME_CONSOLE;
    arrayOfProductType[3] = BLURAY_PLAYER;
    $VALUES = arrayOfProductType;
  }
  
  private ProductType() {}
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.libraries.tvdetect.ProductType
 * JD-Core Version:    0.7.0.1
 */