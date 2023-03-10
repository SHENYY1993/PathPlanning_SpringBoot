package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.enumtype.PathType;

import java.util.List;
import java.util.Map;

public abstract class Path<T, O> {
    protected List<T> path;

    protected T source;

    protected T target;

    protected List<O> obstacles;

    protected AlgoType algoType;

    protected PathType pathType;

    protected Map<String, Double> params;

    public Path(T source, T target, List<O> obstacles, AlgoType algoType, PathType pathType, Map<String, Double> params) {
        this.source = source;
        this.target = target;
        this.obstacles = obstacles;
        this.algoType = algoType;
        this.pathType = pathType;
        this.params = params;
    }

    /**
     * 路径构建
     */
    public abstract void construct();
}
