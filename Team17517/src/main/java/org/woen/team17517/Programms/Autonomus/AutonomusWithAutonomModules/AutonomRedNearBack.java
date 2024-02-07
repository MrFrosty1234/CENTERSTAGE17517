package org.woen.team17517.Programms.Autonomus.AutonomusWithAutonomModules;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomRedNearBack extends LinearOpMode {
    UltRobot robot;
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        robot.autnomModules.Move(24000, -24000, 0, 1);
        robot.autnomModules.bacBoardPixels();
        robot.autnomModules.Move(20000, -24000, 0, 1);
        robot.autnomModules.Move(0, 24000, 0, 2);
        robot.autnomModules.eatPixels();
        robot.autnomModules.Move(0, -24000, 0, 2);
        robot.autnomModules.Move(-20000, 24000, 0, 1);
        robot.autnomModules.bacBoardPixels();
    }

}

