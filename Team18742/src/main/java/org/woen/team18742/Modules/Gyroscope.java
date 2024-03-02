package org.woen.team18742.Modules;

import static java.lang.Math.PI;
import static java.lang.Math.signum;
import static java.lang.Math.toDegrees;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.AutonomCollector;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Modules.Manager.IRobotModule;
import org.woen.team18742.Modules.Manager.Module;
import org.woen.team18742.Modules.Odometry.OdometryHandler;
import org.woen.team18742.Tools.Bios;
import org.woen.team18742.Tools.Configs.Configs;
import org.woen.team18742.Tools.Devices;
import org.woen.team18742.Tools.ExponentialFilter;
import org.woen.team18742.Tools.ToolTelemetry;

import kotlin.ULong;

@Module
public class Gyroscope implements IRobotModule {
    private IMU _imu;
    private OdometryHandler _odometrs;
    private final ElapsedTime _deltaTime = new ElapsedTime();

    private double _oldRadians, _allRadians, _allDegree, _radianSpeed, _degreeSpeed, _radianAccel, _degreeAccel, _oldRadianSpeed, _maxRadianSpeed, _maxRadianAccel, _oldOdometer, _odometer;

    private static double _startRotateRadian;

    private BaseCollector _collector;

    private long _iterations = 0;
    private ExponentialFilter _margeFilter = new ExponentialFilter(Configs.Gyroscope.MergerCoefSeconds);

    @Override
    public void Init(BaseCollector collector) {
        _collector = collector;

        _imu = Devices.IMU;
        _odometrs = collector.GetModule(OdometryHandler.class);

        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
    }

    @Override
    public void Start() {
        if (_collector instanceof AutonomCollector) {
            Reset();
        }
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
        _margeFilter.UpdateCoef(Configs.Gyroscope.MergerCoefSeconds);

        if (Configs.GeneralSettings.IsUseOdometers) {
            double odometerTurn = ChopAngle(ChopAngle((-_odometrs.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft + _odometrs.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2) + Bios.GetStartPosition().Rotation);
            _radianSpeed = (-_odometrs.GetSpeedOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft + _odometrs.GetSpeedOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2;

            if(_iterations % Configs.Gyroscope.Iterations == 0){
                double gyroRotate = ChopAngle(ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _startRotateRadian) + Bios.GetStartPosition().Rotation);

                _allRadians = _margeFilter.UpdateRaw(odometerTurn, odometerTurn - gyroRotate);
            }
            else
                _allRadians += ChopAngle(odometerTurn - _oldOdometer);

            _oldOdometer = odometerTurn;
        } else {
            _allRadians = ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _startRotateRadian);
            _radianSpeed = ChopAngle(_allRadians - _oldRadians) / _deltaTime.seconds();
        }

        _allRadians = ChopAngle(_allRadians);

        _iterations++;

        _radianAccel = ChopAngle(_radianSpeed - _oldRadianSpeed) / _deltaTime.seconds();

        if (Math.abs(_radianSpeed) > _maxRadianSpeed)
            _maxRadianSpeed = Math.abs(_radianSpeed);

        if (Math.abs(_radianAccel) > _maxRadianAccel)
            _maxRadianAccel = Math.abs(_radianAccel);

        _allDegree = toDegrees(_allRadians);
        _degreeSpeed = toDegrees(_radianSpeed);
        _degreeAccel = toDegrees(_radianAccel);

        ToolTelemetry.AddLine("rotation = " + _allDegree);
        ToolTelemetry.AddLine("speed rotation = " + _degreeSpeed);
        ToolTelemetry.AddLine("accel rotation = " + _degreeAccel);
        ToolTelemetry.AddLine("max accel rotation = " + toDegrees(_maxRadianAccel));
        ToolTelemetry.AddLine("max speed rotation = " + toDegrees(_maxRadianSpeed));

        _oldRadians = _allRadians;
        _oldRadianSpeed = _radianSpeed;

        _deltaTime.reset();
    }

    public void Reset() {
        _startRotateRadian = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        _margeFilter.Reset();

        _oldRadians = 0;
    }

    public static double ChopAngle(double angle) {
        while (Math.abs(angle) > PI) {
            angle -= 2 * PI * signum(angle);
        }

        return angle;
    }
}
