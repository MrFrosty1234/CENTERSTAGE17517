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

    public Vector2 Position = new Vector2(), EncoderPosition = new Vector2();
    private Drivetrain _driverTrain;
    private Gyroscope _gyro;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;
    //private CVOdometry _CVOdometry = new CVOdometry();
    private OdometryHandler _odometrs;

    private final ExponentialFilter _filterX = new ExponentialFilter(Configs.Odometry.XCoef), _filterY = new ExponentialFilter(Configs.Odometry.YCoef);

    private AutonomCollector _collector;

    private ElapsedTime _deltaTime = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        //_CVOdometry.Init(collector);

        _driverTrain = collector.GetModule(Drivetrain.class);
        _gyro = collector.GetModule(Gyroscope.class);
        _odometrs = collector.GetModule(OdometryHandler.class);

        if(collector instanceof AutonomCollector)
            _collector = (AutonomCollector) collector;
    }

    public VisionProcessor GetProcessor(){
        return null;//_CVOdometry.GetProcessor();
    }

    @Override
    public void Update() {
        _filterX.UpdateCoef(Configs.Odometry.XCoef);
        _filterY.UpdateCoef(Configs.Odometry.YCoef);

        Vector2 shift = null;

        if(Configs.GeneralSettings.IsUseOdometers){
            double odometrXLeft = _odometrs.GetOdometerXLeft(), odometrY = _odometrs.GetOdometerY(), odometrXRight = _odometrs.GetOdometerXRight();

            double deltaX = -(odometrXLeft - _oldOdometrXLeft + odometrXRight - _oldOdometrXRight) / 2;
            //TODO разность (_gyro.GetRadians() - _oldRotate) может выйти за пределы 180 градусов!
            double deltaY = -((odometrY - _oldOdometrY) - Configs.Odometry.RadiusOdometrY * (_gyro.GetRadians() - _oldRotate));

            _oldOdometrXLeft = odometrXLeft;
            _oldOdometrXRight = odometrXRight;
            _oldOdometrY = odometrY;

            _oldRotate = _gyro.GetRadians();

            shift = new Vector2(deltaX *
                    cos(-_gyro.GetRadians()) +
                    deltaY * sin(-_gyro.GetRadians()),
                    -deltaX * sin(-_gyro.GetRadians()) +
                            deltaY * cos(-_gyro.GetRadians()));
        }
        else {
            double lfd = _driverTrain.GetLeftForwardEncoder();
            double lbd = _driverTrain.GetLeftBackEncoder();
            double rfd = _driverTrain.GetRightForwardEncoder();
            double rbd = _driverTrain.GetRightBackEncoder();

            double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

            double deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
            double deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

            deltaY = deltaY * Configs.Odometry.YLag;

            _leftForwardDrive = lfd;
            _leftBackDrive = lbd;
            _rightBackDrive = rbd;
            _rightForwardDrive = rfd;

            shift = new Vector2(deltaX *
                    cos(_gyro.GetRadians()) +
                    deltaY * sin(_gyro.GetRadians()),
                    -deltaX * sin(_gyro.GetRadians()) +
                            deltaY * cos(_gyro.GetRadians()));
        }

        Position = Vector2.Plus(shift, Position);

        Speed.X = shift.X / _deltaTime.seconds();
        Speed.Y = shift.Y / _deltaTime.seconds();

        //_CVOdometry.Update();

        //if(!_CVOdometry.IsZero) {
        //    Position.X = _filterX.Update(Position.X, _CVOdometry.Position.X);
        //    Position.Y = _filterY.Update(Position.Y, _CVOdometry.Position.Y);
        //}

        ToolTelemetry.DrawCircle(Position, 10, "#000000");
        ToolTelemetry.DrawCircle(EncoderPosition, 10, "#999999");
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