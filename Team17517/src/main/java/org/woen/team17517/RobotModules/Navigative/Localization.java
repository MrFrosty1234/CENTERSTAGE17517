package org.woen.team17517.RobotModules.Navigative;
import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.VEL_ANGLE_TO_ENC;
import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.toSm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;
@Config
public class Localization implements RobotModule {
    UltRobot robot;
    public StartPosition startPosition = StartPosition.ZERO;
    private final Vector2D vectorPositionGlobal = new Vector2D();
    private final Vector2D vectorVelocityGlobal = new Vector2D();
    private final Vector2D vectorPositionLocal = new Vector2D();
    private final Vector2D vectorVelocityLocal = new Vector2D();
    private final DcMotorEx odometerRightY;
    private final DcMotorEx odometerLeftY;
    private final DcMotorEx odometerX;
    private double h;
    public Localization(UltRobot robot){
        this.robot = robot;
        odometerRightY = robot.hardware.odometers.odometrRightY;
        odometerLeftY =  robot.hardware.odometers.odometrLeftY;
        odometerX = robot.hardware.odometers.odometrX;
        robot.hardware.odometers.setDirection(odometerX,-1);
        robot.hardware.odometers.setDirection(odometerRightY,-1);
        robot.hardware.odometers.setDirection(odometerLeftY,1);
        vectorPositionGlobal.copyFrom(startPosition.getVector());
        h = 0;
    }
    private double velH = 0;
    public double getVelLocalX() {return vectorVelocityLocal.getX();}
    public double getVelLocalY() {return vectorVelocityLocal.getY();}
    public double getVelLocalH() {return velH;}
    public double getVelGlobalX() {return vectorVelocityGlobal.getX();}
    public double getVelGlobalY() {return vectorVelocityGlobal.getY();}
    public double getGlobalPosX(){return vectorPositionGlobal.getX();}
    public double getGlobalPosY(){return vectorPositionGlobal.getY();}
    public double getGlobalAngle(){return h;}
    public Vector2D getLocalVelocityVector(){return vectorVelocityLocal;}
    public Vector2D getGlobalPositionVector(){return vectorPositionGlobal;}
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
    private double velOdometerX=0;
    private double velOdometerRightY=0;
    private double velOdometerLeftY =0;

    private void overflowDef(){
        double deltaTime = (double) System.nanoTime() / ElapsedTime.SECOND_IN_NANO - startVelTime;

        double posOdometerX = robot.hardware.odometers.getPosition(odometerX);
        double posOdometerRightY = robot.hardware.odometers.getPosition(odometerRightY);
        double posOdometerLeftY = robot.hardware.odometers.getPosition(odometerLeftY);

        if(deltaTime > 0.1){
            mathSpeedOdometerX      = (posOdometerX - posOdometerXOld) / deltaTime;
            mathSpeedOdometerRightY = (posOdometerRightY - posOdometerRightYOld) / deltaTime;
            mathSpeedOdometerLeftY  = (posOdometerLeftY - posOdometerLeftYOld) / deltaTime;

            posOdometerXOld      = posOdometerX;
            posOdometerRightYOld = posOdometerRightY;
            posOdometerLeftYOld  = posOdometerLeftY;

            startVelTime = (double) System.nanoTime() /ElapsedTime.SECOND_IN_NANO;
        }

        hardVelOdometerX = robot.hardware.odometers.getVelocity(odometerX);
        hardVelOdometerRightY = robot.hardware.odometers.getVelocity(odometerRightY);
        hardVelOdometerLeftY  = robot.hardware.odometers.getVelocity(odometerLeftY);

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
        double velX = toSm(velOdometerX);
        double velY = toSm((velOdometerLeftY + velOdometerRightY)/2d);
        velH = ((velOdometerLeftY - velOdometerRightY)/2d)/ VEL_ANGLE_TO_ENC;
        vectorVelocityLocal.setCord(velX,velY);
    }
    public double hEncoder = 0;
    private double hOld = 0;
    public static double xOdometerDistance = 11;
    private void localPositionUpdate(){
        h = robot.gyro.getAngle() + startPosition.getAngle();
        double    deltaH = h - hOld;
        deltaH = Vector2D.getAngleError(deltaH);
        hOld = h;
        double xCorrect = xOdometerDistance*Math.toRadians(deltaH);
        robot.linearOpMode.telemetry.addData("xCorrect",xCorrect);
        double yEnc = toSm((robot.hardware.odometers.getPosition(odometerRightY) + robot.hardware.odometers.getPosition(odometerLeftY))/2d);
        double xEnc = toSm(robot.hardware.odometers.getPosition(odometerX));//-xCorrect;
        hEncoder = (-robot.hardware.odometers.getPosition(odometerRightY) + robot.hardware.odometers.getPosition(odometerLeftY))/2d;
        vectorPositionLocal.setCord(xEnc,yEnc);
    }
    private final Vector2D vectorDeltaPosition = new Vector2D();
    private final Vector2D vectorPositionLocalOld = new Vector2D();
    private void globalPositionUpdate(){
        vectorDeltaPosition.copyFrom(vectorPositionLocal);
        vectorDeltaPosition.minus(vectorPositionLocalOld);
        vectorPositionLocalOld.copyFrom(vectorPositionLocal);
        vectorDeltaPosition.turn(h);
        vectorPositionGlobal.plus(vectorDeltaPosition);
    }
    private final Vector2D vectorDeltaVelocity = new Vector2D();
    private final Vector2D vectorVelocityLocalOld = new Vector2D();
    private void globalVelocityUpdate(){
        vectorDeltaVelocity.copyFrom(vectorVelocityLocal);
        vectorDeltaVelocity.minus(vectorVelocityLocalOld);
        vectorDeltaVelocity.turn(h);
        vectorVelocityGlobal.plus(vectorDeltaVelocity);
        vectorVelocityLocalOld.copyFrom(vectorVelocityLocal);
    }
    @Override
    public void update(){
        localVelocityUpdate();
        localPositionUpdate();
        globalVelocityUpdate();
        globalPositionUpdate();
    }
}
