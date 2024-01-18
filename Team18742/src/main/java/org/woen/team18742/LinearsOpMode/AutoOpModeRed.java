package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Autonomous
public class AutoOpModeRed extends LineraOpModeBase{
    @Override
    protected BaseCollector GetCollector() {
        AutonomCollector.StartPosition = StartRobotPosition.RED_BACK;

        return new BaseCollector(this);
    }

    @Override
    protected double GetStartTime() {
        return 5;
    }
}