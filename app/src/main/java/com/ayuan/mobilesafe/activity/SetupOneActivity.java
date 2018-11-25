package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 导航界面1
 */
public class SetupOneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_one);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到第二个导航界面
                Intent intent = new Intent(SetupOneActivity.this, SetupTwoActivity.class);
                startActivity(intent);
                LinearLayout ll_one = (LinearLayout) findViewById(R.id.ll_one);
                //开启平移动画
                finish();
                SystemClock.sleep(200);
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
            }
        });
    }
}
