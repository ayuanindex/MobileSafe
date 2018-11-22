package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 这是第一个导航界面
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
                Intent intent = new Intent(SetupOneActivity.this, SetupTwoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
