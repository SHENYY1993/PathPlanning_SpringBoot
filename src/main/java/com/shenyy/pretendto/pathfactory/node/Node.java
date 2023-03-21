package com.shenyy.pretendto.pathfactory.node;

import java.util.ArrayList;

public class Node {
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