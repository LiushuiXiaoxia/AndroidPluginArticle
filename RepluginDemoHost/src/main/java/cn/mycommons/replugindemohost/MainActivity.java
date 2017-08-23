package cn.mycommons.replugindemohost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.qihoo360.replugin.RePlugin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean result = RePlugin.preload("plugin");
        boolean installed = RePlugin.isPluginInstalled("plugin");
        Intent intent = RePlugin.createIntent("plugin", "cn.mycommons.replugindemoplugin.MainActivity");
        RePlugin.startActivity(this, intent);
        // RePlugin.startActivity(this, new Intent(), "plugin", "cn.mycommons.replugindemoplugin.MainActivity");
    }
}