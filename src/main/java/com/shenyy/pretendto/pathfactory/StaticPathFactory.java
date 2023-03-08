package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.enumtype.PathType;

import java.util.List;
import java.util.Map;

public class StaticPathFactory<T, O> extends PathFactory {
    public StaticPathFactory(T source, T target, List<O> obstacles, AlgoType algoType, Map params) {
        super(source, target, obstacles, algoType, params);
    }

    @Override
    Path createStaticPath2D() {
        Path path = new Path2D(source, target, obstacles, algoType, PathType.STATIC_PATH, params);
        return path;
    }

    @Override
    Path createDynamicPath2D() {
        return null;
    }

    @Override
    Path createStaticPath3D() {
        Path path = new Path3D(source, target, obstacles, algoType, PathType.STATIC_PATH, params);
        return path;
    }

    @Override
    Path createDynamicPath3D() {
        return null;
    }
}
