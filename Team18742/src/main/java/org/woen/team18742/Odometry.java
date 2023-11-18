package org.woen.team18742;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Odometry {
    public double X = 0, Y = 0;

    private DriverTrain _driverTrain;
    private Gyroscope _gyro;

    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;

    private Telemetry _telemetry;

    public Odometry(BaseCollector collector){
        _driverTrain = collector.Driver;
        _gyro = collector.Gyro;
        _telemetry = collector.CommandCode.telemetry;
    }

    public void Update(){
        double lfd = _driverTrain.GetLeftForwardIncoder();
        double lbd = _driverTrain.GetLeftBackIncoder();
        double rfd = _driverTrain.GetRightForwardIncoder();
        double rbd = _driverTrain.GetRightBackIncoder();

        double deltaX = lfd - _leftForwardDrive + lbd - _leftBackDrive + rfd - _rightForwardDrive + rbd - _rightBackDrive;
        double deltaY = -lfd + _leftForwardDrive + lbd - _leftBackDrive + rfd - _rightForwardDrive - rbd + _rightBackDrive;

        deltaY = deltaY * 0.85602812451;

        X += deltaX * cos(-_gyro.GetRadians()) + deltaY * sin(-_gyro.GetRadians());
        Y += -deltaX * sin(-_gyro.GetRadians()) + deltaY * cos(-_gyro.GetRadians());

        _telemetry.addLine("X = " + X + " Y = " + Y);

        _leftForwardDrive = lfd;
        _leftBackDrive = lbd;
        _rightBackDrive = rbd;
        _rightForwardDrive = rfd;
    }
}