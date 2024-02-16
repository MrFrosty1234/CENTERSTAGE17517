package org.woen.team17517.Programms.Autonomus.AutonomusWithOutCamera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Programms.Autonomus.AutnomModules;
import org.woen.team17517.RobotModules.UltRobot;
@Autonomous
public class SimpleAutoRedNear extends LinearOpMode {
    UltRobot robot;
    AutnomModules autnomModules;
    public void runOpMode() {
        robot = new UltRobot(this);
        autnomModules = new AutnomModules(robot);
        waitForStart();
        autnomModules.move(60000, -60000, 0, 0.9);
        autnomModules.move(0, -30000, 0, 2);
        autnomModules.bacBoardPixels();
        autnomModules.move(0,15000,0,1);
        autnomModules.move(-30000,0,0,2);
    }

}
