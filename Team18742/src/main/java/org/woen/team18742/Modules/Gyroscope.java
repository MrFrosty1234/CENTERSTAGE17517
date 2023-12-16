package org.woen.team18742.Modules;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.woen.team18742.Collectors.BaseCollector;
import org.woen.team18742.Tools.Devices;

public class Gyroscope {
    private final IMU _imu;

    public Gyroscope(BaseCollector collector){
        _imu = Devices.IMU;
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
