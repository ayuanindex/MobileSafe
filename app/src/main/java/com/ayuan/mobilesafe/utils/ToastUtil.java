package com.ayuan.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    /**
     * 打印吐司（Toast)
     * 短时间
     *
     * @param context 传入上下文
     * @param message 传入需要显示的消息
     */
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 打印吐司(Toast)
     * 长时间
     *
     * @param context 传入上下文
     * @param message 传入需要显示的消息
     */
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
