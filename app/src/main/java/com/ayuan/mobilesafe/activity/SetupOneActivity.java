package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 导航界面1
 */
public class SetupOneActivity extends AppCompatActivity {
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_one);

		Button btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到第二个导航界面
				Intent intent = new Intent(SetupOneActivity.this, SetupTwoActivity.class);
				startActivity(intent);
				//开启平移动画
				finish();
				overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
			}
		});

		//滑动切换
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				//e1 起始点
				//e2 抬起点
				if (e1.getRawX() - e2.getRawX() > 100) {
					//跳转到下一页、
					startActivity(new Intent(SetupOneActivity.this, SetupTwoActivity.class));
					finish();
					overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
				}
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
