package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ayuan.mobilesafe.view.SettingItemView;

/**
 * 导航界面2
 */
public class SetupTwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_two);

        Button btn_next = (Button) findViewById(R.id.btn_next);
        Button btn_previous = (Button) findViewById(R.id.btn_previous);

        //返回上一页的按钮
        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //前往下一页的按钮
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupTwoActivity.this, SetupThreeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //这是绑定手机卡的按钮
        final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = siv_sim_bound.isChecked();
                siv_sim_bound.setCheck(!checked);
            }
        });
    }
}
