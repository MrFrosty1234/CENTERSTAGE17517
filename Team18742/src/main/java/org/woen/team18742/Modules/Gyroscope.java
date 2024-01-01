package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Tools.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private OdometryHandler _odometryHandler;
    private ExponentialFilter _filter = new ExponentialFilter(Configs.Gyroscope.MergerCoefSeconds);

    @Override
    public void Init(BaseCollector collector) {
        _imu = Devices.IMU;
        _odometryHandler = collector.GetModule(OdometryHandler.class);
        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }

    @Override
    public void Start() {
        Reset();
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

        if (Configs.GeneralSettings.IsUseOdometrs) {
            _odometrRadians = (_odometryHandler.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft - _odometryHandler.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2;
            _odometrDegree = Math.toDegrees(_odometrRadians);

            _odometrRadians = ChopAngele(_odometrRadians);

            _radians = _filter.Update(ChopAngele(_odometrRadians - _radians), _radians);
            _degree = Math.toDegrees(_radians);
        }
    }

    @Override
    public void Stop() {}

    public void Reset() {
        _imu.resetYaw();
        _filter.Reset();
    }

    public static double ChopAngele(double angle){
        while (abs(angle) > 2 * Math.PI)
            angle -= signum(angle) * Math.PI;

        return angle;
    }
}
