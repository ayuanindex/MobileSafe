package com.ayuan.mobilesafe.utils;

import android.util.Log;

import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static String MD5 = "MD5";//表示进行md5算法
    private static String TAG = "MD5Utils";

    /**
     * 将字符串加密(默认使用md5算法
     *
     * @param string 需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String encoder(String string) {
        StringBuffer buffer = new StringBuffer("");
        try {
            //1.指定加密算法类型
            MessageDigest instance = MessageDigest.getInstance(MD5);
            //2.将需要加密的字符串转换成byte类型的数组，然后进行随机hash算法过程
            byte[] digest = instance.digest((string + "encoder").getBytes());
            //3.循环遍历digest，然后让其生成32位的字符串
            for (byte b : digest) {
                int i = b & 0xff;//固定写法
                //int类型的i需要转换成16进制的字符
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "算法类型出现错误");
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    /**
     * 将字符串加密(自选算法)
     *
     * @param string    需要加密的字符串
     * @param algorithm 需要的算法
     * @return 返回加密过后的字符串
     */
    public static String encoder(String string, String algorithm) {
        StringBuffer buffer = new StringBuffer("");
        try {
            //1.指定加密算法类型
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            //2.将需要加密的字符串转换成byte类型的数组，然后进行随机hash算法过程
            byte[] digest = instance.digest((string + "encoder").getBytes());
            //3.循环遍历digest，然后让其生成32位的字符串
            for (byte b : digest) {
                int i = b & 0xff;//固定写法
                //int类型的i需要转换成16进制的字符
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "算法类型出现错误");
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }
}
