package com.test.uploadapi;

import com.alibaba.fastjson.JSON;
import org.junit.*;

/**
 * Created by chenhualang on 2017/3/14.
 */
public class StringTest {
    @org.junit.Test
    public void testString(){
        String str1 = "���ι���Ȩ������������깺���൱�Ļ�����";
        String str2 = "�������";
        String remark = str1.replaceAll("Ȩ�������",str2);
        System.out.println(remark);
    }
}
