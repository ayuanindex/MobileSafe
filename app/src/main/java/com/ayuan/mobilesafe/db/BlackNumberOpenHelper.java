package com.ayuan.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberOpenHelper extends SQLiteOpenHelper {

	public BlackNumberOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 创建数据库中表的方法
	 *
	 * @param db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlString = "CREATE TABLE blacknumber(_id integer PRIMARY KEY AUTOINCREMENT,phone varchar(20),mode varchar(5));";
		db.execSQL(sqlString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
