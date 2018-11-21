package com.ayuan.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

public class HomeActivity extends AppCompatActivity {

    private String TAG = "HomeActivity";
    private GridView gv_home;
    private String[] mTitleString;
    private int[] mMipmapIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化UI
        initUI();
        //初始化数据的方法
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //准备数据（文字九组，图片九张）
        mTitleString = new String[]{
                "手机防盗", "通信卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"
        };
        mMipmapIds = new int[]{
                R.mipmap.home_safe, R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
                R.mipmap.home_taskmanager, R.mipmap.home_netmanager, R.mipmap.home_trojan,
                R.mipmap.home_sysoptimize, R.mipmap.home_tools, R.mipmap.home_settings
        };

        //为GridView控件设置数据
        if (gv_home != null) {
            gv_home.setAdapter(new MyAdapter());
            //为九宫格单个条目设置点击事件
            gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            //手机防盗
                            //开启对话框
                            showDialog();
                            break;
                        case 1:
                            //通信卫士
                            break;
                        case 2:
                            //软件管理
                            break;
                        case 3:
                            //进程管理
                            break;
                        case 4:
                            //流量统计
                            break;
                        case 5:
                            //手机杀毒
                            break;
                        case 6:
                            //缓存清理
                            break;
                        case 7:
                            //高级工具
                            break;
                        case 8:
                            //设置中心
                            enterSetting();
                            break;
                    }
                }
            });
        }
    }

    /**
     * 手机防盗对话框判断显示
     */
    private void showDialog() {
        //判断本地是否有存储密码(sp String）
        String mobile_safe_password = SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PASSWORD, "");
        //TextUtils.isEmpty字符串为空时返回true
        if (TextUtils.isEmpty(mobile_safe_password)) {
            //1.初始设置密码的对话框
            showSetPasswordDialog();
        } else {
            //2.确认密码的对话框
            showConfirmPasswordDialog();
        }
    }

    /**
     * 弹出确认密码的对话框
     */
    private void showConfirmPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View inflate = View.inflate(this, R.layout.dialog_confrim_password, null);
        alertDialog.setView(inflate);
        alertDialog.show();
        Button btn_submit = (Button) inflate.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) inflate.findViewById(R.id.btn_cancel);
        //为确认按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取得对话框输入的密码
                EditText et_set_password = (EditText) inflate.findViewById(R.id.et_set_password);
                String set_password = et_set_password.getText().toString().trim();
                //从文件中获取密码
                String repassword = SpUtils.getString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PASSWORD, "");
                if (TextUtils.isEmpty(set_password)) {
                    //为输入框设置提示内容
                    et_set_password.setHint("请输入密码");
                    //将提示内容设置为红色
                    et_set_password.setHintTextColor(Color.RED);
                    //为输入框设置动效（左右晃动）
                    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
                    translateAnimation.setInterpolator(new CycleInterpolator(5));
                    translateAnimation.setDuration(200);
                    et_set_password.startAnimation(translateAnimation);
                    return;
                } else if (!set_password.equals(repassword)) {
                    //将输入框置为空值
                    et_set_password.setText("");
                    //为输入框设置提示内容
                    et_set_password.setHint("密码错误");
                    et_set_password.setHintTextColor(Color.RED);
                    //为输入框设置动效（左右晃动）
                    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
                    translateAnimation.setInterpolator(new CycleInterpolator(5));
                    translateAnimation.setDuration(200);
                    et_set_password.startAnimation(translateAnimation);
                    return;
                } else {
                    //密码输入正确
                    Intent intent = new Intent(getApplicationContext(), TestActvity.class);
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            }
        });

        //为取消按钮设置点击事件
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 弹出设置密码的对话框
     */
    private void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //这是自己创建对话框，不是调用默认的对话框
        final AlertDialog alertDialog = builder.create();
        //因为需要去自己定义对话框的展示样式，所以需要调用setView（）方法
        final View inflate = View.inflate(this, R.layout.dialog_set_password, null);
        //让对话框显示自己定义的view
        alertDialog.setView(inflate);
        alertDialog.show();
        Button btn_submit = (Button) inflate.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        //为确认按钮设置点击事件
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_password = (EditText) inflate.findViewById(R.id.et_set_password);
                EditText et_confirm_password = (EditText) inflate.findViewById(R.id.et_confirm_password);
                String setPassword = et_set_password.getText().toString().trim();
                String confirmPassword = et_confirm_password.getText().toString().trim();
                if (!TextUtils.isEmpty(setPassword) && !TextUtils.isEmpty(confirmPassword) && setPassword.equals(confirmPassword)) {
                    Log.i(TAG, "成功开启页面");
                    //将设置好的密码存起来
                    SpUtils.putString(HomeActivity.this, ConstantValue.MOBILE_SAFE_PASSWORD, setPassword);
                    //进入应用手机防盗模块
                    Intent intent = new Intent(getApplicationContext(), TestActvity.class);
                    startActivity(intent);
                    //跳转到新的界面以后需要将AlertDialog销毁
                    alertDialog.dismiss();
                } else if (TextUtils.isEmpty(confirmPassword) && TextUtils.isEmpty(setPassword)) {
                    et_set_password.setHint("请输入密码");
                    et_confirm_password.setHint("确认密码");
                    et_confirm_password.setHintTextColor(Color.RED);
                    et_set_password.setHintTextColor(Color.RED);
                    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
                    translateAnimation.setInterpolator(new CycleInterpolator(5));
                    translateAnimation.setDuration(200);
                    et_confirm_password.startAnimation(translateAnimation);
                    et_set_password.startAnimation(translateAnimation);
                    return;
                } else {
                    //设置新提示
                    et_confirm_password.setHint("两次密码输入不一致");
                    //将确认密码输入设置为空值
                    et_confirm_password.setText("");
                    et_confirm_password.setHintTextColor(Color.RED);
                    Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
                    translateAnimation.setInterpolator(new CycleInterpolator(5));
                    translateAnimation.setDuration(200);
                    et_confirm_password.startAnimation(translateAnimation);
                    return;
                }
            }
        });
        //为取消按钮设置点击事件
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //进入设置界面
    private void enterSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    private class MyAdapter extends BaseAdapter {
        /**
         * 显示条目的数量
         *
         * @return 返回值为条目的总数(文字组数 = 图片的张数)
         */
        @Override
        public int getCount() {
            return mTitleString.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleString[position];
        }

        @Override
        public long getItemId(int position) {
            return position;//返回选中的索引id
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(HomeActivity.this, R.layout.gridview_item, null);
            } else {
                view = convertView;
            }
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            iv_icon.setImageResource(mMipmapIds[position]);
            tv_title.setText(mTitleString[position]);
            return view;
        }
    }
}
