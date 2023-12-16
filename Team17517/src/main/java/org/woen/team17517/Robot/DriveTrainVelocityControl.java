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

    public static double kdX = 0;
    public static double kiX = 0.000_000_1;
    public static double kpX = 0.0003;

    public static double kdRat = 0;
    public static double kiRat = 0.000_000_1;
    public static double kpRat = 0.0003;

    public static double kdY  = 0;
    public static double kiY =0.000_000_1;
    public static double kpY = 0.0003;

    public static  double maxIY = 0.2;
    public static  double maxIRat = 0.2;
    public static  double maxIX = 0.2;

    public PIDMethod speedX = new PIDMethod(kpX, kiX,kdX,ksX,maxIX);
    public PIDMethod speedRat = new PIDMethod(kpRat,kiRat,kdRat,ksY,maxIY);
    public PIDMethod speedY = new PIDMethod(kpY, kiY,kdY,ksRat,maxIRat);
    public static double ksRat = 0.000479;
    public static double ksY = 0.0004;
    public static double ksX = 0.00045;
    public static double kSlide = 0.85;

    private static double encRatConstant;
    public  final double maxRobotSpeed = diameter*PI/0.2;
    public final double maxCircleRobotSpeed = Math.toDegrees(maxRobotSpeed/trackLength);

    public double targetH = 0;
    public Vector2D vector = new Vector2D(0,0);
    public double targetAngle;

    public double yEnc;
    public double xEnc;
    public double ratEnc;

    public final DcMotorEx left_front_drive;
    public final DcMotorEx left_back_drive;
    public final DcMotorEx right_front_drive;
    public final DcMotorEx right_back_drive;

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
        this.yEnc = (left_back_drive.getVelocity()+ 0*left_front_drive.getVelocity()+
                right_front_drive.getVelocity()*0+ right_back_drive.getVelocity())/2.0;
        this.xEnc = ((-left_back_drive.getVelocity()+ left_front_drive.getVelocity()-
                0*right_front_drive.getVelocity()+ 0*right_back_drive.getVelocity())/2.0)*kSlide;
        this.ratEnc = (left_back_drive.getVelocity()*0+ left_front_drive.getVelocity()-
                right_front_drive.getVelocity()*0 - right_back_drive.getVelocity())/2.0;
    }
    private static double diameter = 0.098;
    private static double encTransmissionCoificent = 1d/20d;
    private static double encCleanConst = 24.0;
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
        double degreesRat = ratEnc;//smToDegrees(encToSm(ratEnc));
        return speedRat.PID(target,this.voltage,degreesRat,ksRat);
    }
    public double velocityMoveX(double target)
    {
        double xEncSm = xEnc;//encToSm(xEnc);
        return speedX.PID(target,this.voltage,xEncSm,ksX);
    }
    public double velocityMoveY(double target)
    {
        double yEncSm = yEnc;//encToSm(yEnc);
        return speedY.PID(target,this.voltage,yEncSm,ksY);
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

    public double velRat = 0;
    public double velX = 0;
    public double velY = 0;

    public void update() {
        encUpdate();
        this.voltage = robot.voltageSensorPoint.getVol();

        this.speedRat.setCoefficent(kpRat,kiRat,kdRat,ksRat,maxIRat);
        this.speedX.setCoefficent(kpX,kiX,kdX,ksX,maxIX);
        this.speedY.setCoefficent(kpY,kiY,kdY,ksY,maxIY);


        velRat = velocityMoveRat(targetH);
        velX = velocityMoveX(vector.x);
        velY = velocityMoveY(vector.y);

        left_front_drive.setPower(velX + velY + velRat);
        right_front_drive.setPower(-velX + velY - velRat);
        left_back_drive.setPower(-velX + velY + velRat);
        right_back_drive.setPower(velX + velY - velRat);
    }
}