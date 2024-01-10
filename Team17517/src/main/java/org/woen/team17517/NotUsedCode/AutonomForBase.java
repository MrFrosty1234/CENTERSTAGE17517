package org.woen.team17517.NotUsedCode;

import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.Camera;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;


public class AutonomForBase {
    UltRobot robot;
    Camera camera;
    VisionPortal visionPortal;
    PipeLine pipeLine;


   /* public AutonomForBase(UltRobot robot){
        this.robot = robot;

        camera = new Camera(robot.linearOpMode.hardwareMap);
        pipeLine = new PipeLine();
        robot.lift.reset();
        int positionEllment = pipeLine.pos;
        robot.linearOpMode.waitForStart();
        robot.grabber.closeGraber();
        Runnable[] actions = {
                () -> {
                    if (positionEllment == 1)
                        robot.driveTrain.moveField(60, -60, 0);
                    if (positionEllment == 2)
                        robot.driveTrain.moveField(60, 0, 0);
                    if (positionEllment == 3)
                        robot.driveTrain.moveField(60, 60, 0);
                },
                () -> {
                    robot.driveTrain.moveField(60, 60, 90);
                },
                () -> {
                    robot.driveTrain.moveField(60, 200, 90);

                },
                () -> {
                    robot.driveTrain.moveField(60, 300, 90);
                },
                () -> {
                    robot.driveTrain.moveField(30, 300, 90);
                },
                () -> {
                    robot.driveTrain.moveField(30, 350, 90);
                }
        };
        Runnable[] liftAndPixels = {
                () -> {
                    robot.grabber.powerPixelMotor(-1);
                },
                () -> {
                    robot.timer.getTimeForTimer(500);
                },
                () -> {
                    robot.grabber.powerPixelMotor(0);
                },
                () -> {
                    robot.lift.moveUP();
                },
                () -> {
                    robot.grabber.perekidStart();
                },
                () -> {
                    robot.timer.getTimeForTimer(500);
                },
                () -> {
                    robot.grabber.openGraber();
                },
                () -> {
                    robot.timer.getTimeForTimer(500);
                },
                () -> {
                    robot.grabber.perekidFinish();
                }
        };

        robot.updateWhilePositionFalse(actions);
        robot.updateWhilePositionFalse(liftAndPixels);
    }
    */
}