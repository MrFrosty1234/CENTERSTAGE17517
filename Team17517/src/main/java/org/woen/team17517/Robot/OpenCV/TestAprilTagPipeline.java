package org.woen.team17517.Robot.OpenCV;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.woen.team17517.Robot.UltRobot;

import java.util.List;

@TeleOp(name = "AprilTag pipeline")
public class TestAprilTagPipeline {

    private AprilTagProcessor aprilTagPipeline;
    public VisionPortal visionPortal;
    PipeLine colorDetecionPipeLine = new PipeLine();

    FtcDashboard dashboard = null;

    UltRobot robot;
    public VectorF fieldCameraPos;


    public TestAprilTagPipeline(UltRobot robot) {
        this.robot = robot;
    }


    void startPipeline() {
        visionPortal = new VisionPortal.Builder()
                .setCamera(robot.linearOpMode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(colorDetecionPipeLine)
                .addProcessor(aprilTagPipeline)
                .build();
    }

    public void visionPortalWork(){


        dashboard = FtcDashboard.getInstance();

        aprilTagPipeline = new AprilTagProcessor.Builder()
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setDrawAxes(true)
                .build();


        startPipeline();

        while (robot.linearOpMode.opModeIsActive()) {
            List<AprilTagDetection> detections = aprilTagPipeline.getDetections();

            for (AprilTagDetection detection : detections) {
                if (detection != null)
                    if (detection.rawPose != null) {
                        robot.linearOpMode.telemetry.addData("detection id", detection.id);

                        AprilTagPoseRaw rawTagPose = detection.rawPose;
                        VectorF rawTagPoseVector = new VectorF(
                                (float) rawTagPose.x, (float) rawTagPose.y, (float) rawTagPose.z);

                        AprilTagMetadata metadata = detection.metadata;

                        VectorF fieldTagPos = metadata.fieldPosition;

                        Quaternion fieldTagQ = metadata.fieldOrientation;

                        VectorF rotatedPosVector = fieldTagQ.applyToVector(detection.rawPose.R.inverted().multiplied(rawTagPoseVector));

                        fieldCameraPos = fieldTagPos.subtracted(rotatedPosVector);

                        double x = DistanceUnit.CM.fromInches(fieldCameraPos.get(0));
                        double y = DistanceUnit.CM.fromInches(fieldCameraPos.get(1));
                        double z = DistanceUnit.CM.fromInches(fieldCameraPos.get(2));

                       robot.linearOpMode.telemetry.addLine("Camera position:")
                                .addData("x", fieldCameraPos.get(0))
                                .addData("y", fieldCameraPos.get(1))
                                .addData("z", fieldCameraPos.get(2));

                        TelemetryPacket telemetryPacket = new TelemetryPacket();
                        telemetryPacket.addLine("AprilTag test");
                        Canvas canvas = telemetryPacket.fieldOverlay();
                        canvas.setFill("#FFA000").fillCircle(DistanceUnit.INCH.fromCm(x), DistanceUnit.INCH.fromCm(y), DistanceUnit.INCH.fromCm(22.85));
                        telemetryPacket.put("x", x);
                        telemetryPacket.put("y", y);
                        dashboard.sendTelemetryPacket(telemetryPacket);

                    }

            }
            robot.linearOpMode.telemetry.update();
        }

        visionPortal.close();
    }
}

