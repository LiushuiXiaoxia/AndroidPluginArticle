package cn.mycommons.replugindemoplugin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import cn.mycommons.replugindemobase.Instance;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSharedPreferences("plugin", Context.MODE_PRIVATE).edit().putInt("plugin", 456).apply();

        Object object = Instance.getObject();

        Log.e(TAG, "Plugin onCreate: Instance = " + object);
        Log.e(TAG, "Plugin onCreate: Instance = " + this);
        Log.e(TAG, "Plugin onCreate: Application = " + getApplication());

        Toast.makeText(getApplicationContext(), "" + object, Toast.LENGTH_SHORT).show();
    }
}