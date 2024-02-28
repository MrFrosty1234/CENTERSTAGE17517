package org.woen.team17517.RobotModules.DriveTrain;

import static java.lang.Math.PI;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.PID;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;

@Config
public class DriveTrainVelocityControl implements RobotModule {
    UltRobot robot;
    private double voltage;

    public static double kdX = 0;
    public static double kiX = 0.0000012;
    public static double kpX = 0.0000015;

    public static double kdRat = 0;
    public static double kiRat = 0.0015;
    public static double kpRat = 0.000008;

    public static double kdY  = 0;
    public static double kiY = 0.0000012;
    public static double kpY = 0.0000014;

    public static  double maxIY = 0.03;
    public static  double maxIRat = 0.03;
    public static  double maxIX = 0.03;

    public static double ksRat = 0.000025;
    public static double ksY = 0.0000116;
    public static double ksX = 0.000016;
    public static double kSlide = 1;

    public final double odToEnc = 98;
    private PID speedX = new PID(kpX, kiX,kdX,ksX,maxIX);
    private PID speedH = new PID(kpRat,kiRat,kdRat,ksY,maxIY);
    private PID speedY = new PID(kpY, kiY,kdY,ksRat,maxIRat);
    public Map<String, Double> getPIDX(){
        HashMap<String, Double> pidX = new HashMap<>();
        pidX.put("P",speedX.getP());
        pidX.put("I",speedX.getI());
        pidX.put("D",speedX.getD());
        return pidX;
    }
    public  Map<String, Double> getPIDY(){
        HashMap<String, Double> pidY = new HashMap<>();
        pidY.put("P",speedY.getP());
        pidY.put("I",speedY.getI());
        pidY.put("D",speedY.getD());
        return pidY;
    }
    public Map<String, Double> getPIDH(){
        HashMap<String, Double> pidH = new HashMap<>();
        pidH.put("P",speedH.getP());
        pidH.put("I",speedH.getI());
        pidH.put("D",speedH.getD());
        return pidH;
    }
    public HashMap<String,Double> getEncoders(){
        HashMap<String,Double> encoderMap = new HashMap<>();
        encoderMap.put("xEnc",xEnc);
        encoderMap.put("yEnc",yEnc);
        encoderMap.put("hEnc",hEnc);
        return encoderMap;
    }
    public HashMap<String, Double> getTargets(){
        HashMap<String, Double> targetsMap = new HashMap<>();
        targetsMap.put("targetY",vector.getY());
        targetsMap.put("targetX",vector.getX());
        targetsMap.put("targetH",targetH);
        return targetsMap;
    }
    public HashMap<String,Double> getPowers(){
        HashMap<String,Double> powerMap = new HashMap<>();
        powerMap.put("powerX",powerX);
        powerMap.put("powerY",powerY);
        powerMap.put("powerH",powerH);
        return powerMap;
    }

    private double targetH = 0;
    private Vector2D vector = new Vector2D(0,0);
    private  double yEnc;
    private double xEnc;
    private double hEnc;

    private final DcMotorEx left_front_drive;
    private final DcMotorEx left_back_drive;
    private final DcMotorEx right_front_drive;
    private final DcMotorEx right_back_drive;

    public DriveTrainVelocityControl(UltRobot robot)
    {
        this.robot = robot;

        left_front_drive = robot.hardware.driveTrainMotors.left_front_drive;
        left_back_drive =  robot.hardware.driveTrainMotors.left_back_drive;

        right_front_drive = robot.hardware.driveTrainMotors.right_front_drive;
        right_back_drive = robot.hardware.driveTrainMotors.right_back_drive;
        voltage = 12;

        reset();
    }
    private void reset()
    {
        left_front_drive.setDirection(DcMotor.Direction.FORWARD);
        left_back_drive.setDirection(DcMotor.Direction.FORWARD);

        right_front_drive.setDirection(DcMotor.Direction.REVERSE);
        right_back_drive.setDirection(DcMotor.Direction.REVERSE);

        left_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        left_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_front_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_back_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_back_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        targetH = 0;
    }

     private void  encUpdate()
    {
        this.yEnc = (left_back_drive.getVelocity()+ left_front_drive.getVelocity()+
                right_front_drive.getVelocity()+ right_back_drive.getVelocity())/4.0;
        this.xEnc = ((-left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity()+ right_back_drive.getVelocity())/4.0)*kSlide;
        this.hEnc = (left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity() - right_back_drive.getVelocity())/4.0;
    }
    private void  odUpdate()
    {
        this.yEnc = robot.odometry.getVelCleanY();
        this.xEnc = robot.odometry.getVelCleanX();
        this.hEnc = robot.odometry.getVelCleanH();
    }
    private static double transmission = 19d/16d;
    private static double maxMotorRoundPerSecond = 5d/transmission;
    private static double lightOfWheel = 9.6d*PI;
    private static double maxRobotSpeed = lightOfWheel * maxMotorRoundPerSecond;
    private static double lightOfOdometer = 4.8d*PI;
    private static double maxRoundOdometerPerSecond = maxRobotSpeed/lightOfOdometer;
    private static double odometerConstant = 8192;
    private static double maxLinearSpeedOd = maxRoundOdometerPerSecond*odometerConstant;
    private static double maxAngleSpeedOd = 30000;
    private double moveRat(double target)
    {
        return speedH.pid(target,hEnc,this.voltage);
    }
    private double moveX(double target)
    {
        return speedX.pid(target,xEnc,this.voltage);
    }
    private double moveY(double target)
    {
        return speedY.pid(target,yEnc,this.voltage);
    }
    public double linearVelocityPercent(double target){
        return maxLinearSpeedOd*target;
    }
    public double angularVelocityPercent(double target){
        return target*maxAngleSpeedOd;
    }
    public void moveRobotCord(Vector2D vector, double h){
         this.vector.setCord(vector.getX(),vector.getY());
         targetH = h;
    }
    public void moveRobotCord(double x, double y, double h){
        this.vector.setCord(x,y);
        targetH = h;
    }
    public void moveGlobalCord(Vector2D vector, double targetH){
        vector.vectorRat(-robot.odometry.getH());
        this.vector.setCord(vector.getX(),vector.getY());
        this.targetH = targetH;
    }
    public void moveGlobalCord(double x, double y, double targetH){
        vector.setCord(x,y);
        vector.vectorRat(-robot.odometry.getH());
        this.targetH = targetH;
    }

    private double powerH = 0;
    private double powerX = 0;
    private double powerY = 0;
    public void update() {
        odUpdate();
        this.voltage = robot.voltageSensorPoint.getVol();


        this.speedH.setCoefficent(kpRat,kiRat,kdRat,ksRat,maxIRat);
        this.speedX.setCoefficent(kpX,kiX,kdX,ksX,maxIX);
        this.speedY.setCoefficent(kpY,kiY,kdY,ksY,maxIY);


        powerH = moveRat(targetH);
        powerX = moveX(vector.getX());
        powerY = moveY(vector.getY());

        if (Math.abs(powerH) < 0.03){
            powerH = 0;
        }
        if (Math.abs(powerX) < 0.03){
            powerX = 0;
        }
        if (Math.abs(powerY) < 0.03){
            powerY = 0;
        }

        left_front_drive.setPower(powerX + powerY + powerH);
        right_front_drive.setPower(-powerX + powerY - powerH);
        left_back_drive.setPower(-powerX + powerY + powerH);
        right_back_drive.setPower(powerX + powerY - powerH);
    }
}