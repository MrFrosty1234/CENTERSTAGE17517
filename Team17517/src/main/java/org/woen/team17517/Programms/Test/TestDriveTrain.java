package org.woen.team17517.Programms.Test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
@Config
public class TestDriveTrain extends LinearOpMode {
    UltRobot robot;
    public static double x = 0;
    public static double y = 0;
    public static double h = 0;
    @Override
    public void runOpMode(){
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            robot.updateWhilePositionFalse(new Runnable[]{
                    ()->robot.driveTrainVelocityControl.moveAngle(h),
                    ()->robot.driveTrainVelocityControl.moveWithAngleControl(x,y),
                    ()->robot.timer.getTimeForTimer(0.5),
                    ()->robot.driveTrainVelocityControl.moveWithAngleControl(-x,-y),
                    ()->robot.timer.getTimeForTimer(0.5),
            });
        }
    }
}
