package com.ayuan.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static SharedPreferences sharedPreferences;

    /**
     * 从sp中读取boolean的表示
     *
     * @param context 调用者传入的上下文环境
     * @param key     存储节点的名称
     * @param value   存储节点的Boolea值
     */
    public static void putBoolean(Context context, String key, boolean value) {
        //参数列表：存储文件节点的名称，读写方式（模式）
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 写入boolean的标识至sp中
     *
     * @param context  调用者传入的上下文环境
     * @param key      节点的名称
     * @param defValue 没有节点时的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 存储字符串的方法
     *
     * @param context 上下文
     * @param key     节点名称
     * @param value   存储节点的值String
     */
    public static void putString(Context context, String key, String value) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 写入boolean的标识至sp中
     *
     * @param context  调用者传入的上下文环境
     * @param key      节点的名称
     * @param defValue 没有节点时的默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context context, String key, String defValue) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(key, defValue);
    }
}
