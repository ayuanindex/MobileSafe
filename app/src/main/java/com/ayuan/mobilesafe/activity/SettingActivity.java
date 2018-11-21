package com.ayuan.mobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.view.SettingItemView;


public class SettingActivity extends AppCompatActivity {

    private String TAG = "SettingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    /**
     * 初始化UI
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //获取已有的卡关状态进行显示
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前事选中的，点击过后变成未选中的状态
                boolean flag = siv_update.isChecked();
                siv_update.setCheck(!flag);
                //将取反后的状态存储到相应的sp中
                SpUtils.putBoolean(SettingActivity.this, ConstantValue.OPEN_UPDATE, !flag);
            }
        });
    }
}
