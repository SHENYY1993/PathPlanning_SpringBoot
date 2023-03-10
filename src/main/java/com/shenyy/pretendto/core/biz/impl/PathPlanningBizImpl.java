package com.shenyy.pretendto.core.biz.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shenyy.pretendto.core.biz.PathPlanningBiz;
import com.shenyy.pretendto.pathfactory.*;
import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.utils.CommonUtils;
import javafx.geometry.Point3D;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PathPlanningBizImpl implements PathPlanningBiz {
    public static void main(String[] args) {
        PathPlanningBizImpl pathPlanningBizImpl = new PathPlanningBizImpl();
        pathPlanningBizImpl.getPath(new JSONObject());
    }

    @Override
    public JSONObject getPath(JSONObject scenario) {
        /**解析scenario*/
        Point source = new Point();
        Point target = new Point();
        List<Obstacle> obstacles = new ArrayList<>();
        if (CommonUtils.isKeyValueNotEmpty(scenario, "source")) {
            JSONObject jsonObject = JSONObject.parseObject(scenario.get("source").toString());
            if (CommonUtils.isKeyValueNotEmpty(jsonObject, "x")
                    && CommonUtils.isKeyValueNotEmpty(jsonObject, "y")) {
                source = new Point(Integer.parseInt(jsonObject.get("x").toString()), Integer.parseInt(jsonObject.get("y").toString()));
            }
        }
        if (CommonUtils.isKeyValueNotEmpty(scenario, "target")) {
            JSONObject jsonObject = JSONObject.parseObject(scenario.get("target").toString());
            if (CommonUtils.isKeyValueNotEmpty(jsonObject, "x")
                    && CommonUtils.isKeyValueNotEmpty(jsonObject, "y")) {
                target = new Point(Integer.parseInt(jsonObject.get("x").toString()), Integer.parseInt(jsonObject.get("y").toString()));
            }
        }

        if (CommonUtils.isKeyValueNotEmpty(scenario, "obstacles")) {
            obstacles = JSONArray.parseArray(scenario.get("obstacles").toString(),Obstacle.class);
        }
        Obstacle<Point> obstacle = new CircleObstacle(new Point(2, 2), 0.5);
        obstacles.add(obstacle);
        /**算法参数*/
        Map<String, Double> params = new HashMap<>();

        PathFactory<Point, Obstacle<Point>> staticPathFactory = new StaticPathFactory<>(source, target, obstacles, AlgoType.DIJKSTRA, params);

        Path path = staticPathFactory.createStaticPath2D();
        path.construct();

        /**打包结果*/
        JSONObject res = new JSONObject();
        return res;
    }

    @Override
    public JSONObject getPath3D(JSONObject scenario) {
        return null;
    }
}
