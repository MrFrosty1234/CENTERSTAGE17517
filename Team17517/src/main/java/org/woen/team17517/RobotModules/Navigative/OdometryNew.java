package org.woen.team17517.RobotModules.Navigative;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;

public class OdometryNew implements RobotModule {
    UltRobot robot;
    private Vector2D vector = new Vector2D();
    private final DcMotorEx odometrRightY;
    private final DcMotorEx odometrLeftY;
    private final DcMotorEx odometrX;
    private double xEncOld = 0;
    private double yEncOld = 0;
    private double h;
    private  double yEnc;
    private double xEnc;
    public OdometryNew(UltRobot robot){
        this.robot = robot;

        odometrRightY = robot.hardware.odometers.odometrRightY;
        odometrLeftY =  robot.hardware.odometers.odometrLeftY;
        odometrX = robot.hardware.odometers.odometrX;
        robot.hardware.odometers.setDirection(odometrX,-1);
        robot.hardware.odometers.setDirection(odometrRightY,-1);
        robot.hardware.odometers.setDirection(odometrLeftY,1);
        vector.setCord(0,0);
        h = 0;
    }
    private double velX = 0;
    private double velY = 0;
    private double velH = 0;
    public double getCleanLeftY() {return robot.hardware.odometers.getVelocity(odometrLeftY);}
    public double getCleanRightY(){return robot.hardware.odometers.getVelocity(odometrRightY);}

    public double getVelCleanX() {
        return velX;
    }
    public double getVelCleanY() {
        return velY;
    }
    public double getVelCleanH() {return velH;}

    public double getX(){return vector.getX();}
    public double getY(){return vector.getY();}
    public double getH(){return h;}

    public Vector2D getPositionVector(){return vector;}
    private Vector2D vectorDeltaPosition = new Vector2D();
    private double startVelTime = System.nanoTime()/1_000_000d;
    private double mathSpeedOdometerRightY = 0;
    private double mathSpeedOdometerX = 0;
    private double mathSpeedOdometerLeftY = 0;
    private double posOdometerLeftYOld = 0;
    private double posOdometerRightYOld = 0;
    private double posOdometerXOld = 0;
    private double hardVelOdometrX = 0;
    private double hardVelOdometerRightY = 0;
    private double hardVelOdometerLeftY = 0;
    public double posOdometerX =0;
    public double posOdometerRightY =0;
    public double posOdometerLeftY =0;
    public double velOdometerX=0;
    public double velOdometerRightY=0;
    public double velOdometerLeftY =0;
    private void odometerVelocityUpdate(){
        overflowDef();
        velX = velOdometerX;
        velY = (velOdometerLeftY + velOdometerRightY)/2d;
        velH = (velOdometerLeftY - velOdometerRightY)/2d;
    }
    private void overflowDef(){
        double deltaTime = System.nanoTime()/1_000_000d - startVelTime;

        posOdometerX      = robot.hardware.odometers.getPosition(odometrX);
        posOdometerRightY = robot.hardware.odometers.getPosition(odometrRightY);
        posOdometerLeftY  = robot.hardware.odometers.getPosition(odometrLeftY);

        if(deltaTime > 0.1){
            mathSpeedOdometerX      = (posOdometerX - posOdometerXOld) / deltaTime;
            mathSpeedOdometerRightY = (posOdometerRightY - posOdometerRightYOld) / deltaTime;
            mathSpeedOdometerLeftY  = (posOdometerLeftY - posOdometerLeftYOld) / deltaTime;

            posOdometerXOld      = posOdometerX;
            posOdometerRightYOld = posOdometerRightY;
            posOdometerLeftYOld  = posOdometerLeftY;

            startVelTime = System.nanoTime()/1_000_000d;
        }

        hardVelOdometrX       = robot.hardware.odometers.getVelocity(odometrX);
        hardVelOdometerRightY = robot.hardware.odometers.getVelocity(odometrRightY);
        hardVelOdometerLeftY  = robot.hardware.odometers.getVelocity(odometrLeftY);

        velOdometerX      = hardVelOdometrX + Math.round( (mathSpeedOdometerX - hardVelOdometrX) / (double) 0x10000 ) * (double)0x10000;
        velOdometerLeftY = hardVelOdometerLeftY + Math.round( (mathSpeedOdometerLeftY - hardVelOdometerLeftY) / (double) 0x10000 ) * (double)0x10000;
        velOdometerRightY = hardVelOdometerRightY + Math.round( (mathSpeedOdometerRightY - hardVelOdometerRightY) / (double) 0x10000 ) * (double)0x10000;
    }

    public double getHardVelOdometerLeftY() {
        return hardVelOdometerLeftY;
    }
    public double getHardVelOdometerRightY() {
        return hardVelOdometerRightY;
    }
    public double getHardVelOdometrX() {
        return hardVelOdometrX;
    }

    public double getMathSpeedOdometerLeftY() {
        return mathSpeedOdometerLeftY;
    }
    public double getMathSpeedOdometerX() {
        return mathSpeedOdometerX;
    }
    public double getMathSpeedOdometerRightY() {
        return mathSpeedOdometerRightY;
    }

    private void odometerUpdate(){
        this.yEnc = (robot.hardware.odometers.getPosition(odometrRightY) + robot.hardware.odometers.getPosition(odometrLeftY))/2d;
        this.xEnc = robot.hardware.odometers.getPosition(odometrX);
        h = robot.gyro.getAngle();
    }
    public void update(){
        odometerVelocityUpdate();
        odometerUpdate();
        vectorDeltaPosition.setCord(xEnc-xEncOld,yEnc-yEncOld);
        vectorDeltaPosition.vectorRat(h);
        vector.vectorSum(vectorDeltaPosition);
        xEncOld = xEnc;
        yEncOld = yEnc;
    }
}
