package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.checkerframework.checker.units.qual.C;

public class CommandCode extends LinearOpMode {
    private Collector _collector;

    @Override
    public void runOpMode() {
        _collector = new Collector(this);

        waitForStart();
        resetRuntime();

        while (opModeIsActive()) {
            _collector.Update();
        }
    }
}
