package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ToolTelemetry {
    private static Telemetry _telemetry, _dashTelemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = telemetry;
        _dashTelemetry = FtcDashboard.getInstance().getTelemetry();
    }

    public static void Update(){
        _dashTelemetry.update();
    }

    public static void AddLine(String str) {
        _telemetry.addLine(str);
    }

    public static void AddValDash(String name, Object val) {
        _dashTelemetry.addData(name, val);
    }
}
