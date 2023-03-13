package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.ObstacleType;
import javafx.geometry.Point2D;

public class CircleObstacle extends Obstacle<Point2D> {
    public CircleObstacle(Point2D location, double radius) {
        super(location, ObstacleType.CIRCLE, radius);
    }
}
