package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Modules.OdometrsHandler;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.MedianFilter;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Odometry {
    public static boolean IsOdometr = false;
    private double _oldRotate = 0, _oldOdometrXLeft, _oldOdometrXRight, _oldOdometrY;

    public double X = 101, Y = 0;
    private final DriverTrain _driverTrain;
    private final Gyroscope _gyro;
    public static double YCoef = 0.9;
    public static double XCoef = 0.9;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;
    private final CVOdometry _CVOdometry;
    private final OdometrsHandler _odometrs;

    private final MedianFilter _filterX = new MedianFilter(XCoef), _filterY = new MedianFilter(YCoef);

    public Odometry(BaseCollector collector) {
        _CVOdometry = new CVOdometry();
        _driverTrain = collector.Driver;
        _gyro = collector.Gyro;
        _odometrs = collector.Odometrs;
    }

    public VisionProcessor GetProcessor(){
        return _CVOdometry.GetProcessor();
    }

    public void Update() {
        _filterX.UpdateCoef(XCoef);
        _filterY.UpdateCoef(YCoef);

        double deltaX, deltaY;

        if(IsOdometr){
            double deltaRotate = _gyro.GetRadians() - _oldRotate;

            double odometrXLeft = _odometrs.GetOdometrXLeft(), odometrY = _odometrs.GetOdometrY(), odometrXRight = _odometrs.GetOdometrXRigth();

            deltaX = (odometrXLeft - _oldOdometrXLeft + odometrXRight - _oldOdometrXRight) / 2;
            deltaY = (odometrY - _oldOdometrY) - OdometrsHandler.RadiusOdometr * deltaRotate;

            _oldOdometrXLeft = odometrXLeft;
            _oldOdometrXRight = odometrXRight;
            _oldOdometrY = odometrY;

            _oldRotate = _gyro.GetRadians();
        }
        else {
            double lfd = _driverTrain.GetLeftForwardIncoder();
            double lbd = _driverTrain.GetLeftBackIncoder();
            double rfd = _driverTrain.GetRightForwardIncoder();
            double rbd = _driverTrain.GetRightBackIncoder();

            double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

            deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
            deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

            deltaY = deltaY * 0.8;

            _leftForwardDrive = lfd;
            _leftBackDrive = lbd;
            _rightBackDrive = rbd;
            _rightForwardDrive = rfd;
        }

        X += deltaX * cos(_gyro.GetRadians()) + deltaY * sin(_gyro.GetRadians());
        Y += -deltaX * sin(_gyro.GetRadians()) + deltaY * cos(_gyro.GetRadians());

        _CVOdometry.Update();

        if(!_CVOdometry.IsZero) {
            //X += (_CVOdometry.X - X) * (deltaTime / (XCoef + deltaTime));
            //Y += (_CVOdometry.Y - Y) * (deltaTime / (YCoef + deltaTime));

            X = _filterX.Update(X, _CVOdometry.X);
            Y = _filterY.Update(Y, _CVOdometry.Y);
        }

        ToolTelemetry.DrawCircle(X, Y, 10, "#FFFFFF");
        ToolTelemetry.AddLine("OdometryX = " + X + " OdometryY = " + Y);
    }

    public void Start(){
        _filterX.Reset();
        _filterY.Reset();
    }
}