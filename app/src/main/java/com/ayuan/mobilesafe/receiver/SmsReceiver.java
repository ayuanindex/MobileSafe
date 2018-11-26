package com.ayuan.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.ayuan.mobilesafe.activity.R;
import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//1.判断是否开启了防盗保护
		boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);
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
				if (messageBody.contains("#*alarm*#")) {
					//9.播放音乐(准备音乐，MediaPlay)
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
					//10.设置无限循环播放音乐
					mediaPlayer.setLooping(true);
					//11.开始播放音乐
					mediaPlayer.start();
				}
			}
		}
	}
}
