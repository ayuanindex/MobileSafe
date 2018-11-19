package com.ayuan.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static SharedPreferences settingValue;

    /**
     * 从sp中读取boolean的表示
     *
     * @param context 调用者传入的上下文环境
     * @param key     存储节点的名称
     * @param value   存储节点的Boolea值l
     */
    public static void putBoolean(Context context, String key, boolean value) {
        //参数列表：存储文件节点的名称，读写方式（模式）
        if (settingValue == null) {
            settingValue = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = settingValue.edit();
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
        if (settingValue == null) {
            settingValue = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return settingValue.getBoolean(key, defValue);
    }
}
