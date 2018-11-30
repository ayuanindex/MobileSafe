package com.ayuan.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
						tv_toast.setLayoutParams(layoutParams);
					}
					break;
			}
		}
	};

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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//销毁Toast，取消对电话状态的监听
		if (myPhoneStateListener != null && mTelephonyManager != null) {
			mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
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
					Log.i(TAG, "现在是空闲状状态");
					//挂断电话的时候窗体需要去移除Toast
					if (mToastInflate != null && mWindowManager != null) {
						mWindowManager.removeView(mToastInflate);
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					//摘机状态（接听电话）
					Log.i(TAG, "现在是摘机状态");
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					//响铃的状态（在响铃是展示Toast）
					Log.i(TAG, "现在是响铃状态");
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
		//在窗体上挂载一个View(需要添加权限)
		mWindowManager.addView(mToastInflate, params);
		query(phoneNumber);
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
