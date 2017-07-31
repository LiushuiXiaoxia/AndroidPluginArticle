package cn.mycommons.hookdemo;

import android.util.Log;

/**
 * ServiceImpl <br/>
 * Created by xiaqiulei on 2017-07-31.
 */
public class ServiceImpl implements IService {

    private static final String TAG = "ServiceImpl";

    @Override
    public void fun() {
        Log.i(TAG, "fun: ");
    }
}