package com.shenyy.pretendto.pathfactory.algo;

import com.shenyy.pretendto.pathfactory.Path;
import com.shenyy.pretendto.pathfactory.dijkstra2.NodeGUI;
import com.shenyy.pretendto.pathfactory.dijkstra2.PathFinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgo extends PathAlgo {
    private int cityNum; // 城市数量
    private double[][] distanceMatrix; // 城市距离矩阵
    private List<Node> cityPosition = new ArrayList<>();
    private int[][] population; // 种群
    private int populationSize; // 种群大小
    private int maxGenerations; // 最大迭代次数
    private int curGen = 0; // 当前运行代数
    private double crossoverRate; // 交叉率
    private double mutationRate; // 变异率
    private double minFitness; // 最优个体适应度
    private int[] bestIndividual; // 最优个体

    private static final double LEN = 0.5; //补偿半格长度

    public GeneticAlgo(Path path) {
        super(path);
        //获取城市数量
        cityNum = 0;
        NodeGUI[][] map = PathFinding.getInstance().map;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j].getType() == 2) {
                    cityNum++;
                }
            }
        }

        maxGenerations = PathFinding.getInstance().MAX_GEN;
        crossoverRate = PathFinding.getInstance().param1;
        mutationRate = PathFinding.getInstance().param2;
        populationSize = (int) PathFinding.getInstance().param3;
    }

    @Override
    public void initialize() {
        System.out.println("Genetic algorithm initializing...");
        //初始化需要途径的点
        double[] x;
        double[] y;

        //获取城市分布
        NodeGUI[][] map = PathFinding.getInstance().map;

        distanceMatrix = new double[cityNum][cityNum];
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
            distanceMatrix[i][i] = 0; // 对角线为0
            for (int j = i + 1; j < cityNum; j++) {
                double rij = Math.sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j])));

                distanceMatrix[i][j] = rij;
                distanceMatrix[j][i] = distanceMatrix[i][j];
            }
        }

        minFitness = Double.MAX_VALUE;
        bestIndividual = new int[cityNum];
        population = initPopulation(populationSize);
    }

    @Override
    public void construct() {
        System.out.println("Genetic algorithm constructing 2D path...");
        run();
        /**GUI Update*/
        updateGui();
        PathFinding.getInstance().solving = false;
    }

    // 执行遗传算法
    public void run() {
        for (int i = 0; i < maxGenerations; i++) {
            if (!PathFinding.getInstance().solving) {
                break;
            }
            curGen = i;

            int[][] newPopulation = new int[populationSize][cityNum];
            for (int j = 0; j < populationSize; j++) {
                int[] parent1 = selection(population);
                int[] parent2 = selection(population);
                int[][] offspring = crossover(parent1, parent2);
                offspring[0] = mutation(offspring[0]);
                offspring[1] = mutation(offspring[1]);
                newPopulation[j] = evaluateFitness(offspring[0]) < evaluateFitness(offspring[1]) ? offspring[0] : offspring[1];
            }
            population = newPopulation;

            for (int[] individual : population) {
                double fitness = evaluateFitness(individual);
                if (fitness < minFitness) {
                    minFitness = fitness;
                    bestIndividual = individual;

                    /**GUI Update*/
                    updateGui();
                }
            }
        }
    }

    // 初始化种群，每个个体表示一个可行的旅行路径
    private int[][] initPopulation(int size) {
        int[][] population = new int[size][distanceMatrix.length];
        for (int i = 0; i < size; i++) {
            population[i] = generateIndividual();
        }
        return population;
    }

    // 随机生成一个个体（即随机排列城市序号）
    private int[] generateIndividual() {
        int[] individual = new int[distanceMatrix.length];
        for (int i = 0; i < individual.length; i++) {
            individual[i] = i;
        }
        Random rand = new Random();
        for (int i = individual.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = individual[i];
            individual[i] = individual[j];
            individual[j] = temp;
        }
        return individual;
    }

    // 计算一个个体的适应度（即旅行路径的总长度）
    private double evaluateFitness(int[] individual) {
        double fitness = 0;
        for (int i = 0; i < individual.length - 1; i++) {
            fitness += distanceMatrix[individual[i]][individual[i + 1]];
        }
        fitness += distanceMatrix[individual[individual.length - 1]][individual[0]];
        return fitness;
    }

    // 轮盘赌选择
    private int[] selection(int[][] population) {
        double totalFitness = 0;
        for (int[] individual : population) {
            totalFitness += evaluateFitness(individual);
        }
        double rand = Math.random() * totalFitness;
        double sum = 0;
        for (int[] individual : population) {
            sum += evaluateFitness(individual);
            if (sum >= rand) {
                return individual;
            }
        }
        return null;
    }

    // 部分映射交叉
    private int[][] crossover(int[] parent1, int[] parent2) {
        int[][] offspring = new int[2][parent1.length];
        Random rand = new Random();
        int cuttingPoint1 = rand.nextInt(parent1.length);
        int cuttingPoint2 = rand.nextInt(parent1.length);
        int start = Math.min(cuttingPoint1, cuttingPoint2);
        int end = Math.max(cuttingPoint1, cuttingPoint2);
        int[] mapping1 = new int[parent1.length];
        int[] mapping2 = new int[parent2.length];
        Arrays.fill(mapping1, -1);
        Arrays.fill(mapping2, -1);
        for (int i = start; i <= end; i++) {
            offspring[0][i] = parent1[i];
            offspring[1][i] = parent2[i];
            mapping1[parent1[i]] = parent2[i];
            mapping2[parent2[i]] = parent1[i];
        }
        for (int i = 0; i < parent1.length; i++) {
            if (i < start || i > end) {
                int p1 = parent1[i];
                int p2 = parent2[i];
                while (mapping1[p1] != -1) {
                    p1 = mapping1[p1];
                }
                while (mapping2[p2] != -1) {
                    p2 = mapping2[p2];
                }
                offspring[0][i] = p1;
                offspring[1][i] = p2;
            }
        }
        return offspring;
    }

    // 位变异
    private int[] mutation(int[] individual) {
        Random rand = new Random();
        if (rand.nextDouble() < mutationRate) {
            int index1 = rand.nextInt(individual.length);
            int index2 = rand.nextInt(individual.length);
            int temp = individual[index1];
            individual[index1] = individual[index2];
            individual[index2] = temp;
        }
        return individual;
    }

    /**
     * GUI Update
     */
    private void updateGui() {
        PathFinding.getInstance().checks = curGen;
        PathFinding.getInstance().length = minFitness;
        List<Node> path = new ArrayList<>();
        for (int index = 0; index < bestIndividual.length; index++) {
            path.add(new Node(cityPosition.get(bestIndividual[index]).getX(), cityPosition.get(bestIndividual[index]).getY()));
        }
        path.add(new Node(cityPosition.get(bestIndividual[0]).getX(), cityPosition.get(bestIndividual[0]).getY()));
        for (int index = 0; index < path.size() - 1; index++) {
            path.get(index).setParent(path.get(index + 1));
        }
        PathFinding.getInstance().linePath = path;
        PathFinding.getInstance().Update();
        PathFinding.getInstance().delay();
    }
}
