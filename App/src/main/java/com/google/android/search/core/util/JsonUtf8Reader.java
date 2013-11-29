package com.google.android.search.core.util;

import android.util.JsonToken;
import android.util.MalformedJsonException;
import com.google.android.voicesearch.logger.BugLogger;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtf8Reader
  implements Closeable
{
  private static final byte[] FALSE = { 102, 97, 108, 115, 101 };
  private static final byte[] NULL = { 110, 117, 108, 108 };
  private static final byte[] TRUE = { 116, 114, 117, 101 };
  private byte[] buffer = new byte[8192];
  private int eolPos = -1;
  private final InputStream in;
  private boolean lenient = false;
  private int limit = 0;
  private int lineNumber = 1;
  private StringRef name;
  private int pos = 0;
  private final StringRef reusableStringRef;
  private final List<JsonScope> stack = new ArrayList();
  private final Utf8StringPool stringPool = new Utf8StringPool();
  private JsonToken token;
  private StringRef value;
  
  public JsonUtf8Reader(InputStream paramInputStream)
  {
    push(JsonScope.EMPTY_DOCUMENT);
    this.reusableStringRef = new StringRef();
    if (paramInputStream == null) {
      throw new NullPointerException("in == null");
    }
    this.in = paramInputStream;
  }
  
  private int addEscapeCharacter(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte1 = this.buffer;
    int i = this.pos;
    this.pos = (i + 1);
    int j = arrayOfByte1[i];
    switch (j)
    {
    }
    for (;;)
    {
      byte[] arrayOfByte2 = this.buffer;
      int k = paramInt + 1;
      arrayOfByte2[paramInt] = j;
      return k;
      return addUnicodeCharacter(paramInt, readUnicodeHexValue());
      j = 9;
      continue;
      j = 8;
      continue;
      j = 10;
      continue;
      j = 13;
      continue;
      j = 12;
      continue;
      BugLogger.record(8642715);
      throw new IOException("Found \\x in JSON");
      this.lineNumber = (1 + this.lineNumber);
      this.eolPos = (-1 + this.pos);
    }
  }
  
  private int addToBuffer(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt3 - paramInt2;
    if ((i != 0) && (paramInt2 != paramInt1)) {
      System.arraycopy(this.buffer, paramInt2, this.buffer, paramInt1, i);
    }
    return paramInt1 + i;
  }
  
  private int addUnicodeCharacter(int paramInt1, int paramInt2)
  {
    if (paramInt2 < 128)
    {
      byte[] arrayOfByte6 = this.buffer;
      int i1 = paramInt1 + 1;
      arrayOfByte6[paramInt1] = ((byte)paramInt2);
      return i1;
    }
    if (paramInt2 < 2048)
    {
      byte[] arrayOfByte4 = this.buffer;
      int m = paramInt1 + 1;
      arrayOfByte4[paramInt1] = ((byte)(0xC0 | paramInt2 >> 6));
      byte[] arrayOfByte5 = this.buffer;
      int n = m + 1;
      arrayOfByte5[m] = ((byte)(0x80 | paramInt2 & 0x3F));
      return n;
    }
    byte[] arrayOfByte1 = this.buffer;
    int i = paramInt1 + 1;
    arrayOfByte1[paramInt1] = ((byte)(0xE0 | paramInt2 >> 12));
    byte[] arrayOfByte2 = this.buffer;
    int j = i + 1;
    arrayOfByte2[i] = ((byte)(0x80 | 0x3F & paramInt2 >> 6));
    byte[] arrayOfByte3 = this.buffer;
    int k = j + 1;
    arrayOfByte3[j] = ((byte)(0x80 | paramInt2 & 0x3F));
    return k;
  }
  
  private JsonToken advance()
    throws IOException
  {
    peek();
    JsonToken localJsonToken = this.token;
    this.token = null;
    this.value = null;
    this.name = null;
    return localJsonToken;
  }
  
  private void checkLenient()
    throws IOException
  {
    if (!this.lenient) {
      throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
    }
  }
  
  private JsonToken decodeLiteral()
  {
    if ((this.value == this.reusableStringRef) && (this.value.mData == this.buffer)) {}
    int i;
    int j;
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      i = this.value.length();
      j = this.value.offset();
      if ((i != 4) || ((110 != this.buffer[j]) && (78 != this.buffer[j])) || ((117 != this.buffer[(j + 1)]) && (85 != this.buffer[(j + 1)])) || ((108 != this.buffer[(j + 2)]) && (76 != this.buffer[(j + 2)])) || ((108 != this.buffer[(j + 3)]) && (76 != this.buffer[(j + 3)]))) {
        break;
      }
      this.value.reset(NULL);
      return JsonToken.NULL;
    }
    if ((i == 4) && ((116 == this.buffer[j]) || (84 == this.buffer[j])) && ((114 == this.buffer[(j + 1)]) || (82 == this.buffer[(j + 1)])) && ((117 == this.buffer[(j + 2)]) || (85 == this.buffer[(j + 2)])) && ((101 == this.buffer[(j + 3)]) || (69 == this.buffer[(j + 3)])))
    {
      this.value.reset(TRUE);
      return JsonToken.BOOLEAN;
    }
    if ((i == 5) && ((102 == this.buffer[j]) || (70 == this.buffer[j])) && ((97 == this.buffer[(j + 1)]) || (65 == this.buffer[(j + 1)])) && ((108 == this.buffer[(j + 2)]) || (76 == this.buffer[(j + 2)])) && ((115 == this.buffer[(j + 3)]) || (83 == this.buffer[(j + 3)])) && ((101 == this.buffer[(j + 4)]) || (69 == this.buffer[(j + 4)])))
    {
      this.value.reset(FALSE);
      return JsonToken.BOOLEAN;
    }
    this.value.reset(this.buffer, j, i);
    return decodeNumber(this.buffer, j, i);
  }
  
  private JsonToken decodeNumber(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramArrayOfByte[i];
    if (j == 45)
    {
      i++;
      j = paramArrayOfByte[i];
    }
    int k;
    int m;
    if (j == 48)
    {
      k = i + 1;
      m = paramArrayOfByte[k];
    }
    while (m == 46)
    {
      k++;
      m = paramArrayOfByte[k];
      for (;;)
      {
        if ((m >= 48) && (m <= 57))
        {
          k++;
          m = paramArrayOfByte[k];
          continue;
          if ((j >= 49) && (j <= 57))
          {
            k = i + 1;
            for (m = paramArrayOfByte[k]; (m >= 48) && (m <= 57); m = paramArrayOfByte[k]) {
              k++;
            }
            break;
          }
          return JsonToken.STRING;
        }
      }
    }
    if ((m == 101) || (m == 69))
    {
      int n = k + 1;
      int i1 = paramArrayOfByte[n];
      if ((i1 == 43) || (i1 == 45))
      {
        n++;
        i1 = paramArrayOfByte[n];
      }
      int i2;
      if ((i1 >= 48) && (i1 <= 57))
      {
        k = n + 1;
        i2 = paramArrayOfByte[k];
      }
      while ((i2 >= 48) && (i2 <= 57))
      {
        k++;
        i2 = paramArrayOfByte[k];
        continue;
        return JsonToken.STRING;
      }
    }
    if (k == paramInt1 + paramInt2) {
      return JsonToken.NUMBER;
    }
    return JsonToken.STRING;
  }
  
  private void expect(JsonToken paramJsonToken)
    throws IOException
  {
    peek();
    if (this.token != paramJsonToken) {
      throw new IllegalStateException("Expected " + paramJsonToken + " but was " + peek());
    }
    advance();
  }
  
  private boolean fillBuffer(int paramInt)
    throws IOException
  {
    this.limit -= this.pos;
    this.eolPos -= this.pos;
    if (paramInt > this.buffer.length)
    {
      byte[] arrayOfByte = this.buffer;
      this.buffer = new byte[Math.max(paramInt, 2 * arrayOfByte.length)];
      System.arraycopy(arrayOfByte, this.pos, this.buffer, 0, this.limit);
    }
    for (;;)
    {
      this.pos = 0;
      do
      {
        int i = this.in.read(this.buffer, this.limit, Math.min(this.buffer.length - this.limit, 8192));
        bool = false;
        if (i == -1) {
          break;
        }
        this.limit = (i + this.limit);
      } while (this.limit < paramInt);
      boolean bool = true;
      return bool;
      if ((this.limit != 0) && (this.pos != 0)) {
        System.arraycopy(this.buffer, this.pos, this.buffer, 0, this.limit);
      }
    }
  }
  
  private CharSequence getSnippet()
  {
    int i = Math.min(this.pos, 20);
    int j = Math.min(this.limit - this.pos, 20);
    byte[] arrayOfByte = new byte[i + j];
    System.arraycopy(this.buffer, this.pos - i, arrayOfByte, 0, i);
    System.arraycopy(this.buffer, this.pos, arrayOfByte, i, j);
    return new String(arrayOfByte);
  }
  
  private JsonToken nextInArray(boolean paramBoolean)
    throws IOException
  {
    if (paramBoolean) {
      replaceTop(JsonScope.NONEMPTY_ARRAY);
    }
    for (;;)
    {
      switch (nextNonWhitespace())
      {
      default: 
        this.pos = (-1 + this.pos);
        return nextValue();
        switch (nextNonWhitespace())
        {
        case 44: 
        default: 
          throw syntaxError("Unterminated array");
        case 93: 
          pop();
          JsonToken localJsonToken3 = JsonToken.END_ARRAY;
          this.token = localJsonToken3;
          return localJsonToken3;
        }
        checkLenient();
      }
    }
    if (paramBoolean)
    {
      pop();
      JsonToken localJsonToken2 = JsonToken.END_ARRAY;
      this.token = localJsonToken2;
      return localJsonToken2;
    }
    checkLenient();
    this.pos = (-1 + this.pos);
    this.value.reset(NULL);
    JsonToken localJsonToken1 = JsonToken.NULL;
    this.token = localJsonToken1;
    return localJsonToken1;
  }
  
  private JsonToken nextInObject(boolean paramBoolean)
    throws IOException
  {
    int i;
    if (paramBoolean)
    {
      switch (nextNonWhitespace())
      {
      default: 
        this.pos = (-1 + this.pos);
        i = nextNonWhitespace();
        switch (i)
        {
        default: 
          checkLenient();
          this.pos = (-1 + this.pos);
          this.name = nextLiteral();
          if (this.name.length() != 0) {
            break label207;
          }
          throw syntaxError("Expected name");
        }
      case 125: 
        pop();
        JsonToken localJsonToken3 = JsonToken.END_OBJECT;
        this.token = localJsonToken3;
        return localJsonToken3;
      }
    }
    else
    {
      switch (nextNonWhitespace())
      {
      case 44: 
      case 59: 
      default: 
        throw syntaxError("Unterminated object");
      }
      pop();
      JsonToken localJsonToken1 = JsonToken.END_OBJECT;
      this.token = localJsonToken1;
      return localJsonToken1;
      checkLenient();
      this.name = nextString((byte)i);
    }
    label207:
    replaceTop(JsonScope.DANGLING_NAME);
    JsonToken localJsonToken2 = JsonToken.NAME;
    this.token = localJsonToken2;
    return localJsonToken2;
  }
  
  private StringRef nextLiteral()
    throws IOException
  {
    int i = 0;
    while (i + this.pos < this.limit) {
      switch (this.buffer[(i + this.pos)])
      {
      default: 
        i++;
        break;
      case 35: 
      case 47: 
      case 59: 
      case 61: 
      case 92: 
        checkLenient();
      }
    }
    for (;;)
    {
      this.reusableStringRef.reset(this.buffer, this.pos, i);
      this.pos = (i + this.pos);
      return this.reusableStringRef;
      this.lineNumber = (1 + this.lineNumber);
      this.eolPos = (i + this.pos);
      continue;
      if (fillBuffer(i + 1)) {
        break;
      }
      this.buffer[this.limit] = 0;
    }
  }
  
  private int nextNonWhitespace()
    throws IOException
  {
    while ((this.pos < this.limit) || (fillBuffer(1)))
    {
      byte[] arrayOfByte = this.buffer;
      int i = this.pos;
      this.pos = (i + 1);
      int j = arrayOfByte[i];
      switch (j)
      {
      case 9: 
      case 13: 
      case 32: 
      default: 
      case 10: 
      case 47: 
        do
        {
          return j;
          this.lineNumber = (1 + this.lineNumber);
          this.eolPos = (-1 + this.pos);
          break;
        } while ((this.pos == this.limit) && (!fillBuffer(1)));
        checkLenient();
        switch (this.buffer[this.pos])
        {
        default: 
          return j;
        case 42: 
          this.pos = (1 + this.pos);
          if (!skipTo("*/")) {
            throw syntaxError("Unterminated comment");
          }
          this.pos = (2 + this.pos);
          break;
        }
        this.pos = (1 + this.pos);
        skipToEndOfLine();
        break;
      }
      checkLenient();
      skipToEndOfLine();
    }
    throw new EOFException("End of input");
  }
  
  private StringRef nextString(byte paramByte)
    throws IOException
  {
    int i = 0;
    int m;
    byte b;
    label257:
    do
    {
      int j = this.pos;
      this.pos = (i + this.pos);
      int k = this.pos;
      m = 1;
      int n = k;
      for (;;)
      {
        if (this.pos >= this.limit) {
          break label257;
        }
        byte[] arrayOfByte = this.buffer;
        int i1 = this.pos;
        this.pos = (i1 + 1);
        b = arrayOfByte[i1];
        if (b == paramByte)
        {
          int i3 = addToBuffer(k, n, -1 + this.pos);
          this.reusableStringRef.reset(this.buffer, j, i3 - j);
          return this.reusableStringRef;
        }
        if (b != 92) {
          break label328;
        }
        k = addToBuffer(k, n, -1 + this.pos);
        if ((5 + this.pos <= this.limit) || ((this.pos < this.limit) && (this.buffer[this.pos] != 117))) {
          try
          {
            int i2 = addEscapeCharacter(k);
            k = i2;
            n = this.pos;
          }
          catch (NumberFormatException localNumberFormatException)
          {
            this.reusableStringRef.reset(this.buffer, j, k - j);
            this.pos = (-2 + this.pos);
            throw localNumberFormatException;
          }
        }
      }
      if (this.pos < this.limit) {
        m = 5 + this.pos - this.limit;
      }
      n = -1 + this.pos;
      this.limit = addToBuffer(k, n, this.limit);
      this.pos = j;
      i = k - this.pos;
    } while (fillBuffer(m + (this.limit - this.pos)));
    if (this.pos == this.limit) {}
    for (String str = "Unterminated string";; str = "Unterminated escape sequence")
    {
      throw syntaxError(str);
      label328:
      if (b != 10) {
        break;
      }
      this.lineNumber = (1 + this.lineNumber);
      this.eolPos = (-1 + this.pos);
      break;
    }
  }
  
  private JsonToken nextValue()
    throws IOException
  {
    int i = nextNonWhitespace();
    switch (i)
    {
    default: 
      this.pos = (-1 + this.pos);
      return readLiteral();
    case 123: 
      push(JsonScope.EMPTY_OBJECT);
      JsonToken localJsonToken3 = JsonToken.BEGIN_OBJECT;
      this.token = localJsonToken3;
      return localJsonToken3;
    case 91: 
      push(JsonScope.EMPTY_ARRAY);
      JsonToken localJsonToken2 = JsonToken.BEGIN_ARRAY;
      this.token = localJsonToken2;
      return localJsonToken2;
    case 39: 
      checkLenient();
    }
    this.value = nextString((byte)i);
    JsonToken localJsonToken1 = JsonToken.STRING;
    this.token = localJsonToken1;
    return localJsonToken1;
  }
  
  private JsonToken objectValue()
    throws IOException
  {
    switch (nextNonWhitespace())
    {
    case 59: 
    case 60: 
    default: 
      throw syntaxError("Expected ':'");
    case 61: 
      checkLenient();
      if (((this.pos < this.limit) || (fillBuffer(1))) && (this.buffer[this.pos] == 62)) {
        this.pos = (1 + this.pos);
      }
      break;
    }
    replaceTop(JsonScope.NONEMPTY_OBJECT);
    return nextValue();
  }
  
  private JsonScope peekStack()
  {
    return (JsonScope)this.stack.get(-1 + this.stack.size());
  }
  
  private JsonScope pop()
  {
    return (JsonScope)this.stack.remove(-1 + this.stack.size());
  }
  
  private void push(JsonScope paramJsonScope)
  {
    this.stack.add(paramJsonScope);
  }
  
  private JsonToken readLiteral()
    throws IOException
  {
    this.value = nextLiteral();
    if (this.value.length() == 0) {
      throw syntaxError("Expected literal value");
    }
    this.token = decodeLiteral();
    if (this.token == JsonToken.STRING) {
      checkLenient();
    }
    return this.token;
  }
  
  private int readUnicodeHexValue()
  {
    int i = 0;
    int j = 0;
    if (j != 4)
    {
      int k = this.buffer[(j + this.pos)];
      int m;
      if ((48 <= k) && (k <= 57)) {
        m = k - 48;
      }
      for (;;)
      {
        i = m + (i << 4);
        j++;
        break;
        if ((97 <= k) && (k <= 102))
        {
          m = 10 + (k - 97);
        }
        else
        {
          if ((65 > k) || (k > 70)) {
            break label100;
          }
          m = 10 + (k - 65);
        }
      }
      label100:
      throw new NumberFormatException("Bad unicode escape sequence: \\u" + new String(this.buffer, this.pos, 4));
    }
    this.pos = (4 + this.pos);
    return i;
  }
  
  private void replaceTop(JsonScope paramJsonScope)
  {
    this.stack.set(-1 + this.stack.size(), paramJsonScope);
  }
  
  private boolean skipTo(String paramString)
    throws IOException
  {
    if ((this.pos + paramString.length() <= this.limit) || (fillBuffer(paramString.length())))
    {
      for (int i = 0;; i++)
      {
        if (i >= paramString.length()) {
          break label75;
        }
        if (this.buffer[(i + this.pos)] != paramString.charAt(i))
        {
          this.pos = (1 + this.pos);
          break;
        }
      }
      label75:
      return true;
    }
    return false;
  }
  
  private void skipToEndOfLine()
    throws IOException
  {
    int j;
    do
    {
      if ((this.pos < this.limit) || (fillBuffer(1)))
      {
        byte[] arrayOfByte = this.buffer;
        int i = this.pos;
        this.pos = (i + 1);
        j = arrayOfByte[i];
        if (j != 13) {}
      }
      else
      {
        return;
      }
    } while (j != 10);
    this.lineNumber = (1 + this.lineNumber);
    this.eolPos = (-1 + this.pos);
  }
  
  private IOException syntaxError(String paramString)
    throws IOException
  {
    throw new MalformedJsonException(paramString + " at line " + this.lineNumber + " column " + (this.pos - this.eolPos));
  }
  
  public void beginArray()
    throws IOException
  {
    expect(JsonToken.BEGIN_ARRAY);
  }
  
  public void beginObject()
    throws IOException
  {
    expect(JsonToken.BEGIN_OBJECT);
  }
  
  public void close()
    throws IOException
  {
    this.value = null;
    this.token = null;
    this.stack.clear();
    this.stack.add(JsonScope.CLOSED);
    this.in.close();
  }
  
  public void endArray()
    throws IOException
  {
    expect(JsonToken.END_ARRAY);
  }
  
  public void endObject()
    throws IOException
  {
    expect(JsonToken.END_OBJECT);
  }
  
  public boolean hasNext()
    throws IOException
  {
    peek();
    return (this.token != JsonToken.END_OBJECT) && (this.token != JsonToken.END_ARRAY);
  }
  
  public boolean nextBoolean()
    throws IOException
  {
    peek();
    if (this.token != JsonToken.BOOLEAN) {
      throw new IllegalStateException("Expected a boolean but was " + this.token);
    }
    if (this.value.data() == TRUE) {}
    for (boolean bool = true;; bool = false)
    {
      advance();
      return bool;
    }
  }
  
  public int nextInt()
    throws IOException
  {
    peek();
    if ((this.token != JsonToken.STRING) && (this.token != JsonToken.NUMBER)) {
      throw new IllegalStateException("Expected an int but was " + this.token);
    }
    str = this.stringPool.getString(this.value.data(), this.value.offset(), this.value.length());
    try
    {
      int j = Integer.parseInt(str);
      i = j;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      int i;
      double d;
      do
      {
        d = Double.parseDouble(str);
        i = (int)d;
      } while (i == d);
      throw new NumberFormatException(str);
    }
    advance();
    return i;
  }
  
  public String nextName()
    throws IOException
  {
    StringRef localStringRef = nextNameRef();
    return this.stringPool.getString(localStringRef.data(), localStringRef.offset(), localStringRef.length());
  }
  
  public StringRef nextNameRef()
    throws IOException
  {
    peek();
    if (this.token != JsonToken.NAME) {
      throw new IllegalStateException("Expected a name but was " + peek());
    }
    if (this.name == this.reusableStringRef) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      advance();
      return this.reusableStringRef;
    }
  }
  
  public String nextString()
    throws IOException
  {
    StringRef localStringRef = nextStringRef();
    return this.stringPool.getString(localStringRef.data(), localStringRef.offset(), localStringRef.length());
  }
  
  public byte[] nextStringBytes()
    throws IOException
  {
    StringRef localStringRef = nextStringRef();
    return this.stringPool.getBytes(localStringRef.data(), localStringRef.offset(), localStringRef.length());
  }
  
  public StringRef nextStringRef()
    throws IOException
  {
    peek();
    if ((this.token != JsonToken.STRING) && (this.token != JsonToken.NUMBER)) {
      throw new IllegalStateException("Expected a string but was " + peek());
    }
    if (this.value == this.reusableStringRef) {}
    for (boolean bool = true;; bool = false)
    {
      Preconditions.checkState(bool);
      advance();
      return this.reusableStringRef;
    }
  }
  
  public JsonToken peek()
    throws IOException
  {
    JsonToken localJsonToken3;
    if (this.token != null) {
      localJsonToken3 = this.token;
    }
    do
    {
      return localJsonToken3;
      switch (1.$SwitchMap$com$google$android$search$core$util$JsonScope[peekStack().ordinal()])
      {
      default: 
        throw new AssertionError();
      case 1: 
        replaceTop(JsonScope.NONEMPTY_DOCUMENT);
        localJsonToken3 = nextValue();
      }
    } while ((this.lenient) || (this.token == JsonToken.BEGIN_ARRAY) || (this.token == JsonToken.BEGIN_OBJECT));
    throw new IOException("Expected JSON document to start with '[' or '{' but was " + this.token);
    return nextInArray(true);
    return nextInArray(false);
    return nextInObject(true);
    return objectValue();
    return nextInObject(false);
    try
    {
      JsonToken localJsonToken2 = nextValue();
      if (this.lenient) {
        return localJsonToken2;
      }
      throw syntaxError("Expected EOF");
    }
    catch (EOFException localEOFException)
    {
      JsonToken localJsonToken1 = JsonToken.END_DOCUMENT;
      this.token = localJsonToken1;
      return localJsonToken1;
    }
    throw new IllegalStateException("JsonReader is closed");
  }
  
  public void setLenient(boolean paramBoolean)
  {
    this.lenient = paramBoolean;
  }
  
  public void skipValue()
    throws IOException
  {
    int i = 0;
    for (;;)
    {
      JsonToken localJsonToken = advance();
      if ((localJsonToken == JsonToken.BEGIN_ARRAY) || (localJsonToken == JsonToken.BEGIN_OBJECT)) {
        i++;
      }
      while (i == 0)
      {
        return;
        if ((localJsonToken == JsonToken.END_ARRAY) || (localJsonToken == JsonToken.END_OBJECT)) {
          i--;
        }
      }
    }
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + " near " + getSnippet();
  }
  
  public static final class StringRef
  {
    private byte[] mData;
    private int mLength;
    private int mOffset;
    
    StringRef()
    {
      reset(null, 0, 0);
    }
    
    public boolean contentEquals(byte paramByte)
    {
      return (this.mLength == 1) && (this.mData[this.mOffset] == paramByte);
    }
    
    public boolean contentEquals(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null) {}
      for (int i = 0;; i = paramArrayOfByte.length) {
        return contentEquals(paramArrayOfByte, 0, i);
      }
    }
    
    public boolean contentEquals(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if (paramInt2 != this.mLength) {
        return false;
      }
      if ((paramArrayOfByte == this.mData) && (paramInt1 == this.mOffset)) {
        return true;
      }
      for (int i = 0;; i++)
      {
        if (i == paramInt2) {
          break label64;
        }
        if (paramArrayOfByte[(paramInt1 + i)] != this.mData[(i + this.mOffset)]) {
          break;
        }
      }
      label64:
      return true;
    }
    
    public byte[] data()
    {
      return this.mData;
    }
    
    public int length()
    {
      return this.mLength;
    }
    
    public int offset()
    {
      return this.mOffset;
    }
    
    void reset(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null) {}
      for (int i = 0;; i = paramArrayOfByte.length)
      {
        reset(paramArrayOfByte, 0, i);
        return;
      }
    }
    
    void reset(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      if (paramArrayOfByte == null)
      {
        if ((paramInt1 != 0) || (paramInt2 != 0)) {
          throw new NullPointerException();
        }
      }
      else if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 > paramArrayOfByte.length - paramInt1)) {
        throw new IndexOutOfBoundsException();
      }
      this.mData = paramArrayOfByte;
      this.mOffset = paramInt1;
      this.mLength = paramInt2;
    }
    
    public String toString()
    {
      if (this.mData == null) {
        return "";
      }
      return new String(this.mData, this.mOffset, this.mLength);
    }
    
    public String toString(Charset paramCharset)
    {
      if (this.mData == null) {
        return "";
      }
      return new String(this.mData, this.mOffset, this.mLength, paramCharset);
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.search.core.util.JsonUtf8Reader
 * JD-Core Version:    0.7.0.1
 */