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
    private double hOld;
    private double h;
    private  double yEnc;
    private double xEnc;
    public OdometryNew(UltRobot robot){
        this.robot = robot;

        odometrRightY = robot.hardware.odometers.odometrRightY;
        odometrLeftY =  robot.hardware.odometers.odometrLeftY;
        odometrX = robot.hardware.odometers.odometrX;

        vector.setCord(0,0);
        h = 0;
    }
    private double velX = 0;
    private double velY = 0;
    private double velH = 0;
    public double getCleanLeftY() {
        return odometrLeftY.getVelocity();
    }

    public double getCleanRightY() {
        return -odometrRightY.getVelocity();
    }

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
    private long startVelTime = System.currentTimeMillis();
    private double mathSpeedY = 0;
    private double mathSpeedX = 0;
    private double mathSpeedH = 0;
    private double posHOld = 0;
    private double posYOld = 0;
    private double posXOld = 0;
    private void velocityUpdate(){
        double posH = (odometrLeftY.getCurrentPosition()+odometrRightY.getCurrentPosition())/2d;
        double posY = (odometrLeftY.getCurrentPosition()-odometrRightY.getCurrentPosition())/2d;
        double posX = -odometrX.getCurrentPosition();
        long deltaTime = System.currentTimeMillis() - startVelTime;
        if(deltaTime > 1){
            mathSpeedX = (posX-posXOld)/deltaTime;
            mathSpeedY = (posY-posYOld)/deltaTime;
            mathSpeedH = (posH-posHOld)/deltaTime;
        }
        posXOld = posX;
        posYOld = posY;
        posHOld = posH;
        velX = -odometrX.getVelocity();
        velY = (odometrLeftY.getVelocity()-odometrRightY.getVelocity())/2d;
        velH = (odometrLeftY.getVelocity()+odometrRightY.getVelocity())/2d;

        velH = velH + Math.round((mathSpeedH-velH)/(double)Short.MAX_VALUE)*(double)Short.MAX_VALUE;
        velX = velX + Math.round((mathSpeedX-velX)/(double)Short.MAX_VALUE)*(double)Short.MAX_VALUE;
        velY = velY + Math.round((mathSpeedY-velY)/(double)Short.MAX_VALUE)*(double)Short.MAX_VALUE;
    }
    private void odometerUpdate(){
        this.yEnc = ((double) -odometrRightY.getCurrentPosition() + (double) odometrLeftY.getCurrentPosition())/2d;
        this.xEnc = -odometrX.getCurrentPosition();
        h = robot.gyro.getAngle();
    }
    public void update(){
        velocityUpdate();
        odometerUpdate();
        vectorDeltaPosition.setCord(xEnc-xEncOld,yEnc-yEncOld);
        vectorDeltaPosition.vectorRat(h);
        vector.vectorSum(vectorDeltaPosition);
        xEncOld = xEnc;
        yEncOld = yEnc;
        hOld=h;
    }
}
