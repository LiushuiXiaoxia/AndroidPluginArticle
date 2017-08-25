package cn.mycommons.androidpluginarticle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSharedPreferences("config", Context.MODE_PRIVATE).edit().putInt("abc", 123).apply();

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("cn.mycommons.classloaderdemo", "cn.mycommons.classloaderdemo.MainActivity"));
                startActivity(intent);
            }
        });
    }
}