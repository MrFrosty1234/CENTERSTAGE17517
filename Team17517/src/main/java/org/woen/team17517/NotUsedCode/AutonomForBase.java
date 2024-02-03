package org.woen.team17517.NotUsedCode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.OpenCV.Camera;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;
@Config
@TeleOp
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
            robot.driveTrainVelocityControl.moveRobotCord(0,200000,0);
        }
    }

}