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
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeLine)
                .build();
        telemetry.update();
        visionPortal.close();
        robot.grabber.enable(true);
       if(pipeLine.pos == 1)
           robot.driveTrain.moveField(60, 60, 0);
       if(pipeLine.pos == 2)
           robot.driveTrain.moveField(60,0,0);
       if(pipeLine.pos == 3)
           robot.driveTrain.moveField(60,-60,0);
    }
}
