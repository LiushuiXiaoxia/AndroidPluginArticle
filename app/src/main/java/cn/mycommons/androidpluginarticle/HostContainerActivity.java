package cn.mycommons.androidpluginarticle;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * HostContainerActivity <br/>
 * Created by xiaqiulei on 2017-08-21.
 */
public class HostContainerActivity extends BaseActivity {

    public static final String EXTRA_BASE_ACTIVITY = "extra_base_activity";
    private BaseActivity remote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String clazz = getIntent().getStringExtra(EXTRA_BASE_ACTIVITY);
        try {
            remote = (BaseActivity) Class.forName(clazz).newInstance();
            remote.onCreate(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        remote.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        remote.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        remote.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

        remote.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        remote.onDestroy();
    }
}