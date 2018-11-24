package com.ayuan.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;
import com.ayuan.mobilesafe.view.SettingItemView;

/**
 * 导航界面2
 */
public class SetupTwoActivity extends AppCompatActivity {

    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_two);

        initUI();

        Button btn_next = (Button) findViewById(R.id.btn_next);
        Button btn_previous = (Button) findViewById(R.id.btn_previous);

        //返回上一页的按钮
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupTwoActivity.this, SetupOneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //前往下一页的按钮
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sim_number = SpUtils.getString(SetupTwoActivity.this, ConstantValue.SIM_NUMBER, "");
                if (!TextUtils.isEmpty(sim_number)) {
                    //跳转到第三个导航界面
                    Intent intent = new Intent(SetupTwoActivity.this, SetupThreeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showShort(SetupTwoActivity.this, "请绑定SIM卡");
                }
            }
        });
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //1.回显(读取已有的绑定状态,用作显示,Sp中是否存储了SIM卡的序列号)
        String sim_number = SpUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        //2.判断序列号是否为空
        if (TextUtils.isEmpty(sim_number)) {
            //如果为空则将其设置为未选中
            siv_sim_bound.setCheck(false);
        } else {
            //如果不为空则设置为选中
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3.获取当前条目原有的状态
                boolean checked = siv_sim_bound.isChecked();
                //4.将原有的状态去反，状态设置给当前条目
                siv_sim_bound.setCheck(!checked);
                if (!checked) {
                    //5.存储SIM卡序列号
                    //5.1.获取SIM卡序列号
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") String simSerialNumber = telephonyManager.getSimSerialNumber();
                    //5.2.存储序列号
                    SpUtils.putString(SetupTwoActivity.this, ConstantValue.SIM_NUMBER, simSerialNumber);
                    ToastUtil.showShort(SetupTwoActivity.this, "SIM卡已绑定");
                } else {
                    //6.将存储的SIM卡号的节点，从sp中删除掉
                    SpUtils.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                    ToastUtil.showShort(SetupTwoActivity.this, "SIM已解绑");
                }
            }
        });
    }
}
