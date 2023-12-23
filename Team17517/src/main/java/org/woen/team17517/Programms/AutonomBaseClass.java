package org.woen.team17517.Programms;

import android.icu.text.Transliterator;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.Robot.Button;
import org.woen.team17517.Robot.UltRobot;

import java.nio.Buffer;
import java.util.HashMap;

public class AutonomBaseClass extends LinearOpMode{
    UltRobot robot;
    DcMotor left_front_drive;
    DcMotor left_back_drive;

    DcMotor right_front_drive;
    DcMotor right_back_drive;
    boolean dpadUp = gamepad1.dpad_up;
    boolean dpadDown = gamepad1.dpad_down;
    boolean dpadRight = gamepad1.dpad_right;
    boolean dpadLeft = gamepad1.dpad_left;
    boolean rightBumper = gamepad1.right_bumper;
    Button button = new Button();
    StartTeam startTeam = StartTeam.BlUE;
    StartPosition startPosition = StartPosition.RIGHT;
    @Override
    public void waitForStart() {
        long  timeToSleep= 0;
        while (!isStarted()) {
            if (button.update(dpadDown)){
                startTeam = StartTeam.BlUE;
            }
            if (button.update(dpadUp)){
                startTeam = StartTeam.RED;
            }
            if (button.update(dpadLeft)){
                startPosition = StartPosition.LEFT;
            }
            if (button.update(dpadRight)){
                startPosition = StartPosition.RIGHT;
            }
            if (button.update(rightBumper)){
                timeToSleep += 1000;
            }
            telemetry.addData("Team",startTeam);
            telemetry.addData("Position",startPosition);
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
    EncoderMove blueRight;
    EncoderMove blueLeft;
    EncoderMove redRight;
    EncoderMove redLeft;

    public void setBlueLeft(Runnable[] blueLeft) {
        this.blueLeft.setActions(blueLeft);
    }
    public void setBlueRight(Runnable[] blueRight){
        this.blueRight.setActions(blueRight);
    }
    public void setRedLeft(Runnable[] redLeft){
        this.blueRight.setActions(redLeft);
    }
    public void setRedRight(Runnable[] redRight){
        this.blueRight.setActions(redRight);
    }

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
                        robot.updateWhilePositionFalse(blueLeft.getActions());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(blueRight.getActions());
                        break;
                }
            case RED:
                switch (startPosition) {
                    case LEFT:
                        robot.updateWhilePositionFalse(redLeft.getActions());
                        break;
                    case RIGHT:
                        robot.updateWhilePositionFalse(redRight.getActions());
                        break;
                }
        }
    }
}
