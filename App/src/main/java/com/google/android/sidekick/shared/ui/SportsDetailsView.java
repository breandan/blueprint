package com.google.android.sidekick.shared.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.google.android.shared.util.LayoutUtils;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class SportsDetailsView
  extends TableLayout
{
  private static final Column SEPARATOR_COLUMN = new Column("|", "|", "|", false, 0, null);
  private ViewGroup mHeader;
  private int mMarginWidth;
  private ViewGroup mTeam1Row;
  private ViewGroup mTeam2Row;
  
  public SportsDetailsView(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  private TextView addCell(ViewGroup paramViewGroup, String paramString, boolean paramBoolean, int paramInt1, int paramInt2, LayoutInflater paramLayoutInflater)
  {
    if (paramString == null) {
      paramString = getContext().getString(2131362319);
    }
    TextView localTextView = (TextView)paramLayoutInflater.inflate(2130968842, paramViewGroup, false);
    localTextView.setText(paramString);
    localTextView.setGravity(paramInt1);
    TableRow.LayoutParams localLayoutParams = (TableRow.LayoutParams)localTextView.getLayoutParams();
    localLayoutParams.gravity = paramInt1;
    if (paramBoolean) {
      localLayoutParams.weight = 1.0F;
    }
    for (;;)
    {
      localTextView.setLayoutParams(localLayoutParams);
      paramViewGroup.addView(localTextView);
      return localTextView;
      if (paramInt2 > 0) {
        LayoutUtils.setPaddingRelative(localTextView, paramInt2, localTextView.getPaddingTop(), 0, localTextView.getPaddingBottom());
      }
    }
  }
  
  private void addColumns(List<Column> paramList, boolean paramBoolean)
  {
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    getContext().getResources();
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Column localColumn = (Column)localIterator.next();
      if (localColumn == SEPARATOR_COLUMN)
      {
        addSeparator(this.mHeader, localLayoutInflater);
        addSeparator(this.mTeam1Row, localLayoutInflater);
        addSeparator(this.mTeam2Row, localLayoutInflater);
        i = 0;
      }
      else
      {
        if ((localColumn.mStretch) || (paramBoolean)) {}
        for (boolean bool = true;; bool = false)
        {
          addCell(this.mHeader, localColumn.mHeaderName, bool, localColumn.mGravity, i, localLayoutInflater).setTextColor(getContext().getResources().getColor(2131230836));
          addCell(this.mTeam1Row, localColumn.mTeam1Value, bool, localColumn.mGravity, i, localLayoutInflater);
          addCell(this.mTeam2Row, localColumn.mTeam2Value, bool, localColumn.mGravity, i, localLayoutInflater);
          i = this.mMarginWidth;
          break;
        }
      }
    }
  }
  
  private void addSeparator(ViewGroup paramViewGroup, LayoutInflater paramLayoutInflater)
  {
    View localView = paramLayoutInflater.inflate(2130968705, paramViewGroup, false);
    ((TableRow.LayoutParams)localView.getLayoutParams()).setMargins(this.mMarginWidth, 0, this.mMarginWidth, 0);
    paramViewGroup.addView(localView);
  }
  
  private void init(Context paramContext)
  {
    LayoutInflater.from(paramContext).inflate(2130968840, this);
    this.mHeader = ((ViewGroup)findViewById(2131297041));
    this.mTeam1Row = ((ViewGroup)findViewById(2131297042));
    this.mTeam2Row = ((ViewGroup)findViewById(2131297043));
    setBackgroundResource(2131230849);
    TableLayout.LayoutParams localLayoutParams = new TableLayout.LayoutParams(-1, -2);
    localLayoutParams.gravity = 1;
    setLayoutParams(localLayoutParams);
    this.mMarginWidth = getContext().getResources().getDimensionPixelOffset(2131689834);
  }
  
  public static class Builder
  {
    private final List<SportsDetailsView.Column> mColumns = Lists.newArrayList();
    private final boolean mCompact;
    
    public Builder(boolean paramBoolean)
    {
      this.mCompact = paramBoolean;
    }
    
    public Builder addCentered(String paramString1, String paramString2, String paramString3)
    {
      this.mColumns.add(new SportsDetailsView.Column(paramString1, paramString2, paramString3, false, 17, null));
      return this;
    }
    
    public Builder addLeftAlignedStretched(String paramString1, String paramString2, String paramString3)
    {
      this.mColumns.add(new SportsDetailsView.Column(paramString1, paramString2, paramString3, true, 8388611, null));
      return this;
    }
    
    public Builder addSeparator()
    {
      this.mColumns.add(SportsDetailsView.SEPARATOR_COLUMN);
      return this;
    }
    
    public SportsDetailsView build(Context paramContext)
    {
      SportsDetailsView localSportsDetailsView = new SportsDetailsView(paramContext);
      localSportsDetailsView.addColumns(this.mColumns, this.mCompact);
      return localSportsDetailsView;
    }
  }
  
  private static class Column
  {
    private final int mGravity;
    private final String mHeaderName;
    private final boolean mStretch;
    private final String mTeam1Value;
    private final String mTeam2Value;
    
    private Column(String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
    {
      this.mHeaderName = paramString1;
      this.mTeam1Value = paramString2;
      this.mTeam2Value = paramString3;
      this.mStretch = paramBoolean;
      this.mGravity = paramInt;
    }
  }
}


/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar
 * Qualified Name:     com.google.android.sidekick.shared.ui.SportsDetailsView
 * JD-Core Version:    0.7.0.1
 */