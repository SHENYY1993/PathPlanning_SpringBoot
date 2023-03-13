package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import javafx.geometry.Point2D;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactoryProducer {
    public static void main(String[] args) {
        Point2D source = new Point2D(0, 0);
        Point2D target = new Point2D(0, 0);
        List<Obstacle<Point2D>> obstacles = new ArrayList<>();
        Obstacle<Point2D> obstacle = new CircleObstacle(new Point2D(2, 2), 0.5);
        obstacles.add(obstacle);
        Map<String, Double> params = new HashMap<>();

        PathFactory<Point, Obstacle<Point>> staticPathFactory = new StaticPathFactory<>(source, target, obstacles, AlgoType.DIJKSTRA, params);

        Path path = staticPathFactory.createStaticPath2D();
        path.construct();
        System.out.println(path.getPath());
    }
}
