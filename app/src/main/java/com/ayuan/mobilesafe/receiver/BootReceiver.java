package com.ayuan.mobilesafe.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;

public class BootReceiver extends BroadcastReceiver {
	private String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		//一旦监听到开机广播，就需要发送短信给指定的安全号码;
		//1.获取本地存储的SIM卡序列号
		String spSimSerialNumber = SpUtils.getString(context, ConstantValue.SIM_NUMBER, "");
		//2.获取当前插入手机sim卡的序列号(Telephoney)
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		@SuppressLint("MissingPermission") String simSerialNumber = telephonyManager.getSimSerialNumber();
		//3.将两个序列卡号进行比对
		if (!spSimSerialNumber.equals(simSerialNumber + "123")) {
			//4.如果两者不相同名则发送短信给安全号码
			//获取发送对象的号码
			String contactPhone = SpUtils.getString(context, ConstantValue.CONTACT_PHONE, "");
			SmsManager smsManager = SmsManager.getDefault();
			if (!TextUtils.isEmpty(contactPhone)) {
				//destinationAddress:是需要发送的号码；sentIntent:发送短信之后的Intent    deliveryIntent:接收到短信之后的意图
				smsManager.sendTextMessage(contactPhone, null, "您的sim卡被更换", null, null);
			}
			return;
		}
	}
}
