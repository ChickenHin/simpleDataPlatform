package com.meituan.qa.schedule;

import java.util.Date;
import java.util.Timer;

public class PullDataTimer {
    Timer timer;
    Date date;
    public PullDataTimer() {
        timer = new Timer();
        date = new Date();
        date.setHours(6);
        timer.schedule(new PullDataTask(), date);
    }
}
