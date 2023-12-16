package org.woen.team17517.Programms;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.woen.team17517.Robot.UltRobot;

@Autonomous
public class AutonomBase extends LinearOpMode {
    DcMotor left_front_drive;
    DcMotor left_back_drive;

    DcMotor right_front_drive;
    DcMotor right_back_drive;
    UltRobot robot;
    @Override
    public void runOpMode() {
        robot = new UltRobot(this);
        left_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_front_drive");
        left_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("left_back_drive");
        right_front_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_front_drive");
        right_back_drive = robot.linearOpMode.hardwareMap.dcMotor.get("right_back_drive");

        right_front_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        left_back_drive.setDirection(DcMotorSimple.Direction.FORWARD);



        waitForStart();
        left_front_drive.setPower(1);
        left_back_drive.setPower(1);
        right_front_drive.setPower(1);
        right_back_drive.setPower(1);
        sleep(800);
        left_front_drive.setPower(0);
        left_back_drive.setPower(0);
        right_front_drive.setPower(0);
        right_back_drive.setPower(0);

    }

}
