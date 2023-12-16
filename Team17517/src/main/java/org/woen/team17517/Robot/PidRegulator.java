package org.woen.team17517.Robot;

import static java.lang.Math.abs;

public class PidRegulator { //TODO Feedforward
    double kP = 0;
    double kI = 0;
    double kD = 0;
    double ui = 0;
    double errold;
    double told; //TODO ElapsedTimeA
    double u_max;

    public PidRegulator(double p, double i, double d, double u_max) {
        kP = p;
        kI = i;
        kD = d;
        this.u_max = u_max;
    }
    public PidRegulator(double p, double i, double d) {
        kP = p;
        kI = i;
        kD = d;
        this.u_max = Double.POSITIVE_INFINITY;
    }


    public double update(double err) {
        double time = System.currentTimeMillis() / 1000.0;
        double up = err * kP;
        ui += (err * kI) * (time - told);
        if (abs(ui) > u_max) { //TODO parameter
            ui = u_max;
        }
        double ud = (err - errold) * kD / (time - told);
        errold = err;
        told = time;
        double u = up + ud + ui;
        return u;
    }

    public void reset() {
        ui = 0;
        told = System.currentTimeMillis() / 1000.0;
    }
}
