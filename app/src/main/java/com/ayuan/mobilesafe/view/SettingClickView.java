package com.ayuan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayuan.mobilesafe.activity.R;

public class SettingClickView extends RelativeLayout {
	private String TAG = "SettingItemView";
	private TextView tv_des;
	private TextView tv_title;

	public SettingClickView(Context context) {
		this(context, null);
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//xml--->View  将设置界面的一个条目转换成View对象
		//root:代表是否将布局文件放置到此类中去，传入的参数是需要挂在类的对象，this表示当前类
		View inflate = View.inflate(context, R.layout.setting_click_view, this);
		//获取自定义组合控件里面的控件
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
	}

	/**
	 * 设置标题的内容
	 *
	 * @param title 需要显示的内容
	 */
	public void setTitle(String title) {
		if (tv_title != null) {
			tv_title.setText(title);
		}
	}

	/**
	 * 设置描述内容
	 *
	 * @param des
	 */
	public void setDes(String des) {
		if (tv_des != null) {
			tv_des.setText(des);
		}
	}
}
