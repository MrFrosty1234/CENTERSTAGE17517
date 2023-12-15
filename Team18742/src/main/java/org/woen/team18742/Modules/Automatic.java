package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.PID;

public class Automatic {
    private AutonomCollector _collector;
    private Odometry _odometry;

    public Automatic(AutonomCollector collector) {
        _collector = collector;
        _odometry = collector.Odometry;
    }

    private PID _pidForward = new PID(0.8, 1, 1, 1), _pidSide = new PID(0.8, 1, 1, 1);

    public void PIDMove(double forward, double side) {
        _movedTargetX = _odometry.X + forward;
        _movedTargetY = _odometry.Y + side;

        _pidForward.Reset();
        _pidSide.Reset();
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

    public void SetSpeedWorldCoords(double speedForward, double speedSide) {
        speedForward = -speedForward;

        double x = cos(-_collector.Gyro.GetRadians()) * speedForward - sin(-_collector.Gyro.GetRadians()) * speedSide,
                y = sin(-_collector.Gyro.GetRadians()) * speedForward + cos(-_collector.Gyro.GetRadians()) * speedSide;

        _collector.Driver.DriveDirection(x, y, 0);
    }

    private double GetDistance(double x, double y) {
        double difX = _odometry.X - x, difY = _odometry.Y - y;

        return Math.sqrt(difX * difX + difY * difY);
    }

    private double _movedTargetX = 0, _movedTargetY = 0;

    public boolean isMovedEnd() {
        return GetDistance(_movedTargetX, _movedTargetY) > 2;
    }

    public void Update() {
        if (isMovedEnd())
            _collector.Driver.Stop();
        else
            SetSpeedWorldCoords(_pidForward.Update(_odometry.X - _movedTargetX), _pidSide.Update(_odometry.Y - _movedTargetY));
    }
}