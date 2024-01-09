package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.vision.VisionPortal;

import org.woen.team17517.Service.Button;
import org.woen.team17517.RobotModules.Camera;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomBaseClass extends LinearOpMode{
    UltRobot robot;
    DcMotor left_front_drive;
    DcMotor left_back_drive;
    DcMotor right_front_drive;
    DcMotor right_back_drive;

    boolean dpadUp = false;
    boolean dpadDown = false;
    boolean dpadRight = false;
    Button dpadRightButton = new Button();
    boolean dpadLeft = false;
    Button dpadLeftButton = new Button();
    Button dpadDownButton = new Button();
    Button dpadUpButton = new Button();
    boolean rightBumper = false;
    Button rightBumperButton   = new Button();
    boolean rightTriger = false;
    Button rigtTrigerButton = new Button();

    Camera camera;
    VisionPortal visionPortal;
    PipeLine pipeLine;

    int positionEllment = 0;

    Button button = new Button();
    StartTeam startTeam = StartTeam.BlUE;
    StartPosition startPosition = StartPosition.RIGHT;
    @Override
    public void waitForStart() {
        long  timeToSleep= 0;
        while (!isStarted()) {
            dpadDown = gamepad1.dpad_down;
            dpadUp = gamepad1.dpad_up;
            dpadLeft = gamepad1.dpad_left;
            dpadRight = gamepad1.dpad_right;
            rightBumper = gamepad1.right_bumper;
            rightTriger = gamepad1.right_trigger > 0;

            if (dpadDownButton.update(dpadDown)){
                startTeam = StartTeam.BlUE;
            }
            if (dpadUpButton.update(dpadUp)){
                startTeam = StartTeam.RED;
            }
            if (dpadLeftButton.update(dpadLeft)){
                startPosition = StartPosition.LEFT;
            }
            if (dpadRightButton.update(dpadRight)){
                startPosition = StartPosition.RIGHT;
            }
            if (rightBumperButton.update(rightBumper)){
                timeToSleep += 1000;
            }
            if (rigtTrigerButton.update(rightTriger)){
                timeToSleep += 5000;
            }

            telemetry.addData("Team",startTeam);
            telemetry.addData("Position",startPosition);
            telemetry.addData("Time to sleep", timeToSleep);
            telemetry.update();
            }
            sleep(timeToSleep);
    }


    enum StartTeam{
        BlUE,
        RED
    }
    enum StartPosition{
        RIGHT,
        LEFT
    }

    public Runnable[] getBlueLeft() {
        return blueLeft;
    }

    public Runnable[] getBlueRight() {
        return blueRight;
    }

    public Runnable[] getRedLeft() {
        return redLeft;
    }

    public Runnable[] getRedRight() {
        return redRight;
    }

    Runnable[] blueRight =  {};
    Runnable[] blueLeft = {};
    Runnable[] redRight = {};
    Runnable[] redLeft = {};

    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");

        robot.grabber.graberToOpen();
        robot.timer.getTimeForTimer(1);
        robot.grabber.graberToClose();


        waitForStart();
        switch (startTeam) {
            case BlUE:
                switch (startPosition){
                    case LEFT:
                        robot.updateWhilePositionFalse(getBlueLeft());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(getBlueRight());
                        break;
                }
            case RED:
                switch (startPosition) {
                    case LEFT:
                        robot.updateWhilePositionFalse(getRedLeft());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(getRedRight());
                        break;
                }
        }
        robot.allUpdate();
    }
}
