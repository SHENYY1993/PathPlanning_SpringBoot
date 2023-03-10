package com.shenyy.pretendto.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.shenyy.pretendto.core.biz.PathPlanningBiz;
import com.shenyy.pretendto.utils.CommonUtils;
import javafx.geometry.Point3D;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pathPlanning")
public class PathPlanningController {
    @Resource
    PathPlanningBiz pathPlanningBiz;

    @DS("master")
    @PostMapping(value = "/getPath")
    public JSONObject getPath(@RequestBody JSONObject scenario) {
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Dimension")) {
            if (scenario.get("Dimension") == "3D") {
                return pathPlanningBiz.getPath3D(scenario);
            }
        }
        return pathPlanningBiz.getPath(scenario);
    }
}
