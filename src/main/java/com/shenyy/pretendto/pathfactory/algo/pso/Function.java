package com.shenyy.pretendto.pathfactory.algo.pso;

import com.shenyy.pretendto.pathfactory.Path2D;
import com.shenyy.pretendto.pathfactory.algo.ant.ACOAlgo;
import com.shenyy.pretendto.pathfactory.gui.PathFinding;

class Function {

    /**
     * Calculate the result of (x^4)-2(x^3).
     * Domain is (-infinity, infinity).
     * Minimum is -1.6875 at x = 1.5.
     *
     * @param x the x component
     * @return the y component
     */
    static double functionA(double x) {
        return Math.pow(x, 4) - 2 * Math.pow(x, 3);
    }

    /**
     * Perform Ackley's function.
     * Domain is [5, 5]
     * Minimum is 0 at x = 0 & y = 0.
     *
     * @param x the x component
     * @param y the y component
     * @return the z component
     */
    static double ackleysFunction(double x, double y) {
        double p1 = -20 * Math.exp(-0.2 * Math.sqrt(0.5 * ((x * x) + (y * y))));
        double p2 = Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y)));
        return p1 - p2 + Math.E + 20;
    }

    /**
     * Perform Booth's function.
     * Domain is [-10, 10]
     * Minimum is 0 at x = 1 & y = 3.
     *
     * @param x the x component
     * @param y the y component
     * @return the z component
     */
    static double boothsFunction(double x, double y) {
        double p1 = Math.pow(x + 2 * y - 7, 2);
        double p2 = Math.pow(2 * x + y - 5, 2);
        return p1 + p2;
    }

    /**
     * Perform the Three-Hump Camel function.
     *
     * @param x the x component
     * @param y the y component
     * @return the z component
     */
    static double threeHumpCamelFunction(double x, double y) {
        double p1 = 2 * x * x;
        double p2 = 1.05 * Math.pow(x, 4);
        double p3 = Math.pow(x, 6) / 6;
        return p1 - p2 + p3 + x * y + y * y;
    }

    /**
     * Perform the Three-Hump Camel function.
     *
     * @return the z component
     */
    static double acoParamFunction(double alpha, double beta, double rho, double numFactor) {
        PathFinding.getInstance().MAX_GEN = 100;
        PathFinding.getInstance().param1 = alpha;
        PathFinding.getInstance().param2 = beta;
        PathFinding.getInstance().param3 = rho;

        PathFinding.getInstance().solving = true;
        ACOAlgo pathAlgo = new ACOAlgo(new Path2D(null, null, null, null, null, null), 10, alpha, beta, rho);
        pathAlgo.initialize();
        pathAlgo.construct();

        double targetFunction = 0.5 * pathAlgo.bestLength + 0.2 * numFactor + 0.3 * ACOAlgo.bestGen;
        return targetFunction;
    }

}