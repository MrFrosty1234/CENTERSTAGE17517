package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;

@Autonomous
public class Autonom extends LinearOpMode {

    @Override
    public void runOpMode() {
        AutonomCollector _collector = new AutonomCollector(this);

        while(!isStarted()){
            _collector.PreUpdate();
        }

        resetRuntime();

        _collector.Start();

        while (opModeIsActive()) {
            _collector.Update();
        }

        _collector.Stop();
    }
}