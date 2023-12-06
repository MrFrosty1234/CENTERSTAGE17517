package org.woen.team18742.Modules;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;

public class Gyroscope {
    private IMU _imu;

    public Gyroscope(BaseCollector collector){
        _imu = collector.CommandCode.hardwareMap.get(IMU.class, "imu");
        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.UP)));
        Reset();
    }

    public double GetRadians(){
        return _radians;
    }

    public double GetDegrees(){
        return _degree;
    }

    private double _degree, _radians;

    public void Update(){
        _degree = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        _radians = _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public void Reset(){
        _imu.resetYaw();
    }
}