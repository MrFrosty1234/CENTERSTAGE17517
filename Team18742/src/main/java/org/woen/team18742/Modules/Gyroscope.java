package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private OdometryHandler _odometrs;
    private ExponentialFilter _filter = new ExponentialFilter(Configs.Gyroscope.MergerCoefSeconds);

    private ElapsedTime _deltaTime = new ElapsedTime();

    @Override
    public void Init(BaseCollector collector) {
        _imu = Devices.IMU;
        _odometrs = collector.GetModule(OdometryHandler.class);

        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }

    @Override
    public void Start() {
        Reset();
        _deltaTime.reset();
    }

    public double GetRadians() {
        return _radians;
    }

    public double GetDegrees() {
        return _degree;
    }

    private double _degree, _radians, _odometrDegree, _odometrRadians;

    @Override
    public void Update() {
        _filter.UpdateCoef(Configs.Gyroscope.MergerCoefSeconds);

        _degree = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        _radians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (Configs.GeneralSettings.IsUseOdometers) {
            _odometrRadians = -(_odometrs.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft - _odometrs.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2;
            _odometrDegree = Math.toDegrees(_odometrRadians);

            //_radians = _filter.Update(_odometrRadians - _radians, _radians);
            //_degree = Math.toDegrees(_radians);
            //ToolTelemetry.AddLine("Gyro1 = " + _odometrDegree + " Gyro = " + _degree);
        }

        SpeedTurn = (_radians - _oldRadians) / _deltaTime.seconds();

        _oldRadians = _radians;

        _deltaTime.reset();
    }

    private double _oldRadians;

    public double SpeedTurn = 0;

    public void Reset() {
        _imu.resetYaw();
        _filter.Reset();
    }
}
