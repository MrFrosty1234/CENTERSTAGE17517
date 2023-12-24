
package org.woen.team17517.Robot;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class DrivetrainNew implements RobotModule{

    UltRobot robot;

    boolean autoMode = false;

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


    PidRegulator pidX = new PidRegulator(kPX, kIX, kDX, u_maxX);
    PidRegulator pidY = new PidRegulator(kPY, kIY, kDY, u_maxY);
    PidRegulator pidH = new PidRegulator(kPH, kIH, kDH, u_maxH);

    public void setTarget(double x, double y, double h) {
        targetX = x;
        targetH = h;
        targetY = y;
        autoMode = true;
    }

  //  void enableAutoMode(boolean )


    public DrivetrainNew(UltRobot robot) {
        this.robot = robot;
    }

     public void update() {
        if (autoMode) {
            double x = robot.odometry.y ;
            double y = robot.odometry.x;
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
            double H = pidH.update(errH);
            double X = pidX.update(errX);
            double Y = pidY.update(errY);


            if (abs(H)> maxSpeedR){
                H = signum(H)*maxSpeedR;            }
            if (abs(X)> maxSpeedL){
                X = signum(X)* maxSpeedL;
            }
            if (abs(Y)> maxSpeedL){
                Y = signum(Y)* maxSpeedL;
            }



            robot.driveTrainVelocityControl.moveRobotCord(X, Y, H);
        }
    }
    public void speedcontrol(){

    }


     public boolean isAtPosition() {
       // if((errX < minX) && (errY < minY) && (errH < minH)){
            return true;
       // }
       // return false;
    }
}
