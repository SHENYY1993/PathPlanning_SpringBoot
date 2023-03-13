package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.CircleObstacle;
import com.shenyy.pretendto.pathfactory.Path;
import javafx.geometry.Point2D;

public class DijkstraAlgo extends PathAlgo<Point2D, CircleObstacle> {
    public DijkstraAlgo(Path<Point2D, CircleObstacle> path) {
        super(path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("Dijkstra algorithm initializing...");

    }

    @Override
    public void construct() {
        //TODO 2D path construct
        System.out.println("Dijkstra algorithm constructing 2D path...");
        path.getPath().add(new Point2D(0, 0));
        path.getPath().add(new Point2D(1, 2));
        path.getPath().add(new Point2D(3, 3));
    }
}
