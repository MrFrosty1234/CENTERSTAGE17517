package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;

@Autonomous(preselectTeleOp = "TeleOp")
public class AutoOpMode extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        return new AutonomCollector(this);
    }
}
