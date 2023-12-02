package org.woen.team18742.Tools;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opengl.models.MeshObject;
import org.woen.team18742.Collectors.BaseCollector;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry){
        _telemetry = telemetry;
    }

    public static void AddLine(String str){
        _telemetry.addLine(str);
    }
}
