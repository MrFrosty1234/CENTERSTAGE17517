package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team18742.Tools.Configs.Configs;

@TeleOp(name = "Enable Competition Settings")
public class EnableCompetitions extends LinearOpMode {
    @Override
    public void runOpMode() {
        Configs.GeneralSettings.IsCameraDebug.NotSaveSet(false);
        Configs.GeneralSettings.IsAutonomEnable.NotSaveSet(true);
        Configs.GeneralSettings.IsCachinger.NotSaveSet(true);
        Configs.GeneralSettings.IsUseOdometrs.NotSaveSet(true);
        Configs.GeneralSettings.TelemetryOn.NotSaveSet(false);

        waitForStart();

        while (opModeIsActive());
    }
}
