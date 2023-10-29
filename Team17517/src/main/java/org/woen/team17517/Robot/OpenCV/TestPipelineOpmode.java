package org.woen.team17517.Robot.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp(name = "Test Pipeline OpMode")
public class TestPipelineOpmode extends LinearOpMode {

    VisionPortal visionPortal;
    PipeLine pipeLine = new PipeLine();
    @Override
    public void runOpMode() throws InterruptedException {
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(pipeLine)
                .build();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("number of parking", pipeLine.pos);
            telemetry.addData("team", pipeLine.team);
            telemetry.update();
        }



        visionPortal.close();
    }
}
