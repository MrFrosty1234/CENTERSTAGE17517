package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Collectors.TeleOpCollector;
import org.woen.team18742.Tools.ToolTelemetry;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LineraOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        return new TeleOpCollector(this);
    }
}