package com.ayuan.mobilesafe.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

public class LocationService extends Service {
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 服务第一次创建的时候调用
	 */
	@SuppressLint("MissingPermission")
	@Override
	public void onCreate() {
		super.onCreate();
		//获取位置管理者对象
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		//以最优的方式获取经纬度坐标
		Criteria criteria = new Criteria();
		//允许花费
		criteria.setCostAllowed(true);
		//指定获取经纬度的精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//获取最优的定位方式
		String bestProvider = locationManager.getBestProvider(criteria, true);
		/**
		 * 在一定事件间隔，移动一定距离后获取经纬度坐标
		 * 通过locationManager获取经纬度坐标
		 *
		 * provider:定位方式(可指定为：卫星定位，基站定位，网络定位)
		 * minTime:获取经纬度坐标的最小间隔时间
		 * minDistance:移动的最小间距
		 * listener:位置的监听
		 */
		locationManager.requestLocationUpdates(bestProvider, 10000, 0, new MyLocationListener());
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			//纬度
			double latitude = location.getLatitude();
			//经度
			double longitude = location.getLongitude();
			//拿到安全联系人号码
			String contect_phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
			//获取后发送短信(内容为经纬度)
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(contect_phone, null, "经度:" + longitude + ",纬度:" + latitude, null, null);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
