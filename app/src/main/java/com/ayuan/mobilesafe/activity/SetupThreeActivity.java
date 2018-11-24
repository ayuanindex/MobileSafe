package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ayuan.mobilesafe.utils.ConstantValue;
import com.ayuan.mobilesafe.utils.SpUtils;
import com.ayuan.mobilesafe.utils.ToastUtil;

/**
 * 导航界面3
 */
public class SetupThreeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CONTACT = 0;
    private static final String TAG = "SetupThreeActivity";
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

        String number = SpUtils.getString(this, ConstantValue.SECURITY_NUMBER, "");
        if (!TextUtils.isEmpty(number)) {
            et_phone_number.setText(number);
        }

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
        switch (resultCode) {
            case CONTACT:
                String number = data.getStringExtra("number");
                if (number != null) {
                    if (et_phone_number != null) {
                        et_phone_number.setText(number);
                    }
                }
                break;
        }
    }

    /**
     * 跳转到下一个页面
     */
    private void nextJump() {
        if (et_phone_number != null) {
            String number = et_phone_number.getText().toString().trim();
            if (!TextUtils.isEmpty(number)) {
                SpUtils.putString(this, ConstantValue.SECURITY_NUMBER, number);
                Intent intent = new Intent(getApplicationContext(), SetupFourActivity.class);
                startActivity(intent);
                finish();
            } else {
                ToastUtil.showShort(this, "请选择联系人");
            }
        }
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
