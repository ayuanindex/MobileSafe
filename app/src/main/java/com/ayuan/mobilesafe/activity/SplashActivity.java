package com.ayuan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.StreamUtil;
import com.ayuan.mobilesafe.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    /**
     * 更新版本的状态码----100
     */
    private static final int UPDATE_VERSION = 100;
    /**
     * 进入应用程序的状态码-----101
     */
    private static final int ENTER_HOME = 101;
    /**
     * url地址出现异常-------102
     */
    private static final int URL_ERROR = 102;
    /**
     * io流出现异常-------103
     */
    private static final int IO_ERROR = 103;
    /**
     * json解析异常
     */
    private static final int JSON_ERROR = 104;
    private String TAG = "SplashActivity";
    private TextView tv_version_name;
    private int mLoaclVersionCode;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.what;
            switch (code) {
                case UPDATE_VERSION:
                    //弹出对话框提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面
                    enterHome();
                    break;
                case URL_ERROR:
                    //弹出Toast提示Url异常
                    ToastUtil.showShort(SplashActivity.this, "url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    //弹出Toast提示IO流异常
                    ToastUtil.showShort(SplashActivity.this, "读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    //弹出Toast提示json解析异常
                    ToastUtil.showShort(SplashActivity.this, "json解析异常");
                    enterHome();
                    break;
            }
        }
    };

    private String mVersionName;
    private String mVersionDes;
    private String mVersionCode;
    private String mDownloadUrl;
    private RelativeLayout rl_root;

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
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框，是依赖于Activity存在的
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置左上角图标
        builder.setIcon(android.R.drawable.stat_sys_warning);
        builder.setTitle("版本更新");
        //设置描述内容
        builder.setMessage(mVersionDes);
        //消极按钮
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
            }
        });
        //积极按钮，立即更新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里执行更新应用程序的代码逻辑(下载apk)
                downloadApk(mDownloadUrl);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadApk(String url) {
        //apk下载地址，防止apk的所在路径
        //1.判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2.sd卡存储路径
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mobilesafe.apk";
            //3.发送请求，获取apk，并且放置到指定的路径下
            HttpUtils httpUtils = new HttpUtils();
            /**
             * 发送请求，传递参数
             * @param url 下载地址
             * @param target 存放的目标地址
             * @param callback
             */
            httpUtils.download(url, path, new RequestCallBack<File>() {
                //刚开始下载时的方法
                @Override
                public void onStart() {
                    super.onStart();
                    Log.i(TAG, "刚刚开始下载");
                }

                /**
                 * 下载过程中的方法
                 * @param total 下载文件的总大小
                 * @param current 当前的下载位置
                 * @param isUploading 是否正在下载
                 */
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.i(TAG, "下载中……");
                    Log.i(TAG, "total:" + total);
                    Log.i(TAG, "current:" + current);
                    Log.i(TAG, "isUploading:" + isUploading);
                }

                /**
                 * 下载成功
                 * @param responseInfo
                 */
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载过后放置在sd卡中的文件
                    Log.i(TAG, "下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    if (file != null) {
                        installApk(file);
                    }
                }

                /**
                 * 下载失败
                 * @param e
                 * @param s
                 */
                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(TAG, "下载失败");
                }
            });
        }
    }

    /**
     * 安装apk的方法
     *
     * @param file 需要安装的文件
     */
    private void installApk(File file) {
        //调用系统应用来安装apk，用Intent来进行页面跳转进行安装
        ToastUtil.showShort(this, "正在安装请稍后");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*//使用文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装的类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 进入应用程序的主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面后,将导航界面关闭掉(导航界面只可见一次)
        finish();//此方法会将Activity移除任务栈
    }

    /**
     * 获取数据的方法
     */
    private void initData() {
        //1.获取版本名称
        if (tv_version_name != null) {
            tv_version_name.setText("版本名称" + getmVersionName());
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
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            //如果设置中已经开启自动更新,则去比对版本号进行更新提示
            checkVersion();
        } else {
            //如果没有开启自动更新，则显示开屏动画后再进入主界面
            //延时3s进入应用程序主界面
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 3000);
        }
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //URL url = new URL("http://172.29.255.67:8080/update.json");
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
                        String json = StreamUtil.StreamToString(inputStream);
                        //7.json的解析
                        JSONObject jsonObject = new JSONObject(json);
                        //解析版本名称
                        mVersionName = jsonObject.getString("versionName");
                        //解析版本描述
                        mVersionDes = jsonObject.getString("versionDes");
                        //解析版本号
                        mVersionCode = jsonObject.getString("versionCode");
                        //解析下载地址
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        //8.比对版本号(服务器版本号>本地版本号，提示用户更新)
                        if (mLoaclVersionCode != 0 && mLoaclVersionCode < Integer.parseInt(mVersionCode)) {
                            //9.1提示用户更新(弹出对话框)
                            message.what = UPDATE_VERSION;
                        } else {
                            //9.2进入应用程序主界面
                            message.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = JSON_ERROR;
                } finally {
                    //指定睡眠时间,请求网络的时长超过 4s 则不做处理
                    //请求时间小于4s，强制让其睡眠满4s
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        long sleepTime = 4000 - (endTime - startTime);
                        try {
                            Thread.sleep(sleepTime);
                            if (mHandler != null) {
                                mHandler.sendMessage(message);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
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
    private String getmVersionName() {
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
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        rl_root.startAnimation(alphaAnimation);
    }
}
