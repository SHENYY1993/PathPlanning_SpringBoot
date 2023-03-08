package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactoryProducer {
    public static void main(String[] args) {
        Point source = new Point();
        Point target = new Point();
        List<Obstacle<Point>> obstacles = new ArrayList<>();
        Obstacle<Point> obstacle = new CircleObstacle(new Point(2, 2), 0.5);
        obstacles.add(obstacle);
        Map<String, Double> params = new HashMap<>();

        PathFactory<Point, Obstacle<Point>> staticPathFactory = new StaticPathFactory<>(source, target, obstacles, AlgoType.DIJKSTRA, params);

        Path path = staticPathFactory.createStaticPath2D();
        path.construct();
    }
}
