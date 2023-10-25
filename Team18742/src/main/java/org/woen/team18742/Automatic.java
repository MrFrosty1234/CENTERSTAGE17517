package org.woen.team18742;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Automatic {
    private Collector _collector;
    private Odometry _odometry;

    public Automatic(Collector collector){
        _collector = collector;
        _odometry = collector.Odometry;
    }

    public void Start(){
        turnGyro(90);
    }

    private void turnGyro(double degrees) {
        PID pid = new PID(1, 1, 1, 180);

        while (_collector.CommandCode.opModeIsActive() && abs(pid.ErrOld) > 2)
            _collector.Driver.DriveDirection(0, 0, pid.Update(_collector.Gyro.GetDegrees(), degrees));

        _collector.Driver.Stop();
    }

    private void SetSpeedWorldCoords(double speedForward, double speedSide){
        double vectorInRotation = Math.atan2(speedSide, speedForward);

        double worldVectorRotation = vectorInRotation - _collector.Gyro.GetRadians();

        double power = speedForward * speedForward + speedSide * speedSide;

        _collector.Driver.DriveDirection(power * cos(-worldVectorRotation) + power * sin(-worldVectorRotation),
                -power * sin(-worldVectorRotation) + power * cos(-worldVectorRotation), 0);
    }

    private void moveWorldCoords(double x, double y){
        double targetDegree = Math.atan2(y, x);

        double vectorDegree = targetDegree - _collector.Gyro.GetRadians();

        double vectorX = cos(-vectorDegree) + sin(-vectorDegree),
        vectorY = -sin(-vectorDegree) + cos(-vectorDegree);

        double targetX = x + _odometry.X, targetY = y + _odometry.Y;

        while (_collector.CommandCode.opModeIsActive() && GetDistance(targetX, targetY) > 2)
        {
            _odometry.Update();

            _collector.Driver.DriveDirection(vectorX, vectorY, 0);
        }

        _collector.Driver.Stop();
    }

    private double GetDistance(double x, double y){
        double difX = x - _odometry.X, difY = y - _odometry.Y;

        return difX * difX + difY * difY;
    }
}
