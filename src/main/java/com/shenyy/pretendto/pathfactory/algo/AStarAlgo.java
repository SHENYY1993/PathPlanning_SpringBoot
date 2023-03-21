package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.node.NodeGrid;
import com.shenyy.pretendto.pathfactory.gui.PathFinding;

import java.util.ArrayList;

public class AStarAlgo<T, O> extends PathAlgo {
    private boolean solving = false;
    private int cells;
    private NodeGrid[][] map;
    private int checks = 0;
    private double length = 0;
    private int startx = -1;
    private int starty = -1;
    private int finishx = -1;
    private int finishy = -1;

    public AStarAlgo(Path<T, O> path) {
        super(path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("A star algorithm initializing...");
        cells = PathFinding.getInstance().cells;
        map = PathFinding.getInstance().map;
        checks = PathFinding.getInstance().checks;
        length = PathFinding.getInstance().length;
        startx = PathFinding.getInstance().startx;
        starty = PathFinding.getInstance().starty;
        finishx = PathFinding.getInstance().finishx;
        finishy = PathFinding.getInstance().finishy;
        solving = PathFinding.getInstance().solving;
    }

    @Override
    public void construct() {
        //TODO 2D path construct
        System.out.println("A star algorithm constructing 2D path...");
        ArrayList<NodeGrid> priority = new ArrayList<>();
        priority.add(map[startx][starty]);
        while (solving) {
            if (priority.size() <= 0) {
                solving = false;
                break;
            }
            ArrayList<NodeGrid> explored = exploreNeighbors(priority.get(0));
            if (explored.size() > 0) {
                priority.remove(0);
                priority.addAll(explored);
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();
            } else {
                priority.remove(0);
            }
            sortQue(priority);    //SORT THE PRIORITY QUE
        }
    }

    public ArrayList<NodeGrid> exploreNeighbors(NodeGrid current) {    //EXPLORE NEIGHBORS
        ArrayList<NodeGrid> explored = new ArrayList<>();    //LIST OF NODES THAT HAVE BEEN EXPLORED
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int xbound = current.getX() + a;
                int ybound = current.getY() + b;
                if ((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {    //MAKES SURE THE NODE IS NOT OUTSIDE THE GRID
                    NodeGrid neighbor = map[xbound][ybound];
                    double hops = current.getHops() + current.getEuclidDist(xbound, ybound);
                    if ((neighbor.getHops() == -1 || neighbor.getHops() > hops) && neighbor.getType() != 2) {    //CHECKS IF THE NODE IS NOT A WALL AND THAT IT HAS NOT BEEN EXPLORED
                        explore(neighbor, current.getX(), current.getY(), hops);    //EXPLORE THE NODE
                        explored.add(neighbor);    //ADD THE NODE TO THE LIST
                    }
                }
            }
        }
        return explored;
    }

    public void explore(NodeGrid current, int lastx, int lasty, double hops) {    //EXPLORE A NODE
        if (current.getType() != 0 && current.getType() != 1)    //CHECK THAT THE NODE IS NOT THE START OR FINISH
            current.setType(4);    //SET IT TO EXPLORED
        current.setLastNode(lastx, lasty);    //KEEP TRACK OF THE NODE THAT THIS NODE IS EXPLORED FROM
        current.setHops(hops);    //SET THE HOPS FROM THE START
        checks++;
        PathFinding.getInstance().checks = checks;
        if (current.getType() == 1) {    //IF THE NODE IS THE FINISH THEN BACKTRACK TO GET THE PATH
            backtrack(current.getLastX(), current.getLastY(), hops);
        }
    }

    public void backtrack(int lx, int ly, double hops) {    //BACKTRACK
        length = hops;
        PathFinding.getInstance().length = length;
        while (hops > Math.sqrt(2) + 0.1) {    //BACKTRACK FROM THE END OF THE PATH TO THE START
            NodeGrid current = map[lx][ly];
            current.setType(5);
            lx = current.getLastX();
            ly = current.getLastY();
            hops = hops - current.getEuclidDist(lx, ly);
            PathFinding.getInstance().Update();
            PathFinding.getInstance().delay();
        }
        solving = false;
        PathFinding.getInstance().solving = solving;
    }

    public ArrayList<NodeGrid> sortQue(ArrayList<NodeGrid> sort) {    //SORT PRIORITY QUE
        int c = 0;
        while (c < sort.size()) {
            int sm = c;
            for (int i = c + 1; i < sort.size(); i++) {
                if (sort.get(i).getEuclidDist(finishx, finishy) + sort.get(i).getHops() < sort.get(sm).getEuclidDist(finishx, finishy) + sort.get(sm).getHops())
                    sm = i;
            }
            if (c != sm) {
                NodeGrid temp = sort.get(c);
                sort.set(c, sort.get(sm));
                sort.set(sm, temp);
            }
            c++;
        }
        return sort;
    }
}
