package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.PID;
import org.woen.team18742.Tools.ToolTelemetry;

public class Automatic {
    private AutonomCollector _collector;
    private Odometry _odometry;
    public static double PidForwardP = 0.2, PidForwardI = 0, PidForwardD = 0;
    public static double PidSideP = 0.2, PidSideI = 0, PidSideD = 0;
    public static double PidRotateP = 0.2, PidRotateI = 0, PidRotateD = 0;

    public Automatic(AutonomCollector collector) {
        _collector = collector;
        _odometry = collector.Odometry;
    }

    private PID _pidForward = new PID(PidForwardP, PidForwardI, PidForwardD, 1), _pidSide = new PID(PidSideP, PidSideI, PidSideD, 1), _pidTurn = new PID(PidRotateP, PidRotateI, PidRotateD, 1);

    public void PIDMove(double forward, double side) {
        _movedTargetX = _movedTargetX + forward;
        _movedTargetY = _movedTargetY + side;

        _pidForward.Reset();
        _pidSide.Reset();

        _pidForward.Update(forward);
        _pidSide.Update(side);
    }

    private void turnGyro(double degrees) {
        _pidTurn.Reset();

        _turnTarget = degrees;

        _pidTurn.Update(_turnTarget);
    }

    public void SetSpeedWorldCoords(double speedForward, double speedSide, double rotate) {
        double x = cos(_collector.Gyro.GetRadians()) * speedForward - sin(_collector.Gyro.GetRadians()) * speedSide,
                y = sin(_collector.Gyro.GetRadians()) * speedForward + cos(_collector.Gyro.GetRadians()) * speedSide;

        _collector.Driver.DriveDirection(x, y, rotate);
    }

    private double _movedTargetX = 0, _movedTargetY = 0, _turnTarget = 0;

    public boolean isMovedEnd() {
        return Math.abs(_pidForward.Err) < 2d && Math.abs(_pidSide.Err) < 2d && Math.abs(_pidTurn.Err) < 2d;
    }

    public void Update() {
        double UForward = _pidForward.Update(_movedTargetX - _odometry.X), uSide = _pidSide.Update(_movedTargetY - _odometry.Y), uTurn = _pidTurn.Update(_turnTarget - _collector.Gyro.GetDegrees());

        if (isMovedEnd())
            _collector.Driver.Stop();
        else
            SetSpeedWorldCoords(UForward,  uSide, uTurn);

        ToolTelemetry.AddLine( "Autonom:" + _pidForward.Err + " " + _pidSide.Err);
    }
}