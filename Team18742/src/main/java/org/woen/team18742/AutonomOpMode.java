package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous
public class AutonomOpMode extends LinearOpMode {
    private TeleOpCollector _collector;

    @Override
    public void runOpMode() {
        _collector = new TeleOpCollector(this);

        waitForStart();
        resetRuntime();

        _collector.Start();

        _collector.Driver.DriveDirection(0,0,0);
        ///////////////////////
        _collector.Driver.DriveDirection(-0.3,0,0);
        sleep(2300);
        _collector.Driver.DriveDirection(0.5,0,0);
        sleep(200);
        /////////////////////////
        while (opModeIsActive()) {
            _collector.Update();
        }
    }
}