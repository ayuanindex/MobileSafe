package com.ayuan.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayuan.mobilesafe.activity.R;

public class SettingItemView extends RelativeLayout {
    private String NAMESPACE = "http://schemas.android.com/apk/com.ayuan.mobilesafe";
    private String TAG = "SettingItemView";
    private CheckBox cb_box;
    private TextView tv_des;
    private TextView tv_title;
    private String mDesoff;
    private String mDeson;
    private String mDestitle;

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
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //获取自定义以及原声属性的操作，写在此处，AttributeSet attrs对象中去获取
        initAttrs(attrs);
        tv_title.setText(mDestitle);
        tv_des.setText(mDesoff);
    }

    /**
     * 返回属性集合中自定义的属性值
     *
     * @param attrs 构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs) {
        /*//获取属性的总个数
        int attrCount = attrs.getAttributeCount();
        //获取属性名称以及属性值
        for (int i = 0; i < attrCount; i++) {
            //获取属性名称
            String attributeName = attrs.getAttributeName(i);
            //获取属性值
            String attributeValue = attrs.getAttributeValue(i);
            Log.i(TAG, "attributeName:" + attributeName + "---->attributeValue:" + attributeValue);
        }*/
        //通过命名空间+属性名称获取属性值
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
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
            tv_des.setText(mDeson);
        } else {
            //关闭状态
            tv_des.setText(mDesoff);
        }
    }
}
