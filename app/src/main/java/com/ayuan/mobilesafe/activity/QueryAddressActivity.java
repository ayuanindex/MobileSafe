package com.ayuan.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayuan.mobilesafe.engine.AddressDao;

/**
 * 电话归属地查询界面
 */
public class QueryAddressActivity extends AppCompatActivity {

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					if (tv_attribution != null) {
						String address = (String) msg.obj;
						tv_attribution.setText(address);
					}
					break;
			}
		}
	};
	private EditText et_phone;
	private TextView tv_attribution;
	private Button btn_query;
	private Button btn_clear;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_attribution = (TextView) findViewById(R.id.tv_attribution);
		btn_query = (Button) findViewById(R.id.btn_query);
		btn_clear = (Button) findViewById(R.id.btn_clear);

		/*点击进行的查询操作*/
		btn_query.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String phone = et_phone.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					//抖动效果
					anim((View) et_phone);
					et_phone.setHint("请输入号码");
					tv_attribution.setText("");
					et_phone.setHintTextColor(Color.RED);
					return;
				}
				//查询是耗时操作，需要开启子线程
				query(phone);
			}
		});

		//实时查询（监听输入框文本内容的变化）
		et_phone.addTextChangedListener(new TextWatcher() {
			/***
			 * 文本改变之前调用的方法
			 * @param s
			 * @param start
			 * @param count
			 * @param after
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			/**
			 * 当文本发生改变的时候的方法
			 * @param s
			 * @param start
			 * @param before
			 * @param count
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			/**
			 * 文本改变之后调用的方法
			 * @param s
			 */
			@Override
			public void afterTextChanged(Editable s) {
				if (et_phone != null) {
					String phone = et_phone.getText().toString().trim();
					if (!TextUtils.isEmpty(phone)) {
						query(phone);
					}
				}
			}
		});

		/*清空输入框的按钮*/
		btn_clear.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				/*左右晃动的效果*/
				anim((View) et_phone);
				et_phone.setHint("请输入需要查询的号码");
				et_phone.setHintTextColor(R.color.hintColor);
				et_phone.setText("");
				tv_attribution.setText("");
			}
		});
	}

	/**
	 * 获取电话号码归属地的方法
	 * 耗时操作
	 *
	 * @param phone 需要查询的号码
	 */
	private void query(final String phone) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				String address = AddressDao.getAddress(phone);
				//消息机制，告知主线程查询结束，可以去使用查询结果
				Message message = Message.obtain();
				message.what = 1;
				message.obj = address;
				mHandler.sendMessage(message);
			}
		}.start();
	}

	/**
	 * 动画效果展示
	 *
	 * @param view 传入的控件
	 */
	private void anim(View view) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		//interpolator插补器，数学函数
		translateAnimation.setInterpolator(new CycleInterpolator(3));
		translateAnimation.setRepeatMode(Animation.REVERSE);
		translateAnimation.setDuration(500);
		view.startAnimation(translateAnimation);
	}
}
