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
		edit.putBoolean(key, value).commit();
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
			sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		SharedPreferences.Editor edit = sharedPreferences.edit();
		edit.putString(key, value).commit();
	}

	/**
	 * 从Sp中读取字符串
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

	/**
	 * 将int类型的至存储之sp中
	 *
	 * @param context 调用者传入的上下文环境
	 * @param key     节点的名称
	 * @param value   存储节点的值int
	 */
	public static void putInt(Context context, String key, int value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, value).commit();
	}

	/**
	 * 从Sp中读取int类型的数据
	 *
	 * @param context  调用者传入的上下文环境
	 * @param key      节点的名称
	 * @param defValue 没有节点时的默认值
	 * @return 默认值或者此节点读取到的结果
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * 从sp中移除指定节点
	 *
	 * @param context 上下文
	 * @param node    需要删除的节点名称
	 */
	public static void remove(Context context, String node) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		}
		SharedPreferences.Editor edit = sharedPreferences.edit();
		edit.remove(node).commit();
	}
}
