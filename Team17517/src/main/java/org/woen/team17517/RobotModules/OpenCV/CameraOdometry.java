package org.woen.team17517.RobotModules.OpenCV;


import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.team17517.RobotModules.UltRobot;

import java.util.List;




public class CameraOdometry{

    UltRobot robot;

    private static final boolean USE_WEBCAM = true;

    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;
    public CameraOdometry(UltRobot robot) {
        this.robot = robot;
    }

    public void camera() {

        initAprilTag();

        robot.linearOpMode.telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        robot.linearOpMode.telemetry.addData(">", "Touch Play to start OpMode");
        robot.linearOpMode.telemetry.update();
        robot.linearOpMode.waitForStart();

        if (robot.linearOpMode.opModeIsActive()) {
            while (robot.linearOpMode.opModeIsActive()) {

                telemetryAprilTag();

                robot.linearOpMode.telemetry.update();

                if (robot.linearOpMode.gamepad1.dpad_down) {
                    visionPortal.stopStreaming();
                } else if (robot.linearOpMode.gamepad1.dpad_up) {
                    visionPortal.resumeStreaming();
                }

                robot.linearOpMode.sleep(20);
            }
        }

        visionPortal.close();

    }

    private void initAprilTag() {

        aprilTag = new AprilTagProcessor.Builder()


                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        if (USE_WEBCAM) {
            builder.setCamera(robot.linearOpMode.hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }


        builder.addProcessor(aprilTag);

        visionPortal = builder.build();


    }


    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        robot.linearOpMode.telemetry.addData("# AprilTags Detected", currentDetections.size());

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                robot.linearOpMode. telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                robot.linearOpMode.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                robot.linearOpMode. telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                robot.linearOpMode.telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                robot.linearOpMode.telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                robot.linearOpMode.telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
        robot.linearOpMode.telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        robot.linearOpMode.telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        robot.linearOpMode.telemetry.addLine("RBE = Range, Bearing & Elevation");
    }

}
