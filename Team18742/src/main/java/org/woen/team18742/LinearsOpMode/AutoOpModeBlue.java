package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;

@Autonomous
public class AutoOpModeBlue extends LinearOpMode {
    @Override
    public void runOpMode() {
        try {
            ElapsedTime time = new ElapsedTime();

            time.reset();

            AutonomCollector _collector = new AutonomCollector(this);

            while (!isStarted()) {
                _collector.PreUpdate();
            }

            if(gamepad1.dpad_down)
                AutonomCollector.StartPosition = StartRobotPosition.RED_BACK;
            else
                AutonomCollector.StartPosition = StartRobotPosition.RED_FORWARD;

            while (time.seconds() < 7.5 && isStarted());

            resetRuntime();

            _collector.Start();

            while (opModeIsActive()) {
                _collector.Update();
            }

            _collector.Stop();
        }
        catch (Exception e){
            ToolTelemetry.AddLine(e.getMessage());
            ToolTelemetry.Update();

            throw e;
        }
    }
}
