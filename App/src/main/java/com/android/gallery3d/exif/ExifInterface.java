package com.android.gallery3d.exif;

import android.util.SparseIntArray;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TimeZone;

public class ExifInterface
{
  public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
  public static final int TAG_APERTURE_VALUE;
  public static final int TAG_ARTIST;
  public static final int TAG_BITS_PER_SAMPLE;
  public static final int TAG_BRIGHTNESS_VALUE;
  public static final int TAG_CFA_PATTERN;
  public static final int TAG_COLOR_SPACE;
  public static final int TAG_COMPONENTS_CONFIGURATION;
  public static final int TAG_COMPRESSED_BITS_PER_PIXEL;
  public static final int TAG_COMPRESSION;
  public static final int TAG_CONTRAST;
  public static final int TAG_COPYRIGHT;
  public static final int TAG_CUSTOM_RENDERED;
  public static final int TAG_DATE_TIME;
  public static final int TAG_DATE_TIME_DIGITIZED;
  public static final int TAG_DATE_TIME_ORIGINAL;
  public static final int TAG_DEVICE_SETTING_DESCRIPTION;
  public static final int TAG_DIGITAL_ZOOM_RATIO;
  public static final int TAG_EXIF_IFD;
  public static final int TAG_EXIF_VERSION;
  public static final int TAG_EXPOSURE_BIAS_VALUE;
  public static final int TAG_EXPOSURE_INDEX;
  public static final int TAG_EXPOSURE_MODE;
  public static final int TAG_EXPOSURE_PROGRAM;
  public static final int TAG_EXPOSURE_TIME;
  public static final int TAG_FILE_SOURCE;
  public static final int TAG_FLASH;
  public static final int TAG_FLASHPIX_VERSION;
  public static final int TAG_FLASH_ENERGY;
  public static final int TAG_FOCAL_LENGTH;
  public static final int TAG_FOCAL_LENGTH_IN_35_MM_FILE;
  public static final int TAG_FOCAL_PLANE_RESOLUTION_UNIT;
  public static final int TAG_FOCAL_PLANE_X_RESOLUTION;
  public static final int TAG_FOCAL_PLANE_Y_RESOLUTION;
  public static final int TAG_F_NUMBER;
  public static final int TAG_GAIN_CONTROL;
  public static final int TAG_GPS_ALTITUDE;
  public static final int TAG_GPS_ALTITUDE_REF;
  public static final int TAG_GPS_AREA_INFORMATION;
  public static final int TAG_GPS_DATE_STAMP;
  public static final int TAG_GPS_DEST_BEARING;
  public static final int TAG_GPS_DEST_BEARING_REF;
  public static final int TAG_GPS_DEST_DISTANCE;
  public static final int TAG_GPS_DEST_DISTANCE_REF;
  public static final int TAG_GPS_DEST_LATITUDE;
  public static final int TAG_GPS_DEST_LATITUDE_REF;
  public static final int TAG_GPS_DEST_LONGITUDE;
  public static final int TAG_GPS_DEST_LONGITUDE_REF;
  public static final int TAG_GPS_DIFFERENTIAL;
  public static final int TAG_GPS_DOP;
  public static final int TAG_GPS_IFD;
  public static final int TAG_GPS_IMG_DIRECTION;
  public static final int TAG_GPS_IMG_DIRECTION_REF;
  public static final int TAG_GPS_LATITUDE;
  public static final int TAG_GPS_LATITUDE_REF;
  public static final int TAG_GPS_LONGITUDE;
  public static final int TAG_GPS_LONGITUDE_REF;
  public static final int TAG_GPS_MAP_DATUM;
  public static final int TAG_GPS_MEASURE_MODE;
  public static final int TAG_GPS_PROCESSING_METHOD;
  public static final int TAG_GPS_SATTELLITES;
  public static final int TAG_GPS_SPEED;
  public static final int TAG_GPS_SPEED_REF;
  public static final int TAG_GPS_STATUS;
  public static final int TAG_GPS_TIME_STAMP;
  public static final int TAG_GPS_TRACK;
  public static final int TAG_GPS_TRACK_REF;
  public static final int TAG_GPS_VERSION_ID;
  public static final int TAG_IMAGE_DESCRIPTION;
  public static final int TAG_IMAGE_LENGTH;
  public static final int TAG_IMAGE_UNIQUE_ID;
  public static final int TAG_IMAGE_WIDTH = defineTag(0, (short)256);
  public static final int TAG_INTEROPERABILITY_IFD;
  public static final int TAG_INTEROPERABILITY_INDEX;
  public static final int TAG_ISO_SPEED_RATINGS;
  public static final int TAG_JPEG_INTERCHANGE_FORMAT;
  public static final int TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
  public static final int TAG_LIGHT_SOURCE;
  public static final int TAG_MAKE;
  public static final int TAG_MAKER_NOTE;
  public static final int TAG_MAX_APERTURE_VALUE;
  public static final int TAG_METERING_MODE;
  public static final int TAG_MODEL;
  public static final int TAG_OECF;
  public static final int TAG_ORIENTATION;
  public static final int TAG_PHOTOMETRIC_INTERPRETATION;
  public static final int TAG_PIXEL_X_DIMENSION;
  public static final int TAG_PIXEL_Y_DIMENSION;
  public static final int TAG_PLANAR_CONFIGURATION;
  public static final int TAG_PRIMARY_CHROMATICITIES;
  public static final int TAG_REFERENCE_BLACK_WHITE;
  public static final int TAG_RELATED_SOUND_FILE;
  public static final int TAG_RESOLUTION_UNIT;
  public static final int TAG_ROWS_PER_STRIP;
  public static final int TAG_SAMPLES_PER_PIXEL;
  public static final int TAG_SATURATION;
  public static final int TAG_SCENE_CAPTURE_TYPE;
  public static final int TAG_SCENE_TYPE;
  public static final int TAG_SENSING_METHOD;
  public static final int TAG_SHARPNESS;
  public static final int TAG_SHUTTER_SPEED_VALUE;
  public static final int TAG_SOFTWARE;
  public static final int TAG_SPATIAL_FREQUENCY_RESPONSE;
  public static final int TAG_SPECTRAL_SENSITIVITY;
  public static final int TAG_STRIP_BYTE_COUNTS;
  public static final int TAG_STRIP_OFFSETS;
  public static final int TAG_SUBJECT_AREA;
  public static final int TAG_SUBJECT_DISTANCE;
  public static final int TAG_SUBJECT_DISTANCE_RANGE;
  public static final int TAG_SUBJECT_LOCATION;
  public static final int TAG_SUB_SEC_TIME;
  public static final int TAG_SUB_SEC_TIME_DIGITIZED;
  public static final int TAG_SUB_SEC_TIME_ORIGINAL;
  public static final int TAG_TRANSFER_FUNCTION;
  public static final int TAG_USER_COMMENT;
  public static final int TAG_WHITE_BALANCE;
  public static final int TAG_WHITE_POINT;
  public static final int TAG_X_RESOLUTION;
  public static final int TAG_Y_CB_CR_COEFFICIENTS;
  public static final int TAG_Y_CB_CR_POSITIONING;
  public static final int TAG_Y_CB_CR_SUB_SAMPLING;
  public static final int TAG_Y_RESOLUTION;
  protected static HashSet<Short> sBannedDefines;
  private static HashSet<Short> sOffsetTags;
  private ExifData mData = new ExifData(DEFAULT_BYTE_ORDER);
  private final DateFormat mDateTimeStampFormat = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
  private final DateFormat mGPSDateStampFormat = new SimpleDateFormat("yyyy:MM:dd");
  private final Calendar mGPSTimeStampCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  private SparseIntArray mTagInfo = null;
  
