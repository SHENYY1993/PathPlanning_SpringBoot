package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.algo.*;
import com.shenyy.pretendto.pathfactory.algo.ant.ACOAlgo;
import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.enumtype.PathType;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Map;

public class Path2D extends Path {
    public Path2D(Point2D source, Point2D target, List<Obstacle> obstacles, AlgoType algoType, PathType pathType, Map params) {
        super(source, target, obstacles, algoType, pathType, params);
    }

    @Override
    public void construct() {
        PathAlgo<Point2D, CircleObstacle> pathAlgo;
        switch (algoType) {
            case DIJKSTRA:
                pathAlgo = new DijkstraAlgo(this);
                break;
            case A_STAR:
                pathAlgo = new AStarAlgo<>(this);
                break;
            case RRT:
                pathAlgo = new RRTAlgo<>(this);
                break;
            case RRT_STAR:
//                pathAlgo = new RRTStarAlgo<>(this);
                pathAlgo = new RRTStarAlgo2(this);
                break;
            case INFORMED_RRT:
                pathAlgo = new InformedRRTStarAlgo<>(this);
                break;
            case ACO:
                pathAlgo = new ACOAlgo(this, 1000, 1, 2, 0.2);
                break;
            default:
                pathAlgo = new InformedRRTStarAlgo<>(this);
                break;
        }

        pathAlgo.initialize();
        pathAlgo.construct();
    }
}
