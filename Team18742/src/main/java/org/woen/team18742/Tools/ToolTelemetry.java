package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = telemetry;
    }

    private static TelemetryPacket _packet = new TelemetryPacket();

    public static void Update(){
        _telemetry.update();
        FtcDashboard ftcDashboard = FtcDashboard.getInstance();

        ftcDashboard.sendTelemetryPacket(_packet);

        _packet = new TelemetryPacket();
    }

    public static void DrawCircle(double x, double y, double radius, String color){
        _packet.fieldOverlay().fillCircle(x,y,radius);
    }

    public static void AddLine(String str) {
        _telemetry.addLine(str);
        _packet.addLine(str);
    }

    public static void AddVal(String name, Object val) {
        _telemetry.addData(name, val);
        _packet.put(name, val);
    }
}
