package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.StartRobotPosition;
import org.woen.team18742.Tools.ToolTelemetry;

public class LineraOpModeBase extends LinearOpMode {

    protected BaseCollector GetCollector(){
        return null;
    }

    protected double GetStartTime(){
        return 0;
    }

    @Override
    public void runOpMode() {
        try {
            ElapsedTime time = new ElapsedTime();

            time.reset();

            BaseCollector _collector = GetCollector();

            ToolTelemetry.Update();

            double startTime = GetStartTime();

            while (!isStarted());
            while (time.seconds() < startTime && isStarted());

            resetRuntime();

            _collector.Start();

            while (opModeIsActive()) {
                _collector.Update();
            }

            _collector.Stop();
        }
        catch (Exception e){
            ToolTelemetry.AddLine(e.getMessage());

            for(StackTraceElement i : e.getStackTrace())
                ToolTelemetry.AddLine(i.getClassName());

            ToolTelemetry.Update();

            throw e;
        }
    }
}
