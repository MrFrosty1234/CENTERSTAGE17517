package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Autonomous
public class AutoOpModeRed extends LinearOpMode {
    @Override
    public void runOpMode() {
        try {
            ElapsedTime time = new ElapsedTime();
            
            time.reset();

            AutonomCollector.StartPosition = StartRobotPosition.BLUE_BACK;

            AutonomCollector _collector = new AutonomCollector(this);

            if(gamepad1.dpad_down)
                AutonomCollector.StartPosition = StartRobotPosition.BLUE_BACK;
            else
                AutonomCollector.StartPosition = StartRobotPosition.BLUE_FORWAD;

            while (!isStarted()) {
                _collector.PreUpdate();
            }

            while (time.seconds() < 7.5 && isStarted());

            resetRuntime();

            _collector.Start();

            /*Drivetrain drivetrain = _collector.GetModule(Drivetrain.class);

            drivetrain.Stop();
            drivetrain.SimpleDriveDirection(new Vector2(0.5,0), 0);
            sleep(1700);
            drivetrain.SimpleDriveDirection(new Vector2(-0.2,0),0);
            sleep(1000);
            drivetrain.Stop();*/

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