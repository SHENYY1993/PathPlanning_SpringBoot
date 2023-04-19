package com.shenyy.pretendto.pathfactory.algo.pso;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.algo.PathAlgo;
import com.shenyy.pretendto.pathfactory.gui.PathFinding;
import com.shenyy.pretendto.pathfactory.node.Node;
import com.shenyy.pretendto.pathfactory.node.NodeGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PSOAlgo extends PathAlgo {
    public static void main(String[] args) {

    }

    public PSOAlgo(Path path) {
        super(path);
    }

    @Override
    public void initialize() {
        System.out.println("PSO algorithm initializing...");

    }

    @Override
    public void construct() {
        Main.menu(false);
    }


}
