package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous
public class AutonomOpMode extends LinearOpMode {
    private BaseCollector _collector;

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