package org.woen.team17517.Programms.Autonomus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.woen.team17517.RobotModules.OpenCV.PipeLine;

@TeleOp

public class OpenCVTest extends LinearOpMode {

    private PipeLine pipeLine;


    public void runOpMode() throws InterruptedException {

        pipeLine = new PipeLine();

        VisionPortal visionPortal;

        visionPortal = new VisionPortal.Builder().setCamera(hardwareMap.get(WebcamName.class, "Webcam 1")).addProcessors(pipeLine).build();

        waitForStart();


        while (opModeIsActive()){

            telemetry.addData("Position", pipeLine.pos);
            telemetry.addData("cx", pipeLine.cx);
            telemetry.update();
        }

        visionPortal.close();
    }
}
