package org.woen.team17517.Robot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.woen.team17517.Robot.OpenCV.CameraOdometry;

public class UltRobot {
    public DriveTrain driveTrain;
    public Grabber grabber;
    public Lift lift;
    public Lighting lighting;
    public Odometry odometry;
    public LinearOpMode linearOpMode;
    public Gyro gyro;
    public Telemetry telemetry;
    public VoltageSensorPoint voltageSensorPoint;
    public CameraOdometry cameraOdometry;

    public UltRobot(LinearOpMode linearOpMode1) {
        linearOpMode = linearOpMode1;
        driveTrain = new DriveTrain(this);
        grabber = new Grabber(this);
        lift = new Lift(this);
        telemetry = new Telemetry(this);
        voltageSensorPoint = new VoltageSensorPoint(this);
        cameraOdometry = new CameraOdometry(this);

        gyro = new Gyro(this);
        lighting = new Lighting(this);
        odometry = new Odometry(this);
    }

    public void allUpdate() {
        lift.update();
        //lighting.update();
        odometry.update();
        gyro.update();
        grabber.update();
        telemetry.update();
    }
}
