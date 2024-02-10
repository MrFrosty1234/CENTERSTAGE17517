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
        robot.autnomModules.Move(30000, 0, 0, 2.2);
        robot.autnomModules.Move(0,-30000,0,4);
        //robot.autnomModules.bacBoardPixels();
        robot.autnomModules.Move(30000, 20000, 0, 1.2);
        robot.autnomModules.Move(0, 30000, -400, 6);
        robot.autnomModules.eatPixels();
        robot.autnomModules.Move(0, -30000, 0, 6);
        robot.autnomModules.Move(-30000,-20000 , 0, 1.3);
        robot.autnomModules.bacBoardPixels();
    }

}

