package com.ayuan.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    @org.junit.Test
    public void encryption() {
        String password = "123" + "encoder";
        encoder(password);
    }

    /**
     * 将字符串加密
     *
     * @param password 需要加密的字符
     */
    private void encoder(String password) {
        //指定加密的算法类型
        try {
            //1.指定算法加密类型
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //2.将需要加密的字符串转换成byte类型的数组，然后进行随机hash算法过程
            byte[] digest = md5.digest(password.getBytes());
            //定义StringBuffer去拼接得到的字符串
            StringBuffer buffer = new StringBuffer("");
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
            //5.打印测试
            System.out.printf(buffer.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.printf("没有此算法");
            e.printStackTrace();
        }
    }
}
