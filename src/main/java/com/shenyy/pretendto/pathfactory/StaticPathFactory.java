package com.shenyy.pretendto.pathfactory;

import com.shenyy.pretendto.pathfactory.enumtype.AlgoType;
import com.shenyy.pretendto.pathfactory.enumtype.PathType;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.Map;

public class StaticPathFactory<T, O> extends PathFactory {
    public StaticPathFactory(T source, T target, List<O> obstacles, AlgoType algoType, Map params) {
        super(source, target, obstacles, algoType, params);
    }

    @Override
    public Path createStaticPath2D() {
        Path path = new Path2D((Point2D) source, (Point2D) target, obstacles, algoType, PathType.STATIC_PATH, params);
        return path;
    }

    @Override
    public Path createDynamicPath2D() {
        return null;
    }

    @Override
    public Path createStaticPath3D() {
        Path path = new Path3D(source, target, obstacles, algoType, PathType.STATIC_PATH, params);
        return path;
    }

    @Override
    public Path createDynamicPath3D() {
        return null;
    }
}
