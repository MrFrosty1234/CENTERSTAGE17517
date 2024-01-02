package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.woen.team18742.Tools.Configs.Configs;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = telemetry;
    }

    private static TelemetryPacket _packet = new TelemetryPacket();

    public static void Update(){
        if(!Configs.GeneralSettings.TelemetryOn.Get())
            return;

        _telemetry.update();
        FtcDashboard ftcDashboard = FtcDashboard.getInstance();

        ftcDashboard.sendTelemetryPacket(_packet);

        _packet = new TelemetryPacket();
    }

    public static void DrawCircle(Vector2 pos, double radius, String color){
        if(Configs.GeneralSettings.TelemetryOn.Get())
            _packet.fieldOverlay().fillCircle(pos.X,pos.Y,radius);
    }

    public static void AddLine(String str) {
        if(Configs.GeneralSettings.TelemetryOn.Get()) {
            _telemetry.addLine(str);
            _packet.addLine(str);
        }
    }

    public static void AddVal(String name, Object val) {
        if(Configs.GeneralSettings.TelemetryOn.Get()) {
            _telemetry.addData(name, val);
            _packet.put(name, val);
        }
    }
}