package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;

public class SetupOverActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_over);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		TextView tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
		ImageView iv_lock = (ImageView) findViewById(R.id.iv_lock);
		RelativeLayout rl_afresh = (RelativeLayout) findViewById(R.id.rl_afresh);

		//从sp中取得安全号码
		String safe_number = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
		//从Sp中取得防盗开关的状态
		boolean sercurity = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
		//将安全号码回显到控件上
		tv_safe_number.setText(safe_number);
		//将防盗保护的状态回显到控件上
		iv_lock.setImageResource(R.mipmap.lock);

		//给重置按钮设置点击事件
		rl_afresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SpUtils.remove(SetupOverActivity.this, ConstantValue.CONTACT_PHONE);
				SpUtils.remove(SetupOverActivity.this, ConstantValue.OPEN_SECURITY);
				SpUtils.remove(SetupOverActivity.this, ConstantValue.SIM_NUMBER);
				SpUtils.remove(SetupOverActivity.this, ConstantValue.SETUP_OVER);
				Intent intent = new Intent(SetupOverActivity.this, SetupOneActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
				finish();
			}
		});
	}
}
