package org.woen.team18742;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Automatic {
    private AutonomCollector _collector;
    private Odometry _odometry;

    public Automatic(AutonomCollector collector) {
        _collector = collector;
        _odometry = collector.Odometry;
    }

    public void Start() {
        PIDMove(20, 10);
        PIDMove(0, -60);
    }

    private void PIDMove(double forward, double side) {
        PID pidForward = new PID(0.8, 1, 1, 1), pidSide = new PID(0.8, 1, 1, 1);

        double targetX = _collector.Odometry.X + forward, targetY = _collector.Odometry.Y + side;

        while (_collector.CommandCode.opModeIsActive() && GetDistance(targetX, targetY) > 2) {
            SetSpeedWorldCoords(pidForward.Update(targetX - _collector.Odometry.X), pidSide.Update(targetY - _collector.Odometry.Y));

            _collector.CommandCode.telemetry.addLine("rot = " + GetDistance(targetX, targetY));

            _collector.CommandCode.telemetry.update();

            _collector.Gyro.Update();
            _odometry.Update();
        }

        _collector.Driver.Stop();
    }

    double fixangle(double degrees) {
        while (degrees > 180)
            degrees = degrees - 360;
        while (degrees < -180)
            degrees = degrees + 360;
        return degrees;
    }

    private void turnGyro(double degrees) {
        PID pidTurn = new PID(1, 1, 1, 1);
        do {
            double angleError = _collector.Gyro.GetDegrees() - degrees;
            angleError = fixangle(angleError);
            _collector.Driver.DriveDirection(0, 0, pidTurn.Update(angleError));

            _collector.Gyro.Update();
            _odometry.Update();
        } while (_collector.CommandCode.opModeIsActive() && abs(pidTurn.Err) > 2);
        _collector.Driver.Stop();
    }

    public void SetSpeedWorldCoords(double speedForward, double speedSide) {
        double x = cos(-_collector.Gyro.GetRadians()) * speedForward - sin(-_collector.Gyro.GetRadians()) * -speedSide,
                y = sin(-_collector.Gyro.GetRadians()) * speedForward + cos (-_collector.Gyro.GetRadians()) * -speedSide;

        _collector.CommandCode.telemetry.addLine("X = " + x + " Y = " + y);

        _collector.Driver.DriveDirection(x, y, 0);
    }

    private double GetDistance(double x, double y) {
        double difX = x - _odometry.X, difY = y - _odometry.Y;

        return Math.sqrt(difX * difX + difY * difY);
    }
}