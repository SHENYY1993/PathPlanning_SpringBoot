package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.ObstacleType;

import java.awt.*;

public class CylinderObstacle extends Obstacle<Point> {
    public CylinderObstacle(Point location, double radius) {
        super(location, ObstacleType.CYLINDER, radius);
    }
}
