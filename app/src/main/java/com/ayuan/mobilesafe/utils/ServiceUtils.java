package com.ayuan.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.List;

public class ServiceUtils {

	/**
	 * 判单服务是否正在运行
	 *
	 * @param context     上下文环境
	 * @param serviceName 服务的名称
	 * @return 返回TRUE代表服务正在运行   返回FALSE表示服务已经关闭
	 */
	public static boolean isRunnning(Context context, String serviceName) {
		//1.获取activityManager管理者对象(可以获取当前手机正在运行的所有的服务)
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//2.获取 手机中正在运行的服务
		List<ActivityManager.RunningServiceInfo> runningServicesCount = mActivityManager.getRunningServices(100);
		//3.遍历获取的所有的服务集合，拿到每一个服务的类的名称，和传递进来的类名做对比，如果一致说明服务正在运行
		Iterator<ActivityManager.RunningServiceInfo> iterable = runningServicesCount.iterator();
		while (iterable.hasNext()) {
			ActivityManager.RunningServiceInfo serviceInfo = iterable.next();
			String className = serviceInfo.service.getClassName();
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
