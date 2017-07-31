package cn.mycommons.hookdemo;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * MyInvocationHandler <br/>
 * Created by xiaqiulei on 2017-07-31.
 */
public class MyInvocationHandler implements InvocationHandler {

    private static final String TAG = "MyInvocationHandler";

    @NonNull
    private IService service;

    public MyInvocationHandler(@NonNull IService service) {
        this.service = service;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Log.i(TAG, "invoke: before");
        Object result = method.invoke(service, objects);
        Log.i(TAG, "invoke: after");
        return result;
    }
}
