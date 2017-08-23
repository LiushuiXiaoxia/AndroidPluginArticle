package cn.mycommons.replugindemobase;

/**
 * Instance <br/>
 * Created by xiaqiulei on 2017-08-23.
 */
public class Instance {

    private static Object object;

    public static Object getObject() {
        return object;
    }

    public static void setObject(Object object) {
        Instance.object = object;
    }
}