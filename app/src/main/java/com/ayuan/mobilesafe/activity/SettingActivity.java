package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ayuan.mobilesafe.service.AddressService;
import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.view.SettingItemView;

/**
 * 设置界面
 */
public class SettingActivity extends AppCompatActivity {

	private String TAG = "SettingActivity";
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initUI();
		initUpdate();
		initAddress();
	}


	private void initUI() {
		//滑动切换
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				//e1 起始点
				//e2 抬起点
				if (e2.getX() - e1.getX() > 100) {
					//上一页
					startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					finish();
					overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * 初始化UI
	 */
	private void initUpdate() {
		final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
		//获取已有的卡关状态进行显示
		boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
		//是否选中根据上一次存储的结果去做决定
		siv_update.setCheck(open_update);
		siv_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//如果之前事选中的，点击过后变成未选中的状态
				boolean flag = siv_update.isChecked();
				siv_update.setCheck(!flag);
				//将取反后的状态存储到相应的sp中
				SpUtils.putBoolean(SettingActivity.this, ConstantValue.OPEN_UPDATE, !flag);
			}
		});
	}

	/**
	 * 是否显示电话号码归属地的方法
	 */
	private void initAddress() {
		final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
		//点击过程中状态(是否开启电话号码归属地)的切换
		siv_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = siv_address.isChecked();
				siv_address.setCheck(!checked);
				if (!checked) {
					//开启服务，管理土司
					startService(new Intent(SettingActivity.this, AddressService.class));
				} else {
					//关闭服务,不需要显示土司
					stopService(new Intent(SettingActivity.this, AddressService.class));
				}
				//SpUtils.putBoolean(SettingActivity.this, ConstantValue.open_);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
