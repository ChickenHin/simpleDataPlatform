package com.meituan.qa.bean;

import com.meituan.qa.util.DateUtil;

public class AppkeyOctoData implements Comparable<AppkeyOctoData> {

    private long id;
    private int tp95;
    private int tp99;
    private int visitCount;
    private double successrate;
    private String datatime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    @Override
    public int compareTo(AppkeyOctoData aod) {
        if(DateUtil.strToTimeStamp(this.datatime) >= DateUtil.strToTimeStamp(aod.getDatatime())) {
            return 1;
        } else {
            return -1;
        }
 }
}
