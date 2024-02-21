package org.woen.team17517.Programms.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.woen.team17517.RobotModules.UltRobot;
import org.woen.team17517.Service.Button;


public abstract class TeleOPBase extends LinearOpMode {
    Button liftUpBut = new Button();
    Button liftDownBut = new Button();
    Button openGrabberBut = new Button();
    Button closeGrabberBut = new Button();
    Button startPlaneBut = new Button();
    Button aimPlaneBut = new Button();
    Button openGrabberMunBut = new Button();
    Button closeGrabberMunBut = new Button();

    public static double aimPos = 0.35;
    public static double startPos = 0.9;
    public static double notStartPose = 0;
    public static double notAimedPos = 0;
    public static boolean telemetryTeleOp =false;
    boolean planeIsAimed = false;
    boolean planeIsStarted = false;
    String planeStatus = "Stay";
    UltRobot robot;
    TeleOpModules teleOpModules;
    @Override
    public void runOpMode() {
        robot = new UltRobot(this);
        waitForStart();
        while (opModeIsActive()){
            robot.telemetryOutput.teleOp = telemetryTeleOp;
        }
    }
     public abstract void main();
}
