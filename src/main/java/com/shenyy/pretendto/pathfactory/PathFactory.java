package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;

import java.util.List;
import java.util.Map;

abstract class PathFactory<T, O> {
    protected T source;

    protected T target;

    protected List<O> obstacles;

    protected AlgoType algoType;

    protected Map<String, Double> params;

    public PathFactory(T source, T target, List<O> obstacles, AlgoType algoType, Map<String, Double> params) {
        this.source = source;
        this.target = target;
        this.obstacles = obstacles;
        this.algoType = algoType;
        this.params = params;
    }

    /**
     * 静态路径规划2D
     */
    abstract Path createStaticPath2D();

    /**
     * 动态路径规划2D
     */
    abstract Path createDynamicPath2D();

    /**
     * 静态路径规划3D
     */
    abstract Path createStaticPath3D();


    /**
     * 动态路径规划3D
     */
    abstract Path createDynamicPath3D();
}
