package com.shenyy.pretendto.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.shenyy.pretendto.core.biz.PathPlanningBiz;
import com.shenyy.pretendto.utils.CommonUtils;
import javafx.geometry.Point3D;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pathPlanning")
public class PathPlanningController {
    @Resource
    PathPlanningBiz pathPlanningBiz;

    @DS("master")
    @PostMapping(value = "/getPath")
    public JSONObject getPath(@RequestBody JSONObject scenario) {
        return pathPlanningBiz.getPath(scenario);
    }

    @GetMapping(value = "/getSimulationPath")
    public JSONObject getSimulationPath() {
        return pathPlanningBiz.getSimulationPath();
    }
}
