package com.shenyy.pretendto.core.biz.impl;

import com.shenyy.pretendto.core.biz.RedisBiz;
import org.junit.Test;
import test.ParentTest;

import javax.annotation.Resource;

public class RedisBizImplTest extends ParentTest {
    @Resource
    RedisBiz redisBiz;

    @Test
    public void testSet() {
        Object object = redisBiz.get("key-6");
        System.out.println(object.toString());
    }

    @Test
    public void testSetnx() {
        redisBiz.setIfAbsent("key-6", "new value");
    }

    @Test
    public void testGet() {
    }

    @Test
    public void testDelete() {
    }
}