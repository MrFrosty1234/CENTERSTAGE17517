package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.woen.team18742.Collectors.BaseCollector;

@Autonomous
public class AutoOpMode extends LinearOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        return new BaseCollector(this);
    }

    @Override
    protected double GetStartTime() {
        return 5;
    }
}
