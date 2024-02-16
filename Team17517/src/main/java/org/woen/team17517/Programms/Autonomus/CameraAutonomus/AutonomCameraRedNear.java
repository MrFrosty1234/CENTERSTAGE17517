package org.woen.team17517.Programms.Autonomus.CameraAutonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.Programms.Autonomus.AutnomModules;
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
        pipeLine = new PipeLine();

        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();

        waitForStart();


        visionPortal.close();

        robot.linearOpMode.telemetry.addData("Pos", pipeLine);
        if (pipeLine.pos == 3) {
            robot.updateWhilePositionFalse(new Runnable[]{
                    () -> autnomModules.move(-15500, -22000, 0, 1.6),
                    () -> autnomModules.move(0, 22000, 0, 0.5),
                    () -> autnomModules.move(0, 0, 12000, 1.2),
                    () -> autnomModules.move(-6000, -30000, 0, 2.3),
                    () -> autnomModules.bacBoardPixels(),
                    () -> autnomModules.move(0, 30000, 0, 0.5)
                    }

            );
        } else if (pipeLine.pos == 2) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> autnomModules.move(0, -30000, 0, 1.5),
                            () -> autnomModules.move(-10000, 30000, 0, 0.5),
                            () -> autnomModules.move(0, 0, 12000, 1.25),
                            () -> autnomModules.move(4000, -30000, 0, 2.8),
                            () -> autnomModules.bacBoardPixels(),
                            () -> autnomModules.move(0, 30000, 0, 0.5),

                    }
            );
        } else if (pipeLine.pos == 1) {
            robot.updateWhilePositionFalse(new Runnable[]{
                            () -> autnomModules.move(0, -28000, 0, 1.05),
                            () -> autnomModules.move(0, -12000, -12000, 0.9),
                            () -> autnomModules.move(0, 30000, 0, 0.5),
                            () -> autnomModules.move(0, 0, 12000, 2.3),
                            () -> autnomModules.move(10000, -30000, 0, 2.4),
                            () -> autnomModules.bacBoardPixels(),
                            () -> autnomModules.move(0, 30000, 0, 0.5)
                    }
            );
        }
    }
}