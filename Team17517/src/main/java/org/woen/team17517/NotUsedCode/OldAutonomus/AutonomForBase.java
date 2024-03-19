package org.woen.team17517.NotUsedCode.OldAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

public class AutonomForBase extends LinearOpMode {
    UltRobot robot;
    public static double x;
    public static double y = 1000;
    public static double targetH;
    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            robot.updateWhilePositionFalse(new Runnable[]{
                    ()->robot.driveTrainVelocityControl.moveRobotCord(x,y,targetH),
                    ()->robot.timer.waitSeconds(1),
                    ()->robot.driveTrainVelocityControl.moveRobotCord(-x,-y,-targetH),
                    ()->robot.timer.waitSeconds(1)
            });

        }
    }

}