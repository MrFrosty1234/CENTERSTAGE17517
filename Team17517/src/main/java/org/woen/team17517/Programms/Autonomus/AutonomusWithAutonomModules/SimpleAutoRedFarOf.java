package org.woen.team17517.Programms.Autonomus.AutonomusWithAutonomModules;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class SimpleAutoRedFarOf extends LinearOpMode {
    UltRobot robot;
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        robot.autnomModules.Move(30000, 0, 0, 2.2);
        robot.autnomModules.Move(0,-30000,0,4);
        robot.autnomModules.Move(0,15000,0,1);
        robot.autnomModules.bacBoardPixels();
    }

}

