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

import java.util.List;

@TeleOp(name = "AprilTag pipeline")
public class TestAprilTagPipeline extends LinearOpMode {

    private AprilTagProcessor aprilTagPipeline;
    VisionPortal visionPortal;
    PipeLine colorDetecionPipeLine = new PipeLine();

    FtcDashboard dashboard = null;

    @Override
    public void runOpMode() throws InterruptedException {

        dashboard = FtcDashboard.getInstance();

        aprilTagPipeline = new AprilTagProcessor.Builder()
                /* Задать единицы в градусах и сантиметрах */
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setDrawAxes(true)
                //.setLensIntrinsics(622.001f,622.001f,319.803f,241.251f)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                //   .addProcessor(colorDetecionPipeLine) // Добавить пайплайн для цветов
                .addProcessor(aprilTagPipeline) // Добавить пайплайн для тегов
                .build(); // Запустить пайплайны

        waitForStart();

        while (opModeIsActive()) {
            List<AprilTagDetection> detections = aprilTagPipeline.getDetections();

            for (AprilTagDetection detection : detections) {
                if (detection != null)
                    if (detection.rawPose != null) {
                        telemetry.addData("detection id", detection.id);

                        /* Считать позицию тэга относительно камеры и записать её в VectorF*/
                        AprilTagPoseRaw rawTagPose = detection.rawPose;
                        VectorF rawTagPoseVector = new VectorF(
                                (float) rawTagPose.x, (float) rawTagPose.y, (float) rawTagPose.z);
                        /* Cчитать метаданные из тэга */
                        AprilTagMetadata metadata = detection.metadata;
                        /* Достать позицию тега относительно поля */
                        VectorF fieldTagPos = metadata.fieldPosition;
                        /* Достать угол тега относительно поля */
                        Quaternion fieldTagQ = metadata.fieldOrientation;
                        /* Повернуть относительное положение на угол между тегом и полем */
                        VectorF rotatedPosVector = fieldTagQ.applyToVector(detection.rawPose.R.inverted().multiplied(rawTagPoseVector));
                        /* Вычесть полученное положение камеры из абсолютного положения тега */
                        VectorF fieldCameraPos = fieldTagPos.subtracted(rotatedPosVector);

                        double x = DistanceUnit.CM.fromInches(fieldCameraPos.get(0));
                        double y = DistanceUnit.CM.fromInches(fieldCameraPos.get(1));
                        double z = DistanceUnit.CM.fromInches(fieldCameraPos.get(2));

                        telemetry.addLine("Camera position:")
                                .addData("x", fieldCameraPos.get(0))
                                .addData("y", fieldCameraPos.get(1))
                                .addData("z", fieldCameraPos.get(2));

                        TelemetryPacket telemetryPacket = new TelemetryPacket();
                        telemetryPacket.addLine("AprilTag test");
                        Canvas canvas = telemetryPacket.fieldOverlay();
                        canvas.setFill("#FFA000").fillCircle(DistanceUnit.INCH.fromCm(x), DistanceUnit.INCH.fromCm(y),DistanceUnit.INCH.fromCm(22.85));
                        telemetryPacket.put("x", x);
                        telemetryPacket.put("y", y);
                        dashboard.sendTelemetryPacket(telemetryPacket);

                    }

            }
            telemetry.update();
        }

        visionPortal.close();
    }
}