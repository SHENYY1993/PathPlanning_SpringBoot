package com.shenyy.pretendto.pathfactory.algo.ant;

import java.util.Random;
import java.util.Vector;

public class Ant implements Cloneable {

    private Vector<Integer> tabu; // 禁忌表
    private Vector<Integer> allowedCities; // 允许搜索的采样点
    private double[][] delta; // 信息数变化矩阵
    private double[][] distance; // 距离矩阵
    private double alpha;
    private double beta;

    private double tourLength; // 路径长度
    private int cityNum; // 采样点数量
    private int firstCity; // 起始采样点
    private int currentCity; // 当前采样点

    public Ant() {
        cityNum = 30;
        tourLength = 0;
    }

    /**
     * Constructor of Ant
     *
     * @param num 蚂蚁数量
     */
    public Ant(int num) {
        cityNum = num;
        tourLength = 0;
    }

    /**
     * 初始化蚂蚁，随机选择起始位置
     *
     * @param distance 距离矩阵
     * @param a        alpha
     * @param b        beta
     */

    public void init(double[][] distance, double a, double b) {
        alpha = a;
        beta = b;
        // 初始允许搜索的采样点集合
        allowedCities = new Vector<Integer>();
        // 初始禁忌表
        tabu = new Vector<Integer>();
        // 初始距离矩阵
        this.distance = distance;
        // 初始信息数变化矩阵为0
        delta = new double[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            Integer integer = new Integer(i);
            allowedCities.add(integer);
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0;
            }
        }
        // 随机挑选一个采样点作为起始采样点
        Random random = new Random(System.currentTimeMillis());
//        firstCity = random.nextInt(cityNum);//因为随机数生成不随机，导致蚁群初始城市分布不均匀，影响算法效果
        firstCity = (int) (1000 * Math.random()) % cityNum;

        // 允许搜索的采样点集合中移除起始采样点
        for (Integer i : allowedCities) {
            if (i.intValue() == firstCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 将起始采样点添加至禁忌表
        tabu.add(Integer.valueOf(firstCity));
        // 当前采样点为起始采样点
        currentCity = firstCity;
    }

    /**
     * 选择下一个采样点
     *
     * @param pheromone 信息素矩阵
     */

    public void selectNextCity(double[][] pheromone) {
        double[] p = new double[cityNum];
        double sum = 0.0;
        // 计算分母部分
        for (Integer i : allowedCities) {
            sum += Math.pow(pheromone[currentCity][i.intValue()], alpha)
                    * Math.pow(1.0 / distance[currentCity][i.intValue()], beta);
        }
        // 计算概率矩阵
        for (int i = 0; i < cityNum; i++) {
            boolean flag = false;
            for (Integer j : allowedCities) {
                if (i == j.intValue()) {
                    p[i] = (Math.pow(pheromone[currentCity][i], alpha) * Math.pow(1.0 / distance[currentCity][i], beta))
                            / sum;
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                p[i] = 0;
            }
        }
        // 轮盘赌选择下一个采样点
        Random random = new Random(System.currentTimeMillis());
        double selectP = random.nextDouble();
        int selectCity = 0;
        double sum1 = 0;
        for (int i = 0; i < cityNum; i++) {
            sum1 += p[i];
            if (sum1 >= selectP) {
                selectCity = i;
                break;
            }
        }
        // 从允许选择的采样点中去除select city
        for (Integer i : allowedCities) {
            if (i.intValue() == selectCity) {
                allowedCities.remove(i);
                break;
            }
        }
        // 在禁忌表中添加select city
        tabu.add(Integer.valueOf(selectCity));
        // 将当前采样点改为选择的采样点
        currentCity = selectCity;
    }

    /**
     * 计算路径长度
     *
     * @return 路径长度
     */
    private double calculateTourLength() {
        double len = 0;
        //禁忌表tabu最终形式：起始采样点,采样点1,采样点2...采样点n,起始采样点
        for (int i = 0; i < cityNum; i++) {
            len += distance[this.tabu.get(i).intValue()][this.tabu.get(i + 1)
                    .intValue()];
        }
        return len;
    }

    public Vector<Integer> getAllowedCities() {
        return allowedCities;
    }

    public void setAllowedCities(Vector<Integer> allowedCities) {
        this.allowedCities = allowedCities;
    }

    public double getTourLength() {
        tourLength = calculateTourLength();
        return tourLength;
    }

    public void setTourLength(double tourLength) {
        this.tourLength = tourLength;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public Vector<Integer> getTabu() {
        return tabu;
    }

    public void setTabu(Vector<Integer> tabu) {
        this.tabu = tabu;
    }

    public double[][] getDelta() {
        return delta;
    }

    public void setDelta(double[][] delta) {
        this.delta = delta;
    }

    public int getFirstCity() {
        return firstCity;
    }

    public void setFirstCity(int firstCity) {
        this.firstCity = firstCity;
    }

}