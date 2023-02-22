package com.shenyy.pretendto.core.controller;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.ParentTest;

public class RedisControllerTest extends ParentTest {
    @Autowired
    RedisController redisController;

    @Test
    public void testExecuteRedisCliCommand() {
        Object result = redisController.executeRedisCliCommand("get", "key-2");
        System.out.println(result);
    }

    @Test
    public void testGet() {
        Object result = redisController.get("key-6");
        System.out.println(result);
    }

    public void testSet() {
    }

    public void testTestGet() {
    }

    public void testDelete() {
    }
}