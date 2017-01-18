package com.huawei.colin.mediaplayer.util;

/**
 * Created by Colin on 2017/1/19.
 * Description:本类主要用于提供http请求的数据封装
 */
public class Http {

    /**
     * <Description>safe method for check if  str is null
     * <Detail>
     * @param str The string to check.
     * @return
     *
     */
    public static boolean isEmpty(String str) {
        return str == null ? true : str.isEmpty() ? true : false;
    }

    /**
     * @param a
     * @param b
     * @param c
     * @return the biggest number of a,b,c
     */
    public static int findBiggest(int a, int b, int c) {
        return a < b ? (b < c ? c : b) : (a < c ? c : a);
    }
}
