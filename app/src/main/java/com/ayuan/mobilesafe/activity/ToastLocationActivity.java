package com.ayuan.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

/**
 * 设置来电归属地悬浮Toast显示的位置
 */
public class ToastLocationActivity extends AppCompatActivity {

	private ImageView iv_drag;
	private Button btn_top;
	private Button btn_bottom;
	private String TAG = "ToastLocationActivity";
	private WindowManager mWindowManager;
	private long startTime = 0;
	private long[] mHits = new long[2];

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);

		initUI();
	}

	/**
	 * 初始化UI
	 */
	@SuppressLint("NewApi")
	private void initUI() {
		//可拖拽双击居中图片的控件
		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		btn_top = (Button) findViewById(R.id.btn_top);
		btn_bottom = (Button) findViewById(R.id.btn_bottom);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = mWindowManager.getDefaultDisplay();
		int screenWidth = defaultDisplay.getWidth();//屏幕宽度
		int screenHeight = defaultDisplay.getHeight();//屏幕高度

		int locationX = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		int locationY = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
		//左上角坐标作用在iv_drag上
		//ImageView在相对布局中，所以其所在位置的规则需要由相对布局提供
		//指定宽高都是warp_content
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//将左上角的坐标作用在iv_drag对应规则参数上
		layoutParams.leftMargin = locationX;
		layoutParams.topMargin = locationY;
		//将以上规则作用在iv_drag上
		iv_drag.setLayoutParams(layoutParams);

		if (locationY > (screenHeight / 2) - 70) {
			if (btn_top != null && btn_bottom != null) {
				btn_top.setVisibility(View.VISIBLE);//显示顶部控件
				btn_bottom.setVisibility(View.INVISIBLE);//隐藏底部控件
			}
		} else {
			if (btn_top != null && btn_bottom != null) {
				btn_top.setVisibility(View.INVISIBLE);
				btn_bottom.setVisibility(View.VISIBLE);
			}
		}

		iv_drag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[mHits.length - 1] - mHits[0] < 500) {
					Toast.makeText(ToastLocationActivity.this, "Hah", Toast.LENGTH_SHORT).show();
				}
			}
		});


		//监听某一个控件的拖拽过程(按下，移动，抬起)
		iv_drag.setOnTouchListener(new View.OnTouchListener() {
			private int startX;
			private int startY;

			//对不同的事件做不同的逻辑处理
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Display defaultDisplay = mWindowManager.getDefaultDisplay();
				int screenWidth = defaultDisplay.getWidth();//屏幕宽度
				int screenHeight = defaultDisplay.getHeight();//屏幕高度

				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						//每次移动一点就去一次值进行偏移量的计算
						int moveX = (int) event.getRawX();
						int moveY = (int) event.getRawY();
						//计算偏移量
						int disX = moveX - startX;
						int disY = moveY - startY;
						//1.找到当前控件所在屏幕上的位置(坐标)
						int left = iv_drag.getLeft() + disX;//距离屏幕左边缘的距离
						int top = iv_drag.getTop() + disY;//距离屏幕上边缘的距离
						int right = iv_drag.getRight() + disX;//距离屏幕右边的距离
						int bottom = iv_drag.getBottom() + disY;//距离图片下边的距离
						//容错处理(iv_drag不能拖拽出手机屏幕)
						if (left < 0 || top < 0 || right > screenWidth || bottom > screenHeight) {
							return true;
						}
						if (bottom > screenHeight - 50) {
							iv_drag.layout(left, top, right, screenHeight - 50);
							break;
						}
						if (top > (screenHeight / 2) - 70) {
							if (btn_top != null && btn_bottom != null) {
								btn_top.setVisibility(View.VISIBLE);//显示顶部控件
								btn_bottom.setVisibility(View.INVISIBLE);//隐藏底部控件
							}
						} else {
							if (btn_top != null && btn_bottom != null) {
								btn_top.setVisibility(View.INVISIBLE);
								btn_bottom.setVisibility(View.VISIBLE);
							}
						}
						//2.告知移动的控件，按照计算出来的坐标去做展示
						iv_drag.layout(left, top, right, bottom);
						//3.重置起始坐标
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						//4.记录拖拽后的控件的坐标位置
						SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
						SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
						break;
				}
				return true;/*一定一定要记得把此值设为true*/
			}
		});
	}
}
