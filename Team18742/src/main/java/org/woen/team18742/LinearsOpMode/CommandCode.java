package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team18742.Collectors.TeleOpCollector;

@TeleOp
public class CommandCode extends LinearOpMode {
    private TeleOpCollector _collector;

    @Override
    public void runOpMode() {
        _collector = new TeleOpCollector(this);

        waitForStart();
        resetRuntime();

        _collector.Start();

        while (opModeIsActive()) {
            _collector.Update();
            telemetry.update();
        }
    }
}