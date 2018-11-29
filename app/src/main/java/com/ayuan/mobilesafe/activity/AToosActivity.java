package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * 高级工具------>归属地查询
 *
 * @author ayuan
 */
public class AToosActivity extends AppCompatActivity {

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);

		initUI();
		//电话归属地查询
		initPhoneAddress();
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
					overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});


	}

	/**
	 * 电话归属地查询方法
	 */
	private void initPhoneAddress() {
		TextView viewById = (TextView) findViewById(R.id.tv_query_phone_address);
		viewById.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), QueryAddressActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
