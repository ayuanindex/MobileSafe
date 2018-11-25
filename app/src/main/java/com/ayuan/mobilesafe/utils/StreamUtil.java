package com.ayuan.mobilesafe.utils;

import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtil {
	/**
	 * @param inputStream 流对象
	 * @return 流转换成的字符串；返回NULL代表发生异常
	 */
	@Nullable
	public static String StreamToString(InputStream inputStream) {
		//1.在读取的过程中，将读取额内容存储在缓存中然后一次性的转换成字符串返回
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		//2.读取流数据的操作，读到没有为止
		byte[] bytes = new byte[1024];
		//3.记录读取内容的临时变量
		int len = -1;
		try {
			while ((len = inputStream.read(bytes)) != -1) {
				byteArrayOutputStream.write(bytes, 0, len);
			}
			//返回读取的数据
			return byteArrayOutputStream.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {//关闭流的操作
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
