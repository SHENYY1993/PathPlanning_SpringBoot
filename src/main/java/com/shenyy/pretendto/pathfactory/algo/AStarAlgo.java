package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;

import java.util.Map;

public class AStarAlgo<T, O> extends PathAlgo {
    public AStarAlgo(Map<String, Double> paramMap, Path<T, O> path) {
        super(paramMap, path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("A star algorithm initializing...");
    }

    @Override
    public void construct() {
        //TODO 2D path construct
        System.out.println("A star algorithm constructing 2D path...");
    }
}
