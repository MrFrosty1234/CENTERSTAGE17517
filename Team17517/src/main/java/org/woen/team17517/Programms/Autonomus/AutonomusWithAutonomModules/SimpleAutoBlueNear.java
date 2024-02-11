package org.woen.team17517.Programms.Autonomus.AutonomusWithAutonomModules;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class SimpleAutoBlueNear extends LinearOpMode {
    UltRobot robot;
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        robot.autnomModules.move(-60000, -60000, 0, 0.9);
        robot.autnomModules.move(0, -30000, 0, 2);
        robot.autnomModules.bacBoardPixels();
        robot.autnomModules.move(0,15000,0,1);
        robot.autnomModules.move(30000,0,0,2);
    }
}
