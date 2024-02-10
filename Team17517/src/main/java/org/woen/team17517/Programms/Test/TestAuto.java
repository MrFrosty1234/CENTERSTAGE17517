package org.woen.team17517.Programms.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class TestAuto extends LinearOpMode {
    UltRobot robot;
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()) {
            robot.lift.moveUP();
            robot.allUpdate();
        }
    }
}