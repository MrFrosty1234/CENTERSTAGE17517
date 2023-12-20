package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.woen.team17517.Robot.UltRobot;

public class AutonomBaseClass extends LinearOpMode{
    UltRobot robot;
    DcMotor left_front_drive;
    DcMotor left_back_drive;

    DcMotor right_front_drive;
    DcMotor right_back_drive;
    @Override
        public void waitForStart() {
            while (!isStarted()) {
                if (gamepad1.left_bumper){

                }
            }
    }


    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");
        waitForStart();


    }
}
