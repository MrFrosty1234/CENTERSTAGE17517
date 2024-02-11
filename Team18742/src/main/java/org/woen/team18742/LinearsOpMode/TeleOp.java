package org.woen.team18742.LinearsOpMode;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Collectors.TeleOpCollector;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        IsStarted = true;
        return new TeleOpCollector(this);
    }
}