package com.ayuan.mobilesafe.activity;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ContactListActivity extends AppCompatActivity {

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
        //获取内容提供者
        ContentResolver contentResolver = getContentResolver();
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
