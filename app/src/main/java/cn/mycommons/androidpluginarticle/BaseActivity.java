package cn.mycommons.androidpluginarticle;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * BaseActivity <br/>
 * Created by xiaqiulei on 2017-08-21.
 */
public class BaseActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}