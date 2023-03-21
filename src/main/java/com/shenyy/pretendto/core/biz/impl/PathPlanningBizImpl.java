package com.shenyy.pretendto.core.biz.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shenyy.pretendto.core.biz.PathPlanningBiz;
import com.shenyy.pretendto.pathfactory.*;
import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.gui.PathFinding;
import com.shenyy.pretendto.pathfactory.node.Node;
import com.shenyy.pretendto.utils.CommonUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class PathPlanningBizImpl implements PathPlanningBiz {
    public static void main(String[] args) {
        JSONObject scenario = new JSONObject();
        scenario.put("Dimension", "2D");
        JSONObject source = new JSONObject();
        source.put("X", 0.0);
        source.put("Y", 0.0);
        scenario.put("Source", source);
        JSONObject target = new JSONObject();
        target.put("X", 10.0);
        target.put("Y", 10.0);
        scenario.put("Target", target);
        JSONObject params = new JSONObject();
        params.put("P", 1.0);
        params.put("Q", 2.0);
        scenario.put("Params", params);

        List<List<Double>> obstacles = new ArrayList<>();
        Obstacle<Point2D> obstacle = new CircleObstacle(new Point2D(2, 2), 0.5);
        Obstacle<Point2D> obstacle1 = new CircleObstacle(new Point2D(4, 6), 0.5);
        String str = "[[2,2,0.5],[4,6,0.5]]";

        scenario.put("Obstacles", str);
        JSONObject jsonObject = new JSONObject(scenario);
        System.out.println(scenario);

        PathPlanningBizImpl pathPlanningBizImpl = new PathPlanningBizImpl();
        System.out.println(pathPlanningBizImpl.getPath(jsonObject));
    }

    @Override
    public JSONObject getSimulationPath() {
        JSONObject res = new JSONObject();
        Node node = PathFinding.getInstance().linePath.get(0);
        List<double[]> path = new ArrayList<>();
        while (true) {
            if (node.getParent() == null) {
                break;
            }
            path.add(new double[]{node.getX(), node.getY()});
            node = node.getParent();
        }
        res.put("path", path);
        return res;
    }

    @Override
    public JSONObject getPath(JSONObject scenario) {
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Dimension")) {
            if (scenario.get("Dimension") == "3D") {
                return getPath3D(scenario);
            } else {
                return getPath2D(scenario);
            }
        } else {
            return null;//Dimension Not Specified
        }
    }

    @Override
    public JSONObject getPath2D(JSONObject scenario) {
        /**解析scenario*/
        Point2D source = new Point2D(0, 0);
        Point2D target = new Point2D(0, 0);
        List<Obstacle> obstacles = new ArrayList<>();
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Source")) {
            JSONObject jsonObject = JSON.parseObject(scenario.getString("Source"));
            if (CommonUtils.isKeyValueNotEmpty(jsonObject, "X")
                    && CommonUtils.isKeyValueNotEmpty(jsonObject, "Y")) {
                source = new Point2D(Double.parseDouble(jsonObject.getString("X")), Double.parseDouble(jsonObject.getString("Y")));
            }
        }
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Target")) {
            JSONObject jsonObject = JSON.parseObject(scenario.getString("Target"));
            if (CommonUtils.isKeyValueNotEmpty(jsonObject, "X")
                    && CommonUtils.isKeyValueNotEmpty(jsonObject, "Y")) {
                target = new Point2D(Double.parseDouble(jsonObject.getString("X")), Double.parseDouble(jsonObject.getString("Y")));
            }
        }
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Obstacles")) {
            List<List> tempList = JSONArray.parseArray(scenario.getString("Obstacles"), List.class);
            for (List list :
                    tempList) {
                Obstacle<Point2D> obstacle = new CircleObstacle(new Point2D(Double.parseDouble(list.get(0).toString()), Double.parseDouble(list.get(1).toString())), Double.parseDouble(list.get(2).toString()));
                obstacles.add(obstacle);
            }
        }

        /**算法参数*/
        Map<String, Double> params = new HashMap<>();
        if (CommonUtils.isKeyValueNotEmpty(scenario, "Params")) {
            String jsonString = scenario.getString("Params");
            params = JSON.parseObject(jsonString, new TypeReference<Map<String, Double>>() {
            });
        }

        PathFactory<Point2D, CircleObstacle> staticPathFactory = new StaticPathFactory<>(source, target, obstacles, AlgoType.ACO, params);

        Path<Point2D, CircleObstacle> path = staticPathFactory.createStaticPath2D();
        path.construct();

        /**打包结果*/
        Map<String, Object> map = new HashMap<>();
        List<Point2D> point2DList = path.getPath();
        map.put("Path", point2DList);

        JSONObject res = new JSONObject(map);
        return res;
    }

    @Override
    public JSONObject getPath3D(JSONObject scenario) {
        return null;
    }
}
