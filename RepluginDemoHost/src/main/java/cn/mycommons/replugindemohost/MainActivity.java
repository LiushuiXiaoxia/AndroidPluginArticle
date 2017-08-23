package cn.mycommons.replugindemohost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import cn.mycommons.replugindemobase.Instance;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPlugin();
            }
        });
        doPlugin();
    }

    void doPlugin() {
        Instance.setObject("Host");

        Log.e(TAG, "Host onCreate: Instance = " + Instance.getObject());
        Log.e(TAG, "Host onCreate: Instance = " + this);
        Log.e(TAG, "Host onCreate: Application = " + getApplication());

        getSharedPreferences("host", Context.MODE_PRIVATE).edit().putInt("host", 123).apply();

        boolean installed = RePlugin.isPluginInstalled("plugin");
        if (!installed) {
            RePlugin.preload("plugin");
        }

        PluginInfo plugin = RePlugin.getPluginInfo("plugin");
        Log.e(TAG, "doPlugin: " + plugin);

        Intent intent = RePlugin.createIntent("plugin", "cn.mycommons.replugindemoplugin.MainActivity");
        RePlugin.startActivity(this, intent);
        // RePlugin.startActivity(this, new Intent(), "plugin", "cn.mycommons.replugindemoplugin.MainActivity");
    }
}