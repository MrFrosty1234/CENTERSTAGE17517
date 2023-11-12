package org.woen.team17517.Robot;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
public class DriveTrainVelocityControl {
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
    private static double encConstant = 24, encRatConstant;
    private static double encTransmissionCoificent = 1d/20d;
    private static double diameter = 0.098;
    private static double trackLength = 27d/2d;
    private static double ksRat = 1d/2400d;
    private static double ksY = 1d/2400d;
    private static double ksX = 1d/2400d;
    private static double kSlide = 0.85;
    private PIDCmethod speedX = new PIDCmethod(kpX, kiX,kdX,ksX);
    private PIDCmethod speedRat = new PIDCmethod(kpRat,kiRat,kdRat,ksY);
    private PIDCmethod speedY = new PIDCmethod(kpY, kiY,kdY,ksRat);
    public double targetX   = 0;
    public double targetY   = 0;
    public double targetRat = 0;
    public double yEnc;
    public double xEnc;
    public double ratEnc;
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
    public double encToSmRat()
    {
        encUpdate();
        return ratEnc*encRatConstant;
    }
    public double cmToDegrees(double cm)
    {
        return (360*(cm/trackLength*2*Math.PI));
    }
    public double velocityMoveRat(double target)
    {
        double DegTarget = cmToDegrees(target);
        return speedRat.PID(DegTarget,this.voltage,this.ratEnc,ksRat);
    }
    public double velocityMoveX(double target)
    {
        return speedY.PID(target,this.voltage,this.xEnc,ksX);
    }
    public double velocityMoveY(double target)
    {
        return speedX.PID(target,this.voltage,this.yEnc,ksY);
    }
    public double getMetersPerSecondSpeed(double target)
    {
        return target/encConstant*encTransmissionCoificent*diameter*Math.PI;
    }

    public void update() {
        encUpdate();
        this.speedRat.setCoificent(kpRat,kiRat,kdRat,ksRat);
        this.speedX.setCoificent(kpX,kiX,kdX,ksX);
        this.speedY.setCoificent(kpY,kiY,kdY,ksY);
        double velRat = velocityMoveRat(targetRat);
        double velX = velocityMoveX(targetX);
        double velY = velocityMoveY(targetY);
        left_front_drive.setPower(velX + velY + velRat);
        right_front_drive.setPower(-velX + velY - velRat);
        left_back_drive.setPower(-velX + velY + velRat);
        right_back_drive.setPower(velX + velY - velRat);
    }
}