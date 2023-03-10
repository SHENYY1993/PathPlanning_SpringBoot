package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.enumtype.PathType;
import javafx.geometry.Point3D;

import java.util.List;
import java.util.Map;

public class Path3D<T, O> extends Path {
    public Path3D(T source, T target, List<O> obstacles, AlgoType algoType, PathType pathType, Map params) {
        super(source, target, obstacles, algoType, pathType, params);
    }

    @Override
    public void construct() {
        //TODO 3D path construct
        System.out.println("Constructing 3D path...");
    }
}
