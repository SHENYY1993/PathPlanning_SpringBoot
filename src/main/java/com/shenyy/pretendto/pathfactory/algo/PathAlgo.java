package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;

import java.util.Map;

public abstract class PathAlgo<T, O> {
    Map<String, Double> paramMap;

    Path<T, O> path;

    public PathAlgo(Map<String, Double> paramMap, Path<T, O> path) {
        this.paramMap = paramMap;
        this.path = path;
    }

    public abstract void initialize();

    public abstract void construct();
}
