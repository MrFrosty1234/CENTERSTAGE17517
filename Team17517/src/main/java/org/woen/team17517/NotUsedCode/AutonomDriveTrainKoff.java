package org.woen.team17517.NotUsedCode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;


public class AutonomDriveTrainKoff extends LinearOpMode {
    UltRobot robot;
    @Override
    public void runOpMode (){
        waitForStart();
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()) {
            robot.updateWhilePositionFalse(new Runnable[]{()->robot.drivetrainNew.setTarget(500,0,0),
                                                            ()->robot.drivetrainNew.setTarget(-500,0,0)});
        }
    }

}
