package org.woen.team18742.Modules;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
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
import org.woen.team18742.Tools.Vector2;

import kotlin._Assertions;

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
        return _allRadians;
    }

    public double GetDegrees() {
        return _allDegree;
    }

    private double _oldRadians, _oldOdometryRadians, _allRadians, _allDegree, _deltaRadians;

    @Override
    public void Update() {
        _filter.UpdateCoef(Configs.Gyroscope.MergerCoefSeconds);

//        double deltaRadians = ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - _oldRadians);

//        _oldRadians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        //double radians = ChopAngle(_allRadians + deltaRadians);

       // SpeedTurn = deltaRadians / _deltaTime.seconds();

        if (Configs.GeneralSettings.IsUseOdometers) {
            double odometerTurn = ChopAngle(-(_odometrs.GetOdometerXLeft() / Configs.Odometry.RadiusOdometrXLeft - _odometrs.GetOdometerXRight() / Configs.Odometry.RadiusOdometrXRight) / 2 + Bios.GetStartPosition().Rotation);

            //double odometrRadians = ChopAngle(_allRadians + ChopAngle(odometerTurn - _oldOdometryRadians));

            //_oldOdometryRadians = odometerTurn;

            //_allRadians = _filter.UpdateRaw(radians, ChopAngle(odometrRadians - _allRadians));

            _allRadians = (odometerTurn + ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Bios.GetStartPosition().Rotation)) / 2;

          //  ToolTelemetry.DrawRect(_odometrs.Position, new Vector2(15, 15), odometerTurn, "#00FF00");
          //  ToolTelemetry.DrawRect(_odometrs.Position, new Vector2(15, 15), _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS), "#0000FF");
        }
        else
            _allRadians = ChopAngle(_imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + Bios.GetStartPosition().Rotation);

        _allDegree = Math.toDegrees(_allRadians);

        ToolTelemetry.AddLine("rotation = " + _allDegree);

        SpeedTurn = (_oldRadians - _allRadians) / _deltaTime.seconds();

        _deltaTime.reset();
    }

    public double SpeedTurn = 0;

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
