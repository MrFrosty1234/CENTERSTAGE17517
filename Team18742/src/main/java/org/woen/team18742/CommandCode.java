package org.woen.team18742;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.checkerframework.checker.units.qual.C;

@TeleOp
public class CommandCode extends LinearOpMode {
    private Collector _collector;

    @Override
    public void runOpMode() {
        _collector = new Collector(this);

        waitForStart();
        resetRuntime();

        _collector.Start();

        while (opModeIsActive()) {
            _collector.Update();

        }
    }
}
