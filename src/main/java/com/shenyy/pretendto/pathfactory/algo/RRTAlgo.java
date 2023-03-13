package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;

import java.util.Map;

public class RRTAlgo<T, O> extends PathAlgo {
    public RRTAlgo( Path<T, O> path) {
        super(path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("RRT algorithm initializing...");
    }

    @Override
    public void construct() {
        //TODO 2D path construct
        System.out.println("RRT algorithm constructing 2D path...");
    }
}
