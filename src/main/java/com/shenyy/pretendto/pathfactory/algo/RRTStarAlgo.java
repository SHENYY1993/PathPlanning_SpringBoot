package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.dijkstra2.NodeGUI;
import com.shenyy.pretendto.pathfactory.dijkstra2.PathFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RRTStarAlgo extends PathAlgo {
    public static void main(String[] args) {
        // 创建地图
//        Map map = new Map(800, 600);

        // 添加障碍物
        ArrayList<Node> obstacles = new ArrayList<Node>();
        obstacles.add(new Node(300, 300, 50));
        obstacles.add(new Node(500, 400, 50));
        obstacles.add(new Node(600, 200, 50));
//        map.setObstacles(obstacles);

        // 创建RRT*算法对象
//        RRTStarAlgo2 rrt = new RRTStarAlgo2(map, 10);

        // 设置起点和终点
        Node start = new Node(100, 100);
        Node goal = new Node(700, 500);

        // 运行RRT*算法
        ArrayList<Node> path = RRTStarAlgo.search(start, goal, obstacles, 0, 800, 0, 600);
        for (Node node :
                path) {
            System.out.println("[" + node.getX() + "," + node.getY() + "]");
        }

        // 绘制路径和地图
//        map.drawPath(path);
//        map.drawObstacles(obstacles);
//        map.show();
    }

    private static final double LEN = 0.5; //补偿半格长度

    private static double SEARCH_RADIUS = 50.0; // 搜索半径
    private static double GOAL_RADIUS = 10.0; // 目标半径
    private static final int MAX_ITERATIONS = 5000; // 最大迭代次数
    private static final double GAMMA = 1.0; // 权重因子
    private static final double DELTA_Q = 1.0; // 步长
    private static final Random random = new Random(); // 随机数生成器
    private static ArrayList<Node> obstacles = new ArrayList<Node>(); // 障碍物集合
    private static Node start;
    private static Node goal;
    private int xMin = -1;
    private int xMax = -1;
    private int yMin = -1;
    private int yMax = -1;
    private static List<Node> nodes;
    private static boolean findOne = false;

    public RRTStarAlgo(Path path) {
        super(path);
    }

    @Override
    public void initialize() {
        System.out.println("RRT star algorithm initializing...");
        // 设置起点和终点
        start = new Node(PathFinding.getInstance().startx + LEN, PathFinding.getInstance().starty + LEN);
        goal = new Node(PathFinding.getInstance().finishx + LEN, PathFinding.getInstance().finishy + LEN);

        // 添加障碍物
        NodeGUI[][] map = PathFinding.getInstance().map;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getType() == 2) {
                    obstacles.add(new Node(map[i][j].getX() + LEN, map[i][j].getY() + LEN, 0.5 * Math.sqrt(2)));
                }
            }
        }

        // 设置采样范围
        xMin = 0;
        xMax = PathFinding.getInstance().cells;
        yMin = 0;
        yMax = PathFinding.getInstance().cells;

        //相关参数
        SEARCH_RADIUS = PathFinding.getInstance().cells * 0.2;
        GOAL_RADIUS = PathFinding.getInstance().cells * 0.05;

        nodes = PathFinding.getInstance().nodeList;
    }

    @Override
    public void construct() {// 创建地图
        System.out.println("RRT star algorithm constructing 2D path...");
        // 运行RRT*算法
        ArrayList<Node> path = RRTStarAlgo.search(start, goal, obstacles, xMin, xMax, yMin, yMax);
        PathFinding.getInstance().linePath = path;

        PathFinding.getInstance().solving = false;
    }

    private static Node generateRandomNode(double xMin, double xMax, double yMin, double yMax) {
        double x = goal.getX();
        double y = goal.getY();
        if (Math.random() > 0.1) {
            x = xMin + (xMax - xMin) * random.nextDouble();
            y = yMin + (yMax - yMin) * random.nextDouble();
        }

        return new Node(x, y);
    }

    private static Node getNearestNode(Node randomNode, List<Node> nodes) {
        Node nearestNode = null;
        double minDistance = Double.MAX_VALUE;
        for (Node node : nodes) {
            double distance = node.getDistance(randomNode);
            if (distance < minDistance) {
                nearestNode = node;
                minDistance = distance;
            }
        }
        return nearestNode;
    }

    private static Node getBestParent(Node newNode, List<Node> nodes) {
        Node bestParent = newNode.getParent();
        double minCost = Double.MAX_VALUE;
        ArrayList<Node> neighbors = getNeighbors(newNode, nodes);
        for (Node neighbor : neighbors) {
            double cost = neighbor.getCost() + neighbor.getDistance(newNode);
            if (cost < minCost && newNode.isCollisionFree(neighbor, obstacles)) {
                bestParent = neighbor;
                minCost = cost;
            }
        }
        newNode.setCost(minCost);
        return bestParent;
    }

    private static ArrayList<Node> getNeighbors(Node newNode, List<Node> nodes) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.getDistance(newNode) < SEARCH_RADIUS) {
                neighbors.add(node);
            }
        }
        return neighbors;
    }

    private static void rewire(Node newNode, List<Node> nodes) {
        ArrayList<Node> neighbors = getNeighbors(newNode, nodes);
        for (Node neighbor : neighbors) {
            if (newNode.isCollisionFree(neighbor, obstacles)) {
                double cost = newNode.getCost() + newNode.getDistance(neighbor);
                if (cost < neighbor.getCost()) {
                    neighbor.setParent(newNode);
                    neighbor.setCost(cost);

                    /**GUI Update*/
                    PathFinding.getInstance().Update();
                    PathFinding.getInstance().delay();
                }
            }
        }
    }

    public static ArrayList<Node> search(Node start, Node goal, ArrayList<Node> obstacles, double xMin, double xMax, double yMin, double yMax) {
        nodes.add(start);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (!PathFinding.getInstance().solving) {
                break;
            }
            /**GUI Update*/
            PathFinding.getInstance().checks = i;

            Node randomNode = generateRandomNode(xMin, xMax, yMin, yMax);
            Node nearestNode = getNearestNode(randomNode, nodes);
            Node newNode = nearestNode.moveTowards(randomNode, DELTA_Q);

            if (newNode.isCollisionFree(nearestNode, obstacles)) {
                Node bestParent = getBestParent(newNode, nodes);
                newNode.setParent(nearestNode);
                nodes.add(newNode);
                /**GUI Update*/
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();

//                Node bestParent = getBestParent(newNode, nodes);
//                newNode.setParent(bestParent);
//                newNode.setCost(bestParent.getCost() + bestParent.getDistance(newNode));
//                nodes.add(newNode);
                nodes.get(nodes.size() - 1).setParent(bestParent);
                nodes.get(nodes.size() - 1).setCost(bestParent.getCost() + bestParent.getDistance(newNode));
                /**GUI Update*/
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();

                rewire(newNode, nodes);
                /**GUI Update*/
                PathFinding.getInstance().Update();
                PathFinding.getInstance().delay();

                if (newNode.getDistance(goal) < GOAL_RADIUS) {
                    goal.setParent(newNode);
                    goal.setCost(newNode.getCost() + newNode.getDistance(goal));
                    nodes.add(goal);
                    rewire(goal, nodes);

                    /**GUI Update*/
                    PathFinding.getInstance().length = goal.getCost();
                    PathFinding.getInstance().Update();
                    PathFinding.getInstance().delay();

                    PathFinding.getInstance().linePath = getPath(start, goal);
                    findOne = true;

                    /**输出路径*/
                    System.out.print("Length: " + goal.getCost() + " | ");
                    for (Node node :
                            PathFinding.getInstance().linePath) {
                        System.out.print("(" + node.getX() + "," + node.getY() + ")");
                    }
                    System.out.println();
                    return getPath(start, goal);
                }
            }
        }
        if (findOne) {
            return getPath(start, goal);
        }
        return null; // 未找到路径
    }

    private static ArrayList<Node> getPath(Node start, Node goal) {
        ArrayList<Node> path = new ArrayList<Node>();
        Node node = goal;
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
