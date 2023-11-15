package org.woen.team18742.OpenCV;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp(name = "Test Pipeline OpMode")
public class OpMode extends LinearOpMode {
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
            telemetry.addData("camera", visionPortal.getCameraState());
            telemetry.update();

        }



        visionPortal.close();
    }
}
