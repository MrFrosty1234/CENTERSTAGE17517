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

    public static double kdX = 0       ;
    public static double kiX = 2.5e-8 ;
    public static double kpX = 0.000006;


    public static double kdY = 0       ;
    public static double kiY = 2.5e-8  ;
    public static double kpY = 0.000006;

    public static double kdRat = 0;
    public static double kiRat = 0.0015;
    public static double kpRat = 0.000008;

    public static  double maxIY = 0.05;
    public static  double maxIRat = 0.04;
    public static  double maxIX = 0.04;

    public static double ksRat = 0.000025;
    public static double ksY = 0.0000115;
    public static double ksX = 0.0000115;
    public static double kSlide = 1.2;


    private PID speedX = new PID(kpX,kiX,kdX,ksX,maxIX,0);
    private PID speedH = new PID(kpRat,kiRat,kdRat,ksY,maxIY,0);
    private PID speedY = new PID(kpY,kiY,kdY,ksRat,maxIRat,0);
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
    private double angle = 0;
    private void  odUpdate()
    {
        angle = robot.odometry.getGlobalAngle();
        this.yEnc = toEnc(robot.odometry.getVelLocalY());
        this.xEnc = toEnc(robot.odometry.getVelLocalX());
        this.hEnc = robot.odometry.getVelLocalH();
    }
    public static double VEL_ANGLE_TO_ENC = 126.68033d;
    private static final double transmission = 19d/16d;
    private static final double maxMotorRoundPerSecond = 5d/transmission;
    private static final double lightOfWheel = 9.6d*PI;
    public  static final double maxLinerSpeedSm = lightOfWheel * maxMotorRoundPerSecond;
    private static final double lightOfOdometer = 4.8d*PI;
    private static final double maxRoundOdometerPerSecond = maxLinerSpeedSm /lightOfOdometer;
    private static final double odometerConstant = 8192;
    public static final double maxLinearSpeedOd = maxRoundOdometerPerSecond*odometerConstant;
    public static final double trackLight = 27d;
    public static final double maxAngleSpeedOd = 30_000;
    public static final double ENC_TO_SM = lightOfOdometer/odometerConstant;
    public static double toEnc(double target){
        return target*maxLinearSpeedOd/maxLinerSpeedSm;
    }
    public static double toSm(double target){return target*maxLinerSpeedSm/maxLinearSpeedOd;}
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
    public static double kpAngle=450;
    public static double kiAngle=0;
    public static double kdAngle=50;
    public static double ksAngle=0;
    public static double maxIAngle;
    PID pidAngle = new PID(kpAngle,kiAngle,kdAngle,ksAngle,maxIAngle,0);
    private double targetAngle = 0;

    public double getAngle() {
        return angle;
    }

    public double getTargetAngle() {
        return targetAngle;
    }

    public void moveAngle(double target){
        isGlobal = true;
        pidAngle.setIsAngle(true);
        targetAngle = target;
    }
    public void moveWithAngleControl(double x, double y){
        isGlobal = true;
        this.vector.setCord(x,y);
    }
    public void moveRobotCord(Vector2D vector, double h){
        isGlobal = false;
        this.vector.setCord(vector.getX(),vector.getY());
        targetH = h;
    }
    public void moveRobotCord(double x, double y, double h){
        isGlobal = false;
        this.vector.setCord(x,y);
        targetH = h;
    }
    public void moveGlobalCord(Vector2D vector, double targetH){
        isGlobal = false;
        vector.turn(-robot.odometry.getGlobalAngle());
        this.vector.setCord(vector.getX(),vector.getY());
        this.targetH = targetH;
    }
    public void moveGlobalCord(double x, double y, double targetH){
        isGlobal = false;
        vector.setCord(x,y);
        vector.turn(-robot.odometry.getGlobalAngle());
        this.targetH = targetH;
    }

    private double powerH = 0;
    private double powerX = 0;
    private double powerY = 0;
    private boolean isGlobal=false;
    @Override
    public boolean isAtPosition(){
        return !isGlobal || Math.abs(Vector2D.getAngleError(targetAngle-angle))<10;

    }
    public double powerLeft_front_drive = 0;
    public double powerRight_front_drive= 0;
    public double powerLeft_back_drive  = 0;
    public double powerRight_back_drive = 0;
    public void update() {
        odUpdate();
        this.voltage = robot.voltageSensorPoint.getVol();


        this.speedH.setCoefficients(kpRat,kiRat,kdRat,ksRat,maxIRat,0);
        this.speedX.setCoefficients(kpX,kiX,kdX,ksX,maxIX,0);
        this.speedY.setCoefficients(kpY,kiY,kdY,ksY,maxIY,0);
        this.pidAngle.setCoefficients(kpAngle,kiAngle,kdAngle,ksAngle,maxIAngle,0);
        if(isGlobal) targetH = pidAngle.pid(targetAngle,angle,voltage);

        powerH = moveRat(targetH);
        powerX = moveX(vector.getX())*kSlide;
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
        powerLeft_front_drive = powerX + powerY + powerH;
        powerRight_front_drive  = -powerX + powerY - powerH;
        powerLeft_back_drive  = -powerX + powerY + powerH;
        powerRight_back_drive = powerX + powerY - powerH;

        left_front_drive.setPower(powerLeft_front_drive);
        right_front_drive.setPower(powerRight_front_drive);
        left_back_drive.setPower(powerLeft_back_drive);
        right_back_drive.setPower(powerRight_back_drive);
    }
}