package com.ayuan.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class AddressService extends Service {

	private String TAG = "AddressService";
	private TelephonyManager mTelephonyManager;
	private MyPhoneStateListener myPhoneStateListener;

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
					//空闲状态，没有任何活动
					Log.i(TAG, "现在是空闲状状态");
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

	private void showToast(String phoneNumber) {
		Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
	}
}
