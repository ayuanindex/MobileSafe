package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;

/**
 * 导航界面4
 */
public class SetupFourActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_next;
    private Button btn_previous;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_four);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_previous = (Button) findViewById(R.id.btn_previous);

        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                nextJump();
                break;
            case R.id.btn_previous:
                previousJump();
                break;
        }
    }

    /**
     * 跳转到下一个页面
     */
    private void nextJump() {
        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
        startActivity(intent);
        finish();
        SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
    }

    /**
     * 跳转到上一个页面
     */
    private void previousJump() {
        Intent intent = new Intent(getApplicationContext(), SetupThreeActivity.class);
        startActivity(intent);
        finish();
    }


}
