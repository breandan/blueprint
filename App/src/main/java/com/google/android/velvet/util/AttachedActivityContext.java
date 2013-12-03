package com.google.android.velvet.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewParent;

import com.google.android.shared.util.Util;
import com.google.common.base.Preconditions;

public class AttachedActivityContext
        extends ContextWrapper {
    private Activity mAttachedToActivity;
    private boolean mHaveView;

    public AttachedActivityContext(Context paramContext) {
        super(paramContext);
        if (!(paramContext instanceof Activity)) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool, "Expected an application context");
            return;
        }
    }

    private void setActivityFromView(View paramView) {
        while ((paramView != null) && (!(paramView.getContext() instanceof Activity))) {
            ViewParent localViewParent = paramView.getParent();
            if ((localViewParent instanceof View)) {
                paramView = (View) localViewParent;
            } else {
                paramView = null;
            }
        }
        if (paramView != null) {
        }
        for (Activity localActivity = (Activity) paramView.getContext(); ; localActivity = null) {
            this.mAttachedToActivity = localActivity;
            if ((Util.SDK_INT >= 19) && (this.mAttachedToActivity != null)) {
                paramView.dispatchConfigurationChanged(this.mAttachedToActivity.getResources().getConfiguration());
            }
            return;
        }
    }

    public Object getSystemService(String paramString) {
        if ((this.mAttachedToActivity != null) && ((paramString.equals("window")) || (paramString.equals("layout_inflater")))) {
            return this.mAttachedToActivity.getSystemService(paramString);
        }
        return super.getSystemService(paramString);
    }

    public Resources.Theme getTheme() {
        if (this.mAttachedToActivity != null) {
            return this.mAttachedToActivity.getTheme();
        }
        return super.getTheme();
    }

    public void setView(View paramView) {
        if (!this.mHaveView) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkState(bool);
            this.mHaveView = true;
            paramView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View paramAnonymousView) {
                    AttachedActivityContext.this.setActivityFromView(paramAnonymousView);
                }

                public void onViewDetachedFromWindow(View paramAnonymousView) {
                    AttachedActivityContext.this.setActivityFromView(null);
                }
            });
            return;
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.velvet.util.AttachedActivityContext

 * JD-Core Version:    0.7.0.1

 */