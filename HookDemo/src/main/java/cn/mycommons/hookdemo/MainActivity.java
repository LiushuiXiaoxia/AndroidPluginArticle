package cn.mycommons.hookdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MainActivity extends AppCompatActivity {

    private IService iService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iService = new ServiceImpl();
        callService();

        // reflectHock();
        proxyHook();

        callService();
    }

    void reflectHock() {
        try {
            Class<? extends MainActivity> aClass = getClass();
            Field field = aClass.getDeclaredField("iService");
            field.setAccessible(true);
            IService service = (IService) field.get(this);
            IService proxy = new ServiceProxy(service);
            field.set(this, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void proxyHook() {
        try {
            Class<? extends MainActivity> aClass = getClass();
            Field field = aClass.getDeclaredField("iService");
            field.setAccessible(true);
            IService value = (IService) field.get(this);

            InvocationHandler handler = new MyInvocationHandler(value);
            ClassLoader classLoader = value.getClass().getClassLoader();
            Object instance = Proxy.newProxyInstance(classLoader, value.getClass().getInterfaces(), handler);

            field.set(this, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void callService() {
        iService.fun();
    }
}