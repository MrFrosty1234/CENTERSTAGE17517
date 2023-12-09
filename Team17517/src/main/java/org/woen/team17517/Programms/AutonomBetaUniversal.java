package org.woen.team17517.Programms;

import android.widget.VideoView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.Robot.Lift;
import org.woen.team17517.Robot.OpenCV.PipeLine;
import org.woen.team17517.Robot.UltRobot;
import org.woen.team17517.Robot.Camera;

@Autonomous
public class AutonomBetaUniversal extends LinearOpMode {
    UltRobot robot;
    Camera camera;
    VisionPortal visionPortal;
    PipeLine pipeLine;
    public void runOpMode() {

        robot = new UltRobot(this);

        camera = new Camera(hardwareMap);
        pipeLine = new PipeLine();
        robot.lift.reset();
        int positionEllment = pipeLine.pos;
        waitForStart();
        robot.grabber.closeGraber();
        Runnable[] actions = {
                ()->{if(positionEllment == 1)
                    robot.driveTrain.moveField(60,-60,0);
                    if(positionEllment == 2)
                        robot.driveTrain.moveField(60,0,0);
                    if(positionEllment == 3)
                        robot.driveTrain.moveField(60,60, 0);
                    },
                ()->{
                    robot.driveTrain.moveField(60,60,90);
                },
                ()-> {
                    robot.driveTrain.moveField(60,200,90);

                },
                ()->{
                    robot.driveTrain.moveField(60,300,90);
                },
                ()->{
                    robot.driveTrain.moveField(30,300,90);
                },
                ()-> {
                    robot.driveTrain.moveField(30,350,90);
                }
        };
        Runnable[] liftAndPixels = {
                ()->{
                    robot.grabber.powerPixelMotor(-1);
                    robot.timer.getTimeForTimer(500);
                    robot.grabber.powerPixelMotor(0);
                },
                ()->{
                    robot.lift.liftPosition = Lift.LiftPosition.UP;
                    robot.grabber.perekidStart();
                    robot.timer.getTimeForTimer(500);
                    robot.grabber.openGraber();
                    robot.timer.getTimeForTimer(500);
                    robot.grabber.perekidFinish();
                },
        };
        Runnable[] allTasks = {
                ()->{
                    robot.updateWhilePositionFalse(actions);
                    robot.updateWhilePositionFalse(liftAndPixels);
                }
        };


    }
}