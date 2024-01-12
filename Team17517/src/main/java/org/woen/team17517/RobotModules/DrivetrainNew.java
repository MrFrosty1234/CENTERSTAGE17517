
package org.woen.team17517.RobotModules;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team17517.Service.PIDMethod;
import org.woen.team17517.Service.RobotModule;

import java.util.HashMap;

@Config
public class DrivetrainNew implements RobotModule {
    UltRobot robot;

    boolean autoMode = false;
    double voltage ;
    public static double kPX = 1.2;
    public static double kDX = 0;
    public static double kIX = 2;

    public static double kPY = 1.2;
    public static double kDY = 0;
    public static double kIY = 2;

    public static double kPH = 1;
    public static double kDH = 0;
    public static double kIH = 0;

    public static double u_maxX = 0;
    public static double u_maxH = 0;
    public static double u_maxY = 0;

    private double targetX = 0;
    private double targetH = 0;
    private double targetY = 0;
    public HashMap<String,Double> getTargets() {
        HashMap<String,Double> targetMap = new HashMap<>();
        targetMap.put("X",targetX);
        targetMap.put("H",targetH);
        targetMap.put("Y",targetY);
        return targetMap;
    }
    private double errX = 0;
    private double errY = 0;
    private double errH = 0;
    public HashMap<String,Double> getErrors() {
        HashMap<String,Double> errorMap = new HashMap<>();
        errorMap.put("X",errX);
        errorMap.put("H",errH);
        errorMap.put("Y",errY);
        return errorMap;
    }
    public static double minX = 100;
    public static double minH = 100;
    public static double minY = 100;

    private static double diameter = 9.8;
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
        targetH = 0;
        targetX = 0;
        targetY = 0;
    }
    private double x;
    private double y;
    private double h;
    public HashMap<String,Double> getPosition(){
        HashMap<String,Double> positionMap = new HashMap<>();
        positionMap.put("X",x);
        positionMap.put("Y",y);
        positionMap.put("H",h);
        return positionMap;
    }
     public void update() {
        voltage = robot.voltageSensorPoint.getVol();
        pidH.setCoefficent(kPH,kIH,kDH,0,u_maxH);
        pidX.setCoefficent(kPX,kIX,kDX,0, u_maxX);
        pidY.setCoefficent(kPY,kIY,kDY,0, u_maxY);
        if (autoMode) {
            x = robot.odometry.x;
            y = robot.odometry.y;
            h = robot.odometry.heading;

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

            robot.driveTrainVelocityControl.moveGlobalCord(Y, X, H);
        }
    }

    @Override
    public boolean isAtPosition() {
         return ((abs(errX) < minX) && (abs(errY) < minY) && (abs(errH) < minH));
        }
}

