package org.woen.team17517.NotUsedCode.OldAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

import org.woen.team17517.Service.Button;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;


public class AutonomBaseClass extends LinearOpMode{
    public UltRobot robot;
    DcMotor left_front_drive;
    DcMotor left_back_drive;
    DcMotor right_front_drive;
    DcMotor right_back_drive;


    public int positionOfPixel = 1;
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
    PipeLine pipeLine;
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

    public Runnable[] getBlueNearBack() {
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

        pipeLine = new PipeLine();

        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();
        sleep(1500);




        waitForStart();


        positionOfPixel = pipeLine.pos;
        telemetry.addData("Position", pipeLine.pos);
        telemetry.update();
        sleep(1000);
        visionPortal.close();
        switch (startTeam) {
            case BlUE:
                switch (startPosition){
                    case LEFT:
                        robot.updateWhilePositionFalse(getBlueLeft());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(getBlueNearBack());
                        break;
                }
                break;
            case RED:
                switch (startPosition) {
                    case LEFT:
                        robot.updateWhilePositionFalse(getRedLeft());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(getRedRight());
                        break;
                }
                break;
        }
        robot.allUpdate();
    }
}
