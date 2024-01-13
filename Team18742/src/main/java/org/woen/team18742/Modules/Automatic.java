package org.woen.team18742.Modules;

import static java.lang.Math.PI;
import static java.lang.Math.signum;

import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.Odometry;
import org.woen.team18742.Tools.Battery;
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

    public void SetSpeed(double speed){
        _PIDFTurn.SrtLimitU(speed);
        _PIDFForward.SrtLimitU(speed);
        _PIDFSide.SrtLimitU(speed);
    }

    private final PIDF _PIDFForward = new PIDF(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD, Configs.DriveTrainWheels.speed, 1);
    private final PIDF _PIDFSide = new PIDF(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideI, Configs.AutomaticSidePid.PidSideD, Configs.DriveTrainWheels.speed, 1);
    private final PIDF _PIDFTurn = new PIDF(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD, Configs.DriveTrainWheels.speed, 1);

    public void PIDMove(Vector2 moved) {
        PIDMoveToPoint(Vector2.Plus(moved, _targetPosition));
    }

    public void PIDMove(Vector2 moved, double rotation){
        PIDMoveToPoint(Vector2.Plus(moved, _targetPosition));
        TurnGyro(rotation);
    }

    public void PIDMoveToPoint(Vector2 moved, double rotate){
        PIDMoveToPoint(moved);
        TurnGyro(rotate);
    }

    public void PIDMoveToPoint(Vector2 moved){
        _targetPosition = moved;

        _PIDFForward.Reset();
        _PIDFSide.Reset();

        _PIDFForward.Update(_targetPosition.X - _odometry.Position.X);
        _PIDFSide.Update(_targetPosition.Y - _odometry.Position.Y);
    }

    public void TurnGyro(double degrees) {
        _PIDFTurn.Reset();

        _turnTarget = degrees;

        _PIDFTurn.Update(Gyroscope.ChopAngle(_gyro.GetRadians() - _turnTarget));
    }

    private double _turnTarget = 0;
    private Vector2 _targetPosition = new Vector2();

    public boolean isMovedEnd() {
        return Math.abs(_PIDFForward.Err) < 10d && Math.abs(_PIDFSide.Err) < 10d && Math.abs(_PIDFTurn.Err) < PI / 10;
    }

    @Override
    public void Update() {
        _PIDFForward.UpdateCoefs(Configs.AutomaticForwardPid.PidForwardP, Configs.AutomaticForwardPid.PidForwardI, Configs.AutomaticForwardPid.PidForwardD);
        _PIDFSide.UpdateCoefs(Configs.AutomaticSidePid.PidSideP, Configs.AutomaticSidePid.PidSideI, Configs.AutomaticSidePid.PidSideD);
        _PIDFTurn.UpdateCoefs(Configs.AutomaticRotatePid.PidRotateP, Configs.AutomaticRotatePid.PidRotateI, Configs.AutomaticRotatePid.PidRotateD);

        if(Configs.GeneralSettings.IsAutonomEnable) {
            _driverTrain.SetSpeedWorldCoords(
                    new Vector2(_PIDFForward.Update(_targetPosition.X - _odometry.Position.X) / Battery.ChargeDelta, _PIDFSide.Update(_targetPosition.Y - _odometry.Position.Y) / Battery.ChargeDelta),
                    _PIDFTurn.Update(Gyroscope.ChopAngle(_gyro.GetRadians() - _turnTarget)) / Battery.ChargeDelta);
        }

        ToolTelemetry.AddLine( "Autonom:" + _PIDFForward.Err + " " + _PIDFSide.Err + " " + _PIDFTurn.Err);
    }

    @Override
    public void Start(){
        _PIDFSide.Start();
        _PIDFForward.Start();
        _PIDFTurn.Start();
    }
}