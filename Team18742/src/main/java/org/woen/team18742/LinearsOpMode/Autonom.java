package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Vector2;

@Autonomous
public class Autonom extends LinearOpMode {

    @Override
    public void runOpMode() {
        AutonomCollector _collector = new AutonomCollector(this);

        while(!isStarted()){
            _collector.PreUpdate();
        }

        resetRuntime();

        _collector.Driver.Stop();
        _collector.Driver.DriveDirection(new Vector2(0.5,0), 0);
        sleep(1700);
        _collector.Driver.DriveDirection(new Vector2(-0.2,0),0);
        sleep(1000);
        _collector.Driver.Stop();

        _collector.Start();

        while (opModeIsActive()) {
            //_collector.Update();
        }

        _collector.Stop();
    }
}