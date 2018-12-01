package com.ayuan.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ayuan.mobilesafe.activity.R;
import com.ayuan.mobilesafe.engine.AddressDao;
import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

public class AddressService extends Service {

	private String TAG = "AddressService";
	private TelephonyManager mTelephonyManager;
	private MyPhoneStateListener myPhoneStateListener;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View mToastInflate;
	private WindowManager mWindowManager;
	private int mScreenWidth;
	private int mScreenHeight;
	private TextView tv_toast;
	private int[] toastStyles = new int[]{
			R.drawable.call_locate_white,
			R.drawable.call_locate_orange,
			R.drawable.call_locate_blue,
			R.drawable.call_locate_gray,
			R.drawable.call_locate_green
	};//存储图片所在对的索引

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
				case 0:
					if (mToastInflate != null && tv_toast != null) {
						String address = (String) msg.obj;
						int toastStyleIndex = SpUtils.getInt(AddressService.this, ConstantValue.TOAST_STYLE, 0);
						tv_toast.setBackgroundResource(toastStyles[toastStyleIndex]);
						tv_toast.setText(address);
					}
					break;
			}
		}
	};
	private InnerOutCallReceiver mInnerOutCallReceiver;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//管理Toast的显示，关闭的时候电话状态就不需要监听（在有电话拨打进来的时候才可以进行管理）
		//1.拿到电话管理者对象
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		//2.监听电话状态
		mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		//获取窗口管理者对象
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = mWindowManager.getDefaultDisplay();
		//屏幕宽度
		mScreenWidth = defaultDisplay.getWidth();
		//屏幕高度
		mScreenHeight = defaultDisplay.getHeight();

		//监听播出电话的广播接收者
		IntentFilter intentFilter = new IntentFilter();
		//监听的过滤条件
		intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		//创建相应的广播接收者
		mInnerOutCallReceiver = new InnerOutCallReceiver();
		registerReceiver(mInnerOutCallReceiver, intentFilter);//注册广播接收者
	}

	private class InnerOutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			//接收到广播之后需要显示自定义的Toast,显示播出号码的归属地
			//获取播出电话号码的字符串
			String phone = getResultData();
			showToast(phone);//将号码传给弹出Toast的方法，让其把号码显示在控件上
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//销毁Toast，取消对电话状态的监听
		if (myPhoneStateListener != null && mTelephonyManager != null) {
			mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		if (mInnerOutCallReceiver != null) {
			unregisterReceiver(mInnerOutCallReceiver);
		}
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		//3.电话状态发生改变会触发的方法
		@Override
		public void onCallStateChanged(int state, String phoneNumber) {
			super.onCallStateChanged(state, phoneNumber);
			switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					//空闲状态，没有任何活动(移除Toast)
					//挂断电话的时候窗体需要去移除Toast
					if (mToastInflate != null && mWindowManager != null) {
						mWindowManager.removeView(mToastInflate);
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					//摘机状态（接听电话）
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					//响铃的状态（在响铃是展示Toast）
					showToast(phoneNumber);
					break;
			}
		}
	}

	/**
	 * 弹出Toast的方法
	 *
	 * @param phoneNumber Toast上需要显示的内容
	 */
	private void showToast(String phoneNumber) {
		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		//在响铃的时候显示,和电话类型一致
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON/*让屏幕开启的时候显示Toast*/
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;/*不能获取焦点*/
		//指定Toast的位置
		params.gravity = Gravity.LEFT + Gravity.TOP;//(指定显示在左上角)
		//Toast显示效果(Toast的布局文件),xml----->view（Toast），将Toast挂载到windowManager窗体上
		mToastInflate = View.inflate(getApplicationContext(), R.layout.toast_view, null);
		//找到需要显示内容的控件
		tv_toast = (TextView) mToastInflate.findViewById(R.id.tv_toast);
		int locationX = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
		int locationY = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);
		params.x = locationX;
		params.y = locationY;
		//在窗体上挂载一个View(需要添加权限)
		mWindowManager.addView(mToastInflate, params);
		query(phoneNumber);

		tv_toast.setOnTouchListener(new View.OnTouchListener() {
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
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
						//2.告知移动的控件，按照计算出来的坐标去做展示
						params.x = disX + params.x;
						params.y = disY + params.y;
						//容错处理
						if (params.x < 0) {
							params.x = 0;
						}

						if (params.y < 0) {
							params.y = 0;
						}

						if (params.x > mScreenWidth - mToastInflate.getWidth()) {
							params.x = mScreenWidth - mToastInflate.getWidth();
						}

						if (params.y > mScreenHeight - mToastInflate.getHeight()) {
							params.y = mScreenHeight - mToastInflate.getHeight();
						}

						//告知窗体Toast需要按照手势的移动进行移动
						mWindowManager.updateViewLayout(mToastInflate, params);
						//3.重置起始坐标
						startX = (int) event.getRawX();
						startY = (int) event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						SpUtils.putInt(AddressService.this, ConstantValue.LOCATION_X, params.x);
						SpUtils.putInt(AddressService.this, ConstantValue.LOCATION_Y, params.y);
						break;
				}
				//既要相应点击事件，又要响应拖拽过程，结果需要修改为false
				return true;/*一定一定要记得把此值设为true*/
			}
		});
	}

	//电话号码的查询操作
	private void query(final String phoneNumber) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				String address = AddressDao.getAddress(phoneNumber);
				Message message = Message.obtain();
				message.obj = address;
				message.what = 0;
				mHandler.sendMessage(message);
			}
		}.start();
	}
}
