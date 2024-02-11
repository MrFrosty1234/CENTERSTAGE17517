package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCameraRedNear extends LinearOpMode {
    UltRobot robot;

    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        robot.autnomModules.cameraMove();
        sleep(100000);
    }
}