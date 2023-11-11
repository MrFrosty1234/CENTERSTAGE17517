package org.woen.team18742;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
        PIDMove(2, 0);
    }

    private void PIDMove(double forward, double side) {
        PID pidForward = new PID(1, 1, 1, 1), pidSide = new PID(1, 1, 1, 1);

        double targetX = _collector.Odometry.X + forward, targetY = _collector.Odometry.Y + side;

        while (_collector.CommandCode.opModeIsActive() && GetDistance(targetX, targetY) > 2) {
            _collector.CommandCode.telemetry.addLine(GetDistance(targetX, targetY) + "");
            SetSpeedWorldCoords(pidForward.Update(targetX -_collector.Odometry.X), pidSide.Update(targetY - _collector.Odometry.Y));

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
            double angleError = degrees - _collector.Gyro.GetDegrees();
            angleError = fixangle(angleError);
            _collector.Driver.DriveDirection(0, 0, pidTurn.Update(angleError));
        } while (_collector.CommandCode.opModeIsActive() && abs(pidTurn.ErrOld) > 2);
        _collector.Driver.Stop();
    }

    public void SetSpeedWorldCoords(double speedForward, double speedSide) {
        double vectorInRotation = Math.atan2(speedSide, speedForward);

        double worldVectorRotation = vectorInRotation - _collector.Gyro.GetRadians();

        double power = Math.sqrt(speedForward * speedForward + speedSide * speedSide);

        _collector.Driver.DriveDirection(-(power * cos(-worldVectorRotation) + power * sin(-worldVectorRotation)),
                -(-power * sin(-worldVectorRotation) + power * cos(-worldVectorRotation)), 0);

    }


    private double GetDistance(double x, double y) {
        double difX = x - _odometry.X, difY = y - _odometry.Y;

        return Math.sqrt(difX * difX + difY * difY);
    }
}