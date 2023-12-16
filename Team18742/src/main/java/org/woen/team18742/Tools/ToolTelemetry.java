package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ToolTelemetry {
    private static MultipleTelemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public static void Update(){
        _telemetry.update();
    }

    public static void AddLine(String str) {
        _telemetry.addLine(str);
    }

    public static void AddVal(String name, Object val) {
        _telemetry.addData(name, val);
    }
}
