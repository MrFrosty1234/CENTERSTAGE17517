package org.woen.team17517.Robot;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.signum;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DriveTrainVelocityControl {
    UltRobot robot;
    private double voltage;
    private double kd;
    private double ki;
    private double kp;
    private double ks;
    private double kSlide;
    PIDCmethod speed = new PIDCmethod(kp,ki,kd,ks);
    private PIDCmethod PIDvelocity = new PIDCmethod(kp,ki,kd,ks);
    private double targetX = 0;
    private  double targetY = 0;
    private double yEnc;
    private double xEnc;
    private final DcMotorEx leftFrontDrive;
    private final DcMotorEx leftBackDrive;
    private final DcMotorEx rightFrontDrive;
    private final DcMotorEx rightBackDrive;
    public DriveTrainVelocityControl(UltRobot robot)
    {
        this.robot=robot;

        leftFrontDrive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "leftFrontDrive");
        leftBackDrive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "leftBackDrive");

        rightFrontDrive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "rightFrontDrive");
        rightBackDrive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "rightBackDrive");

        voltage = robot.voltageSensorPoint.getVol();

        reset();
    }
    public void reset()
    {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void  encUpdate()
    {
        this.xEnc = (leftBackDrive.getVelocity()+leftFrontDrive.getVelocity()+
                rightFrontDrive.getVelocity()+rightBackDrive.getVelocity())/4.0;
        this.yEnc =((-leftBackDrive.getVelocity()+leftFrontDrive.getVelocity()-
                rightFrontDrive.getVelocity()+rightBackDrive.getVelocity())/4.0)*kSlide;
    }

    public double velocityMoveY(double target)
    {
        return speed.PID(target,this.voltage,this.yEnc,ks);
    }

    public double velocityMoveX(double target)
    {
        return speed.PID(target,this.voltage,this.xEnc,ks);
    }

    public void update()
    {
        encUpdate();
        double velX = velocityMoveX(targetX);
        double velY = velocityMoveY(targetY);
        leftFrontDrive.setPower(velX+velY);
        leftBackDrive.setPower(velX-velY);
        rightBackDrive.setPower(velX+velY);
        rightFrontDrive.setPower(velX-velY);
    }
}