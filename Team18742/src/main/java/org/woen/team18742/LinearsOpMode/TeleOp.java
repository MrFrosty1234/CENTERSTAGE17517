package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Collectors.TeleOpCollector;
import org.woen.team18742.Tools.ToolTelemetry;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        try {
            BaseCollector collector = new TeleOpCollector(this);

            ToolTelemetry.Update();

            OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();
            waitForStart();
            resetRuntime();

            collector.Start();

            while (opModeIsActive()) {
                collector.Update();
            }
        }
        catch (Exception e){
            ToolTelemetry.AddLine(e.getMessage());
            ToolTelemetry.Update();

            throw e;
        }
    }
}