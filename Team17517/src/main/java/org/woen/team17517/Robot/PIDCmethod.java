package org.woen.team17517.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Config
public  class PIDCmethod {

    public PIDCmethod( double kp,double ki, double kd){
            setCoificent(kp, ki, kd,0);
    }
    public PIDCmethod(double kp, double ki, double kd, double ks){
        setCoificent(kp,ki,kd,ks);

    }
    double kp,kd,ki,ks = 1;
    public void setCoificent(double kp,double ki,double kd,double ks){

        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.ks = ks;

    }
    double time = System.currentTimeMillis();
    double I,D,P,C,lastWrong = 0;
    public double PID(double target,double voltage,double enc,double ks) {
        double time1 = System.currentTimeMillis() / 1000.0-time;
        double wrong = target - enc;
        P = kp * wrong;

        I += wrong * time1 * ki;

        D = ((wrong - lastWrong) / time1) * kd;

        lastWrong = wrong;

        C = 12/voltage;

        double PID = (I + D + P)*C+target*ks;

        return PID;


    }
}