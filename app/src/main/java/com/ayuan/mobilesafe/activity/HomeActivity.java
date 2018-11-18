package com.ayuan.mobilesafe.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private String TAG = "HomeActivity";
    private GridView gv_home;
    private String[] mTitleString;
    private int[] mMipmapIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //初始化UI
        initUI();
        //初始化数据的方法
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //准备数据（文字九组，图片九张）
        mTitleString = new String[]{
                "手机防盗", "通信卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"
        };
        mMipmapIds = new int[]{
                R.mipmap.home_safe, R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
                R.mipmap.home_taskmanager, R.mipmap.home_netmanager, R.mipmap.home_trojan,
                R.mipmap.home_sysoptimize, R.mipmap.home_tools, R.mipmap.home_settings
        };

        //为GridView控件设置数据
        if (gv_home != null) {
            gv_home.setAdapter(new MyAdapter());
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    private class MyAdapter extends BaseAdapter {
        /**
         * 显示条目的数量
         *
         * @return 返回值为条目的总数(文字组数 = 图片的张数)
         */
        @Override
        public int getCount() {
            return mTitleString.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleString[position];
        }

        @Override
        public long getItemId(int position) {
            return position;//返回选中的索引id
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(HomeActivity.this, R.layout.gridview_item, null);
            } else {
                view = convertView;
            }
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_icon.setImageResource(mMipmapIds[position]);
            tv_title.setText(mTitleString[position]);
            return view;
        }
    }
}
