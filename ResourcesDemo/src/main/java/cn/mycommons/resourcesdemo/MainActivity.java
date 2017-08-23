package cn.mycommons.resourcesdemo;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    String name;
    File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = "ResourcesPlugin-debug.apk";
        apkFile = new File(Environment.getExternalStorageDirectory(), name);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStringFromApk();
            }
        });
    }

    void getStringFromApk() {
//        copyApk();
        AssetManager assetManager = createAssetManager();
        Resources pluginResources = createResources(assetManager);
        getStringFromPlugin(pluginResources);
    }

    void copyApk() {
        apkFile.deleteOnExit();
        try {
            InputStream inputStream = getAssets().open(name);
            OutputStream outputStream = new FileOutputStream(apkFile);
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private AssetManager createAssetManager() {
        AssetManager assetManager = null;
        try {
            Class<AssetManager> clazz = AssetManager.class;
            assetManager = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, apkFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetManager;
    }

    @Nullable
    private Resources createResources(AssetManager assetManager) {
        Resources pluginResources = null;
        if (assetManager != null) {
            Resources resources = getResources();
            pluginResources = new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        }
        return pluginResources;
    }

    private void getStringFromPlugin(Resources pluginResources) {
        if (pluginResources != null) {
            int id = pluginResources.getIdentifier("plugin_string", "string", "cn.mycommons.resourcesplugin");
            if (id > 0) {
                String str = pluginResources.getString(id);
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}