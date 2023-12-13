package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Odometry.Odometry;
import org.woen.team18742.Tools.PID;
import org.woen.team18742.Tools.ToolTelemetry;

public class Automatic {
    private AutonomCollector _collector;
    private Odometry _odometry;

    public Automatic(AutonomCollector collector) {
        _collector = collector;
        _odometry = collector.Odometry;
    }

    private PID _pidForward = new PID(0.2, 0, 0, 1), _pidSide = new PID(0.2, 0, 0, 1);

    public void PIDMove(double forward, double side) {
        _movedTargetX = _movedTargetX + forward;
        _movedTargetY = _movedTargetY + side;

        _pidForward.Reset();
        _pidSide.Reset();

        _pidForward.Update(forward);
        _pidSide.Update(side);
    }

    double fixangle(double degrees) {
        while (degrees > 180)
            degrees = degrees - 360;
        while (degrees < -180)
            degrees = degrees + 360;
        return degrees;
    }

    private void turnGyro(double degrees) {
        PID pidTurn = new PID(1, 1, 1, 1);
        do {
            double angleError = _collector.Gyro.GetDegrees() - degrees;
            angleError = fixangle(angleError);
            _collector.Driver.DriveDirection(0, 0, pidTurn.Update(angleError));

            _collector.Gyro.Update();
            _odometry.Update();
        } while (_collector.CommandCode.opModeIsActive() && abs(pidTurn.Err) > 2);
        _collector.Driver.Stop();
    }

    public void SetSpeedWorldCoords(double speedForward, double speedSide, double rotate) {
        double x = cos(_collector.Gyro.GetRadians()) * speedForward - sin(_collector.Gyro.GetRadians()) * speedSide,
                y = sin(_collector.Gyro.GetRadians()) * speedForward + cos(_collector.Gyro.GetRadians()) * speedSide;

        _collector.Driver.DriveDirection(x, y, rotate);
    }

    private double GetDistance(double x, double y) {
        double difX = _odometry.X - x, difY = _odometry.Y - y;

        return Math.sqrt(difX * difX + difY * difY);
    }

    private double _movedTargetX = 0, _movedTargetY = 0;

    public boolean isMovedEnd() {
        return Math.abs(_pidForward.Err) < 2d && Math.abs(_pidSide.Err) < 2d;
    }

    public void Update() {
        double UForward = _pidForward.Update(_movedTargetX - _odometry.X), uSide = _pidSide.Update(_movedTargetY - _odometry.Y);

        if (isMovedEnd())
            _collector.Driver.Stop();
        else
            SetSpeedWorldCoords(UForward,  uSide, 0);

        ToolTelemetry.AddLine( "Autonom:" + _pidForward.Err + " " + _pidSide.Err);
    }
}