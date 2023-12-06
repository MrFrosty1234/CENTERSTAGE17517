package org.woen.team18742.Tools;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry){
        _telemetry = telemetry;
    }

    public static void AddLine(String str){
        _telemetry.addLine(str);
    }
}
