
package org.woen.team17517.RobotModules;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

import org.woen.team17517.Service.PIDMethod;
import org.woen.team17517.Service.RobotModule;

public class DrivetrainNew implements RobotModule {

    UltRobot robot;

    boolean autoMode = false;
    double voltage ;
    public static double kPX = 1;
    public static double kDX = 0;
    public static double kIX = 0;

    public static double kPY = 1;
    public static double kDY = 0;
    public static double kIY = 0;

    public static double kPH = 1;
    public static double kDH = 0;
    public static double kIH = 0;

    public static double u_maxX = 0;
    public static double u_maxH = 0;
    public static double u_maxY = 0;

    private double targetX = 0;
    private double targetH = 0;
    private double targetY = 0;

    double errX = 0;
    double errY = 0;
    double errH = 0;

    public static double minX = 1;
    public static double minH = 1;
    public static double minY = 1;

    private static double diameter = 0.098;
    private static double trackLength = 27d/2d;
    public double maxSpeedL = diameter*PI/0.2;
    public double maxSpeedR = Math.toDegrees(maxSpeedL /trackLength);;


    PIDMethod pidX = new PIDMethod(kPX, kIX, kDX, u_maxX);
    PIDMethod pidY = new PIDMethod(kPY, kIY, kDY, u_maxY);
    PIDMethod pidH = new PIDMethod(kPH, kIH, kDH, u_maxH);

    public void setTarget(double x, double y, double h) {
        targetX = x;
        targetH = h;
        targetY = y;
        autoMode = true;
    }

  //  void enableAutoMode(boolean )


    public DrivetrainNew(UltRobot robot) {
        this.robot = robot;
        voltage = 12;
    }

     public void update() {
        voltage = robot.voltageSensorPoint.getVol();
        pidH.setCoefficent(kPH,kIH,kDH,0,u_maxH);
        pidX.setCoefficent(kPX,kIX,kDX,0, u_maxX);
        pidX.setCoefficent(kPY,kIY,kDY,0, u_maxY);
        if (autoMode) {
            double x = robot.odometry.x ;
            double y = robot.odometry.y;
            double h = robot.odometry.heading;
            errX = targetX - x;
            errY = targetY - y;

            errH = targetH - h;
            while (errH >= 180) {
                errH = errH - 360;
            }

            while (errH < -180) {
                errH = errH + 360;
            }
            double H = pidH.PID(targetH,h,voltage);
            double X = pidX.PID(targetX,x,voltage);
            double Y = pidY.PID(targetY,y,voltage);


            if (abs(H)> maxSpeedR){
                H = signum(H)*maxSpeedR;            }
            if (abs(X)> maxSpeedL){
                X = signum(X)* maxSpeedL;
            }
            if (abs(Y)> maxSpeedL){
                Y = signum(Y)* maxSpeedL;
            }



            robot.driveTrainVelocityControl.moveGlobalCord(Y, -X, H);
        }
    }
    public void speedcontrol(){
    }


    @Override
    public boolean isAtPosition() {
       if((errX < minX) && (errY < minY) && (errH < minH)){
           return true;
       }
        return false;
    }
}
