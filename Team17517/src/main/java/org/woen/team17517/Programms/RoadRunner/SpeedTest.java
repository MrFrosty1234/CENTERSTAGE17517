package org.woen.team17517.Programms.RoadRunner;

import static org.woen.team17517.RobotModules.DriveTrain.DriveTrainVelocityControl.ENC_TO_SM;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.woen.team17517.RobotModules.UltRobot;
@Config
@Autonomous
public class SpeedTest extends LinearOpMode {
    public  static  double x = 0;
    public static double y = 0;
    public static double h = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        UltRobot robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            robot.driveTrainVelocityControl.moveRobotCord(x / ENC_TO_SM, y / ENC_TO_SM, h);
            robot.allUpdate();

        }
    }
}
