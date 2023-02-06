package org.enkasacco.mobile.widget;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


public abstract class BaseApplication extends MultiDexApplication {
    public static int initCount;
    public static BaseApplication mBaseApp;

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        mBaseApp = this;
    }

}
