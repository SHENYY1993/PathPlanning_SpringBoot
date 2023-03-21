package com.shenyy.pretendto.pathfactory.algo.ant;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.node.Node;
import com.shenyy.pretendto.pathfactory.algo.PathAlgo;
import com.shenyy.pretendto.pathfactory.node.NodeGrid;
import com.shenyy.pretendto.pathfactory.gui.PathFinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Ant Colony Optimization
 * @author: shenyy
 * @date:
 */
public class ACOAlgo extends PathAlgo {
    private Ant[] ants; // 蚂蚁
    private int antNum; // 蚂蚁数量
    private int cityNum; // 采样点数量
    private List<Node> cityPosition = new ArrayList<>();
    private int MAX_GEN; // 运行代数
    private int curGen = 0; // 当前运行代数
    private double[][] pheromone; // 信息素矩阵
    private double[][] distance; // 距离矩阵
    private double bestLength; // 最佳长度
    private int[] bestTour; // 最佳路径

    // 三个参数
    private double alpha;
    private double beta;
    private double rho;


    private static final double LEN = 0.5; //补偿半格长度

    public ACOAlgo(Path path) {
        super(path);
    }

    /**
     * constructor of ACO
     *
     * @param g 运行代数
     * @param a alpha（信息素重要程度）
     * @param b beta（启发式因子重要程度）
     * @param r rho（信息素挥发系数）
     **/
    public ACOAlgo(Path path, int g, double a, double b, double r) {
        super(path);
//        cityNum = n;

        //获取城市数量
        cityNum = 0;
        NodeGrid[][] map = PathFinding.getInstance().map;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getType() == 2) {
                    cityNum++;
                }
            }
        }

        antNum = (int) (cityNum * 1.5);
        ants = new Ant[antNum];
        MAX_GEN = PathFinding.getInstance().MAX_GEN;
//        alpha = a;
//        beta = b;
//        rho = r;
        alpha = PathFinding.getInstance().param1;
        beta = PathFinding.getInstance().param2;
        rho = PathFinding.getInstance().param3;
    }

    @Override
    public void initialize() {
        System.out.println("ACO algorithm initializing...");
        MAX_GEN = PathFinding.getInstance().MAX_GEN;
        alpha = PathFinding.getInstance().param1;
        beta = PathFinding.getInstance().param2;
        rho = PathFinding.getInstance().param3;

        //初始化需要途径的点
        double[] x;
        double[] y;

        //获取城市分布
        NodeGrid[][] map = PathFinding.getInstance().map;

        distance = new double[cityNum][cityNum];
        x = new double[cityNum];
        y = new double[cityNum];

        int index = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getType() == 2) {
                    x[index] = map[i][j].getX() + LEN;
                    y[index] = map[i][j].getY() + LEN;
                    cityPosition.add(new Node(x[index], y[index]));
                    index++;
                }
            }
        }

        // 计算距离矩阵
        // 针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，距离计算方法为伪欧氏距离
        for (int i = 0; i < cityNum - 1; i++) {
            distance[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++) {
                double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])));

                distance[i][j] = rij;
                distance[j][i] = distance[i][j];
            }
        }

        init(distance);
    }

    @Override
    public void construct() {
        System.out.println("ACO algorithm constructing 2D path...");
        solve();

        /**GUI Update*/
        updateGui();
        PathFinding.getInstance().solving = false;
    }

    /**
     * 初始化ACO算法类
     */
    public void init(double[][] distanceArr) {
        distance = distanceArr;

        // 初始化信息素矩阵
        pheromone = new double[cityNum][cityNum];
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
            if (!PathFinding.getInstance().solving) {
                break;
            }
            curGen = g;

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

            /**GUI Update*/
            updateGui();
        }
    }

    // 更新信息素
    private void updatePheromone() {
        // 信息素挥发
        for (int i = 0; i < cityNum; i++)
            for (int j = 0; j < cityNum; j++)
                pheromone[i][j] = pheromone[i][j] * (1 - rho);

        // 信息素更新
//        for (int i = 0; i < antNum; i++) { //BUG: 信息素更新应该是cityNum * cityNum的矩阵
        for (int i = 0; i < cityNum; i++) {
            // 更新这只蚂蚁的信息数变化矩阵，对称矩阵
            for (int j = 0; j < cityNum; j++) {
                ants[i].getDelta()[ants[i].getTabu().get(j).intValue()][ants[i].getTabu().get(j + 1).intValue()]
                        = (1. / ants[i].getTourLength());
                ants[i].getDelta()[ants[i].getTabu().get(j + 1).intValue()][ants[i].getTabu().get(j).intValue()]
                        = (1. / ants[i].getTourLength());
            }
            for (int j = 0; j < cityNum; j++) {
                for (int k = 0; k < antNum; k++) {
                    pheromone[i][j] += ants[k].getDelta()[i][j];
                }
            }
        }
    }

    /**
     * GUI Update
     */
    private void updateGui() {
        PathFinding.getInstance().checks = curGen;
        PathFinding.getInstance().length = bestLength;
        List<Node> path = new ArrayList<>();
        for (int index = 0; index < bestTour.length; index++) {
            path.add(new Node(cityPosition.get(bestTour[index]).getX(), cityPosition.get(bestTour[index]).getY()));
        }
        for (int index = 0; index < path.size() - 1; index++) {
            path.get(index).setParent(path.get(index + 1));
        }
        PathFinding.getInstance().linePath = path;
        PathFinding.getInstance().Update();
        PathFinding.getInstance().delay();
    }
}
