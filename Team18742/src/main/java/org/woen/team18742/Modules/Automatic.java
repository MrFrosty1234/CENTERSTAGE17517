package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.PID;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@AutonomModule
public class Automatic implements IRobotModule {
    private Odometry _odometry;
    private Gyroscope _gyro;
    private DriverTrain _driverTrain;
    private AutonomCollector _collector;

    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(Odometry.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driverTrain = collector.GetModule(DriverTrain.class);

        if(collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    private final PID _pidForward = new PID(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD, 1);
    private final PID _pidSide = new PID(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideI, Configs.AutomaticSidePid.PidSideD, 1);
    private final PID _pidTurn = new PID(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD, 1);

    public void PIDMove(Vector2 moved) {
        _targetPosition = Vector2.Plus(_targetPosition, moved);

        _pidForward.Reset();
        _pidSide.Reset();

        _pidForward.Update(moved.X);
        _pidSide.Update(moved.Y);
    }

    public void PIDMove(Vector2 moved, double rotation){
        PIDMove(moved);
        TurnGyro(rotation);
    }

    public void TurnGyro(double degrees) {
        _pidTurn.Reset();

        _turnTarget = degrees;

        _pidTurn.Update(_turnTarget);
    }

    private double _turnTarget = 0;
    private Vector2 _targetPosition = new Vector2();

    public boolean isMovedEnd() {
        return Math.abs(_pidForward.Err) < 2d && Math.abs(_pidSide.Err) < 2d && Math.abs(_pidTurn.Err) < 8d;
    }

    @Override
    public void Update() {
        _pidForward.UpdateCoefs(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD);
        _pidSide.UpdateCoefs(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideD);
        _pidTurn.UpdateCoefs(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD);

        if(Configs.GeneralSettings.IsAutonomEnable) {
            _driverTrain.SetSpeedWorldCoords(
                    new Vector2(_pidForward.Update(_targetPosition.X - _odometry.Position.X), _pidSide.Update(_targetPosition.Y - _odometry.Position.Y)),
                    _pidTurn.Update((_gyro.GetRadians() - _turnTarget)));
        }

        ToolTelemetry.AddLine( "Autonom:" + _pidForward.Err + " " + _pidSide.Err + " " + _pidTurn.Err);
        ToolTelemetry.AddVal("turn err", _pidTurn.Err);
    }

    @Override
    public void Stop() {}

    @Override
    public void Start(){
        _pidSide.Start();
        _pidForward.Start();
        _pidTurn.Start();

        if(_collector != null)
            _targetPosition = _collector.StartPosition.Position.copy();
    }
}