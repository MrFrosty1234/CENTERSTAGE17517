package org.woen.team18742.Tools;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.woen.team18742.Tools.Configs.Configs;

public class ToolTelemetry {
    private static Telemetry _telemetry;

    public static void SetTelemetry(Telemetry telemetry) {
        _telemetry = telemetry;
    }

    private static TelemetryPacket _packet = new TelemetryPacket();

    public static void Update() {
        if (!Configs.GeneralSettings.TelemetryOn)
            return;

        _telemetry.update();
        FtcDashboard ftcDashboard = FtcDashboard.getInstance();

        ftcDashboard.sendTelemetryPacket(_packet);

        _packet = new TelemetryPacket();
    }

    public static Canvas GetCanvas() {
        return _packet.fieldOverlay();
    }

    public static void DrawCircle(Vector2 pos, double radius, Color color) {
        DrawCircle(pos, radius, color.toString());
    }

    public static void DrawCircle(Vector2 pos, double radius, String color) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(0);
            _packet.fieldOverlay().fillCircle(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), DistanceUnit.INCH.fromCm(radius));
        }
    }

    public static void DrawPolygon(Vector2[] points, Color color){
        DrawPolygon(points, color.toString());
    }

    public static void DrawPolygon(Vector2[] points, String color){
        if(Configs.GeneralSettings.TelemetryOn){
            double[] x = new double[points.length], y = new double[points.length];

            for(int i = 0; i < points.length; i++) {
                x[i] = DistanceUnit.INCH.fromCm(points[i].X);
                y[i] = DistanceUnit.INCH.fromCm(points[i].Y);
            }

            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(0);
            _packet.fieldOverlay().fillPolygon(x, y);
        }
    }

    public static void DrawRect(Vector2 pos, Vector2 size, double rotate, Color color) {
        DrawRect(pos, size, rotate, color.toString());
    }

    public static void DrawRect(Vector2 pos, Vector2 size, double rotate, String color) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _packet.fieldOverlay().setFill(color);
            _packet.fieldOverlay().setRotation(rotate);
            _packet.fieldOverlay().fillRect(DistanceUnit.INCH.fromCm(pos.X), DistanceUnit.INCH.fromCm(pos.Y), DistanceUnit.INCH.fromCm(size.X), DistanceUnit.INCH.fromCm(size.Y));
        }
    }

    public static void AddLine(String str) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _telemetry.addLine(str);
            _packet.addLine(str);
        }
    }

    public static void AddLines(String... strs) {
        if (Configs.GeneralSettings.TelemetryOn) {
            for (String i : strs)
                AddLine(i + " ");
        }
    }

    public static void AddVal(String name, Object val) {
        if (Configs.GeneralSettings.TelemetryOn) {
            _telemetry.addData(name, val);
            _packet.put(name, val);
        }
    }
}