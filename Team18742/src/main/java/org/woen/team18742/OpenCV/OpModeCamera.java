package org.woen.team18742.OpenCV;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

@TeleOp(name = "Test Pipeline OpMode")
public class OpModeCamera extends LinearOpMode {
    VisionPortal visionPortal;
    PipeLine pipeLine = new PipeLine();

    @Override
    public void runOpMode() throws InterruptedException {
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeLine)
                .build();

        visionPortal.setProcessorEnabled(pipeLine, false);

        waitForStart();

        visionPortal.setProcessorEnabled(pipeLine, true);

        while (opModeIsActive()) {
            telemetry.addData("number of parking", pipeLine.pos);
            telemetry.addData("team", pipeLine.team);
            telemetry.addData("camera", visionPortal.getCameraState());
            telemetry.update();
        }


        visionPortal.close();
    }
}
