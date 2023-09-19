package org.woen.team17517.Programms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Robot.Robot;
import org.woen.team17517.Robot.Camera;

@Autonomous
public class AutonomBetaUniversal extends LinearOpMode {
    Robot robot;
    Camera camera;

    public void runOpMode() {
        robot = new Robot(this);
        camera = new Camera(hardwareMap);
        robot.lift.reset();
        waitForStart();
        int c = camera.readCamera();
        telemetry.addData("camera", c);
        telemetry.update();
        camera.stopcamera();
        robot.grabber.setPosition(false);
        robot.driveTrain.moveField(60, 0, 0);
        if (c == 18) {
            robot.driveTrain.moveField(60, 0, -90);
            robot.driveTrain.moveField(60, 55, -90);
            robot.driveTrain.moveField(60, 55, -180);
        }
        if (c == 6) {
            robot.driveTrain.moveField(60, 0, 90);
            robot.driveTrain.moveField(60, -55, 90);
            robot.driveTrain.moveField(60, -55, 180);
        }
        if (c == 0) {
            robot.driveTrain.moveField(60, 0, 90);
        }
    }
}
