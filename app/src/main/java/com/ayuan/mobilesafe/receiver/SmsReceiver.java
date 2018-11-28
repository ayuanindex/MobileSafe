package com.ayuan.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.ayuan.mobilesafe.activity.R;
import com.ayuan.mobilesafe.service.LocationService;
import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

public class SmsReceiver extends BroadcastReceiver {
	private ComponentName componentName;

	@Override
	public void onReceive(Context context, Intent intent) {
		//1.判断是否开启了防盗保护
		boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
		//获取管理者对象
		DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName componentName = new ComponentName(context.getApplicationContext(), DeviceAdmin.class);
		if (open_security) {
			//2.获取短信内容
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object o : pdus) {
				//4.获取短信对象
				SmsMessage fromPdu = SmsMessage.createFromPdu((byte[]) o);
				//5.获取短信对象的基本信息
				String address = fromPdu.getOriginatingAddress();
				//6.获取短信内容
				String messageBody = fromPdu.getMessageBody();
				//8.判断是否包含播放音乐的关键字
				//----------------------------------------------------接受到短信进行判断执行哪个功能

				//播放报警音乐
				if (messageBody.contains("#*alarm*#")) {
					//9.播放音乐(准备音乐，MediaPlay)
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
					//10.设置无限循环播放音乐
					mediaPlayer.setLooping(true);
					//11.开始播放音乐
					mediaPlayer.start();
				}

				//获取手机经纬度
				if (messageBody.contains("#*location*#")) {
					//12.开启获取位置的服务
					Intent service = new Intent(context, LocationService.class);
					context.startService(service);
				}

				//锁屏并设置密码
				if (messageBody.contains("#*lockscreen*#")) {
					if (devicePolicyManager.isAdminActive(componentName)) {
						//如果设备管理器开启的话就可以执行锁屏
						devicePolicyManager.lockNow();
						devicePolicyManager.resetPassword("", 0);
					}
				}

				//清除手机数据
				if (messageBody.contains("#*wipedata*#")) {
					if (devicePolicyManager.isAdminActive(componentName)) {
						//falgs传入参数0代表清除所有数据(运行在模拟其中就是重新启动电脑)
						devicePolicyManager.wipeData(0);
					}
				}
			}
		}
	}
}
