package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 导航界面3
 */
public class SetupThreeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CONTACT = 0;
    private Button btn_next;
    private Button btn_previous;
    private Button btn_select_number;
    private EditText et_phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_three);

        initUI();
    }

    private void initUI() {
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        btn_select_number = (Button) findViewById(R.id.btn_select_number);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);

        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_select_number.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                //跳转到下一页
                nextJump();
                break;
            case R.id.btn_previous:
                //跳转到上一页
                previousJump();
                break;
            case R.id.btn_select_number:
                //选择号码按钮
                selectNumber();
                break;
        }
    }

    /**
     * 点击按钮选择联系人
     */
    private void selectNumber() {
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivityForResult(intent, CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CONTACT:
                //这里取得联系人列表返回的联系人号码和姓名信息
                break;
        }
    }

    /**
     * 跳转到下一个页面
     */
    private void nextJump() {
        Intent intent = new Intent(getApplicationContext(), SetupFourActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到上一个页面
     */
    private void previousJump() {
        Intent intent = new Intent(getApplicationContext(), SetupTwoActivity.class);
        startActivity(intent);
        finish();
    }

}
