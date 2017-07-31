package cn.mycommons.androidpluginarticle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * MyService <br/>
 * Created by xiaqiulei on 2017-07-31.
 */
public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}