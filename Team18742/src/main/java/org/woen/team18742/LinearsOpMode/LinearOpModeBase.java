package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.ToolTelemetry;

public class LinearOpModeBase extends LinearOpMode {

    protected BaseCollector GetCollector(){
        return null;
    }
    private void BiosUpdate(Bios bios){
        bios.Update();
        ToolTelemetry.Update();
    }

    protected boolean IsStarted = false;

    @Override
    public void runOpMode() {
        try {
            ToolTelemetry.SetTelemetry(telemetry);
            Bios bios = new Bios(gamepad1);
            BaseCollector _collector = GetCollector();

            if(IsStarted)
                OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();

            while (!isStarted())
                BiosUpdate(bios);

            resetRuntime();
            bios.Stop();
            _collector.Start();

            while (opModeIsActive()) {
                _collector.Update();
                ToolTelemetry.Update();
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
