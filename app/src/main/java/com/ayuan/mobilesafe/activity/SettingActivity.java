package com.ayuan.mobilesafe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.ayuan.mobilesafe.service.AddressService;
import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.ServiceUtils;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.view.SettingClickView;
import com.ayuan.mobilesafe.view.SettingItemView;

/**
 * 设置界面
 */
public class SettingActivity extends AppCompatActivity {

	private String TAG = "SettingActivity";
	private GestureDetector gestureDetector;
	private SettingClickView scv_toast_style;
	private SettingClickView scv_location;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		initUI();
		initUpdate();
		initAddress();
		initToastStyle();
		initLocation();
	}

	/**
	 * 双击居中View所在屏幕位置的处理方法
	 */
	private void initLocation() {
		scv_location = (SettingClickView) findViewById(R.id.scv_location);
		scv_location.setTitle("归属地提示框的位置");
		scv_location.setDes("设置归属地提示框的位置");
		scv_location.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ToastLocationActivity.class));
			}
		});
	}


	/**
	 * 初始化UI
	 */
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
	 * 更新的开关
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
		boolean addressService = ServiceUtils.isRunnning(this, "com.ayuan.mobilesafe.service.AddressService");
		siv_address.setCheck(addressService);
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
			}
		});
	}

	/**
	 * 设置号码归属地显示风格
	 */
	private void initToastStyle() {
		scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
		//1.创建描述文字所在的数组
		final String[] toastStyleDes = {"透明", "橙色", "蓝色", "灰色", "绿色"};
		//2.通过Sp获取Toast显示样式的索引值，用于描述文字
		final int toast_style = SpUtils.getInt(SettingActivity.this, ConstantValue.TOAST_STYLE, 0);
		//3.通过索引，获取字符串中的文字，将其显示在控件上
		scv_toast_style.setTitle("设置归属地显示风格");
		scv_toast_style.setDes(toastStyleDes[toast_style]);
		//4.监听点击事件，弹出对话框
		scv_toast_style.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//5.显示Toast样式的对话框
				showToastStyleDialog(toastStyleDes);
			}
		});
	}

	/**
	 * 创建Toast选择样式的对话框
	 *
	 * @param toastStyleDes 对话框需要显示的数据列表（数组）
	 */
	private void showToastStyleDialog(final String[] toastStyleDes) {
		final int toast_style = SpUtils.getInt(SettingActivity.this, ConstantValue.TOAST_STYLE, 0);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.mipmap.mobilesafe);
		builder.setTitle("请选择样式");
		//单个选择条目的事件监听(items：String类型的数组，checkedItem：已经存在的索引，listener：点击条目的事件监听)
		builder.setSingleChoiceItems(toastStyleDes, toast_style, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (scv_toast_style != null) {
					scv_toast_style.setDes(toastStyleDes[which]);
					SpUtils.putInt(SettingActivity.this, ConstantValue.TOAST_STYLE, which);
					dialog.dismiss();
				}
			}
		});

		//取消条目的事件
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (scv_toast_style != null) {
					scv_toast_style.setDes(toastStyleDes[toast_style]);
				}
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
