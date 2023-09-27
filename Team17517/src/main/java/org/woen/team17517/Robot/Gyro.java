package org.woen.team17517.Robot;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Gyro {
    UltRobot robot;
    IMU gyro;
    public static double xRotation = 0;
    public static double yRotation = 0;
    public static double headingRotation = 0;
    private double angle = 0;
    public Gyro(UltRobot robot){
        this.robot = robot;


        Orientation hubRotation = xyzOrientation(xRotation, yRotation, headingRotation);

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(hubRotation);
        gyro.initialize(new IMU.Parameters(orientationOnRobot));
        reset();
    }
    public void reset(){
        gyro.resetYaw();
    }
    public void update(){
        angle = gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    public double getAngle(){
        return angle;
    }
}
