package com.meituan.qa.bean;

public class AppkeyData {

    private int tp95;
    private int tp99;
    private int visitCount;
    private double successrate;

    public int getTp95() {
        return tp95;
    }

    public void setTp95(int tp95) {
        this.tp95 = tp95;
    }

    public int getTp99() {
        return tp99;
    }

    public void setTp99(int tp99) {
        this.tp99 = tp99;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public double getSuccessrate() {
        return successrate;
    }

    public void setSuccessrate(double successrate) {
        this.successrate = successrate;
    }

}
