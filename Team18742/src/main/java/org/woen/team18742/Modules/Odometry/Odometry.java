package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Tools.ToolTelemetry;

public class Odometry {
    public double X = 120, Y = -115;
    private double _encoderOdometryX, _encoderOdometryY;
    private DriverTrain _driverTrain;
    private Gyroscope _gyro;
    private final double _YCoef = 0.9;
    private final double _XCoef = 0.9;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;

    private double _previusTime;

    private Telemetry _telemetry;
    private ElapsedTime _time;
    private CVOdometry _CVOdometry;

    public Odometry(BaseCollector collector) {
        _time = collector.Time;
        _CVOdometry = new CVOdometry();
        _driverTrain = collector.Driver;
        _gyro = collector.Gyro;
        _telemetry = collector.CommandCode.telemetry;
    }

    public VisionProcessor GetProcessor(){
        return _CVOdometry.GetProcessor();
    }

    public void Update() {
        double lfd = _driverTrain.GetLeftForwardIncoder();
        double lbd = _driverTrain.GetLeftBackIncoder();
        double rfd = _driverTrain.GetRightForwardIncoder();
        double rbd = _driverTrain.GetRightBackIncoder();

        ToolTelemetry.AddLine("OdometryX = " + X + " OdometryY = " + Y);

        double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

        double deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
        double deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

        deltaY = deltaY * 0.85602812451;

        X += deltaX * cos(_gyro.GetRadians()) + deltaY * sin(_gyro.GetRadians());
        Y += -deltaX * sin(_gyro.GetRadians()) + deltaY * cos(_gyro.GetRadians());

        _leftForwardDrive = lfd;
        _leftBackDrive = lbd;
        _rightBackDrive = rbd;
        _rightForwardDrive = rfd;

        _CVOdometry.Update();

        double time = _time.seconds(), deltaTime = time - _previusTime;

        if(!_CVOdometry.IsZero) {
            X += (_CVOdometry.X - X) * (deltaTime / (_XCoef + deltaTime));
        }

        Y += (_CVOdometry.Y - Y) * (deltaTime / (_YCoef + deltaTime));
        _previusTime = time;

        ToolTelemetry.DrawCircle(X, Y, 10, "#FFFFFF");
    }

    public void Start() {
        _previusTime = _time.seconds();
        _CVOdometry.Start();
    }
}