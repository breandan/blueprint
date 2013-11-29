package com.google.android.common.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringUtil
{
  static final Map<String, Character> ESCAPE_STRINGS = new HashMap(252);
  static final Set<Character> HEX_LETTERS;
  
  static
  {
    ESCAPE_STRINGS.put("&nbsp", Character.valueOf(' '));
    ESCAPE_STRINGS.put("&iexcl", Character.valueOf('¡'));
    ESCAPE_STRINGS.put("&cent", Character.valueOf('¢'));
    ESCAPE_STRINGS.put("&pound", Character.valueOf('£'));
    ESCAPE_STRINGS.put("&curren", Character.valueOf('¤'));
    ESCAPE_STRINGS.put("&yen", Character.valueOf('¥'));
    ESCAPE_STRINGS.put("&brvbar", Character.valueOf('¦'));
    ESCAPE_STRINGS.put("&sect", Character.valueOf('§'));
    ESCAPE_STRINGS.put("&uml", Character.valueOf('¨'));
    ESCAPE_STRINGS.put("&copy", Character.valueOf('©'));
    ESCAPE_STRINGS.put("&ordf", Character.valueOf('ª'));
    ESCAPE_STRINGS.put("&laquo", Character.valueOf('«'));
    ESCAPE_STRINGS.put("&not", Character.valueOf('¬'));
    ESCAPE_STRINGS.put("&shy", Character.valueOf('­'));
    ESCAPE_STRINGS.put("&reg", Character.valueOf('®'));
    ESCAPE_STRINGS.put("&macr", Character.valueOf('¯'));
    ESCAPE_STRINGS.put("&deg", Character.valueOf('°'));
    ESCAPE_STRINGS.put("&plusmn", Character.valueOf('±'));
    ESCAPE_STRINGS.put("&sup2", Character.valueOf('²'));
    ESCAPE_STRINGS.put("&sup3", Character.valueOf('³'));
    ESCAPE_STRINGS.put("&acute", Character.valueOf('´'));
    ESCAPE_STRINGS.put("&micro", Character.valueOf('µ'));
    ESCAPE_STRINGS.put("&para", Character.valueOf('¶'));
    ESCAPE_STRINGS.put("&middot", Character.valueOf('·'));
    ESCAPE_STRINGS.put("&cedil", Character.valueOf('¸'));
    ESCAPE_STRINGS.put("&sup1", Character.valueOf('¹'));
    ESCAPE_STRINGS.put("&ordm", Character.valueOf('º'));
    ESCAPE_STRINGS.put("&raquo", Character.valueOf('»'));
    ESCAPE_STRINGS.put("&frac14", Character.valueOf('¼'));
    ESCAPE_STRINGS.put("&frac12", Character.valueOf('½'));
    ESCAPE_STRINGS.put("&frac34", Character.valueOf('¾'));
    ESCAPE_STRINGS.put("&iquest", Character.valueOf('¿'));
    ESCAPE_STRINGS.put("&Agrave", Character.valueOf('À'));
    ESCAPE_STRINGS.put("&Aacute", Character.valueOf('Á'));
    ESCAPE_STRINGS.put("&Acirc", Character.valueOf('Â'));
    ESCAPE_STRINGS.put("&Atilde", Character.valueOf('Ã'));
    ESCAPE_STRINGS.put("&Auml", Character.valueOf('Ä'));
    ESCAPE_STRINGS.put("&Aring", Character.valueOf('Å'));
    ESCAPE_STRINGS.put("&AElig", Character.valueOf('Æ'));
    ESCAPE_STRINGS.put("&Ccedil", Character.valueOf('Ç'));
    ESCAPE_STRINGS.put("&Egrave", Character.valueOf('È'));
    ESCAPE_STRINGS.put("&Eacute", Character.valueOf('É'));
    ESCAPE_STRINGS.put("&Ecirc", Character.valueOf('Ê'));
    ESCAPE_STRINGS.put("&Euml", Character.valueOf('Ë'));
    ESCAPE_STRINGS.put("&Igrave", Character.valueOf('Ì'));
    ESCAPE_STRINGS.put("&Iacute", Character.valueOf('Í'));
    ESCAPE_STRINGS.put("&Icirc", Character.valueOf('Î'));
    ESCAPE_STRINGS.put("&Iuml", Character.valueOf('Ï'));
    ESCAPE_STRINGS.put("&ETH", Character.valueOf('Ð'));
    ESCAPE_STRINGS.put("&Ntilde", Character.valueOf('Ñ'));
    ESCAPE_STRINGS.put("&Ograve", Character.valueOf('Ò'));
    ESCAPE_STRINGS.put("&Oacute", Character.valueOf('Ó'));
    ESCAPE_STRINGS.put("&Ocirc", Character.valueOf('Ô'));
    ESCAPE_STRINGS.put("&Otilde", Character.valueOf('Õ'));
    ESCAPE_STRINGS.put("&Ouml", Character.valueOf('Ö'));
    ESCAPE_STRINGS.put("&times", Character.valueOf('×'));
    ESCAPE_STRINGS.put("&Oslash", Character.valueOf('Ø'));
    ESCAPE_STRINGS.put("&Ugrave", Character.valueOf('Ù'));
    ESCAPE_STRINGS.put("&Uacute", Character.valueOf('Ú'));
    ESCAPE_STRINGS.put("&Ucirc", Character.valueOf('Û'));
    ESCAPE_STRINGS.put("&Uuml", Character.valueOf('Ü'));
    ESCAPE_STRINGS.put("&Yacute", Character.valueOf('Ý'));
    ESCAPE_STRINGS.put("&THORN", Character.valueOf('Þ'));
    ESCAPE_STRINGS.put("&szlig", Character.valueOf('ß'));
    ESCAPE_STRINGS.put("&agrave", Character.valueOf('à'));
    ESCAPE_STRINGS.put("&aacute", Character.valueOf('á'));
    ESCAPE_STRINGS.put("&acirc", Character.valueOf('â'));
    ESCAPE_STRINGS.put("&atilde", Character.valueOf('ã'));
    ESCAPE_STRINGS.put("&auml", Character.valueOf('ä'));
    ESCAPE_STRINGS.put("&aring", Character.valueOf('å'));
    ESCAPE_STRINGS.put("&aelig", Character.valueOf('æ'));
    ESCAPE_STRINGS.put("&ccedil", Character.valueOf('ç'));
    ESCAPE_STRINGS.put("&egrave", Character.valueOf('è'));
    ESCAPE_STRINGS.put("&eacute", Character.valueOf('é'));
    ESCAPE_STRINGS.put("&ecirc", Character.valueOf('ê'));
    ESCAPE_STRINGS.put("&euml", Character.valueOf('ë'));
    ESCAPE_STRINGS.put("&igrave", Character.valueOf('ì'));
    ESCAPE_STRINGS.put("&iacute", Character.valueOf('í'));
    ESCAPE_STRINGS.put("&icirc", Character.valueOf('î'));
    ESCAPE_STRINGS.put("&iuml", Character.valueOf('ï'));
    ESCAPE_STRINGS.put("&eth", Character.valueOf('ð'));
    ESCAPE_STRINGS.put("&ntilde", Character.valueOf('ñ'));
    ESCAPE_STRINGS.put("&ograve", Character.valueOf('ò'));
    ESCAPE_STRINGS.put("&oacute", Character.valueOf('ó'));
    ESCAPE_STRINGS.put("&ocirc", Character.valueOf('ô'));
    ESCAPE_STRINGS.put("&otilde", Character.valueOf('õ'));
    ESCAPE_STRINGS.put("&ouml", Character.valueOf('ö'));
    ESCAPE_STRINGS.put("&divide", Character.valueOf('÷'));
    ESCAPE_STRINGS.put("&oslash", Character.valueOf('ø'));
    ESCAPE_STRINGS.put("&ugrave", Character.valueOf('ù'));
    ESCAPE_STRINGS.put("&uacute", Character.valueOf('ú'));
    ESCAPE_STRINGS.put("&ucirc", Character.valueOf('û'));
    ESCAPE_STRINGS.put("&uuml", Character.valueOf('ü'));
    ESCAPE_STRINGS.put("&yacute", Character.valueOf('ý'));
    ESCAPE_STRINGS.put("&thorn", Character.valueOf('þ'));
    ESCAPE_STRINGS.put("&yuml", Character.valueOf('ÿ'));
    ESCAPE_STRINGS.put("&fnof", Character.valueOf('ƒ'));
    ESCAPE_STRINGS.put("&Alpha", Character.valueOf('Α'));
    ESCAPE_STRINGS.put("&Beta", Character.valueOf('Β'));
    ESCAPE_STRINGS.put("&Gamma", Character.valueOf('Γ'));
    ESCAPE_STRINGS.put("&Delta", Character.valueOf('Δ'));
    ESCAPE_STRINGS.put("&Epsilon", Character.valueOf('Ε'));
    ESCAPE_STRINGS.put("&Zeta", Character.valueOf('Ζ'));
    ESCAPE_STRINGS.put("&Eta", Character.valueOf('Η'));
    ESCAPE_STRINGS.put("&Theta", Character.valueOf('Θ'));
    ESCAPE_STRINGS.put("&Iota", Character.valueOf('Ι'));
    ESCAPE_STRINGS.put("&Kappa", Character.valueOf('Κ'));
    ESCAPE_STRINGS.put("&Lambda", Character.valueOf('Λ'));
    ESCAPE_STRINGS.put("&Mu", Character.valueOf('Μ'));
    ESCAPE_STRINGS.put("&Nu", Character.valueOf('Ν'));
    ESCAPE_STRINGS.put("&Xi", Character.valueOf('Ξ'));
    ESCAPE_STRINGS.put("&Omicron", Character.valueOf('Ο'));
    ESCAPE_STRINGS.put("&Pi", Character.valueOf('Π'));
    ESCAPE_STRINGS.put("&Rho", Character.valueOf('Ρ'));
    ESCAPE_STRINGS.put("&Sigma", Character.valueOf('Σ'));
    ESCAPE_STRINGS.put("&Tau", Character.valueOf('Τ'));
    ESCAPE_STRINGS.put("&Upsilon", Character.valueOf('Υ'));
    ESCAPE_STRINGS.put("&Phi", Character.valueOf('Φ'));
    ESCAPE_STRINGS.put("&Chi", Character.valueOf('Χ'));
    ESCAPE_STRINGS.put("&Psi", Character.valueOf('Ψ'));
    ESCAPE_STRINGS.put("&Omega", Character.valueOf('Ω'));
    ESCAPE_STRINGS.put("&alpha", Character.valueOf('α'));
    ESCAPE_STRINGS.put("&beta", Character.valueOf('β'));
    ESCAPE_STRINGS.put("&gamma", Character.valueOf('γ'));
    ESCAPE_STRINGS.put("&delta", Character.valueOf('δ'));
    ESCAPE_STRINGS.put("&epsilon", Character.valueOf('ε'));
    ESCAPE_STRINGS.put("&zeta", Character.valueOf('ζ'));
    ESCAPE_STRINGS.put("&eta", Character.valueOf('η'));
    ESCAPE_STRINGS.put("&theta", Character.valueOf('θ'));
    ESCAPE_STRINGS.put("&iota", Character.valueOf('ι'));
    ESCAPE_STRINGS.put("&kappa", Character.valueOf('κ'));
    ESCAPE_STRINGS.put("&lambda", Character.valueOf('λ'));
    ESCAPE_STRINGS.put("&mu", Character.valueOf('μ'));
    ESCAPE_STRINGS.put("&nu", Character.valueOf('ν'));
    ESCAPE_STRINGS.put("&xi", Character.valueOf('ξ'));
    ESCAPE_STRINGS.put("&omicron", Character.valueOf('ο'));
    ESCAPE_STRINGS.put("&pi", Character.valueOf('π'));
    ESCAPE_STRINGS.put("&rho", Character.valueOf('ρ'));
    ESCAPE_STRINGS.put("&sigmaf", Character.valueOf('ς'));
    ESCAPE_STRINGS.put("&sigma", Character.valueOf('σ'));
    ESCAPE_STRINGS.put("&tau", Character.valueOf('τ'));
    ESCAPE_STRINGS.put("&upsilon", Character.valueOf('υ'));
    ESCAPE_STRINGS.put("&phi", Character.valueOf('φ'));
    ESCAPE_STRINGS.put("&chi", Character.valueOf('χ'));
    ESCAPE_STRINGS.put("&psi", Character.valueOf('ψ'));
    ESCAPE_STRINGS.put("&omega", Character.valueOf('ω'));
    ESCAPE_STRINGS.put("&thetasym", Character.valueOf('ϑ'));
    ESCAPE_STRINGS.put("&upsih", Character.valueOf('ϒ'));
    ESCAPE_STRINGS.put("&piv", Character.valueOf('ϖ'));
    ESCAPE_STRINGS.put("&bull", Character.valueOf('•'));
    ESCAPE_STRINGS.put("&hellip", Character.valueOf('…'));
    ESCAPE_STRINGS.put("&prime", Character.valueOf('′'));
    ESCAPE_STRINGS.put("&Prime", Character.valueOf('″'));
    ESCAPE_STRINGS.put("&oline", Character.valueOf('‾'));
    ESCAPE_STRINGS.put("&frasl", Character.valueOf('⁄'));
    ESCAPE_STRINGS.put("&weierp", Character.valueOf('℘'));
    ESCAPE_STRINGS.put("&image", Character.valueOf('ℑ'));
    ESCAPE_STRINGS.put("&real", Character.valueOf('ℜ'));
    ESCAPE_STRINGS.put("&trade", Character.valueOf('™'));
    ESCAPE_STRINGS.put("&alefsym", Character.valueOf('ℵ'));
    ESCAPE_STRINGS.put("&larr", Character.valueOf('←'));
    ESCAPE_STRINGS.put("&uarr", Character.valueOf('↑'));
    ESCAPE_STRINGS.put("&rarr", Character.valueOf('→'));
    ESCAPE_STRINGS.put("&darr", Character.valueOf('↓'));
    ESCAPE_STRINGS.put("&harr", Character.valueOf('↔'));
    ESCAPE_STRINGS.put("&crarr", Character.valueOf('↵'));
    ESCAPE_STRINGS.put("&lArr", Character.valueOf('⇐'));
    ESCAPE_STRINGS.put("&uArr", Character.valueOf('⇑'));
    ESCAPE_STRINGS.put("&rArr", Character.valueOf('⇒'));
    ESCAPE_STRINGS.put("&dArr", Character.valueOf('⇓'));
    ESCAPE_STRINGS.put("&hArr", Character.valueOf('⇔'));
    ESCAPE_STRINGS.put("&forall", Character.valueOf('∀'));
    ESCAPE_STRINGS.put("&part", Character.valueOf('∂'));
    ESCAPE_STRINGS.put("&exist", Character.valueOf('∃'));
    ESCAPE_STRINGS.put("&empty", Character.valueOf('∅'));
    ESCAPE_STRINGS.put("&nabla", Character.valueOf('∇'));
    ESCAPE_STRINGS.put("&isin", Character.valueOf('∈'));
    ESCAPE_STRINGS.put("&notin", Character.valueOf('∉'));
    ESCAPE_STRINGS.put("&ni", Character.valueOf('∋'));
    ESCAPE_STRINGS.put("&prod", Character.valueOf('∏'));
    ESCAPE_STRINGS.put("&sum", Character.valueOf('∑'));
    ESCAPE_STRINGS.put("&minus", Character.valueOf('−'));
    ESCAPE_STRINGS.put("&lowast", Character.valueOf('∗'));
    ESCAPE_STRINGS.put("&radic", Character.valueOf('√'));
    ESCAPE_STRINGS.put("&prop", Character.valueOf('∝'));
    ESCAPE_STRINGS.put("&infin", Character.valueOf('∞'));
    ESCAPE_STRINGS.put("&ang", Character.valueOf('∠'));
    ESCAPE_STRINGS.put("&and", Character.valueOf('∧'));
    ESCAPE_STRINGS.put("&or", Character.valueOf('∨'));
    ESCAPE_STRINGS.put("&cap", Character.valueOf('∩'));
    ESCAPE_STRINGS.put("&cup", Character.valueOf('∪'));
    ESCAPE_STRINGS.put("&int", Character.valueOf('∫'));
    ESCAPE_STRINGS.put("&there4", Character.valueOf('∴'));
    ESCAPE_STRINGS.put("&sim", Character.valueOf('∼'));
    ESCAPE_STRINGS.put("&cong", Character.valueOf('≅'));
    ESCAPE_STRINGS.put("&asymp", Character.valueOf('≈'));
    ESCAPE_STRINGS.put("&ne", Character.valueOf('≠'));
    ESCAPE_STRINGS.put("&equiv", Character.valueOf('≡'));
    ESCAPE_STRINGS.put("&le", Character.valueOf('≤'));
    ESCAPE_STRINGS.put("&ge", Character.valueOf('≥'));
    ESCAPE_STRINGS.put("&sub", Character.valueOf('⊂'));
    ESCAPE_STRINGS.put("&sup", Character.valueOf('⊃'));
    ESCAPE_STRINGS.put("&nsub", Character.valueOf('⊄'));
    ESCAPE_STRINGS.put("&sube", Character.valueOf('⊆'));
    ESCAPE_STRINGS.put("&supe", Character.valueOf('⊇'));
    ESCAPE_STRINGS.put("&oplus", Character.valueOf('⊕'));
    ESCAPE_STRINGS.put("&otimes", Character.valueOf('⊗'));
    ESCAPE_STRINGS.put("&perp", Character.valueOf('⊥'));
    ESCAPE_STRINGS.put("&sdot", Character.valueOf('⋅'));
    ESCAPE_STRINGS.put("&lceil", Character.valueOf('⌈'));
    ESCAPE_STRINGS.put("&rceil", Character.valueOf('⌉'));
    ESCAPE_STRINGS.put("&lfloor", Character.valueOf('⌊'));
    ESCAPE_STRINGS.put("&rfloor", Character.valueOf('⌋'));
    ESCAPE_STRINGS.put("&lang", Character.valueOf('〈'));
    ESCAPE_STRINGS.put("&rang", Character.valueOf('〉'));
    ESCAPE_STRINGS.put("&loz", Character.valueOf('◊'));
    ESCAPE_STRINGS.put("&spades", Character.valueOf('♠'));
    ESCAPE_STRINGS.put("&clubs", Character.valueOf('♣'));
    ESCAPE_STRINGS.put("&hearts", Character.valueOf('♥'));
    ESCAPE_STRINGS.put("&diams", Character.valueOf('♦'));
    ESCAPE_STRINGS.put("&quot", Character.valueOf('"'));
    ESCAPE_STRINGS.put("&amp", Character.valueOf('&'));
    ESCAPE_STRINGS.put("&lt", Character.valueOf('<'));
    ESCAPE_STRINGS.put("&gt", Character.valueOf('>'));
    ESCAPE_STRINGS.put("&OElig", Character.valueOf('Œ'));
    ESCAPE_STRINGS.put("&oelig", Character.valueOf('œ'));
    ESCAPE_STRINGS.put("&Scaron", Character.valueOf('Š'));
    ESCAPE_STRINGS.put("&scaron", Character.valueOf('š'));
    ESCAPE_STRINGS.put("&Yuml", Character.valueOf('Ÿ'));
    ESCAPE_STRINGS.put("&circ", Character.valueOf('ˆ'));
    ESCAPE_STRINGS.put("&tilde", Character.valueOf('˜'));
    ESCAPE_STRINGS.put("&ensp", Character.valueOf(' '));
    ESCAPE_STRINGS.put("&emsp", Character.valueOf(' '));
    ESCAPE_STRINGS.put("&thinsp", Character.valueOf(' '));
    ESCAPE_STRINGS.put("&zwnj", Character.valueOf('‌'));
    ESCAPE_STRINGS.put("&zwj", Character.valueOf('‍'));
    ESCAPE_STRINGS.put("&lrm", Character.valueOf('‎'));
    ESCAPE_STRINGS.put("&rlm", Character.valueOf('‏'));
    ESCAPE_STRINGS.put("&ndash", Character.valueOf('–'));
    ESCAPE_STRINGS.put("&mdash", Character.valueOf('—'));
    ESCAPE_STRINGS.put("&lsquo", Character.valueOf('‘'));
    ESCAPE_STRINGS.put("&rsquo", Character.valueOf('’'));
    ESCAPE_STRINGS.put("&sbquo", Character.valueOf('‚'));
    ESCAPE_STRINGS.put("&ldquo", Character.valueOf('“'));
    ESCAPE_STRINGS.put("&rdquo", Character.valueOf('”'));
    ESCAPE_STRINGS.put("&bdquo", Character.valueOf('„'));
    ESCAPE_STRINGS.put("&dagger", Character.valueOf('†'));
    ESCAPE_STRINGS.put("&Dagger", Character.valueOf('‡'));
    ESCAPE_STRINGS.put("&permil", Character.valueOf('‰'));
    ESCAPE_STRINGS.put("&lsaquo", Character.valueOf('‹'));
    ESCAPE_STRINGS.put("&rsaquo", Character.valueOf('›'));
    ESCAPE_STRINGS.put("&euro", Character.valueOf('€'));
    HEX_LETTERS = new HashSet(12);
    HEX_LETTERS.add(Character.valueOf('a'));
    HEX_LETTERS.add(Character.valueOf('A'));
    HEX_LETTERS.add(Character.valueOf('b'));
    HEX_LETTERS.add(Character.valueOf('B'));
    HEX_LETTERS.add(Character.valueOf('c'));
    HEX_LETTERS.add(Character.valueOf('C'));
    HEX_LETTERS.add(Character.valueOf('d'));
    HEX_LETTERS.add(Character.valueOf('D'));
    HEX_LETTERS.add(Character.valueOf('e'));
    HEX_LETTERS.add(Character.valueOf('E'));
    HEX_LETTERS.add(Character.valueOf('f'));
    HEX_LETTERS.add(Character.valueOf('F'));
  }
  
  public static final String unescapeHTML(String paramString)
  {
    return unescapeHTML(paramString, false);
  }
  
  public static final String unescapeHTML(String paramString, boolean paramBoolean)
  {
    int i = paramString.indexOf('&');
    if (i == -1) {
      return paramString;
    }
    char[] arrayOfChar1 = paramString.toCharArray();
    char[] arrayOfChar2 = new char[arrayOfChar1.length];
    System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
    j = i;
    int k = i;
    while (k < arrayOfChar1.length) {
      if (arrayOfChar1[k] != '&')
      {
        int i14 = j + 1;
        int i15 = k + 1;
        arrayOfChar2[j] = arrayOfChar1[k];
        k = i15;
        j = i14;
      }
      else
      {
        int m = k + 1;
        int n = arrayOfChar1.length;
        int i1 = 0;
        if (m < n)
        {
          int i13 = arrayOfChar1[m];
          i1 = 0;
          if (i13 == 35)
          {
            m++;
            i1 = 1;
          }
        }
        int i2 = arrayOfChar1.length;
        int i3 = 0;
        if (m < i2) {
          if (arrayOfChar1[m] != 'x')
          {
            int i12 = arrayOfChar1[m];
            i3 = 0;
            if (i12 != 88) {}
          }
          else
          {
            m++;
            i3 = 1;
          }
        }
        label175:
        char c2;
        boolean bool3;
        label210:
        long l1;
        int i9;
        if (m < arrayOfChar1.length)
        {
          c2 = arrayOfChar1[m];
          bool3 = Character.isDigit(c2);
          if (i1 == 0) {
            break label472;
          }
          if ((i3 != 0) || (bool3)) {}
        }
        else if ((m > arrayOfChar1.length) || (!paramBoolean))
        {
          int i4 = arrayOfChar1.length;
          i5 = 0;
          if (m < i4)
          {
            int i7 = arrayOfChar1[m];
            i5 = 0;
            if (i7 != 59) {}
          }
        }
        else
        {
          if ((k + 2 >= arrayOfChar1.length) || (paramString.charAt(k + 1) != '#')) {
            break label535;
          }
          l1 = 0L;
          i9 = k + 2;
        }
        for (;;)
        {
          try
          {
            c1 = paramString.charAt(i9);
            if (i3 != 0)
            {
              String str3 = new String(arrayOfChar1, k + 3, -3 + (m - k));
              long l2 = Long.parseLong(str3, 16);
              l1 = l2;
              boolean bool1 = l1 < 0L;
              i5 = 0;
              if (bool1)
              {
                boolean bool2 = l1 < 65536L;
                i5 = 0;
                if (bool2)
                {
                  i10 = j + 1;
                  i11 = (char)(int)l1;
                }
              }
            }
          }
          catch (NumberFormatException localNumberFormatException1)
          {
            char c1;
            int i11;
            int i6;
            label472:
            String str4;
            long l3;
            label535:
            String str2;
            Character localCharacter;
            int i8;
            String str1;
            i5 = 0;
            continue;
          }
          try
          {
            arrayOfChar2[j] = i11;
            i5 = 1;
            j = i10;
          }
          catch (NumberFormatException localNumberFormatException2)
          {
            j = i10;
            i5 = 0;
            continue;
          }
          if ((m < arrayOfChar1.length) && (arrayOfChar1[m] == ';')) {
            m++;
          }
          if (i5 == 0)
          {
            i6 = m - k;
            System.arraycopy(arrayOfChar1, k, arrayOfChar2, j, i6);
            j += m - k;
          }
          k = m;
          break;
          if ((i3 != 0) && (!bool3) && (!HEX_LETTERS.contains(Character.valueOf(c2)))) {
            break label210;
          }
          if ((!bool3) && (!Character.isLetter(c2))) {
            break label210;
          }
          m++;
          break label175;
          if (Character.isDigit(c1))
          {
            str4 = new String(arrayOfChar1, k + 2, -2 + (m - k));
            l3 = Long.parseLong(str4);
            l1 = l3;
            continue;
            str2 = new String(arrayOfChar1, k, m - k);
            localCharacter = (Character)ESCAPE_STRINGS.get(str2);
            i5 = 0;
            if (localCharacter != null)
            {
              i8 = j + 1;
              arrayOfChar2[j] = localCharacter.charValue();
              i5 = 1;
              j = i8;
            }
          }
        }
      }
    }
    str1 = new String(arrayOfChar2, 0, j);
    return str1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.common.base.StringUtil
 * JD-Core Version:    0.7.0.1
 */