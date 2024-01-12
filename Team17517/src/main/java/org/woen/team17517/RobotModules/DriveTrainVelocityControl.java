package org.woen.team17517.RobotModules;

import static java.lang.Math.PI;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.HashMap;
import java.util.Map;

import org.woen.team17517.Service.PIDMethod;
import org.woen.team17517.Service.RobotModule;
import org.woen.team17517.Service.Vector2D;

@Config
public class DriveTrainVelocityControl implements RobotModule {
    UltRobot robot;
    private double voltage;

    public static double kdX = 0;
    public static double kiX = 0.000_000_1;
    public static double kpX = 0.0003;

    public static double kdRat = 0;
    public static double kiRat = 0.000_000_1;
    public static double kpRat = 0.0003;

    public static double kdY  = 0;
    public static double kiY =0.000_000_1;
    public static double kpY = 0.0003;

    public static  double maxIY = 0.01;
    public static  double maxIRat = 0.01;
    public static  double maxIX = 0.01;

    private PIDMethod speedX = new PIDMethod(kpX, kiX,kdX,ksX,maxIX);
    private PIDMethod speedH = new PIDMethod(kpRat,kiRat,kdRat,ksY,maxIY);
    private PIDMethod speedY = new PIDMethod(kpY, kiY,kdY,ksRat,maxIRat);
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
    public static double ksRat = 0.000479;
    public static double ksY = 0.0004;
    public static double ksX = 0.00045;
    public static double kSlide = 0.5;

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

        left_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_front_drive");
        left_back_drive =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_back_drive");

        right_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_front_drive");
        right_back_drive =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_back_drive");

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

        vector.setCord(0,0);
        targetH = 0;
    }

    private void  encUpdate()
    {
        this.yEnc = (left_back_drive.getVelocity()+ 0*left_front_drive.getVelocity()+
                right_front_drive.getVelocity()*0+ right_back_drive.getVelocity())/2.0;
        this.xEnc = ((-left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                0*right_front_drive.getVelocity()+ 0*right_back_drive.getVelocity())/2.0)*kSlide;
        this.hEnc = (left_back_drive.getVelocity()*0+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity()*0 - right_back_drive.getVelocity())/2.0;
    }
    private static double diameter = 9.8;
    private static double gearboxRatio = 1d/20d;
    private static double encConstNoGearbox = 24.0;
    private static double trackLength = 27d/2d;
    private static double encConstant = PI*diameter/(encConstNoGearbox / gearboxRatio);
    private static double maxMotorRpm = 280.0;
    private static double maxLinearSpeed = (maxMotorRpm / 60.0) * diameter * PI;
    private final double maxAngularSpeed = Math.toDegrees(maxLinearSpeed/trackLength);
    private double smToEnc(double target)
    {
        return target/encConstant;
    }
    private double encToSm(double target){return  target*encConstant;}
    private double smToDegrees(double sm)
    {
        return Math.toDegrees(sm/trackLength);
    }
    private double moveRat(double target)
    {
        return speedH.PID(target,hEnc,this.voltage);
    }
    private double moveX(double target)
    {

        return speedX.PID(target,xEnc,this.voltage);
    }
    private double moveY(double target)
    {
        return speedY.PID(target,yEnc,this.voltage);
    }
    public double linearVelocityPercent(double target){
        return smToEnc(maxLinearSpeed)*target;
    }
    public double angularVelocityPercent(double target)
    {
        return target*smToDegrees(maxAngularSpeed);
    }
    public double getMetersPerSecondSpeed(double target)
    {
        return target/encConstant* gearboxRatio *diameter*Math.PI;
    }
    public void moveRobotCord(Vector2D vector, double targetAngle){
         this.vector.setCord(vector.getX(),vector.getY());
         this.vector.setCord(vector.getX(),vector.getY());
         targetH = targetAngle;
    }
    public void moveRobotCord(double x, double y, double h){
        this.vector.setCord(x,y);
        targetH = h;
    }
    public void moveGlobalCord(Vector2D vector, double targetH){
        vector.vectorRat(robot.odometry.heading);
        this.vector.setCord(vector.getX(),vector.getY());
        this.targetH = targetH;
    }
    public void moveGlobalCord(double x, double y, double targetH){
        Vector2D vectorGot = new Vector2D(x,y);
        vectorGot.vectorRat(robot.odometry.heading);
        this.vector.setCord(vectorGot.getX(),vectorGot.getY());
        this.targetH = targetH;
    }

    private double powerH = 0;
    private double powerX = 0;
    private double powerY = 0;
    public void update() {
        encUpdate();
        this.voltage = robot.voltageSensorPoint.getVol();

        this.speedH.setCoefficent(kpRat,kiRat,kdRat,ksRat,maxIRat);
        this.speedX.setCoefficent(kpX,kiX,kdX,ksX,maxIX);
        this.speedY.setCoefficent(kpY,kiY,kdY,ksY,maxIY);


        powerH = moveRat(targetH);
        powerX = moveX(vector.getX());
        powerY = moveY(vector.getY());

        left_front_drive.setPower(powerX + powerY + powerH);
        right_front_drive.setPower(-powerX + powerY - powerH);
        left_back_drive.setPower(-powerX + powerY + powerH);
        right_back_drive.setPower(powerX + powerY - powerH);
    }
}