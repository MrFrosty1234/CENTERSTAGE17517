package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;

@Autonomous
public class AutoOpModeBlue extends LineraOpModeBase {
    @Override
    protected BaseCollector GetCollector() {
        AutonomCollector.StartPosition = StartRobotPosition.BLUE_BACK;

        return new BaseCollector(this);
    }

    @Override
    protected double GetStartTime() {
        return 5;
    }
}
