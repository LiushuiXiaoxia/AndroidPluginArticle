package cn.mycommons.classloaderdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testLoader();
        testCustomClassLoader();
    }

    private void testCustomClassLoader() {
        int mode = Context.MODE_PRIVATE;
        String path = getDir("myApk", mode).getAbsolutePath().concat("/app-debug.apk");
        try {
            Log.i(TAG, "onCreate: path = " + path);

            CopyUtil.copy(getAssets().open("app-debug.apk"), new File(path));
            String opt = getDir("myOpt", mode).getAbsolutePath();
            String lib = getDir("myLib", mode).getAbsolutePath();
            DexClassLoader dexClassLoader = new DexClassLoader(path, opt, lib, getClassLoader());
            Class<?> appClass = dexClassLoader.loadClass("cn.mycommons.androidpluginarticle.AppClass");
            Object obj = appClass.newInstance();
            Method method = appClass.getMethod("fun");
            method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testLoader() {
        Class<?> aClass = getClass();
        ClassLoader classLoader = aClass.getClassLoader();
        while (classLoader != null) {
            Log.i(TAG, "onCreate: classLoader = " + classLoader);
            classLoader = classLoader.getParent();
        }
    }
}