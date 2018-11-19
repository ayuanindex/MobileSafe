package com.ayuan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayuan.mobilesafe.activity.R;

public class SettingItemView extends RelativeLayout {
    private String TAG = "SettingItemView";
    private CheckBox cb_box;
    private TextView tv_des;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->View  将设置界面的一个条目转换成View对象
        //root:代表是否将布局文件放置到此类中去，传入的参数是需要挂在类的对象，this表示当前类
        View inflate = View.inflate(context, R.layout.setting_item_view, this);
        /*//也可以用下面的方法
        View inflate1 = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(inflate1);*/
        //获取自定义组合控件里面的控件
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        boolean checked = cb_box.isChecked();
        if (checked) {
            tv_des.setText("自动更新已开启");
        } else {
            tv_des.setText("自动更新已关闭");
        }
        /*this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断 条目是否被选中，如果被cb_box已经被选中，则选中条目是将其设置为未选中，若本来为开启，反之亦然
                boolean flag = isChecked() ? false : true;
                setCheck(flag);
                Log.i(TAG, "状态:" + cb_box.isChecked());
            }
        });*/
        /*boolean checked = cb_box.isChecked();
        if (checked) {
            tv_des.setText("自动更新已开启");
        }*/
    }

    /**
     * 判断是否开启的方法
     *
     * @return 返回当前SettingItemView是否选中状态  TRUE 开启（CheckBox--true）  FALSE关闭（CheckBox---false）
     */
    public boolean isChecked() {
        //由CheckBox的结果，决定当前条目是否开启
        return cb_box.isChecked();
    }

    /**
     * @param isChecked 是否作为开启的变量，由点击过程中去做传递
     */
    public void setCheck(boolean isChecked) {
        //当前条目在 选择的过程中，cb_box的选中状态也跟随（isChecked）变化
        cb_box.setChecked(isChecked);
        if (isChecked) {
            //开启状态
            tv_des.setText("自动更新已开启");
        } else {
            //关闭状态
            tv_des.setText("自动更新已关闭");
        }
    }
}
