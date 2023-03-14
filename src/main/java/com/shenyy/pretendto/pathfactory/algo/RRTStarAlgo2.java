package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RRTStarAlgo2 extends PathAlgo {
    public static void main(String[] args) {
//        // 创建地图
//        Map map = new Map(800, 600);
//
//        // 添加障碍物
//        ArrayList<Node> obstacles = new ArrayList<Node>();
//        obstacles.add(new Node(300, 300, 50));
//        obstacles.add(new Node(500, 400, 50));
//        obstacles.add(new Node(600, 200, 50));
//        map.setObstacles(obstacles);
//
//        // 创建RRT*算法对象
//        RRTStar rrt = new RRTStar(map, 10);
//
//        // 设置起点和终点
//        Node start = new Node(100, 100);
//        Node goal = new Node(700, 500);
//
//        // 运行RRT*算法
//        ArrayList<Node> path = rrt.findPath(start, goal);
//
//        // 绘制路径和地图
//        map.drawPath(path);
//        map.drawObstacles(obstacles);
//        map.show();
    }

    private static final double SEARCH_RADIUS = 50.0; // 搜索半径
    private static final double GOAL_RADIUS = 10.0; // 目标半径
    private static final int MAX_ITERATIONS = 10000; // 最大迭代次数
    private static final double GAMMA = 1.0; // 权重因子
    private static final double DELTA_Q = 1.0; // 步长
    private static final Random random = new Random(); // 随机数生成器
    private static final ArrayList<Node> obstacles = new ArrayList<Node>(); // 障碍物集合

    public RRTStarAlgo2(Path path) {
        super(path);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void construct() {// 创建地图
    }

    private static Node generateRandomNode(double xMin, double xMax, double yMin, double yMax) {
        double x = xMin + (xMax - xMin) * random.nextDouble();
        double y = yMin + (yMax - yMin) * random.nextDouble();
        return new Node(x, y);
    }

    private static Node getNearestNode(Node randomNode, ArrayList<Node> nodes) {
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

    private static Node getBestParent(Node newNode, ArrayList<Node> nodes) {
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

    private static ArrayList<Node> getNeighbors(Node newNode, ArrayList<Node> nodes) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.getDistance(newNode) < SEARCH_RADIUS) {
                neighbors.add(node);
            }
        }
        return neighbors;
    }

    private static void rewire(Node newNode, ArrayList<Node> nodes) {
        ArrayList<Node> neighbors = getNeighbors(newNode, nodes);
        for (Node neighbor : neighbors) {
            if (newNode.isCollisionFree(neighbor, obstacles)) {
                double cost = newNode.getCost() + newNode.getDistance(neighbor);
                if (cost < neighbor.getCost()) {
                    neighbor.setParent(newNode);
                    neighbor.setCost(cost);
                }
            }
        }
    }

    public static ArrayList<Node> search(Node start, Node goal, ArrayList<Node> obstacles, double xMin, double xMax, double yMin, double yMax) {
        RRTStarAlgo2.obstacles.clear();
        RRTStarAlgo2.obstacles.addAll(obstacles);

        ArrayList<Node> nodes = new ArrayList<Node>();
        nodes.add(start);

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            Node randomNode = generateRandomNode(xMin, xMax, yMin, yMax);
            Node nearestNode = getNearestNode(randomNode, nodes);
            Node newNode = nearestNode.moveTowards(randomNode, DELTA_Q);

            if (newNode.isCollisionFree(nearestNode, obstacles)) {
                Node bestParent = getBestParent(newNode, nodes);
                newNode.setParent(bestParent);
                newNode.setCost(bestParent.getCost() + bestParent.getDistance(newNode));

                nodes.add(newNode);
                rewire(newNode, nodes);

                if (newNode.getDistance(goal) < GOAL_RADIUS) {
                    goal.setParent(newNode);
                    goal.setCost(newNode.getCost() + newNode.getDistance(goal));
                    nodes.add(goal);
                    rewire(goal, nodes);
                    return getPath(start, goal);
                }
            }
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

class Node {
    private double x;
    private double y;
    private Node parent;
    private double cost;
    private double radius = -1;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Node(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getDistance(Node other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Node moveTowards(Node other, double maxDistance) {
        double distance = getDistance(other);
        if (distance <= maxDistance) {
            return other;
        } else {
            double ratio = maxDistance / distance;
            double dx = other.x - x;
            double dy = other.y - y;
            return new Node(x + dx * ratio, y + dy * ratio);
        }
    }

    public boolean isCollisionFree(Node other, ArrayList<Node> obstacles) {
        for (Node obstacle : obstacles) {
            if (isSegmentInCircle(this, other, obstacle, obstacle.getRadius())) {
                return false;
            }
        }
        return true;
    }

    private double getRadius() {
        return radius;
    }

    private boolean isSegmentInCircle(Node p1, Node p2, Node c, double r) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double a = dx * dx + dy * dy;
        double b = 2 * (dx * (p1.x - c.x) + dy * (p1.y - c.y));
        double cc = c.x * c.x + c.y * c.y + p1.x * p1.x + p1.y * p1.y - 2 * (c.x * p1.x + c.y * p1.y) - r * r;
        double deter = b * b - 4 * a * cc;

        if (deter < 0) {
            return false;
        }

        double e = Math.sqrt(deter);
        double u1 = (-b + e) / (2 * a);
        double u2 = (-b - e) / (2 * a);

        if (u1 >= 0 && u1 <= 1) {
            return true;
        }

        if (u2 >= 0 && u2 <= 1) {
            return true;
        }

        return false;
    }
}
