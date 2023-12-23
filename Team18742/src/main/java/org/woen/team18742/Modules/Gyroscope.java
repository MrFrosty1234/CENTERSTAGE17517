package org.woen.team18742.Modules;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponationFilter;

@Config
public class Gyroscope {
    private final IMU _imu;
    private final OdometrsHandler _odometrs;
    public static boolean IsMerger = false;
    public static double MergerCoef = 0.9;
    private ExponationFilter _filter = new ExponationFilter(MergerCoef);

    public Gyroscope(BaseCollector collector) {
        _imu = Devices.IMU;
        _odometrs = collector.Odometrs;
        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }

    public double GetRadians() {
        return _radians;
    }

    public double GetDegrees() {
        return _degree;
    }

    private double _degree, _radians, _odometrDegree, _odometrRadians;

    public void Update() {
        _filter.UpdateCoef(MergerCoef);

        _degree = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        _radians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (IsMerger) {
            _odometrRadians = (_odometrs.GetOdometrXLeft() - _odometrs.GetOdometrXRigth()) / 2.0 / OdometrsHandler.RadiusOdometr;
            _odometrDegree = Math.toDegrees(_odometrRadians);

            _odometrRadians = ChopAngele(_odometrRadians);

            _radians = _filter.Update(ChopAngele(_odometrRadians - _radians), _radians);
            _degree = Math.toDegrees(_radians);
        }
    }

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
