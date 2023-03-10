package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.ObstacleType;

public abstract class Obstacle<T> {
    protected T location;
    protected ObstacleType obstacleType;
    protected double radius;

    public Obstacle(T location, ObstacleType obstacleType, double radius) {
        this.location = location;
        this.obstacleType = obstacleType;
        this.radius = radius;
    }
}
