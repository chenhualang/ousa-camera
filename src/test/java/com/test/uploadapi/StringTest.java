package com.test.uploadapi;

import com.alibaba.fastjson.JSON;
import org.junit.*;

/**
 * Created by chenhualang on 2017/3/14.
 */
public class StringTest {
    @org.junit.Test
    public void testString(){
        String str1 = "单次购买权益类基金赠送申购费相当的积分数";
        String str2 = "好买基金";
        String remark = str1.replaceAll("权益类基金",str2);
        System.out.println(remark);
    }
}
