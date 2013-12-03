package com.google.android.voicesearch.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.velvet.VelvetServices;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

public class AppSelectionHelper {
    private static final Intent LAUNCHER_INTENT = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER");
    private final PackageManager mPackageManager;
    private final PreferredApplicationsManager mPreferredApplicationsManager;
    private final Resources mResources;

    public AppSelectionHelper(PreferredApplicationsManager paramPreferredApplicationsManager, Context paramContext, PackageManager paramPackageManager, Resources paramResources) {
        this.mPreferredApplicationsManager = paramPreferredApplicationsManager;
        this.mPackageManager = paramPackageManager;
        this.mResources = paramResources;
    }

    private List<App> findActivitiesInternal(String paramString, Intent paramIntent) {
        ArrayList localArrayList = Lists.newArrayList();
        List localList = this.mPackageManager.queryIntentActivities(paramIntent, 0);
        if (localList != null) {
            Iterator localIterator = localList.iterator();
            while (localIterator.hasNext()) {
                ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
                ActivityInfo localActivityInfo = localResolveInfo.activityInfo;
                CharSequence localCharSequence = localResolveInfo.loadLabel(this.mPackageManager);
                if ((paramString == null) || (paramString.equalsIgnoreCase(localCharSequence.toString()))) {
                    Intent localIntent = new Intent(paramIntent);
                    localIntent.setPackage(localActivityInfo.applicationInfo.packageName);
                    localIntent.setClassName(localActivityInfo.applicationInfo.packageName, localActivityInfo.name);
                    localArrayList.add(createApp(localIntent, this.mPackageManager, localResolveInfo));
                }
            }
        }
        return localArrayList;
    }

    public static AppSelectionHelper fromContext(Context paramContext) {
        return new AppSelectionHelper(new PreferredApplicationsManager(VelvetServices.get().getPreferenceController().getMainPreferences()), paramContext, paramContext.getPackageManager(), paramContext.getResources());
    }

    public void appSelected(String paramString1, String paramString2) {
        this.mPreferredApplicationsManager.setPreferredApplication(paramString1, paramString2);
    }

    protected App createApp(Intent paramIntent, PackageManager paramPackageManager, ResolveInfo paramResolveInfo) {
        ActivityInfo localActivityInfo = paramResolveInfo.activityInfo;
        return new App(paramIntent, localActivityInfo.loadLabel(paramPackageManager).toString(), localActivityInfo.loadIcon(paramPackageManager));
    }

    public List<App> findActivities(Intent paramIntent) {
        if (paramIntent == null) {
            return Lists.newArrayList();
        }
        return findActivitiesInternal(null, paramIntent);
    }

    public List<App> findActivities(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return Collections.emptyList();
        }
        ComponentName localComponentName = ComponentName.unflattenFromString(paramString);
        if (localComponentName != null) {
            Intent localIntent = new Intent("android.intent.action.MAIN");
            localIntent.setComponent(localComponentName);
            return findActivitiesInternal(null, localIntent);
        }
        return findActivitiesInternal(paramString, LAUNCHER_INTENT);
    }

    public App getPlayStoreLink(Uri paramUri) {
        return new App(new Intent("android.intent.action.VIEW").setPackage("com.android.vending").setData(paramUri), this.mResources.getString(2131363453), this.mResources.getDrawable(2130837679));
    }

    @Nonnull
    public App getSelectedApp(String paramString, Collection<App> paramCollection) {
        if (paramCollection.size() > 0) {
        }
        for (boolean bool = true; ; bool = false) {
            Preconditions.checkArgument(bool);
            String str = this.mPreferredApplicationsManager.getPreferredApplication(paramString);
            Iterator localIterator = paramCollection.iterator();
            App localApp;
            do {
                if (!localIterator.hasNext()) {
                    break;
                }
                localApp = (App) localIterator.next();
            } while (!localApp.getPackageName().equals(str));
            return localApp;
        }
        return (App) paramCollection.iterator().next();
    }

    public boolean isSupported(Intent paramIntent) {
        int i = this.mPackageManager.queryIntentActivities(paramIntent, 0).size();
        boolean bool = false;
        if (i > 0) {
            bool = true;
        }
        return bool;
    }

    public static class App {
        private final Drawable mIcon;
        private final Intent mIntent;
        private final String mLabel;

        public App(Intent paramIntent, String paramString, Drawable paramDrawable) {
            this.mIntent = paramIntent;
            this.mIcon = paramDrawable;
            this.mLabel = paramString;
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public Intent getLaunchIntent() {
            return this.mIntent;
        }

        public String getPackageName() {
            return this.mIntent.getPackage();
        }
    }
}



/* Location:           C:\Cygwin\home\breandan\apk-tool\classes-dex2jar.jar

 * Qualified Name:     com.google.android.voicesearch.util.AppSelectionHelper

 * JD-Core Version:    0.7.0.1

 */