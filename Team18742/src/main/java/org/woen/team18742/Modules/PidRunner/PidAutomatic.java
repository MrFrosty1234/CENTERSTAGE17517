package org.woen.team18742.Modules.PidRunner;

import static java.lang.Math.PI;
import static java.lang.Math.signum;

import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.DriveTrain.Drivetrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.AutonomModule;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Battery;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.PIDF;
import org.woen.team18742.Tools.Vector2;

@AutonomModule
public class PidAutomatic implements IRobotModule {
    private OdometryHandler _odometry;
    private Gyroscope _gyro;
    private Drivetrain _driverTrain;
    @Override
    public void Init(BaseCollector collector) {
        _odometry = collector.GetModule(OdometryHandler.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _driverTrain = collector.GetModule(Drivetrain.class);
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

        if(Configs.GeneralSettings.IsAutonomEnable && _isEnable) {
            _driverTrain.SetSpeedWorldCoords(
                    new Vector2(_PIDFForward.Update(_targetPosition.X - _odometry.Position.X) / Battery.ChargeDelta, _PIDFSide.Update(_targetPosition.Y - _odometry.Position.Y) / Battery.ChargeDelta),
                    _PIDFTurn.Update(Gyroscope.ChopAngle(_gyro.GetRadians() - _turnTarget)) / Battery.ChargeDelta);
        }
    }

    @Override
    public void Start(){
        _PIDFSide.Start();
        _PIDFForward.Start();
        _PIDFTurn.Start();
    }

    private boolean _isEnable = false;

    public void Disable(){
        _isEnable = false;
    }

    public void Enable(){
        _isEnable = true;
    }
}