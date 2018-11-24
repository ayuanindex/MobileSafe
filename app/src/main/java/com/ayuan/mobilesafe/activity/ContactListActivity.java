package com.ayuan.mobilesafe.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = "ContactListActivity";
    private ListView lv_contact;

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
                //循环游标直到没有数据为止
                while (query.moveToNext() && query.getCount() >= 1) {
                    String contact_id = query.getString(0);
                    Log.i(TAG, "contact_id:" + contact_id);
                }
            }
        }.start();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setAdapter(new ContactAdapter());
    }

    /**
     * ListView的数据适配器
     */
    private class ContactAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
