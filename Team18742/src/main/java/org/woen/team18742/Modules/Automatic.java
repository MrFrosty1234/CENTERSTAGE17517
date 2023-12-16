package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.PID;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Automatic {
    private final AutonomCollector _collector;
    private final Odometry _odometry;
    public static double PidForwardP = 0.2, PidForwardI = 0, PidForwardD = 0;
    public static double PidSideP = 0.2, PidSideI = 0, PidSideD = 0;
    public static double PidRotateP = 0.02, PidRotateI = 0, PidRotateD = 0;

    public Automatic(AutonomCollector collector) {
        _collector = collector;
        _odometry = collector.Odometry;
    }

    private final PID _pidForward = new PID(PidForwardP, PidForwardI, PidForwardD, 1);
    private final PID _pidSide = new PID(PidSideP, PidSideI, PidSideD, 1);
    private final PID _pidTurn = new PID(PidRotateP, PidRotateI, PidRotateD, 1);

    public void PIDMove(double forward, double side) {
        _movedTargetX = _movedTargetX + forward;
        _movedTargetY = _movedTargetY + side;

        _pidForward.Reset();
        _pidSide.Reset();

        _pidForward.Update(forward);
        _pidSide.Update(side);
    }

    public void PIDMove(double forward, double side, double rotation){
        PIDMove(forward, side);
        TurnGyro(rotation);
    }

    public void TurnGyro(double degrees) {
        _pidTurn.Reset();

        _turnTarget = degrees;

        _pidTurn.Update(_turnTarget);
    }

    private double _movedTargetX = 101, _movedTargetY = 0, _turnTarget = 0;

    public boolean isMovedEnd() {
        return Math.abs(_pidForward.Err) < 2d && Math.abs(_pidSide.Err) < 2d && Math.abs(_pidTurn.Err) < 8d;
    }

    public void Update() {
        _pidForward.UpdateCoefs(PidForwardP, PidForwardI, PidForwardD);
        _pidSide.UpdateCoefs(PidSideP, PidSideI, PidSideD);
        _pidTurn.UpdateCoefs(PidRotateP, PidRotateI, PidRotateD);

        double UForward = _pidForward.Update(_movedTargetX - _odometry.X), uSide = _pidSide.Update(_movedTargetY - _odometry.Y), uTurn = _pidTurn.Update(_collector.Gyro.GetDegrees() - _turnTarget);

        _collector.Driver.SetSpeedWorldCoords(UForward,  uSide, uTurn);

        ToolTelemetry.AddLine( "Autonom:" + _pidForward.Err + " " + _pidSide.Err + " " + _pidTurn.Err);
        ToolTelemetry.AddVal("turn err", _pidTurn.Err);
    }
}