package com.shenyy.pretendto.utils.geometry;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    public static void main(String[] args) {
        //测试pointInPolygon
        Point2D[] points = new Point2D[6];
        points[0] = new Point2D(0, 0);
        points[1] = new Point2D(2, 0);
        points[2] = new Point2D(2, 1);
        points[3] = new Point2D(1, 1);
        points[4] = new Point2D(1, 2);
        points[5] = new Point2D(0, 2);
        System.out.println("polygon");

        List<Point2D> pointList = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            pointList.add(points[i]);
            System.out.println(points[i].getX() + "," + points[i].getY());
        }
//        Point2D testPoint = new Point2D(-0.5, 0);
//        System.out.println(testPoint + ": " + pointInPolygon(pointList, testPoint));
        for (int i = 0; i < 100; i++) {
            Point2D testPoint = new Point2D(6 * Math.random() - 2, 6 * Math.random() - 2);
            System.out.println(testPoint.getX() + "," + testPoint.getY() + "," + (pointInPolygon(points, testPoint) ? 1 : 0));
        }
    }

    /*****属性*****/
    private List<Point2D> point;

    public Polygon(List<Point2D> point) {
        this.point = point;
    }

    public List<Point2D> getPoint() {
        return point;
    }

    public void setPoint(List<Point2D> point) {
        this.point = point;
    }

    /**
     * @description: 射线法判断点是否在多边形（凸/凹）范围内
     * @author: shenyy
     * @date:
     * @param:
     * @return:
     */
    public static boolean pointInPolygon(Point2D[] polygon, Point2D point) {
        int n = polygon.length;
        if (n < 3) return false;

        //获取最大的x值
        double xMax = point.getX() + 100;
        for (int i = 0; i < n; i++) {
            if (polygon[i].getX() > xMax) {
                xMax = polygon[i].getX() + 100;
            }
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            Point2D p1 = polygon[i];
            Point2D p2 = polygon[(i + 1) % n];
            double sin = (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) - (point.getX() - p1.getX()) * (p2.getY() - p1.getY());
            if (Math.abs(sin) < 1e-9) {// 如果sin值过小，则认为连线与边界平行，忽略该边界
                continue;
            }

            if ((p1.getY() < point.getY() && p2.getY() >= point.getY())
                    || (p2.getY() < point.getY() && p1.getY() >= point.getY())) {
                boolean cross = SegCross(point, new Point2D(xMax, point.getY()), p1, p2);
                if (cross) {
                    count++;
                }
            }
        }
        return count % 2 == 1;
    }

    /**
     * @description: 射线法判断点是否在多边形（凸/凹）范围内
     * @author: shenyy
     * @date:
     * @param:
     * @return:
     */
    public static boolean pointInPolygon(List<Point2D> polygon, Point2D point) {
        int n = polygon.size();
        if (n < 3) return false;

        //获取最大的x值
        double xMax = point.getX() + 100;
        for (int i = 0; i < n; i++) {
            if (polygon.get(i).getX() > xMax) {
                xMax = polygon.get(i).getX() + 100;
            }
        }

        int count = 0;
        for (int i = 0; i < n; i++) {
            Point2D p1 = polygon.get(i);
            Point2D p2 = polygon.get((i + 1) % n);
            double sin = (point.getY() - p1.getY()) * (p2.getX() - p1.getX()) - (point.getX() - p1.getX()) * (p2.getY() - p1.getY());
            if (Math.abs(sin) < 1e-9) {// 如果sin值过小，则认为连线与边界平行，忽略该边界
                continue;
            }

            if ((p1.getY() < point.getY() && p2.getY() >= point.getY())
                    || (p2.getY() < point.getY() && p1.getY() >= point.getY())) {
                boolean cross = SegCross(point, new Point2D(xMax, point.getY()), p1, p2);
                if (cross) {
                    count++;
                }
            }
        }
        return count % 2 == 1;
    }

    /**
     * @author: shenyy
     * @methodsName: SegCross
     * @description: 两线段是否相交判断
     * @param: segA1 线段A点1
     * segA2 线段A点2
     * segB1 线段B点1
     * segB2 线段B点2
     * @return: crossFlag 相交标识符
     * @throws:
     */
    public static boolean SegCross(Point2D segA1, Point2D segA2, Point2D segB1, Point2D segB2) {
        boolean crossFlag = false;
        //三维向量构建
        Vector3 vecA1B1 = new Vector3(segB1.getX() - segA1.getX(), segB1.getY() - segA1.getY(), 0);
        Vector3 vecA2B1 = new Vector3(segB1.getX() - segA2.getX(), segB1.getY() - segA2.getY(), 0);
        Vector3 vecB2B1 = new Vector3(segB1.getX() - segB2.getX(), segB1.getY() - segB2.getY(), 0);

        Vector3 vecB1A1 = new Vector3(segA1.getX() - segB1.getX(), segA1.getY() - segB1.getY(), 0);
        Vector3 vecB2A1 = new Vector3(segA1.getX() - segB2.getX(), segA1.getY() - segB2.getY(), 0);
        Vector3 vecA2A1 = new Vector3(segA1.getX() - segA2.getX(), segA1.getY() - segA2.getY(), 0);

        //叉乘计算,点乘计算
        Vector3 vecCross1 = new Vector3(0.0, 0.0, 0.0);
        Vector3 vecCross2 = new Vector3(0.0, 0.0, 0.0);
        vecCross1 = vecCross1.CrossProduct(vecA1B1, vecB2B1);
        vecCross2 = vecCross2.CrossProduct(vecA2B1, vecB2B1);
        double dotResult1 = vecCross1.getiFactor() * vecCross2.getiFactor() + vecCross1.getjFactor() * vecCross2.getjFactor() + vecCross1.getkFactor() * vecCross2.getkFactor();

        vecCross1 = vecCross1.CrossProduct(vecB1A1, vecA2A1);
        vecCross2 = vecCross2.CrossProduct(vecB2A1, vecA2A1);
        double dotResult2 = vecCross1.getiFactor() * vecCross2.getiFactor() + vecCross1.getjFactor() * vecCross2.getjFactor() + vecCross1.getkFactor() * vecCross2.getkFactor();

        //判断线段是否相交
        if (dotResult1 < 0 && dotResult2 < 0) {
            crossFlag = true;
        }

        //返回结果
        return crossFlag;
    }
}
