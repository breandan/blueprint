package com.google.android.velvet.ui;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import javax.annotation.Nullable;

public class PopupMenuController
        implements View.OnClickListener, PopupMenu.OnDismissListener, PopupMenu.OnMenuItemClickListener {
    private final Activity mActivity;
    @Nullable
    private PopupMenu mMenu;

    public PopupMenuController(Activity paramActivity) {
        this.mActivity = paramActivity;
    }

    public void ensureClosed() {
        if (this.mMenu != null) {
            this.mMenu.dismiss();
            this.mMenu = null;
        }
    }

    public void onClick(View paramView) {
        this.mMenu = new PopupMenu(this.mActivity, paramView);
        this.mActivity.onCreatePanelMenu(0, this.mMenu.getMenu());
        this.mActivity.onPreparePanel(0, null, this.mMenu.getMenu());
        this.mMenu.setOnMenuItemClickListener(this);
        this.mMenu.setOnDismissListener(this);
        this.mMenu.show();
    }

    public void onDismiss(PopupMenu paramPopupMenu) {
        this.mMenu = null;
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        return this.mActivity.onMenuItemSelected(0, paramMenuItem);
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.ui.PopupMenuController

 * JD-Core Version:    0.7.0.1

 */