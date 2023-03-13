package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.CircleObstacle;
import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.dijkstra2.Node;
import com.shenyy.pretendto.pathfactory.dijkstra2.PathFinding;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgo extends PathAlgo<Point2D, CircleObstacle> {
    private boolean solving = false;
    private int cells;
    private Node[][] map;
    private int checks = 0;
    private int length = 0;
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
        List<Node> priority = new ArrayList<>();    //CREATE A PRIORITY QUE
        priority.add(map[startx][starty]);    //ADD THE START TO THE QUE
        while (solving) {
            if (priority.size() <= 0) {    //IF THE QUE IS 0 THEN NO PATH CAN BE FOUND
                solving = false;
                PathFinding.getInstance().solving = solving;
                break;
            }
            int hops = priority.get(0).getHops() + 1;    //INCREMENT THE HOPS VARIABLE
            ArrayList<Node> explored = exploreNeighbors(priority.get(0), hops);    //CREATE AN ARRAYLIST OF NODES THAT WERE EXPLORED
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

    public ArrayList<Node> exploreNeighbors(Node current, int hops) {    //EXPLORE NEIGHBORS
        ArrayList<Node> explored = new ArrayList<>();    //LIST OF NODES THAT HAVE BEEN EXPLORED
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                int xbound = current.getX() + a;
                int ybound = current.getY() + b;
                if ((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {    //MAKES SURE THE NODE IS NOT OUTSIDE THE GRID
                    Node neighbor = map[xbound][ybound];
                    if ((neighbor.getHops() == -1 || neighbor.getHops() > hops) && neighbor.getType() != 2) {    //CHECKS IF THE NODE IS NOT A WALL AND THAT IT HAS NOT BEEN EXPLORED
                        explore(neighbor, current.getX(), current.getY(), hops);    //EXPLORE THE NODE
                        explored.add(neighbor);    //ADD THE NODE TO THE LIST
                    }
                }
            }
        }
        return explored;
    }

    public void explore(Node current, int lastx, int lasty, int hops) {    //EXPLORE A NODE
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

    public void backtrack(int lx, int ly, int hops) {    //BACKTRACK
        length = hops;
        PathFinding.getInstance().length = length;
        while (hops > 1) {    //BACKTRACK FROM THE END OF THE PATH TO THE START
            Node current = map[lx][ly];
            current.setType(5);
            lx = current.getLastX();
            ly = current.getLastY();
            hops--;
        }
        solving = false;
        PathFinding.getInstance().solving = solving;
    }
}
