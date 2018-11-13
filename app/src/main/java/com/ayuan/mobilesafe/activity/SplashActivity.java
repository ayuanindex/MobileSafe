package com.ayuan.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    private TextView tv_version_name;
    private int mLoaclVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();
        //初始化数据
        initData();
    }

    /**
     * 获取数据的方法
     */
    private void initData() {
        //1.获取版本名称
        if (tv_version_name != null) {
            tv_version_name.setText("版本名称" + getVersionName());
        }
        //检测(本地版本号和服务断版本号)是否有更新，如果有更新，提示用户下载
        //2.获取本地版本号
        mLoaclVersionCode = getmLoaclVersionCode();
        //3.获取服务器版本号（客户端发送请求，服务器给出相应（json，xml））
        //http://172.29.255.67:8080/update53.json?key=value   服务器返回200代表请求成功，以流的方式传输过来
        /*json中内容包含：
            1.更新版本的版本名称
            2.版本描述信息
            3.服务器版本号
            4.新版本apk的下载地址
         */
        checkVersion();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //URL url = new URL("http://172.29.255.67:8080/updata.json");
                    //1.封装url地址
                    URL url = new URL("http://10.0.2.2:8080/update.json");//仅限于模拟器访问电脑上的tomcat
                    //2.开启一个链接
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //3.设置创建的请求参数（请求头）
                    httpURLConnection.setConnectTimeout(2000);//连接超时时间
                    httpURLConnection.setReadTimeout(2000);//读取超时时间
                    httpURLConnection.setRequestMethod("GET");//请求方式
                    //4.获取响应码
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        //5.以流的形式，将数据下载下来
                        InputStream inputStream = httpURLConnection.getInputStream();
                        //6.将流转为字符串
                        String jsonToString = StreamUtil.StreamToString(inputStream);
                        Log.i(TAG, "json:" + jsonToString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 获取版本号的方法：清单文件中
     *
     * @return 应用版本号，返回值为0代表出现异常
     */
    private int getmLoaclVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称的方法：清单文件中
     *
     * @return 应用版本名称，返回NULL代表有异常出现
     */
    private String getVersionName() {
        //1.包管理者对象packageManger
        PackageManager packageManager = getPackageManager();
        try {
            //2.从包管理者对象中，获取指定包名的基本信息（版本名称，版本号）,第二个参数传入0表示获取基本信息
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            //3.拿到版本名称
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI方法
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }
}
