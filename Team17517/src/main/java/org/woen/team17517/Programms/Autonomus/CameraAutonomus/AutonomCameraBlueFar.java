package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCameraBlueFar extends LinearOpMode {
    UltRobot robot;
    PipeLine pipeLine;


    public void runOpMode() {
        robot = new UltRobot(this);

        pipeLine = new PipeLine();

        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();

        waitForStart();


        visionPortal.close();

        robot.linearOpMode.telemetry.addData("Pos", pipeLine);
        if (pipeLine.pos == 3) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(-15500, -22000, 0, 1.6),
                            () -> robot.autnomModules.move(0, 22000, 0, 0.5)
                    }

            );
        } else if (pipeLine.pos == 2) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(0, -30000, 0, 1.45),
                            () -> robot.autnomModules.move(-10000, 30000, 0, 1.25),
                    () -> robot.autnomModules.move(0,0,24000,0.6),
                    () -> robot.autnomModules.move(50000,0,0,1.8)
                    }
            );
        } else if (pipeLine.pos == 1) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(0, -28000, 0, 1.05),
                            () -> robot.autnomModules.move(0, -13000, -12000, 0.9),
                            () -> robot.autnomModules.move(0, 30000, 0, 0.5)
                    }
            );
        }
    }
}