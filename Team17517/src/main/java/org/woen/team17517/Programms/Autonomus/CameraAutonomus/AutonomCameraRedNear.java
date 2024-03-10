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
public class AutonomCameraRedNear extends LinearOpMode {
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
            autnomModules.move(0, -28000, 0, 1);
            autnomModules.move(-20000, 28000, 0, 1);
            autnomModules.move(0, -30000, 0, 1);
            autnomModules.move(20000, 0, 0, 1.05);
            robot.grabber.openPurplePixel();
            autnomModules.move(-20000, 0, 0, 1);
            autnomModules.backdropLow();
            autnomModules.move(15000, -35000, 90, 2.4);
            autnomModules.scoring();
            autnomModules.move(0, 20000, 90, 0.3);
            autnomModules.move(-25000, 0, 90, 1.2);

        } else if (pipeLine.pos == 2) {
            autnomModules.move(0, -40000, 0,0.9);
            robot.grabber.openPurplePixel();
            autnomModules.move(-10500, 30000, 0, 0.5);
            autnomModules.move(0, 0, 90, 0);
            autnomModules.backdropLow();
            autnomModules.move(11000, -31000, 90, 1.8);
            autnomModules.scoring();
            autnomModules.move(0, 30000, 90, 0.15);
            autnomModules.move(-30000, 0, 90, 1.1);
        } else if (pipeLine.pos == 3) {
            autnomModules.move(-16000, -32000, 0, 0.9);
            robot.grabber.openPurplePixel();
            autnomModules.move(0, 22000, 0, 0.5);
            autnomModules.backdropLow();
            autnomModules.move(0, 0, 90, 0);
            autnomModules.move(9000, -32000, 90, 1.3);
            autnomModules.scoring();
            autnomModules.move(0, 30000, 90, 0.2);
            autnomModules.move(-30000, 0, 90, 0.9);
        }
    }
}