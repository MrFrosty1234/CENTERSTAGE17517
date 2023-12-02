package org.woen.team18742;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.woen.team18742.OpenCV.CVOdometry;

public class Odometry {
    public double X = 0, Y = 0;
    private double _myX, _myY;
    private DriverTrain _driverTrain;
    private Gyroscope _gyro;
    private final double _YCoef = 0.9;
    private final double _XCoef = 0.9;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;

    private long _previusTime;

    private Telemetry _telemetry;

    private CVOdometry _CVOdometry;

    public Odometry(BaseCollector collector) {
        _CVOdometry = new CVOdometry(collector.CommandCode.hardwareMap.get(WebcamName.class, "Webcam 1"));
        _driverTrain = collector.Driver;
        _gyro = collector.Gyro;
        _telemetry = collector.CommandCode.telemetry;
    }

    public void Update() {
        double lfd = _driverTrain.GetLeftForwardIncoder();
        double lbd = _driverTrain.GetLeftBackIncoder();
        double rfd = _driverTrain.GetRightForwardIncoder();
        double rbd = _driverTrain.GetRightBackIncoder();

        double deltaX = lfd - _leftForwardDrive + lbd - _leftBackDrive + rfd - _rightForwardDrive + rbd - _rightBackDrive;
        double deltaY = -lfd + _leftForwardDrive + lbd - _leftBackDrive + rfd - _rightForwardDrive - rbd + _rightBackDrive;

        deltaY = deltaY * 0.85602812451;

        _myX += deltaX * cos(-_gyro.GetRadians()) + deltaY * sin(-_gyro.GetRadians());
        _myY += -deltaX * sin(-_gyro.GetRadians()) + deltaY * cos(-_gyro.GetRadians());

        _leftForwardDrive = lfd;
        _leftBackDrive = lbd;
        _rightBackDrive = rbd;
        _rightForwardDrive = rfd;

        _CVOdometry.Update();

        long time = System.currentTimeMillis(), deltaTime = time - _previusTime;

        X = X + (_CVOdometry.X - _myX) * deltaTime / (_XCoef + deltaTime);
        Y = Y + (_CVOdometry.Y - _myY) * deltaTime / (_YCoef + deltaTime);

        _previusTime = time;
    }

    public void Start() {
        _CVOdometry.Start();
        _previusTime = System.currentTimeMillis();
    }
}