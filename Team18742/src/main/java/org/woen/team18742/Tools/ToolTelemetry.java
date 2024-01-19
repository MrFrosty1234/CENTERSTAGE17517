package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.woen.team18742.Tools.Configs.Configs;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = telemetry;
    }

    private static TelemetryPacket _packet = new TelemetryPacket();

    public static void Update(){
        if(!Configs.GeneralSettings.TelemetryOn || _telemetry == null)
            return;

        _telemetry.update();
        FtcDashboard ftcDashboard = FtcDashboard.getInstance();

        ftcDashboard.sendTelemetryPacket(_packet);

        _packet = new TelemetryPacket();
    }

    public static void DrawCircle(Vector2 pos, double radius, String color){
        if(Configs.GeneralSettings.TelemetryOn || _telemetry != null) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(0);
            _packet.fieldOverlay().fillCircle(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), radius);
        }
    }

    public static void DrawRect(Vector2 pos, Vector2 size, double rotate, String color){
        if(Configs.GeneralSettings.TelemetryOn || _telemetry != null) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(rotate);
            _packet.fieldOverlay().fillRect(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), size.X, size.Y);
        }
    }

    public static void AddLine(String str) {
        if(Configs.GeneralSettings.TelemetryOn || _telemetry != null) {
            _telemetry.addLine(str);
            _packet.addLine(str);
        }
    }

    public static void AddVal(String name, Object val) {
        if(Configs.GeneralSettings.TelemetryOn ||_telemetry != null) {
            _telemetry.addData(name, val);
            _packet.put(name, val);
        }
    }
}