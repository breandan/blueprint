package com.google.android.velvet.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NavigatingPhotoViewActivity
        extends VelvetPhotoViewActivity {
    private MenuItem mNavMenuItem;

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        super.onCreateOptionsMenu(paramMenu);
        getMenuInflater().inflate(2131886085, paramMenu);
        this.mNavMenuItem = paramMenu.findItem(2131297285);
        updateNavItem(getCursorAtProperPosition());
        return true;
    }

    public void onPageSelected(int paramInt) {
        super.onPageSelected(paramInt);
        Cursor localCursor = getCursorAtProperPosition();
        updateNavItem(localCursor);
        updateCopyrightString(localCursor);
    }

    void setNavMenuItemForTest(MenuItem paramMenuItem) {
        this.mNavMenuItem = paramMenuItem;
    }

    void updateCopyrightString(Cursor paramCursor) {
        TextView localTextView = (TextView) findViewById(2131296650);
        int i = 0;
        if (paramCursor != null) {
            int j = paramCursor.getColumnIndex("source");
            i = 0;
            if (j != -1) {
                String str = paramCursor.getString(j);
                boolean bool = TextUtils.isEmpty(str);
                i = 0;
                if (!bool) {
                    localTextView.setText(str);
                    localTextView.setVisibility(0);
                    i = 1;
                }
            }
        }
        if (i == 0) {
            localTextView.setVisibility(8);
        }
    }

    void updateNavItem(Cursor paramCursor) {
        if (this.mNavMenuItem != null) {
            int i = 0;
            if (paramCursor != null) {
                int j = paramCursor.getColumnIndex("nav_uri");
                i = 0;
                if (j != -1) {
                    final String str = paramCursor.getString(j);
                    boolean bool = TextUtils.isEmpty(str);
                    i = 0;
                    if (!bool) {
                        this.mNavMenuItem.setVisible(true);
                        this.mNavMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem) {
                                Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                                localIntent.setFlags(268435456);
                                NavigatingPhotoViewActivity.this.getApplicationContext().startActivity(localIntent);
                                return true;
                            }
                        });
                        i = 1;
                    }
                }
            }
            if (i == 0) {
                this.mNavMenuItem.setVisible(false);
                this.mNavMenuItem.setOnMenuItemClickListener(null);
            }
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.gallery.NavigatingPhotoViewActivity

 * JD-Core Version:    0.7.0.1

 */