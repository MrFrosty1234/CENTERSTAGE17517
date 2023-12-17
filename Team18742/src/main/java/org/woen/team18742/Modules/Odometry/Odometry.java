package org.woen.team18742.Modules.Odometry;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.VisionProcessor;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.DriverTrain;
import org.woen.team18742.Modules.Gyroscope;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ToolTelemetry;

@Config
public class Odometry {
    public static boolean IsOdometr = false;
    public static double RadiusOdometr = 5; //random number
    private double _oldRotate = 0, _oldOdometrX, _oldOdometrY;
    private DcMotor _odometrX, _odometrY;
    private final double _diametrOdometr = 4.8, _encoderconstatOdometr = 1440;

    public double X = 101, Y = 0;
    private final DriverTrain _driverTrain;
    private final Gyroscope _gyro;
    public static double YCoef = 0.9;
    public static double XCoef = 0.9;
    private double _leftForwardDrive = 0, _leftBackDrive = 0, _rightForwardDrive = 0, _rightBackDrive = 0;

    private double _previusTime;

    private final ElapsedTime _time;
    private final CVOdometry _CVOdometry;

    public Odometry(BaseCollector collector) {
        _time = collector.Time;
        _CVOdometry = new CVOdometry();
        _driverTrain = collector.Driver;
        _gyro = collector.Gyro;
        _odometrX = Devices.OdometrX;
        _odometrY = Devices.OdometrY;
    }

    public VisionProcessor GetProcessor(){
        return _CVOdometry.GetProcessor();
    }

    public void Update() {
        double time = _time.seconds(), deltaTime = time - _previusTime;

        if(IsOdometr){
            double deltaRotate = _gyro.GetRadians() - _oldRotate;

            double odometrX = _odometrX.getCurrentPosition();
            double odometrY = _odometrY.getCurrentPosition();

            X += ((odometrX - _oldOdometrX) / _encoderconstatOdometr * PI * _diametrOdometr) - RadiusOdometr * deltaRotate;
            Y += ((odometrY - _oldOdometrY) / _encoderconstatOdometr * PI * _diametrOdometr) - RadiusOdometr * deltaRotate;

            _oldOdometrX = odometrX;
            _oldOdometrY = odometrY;

            _oldRotate = _gyro.GetRadians();
        }
        else {
            double lfd = _driverTrain.GetLeftForwardIncoder();
            double lbd = _driverTrain.GetLeftBackIncoder();
            double rfd = _driverTrain.GetRightForwardIncoder();
            double rbd = _driverTrain.GetRightBackIncoder();

            ToolTelemetry.AddLine("OdometryX = " + X + " OdometryY = " + Y);

            double deltaLfd = lfd - _leftForwardDrive, deltaLbd = lbd - _leftBackDrive, deltaRfd = rfd - _rightForwardDrive, deltaRbd = rbd - _rightBackDrive;

            double deltaX = deltaLfd + deltaLbd + deltaRfd + deltaRbd;
            double deltaY = -deltaLfd + deltaLbd + deltaRfd - deltaRbd;

            deltaY = deltaY * 0.8;

            X += deltaX * cos(_gyro.GetRadians()) + deltaY * sin(_gyro.GetRadians());
            Y += -deltaX * sin(_gyro.GetRadians()) + deltaY * cos(_gyro.GetRadians());

            _leftForwardDrive = lfd;
            _leftBackDrive = lbd;
            _rightBackDrive = rbd;
            _rightForwardDrive = rfd;
        }

        _CVOdometry.Update();

        if(!_CVOdometry.IsZero) {
            X += (_CVOdometry.X - X) * (deltaTime / (XCoef + deltaTime));
            Y += (_CVOdometry.Y - Y) * (deltaTime / (YCoef + deltaTime));
        }

        _previusTime = time;

        ToolTelemetry.DrawCircle(X, Y, 10, "#FFFFFF");
    }

    public void Start() {
        _previusTime = _time.seconds();

        _odometrX.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometrX.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        _odometrY.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        _odometrY.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}