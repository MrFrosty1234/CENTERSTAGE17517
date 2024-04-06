package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.Programms.Autonomus.AutnomModules;
import org.woen.team17517.RobotModules.Intake.Lift.LiftPosition;
import org.woen.team17517.RobotModules.Intake.State;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCameraBlueFar extends LinearOpMode {
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
            autnomModules.move(0, -28000, 0, 1.3);
            robot.grabber.openPurplePixel();
            autnomModules.move(-20000, 28000, 0, 1);
            autnomModules.move(0, -30000, 0, 1.45);
            autnomModules.move(30000, 0, 0, 1);
            autnomModules.move(-20000, 0, 0, 1);
            ////////////////
            autnomModules.move(0,-30000,0,0.85);
            autnomModules.move(0,0,-90,0);
            autnomModules.move(0,-40000,-90,3);
            autnomModules.move(40000,0,-90,1.2);
            autnomModules.backdropLow();
            autnomModules.move(0,-20000,-90,1);
            autnomModules.scoring();
            robot.intake.setState(State.WAIT_DOWN);
            autnomModules.move(0,30000,-90,0.5);
            autnomModules.move(30000,0,-90,1.5);
        } else if (pipeLine.pos == 2) {
            autnomModules.move(0, -40000, 0,1);
            robot.grabber.openPurplePixel();
            autnomModules.move(0, 40000, 0,0.25);
            autnomModules.move(0,-10000,-90,0.5);
            /////////////////
            autnomModules.move(-20000,0,-90,0.5);
            autnomModules.move(0,-40000,-90,2.3);
            autnomModules.backdropLow();
            autnomModules.move(-2500,-20000,-90,1.5);
            autnomModules.scoring();
            robot.intake.setState(State.WAIT_DOWN);
            autnomModules.move(0,30000,-90,0.5);
            autnomModules.move(30000,0,-90,1.5);
        } else if (pipeLine.pos == 3) {
            autnomModules.move(-17500, -32000, 0, 1.2);
            robot.grabber.openPurplePixel();
            autnomModules.move(0, 22000, 0, 0.5);
            ///////////
            autnomModules.move(0,20000,0,0.5);
            autnomModules.move(30000,0,0,0.6);
            autnomModules.move(0,-30000,0,1.9);
            autnomModules.move(0,0,-90,0);
            autnomModules.move(0,-40000,-90,2.8);
            autnomModules.move(40000,0,-90,0.8);
            autnomModules.backdropLow();
            autnomModules.move(0,-20000,-90,1);
            autnomModules.scoring();
            robot.intake.setState(State.WAIT_DOWN);
            autnomModules.move(0,30000,-90,0.5);
            autnomModules.move(30000,0,-90,1.5);
        }
    }
}

