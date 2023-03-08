package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.algo.*;
import com.shenyy.pretendto.pathfactory.enumtype.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Path2D<T, O> extends Path {
    public Path2D(T source, T target, List<O> obstacles, AlgoType algoType, PathType pathType, Map params) {
        super(source, target, obstacles, algoType, pathType, params);
    }

    @Override
    void construct() {
        PathAlgo<Point, Obstacle<Point>> pathAlgo;
        switch (algoType) {
            case DIJKSTRA:
                pathAlgo = new DijkstraAlgo<>(params, this);
                break;
            case A_STAR:
                pathAlgo = new AStarAlgo<>(params, this);
                break;
            case RRT:
                pathAlgo = new RRTAlgo<>(params, this);
                break;
            case RRT_STAR:
                pathAlgo = new RRTStarAlgo<>(params, this);
                break;
            case INFORMED_RRT:
                pathAlgo = new InformedRRTStarAlgo<>(params, this);
                break;
            default:
                pathAlgo = new InformedRRTStarAlgo<>(params, this);
                break;
        }

        pathAlgo.initialize();
        pathAlgo.construct();
    }
}
