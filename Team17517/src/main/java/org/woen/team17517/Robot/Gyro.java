package org.woen.team17517.Robot;

import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.xyzOrientation;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Gyro implements RobotModule{
    UltRobot robot;

    public static double xRotation = 0;
    public static double yRotation = 0;
    public static double headingRotation = 0;
    private double angle = 0;
    IMU gyro;
    public Gyro(UltRobot robot){
        this.robot = robot;

        gyro = robot.linearOpMode.hardwareMap.get(IMU.class,"imu");
        Orientation hubRotation = xyzOrientation(xRotation, yRotation, headingRotation);

        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(hubRotation);
        gyro.initialize(new IMU.Parameters(orientationOnRobot));
        reset();
    }
    public void reset(){
        gyro.resetYaw();
    }

    @Override
    public boolean isAtPosition() {
        return false;
    }

    public void update(){
        angle = gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    public double getAngle(){
        return angle;
    }

}
