package org.woen.team17517.Service;
import com.acmerobotics.dashboard.config.Config;
@Config
public  class PID {
    public PID(double kp, double ki, double kd, Double maxI){
            setCoefficent(kp, ki, kd,0,maxI);
    }
    public PID(double kp, double ki, double kd, double ks, Double maxI){
        setCoefficent(kp,ki,kd,ks,maxI);
    }
    private double kp,kd,ki,ks = 0;
    Double maxI = Double.POSITIVE_INFINITY;
    public void setCoefficent(double kp, double ki, double kd, double ks,Double maxI){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.ks = ks;
        this.maxI = maxI;
    }
    private double timeOld = System.nanoTime();
    private Double deltaTime = null;
    private double lastWrong = 0;
    private double P = 0;
    private double D = 0;
    private double I = 0;
    private double V = 0;
    public double getI(){
        return I;
    }
    public double getP(){return P;}
    public double getD(){return D;}
    public double pid(double target, double enc, double voltage) {
        double wrong = target - enc;
        P = kp * wrong;
        if (deltaTime != null) {
            I = I + wrong * deltaTime * ki;

            D = ((wrong - lastWrong) / deltaTime) * kd;
        }else{
            I = 0;
            D = 0;
        }
        lastWrong = wrong;

        V = 12/voltage;
        if(I>maxI){
            I = maxI;
        }
        if(I<-maxI){
            I = -maxI;
        }
        double PID = (I + D + P)* V +target*ks;
        double timeNow = (double) System.nanoTime() / 1_000_000d;
        deltaTime = Double.valueOf(timeNow - timeOld);
        timeOld = timeNow;
        return PID;

    }
}