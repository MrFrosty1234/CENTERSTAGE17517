package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.woen.team18742.Tools.Configs;

@TeleOp
public class EnableCompetitions extends LinearOpMode {
    @Override
    public void runOpMode() {
        Configs.GeneralSettings.IsCameraDebug = false;
        Configs.GeneralSettings.IsAutonomEnable = true;
        Configs.GeneralSettings.IsCachinger = true;
        Configs.GeneralSettings.IsUseOdometrs = true;

        waitForStart();

        while (opModeIsActive());
    }
}
