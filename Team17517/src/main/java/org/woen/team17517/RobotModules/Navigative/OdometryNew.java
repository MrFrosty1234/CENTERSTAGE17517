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

    private double cleanRightY = 0;
    private double cleanLeftY = 0;
    private double cleanVelX = 0;
    private double cleanVelY = 0;
    private double cleanVelH = 0;
    public double getCleanLeftY() {
        return cleanLeftY;
    }

    public double getCleanRightY() {
        return cleanRightY;
    }

    public double getVelCleanX() {
        return cleanVelX;
    }
    public double getVelCleanY() {
        return cleanVelY;
    }

    public double getVelCleanH() {return cleanVelH;}

    public double getX(){return vector.getX();}

    public double getY(){return vector.getY();}
    public double getH(){return h;}
    public Vector2D getPositionVector(){return vector;}
    private Vector2D vectorDeltaPosition = new Vector2D();
    private void velocityUpdate(){
        cleanVelX = -odometrX.getVelocity();
        cleanVelY = (odometrLeftY.getVelocity()-odometrRightY.getVelocity())/2d;
        cleanVelH = (odometrLeftY.getVelocity()+odometrRightY.getVelocity())/2d;

        cleanRightY = -odometrRightY.getVelocity();
        cleanLeftY = odometrLeftY.getVelocity();
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
        vectorDeltaPosition.vectorRat(h-hOld);
        xEncOld = xEnc;
        yEncOld = yEnc;
        hOld=h;
        vector.vectorSum(vectorDeltaPosition);
    }
}
