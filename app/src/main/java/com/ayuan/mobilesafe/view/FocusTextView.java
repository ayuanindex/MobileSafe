package com.ayuan.mobilesafe.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author ayuan
 * 能够获取焦点的自定义TexiView
 */
@SuppressLint("AppCompatCustomView")
public class FocusTextView extends TextView {
	/**
	 * 使用在通过java代码创建的控件
	 *
	 * @param context 上下文
	 */
	public FocusTextView(Context context) {
		super(context);
	}

	/**
	 * 由系统调用
	 *
	 * @param context 上下文
	 * @param attrs   属性集合
	 */
	public FocusTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 由系统调用的构造方法
	 *
	 * @param context      上下文
	 * @param attrs        属性集合
	 * @param defStyleAttr 布局文件中定义的样式主题(style.xml)
	 */
	public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 重写获取焦点的方法,由系统调用，调用的时候默认就能获取焦点
	 *
	 * @return TRUE表示能够过去焦点
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
}
