package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.ToolTelemetry;

public class LinearOpModeBase extends LinearOpMode {

    protected BaseCollector GetCollector(){
        return null;
    }

    protected double GetStartTime(){
        return 0;
    }

    private void BiosUpdate(Bios bios){
        bios.Update();
        ToolTelemetry.Update();
    }

    @Override
    public void runOpMode() {
        try {
            Bios bios = new Bios(gamepad1);

            ElapsedTime time = new ElapsedTime();

            time.reset();

            BaseCollector _collector = GetCollector();

            ToolTelemetry.Update();

            double startTime = GetStartTime();

            while (!isStarted())
                BiosUpdate(bios);

            while (time.seconds() < startTime && isStarted())
                BiosUpdate(bios);

            resetRuntime();

            _collector.Start();

            while (opModeIsActive()) {
                _collector.Update();
            }

            _collector.Stop();
            bios.Stop();
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
