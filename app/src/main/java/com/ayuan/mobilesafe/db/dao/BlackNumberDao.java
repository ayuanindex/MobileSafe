package com.ayuan.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ayuan.mobilesafe.db.BlackNumberOpenHelper;
import com.ayuan.mobilesafe.db.domain.BlackNumberInfo;

import java.util.ArrayList;

/**
 * 对黑名单表的增删改查操作
 */
public class BlackNumberDao {
	private final BlackNumberOpenHelper blackNumberOpenHelper;
	//BlackNumberDao单例模式
	//1.私有化构造方法

	private BlackNumberDao(Context context) {
		//创建数据库以及表结构
		blackNumberOpenHelper = new BlackNumberOpenHelper(context, "blacknumber.db", null, 1);
	}

	//2.声明一个当前类的对象
	private static BlackNumberDao blackNumberDao = null;

	/**
	 * 3.提供一个静态方法，如果但前类的对象为空，创建一个新的
	 *
	 * @param context 传入的上下文环境
	 * @return 返回一个当前类的对象
	 */
	public static BlackNumberDao getInstance(Context context) {
		if (blackNumberDao == null) {
			blackNumberDao = new BlackNumberDao(context);
		}
		return blackNumberDao;
	}

	/**
	 * 在数据库中添加一条记录
	 *  @param phone 需要拦截的号码
	 * @param mode  拦截类型(1——》短信  2——》电话  3——》拦截所有(短信 + 电话))
     */
	public void insert(String phone, int mode) {
		//1.开启数据库，准备做写入操作
		SQLiteDatabase database = blackNumberOpenHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("phone", phone);
		contentValues.put("mode", mode);
		database.insert("blacknumber", null, contentValues);
		database.close();
	}

	/**
	 * 从数据库中删除一条电话号码的方法
	 *
	 * @param phone 需要删除的号码
	 */
	public void delete(String phone) {
		SQLiteDatabase database = blackNumberOpenHelper.getWritableDatabase();
		database.delete("blacknumber", "phone=?", new String[]{phone});
		database.close();
	}

	/**
	 * 根据电话号码来更新拦截模式
	 *
	 * @param phone 更新拦截模式的电话号码
	 * @param mode  需要更新的模式
	 */
	public void update(String phone, String mode) {
		SQLiteDatabase database = blackNumberOpenHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		database.update("blacknumber", contentValues, "phone=?", new String[]{phone});
		database.close();
	}

	/**
	 * @return 查询到数据库中所有的号码以及拦截类型所在的集合
	 */
	public ArrayList<BlackNumberInfo> findAll() {
		SQLiteDatabase database = blackNumberOpenHelper.getWritableDatabase();
		Cursor query = database.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
		ArrayList<BlackNumberInfo> blackNumberInfos = null;
		if (query.getCount() > 0) {
			blackNumberInfos = new ArrayList<BlackNumberInfo>();
			while (query.moveToNext()) {
				String phone = query.getString(0);
				String mode = query.getString(1);
				BlackNumberInfo blackNumberInfo = new BlackNumberInfo(phone, mode);
				blackNumberInfos.add(blackNumberInfo);
			}
			query.close();
			database.close();
			return blackNumberInfos;
		}
		return blackNumberInfos;
	}
}
