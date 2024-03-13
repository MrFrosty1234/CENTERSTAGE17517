package org.woen.team17517.RobotModules.Navigative;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;

public class OdometryNew implements RobotModule {
    UltRobot robot;
    private Vector2D vectorPositionGlobal = new Vector2D();
    private Vector2D vectorVelocityGlobal = new Vector2D();
    private Vector2D vectorPositionLocal = new Vector2D();
    private Vector2D vectorVelovityLocal = new Vector2D();

    private final DcMotorEx odometerRightY;
    private final DcMotorEx odometrLeftY;
    private final DcMotorEx odometrX;
    private double h;
    public OdometryNew(UltRobot robot){
        this.robot = robot;
        odometerRightY = robot.hardware.odometers.odometrRightY;
        odometrLeftY =  robot.hardware.odometers.odometrLeftY;
        odometrX = robot.hardware.odometers.odometrX;
        robot.hardware.odometers.setDirection(odometrX,-1);
        robot.hardware.odometers.setDirection(odometerRightY,-1);
        robot.hardware.odometers.setDirection(odometrLeftY,1);
        vectorPositionGlobal.setCord(0,0);
        h = 0;
    }
    private double velH = 0;
    public double getVelLocalX() {return vectorVelovityLocal.getX();}
    public double getVelLocalY() {return vectorVelovityLocal.getY();}
    public double getVelLocalH() {return velH;}
    public double getVelGlobalX() {return vectorVelocityGlobal.getX();}
    public double getVelGlobalY() {return vectorVelocityGlobal.getY();}
    public double getGlobalPosX(){return vectorPositionGlobal.getX();}
    public double getGlobalPosY(){return vectorPositionGlobal.getY();}
    public double getGlobalAngle(){return h;}

    public Vector2D getPositionVector(){return vectorPositionGlobal;}
    private double startVelTime = (double) System.nanoTime() /ElapsedTime.SECOND_IN_NANO;
    private double mathSpeedOdometerRightY = 0;
    private double mathSpeedOdometerX = 0;
    private double mathSpeedOdometerLeftY = 0;
    private double posOdometerLeftYOld = 0;
    private double posOdometerRightYOld = 0;
    private double posOdometerXOld = 0;
    private double hardVelOdometerX = 0;
    private double hardVelOdometerRightY = 0;
    private double hardVelOdometerLeftY = 0;
    private double posOdometerX =0;
    private double posOdometerRightY =0;
    private double posOdometerLeftY =0;
    private double velOdometerX=0;
    private double velOdometerRightY=0;
    private double velOdometerLeftY =0;

    private void overflowDef(){
        double deltaTime = (double) System.nanoTime() / ElapsedTime.SECOND_IN_NANO - startVelTime;

        posOdometerX      = robot.hardware.odometers.getPosition(odometrX);
        posOdometerRightY = robot.hardware.odometers.getPosition(odometerRightY);
        posOdometerLeftY  = robot.hardware.odometers.getPosition(odometrLeftY);

        if(deltaTime > 0.1){
            mathSpeedOdometerX      = (posOdometerX - posOdometerXOld) / deltaTime;
            mathSpeedOdometerRightY = (posOdometerRightY - posOdometerRightYOld) / deltaTime;
            mathSpeedOdometerLeftY  = (posOdometerLeftY - posOdometerLeftYOld) / deltaTime;

            posOdometerXOld      = posOdometerX;
            posOdometerRightYOld = posOdometerRightY;
            posOdometerLeftYOld  = posOdometerLeftY;

            startVelTime = (double) System.nanoTime() /ElapsedTime.SECOND_IN_NANO;
        }

        hardVelOdometerX = robot.hardware.odometers.getVelocity(odometrX);
        hardVelOdometerRightY = robot.hardware.odometers.getVelocity(odometerRightY);
        hardVelOdometerLeftY  = robot.hardware.odometers.getVelocity(odometrLeftY);

        velOdometerX      = hardVelOdometerX + Math.round( (mathSpeedOdometerX - hardVelOdometerX) / (double) 0x10000 ) * (double)0x10000;
        velOdometerLeftY = hardVelOdometerLeftY + Math.round( (mathSpeedOdometerLeftY - hardVelOdometerLeftY) / (double) 0x10000 ) * (double)0x10000;
        velOdometerRightY = hardVelOdometerRightY + Math.round( (mathSpeedOdometerRightY - hardVelOdometerRightY) / (double) 0x10000 ) * (double)0x10000;
    }

    public double getHardVelOdometerLeftY() {
        return hardVelOdometerLeftY;
    }
    public double getHardVelOdometerRightY() {
        return hardVelOdometerRightY;
    }
    public double getHardVelOdometerX() {
        return hardVelOdometerX;
    }

    public double getMathSpeedOdometerLeftY() {
        return mathSpeedOdometerLeftY;
    }
    public double getMathSpeedOdometerX() {
        return mathSpeedOdometerX;
    }
    public double getMathSpeedOdometerRightY() {return mathSpeedOdometerRightY;}
    private void localVelocityUpdate(){
        overflowDef();
        double velX = velOdometerX;
        double velY = (velOdometerLeftY + velOdometerRightY)/2d;
        velH = (velOdometerLeftY - velOdometerRightY)/2d;
        vectorVelovityLocal.setCord(velX,velY);
    }
    private void localPositionUpdate(){
        double yEnc = (robot.hardware.odometers.getPosition(odometerRightY) + robot.hardware.odometers.getPosition(odometrLeftY))/2d;
        double xEnc = robot.hardware.odometers.getPosition(odometrX);
        h = robot.gyro.getAngle();
        vectorPositionLocal.setCord(xEnc,yEnc);
    }
    private Vector2D vectorDeltaPosition = new Vector2D();
    private Vector2D vectorPositionLocalOld = new Vector2D();
    private void globalPositionUpdate(){
        vectorDeltaPosition = vectorPositionLocal;
        vectorDeltaPosition.minus(vectorPositionLocalOld);
        vectorDeltaPosition.turn(h);
        vectorPositionGlobal.plus(vectorDeltaPosition);
        vectorPositionLocalOld = vectorPositionLocal;
    }
    private Vector2D vectorDeltaVelocity = new Vector2D();
    private Vector2D vectorVelocityLocalOld = new Vector2D();
    private void globalVelocityUpdate(){
        vectorDeltaVelocity = vectorVelovityLocal;
        vectorDeltaVelocity.minus(vectorVelocityLocalOld);
        vectorDeltaVelocity.turn(h);
        vectorVelocityGlobal.plus(vectorDeltaVelocity);
        vectorVelocityLocalOld = vectorVelovityLocal;
    }
    public void update(){
        localVelocityUpdate();
        localPositionUpdate();
        globalVelocityUpdate();
        globalPositionUpdate();
    }
}
