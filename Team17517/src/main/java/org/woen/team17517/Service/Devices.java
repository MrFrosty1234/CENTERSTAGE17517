package org.woen.team17517.Service;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.woen.team17517.RobotModules.UltRobot;

@com.acmerobotics.dashboard.config.Config
public class Devices {
    UltRobot robot;
    public Devices(UltRobot robot){
        left_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "left_back_drive");

        right_front_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "right_back_drive");

        odometrLeft = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "odometrLeft");

        liftMotor = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "liftMotor");

        intakeMotor = robot.linearOpMode.hardwareMap.get(DcMotorEx.class, "intakeMotor");

        progibServo = robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLift");
        pixelServoRight = robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoRight");
        pixelServoLeft = robot.linearOpMode.hardwareMap.get(Servo.class, "pixelServoLeft");

        aimPlaneServo = robot.linearOpMode.hardwareMap.get(Servo.class,"planeServo");
        startPlaneSrevo = robot.linearOpMode.hardwareMap.get(Servo.class,"planeServoStart");
        buttonUp = robot.linearOpMode.hardwareMap.digitalChannel.get("buttonUp");
        buttonDown = robot.linearOpMode.hardwareMap.digitalChannel.get("buttonDown");
        reset();

        this.robot = robot;
    }
    public DcMotorEx left_front_drive;
    public DcMotorEx left_back_drive;
    public DcMotorEx right_front_drive;
    public DcMotorEx right_back_drive;
    public DcMotorEx liftMotor;
    public DcMotorEx intakeMotor;
    public DcMotorEx odometrLeft;

    public Servo pixelServoRight;
    public Servo progibServo;
    public Servo pixelServoLeft;
    public Servo startPlaneSrevo;
    public Servo aimPlaneServo;
    public DigitalChannel buttonUp;
    public DigitalChannel buttonDown;



    private void reset() {
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

        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        buttonUp.setMode(DigitalChannel.Mode.INPUT);
        buttonDown.setMode(DigitalChannel.Mode.INPUT);

    }
}
