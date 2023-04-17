package com.shenyy.pretendto.utils.geometry;

/**
 * Created by SHENYY on 2021/06/05.
 * This class manages some 3-dimension vectors functions.
 */

public class Vector3 {
    private double iFactor;
    private double jFactor;
    private double kFactor;

    public Vector3(double i, double j, double k) {
        this.iFactor = i;
        this.jFactor = j;
        this.kFactor = k;
    }

    public double getiFactor() {
        return this.iFactor;
    }

    public double getjFactor() {
        return this.jFactor;
    }

    public double getkFactor() {
        return this.kFactor;
    }

    public void setiFactor(double iFactor) {
        this.iFactor = iFactor;
    }

    public void setjFactor(double jFactor) {
        this.jFactor = jFactor;
    }

    public void setkFactor(double kFactor) {
        this.kFactor = kFactor;
    }

    public Vector3 CrossProduct(Vector3 vector1, Vector3 vector2) {
        Vector3 result = new Vector3(0.0, 0.0, 0.0);
        double a1 = vector1.getiFactor();
        double a2 = vector1.getjFactor();
        double a3 = vector1.getkFactor();
        double b1 = vector2.getiFactor();
        double b2 = vector2.getjFactor();
        double b3 = vector2.getkFactor();
        result.setiFactor(a2 * b3 - a3 * b2);
        result.setjFactor(a3 * b1 - a1 * b3);
        result.setkFactor(a1 * b2 - a2 * b1);

        return result;
    }

}
