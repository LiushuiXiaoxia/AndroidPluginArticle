package cn.mycommons.replugindemoplugin;

import android.app.Application;
import android.util.Log;

/**
 * PluginApp <br/>
 * Created by xiaqiulei on 2017-08-24.
 */
public class PluginApp extends Application {

    private static final String TAG = "PluginApp";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "onCreate: ");
    }
}