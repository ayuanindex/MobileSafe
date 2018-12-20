package com.ayuan.mobilesafe.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ayuan.mobilesafe.db.dao.BlackNumberDao;
import com.ayuan.mobilesafe.db.domain.BlackNumberInfo;
import com.ayuan.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;

public class BlackNumberActivity extends AppCompatActivity {

    private static final String TAG = "BlackNumberActivity";
    private ImageView iv_add;
    private ListView lv_blacknumber;
    private BlackNumberDao mBlackNumberDao;
    private ArrayList<BlackNumberInfo> mBlackNumberInfos;
    private BlackNumberAdapter mBlackNumberAdapter;
    private int mode;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //4.告知ListView可以去设置数据适配器
                    mBlackNumberAdapter = new BlackNumberAdapter();
                    lv_blacknumber.setAdapter(mBlackNumberAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);

        initUI();
        initData();
    }

    private void initUI() {
        iv_add = (ImageView) findViewById(R.id.iv_add);
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(BlackNumberActivity.this, "正在添加");
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View inflate = View.inflate(this, R.layout.dialog_add_blacknumber, null);
        alertDialog.setView(inflate, 0, 0, 0, 0);

        final EditText et_phone = (EditText) inflate.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup) inflate.findViewById(R.id.rg_group);
        Button btn_submit = (Button) inflate.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        //监听其选中条目的切换过程
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sms:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取输入框中的电话号码
                String phone = et_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    //2.数据库的插入当前输入的拦截号码
                    if (mBlackNumberDao == null) {
                        mBlackNumberDao = BlackNumberDao.getInstance(BlackNumberActivity.this);
                    }
                    mBlackNumberDao.insert(phone, mode);
                    //3.让数据库和集合保持同步(1.数据库中数据重新读一遍,2.手动向集合中添加一个对象(插入数据构建的对象))
                    BlackNumberInfo blackNumberInfo = new BlackNumberInfo(phone, String.valueOf(mode));
                    //4.将对象插入到集合的最顶部
                    mBlackNumberInfos.add(0, blackNumberInfo);
                    //5.通知数据适配器刷新(数据适配器中的数据有改变了)
                    if (mBlackNumberAdapter != null) {
                        mBlackNumberAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }
                } else {
                    Toast.makeText(BlackNumberActivity.this, "请输入需要拦截的号码", Toast.LENGTH_SHORT).show();
                    TranslateAnimation translateAnimation = new TranslateAnimation(-10, 10, 0, 0);
                    translateAnimation.setDuration(50);
                    translateAnimation.setRepeatCount(5);
                    translateAnimation.setRepeatMode(Animation.REVERSE);
                    et_phone.startAnimation(translateAnimation);
                }
            }
        });

        alertDialog.show();
    }

    private void initData() {
        //获取数据库中所有的电话号码
        new Thread() {
            @Override
            public void run() {
                super.run();
                //1.获取操作黑名单数据库的对                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       象
                mBlackNumberDao = BlackNumberDao.getInstance(BlackNumberActivity.this);
                //2.查询所有的方法
                mBlackNumberInfos = mBlackNumberDao.findAll();
                if (mBlackNumberInfos != null) {
                    Log.i(TAG, "哈哈执行到我了");
                    //3.通过消息机制告知主线程可以去使用包含数据的集合
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else {
                    Log.i(TAG, "哈哈:数据库里面没有数据");
                }
            }
        }.start();
    }

    private class BlackNumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mBlackNumberInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mBlackNumberInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(BlackNumberActivity.this, R.layout.listview_blacknumber_item, null);
            } else {
                view = convertView;
            }
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

            tv_phone.setText(mBlackNumberInfos.get(position).getPhone());
            switch (mBlackNumberInfos.get(position).getMode()) {
                case "1":
                    tv_mode.setText("拦截短信");
                    break;
                case "2":
                    tv_mode.setText("拦截电话");
                    break;
                case "3":
                    tv_mode.setText("拦截所有");
                    break;
            }
            return view;
        }
    }
}
