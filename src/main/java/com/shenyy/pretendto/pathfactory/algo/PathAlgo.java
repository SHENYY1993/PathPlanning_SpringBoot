package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;

public abstract class PathAlgo<T, O> {
    Path<T, O> path;

    public PathAlgo(Path<T, O> path) {
        this.path = path;
    }

    public abstract void initialize();

    public abstract void construct();
}
