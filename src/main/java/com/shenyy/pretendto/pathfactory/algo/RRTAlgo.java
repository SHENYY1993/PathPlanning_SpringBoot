package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.dijkstra2.Node;
import com.shenyy.pretendto.pathfactory.dijkstra2.PathFinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RRTAlgo<T, O> extends PathAlgo {
    private boolean solving = false;
    private int cells;
    private Node[][] map;
    private int checks = 0;
    private double length = 0;
    private int startx = -1;
    private int starty = -1;
    private int finishx = -1;
    private int finishy = -1;
    private int gen = 200;
    private double stepSize = 1;

    public RRTAlgo(Path<T, O> path) {
        super(path);
    }

    @Override
    public void initialize() {
        //TODO 2D path construct
        System.out.println("RRT algorithm initializing...");
        cells = PathFinding.getInstance().cells;
        map = PathFinding.getInstance().map;
        checks = PathFinding.getInstance().checks;
        length = PathFinding.getInstance().length;
        startx = PathFinding.getInstance().startx;
        starty = PathFinding.getInstance().starty;
        finishx = PathFinding.getInstance().finishx;
        finishy = PathFinding.getInstance().finishy;
        solving = PathFinding.getInstance().solving;
        gen = PathFinding.getInstance().cells * 10;
    }

    @Override
    public void construct() {
        //TODO 2D path construct
        System.out.println("RRT algorithm constructing 2D path...");
        List<Node> priority = new ArrayList<>();    //CREATE A PRIORITY QUE
        priority.add(map[startx][starty]);    //ADD THE START TO THE QUE
        for (int i = 0; i < gen; i++) {
            if (priority.size() <= 0) {
                solving = false;
                break;
            }
            /**Generate the sample point(生成随机采样点x_rand)*/
            //1.在cells * cells内随机生成采样点点，按一定概率（5%~10%）直接选择目标点作为采样点
            //2.如果在障碍物上直接废弃，重新生成
            int samplePointX = finishx;
            int samplePointY = finishy;
            if (Math.random() > 0.1) {
                samplePointX = (int) (Math.random() * cells);
                samplePointY = (int) (Math.random() * cells);
            }

            if (map[samplePointX][samplePointY].getType() == 2
                    || map[samplePointX][samplePointY].getType() == 4) {
                i--;
                continue;
            }

            /**Obtain the nearest point to the sample point(获取集合中距离x_rand最近的点x_near)*/
            double dist = map[samplePointX][samplePointY].getEuclidDist(priority.get(0).getX(), priority.get(0).getY());
            int nearPointX = priority.get(0).getX();
            int nearPointY = priority.get(0).getY();
            for (int j = 0; j < priority.size(); j++) {
                if (dist > map[samplePointX][samplePointY].getEuclidDist(priority.get(j).getX(), priority.get(j).getY())) {
                    nearPointX = priority.get(j).getX();
                    nearPointY = priority.get(j).getY();
                    dist = map[samplePointX][samplePointY].getEuclidDist(priority.get(j).getX(), priority.get(j).getY());
                }
            }

            /**Generate the new point with one step size(按照步长stepSize在x_near到x_rand的线段上生成新的点x_new)*/
            /**Generate the line(x_near, x_new)*/
            int newPointX = samplePointX;
            int newPointY = samplePointY;
            if (samplePointX > nearPointX) {
                newPointX = nearPointX + 1;
            } else if (samplePointX < nearPointX) {
                newPointX = nearPointX - 1;
            }
            if (samplePointY > nearPointY) {
                newPointY = nearPointY + 1;
            } else if (samplePointY < nearPointY) {
                newPointY = nearPointY - 1;
            }

            /**Judge whether collision or not (判断线段是否与障碍物碰撞)*/
            if (map[newPointX][newPointY].getType() == 2) {
                continue;
            }
            map[newPointX][newPointY].setType(4);    //SET IT TO EXPLORED
            map[newPointX][newPointY].setLastNode(nearPointX, nearPointY);    //KEEP TRACK OF THE NODE THAT THIS NODE IS EXPLORED FROM
            map[newPointX][newPointY].setHops(map[nearPointX][nearPointY].getHops() + 1);    //SET THE HOPS FROM THE START
            checks++;
            PathFinding.getInstance().checks = checks;
            priority.add(map[newPointX][newPointY]);
            PathFinding.getInstance().Update();
            PathFinding.getInstance().delay();

            /**Judge whether reach the goal or not (判断是否到达目标点x_goal)*/
            if (map[newPointX][newPointY].getEuclidDist(finishx, finishy) <= Math.sqrt(2)) {
                PathFinding.getInstance().solving = false;
                /**回溯路径*/
                backtrack(newPointX, newPointY, map[newPointX][newPointY].getHops());
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();
                break;
            }
        }
    }

    public void backtrack(int lx, int ly, double hops) {    //BACKTRACK
        length = hops;
        PathFinding.getInstance().length = length;
        while (hops > 0) {    //BACKTRACK FROM THE END OF THE PATH TO THE START
            Node current = map[lx][ly];
            current.setType(5);
            lx = current.getLastX();
            ly = current.getLastY();
            hops--;
            PathFinding.getInstance().Update();
            PathFinding.getInstance().delay();
        }
        solving = false;
        PathFinding.getInstance().solving = solving;
    }
}
