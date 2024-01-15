package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.ftccommon.external.OnCreate;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.ToolTelemetry;

@TeleOp(name = "Enable Competition Settings")
public class EnableCompetitions extends LinearOpMode {
    @Override
    public void runOpMode() {
        Configs.GeneralSettings.IsCameraDebug = false;
        Configs.GeneralSettings.IsAutonomEnable = true;
        Configs.GeneralSettings.IsCachinger = true;
        Configs.GeneralSettings.IsUseOdometers = true;
        Configs.GeneralSettings.TelemetryOn = false;
        LiftPose.AVERAGE.Pose = 190;

        waitForStart();

        while (opModeIsActive());
    }
}
