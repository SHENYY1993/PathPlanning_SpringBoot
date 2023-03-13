package com.shenyy.pretendto.core.controller;

import com.alibaba.fastjson.JSONObject;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.ParentTest;

import java.util.HashMap;
import java.util.Map;

public class PathPlanningControllerTest extends ParentTest {
    @Autowired
    PathPlanningController pathPlanningController;

    @Test
    public void testGetPath() {
        Map<String, Object> map = new HashMap<>();
        map.put("Dimension", "2D");
        Map<String, Double> source = new HashMap<>();
        source.put("X", 0.0);
        source.put("Y", 0.0);
        map.put("Source", source);
        Map<String, Double> target = new HashMap<>();
        source.put("X", 10.0);
        source.put("Y", 10.0);
        map.put("Target", target);
        Map<String, Double> params = new HashMap<>();
        params.put("P", 1.0);
        params.put("Q", 2.0);

        map.put("Params", params);
        JSONObject jsonObject = new JSONObject(map);
        Object res = pathPlanningController.getPath(jsonObject);
        System.out.println(res);
    }
}