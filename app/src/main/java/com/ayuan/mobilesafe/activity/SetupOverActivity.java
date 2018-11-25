package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;

public class SetupOverActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
		if (setup_over) {
			//密码输入验证成功，并且四个导航界面设置完成------》才能跳转到设置完成的功能列表界面
			setContentView(R.layout.activity_setup_over);//这是加载
		} else {
			//密码输入验证成功，如果四个导航界面没有设置完成---》则跳转到第一个导航界面
			Intent intent = new Intent(getApplicationContext(), SetupOneActivity.class);
			startActivity(intent);
			//开启了一个新的界面以后，关闭功能列表界面
			finish();
		}

		TextView viewById = (TextView) findViewById(R.id.tv_reselt);
		viewById.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showShort(SetupOverActivity.this, "你好我是你爸爸");
			}
		});
	}
}
