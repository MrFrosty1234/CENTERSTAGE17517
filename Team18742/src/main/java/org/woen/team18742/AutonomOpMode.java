package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
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
        _collector.Driver.DriveDirection(0,1,0);
        sleep(3000);
        _collector.Driver.DriveDirection(0,0,0);
        /////////////////////////
        while (opModeIsActive()) {
            _collector.Update();
        }
    }
}