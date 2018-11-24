package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;

/**
 * 导航界面4
 */
public class SetupFourActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_next;
    private Button btn_previous;
    private CheckBox cb_box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_four);

        initUI();
    }

    private void initUI() {
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //回显是否选中的状态
        boolean status = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        //回显checkbox的状态
        cb_box.setChecked(status);
        //根据状态，修改checkbox后续文字的显示
        if (status) {
            cb_box.setText("安全设置已开启");
        } else {
            cb_box.setText("安全设置已关闭");
        }
        //点击过程中，监听选中状态发生改变
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked：点击后的状态
                //存储点击后的状态
                SpUtils.putBoolean(SetupFourActivity.this, ConstantValue.OPEN_SECURITY, isChecked);
                if (isChecked) {
                    cb_box.setText("安全设置已开启");
                } else {
                    cb_box.setText("安全设置已关闭");
                }
            }
        });

        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                //跳转到下一页
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
        boolean checked = cb_box.isChecked();
        if (checked) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
            finish();
        } else {
            ToastUtil.showShort(this, "请开启防盗保护");
        }
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