  static
  {
    TAG_IMAGE_LENGTH = defineTag(0, (short)257);
    TAG_BITS_PER_SAMPLE = defineTag(0, (short)258);
    TAG_COMPRESSION = defineTag(0, (short)259);
    TAG_PHOTOMETRIC_INTERPRETATION = defineTag(0, (short)262);
    TAG_IMAGE_DESCRIPTION = defineTag(0, (short)270);
    TAG_MAKE = defineTag(0, (short)271);
    TAG_MODEL = defineTag(0, (short)272);
    TAG_STRIP_OFFSETS = defineTag(0, (short)273);
    TAG_ORIENTATION = defineTag(0, (short)274);
    TAG_SAMPLES_PER_PIXEL = defineTag(0, (short)277);
    TAG_ROWS_PER_STRIP = defineTag(0, (short)278);
    TAG_STRIP_BYTE_COUNTS = defineTag(0, (short)279);
    TAG_X_RESOLUTION = defineTag(0, (short)282);
    TAG_Y_RESOLUTION = defineTag(0, (short)283);
    TAG_PLANAR_CONFIGURATION = defineTag(0, (short)284);
    TAG_RESOLUTION_UNIT = defineTag(0, (short)296);
    TAG_TRANSFER_FUNCTION = defineTag(0, (short)301);
    TAG_SOFTWARE = defineTag(0, (short)305);
    TAG_DATE_TIME = defineTag(0, (short)306);
    TAG_ARTIST = defineTag(0, (short)315);
    TAG_WHITE_POINT = defineTag(0, (short)318);
    TAG_PRIMARY_CHROMATICITIES = defineTag(0, (short)319);
    TAG_Y_CB_CR_COEFFICIENTS = defineTag(0, (short)529);
    TAG_Y_CB_CR_SUB_SAMPLING = defineTag(0, (short)530);
    TAG_Y_CB_CR_POSITIONING = defineTag(0, (short)531);
    TAG_REFERENCE_BLACK_WHITE = defineTag(0, (short)532);
    TAG_COPYRIGHT = defineTag(0, (short)-32104);
    TAG_EXIF_IFD = defineTag(0, (short)-30871);
    TAG_GPS_IFD = defineTag(0, (short)-30683);
    TAG_JPEG_INTERCHANGE_FORMAT = defineTag(1, (short)513);
    TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = defineTag(1, (short)514);
    TAG_EXPOSURE_TIME = defineTag(2, (short)-32102);
    TAG_F_NUMBER = defineTag(2, (short)-32099);
    TAG_EXPOSURE_PROGRAM = defineTag(2, (short)-30686);
    TAG_SPECTRAL_SENSITIVITY = defineTag(2, (short)-30684);
    TAG_ISO_SPEED_RATINGS = defineTag(2, (short)-30681);
    TAG_OECF = defineTag(2, (short)-30680);
    TAG_EXIF_VERSION = defineTag(2, (short)-28672);
    TAG_DATE_TIME_ORIGINAL = defineTag(2, (short)-28669);
    TAG_DATE_TIME_DIGITIZED = defineTag(2, (short)-28668);
    TAG_COMPONENTS_CONFIGURATION = defineTag(2, (short)-28415);
    TAG_COMPRESSED_BITS_PER_PIXEL = defineTag(2, (short)-28414);
    TAG_SHUTTER_SPEED_VALUE = defineTag(2, (short)-28159);
    TAG_APERTURE_VALUE = defineTag(2, (short)-28158);
    TAG_BRIGHTNESS_VALUE = defineTag(2, (short)-28157);
    TAG_EXPOSURE_BIAS_VALUE = defineTag(2, (short)-28156);
    TAG_MAX_APERTURE_VALUE = defineTag(2, (short)-28155);
    TAG_SUBJECT_DISTANCE = defineTag(2, (short)-28154);
    TAG_METERING_MODE = defineTag(2, (short)-28153);
    TAG_LIGHT_SOURCE = defineTag(2, (short)-28152);
    TAG_FLASH = defineTag(2, (short)-28151);
    TAG_FOCAL_LENGTH = defineTag(2, (short)-28150);
    TAG_SUBJECT_AREA = defineTag(2, (short)-28140);
    TAG_MAKER_NOTE = defineTag(2, (short)-28036);
    TAG_USER_COMMENT = defineTag(2, (short)-28026);
    TAG_SUB_SEC_TIME = defineTag(2, (short)-28016);
    TAG_SUB_SEC_TIME_ORIGINAL = defineTag(2, (short)-28015);
    TAG_SUB_SEC_TIME_DIGITIZED = defineTag(2, (short)-28014);
    TAG_FLASHPIX_VERSION = defineTag(2, (short)-24576);
    TAG_COLOR_SPACE = defineTag(2, (short)-24575);
    TAG_PIXEL_X_DIMENSION = defineTag(2, (short)-24574);
    TAG_PIXEL_Y_DIMENSION = defineTag(2, (short)-24573);
    TAG_RELATED_SOUND_FILE = defineTag(2, (short)-24572);
    TAG_INTEROPERABILITY_IFD = defineTag(2, (short)-24571);
    TAG_FLASH_ENERGY = defineTag(2, (short)-24053);
    TAG_SPATIAL_FREQUENCY_RESPONSE = defineTag(2, (short)-24052);
    TAG_FOCAL_PLANE_X_RESOLUTION = defineTag(2, (short)-24050);
    TAG_FOCAL_PLANE_Y_RESOLUTION = defineTag(2, (short)-24049);
    TAG_FOCAL_PLANE_RESOLUTION_UNIT = defineTag(2, (short)-24048);
    TAG_SUBJECT_LOCATION = defineTag(2, (short)-24044);
    TAG_EXPOSURE_INDEX = defineTag(2, (short)-24043);
    TAG_SENSING_METHOD = defineTag(2, (short)-24041);
    TAG_FILE_SOURCE = defineTag(2, (short)-23808);
    TAG_SCENE_TYPE = defineTag(2, (short)-23807);
    TAG_CFA_PATTERN = defineTag(2, (short)-23806);
    TAG_CUSTOM_RENDERED = defineTag(2, (short)-23551);
    TAG_EXPOSURE_MODE = defineTag(2, (short)-23550);
    TAG_WHITE_BALANCE = defineTag(2, (short)-23549);
    TAG_DIGITAL_ZOOM_RATIO = defineTag(2, (short)-23548);
    TAG_FOCAL_LENGTH_IN_35_MM_FILE = defineTag(2, (short)-23547);
    TAG_SCENE_CAPTURE_TYPE = defineTag(2, (short)-23546);
    TAG_GAIN_CONTROL = defineTag(2, (short)-23545);
    TAG_CONTRAST = defineTag(2, (short)-23544);
    TAG_SATURATION = defineTag(2, (short)-23543);
    TAG_SHARPNESS = defineTag(2, (short)-23542);
    TAG_DEVICE_SETTING_DESCRIPTION = defineTag(2, (short)-23541);
    TAG_SUBJECT_DISTANCE_RANGE = defineTag(2, (short)-23540);
    TAG_IMAGE_UNIQUE_ID = defineTag(2, (short)-23520);
    TAG_GPS_VERSION_ID = defineTag(4, (short)0);
    TAG_GPS_LATITUDE_REF = defineTag(4, (short)1);
    TAG_GPS_LATITUDE = defineTag(4, (short)2);
    TAG_GPS_LONGITUDE_REF = defineTag(4, (short)3);
    TAG_GPS_LONGITUDE = defineTag(4, (short)4);
    TAG_GPS_ALTITUDE_REF = defineTag(4, (short)5);
    TAG_GPS_ALTITUDE = defineTag(4, (short)6);
    TAG_GPS_TIME_STAMP = defineTag(4, (short)7);
    TAG_GPS_SATTELLITES = defineTag(4, (short)8);
    TAG_GPS_STATUS = defineTag(4, (short)9);
    TAG_GPS_MEASURE_MODE = defineTag(4, (short)10);
    TAG_GPS_DOP = defineTag(4, (short)11);
    TAG_GPS_SPEED_REF = defineTag(4, (short)12);
    TAG_GPS_SPEED = defineTag(4, (short)13);
    TAG_GPS_TRACK_REF = defineTag(4, (short)14);
    TAG_GPS_TRACK = defineTag(4, (short)15);
    TAG_GPS_IMG_DIRECTION_REF = defineTag(4, (short)16);
    TAG_GPS_IMG_DIRECTION = defineTag(4, (short)17);
    TAG_GPS_MAP_DATUM = defineTag(4, (short)18);
    TAG_GPS_DEST_LATITUDE_REF = defineTag(4, (short)19);
    TAG_GPS_DEST_LATITUDE = defineTag(4, (short)20);
    TAG_GPS_DEST_LONGITUDE_REF = defineTag(4, (short)21);
    TAG_GPS_DEST_LONGITUDE = defineTag(4, (short)22);
    TAG_GPS_DEST_BEARING_REF = defineTag(4, (short)23);
    TAG_GPS_DEST_BEARING = defineTag(4, (short)24);
    TAG_GPS_DEST_DISTANCE_REF = defineTag(4, (short)25);
    TAG_GPS_DEST_DISTANCE = defineTag(4, (short)26);
    TAG_GPS_PROCESSING_METHOD = defineTag(4, (short)27);
    TAG_GPS_AREA_INFORMATION = defineTag(4, (short)28);
    TAG_GPS_DATE_STAMP = defineTag(4, (short)29);
    TAG_GPS_DIFFERENTIAL = defineTag(4, (short)30);
    TAG_INTEROPERABILITY_INDEX = defineTag(3, (short)1);
    sOffsetTags = new HashSet();
    sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_GPS_IFD)));
    sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_EXIF_IFD)));
    sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT)));
    sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_INTEROPERABILITY_IFD)));
    sOffsetTags.add(Short.valueOf(getTrueTagKey(TAG_STRIP_OFFSETS)));
    sBannedDefines = new HashSet(sOffsetTags);
    sBannedDefines.add(Short.valueOf(getTrueTagKey(-1)));
    sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)));
    sBannedDefines.add(Short.valueOf(getTrueTagKey(TAG_STRIP_BYTE_COUNTS)));
  }
  
  public ExifInterface()
  {
    this.mGPSDateStampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }
  
  protected static void closeSilently(Closeable paramCloseable)
  {
    if (paramCloseable != null) {}
    try
    {
      paramCloseable.close();
      return;
    }
    catch (Throwable localThrowable) {}
  }
  
  public static int defineTag(int paramInt, short paramShort)
  {
    return 0xFFFF & paramShort | paramInt << 16;
  }
  
  protected static int getAllowedIfdFlagsFromInfo(int paramInt)
  {
    return paramInt >>> 24;
  }
  
  protected static int getFlagsFromAllowedIfds(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt == null) || (paramArrayOfInt.length == 0))
    {
      i = 0;
      return i;
    }
    int i = 0;
    int[] arrayOfInt = IfdData.getIfds();
    int j = 0;
    label21:
    int k;
    if (j < 5) {
      k = paramArrayOfInt.length;
    }
    for (int m = 0;; m++) {
      if (m < k)
      {
        int n = paramArrayOfInt[m];
        if (arrayOfInt[j] == n) {
          i |= 1 << j;
        }
      }
      else
      {
        j++;
        break label21;
        break;
      }
    }
  }
  
  public static int getRotationForOrientationValue(short paramShort)
  {
    switch (paramShort)
    {
    case 1: 
    case 2: 
    case 4: 
    case 5: 
    case 7: 
    default: 
      return 0;
    case 6: 
      return 90;
    case 3: 
      return 180;
    }
    return 270;
  }
  
  public static int getTrueIfd(int paramInt)
  {
    return paramInt >>> 16;
  }
  
  public static short getTrueTagKey(int paramInt)
  {
    return (short)paramInt;
  }
  
  private void initTagInfo()
  {
    int i = getFlagsFromAllowedIfds(new int[] { 0, 1 }) << 24;
    this.mTagInfo.put(TAG_MAKE, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_IMAGE_WIDTH, 0x1 | 0x40000 | i);
    this.mTagInfo.put(TAG_IMAGE_LENGTH, 0x1 | 0x40000 | i);
    this.mTagInfo.put(TAG_BITS_PER_SAMPLE, 0x3 | 0x30000 | i);
    this.mTagInfo.put(TAG_COMPRESSION, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_PHOTOMETRIC_INTERPRETATION, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_ORIENTATION, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_SAMPLES_PER_PIXEL, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_PLANAR_CONFIGURATION, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_Y_CB_CR_SUB_SAMPLING, 0x2 | 0x30000 | i);
    this.mTagInfo.put(TAG_Y_CB_CR_POSITIONING, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_X_RESOLUTION, 0x1 | 0x50000 | i);
    this.mTagInfo.put(TAG_Y_RESOLUTION, 0x1 | 0x50000 | i);
    this.mTagInfo.put(TAG_RESOLUTION_UNIT, 0x1 | 0x30000 | i);
    this.mTagInfo.put(TAG_STRIP_OFFSETS, 0x0 | 0x40000 | i);
    this.mTagInfo.put(TAG_ROWS_PER_STRIP, 0x1 | 0x40000 | i);
    this.mTagInfo.put(TAG_STRIP_BYTE_COUNTS, 0x0 | 0x40000 | i);
    this.mTagInfo.put(TAG_TRANSFER_FUNCTION, 0x300 | 0x30000 | i);
    this.mTagInfo.put(TAG_WHITE_POINT, 0x2 | 0x50000 | i);
    this.mTagInfo.put(TAG_PRIMARY_CHROMATICITIES, 0x6 | 0x50000 | i);
    this.mTagInfo.put(TAG_Y_CB_CR_COEFFICIENTS, 0x3 | 0x50000 | i);
    this.mTagInfo.put(TAG_REFERENCE_BLACK_WHITE, 0x6 | 0x50000 | i);
    this.mTagInfo.put(TAG_DATE_TIME, 0x14 | 0x20000 | i);
    this.mTagInfo.put(TAG_IMAGE_DESCRIPTION, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_MAKE, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_MODEL, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_SOFTWARE, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_ARTIST, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_COPYRIGHT, 0x0 | 0x20000 | i);
    this.mTagInfo.put(TAG_EXIF_IFD, 0x1 | 0x40000 | i);
    this.mTagInfo.put(TAG_GPS_IFD, 0x1 | 0x40000 | i);
    int j = getFlagsFromAllowedIfds(new int[] { 1 }) << 24;
    this.mTagInfo.put(TAG_JPEG_INTERCHANGE_FORMAT, 0x1 | 0x40000 | j);
    this.mTagInfo.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 0x1 | 0x40000 | j);
    int k = getFlagsFromAllowedIfds(new int[] { 2 }) << 24;
    this.mTagInfo.put(TAG_EXIF_VERSION, 0x4 | 0x70000 | k);
    this.mTagInfo.put(TAG_FLASHPIX_VERSION, 0x4 | 0x70000 | k);
    this.mTagInfo.put(TAG_COLOR_SPACE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_COMPONENTS_CONFIGURATION, 0x4 | 0x70000 | k);
    this.mTagInfo.put(TAG_COMPRESSED_BITS_PER_PIXEL, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_PIXEL_X_DIMENSION, 0x1 | 0x40000 | k);
    this.mTagInfo.put(TAG_PIXEL_Y_DIMENSION, 0x1 | 0x40000 | k);
    this.mTagInfo.put(TAG_MAKER_NOTE, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_USER_COMMENT, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_RELATED_SOUND_FILE, 0xD | 0x20000 | k);
    this.mTagInfo.put(TAG_DATE_TIME_ORIGINAL, 0x14 | 0x20000 | k);
    this.mTagInfo.put(TAG_DATE_TIME_DIGITIZED, 0x14 | 0x20000 | k);
    this.mTagInfo.put(TAG_SUB_SEC_TIME, 0x0 | 0x20000 | k);
    this.mTagInfo.put(TAG_SUB_SEC_TIME_ORIGINAL, 0x0 | 0x20000 | k);
    this.mTagInfo.put(TAG_SUB_SEC_TIME_DIGITIZED, 0x0 | 0x20000 | k);
    this.mTagInfo.put(TAG_IMAGE_UNIQUE_ID, 0x21 | 0x20000 | k);
    this.mTagInfo.put(TAG_EXPOSURE_TIME, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_F_NUMBER, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_EXPOSURE_PROGRAM, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_SPECTRAL_SENSITIVITY, 0x0 | 0x20000 | k);
    this.mTagInfo.put(TAG_ISO_SPEED_RATINGS, 0x0 | 0x30000 | k);
    this.mTagInfo.put(TAG_OECF, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_SHUTTER_SPEED_VALUE, 0x1 | 0xA0000 | k);
    this.mTagInfo.put(TAG_APERTURE_VALUE, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_BRIGHTNESS_VALUE, 0x1 | 0xA0000 | k);
    this.mTagInfo.put(TAG_EXPOSURE_BIAS_VALUE, 0x1 | 0xA0000 | k);
    this.mTagInfo.put(TAG_MAX_APERTURE_VALUE, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_SUBJECT_DISTANCE, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_METERING_MODE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_LIGHT_SOURCE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_FLASH, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_FOCAL_LENGTH, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_SUBJECT_AREA, 0x0 | 0x30000 | k);
    this.mTagInfo.put(TAG_FLASH_ENERGY, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_SPATIAL_FREQUENCY_RESPONSE, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_FOCAL_PLANE_X_RESOLUTION, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_FOCAL_PLANE_Y_RESOLUTION, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_SUBJECT_LOCATION, 0x2 | 0x30000 | k);
    this.mTagInfo.put(TAG_EXPOSURE_INDEX, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_SENSING_METHOD, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_FILE_SOURCE, 0x1 | 0x70000 | k);
    this.mTagInfo.put(TAG_SCENE_TYPE, 0x1 | 0x70000 | k);
    this.mTagInfo.put(TAG_CFA_PATTERN, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_CUSTOM_RENDERED, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_EXPOSURE_MODE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_WHITE_BALANCE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_DIGITAL_ZOOM_RATIO, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_FOCAL_LENGTH_IN_35_MM_FILE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_SCENE_CAPTURE_TYPE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_GAIN_CONTROL, 0x1 | 0x50000 | k);
    this.mTagInfo.put(TAG_CONTRAST, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_SATURATION, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_SHARPNESS, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_DEVICE_SETTING_DESCRIPTION, 0x0 | 0x70000 | k);
    this.mTagInfo.put(TAG_SUBJECT_DISTANCE_RANGE, 0x1 | 0x30000 | k);
    this.mTagInfo.put(TAG_INTEROPERABILITY_IFD, 0x1 | 0x40000 | k);
    int m = getFlagsFromAllowedIfds(new int[] { 4 }) << 24;
    this.mTagInfo.put(TAG_GPS_VERSION_ID, 0x4 | 0x10000 | m);
    this.mTagInfo.put(TAG_GPS_LATITUDE_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_LONGITUDE_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_LATITUDE, 0x3 | 0xA0000 | m);
    this.mTagInfo.put(TAG_GPS_LONGITUDE, 0x3 | 0xA0000 | m);
    this.mTagInfo.put(TAG_GPS_ALTITUDE_REF, 0x1 | 0x10000 | m);
    this.mTagInfo.put(TAG_GPS_ALTITUDE, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_TIME_STAMP, 0x3 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_SATTELLITES, 0x0 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_STATUS, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_MEASURE_MODE, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DOP, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_SPEED_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_SPEED, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_TRACK_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_TRACK, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_IMG_DIRECTION_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_IMG_DIRECTION, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_MAP_DATUM, 0x0 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_LATITUDE_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_LATITUDE, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_BEARING_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_BEARING, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_DISTANCE_REF, 0x2 | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DEST_DISTANCE, 0x1 | 0x50000 | m);
    this.mTagInfo.put(TAG_GPS_PROCESSING_METHOD, 0x0 | 0x70000 | m);
    this.mTagInfo.put(TAG_GPS_AREA_INFORMATION, 0x0 | 0x70000 | m);
    this.mTagInfo.put(TAG_GPS_DATE_STAMP, 0xB | 0x20000 | m);
    this.mTagInfo.put(TAG_GPS_DIFFERENTIAL, 0xB | 0x30000 | m);
    int n = getFlagsFromAllowedIfds(new int[] { 3 }) << 24;
    this.mTagInfo.put(TAG_INTEROPERABILITY_INDEX, 0x0 | 0x20000 | n);
  }
  
  protected static boolean isIfdAllowed(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = IfdData.getIfds();
    int i = getAllowedIfdFlagsFromInfo(paramInt1);
    for (int j = 0; j < arrayOfInt.length; j++) {
      if ((paramInt2 == arrayOfInt[j]) && ((0x1 & i >> j) == 1)) {
        return true;
      }
    }
    return false;
  }
  
  protected static boolean isOffsetTag(short paramShort)
  {
    return sOffsetTags.contains(Short.valueOf(paramShort));
  }
  
  public int getDefinedTagDefaultIfd(int paramInt)
  {
    if (getTagInfo().get(paramInt) == 0) {
      return -1;
    }
    return getTrueIfd(paramInt);
  }
  
  public ExifTag getTag(int paramInt1, int paramInt2)
  {
    if (!ExifTag.isValidIfd(paramInt2)) {
      return null;
    }
    return this.mData.getTag(getTrueTagKey(paramInt1), paramInt2);
  }
  
  protected SparseIntArray getTagInfo()
  {
    if (this.mTagInfo == null)
    {
      this.mTagInfo = new SparseIntArray();
      initTagInfo();
    }
    return this.mTagInfo;
  }
  
  public Integer getTagIntValue(int paramInt)
  {
    return getTagIntValue(paramInt, getDefinedTagDefaultIfd(paramInt));
  }
  
  public Integer getTagIntValue(int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = getTagIntValues(paramInt1, paramInt2);
    if ((arrayOfInt == null) || (arrayOfInt.length <= 0)) {
      return null;
    }
    return new Integer(arrayOfInt[0]);
  }
  
  public int[] getTagIntValues(int paramInt1, int paramInt2)
  {
    ExifTag localExifTag = getTag(paramInt1, paramInt2);
    if (localExifTag == null) {
      return null;
    }
    return localExifTag.getValueAsInts();
  }
  
  public void readExif(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream == null) {
      throw new IllegalArgumentException("Argument is null");
    }
    try
    {
      ExifData localExifData = new ExifReader(this).read(paramInputStream);
      this.mData = localExifData;
      return;
    }
    catch (ExifInvalidFormatException localExifInvalidFormatException)
    {
      throw new IOException("Invalid exif format : " + localExifInvalidFormatException);
    }
  }
  
  public void readExif(String paramString)
    throws FileNotFoundException, IOException
  {
    if (paramString == null) {
      throw new IllegalArgumentException("Argument is null");
    }
    localObject = null;
    try
    {
      localBufferedInputStream = new BufferedInputStream(new FileInputStream(paramString));
      closeSilently((Closeable)localObject);
    }
    catch (IOException localIOException1)
    {
      try
      {
        readExif(localBufferedInputStream);
        localBufferedInputStream.close();
        return;
      }
      catch (IOException localIOException2)
      {
        for (;;)
        {
          BufferedInputStream localBufferedInputStream;
          localObject = localBufferedInputStream;
        }
      }
      localIOException1 = localIOException1;
    }
    throw localIOException1;
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.gallery3d.exif.ExifInterface
 * JD-Core Version:    0.7.0.1
 */