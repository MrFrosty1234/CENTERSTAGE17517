package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;
import org.woen.team17517.RobotModules.UltRobot;

@Autonomous
public class AutonomCameraBlueNear extends LinearOpMode {
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
        if (pipeLine.pos == 1) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(13500, -22000, 0, 1.6),
                            () -> robot.autnomModules.move(0, 22000, 0, 0.5),
                            () -> robot.autnomModules.move(0, 0, -12000, 1),
                            () -> robot.autnomModules.move(5000, -30000, 0, 2.3),
                            () -> robot.autnomModules.bacBoardPixels(),
                            () -> robot.autnomModules.move(0, 30000, 0, 0.5)
                    }

            );
        } else if (pipeLine.pos == 2) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(0, -30000, 0, 1.35),
                            () -> robot.autnomModules.move(10000, 30000, 0, 0.5),
                            () -> robot.autnomModules.move(0, 0, -12000, 1),
                            () -> robot.autnomModules.move(-6000, -30000, 0, 2.8),
                            () -> robot.autnomModules.bacBoardPixels(),
                            () -> robot.autnomModules.move(0, 30000, 0, 0.5),

                    }
            );
        } else if (pipeLine.pos == 3) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> robot.autnomModules.move(0, -28000, 0, 1.05),
                            () -> robot.autnomModules.move(0, -14000, 12000, 0.9),
                            () -> robot.autnomModules.move(0, 30000, 0, 0.5),
                            () -> robot.autnomModules.move(0, 0, -12000, 2.1),
                            () -> robot.autnomModules.move(-20000, -30000, 0, 2.4),
                            () -> robot.autnomModules.bacBoardPixels(),
                            () -> robot.autnomModules.move(0, 30000, 0, 0.5)
                    }
            );
        }
    }
}
