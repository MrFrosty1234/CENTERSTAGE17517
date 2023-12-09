package org.woen.team17517.Robot;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class DriveTrainVelocityControl implements RobotModule{
    UltRobot robot;
    private double voltage;
    public static double kdX;
    public static double kiX;
    public static double kpX;
    public static double kdRat;
    public static double kiRat;
    public static double kpRat;
    public static double kdY;
    public static double kiY;
    public static double kpY;
    private static double ksRat = 1d/2400d;
    private static double ksY = 1d/2400d;
    private static double ksX = 1d/2400d;
    private static double kSlide = 0.85;
    private static double encRatConstant;
    private  final double  maxRobotSpeed = diameter*PI/0.2;
    private final double maxCircleRobotSpeed = Math.toDegrees(maxRobotSpeed/trackLength);
    private PIDMethod speedX = new PIDMethod(kpX, kiX,kdX,ksX);
    private PIDMethod speedRat = new PIDMethod(kpRat,kiRat,kdRat,ksY);
    private PIDMethod speedY = new PIDMethod(kpY, kiY,kdY,ksRat);
    public double targetH = 0;
    public double yEnc;
    public double xEnc;
    public double targetAngle;
    public double ratEnc;
    public Vector2D vector = new Vector2D(0,0);
    private final DcMotorEx left_front_drive;
    private final DcMotorEx left_back_drive;
    private final DcMotorEx right_front_drive;
    private final DcMotorEx right_back_drive;

    public DriveTrainVelocityControl(UltRobot robot)
    {

        this.robot = robot;

        left_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_front_drive");
        left_back_drive =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_back_drive ");

        right_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_front_drive");
        right_back_drive =  robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_back_drive");

        voltage = robot.voltageSensorPoint.getVol();

        reset();
    }
    public void reset()
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
    }

    public void  encUpdate()
    {
        this.yEnc = (left_back_drive.getVelocity()+ left_front_drive.getVelocity()+
                right_front_drive.getVelocity()+ right_back_drive.getVelocity())/4.0;
        this.xEnc = ((-left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity()+ right_back_drive.getVelocity())/4.0)*kSlide;
        this.ratEnc = (left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity()- right_back_drive.getVelocity())/4.0;
    }
    private static double diameter = 0.098;
    private static double encTransmissionCoificent = 1d/20d;
    private static double encCleanConst = 24;
    private static double trackLength = 27d/2d;
    private static double encConstant = Math.PI*diameter/(encCleanConst/encTransmissionCoificent);
    public double smToEnc(double target)
    {
        return target*encConstant;
    }
    public double encToSm(double target){return  target/encConstant;}
    public double smToDegrees(double sm)
    {
        return Math.toDegrees(sm/trackLength);
    }
    public double velocityMoveRat(double target)
    {
        double degreesRat = smToDegrees(encToSm(ratEnc));
        return speedRat.PID(target,this.voltage,degreesRat,ksRat);
    }
    public double velocityMoveX(double target)
    {
        double xEncSm = encToSm(xEnc);
        return speedY.PID(target,this.voltage,xEncSm,ksX);
    }
    public double velocityMoveY(double target)
    {
        double yEncSm = encToSm(yEnc);
        return speedX.PID(target,this.voltage,yEncSm,ksY);
    }
    public double setProcentSpeed(double target){
        return maxRobotSpeed*target;
    }
    public double setProcentSpeedCircle(double target){
        return target*maxCircleRobotSpeed;
    }
    public double getMetersPerSecondSpeed(double target)
    {
        return target/encConstant*encTransmissionCoificent*diameter*Math.PI;
    }
    public void  velocityMoveRobotCord(Vector2D vector,double targetAngle){
         this.vector.x = vector.x;
         this.vector.y = vector.y;
         targetH = targetAngle;
    }
    public void velocityMoveGlobalCord(Vector2D vector, double targetAngle){
        this.targetH = targetAngle;
        this.vector = vector.vectorRat(robot.odometry.heading);
        this.vector.x =vector.x;
        this.vector.y =vector.y;
    }
    @Override
    public boolean isAtPosition() {
        return true;
    }

    public void update() {
        encUpdate();

        this.speedRat.setCoefficent(kpRat,kiRat,kdRat,ksRat);
        this.speedX.setCoefficent(kpX,kiX,kdX,ksX);
        this.speedY.setCoefficent(kpY,kiY,kdY,ksY);

        double velRat = velocityMoveRat(targetH);
        double velX = velocityMoveX(vector.x);
        double velY = velocityMoveY(vector.y);

        left_front_drive.setPower(velX + velY + velRat);
        right_front_drive.setPower(-velX + velY - velRat);
        left_back_drive.setPower(-velX + velY + velRat);
        right_back_drive.setPower(velX + velY - velRat);
    }
}