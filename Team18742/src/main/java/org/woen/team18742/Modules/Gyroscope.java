package org.woen.team18742.Modules;

import static java.lang.Math.PI;
import static java.lang.Math.signum;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private OdometryHandler _odometrs;
    private final ExponentialFilter _filter = new ExponentialFilter(Configs.Gyroscope.MergerCoefSeconds);

    private final ElapsedTime _deltaTime = new ElapsedTime();

    private double _oldRadians, _allRadians, _allDegree, _radianSpeed, _degreeSpeed;

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
        return _allRadians;
    }

    public double GetDegrees() {
        return _allDegree;
    }

    public double GetSpeedRadians() {
        return _radianSpeed;
    }

    public double GetSpeedDegrees() {
        return _degreeSpeed;
    }

    @Override
    public void Update() {
        _filter.UpdateCoef(Configs.Gyroscope.MergerCoefSeconds);

        if (Configs.GeneralSettings.IsUseOdometers) {
            double odometerTurn = -(_odometrs.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft - _odometrs.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2;
            _radianSpeed = -(_odometrs.GetSpeedOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft - _odometrs.GetSpeedOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2;

            _allRadians = ChopAngle(odometerTurn + _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) / 2);
        }
        else {
            _allRadians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            _radianSpeed = (_allRadians - _oldRadians) / _deltaTime.seconds();
        }

        _allRadians = ChopAngle(_allRadians + Bios.GetStartPosition().Rotation);

        _allDegree = Math.toDegrees(_allRadians);
        _degreeSpeed = Math.toDegrees(_radianSpeed);

        ToolTelemetry.AddLine("rotation = " + _allDegree);
        ToolTelemetry.AddLine("speed rotation = " + _degreeSpeed);

        _oldRadians = _allRadians;

        _deltaTime.reset();
    }

    public void Reset() {
        _imu.resetYaw();
        _filter.Reset();

        _oldRadians = 0;
    }

    public static double ChopAngle(double angle){
        while (Math.abs(angle) > PI){
            angle -= 2 * PI * signum(angle);
        }

        return angle;
    }
}
