package com.ayuan.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

        //获取联系人号码进行回显
        String number = SpUtils.getString(this, ConstantValue.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(number)) {
            et_phone_number.setText(number);
        }

        //为按钮设置点击事件
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);
        btn_select_number.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     *
     * @param v
     */
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
                if (data != null) {
                    String number = data.getStringExtra("number");
                    if (number != null) {
                        if (et_phone_number != null) {
                            et_phone_number.setText(number);
                            //存储安全联系人
                            SpUtils.putString(this, ConstantValue.CONTACT_PHONE, number);
                        }
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
            //点击按钮以后，需要获取输入框中的联系人，再做下一页的操作
            String number = et_phone_number.getText().toString().trim();
            if (!TextUtils.isEmpty(number)) {
                SpUtils.putString(this, ConstantValue.CONTACT_PHONE, number);
                Intent intent = new Intent(getApplicationContext(), SetupFourActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
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
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);
        finish();
    }

}
