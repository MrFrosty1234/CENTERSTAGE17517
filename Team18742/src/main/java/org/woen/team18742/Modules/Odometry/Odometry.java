package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Drivetrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.OdometryHandler;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;
import org.woen.team18742.Tools.Vector2;

@Module
public class Odometry implements IRobotModule {
    private double _oldRotate = 0, _oldOdometrXLeft, _oldOdometrXRight, _oldOdometrY;

    public Vector2 Position = new Vector2();
    private Drivetrain _driverTrain;
    private Gyroscope _gyro;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;
    private CVOdometry _CVOdometry;
    private OdometryHandler _odometrs;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);

    private AutonomCollector _collector;

    private ElapsedTime _deltaTime = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _CVOdometry = new CVOdometry(collector);

        _driverTrain = collector.GetModule(Drivetrain.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _odometrs = collector.GetModule(OdometryHandler.class);

        if(collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    public VisionProcessor GetProcessor(){
        return _CVOdometry.GetProcessor();
    }

    @Override
    public void Update() {
        _filterX.UpdateCoef(Configs.Odometry.XCoef);
        _filterY.UpdateCoef(Configs.Odometry.YCoef);

        double deltaX, deltaY;

        if(Configs.GeneralSettings.IsUseOdometers){
            double deltaRotate = _gyro.GetRadians() - _oldRotate;

            double odometrXLeft = _odometrs.GetOdometerXLeft(), odometrY = _odometrs.GetOdometerY(), odometrXRight = _odometrs.GetOdometerXRight();

            deltaX = (odometrXLeft - _oldOdometrXLeft + odometrXRight - _oldOdometrXRight) / 2;
            deltaY = (odometrY - _oldOdometrY) - Configs.Odometry.RadiusOdometrY * deltaRotate;

            _oldOdometrXLeft = odometrXLeft;
            _oldOdometrXRight = odometrXRight;
            _oldOdometrY = odometrY;

            _oldRotate = _gyro.GetRadians();
        }
        else {
            double lfd = _driverTrain.GetLeftForwardEncoder();
            double lbd = _driverTrain.GetLeftBackEncoder();
            double rfd = _driverTrain.GetRightForwardEncoder();
            double rbd = _driverTrain.GetRightBackEncoder();

            double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

            deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
            deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

            deltaY = deltaY * 0.8;

            _leftForwardDrive = lfd;
            _leftBackDrive = lbd;
            _rightBackDrive = rbd;
            _rightForwardDrive = rfd;
        }

        Vector2 shift = new Vector2(deltaX * cos(_gyro.GetRadians()) + deltaY * sin(_gyro.GetRadians()), -deltaX * sin(_gyro.GetRadians()) + deltaY * cos(_gyro.GetRadians()));

        Position = Vector2.Plus(shift, Position);

        Speed.X = shift.X / _deltaTime.seconds();
        Speed.Y = shift.Y / _deltaTime.seconds();

        _CVOdometry.Update();

        if(!_CVOdometry.IsZero) {
            Position.X = _filterX.Update(Position.X, _CVOdometry.Position.X);
            Position.Y = _filterY.Update(Position.Y, _CVOdometry.Position.Y);
        }

        ToolTelemetry.DrawCircle(Position, 10, "#FFFFFF");
        ToolTelemetry.AddLine("OdometryX :" + Position);

        _deltaTime.reset();
    }

    public Vector2 Speed = new Vector2();

    @Override
    public void Start(){
        _filterX.Reset();
        _filterY.Reset();

        _deltaTime.reset();

        if(_collector != null)
            Position = _collector.StartPosition.Position.copy();
    }
}