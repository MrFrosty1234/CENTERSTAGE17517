package org.woen.team17517.RobotModules.DriveTrain;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team17517.RobotModules.UltRobot;import org.woen.team17517.Service.PID;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;

import java.util.HashMap;
@Config

public class DriveTrain implements RobotModule{
    UltRobot robot;
    private double voltage = 12;
    public boolean autoMode = false;
    public DriveTrain(UltRobot robot){
        this.robot = robot;
        voltage = 12;
        targetH = 0;
        targetVector.setCord(0,0);
    }
    public static double kPX = 2000;
    public static double kDX = 0;
    public static double kIX = 0;

    public static double kPY = 2000;
    public static double kDY = 0;
    public static double kIY = 0;

    public static double kPH = 450;
    public static double kDH = 0;
    public static double kIH = 0;

    public static double ImaxX = 0;
    public static double ImaxH = 0;
    public static double ImaxY = 0;

    private double targetH = 0;
    private Vector2D targetVector = new Vector2D();

    private double posH = 0;
    private Vector2D positionVector = new Vector2D();

    private double X;
    private double Y;
    private double H;

    private double errX;
    private double errY;
    private double errH;
    public static double minErrX = 5;
    public static double minErrY = 5;
    public static double minErrH = 5;

    public static double kg = 0;


    private PID pidX = new PID(kPX,kIX,kDX,0,ImaxX,0);
    private PID pidY = new PID(kPY,kIY,kDY,0,ImaxY,0);
    private PID pidH = new PID(kPH,kIH,kDH,0,ImaxH,0);
    public static double kt = 5;

    HashMap<String,Double> positionMap = new HashMap<>();
    HashMap<String,Double> targetMap = new HashMap<>();
    HashMap<String,Double> errorMap = new HashMap<>();
    public HashMap<String,Double> getPosition(){
        positionMap.put("X",positionVector.getX());
        positionMap.put("Y",positionVector.getY());
        positionMap.put("H",posH);
        return positionMap;
    }
    public HashMap<String,Double> getTargets() {
        targetMap.put("X",targetVector.getX());
        targetMap.put("H",targetH);
        targetMap.put("Y",targetVector.getY());
        return targetMap;
    }
    public HashMap<String,Double> getErrors() {
        errorMap.put("X",errX);
        errorMap.put("H",errH);
        errorMap.put("Y",errY);
        return errorMap;
    }
    public void reset(){
        timer.reset();
        timer.seconds();
    }
    ElapsedTime timer = new ElapsedTime();
    public void moveGlobal(double x, double y, double h) {
        targetVector.setCord(x,y);
        targetH = h;
        reset();
        autoMode = true;
    }
    public void update(){
        if (autoMode) {
            pidH.setIsAngle(true);
            voltage = robot.voltageSensorPoint.getVol();

            positionVector = robot.odometry.getGlobalPositionVector();
            posH = robot.odometry.getGlobalAngle();

            errX = targetVector.getX() - positionVector.getX();
            errY = targetVector.getY() - positionVector.getY();
            errH = Vector2D.getAngleError(targetH - posH);


            pidX.setCoefficients(kPX, kIX, kDX, 0, ImaxX, kg);
            pidY.setCoefficients(kPY, kIY, kDY, 0, ImaxY, kg);
            pidH.setCoefficients(kPH, kIH, kDH, 0, ImaxH, kg);

            X = pidX.pid(targetVector.getX(), positionVector.getX(), voltage);
            Y = pidY.pid(targetVector.getY(), positionVector.getY(), voltage);

            robot.driveTrainVelocityControl.moveAngle(targetH);
            Vector2D vector = new Vector2D(X,Y);
            vector.turn(-posH);
            robot.driveTrainVelocityControl.moveWithAngleControl(vector.getX(),vector.getY());
        }
        }

    public boolean isAtPosition(){
        return !autoMode || (Math.abs(errH) < minErrH && Math.abs(errX) < minErrX && Math.abs(errY) < minErrY);
    }
}


