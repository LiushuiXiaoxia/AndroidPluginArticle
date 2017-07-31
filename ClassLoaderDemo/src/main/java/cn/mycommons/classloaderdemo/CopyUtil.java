package cn.mycommons.classloaderdemo;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * CopyUtil <br/>
 * Created by xiaqiulei on 2017-07-31.
 */
public class CopyUtil {

    public static void copy(@NonNull InputStream inputStream, @NonNull File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}