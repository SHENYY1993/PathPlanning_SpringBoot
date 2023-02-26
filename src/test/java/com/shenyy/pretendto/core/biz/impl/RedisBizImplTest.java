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

    @Test
    public void testSetToCache() {
        redisBiz.setToCache("key-002", "value-002", 60L);
        Object result = redisBiz.getFromCache("key-002");
        System.out.println(result);
    }

    @Test
    public void testGetFromCache() {
        Object result = redisBiz.getFromCache("key-002");
        System.out.println(result);
    }
}