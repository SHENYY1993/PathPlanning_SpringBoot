package com.shenyy.pretendto.pathfactory.algo.ant;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.algo.Node;
import com.shenyy.pretendto.pathfactory.algo.PathAlgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: Ant Colony Optimization
 * @author: shenyy
 * @date:
 */
public class ACOAlgo extends PathAlgo {
    private Ant[] ants; // 蚂蚁
    private int antNum; // 蚂蚁数量
    private int cityNum; // 采样点数量
    private int MAX_GEN; // 运行代数
    private float[][] pheromone; // 信息素矩阵
    private double[][] distance; // 距离矩阵
    private double bestLength; // 最佳长度
    private int[] bestTour; // 最佳路径

    // 三个参数
    private float alpha;
    private float beta;
    private float rho;

    public ACOAlgo(Path path) {
        super(path);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void construct() {

    }

    /**
     * constructor of ACO
     *
     * @param n 采样点数量
     * @param m 蚂蚁数量
     * @param g 运行代数
     * @param a alpha（信息素重要程度）
     * @param b beta（启发式因子重要程度）
     * @param r rho（信息素挥发系数）
     **/
    public ACOAlgo(Path path, int n, int m, int g, float a, float b, float r) {
        super(path);
        cityNum = n;
        antNum = m;
        ants = new Ant[antNum];
        MAX_GEN = g;
        alpha = a;
        beta = b;
        rho = r;
    }

    // 给编译器一条指令，告诉它对被批注的代码元素内部的某些警告保持静默
    @SuppressWarnings("resource")
    /**
     * 初始化ACO算法类
     */
    public void init(List<Node> sampleRoute) {
        // 读取数据
        double[] x;
        double[] y;

        distance = new double[cityNum][cityNum];
        x = new double[cityNum];
        y = new double[cityNum];
        for (int i = 0; i < cityNum; i++) {
            x[i] = sampleRoute.get(i).getX();// x坐标
            y[i] = sampleRoute.get(i).getY();// y坐标
        }
        // 计算距离矩阵
        // 针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，距离计算方法为伪欧氏距离
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++) {
                double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]))) * 10000000;

                distance[i][j] = rij;
                distance[j][i] = distance[i][j];
            }
        }
        distance[cityNum - 1][cityNum - 1] = 0;
        // 初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = 1f; // 初始化为1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new int[cityNum + 1];
        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta);
        }
    }

    /**
     * 初始化ACO算法类
     */
    public void init(double[][] distanceArr) {
        // 读取数据
        double[] x;
        double[] y;

        distance = distanceArr;

        // 初始化信息素矩阵
        pheromone = new float[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                pheromone[i][j] = 1f; // 初始化为1
            }
        }
        bestLength = Integer.MAX_VALUE;
        bestTour = new int[cityNum + 1];
        // 随机放置蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(cityNum);
            ants[i].init(distance, alpha, beta);
        }
    }

    public void solve() {
        // 迭代MAX_GEN次
        for (int g = 0; g < MAX_GEN; g++) {
            // antNum只蚂蚁
            for (int i = 0; i < antNum; i++) {
                // i这只蚂蚁走cityNum步，完整一个TSP
                for (int j = 1; j < cityNum; j++) {
                    ants[i].selectNextCity(pheromone);
                }

                if (g > 0) {
                    for (int k = 0; k < cityNum; k++) {
                        ants[0].getTabu().set(k, bestTour[k]);
                    }
                }
                // 把这只蚂蚁起始采样点加入其禁忌表中
                // 禁忌表最终形式：起始采样点,采样点1,采样点2...采样点n,起始采样点
                //ants[i].getTabu().add(ants[i].getFirstCity());
                ants[i].getTabu().add(ants[i].getTabu().get(0));
                // 查看这只蚂蚁行走路径距离是否比当前距离优秀
                if (ants[i].getTourLength() < bestLength) {
                    // 比当前优秀则拷贝优秀TSP路径
                    bestLength = ants[i].getTourLength();
                    for (int k = 0; k < cityNum + 1; k++) {
                        bestTour[k] = ants[i].getTabu().get(k).intValue();
                    }
                }
            }
            // 更新信息素
            updatePheromone();
            // 重新初始化蚂蚁
            for (int i = 0; i < antNum; i++) {
                ants[i].init(distance, alpha, beta);
            }
        }
    }

    //最优采样航线排序（依据bestTour排序sampleRoute）
    public Map<String, List<Node>> optSampleRoute(List<Node> sampleRoute,
                                                  //List<Point> sampleRouteAlt,
                                                  Node homePoint) {
        //多次循环最优结果顺序
        int[] optimumTourSeq = new int[cityNum + 1];

        //初始化最优采样航线optSamplePoints
        List<Node> optSamplePoints = new ArrayList<>();
        //List<Point> optSamplePointsAlt = new ArrayList<>();

        //原采样航线总距离
        double origTotalDist = 0.0;

        //原采样航线总距离计算
        for (int j = 0; j < sampleRoute.size(); j++) {
            if (j != 0) {
                origTotalDist += Math.sqrt(Math.pow(sampleRoute.get(j).getX() - sampleRoute.get(j - 1).getX(), 2) + Math.pow(sampleRoute.get(j).getY() - sampleRoute.get(j - 1).getY(), 2));
            } else {
                origTotalDist += Math.sqrt(Math.pow(sampleRoute.get(j).getX() - sampleRoute.get(sampleRoute.size() - 1).getX(), 2) + Math.pow(sampleRoute.get(j).getY() - sampleRoute.get(sampleRoute.size() - 1).getY(), 2));
            }
        }

        //最优采样航线初始化
        if (optSamplePoints.size() == 0) {
            for (int k = 0; k < sampleRoute.size(); k++) {
                optSamplePoints.add(sampleRoute.get(k));
                //optSamplePointsAlt.add(sampleRoute.get(k));
            }
            optSamplePoints.add(sampleRoute.get(0));
            //optSamplePointsAlt.add(sampleRoute.get(0));
        }
        //最优采样航线总距离赋初始值
        double optTotalDist = origTotalDist;

        //循环5次，计算最优的
        for (int i = 0; i < 1; i++) {
            List<Node> optSamplePointsTemp = new ArrayList<>();
            //List<Point> optSamplePointsAltTemp = new ArrayList<>();
            double totalDist = 0.0;

            init(sampleRoute);
            //单次最优路径计算
            solve();

            //最优采样航线航点排序赋值
            for (int j = 0; j < sampleRoute.size() + 1; j++) {
                optSamplePointsTemp.add(sampleRoute.get(bestTour[j]));
                //optSamplePointsAltTemp.add(sampleRouteAlt.get(bestTour[j]));
                //计算最优采样航线总距离
                if (j != 0) {
                    totalDist += Math.sqrt(Math.pow(optSamplePointsTemp.get(j).getX() - optSamplePointsTemp.get(j - 1).getX(), 2) + Math.pow(optSamplePointsTemp.get(j).getY() - optSamplePointsTemp.get(j - 1).getY(), 2));
                }
            }

            //记录最优采样航线
            if (optTotalDist >= totalDist) {
                optTotalDist = totalDist;
                for (int k = 0; k < sampleRoute.size() + 1; k++) {
                    optimumTourSeq[k] = bestTour[k];
                }
                if (optSamplePoints.size() == 0) {
                    for (int k = 0; k < sampleRoute.size() + 1; k++) {
                        optSamplePoints.add(optSamplePointsTemp.get(k));
                        //optSamplePointsAlt.add(optSamplePointsAltTemp.get(k));
                    }
                } else {
                    for (int k = 0; k < sampleRoute.size() + 1; k++) {
                        optSamplePoints.set(k, optSamplePointsTemp.get(k));
                        //optSamplePointsAlt.set(k,optSamplePointsAltTemp.get(k));
                    }
                }
            }
        }

        //计算距离home点最近的点
        int origPointIndex = 0;
        double distToHome = Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(origPointIndex).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(origPointIndex).getY(), 2));
        for (int k = 0; k < optSamplePoints.size() - 1; k++) {
            if (Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(k).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(k).getY(), 2)) < distToHome) {
                distToHome = Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(k).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(k).getY(), 2));
                origPointIndex = k;
            }
        }

        //以距离home点最近的点为起点排序optimumTourSeq, optSamplePoints及optSamplePointsAlt
        List<Node> tempPointsList = new ArrayList<>();
        //List<Point> tempPointsAltList = new ArrayList<>();
        int[] tempTourSeq = new int[cityNum + 1];
        for (int k = 0; k < optSamplePoints.size() - origPointIndex; k++) {
            tempPointsList.add(optSamplePoints.get(origPointIndex + k));
            //tempPointsAltList.add(optSamplePointsAlt.get(origPointIndex + k));
            tempTourSeq[k] = optimumTourSeq[origPointIndex + k];
        }
        for (int k = 1; k < origPointIndex; k++) {
            tempPointsList.add(optSamplePoints.get(k));
            //tempPointsAltList.add(optSamplePointsAlt.get(k));
            tempTourSeq[optSamplePoints.size() - origPointIndex - 1 + k] = optimumTourSeq[k];
        }
        tempPointsList.add(tempPointsList.get(0));
        //tempPointsAltList.add(tempPointsAltList.get(0));
        tempTourSeq[optSamplePoints.size() - 1] = optimumTourSeq[origPointIndex];

        for (int k = 0; k < optSamplePoints.size(); k++) {
            optSamplePoints.set(k, tempPointsList.get(k));
            //optSamplePointsAlt.set(k,tempPointsAltList.get(k));
            optimumTourSeq[k] = tempTourSeq[k];
        }

        //多次循环最优结果顺序返回给bestTour
        for (int k = 0; k < sampleRoute.size() + 1; k++) {
            bestTour[k] = optimumTourSeq[k];
        }

        // 打印最佳结果
