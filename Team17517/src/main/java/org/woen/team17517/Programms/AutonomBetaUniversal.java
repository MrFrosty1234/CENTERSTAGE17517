package org.woen.team17517.Programms;

import android.widget.VideoView;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
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
        waitForStart();
        robot.driveTrain.moveField(140,30,0);
        robot.driveTrain.moveField(140,60,0);
        robot.driveTrain.moveField(200,60,0);

    }
}