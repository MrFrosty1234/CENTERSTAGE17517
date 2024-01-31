package org.woen.team18742.LinearsOpMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Modules.Lift.LiftPose;
import org.woen.team18742.Tools.Configs.Configs;

@TeleOp(name = "Enable Competition Settings")
public class EnableCompetitions extends LinearOpMode {
    @Override
    public void runOpMode() {

        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).startActiveOpMode();
        waitForStart();
        if (opModeIsActive()) {
            Configs.GeneralSettings.IsCameraDebug = false;
            Configs.GeneralSettings.IsAutonomEnable = true;
            Configs.GeneralSettings.IsCachinger = true;
            Configs.GeneralSettings.IsUseOdometers = true;
            Configs.GeneralSettings.TelemetryOn = false;
        }
    }
}