//        printOptimal();

        //打印最优采样点航线纬度数据列
        //打印航线经度数据列
//		System.out.print("最优采样航线经度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.getY()));
//		}
//		//打印航线纬度数据列
//		System.out.print("最优采样航线纬度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.getX()));
//		}
//		//打印航线高度数据列
//		System.out.print("最优采样航线高度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.height));
//		}
//
//		System.out.print("最优采样航线输出完成！\n");
//		//打印距离优化值optTotalDist
//		System.out.print(String.format("总距离：%.10f  ",optTotalDist));
//		if(origTotalDist != 0) {
//			System.out.print(String.format("距离优化百分比：%.2f", 100 * (origTotalDist - optTotalDist) / origTotalDist));
//		}
//		System.out.print("%\n\n");

        //步骤5：返回采样点航线
        Map<String, List<Node>> response = new HashMap<>();
        response.put("optSampleRoute", optSamplePoints);
        //response.put("optSampleRouteAlt",optSamplePointsAlt);
        return response;
    }

    /**
     * @author: shenyy
     * @methodsName: segmentSort
     * @description: 线段排序计算
     * @param: sampleRoute 填充线段端点
     * @return: 排序顺序
     * @throws:
     */
    //最优采样航线排序（依据bestTour排序sampleRoute）
    public int[] segmentSort(List<Node> sampleRoute,
                             Node homePoint,
                             double[][] distanceArr) {
        //多次循环最优结果顺序
        int[] optimumTourSeq = new int[cityNum + 1];

        //初始化最优采样航线optSamplePoints
        List<Node> optSamplePoints = new ArrayList<>();
        //List<Point> optSamplePointsAlt = new ArrayList<>();

        //原采样航线总距离
        double origTotalDist = 0.0;

        //原采样航线总距离计算
        for (int j = 0; j < sampleRoute.size(); j++) {
            if (j != 0) {
                origTotalDist += Math.sqrt(Math.pow(sampleRoute.get(j).getX() - sampleRoute.get(j - 1).getX(), 2) + Math.pow(sampleRoute.get(j).getY() - sampleRoute.get(j - 1).getY(), 2));
            } else {
                origTotalDist += Math.sqrt(Math.pow(sampleRoute.get(j).getX() - sampleRoute.get(sampleRoute.size() - 1).getX(), 2) + Math.pow(sampleRoute.get(j).getY() - sampleRoute.get(sampleRoute.size() - 1).getY(), 2));
            }
        }

        //最优采样航线初始化
        if (optSamplePoints.size() == 0) {
            for (int k = 0; k < sampleRoute.size(); k++) {
                optSamplePoints.add(sampleRoute.get(k));
                //optSamplePointsAlt.add(sampleRoute.get(k));
            }
            optSamplePoints.add(sampleRoute.get(0));
            //optSamplePointsAlt.add(sampleRoute.get(0));
        }
        //最优采样航线总距离赋初始值
        double optTotalDist = origTotalDist;

        //循环5次，计算最优的
        for (int i = 0; i < 1; i++) {
            List<Node> optSamplePointsTemp = new ArrayList<>();
            //List<Point> optSamplePointsAltTemp = new ArrayList<>();
            double totalDist = 0.0;

            init(distanceArr);
            //单次最优路径计算
            solve();

            //最优采样航线航点排序赋值
            for (int j = 0; j < sampleRoute.size() + 1; j++) {
                optSamplePointsTemp.add(sampleRoute.get(bestTour[j]));
                //optSamplePointsAltTemp.add(sampleRouteAlt.get(bestTour[j]));
                //计算最优采样航线总距离
                if (j != 0) {
                    totalDist += Math.sqrt(Math.pow(optSamplePointsTemp.get(j).getX() - optSamplePointsTemp.get(j - 1).getX(), 2) + Math.pow(optSamplePointsTemp.get(j).getY() - optSamplePointsTemp.get(j - 1).getY(), 2));
                }
            }

            //记录最优采样航线
            if (optTotalDist >= totalDist) {
                optTotalDist = totalDist;
                for (int k = 0; k < sampleRoute.size() + 1; k++) {
                    optimumTourSeq[k] = bestTour[k];
                }
                if (optSamplePoints.size() == 0) {
                    for (int k = 0; k < sampleRoute.size() + 1; k++) {
                        optSamplePoints.add(optSamplePointsTemp.get(k));
                        //optSamplePointsAlt.add(optSamplePointsAltTemp.get(k));
                    }
                } else {
                    for (int k = 0; k < sampleRoute.size() + 1; k++) {
                        optSamplePoints.set(k, optSamplePointsTemp.get(k));
                        //optSamplePointsAlt.set(k,optSamplePointsAltTemp.get(k));
                    }
                }
            }
        }

        //计算距离home点最近的点
        int origPointIndex = 0;
        double distToHome = Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(origPointIndex).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(origPointIndex).getY(), 2));
        for (int k = 0; k < optSamplePoints.size() - 1; k++) {
            if (Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(k).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(k).getY(), 2)) < distToHome) {
                distToHome = Math.sqrt(Math.pow(homePoint.getX() - optSamplePoints.get(k).getX(), 2) + Math.pow(homePoint.getY() - optSamplePoints.get(k).getY(), 2));
                origPointIndex = k;
            }
        }

        //以距离home点最近的点为起点排序optimumTourSeq, optSamplePoints及optSamplePointsAlt
        List<Node> tempPointsList = new ArrayList<>();
        //List<Point> tempPointsAltList = new ArrayList<>();
        int[] tempTourSeq = new int[cityNum + 1];
        for (int k = 0; k < optSamplePoints.size() - origPointIndex; k++) {
            tempPointsList.add(optSamplePoints.get(origPointIndex + k));
            //tempPointsAltList.add(optSamplePointsAlt.get(origPointIndex + k));
            tempTourSeq[k] = optimumTourSeq[origPointIndex + k];
        }
        for (int k = 1; k < origPointIndex; k++) {
            tempPointsList.add(optSamplePoints.get(k));
            //tempPointsAltList.add(optSamplePointsAlt.get(k));
            tempTourSeq[optSamplePoints.size() - origPointIndex - 1 + k] = optimumTourSeq[k];
        }
        tempPointsList.add(tempPointsList.get(0));
        //tempPointsAltList.add(tempPointsAltList.get(0));
        tempTourSeq[optSamplePoints.size() - 1] = optimumTourSeq[origPointIndex];

        for (int k = 0; k < optSamplePoints.size(); k++) {
            optSamplePoints.set(k, tempPointsList.get(k));
            //optSamplePointsAlt.set(k,tempPointsAltList.get(k));
            optimumTourSeq[k] = tempTourSeq[k];
        }

        //多次循环最优结果顺序返回给bestTour
        for (int k = 0; k < sampleRoute.size() + 1; k++) {
            bestTour[k] = optimumTourSeq[k];
        }

        // 打印最佳结果
        printOptimal();

        //打印最优采样点航线纬度数据列
        //打印航线经度数据列
