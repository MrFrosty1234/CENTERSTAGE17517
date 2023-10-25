package org.woen.team18742;

import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Automatic {
    private Collector _collector;

    public Automatic(Collector collector){
        imu = collector.CommandCode.hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        sensorDistance = collector.CommandCode.hardwareMap.get(DistanceSensor.class, "sensor_distance");

        _collector = collector;
    }

    public void Start(){
        encoder(15);
        turnGyro(90);
        distanceSensor(20);
    }

    private double targetDegrees;
    private IMU imu;
    private RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
    private RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
    private RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
    private DistanceSensor sensorDistance;

    void moveForward(double distance) {
        _collector.Driver.ResetIncoder();

        PID pid = new PID(1, 1, 1, 20);

        while (_collector.CommandCode.opModeIsActive() && abs(pid.ErrOld) > 2)
            _collector.Driver.DriveDirection(pid.Update(sensorDistance.getDistance(DistanceUnit.CM), distance), 0, 0);

        _collector.Driver.Stop();
    }

    void turnGyro(double degrees) {
        imu.resetYaw();
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();

        PID pid = new PID(1, 1, 1, 180);

        while (_collector.CommandCode.opModeIsActive() && abs(pid.ErrOld) > 2)
            _collector.Driver.DriveDirection(0, 0, pid.Update(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES), degrees));

        _collector.Driver.Stop();
    }


    void distanceSensor(double distance) {
        imu.resetYaw();
        double errold = 0;
        double kp = 1;
        double kd = 1;
        double kp1 = 1;
        double kd1 = 1;
        double time = 0;
        double timeold = 0;
        double time1 = 0;
        double timeold1 = 0;
        double rastoyanie = 0;
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        double errTurn = targetDegrees - orientation.getYaw(AngleUnit.DEGREES);
        double errDistance = distance - rastoyanie;
        double uForward = (errDistance * kp1) + (errDistance - errold) * kd1 / (time1 - timeold1);

        while (_collector.CommandCode.opModeIsActive() && abs(errTurn) > 2 && abs(errDistance) > 2) {
            orientation = imu.getRobotYawPitchRollAngles();
            errTurn = targetDegrees - orientation.getYaw(AngleUnit.DEGREES);
            if (errTurn > 180) {
                errTurn = errTurn - 360;
            }
            if (errTurn < (-180)) {
                errTurn = errTurn + 360;
            }
            time = System.currentTimeMillis() / 1000.0;
            time1 = System.currentTimeMillis() / 1000.0;
            double uTurn = (errTurn * kp) + (errTurn - errold) * kd / (time - timeold);
            errold = errTurn;
            timeold1 = time1;
            timeold = time;
            rastoyanie = sensorDistance.getDistance(DistanceUnit.CM);

            _collector.Driver.DriveDirection(uForward, uTurn, 0);
        }

        _collector.Driver.Stop();
    }
    void encoder(double distance) {
        _collector.Driver.ResetIncoder();

        PID pid = new PID(1, 1, 1, 20);

        while (_collector.CommandCode.opModeIsActive() && abs(pid.ErrOld) > 2)
            _collector.Driver.DriveDirection(pid.Update(_collector.Driver.GetDistance(), distance), 0, 0);

        _collector.Driver.Stop();
    }
}
