package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomDriveTrainKoff extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode (){
        robot = new UltRobot(this);
        while (opModeIsActive()) {
            robot.drivetrainNew.setTarget(1000,0,0);
            robot.drivetrainNew.setTarget(-1000,0,0);
            robot.allUpdate();
        }
    }

}
