package com.android.datetimepicker.time;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.android.datetimepicker.HapticFeedbackController;
import com.android.datetimepicker.R.color;
import com.android.datetimepicker.R.drawable;
import com.android.datetimepicker.R.id;
import com.android.datetimepicker.R.layout;
import com.android.datetimepicker.R.string;
import com.android.datetimepicker.Utils;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class TimePickerDialog
  extends DialogFragment
  implements RadialPickerLayout.OnValueSelectedListener
{
  private boolean mAllowAutoAdvance;
  private int mAmKeyCode;
  private View mAmPmHitspace;
  private TextView mAmPmTextView;
  private String mAmText;
  private OnTimeSetListener mCallback;
  private String mDeletedKeyFormat;
  private TextView mDoneButton;
  private String mDoublePlaceholderText;
  private HapticFeedbackController mHapticFeedbackController;
  private String mHourPickerDescription;
  private TextView mHourSpaceView;
  private TextView mHourView;
  private boolean mInKbMode;
  private int mInitialHourOfDay;
  private int mInitialMinute;
  private boolean mIs24HourMode;
  private Node mLegalTimesTree;
  private String mMinutePickerDescription;
  private TextView mMinuteSpaceView;
  private TextView mMinuteView;
  private char mPlaceholderText;
  private int mPmKeyCode;
  private String mPmText;
  private String mSelectHours;
  private String mSelectMinutes;
  private int mSelectedColor;
  private boolean mThemeDark;
  private RadialPickerLayout mTimePicker;
  private ArrayList<Integer> mTypedTimes;
  private int mUnselectedColor;
  
  private boolean addKeyIfLegal(int paramInt)
  {
    if (((this.mIs24HourMode) && (this.mTypedTimes.size() == 4)) || ((!this.mIs24HourMode) && (isTypedTimeFullyLegal()))) {
      return false;
    }
    this.mTypedTimes.add(Integer.valueOf(paramInt));
    if (!isTypedTimeLegalSoFar())
    {
      deleteLastTypedKey();
      return false;
    }
    int i = getValFromKeyCode(paramInt);
    RadialPickerLayout localRadialPickerLayout = this.mTimePicker;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(i);
    Utils.tryAccessibilityAnnounce(localRadialPickerLayout, String.format("%d", arrayOfObject));
    if (isTypedTimeFullyLegal())
    {
      if ((!this.mIs24HourMode) && (this.mTypedTimes.size() <= 3))
      {
        this.mTypedTimes.add(-1 + this.mTypedTimes.size(), Integer.valueOf(7));
        this.mTypedTimes.add(-1 + this.mTypedTimes.size(), Integer.valueOf(7));
      }
      this.mDoneButton.setEnabled(true);
    }
    return true;
  }
  
  private int deleteLastTypedKey()
  {
    int i = ((Integer)this.mTypedTimes.remove(-1 + this.mTypedTimes.size())).intValue();
    if (!isTypedTimeFullyLegal()) {
      this.mDoneButton.setEnabled(false);
    }
    return i;
  }
  
  private void finishKbMode(boolean paramBoolean)
  {
    this.mInKbMode = false;
    if (!this.mTypedTimes.isEmpty())
    {
      int[] arrayOfInt = getEnteredTime(null);
      this.mTimePicker.setTime(arrayOfInt[0], arrayOfInt[1]);
      if (!this.mIs24HourMode) {
        this.mTimePicker.setAmOrPm(arrayOfInt[2]);
      }
      this.mTypedTimes.clear();
    }
    if (paramBoolean)
    {
      updateDisplay(false);
      this.mTimePicker.trySettingInputEnabled(true);
    }
  }
  
  private void generateLegalTimesTree()
  {
    Node localNode1 = new Node(new int[0]);
    this.mLegalTimesTree = localNode1;
    if (this.mIs24HourMode)
    {
      Node localNode2 = new Node(new int[] { 7, 8, 9, 10, 11, 12 });
      Node localNode3 = new Node(new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
      localNode2.addChild(localNode3);
      Node localNode4 = new Node(new int[] { 7, 8 });
      this.mLegalTimesTree.addChild(localNode4);
      Node localNode5 = new Node(new int[] { 7, 8, 9, 10, 11, 12 });
      localNode4.addChild(localNode5);
      localNode5.addChild(localNode2);
      Node localNode6 = new Node(new int[] { 13, 14, 15, 16 });
      localNode5.addChild(localNode6);
      Node localNode7 = new Node(new int[] { 13, 14, 15, 16 });
      localNode4.addChild(localNode7);
      localNode7.addChild(localNode2);
      Node localNode8 = new Node(new int[] { 9 });
      this.mLegalTimesTree.addChild(localNode8);
      Node localNode9 = new Node(new int[] { 7, 8, 9, 10 });
      localNode8.addChild(localNode9);
      localNode9.addChild(localNode2);
      Node localNode10 = new Node(new int[] { 11, 12 });
      localNode8.addChild(localNode10);
      localNode10.addChild(localNode3);
      Node localNode11 = new Node(new int[] { 10, 11, 12, 13, 14, 15, 16 });
      this.mLegalTimesTree.addChild(localNode11);
      localNode11.addChild(localNode2);
      return;
    }
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = getAmOrPmKeyCode(0);
    arrayOfInt[1] = getAmOrPmKeyCode(1);
    Node localNode12 = new Node(arrayOfInt);
    Node localNode13 = new Node(new int[] { 8 });
    this.mLegalTimesTree.addChild(localNode13);
    localNode13.addChild(localNode12);
    Node localNode14 = new Node(new int[] { 7, 8, 9 });
    localNode13.addChild(localNode14);
    localNode14.addChild(localNode12);
    Node localNode15 = new Node(new int[] { 7, 8, 9, 10, 11, 12 });
    localNode14.addChild(localNode15);
    localNode15.addChild(localNode12);
    Node localNode16 = new Node(new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    localNode15.addChild(localNode16);
    localNode16.addChild(localNode12);
    Node localNode17 = new Node(new int[] { 13, 14, 15, 16 });
    localNode14.addChild(localNode17);
    localNode17.addChild(localNode12);
    Node localNode18 = new Node(new int[] { 10, 11, 12 });
    localNode13.addChild(localNode18);
    Node localNode19 = new Node(new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    localNode18.addChild(localNode19);
    localNode19.addChild(localNode12);
    Node localNode20 = new Node(new int[] { 9, 10, 11, 12, 13, 14, 15, 16 });
    this.mLegalTimesTree.addChild(localNode20);
    localNode20.addChild(localNode12);
    Node localNode21 = new Node(new int[] { 7, 8, 9, 10, 11, 12 });
    localNode20.addChild(localNode21);
    Node localNode22 = new Node(new int[] { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 });
    localNode21.addChild(localNode22);
    localNode22.addChild(localNode12);
  }
  
  private int getAmOrPmKeyCode(int paramInt)
  {
    int i = -1;
    int j;
    if ((this.mAmKeyCode == i) || (this.mPmKeyCode == i))
    {
      KeyCharacterMap localKeyCharacterMap = KeyCharacterMap.load(i);
      j = 0;
      if (j < Math.max(this.mAmText.length(), this.mPmText.length()))
      {
        int k = this.mAmText.toLowerCase(Locale.getDefault()).charAt(j);
        int m = this.mPmText.toLowerCase(Locale.getDefault()).charAt(j);
        if (k == m) {
          break label164;
        }
        KeyEvent[] arrayOfKeyEvent = localKeyCharacterMap.getEvents(new char[] { k, m });
        if ((arrayOfKeyEvent == null) || (arrayOfKeyEvent.length != 4)) {
          break label153;
        }
        this.mAmKeyCode = arrayOfKeyEvent[0].getKeyCode();
        this.mPmKeyCode = arrayOfKeyEvent[2].getKeyCode();
      }
    }
    label142:
    if (paramInt == 0) {
      i = this.mAmKeyCode;
    }
    label153:
    label164:
    while (paramInt != 1)
    {
      return i;
      Log.e("TimePickerDialog", "Unable to find keycodes for AM and PM.");
      break label142;
      j++;
      break;
    }
    return this.mPmKeyCode;
  }
  
  private int[] getEnteredTime(Boolean[] paramArrayOfBoolean)
  {
    int i = -1;
    int j = 1;
    int i2;
    int k;
    int m;
    int n;
    label65:
    int i1;
    if ((!this.mIs24HourMode) && (isTypedTimeFullyLegal()))
    {
      i2 = ((Integer)this.mTypedTimes.get(-1 + this.mTypedTimes.size())).intValue();
      if (i2 == getAmOrPmKeyCode(0))
      {
        i = 0;
        j = 2;
      }
    }
    else
    {
      k = -1;
      m = -1;
      n = j;
      if (n > this.mTypedTimes.size()) {
        break label225;
      }
      i1 = getValFromKeyCode(((Integer)this.mTypedTimes.get(this.mTypedTimes.size() - n)).intValue());
      if (n != j) {
        break label136;
      }
      k = i1;
    }
    for (;;)
    {
      n++;
      break label65;
      if (i2 != getAmOrPmKeyCode(1)) {
        break;
      }
      i = 1;
      break;
      label136:
      if (n == j + 1)
      {
        k += i1 * 10;
        if ((paramArrayOfBoolean != null) && (i1 == 0)) {
          paramArrayOfBoolean[1] = Boolean.valueOf(true);
        }
      }
      else if (n == j + 2)
      {
        m = i1;
      }
      else if (n == j + 3)
      {
        m += i1 * 10;
        if ((paramArrayOfBoolean != null) && (i1 == 0)) {
          paramArrayOfBoolean[0] = Boolean.valueOf(true);
        }
      }
    }
    label225:
    return new int[] { m, k, i };
  }
  
  private static int getValFromKeyCode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 7: 
      return 0;
    case 8: 
      return 1;
    case 9: 
      return 2;
    case 10: 
      return 3;
    case 11: 
      return 4;
    case 12: 
      return 5;
    case 13: 
      return 6;
    case 14: 
      return 7;
    case 15: 
      return 8;
    }
    return 9;
  }
  
  private boolean isTypedTimeFullyLegal()
  {
    if (this.mIs24HourMode)
    {
      int[] arrayOfInt = getEnteredTime(null);
      return (arrayOfInt[0] >= 0) && (arrayOfInt[1] >= 0) && (arrayOfInt[1] < 60);
    }
    boolean bool1;
    if (!this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(0))))
    {
      boolean bool2 = this.mTypedTimes.contains(Integer.valueOf(getAmOrPmKeyCode(1)));
      bool1 = false;
      if (!bool2) {}
    }
    else
    {
      bool1 = true;
    }
    return bool1;
  }
  
  private boolean isTypedTimeLegalSoFar()
  {
    Node localNode = this.mLegalTimesTree;
    Iterator localIterator = this.mTypedTimes.iterator();
    while (localIterator.hasNext())
    {
      localNode = localNode.canReach(((Integer)localIterator.next()).intValue());
      if (localNode == null) {
        return false;
      }
    }
    return true;
  }
  
  public static TimePickerDialog newInstance(OnTimeSetListener paramOnTimeSetListener, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    TimePickerDialog localTimePickerDialog = new TimePickerDialog();
    localTimePickerDialog.initialize(paramOnTimeSetListener, paramInt1, paramInt2, paramBoolean);
    return localTimePickerDialog;
  }
  
  private boolean processKeyUp(int paramInt)
  {
    if ((paramInt == 111) || (paramInt == 4)) {
      dismiss();
    }
    label109:
    do
    {
      do
      {
        do
        {
          return true;
          if (paramInt != 61) {
            break;
          }
          if (!this.mInKbMode) {
            break label180;
          }
        } while (!isTypedTimeFullyLegal());
        finishKbMode(true);
        return true;
        if (paramInt != 66) {
          break label109;
        }
        if (!this.mInKbMode) {
          break;
        }
      } while (!isTypedTimeFullyLegal());
      finishKbMode(false);
      if (this.mCallback != null) {
        this.mCallback.onTimeSet(this.mTimePicker, this.mTimePicker.getHours(), this.mTimePicker.getMinutes());
      }
      dismiss();
      return true;
      if (paramInt == 67) {
        if ((this.mInKbMode) && (!this.mTypedTimes.isEmpty()))
        {
          i = deleteLastTypedKey();
          if (i != getAmOrPmKeyCode(0)) {
            break label182;
          }
          str = this.mAmText;
          Utils.tryAccessibilityAnnounce(this.mTimePicker, String.format(this.mDeletedKeyFormat, new Object[] { str }));
          updateDisplay(true);
        }
      }
      while ((paramInt != 7) && (paramInt != 8) && (paramInt != 9) && (paramInt != 10) && (paramInt != 11) && (paramInt != 12) && (paramInt != 13) && (paramInt != 14) && (paramInt != 15) && (paramInt != 16) && ((this.mIs24HourMode) || ((paramInt != getAmOrPmKeyCode(0)) && (paramInt != getAmOrPmKeyCode(1))))) {
        for (;;)
        {
          int i;
          String str;
          return false;
          if (i == getAmOrPmKeyCode(1))
          {
            str = this.mPmText;
          }
          else
          {
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Integer.valueOf(getValFromKeyCode(i));
            str = String.format("%d", arrayOfObject);
          }
        }
      }
      if (!this.mInKbMode)
      {
        if (this.mTimePicker == null)
        {
          Log.e("TimePickerDialog", "Unable to initiate keyboard mode, TimePicker was null.");
          return true;
        }
        this.mTypedTimes.clear();
        tryStartingKbMode(paramInt);
        return true;
      }
    } while (!addKeyIfLegal(paramInt));
    label180:
    label182:
    updateDisplay(false);
    return true;
  }
  
  private void setCurrentItemShowing(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.mTimePicker.setCurrentItemShowing(paramInt, paramBoolean1);
    TextView localTextView;
    int j;
    if (paramInt == 0)
    {
      int m = this.mTimePicker.getHours();
      if (!this.mIs24HourMode) {
        m %= 12;
      }
      this.mTimePicker.setContentDescription(this.mHourPickerDescription + ": " + m);
      if (paramBoolean3) {
        Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectHours);
      }
      localTextView = this.mHourView;
      if (paramInt != 0) {
        break label232;
      }
      j = this.mSelectedColor;
      label103:
      if (paramInt != 1) {
        break label241;
      }
    }
    label232:
    label241:
    for (int k = this.mSelectedColor;; k = this.mUnselectedColor)
    {
      this.mHourView.setTextColor(j);
      this.mMinuteView.setTextColor(k);
      ObjectAnimator localObjectAnimator = Utils.getPulseAnimator(localTextView, 0.85F, 1.1F);
      if (paramBoolean2) {
        localObjectAnimator.setStartDelay(300L);
      }
      localObjectAnimator.start();
      return;
      int i = this.mTimePicker.getMinutes();
      this.mTimePicker.setContentDescription(this.mMinutePickerDescription + ": " + i);
      if (paramBoolean3) {
        Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mSelectMinutes);
      }
      localTextView = this.mMinuteView;
      break;
      j = this.mUnselectedColor;
      break label103;
    }
  }
  
  private void setHour(int paramInt, boolean paramBoolean)
  {
    String str1;
    if (this.mIs24HourMode) {
      str1 = "%02d";
    }
    for (;;)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      String str2 = String.format(str1, arrayOfObject);
      this.mHourView.setText(str2);
      this.mHourSpaceView.setText(str2);
      if (paramBoolean) {
        Utils.tryAccessibilityAnnounce(this.mTimePicker, str2);
      }
      return;
      str1 = "%d";
      paramInt %= 12;
      if (paramInt == 0) {
        paramInt = 12;
      }
    }
  }
  
  private void setMinute(int paramInt)
  {
    if (paramInt == 60) {
      paramInt = 0;
    }
    Locale localLocale = Locale.getDefault();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    String str = String.format(localLocale, "%02d", arrayOfObject);
    Utils.tryAccessibilityAnnounce(this.mTimePicker, str);
    this.mMinuteView.setText(str);
    this.mMinuteSpaceView.setText(str);
  }
  
  private void tryStartingKbMode(int paramInt)
  {
    if ((this.mTimePicker.trySettingInputEnabled(false)) && ((paramInt == -1) || (addKeyIfLegal(paramInt))))
    {
      this.mInKbMode = true;
      this.mDoneButton.setEnabled(false);
      updateDisplay(false);
    }
  }
  
  private void updateAmPmDisplay(int paramInt)
  {
    if (paramInt == 0)
    {
      this.mAmPmTextView.setText(this.mAmText);
      Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mAmText);
      this.mAmPmHitspace.setContentDescription(this.mAmText);
      return;
    }
    if (paramInt == 1)
    {
      this.mAmPmTextView.setText(this.mPmText);
      Utils.tryAccessibilityAnnounce(this.mTimePicker, this.mPmText);
      this.mAmPmHitspace.setContentDescription(this.mPmText);
      return;
    }
    this.mAmPmTextView.setText(this.mDoublePlaceholderText);
  }
  
  private void updateDisplay(boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.mTypedTimes.isEmpty()))
    {
      int i = this.mTimePicker.getHours();
      int j = this.mTimePicker.getMinutes();
      setHour(i, true);
      setMinute(j);
      if (!this.mIs24HourMode) {
        if (i >= 12) {
          break label91;
        }
      }
      label91:
      for (int k = 0;; k = 1)
      {
        updateAmPmDisplay(k);
        setCurrentItemShowing(this.mTimePicker.getCurrentItemShowing(), true, true, true);
        this.mDoneButton.setEnabled(true);
        return;
      }
    }
    Boolean[] arrayOfBoolean = new Boolean[2];
    arrayOfBoolean[0] = Boolean.valueOf(false);
    arrayOfBoolean[1] = Boolean.valueOf(false);
    int[] arrayOfInt = getEnteredTime(arrayOfBoolean);
    String str1;
    label136:
    String str2;
    label150:
    String str3;
    if (arrayOfBoolean[0].booleanValue())
    {
      str1 = "%02d";
      if (!arrayOfBoolean[1].booleanValue()) {
        break label257;
      }
      str2 = "%02d";
      if (arrayOfInt[0] != -1) {
        break label265;
      }
      str3 = this.mDoublePlaceholderText;
      label163:
      if (arrayOfInt[1] != -1) {
        break label302;
      }
    }
    label257:
    label265:
    label302:
    Object[] arrayOfObject2;
    for (String str4 = this.mDoublePlaceholderText;; str4 = String.format(str2, arrayOfObject2).replace(' ', this.mPlaceholderText))
    {
      this.mHourView.setText(str3);
      this.mHourSpaceView.setText(str3);
      this.mHourView.setTextColor(this.mUnselectedColor);
      this.mMinuteView.setText(str4);
      this.mMinuteSpaceView.setText(str4);
      this.mMinuteView.setTextColor(this.mUnselectedColor);
      if (this.mIs24HourMode) {
        break;
      }
      updateAmPmDisplay(arrayOfInt[2]);
      return;
      str1 = "%2d";
      break label136;
      str2 = "%2d";
      break label150;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(arrayOfInt[0]);
      str3 = String.format(str1, arrayOfObject1).replace(' ', this.mPlaceholderText);
      break label163;
      arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(arrayOfInt[1]);
    }
  }
  
  public void initialize(OnTimeSetListener paramOnTimeSetListener, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.mCallback = paramOnTimeSetListener;
    this.mInitialHourOfDay = paramInt1;
    this.mInitialMinute = paramInt2;
    this.mIs24HourMode = paramBoolean;
    this.mInKbMode = false;
    this.mThemeDark = false;
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if ((paramBundle != null) && (paramBundle.containsKey("hour_of_day")) && (paramBundle.containsKey("minute")) && (paramBundle.containsKey("is_24_hour_view")))
    {
      this.mInitialHourOfDay = paramBundle.getInt("hour_of_day");
      this.mInitialMinute = paramBundle.getInt("minute");
      this.mIs24HourMode = paramBundle.getBoolean("is_24_hour_view");
      this.mInKbMode = paramBundle.getBoolean("in_kb_mode");
      this.mThemeDark = paramBundle.getBoolean("dark_theme");
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    getDialog().getWindow().requestFeature(1);
    View localView1 = paramLayoutInflater.inflate(R.layout.time_picker_dialog, null);
    KeyboardListener localKeyboardListener = new KeyboardListener(null);
    localView1.findViewById(R.id.time_picker_dialog).setOnKeyListener(localKeyboardListener);
    Resources localResources = getResources();
    this.mHourPickerDescription = localResources.getString(R.string.hour_picker_description);
    this.mSelectHours = localResources.getString(R.string.select_hours);
    this.mMinutePickerDescription = localResources.getString(R.string.minute_picker_description);
    this.mSelectMinutes = localResources.getString(R.string.select_minutes);
    int i;
    int j;
    label135:
    label634:
    int n;
    int i1;
    int i2;
    int i3;
    ColorStateList localColorStateList1;
    int i4;
    int i5;
    int i6;
    int i7;
    ColorStateList localColorStateList2;
    int i8;
    int i9;
    label773:
    label797:
    int i10;
    label828:
    label855:
    label879:
    TextView localTextView4;
    if (this.mThemeDark)
    {
      i = R.color.red;
      this.mSelectedColor = localResources.getColor(i);
      if (!this.mThemeDark) {
        break label957;
      }
      j = R.color.white;
      this.mUnselectedColor = localResources.getColor(j);
      this.mHourView = ((TextView)localView1.findViewById(R.id.hours));
      this.mHourView.setOnKeyListener(localKeyboardListener);
      this.mHourSpaceView = ((TextView)localView1.findViewById(R.id.hour_space));
      this.mMinuteSpaceView = ((TextView)localView1.findViewById(R.id.minutes_space));
      this.mMinuteView = ((TextView)localView1.findViewById(R.id.minutes));
      this.mMinuteView.setOnKeyListener(localKeyboardListener);
      this.mAmPmTextView = ((TextView)localView1.findViewById(R.id.ampm_label));
      this.mAmPmTextView.setOnKeyListener(localKeyboardListener);
      String[] arrayOfString = new DateFormatSymbols().getAmPmStrings();
      this.mAmText = arrayOfString[0];
      this.mPmText = arrayOfString[1];
      this.mHapticFeedbackController = new HapticFeedbackController(getActivity());
      this.mTimePicker = ((RadialPickerLayout)localView1.findViewById(R.id.time_picker));
      this.mTimePicker.setOnValueSelectedListener(this);
      this.mTimePicker.setOnKeyListener(localKeyboardListener);
      this.mTimePicker.initialize(getActivity(), this.mHapticFeedbackController, this.mInitialHourOfDay, this.mInitialMinute, this.mIs24HourMode);
      int k = 0;
      if (paramBundle != null)
      {
        boolean bool = paramBundle.containsKey("current_item_showing");
        k = 0;
        if (bool) {
          k = paramBundle.getInt("current_item_showing");
        }
      }
      setCurrentItemShowing(k, false, true, true);
      this.mTimePicker.invalidate();
      this.mHourView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          TimePickerDialog.this.setCurrentItemShowing(0, true, false, true);
          TimePickerDialog.this.tryVibrate();
        }
      });
      this.mMinuteView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          TimePickerDialog.this.setCurrentItemShowing(1, true, false, true);
          TimePickerDialog.this.tryVibrate();
        }
      });
      this.mDoneButton = ((TextView)localView1.findViewById(R.id.done_button));
      this.mDoneButton.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          if ((TimePickerDialog.this.mInKbMode) && (TimePickerDialog.this.isTypedTimeFullyLegal())) {
            TimePickerDialog.this.finishKbMode(false);
          }
          for (;;)
          {
            if (TimePickerDialog.this.mCallback != null) {
              TimePickerDialog.this.mCallback.onTimeSet(TimePickerDialog.this.mTimePicker, TimePickerDialog.this.mTimePicker.getHours(), TimePickerDialog.this.mTimePicker.getMinutes());
            }
            TimePickerDialog.this.dismiss();
            return;
            TimePickerDialog.this.tryVibrate();
          }
        }
      });
      this.mDoneButton.setOnKeyListener(localKeyboardListener);
      this.mAmPmHitspace = localView1.findViewById(R.id.ampm_hitspace);
      if (!this.mIs24HourMode) {
        break label965;
      }
      this.mAmPmTextView.setVisibility(8);
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      localLayoutParams.addRule(13);
      ((TextView)localView1.findViewById(R.id.separator)).setLayoutParams(localLayoutParams);
      this.mAllowAutoAdvance = true;
      setHour(this.mInitialHourOfDay, true);
      setMinute(this.mInitialMinute);
      this.mDoublePlaceholderText = localResources.getString(R.string.time_placeholder);
      this.mDeletedKeyFormat = localResources.getString(R.string.deleted_key);
      this.mPlaceholderText = this.mDoublePlaceholderText.charAt(0);
      this.mPmKeyCode = -1;
      this.mAmKeyCode = -1;
      generateLegalTimesTree();
      if (!this.mInKbMode) {
        break label1015;
      }
      this.mTypedTimes = paramBundle.getIntegerArrayList("typed_times");
      tryStartingKbMode(-1);
      this.mHourView.invalidate();
      this.mTimePicker.setTheme(getActivity().getApplicationContext(), this.mThemeDark);
      n = localResources.getColor(R.color.white);
      i1 = localResources.getColor(R.color.circle_background);
      i2 = localResources.getColor(R.color.line_background);
      i3 = localResources.getColor(R.color.numbers_text_color);
      localColorStateList1 = localResources.getColorStateList(R.color.done_text_color);
      i4 = R.drawable.done_background_color;
      i5 = localResources.getColor(R.color.dark_gray);
      i6 = localResources.getColor(R.color.light_gray);
      i7 = localResources.getColor(R.color.line_dark);
      localColorStateList2 = localResources.getColorStateList(R.color.done_text_color_dark);
      i8 = R.drawable.done_background_color_dark;
      View localView2 = localView1.findViewById(R.id.time_display_background);
      if (!this.mThemeDark) {
        break label1036;
      }
      i9 = i5;
      localView2.setBackgroundColor(i9);
      View localView3 = localView1.findViewById(R.id.time_display);
      if (!this.mThemeDark) {
        break label1043;
      }
      localView3.setBackgroundColor(i5);
      TextView localTextView1 = (TextView)localView1.findViewById(R.id.separator);
      if (!this.mThemeDark) {
        break label1050;
      }
      i10 = n;
      localTextView1.setTextColor(i10);
      TextView localTextView2 = (TextView)localView1.findViewById(R.id.ampm_label);
      if (!this.mThemeDark) {
        break label1057;
      }
      localTextView2.setTextColor(n);
      View localView4 = localView1.findViewById(R.id.line);
      if (!this.mThemeDark) {
        break label1064;
      }
      localView4.setBackgroundColor(i7);
      TextView localTextView3 = this.mDoneButton;
      if (!this.mThemeDark) {
        break label1071;
      }
      label899:
      localTextView3.setTextColor(localColorStateList2);
      RadialPickerLayout localRadialPickerLayout = this.mTimePicker;
      if (!this.mThemeDark) {
        break label1078;
      }
      label919:
      localRadialPickerLayout.setBackgroundColor(i6);
      localTextView4 = this.mDoneButton;
      if (!this.mThemeDark) {
        break label1085;
      }
    }
    for (;;)
    {
      localTextView4.setBackgroundResource(i8);
      return localView1;
      i = R.color.blue;
      break;
      label957:
      j = R.color.numbers_text_color;
      break label135;
      label965:
      this.mAmPmTextView.setVisibility(0);
      if (this.mInitialHourOfDay < 12) {}
      for (int m = 0;; m = 1)
      {
        updateAmPmDisplay(m);
        this.mAmPmHitspace.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View paramAnonymousView)
          {
            TimePickerDialog.this.tryVibrate();
            int i = TimePickerDialog.this.mTimePicker.getIsCurrentlyAmOrPm();
            if (i == 0) {
              i = 1;
            }
            for (;;)
            {
              TimePickerDialog.this.updateAmPmDisplay(i);
              TimePickerDialog.this.mTimePicker.setAmOrPm(i);
              return;
              if (i == 1) {
                i = 0;
              }
            }
          }
        });
        break;
      }
      label1015:
      if (this.mTypedTimes != null) {
        break label634;
      }
      this.mTypedTimes = new ArrayList();
      break label634;
      label1036:
      i9 = n;
      break label773;
      label1043:
      i5 = n;
      break label797;
      label1050:
      i10 = i3;
      break label828;
      label1057:
      n = i3;
      break label855;
      label1064:
      i7 = i2;
      break label879;
      label1071:
      localColorStateList2 = localColorStateList1;
      break label899;
      label1078:
      i6 = i1;
      break label919;
      label1085:
      i8 = i4;
    }
  }
  
  public void onPause()
  {
    super.onPause();
    this.mHapticFeedbackController.stop();
  }
  
  public void onResume()
  {
    super.onResume();
    this.mHapticFeedbackController.start();
  }
  
  public void onSaveInstanceState(Bundle paramBundle)
  {
    if (this.mTimePicker != null)
    {
      paramBundle.putInt("hour_of_day", this.mTimePicker.getHours());
      paramBundle.putInt("minute", this.mTimePicker.getMinutes());
      paramBundle.putBoolean("is_24_hour_view", this.mIs24HourMode);
      paramBundle.putInt("current_item_showing", this.mTimePicker.getCurrentItemShowing());
      paramBundle.putBoolean("in_kb_mode", this.mInKbMode);
      if (this.mInKbMode) {
        paramBundle.putIntegerArrayList("typed_times", this.mTypedTimes);
      }
      paramBundle.putBoolean("dark_theme", this.mThemeDark);
    }
  }
  
  public void onValueSelected(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt1 == 0)
    {
      setHour(paramInt2, false);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt2);
      String str = String.format("%d", arrayOfObject);
      if ((this.mAllowAutoAdvance) && (paramBoolean))
      {
        setCurrentItemShowing(1, true, true, false);
        str = str + ". " + this.mSelectMinutes;
        Utils.tryAccessibilityAnnounce(this.mTimePicker, str);
      }
    }
    do
    {
      return;
      this.mTimePicker.setContentDescription(this.mHourPickerDescription + ": " + paramInt2);
      break;
      if (paramInt1 == 1)
      {
        setMinute(paramInt2);
        this.mTimePicker.setContentDescription(this.mMinutePickerDescription + ": " + paramInt2);
        return;
      }
      if (paramInt1 == 2)
      {
        updateAmPmDisplay(paramInt2);
        return;
      }
    } while (paramInt1 != 3);
    if (!isTypedTimeFullyLegal()) {
      this.mTypedTimes.clear();
    }
    finishKbMode(true);
  }
  
  public void setOnTimeSetListener(OnTimeSetListener paramOnTimeSetListener)
  {
    this.mCallback = paramOnTimeSetListener;
  }
  
  public void tryVibrate()
  {
    this.mHapticFeedbackController.tryVibrate();
  }
  
  private class KeyboardListener
    implements View.OnKeyListener
  {
    private KeyboardListener() {}
    
    public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
    {
      if (paramKeyEvent.getAction() == 1) {
        return TimePickerDialog.this.processKeyUp(paramInt);
      }
      return false;
    }
  }
  
  private class Node
  {
    private ArrayList<Node> mChildren;
    private int[] mLegalKeys;
    
    public Node(int... paramVarArgs)
    {
      this.mLegalKeys = paramVarArgs;
      this.mChildren = new ArrayList();
    }
    
    public void addChild(Node paramNode)
    {
      this.mChildren.add(paramNode);
    }
    
    public Node canReach(int paramInt)
    {
      if (this.mChildren == null) {
        return null;
      }
      Iterator localIterator = this.mChildren.iterator();
      while (localIterator.hasNext())
      {
        Node localNode = (Node)localIterator.next();
        if (localNode.containsKey(paramInt)) {
          return localNode;
        }
      }
      return null;
    }
    
    public boolean containsKey(int paramInt)
    {
      for (int i = 0; i < this.mLegalKeys.length; i++) {
        if (this.mLegalKeys[i] == paramInt) {
          return true;
        }
      }
      return false;
    }
  }
  
  public static abstract interface OnTimeSetListener
  {
    public abstract void onTimeSet(RadialPickerLayout paramRadialPickerLayout, int paramInt1, int paramInt2);
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.android.datetimepicker.time.TimePickerDialog
 * JD-Core Version:    0.7.0.1
 */