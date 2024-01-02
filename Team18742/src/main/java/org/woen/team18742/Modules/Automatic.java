package org.woen.team18742.Modules;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@AutonomModule
public class Automatic implements IRobotModule {
    private Odometry _odometry;
    private Gyroscope _gyro;
    private Drivetrain _driverTrain;
    private AutonomCollector _collector;

    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(Odometry.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driverTrain = collector.GetModule(Drivetrain.class);

        if(collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    private final PIDF _PIDFForward = new PIDF(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD, 1);
    private final PIDF _PIDFSide = new PIDF(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideI, Configs.AutomaticSidePid.PidSideD, 1);
    private final PIDF _PIDFTurn = new PIDF(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD, 1);

    public void PIDMove(Vector2 moved) {
        _targetPosition = Vector2.Plus(_targetPosition, moved);

        _PIDFForward.Reset();
        _PIDFSide.Reset();

        _PIDFForward.Update(moved.X);
        _PIDFSide.Update(moved.Y);
    }

    public void PIDMove(Vector2 moved, double rotation){
        PIDMove(moved);
        TurnGyro(rotation);
    }

    public void TurnGyro(double degrees) {
        _PIDFTurn.Reset();

        _turnTarget = degrees;

        _PIDFTurn.Update(_turnTarget);
    }

    private double _turnTarget = 0;
    private Vector2 _targetPosition = new Vector2();

    public boolean isMovedEnd() {
        return Math.abs(_PIDFForward.Err) < 2d && Math.abs(_PIDFSide.Err) < 2d && Math.abs(_PIDFTurn.Err) < 8d;
    }

    @Override
    public void Update() {
        _PIDFForward.UpdateCoefs(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD);
        _PIDFSide.UpdateCoefs(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideD);
        _PIDFTurn.UpdateCoefs(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD);

        if(Configs.GeneralSettings.IsAutonomEnable.Get()) {
            _driverTrain.SetSpeedWorldCoords(
                    new Vector2(_PIDFForward.Update(_targetPosition.X - _odometry.Position.X), _PIDFSide.Update(_targetPosition.Y - _odometry.Position.Y)),
                    _PIDFTurn.Update((_gyro.GetRadians() - _turnTarget)));
        }

        ToolTelemetry.AddLine( "Autonom:" + _PIDFForward.Err + " " + _PIDFSide.Err + " " + _PIDFTurn.Err);
        ToolTelemetry.AddVal("turn err", _PIDFTurn.Err);
    }

    @Override
    public void Stop() {}

    @Override
    public void Start(){
        _PIDFSide.Start();
        _PIDFForward.Start();
        _PIDFTurn.Start();

        if(_collector != null)
            _targetPosition = _collector.StartPosition.Position.copy();
    }
}