//		System.out.print("最优采样航线经度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.getY()));
//		}
//		//打印航线纬度数据列
//		System.out.print("最优采样航线纬度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.getX()));
//		}
//		//打印航线高度数据列
//		System.out.print("最优采样航线高度序列：\n");
//		for (Point wayPoint : optSamplePoints) {
//			System.out.print(String.format("%.14f\n", wayPoint.height));
//		}
//
//		System.out.print("最优采样航线输出完成！\n");
//		//打印距离优化值optTotalDist
//		System.out.print(String.format("总距离：%.10f  ",optTotalDist));
//		if(origTotalDist != 0) {
//			System.out.print(String.format("距离优化百分比：%.2f", 100 * (origTotalDist - optTotalDist) / origTotalDist));
//		}
//		System.out.print("%\n\n");

        //步骤5：返回排序结果
        return bestTour;
    }

    // 更新信息素
    private void updatePheromone() {
        // 信息素挥发
        for (int i = 0; i < cityNum; i++)
            for (int j = 0; j < cityNum; j++)
                pheromone[i][j] = pheromone[i][j] * (1 - rho);

        // 信息素更新
        for (int i = 0; i < antNum; i++) {
            //新增测试
            // 更新这只蚂蚁的信息数变化矩阵，对称矩阵
            for (int j = 0; j < cityNum; j++) {
                ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i]
                        .getTabu().get(j + 1).intValue()] = (float) (1. / ants[i]
                        .getTourLength());
                ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i]
                        .getTabu().get(j).intValue()] = (float) (1. / ants[i]
                        .getTourLength());
            }
            for (int j = 0; j < cityNum; j++) {
                for (int k = 0; k < antNum; k++) {
                    pheromone[i][j] += ants[k].getDelta()[i][j];
                }
            }
        }
    }

    private void printOptimal() {
        System.out.println("The optimal length is: " + bestLength);
        System.out.println("The optimal tour is: ");
        for (int i = 0; i < cityNum + 1; i++) {
            System.out.println(bestTour[i]);
        }
    }
}
