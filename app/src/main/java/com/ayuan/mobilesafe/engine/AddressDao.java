package com.ayuan.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.service.autofill.RegexValidator;
import android.util.Log;

import java.util.regex.Pattern;


public class AddressDao {
	private static final String TAG = "AddressDao";
	//1.指定访问数据库的路径
	public static String path = "/data/data/com.ayuan.mobilesafe/files/address.db";
	private static String mNnknownNumber = "未知号码";

	/**
	 * 开启数据库连接，进行访问
	 * 传递一个号码，开启数据库连接返回一个归属地
	 *
	 * @param phoneNumber 需要查询的号码
	 */
	public static String getAddress(String phoneNumber) {
		//使用正则表达式匹配手机号码
		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		boolean matches = phoneNumber.matches("^1[3-8]\\d{9}");
		if (matches) {
			phoneNumber = phoneNumber.substring(0, 7);
			//2.开启数据库连接(使用只读方式打开数据库)
			//3.执行数据库的查询操作
			Cursor data1 = sqLiteDatabase.query("data1", new String[]{"outkey"}, "id=?", new String[]{phoneNumber}, null, null, null);
			//4.查到即可
			if (data1.getCount() >= 1 && data1.moveToNext()) {
				String outkey = data1.getString(0);
				//5.通过data1返回的结果，作为外键查询data2表
				Cursor data2 = sqLiteDatabase.query("data2", new String[]{"location"}, "id=?", new String[]{outkey}, null, null, null);
				if (data2.getCount() >= 1) {
					while (data2.moveToNext()) {
						//6.获取查询到的电话归属地
						String location = data2.getString(0);
						return location;
					}
				}
			}
		} else {
			int length = phoneNumber.length();
			switch (length) {
				case 3:
					return "紧急电话";
				case 4:
					return "模拟器";
				case 5:
					return "服务电话";
				case 7:
					return "本地电话";
				case 11:
					//(3+8)区号+座机号(外地)，查询data2
					String area = phoneNumber.substring(1, 3);
					Cursor data2 = sqLiteDatabase.query("data2", new String[]{"location"}, "area=?", new String[]{area}, null, null, null);
					if (data2.getCount() >= 1 && data2.moveToNext()) {
						String location = data2.getString(0);
						return location;
					}
					break;
				case 12:
					//(4+8)区号+座机号码
					String area1 = phoneNumber.substring(1, 4);
					Cursor data21 = sqLiteDatabase.query("data2", new String[]{"location"}, "area=?", new String[]{area1}, null, null, null);
					if (data21.getCount() >= 1 && data21.moveToNext()) {
						String location = data21.getString(0);
						return location;
					}
					break;
			}
		}
		return mNnknownNumber;
	}
}
