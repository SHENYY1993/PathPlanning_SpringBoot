package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.CircleObstacle;
import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.dijkstra2.NodeGUI;
import com.shenyy.pretendto.pathfactory.dijkstra2.PathFinding;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgo extends PathAlgo<Point2D, CircleObstacle> {
    private boolean solving = false;
    private int cells;
    private NodeGUI[][] map;
    private int checks = 0;
    private double length = 0;
    private int startx = -1;
    private int starty = -1;
    private int finishx = -1;
    private int finishy = -1;

    public DijkstraAlgo(Path<Point2D, CircleObstacle> path) {
        super(path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("Dijkstra algorithm initializing...");
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
        System.out.println("Dijkstra algorithm constructing 2D path...");
        List<NodeGUI> priority = new ArrayList<>();    //CREATE A PRIORITY QUE
        priority.add(map[startx][starty]);    //ADD THE START TO THE QUE
        while (solving) {
            if (priority.size() <= 0) {    //IF THE QUE IS 0 THEN NO PATH CAN BE FOUND
                solving = false;
                PathFinding.getInstance().solving = solving;
                break;
            }
            ArrayList<NodeGUI> explored = exploreNeighbors(priority.get(0));    //CREATE AN ARRAYLIST OF NODES THAT WERE EXPLORED
            if (explored.size() > 0) {
                priority.remove(0);    //REMOVE THE NODE FROM THE QUE
                priority.addAll(explored);    //ADD ALL THE NEW NODES TO THE QUE
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();
            } else {    //IF NO NODES WERE EXPLORED THEN JUST REMOVE THE NODE FROM THE QUE
                priority.remove(0);
            }
        }
    }

    public ArrayList<NodeGUI> exploreNeighbors(NodeGUI current) {    //EXPLORE NEIGHBORS
        ArrayList<NodeGUI> explored = new ArrayList<>();    //LIST OF NODES THAT HAVE BEEN EXPLORED
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int xbound = current.getX() + a;
                int ybound = current.getY() + b;
                if ((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {    //MAKES SURE THE NODE IS NOT OUTSIDE THE GRID
                    NodeGUI neighbor = map[xbound][ybound];
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

    public void explore(NodeGUI current, int lastx, int lasty, double hops) {    //EXPLORE A NODE
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
            NodeGUI current = map[lx][ly];
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
}
