package cn.mycommons.hookdemo;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * ServiceProxy <br/>
 * Created by xiaqiulei on 2017-07-31.
 */
public class ServiceProxy implements IService {

    private static final String TAG = "ServiceProxy";

    @NonNull
    private IService base;

    public ServiceProxy(@NonNull IService base) {
        this.base = base;
    }

    @Override
    public void fun() {
        Log.i(TAG, "fun: before");
        base.fun();
        Log.i(TAG, "fun: after");
    }
}
