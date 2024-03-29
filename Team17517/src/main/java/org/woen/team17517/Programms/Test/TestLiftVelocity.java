package org.woen.team17517.Programms.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
@Config
public class TestLiftVelocity extends LinearOpMode {
    UltRobot robot;
    public static double x = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            robot.hang.up();
            robot.allUpdate();
        }
    }
}
