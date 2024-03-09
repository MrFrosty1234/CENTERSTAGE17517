package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.Programms.Autonomus.AutnomModules;
import org.woen.team17517.RobotModules.Intake.State;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCameraRedFar extends LinearOpMode {
    UltRobot robot;
    PipeLine pipeLine;
    AutnomModules autnomModules;

    public void runOpMode() {
        robot = new UltRobot(this);
        autnomModules = new AutnomModules(robot);

        robot.intake.on();

        pipeLine = new PipeLine();

        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();

        waitForStart();


        visionPortal.close();

        robot.linearOpMode.telemetry.addData("Pos", pipeLine);
        robot.grabber.closePurplePixel();
        if (pipeLine.pos == 1) {
            autnomModules.move(9500, -18000, -10000, 1.6);
            robot.grabber.openPurplePixel();
            autnomModules.move(15000,40000,0,2.4);
        } else if (pipeLine.pos == 2) {
            autnomModules.move(0, -40000, -10000, 0.9);
            robot.grabber.openPurplePixel();
            autnomModules.move(10000,40000,0,2);
        } else if (pipeLine.pos == 3) {
            autnomModules.move(0, -28000, 0, 1.05);
            autnomModules.move(0, -14000, 12000, 0.9);
            autnomModules.move(0, 30000, 0, 1.3);
            robot.grabber.openPurplePixel();
        }
    }
}

