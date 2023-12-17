package org.woen.team18742.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;

@Config
public class Gyroscope {
    private final IMU _imu;
    private final OdometrsHandler _odometrs;
    public static boolean IsMerger = false;
    private final ElapsedTime _time;
    public static double MergerCoef = 0.9;

    public Gyroscope(BaseCollector collector) {
        _time = collector.Time;
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

    private double _degree, _radians, _odometrDegree, _odometrRadians, _oldTime;

    public void Update() {
        _degree = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        _radians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (IsMerger) {
            _odometrRadians = (_odometrs.GetOdometrXLeft() - _odometrs.GetOdometrXRigth()) / 2 / OdometrsHandler.RadiusOdometr;
            _odometrDegree = Math.toDegrees(_odometrRadians);

            double deltaTime = _time.seconds() - _oldTime;

            _radians += (_odometrRadians - _radians) * (deltaTime / (MergerCoef + deltaTime));
            _degree = Math.toDegrees(_radians);
        }

        _oldTime = _time.seconds();
    }

    public void Reset() {
        _imu.resetYaw();
    }
}
