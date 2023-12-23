package org.woen.team17517.Programms;

import android.icu.text.Transliterator;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.R;
import org.woen.team17517.Robot.Button;
import org.woen.team17517.Robot.Camera;
import org.woen.team17517.Robot.OpenCV.PipeLine;
import org.woen.team17517.Robot.UltRobot;

import java.nio.Buffer;
import java.util.HashMap;
@TeleOp
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
    Runnable[] blueRight =  {
            () -> {
                robot.driveTrain.moveField(60, -60, 0);
            },
            /*    if (positionEllment == 1)
                    robot.driveTrain.moveField(60, -60, 0);
                if (positionEllment == 2)
                    robot.driveTrain.moveField(60, 0, 0);
                if (positionEllment == 3)
                    robot.driveTrain.moveField(60, 60, 0);
            },
            () -> {
                robot.driveTrain.moveField(60, 60, 90);
            },
            () -> {
                robot.driveTrain.moveField(60, 200, 90);

            },
            () -> {
                robot.driveTrain.moveField(60, 300, 90);
            },
            () -> {
                robot.driveTrain.moveField(30, 300, 90);
            },
            () -> {
                robot.driveTrain.moveField(30, 350, 90);
            }

             */
    };
    Runnable[] blueLeft;
    Runnable[] redRight = {
            () -> {
                robot.driveTrain.moveField(60, -60, 0);
            },
         /*       if (positionEllment == 1)
                    robot.driveTrain.moveField(60, -60, 0);
                if (positionEllment == 2)
                    robot.driveTrain.moveField(60, 0, 0);
                if (positionEllment == 3)
                    robot.driveTrain.moveField(60, 60, 0);
            },
           () -> {
                robot.driveTrain.moveField(60, 60, 90);
            },
            () -> {
                robot.driveTrain.moveField(60, 200, 90);

            },
            () -> {
                robot.driveTrain.moveField(60, 300, 90);
            },
            () -> {
                robot.driveTrain.moveField(30, 300, 90);
            },
            () -> {
                robot.driveTrain.moveField(30, 350, 90);

            }
          */
    };
    Runnable[] redLeft;

    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");

        waitForStart();

        switch (startTeam) {
            case BlUE:
                switch (startPosition){
                    case LEFT:
                        robot.updateWhilePositionFalse(blueLeft);
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(blueRight);
                        break;
                }
            case RED:
                switch (startPosition) {
                    case LEFT:
                        robot.updateWhilePositionFalse(redLeft);
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(redRight);
                        break;
                }
        }
    }
}
