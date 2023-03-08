package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.ObstacleType;
import javafx.geometry.Point3D;

public class SphereObstacle extends Obstacle<Point3D> {
    public SphereObstacle(Point3D location, double radius) {
        super(location, ObstacleType.SPHERE, radius);
    }
}
