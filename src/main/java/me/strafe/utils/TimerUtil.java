package me.strafe.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    public static boolean TimeOver = true;

    public static void runTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            int counter = 3;
            //Countdown clock in seconds
            @Override
            public void run() {
                if(counter>0) {
                    counter--;
                } else {
                    timer.cancel();
                    TimeOver = false;
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}
