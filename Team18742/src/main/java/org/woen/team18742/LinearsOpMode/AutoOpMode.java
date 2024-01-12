package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Tools.ToolTelemetry;

@Autonomous
public class AutoOpMode extends LinearOpMode {
    private ElapsedTime _time = new ElapsedTime();

    @Override
    public void runOpMode() {
        try {
            _time.reset();

            AutonomCollector _collector = new AutonomCollector(this);

            while (!isStarted()) {
                _collector.PreUpdate();
            }

            while (_time.seconds() < 7 && isStarted());

            resetRuntime();

        /*_collector.Driver.Stop();
        _collector.Driver.DriveDirection(new Vector2(0.5,0), 0);
        sleep(1700);
        _collector.Driver.DriveDirection(new Vector2(-0.2,0),0);
        sleep(1000);
        _collector.Driver.Stop();*/

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