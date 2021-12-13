package com.wx.wxdemo.util;

import java.security.MessageDigest;

public class SHA1 {
    /**
     * 获取sha1签名
     *
     * @return
     * @throws Exception
     */
    public static String getSHA1(String str)
            throws Exception {
        try {
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            // log.info("End--> " + hexstr.toString());
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("签名异常");
        }
    }
}
