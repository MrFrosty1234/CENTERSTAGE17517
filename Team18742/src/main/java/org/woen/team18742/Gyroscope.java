package org.woen.team18742;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Gyroscope {
    private IMU _imu;

    public Gyroscope(BaseCollector collector){
        _imu = collector.CommandCode.hardwareMap.get(IMU.class, "imu");
        _imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
    }

    public double GetRadians(){
        return _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double GetDegrees(){
        return _imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public void Reset(){
        _imu.resetYaw();
    }
}
