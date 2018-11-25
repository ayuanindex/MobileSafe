package com.ayuan.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListActivity extends AppCompatActivity {

	private static final String TAG = "ContactListActivity";
	private ListView lv_contact;
	private List<Map<String, String>> contactList = new ArrayList<Map<String, String>>();
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
				case 1:
					if (lv_contact != null) {
						ContactAdapter contactAdapter = (ContactAdapter) msg.obj;
						//填充数据适配器
						lv_contact.setAdapter(contactAdapter);
					}
					break;
			}
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		initUI();
		initData();

	}

	/**
	 * 获取联系人数据的方法
	 */
	private void initData() {
		//获取数据为耗时操作，必须开子线程
		new Thread() {

			@Override
			public void run() {
				super.run();
				//获取内容提供者
				ContentResolver contentResolver = getContentResolver();
                /*1) content：//com.android.contacts/raw_contacts
                2) content：//com.android.contacts/data*/
				Uri raw_contacts = Uri.parse("content://com.android.contacts/raw_contacts");
				Uri data = Uri.parse("content://com.android.contacts/data");
				//从数据库里查询数据（读取联系人权限）
				Cursor query = contentResolver.query(raw_contacts, new String[]{"contact_id"}/*需要查询的字段*/, null/*查询条件*/, null/*查询条件的值*/, null/*排序方式*/);
				contactList.clear();
				//循环游标直到没有数据为止
				while (query.moveToNext() && query.getCount() >= 1) {
					String contact_id = query.getString(0);
					if (TextUtils.isEmpty(contact_id)) {
						continue;
					}
					//根据用户唯一性id值，查询data表
					Cursor dataQuery = contentResolver.query(data, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{contact_id}, null);
					HashMap<String, String> map = new HashMap<>();
					while (dataQuery.moveToNext() && dataQuery.getCount() >= 1) {
						//循环获取每一个联系人的电话号码以及数据类型
						String dataString = dataQuery.getString(0);
						String mimetypeString = dataQuery.getString(1);
						if (mimetypeString.equals("vnd.android.cursor.item/name")) {
							if (!TextUtils.isEmpty(dataString)) {
								map.put("name", dataString);
							}
						} else if (mimetypeString.equals("vnd.android.cursor.item/phone_v2")) {
							if (!TextUtils.isEmpty(dataString)) {
								map.put("number", dataString);
							}
						}
					}
					dataQuery.close();
					contactList.add(map);
				}
				query.close();
				//使用消息机制对数据适配器进行设置（设置数据适配器也是更新UI的操作所以不能够再子线程里面进行操作）
				Message message = Message.obtain();
				message.obj = new ContactAdapter();
				message.what = 1;
				handler.sendMessage(message);
			}
		}.start();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//获取点中条目的索引只想集合中的对象
				Map<String, String> contact = contactList.get(position);
				//获取当前条目指向集合中对应的电话号码
				String contactNumber = contact.get("number");
				Intent intent = new Intent(ContactListActivity.this, SetupThreeActivity.class);
				//将特殊字符过滤下来
				contactNumber = contactNumber.replace("-", "").replace(" ", "").trim();
				intent.putExtra("number", contactNumber);
				setResult(0, intent);
				finish();
			}
		});
	}

	/**
	 * ListView的数据适配器
	 */
	private class ContactAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return contactList.size();
		}

		@Override
		public Map<String, String> getItem(int position) {
			return contactList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(ContactListActivity.this, R.layout.listview_contact_item, null);
			} else {
				view = convertView;
			}
			Map<String, String> contact = getItem(position);
			String contactName = contact.get("name");
			String contactNumber = contact.get("number");
			TextView tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_contact_number = (TextView) view.findViewById(R.id.tv_contact_number);
			tv_contact_name.setText(contactName);
			tv_contact_number.setText(contactNumber);
			return view;
		}
	}
}
