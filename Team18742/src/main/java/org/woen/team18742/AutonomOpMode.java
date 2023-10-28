package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class AutonomOpMode extends LinearOpMode {
    private AutonomCollector _collector;

    @Override
    public void runOpMode() {
        _collector = new AutonomCollector(this);

        waitForStart();
        resetRuntime();

        _collector.Start();

        while (opModeIsActive()) {
            _collector.Update();
        }
    }
}
