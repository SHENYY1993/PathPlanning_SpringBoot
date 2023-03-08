package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.ObstacleType;

import java.awt.*;

public class CircleObstacle extends Obstacle<Point> {
    public CircleObstacle(Point location, double radius) {
        super(location, ObstacleType.CIRCLE, radius);
    }
}
