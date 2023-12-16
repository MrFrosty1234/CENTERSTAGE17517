package org.woen.testcode;

import static java.lang.Math.abs;

public class Obnova {
    double ui;
    double told;
    double errold;
    double kp = 1;
    double kd = 1;
    double ki = 1;
    double err;

    public Obnova(){
        told=1;
        ui = 0;
        errold = 0;
        err = 0;
    }

    double funkcia(double err){

        double time = System.currentTimeMillis() / 1000.0;
        double up = err * kp;
        ui += (err * ki) * (time - told);
        if (abs(ui) > 0.25) {
            ui = 0.25;
        }
        double udr = (err - errold) * kd / (time - told);
        errold = err;
        told = time;
        return up + udr + ui;
    }
}
