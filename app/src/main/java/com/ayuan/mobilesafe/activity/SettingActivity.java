package com.ayuan.mobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前事选中的，点击过后变成未选中的状态
                boolean flag = siv_update.isChecked();
                siv_update.setCheck(!flag);
                Log.i(TAG, "状态:" + siv_update.isChecked());
            }
        });
    }
}
