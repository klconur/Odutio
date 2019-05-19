package com.okb.odutio;

import android.app.Application;
import android.content.Context;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class OdutioApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        OdutioApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

}
